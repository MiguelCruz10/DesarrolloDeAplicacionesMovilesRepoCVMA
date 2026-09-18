from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy
from flask_bcrypt import Bcrypt
import os

app = Flask(__name__)

# 1. Configuración de la Base de Datos (SQLite)
# El archivo se guardará en la carpeta del contenedor como 'site.db'
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///site.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)
bcrypt = Bcrypt(app)

# 2. Modelo de Usuario (La tabla en la BD)
class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(20), unique=True, nullable=False)
    password = db.Column(db.String(60), nullable=False) # Aquí guardaremos el hash

    def __repr__(self):
        return f"User('{self.username}')"


# Modelo de Tareas (Para las operaciones CRUD)
class Task(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(100), nullable=False)
    description = db.Column(db.String(200), nullable=True)
    # Relacionamos cada tarea con un usuario para mayor seguridad
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)

    def to_dict(self):
        return {
            'id': self.id,
            'title': self.title,
            'description': self.description,
            'user_id': self.user_id
        }

# 3. Rutas

@app.route('/')
def hello():
    return jsonify({"message": "API Funcionando"})

# Endpoint de REGISTRO
@app.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    username = data.get('username')
    password = data.get('password')

    # Verificar si el usuario ya existe
    if User.query.filter_by(username=username).first():
        return jsonify({"message": "El usuario ya existe"}), 400

    # Encriptar contraseña
    hashed_password = bcrypt.generate_password_hash(password).decode('utf-8')
    
    # Crear y guardar nuevo usuario
    new_user = User(username=username, password=hashed_password)
    db.session.add(new_user)
    db.session.commit()

    return jsonify({"message": "Usuario creado exitosamente"}), 201

# Endpoint de LOGIN
@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data.get('username')
    password = data.get('password')

    user = User.query.filter_by(username=username).first()

    # Verificamos si el usuario existe y si la contraseña coincide con el hash
    if user and bcrypt.check_password_hash(user.password, password):
        return jsonify({
            "status": "success",
            "message": "Login exitoso",
            "user_id": user.id,
            "username": user.username
        }), 200
    else:
        return jsonify({"status": "error", "message": "Credenciales inválidas"}), 401

# ==========================================
# ENDPOINTS CRUD PARA TAREAS
# ==========================================

# 1. Crear una tarea (POST)
@app.route('/tasks', methods=['POST'])
def create_task():
    data = request.get_json()
    user_id = data.get('user_id')
    title = data.get('title')
    description = data.get('description', '')

    if not title or not user_id:
        return jsonify({"message": "Faltan datos obligatorios (title, user_id)"}), 400

    new_task = Task(title=title, description=description, user_id=user_id)
    db.session.add(new_task)
    db.session.commit()

    return jsonify({"message": "Tarea creada", "task": new_task.to_dict()}), 201

# 2. Leer todas las tareas de un usuario (GET)
@app.route('/tasks/<int:user_id>', methods=['GET'])
def get_tasks(user_id):
    tasks = Task.query.filter_by(user_id=user_id).all()
    return jsonify([task.to_dict() for task in tasks]), 200

# 3. Actualizar una tarea (PUT)
@app.route('/tasks/<int:task_id>', methods=['PUT'])
def update_task(task_id):
    task = Task.query.get(task_id)
    if not task:
        return jsonify({"message": "Tarea no encontrada"}), 404

    data = request.get_json()
    task.title = data.get('title', task.title)
    task.description = data.get('description', task.description)
    db.session.commit()

    return jsonify({"message": "Tarea actualizada", "task": task.to_dict()}), 200

# 4. Borrar una tarea (DELETE)
@app.route('/tasks/<int:task_id>', methods=['DELETE'])
def delete_task(task_id):
    task = Task.query.get(task_id)
    if not task:
        return jsonify({"message": "Tarea no encontrada"}), 404

    db.session.delete(task)
    db.session.commit()

    return jsonify({"message": "Tarea eliminada exitosamente"}), 200


if __name__ == '__main__':
    # Esto crea las tablas automáticamente si no existen al iniciar
    with app.app_context():
        db.create_all()

    
    
    app.run(host='0.0.0.0', port=5000, debug=True)
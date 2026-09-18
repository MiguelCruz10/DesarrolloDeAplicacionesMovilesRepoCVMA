package ovh.gabrielhuav.flasklogin.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Estructuras de datos para autenticación
data class LoginRequest(val username: String, val password: String)
data class RegisterRequest(val username: String, val password: String)
data class AuthResponse(val message: String, val status: String?, val user_id: Int?, val username: String?)

// Estructuras de datos para el CRUD de Tareas
data class TaskRequest(val title: String, val description: String, val user_id: Int)
data class TaskUpdateRequest(val title: String, val description: String)
data class TaskResponse(val id: Int, val title: String, val description: String, val user_id: Int)
data class MessageResponse(val message: String)

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("tasks")
    suspend fun createTask(@Body request: TaskRequest): Response<MessageResponse>

    @GET("tasks/{userId}")
    suspend fun getTasks(@Path("userId") userId: Int): Response<List<TaskResponse>>

    @PUT("tasks/{taskId}")
    suspend fun updateTask(@Path("taskId") taskId: Int, @Body request: TaskUpdateRequest): Response<MessageResponse>

    @DELETE("tasks/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: Int): Response<MessageResponse>
}
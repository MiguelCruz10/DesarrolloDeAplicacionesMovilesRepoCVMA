import 'package:flutter_test/flutter_test.dart';
import 'package:app_flutter/main.dart';

void main() {
  testWidgets('Carga inicial de la aplicación', (WidgetTester tester) async {
    await tester.pumpWidget(const CatalogoApp());
    expect(find.text('Catálogo de UI - Flutter'), findsOneWidget);
  });
}

# Login y registro según la presentación de semana 7

El formulario de registro solicita correo y contraseña y crea la cuenta mediante
FirebaseAuth.createUserWithEmailAndPassword. Registra en Analytics el evento
Formulario_registro con Mensaje=Entro_al_registro al abrirse. Tras el éxito muestra
Usuario ha sido creado, cierra la sesión automática de Firebase y vuelve al login.
El registro no depende de una escritura de perfil en Firestore.

El login utiliza signInWithEmailAndPassword y abre MainActivity, que conserva el
panel de gestión y muestra el correo del usuario como título. Cerrar sesión llama
signOut y vuelve al login limpiando la pila de navegación.

Se conserva la validación de correo, contraseña y bloqueo durante las solicitudes.
Las dependencias coinciden con la diapositiva 4: BoM 33.1.0, Auth, Firestore y Analytics.

## Configuración y verificación manual

1. Comprobar que app/google-services.json corresponde al proyecto Firebase.
2. Habilitar Authentication > Sign-in method > Email/Password en la consola.
3. Registrar un correo nuevo y una contraseña de al menos 6 caracteres.
4. Confirmar que vuelve al login y que la cuenta aparece en Authentication.
5. Entrar con esa cuenta, verificar el correo en el panel y cerrar sesión.
6. Probar correo inválido, contraseña vacía, correo duplicado y clave incorrecta.

## Alcance

La tarea de proveedores Google y Microsoft de la diapositiva 20 corresponde a
aplicaciones de pruebas aparte; no forma parte de este ajuste de correo y contraseña.
La inserción de nombre y correo en Firestore de la diapositiva 18 es un ejercicio
separado del registro de Authentication.
Clientes, garantías y servicios técnicos mantienen su repositorio en memoria.
Las reglas de firestore.rules no se publican automáticamente.

## Resultado de compilación
Se intentó assembleDebug --offline con el JDK de Android Studio, también con
IPv4 y --no-daemon. Gradle falló antes de compilar con Unable to establish
loopback connection. La compilación y la prueba real contra Firebase quedan sin verificar.

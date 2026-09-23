# BarberSpot — Registro de Decisiones Arquitectónicas (ADR)

Total de decisiones documentadas: 22  
Decisiones con prueba de concepto ejecutada: 6

---

## ADR-001 — Selección de la arquitectura hexagonal.

**Fecha de propuesta:** 30/08/2026  
**Fecha de aprobación:** 01/09/2026  
**Estado:** Aprobado  
**Participantes:** Arquitecto

**Contexto:**
BarberSpot debe gestionar reglas importantes como consultar la disponibilidad, agendar, cancelar y reprogramar citas. Estas reglas deben mantenerse separadas de elementos externos como la interfaz, la base de datos, la autenticación y el servicio de correo. También se necesita una arquitectura que facilite las pruebas y permita realizar cambios sin afectar todo el sistema.

**Alternativas consideradas:**
- Arquitectura tradicional por capas: divide el sistema en presentación, lógica del negocio y acceso a datos.
- Arquitectura hexagonal: mantiene las reglas del negocio en el centro y conecta las tecnologías externas mediante puertos y adaptadores.
- Arquitectura de microservicios: divide el sistema en varios servicios independientes que se comunican mediante la red.

**Decisión:**
Implementar una arquitectura hexagonal. Las reglas del negocio estarán organizadas en las capas de dominio y aplicación, mientras que PostgreSQL, Auth0, Brevo y otros servicios externos se conectarán mediante interfaces y adaptadores ubicados en infraestructura.

**Consecuencias:**
Positivas: Las reglas de las citas quedan separadas de la tecnología usada en cada servicio externo (autenticación, notificaciones, almacenamiento, seguridad, etc.). Esto facilita probar el sistema y cambiar cualquier herramienta en el futuro sin afectar la lógica de negocio.

Negativas: Se deben crear más interfaces y organizar más carpetas y clases. El equipo debe cuidar que cada parte tenga una responsabilidad clara.

**Justificación:**
Por qué se selecciona: la arquitectura hexagonal mantiene las reglas de disponibilidad, agendamiento, cancelación y reprogramación independientes de las tecnologías externas. Esto reduce el acoplamiento, facilita las pruebas y permite cambiar una herramienta sin modificar la lógica principal.

Por qué no se selecciona la arquitectura tradicional por capas: aunque es más sencilla inicialmente, puede generar dependencias directas entre la lógica del negocio, Spring Boot y PostgreSQL. Esto dificultaría las pruebas y los cambios futuros si las responsabilidades no se separan correctamente.

Por qué no se seleccionan microservicios: requieren administrar varios despliegues, comunicaciones entre servicios, fallas distribuidas y herramientas adicionales de monitoreo. Esa complejidad y costo no se justifican para el alcance inicial de BarberSpot.

---

## ADR-002 — Selección de PostgreSQL como base de datos.

**Fecha de propuesta:** 30/08/2026  
**Fecha de aprobación:** 01/09/2026  
**Estado:** Aprobado  
**Participantes:** Arquitecto Cliente

**Contexto:**
BarberSpot debe almacenar usuarios, barberos, servicios, horarios, bloqueos y citas. Esta información tiene relaciones claras y debe mantenerse consistente. Además, el sistema necesita conservar el historial, generar reportes y responder correctamente cuando varios clientes intenten reservar simultáneamente.

**Alternativas consideradas:**
- PostgreSQL: base de datos relacional con transacciones, claves foráneas, índices y manejo de rangos.
- MySQL: motor relacional muy usado en aplicaciones web, pero con menor soporte nativo que PostgreSQL para restricciones sobre rangos de tiempo y horarios superpuestos.
- MongoDB: base de datos documental con una estructura flexible.
- SQLite: base de datos relacional almacenada en un archivo local.

**Decisión:**
Utilizar PostgreSQL como fuente principal de información de BarberSpot. Los usuarios, servicios, horarios, bloqueos y citas se organizarán en tablas relacionadas mediante claves foráneas, restricciones, índices y transacciones.

**Consecuencias:**
Positivas: Permite guardar la información relacionada de manera ordenada y protege la consistencia de las citas. También soporta varios usuarios usando el sistema al mismo tiempo.

Negativas: El equipo debe administrar cambios en las tablas, copias de seguridad e índices. También debe aprender las funciones propias de PostgreSQL.

**Justificación:**
Por qué se selecciona: PostgreSQL se adapta al modelo relacional de BarberSpot y permite proteger la integridad de los datos mediante claves foráneas y transacciones. También cuenta con herramientas para trabajar con rangos de tiempo, optimizar consultas de disponibilidad y generar reportes.

Por qué no se selecciona MySQL: también es una base de datos relacional válida, pero PostgreSQL proporciona mecanismos más completos para representar intervalos y establecer restricciones sobre horarios superpuestos.

Por qué no se selecciona MongoDB: su flexibilidad resulta útil cuando los datos cambian constantemente de estructura. En BarberSpot, los datos tienen relaciones definidas, por lo que MongoDB obligaría a controlar más relaciones e integridad desde el backend sin aportar una ventaja necesaria.

Por qué no se selecciona SQLite: funciona correctamente para aplicaciones locales y con poca concurrencia, pero BarberSpot será una aplicación web utilizada simultáneamente por clientes, barberos y administradores.

---

## ADR-003 — Selección de Java y Spring Boot para el backend.

**Fecha de propuesta:** 30/08/2026  
**Fecha de aprobación:** 01/09/2026  
**Estado:** Aprobado  
**Participantes:** Arquitecto

**Contexto:**
BarberSpot necesita un backend que exponga una API para gestionar usuarios, barberos, servicios, horarios, citas, reportes y notificaciones. También debe aplicar validaciones, controlar permisos, conectarse con PostgreSQL y ejecutar transacciones para conservar la consistencia de las reservas.

**Alternativas consideradas:**
- Java con Spring Boot: plataforma para construir API REST con herramientas de seguridad, validación, persistencia y transacciones.
- TypeScript con NestJS: framework del ecosistema Node.js que permite organizar el backend mediante módulos.
- Python con Django REST Framework: framework que proporciona un ORM, autenticación y herramientas para construir API.
- C# con ASP.NET Core: plataforma de Microsoft para desarrollar servicios web y API.

**Decisión:**
Desarrollar el backend de BarberSpot con Java y Spring Boot. Las funcionalidades se expondrán mediante una API REST y se organizarán respetando las dependencias de la arquitectura hexagonal.

**Consecuencias:**
Positivas: Spring Boot ofrece herramientas listas para crear la API, validar datos, proteger funciones y trabajar con PostgreSQL.

Negativas: La aplicación puede consumir más memoria que opciones ligeras. El equipo necesita conocer Java y la forma de trabajar de Spring Boot.

**Justificación:**
Por qué se selecciona: Spring Boot proporciona herramientas integradas para validar datos, aplicar seguridad, implementar API REST y controlar transacciones. Su integración con Spring Data JPA facilita el acceso a PostgreSQL y su sistema de inyección de dependencias permite conectar los puertos y adaptadores de la arquitectura hexagonal.

Por qué no se selecciona NestJS: es una alternativa modular y adecuada, pero obligaría a configurar el acceso a datos, la seguridad y las transacciones utilizando herramientas diferentes del ecosistema Node.js. Para BarberSpot no ofrece una ventaja que justifique cambiar el conjunto de tecnologías definido para el backend.

Por qué no se selecciona Django REST Framework: permite construir funcionalidades rápidamente gracias a su ORM incorporado (Django ORM), pero esa integración mezcla la lógica de negocio con el acceso a datos, dificultando mantener el dominio independiente de la persistencia, tal como lo exige la arquitectura hexagonal (ADR-001).

Por qué no se selecciona ASP.NET Core: proporciona seguridad, rendimiento e inyección de dependencias comparables, pero introduciría el ecosistema .NET sin ofrecer una ventaja necesaria para las funciones y el alcance de BarberSpot.

---

## ADR-004 — Selección de React y Vite para desarrollar el frontend.

**Fecha de propuesta:** 30/08/2026  
**Fecha de aprobación:** 01/09/2026  
**Estado:** Aprobado  
**Participantes:** Cliente Arquitecto

**Contexto:**
BarberSpot necesita una interfaz web responsive que funcione en celulares, tabletas y computadores. Los clientes deben consultar barberos, servicios y horarios, mientras que los barberos y administradores necesitan gestionar citas e información del negocio. La interfaz debe actualizar los datos sin recargar toda la página y permitir completar las acciones en pocos pasos.

**Alternativas consideradas:**
- React con Vite: construcción de interfaces dinámicas mediante componentes reutilizables.
- Angular: framework completo con estructura, herramientas y convenciones incorporadas.
- Vue con Vite: framework progresivo y ligero basado en componentes.
- HTML, CSS y JavaScript sin framework: desarrollo mediante las herramientas nativas del navegador.

**Decisión:**
Desarrollar el frontend con React y Vite como una aplicación web responsive. React gestionará los componentes y cambios de estado de la interfaz, mientras que Vite se utilizará para el desarrollo y la generación de los archivos desplegables.

**Consecuencias:**
Positivas: Permite reutilizar elementos como calendarios, formularios y tarjetas. La interfaz puede actualizar horarios sin recargar toda la página.

Negativas: Se deben controlar bien los datos que cambian en pantalla y mantener actualizadas las librerías utilizadas.

**Justificación:**
Por qué se selecciona: React permite crear componentes reutilizables para calendarios, formularios, tarjetas de barberos, botones y tablas de citas. También facilita actualizar los horarios y estados después de consultar la API, sin recargar completamente la página. Su ecosistema ofrece herramientas maduras para navegación, formularios y autenticación. Vite simplifica la configuración y la construcción del frontend.

Por qué no se selecciona Angular: incluye una estructura completa, inyección de dependencias, formularios, enrutamiento y otras herramientas. Aunque esto resulta útil en sistemas empresariales grandes, agrega configuraciones y convenciones que aumentan la complejidad para el alcance actual de BarberSpot.

Por qué no se selecciona Vue: es una alternativa válida, ligera y basada en componentes. Sin embargo, no proporciona una ventaja funcional determinante frente a React para las pantallas y operaciones requeridas, mientras que React cuenta con un ecosistema más amplio de integraciones disponibles para el proyecto.

Por qué no se selecciona JavaScript sin framework: requeriría administrar manualmente el estado, la navegación, la actualización de elementos y la reutilización de componentes. Esto dificultaría el mantenimiento cuando aumenten las vistas de clientes, barberos y administradores.

---

## ADR-005 — Selección de Auth0 para la autenticación y autorización de los usuarios.

**Fecha de propuesta:** 30/08/2026  
**Fecha de aprobación:** 01/09/2026  
**Estado:** Aceptado  
**Participantes:** Arquitecto Cliente

**Contexto:**
BarberSpot tendrá clientes, barberos y administradores con permisos diferentes. El sistema debe proteger las cuentas, impedir el acceso a funciones de otros roles y evitar almacenar contraseñas de manera insegura.

**Alternativas consideradas:**
- Auth0: servicio administrado compatible con OAuth 2.0, OpenID Connect y JWT.
- Autenticación propia con Spring Security: almacenar usuarios y gestionar contraseñas, sesiones o tokens directamente desde BarberSpot.
- Firebase Authentication: servicio de identidad administrado por Google.
- Keycloak: plataforma de identidad de código abierto instalada y administrada por el equipo.

**Decisión:**
Utilizar Auth0 como proveedor de identidad. El frontend realizará el inicio de sesión y recibirá un token JWT, mientras que el backend validará el token y aplicará los permisos de cliente, barbero y administrador en cada endpoint. Las validaciones también comprobarán que cada usuario solamente pueda acceder a sus propios datos.

**Consecuencias:**
Positivas: BarberSpot no guarda directamente las contraseñas y puede controlar el acceso de clientes, barberos y administradores.

Negativas: El sistema depende de un servicio externo y de los límites de su plan. Los roles y permisos deben configurarse cuidadosamente.

**Justificación:**
Por qué se selecciona: Auth0 permite delegar funciones sensibles como el inicio de sesión, la protección de credenciales, la recuperación de cuentas y la emisión de tokens. Utiliza estándares compatibles con React y Spring Security. Esto reduce la cantidad de funciones de seguridad que BarberSpot debe implementar y permite centralizar la identidad de los usuarios.

Por qué no se selecciona autenticación propia: ofrecería mayor control, pero el equipo tendría que implementar y mantener el almacenamiento seguro de contraseñas, la recuperación de cuentas, la expiración de tokens, la protección contra intentos repetidos y otras medidas sensibles. Un error podría comprometer los datos de los usuarios.

Por qué no se selecciona Firebase Authentication: también es un servicio administrado, pero el manejo de los roles y permisos específicos de BarberSpot requeriría configurar reclamaciones personalizadas y adaptar su integración con Spring Security.

Por qué no se selecciona Keycloak: ofrece control completo y evita depender de un servicio externo, pero requiere instalar, actualizar, proteger, monitorear y respaldar un servidor adicional.

**Prueba de concepto:** [`/pocs/auth0`](../pocs/auth0) — Se generó un token real de Auth0 y se validó su firma (SHA256withRSA) contra las llaves públicas JWKS, sin almacenar secretos compartidos.

---

## ADR-006 — Selección de Brevo como proveedor exclusivo para el envío de notificaciones por correo electrónico.

**Fecha de propuesta:** 30/08/2026  
**Fecha de aprobación:** 01/09/2026  
**Estado:** Aceptado  
**Participantes:** Cliente Arquitecto

**Contexto:**
BarberSpot necesita notificar automáticamente a los clientes y a los barberos cuando una cita sea agendada, cancelada o reprogramada. Se requiere un servicio externo confiable que permita enviar correos transaccionales mediante plantillas personalizadas, que no afecte el presupuesto del proyecto y que sea fácil de integrar con la API de Spring Boot sin acoplar la lógica del negocio a una plataforma específica.

**Alternativas consideradas:**
- Brevo (Antes Sendinblue):
- Plataforma en la nube especializada en envío de correos transaccionales mediante API REST. Ofrece un nivel gratuito amplio (hasta 300 correos al día), editor visual de plantillas con variables dinámicas, seguimiento de entrega (webhooks) y fácil integración con Spring Boot mediante un adaptador HTTP.
- SendGrid:
- Proveedor de correo transaccional en la nube ampliamente utilizado en la industria. Ofrece API REST y gestión de plantillas. Sin embargo, sus planes gratuitos actuales son más restringidos en el tiempo (periodos de prueba de 30 días) o requieren ingresar medios de pago desde el inicio.
- Amazon SES (Simple Email Service):
- Servicio de correo masivo y transaccional de Amazon Web Services (AWS). Es una opción extremadamente económica a gran escala, pero requiere configurar infraestructura dentro del ecosistema de AWS (verificación de dominios en Route53, gestión de políticas IAM) y no ofrece un creador visual de plantillas tan amigable.
- Servidor SMTP Convencional (Gmail / Outlook):
- Enviar los correos utilizando directamente la cuenta de correo del negocio mediante el protocolo SMTP tradicional. No requiere servicios externos de correo, pero tiene límites diarios muy estrictos (riesgo de bloqueo de la cuenta por spam), latencia alta en las peticiones y no permite manejar plantillas avanzadas de HTML.

**Decisión:**
Utilizar Brevo como proveedor de notificaciones por correo electrónico en BarberSpot.
El backend en Spring Boot interactuará con Brevo mediante un puerto genérico (NotificationGateway) y un adaptador específico (BrevoNotificationAdapter), garantizando que la lógica del negocio no dependa directamente de la API de Brevo. Las credenciales de acceso se protegerán en Vault o variables de entorno.

**Consecuencias:**
Positivas:Envío de correos confiable y con diseño profesional mediante plantillas.
Cero costo de infraestructura en la etapa inicial del negocio.
Si en el futuro se desea cambiar de proveedor, solo se reemplaza el adaptador sin tocar las reglas del agendamiento.
NegativasEl sistema depende de la disponibilidad de la API de Brevo para que los correos lleguen a tiempo.
Si la barbería supera las 300 citas/notificaciones diarias en el futuro, se deberá contratar un plan de pago de Brevo.

**Justificación:**
Por qué se selecciona: Brevo ofrece un plan gratuito permanente (300 correos diarios, sin tarjeta de crédito), un editor visual de plantillas para personalizar los correos sin tocar el código, y una API REST sencilla de conectar con Spring Boot mediante el adaptador BrevoNotificationAdapter, incluyendo seguimiento del estado de cada envío (enviado, entregado, rebotado).

Por qué no se selecciona SendGrid: en un principio parecía una opción sólida, pero al validarlo se confirmó que ya no ofrece un plan gratuito permanente, solo una prueba de 60 días con 100 correos diarios; al finalizar, exige contratar un plan de pago para seguir enviando correos.

Por qué no se selecciona Amazon SES: aunque es económico para grandes volúmenes, exige configurar varias herramientas adicionales de Amazon solo para empezar a enviar correos, y no tiene un editor visual de plantillas, por lo que cada correo tocaría diseñarlo escribiendo código.

Por qué no se selecciona Servidor SMTP Convencional (Gmail, Outlook o Correo Tradicional):

Intentar enviar las notificaciones automáticas desde una cuenta de correo tradicional fue descartado porque los proveedores como Gmail u Outlook aplican límites diarios de envío muy estrictos que marcarían los mensajes como *spam* o bloquearían la cuenta rápidamente. Además, este método es mucho más lento y haría que la página web se quede cargando varios segundos mientras se envía el correo. Finalmente, un correo común no permite utilizar plantillas de diseño dinámicas para que los mensajes se vean profesionales, ni nos proporciona reportes para saber si el correo realmente le llegó al cliente o rebotó.

**Prueba de concepto:** [`/pocs/brevo`](../pocs/brevo) — Se envió un correo transaccional real con mecanismo de reintentos (3 intentos controlados sin bloquear el flujo).

---

## ADR-007 — Publicación de BarberSpot en Cloudflare Pages, Railway y Supabase.

**Fecha de propuesta:** 30/08/2026  
**Fecha de aprobación:** 04/09/2026  
**Estado:** Aprobado  
**Participantes:** Cliente Arquitecto

**Contexto:**
BarberSpot debe estar disponible para clientes, barberos y administradores desde cualquier lugar y horario. El equipo cuenta con un presupuesto limitado y no dispone de personal dedicado a administrar servidores. La infraestructura también debe permitir actualizar el frontend, el backend y la base de datos de manera controlada.

**Alternativas consideradas:**
- Cloudflare Pages, Railway y Supabase: publicar cada parte de BarberSpot en una plataforma administrada.
- Servidor VPS: instalar el frontend, el backend y PostgreSQL en un servidor virtual.
- AWS o Microsoft Azure: utilizar diferentes servicios empresariales de computación, almacenamiento y bases de datos.Servidor local: ejecutar BarberSpot desde un computador ubicado en la barbería.

**Decisión:**
Publicar el frontend desarrollado con React y Vite en Cloudflare Pages, ejecutar el backend de Spring Boot en Railway y alojar la base de datos PostgreSQL en Supabase. Las tres plataformas se configurarán para comunicarse de manera segura.

Recorrido de una solicitud: el frontend se sirve directo desde Cloudflare Pages (no pasa por Railway). Las peticiones a la API siguen esta ruta: Cliente -> Cloudflare (WAF) -> API Gateway (Kong) -> Backend (Railway) -> Base de datos (Supabase). El WAF y el Gateway no son parte del hosting (este ADR) — son capas de seguridad y enrutamiento ubicadas delante del backend ya publicado.

**Consecuencias:**
Positivas: La aplicación puede estar disponible sin depender de un computador de la barbería. Cada parte puede publicarse y actualizarse por separado.

Negativas: La configuración queda repartida entre varias plataformas. Los límites y precios pueden cambiar cuando crezca el uso.

**Justificación:**
Por qué se selecciona: Cloudflare Pages permite publicar el frontend sin administrar un servidor web. Railway facilita la ejecución del backend de Spring Boot y Supabase ofrece PostgreSQL como un servicio administrado. Esta combinación reduce el trabajo de instalación y mantenimiento y permite comenzar con costos bajos, aunque debe aclararse que Railway ya no ofrece un nivel completamente gratuito: su plan básico tiene un costo aproximado de $5 USD al mes, que se cubre dentro del presupuesto total de $5.000.000 asignado al proyecto.

Por qué no se selecciona un VPS: el equipo tendría que instalar y actualizar el sistema operativo, configurar la seguridad, administrar PostgreSQL, renovar certificados y realizar copias de seguridad. También concentraría todos los componentes en un único servidor.

Por qué no se seleccionan AWS o Azure: ofrecen una infraestructura más amplia y escalable, pero requieren mayor configuración, administración y control de costos. Esa complejidad no es necesaria para el alcance inicial de BarberSpot.

Por qué no se selecciona un servidor local: el funcionamiento dependería de la energía, la conexión a Internet y el computador de la barbería. Una falla local impediría que los clientes accedieran al sistema.

---

## ADR-008 — Automatización de pruebas y despliegues mediante GitHub Actions.

**Fecha de propuesta:** 31/08/2026  
**Fecha de aprobación:** 04/09/2026  
**Estado:** Aprobado  
**Participantes:** Arquitecto

**Contexto:**
BarberSpot será modificado frecuentemente durante el desarrollo y después de su publicación. Cada cambio debe compilarse, probarse y verificarse antes de llegar a producción. El equipo necesita un procedimiento uniforme que reduzca errores y evite que los despliegues dependan del computador de una sola persona.

**Alternativas consideradas:**
- Proceso manual: ejecutar las pruebas y desplegar la aplicación desde el computador de un integrante.
- GitHub Actions: automatizar compilación, pruebas y despliegues desde el repositorio de GitHub.
- Jenkins: instalar y administrar un servidor propio para ejecutar los procesos de automatización.
- GitLab CI/CD: utilizar los pipelines proporcionados por GitLab.

**Decisión:**
Implementar un flujo de CI/CD con GitHub Actions. Cada cambio enviado al repositorio ejecutará la compilación y las pruebas del backend y del frontend. El despliegue se realizará únicamente si las validaciones finalizan correctamente.

**Consecuencias:**
Positivas: Todos los cambios pasan por las mismas verificaciones antes de publicarse. Esto reduce errores y hace que el proceso sea repetible.

Negativas: Los flujos y permisos deben mantenerse actualizados. Algunas ejecuciones pueden consumir tiempo incluido en el plan.

**Justificación:**
Por qué se selecciona: GitHub Actions se integra directamente con el repositorio y permite definir un único procedimiento para todo el equipo. Puede compilar Spring Boot, ejecutar las pruebas, construir el frontend y publicar las nuevas versiones utilizando credenciales protegidas.

Por qué no se selecciona el proceso manual: depende de que cada integrante recuerde y ejecute correctamente todos los pasos. También puede producir diferencias entre computadores y permitir que una versión con errores llegue a producción.

Por qué no se selecciona Jenkins: ofrece muchas posibilidades de personalización, pero requiere instalar, proteger, actualizar y monitorear un servidor adicional.

Por qué no se selecciona GitLab CI/CD: también permite automatizar el proceso, pero requeriría trasladar o duplicar el repositorio en otra plataforma sin ofrecer una ventaja necesaria para BarberSpot.

---

## ADR-009 — Actualización automática: automática de citas mediante Server-Sent Events (SSE).

**Fecha de propuesta:** 31/08/2026  
**Fecha de aprobación:** 04/09/2026  
**Estado:** Aceptado  
**Participantes:** Cliente Arquitecto

**Contexto:**
Cuando un cliente agenda, cancela o reprograma una cita, tanto el barbero como otros clientes consultando el mismo horario necesitan conocer el cambio oportunamente — por ejemplo, si un horario deja de estar disponible mientras alguien más lo estaba seleccionando. Las operaciones seguirán realizándose mediante la API REST, pero el backend necesita informar los cambios a la interfaz sin exigir una recarga manual.

**Alternativas consideradas:**
- Actualización manual: el barbero debe recargar la página para consultar cambios.
- Polling: el frontend consulta periódicamente la API para buscar novedades.
- WebSocket: mantener una comunicación bidireccional permanente entre frontend y backend.
- Server-Sent Events (SSE): mantener un canal unidireccional desde el servidor para enviar eventos al navegador.

**Decisión:**
Utilizar SSE para notificar a los frontends de clientes y barberos los cambios de las citas y conservar la API REST para crear, cancelar, reprogramar o modificar información.

**Consecuencias:**
Positivas: El barbero puede ver cambios en su agenda sin actualizar manualmente la página. Se utiliza una comunicación sencilla desde el servidor hacia el navegador.

Negativas: Se deben manejar las reconexiones cuando se pierde internet y comprobar que el servicio de despliegue mantenga la conexión.

**Justificación:**
Por qué se selecciona: SSE es apropiado porque la información debe viajar principalmente desde el servidor hacia el navegador. Permite actualizar la agenda sin recargar la página y utiliza una conexión HTTP más sencilla que una comunicación bidireccional completa.

Por qué no se selecciona la actualización manual: el barbero podría olvidar recargar la página y continuar viendo una agenda desactualizada.

Por qué no se selecciona polling: realiza solicitudes repetidas incluso cuando no existen cambios. Además, la actualización solamente ocurre cuando se cumple el intervalo configurado.

Por qué no se selecciona WebSocket: permite enviar información en ambas direcciones, pero BarberSpot ya utilizará la API REST para las acciones del usuario. Agregaría complejidad sin una necesidad concreta.

**Prueba de concepto:** [`/pocs/sse`](../pocs/sse) — Se comprobó que el servidor puede enviar eventos al navegador de forma continua sin que el cliente los solicite (a diferencia de polling).

---

## ADR-010 — Registro y seguimiento de errores mediante logs estructurados y traceId.

**Fecha de propuesta:** 31/08/2026  
**Fecha de aprobación:** 04/09/2026  
**Estado:** Aceptado  
**Participantes:** Arquitecto

**Contexto:**
Cuando ocurra una falla durante el agendamiento u otra operación, el equipo debe identificar qué solicitud presentó el problema, en qué módulo ocurrió y cuál fue la causa. Al usuario se le debe mostrar un mensaje comprensible sin revelar información técnica ni datos sensibles.

**Alternativas consideradas:**
- Mensajes básicos en consola: registrar textos sin una estructura ni un identificador común.
- Logs estructurados con MDC y traceId: asignar un código único a cada solicitud y conservarlo en todos sus registros.
- OpenTelemetry con una plataforma completa: recopilar logs, métricas y trazas mediante herramientas especializadas.
- Servicio externo de errores: enviar las excepciones a una plataforma externa de seguimiento.

**Decisión:**
Implementar logs estructurados con MDC y asignar un traceId a cada solicitud. También se utilizará un manejador global de errores y un endpoint de salud. Cuando ocurra una falla inesperada, el usuario recibirá el código de seguimiento para que el equipo pueda localizarla en los registros.

**Consecuencias:**
Positivas: El equipo puede encontrar más fácilmente el error relacionado con una solicitud y darle al usuario un código de seguimiento.

Negativas: Se generan más registros y se debe evitar guardar contraseñas, tokens u otros datos sensibles.

**Justificación:**
Por qué se selecciona: los logs estructurados y el traceId permiten relacionar el error mostrado al usuario con los registros del backend. Esto ayuda a localizar la petición, el módulo y la causa sin exponer detalles técnicos. El endpoint de salud permitirá comprobar si la aplicación está funcionando.

Por qué no se seleccionan mensajes básicos en consola: los registros quedarían desorganizados y sería difícil seguir una misma solicitud cuando pasa por diferentes componentes.

Por qué no se selecciona inicialmente una plataforma completa con OpenTelemetry: proporciona información más detallada, pero requiere herramientas adicionales de almacenamiento, visualización y mantenimiento que aumentan la complejidad y el costo.

Por qué no se selecciona únicamente un servicio externo: generaría dependencia de otro proveedor y podría enviar información sensible fuera del sistema si no se configura correctamente.

**Prueba de concepto:** [`/pocs/logs-traceid`](../pocs/logs-traceid) — Se comprobó la correlación de logs entre módulos mediante un traceId único, y que el usuario final solo recibe el código de seguimiento sin detalles técnicos.

---

## ADR-011 — Documentación de la API con OpenAPI/Swagger y versionamiento por URL.

**Fecha de propuesta:** 31/08/2026  
**Fecha de aprobación:** 04/09/2026  
**Estado:** Aprobado  
**Participantes:** Arquitecto

**Contexto:**
El frontend y el backend serán desarrollados como componentes separados. Ambos necesitan conocer los endpoints, parámetros, respuestas, errores y requisitos de autenticación. Además, los cambios futuros no deben romper inmediatamente a los clientes existentes.

**Alternativas consideradas:**
- Documentación informal: explicar los endpoints mediante mensajes o notas.
- Documento manual: registrar las operaciones en un archivo o tabla.
- Colecciones de Postman o Bruno: guardar solicitudes de ejemplo para probar la API.
- OpenAPI y Swagger: definir formalmente los endpoints, parámetros, respuestas, errores y requisitos de seguridad.

**Decisión:**
Documentar la API REST de BarberSpot mediante OpenAPI y utilizar Swagger UI para consultar y probar los endpoints en los ambientes de desarrollo y pruebas. El acceso a Swagger será restringido o deshabilitado en producción.

**Consecuencias:**
Positivas: El frontend conoce claramente los endpoints, datos, errores y permisos de la API. También facilita realizar pruebas.

Negativas: La documentación debe actualizarse cuando cambie la API y su acceso debe limitarse en producción.

**Justificación:**
Por qué se selecciona: OpenAPI crea un contrato estructurado para la comunicación entre frontend y backend. Swagger UI permite consultar y probar los endpoints, visualizar los formatos de los datos y conocer los códigos de respuesta y requisitos de autenticación.

Por qué no se selecciona documentación informal: la información puede quedar dispersa, incompleta o depender de lo que recuerde cada integrante.

Por qué no se selecciona únicamente un documento manual: tendría que actualizarse separadamente del código y podría quedar desactualizado cuando cambie un endpoint.

Por qué no se seleccionan únicamente colecciones de Postman o Bruno: son útiles para ejecutar pruebas y conservar ejemplos, pero no describen de manera completa y estandarizada todos los esquemas, errores y reglas de seguridad de la API.

---

## ADR-012 — Protección de credenciales mediante HashiCorp Vault y variables de entorno.

**Fecha de propuesta:** 31/08/2026  
**Fecha de aprobación:** 04/09/2026  
**Estado:** Aceptado  
**Participantes:** Arquitecto

**Contexto:**
BarberSpot utilizará información sensible como la contraseña de PostgreSQL, la clave privada de Brevo, los tokens de despliegue y otras credenciales. Estos valores no deben aparecer en el código ni quedar publicados en el repositorio. También deben poder cambiarse según el ambiente sin modificar la aplicación.

**Alternativas consideradas:**
- Credenciales dentro del código: escribir las claves directamente en los archivos de la aplicación.
- Archivos de configuración versionados: guardar las claves en archivos .env o de propiedades incluidos en el repositorio.
- Variables de entorno y secretos de cada plataforma: configurar las credenciales separadamente en Railway, Cloudflare y GitHub.
- HashiCorp Vault con variables de entorno: centralizar los secretos en Vault y utilizar variables de entorno para configurar el acceso desde la aplicación.
- Azure Key Vault o AWS Secrets Manager: almacenar las credenciales en un servicio asociado a una nube específica.

**Decisión:**
Utilizar HashiCorp Vault como baúl central para almacenar las credenciales sensibles. El backend accederá a Vault durante su ejecución y las variables de entorno se utilizarán para indicar la dirección y configuración necesaria para conectarse. El token inicial de acceso a Vault se almacenará en el mecanismo de secretos de Railway. Los archivos locales .env estarán excluidos del repositorio y solamente se entregará un .env.example sin valores reales.

**Consecuencias:**
Consecuencias positivas: Mantiene las credenciales fuera del código y permite configuraciones distintas por ambiente.

Consecuencias negativas: Los secretos quedan distribuidos y deben rotarse y protegerse correctamente.

**Justificación:**
Por qué se selecciona: HashiCorp Vault centraliza las credenciales, permite controlar quién puede consultarlas, registrar los accesos y cambiar los secretos sin modificar el código. Las variables de entorno permiten configurar la conexión según el ambiente sin publicar información sensible.

Por qué no se incluyen las credenciales en el código: cualquier persona con acceso al repositorio podría obtenerlas y cambiar una clave exigiría modificar y desplegar nuevamente la aplicación.

Por qué no se utilizan archivos versionados: aunque facilitan la configuración, podrían exponer accidentalmente las credenciales en GitHub y no proporcionan control de acceso ni auditoría.

Por qué no se utilizan únicamente los secretos de las plataformas: las credenciales quedarían distribuidas entre varios proveedores, dificultando su administración, rotación y seguimiento desde un lugar central.

Por qué no se seleccionan Azure Key Vault o AWS Secrets Manager: generarían dependencia de nubes que no fueron elegidas para publicar BarberSpot.

**Prueba de concepto:** [`/pocs/vault`](../pocs/vault) — Se comprobó el guardado y la lectura de una credencial cifrada mediante la API REST de Vault (modo desarrollo local).

---

## ADR-013 — Protección de la información mediante copias de seguridad automáticas de PostgreSQL.

**Fecha de propuesta:** 31/08/2026  
**Fecha de aprobación:** 05/09/2026  
**Estado:** Aprobado  
**Participantes:** Cliente Arquitecto

**Contexto:**
BarberSpot almacenará usuarios, servicios, horarios, citas e historial en PostgreSQL. Una eliminación accidental, un error durante una actualización o una falla del proveedor podría ocasionar la pérdida de información. El sistema necesita un procedimiento que permita recuperar los datos sin depender de que una persona recuerde hacer copias manualmente.

**Alternativas consideradas:**
- Sin copias adicionales: depender únicamente de la base de datos activa en Supabase.
- Copias manuales: exportar la información cuando un integrante lo considere necesario.
- Copias automáticas programadas: generar respaldos periódicos y conservar varias versiones.
- Replicación en tiempo real: mantener una segunda base de datos sincronizada permanentemente.

**Decisión:**
Configurar copias de seguridad automáticas diarias de PostgreSQL y conservar varias versiones. También se realizará periódicamente una prueba de restauración para comprobar que los respaldos puedan recuperarse. Se utilizarán las copias ofrecidas por Supabase si el plan seleccionado las incluye; de lo contrario, se programarán exportaciones automáticas hacia un almacenamiento separado.

**Consecuencias:**
Positivas: Permite recuperar usuarios, horarios y citas después de una falla o eliminación accidental.

Negativas: Las copias ocupan espacio y deben revisarse. Con una copia diaria podrían perderse hasta 24 horas de cambios.

**Justificación:**
Por qué se selecciona: las copias automáticas reducen la dependencia de tareas humanas y permiten recuperar una versión anterior después de una eliminación, corrupción o falla. Probar la restauración confirma que el respaldo realmente puede utilizarse.

Por qué no se selecciona trabajar sin copias adicionales: si la base de datos activa se elimina o corrompe, BarberSpot podría perder definitivamente las citas y el historial.

Por qué no se seleccionan únicamente copias manuales: pueden olvidarse, realizarse con poca frecuencia o quedar almacenadas de manera insegura.

Por qué no se selecciona replicación en tiempo real: requiere más infraestructura y costo. Además, una eliminación accidental también podría replicarse inmediatamente, por lo que no reemplaza los respaldos históricos.

---

## ADR-014 — Selección de una API REST para la comunicación entre frontend y backend.

**Fecha de propuesta:** 31/08/2026  
**Fecha de aprobación:** 05/09/2026  
**Estado:** Aprobado  
**Participantes:** Cliente Arquitecto

**Contexto:**
el frontend de BarberSpot necesita comunicarse con el backend para consultar barberos, servicios y horarios, además de agendar, cancelar y reprogramar citas. La comunicación debe ser compatible con navegadores, utilizar formatos conocidos y permitir aplicar autenticación y permisos.

**Alternativas consideradas:**
- API REST: utiliza HTTP, direcciones por recursos, métodos como GET, POST, PUT y DELETE, y normalmente intercambia datos en JSON.
- GraphQL: permite que el frontend solicite exactamente los campos que necesita mediante consultas.
- SOAP: utiliza contratos formales y mensajes principalmente en XML.
- gRPC: utiliza Protocol Buffers y comunicación binaria de alto rendimiento.

**Decisión:**
Construir una API REST con Spring Boot, utilizando HTTPS y JSON. Los endpoints representarán recursos como usuarios, barberos, servicios, horarios y citas, y emplearán los métodos y códigos de respuesta estándar de HTTP.

**Consecuencias:**
Positivas: permite una comunicación sencilla entre React y Spring Boot, funciona directamente en los navegadores y facilita probar los endpoints con Swagger, Postman o Bruno.

Negativas: se deben organizar y documentar correctamente los endpoints. Algunos cambios futuros pueden exigir crear una nueva versión de la API y REST no actualiza la agenda en tiempo real por sí solo, por lo que se complementará con SSE.

**Justificación:**
Por qué se selecciona: REST se adapta a las operaciones de BarberSpot, que consisten principalmente en consultar, registrar y actualizar recursos. Es compatible directamente con React, navegadores y Spring Boot, y facilita las pruebas mediante Swagger, Postman o Bruno.

Por qué no se selecciona GraphQL: ofrece consultas flexibles, pero requiere definir esquemas, resolutores y controles adicionales para limitar consultas complejas. BarberSpot no necesita que el frontend combine grandes cantidades de información variable.

Por qué no se selecciona SOAP: sus mensajes XML y contratos agregan complejidad y mayor cantidad de información transmitida. Sus características son más apropiadas para integraciones empresariales que requieren estándares específicos.

Por qué no se selecciona gRPC: ofrece alto rendimiento entre servicios, pero su uso desde navegadores requiere configuraciones o intermediarios adicionales. BarberSpot necesita una comunicación web sencilla y no posee múltiples servicios internos que justifiquen esta tecnología.

---

## ADR-015 — Distribución de contenido estático mediante Cloudflare CDN.

**Fecha de propuesta:** 31/08/2026  
**Fecha de aprobación:** 05/09/2026  
**Estado:** Aprobado  
**Participantes:** Cliente Arquitecto

**Contexto:**
BarberSpot debe cargar rápidamente el frontend, estilos, scripts e imágenes en celulares y computadores. Servir todos los archivos desde un único servidor puede aumentar la latencia y el consumo del backend.

**Alternativas consideradas:**
- Sin CDN: servir todos los archivos directamente desde el servidor de origen.
- Cloudflare CDN: distribuir y almacenar en caché el contenido estático desde ubicaciones cercanas a los usuarios.
- Amazon CloudFront: utilizar la red de distribución de AWS.
- Fastly: utilizar una CDN administrada con funciones avanzadas de entrega y caché.

**Decisión:**
Utilizar Cloudflare CDN junto con Cloudflare Pages para distribuir el frontend y los archivos estáticos de BarberSpot.

**Consecuencias:**
Positivas: El sitio y sus imágenes pueden cargar más rápido y el servidor principal recibe menos trabajo.

Negativas: Se deben controlar los archivos guardados temporalmente para que los usuarios reciban las versiones actualizadas.

**Justificación:**
Por qué se selecciona: Cloudflare CDN ya se integra con Cloudflare Pages, ofrece HTTPS y distribuye el contenido sin añadir otra plataforma. Esta capacidad se activa automáticamente al usar Cloudflare Pages (ADR-007), pero se documenta como una decisión independiente para dejar explícito su rol dentro de la arquitectura.

Por qué no se selecciona esta opción (trabajar sin CDN): todas las solicitudes llegarían al mismo origen, aumentando el tiempo de carga y el trabajo del servidor.

Por qué no se selecciona esta opción (Amazon CloudFront): es una alternativa adecuada, pero agregaría una cuenta y configuración de AWS que el proyecto no necesita.

Por qué no se selecciona esta opción (Fastly): también distribuye contenido, pero añade otro proveedor sin una ventaja necesaria para el volumen inicial.

---

## ADR-016 — Filtrado de tráfico malicioso mediante Cloudflare WAF

**Fecha de propuesta:** 31/08/2026  
**Fecha de aprobación:** 05/09/2026  
**Estado:** Aprobado  
**Participantes:** Cliente Arquitecto

**Contexto:**
La API y el sitio estarán expuestos a internet y pueden recibir solicitudes maliciosas, automatizadas o excesivas. BarberSpot necesita filtrar tráfico antes de que llegue al backend.

**Alternativas consideradas:**
- Sin WAF: depender únicamente de las validaciones del backend.
- Cloudflare WAF: aplicar reglas administradas, restricciones y controles de tráfico en el perímetro.
- AWS WAF: proteger los recursos mediante el servicio de AWS.
- ModSecurity: instalar y administrar reglas de firewall en infraestructura propia.

**Decisión:**
Utilizar Cloudflare WAF con reglas administradas y limitación de solicitudes para filtrar el tráfico entrante antes de que llegue al API Gateway, que es el único punto de entrada hacia el frontend y el backend de BarberSpot.

**Consecuencias:**
Positivas: Ayuda a bloquear solicitudes peligrosas o repetitivas antes de que lleguen al backend.

Negativas: Una regla incorrecta puede bloquear a un usuario legítimo y algunas funciones pueden tener costo.

**Justificación:**
Por qué se selecciona: Cloudflare WAF se integra con la plataforma usada para publicar el frontend y permite aplicar reglas de protección desde un mismo lugar.

Por qué no se selecciona esta opción (trabajar sin WAF): dejaría toda la protección en el backend y permitiría que más solicitudes dañinas consumieran sus recursos.

Por qué no se selecciona esta opción (AWS WAF): la infraestructura principal no está alojada en AWS y se añadiría otro proveedor.

Por qué no se selecciona esta opción (ModSecurity): obligaría al equipo a instalar, actualizar y mantener manualmente las reglas de protección.

---

## ADR-017 — Almacenamiento de imágenes mediante Supabase Storage como Blob Storage.

**Fecha de propuesta:** 31/08/2026  
**Fecha de aprobación:** 05/09/2026  
**Estado:** Aprobado  
**Participantes:** Cliente Arquitecto

**Contexto:**
BarberSpot puede almacenar fotografías de barberos, imágenes de servicios y otros archivos. Guardar estos objetos directamente en PostgreSQL aumentaría el tamaño de la base de datos y dificultaría su entrega.

**Alternativas consideradas:**
- Guardar archivos en PostgreSQL: almacenar los bytes dentro de la base de datos relacional.
- Supabase Storage: utilizar buckets y políticas de acceso integradas con el entorno de Supabase.
- Amazon S3: almacenar objetos en el servicio de AWS.
- Cloudflare R2: utilizar almacenamiento de objetos integrado con Cloudflare.

**Decisión:**
Utilizar Supabase Storage como Blob Storage, con buckets separados y políticas que controlen quién puede cargar, modificar o consultar los archivos.

**Consecuencias:**
Positivas: Las fotografías se guardan separadas de las citas y pueden crecer sin aumentar innecesariamente la base de datos.

Negativas: Se deben controlar los permisos y eliminar los archivos que ya no estén relacionados con un barbero o servicio.

**Justificación:**
Por qué se selecciona: Supabase Storage se administra junto con la base de datos elegida y permite crear espacios separados con reglas de acceso.

Por qué no se selecciona esta opción (guardar las imágenes dentro de PostgreSQL): aumentaría el tamaño de la base de datos y haría más pesadas las copias de seguridad.

Por qué no se selecciona esta opción (Amazon S3): es un servicio sólido, pero agregaría una plataforma diferente para una necesidad que Supabase ya puede cubrir.

Por qué no se selecciona esta opción (Cloudflare R2): también podría funcionar, pero repartiría la información entre más servicios y aumentaría la administración.

---

## ADR-018 — Centralización del acceso a la API mediante Kong API Gateway.

**Fecha de propuesta:** 31/08/2026  
**Fecha de aprobación:** 05/09/2026  
**Estado:** Aprobado  
**Participantes:** Arquitecto

**Contexto:**
El frontend necesita un punto único para acceder al backend. También se deben centralizar tareas de soporte como enrutamiento, CORS, límites de solicitudes y políticas de acceso sin repetirlas en todos los endpoints.

**Alternativas consideradas:**
- Acceso directo al backend: publicar Spring Boot sin un intermediario.
- Kong API Gateway: centralizar rutas y políticas mediante plugins y configuración declarativa.
- Spring Cloud Gateway: construir el gateway dentro del ecosistema Spring.
- Cloudflare como proxy: utilizar únicamente las capacidades de proxy y seguridad del proveedor perimetral.

**Decisión:**
Utilizar Kong como API Gateway entre el frontend y el backend, configurando rutas, CORS, límites de solicitudes y validaciones comunes.

**Consecuencias:**
Positivas: La API tiene un único punto de entrada donde se controlan rutas, permisos y cantidad de solicitudes.

Negativas: Se agrega otro componente que debe configurarse, publicarse y mantenerse disponible.

**Justificación:**
Por qué se selecciona: Kong permite organizar las rutas y aplicar controles comunes sin repetirlos en cada parte del backend.

Por qué no se selecciona esta opción (el acceso directo al backend): expondría la aplicación y repartiría los controles entre diferentes endpoints.

Por qué no se selecciona esta opción (Spring Cloud Gateway): obligaría a desarrollar y mantener otro componente en Spring cuando Kong ya ofrece esas funciones preparadas.

Por qué no se selecciona esta opción (usar solamente Cloudflare como proxy): protege el acceso externo, pero no organiza con el mismo detalle las reglas propias de la API.

---

## ADR-019 — Redis como caché de catálogos

**Fecha de propuesta:** 31/08/2026  
**Fecha de aprobación:** 05/09/2026  
**Estado:** Aceptado  
**Participantes:** Arquitecto

**Contexto:**
BarberSpot necesita consultar frecuentemente los catálogos de mensajes, parámetros y notificaciones durante sus operaciones. Realizar todas estas consultas directamente a PostgreSQL puede generar lecturas repetitivas y aumentar el tiempo de respuesta cuando varios usuarios utilizan el sistema simultáneamente. Se requiere reducir esas consultas sin comprometer la integridad de la información. Los cambios en los catálogos deben reflejarse oportunamente y una falla de la caché no debe impedir el funcionamiento del sistema. PostgreSQL debe continuar siendo la fuente principal de los datos.

**Alternativas consideradas:**
- Consulta directa a PostgreSQL: Obtener los mensajes, parámetros y definiciones de notificación desde la base de datos en cada consulta. Simplifica la infraestructura, pero mantiene las lecturas repetitivas.
- Caché local con Caffeine:Conservar temporalmente los catálogos en la memoria del backend. Ofrece acceso rápido sin otro servidor, pero cada instancia mantiene una copia independiente y pierde la caché al reiniciarse.
- Caché compartida con Redis:Conservar temporalmente los catálogos en un servicio accesible por las instancias del backend, manteniendo PostgreSQL como fuente principal. Permite compartir la caché, pero requiere administrar conexión, expiración, invalidación y recuperación ante fallas.
- Upstash Redis (Servicio de caché administrado en la nube):Es una plataforma que ofrece bases de datos Redis alojadas en la nube bajo un modelo Serverless (pago por uso según el número de peticiones). Se conecta mediante el protocolo estándar de Redis y no requiere instalar ni administrar un servidor propio.

**Decisión:**
Utilizar Redis como caché compartida para los catálogos de mensajes, parámetros y notificaciones, manteniendo PostgreSQL como fuente principal. Aplicar el patrón cache-aside, con tiempo de expiración e invalidación después de confirmar cambios en la base de datos. Si Redis no está disponible, consultar directamente PostgreSQL. La caché no se utilizará como autoridad para confirmar reservas ni permisos.

**Consecuencias:**
Positivas: Reduce las consultas repetitivas a PostgreSQL y puede mejorar el tiempo de respuesta. Permite compartir la caché entre instancias del backend. Si Redis falla, el sistema puede continuar consultando PostgreSQL.

Negativas: Agrega un componente que requiere configuración, recursos y mantenimiento. Una invalidación incorrecta puede devolver datos desactualizados. Cuando Redis no esté disponible, aumentarán las consultas a PostgreSQL y podría disminuir el rendimiento.

**Justificación:**
Por qué se selecciona Redis:Permite almacenar temporalmente los catálogos y reducir consultas repetitivas a PostgreSQL. También permite compartir la caché si el backend utiliza varias instancias, manteniendo PostgreSQL como fuente principal.

Por qué no se selecciona únicamente la consulta directa a PostgreSQL:Es una opción más sencilla, pero conserva todas las lecturas repetitivas y no incorpora la reducción de carga buscada.

Por qué no se selecciona Caffeine: Cada instancia mantiene su propia copia, lo que exige coordinación adicional para reflejar cambios entre instancias. Es una alternativa válida si el backend permanece en una sola instancia.

Por qué no se selecciona Upstash Redis: aunque es económico y fácil de configurar, al ser un servicio externo agrega una pequeña demora en cada consulta por viajar a través de internet, y su plan gratuito tiene un límite diario de peticiones que podría agotarse en un día de mucho movimiento, generando fallas o costos inesperados.

**Prueba de concepto:** [`/pocs/redis`](../pocs/redis) — Se comprobó el patrón cache-aside (cache miss -> guarda, cache hit -> lee) y el comportamiento de respaldo si Redis no está disponible.

---

## ADR-020 — Catálogo centralizado de mensajes

**Fecha de propuesta:** 31/08/2026  
**Fecha de aprobación:** 05/09/2026  
**Estado:** Aprobado  
**Participantes:** Arquitecto

**Contexto:**
BarberSpot debe mostrar mensajes claros al confirmar operaciones, validar información o informar errores durante el agendamiento, la cancelación y la reprogramación de citas. Mantener estos textos dispersos en diferentes partes del código puede producir mensajes repetidos, contradictorios o difíciles de actualizar. Se necesita una definición centralizada que permita identificar cada mensaje mediante un código estable y mantener una comunicación uniforme con los usuarios. Los textos deben poder modificarse de manera controlada, sin revelar detalles técnicos ni información sensible.

**Alternativas consideradas:**
- Mensajes directamente en el código:Escribir los textos dentro de controllers, servicios o componentes. Es sencillo inicialmente, pero dificulta mantener uniformidad y exige modificar el código para actualizar los mensajes.
- Archivos de recursos versionados:Organizar los códigos y textos en archivos de propiedades o JSON incluidos con la aplicación. Centraliza los mensajes, pero los cambios requieren publicar una nueva versión.
- Catálogo de mensajes en PostgreSQL:Almacenar códigos estables, textos, plantillas y estado en tablas administradas mediante funciones autorizadas. Permite cambios controlados sin recompilar, pero requiere validación, permisos y mensajes de respaldo ante indisponibilidad.

**Decisión:**
Implementar un catálogo de mensajes en PostgreSQL con código estable, texto o plantilla y estado activo. El backend resolverá los mensajes mediante este catálogo y permitirá su modificación únicamente a administradores autorizados. Validar las variables de las plantillas y conservar mensajes mínimos de respaldo para responder cuando el catálogo no esté disponible.

**Consecuencias:**
Positivas:Mantiene mensajes uniformes y reutilizables. Permite actualizar los textos sin recompilar la aplicación, conservando sus códigos estables. Facilita ofrecer respuestas comprensibles sin exponer detalles técnicos.

Negativas:Requiere administrar permisos, validar plantillas y mantener los códigos utilizados por la aplicación. Un cambio incorrecto puede generar mensajes confusos o incompletos. Se necesitan mensajes de respaldo cuando el catálogo no esté disponible.

**Justificación:**
Por qué se selecciona PostgreSQL:Permite centralizar códigos, textos y plantillas, y modificarlos mediante funciones administrativas autorizadas sin recompilar, siempre que se mantenga el contrato de códigos utilizado por la aplicación.

Por qué no se seleccionan mensajes directamente en el código:Dispersan los textos, favorecen inconsistencias y obligan a modificar y publicar la aplicación para actualizarlos.

Por qué no se seleccionan archivos de recursos versionados:Organizan los mensajes, pero sus cambios requieren publicar una nueva versión y no permiten la administración durante la operación prevista para BarberSpot.

---

## ADR-021 — Catálogo de parámetros de negocio

**Fecha de propuesta:** 31/08/2026  
**Fecha de aprobación:** 05/09/2026  
**Estado:** Aprobado  
**Participantes:** Arquitecto

**Contexto:**
BarberSpot necesita aplicar reglas de negocio que pueden cambiar durante su operación, como la anticipación mínima permitida para cancelar o reprogramar una cita. Mantener estos valores directamente en el código obligaría a modificar y publicar la aplicación cada vez que el negocio solicite un ajuste. Se requiere administrar los parámetros de manera centralizada y controlada, verificando sus tipos, rangos y coherencia antes de aceptar cambios. Solo los usuarios autorizados deben modificarlos y las credenciales de los servicios no deben almacenarse como parámetros de negocio.

**Alternativas consideradas:**
- Valores fijos en el código:Definir directamente las reglas, como la anticipación mínima para cancelar una cita. Facilita la implementación inicial, pero cada ajuste requiere modificar, probar y publicar la aplicación.
- Archivos de configuración o variables de entorno: Mantener los valores fuera del código y configurarlos por ambiente. Evita recompilar para cambiar valores, pero normalmente requiere reiniciar o redesplegar y depende de personal técnico.
- Catálogo de parámetros en PostgreSQL:Almacenar código, tipo, valor, unidad y restricciones de cada parámetro, con edición administrativa autorizada. Permite ajustes durante la operación, pero requiere validar coherencia y controlar cuándo entran en vigor.

**Decisión:**
Implementar un catálogo de parámetros de negocio en PostgreSQL con código, tipo, valor, unidad, restricciones y versión. Permitir cambios mediante funciones administrativas autorizadas, validando su formato, rango y coherencia antes de guardarlos. Los casos de uso aplicarán los valores vigentes y los cambios invalidarán la caché correspondiente. Las credenciales permanecerán en Vault, fuera de este catálogo.

**Consecuencias:**
Positivas:Permite ajustar las reglas configurables del negocio sin modificar el código. Centraliza sus valores y facilita comprobar tipos, rangos y versiones. Limita los cambios a administradores autorizados.

Negativas: Un valor incorrecto puede afectar operaciones como la cancelación o reprogramación. Requiere validaciones de coherencia y control de entrada en vigor. Si se utiliza caché, debe evitarse que las operaciones apliquen valores desactualizados.

**Justificación:**
Por qué se selecciona PostgreSQL: Permite administrar reglas configurables mediante valores tipados, restricciones y versiones. El administrador puede realizar ajustes autorizados sin modificar el código, conservando controles sobre su validez.

Por qué no se seleccionan valores fijos en el código: Cada cambio de regla exige desarrollo, pruebas y publicación, incluso cuando solo cambia un valor permitido.

Por qué no se seleccionan archivos de configuración o variables de entorno: Son adecuados para configuración técnica, pero normalmente requieren intervención técnica y reinicio o redespliegue. No ofrecen por sí solos la administración de reglas de negocio prevista.

---

## ADR-022 — Catálogo de notificaciones

**Fecha de propuesta:** 31/08/2026  
**Fecha de aprobación:** 05/09/2026  
**Estado:** Aprobado  
**Participantes:** Arquitecto

**Contexto:**
BarberSpot debe notificar al cliente y al barbero cuando una cita sea agendada, cancelada o reprogramada. Cada evento requiere definir el contenido del aviso, sus destinatarios y los datos necesarios para construirlo. Mantener estas definiciones dispersas en el código dificulta su actualización y puede generar comunicaciones inconsistentes. Se necesita organizar los tipos de notificación y sus plantillas de manera centralizada, con cambios controlados. Esta definición debe mantenerse separada del mecanismo de envío y del registro de las notificaciones pendientes, enviadas o fallidas.

**Alternativas consideradas:**
- Definiciones dentro del código: Construir el asunto, contenido y reglas de cada aviso directamente en los servicios.
- Plantillas en archivos versionados: Mantener las plantillas de correo en archivos separados del código.
- Plantillas administradas en Brevo: Gestionar el contenido en el proveedor y referenciar sus identificadores desde el backend.
- Catálogo de notificaciones en PostgreSQL: Centralizar tipos de evento, canal, plantilla o referencia de Brevo, versión y variables permitidas.

**Decisión:**
Implementar un catálogo de notificaciones en PostgreSQL para los eventos de agendamiento, cancelación y reprogramación de citas. Registrar el canal de correo, la referencia de plantilla de Brevo, su versión, las variables permitidas y los roles destinatarios. El backend determinará los destinatarios concretos y utilizará el Notification Gateway para el envío.

**Consecuencias:**
Positivas:Centraliza los tipos de aviso y mantiene coherencia en contenido, variables y destinatarios. Facilita actualizar plantillas de manera controlada y separa su definición del mecanismo de envío.

Negativas:Requiere coordinar las versiones del catálogo con las plantillas de Brevo. Una plantilla inexistente o incompatible puede impedir el envío, aunque la cita permanezca registrada.

**Justificación:**
Por qué se selecciona PostgreSQL: Permite organizar los tipos de aviso, referencias de plantillas, versiones, variables y roles destinatarios. Mantiene estas definiciones bajo control de BarberSpot y separadas del transporte de correo.

Por qué no se seleccionan definiciones dentro del código: Dificultan mantener uniformidad y requieren modificar y publicar la aplicación para actualizar las definiciones.

Por qué no se seleccionan únicamente archivos versionados: Facilitan revisión, pero sus cambios requieren una nueva publicación y no ofrecen la administración durante la operación prevista.

Por qué no se selecciona depender únicamente de Brevo:Las plantillas de Brevo se utilizarán para el diseño visual del correo HTML, pero no sustituyen el catálogo local que relaciona qué evento del negocio activa la notificación, cuáles variables son obligatorias y qué rol debe recibir el mensaje.

---

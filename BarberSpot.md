# BarberSpot — Documentación de Arquitectura de Software

Proyecto de Ingeniería de Software 2 — BarberSpot

---

## 1. Funcionalidades Críticas

| Identificador Requisito Funcional/Historia de Usuario/Caso de Uso | Especificación Requisito Funcional/Historia de Usuario/Caso de Uso | Tipo de funcionalidad significativa | Justificación | Observación |
|---|---|---|---|---|
| HU-03 | Como usuario del sistema (cliente/barbero/administrador), quiero iniciar sesión con mi correo y contraseña, para acceder al sistema según mi rol. | Valor de negocio | Es la puerta de entrada a todo el sistema; sin autenticación, ningún actor (cliente, barbero o administrador) puede operar dentro de BarberSpot. |  |
| HU-05 | Como administrador, quiero registrar un nuevo barbero con su perfil (nombre, foto, especialidad), para que esté disponible en el sistema y los clientes puedan seleccionarlo. | Valor de negocio | Sin barberos registrados no existe oferta que mostrar, es la base del catálogo comercial sobre el que se va a trabajar en la barbería. |  |
| HU-07 | Como administrador, quiero registrar un servicio (nombre, precio y duración), para que los clientes puedan seleccionarlo al agendar su cita. | Valor de negocio | Sin servicios registrados no hay nada que el cliente pueda elegir al agendar; es la base del catálogo comercial de la barberia. |  |
| HU-09 | Como barbero, quiero configurar mi horario base semanal, para que los clientes solo vean horarios en los que realmente trabajo. | Ambos | Habilita al barbero a recibir citas y sostiene todo el flujo de agendamiento, ya que sin esto no hay disponibilidad que ofrecer al cliente. Técnicamente, el horario configurado debe alimentar correctamente el cálculo posterior de disponibilidad, validando que las franjas horarias sean coherentes. |  |
| HU-11 | Como cliente, quiero ver el listado de barberos disponibles y consultar sus horarios libres, para elegir con quién y cuándo agendar mi cita. | Ambos | Es el primer paso del flujo principal del negocio, ya que sin esta consulta el cliente no puede decidir cuándo y con quién agendar y además se necesita que en el lado técnico requiere cruzar en tiempo real el horario base del barbero, sus bloqueos puntuales y las citas ya agendadas para calcular la disponibilidad real | Al ser una consulta muy frecuente, su rendimiento debe probarse bajo carga; una respuesta lenta aquí afecta directamente la experiencia de todo el flujo de agendamiento. |
| HU-12 | Como cliente, quiero seleccionar un barbero, un servicio y un horario disponible, para reservar mi cita sin depender de llamadas telefónicas. | Ambos | Es la funcionalidad central del negocio, ya que reemplaza la gestión manual de citas por llamadas o WhatsApp, que era el problema original a resolver, y además se necesita que en el lado técnico el sistema garantice que dos clientes no reserven el mismo horario con el mismo barbero al mismo tiempo |  |
| HU-14 | Como cliente, quiero cancelar una cita agendada, para liberar el horario si ya no puedo asistir, dentro del tiempo permitido. | Ambos | Evita horarios bloqueados sin uso real, mejorando la relación con el cliente y protegiendo la imagen del negocio, y además se necesita que en el lado técnico se valide el tiempo límite de cancelación y se libere el horario de forma consistente |  |
| HU-15 | Como barbero, quiero ver mis citas asignadas por día, para organizar mi tiempo de trabajo. | Valor de negocio | Es la herramienta diaria de trabajo del barbero; sin esta vista no puede organizar su jornada ni saber a quién va atender. |  |
| HU-18 | Como administrador, quiero ver reportes de ingresos, el servicio más solicitado y el desempeño por barbero, para tomar decisiones informadas sobre el negocio. | Ambos | Genera un valor a la barberia, ya que de esta manera van a estar más contabilizados los clientes que adquieren un servicio en la barberia y adquiere un reto técnico porque  requiere cálculos y seguridad. |  |
| HU-20 | Como cliente, quiero recibir un correo de confirmación al agendar una cita, para tener constancia de la reserva realizada. | Reto técnico | Requiere integrar un servicio externo de correo y manejar posibles fallos de envío sin bloquear ni afectar el proceso de agendamiento. |  |
| HU-21 | Como barbero, quiero recibir un correo de confirmacion cuando me agendan o cuando me cancelan una cita. | Reto técnico | Requiere integrar el mismo servicio externo de correo utilizado en HU-20, adaptando el contenido del mensaje para notificar al barbero sobre nuevas citas o cancelaciones en su agenda |  |

## 2. Restricciones de Negocio

| Tipo | Restricción de Negocio | Justificación | Plan acción |
|---|---|---|---|
| Humano | El administrador de la barbería solo dispone de aproximadamente 1 hora al día, para reunirse con el equipo de desarrollo, validar avances y resolver dudas sobre el negocio. | Al ser el único administrador del negocio, su tiempo está mayormente ocupado atendiendo clientes, por lo que su disponibilidad para el proyecto es limitada y debe aprovecharse de forma eficiente. | Programar reuniones cortas y estructuradas (máximo 60 minutos), con agenda enviada previamente, y consolidar dudas para resolverlas en bloque en cada sesión. |
| Humano | Los barberos, al estar atendiendo clientes durante toda su jornada laboral, solo pueden dedicar tiempo puntual y esporádico entre cortes para probar el sistema y dar retroalimentación sobre su funcionamiento. | La validación de funcionalidades clave como la configuración de disponibilidad y la gestión de citas depende directamente de los barberos, por lo que su poca disponibilidad puede retrasar la retroalimentación necesaria para ajustar el sistema antes del lanzamiento. | Realizar sesiones breves de prueba aprovechando pausas entre citas, y habilitar un canal asincrónico (chat) para que los barberos reporten observaciones sin necesidad de reunirse. |
| Humano | Los clientes de la barbería incluyen personas de distintas edades y niveles de familiaridad con la tecnología, incluyendo clientes de mayor edad que prefieren la atención tradicional y tienen poca experiencia usando páginas web. | Si la interfaz no es lo suficientemente simple e intuitiva, se corre el riesgo de excluir a un segmento importante de clientes habituales del negocio, quienes podrían optar por seguir agendando por llamada o simplemente dejar de asistir si sienten que el proceso digital es complicado. | Implementar capacitaciones cortas y guías prácticas para clientes y barberos, para el manejo del sistema. |
| Tiempo | El proyecto de digitalización de la barbería debe iniciar el 1 de agosto de 2026 y estar completamente en funcionamiento antes del 1 de diciembre de 2026, fecha límite establecida por el dueño del negocio. | El dueño del negocio fijó esta fecha porque diciembre representa el pico de ingresos anual de la barbería, por lo que exige que el sistema esté operativo y probado antes de esa fecha. | Definir un cronograma con entregas parciales cada 2-3 semanas, priorizando primero las funcionalidades críticas de disponibilidad y agendamiento, para asegurar un sistema usable antes de la fecha límite. |
| Tiempo | El negocio necesita mantener el sistema de citas manual (llamadas y WhatsApp) funcionando en paralelo con BarberSpot durante un período inicial, antes de depender por completo del sistema digital, para no perder citas si surgen fallas o ajustes en las primeras semanas de uso. | El dueño del negocio no puede arriesgarse a que un error o una funcionalidad no probada en un escenario real haga que un cliente se quede sin poder agendar; mantener ambos canales simultáneamente durante la transición reduce ese riesgo mientras se gana confianza en el sistema. | Mantener el canal de WhatsApp y llamadas activo como respaldo durante las primeras semanas de uso del sistema, y definir una fecha de cierre gradual una vez se confirme la estabilidad del sistema. |
| Legal | Garantizar el habeas data para todos los usuarios de la plataforma | De acuerdo con las leyes Colombianas, todos los datos personales de los usuarios deben ser protegidos y salvaguardados siguendo unos protocolos estrictos de seguridad. | Incluir un aviso de tratamiento de datos en el registro de usuarios, cifrar las contraseñas almacenadas y restringir el acceso a la información personal únicamente a los roles autorizados. |
| Presupuesto | El negocio cuenta con un presupuesto inicial limitado, cercano a $5.000.000, para invertir en tecnología, al tratarse de una barbería local de un solo local. | Al ser un negocio pequeño, no cuenta con un gran capital destinado a licencias de software costosas ni a plataformas de terceros que cobran suscripción o comisión por cada cita agendada, por lo que el software debe ser adaptado a este presupuesto inicial y ser capaz de no tener una mensualidad costosa que no lleve a perdidas al negocio. | Priorizar funcionalidades críticas y diseñar arquitectura modular que permita entregar MVP escalable sin sobrepasar presupuesto. |
| Proceso | Aunque el proceso de agendamiento y las funcionalidades principales de BarberSpot ya están definidas, es posible que durante el desarrollo surjan nuevas necesidades o ajustes no contemplados inicialmente, a medida que el dueño del negocio y los barberos interactúen con el sistema y identifiquen mejoras sobre su operación real. | A pesar de contar con un levantamiento de requisitos previo, el negocio seguirá operando y evolucionando durante el desarrollo del proyecto, por lo que es natural que surjan necesidades adicionales una vez el dueño y los barberos empiecen a visualizar el sistema en funcionamiento y lo comparen con su forma actual de trabajar. | Mantener reuniones periódicas de seguimiento con el dueño del negocio para validar avances y capturar nuevas necesidades a tiempo, priorizando su incorporación en iteraciones posteriores sin afectar las funcionalidades críticas ya comprometidas. |
| Proceso | El negocio exige que el pago de los servicios se mantenga presencial en el local, sin procesar pagos en línea a través del sistema. | El dueño del negocio prefiere evitar la complejidad legal, financiera y de seguridad asociada al procesamiento de pagos electrónicos (pasarelas de pago, comisiones, fraude), manteniendo el cobro como se ha manejado tradicionalmente. | Diseñar el flujo de agendamiento de forma que el sistema únicamente reserve el horario y el servicio, dejando el cobro como un paso posterior gestionado directamente en el local. |

## 3. Restricciones Técnicas

| Tipo | Categoria | Restricción técnica | Justificación |
|---|---|---|---|
| Impuesta por el cliente | Compatibilidad de dispositivos | El sistema debe implementarse como una aplicación web responsive, con diseño mobile-first, compatible con los navegadores más usados, sin requerir instalación de ninguna aplicación. | El dueño del negocio solicitó explícitamente que sus clientes pudieran agendar desde el celular sin fricciones de instalación, ya que es el dispositivo que más utilizan tanto los clientes como los propios barberos. |
| Propia del proyecto | Arquitectura de software | El sistema debe construirse siguiendo una arquitectura en capas (presentación, lógica de negocio y acceso a datos). | Separar responsabilidades en capas facilita que el equipo trabaje en paralelo (frontend y backend) sin bloquearse, y permite corregir o mejorar un módulo sin afectar el resto del sistema, algo clave dado el tamaño reducido del equipo. |
| Propia del proyecto | Base de datos | El modelo de datos debe implementarse sobre una base de datos relacional (SQL). | Las relaciones entre las entidades del negocio (citas, barberos, servicios, usuarios) son claras y estructuradas, por lo que un motor relacional garantiza integridad referencial y consultas eficientes, frente a una base NoSQL que no aporta ventajas en este caso. |
| Propia del proyecto | Concurrencia y consistencia | El módulo de agendamiento debe implementar control de concurrencia (transacciones atómicas o bloqueo optimista) al registrar una cita. | Es el reto técnico más crítico del sistema: sin este control, dos clientes podrían reservar el mismo horario con el mismo barbero, generando pérdida de confianza y problemas operativos reales para el negocio. |
| Propia del proyecto | Seguridad | El sistema debe implementar control de acceso basado en roles (RBAC) y almacenar las contraseñas cifradas mediante hashing (nunca en texto plano). | Al manejar datos personales de clientes, barberos y administrador, el sistema debe garantizar que cada usuario acceda únicamente a las funciones de su rol, cumpliendo además con lo exigido por la Ley 1581 de 2012 (Habeas Data). |
| Propia del proyecto | Infraestructura y despliegue | El sistema debe operar priorizando servicios de hosting, base de datos y correo de bajo costo o gratuitos, ajustándose al presupuesto de $5.000.000 disponible para desarrollo, puesta en producción y mantenimiento durante 1 año. | El presupuesto es reducido frente a la escala de un proyecto empresarial; debe cubrir tanto la construcción como la operación de todo un año, por lo que no se justifica invertir en infraestructura cloud-native compleja cuando servicios básicos son suficientes para el volumen esperado del negocio. |
| Propia del proyecto | Arquitectura de software | La lógica de negocio debe exponerse mediante una API REST consumida por el frontend. | Desacoplar el backend del frontend permite que, en el futuro, se pueda construir una aplicación móvil nativa u otro cliente sin necesidad de reescribir la lógica de negocio ya implementada. |
| Propia del proyecto | Diseño | Se debe propender por un diseño con bajo acoplamiento y alta cohesión entre los módulos del sistema (usuarios, disponibilidad, agendamiento, reportes). | Permite que cambios o nuevas funcionalidades se integren sin afectar otros módulos, protegiendo especialmente el funcionamiento del agendamiento y la disponibilidad, que son el corazón del negocio. |
| Propia del proyecto | Metodológico | Se debe propender por la aplicación de una metodología ágil (Scrum simplificado), con entregas parciales cada 2-3 semanas. | Permite mostrar avances constantes al dueño del negocio dentro de su disponibilidad limitada, y ajustar el rumbo del proyecto rápidamente si surgen nuevas necesidades durante el desarrollo, tal como se identificó en las restricciones de proceso. |
| Propia del proyecto | Documentación | Se debe propender por documentar de forma clara los endpoints de la API y los módulos principales del sistema. | Facilita que cualquier integrante del equipo entienda y dé mantenimiento al código de un compañero, reduciendo el riesgo de retrasos por la experiencia limitada del equipo y el tamaño reducido del mismo. |
| Propia del proyecto | Control de versiones | El equipo debe utilizar Git y un repositorio remoto (GitHub) con un flujo de ramas definido para el trabajo colaborativo. | Al ser varios integrantes trabajando en distintos módulos del sistema, un flujo de ramas ordenado evita conflictos de código y facilita revisar cambios antes de integrarlos a la versión principal. |
| Propia del proyecto | Prácticas de código limpio | El desarrollo debe seguir principios de código limpio (Clean Code), evitando código desordenado y "code smells". | Facilita que cualquier integrante del equipo entienda y modifique el código de un compañero, algo especialmente importante dado el tamaño reducido del equipo y el tiempo limitado del semestre. |
| Propia del proyecto | Patrones de diseño | Se debe propender por la aplicación de los principios DRY (Don't Repeat Yourself) y KISS (Keep It Simple, Stupid) en el diseño e implementación del sistema. | Evitar duplicación de código y mantener soluciones simples reduce la probabilidad de errores y facilita el mantenimiento del sistema por parte de un equipo con experiencia limitada en proyectos de este tamaño. |
| Propia del proyecto | Pruebas de software | Se deben implementar pruebas unitarias para el módulo de cálculo de disponibilidad y el módulo de agendamiento de citas. | Son los módulos más complejos y propensos a errores del sistema (cálculo de horarios libres y control de concurrencia), por lo que probarlos de forma aislada reduce el riesgo de fallos en producción. |
| Propia del proyecto | Prácticas de desarrollo | Los parámetros de negocio más propensos a cambiar (tiempo límite de cancelación, duración de servicios) deben quedar configurables desde el panel de administración, sin requerir cambios de código. | Ya se identificó que algunas reglas del negocio podrían ajustarse durante el desarrollo; dejar estos valores parametrizables evita despliegues innecesarios y le da autonomía al negocio para ajustar su operación. |

## 4. Trade-off QA (Priorización inicial de Atributos de Calidad)

| Puesto | Atributo de calidad |
|---|---|
| 1 | Disponibilidad |
| 2 | Usabilidad |
| 3 | Confiabilidad |
| 4 | Seguridad |
| 5 | Rendimiento |
| 6 | Capacidad de ser administrado |
| 7 | Costo |
| 8 | Escalabilidad |
| 9 | Capacidad de ser soportado |
| 10 | Accesibilidad |

## 5. Mapa de Empatía (Votación real por rol)

| Atributo de calidad | Cliente | Barbero | Administrador barberia | Total | Ponderador global |
|---|---|---|---|---|---|
| Disponibilidad | 1 | 1 | 3 | 5 | 3% |
| Usabilidad | 2 | 4 | 7 | 13 | 8% |
| Confiabilidad | 3 | 2 | 4 | 9 | 5% |
| Seguridad | 4 | 5 | 8 | 17 | 10% |
| Rendimiento | 5 | 3 | 5 | 13 | 8% |
| Capacidad de ser administrado | 10 | 8 | 1 | 19 | 12% |
| Costo | 9 | 6 | 2 | 17 | 10% |
| Escalabilidad | 8 | 10 | 6 | 24 | 15% |
| Capacidad de ser soportado | 7 | 9 | 9 | 25 | 15% |
| Accesibilidad | 6 | 7 | 10 | 23 | 14% |
|  | 55 | 55 | 55 | 165 | 100% |

## 6. Preguntas de Atributos de Calidad

### Disponibilidad

| Pregunta | Respuesta |
|---|---|
| Deseas que el sistema este siempre disponible para el cliente (todos los dias de la semana, a cualquier hora, 24/7)? | Sí |
| ¿Desea que el sistema permita que varios clientes agenden citas al mismo tiempo sin fallar, soportando al menos 20 a 30 personas conectadas simultáneamente (por ejemplo: un sábado en la mañana, la hora de mayor demanda)? | Sí |
| ¿Desea que el sistema esté disponible desde cualquier dispositivo con navegador web, sin importar la marca (por ejemplo: celular, tablet o computador)? | Sí |
| ¿Desea que el sistema se recupere en menos de 5 minutos después de una falla del servidor, conservando las citas ya registradas (por ejemplo: si el servidor se reinicia, las citas del día no desaparecen)? | Sí |
| ¿Desea que el sistema esté disponible todo el tiempo para sus clientes, incluso durante actualizaciones o mejoras futuras (por ejemplo: que se publique una mejora sin que el cliente note que el sistema estuvo caído)? | Sí |
| ¿Le preocuparía que, por usar un hosting de bajo costo, el sistema tarde algunos segundos en responder tras un período sin uso (por ejemplo: la primera persona que entra en la mañana espera unos segundos más de lo normal)? | No |
| ¿Desea que el sistema le avise al administrador si llega a presentar una caída prolongada (por ejemplo: una notificación si el sistema lleva más de 10 minutos sin responder)? | Sí |
| ¿Desea que la información de las citas se conserve en un respaldo distinto del servidor principal, para no perderla si este falla (por ejemplo: si el servidor donde funciona BarberSpot se daña, las citas de la semana se pueden recuperar desde una copia guardada en otro lugar, en vez de perderse por completo)? | Sí |
| ¿Necesita que el sistema esté disponible el 100% del tiempo sin ninguna excepción, con infraestructura redundante en varios servidores desde el lanzamiento inicial? | No |
### Usabilidad

| Pregunta | Respuesta |
|---|---|
| ¿Desea que un cliente que use el sistema por primera vez pueda completar el agendamiento sin pedir ayuda a nadie (por ejemplo: entender por sí solo dónde elegir el barbero, el servicio y el horario)? | Sí |
| ¿Desea que el sistema muestre mensajes claros y específicos cuando falte un campo o haya un error (por ejemplo: "Selecciona un servicio antes de continuar")? | Sí |
| ¿Desea que el cliente pueda agendar una cita en máximo 4 pasos (por ejemplo: elegir barbero, elegir servicio, elegir horario y confirmar, sin pantallas adicionales)? | Sí |
| ¿Desea que el sistema muestre un resumen con nombre del barbero, servicio, precio, fecha y hora antes de confirmar la cita (por ejemplo: una pantalla final de revisión antes del botón "Confirmar")? | Sí |
| ¿Desea que el sistema pida confirmación antes de acciones importantes como cancelar una cita (por ejemplo: ¿Seguro que quieres cancelar tu cita del viernes)? | Sí |
| ¿Desea que el sistema use un lenguaje sencillo y cercano, evitando términos técnicos (por ejemplo: decir "Tu cita fue guardada" en vez de "Registro exitoso en la base de datos")? | Sí |
| ¿Desea que el proceso de agendar una cita sea igual de sencillo desde el celular que desde un computador (los mismos pasos, solo adaptados al tamaño de pantalla)? | Sí |
| ¿Desea que el sistema recuerde automáticamente el último barbero o servicio elegido por un cliente para agilizar su próxima cita? | No |
| ¿Desea que el sistema esté disponible en más de un idioma desde el lanzamiento inicial (por ejemplo: que un cliente extranjero pueda cambiarlo a inglés)? | No |
### Confiabilidad

| Pregunta | Respuesta |
|---|---|
| ¿Desea que el sistema nunca permita que dos clientes agenden el mismo horario con el mismo barbero (por ejemplo: si Juan y Jose intentan reservar las 3:00 p. m. al mismo tiempo, solo uno de los dos logra la reserva y al otro se le informa de inmediato)? | Sí |
| ¿Desea que una cita cancelada libere correctamente el horario en menos de 1 minuto (por ejemplo: si Juan cancela su cita de las 3:00 p. m., ese horario aparece disponible de inmediato para otro cliente)? | Sí |
| ¿Desea que el cliente conserve su cita original si una reprogramación no puede completarse (por ejemplo: si otra persona toma el nuevo horario elegido, el sistema no borra la cita que el cliente ya tenía)? | Sí |
| ¿Desea que el estado de una cita sea siempre el mismo para los tres actores del sistema (por ejemplo: si el barbero marca una cita como "finalizada", el cliente y el administrador la ven igual, sin retrasos ni inconsistencias)? | Sí |
| ¿Desea que un cambio futuro en el precio de un servicio no modifique el valor de citas ya realizadas (por ejemplo: una cita agendada cuando el corte costaba $25.000 mantiene ese valor en el historial, aunque después el precio suba a $30.000)? | Sí |
| ¿Desea que si el sistema falla justo cuando el cliente va a confirmar la cita, no se genere una reserva a medias (por ejemplo: si se corta la conexión al confirmar, no debe quedar la cita en el sistema)? | Sí |
| ¿Desea que la información de una misma cita sea idéntica para el cliente, el barbero y el administrador, sin versiones distintas entre ellos? | Sí |
| ¿Desea que, si el sistema debe reiniciarse por alguna falla, ninguna cita ya confirmada se pierda o se duplique? | Sí |
### Seguridad

| Pregunta | Respuesta |
|---|---|
| ¿Desea que cada usuario solo pueda acceder a las funciones de su propio rol (por ejemplo: que un cliente no pueda entrar al panel donde se editan los precios y los barberos)? | Sí |
| ¿Desea que los datos personales de cada cliente estén protegidos frente a otros usuarios (por ejemplo: que un cliente no pueda ver el teléfono, correo o historial de citas de otro cliente)? | Sí |
| ¿Desea que ninguna persona, ni siquiera el administrador o el equipo técnico, pueda consultar la contraseña real de un usuario (por ejemplo: que las contraseñas se guarden cifradas y sean imposibles de leer directamente)? | Sí |
| ¿Es consciente de que, al recolectar datos personales, el sistema debe cumplir con la Ley 1581 de 2012 de protección de datos en Colombia (por ejemplo: pedir autorización al cliente antes de guardar su correo y teléfono)? | Sí |
| ¿Desea que el sistema cierre la sesión automáticamente tras 30 minutos de inactividad en un computador compartido del negocio? | Sí |
| ¿Desea que el sistema bloquee temporalmente una cuenta después de varios intentos fallidos de inicio de sesión (por ejemplo: tras 5 intentos incorrectos seguidos)? | Sí |
| ¿Necesita que el sistema exija un código adicional de verificación (autenticación de doble factor) cada vez que el administrador inicia sesión, desde el lanzamiento inicial? | No |
| ¿Necesita que el sistema cumpla con certificaciones internacionales de seguridad, como ISO 27001, desde el lanzamiento inicial? | No |
### Rendimiento

| Pregunta | Respuesta |
|---|---|
| ¿Desea que los horarios disponibles de un barbero aparezcan en menos de 2 segundos después de seleccionarlo (por ejemplo: elegir un barbero y ver de inmediato qué horas están libres esta semana)? | Sí |
| ¿Desea que la confirmación de una cita se muestre en menos de 3 segundos después de presionar "Confirmar" (por ejemplo: saber casi de inmediato si la cita quedó registrada)? | Sí |
| ¿Desea que el sistema mantenga este mismo tiempo de respuesta cuando al menos 30 clientes lo usen al mismo tiempo (por ejemplo: un sábado en la mañana, sin que la página se vuelva notoriamente más lenta)? | Sí |
| ¿Aceptaría un tiempo de espera de hasta 5 segundos al generar reportes con mucha información (por ejemplo: que el administrador espere unos segundos al consultar los ingresos de todo un año)? | Sí |
| ¿Desea que la agenda de un barbero cargue en menos de 2 segundos, incluso si tiene más de 50 citas registradas en la semana (por ejemplo: consultar toda la semana sin una espera larga)? | Sí |
| ¿Desea que cancelar una cita se procese en menos de 3 segundos desde que se confirma la acción? | Sí |
| ¿Necesita que el sistema mantenga estos mismos tiempos de respuesta si en el futuro se manejan varias sedes al mismo tiempo? | No |
| ¿Necesita que el sistema responda en menos de 100 milisegundos, como una plataforma financiera de alta frecuencia (por ejemplo: procesar órdenes de compra y venta de acciones en tiempo real)? | No |
### Capacidad de ser administrado

| Pregunta | Respuesta |
|---|---|
| ¿Desea que el administrador pueda agregar, editar o desactivar un barbero en menos de 5 minutos, sin ayuda de un programador (por ejemplo: registrar un barbero nuevo el mismo día que llega a trabajar)? | Sí |
| ¿Desea que el administrador pueda cambiar el precio de un servicio directamente desde el sistema (por ejemplo: subir el corte de $25.000 a $30.000 sin pedir ayuda externa)? | Sí |
| ¿Desea que cada barbero pueda bloquear sus propios horarios no laborables (por ejemplo: bloquear el lunes en la mañana porque tiene una cita médica)? | Sí |
| ¿Desea que el administrador pueda buscar citas usando filtros específicos, como barbero, fecha o estado (por ejemplo: consultar las citas canceladas de un barbero en una semana puntual)? | Sí |
| ¿Desea recibir un resumen simple de cuántas citas se agendaron y cancelaron en la semana, sin tener que pedirlo a un técnico? | Sí |
| ¿Desea poder activar o desactivar temporalmente un servicio del catálogo (por ejemplo: pausar "tratamientos capilares" mientras no tenga el producto disponible)? | Sí |
| ¿Desea tener distintos niveles de administrador, con permisos diferentes entre ellos (por ejemplo: un encargado que solo vea reportes, pero no pueda cambiar precios)? | No |
### Costo

| Pregunta | Respuesta |
|---|---|
| ¿Desea que el sistema utilice inicialmente herramientas gratuitas o de bajo costo mientras el número de usuarios sea bajo (por ejemplo: un plan gratuito de hosting mientras la barbería tenga pocos clientes registrados)? | Sí |
| ¿Desea que el desarrollo y la puesta en marcha del sistema no superen el presupuesto de $5.000.000 destinado para un año completo (análisis, desarrollo, pruebas y despliegue incluidos)? | Sí |
| ¿Desea evitar el pago de comisiones por cada cita agendada (por ejemplo: no pagar un valor adicional si en un mes se registran 300 citas, a diferencia de plataformas como Booksy que si cobra comisión por cita registrada)? | Sí |
| ¿Desea que los $5.000.000 cubran tanto la construcción del sistema como su funcionamiento durante el primer año (hosting, dominio, correo)? | Sí |
| ¿Estaría dispuesto a asumir un costo mensual fijo, aunque sea bajo, si eso garantizara mayor estabilidad (por ejemplo: $30.000 al mes por un hosting de pago en vez de uno gratuito)? | No |
| ¿Estaría dispuesto a pagar más de $5.000.000 si surgiera un imprevisto técnico durante el desarrollo (por ejemplo: un costo adicional de $500.000 para resolver un problema no previsto)? | No |
### Escalabilidad

| Pregunta | Respuesta |
|---|---|
| ¿Desea que el sistema siga funcionando correctamente si aumenta hasta un 50% el tráfico habitual (por ejemplo: durante diciembre, cuando más personas consultan y agendan citas)? | Sí |
| ¿Desea que el sistema permita pasar de 3 a 10 barberos sin tener que construir un sistema nuevo? | Sí |
| ¿Desea que se puedan agregar nuevos servicios en el futuro sin modificar el sistema completo (por ejemplo: incluir coloración o tratamientos capilares)? | Sí |
| ¿Desea que, en el futuro, BarberSpot pueda administrar otra sede desde la misma aplicación (por ejemplo: gestionar barberos y citas de una segunda barbería en otro municipio)? | No |
| ¿Desea que el sistema pueda adaptarse si en el futuro decide ofrecer combos de servicios (por ejemplo: "corte + barba + cejas" a un precio especial)? | Sí |
| ¿Planea abrir una segunda sede en los próximos 6 meses? | No |
| ¿Planea aumentar significativamente el número de clientes registrados en el próximo año (por ejemplo: pasar de unos pocos cientos a varios miles)? | No |
### Accesibilidad

| Pregunta | Respuesta |
|---|---|
| ¿Desea que el sistema funcione igual en los navegadores más utilizados: Chrome, Safari, Edge y Firefox? | Sí |
| ¿Desea que el texto tenga un tamaño mínimo legible, de al menos 16 píxeles (por ejemplo: que un cliente adulto mayor pueda leer sin acercar mucho la pantalla)? | Sí |
| ¿Desea que los colores usados para mensajes de confirmación, advertencia y error sean claramente distinguibles entre sí (por ejemplo: verde para éxito, rojo para error, con suficiente contraste)? | Sí |
| ¿Desea que los botones y campos sean lo suficientemente grandes para tocarlos con facilidad desde un celular, sin necesidad de hacer zoom? | Sí |
| ¿Le interesa que, desde el lanzamiento inicial, el sistema sea compatible con lectores de pantalla para personas con discapacidad visual? | No |
| ¿Necesita una versión especial y distinta del sistema pensada solo para personas mayores? | No |
### Capacidad de ser soportado

| Pregunta | Respuesta |
|---|---|
| ¿Desea que el sistema registre fecha, hora y módulo exacto en el que ocurrió un error (por ejemplo: "10 de octubre, 3:00 p. m., falló el envío del correo de confirmación")? | Sí |
| ¿Desea que el equipo técnico pueda identificar en menos de 15 minutos cuál parte del sistema está fallando (por ejemplo: saber si el problema es de la página, el servidor, la base de datos o el correo)? | Sí |
| ¿Desea que existan copias de seguridad diarias para recuperar la información en caso de falla (por ejemplo: recuperar las citas y usuarios guardados el día anterior si la base de datos falla)? | Sí |
| ¿Desea que el equipo de desarrollo corrija las fallas reportadas en un plazo máximo de 48 horas? | Sí |
| ¿Necesita soporte técnico disponible las 24 horas, los 7 días de la semana, como un call center? | No |
| ¿Necesita un manual técnico detallado del sistema entregado desde el primer día de lanzamiento? | No |

## 7. Escenarios de Calidad (con votación)

| ID | Atributo | Fuente del estímulo | Estímulo | Ambiente | Artefacto | Respuesta | Medida de la respuesta | Votos Cliente | Votos Barbero | Votos Administrador |
|---|---|---|---|---|---|---|---|---|---|---|
| **Disponibilidad** | | | | | | | | | | |
| ESC-DISP-001 | Disponibilidad | Cliente | Intenta acceder al sistema para agendar una cita en cualquier día u hora | Operación normal, cualquier día de la semana | Aplicación web completa | El sistema permite el acceso y la consulta de disponibilidad sin restricción horaria | Disponible las 24 horas, los 7 días de la semana | 2 | 0 | 0 |
| ESC-DISP-002 | Disponibilidad | Múltiples clientes | Acceden e intentan agendar citas al mismo tiempo | Hora de mayor demanda (sábado en la mañana) | Módulo de agendamiento | El sistema atiende las solicitudes sin caerse ni degradar el servicio | Soporta al menos 20 a 30 usuarios simultáneos sin fallas | 3 | 2 | 0 |
| ESC-DISP-003 | Disponibilidad | Cliente / Barbero / Administrador | Accede al sistema desde un dispositivo con navegador web | Operación normal, cualquier dispositivo | Interfaz web responsive | El sistema carga y funciona correctamente sin importar el dispositivo | Funciona en el 100% de los dispositivos con navegador actualizado | 0 | 0 | 0 |
| ESC-DISP-004 | Disponibilidad | Falla del servidor | El servidor deja de responder temporalmente | Condición de falla técnica | Servidor / infraestructura de hosting | El sistema se reactiva conservando las citas ya registradas | Recuperación en menos de 5 minutos, sin pérdida de datos | 0 | 0 | 0 |
| ESC-DISP-005 | Disponibilidad | Equipo de desarrollo | Publica una actualización o mejora del sistema | Mantenimiento planeado | Proceso de despliegue | La actualización se aplica sin que el cliente perciba una caída del servicio | 0 minutos de interrupción notoria durante despliegues | 0 | 0 | 0 |
| ESC-DISP-006 | Disponibilidad | Falla prolongada del sistema | El sistema deja de responder por más de 10 minutos | Condición de falla extendida | Módulo de monitoreo/alertas | El sistema notifica al administrador sobre la caída | Notificación enviada antes de cumplirse 10 minutos de inactividad | 0 | 0 | 0 |
| ESC-DISP-007 | Disponibilidad | Falla del servidor principal | El servidor principal se daña o deja de funcionar | Condición de falla grave | Sistema de respaldo (backup) | Las citas se recuperan desde una copia almacenada en otro lugar | Recuperación completa sin pérdida de citas registradas | 0 | 0 | 1 |
| ESC-DISP-008 | Disponibilidad | Barbero | Consulta su agenda del día mientras el sistema presenta una falla breve | Condición de falla técnica breve | Módulo de agenda del barbero | El sistema se recupera y muestra la agenda sin pérdida de citas | Menos de 5 minutos de indisponibilidad para el barbero | 0 | 4 | 0 |
| ESC-DISP-009 | Disponibilidad | Administrador | Ingresa a los reportes del negocio en horario nocturno | Operación fuera del horario de atención | Panel de administración | El sistema permanece accesible para consulta administrativa en cualquier horario | Acceso disponible las 24 horas para el rol administrador | 0 | 0 | 2 |
| ESC-DISP-010 | Disponibilidad | Cliente | Accede al sistema desde una conexión de datos móviles inestable | Zona con señal débil | Aplicación web | El sistema carga la información esencial aunque la conexión sea lenta | Página de agendamiento carga en menos de 8 segundos incluso con conexión débil | 0 | 0 | 0 |
| **Usabilidad** | | | | | | | | | | |
| ESC-USA-001 | Usabilidad | Cliente nuevo | Usa el sistema por primera vez para agendar una cita | Primera visita, sin experiencia previa | Módulo de agendamiento | El cliente completa el proceso guiándose solo por la interfaz | Completa el agendamiento sin solicitar ayuda externa | 3 | 0 | 0 |
| ESC-USA-002 | Usabilidad | Cliente | Omite un campo obligatorio (ej. no selecciona un servicio) | Proceso de agendamiento en curso | Formulario de agendamiento | El sistema muestra un mensaje específico indicando qué falta | Mensaje entendible, sin términos técnicos | 1 | 0 | 0 |
| ESC-USA-003 | Usabilidad | Cliente | Agenda una cita completa (barbero, servicio, horario, confirmación) | Operación normal | Flujo de agendamiento | El sistema guía el proceso en pasos secuenciales simples | Máximo 4 pasos para completar el agendamiento | 2 | 0 | 0 |
| ESC-USA-004 | Usabilidad | Cliente | Está a punto de confirmar una cita | Último paso del agendamiento | Pantalla de resumen | El sistema muestra barbero, servicio, precio, fecha y hora antes de guardar | 100% de los campos relevantes visibles antes de confirmar | 0 | 0 | 0 |
| ESC-USA-005 | Usabilidad | Cliente | Presiona el botón "Cancelar cita" | Operación normal | Módulo de gestión de citas del cliente | El sistema solicita confirmación antes de ejecutar la cancelación | Confirmación explícita requerida antes de cancelar | 0 | 0 | 0 |
| ESC-USA-006 | Usabilidad | Cliente / Barbero | Recibe un mensaje del sistema tras una acción | Operación normal | Mensajes del sistema | El sistema usa lenguaje cercano y no técnico | Mensajes en lenguaje natural, sin jerga técnica | 0 | 0 | 0 |
| ESC-USA-007 | Usabilidad | Cliente | Agenda una cita desde celular y luego desde un computador | Operación normal en distintos dispositivos | Interfaz responsive | El sistema mantiene los mismos pasos, solo adapta el diseño | Mismo número de pasos y flujo en ambos dispositivos | 0 | 1 | 0 |
| ESC-USA-008 | Usabilidad | Barbero | Actualiza el estado de una cita entre cortes | Tiempo limitado, pocos minutos disponibles | Panel del barbero | El sistema permite completar la acción con un solo clic | Acción completada en menos de 3 pasos | 0 | 3 | 0 |
| ESC-USA-009 | Usabilidad | Administrador | Necesita cambiar el precio de un servicio | Operación normal | Panel de administración de servicios | El sistema permite el cambio con un formulario simple y directo | Cambio realizado en menos de 5 minutos sin ayuda externa | 0 | 0 | 0 |
| ESC-USA-010 | Usabilidad | Cliente | Necesita cancelar una cita ya agendada | Consulta del listado "Mis citas" | Módulo de mis citas | El botón de cancelar es visible y accesible directamente desde el listado, sin entrar a otra pantalla | Acción de cancelar accesible en 1 clic desde el listado | 0 | 0 | 0 |
| **Confiabilidad** | | | | | | | | | | |
| ESC-CONF-001 | Confiabilidad | Dos clientes simultáneos | Intentan reservar el mismo horario con el mismo barbero al mismo tiempo | Alta concurrencia | Módulo de agendamiento / base de datos de citas | El sistema confirma la reserva de uno solo y notifica al otro | 0% de citas duplicadas para el mismo horario y barbero | 4 | 2 | 2 |
| ESC-CONF-002 | Confiabilidad | Cliente | Cancela una cita agendada | Operación normal | Módulo de gestión de citas | El horario cancelado queda disponible de inmediato para otros clientes | Liberación del horario en menos de 1 minuto | 0 | 0 | 0 |
| ESC-CONF-003 | Confiabilidad | Cliente | Intenta reprogramar su cita a un horario que otro cliente toma primero | Alta concurrencia | Módulo de agendamiento | El sistema conserva la cita original sin eliminarla | 0% de citas eliminadas por reprogramación fallida | 1 | 0 | 0 |
| ESC-CONF-004 | Confiabilidad | Barbero | Marca una cita como "finalizada" | Operación normal | Módulo de estado de citas | El cliente y el administrador ven el mismo estado de inmediato | 100% de coincidencia del estado entre los tres actores | 0 | 0 | 0 |
| ESC-CONF-005 | Confiabilidad | Administrador | Actualiza el precio de un servicio | Operación normal | Módulo de servicios / historial de citas | Las citas anteriores conservan el precio con el que fueron agendadas | 0% de citas históricas modificadas por cambios de precio | 0 | 0 | 0 |
| ESC-CONF-006 | Confiabilidad | Falla de conexión | Se interrumpe la conexión justo al confirmar una cita | Condición de falla de red | Módulo de agendamiento | El sistema no registra una cita incompleta | 0% de citas "fantasma" generadas por fallas de conexión | 0 | 0 | 0 |
| ESC-CONF-007 | Confiabilidad | Cliente, barbero y administrador | Consultan la misma cita desde sus respectivas vistas | Operación normal | Base de datos de citas | Los tres ven exactamente la misma información | 0 discrepancias entre las vistas de los tres actores | 0 | 0 | 0 |
| ESC-CONF-008 | Confiabilidad | Falla del sistema | El sistema debe reiniciarse por una falla técnica | Condición de falla | Base de datos / servidor | Las citas ya confirmadas se mantienen intactas | 0% de pérdida o duplicación de citas tras el reinicio | 0 | 0 | 0 |
| ESC-CONF-009 | Confiabilidad | Cliente | Llega a la barbería a la hora de su cita agendada | Operación normal | Sistema de agendamiento | El barbero cuenta con el registro exacto de la cita | 100% de coincidencia entre la cita agendada y la atención real | 0 | 0 | 0 |
| ESC-CONF-010 | Confiabilidad | Barbero | Revisa su agenda del día para saber a quién atender | Operación normal | Módulo de agenda del barbero | La agenda mostrada corresponde exactamente a las citas reales confirmadas | 0 discrepancias entre agenda mostrada y citas reales | 0 | 4 | 0 |
| ESC-CONF-011 | Confiabilidad | Administrador | Genera un reporte de ingresos del mes | Operación normal | Módulo de reportes | El reporte refleja únicamente citas realmente completadas y cobradas | 100% de exactitud entre el reporte y las citas finalizadas | 0 | 0 | 3 |
| **Seguridad** | | | | | | | | | | |
| ESC-SEG-001 | Seguridad | Cliente | Intenta acceder al panel de edición de precios y barberos | Operación normal | Módulo de control de acceso (RBAC) | El sistema bloquea el acceso por no corresponder a su rol | 100% de los accesos no autorizados bloqueados | 0 | 0 | 0 |
| ESC-SEG-002 | Seguridad | Cliente | Intenta consultar el teléfono o historial de otro cliente | Operación normal | Módulo de gestión de usuarios | El sistema impide el acceso a datos de terceros | 0% de exposición de datos personales entre clientes | 2 | 0 | 0 |
| ESC-SEG-003 | Seguridad | Administrador o equipo técnico | Intenta consultar la contraseña real de un usuario | Operación normal / mantenimiento | Módulo de autenticación | El sistema almacena las contraseñas cifradas (hash) | 0% de contraseñas visibles en texto plano | 0 | 0 | 0 |
| ESC-SEG-004 | Seguridad | Cliente nuevo | Se registra en el sistema proporcionando datos personales | Cumplimiento legal (Ley 1581 de 2012) | Módulo de registro de usuarios | El sistema solicita autorización antes de almacenar sus datos | 100% de los registros con consentimiento explícito | 2 | 0 | 0 |
| ESC-SEG-005 | Seguridad | Usuario en computador compartido | Deja la sesión abierta sin actividad | Computador compartido en el local | Módulo de autenticación | El sistema cierra la sesión automáticamente | Cierre automático tras 30 minutos de inactividad | 0 | 0 | 0 |
| ESC-SEG-006 | Seguridad | Usuario o atacante | Ingresa la contraseña incorrecta varias veces seguidas | Intento de acceso no autorizado | Módulo de autenticación | El sistema bloquea temporalmente la cuenta | Bloqueo tras 5 intentos fallidos consecutivos | 0 | 0 | 0 |
| ESC-SEG-007 | Seguridad | Barbero | Intenta acceder a la información de citas de otro barbero | Operación normal | Módulo de control de acceso | El sistema restringe la vista únicamente a sus propias citas | 100% de restricción de acceso entre barberos | 0 | 2 | 0 |
| ESC-SEG-008 | Seguridad | Cliente | Ingresa su contraseña en el formulario de inicio de sesión | Conexión a través de internet | Canal de comunicación (HTTPS) | Los datos viajan cifrados durante la transmisión | 100% de las comunicaciones bajo protocolo HTTPS | 0 | 0 | 0 |
| ESC-SEG-009 | Seguridad | Administrador | Da de baja a un barbero que ya no trabaja en el negocio | Operación normal | Módulo de gestión de usuarios | El barbero pierde acceso inmediato al sistema | Revocación de acceso en menos de 1 minuto | 0 | 0 | 0 |
| ESC-SEG-010 | Seguridad | Cliente | Se registra usando su correo electrónico | Operación normal | Módulo de registro | El sistema valida que el correo tenga un formato válido antes de crear la cuenta | 100% de los registros con correo validado antes de guardarse | 0 | 0 | 0 |
| **Rendimiento** | | | | | | | | | | |
| ESC-REND-001 | Rendimiento | Cliente | Selecciona un barbero para ver su disponibilidad | Operación normal | Módulo de cálculo de disponibilidad | El sistema muestra los horarios libres | Tiempo de respuesta menor o igual a 2 segundos | 2 | 0 | 0 |
| ESC-REND-002 | Rendimiento | Cliente | Presiona "Confirmar" para agendar una cita | Operación normal | Módulo de agendamiento | El sistema procesa y confirma la solicitud | Tiempo de respuesta menor a 3 segundos | 0 | 0 | 0 |
| ESC-REND-003 | Rendimiento | Múltiples clientes | 30 clientes consultan horarios al mismo tiempo | Alta demanda (sábado) | Módulo de disponibilidad | El sistema mantiene el mismo tiempo de respuesta | Sin degradación perceptible con 30 usuarios simultáneos | 0 | 0 | 0 |
| ESC-REND-004 | Rendimiento | Administrador | Genera el reporte de ingresos de todo un año | Consulta con gran volumen de datos | Módulo de reportes | El sistema procesa y muestra el resultado | Tiempo de respuesta de hasta 5 segundos | 0 | 0 | 1 |
| ESC-REND-005 | Rendimiento | Barbero | Consulta su agenda de la semana | Semana con alta carga de citas (+50) | Módulo de agenda del barbero | El sistema muestra la agenda completa | Tiempo de carga menor a 2 segundos | 0 | 3 | 0 |
| ESC-REND-006 | Rendimiento | Cliente | Confirma la cancelación de una cita | Operación normal | Módulo de gestión de citas | El sistema procesa la cancelación | Tiempo de respuesta menor a 3 segundos | 0 | 0 | 0 |
| ESC-REND-007 | Rendimiento | Barbero | Actualiza el estado de una cita entre un corte y otro | Tiempo limitado del barbero | Panel del barbero | El sistema guarda el cambio de inmediato | Tiempo de respuesta menor a 2 segundos | 0 | 2 | 0 |
| ESC-REND-008 | Rendimiento | Administrador | Filtra el listado de citas por barbero y fecha | Operación normal | Módulo de listado de citas | El sistema muestra los resultados filtrados | Tiempo de respuesta menor a 3 segundos | 0 | 0 | 0 |
| ESC-REND-009 | Rendimiento | Cliente | Carga la página principal del sistema por primera vez | Primera visita, sin caché del navegador | Frontend del sistema | La página carga completamente | Tiempo de carga inicial menor a 3 segundos | 0 | 0 | 0 |
| ESC-REND-010 | Rendimiento | Administrador | Consulta el listado completo de citas del mes | Operación normal con volumen mensual de citas | Módulo de listado de citas | El sistema muestra los resultados filtrados o paginados | Tiempo de respuesta menor a 3 segundos | 0 | 0 | 0 |
| **Capacidad de ser administrado** | | | | | | | | | | |
| ESC-ADM-001 | Capacidad de ser administrado | Administrador | Registra un nuevo barbero que acaba de llegar al negocio | Operación normal | Módulo de administración de barberos | El sistema permite el registro sin ayuda de un programador | Registro completo en menos de 5 minutos | 0 | 0 | 5 |
| ESC-ADM-002 | Capacidad de ser administrado | Administrador | Actualiza el precio de un servicio | Operación normal | Módulo de administración de servicios | El sistema aplica el nuevo precio de inmediato | Cambio reflejado en menos de 1 minuto, sin ayuda externa | 0 | 0 | 0 |
| ESC-ADM-003 | Capacidad de ser administrado | Barbero | Bloquea un horario en el que no trabajará (ej. cita médica) | Operación normal | Módulo de disponibilidad del barbero | El sistema oculta ese horario a los clientes | Bloqueo aplicado y visible de inmediato | 0 | 2 | 3 |
| ESC-ADM-004 | Capacidad de ser administrado | Administrador | Busca las citas canceladas de un barbero en una semana puntual | Operación normal | Módulo de listado de citas | El sistema filtra y muestra los resultados exactos | Resultados correctos según los filtros aplicados | 0 | 0 | 0 |
| ESC-ADM-005 | Capacidad de ser administrado | Administrador | Consulta cuántas citas se agendaron y cancelaron en la semana | Operación normal | Módulo de reportes | El sistema muestra el resumen sin intervención técnica | Información disponible sin soporte externo | 0 | 0 | 2 |
| ESC-ADM-006 | Capacidad de ser administrado | Administrador | Pausa temporalmente un servicio del catálogo | Operación normal | Módulo de administración de servicios | El servicio deja de estar disponible para agendar | Cambio aplicado de inmediato en el catálogo visible | 0 | 0 | 0 |
| ESC-ADM-007 | Capacidad de ser administrado | Administrador | Desactiva la cuenta de un cliente con historial de inasistencias | Operación normal | Módulo de gestión de usuarios | El cliente pierde la posibilidad de agendar nuevas citas | Restricción aplicada de inmediato | 0 | 0 | 0 |
| ESC-ADM-008 | Capacidad de ser administrado | Administrador | Necesita ajustar el tiempo límite de cancelación de citas | Operación normal | Panel de configuración de reglas de negocio | El sistema permite modificar este parámetro sin tocar código | Cambio aplicado sin intervención del equipo de desarrollo | 0 | 0 | 0 |
| ESC-ADM-009 | Capacidad de ser administrado | Administrador | Revisa el historial de cambios de precios de un servicio | Operación normal | Módulo de servicios | El sistema conserva un registro de los cambios realizados | Historial disponible para consulta del administrador | 0 | 0 | 0 |
| ESC-ADM-010 | Capacidad de ser administrado | Administrador | Necesita saber cuál es el barbero con más citas atendidas en el mes | Operación normal | Módulo de reportes | El sistema muestra el ranking de barberos por número de citas | Información disponible sin cálculos manuales | 0 | 0 | 0 |
| **Costo** | | | | | | | | | | |
| ESC-COST-001 | Costo | Equipo de desarrollo | Selecciona los servicios de hosting, base de datos y correo | Presupuesto limitado | Infraestructura del sistema | Se eligen planes gratuitos o de bajo costo mientras el número de usuarios sea bajo | Costo de infraestructura igual a $0 en la etapa inicial | 0 | 0 | 2 |
| ESC-COST-002 | Costo | Equipo de desarrollo | Ejecuta el análisis, desarrollo, pruebas y despliegue del sistema | Presupuesto asignado por el negocio | Proyecto completo | El costo total no supera el presupuesto asignado | Costo total menor o igual a $5.000.000 | 0 | 0 | 4 |
| ESC-COST-003 | Costo | Negocio | Se agendan 300 citas en un mes | Operación normal | Sistema completo (propiedad del negocio) | El negocio no paga ningún valor adicional por las citas agendadas | $0 de comisión, sin importar el volumen mensual | 0 | 0 | 0 |
| ESC-COST-004 | Costo | Negocio | Opera el sistema durante el primer año completo | Presupuesto de $5.000.000 para construcción y operación | Infraestructura y desarrollo | El presupuesto cubre construcción, hosting, dominio y correo del año | Costo total del año dentro de los $5.000.000 asignados | 0 | 0 | 0 |
| ESC-COST-005 | Costo | Equipo de desarrollo | El plan gratuito de un servicio alcanza su límite de uso | Crecimiento moderado del negocio | Infraestructura del sistema | El equipo evalúa migrar a un plan de bajo costo dentro del presupuesto restante | Migración posible sin exceder los $5.000.000 totales | 0 | 0 | 0 |
| ESC-COST-006 | Costo | Equipo de desarrollo | Elige entre varias opciones de hosting gratuito disponibles | Selección de infraestructura | Infraestructura del sistema | El equipo selecciona la opción que mejor cubra las necesidades sin generar costos | Costo de hosting igual a $0 durante la fase inicial | 0 | 0 | 0 |
| ESC-COST-007 | Costo | Negocio | Solicita conocer cuánto del presupuesto se ha utilizado en el desarrollo | Seguimiento del proyecto | Reporte de gestión del proyecto | El equipo informa el avance del presupuesto ejecutado | Seguimiento del gasto dentro del límite de $5.000.000 | 0 | 0 | 0 |
| **Escalabilidad** | | | | | | | | | | |
| ESC-ESCA-001 | Escalabilidad | Clientes | Aumenta hasta un 50% el tráfico habitual | Temporada de diciembre | Sistema completo | El sistema sigue funcionando correctamente | Sin degradación del servicio con hasta 50% más de tráfico | 1 | 0 | 0 |
| ESC-ESCA-002 | Escalabilidad | Administrador | Aumenta el número de barberos de 3 a 10 | Crecimiento del negocio | Módulo de administración de barberos | El sistema incorpora los nuevos barberos sin cambios estructurales | Escalamiento sin necesidad de reconstruir el sistema | 0 | 0 | 0 |
| ESC-ESCA-003 | Escalabilidad | Administrador | Agrega un nuevo servicio (ej. coloración) | Crecimiento del catálogo | Módulo de administración de servicios | El sistema lo incorpora sin modificar el resto de la plataforma | Nuevo servicio disponible sin cambios de código | 0 | 0 | 0 |
| ESC-ESCA-004 | Escalabilidad | Administrador | Crea un combo de servicios (ej. corte + barba + cejas) | Evolución del catálogo | Módulo de servicios | El sistema permite adaptar el catálogo a esta nueva necesidad | Incorporación posible sin rediseñar el módulo de servicios | 0 | 0 | 0 |
| ESC-ESCA-005 | Escalabilidad | Equipo de desarrollo | Diseña el sistema pensando en un eventual crecimiento futuro | Diseño inicial del sistema | Arquitectura en capas | El sistema se construye de forma modular para facilitar cambios futuros | Arquitectura preparada para escalar sin reescritura completa | 0 | 0 | 0 |
| ESC-ESCA-006 | Escalabilidad | Negocio | Opera con el volumen real esperado de una barbería local (3 a 10 barberos, 30 a 50 citas diarias) | Operación normal de un solo local | Arquitectura completa del sistema (base de datos, servidor) | El sistema maneja este volumen sin degradación de rendimiento ni infraestructura adicional | Rendimiento estable con hasta 50 citas diarias y 10 barberos activos | 0 | 0 | 1 |
| ESC-ESCA-007 | Escalabilidad | Negocio | El número de clientes registrados crece de forma orgánica mes a mes | Crecimiento moderado esperado | Base de datos de clientes | El sistema almacena y consulta la información de nuevos clientes sin degradar el rendimiento | Sin degradación con hasta unos pocos miles de clientes registrados en el primer año | 0 | 0 | 0 |
| ESC-ESCA-008 | Escalabilidad | Administrador | Decide ofrecer el servicio de limpieza facial además de los ya existentes | Expansión del catálogo | Módulo de servicios | El sistema permite agregarlo sin afectar los servicios ya configurados | Nuevo servicio operativo en menos de 5 minutos, sin tiempo de inactividad | 0 | 0 | 0 |
| **Accesibilidad** | | | | | | | | | | |
| ESC-ACC-001 | Accesibilidad | Cliente | Accede usando Chrome, Safari, Edge o Firefox | Operación normal | Interfaz web | El sistema funciona igual en los cuatro navegadores | 0 errores de funcionamiento entre navegadores | 1 | 0 | 0 |
| ESC-ACC-002 | Accesibilidad | Cliente adulto mayor | Lee la información del sistema desde su celular | Operación normal | Interfaz de usuario | El texto se muestra en un tamaño legible sin acercar la pantalla | Tamaño de texto mínimo de 16 píxeles | 0 | 0 | 0 |
| ESC-ACC-003 | Accesibilidad | Cliente | Recibe un mensaje de confirmación, advertencia o error | Operación normal | Sistema de mensajes/notificaciones | Cada tipo de mensaje usa un color claramente distinguible | Contraste suficiente entre verde, rojo y amarillo | 0 | 0 | 0 |
| ESC-ACC-004 | Accesibilidad | Cliente | Presiona un botón desde su celular | Uso móvil | Interfaz responsive | El botón se puede presionar sin necesidad de hacer zoom | Tamaño mínimo de botones adecuado para pantallas táctiles | 2 | 1 | 0 |
| ESC-ACC-005 | Accesibilidad | Cliente | Ingresa al sistema desde un celular de gama media o baja | Dispositivo con pantalla reducida | Interfaz responsive | El sistema ajusta el contenido para verse completo sin desplazamiento horizontal | 0 elementos cortados en resoluciones desde 360px de ancho | 0 | 0 | 0 |
| ESC-ACC-006 | Accesibilidad | Cliente | Necesita identificar qué campos son obligatorios en un formulario | Proceso de registro o agendamiento | Formularios del sistema | El sistema marca visualmente los campos obligatorios | 100% de los campos obligatorios claramente identificados | 0 | 0 | 0 |
| **Capacidad de ser soportado** | | | | | | | | | | |
| ESC-SOP-001 | Capacidad de ser soportado | Falla del sistema | Ocurre un error durante el agendamiento | Operación normal en producción | Sistema de logs | El sistema registra fecha, hora y módulo afectado | 100% de los errores relevantes registrados con esta información | 0 | 0 | 0 |
| ESC-SOP-002 | Capacidad de ser soportado | Equipo técnico | Recibe un reporte de falla del sistema | Soporte post-implementación | Sistema de logs | El equipo identifica el módulo responsable de la falla | Identificación en menos de 15 minutos de revisión | 0 | 0 | 0 |
| ESC-SOP-003 | Capacidad de ser soportado | Falla de la base de datos | La base de datos presenta una falla | Condición de falla | Sistema de respaldo (backup) | El sistema permite recuperar la información del día anterior | Copias de seguridad realizadas diariamente | 0 | 0 | 0 |
| ESC-SOP-004 | Capacidad de ser soportado | Cliente o barbero | Reporta una falla del sistema | Soporte post-implementación | Proceso de soporte técnico | El equipo de desarrollo corrige la falla reportada | Corrección aplicada en un plazo máximo de 48 horas | 0 | 0 | 0 |
| ESC-SOP-005 | Capacidad de ser soportado | Equipo técnico | Necesita revisar el estado general del sistema periódicamente | Mantenimiento preventivo | Panel de monitoreo básico | El equipo verifica que el sistema, la base de datos y el correo funcionen correctamente | Revisión posible en menos de 10 minutos | 0 | 0 | 0 |
| ESC-SOP-006 | Capacidad de ser soportado | Cliente o barbero | Reporta que no puede iniciar sesión | Soporte post-implementación | Canal de soporte (correo o formulario de contacto) | El equipo recibe el reporte y contacta al usuario para resolverlo | Primera respuesta en menos de 24 horas | 0 | 0 | 0 |
| **TOTAL** |  |  |  |  |  |  |  | 26 | 26 | 26 |

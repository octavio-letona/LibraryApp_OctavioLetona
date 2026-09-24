1. Término técnico: Clase
2. Definición formal: En la programación orientada a objetos (POO), una clase es una plantilla, plano o modelo que define el estado (atributos) y el comportamiento (métodos) que compartirán todos los objetos de ese tipo.
3. Definición en mis palabras: Es como el molde o los planos arquitectónicos de una casa. No es la casa en sí, pero nos dice qué características va a tener y qué cosas podrá hacer una vez que la construyamos.
4. Ubicación en el código: Se encuentra en los paquetes del modelo o entidades (ej. src/main/java/modelo/Usuario.java).
5. Ejemplo práctico & problema que resuelve: En un sistema de gestión, en lugar de programar variables sueltas para cada usuario nuevo, se crea la clase Usuario. Esto resuelve el problema de la repetición de código, permitiendo instanciar infinitos usuarios que automáticamente tendrán la estructura correcta (nombre, ID, correo).

1. Término técnico: Objeto
2. Definición formal: Una instancia de una clase que encapsula estado y comportamiento. Ocupa un espacio dinámico en la memoria durante la ejecución del programa y puede interactuar con otros objetos.
3. Definición en mis palabras: Es lo que sale del molde. Si la clase es el plano del carro, el objeto es el carro ya construido, con su color específico, listo para arrancar en la memoria del programa.
4. Ubicación en el código: Se ubica generalmente en los Controladores o en los métodos DAO al instanciar datos (ej. Usuario nuevoUsuario = new Usuario();).
5. Ejemplo práctico & problema que resuelve: Al obtener datos de una base de datos MySQL, convertimos cada fila en un objeto de Java. Esto resuelve el problema de tener que manejar datos sueltos, permitiendo manipular la información de forma lógica y estructurada dentro del flujo de la aplicación.

1. Término técnico: Encapsulamiento
2. Definición formal: Principio de la POO que consiste en ocultar el estado interno de un objeto, requiriendo que toda interacción o modificación de sus atributos se realice estrictamente a través de métodos públicos (getters y setters).
3. Definición en mis palabras: Es como una caja fuerte para los datos. Protege las variables para que ninguna otra parte del código las modifique de forma equivocada desde afuera, obligando a usar puertas específicas y controladas para acceder a ellas.
4. Ubicación en el código: Se evidencia mediante el uso del modificador de acceso private en los atributos de cualquier modelo (ej. private int id;) y sus respectivos métodos getId() y setId().
5. Ejemplo práctico & problema que resuelve: En una cuenta bancaria, hacer que el atributo saldo sea privado y solo permitir su cambio a través del método depositar(). Esto resuelve el problema de la integridad de los datos, evitando que por error otro módulo del código asigne un saldo negativo directamente (cuenta.saldo = -500;).

1. Término técnico: Tipo Primitivo
2. Definición formal: Son los tipos de datos más básicos y elementales del lenguaje que no son objetos. Almacenan directamente sus valores en la memoria (stack) y no poseen métodos propios. En Java incluyen int, double, boolean, char, entre otros.
3. Definición en mis palabras: Son los datos crudos y sencillos. A diferencia de los objetos, no tienen "superpoderes" ni métodos, solo sirven para guardar un valor directo de la forma más ligera posible.
4. Ubicación en el código: En la declaración de atributos de las clases o variables locales dentro de los controladores (ej. int edad = 16; o boolean estaActivo = true;).
5. Ejemplo práctico & problema que resuelve: Se utilizan para contadores de bucles for o para almacenar operaciones matemáticas simples. Resuelven el problema del rendimiento y consumo de memoria, ya que procesar un tipo primitivo es mucho más rápido y ligero para la computadora que procesar un objeto complejo.

1. Término técnico: Clase Wrapper (Clase Envoltorio)
2. Definición formal: Clases proporcionadas por la API de Java (como Integer, Double, Boolean) que encapsulan un tipo primitivo dentro de un objeto. Proporcionan métodos de utilidad y permiten que los valores primitivos sean tratados como objetos.
3. Definición en mis palabras: Es como meter un dato simple (primitivo) dentro de una cajita (objeto) para que adquiera métodos útiles y pueda entrar a estructuras de datos que son exclusivas ("VIP") para puros objetos.
4. Ubicación en el código: Al utilizar colecciones en los DAO o controladores, y al hacer conversiones de texto a número (ej. Integer.parseInt(textField.getText());).
5. Ejemplo práctico & problema que resuelve: En Java, estructuras como un ArrayList no aceptan tipos primitivos; no puedes hacer ArrayList<int>. La clase Wrapper resuelve este problema permitiendo declarar ArrayList<Integer>, además de resolver la necesidad de convertir cadenas de texto provenientes de una interfaz gráfica (JavaFX) a valores numéricos.

1.Término técnico: DAO (Data Access Object)
2.Definición formal: Patrón de diseño que separa la interfaz de programación de aplicaciones (API) y la lógica de negocio de la infraestructura de persistencia o acceso a la base de datos.
3.Definición en mis palabras: Es una de las maneras que tenemos de comunicar Java con nuestra base de datos. Sirve como un puente e intermediario especializado entre nuestra aplicación y la base de datos.
4.Ubicación en el código: En la interfaz y clases del paquete DAO al momento de conectar el modelo con la persistencia (ej. LibroDAO.java, LibroDAOImpl.java).
5.Ejemplo práctico & problema que resuelve: Se utilizan como una manera de conectar y gestionar las operaciones CRUD de la base de datos. Resuelve el problema del acoplamiento directo entre la interfaz/lógica y la base de datos.

1.Término técnico: JDBC (Java Database Connectivity)
2.Definición formal: Interfaz de programación de aplicaciones (API) estándar de Java que permite conectar un programa desarrollado en Java con una base de datos relacional.
3.Definición en mis palabras: Es una API que funciona como una capa intermedia o cable estándar para enviar consultas SQL y recibir respuestas de la base de datos.
4.Ubicación en el código: En la clase de conexión y métodos DAO mediante importaciones del paquete java.sql.* (ej. java.sql.Connection, java.sql.DriverManager).
5.Ejemplo práctico & problema que resuelve: Permite la comunicación nativa entre Java y motores como MySQL. Resuelve principalmente el problema de la falta de un estándar único para conectar aplicaciones Java con diferentes sistemas gestores de bases de datos.

1.Término técnico: Singleton
2.Definición formal: Patrón de diseño creacional que garantiza que una clase tenga una sola instancia en todo el programa y ofrece un punto de acceso global a ella.
3.Definición en mis palabras: Es un patrón que nos ayuda a garantizar que una clase se conecte una sola vez y que esa misma instancia sea reutilizada por las demás clases.
4.Ubicación en el código: Se encuentra en la clase de gestión de conexión a la base de datos (ej. src/main/java/conexion/Conexion.java en el método getInstance()).
5.Ejemplo práctico & problema que resuelve: Se aplica al abrir la conexión a MySQL. Resuelve el problema del consumo excesivo de memoria y la saturación del servidor por abrir múltiples conexiones innecesarias.

1.Término técnico: PreparedStatement
2.Definición formal: Interfaz en Java (perteneciente al paquete java.sql) que representa una sentencia SQL precompilada y parametrizada en el servidor de base de datos.
3.Definición en mis palabras: Es una plantilla de consulta SQL parametrizada con signos de interrogación (?) que sustituyen los valores reales de forma segura.
4.Ubicación en el código: Se encuentra dentro de los métodos de las clases DAO cuando se construyen y ejecutan las consultas SQL (ej. LibroDAOImpl.java).
5.Ejemplo práctico & problema que resuelve: Se usa en consultas de inserción, modificación o búsqueda con parámetros. Resuelve dos problemas críticos: la inyección SQL (seguridad) y el rendimiento en consultas repetitivas.

1.Término técnico: Inyección SQL
2.Definición formal: Vulnerabilidad de seguridad en la que un atacante inserta código SQL malicioso a través de los campos de entrada de datos de la aplicación para manipular la consulta ejecutada en la base de datos.
3.Definición en mis palabras: Es un ataque donde un usuario escribe comandos SQL en un cuadro de texto (como un campo de login o búsqueda) para engañar al sistema y acceder o borrar datos sin permiso.
4.Ubicación en el código: Se previene en el código de nuestros DAO (ej. LibroDAOImpl.java) utilizando PreparedStatement en lugar de concatenar cadenas directamente.
5.Ejemplo práctico & problema que resuelve: Ocurre al concatenar variables directamente en un String de SQL ("WHERE user = '" + input + "'"). El uso de consultas preparadas resuelve esta vulnerabilidad al tratar los datos de entrada estrictamente como valores y no como instrucciones ejecutables.
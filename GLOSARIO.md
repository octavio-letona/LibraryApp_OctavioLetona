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
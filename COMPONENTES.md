# COMPONENTES.md

Bitácora de investigación y análisis arquitectónico de cada clase, interfaz y archivo FXML reconstruido.

## 1. Autor.java

* **Nombre de la clase / paquete:** `org.ol.model.Autor` (Paquete `org.ol.model`)
* **Capa arquitectónica (MVC/DAO):** Modelo (`Model` / Entidad)
* **Responsabilidad única:** Representar los datos demográficos e identificadores únicos de un autor o creador literario en el sistema.
* **Dependencias directas:**
  * `AutorLibro.java`
* **Diagrama/Flujo del dato:**
  ```mermaid
  graph LR
      BD[(Base de Datos)] -->|Mapeo/Query| AutorDAO[org.ol.dao.AutorDAO]
      AutorDAO -->|Instanciación| Autor[Autor.java]
      Autor -->|Asociación| AutorLibro[AutorLibro.java]
      Autor -->|Visualización| UI[Gestión de Autores FXML]
  ```

## 2. AutorLibro.java

* **Nombre de la clase / paquete:** `org.ol.model.AutorLibro` (Paquete `org.ol.model`)
* **Capa arquitectónica (MVC/DAO):** Modelo (`Model` / Entidad Relacional)
* **Responsabilidad única:** Representar la tabla intermedia y relación N:M (muchos a muchos) entre las entidades `Autor` y `Libro`.
* **Dependencias directas:**
  * `Autor.java`
  * `Libro.java`
* **Diagrama/Flujo del dato:**
  ```mermaid
  graph TD
      Autor[Autor.java] --> AutorLibro[AutorLibro.java]
      Libro[Libro.java] --> AutorLibro[AutorLibro.java]
      AutorLibro -->|Persistencia| BD[(Base de Datos)]
  ```

## 3. Categoria.java

* **Nombre de la clase / paquete:** `org.ol.model.Categoria` (Paquete `org.ol.model`)
* **Capa arquitectónica (MVC/DAO):** Modelo (`Model` / Entidad)
* **Responsabilidad única:** Representar la clasificación o género literario al cual pertenecen los libros, facilitando la categorización y filtrado en el catálogo.
* **Dependencias directas:**
  * `Libro.java`
* **Diagrama/Flujo del dato:**
  ```mermaid
  graph LR
      BD[(Base de Datos)] -->|Mapeo/Query| CategoriaDAO[org.ol.dao.CategoriaDAO]
      CategoriaDAO -->|Instanciación| Categoria[Categoria.java]
      Categoria -->|Asignación| Libro[Libro.java]
      Categoria -->|Filtro| Controller[org.ol.controller.CatalogoController]
  ```

## 4. Cliente.java

* **Nombre de la clase / paquete:** `org.ol.model.Cliente` (Paquete `org.ol.model`)
* **Capa arquitectónica (MVC/DAO):** Modelo (`Model` / Entidad)
* **Responsabilidad única:** Almacenar la información de identificación, contacto e historial de transacciones de los compradores.
* **Dependencias directas:**
  * `Venta.java`
* **Diagrama/Flujo del dato:**
  ```mermaid
  graph LR
      UI[Formulario Cliente] -->|Creación| Cliente[Cliente.java]
      Cliente -->|Asociación| Venta[Venta.java]
      Cliente -->|Persistencia| ClienteDAO[org.ol.dao.ClienteDAO]
  ```

## 5. DetalleVenta.java

* **Nombre de la clase / paquete:** `org.ol.model.DetalleVenta` (Paquete `org.ol.model`)
* **Capa arquitectónica (MVC/DAO):** Modelo (`Model` / Entidad)
* **Responsabilidad única:** Representar cada ítem o renglón individual dentro de una transacción de venta (cantidad, precio unitario, subtotal y libro asociado).
* **Dependencias directas:**
  * `Venta.java`
  * `Libro.java`
* **Diagrama/Flujo del dato:**
  ```mermaid
  graph LR
      Libro[Libro.java] --> DetalleVenta[DetalleVenta.java]
      DetalleVenta -->|Agregación| Venta[Venta.java]
  ```

## 6. Libro.java

* **Nombre de la clase / paquete:** `org.ol.model.Libro` (Paquete `org.ol.model`)
* **Capa arquitectónica (MVC/DAO):** Modelo (`Model` / Entidad Principal)
* **Responsabilidad única:** Encapsular los atributos, estado y reglas de negocio asociadas a un libro (ISBN, título, precio, stock, estado, categoría) dentro del catálogo.
* **Dependencias directas:**
  * `Categoria.java`
  * `AutorLibro.java`
* **Diagrama/Flujo del dato:**
  ```mermaid
  graph LR
      BD[(Base de Datos)] -->|Mapeo| LibroDAO[org.ol.dao.LibroDAO]
      LibroDAO -->|Instanciación| Libro[Libro.java]
      Libro -->|Lectura/Modificación| Controller[org.ol.controller.LibroController]
      Controller -->|Visualización| View[org.ol.view.LibroView]
  ```

## 7. LineaFactura.java

* **Nombre de la clase / paquete:** `org.ol.model.LineaFactura` (Paquete `org.ol.model`)
* **Capa arquitectónica (MVC/DAO):** Modelo (`Model` / Entidad)
* **Responsabilidad única:** Modelar el desglose de conceptos, impuestos y subtotales impresos en un comprobante fiscal/factura.
* **Dependencias directas:**
  * `Venta.java`
  * `DetalleVenta.java`
* **Diagrama/Flujo del dato:**
  ```mermaid
  graph LR
      DetalleVenta[DetalleVenta.java] --> LineaFactura[LineaFactura.java]
      LineaFactura -->|Generación| FacturaPDF/UI
  ```

## 8. Usuario.java

* **Nombre de la clase / paquete:** `org.ol.model.Usuario` (Paquete `org.ol.model`)
* **Capa arquitectónica (MVC/DAO):** Modelo (`Model` / Entidad de Seguridad)
* **Responsabilidad única:** Contener las credenciales, roles y permisos de acceso para la autenticación y autorización del personal que opera el sistema.
* **Dependencias directas:**
  * `Venta.java`
* **Diagrama/Flujo del dato:**
  ```mermaid
  graph LR
      Security[org.ol.util.SecurityUtil] --> Usuario[Usuario.java]
      Usuario -->|Validación| AuthController[org.ol.controller.AuthController]
  ```

## 9. Venta.java

* **Nombre de la clase / paquete:** `org.ol.model.Venta` (Paquete `org.ol.model`)
* **Capa arquitectónica (MVC/DAO):** Modelo (`Model` / Entidad Encabezado)
* **Responsabilidad única:** Agrupar y gestionar la cabecera de la transacción comercial (fecha, total, cliente, vendedor/usuario e ítems vendidos).
* **Dependencias directas:**
  * `Cliente.java`
  * `Usuario.java`
  * `DetalleVenta.java`
* **Diagrama/Flujo del dato:**
  ```mermaid
  graph TD
      Cliente[Cliente.java] --> Venta[Venta.java]
      Usuario[Usuario.java] --> Venta[Venta.java]
      DetalleVenta[DetalleVenta.java] --> Venta[Venta.java]
      Venta -->|Persistencia| VentaDAO[org.ol.dao.VentaDAO]
  ```
SET FOREIGN_KEY_CHECKS=0;

-- tabla de usuarios
create table if not exists usuarios (
    id_usuario int auto_increment primary key,
    username varchar(50) not null unique,
    password_hash varchar(255) not null,
    rol enum('admin', 'empleado', 'cajero') not null,
    activo boolean default true,
    fecha_creacion timestamp default current_timestamp    
);

-- Agregar columnas solo si no existen (ignora error 1060: Duplicate column)
DROP PROCEDURE IF EXISTS _add_usuario_columns;
DELIMITER //
CREATE PROCEDURE _add_usuario_columns()
BEGIN
    DECLARE CONTINUE HANDLER FOR 1060 BEGIN END;
    ALTER TABLE usuarios ADD COLUMN email VARCHAR(50) NOT NULL DEFAULT '' AFTER username;
    ALTER TABLE usuarios ADD COLUMN first_name VARCHAR(50) NOT NULL DEFAULT '' AFTER email;
    ALTER TABLE usuarios ADD COLUMN last_name VARCHAR(50) NOT NULL DEFAULT '' AFTER first_name;
END //
DELIMITER ;
CALL _add_usuario_columns();
DROP PROCEDURE _add_usuario_columns;

-- Asegurarnos de que tengan un valor por defecto si ya existían de un intento anterior
ALTER TABLE usuarios MODIFY COLUMN email VARCHAR(50) NOT NULL DEFAULT '';
ALTER TABLE usuarios MODIFY COLUMN first_name VARCHAR(50) NOT NULL DEFAULT '';
ALTER TABLE usuarios MODIFY COLUMN last_name VARCHAR(50) NOT NULL DEFAULT '';

-- procedimiento para registrar usuario
drop procedure if exists sp_registrar_usuario;
delimiter //
create procedure sp_registrar_usuario(
    in _username varchar(50), 
    in _password_hash varchar(255), 
    in _rol varchar(20)
)
begin
    insert into usuarios (username, password_hash, rol) 
    values (_username, _password_hash, _rol);
end //
delimiter ;

drop procedure if exists sp_iniciar_sesion;
-- procedimiento para iniciar sesión
delimiter //
create procedure sp_iniciar_sesion(
    in _username varchar(50), 
    in _password_hash varchar(255)
)
begin
    select id_usuario, username, rol 
    from usuarios 
    where username = _username 
      and password_hash = _password_hash 
      and activo = true 
    limit 1;
end //
delimiter ;

-- call sp_registrar_usuario('alvaro',sha2('admin',256),'admin');
-- call sp_iniciar_sesion('alvaro',sha2('admin',256));

select * from usuarios;


use libreriadb_in4cm;

-- ============================================================================
-- PASO 1: Eliminar Llaves Foráneas (FK) que tienen ON DELETE CASCADE peligroso
-- o cuyas columnas padre van a cambiar de tipo de dato.
-- ============================================================================
DROP PROCEDURE IF EXISTS _drop_old_fks;
DELIMITER //
CREATE PROCEDURE _drop_old_fks()
BEGIN
    DECLARE CONTINUE HANDLER FOR 1091 BEGIN END;
    -- Ignora error 1091 si la llave ya no existe
    ALTER TABLE libros DROP FOREIGN KEY fk_a_categorias;
    ALTER TABLE libros DROP FOREIGN KEY fk_a_editoriales;
    
    -- Ignora error 1146 (Table doesn't exist) si las tablas ya fueron renombradas
    BEGIN
        DECLARE CONTINUE HANDLER FOR 1146 BEGIN END;
        ALTER TABLE compras DROP FOREIGN KEY fk_a_cliente;
        ALTER TABLE detalle_compra DROP FOREIGN KEY fk_a_libros;
    END;
END //
DELIMITER ;
CALL _drop_old_fks();
DROP PROCEDURE _drop_old_fks;

-- ============================================================================
-- PASO 2 a 5: Cambios estructurales protegidos contra re-ejecución
-- ============================================================================
DROP PROCEDURE IF EXISTS _apply_ddl_changes;
DELIMITER //
CREATE PROCEDURE _apply_ddl_changes()
BEGIN
    -- Ignorar si la columna no existe (1054), columna duplicada (1060), 
    -- tabla no existe (1146), tabla ya existe (1050),
    -- o incompatibilidad de FK por tratar de modificar una columna que ya está atada a una FK nueva (3780)
    DECLARE CONTINUE HANDLER FOR 1054, 1060, 1146, 1050, 3780 BEGIN END;

    -- Modificar CUI
    ALTER TABLE clientes MODIFY COLUMN cui varchar(13);

    -- Renombrar columnas (falla 1054 si ya se renombraron)
    ALTER TABLE editoriales RENAME COLUMN direccion_editoria TO direccion_editorial;
    ALTER TABLE usuarios RENAME COLUMN id TO id_usuario;

    -- Agregar columnas a libros (falla 1060 si ya existen)
    ALTER TABLE libros ADD COLUMN stock int not null default 0;
    ALTER TABLE libros ADD COLUMN stock_minimo int not null default 5;
    ALTER TABLE libros MODIFY COLUMN precio decimal(10,2) not null;

    -- Modificar compras (falla 1146 si ya se renombró a ventas)
    ALTER TABLE compras MODIFY COLUMN cui_cliente varchar(13);
    ALTER TABLE compras MODIFY COLUMN total_compra decimal(10,2) not null default 0.00;
    ALTER TABLE compras ADD COLUMN id_usuario int not null;

    -- Modificar detalle_compra (falla 1146 si ya se renombró a detalle_venta)
    ALTER TABLE detalle_compra ADD COLUMN cantidad int not null default 1;
    ALTER TABLE detalle_compra ADD COLUMN precio_unitario decimal(10,2) not null default 0.00;

    -- Renombrar tablas (falla 1050 si ventas o detalle_venta ya existen)
    RENAME TABLE compras TO ventas, detalle_compra TO detalle_venta;

    -- Renombrar columnas en las tablas nuevas (falla 1054 si ya se renombraron)
    ALTER TABLE ventas RENAME COLUMN no_compra TO no_venta;
    ALTER TABLE ventas RENAME COLUMN fecha_compra TO fecha_venta;
    ALTER TABLE ventas RENAME COLUMN total_compra TO total_venta;

    ALTER TABLE detalle_venta RENAME COLUMN id_detalle_compra TO id_detalle_venta;
    ALTER TABLE detalle_venta RENAME COLUMN no_compra TO no_venta;
END //
DELIMITER ;
CALL _apply_ddl_changes();
DROP PROCEDURE _apply_ddl_changes;

-- ============================================================================
-- Recrear las Llaves Foráneas con ON DELETE RESTRICT de forma segura
-- ============================================================================
DROP PROCEDURE IF EXISTS _apply_fks;
DELIMITER //
CREATE PROCEDURE _apply_fks()
BEGIN
    -- Ignorar si la llave foránea ya existe (error 1826 Duplicate foreign key)
    DECLARE CONTINUE HANDLER FOR 1826 BEGIN END;
    
    ALTER TABLE libros ADD CONSTRAINT fk_libros_categoria FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria) ON DELETE RESTRICT;
    ALTER TABLE libros ADD CONSTRAINT fk_libros_editorial FOREIGN KEY (nit_editorial) REFERENCES editoriales(nit) ON DELETE RESTRICT;
    
    ALTER TABLE ventas ADD CONSTRAINT fk_ventas_cliente FOREIGN KEY (cui_cliente) REFERENCES clientes(cui) ON DELETE RESTRICT;
    ALTER TABLE ventas ADD CONSTRAINT fk_ventas_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE RESTRICT;
    
    ALTER TABLE detalle_venta ADD CONSTRAINT fk_dc_libro FOREIGN KEY (isbn) REFERENCES libros(isbn) ON DELETE RESTRICT;
END //
DELIMITER ;
CALL _apply_fks();
DROP PROCEDURE _apply_fks;

-- =============================================================================
-- Edicion de sp para ventas eliminamos al inicio compras
-- =============================================================================
drop procedure if exists sp_insertarcompra;
drop procedure if exists sp_listarcompras;
drop procedure if exists sp_buscarcompra;
drop procedure if exists sp_actualizarcompra;
drop procedure if exists sp_eliminarcompra;

drop procedure if exists sp_insertar_venta;
drop procedure if exists sp_listar_ventas;
drop procedure if exists sp_buscar_venta;
drop procedure if exists sp_actualizar_venta;
drop procedure if exists sp_eliminar_venta;

delimiter $$

create procedure sp_listar_ventas()
begin
    select no_venta, fecha_venta, total_venta, cui_cliente, id_usuario
    from ventas;
end $$
create procedure sp_buscar_venta(in _no int)
begin
    select no_venta, fecha_venta, total_venta, cui_cliente, id_usuario
    from ventas where no_venta = _no;
end $$


create procedure sp_insertar_venta(
	in _total decimal(8,2),
    in _cui varchar(13),
    in _id_usuario int)
begin
    insert into ventas(total_venta, cui_cliente, id_usuario)
    values (_total, _cui, _id_usuario);
end $$

create procedure sp_actualizar_venta(
	in _no int,
    in _fecha date,
    in _total decimal(8,2),
    in _cui bigint,
    in _id_usuario int)
begin
    update ventas
    set fecha_venta = _fecha, total_venta = _total, cui_cliente = _cui, id_usuario = _id_usuario
    where no_venta = _no;
end $$

create procedure sp_eliminar_venta(in _no int)
begin
    delete from ventas where no_venta = _no;
end $$

delimiter ;
    

-- =============================================================================
-- Edicion de sp para detalle_ventas eliminamos al inicio detalle_compras
-- =============================================================================
drop procedure if exists sp_insertardetallecompra;
drop procedure if exists sp_listardetallecompra;
drop procedure if exists sp_buscardetallecompra;
drop procedure if exists sp_actualizardetallecompra;
drop procedure if exists sp_eliminardetallecompra;

drop procedure if exists sp_listar_detalle_venta;
drop procedure if exists sp_insertar_detalle_venta;
drop procedure if exists sp_buscar_detalle_venta;
drop procedure if exists sp_actualizar_detalle_venta;
drop procedure if exists sp_eliminar_detalle_venta;

delimiter $$

create procedure sp_listar_detalle_venta()
begin
    select id_detalle_venta, no_venta, isbn, cantidad, precio_unitario as precio 
    from detalle_venta;
end $$

create procedure sp_insertar_detalle_venta(
	in _no int, 
    in _isbn varchar(20),
    in _cantidad int,
    in _precio_unidad double (10,2))
begin
    insert into detalle_venta(no_venta, isbn, cantidad, precio_unitario) 
    values (_no, _isbn, _cantidad, _precio_unidad);
end $$

create procedure sp_buscar_detalle_venta(in _id int)
begin
    select id_detalle_venta, no_venta, isbn, cantidad, precio_unitario as precio 
    from detalle_venta where id_detalle_venta = _id;
end $$

create procedure sp_actualizar_detalle_venta(
    in _id_detalle_venta int,
    in _no_venta int,
    in _isbn varchar(20),
    in _cantidad int,
    in _precio double(10,2)
	)
begin
    update detalle_venta 
    set no_venta = _no_venta, isbn = _isbn, cantidad = _cantidad, precio_unitario = _precio
    where id_detalle_venta = _id_detalle_venta;
end $$

create procedure sp_eliminar_detalle_venta(in _id_detalle_venta int)
begin
    delete from detalle_venta where id_detalle_venta = _id_detalle_venta;
end $$

delimiter ;

-- -----

-- =============================================================================
-- 5. crud: libros
-- =============================================================================
drop procedure if exists sp_insertarlibro;
drop procedure if exists sp_listarlibros;
drop procedure if exists sp_buscarlibro;
drop procedure if exists sp_actualizarlibro;
drop procedure if exists sp_buscar_libro_id;

drop procedure if exists sp_listar_todos_libros;
drop procedure if exists sp_crear_libro;
drop procedure if exists sp_actualizar_libro;
drop procedure if exists sp_eliminar_libro;

delimiter $$
create procedure sp_listar_todos_libros()
begin
    select isbn, titulo, fecha_publicacion, precio, id_categoria, nit_editorial, stock from libros;
end $$

create procedure sp_buscar_libro_id(in _isbn varchar(20))
begin
    select isbn, titulo, fecha_publicacion, precio, id_categoria, nit_editorial, stock 
    from libros 
    where isbn = _isbn;
end $$

create procedure sp_crear_libro(
    in _isbn varchar(20),
    in _titulo varchar(100),
    in _fecha_publicacion date,
    in _precio decimal(8,2),
    in _id_categoria int,
    in _nit_editorial varchar(20),
    in _stock int)
begin
    insert into libros(isbn, titulo, fecha_publicacion, precio, id_categoria, nit_editorial, stock) 
    values (_isbn, _titulo, _fecha_publicacion, _precio, _id_categoria, _nit_editorial, _stock);
end $$

create procedure sp_actualizar_libro(
    in _isbn varchar(20),
    in _titulo varchar(100),
    in _fecha_publicacion date,
    in _precio decimal(8,2),
    in _id_categoria int,
    in _nit_editorial varchar(20),
    in _stock int)
begin
    update libros 
    set titulo = _titulo, 
        fecha_publicacion = _fecha_publicacion, 
        precio = _precio, 
        id_categoria = _id_categoria, 
        nit_editorial = _nit_editorial,
        stock = _stock
    where isbn = _isbn;
end $$

create procedure sp_eliminar_libro(in _isbn varchar(20))
begin
    delete from libros where isbn = _isbn;
end $$

delimiter ;


-- =============================================================================
-- 2. crud: editoriales
-- =============================================================================
drop procedure if exists sp_insertar_editorial;
drop procedure if exists sp_listareditoriales;
drop procedure if exists sp_buscareditorial;
drop procedure if exists sp_actualizareditorial;
drop procedure if exists sp_eliminareditorial;

drop procedure if exists sp_crear_editorial;
drop procedure if exists sp_listar_todos_editoriales;
drop procedure if exists sp_buscar_editorial_por_id;
drop procedure if exists sp_actualizar_editorial;
drop procedure if exists sp_eliminar_editorial;

delimiter $$

create procedure sp_crear_editorial(
    in _nit varchar(20),
    in _nombre_editorial varchar(100),
    in _telefono_editorial varchar(15),
    in _direccion_editorial varchar(100)
)
begin
    insert into editoriales(nit, nombre_editorial, telefono_editorial, direccion_editorial) 
    values (_nit, _nombre_editorial, _telefono_editorial, _direccion_editorial);
end $$

create procedure sp_listar_todos_editoriales()
begin
    select nit, nombre_editorial, telefono_editorial, direccion_editorial from editoriales;
end $$

create procedure sp_buscar_editorial_por_id(in _nit varchar(20))
begin
    select nit, nombre_editorial, telefono_editorial, direccion_editorial
    from editoriales 
    where nit = _nit;
end $$

create procedure sp_actualizar_editorial(
    in _nit varchar(20),
    in _nombre_editorial varchar(100),
    in _telefono_editorial varchar(15),
    in _direccion_editorial varchar(100)
)
begin
    update editoriales 
    set nombre_editorial = _nombre_editorial,
        telefono_editorial = _telefono_editorial,
        direccion_editorial = _direccion_editorial
    where nit = _nit;
end $$

create procedure sp_eliminar_editorial(in _nit varchar(20))
begin
    delete from editoriales where nit = _nit;
end $$

delimiter ;

-- ============================================================================
-- SP PARA USUARIO
-- ============================================================================
drop procedure if exists sp_crear_usuario;
drop procedure if exists sp_listar_todos_usuarios;
drop procedure if exists sp_obtener_usuario_por_id;
drop procedure if exists sp_actualizar_usuario;
drop procedure if exists sp_cambiar_password;
drop procedure if exists sp_desactivar_usuario;
drop procedure if exists sp_eliminar_usuario;

delimiter $$
create procedure sp_crear_usuario(
    in _username varchar(50),
    in _email varchar(50),
    in _first_name varchar(50),
    in _last_name varchar(50),
    in _password_hash varchar(255),
    in _rol enum('admin', 'empleado', 'cajero')
)
begin
    insert into usuarios (username, email, first_name, last_name, password_hash, rol)
    values (_username, _email, _first_name, _last_name, _password_hash, _rol);
end $$

create procedure sp_listar_todos_usuarios()
begin
    select 
        id_usuario, 
        username, 
        email, 
        first_name, 
        last_name, 
        rol, 
        activo, 
        fecha_creacion
    from usuarios;
end $$

create procedure sp_obtener_usuario_por_id(in _id_usuario int)
begin
    select 
        id_usuario, 
        username, 
        email, 
        first_name, 
        last_name, 
        rol, 
        activo, 
        fecha_creacion
    from usuarios
    where id_usuario = _id_usuario;
end $$

create procedure sp_actualizar_usuario(
    in _id_usuario int,
    in _username varchar(50),
    in _email varchar(50),
    in _first_name varchar(50),
    in _last_name varchar(50),
    in _rol enum('admin', 'empleado', 'cajero'),
    in _activo boolean
)
begin
    update usuarios
    set username = _username,
        email = _email,
        first_name = _first_name,
        last_name = _last_name,
        rol = _rol,
        activo = _activo
    where id_usuario = _id_usuario;
end $$

create procedure sp_cambiar_password(in _id_usuario int, in _password_hash varchar(255)
)
begin
    update usuarios
    set password_hash = _password_hash
    where id_usuario = _id_usuario;
end $$

create procedure sp_desactivar_usuario(in _id_usuario int)
begin
    update usuarios
    set activo = false
    where id_usuario = _id_usuario;
end $$

-- eliminación física (física y definitiva)
create procedure sp_eliminar_usuario(in _id_usuario int)
begin
    delete from usuarios
    where id_usuario = _id_usuario;
end $$

delimiter ;

-- ------------------------------------------------------------------
-- PROCEDIMIENTO ALMACENADO PARA DESCONTAR STOCK

DROP PROCEDURE IF EXISTS sp_descontar_stock;
DELIMITER //

CREATE PROCEDURE sp_descontar_stock(
    IN _isbn VARCHAR(20),
    IN _cantidad INT
)
BEGIN
    -- Declaramos las variables necesarias
    DECLARE v_stock_actual INT;
    DECLARE v_existe INT;

    -- La cantidad debe ser positiva; evita descontar 0 o stock "negativo"
    IF _cantidad <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La cantidad debe ser mayor que cero.';
    END IF;

    -- Verificamos si el libro existe
    SELECT COUNT(*) INTO v_existe
    FROM libros
    WHERE isbn = _isbn;

    IF v_existe = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Libro no encontrado.';
    END IF;

    -- Obtenemos el stock actual del libro
    SELECT stock INTO v_stock_actual
    FROM libros
    WHERE isbn = _isbn;

    -- Verificamos si hay suficiente stock
    IF v_stock_actual >= _cantidad THEN
        -- Actualizamos restando la cantidad
        UPDATE libros
        SET stock = stock - _cantidad
        WHERE isbn = _isbn;
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock insuficiente.';
    END IF;

END //

DELIMITER ;

-- ============================================================================
-- PROCEDIMIENTO PARA GENERAR LA FACTURA (fusion venta + detalle + cliente + libro + usuario)
-- ============================================================================
drop procedure if exists sp_buscar_factura;
delimiter $$

create procedure sp_buscar_factura(in _no_venta int)
begin
    select
        v.no_venta as numero_factura,
        v.fecha_venta as fecha_emision,
        c.cui as cui_cliente,
        concat(c.nombre_cliente, ' ', c.apellido_cliente) as nombre_cliente,
        c.correo_electronico as correo_cliente,
        l.isbn as isbn_libro,
        l.titulo as titulo_libro,
        dv.cantidad as cantidad,
        dv.precio_unitario as precio_unitario,
        (dv.cantidad * dv.precio_unitario) as subtotal,
        concat(u.first_name, ' ', u.last_name) as usuario_atendio,
        v.total_venta as gran_total
    from ventas v
    inner join clientes c on v.cui_cliente = c.cui
    inner join detalle_venta dv on v.no_venta = dv.no_venta
    inner join libros l on dv.isbn = l.isbn
    inner join usuarios u on v.id_usuario = u.id_usuario
    where v.no_venta = _no_venta;
end $$

delimiter ;

SET FOREIGN_KEY_CHECKS=1;
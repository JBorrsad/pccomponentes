CREATE DATABASE tiendaElectronica;
USE tiendaElectronica;
-- DROP DATABASE tiendaElectronica;

CREATE TABLE USUARIOS (
                          ID INT PRIMARY KEY AUTO_INCREMENT,
                          NOMBRE varchar(30),
                          CONTRASEÑA varchar(20),
                          ROL ENUM ("PROVEEDOR", "CLIENTE"),
                          PAGONOMBRE varchar(30),
                          PAGOAPELLIDO varchar(30),
                          CP NUMERIC,
                          PROVINCIA varchar(50),
                          LOCALIDAD varchar(50),
                          DIRECCION varchar(100),
                          CUENTABANCO VARCHAR(24)
                          
				);





CREATE TABLE PRODUCTOS (
                           ID INT PRIMARY KEY AUTO_INCREMENT,
                           NOMBRE VARCHAR(50),
                           STOCK INT,
                           PRECIO DOUBLE
                           
);


CREATE TABLE PEDIDOS (

		ID INT PRIMARY KEY AUTO_INCREMENT,
		PEDIDO VARCHAR(255) NOT NULL,
		NOMBRE varchar(30),
		APELLIDO varchar(30),
		DIRECCION varchar(100),
		 CUENTABANCO VARCHAR(24)

);


INSERT INTO USUARIOS (ID, NOMBRE, CONTRASEÑA, ROL) VALUES
                                                       (1, '1', '1', 'PROVEEDOR'),
                                                        (2, '2', '2', 'CLIENTE');
                                                    


INSERT INTO PRODUCTOS (ID, NOMBRE, STOCK, PRECIO) VALUES
                                                      (1, 'Ordenador', 50, 899.99),
                                                      (2, 'Auriculares', 30, 49.99),
                                                      (3, 'Teclado', 20, 29.99),
                                                      (4, 'Ratón', 40, 19.99),
                                                      (5, 'Silla gaming', 10, 199.99),
                                                      (6, 'Monitor', 15, 159.99),
                                                      (7, 'Disco duro externo', 25, 79.99),
                                                      (8, 'Memoria RAM', 35, 69.99),
                                                      (9, 'Impresora', 5, 149.99),
                                                      (10, 'Tarjeta gráfica', 8, 299.99),
                                                      (11, 'Webcam', 12, 69.99),
                                                      (12, 'Altavoces', 20, 39.99),
                                                      (13, 'Micrófono', 18, 29.99),
                                                      (14, 'Router', 22, 49.99),
                                                      (15, 'Switch', 15, 29.99),
                                                      (16, 'Cámara de vigilancia', 7, 99.99),
                                                      (17, 'Alfombrilla de ratón', 30, 9.99),
                                                      (18, 'Adaptador WiFi', 20, 19.99),
                                                      (19, 'Aire acondicionado', 3, 399.99),
                                                      (20, 'Cable HDMI', 40, 14.99);
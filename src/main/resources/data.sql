-- Categorias
INSERT INTO categorias (nombre, descripcion) VALUES
    ('Videojuegos', 'Juegos físicos y digitales para consolas y PC'),
    ('Consolas', 'Consolas de última generación y ediciones especiales'),
    ('Accesorios', 'Periféricos para gaming, controles, headsets'),
    ('Equipos', 'Equipos completos y setups para gaming y streaming');

-- Subcategorias
INSERT INTO subcategorias (nombre, descripcion, activa, categoria_id)
SELECT 'Controles Pro', 'Gamepads premium y competitivos', true, c.id
FROM categorias c WHERE c.nombre = 'Accesorios';

INSERT INTO subcategorias (nombre, descripcion, activa, categoria_id)
SELECT 'Audio Inalámbrico', 'Headsets y earbuds diseñados para gaming', true, c.id
FROM categorias c WHERE c.nombre = 'Accesorios';

INSERT INTO subcategorias (nombre, descripcion, activa, categoria_id)
SELECT 'Simulación y Racing', 'Volantes, pedales y accesorios de simulación', true, c.id
FROM categorias c WHERE c.nombre = 'Accesorios';

INSERT INTO subcategorias (nombre, descripcion, activa, categoria_id)
SELECT 'PC Gamer', 'Equipos de escritorio optimizados para juegos', true, c.id
FROM categorias c WHERE c.nombre = 'Equipos';

INSERT INTO subcategorias (nombre, descripcion, activa, categoria_id)
SELECT 'Laptops Gaming', 'Portátiles con tarjetas gráficas dedicadas', true, c.id
FROM categorias c WHERE c.nombre = 'Equipos';

INSERT INTO subcategorias (nombre, descripcion, activa, categoria_id)
SELECT 'Setups Streaming', 'Combos con capturadoras, iluminación y accesorios', true, c.id
FROM categorias c WHERE c.nombre = 'Equipos';

-- Productos
INSERT INTO productos (nombre, descripcion, precio, stock, tipo, categoria_id, subcategoria_id)
SELECT 'The Legend of Zelda: Tears of the Kingdom', 'Edición estándar para Nintendo Switch', 69.99, 25, 'VIDEOJUEGO', c.id, NULL
FROM categorias c WHERE c.nombre = 'Videojuegos';

INSERT INTO productos (nombre, descripcion, precio, stock, tipo, categoria_id, subcategoria_id)
SELECT 'PlayStation 5 Slim', 'Consola PS5 con unidad de discos', 499.00, 10, 'CONSOLA', c.id, NULL
FROM categorias c WHERE c.nombre = 'Consolas';

INSERT INTO productos (nombre, descripcion, precio, stock, tipo, categoria_id, subcategoria_id)
SELECT 'Xbox Elite Wireless Controller Series 2', 'Control inalámbrico personalizable', 179.99, 18, 'ACCESORIO', c.id, sc.id
FROM categorias c
JOIN subcategorias sc ON sc.nombre = 'Controles Pro' AND sc.categoria_id = c.id
WHERE c.nombre = 'Accesorios';

INSERT INTO productos (nombre, descripcion, precio, stock, tipo, categoria_id, subcategoria_id)
SELECT 'NVIDIA GeForce RTX 4070 Super', 'Tarjeta gráfica 12GB GDDR6X', 699.00, 7, 'EQUIPO', c.id, sc.id
FROM categorias c
JOIN subcategorias sc ON sc.nombre = 'PC Gamer' AND sc.categoria_id = c.id
WHERE c.nombre = 'Equipos';

INSERT INTO productos (nombre, descripcion, precio, stock, tipo, categoria_id, subcategoria_id)
SELECT 'Lenovo Legion Pro 7', 'Laptop gaming con RTX 4080', 2499.00, 4, 'EQUIPO', c.id, sc.id
FROM categorias c
JOIN subcategorias sc ON sc.nombre = 'Laptops Gaming' AND sc.categoria_id = c.id
WHERE c.nombre = 'Equipos';

INSERT INTO productos (nombre, descripcion, precio, stock, tipo, categoria_id, subcategoria_id)
SELECT 'Kit Streaming 4K', 'Incluye capturadora, micrófono XLR y paneles LED', 1199.00, 6, 'EQUIPO', c.id, sc.id
FROM categorias c
JOIN subcategorias sc ON sc.nombre = 'Setups Streaming' AND sc.categoria_id = c.id
WHERE c.nombre = 'Equipos';

-- Usamos {noop} para indicar que es texto plano (solo para desarrollo)
INSERT INTO usuarios (id, nombre, email, password, rol)
VALUES (1, 'Admin', 'admin@gamestore.com', '{noop}1234', 'ADMIN');

INSERT INTO usuarios (id, nombre, email, password, rol)
VALUES (2, 'Cliente', 'cliente@gamestore.com', '{noop}1234', 'CLIENTE');

ALTER TABLE usuarios ALTER COLUMN id RESTART WITH 10;
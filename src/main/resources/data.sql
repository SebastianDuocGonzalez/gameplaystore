-- ==========================================
-- 1. CATEGORIAS
-- Usamos SELECT ... WHERE NOT EXISTS para evitar duplicados por nombre
-- ==========================================

INSERT INTO categorias (nombre, descripcion)
SELECT 'Videojuegos', 'Juegos físicos y digitales para consolas y PC'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Videojuegos');

INSERT INTO categorias (nombre, descripcion)
SELECT 'Consolas', 'Consolas de última generación y ediciones especiales'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Consolas');

INSERT INTO categorias (nombre, descripcion)
SELECT 'Accesorios', 'Periféricos para gaming, controles, headsets'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Accesorios');

INSERT INTO categorias (nombre, descripcion)
SELECT 'Equipos', 'Equipos completos y setups para gaming y streaming'
WHERE NOT EXISTS (SELECT 1 FROM categorias WHERE nombre = 'Equipos');
-- ==========================================
-- 2. SUBCATEGORIAS
-- Agregamos la validación AND NOT EXISTS en tus consultas
-- ==========================================

INSERT INTO subcategorias (nombre, descripcion, activa, categoria_id)
SELECT 'Controles Pro', 'Gamepads premium y competitivos', true, c.id
FROM categorias c 
WHERE c.nombre = 'Accesorios'
AND NOT EXISTS (SELECT 1 FROM subcategorias WHERE nombre = 'Controles Pro');

INSERT INTO subcategorias (nombre, descripcion, activa, categoria_id)
SELECT 'Audio Inalámbrico', 'Headsets y earbuds diseñados para gaming', true, c.id
FROM categorias c 
WHERE c.nombre = 'Accesorios'
AND NOT EXISTS (SELECT 1 FROM subcategorias WHERE nombre = 'Audio Inalámbrico');

INSERT INTO subcategorias (nombre, descripcion, activa, categoria_id)
SELECT 'Simulación y Racing', 'Volantes, pedales y accesorios de simulación', true, c.id
FROM categorias c 
WHERE c.nombre = 'Accesorios'
AND NOT EXISTS (SELECT 1 FROM subcategorias WHERE nombre = 'Simulación y Racing');

INSERT INTO subcategorias (nombre, descripcion, activa, categoria_id)
SELECT 'PC Gamer', 'Equipos de escritorio optimizados para juegos', true, c.id
FROM categorias c 
WHERE c.nombre = 'Equipos'
AND NOT EXISTS (SELECT 1 FROM subcategorias WHERE nombre = 'PC Gamer');

INSERT INTO subcategorias (nombre, descripcion, activa, categoria_id)
SELECT 'Laptops Gaming', 'Portátiles con tarjetas gráficas dedicadas', true, c.id
FROM categorias c 
WHERE c.nombre = 'Equipos'
AND NOT EXISTS (SELECT 1 FROM subcategorias WHERE nombre = 'Laptops Gaming');

INSERT INTO subcategorias (nombre, descripcion, activa, categoria_id)
SELECT 'Setups Streaming', 'Combos con capturadoras, iluminación y accesorios', true, c.id
FROM categorias c 
WHERE c.nombre = 'Equipos'
AND NOT EXISTS (SELECT 1 FROM subcategorias WHERE nombre = 'Setups Streaming');


-- ==========================================
-- 3. PRODUCTOS
-- Misma lógica: verificamos que el producto no exista por nombre antes de insertar
-- ==========================================

INSERT INTO productos (nombre, descripcion, precio, stock, tipo, categoria_id, subcategoria_id)
SELECT 'The Legend of Zelda: Tears of the Kingdom', 'Edición estándar para Nintendo Switch', 69.99, 25, 'VIDEOJUEGO', c.id, NULL
FROM categorias c 
WHERE c.nombre = 'Videojuegos'
AND NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'The Legend of Zelda: Tears of the Kingdom');

INSERT INTO productos (nombre, descripcion, precio, stock, tipo, categoria_id, subcategoria_id)
SELECT 'PlayStation 5 Slim', 'Consola PS5 con unidad de discos', 499.00, 10, 'CONSOLA', c.id, NULL
FROM categorias c 
WHERE c.nombre = 'Consolas'
AND NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'PlayStation 5 Slim');

INSERT INTO productos (nombre, descripcion, precio, stock, tipo, categoria_id, subcategoria_id)
SELECT 'Xbox Elite Wireless Controller Series 2', 'Control inalámbrico personalizable', 179.99, 18, 'ACCESORIO', c.id, sc.id
FROM categorias c
JOIN subcategorias sc ON sc.nombre = 'Controles Pro' AND sc.categoria_id = c.id
WHERE c.nombre = 'Accesorios'
AND NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Xbox Elite Wireless Controller Series 2');

INSERT INTO productos (nombre, descripcion, precio, stock, tipo, categoria_id, subcategoria_id)
SELECT 'NVIDIA GeForce RTX 4070 Super', 'Tarjeta gráfica 12GB GDDR6X', 699.00, 7, 'EQUIPO', c.id, sc.id
FROM categorias c
JOIN subcategorias sc ON sc.nombre = 'PC Gamer' AND sc.categoria_id = c.id
WHERE c.nombre = 'Equipos'
AND NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'NVIDIA GeForce RTX 4070 Super');

INSERT INTO productos (nombre, descripcion, precio, stock, tipo, categoria_id, subcategoria_id)
SELECT 'Lenovo Legion Pro 7', 'Laptop gaming con RTX 4080', 2499.00, 4, 'EQUIPO', c.id, sc.id
FROM categorias c
JOIN subcategorias sc ON sc.nombre = 'Laptops Gaming' AND sc.categoria_id = c.id
WHERE c.nombre = 'Equipos'
AND NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Lenovo Legion Pro 7');

INSERT INTO productos (nombre, descripcion, precio, stock, tipo, categoria_id, subcategoria_id)
SELECT 'Kit Streaming 4K', 'Incluye capturadora, micrófono XLR y paneles LED', 1199.00, 6, 'EQUIPO', c.id, sc.id
FROM categorias c
JOIN subcategorias sc ON sc.nombre = 'Setups Streaming' AND sc.categoria_id = c.id
WHERE c.nombre = 'Equipos'
AND NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Kit Streaming 4K');


-- ==========================================
-- 4. USUARIOS
-- Aquí SI funciona tu ON CONFLICT (id) porque tú especificas el ID manualmente
-- ==========================================

INSERT INTO usuarios (id, nombre, email, password, rol)
VALUES (1, 'Admin', 'admin@gamestore.com', '{noop}1234', 'ADMIN')
ON CONFLICT (id) DO NOTHING;

INSERT INTO usuarios (id, nombre, email, password, rol)
VALUES (2, 'Cliente', 'cliente@gamestore.com', '{noop}1234', 'CLIENTE')
ON CONFLICT (id) DO NOTHING;

-- Actualizamos la secuencia para evitar errores al crear nuevos usuarios
SELECT setval('usuarios_id_seq', (SELECT MAX(id) FROM usuarios));
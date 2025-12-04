-- =================================================================================
-- SCRIPT DE DATOS INICIALES (SEED DATA)
-- Compatible con: PostgreSQL 14, 15, 16, 17+
-- =================================================================================

BEGIN; -- Iniciar transacción para asegurar integridad atómica

-- ==========================================
-- 1. CATEGORIAS
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
-- Se agregó la columna 'imagen' para evitar problemas de carga en el frontend
-- ==========================================

-- Producto 1
INSERT INTO productos (nombre, descripcion, precio, stock, tipo, imagen, categoria_id, subcategoria_id)
SELECT 'The Legend of Zelda: Tears of the Kingdom', 'Edición estándar para Nintendo Switch', 69.99, 25, 'VIDEOJUEGO', 'https://images.unsplash.com/photo-1599557297054-05183842c6c4?auto=format&fit=crop&w=400&q=80', c.id, NULL
FROM categorias c 
WHERE c.nombre = 'Videojuegos'
AND NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'The Legend of Zelda: Tears of the Kingdom');

-- Producto 2
INSERT INTO productos (nombre, descripcion, precio, stock, tipo, imagen, categoria_id, subcategoria_id)
SELECT 'PlayStation 5 Slim', 'Consola PS5 con unidad de discos', 499.00, 10, 'CONSOLA', 'https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?auto=format&fit=crop&w=400&q=80', c.id, NULL
FROM categorias c 
WHERE c.nombre = 'Consolas'
AND NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'PlayStation 5 Slim');

-- Producto 3
INSERT INTO productos (nombre, descripcion, precio, stock, tipo, imagen, categoria_id, subcategoria_id)
SELECT 'Xbox Elite Wireless Controller Series 2', 'Control inalámbrico personalizable', 179.99, 18, 'ACCESORIO', 'https://images.unsplash.com/photo-1600080972464-8e5f35f63d88?auto=format&fit=crop&w=400&q=80', c.id, sc.id
FROM categorias c
JOIN subcategorias sc ON sc.nombre = 'Controles Pro' AND sc.categoria_id = c.id
WHERE c.nombre = 'Accesorios'
AND NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Xbox Elite Wireless Controller Series 2');

-- Producto 4
INSERT INTO productos (nombre, descripcion, precio, stock, tipo, imagen, categoria_id, subcategoria_id)
SELECT 'NVIDIA GeForce RTX 4070 Super', 'Tarjeta gráfica 12GB GDDR6X', 699.00, 7, 'EQUIPO', 'https://images.unsplash.com/photo-1591488320449-011701bb6704?auto=format&fit=crop&w=400&q=80', c.id, sc.id
FROM categorias c
JOIN subcategorias sc ON sc.nombre = 'PC Gamer' AND sc.categoria_id = c.id
WHERE c.nombre = 'Equipos'
AND NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'NVIDIA GeForce RTX 4070 Super');

-- Producto 5
INSERT INTO productos (nombre, descripcion, precio, stock, tipo, imagen, categoria_id, subcategoria_id)
SELECT 'Lenovo Legion Pro 7', 'Laptop gaming con RTX 4080', 2499.00, 4, 'EQUIPO', 'https://images.unsplash.com/photo-1603302576837-37561b2e2302?auto=format&fit=crop&w=400&q=80', c.id, sc.id
FROM categorias c
JOIN subcategorias sc ON sc.nombre = 'Laptops Gaming' AND sc.categoria_id = c.id
WHERE c.nombre = 'Equipos'
AND NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Lenovo Legion Pro 7');

-- Producto 6
INSERT INTO productos (nombre, descripcion, precio, stock, tipo, imagen, categoria_id, subcategoria_id)
SELECT 'Kit Streaming 4K', 'Incluye capturadora, micrófono XLR y paneles LED', 1199.00, 6, 'EQUIPO', 'https://images.unsplash.com/photo-1522202176988-66273c2fd55f?auto=format&fit=crop&w=400&q=80', c.id, sc.id
FROM categorias c
JOIN subcategorias sc ON sc.nombre = 'Setups Streaming' AND sc.categoria_id = c.id
WHERE c.nombre = 'Equipos'
AND NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Kit Streaming 4K');

-- ==========================================
-- 4. USUARIOS
-- Nota: La contraseña '{noop}1234' funciona con DelegatingPasswordEncoder
-- ==========================================

INSERT INTO usuarios (id, nombre, email, password, rol)
VALUES (1, 'Admin', 'admin@gamestore.com', '{noop}1234', 'ADMIN')
ON CONFLICT (id) DO NOTHING;

INSERT INTO usuarios (id, nombre, email, password, rol)
VALUES (2, 'Cliente', 'cliente@gamestore.com', '{noop}1234', 'CLIENTE')
ON CONFLICT (id) DO NOTHING;

INSERT INTO usuarios (id, nombre, email, password, rol)
VALUES (3, 'Vendedor', 'vendedor@gamestore.com', '{noop}1234', 'TRABAJADOR')
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 6. ORDENES (Datos de Ejemplo)
-- Creamos historial para el usuario CLIENTE (ID 2)
-- ==========================================

-- Orden 1: Compra grande (PS5 + Juego)
INSERT INTO ordenes (id, fecha, total, user_id)
VALUES (1, '2025-01-15 10:30:00', 568.99, 2)
ON CONFLICT (id) DO NOTHING;

-- Orden 2: Compra accesorio (Control Xbox)
INSERT INTO ordenes (id, fecha, total, user_id)
VALUES (2, '2025-02-01 15:45:00', 179.99, 2)
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 7. DETALLES DE ORDEN
-- Vinculamos productos a las órdenes
-- ==========================================

-- Detalles Orden 1
INSERT INTO detalles_orden (id, orden_id, producto_id, nombre_producto, cantidad, precio_unitario, subtotal)
VALUES (1, 1, 2, 'PlayStation 5 Slim', 1, 499.00, 499.00)
ON CONFLICT (id) DO NOTHING;

INSERT INTO detalles_orden (id, orden_id, producto_id, nombre_producto, cantidad, precio_unitario, subtotal)
VALUES (2, 1, 1, 'The Legend of Zelda: Tears of the Kingdom', 1, 69.99, 69.99)
ON CONFLICT (id) DO NOTHING;

-- Detalles Orden 2
INSERT INTO detalles_orden (id, orden_id, producto_id, nombre_producto, cantidad, precio_unitario, subtotal)
VALUES (3, 2, 3, 'Xbox Elite Wireless Controller Series 2', 1, 179.99, 179.99)
ON CONFLICT (id) DO NOTHING;

-- ==========================================
-- 5. AJUSTE DE SECUENCIAS (CRÍTICO PARA POSTGRESQL)
-- PostgreSQL no actualiza automáticamente la secuencia si insertamos IDs manuales.
-- Esto previene el error "Duplicate Key" al crear el siguiente registro.
-- ==========================================

-- Ajustar secuencia de usuarios (porque insertamos id 1, 2, 3 manualmente)
SELECT setval(pg_get_serial_sequence('usuarios', 'id'), COALESCE((SELECT MAX(id) FROM usuarios), 1));

-- Ajustar secuencia de productos
SELECT setval(pg_get_serial_sequence('productos', 'id'), COALESCE((SELECT MAX(id) FROM productos), 1));

-- Ajustar secuencia de categorias
SELECT setval(pg_get_serial_sequence('categorias', 'id'), COALESCE((SELECT MAX(id) FROM categorias), 1));

-- Ajustar secuencia de subcategorias
SELECT setval(pg_get_serial_sequence('subcategorias', 'id'), COALESCE((SELECT MAX(id) FROM subcategorias), 1));

-- Ajustar secuencia de ordenes
SELECT setval(pg_get_serial_sequence('ordenes', 'id'), COALESCE((SELECT MAX(id) FROM ordenes), 1));

-- Ajustar secuencia de detalles_orden
SELECT setval(pg_get_serial_sequence('detalles_orden', 'id'), COALESCE((SELECT MAX(id) FROM detalles_orden), 1));

COMMIT; -- Confirmar cambios
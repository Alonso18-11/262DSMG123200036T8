-- =============================================================
--  Codelab: Cómo usar SQL para leer y escribir en una base de datos
--  Ejecuta cada consulta en el Database Inspector de Android Studio
--  (App Inspection > Database Inspector > email > Open New Query Tab)
--  o en DB Browser for SQLite con el archivo email.db.
-- =============================================================

-- ---------- 1. SELECT básico ----------
SELECT * FROM email;                       -- todas las columnas y filas
SELECT subject FROM email;                 -- una sola columna
SELECT subject, sender FROM email;         -- varias columnas

-- ---------- 2. Funciones de agregación ----------
SELECT COUNT(*) FROM email;                -- cuántos correos hay
SELECT MAX(received) FROM email;           -- el correo más reciente (tiempo Unix)
SELECT MIN(received) FROM email;           -- el más antiguo

-- ---------- 3. DISTINCT ----------
SELECT DISTINCT sender FROM email;         -- remitentes sin repetir
SELECT COUNT(DISTINCT sender) FROM email;  -- cuántos remitentes distintos

-- ---------- 4. WHERE (filtrar) ----------
SELECT * FROM email WHERE folder = 'inbox';
SELECT * FROM email WHERE folder = 'inbox' AND read = false;   -- no leídos de la bandeja
SELECT * FROM email WHERE folder = 'trash' OR read = false;    -- papelera o no leídos
SELECT * FROM email WHERE starred = true;

-- ---------- 5. LIKE (buscar texto) ----------
SELECT * FROM email WHERE subject LIKE '%fool%';   -- contiene "fool"
SELECT * FROM email WHERE subject LIKE 'fool%';    -- empieza con "fool"
SELECT * FROM email WHERE subject LIKE '%fool';    -- termina con "fool"
SELECT COUNT(*) FROM email WHERE subject LIKE '%fool%';
SELECT DISTINCT sender FROM email WHERE sender LIKE 'h%';

-- ---------- 6. GROUP BY ----------
SELECT folder, COUNT(*) FROM email GROUP BY folder;   -- correos por carpeta

-- ---------- 7. ORDER BY ----------
SELECT * FROM email ORDER BY received DESC;           -- más recientes primero
SELECT * FROM email WHERE folder = 'inbox' AND subject LIKE '%fool%' ORDER BY received DESC;

-- ---------- 8. LIMIT y OFFSET (paginación) ----------
SELECT * FROM email WHERE folder = 'inbox' ORDER BY received DESC LIMIT 10;            -- página 1
SELECT * FROM email WHERE folder = 'inbox' ORDER BY received DESC LIMIT 10 OFFSET 10;  -- página 2

-- ---------- 9. Fechas legibles (extra) ----------
SELECT subject, strftime('%d/%m/%Y %H:%M', received, 'unixepoch') AS fecha
FROM email ORDER BY received DESC LIMIT 5;

-- ---------- 10. INSERT ----------
-- id NULL => SQLite asigna el siguiente id automáticamente
INSERT INTO email
VALUES (NULL, 'Lorem ipsum dolor sit amet', 'sender@example.com', 'inbox', false, false, strftime('%s', 'now'));

SELECT * FROM email WHERE sender = 'sender@example.com';   -- comprobar la inserción

-- ---------- 11. UPDATE ----------
UPDATE email SET read = true WHERE subject = 'Lorem ipsum dolor sit amet';
SELECT read FROM email WHERE subject = 'Lorem ipsum dolor sit amet';

-- ---------- 12. DELETE ----------
DELETE FROM email WHERE subject = 'Lorem ipsum dolor sit amet';
SELECT * FROM email WHERE subject = 'Lorem ipsum dolor sit amet';  -- ya no devuelve filas

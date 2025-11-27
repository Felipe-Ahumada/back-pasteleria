-- Crear base de datos para Pastelería Mil Sabores
CREATE DATABASE IF NOT EXISTS pasteleria_db;
USE pasteleria_db;

-- Las tablas se crearán automáticamente mediante Hibernate (spring.jpa.hibernate.ddl-auto=update)
-- Este script solo asegura que la base de datos exista

-- Mensaje de confirmación
SELECT 'Base de datos pasteleria_db creada exitosamente' AS mensaje;

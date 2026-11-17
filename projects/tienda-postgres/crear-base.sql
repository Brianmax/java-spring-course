-- Ejecutar conectado a una base administrativa, por ejemplo postgres.
-- PostgreSQL no permite CREATE DATABASE dentro de una transaccion.
-- Falla si tienda ya existe; no borra ni reemplaza ninguna base.
CREATE DATABASE tienda WITH ENCODING 'UTF8';

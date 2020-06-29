CREATE TABLE "marca" (
  "id_marca" INT PRIMARY KEY NOT NULL,
  "descripcion" VARCHAR(30)
);

CREATE TABLE "rango_cliente" (
  "id_rango" INT PRIMARY KEY NOT NULL,
  "descripcion" VARCHAR(50)
);

CREATE TABLE "tipo_contrato" (
  "id_tipo" INT PRIMARY KEY NOT NULL,
  "descripcion" VARCHAR(50)
);

CREATE TABLE "Persona" (
  "id_persona" INT PRIMARY KEY NOT NULL,
  "nombre" VARCHAR(100),
  "dui" VARCHAR(10),
  "genero" VARCHAR(1)
);

CREATE TABLE "carro" (
  "id_carro" INT PRIMARY KEY NOT NULL,
  "id_marca" INT NOT NULL,
  "precio" BIGINT,
  "color" CHAR(30)
);

CREATE TABLE "cliente" (
  "id_cliente" INT PRIMARY KEY NOT NULL,
  "id_persona" INT NOT NULL,
  "id_rango" INT NOT NULL,
  "lista_negra" VARCHAR(1)
);

CREATE TABLE "Vendedor" (
  "id_vendedor" INT PRIMARY KEY NOT NULL,
  "id_persona" INT NOT NULL,
  "sueldo" BIGINT,
  "genero" VARCHAR(1)
);

CREATE TABLE "contrato" (
  "id_contrato" INT PRIMARY KEY NOT NULL,
  "id_carro" INT NOT NULL,
  "id_cliente" INT NOT NULL,
  "id_vendedor" INT NOT NULL,
  "costo" BIGINT,
  "id_tipo" INT NOT NULL
);

ALTER TABLE "carro" ADD FOREIGN KEY ("id_marca") REFERENCES "marca" ("id_marca");

ALTER TABLE "cliente" ADD FOREIGN KEY ("id_persona") REFERENCES "Persona" ("id_persona");

ALTER TABLE "cliente" ADD FOREIGN KEY ("id_rango") REFERENCES "rango_cliente" ("id_rango");

ALTER TABLE "Vendedor" ADD FOREIGN KEY ("id_persona") REFERENCES "Persona" ("id_persona");

ALTER TABLE "contrato" ADD FOREIGN KEY ("id_carro") REFERENCES "carro" ("id_carro");

ALTER TABLE "contrato" ADD FOREIGN KEY ("id_cliente") REFERENCES "cliente" ("id_cliente");

ALTER TABLE "contrato" ADD FOREIGN KEY ("id_vendedor") REFERENCES "Vendedor" ("id_vendedor");

ALTER TABLE "contrato" ADD FOREIGN KEY ("id_tipo") REFERENCES "tipo_contrato" ("id_tipo");
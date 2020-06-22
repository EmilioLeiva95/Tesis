CREATE TABLE "marca" (
  "id_marca" SERIAL PRIMARY KEY NOT NULL,
  "descripcion" VARCHAR(30)
);

CREATE TABLE "rango_cliente" (
  "id_rango" SERIAL PRIMARY KEY NOT NULL,
  "descripcion" VARCHAR(50)
);

CREATE TABLE "tipo_contrato" (
  "id_tipo" SERIAL PRIMARY KEY NOT NULL,
  "descripcion" VARCHAR(50)
);

CREATE TABLE "Persona" (
  "id_persona" SERIAL PRIMARY KEY NOT NULL,
  "nombre" VARCHAR(100),
  "dui" VARCHAR(10),
  "genero" VARCHAR(1)
);

CREATE TABLE "carro" (
  "id_carro" SERIAL PRIMARY KEY NOT NULL,
  "id_marca" INT NOT NULL,
  "precio" LONG,
  "color" VARCHAR(30)
);

CREATE TABLE "cliente" (
  "id_cliente" SERIAL PRIMARY KEY NOT NULL,
  "id_persona" INT NOT NULL,
  "id_rango" INT NOT NULL,
  "lista_negra" VAR(1)
);

CREATE TABLE "Vendedor" (
  "id_vendedor" SERIAL PRIMARY KEY NOT NULL,
  "id_persona" int NOT NULL,
  "sueldo" LONG,
  "genero" VAR(1)
);

CREATE TABLE "contrato" (
  "id_contrato" SERIAL PRIMARY KEY NOT NULL,
  "id_carro" INT NOT NULL,
  "id_cliente" INT NOT NULL,
  "id_vendedor" INT NOT NULL,
  "costo" LONG,
  "id_tipo" INT NOT NULL
);

ALTER TABLE "marca" ADD FOREIGN KEY ("id_marca") REFERENCES "carro" ("id_marca");

ALTER TABLE "Persona" ADD FOREIGN KEY ("id_persona") REFERENCES "cliente" ("id_persona");

ALTER TABLE "rango_cliente" ADD FOREIGN KEY ("id_rango") REFERENCES "cliente" ("id_rango");

ALTER TABLE "Persona" ADD FOREIGN KEY ("id_persona") REFERENCES "Vendedor" ("id_persona");

ALTER TABLE "carro" ADD FOREIGN KEY ("id_carro") REFERENCES "contrato" ("id_carro");

ALTER TABLE "cliente" ADD FOREIGN KEY ("id_cliente") REFERENCES "contrato" ("id_cliente");

ALTER TABLE "Vendedor" ADD FOREIGN KEY ("id_vendedor") REFERENCES "contrato" ("id_vendedor");

ALTER TABLE "tipo_contrato" ADD FOREIGN KEY ("id_tipo") REFERENCES "contrato" ("id_tipo");

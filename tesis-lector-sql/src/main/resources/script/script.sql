CREATE TABLE "marca" (
  "id_marca" SERIAL PRIMARY KEY NOT NULL,
  "descripcion" varchar(30)
);

CREATE TABLE "rango_cliente" (
  "id_rango" SERIAL PRIMARY KEY NOT NULL,
  "descripcion" varchar(50)
);

CREATE TABLE "tipo_contrato" (
  "id_tipo" SERIAL PRIMARY KEY NOT NULL,
  "descripcion" varchar(50)
);

CREATE TABLE "Persona" (
  "id_persona" SERIAL PRIMARY KEY NOT NULL,
  "nombre" varchar(100),
  "dui" varchar(10),
  "genero" varchar(1)
);

CREATE TABLE "carro" (
  "id_carro" SERIAL PRIMARY KEY NOT NULL,
  "id_marca" int NOT NULL,
  "precio" long,
  "color" varchar(30)
);

CREATE TABLE "cliente" (
  "id_cliente" SERIAL PRIMARY KEY NOT NULL,
  "id_persona" int NOT NULL,
  "id_rango" int NOT NULL,
  "lista_negra" var(1)
);

CREATE TABLE "Vendedor" (
  "id_vendedor" SERIAL PRIMARY KEY NOT NULL,
  "id_persona" int NOT NULL,
  "sueldo" long,
  "genero" var(1)
);

CREATE TABLE "contrato" (
  "id_contrato" SERIAL PRIMARY KEY NOT NULL,
  "id_carro" int NOT NULL,
  "id_cliente" int NOT NULL,
  "id_vendedor" int NOT NULL,
  "costo" long,
  "id_tipo" int NOT NULL
);

ALTER TABLE "marca" ADD FOREIGN KEY ("id_marca") REFERENCES "carro" ("id_marca");

ALTER TABLE "Persona" ADD FOREIGN KEY ("id_persona") REFERENCES "cliente" ("id_persona");

ALTER TABLE "rango_cliente" ADD FOREIGN KEY ("id_rango") REFERENCES "cliente" ("id_rango");

ALTER TABLE "Persona" ADD FOREIGN KEY ("id_persona") REFERENCES "Vendedor" ("id_persona");

ALTER TABLE "carro" ADD FOREIGN KEY ("id_carro") REFERENCES "contrato" ("id_carro");

ALTER TABLE "cliente" ADD FOREIGN KEY ("id_cliente") REFERENCES "contrato" ("id_cliente");

ALTER TABLE "Vendedor" ADD FOREIGN KEY ("id_vendedor") REFERENCES "contrato" ("id_vendedor");

ALTER TABLE "tipo_contrato" ADD FOREIGN KEY ("id_tipo") REFERENCES "contrato" ("id_tipo");

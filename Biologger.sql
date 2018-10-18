--
-- PostgreSQL database dump
--

-- Dumped from database version 10.5
-- Dumped by pg_dump version 10.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: categoria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categoria (
    idcategoria integer NOT NULL,
    nombre character varying(50) NOT NULL,
    subcategoria boolean NOT NULL,
    idcatprincipal integer
);


ALTER TABLE public.categoria OWNER TO postgres;

--
-- Name: categoria_idcategoria_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.categoria_idcategoria_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.categoria_idcategoria_seq OWNER TO postgres;

--
-- Name: categoria_idcategoria_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.categoria_idcategoria_seq OWNED BY public.categoria.idcategoria;


--
-- Name: confirmacion_idconfirmacion_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.confirmacion_idconfirmacion_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.confirmacion_idconfirmacion_seq OWNER TO postgres;

--
-- Name: codigo_confirmacion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.codigo_confirmacion (
    codigo character varying NOT NULL,
    fecha_creacion timestamp without time zone NOT NULL,
    idusuario integer,
    idconfirmacion integer DEFAULT nextval('public.confirmacion_idconfirmacion_seq'::regclass) NOT NULL
);


ALTER TABLE public.codigo_confirmacion OWNER TO postgres;

--
-- Name: kit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kit (
    idkit integer NOT NULL,
    materiales integer[] NOT NULL,
    fechavencimiento timestamp without time zone NOT NULL,
    idusuario integer NOT NULL
);


ALTER TABLE public.kit OWNER TO postgres;

--
-- Name: kit_idkit_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.kit_idkit_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.kit_idkit_seq OWNER TO postgres;

--
-- Name: kit_idkit_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.kit_idkit_seq OWNED BY public.kit.idkit;


--
-- Name: kit_idusuario_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.kit_idusuario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.kit_idusuario_seq OWNER TO postgres;

--
-- Name: kit_idusuario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.kit_idusuario_seq OWNED BY public.kit.idusuario;


--
-- Name: material; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.material (
    idmaterial integer NOT NULL,
    nombre character varying(50) NOT NULL,
    foto bytea NOT NULL,
    categoria integer[] NOT NULL,
    subcategoria integer[] NOT NULL,
    descripcion text NOT NULL,
    estado boolean NOT NULL,
    idusuario integer NOT NULL
);


ALTER TABLE public.material OWNER TO postgres;

--
-- Name: material_idmaterial_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.material_idmaterial_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.material_idmaterial_seq OWNER TO postgres;

--
-- Name: material_idmaterial_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.material_idmaterial_seq OWNED BY public.material.idmaterial;


--
-- Name: profesor_idvalidacion_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.profesor_idvalidacion_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.profesor_idvalidacion_seq OWNER TO postgres;

--
-- Name: profesor_validacion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.profesor_validacion (
    id_profesor_validacion integer DEFAULT nextval('public.profesor_idvalidacion_seq'::regclass) NOT NULL,
    numero_trabajador character varying(50) NOT NULL,
    idusuario integer
);


ALTER TABLE public.profesor_validacion OWNER TO postgres;

--
-- Name: rol_rol_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.rol_rol_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rol_rol_id_seq OWNER TO postgres;

--
-- Name: rol; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rol (
    rol_id integer DEFAULT nextval('public.rol_rol_id_seq'::regclass) NOT NULL,
    rol character varying(50) NOT NULL
);


ALTER TABLE public.rol OWNER TO postgres;

--
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    idusuario integer NOT NULL,
    correo character varying(50) NOT NULL,
    nombre character varying(50) NOT NULL,
    foto bytea,
    nombreusuario character varying(50) NOT NULL,
    ultima_actualizacion timestamp without time zone,
    fecha_registro timestamp without time zone,
    ultimo_acceso timestamp without time zone,
    activo boolean DEFAULT true NOT NULL,
    contrasena character varying,
    rolid integer DEFAULT 2 NOT NULL
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- Name: usuario_idusuario_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usuario_idusuario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.usuario_idusuario_seq OWNER TO postgres;

--
-- Name: usuario_idusuario_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usuario_idusuario_seq OWNED BY public.usuario.idusuario;


--
-- Name: categoria idcategoria; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria ALTER COLUMN idcategoria SET DEFAULT nextval('public.categoria_idcategoria_seq'::regclass);


--
-- Name: kit idkit; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kit ALTER COLUMN idkit SET DEFAULT nextval('public.kit_idkit_seq'::regclass);


--
-- Name: kit idusuario; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kit ALTER COLUMN idusuario SET DEFAULT nextval('public.kit_idusuario_seq'::regclass);


--
-- Name: material idmaterial; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.material ALTER COLUMN idmaterial SET DEFAULT nextval('public.material_idmaterial_seq'::regclass);


--
-- Name: usuario idusuario; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario ALTER COLUMN idusuario SET DEFAULT nextval('public.usuario_idusuario_seq'::regclass);


--
-- Data for Name: categoria; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.categoria (idcategoria, nombre, subcategoria, idcatprincipal) FROM stdin;
\.


--
-- Data for Name: codigo_confirmacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.codigo_confirmacion (codigo, fecha_creacion, idusuario, idconfirmacion) FROM stdin;
$2a$10$bzt.Zad41e1CVa2hfuCdZeSZk3AE/WbbiNJSZlSYl2vhxV.VrfbX6	2018-10-12 01:16:06.371	1	1
$2a$10$Tftsb9FRm7jk2nPqeOKAX.4Po1UpmeMq3fug42e7urvAoctSmecau	2018-10-17 02:27:51.044	2	2
$2a$10$uEmv7sLdzbNGKtj6wu.A0.EcP/d2rviY1ft5T1.ArbnN5EAIRhVtq	2018-10-17 17:36:47.519	3	3
$2a$10$wk6zdi3YNFi.CtA41qJuiuOg5xp3KAYo8ilhxLeoVMnH/XXr/44u6	2018-10-17 17:43:19.331	4	4
\.


--
-- Data for Name: kit; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.kit (idkit, materiales, fechavencimiento, idusuario) FROM stdin;
\.


--
-- Data for Name: material; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.material (idmaterial, nombre, foto, categoria, subcategoria, descripcion, estado, idusuario) FROM stdin;
\.


--
-- Data for Name: profesor_validacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.profesor_validacion (id_profesor_validacion, numero_trabajador, idusuario) FROM stdin;
1	304283079	2
2	1234	4
\.


--
-- Data for Name: rol; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rol (rol_id, rol) FROM stdin;
1	Administrador
2	Profesor
3	Estudiante
\.


--
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuario (idusuario, correo, nombre, foto, nombreusuario, ultima_actualizacion, fecha_registro, ultimo_acceso, activo, contrasena, rolid) FROM stdin;
2	sr.aldaco@ciencias.unam.mx	Alex Aldaco	\N	aldaco	2018-10-17 02:30:21.936	2018-10-17 02:27:51.044	2018-10-17 02:30:21.936	t	$2a$10$YBfTeE7HvxNS5GO3QoiQlOtHXaNjSsqMQblb5s4NZKXhjWpQKDO/W	3
1	alex.aldaco@ciencias.unam.mx	Alex Aldaco	\N	alex	2018-10-11 18:10:45.216	2018-10-11 18:10:45.216	2018-10-17 03:02:38.228	t	$2a$10$Dlbaxl7IuHOyG4kSqvmj.eCN8lq1HJoa/Vb8C7ESJHrkQMYnEO.eW	1
3	len.cortez@ciencias.unam.mx	Len	\N	len	2018-10-17 17:40:41.679	2018-10-17 17:36:47.519	2018-10-17 17:40:41.679	t	$2a$10$FH.CBxc.YLDatjysRtwoVODDHT2VrkLY2XBDRaCS3UT6QvqmaaefW	3
4	ianecm@ciencias.unam.mx	Ian Eduardo Chavez Muñoa	\N	Ian	2018-10-17 17:45:26.2	2018-10-17 17:43:19.331	2018-10-17 23:56:18.636	t	$2a$10$s3WtpqdjsXzzTJnA4CA8COA6SAG2dLGhn8bW5AFkizj1KLVppAO.u	3
\.


--
-- Name: categoria_idcategoria_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.categoria_idcategoria_seq', 1, false);


--
-- Name: confirmacion_idconfirmacion_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.confirmacion_idconfirmacion_seq', 4, true);


--
-- Name: kit_idkit_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.kit_idkit_seq', 1, false);


--
-- Name: kit_idusuario_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.kit_idusuario_seq', 1, false);


--
-- Name: material_idmaterial_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.material_idmaterial_seq', 1, false);


--
-- Name: profesor_idvalidacion_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.profesor_idvalidacion_seq', 2, true);


--
-- Name: rol_rol_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.rol_rol_id_seq', 3, true);


--
-- Name: usuario_idusuario_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuario_idusuario_seq', 4, true);


--
-- Name: categoria categoria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT categoria_pkey PRIMARY KEY (idcategoria);


--
-- Name: codigo_confirmacion confirmacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.codigo_confirmacion
    ADD CONSTRAINT confirmacion_pkey PRIMARY KEY (idconfirmacion);


--
-- Name: kit id_kitpk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kit
    ADD CONSTRAINT id_kitpk PRIMARY KEY (idkit);


--
-- Name: material id_materialpk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.material
    ADD CONSTRAINT id_materialpk PRIMARY KEY (idmaterial);


--
-- Name: profesor_validacion profesor_validacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.profesor_validacion
    ADD CONSTRAINT profesor_validacion_pkey PRIMARY KEY (id_profesor_validacion);


--
-- Name: rol roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rol
    ADD CONSTRAINT roles_pkey PRIMARY KEY (rol_id);


--
-- Name: codigo_confirmacion uq_confirmacion_idusuario; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.codigo_confirmacion
    ADD CONSTRAINT uq_confirmacion_idusuario UNIQUE (idusuario);


--
-- Name: profesor_validacion uq_profesor_idusuario; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.profesor_validacion
    ADD CONSTRAINT uq_profesor_idusuario UNIQUE (idusuario);


--
-- Name: profesor_validacion uq_profesor_numero_trabajador; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.profesor_validacion
    ADD CONSTRAINT uq_profesor_numero_trabajador UNIQUE (numero_trabajador);


--
-- Name: usuario uq_usuarios_correo; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT uq_usuarios_correo UNIQUE (correo);


--
-- Name: usuario uq_usuarios_nombreusuario; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT uq_usuarios_nombreusuario UNIQUE (nombreusuario);


--
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (idusuario);


--
-- Name: fki_id_confirmacion_usuariofk; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_id_confirmacion_usuariofk ON public.codigo_confirmacion USING btree (idusuario);


--
-- Name: codigo_confirmacion id_confirmacion_usuariofk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.codigo_confirmacion
    ADD CONSTRAINT id_confirmacion_usuariofk FOREIGN KEY (idusuario) REFERENCES public.usuario(idusuario) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: kit id_kit_usuariofk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kit
    ADD CONSTRAINT id_kit_usuariofk FOREIGN KEY (idusuario) REFERENCES public.usuario(idusuario);


--
-- Name: material id_material_usuariofk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.material
    ADD CONSTRAINT id_material_usuariofk FOREIGN KEY (idusuario) REFERENCES public.usuario(idusuario);


--
-- Name: profesor_validacion id_profesor_idusuariofk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.profesor_validacion
    ADD CONSTRAINT id_profesor_idusuariofk FOREIGN KEY (idusuario) REFERENCES public.usuario(idusuario) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--


--
-- PostgreSQL database dump
--

-- Dumped from database version 13.6 (Ubuntu 13.6-1.pgdg20.04+1)
-- Dumped by pg_dump version 14.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: role_table; Type: TABLE; Schema: public; Owner: bocxxkiemgufar
--

CREATE TABLE public.role_table (
    id integer NOT NULL,
    name text
);


ALTER TABLE public.role_table OWNER TO bocxxkiemgufar;

--
-- Name: role_table_id_seq; Type: SEQUENCE; Schema: public; Owner: bocxxkiemgufar
--

CREATE SEQUENCE public.role_table_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.role_table_id_seq OWNER TO bocxxkiemgufar;

--
-- Name: role_table_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bocxxkiemgufar
--

ALTER SEQUENCE public.role_table_id_seq OWNED BY public.role_table.id;


--
-- Name: tasks_table; Type: TABLE; Schema: public; Owner: bocxxkiemgufar
--

CREATE TABLE public.tasks_table (
    id integer NOT NULL,
    title text,
    user_id integer,
    text text
);


ALTER TABLE public.tasks_table OWNER TO bocxxkiemgufar;

--
-- Name: tasks_table_id_seq; Type: SEQUENCE; Schema: public; Owner: bocxxkiemgufar
--

CREATE SEQUENCE public.tasks_table_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tasks_table_id_seq OWNER TO bocxxkiemgufar;

--
-- Name: tasks_table_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bocxxkiemgufar
--

ALTER SEQUENCE public.tasks_table_id_seq OWNED BY public.tasks_table.id;


--
-- Name: user_table; Type: TABLE; Schema: public; Owner: bocxxkiemgufar
--

CREATE TABLE public.user_table (
    id integer NOT NULL,
    password text,
    username text,
    tusername text DEFAULT ''::text NOT NULL
);


ALTER TABLE public.user_table OWNER TO bocxxkiemgufar;

--
-- Name: user_table_id_seq; Type: SEQUENCE; Schema: public; Owner: bocxxkiemgufar
--

CREATE SEQUENCE public.user_table_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_table_id_seq OWNER TO bocxxkiemgufar;

--
-- Name: user_table_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bocxxkiemgufar
--

ALTER SEQUENCE public.user_table_id_seq OWNED BY public.user_table.id;


--
-- Name: user_table_roles; Type: TABLE; Schema: public; Owner: bocxxkiemgufar
--

CREATE TABLE public.user_table_roles (
    user_id integer,
    roles_id integer
);


ALTER TABLE public.user_table_roles OWNER TO bocxxkiemgufar;

--
-- Name: role_table id; Type: DEFAULT; Schema: public; Owner: bocxxkiemgufar
--

ALTER TABLE ONLY public.role_table ALTER COLUMN id SET DEFAULT nextval('public.role_table_id_seq'::regclass);


--
-- Name: tasks_table id; Type: DEFAULT; Schema: public; Owner: bocxxkiemgufar
--

ALTER TABLE ONLY public.tasks_table ALTER COLUMN id SET DEFAULT nextval('public.tasks_table_id_seq'::regclass);


--
-- Name: user_table id; Type: DEFAULT; Schema: public; Owner: bocxxkiemgufar
--

ALTER TABLE ONLY public.user_table ALTER COLUMN id SET DEFAULT nextval('public.user_table_id_seq'::regclass);


--
-- Data for Name: role_table; Type: TABLE DATA; Schema: public; Owner: bocxxkiemgufar
--

COPY public.role_table (id, name) FROM stdin;
1	ROLE_USER
2	ROLE_ADMIN
\.


--
-- Data for Name: tasks_table; Type: TABLE DATA; Schema: public; Owner: bocxxkiemgufar
--

COPY public.tasks_table (id, title, user_id, text) FROM stdin;
6	A	7	a
7	B	7	b
5	Задача 1	4	тест 123 1
8	A	4	a2
14	Задача от бота	4	тестовая задача
15	Задача от бота	4	тест
16	Задача от бота	4	колбек
17	Задача от бота	4	хероку
\.


--
-- Data for Name: user_table; Type: TABLE DATA; Schema: public; Owner: bocxxkiemgufar
--

COPY public.user_table (id, password, username, tusername) FROM stdin;
5	$2a$10$QRDBLblN0gZlol4HjaxQe.RU2WDcwnEEOBPLT.ygXTdVtDA3HGBli	u2	
6	$2a$10$UAqc3p/uX8xpVSNiZpqsYOG/eR6uEXSRRkBghkAk0voHxV17y4FKq	u3	
7	$2a$10$/0IhWFT/TFP3mj4rXaJ2keWCtJueRV8bByZceYj4FzMqU1VRoDNVW	u4	
8	$2a$10$UaU39H6HqW0SBVRk/Q2NhuYo41tEts/h6VWc9zBiHZNZzG9k2BJyu	u5	
9	$2a$10$W7S0vOg10goBDRHdHw/wu.XpylCMVS.GTFfatnptvlFPdv42F8WMO	u6	
4	$2a$10$5OW.Tt3ZSIB7yuA1Eu9GtuiBFP0ejvmd4tbEjuIOE7XEfl6pjtP4W	u1	astakhanov
\.


--
-- Data for Name: user_table_roles; Type: TABLE DATA; Schema: public; Owner: bocxxkiemgufar
--

COPY public.user_table_roles (user_id, roles_id) FROM stdin;
4	1
5	1
6	1
7	1
8	1
9	2
\.


--
-- Name: role_table_id_seq; Type: SEQUENCE SET; Schema: public; Owner: bocxxkiemgufar
--

SELECT pg_catalog.setval('public.role_table_id_seq', 1, false);


--
-- Name: tasks_table_id_seq; Type: SEQUENCE SET; Schema: public; Owner: bocxxkiemgufar
--

SELECT pg_catalog.setval('public.tasks_table_id_seq', 17, true);


--
-- Name: user_table_id_seq; Type: SEQUENCE SET; Schema: public; Owner: bocxxkiemgufar
--

SELECT pg_catalog.setval('public.user_table_id_seq', 9, true);


--
-- Name: role_table role_table_pk; Type: CONSTRAINT; Schema: public; Owner: bocxxkiemgufar
--

ALTER TABLE ONLY public.role_table
    ADD CONSTRAINT role_table_pk PRIMARY KEY (id);


--
-- Name: tasks_table tasks_table_pk; Type: CONSTRAINT; Schema: public; Owner: bocxxkiemgufar
--

ALTER TABLE ONLY public.tasks_table
    ADD CONSTRAINT tasks_table_pk PRIMARY KEY (id);


--
-- Name: user_table user_table_pk; Type: CONSTRAINT; Schema: public; Owner: bocxxkiemgufar
--

ALTER TABLE ONLY public.user_table
    ADD CONSTRAINT user_table_pk PRIMARY KEY (id);


--
-- Name: role_table_id_uindex; Type: INDEX; Schema: public; Owner: bocxxkiemgufar
--

CREATE UNIQUE INDEX role_table_id_uindex ON public.role_table USING btree (id);


--
-- Name: tasks_table_id_uindex; Type: INDEX; Schema: public; Owner: bocxxkiemgufar
--

CREATE UNIQUE INDEX tasks_table_id_uindex ON public.tasks_table USING btree (id);


--
-- Name: user_table_id_uindex; Type: INDEX; Schema: public; Owner: bocxxkiemgufar
--

CREATE UNIQUE INDEX user_table_id_uindex ON public.user_table USING btree (id);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: bocxxkiemgufar
--

REVOKE ALL ON SCHEMA public FROM postgres;
REVOKE ALL ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO bocxxkiemgufar;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: LANGUAGE plpgsql; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON LANGUAGE plpgsql TO bocxxkiemgufar;


--
-- PostgreSQL database dump complete
--


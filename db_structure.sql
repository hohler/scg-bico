--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.2
-- Dumped by pg_dump version 9.6.2

--
-- Name: batch_job_execution; Type: TABLE; Schema: public; Owner: bico
--

CREATE TABLE batch_job_execution (
    job_execution_id bigint NOT NULL,
    version bigint,
    job_instance_id bigint NOT NULL,
    create_time timestamp without time zone NOT NULL,
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    status character varying(10),
    exit_code character varying(2500),
    exit_message character varying(2500),
    last_updated timestamp without time zone,
    job_configuration_location character varying(2500)
);


--
-- Name: batch_job_execution_context; Type: TABLE; Schema: public; Owner: bico
--

CREATE TABLE batch_job_execution_context (
    job_execution_id bigint NOT NULL,
    short_context character varying(2500) NOT NULL,
    serialized_context text
);


--
-- Name: batch_job_execution_params; Type: TABLE; Schema: public; Owner: bico
--

CREATE TABLE batch_job_execution_params (
    job_execution_id bigint NOT NULL,
    type_cd character varying(6) NOT NULL,
    key_name character varying(100) NOT NULL,
    string_val character varying(250),
    date_val timestamp without time zone,
    long_val bigint,
    double_val double precision,
    identifying character(1) NOT NULL
);

--
-- Name: batch_job_execution_seq; Type: SEQUENCE; Schema: public; Owner: bico
--

CREATE SEQUENCE batch_job_execution_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: batch_job_instance; Type: TABLE; Schema: public; Owner: bico
--

CREATE TABLE batch_job_instance (
    job_instance_id bigint NOT NULL,
    version bigint,
    job_name character varying(100) NOT NULL,
    job_key character varying(32) NOT NULL
);


--
-- Name: batch_job_seq; Type: SEQUENCE; Schema: public; Owner: bico
--

CREATE SEQUENCE batch_job_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: batch_step_execution; Type: TABLE; Schema: public; Owner: bico
--

CREATE TABLE batch_step_execution (
    step_execution_id bigint NOT NULL,
    version bigint NOT NULL,
    step_name character varying(100) NOT NULL,
    job_execution_id bigint NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone,
    status character varying(10),
    commit_count bigint,
    read_count bigint,
    filter_count bigint,
    write_count bigint,
    read_skip_count bigint,
    write_skip_count bigint,
    process_skip_count bigint,
    rollback_count bigint,
    exit_code character varying(2500),
    exit_message character varying(2500),
    last_updated timestamp without time zone
);


--
-- Name: batch_step_execution_context; Type: TABLE; Schema: public; Owner: bico
--

CREATE TABLE batch_step_execution_context (
    step_execution_id bigint NOT NULL,
    short_context character varying(2500) NOT NULL,
    serialized_context text
);


--
-- Name: batch_step_execution_seq; Type: SEQUENCE; Schema: public; Owner: bico
--

CREATE SEQUENCE batch_step_execution_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: changemetrics; Type: TABLE; Schema: public; Owner: bico
--

CREATE TABLE changemetrics (
    id bigint NOT NULL,
    age bigint NOT NULL,
    authors integer NOT NULL,
    avgchangeset double precision NOT NULL,
    avgcodechurn double precision NOT NULL,
    avglocadded double precision NOT NULL,
    avglocremoved double precision NOT NULL,
    bugfixes integer NOT NULL,
    codechurn bigint NOT NULL,
    file character varying(255),
    firstcommit timestamp without time zone,
    lastcommit timestamp without time zone,
    locadded bigint NOT NULL,
    locremoved bigint NOT NULL,
    maxchangeset integer NOT NULL,
    maxcodechurn bigint NOT NULL,
    maxlocadded bigint NOT NULL,
    maxlocremoved bigint NOT NULL,
    refactorings integer NOT NULL,
    revisions integer NOT NULL,
    weightedage double precision NOT NULL,
    commit_id bigint
);


--
-- Name: changemetrics_id_seq; Type: SEQUENCE; Schema: public; Owner: bico
--

CREATE SEQUENCE changemetrics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: changemetrics_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bico
--

ALTER SEQUENCE changemetrics_id_seq OWNED BY changemetrics.id;


--
-- Name: commitfiles; Type: TABLE; Schema: public; Owner: bico
--

CREATE TABLE commitfiles (
    id bigint NOT NULL,
    additions integer NOT NULL,
    changetype integer,
    deletions integer NOT NULL,
    newpath character varying(255),
    oldpath character varying(255),
    patch text,
    commit_id bigint
);

--
-- Name: commitfiles_id_seq; Type: SEQUENCE; Schema: public; Owner: bico
--

CREATE SEQUENCE commitfiles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: commitfiles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bico
--

ALTER SEQUENCE commitfiles_id_seq OWNED BY commitfiles.id;


--
-- Name: commitissues; Type: TABLE; Schema: public; Owner: bico
--

CREATE TABLE commitissues (
    id bigint NOT NULL,
    description text,
    link character varying(255),
    name character varying(255),
    priority integer,
    type integer
);


--
-- Name: commitissues_id_seq; Type: SEQUENCE; Schema: public; Owner: bico
--

CREATE SEQUENCE commitissues_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: commitissues_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bico
--

ALTER SEQUENCE commitissues_id_seq OWNED BY commitissues.id;


--
-- Name: commits; Type: TABLE; Schema: public; Owner: bico
--

CREATE TABLE commits (
    id bigint NOT NULL,
    additions integer NOT NULL,
    deletions integer NOT NULL,
    ismergecommit boolean NOT NULL,
    message text,
    ref character varying(255),
    "timestamp" integer NOT NULL,
    parentcommit_id bigint,
    project_id bigint
);


--
-- Name: commits_commitissues; Type: TABLE; Schema: public; Owner: bico
--

CREATE TABLE commits_commitissues (
    commitissue_id bigint NOT NULL,
    commit_id bigint NOT NULL
);


--
-- Name: commits_id_seq; Type: SEQUENCE; Schema: public; Owner: bico
--

CREATE SEQUENCE commits_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: commits_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bico
--

ALTER SEQUENCE commits_id_seq OWNED BY commits.id;


--
-- Name: projects; Type: TABLE; Schema: public; Owner: bico
--

CREATE TABLE projects (
    id bigint NOT NULL,
    branch character varying(255),
    changemetriceverycommits integer NOT NULL,
    changemetrictimewindow integer NOT NULL,
    issuetrackerurlpattern character varying(255),
    name character varying(255),
    type integer,
    url character varying(255),
    sourcemetriceverycommits integer DEFAULT 0 NOT NULL
);

--
-- Name: projects_id_seq; Type: SEQUENCE; Schema: public; Owner: bico
--

CREATE SEQUENCE projects_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: projects_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bico
--

ALTER SEQUENCE projects_id_seq OWNED BY projects.id;


--
-- Name: sourcemetrics; Type: TABLE; Schema: public; Owner: bico
--

CREATE TABLE sourcemetrics (
    id bigint NOT NULL,
    cbo integer NOT NULL,
    classname character varying(255),
    dit integer NOT NULL,
    file character varying(255),
    lcom integer NOT NULL,
    loc integer NOT NULL,
    noc integer NOT NULL,
    nocb integer NOT NULL,
    nof integer NOT NULL,
    nom integer NOT NULL,
    nomwmop integer NOT NULL,
    nona integer NOT NULL,
    nonc integer NOT NULL,
    nopf integer NOT NULL,
    nopm integer NOT NULL,
    nosf integer NOT NULL,
    nosi integer NOT NULL,
    nosm integer NOT NULL,
    rfc integer NOT NULL,
    type character varying(255),
    wmc integer NOT NULL,
    commit_id bigint
);

--
-- Name: sourcemetrics_id_seq; Type: SEQUENCE; Schema: public; Owner: bico
--

CREATE SEQUENCE sourcemetrics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: sourcemetrics_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bico
--

ALTER SEQUENCE sourcemetrics_id_seq OWNED BY sourcemetrics.id;


--
-- Name: szzmetrics; Type: TABLE; Schema: public; Owner: bico
--

CREATE TABLE szzmetrics (
    id bigint NOT NULL,
    bugfix boolean NOT NULL,
    bugs integer NOT NULL,
    file character varying(255),
    commit_id bigint,
    deleted boolean DEFAULT false NOT NULL
);

--
-- Name: szzmetrics_id_seq; Type: SEQUENCE; Schema: public; Owner: bico
--

CREATE SEQUENCE szzmetrics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: szzmetrics_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bico
--

ALTER SEQUENCE szzmetrics_id_seq OWNED BY szzmetrics.id;


--
-- Name: changemetrics id; Type: DEFAULT; Schema: public; Owner: bico
--

ALTER TABLE ONLY changemetrics ALTER COLUMN id SET DEFAULT nextval('changemetrics_id_seq'::regclass);


--
-- Name: commitfiles id; Type: DEFAULT; Schema: public; Owner: bico
--

ALTER TABLE ONLY commitfiles ALTER COLUMN id SET DEFAULT nextval('commitfiles_id_seq'::regclass);


--
-- Name: commitissues id; Type: DEFAULT; Schema: public; Owner: bico
--

ALTER TABLE ONLY commitissues ALTER COLUMN id SET DEFAULT nextval('commitissues_id_seq'::regclass);


--
-- Name: commits id; Type: DEFAULT; Schema: public; Owner: bico
--

ALTER TABLE ONLY commits ALTER COLUMN id SET DEFAULT nextval('commits_id_seq'::regclass);


--
-- Name: projects id; Type: DEFAULT; Schema: public; Owner: bico
--

ALTER TABLE ONLY projects ALTER COLUMN id SET DEFAULT nextval('projects_id_seq'::regclass);


--
-- Name: sourcemetrics id; Type: DEFAULT; Schema: public; Owner: bico
--

ALTER TABLE ONLY sourcemetrics ALTER COLUMN id SET DEFAULT nextval('sourcemetrics_id_seq'::regclass);


--
-- Name: szzmetrics id; Type: DEFAULT; Schema: public; Owner: bico
--

ALTER TABLE ONLY szzmetrics ALTER COLUMN id SET DEFAULT nextval('szzmetrics_id_seq'::regclass);


--
-- Name: batch_job_execution_context batch_job_execution_context_pkey; Type: CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY batch_job_execution_context
    ADD CONSTRAINT batch_job_execution_context_pkey PRIMARY KEY (job_execution_id);


--
-- Name: batch_job_execution batch_job_execution_pkey; Type: CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY batch_job_execution
    ADD CONSTRAINT batch_job_execution_pkey PRIMARY KEY (job_execution_id);


--
-- Name: batch_job_instance batch_job_instance_pkey; Type: CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY batch_job_instance
    ADD CONSTRAINT batch_job_instance_pkey PRIMARY KEY (job_instance_id);


--
-- Name: batch_step_execution_context batch_step_execution_context_pkey; Type: CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY batch_step_execution_context
    ADD CONSTRAINT batch_step_execution_context_pkey PRIMARY KEY (step_execution_id);


--
-- Name: batch_step_execution batch_step_execution_pkey; Type: CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY batch_step_execution
    ADD CONSTRAINT batch_step_execution_pkey PRIMARY KEY (step_execution_id);


--
-- Name: changemetrics changemetrics_pkey; Type: CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY changemetrics
    ADD CONSTRAINT changemetrics_pkey PRIMARY KEY (id);


--
-- Name: commitfiles commitfiles_pkey; Type: CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY commitfiles
    ADD CONSTRAINT commitfiles_pkey PRIMARY KEY (id);


--
-- Name: commitissues commitissues_pkey; Type: CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY commitissues
    ADD CONSTRAINT commitissues_pkey PRIMARY KEY (id);


--
-- Name: commits_commitissues commits_commitissues_pkey; Type: CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY commits_commitissues
    ADD CONSTRAINT commits_commitissues_pkey PRIMARY KEY (commit_id, commitissue_id);


--
-- Name: commits commits_pkey; Type: CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY commits
    ADD CONSTRAINT commits_pkey PRIMARY KEY (id);


--
-- Name: batch_job_instance job_inst_un; Type: CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY batch_job_instance
    ADD CONSTRAINT job_inst_un UNIQUE (job_name, job_key);


--
-- Name: projects projects_pkey; Type: CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY projects
    ADD CONSTRAINT projects_pkey PRIMARY KEY (id);


--
-- Name: sourcemetrics sourcemetrics_pkey; Type: CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY sourcemetrics
    ADD CONSTRAINT sourcemetrics_pkey PRIMARY KEY (id);


--
-- Name: szzmetrics szzmetrics_pkey; Type: CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY szzmetrics
    ADD CONSTRAINT szzmetrics_pkey PRIMARY KEY (id);


--
-- Name: commitfiles fk_6dpl7twcby705uampcsyfyde; Type: FK CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY commitfiles
    ADD CONSTRAINT fk_6dpl7twcby705uampcsyfyde FOREIGN KEY (commit_id) REFERENCES commits(id);


--
-- Name: commits_commitissues fk_9m0kai4mnr30npxk37exeul3w; Type: FK CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY commits_commitissues
    ADD CONSTRAINT fk_9m0kai4mnr30npxk37exeul3w FOREIGN KEY (commit_id) REFERENCES commits(id);


--
-- Name: commits fk_9trykgj1m8q5mqmh5x4u2nxq9; Type: FK CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY commits
    ADD CONSTRAINT fk_9trykgj1m8q5mqmh5x4u2nxq9 FOREIGN KEY (project_id) REFERENCES projects(id);


--
-- Name: sourcemetrics fk_g6crvhpslqihokt9ikqgqywsn; Type: FK CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY sourcemetrics
    ADD CONSTRAINT fk_g6crvhpslqihokt9ikqgqywsn FOREIGN KEY (commit_id) REFERENCES commits(id);


--
-- Name: szzmetrics fk_h1sdrb7p3il72opv8ludtfk6l; Type: FK CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY szzmetrics
    ADD CONSTRAINT fk_h1sdrb7p3il72opv8ludtfk6l FOREIGN KEY (commit_id) REFERENCES commits(id);


--
-- Name: commits fk_lvba9otgogq9bg4h2xeq98myp; Type: FK CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY commits
    ADD CONSTRAINT fk_lvba9otgogq9bg4h2xeq98myp FOREIGN KEY (parentcommit_id) REFERENCES commits(id);


--
-- Name: commits_commitissues fk_mphtd5au4bfdv45bc2vx3a3wh; Type: FK CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY commits_commitissues
    ADD CONSTRAINT fk_mphtd5au4bfdv45bc2vx3a3wh FOREIGN KEY (commitissue_id) REFERENCES commitissues(id);


--
-- Name: changemetrics fk_pyikvosn8ef9at4p8a3ldy45u; Type: FK CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY changemetrics
    ADD CONSTRAINT fk_pyikvosn8ef9at4p8a3ldy45u FOREIGN KEY (commit_id) REFERENCES commits(id);


--
-- Name: batch_job_execution_context job_exec_ctx_fk; Type: FK CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY batch_job_execution_context
    ADD CONSTRAINT job_exec_ctx_fk FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution(job_execution_id);


--
-- Name: batch_job_execution_params job_exec_params_fk; Type: FK CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY batch_job_execution_params
    ADD CONSTRAINT job_exec_params_fk FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution(job_execution_id);


--
-- Name: batch_step_execution job_exec_step_fk; Type: FK CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY batch_step_execution
    ADD CONSTRAINT job_exec_step_fk FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution(job_execution_id);


--
-- Name: batch_job_execution job_inst_exec_fk; Type: FK CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY batch_job_execution
    ADD CONSTRAINT job_inst_exec_fk FOREIGN KEY (job_instance_id) REFERENCES batch_job_instance(job_instance_id);


--
-- Name: batch_step_execution_context step_exec_ctx_fk; Type: FK CONSTRAINT; Schema: public; Owner: bico
--

ALTER TABLE ONLY batch_step_execution_context
    ADD CONSTRAINT step_exec_ctx_fk FOREIGN KEY (step_execution_id) REFERENCES batch_step_execution(step_execution_id);

--
-- PostgreSQL database dump complete
--


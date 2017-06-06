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
-- TOC entry 207 (class 1259 OID 200741)
-- Name: batch_job_execution_context; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE batch_job_execution_context (
    job_execution_id bigint NOT NULL,
    short_context character varying(2500) NOT NULL,
    serialized_context text
);


--
-- TOC entry 204 (class 1259 OID 200707)
-- Name: batch_job_execution_params; Type: TABLE; Schema: public; Owner: -
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
-- TOC entry 209 (class 1259 OID 200756)
-- Name: batch_job_execution_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE batch_job_execution_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 202 (class 1259 OID 200687)
-- Name: batch_job_instance; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE batch_job_instance (
    job_instance_id bigint NOT NULL,
    version bigint,
    job_name character varying(100) NOT NULL,
    job_key character varying(32) NOT NULL
);


--
-- TOC entry 210 (class 1259 OID 200758)
-- Name: batch_job_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE batch_job_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 205 (class 1259 OID 200715)
-- Name: batch_step_execution; Type: TABLE; Schema: public; Owner: -
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
-- TOC entry 206 (class 1259 OID 200728)
-- Name: batch_step_execution_context; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE batch_step_execution_context (
    step_execution_id bigint NOT NULL,
    short_context character varying(2500) NOT NULL,
    serialized_context text
);


--
-- TOC entry 208 (class 1259 OID 200754)
-- Name: batch_step_execution_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE batch_step_execution_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 201 (class 1259 OID 189654)
-- Name: bigcommits; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE bigcommits (
    id bigint NOT NULL,
    issuetype integer,
    commit_id bigint,
    project_id bigint
);


--
-- TOC entry 200 (class 1259 OID 189652)
-- Name: bigcommits_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE bigcommits_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2264 (class 0 OID 0)
-- Dependencies: 200
-- Name: bigcommits_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE bigcommits_id_seq OWNED BY bigcommits.id;


--
-- TOC entry 186 (class 1259 OID 176898)
-- Name: changemetrics; Type: TABLE; Schema: public; Owner: -
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
-- TOC entry 185 (class 1259 OID 176896)
-- Name: changemetrics_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE changemetrics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2265 (class 0 OID 0)
-- Dependencies: 185
-- Name: changemetrics_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE changemetrics_id_seq OWNED BY changemetrics.id;


--
-- TOC entry 188 (class 1259 OID 176906)
-- Name: commitfiles; Type: TABLE; Schema: public; Owner: -
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
-- TOC entry 187 (class 1259 OID 176904)
-- Name: commitfiles_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE commitfiles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2266 (class 0 OID 0)
-- Dependencies: 187
-- Name: commitfiles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE commitfiles_id_seq OWNED BY commitfiles.id;


--
-- TOC entry 190 (class 1259 OID 176917)
-- Name: commitissues; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE commitissues (
    id bigint NOT NULL,
    description text,
    link character varying(255),
    name character varying(255),
    priority integer,
    type integer,
    processed boolean DEFAULT false NOT NULL
);


--
-- TOC entry 189 (class 1259 OID 176915)
-- Name: commitissues_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE commitissues_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2267 (class 0 OID 0)
-- Dependencies: 189
-- Name: commitissues_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE commitissues_id_seq OWNED BY commitissues.id;


--
-- TOC entry 192 (class 1259 OID 176928)
-- Name: commits; Type: TABLE; Schema: public; Owner: -
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
-- TOC entry 193 (class 1259 OID 176937)
-- Name: commits_commitissues; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE commits_commitissues (
    commitissue_id bigint NOT NULL,
    commit_id bigint NOT NULL
);


--
-- TOC entry 191 (class 1259 OID 176926)
-- Name: commits_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE commits_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2268 (class 0 OID 0)
-- Dependencies: 191
-- Name: commits_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE commits_id_seq OWNED BY commits.id;


--
-- TOC entry 195 (class 1259 OID 176944)
-- Name: projects; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE projects (
    id bigint NOT NULL,
    branch character varying(255),
    changemetriceverycommits integer NOT NULL,
    changemetrictimewindow integer NOT NULL,
    issuetrackerurlpattern character varying(255),
    name character varying(255),
    sourcemetriceverycommits integer NOT NULL,
    type integer,
    url character varying(255)
);


--
-- TOC entry 194 (class 1259 OID 176942)
-- Name: projects_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE projects_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2269 (class 0 OID 0)
-- Dependencies: 194
-- Name: projects_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE projects_id_seq OWNED BY projects.id;


--
-- TOC entry 197 (class 1259 OID 176955)
-- Name: sourcemetrics; Type: TABLE; Schema: public; Owner: -
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
-- TOC entry 196 (class 1259 OID 176953)
-- Name: sourcemetrics_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE sourcemetrics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2270 (class 0 OID 0)
-- Dependencies: 196
-- Name: sourcemetrics_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE sourcemetrics_id_seq OWNED BY sourcemetrics.id;


--
-- TOC entry 199 (class 1259 OID 176966)
-- Name: szzmetrics; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE szzmetrics (
    id bigint NOT NULL,
    bugfix boolean NOT NULL,
    bugs integer NOT NULL,
    deleted boolean NOT NULL,
    file character varying(255),
    commit_id bigint
);


--
-- TOC entry 198 (class 1259 OID 176964)
-- Name: szzmetrics_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE szzmetrics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2271 (class 0 OID 0)
-- Dependencies: 198
-- Name: szzmetrics_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE szzmetrics_id_seq OWNED BY szzmetrics.id;


--
-- TOC entry 2094 (class 2604 OID 189657)
-- Name: bigcommits id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY bigcommits ALTER COLUMN id SET DEFAULT nextval('bigcommits_id_seq'::regclass);


--
-- TOC entry 2086 (class 2604 OID 176901)
-- Name: changemetrics id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY changemetrics ALTER COLUMN id SET DEFAULT nextval('changemetrics_id_seq'::regclass);


--
-- TOC entry 2087 (class 2604 OID 176909)
-- Name: commitfiles id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY commitfiles ALTER COLUMN id SET DEFAULT nextval('commitfiles_id_seq'::regclass);


--
-- TOC entry 2088 (class 2604 OID 176920)
-- Name: commitissues id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY commitissues ALTER COLUMN id SET DEFAULT nextval('commitissues_id_seq'::regclass);


--
-- TOC entry 2090 (class 2604 OID 176931)
-- Name: commits id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY commits ALTER COLUMN id SET DEFAULT nextval('commits_id_seq'::regclass);


--
-- TOC entry 2091 (class 2604 OID 176947)
-- Name: projects id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY projects ALTER COLUMN id SET DEFAULT nextval('projects_id_seq'::regclass);


--
-- TOC entry 2092 (class 2604 OID 176958)
-- Name: sourcemetrics id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY sourcemetrics ALTER COLUMN id SET DEFAULT nextval('sourcemetrics_id_seq'::regclass);


--
-- TOC entry 2093 (class 2604 OID 176969)
-- Name: szzmetrics id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY szzmetrics ALTER COLUMN id SET DEFAULT nextval('szzmetrics_id_seq'::regclass);


--
-- TOC entry 2124 (class 2606 OID 200748)
-- Name: batch_job_execution_context batch_job_execution_context_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY batch_job_execution_context
    ADD CONSTRAINT batch_job_execution_context_pkey PRIMARY KEY (job_execution_id);


--
-- TOC entry 2118 (class 2606 OID 200701)
-- Name: batch_job_execution batch_job_execution_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY batch_job_execution
    ADD CONSTRAINT batch_job_execution_pkey PRIMARY KEY (job_execution_id);


--
-- TOC entry 2114 (class 2606 OID 200691)
-- Name: batch_job_instance batch_job_instance_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY batch_job_instance
    ADD CONSTRAINT batch_job_instance_pkey PRIMARY KEY (job_instance_id);


--
-- TOC entry 2122 (class 2606 OID 200735)
-- Name: batch_step_execution_context batch_step_execution_context_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY batch_step_execution_context
    ADD CONSTRAINT batch_step_execution_context_pkey PRIMARY KEY (step_execution_id);


--
-- TOC entry 2120 (class 2606 OID 200722)
-- Name: batch_step_execution batch_step_execution_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY batch_step_execution
    ADD CONSTRAINT batch_step_execution_pkey PRIMARY KEY (step_execution_id);


--
-- TOC entry 2112 (class 2606 OID 189659)
-- Name: bigcommits bigcommits_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bigcommits
    ADD CONSTRAINT bigcommits_pkey PRIMARY KEY (id);


--
-- TOC entry 2096 (class 2606 OID 176903)
-- Name: changemetrics changemetrics_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY changemetrics
    ADD CONSTRAINT changemetrics_pkey PRIMARY KEY (id);


--
-- TOC entry 2098 (class 2606 OID 176914)
-- Name: commitfiles commitfiles_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY commitfiles
    ADD CONSTRAINT commitfiles_pkey PRIMARY KEY (id);


--
-- TOC entry 2100 (class 2606 OID 176925)
-- Name: commitissues commitissues_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY commitissues
    ADD CONSTRAINT commitissues_pkey PRIMARY KEY (id);


--
-- TOC entry 2104 (class 2606 OID 176941)
-- Name: commits_commitissues commits_commitissues_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY commits_commitissues
    ADD CONSTRAINT commits_commitissues_pkey PRIMARY KEY (commit_id, commitissue_id);


--
-- TOC entry 2102 (class 2606 OID 176936)
-- Name: commits commits_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY commits
    ADD CONSTRAINT commits_pkey PRIMARY KEY (id);


--
-- TOC entry 2116 (class 2606 OID 200693)
-- Name: batch_job_instance job_inst_un; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY batch_job_instance
    ADD CONSTRAINT job_inst_un UNIQUE (job_name, job_key);


--
-- TOC entry 2106 (class 2606 OID 176952)
-- Name: projects projects_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY projects
    ADD CONSTRAINT projects_pkey PRIMARY KEY (id);


--
-- TOC entry 2108 (class 2606 OID 176963)
-- Name: sourcemetrics sourcemetrics_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sourcemetrics
    ADD CONSTRAINT sourcemetrics_pkey PRIMARY KEY (id);


--
-- TOC entry 2110 (class 2606 OID 176971)
-- Name: szzmetrics szzmetrics_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY szzmetrics
    ADD CONSTRAINT szzmetrics_pkey PRIMARY KEY (id);


--
-- TOC entry 2126 (class 2606 OID 176977)
-- Name: commitfiles fk_6dpl7twcby705uampcsyfyde; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY commitfiles
    ADD CONSTRAINT fk_6dpl7twcby705uampcsyfyde FOREIGN KEY (commit_id) REFERENCES commits(id);


--
-- TOC entry 2134 (class 2606 OID 189665)
-- Name: bigcommits fk_7u07e9wt9frvca9jim2dij19b; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bigcommits
    ADD CONSTRAINT fk_7u07e9wt9frvca9jim2dij19b FOREIGN KEY (project_id) REFERENCES projects(id);


--
-- TOC entry 2129 (class 2606 OID 176992)
-- Name: commits_commitissues fk_9m0kai4mnr30npxk37exeul3w; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY commits_commitissues
    ADD CONSTRAINT fk_9m0kai4mnr30npxk37exeul3w FOREIGN KEY (commit_id) REFERENCES commits(id);


--
-- TOC entry 2128 (class 2606 OID 176987)
-- Name: commits fk_9trykgj1m8q5mqmh5x4u2nxq9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY commits
    ADD CONSTRAINT fk_9trykgj1m8q5mqmh5x4u2nxq9 FOREIGN KEY (project_id) REFERENCES projects(id);


--
-- TOC entry 2131 (class 2606 OID 177002)
-- Name: sourcemetrics fk_g6crvhpslqihokt9ikqgqywsn; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY sourcemetrics
    ADD CONSTRAINT fk_g6crvhpslqihokt9ikqgqywsn FOREIGN KEY (commit_id) REFERENCES commits(id);


--
-- TOC entry 2132 (class 2606 OID 177007)
-- Name: szzmetrics fk_h1sdrb7p3il72opv8ludtfk6l; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY szzmetrics
    ADD CONSTRAINT fk_h1sdrb7p3il72opv8ludtfk6l FOREIGN KEY (commit_id) REFERENCES commits(id);


--
-- TOC entry 2127 (class 2606 OID 176982)
-- Name: commits fk_lvba9otgogq9bg4h2xeq98myp; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY commits
    ADD CONSTRAINT fk_lvba9otgogq9bg4h2xeq98myp FOREIGN KEY (parentcommit_id) REFERENCES commits(id);


--
-- TOC entry 2130 (class 2606 OID 176997)
-- Name: commits_commitissues fk_mphtd5au4bfdv45bc2vx3a3wh; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY commits_commitissues
    ADD CONSTRAINT fk_mphtd5au4bfdv45bc2vx3a3wh FOREIGN KEY (commitissue_id) REFERENCES commitissues(id);


--
-- TOC entry 2133 (class 2606 OID 189660)
-- Name: bigcommits fk_ofm4nmdmgntau2b5yeud24sdd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bigcommits
    ADD CONSTRAINT fk_ofm4nmdmgntau2b5yeud24sdd FOREIGN KEY (commit_id) REFERENCES commits(id);


--
-- TOC entry 2125 (class 2606 OID 176972)
-- Name: changemetrics fk_pyikvosn8ef9at4p8a3ldy45u; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY changemetrics
    ADD CONSTRAINT fk_pyikvosn8ef9at4p8a3ldy45u FOREIGN KEY (commit_id) REFERENCES commits(id);


--
-- TOC entry 2139 (class 2606 OID 200749)
-- Name: batch_job_execution_context job_exec_ctx_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY batch_job_execution_context
    ADD CONSTRAINT job_exec_ctx_fk FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution(job_execution_id);


--
-- TOC entry 2136 (class 2606 OID 200710)
-- Name: batch_job_execution_params job_exec_params_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY batch_job_execution_params
    ADD CONSTRAINT job_exec_params_fk FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution(job_execution_id);


--
-- TOC entry 2137 (class 2606 OID 200723)
-- Name: batch_step_execution job_exec_step_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY batch_step_execution
    ADD CONSTRAINT job_exec_step_fk FOREIGN KEY (job_execution_id) REFERENCES batch_job_execution(job_execution_id);


--
-- TOC entry 2135 (class 2606 OID 200702)
-- Name: batch_job_execution job_inst_exec_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY batch_job_execution
    ADD CONSTRAINT job_inst_exec_fk FOREIGN KEY (job_instance_id) REFERENCES batch_job_instance(job_instance_id);


--
-- TOC entry 2138 (class 2606 OID 200736)
-- Name: batch_step_execution_context step_exec_ctx_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY batch_step_execution_context
    ADD CONSTRAINT step_exec_ctx_fk FOREIGN KEY (step_execution_id) REFERENCES batch_step_execution(step_execution_id);


-- Completed on 2017-06-06 09:28:42

--
-- PostgreSQL database dump complete
--
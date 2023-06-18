-- schema metric
CREATE SCHEMA IF NOT EXISTS metric
    AUTHORIZATION flexberryuser;

GRANT ALL ON SCHEMA metric TO PUBLIC;

GRANT ALL ON SCHEMA metric TO flexberryuser;

-- table metrics
CREATE TABLE IF NOT EXISTS metric.metrics
(
    id bigint NOT NULL,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    dimensions jsonb NOT NULL,
    CONSTRAINT metrics_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS metric.metrics
    OWNER to flexberryuser;

-- table values
CREATE TABLE IF NOT EXISTS metric."values"
(
    id bigint NOT NULL,
    "timestamp" timestamp with time zone NOT NULL,
    value double precision NOT NULL,
    metric_id bigint NOT NULL,
    metadata json,
    CONSTRAINT values_pkey PRIMARY KEY (id),
    CONSTRAINT metrics_fk FOREIGN KEY (metric_id)
        REFERENCES metric.metrics (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
        NOT VALID
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS metric."values"
    OWNER to flexberryuser;

-- sequence values_id
CREATE SEQUENCE metric.values_id
    INCREMENT 1
    START 1
    MINVALUE 1
    CACHE 1
    OWNED BY metric."values".id;

ALTER SEQUENCE metric.values_id
    OWNER TO flexberryuser;

ALTER TABLE IF EXISTS metric."values"
    ALTER COLUMN id SET DEFAULT nextval('metric.values_id'::regclass);

-- sequence metrics_id
CREATE SEQUENCE metric.metrics_id
    INCREMENT 1
    START 1
    MINVALUE 1
    CACHE 1
    OWNED BY metric."metrics".id;

ALTER SEQUENCE metric.metrics_id
    OWNER TO flexberryuser;

ALTER TABLE IF EXISTS metric.metrics
    ALTER COLUMN id SET DEFAULT nextval('metric.metrics_id'::regclass);
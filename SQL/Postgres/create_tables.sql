CREATE TABLE testentity (

 primaryKey UUID NOT NULL,

 name VARCHAR(255) NULL,
 
 points INTEGER NULL,

 enabled BOOLEAN DEFAULT FALSE NOT NULL,

 PRIMARY KEY (primaryKey));
 
 
 CREATE TABLE customer (

 primarykey UUID NOT NULL,

 name VARCHAR(255) NULL,
 
 age INTEGER NULL,

 PRIMARY KEY (primarykey));
 
 
 CREATE TABLE comment (

 primarykey UUID NOT NULL,

 commentdate timestamp NULL,
 
 commenttext VARCHAR(255) NULL,
 
 customer_id UUID REFERENCES customer(primarykey),

 PRIMARY KEY (primarykey));

CREATE TABLE IF NOT EXISTS public.applicationlog
(
    primarykey uuid NOT NULL,
    category VARCHAR(64),
    eventid integer,
    priority integer,
    severity VARCHAR(32),
    title VARCHAR(256),
    "timestamp" timestamp(3) without time zone,
    machinename VARCHAR(32),
    appdomainname VARCHAR(512),
    processid VARCHAR(256),
    processname VARCHAR(512),
    threadname VARCHAR(512),
    message VARCHAR(2500),
    formattedmessage text,
    CONSTRAINT applicationlog_pkey PRIMARY KEY (primarykey)
);

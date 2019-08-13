CREATE TABLE companies (
    id serial PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

INSERT INTO companies (name)
VALUES
    ('Abibas'),
    ('Aplle'),
    ('Coldate');

CREATE TABLE users (
    id SERIAL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    company_id BIGINT,

    PRIMARY KEY (id),

    CONSTRAINT fk_company
    FOREIGN KEY (company_id)
    REFERENCES companies (id)
);

INSERT INTO users (username, password, company_id)
VALUES
    ('user1', '{noop}123', 1),
    ('user2', '{noop}123', 2),
    ('user3', '{noop}123', 3);

CREATE TABLE roles (
    id SERIAL,
    name VARCHAR(50),

    PRIMARY KEY (id)
);

INSERT INTO roles (name)
VALUES
    ('ROLE_ADMIN'),
    ('ROLE_USER');

CREATE TABLE users_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_user_id_01 FOREIGN KEY (user_id)
    REFERENCES users (id),

    CONSTRAINT fk_role_id_01 FOREIGN KEY (user_id)
    REFERENCES users (id)
);

INSERT INTO users_roles
VALUES
    (1, 1),
    (1, 2),
    (2, 2),
    (3, 2);

CREATE TABLE documents (
    id serial PRIMARY KEY,
    creation_time TIMESTAMP,
    title VARCHAR(50) NOT NULL,
    company_creator BIGINT,
    first_company BIGINT,
    second_company BIGINT,
    first_signature BOOL,
    second_signature BOOL,
    content TEXT,

    CONSTRAINT fk_company_creator
    FOREIGN KEY (company_creator)
    REFERENCES companies (id),

    CONSTRAINT fk_first_company
    FOREIGN KEY (first_company)
    REFERENCES companies (id),

    CONSTRAINT fk_second_company
    FOREIGN KEY (second_company)
    REFERENCES companies (id)
);

-- INSERT INTO documents
-- VALUES
--     (1, 'Document #1', 1, 2, TRUE, FALSE, 'Content of document #1'),
--     (2, 'Document #2', 2, 3, TRUE, FALSE, 'Content of document #2'),
--     (3, 'Document #3', 3, 1, TRUE, FALSE, 'Content of document #3');
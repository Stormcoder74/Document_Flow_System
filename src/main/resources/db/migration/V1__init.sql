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
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    enabled boolean NOT NULL,
    company_id BIGINT,

    PRIMARY KEY (username),

    CONSTRAINT fk_company
    FOREIGN KEY (company_id)
    REFERENCES companies (id)
);

INSERT INTO users
VALUES
('user1', '{noop}123', TRUE, 1),
('user2', '{noop}123', TRUE, 2),
('user3', '{noop}123', TRUE, 3);

CREATE TABLE authorities (
    username VARCHAR(50),
    authority VARCHAR(50),

    CONSTRAINT authorities_pk
    PRIMARY KEY (username, authority),

    CONSTRAINT authorities_ibfk_1
    FOREIGN KEY (username)
    REFERENCES users (username)
);

INSERT INTO authorities
VALUES
('user1', 'ROLE_ADMIN'),
('user1', 'ROLE_USER'),
('user2', 'ROLE_USER'),
('user3', 'ROLE_USER');

CREATE TABLE documents (
    id serial PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    first_company BIGINT,
    second_company BIGINT,
    first_signature BOOL,
    second_signature BOOL,
    content TEXT,

    CONSTRAINT fk_first_company
    FOREIGN KEY (first_company)
    REFERENCES companies (id),

    CONSTRAINT fk_second_company
    FOREIGN KEY (second_company)
    REFERENCES companies (id)
);
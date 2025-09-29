CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    email    VARCHAR(64) UNIQUE NOT NULL CHECK ( length(email) >= 6),
    password VARCHAR(128)       NOT NULL CHECK ( length(password) >= 6),
    nickname VARCHAR(64)        NOT NULL CHECK ( length(nickname) >= 6)
);

CREATE TABLE posts
(
    id      SERIAL PRIMARY KEY,
    content VARCHAR(256) NOT NULL CHECK ( length(content) > 0),
    user_id BIGINT REFERENCES users (id)
);
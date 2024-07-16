CREATE TABLE IF NOT EXISTS users
(
    id SERIAL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS posts
(
    id SERIAL,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    published TIMESTAMP WITH TIME ZONE NOT NULL,

    PRIMARY KEY (id)
)
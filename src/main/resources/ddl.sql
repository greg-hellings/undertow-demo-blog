CREATE TABLE posts (
	id INTEGER IDENTITY PRIMARY KEY,
	title VARCHAR,
	summary CLOB,
	body CLOB
)

CREATE TABLE comments (
	id INTEGER IDENTITY PRIMARY KEY,
	poster VARCHAR,
	body CLOB,
	parent INTEGER,
	post INTEGER
)
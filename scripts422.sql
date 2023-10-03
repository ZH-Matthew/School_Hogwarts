CREATE TABLE human
(
    name                   TEXT PRIMARY KEY,
    age                    INTEGER NOT NULL,
    availability_of_rights BOOLEAN NOT NULL,
    auto_id                INTEGER REFERENCES auto (id)
);

CREATE TABLE auto
(
    id    INTEGER PRIMARY KEY,
    brand TEXT  NOT NULL,
    model TEXT  NOT NULL,
    price MONEY NOT NULL
);

SELECT human.name, human.age, human.availability_of_rights, auto.brand, auto.model, auto.price
FROM human
         INNER JOIN auto ON human.auto_id = auto.id
-- liquibase formatted sql

-- changeset mzhitenev:1
CREATE INDEX students_name_index ON student (name);

-- changeset mzhitenev:2
CREATE INDEX facultys_name_color_index ON faculty (name,color);
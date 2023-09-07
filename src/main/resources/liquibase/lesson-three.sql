--liquibase formatted sql

--changeset ravishev:1
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*)from pg_catalog.pg_tables t inner join pg_indexes i on i.tablename = t.tablename where t.tablename = 'students'and i.indexname ='idx_students_name'
--rollback DROP INDEX IDX_students_name
CREATE INDEX IDX_students_name ON students(name);

--liquibase formatted sql

--changeset ravishev:2
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 select count(*)from pg_catalog.pg_tables t inner join pg_indexes i on i.tablename = t.tablename where t.tablename = 'faculties' and i.indexname ='idx_faculties_name_and_color'
--rollback DROP INDEX IDX_faculties_name_and_color
CREATE UNIQUE INDEX IDX_faculties_name_and_color ON faculties(name,color);
CREATE USER mockless_service;
CREATE ROLE mockless_role;
GRANT CREATE ON DATABASE postgres TO mockless_role;
GRANT mockless_role to mockless_service;
ALTER ROLE mockless_role SET search_path = mockless;

create schema mockless;
alter schema mockless owner to mockless_service;
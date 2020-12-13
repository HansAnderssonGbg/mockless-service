DO
$do$
    BEGIN
        IF NOT EXISTS (
                SELECT FROM pg_catalog.pg_roles  -- SELECT list can be empty for this
                WHERE  rolname = 'mockless_role') THEN
            CREATE ROLE mockless_role;
        END IF;
        IF NOT EXISTS (
                SELECT FROM pg_catalog.pg_roles  -- SELECT list can be empty for this
                WHERE  rolname = 'mockless_service') THEN
            CREATE USER mockless_service;
            GRANT CREATE ON DATABASE postgres TO mockless_role;
            GRANT mockless_role to mockless_service;
        END IF;
    END
$do$;

ALTER ROLE mockless_role SET search_path = mockless;

alter schema mockless owner to mockless_service;
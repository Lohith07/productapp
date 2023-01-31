CREATE USER "party_c" WITH LOGIN PASSWORD 'my_password';
CREATE SCHEMA "party_c_schema";
GRANT USAGE, CREATE ON SCHEMA "party_c_schema" TO "party_c";
GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES ON ALL tables IN SCHEMA "party_c_schema" TO "party_c";
ALTER DEFAULT privileges IN SCHEMA "party_c_schema" GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES ON tables TO "party_c";
GRANT USAGE, SELECT ON ALL sequences IN SCHEMA "party_c_schema" TO "party_c";
ALTER DEFAULT privileges IN SCHEMA "party_c_schema" GRANT USAGE, SELECT ON sequences TO "party_c";
ALTER ROLE "party_c" SET search_path = "party_c_schema";
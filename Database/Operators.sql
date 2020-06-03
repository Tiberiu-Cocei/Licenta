CREATE FUNCTION bicycle_status_compare(bicycle_status, text)
RETURNS boolean
AS '
select cast($1 as text) = $2;
'
LANGUAGE sql IMMUTABLE;

CREATE OPERATOR = (
	leftarg = bicycle_status,
	rightarg = text,
	procedure = bicycle_status_compare
);
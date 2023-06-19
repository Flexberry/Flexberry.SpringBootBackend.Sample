-- wal_level determines how much information is written to the Write-Ahead Log.
-- logical adds information necessary to support logical decoding.
ALTER SYSTEM SET wal_level = logical;
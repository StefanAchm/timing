@echo off
REM Import PostgreSQL dump files into a database, with password support

:: ——— Configuration ———
set DB_NAME=timer
set DB_USER=postgres
set DB_PASS=password

:: Define the directory where the dump files are located
set DUMP_DIR=C:\path\to\dumps

:: Tell psql to use this password
set PGPASSWORD=%DB_PASS%

:: ——— Import dumps ———
psql -U %DB_USER% -d %DB_NAME% -f "%DUMP_DIR%\competitors.sql"
psql -U %DB_USER% -d %DB_NAME% -f "%DUMP_DIR%\rounds.sql"
psql -U %DB_USER% -d %DB_NAME% -f "%DUMP_DIR%\competitor_rounds.sql"

:: Clean up the password from the environment (optional but more secure)
set PGPASSWORD=
set DB_PASS=

echo Done.

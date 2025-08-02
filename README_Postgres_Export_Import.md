# PostgreSQL Database Export & Import Guide

This guide explains how to **export** a PostgreSQL database from a Docker container and **import** it into an existing PostgreSQL instance running directly on a Windows machine.

---

## 📤 Exporting from Docker Container

### 🛠 Prerequisites
- Docker installed and running
- PostgreSQL running in a Docker container
- Access to the container (username/password)
- Terminal access on your system

### 🔍 1. Identify Container Name
List running containers:
```bash
docker ps
```

### 📦 2. Run pg_dump to Export the Database
Replace values with your actual setup:
```bash
docker exec -t <container_name> pg_dump -U <username> <database_name> > ./dump.sql
```

**Example:**
```bash
docker exec -t my_postgres pg_dump -U postgres mydb > ./mydb_dump.sql
```

### 🗜️ (Optional) Compress the Dump
```bash
docker exec -t my_postgres pg_dump -U postgres mydb | gzip > ./mydb_dump.sql.gz
```

---

## 📥 Importing into Windows PostgreSQL

### 🛠 Prerequisites
- PostgreSQL installed on Windows
- `psql` command available in your `PATH`
- Target database already created (empty)

### 📂 1. Place the Dump File
Copy `mydb_dump.sql` (or unzip `mydb_dump.sql.gz`) to a known location, e.g.:

```
C:\db-dumps\mydb_dump.sql
```

### 🧭 2. Open Command Prompt
Use the Start menu or press `Win + R`, then type:
```
cmd
```

### 🧾 3. Run the Import Command
```cmd
psql -U <username> -d <database_name> -f "C:\path\to\mydb_dump.sql"
```

```shell
@echo off
REM Import PostgreSQL dump files into a database

set DB_NAME=mydb
set DB_USER=postgres
set DUMP_DIR=C:\Users\user\OneDrive\Private\Aktuell\00_Allgemein\Sonstiges\KIOT-Bouldercup-timer\2024-db

psql -U %DB_USER% -d %DB_NAME% -f "%DUMP_DIR%\competitors.sql"
psql -U %DB_USER% -d %DB_NAME% -f "%DUMP_DIR%\rounds.sql"
psql -U %DB_USER% -d %DB_NAME% -f "%DUMP_DIR%\competitor_rounds.sql"
```

**Example:**
```cmd
psql -U postgres -d mydb -f "C:\db-dumps\mydb_dump.sql"
```

### 🔑 4. Enter Password (if prompted)

---

## 🧪 Verifying the Import
You can use `psql` or **pgAdmin** to check the tables and data in your imported database.

---

## ❗ Troubleshooting

### `psql` Not Recognized
Add PostgreSQL `bin` folder to your system PATH:
```
C:\Program Files\PostgreSQL\<version>\bin
```

### Permission Issues
Make sure the target database exists and the user has the necessary privileges.

---

## ✅ Summary

| Task    | Tool       | Platform       |
|---------|------------|----------------|
| Export  | `pg_dump`  | Docker/Linux   |
| Import  | `psql`     | Windows Native |

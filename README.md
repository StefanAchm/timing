# KIOT Bouldercup Timer

# 🚀 App Installation Guide

This guide explains how to set up and run the app using Docker Compose.

## 🛠️ Prerequisites

- [Docker](https://www.docker.com/) must be installed and running on your machine.
- Docker Compose (usually bundled with Docker Desktop)
- A modern web browser (e.g., Chrome, Firefox)

## 📦 Setup Instructions

1. **Start Docker**

   Make sure Docker is running on your system before continuing.

2. **Download the Configuration File**

   Download the [docker-compose.yml](docker-compose.yml) file and save it to a local directory on your machine.

3. **(OPTIONAL) Adjust the Environment Variable**

   Open the downloaded [docker-compose.yml](docker-compose.yml) file in a text editor and locate the following line:

   ```yaml
   - VUE_APP_API_BASE_URL=http://localhost:8080/timer/api
   ```

   If you're running the backend on a different machine, replace `localhost` with the correct IP address of the host
   machine running the backend API.

4. **Open a Terminal**

   Navigate to the directory where [docker-compose.yml](docker-compose.yml) is saved.Then open a terminal or command prompt in that folder.
   On Windows, you may want to run it as administrator.

5. **Pull Docker Images**

   Run the following command to pull the latest required images:

   ```bash
   docker-compose pull
   ```
   
    This command will download the necessary Docker images for the application. It may take a few minutes depending on your internet speed.

6. **Start the Application**

   Once pulling is complete, start the application using:

   ```bash
   docker-compose up -d
   ```

7. **Access the App in Your Browser**

   After a few seconds, open your browser and navigate to: http://localhost:8081/#/.
   The app should now be up and running!

   **NOTE:** If you are running the backend on a different machine, replace `localhost` with the IP address of that machine (see step 3).

## 🔄 Updating the Application

If the app is already installed and new Docker images are available, update by running:

```bash
docker-compose pull
docker-compose up -d
```
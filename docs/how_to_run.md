# **Running the Application**
 To run the application, follow the instructions below. Make sure you have a compatible Java version (Java 17) installed on your system.

## 1. Set Java Version
Set the Java version to 17 for this application. Make sure you have Java 17 installed and configured properly on your system.

## 2. Open MySQL Database
Open your MySQL database management tool and create a new schema using the provided script. You can find the **scripts.sql** file in the resources directory. Execute the following script to create the schema:

CREATE SCHEMA db_movies;

This will create a new schema named **db_movies** in your MySQL database.

## 3. Configure Application Properties
You will need an API key to call the OMDB Movies API. Obtain an API key from OMDB API. Set the API key in the application.properties file under the property **OMDB_API_KEY**.

Additionally, set the MySQL database properties in the **application.properties** file:

- MYSQL_HOST=localhost
- MYSQL_USER=mysql-user-here
- MYSQL_PASSWORD=mysql-user-password-here

Replace **localhost** with your MySQL host, **mysql-user-here** with the MySQL username, and **mysql-user-password-here** with the MySQL user's password.

## 4. Start the Application
Open your preferred integrated development environment (IDE) such as Eclipse or IntelliJ (recommended). Import this project as a Maven project. The IDE will download all the necessary dependencies required for the project.

Start the application by running the main application class MoviesApplication.java from your IDE.

**Docker Instructions (Optional)**
If you prefer to run the application using Docker, follow these instructions:

- Ensure that Docker is installed and running on your system.
- Open a terminal or command prompt and navigate to the project's main directory where the Dockerfile is located.
- Run the following commands to create a Docker image and run the container:

    - docker build -t movies-application .
    - docker run -p 8080:8080 movies-application

Note:
- It would require some configuration to connect with local machine database(not going to details).
- Replace 8080 with the desired port number if needed.

## 5. Create Super Admin User
After successfully starting the application, the database tables will be automatically created. To create the initial super admin user, execute the following script:


INSERT INTO db_movies.users (email, password, roles, user_name) VALUES ('superadmin@gmail.com', '$2a$10$AoH4RE0tmBhmxa3JINjtjOUk2Ict90Hsz/EYcCbGDC5MWw99TwiFO', 'Role_SUPER_ADMIN', 'superadmin');

This script creates a super admin user with the following credentials:
- **Username:** superadmin
- **Password:** superadmin

**Note:** The script will only be executed once after the application starts for the first time. The scripts are available in the resource folder (**scripts.sql**).

After executing the script, you can use the super admin credentials to obtain a token for later API calls.

Congratulations! You have successfully set up and started the application. Now you can proceed with using the provided APIs and interacting with the application.
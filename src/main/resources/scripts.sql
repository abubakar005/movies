-- Execute this before starting the application
CREATE SCHEMA db_movies;

-- Execute this after starting the application [After successfully starting the application, the necessary database tables will be created automatically]
INSERT INTO db_movies.users (email, password, roles, user_name)
VALUES ('superadmin@gmail.com', '$2a$10$AoH4RE0tmBhmxa3JINjtjOUk2Ict90Hsz/EYcCbGDC5MWw99TwiFO', 'Role_SUPER_ADMIN', 'superadmin');
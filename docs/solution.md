# **Implementation Details**
This document provides an overview of the implementation details for the application. The application is built using Spring Boot and utilizes a MySQL database for data storage. Spring Security is implemented with JSON Web Token (JWT) for user access control based on tokens.

## **Technology Stack**
The application is built using the following technologies:

- Spring Boot
- Java 17
- Spring Security with JWT
- Actuator
- Open API Documentation (Swagger)
- MySQL Database
- Docker

## **Database Schema**
The application uses three tables in the MySQL database:

1. **Users:** Stores application users' data.
2. **Movies:** Contains information about all the movies.
3. **Movie_Ratings:** Stores user ratings for movies.

## **Super Admin Scripts**
To create Admin user with proper authentication, refer to the **'scripts.sql'** file located in the resources directory. It will be used to create further users after getting token by providing **username** and **password**.

## **Application Initialization**
Upon starting the application, a Map is loaded for each movie from the provided CSV file to determine whether it has won an Oscar award or not. This information is used later when processing API requests. The application does not insert any pre-filled data into the database. Instead, the database grows with the increasing number of requests.

## **API Endpoints**
The following API endpoints are available:

1. <font size="4">Authentication:</font> **'POST /api/v1/user/authenticate'**

- Authenticates the user with a valid username and password.
- Returns a JWT token in the response for subsequent API calls.

2. <font size="4">User Creation:</font> **'POST /api/v1/user/create'**

- Creates a new user with the provided details.

3. <font size="4">Verify Movie's Award Status:</font> **'GET /api/v1/movie/{movie-title}/best-picture-award'**

- Verifies whether a movie has won a 'Best Picture' Oscar award.
* First checks the database for the movie's award status.
- If the movie does not exist in the database, calls the OMBD Movies API (**'http://www.omdbapi.com/?apikey={your-api-key}&t={movie-title}'**) to retrieve the movie's data.
- Checks the loaded Map for the award status of the movie (loaded at application startup only for the category 'Best Picture').
- Returns the response and saves the movie's data into the database.

4. <font size="4">Rate a Movie:</font> **'POST /api/v1/movie/{movie-title}/rating?rating=9'**

- Allows a user to rate a movie by providing the movie title and rating (1 to 10, whole numbers only).
- Verifies the movie's data in the 'movies' table.
- Checks the 'movie_ratings' table to determine whether the user has already rated the movie.
- If the user has already rated the movie, recalculates the overall rating and updates the user's rating.
- If the user has not rated the movie before, creates a new record in the database and calculates the overall rating accordingly.
- If the movie's data does not exist in the local database, retrieves the data by calling the OMBD Movies API, calculates the new overall rating, and saves the updated information into the database.

5. <font size="4">Get Top Rated Movies:</font> **'GET /api/v1/movie/top-rated'**

    + Returns the top 10 highest-rated movies, ordered by the 'officeBox' value.
   - **Note:** If there are less than 10 records in the database, all available movies will be returned in the response. The database builds up over time as more requests are made for different movies.
   Please replace **'{movie-title}'** with the actual title of the movie when making requests to the corresponding endpoints.

**Note:** 
- The API endpoints are accessible on **'localhost:8080'** followed by the respective URL paths mentioned above.
- The token validity is set to 10 minutes (equivalent to milliseconds). You can modify this configuration by changing the value of the property **'jwtExpirationInMs'** in the application.properties.
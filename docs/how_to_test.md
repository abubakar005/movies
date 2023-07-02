# Testing the Application
After starting the application following the instructions in the **how_to_run.md** file, you can test the application using the provided APIs. 

Please note that at the start, you only have the **super admin user** in the database. You will need the credentials of the super admin user to obtain a token by calling the API **POST /api/v1/user/authenticate**. Once you have the token, you can use it to call the other 4 APIs for different purposes. 

Remember to include the token in the Authorization header as a Bearer token.

## **1. Create a New User**
To create a new user, make a POST request to the API **/api/v1/user/create** with the following request body:

```JSON
{
    "username": "user1",
    "email": "user1@gmail.com",
    "password": "user1",
    "roles": "Role_USER"
}
```

This API will create a new user with the provided credentials. The newly created user, which can be used for subsequent API calls.

**Note:** Role-based access is not managed in this application. Any user with proper token can call the 4 APIs.

## **2. Verify if a Movie has won the 'Best Picture' Award**
To verify whether a movie has won the 'Best Picture' award, make a GET request to the API **/api/v1/movie/{movie-title}/best-picture-award**, replacing **{movie-title}** with the title of the movie you want to verify.

- The API will first check the database to see if the movie exists. If it does not exist, it will call the **OMDB Movies API** using the provided OMDB API key. 
- If the movie details are valid and it has won the 'Best Picture' award (based on the map loaded at the start of the application), the response will indicate that the movie has won the award. 
- The movie details will also be saved in the database for future use.

## **3. Give a Rating to a Movie**
To give a rating to a movie, make a POST request to the **API /api/v1/movie/{movie-title}/rating?rating=9**, replacing **{movie-title}** with the title of the movie and **9** with the desired rating (ranging from 1 to 10, whole numbers only).

- Upon receiving the request, the API will verify if the movie exists in the local database. If it does not exist, it will call the **OMDB Movies API** to retrieve the movie details and save them in the database. 
- It will then check the **movie_rating** table to see if the user has already rated the same movie. If not, a new record will be inserted with the **logged-in user's (token user)** rating. 
- If the user has already rated the movie, the API will update the existing rating. In both cases, the movie's overall rating will be calculated and saved in the database.

## **4. Get the List of Top-Rated Movies**
To retrieve the list of top 10 rated movies, make a GET request to the API **/api/v1/movie/top-rated**.

The API will return the top-rated movies, ordered by their overall ratings. If the number of records is less than 10, all movies will be included in the response.

**Note:** 
- I have created a Postman collection and placed it in the **"postman"** folder in the main directory of the project.
- Initially, the database does not have any data, and it builds up over time as more requests are made for different movies.

You can now test the functionality of the application using the provided APIs. Feel free to explore and interact with the application to experience its features.
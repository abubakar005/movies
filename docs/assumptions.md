# Assumptions
During the development of the application, several assumptions were made to ensure the smooth implementation and functionality of the system. These assumptions are listed below:

1. **Movie Verification:** The movie verification process involves retrieving movie details from the given **OMDB API**, but the award status is obtained from the shared CSV file. It is assumed that the movie titles in the **"Nominee"** column against the category **"Best Picture"** in the CSV file correspond to the same movie titles obtained from the API.

2. **Movie Rating:** The application allows users to rate movies by providing a rating through the respective API. It is assumed that the rating values range from 1 to 10 and are whole numbers only.

3. **Top Rated Movies:** The API to retrieve the list of top-rated movies has been implemented. It is assumed that the calculation of movie ratings is accurate and provides the desired results. The list of movies will be sorted based on their ratings, with the highest-rated movies appearing first. In case of a tie in ratings, the sorting will be based on the box office value of the movies.

4. **Token-Based Authentication:** The application expects the caller to provide a token for authentication and authorization purposes. Spring Security has been implemented using JWT (JSON Web Tokens) to handle token-based authentication.

5. **Validity of Data:** It is assumed that the data provided in the CSV file is valid and consistent. The application relies on this data to verify movie awards and perform other related operations.

6. **Availability of OMDB API:** The assumption is made that the **OMDB API**, used to retrieve movie details, will be available and accessible during the runtime of the application. Proper error handling and fallback mechanisms may need to be implemented in case of any API connectivity issues.

7. **Handling Duplicate Movies:** In the CSV file, there were instances where duplicate movies existed with different award status (Yes/No). To handle this, a decision was made to prioritize the later record with the same movie name while uploading the data into the Map during application startup. The award status stored in the Map is based on the later entry.

The provided list of movies from the CSV file, including "Moulin Rouge," "Heaven Can Wait," "Romeo and Juliet," "Mutiny on the Bounty," "Cleopatra," "Metro-Goldwyn-Mayer," "Fox," and "Paramount Famous Lasky," is assumed to be accurate and relevant for the application's functionality.

These assumptions were made based on the available information and project requirements to ensure the successful development and deployment of the application. It is important to consider these assumptions while testing, maintaining, and further enhancing the system.
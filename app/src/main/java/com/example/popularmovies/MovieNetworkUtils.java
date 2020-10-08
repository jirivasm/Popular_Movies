package com.example.popularmovies;


import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MovieNetworkUtils {


    final static String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w185";

    MovieNetworkUtils() {
    }


    public static List<Movie> extractMoviesJson(String JsonResponse) {
        if (JsonResponse.isEmpty())
            return null;

        ArrayList<Movie> movieList = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(JsonResponse);
            JSONArray resultsArray = object.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject movie = resultsArray.getJSONObject(i);
                String movieResource = BASE_IMAGE_URL + movie.getString("poster_path");


                int movieID = movie.getInt("id");


                movieList.add(new Movie(movieResource, movieID));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return movieList;
    }
    public static Movie extractMovieDetailJson(String[] JsonResponse) {
        String movieResource = "";
        String movieName = "";
        String movieReleaseDate = "";
        double movieRating = 0;
        String movieDescription = "";
        int movieID = 0;
        List<Reviews> ReviewsList = new ArrayList<>();
        List<String> TrailersList = new ArrayList<>();

        if (JsonResponse[0].isEmpty() && JsonResponse[1].isEmpty() && JsonResponse[2].isEmpty())
            return null;
        try {
            JSONObject details = new JSONObject(JsonResponse[0]);

            movieResource = BASE_IMAGE_URL + details.getString("poster_path");
            movieName = details.getString("original_title");
            movieReleaseDate = details.getString("release_date");
            movieRating = details.getDouble("vote_average");
            movieDescription = details.getString("overview");
            movieID = details.getInt("id");

            //getting the reviews
            JSONObject reviews = new JSONObject(JsonResponse[1]);

            JSONArray resultsArray = reviews.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject movie = resultsArray.getJSONObject(i);
                String reviewAuthor = movie.getString("author");
                String reviewContent = movie.getString("content");

                ReviewsList.add(new Reviews(reviewAuthor, reviewContent));
            }
            //Getting The Trailers
            JSONObject trailers = new JSONObject(JsonResponse[2]);

            JSONArray youtuberesultsArray = trailers.getJSONArray("youtube");
            for (int i = 0; i < youtuberesultsArray.length(); i++) {

                JSONObject movie = youtuberesultsArray.getJSONObject(i);
                String youtubeKey = movie.getString("source");

                TrailersList.add(youtubeKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Movie(movieResource, movieName, movieReleaseDate, movieDescription, movieRating, movieID, false, ReviewsList,TrailersList);
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            return null;
        }
        return url;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static List<Movie> fetchMovieList(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(MovieNetworkUtils.class.getSimpleName(), "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create a List of movies object


        // Return the List of Movies
        return extractMoviesJson(jsonResponse);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Movie fetchMovieDetails(String requestUrlDetails, String requestUrlReviews, String requestUrlTrailers) {
        // Create URL object
        URL urlDetails = createUrl(requestUrlDetails);
        URL urlReviews = createUrl(requestUrlReviews);
        URL urlTrailers = createUrl(requestUrlTrailers);

        // Perform HTTP request to the URL and receive a JSON response back

        String[] jsonResponse = {requestUrlDetails, requestUrlReviews,requestUrlTrailers};

        try {
            jsonResponse[0] = makeHttpRequest(urlDetails);
            jsonResponse[1] = makeHttpRequest(urlReviews);
            jsonResponse[2] = makeHttpRequest(urlTrailers);

        } catch (IOException e) {
            Log.e(MovieNetworkUtils.class.getSimpleName(), "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create a List of movies object


        // Return the List of Movies
        return extractMovieDetailJson(jsonResponse);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        //Get the connection making sure it is successful
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful,
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(MovieNetworkUtils.class.getSimpleName(), "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(MovieNetworkUtils.class.getSimpleName(), "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}

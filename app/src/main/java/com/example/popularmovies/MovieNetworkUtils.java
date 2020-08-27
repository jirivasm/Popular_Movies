package com.example.popularmovies;

import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MovieNetworkUtils {


    final static String BASE_IMAGE_URL="https://image.tmdb.org/t/p/w185";
    MovieNetworkUtils(){}

    public static List<Movie> extractMoviesJson(String JsonResponse)
    {
        if(JsonResponse.isEmpty())
            return null;

        //TODO: Make JSON parsing here to extract the list of movies.
        ArrayList<Movie> movieList = new ArrayList<>();

        try{
            JSONObject object = new JSONObject(JsonResponse);
            JSONArray resultsArray = object.getJSONArray("results");
            Movie film = new Movie();
            for (int i = 0;i<resultsArray.length();i++)
            {

               JSONObject movie = resultsArray.getJSONObject(i);
               String movieResource = BASE_IMAGE_URL + movie.getString("poster_path");


               String movieName = movie.getString("original_title");


               String movieReleaseDate = movie.getString("release_date");


               String movieDescription = movie.getString("overview");


               double movieVoteAverage = movie.getDouble("vote_average");


               int movieID = movie.getInt("id");


               movieList.add(new Movie(movieResource,movieName,movieReleaseDate,movieDescription,movieVoteAverage,movieID));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return movieList;
    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {

        }
        return url;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static List<Movie> fetchMovieData(String requestUrl) {
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
        List<Movie> news = extractMoviesJson(jsonResponse);

        // Return the List of Movies
        return news;
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

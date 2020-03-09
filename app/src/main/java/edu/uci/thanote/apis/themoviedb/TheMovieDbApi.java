package edu.uci.thanote.apis.themoviedb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.Random;

public interface TheMovieDbApi {
    /**
     * Search for movies.
     *
     * @param apiKey string
     *               default: <<api_key>>
     *               required
     * @param query  string
     *               Pass a text query to search. This value should be URI encoded.
     *               minLength: 1
     *               required
     * @return A Movie object
     * @see <a href=https://developers.themoviedb.org/3/search/search-movies>Search Movies</a>
     */
    @GET("search/movie")
    Call<TMDbMoviesResponse> getMovieBySearching(@Query("api_key") String apiKey,
                                                 @Query("query") String query);

    /**
     * Get the top rated movies on TMDb.
     *
     * @param apiKey string
     *               default: <<api_key>>
     *               required
     * @return A List of top-rated Movie objects
     * @see <a href=https://developers.themoviedb.org/3/movies/get-top-rated-movies>Get Top Rated</a>
     */
    @GET("movie/top_rated")
    Call<TMDbMoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    /**
     * Get a list of the current popular movies on TMDb. This list updates daily.
     *
     * @param apiKey string
     *               default: <<api_key>>
     *               required
     * @param page   integer
     *               Specify which page to query.
     *               minimum: 1
     *               maximum: 500 (docs said 1000 but not true)
     *               default: 1
     *               optional
     * @return A List of popular Movie objects
     * @see <a href=https://developers.themoviedb.org/3/movies/get-popular-movies>Get Popular</a>
     */
    @GET("movie/popular")
    Call<TMDbMoviesResponse> getPopularMovies(@Query("api_key") String apiKey,
                                              @Query("page") int page);

    static int getRandomPageNumber() {
        final int PAGE_MIN = 1;
        final int PAGE_MAX = 500;
        return PAGE_MIN + new Random().nextInt(PAGE_MAX);
    }

    /**
     * Get the daily or weekly trending items. The daily trending list tracks items over the period of a day while items have a 24 hour half life. The weekly list tracks items over a 7 day period, with a 7 day half life.
     *
     * @param apiKey     string
     *                   default: <<api_key>>
     *                   required
     * @param mediaType  Allowed Values: all, movie, tv, person
     * @param timeWindow Allowed Values: day, week
     * @return A List of trending objects
     * @see <a href=https://developers.themoviedb.org/3/trending/get-trending>Get Trending</a>
     */
    @GET("trending/{media_type}/{time_window}")
    Call<TMDbMoviesResponse> getTrendingItems(@Query("api_key") String apiKey,
                                              @Path("media_type") String mediaType,
                                              @Path("time_window") String timeWindow);
}
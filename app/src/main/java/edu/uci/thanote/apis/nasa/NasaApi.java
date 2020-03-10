package edu.uci.thanote.apis.nasa;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Joxon on 2020-03-09.
 */
public interface NasaApi {

    @GET("planetary/apod?")
    Call<NasaApod> getAstronomyPictureOfTheDay(@Query("api_key") String apiKey);

    /**
     * @param apiKey string	DEMO_KEY	api.nasa.gov key for expanded usage
     * @param hd     bool	False	Retrieve the URL for the high resolution image
     * @param date   YYYY-MM-DD	today	The date of the APOD image to retrieve
     *               "Date must be between Jun 16, 1995 and TODAY."
     * @return APOD
     */
    @GET("planetary/apod?")
    Call<NasaApod> getAstronomyPictureOfTheDay(@Query("api_key") String apiKey,
                                               @Query("hd") boolean hd,
                                               @Query("date") String date);
}

package hu.ait.android.weatherinfoapplication.network;

/**
 * Created by johnc on 11/20/2017.
 */

import hu.ait.android.weatherinfoapplication.data.WeatherResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by johnc on 11/13/2017.
 */

public interface CityAPI {

    @GET("data/2.5/weather")
    Call<WeatherResult> getWeather(@Query("q") String city, @Query("units") String units, @Query("appid") String appid);
}

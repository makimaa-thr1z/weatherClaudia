package nobara.open.weather
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val current: CurrentWeather
)
data class CurrentWeather(
    val time: String,
    val temperature_2m: Double,
    val wind_speed_10m: Double
)
    //        if(temperature >= 35) comment = "very hot";
    //        else if(temperature >= 30) comment = "mostly sunny";
    //        else if(temperature >= 25) comment = "partly cloudy";
    //        else if(temperature >= 20) comment = "mostly cloudy";
    //        else if(temperature >= 15) comment = "overcast clouds";
    //        else if(temperature >= 10) comment = "light rain";
    //        else if(temperature >= 5) comment = "heavy rain";
    //        else comment = "freezin' cold";
interface OpenMeteoApi {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current") current: String
    ): WeatherResponse
}
class WeatherAPI
{
    private val retrofit = Retrofit.Builder().baseUrl("https://api.open-meteo.com/").addConverterFactory(GsonConverterFactory.create()).build();
    private val api = retrofit.create(OpenMeteoApi::class.java);
    suspend fun fetchWeather(lat: Double, lon: Double): WeatherResponse
    {
        return api.getWeather(
            lat = lat,
            lon = lon,
            current = "temperature_2m,wind_speed_10m"
        )
    }
}
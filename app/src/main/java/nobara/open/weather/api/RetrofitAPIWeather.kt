package nobara.open.weather.api
import retrofit2.http.GET
import retrofit2.http.Query
interface RetrofitAPIWeather {
    @GET("forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current")
        current: String = "temperature_2m,relative_humidity_2m,rain,cloud_cover,weather_code,wind_speed_10m"
    ): ForecastResponse
}
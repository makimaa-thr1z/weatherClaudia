package nobara.open.weather.api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
data class WeatherDataFromAPI (
    val relative_humidity_2m: Int,
    val cloud_cover: Int,
    val weather_code: Int,
    val temperature_2m: Double,
    val apparent_temperature: Double,
    val rain: Double,
    val wind_speed_10m: Double
)
data class ForecastResponse(
    val latitude: Double,
    val longitude: Double,
    val current: WeatherDataFromAPI
)
object RetrofitWeatherClient {
    private const val BASE_URL = "https://api.open-meteo.com/v1/"
    val apiService: RetrofitAPIWeather by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitAPIWeather::class.java); };
}
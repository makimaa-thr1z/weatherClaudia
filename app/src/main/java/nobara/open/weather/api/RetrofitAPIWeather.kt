package nobara.open.weather.api
import android.content.Context
import android.content.Context.MODE_PRIVATE
import nobara.open.weather.BASIC_PREFS
import nobara.open.weather.TEMPERATURE_PREFERENCE
import retrofit2.http.GET
import retrofit2.http.Query
interface RetrofitAPIWeather {

    @GET("forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current")
        current: String =
            "temperature_2m,relative_humidity_2m,rain,cloud_cover,weather_code,wind_speed_10m",

        @Query("temperature_unit")
        temperatureUnit: String = "celsius"
    ): ForecastResponse
    fun getTemperatureUnit(context: Context): String
    {
        val prefs = context.getSharedPreferences(BASIC_PREFS, MODE_PRIVATE)
        return if(prefs.getInt(TEMPERATURE_PREFERENCE, 0) == 0)
            "celsius"
        else
            "fahrenheit"
    }
}
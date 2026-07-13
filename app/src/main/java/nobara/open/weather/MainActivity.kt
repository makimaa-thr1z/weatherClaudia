package nobara.open.weather
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// we need the location latitudes and longitudes
// because open-meteo depends on them instead of plain location names.
data class LocationLatitudes(
    val longitude: Float,
    val latitudes: Float
)
// "current":{"time":"2026-07-11T07:45","interval":900,"temperature_2m":29.4,
// "relative_humidity_2m":73,
// "rain":0.00,"cloud_cover":80,"surface_pressure":1007.0}}
data class WeatherAPIData(
    val temperature_2m: Float,
    val relative_humidity: Int,
    val rain: Float,
    val surface_pressure: Int
)
class MainActivity : AppCompatActivity() {
    // icons:
    private lateinit var cloudIconsArray: IntArray;
    private lateinit var cloudIcon: ImageView;
    private lateinit var refreshIcon: ImageButton;
    // texts:
    private lateinit var countryArea: TextView;
    private lateinit var temperature: TextView;
    private lateinit var weatherComment: TextView;
    private lateinit var rainOmeter: TextView;
    private lateinit var surfacePressure: TextView;
    private lateinit var humidity: TextView;
    fun setCloudicon(arrayIndex: Int)
    {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        // icons:
        cloudIconsArray = intArrayOf(R.drawable.overcast, R.drawable.drizzle_rain_shower, R.drawable.sunny, R.drawable.rain,
            R.drawable.winter, R.drawable.drizzle_rain_shower, R.drawable.partlycloudy);
        cloudIcon = findViewById(R.id.cloudIcon);
        refreshIcon = findViewById(R.id.refreshIcon);
        // texts:
        countryArea = findViewById(R.id.chosenCountryArea);
        temperature = findViewById(R.id.temperatureText);
        weatherComment = findViewById(R.id.weatherComment);
        rainOmeter = findViewById(R.id.rainProbability);
        surfacePressure = findViewById(R.id.windSpeed);
        humidity = findViewById(R.id.humidity);
        temperature.text = getString(
            R.string.temperature_format, 18.20,
            getString(R.string.degree)
        )
        cloudIcon.setOnClickListener {
            cloudIcon.setImageResource(cloudIconsArray[cloudIconsArray.indices.random()]);
        }
        refreshIcon.setOnClickListener {
            Toast.makeText(this, R.string.refreshing, Toast.LENGTH_SHORT).show();
            temperature.text = "";
            weatherComment.text = "";
            rainOmeter.text = "";
            surfacePressure.text = "";
            humidity.text = "";
        }
    }
}
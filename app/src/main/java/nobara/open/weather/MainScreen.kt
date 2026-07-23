// yeah, this code looks ugly as hell and this shit triggers ocd.
// i'm genuinely sorry.
package nobara.open.weather
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import nobara.open.weather.api.RetrofitWeatherClient
class MainScreen : Fragment()
{
    // icons:
    private lateinit var cloudIcon: ImageView;
    private lateinit var refreshIcon: ImageButton;
    // texts:
    private lateinit var countryArea: TextView;
    private lateinit var temperature: TextView;
    private lateinit var weatherComment: TextView;
    private lateinit var rainOmeter: TextView;
    private lateinit var humidity: TextView;
    private lateinit var windSpeed: TextView;
    private lateinit var lastUpdated: TextView;
    private lateinit var rootLayout: View;
    private lateinit var sharedPref: SharedPreferences;
    private lateinit var calendarData: Calendar;
    private lateinit var nobaraUtils: NobaraUtils;
    private var isUpdated: Boolean = false;
    fun setData(data: WeatherDisplayData, dataUpdate: SetDataHelper)
    {
        if(dataUpdate == SetDataHelper.SKIP_UPDATE)
        {
            countryArea.text = data.city
            temperature.text = getString(R.string.temperature_format, data.temperature.toString(), getString(R.string.degree));
            humidity.text = getString(R.string.temperature_format, data.humidity.toString(), getString(R.string.percentageSymbol));
            windSpeed.text = getString(R.string.temperature_format, data.wind.toString(), "km/h");
            rainOmeter.text = getString(R.string.temperature_format, data.rain.toString(), "mm");
            sharedPref.edit {
                putInt(LAST_DATE, calendarData.get(Calendar.DATE));
                putInt(LAST_MONTH, calendarData.get(Calendar.MONTH));
                putInt(LAST_YEAR, calendarData.get(Calendar.YEAR));
            }
            putComment(data.weatherCode);
        }
        else
        {
            sharedPref.edit {
                putString(CITY_NAME, data.city);
                putFloat(RAIN_DATA, data.rain.toFloat());
                putInt(HUMIDITY_DATA, data.humidity);
                putFloat(WIND_SPEED, data.wind.toFloat());
                putFloat(TEMPERATURE_DATA, data.temperature.toFloat());
                putInt(WEATHER_CODE, data.weatherCode);
            }
        }
    }
    fun putComment(weatherCode: Int)
    {
        when(weatherCode)
        {
            0 -> {
                cloudIcon.setImageResource(R.drawable.sunny);
                weatherComment.text = getString(R.string.sunny);
            }
            1 -> {
                cloudIcon.setImageResource(R.drawable.mainly_clear);
                weatherComment.text = getString(R.string.mostly_sunny);
            }
            2 -> {
                cloudIcon.setImageResource(R.drawable.partlycloudy);
                weatherComment.text = getString(R.string.party_cloudy);
            }
            3 -> {
                cloudIcon.setImageResource(R.drawable.overcast);
                weatherComment.text = getString(R.string.overcast);
            }
            45, 48 -> {
                cloudIcon.setImageResource(R.drawable.fog);
                weatherComment.text = getString(R.string.fog);
            }
            51, 53, 55, 61, 63, 80, 81 -> {
                cloudIcon.setImageResource(R.drawable.rain);
                weatherComment.text = getString(R.string.light_rain);
            }
            56, 57, 66, 67 -> {
                cloudIcon.setImageResource(R.drawable.rain);
                weatherComment.text = getString(R.string.freezing_rain);
            }
            65, 82 -> {
                cloudIcon.setImageResource(R.drawable.rain);
                weatherComment.text = getString(R.string.heavy_rain);
            }
            71, 73, 75, 77, 85, 86 -> {
                cloudIcon.setImageResource(R.drawable.winter);
                weatherComment.text = getString(R.string.snow);
            }
            95, 96, 99 -> {
                cloudIcon.setImageResource(R.drawable.ic_thunderstorm);
                weatherComment.text = getString(R.string.thunderstorm);
            }
            else -> {
                cloudIcon.setImageResource(R.drawable.overcast);
                weatherComment.text = getString(R.string.overcast);
            }
        }
        nobaraUtils.setTheme();
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_screen, container, false);
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        // init sharedPref here
        sharedPref = requireContext().getSharedPreferences(BASIC_PREFS, MODE_PRIVATE);
        // root layout:
        rootLayout = view.findViewById(R.id.main);
        //icons:
        cloudIcon = view.findViewById(R.id.cloudIcon);
        refreshIcon = view.findViewById(R.id.refreshIcon);
        // texts:
        countryArea = view.findViewById(R.id.chosenCountryArea);
        temperature = view.findViewById(R.id.temperatureText);
        weatherComment = view.findViewById(R.id.weatherComment);
        rainOmeter = view.findViewById(R.id.rainProbability);
        humidity = view.findViewById(R.id.humidity);
        windSpeed = view.findViewById(R.id.windSpeed);
        lastUpdated = view.findViewById(R.id.lastUpdatedText);
        calendarData = Calendar.getInstance();
        //init nobara:
        nobaraUtils = NobaraUtils(sharedPref, rootLayout, requireActivity() as MainActivity, requireContext());
        // location shits:
        refreshLocation();
        lastUpdated.text = getString(R.string.lst_updated, sharedPref.getInt(LAST_DATE, 1).toString(), sharedPref.getInt(LAST_MONTH, 1).toString(),
            sharedPref.getInt(LAST_YEAR, 1970).toString());
        // icon triggers:
        view.findViewById<ImageButton>(R.id.settingsIcon).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .hide(this).add(R.id.fragmentContainer, SettingsFragment()).addToBackStack(null).commit()
        }
        view.findViewById<ImageButton>(R.id.refreshIcon).setOnClickListener { isUpdated = false; refreshLocation(); }
    }
    fun refreshLocation()
    {
        // don't update unless isUpdated is false.
        if(isUpdated) return;
        // fixed the bug where the bottom tray value gets messed up.
        nobaraUtils.putLocationData();
        // check internet and get the location data and then pass it to open-meteo to get the stats!
        if(nobaraUtils.isInternetAvailable())
        {
            nobaraUtils.toastThisShit(getString(R.string.refreshingData));
            // first, we want to gather the location and set the shit to Osaka if this moron didn't put shit.
            viewLifecycleOwner.lifecycleScope.launch {
                val latitude = sharedPref.getString(LATITUDE, "34.6937")?.toDoubleOrNull() ?: 34.6937;
                val longitude = sharedPref.getString(LONGITUDE, "135.5022")?.toDoubleOrNull() ?: 135.5022;
                try {
                    val retroFitApi = RetrofitWeatherClient.apiService;
                    val unit = retroFitApi.getTemperatureUnit(requireContext());
                    val weather = retroFitApi.getForecast(latitude, longitude, METEO_DATA, unit);
                    val current = weather.current;
                    val data = WeatherDisplayData(
                        city = nobaraUtils.getCityName(latitude, longitude),
                        rain = current.rain,
                        humidity = current.relative_humidity_2m,
                        wind = current.wind_speed_10m,
                        temperature = current.temperature_2m,
                        weatherCode = current.weather_code
                    )
                    setData(data, SetDataHelper.UPDATE_DATA);
                    setData(data, SetDataHelper.SKIP_UPDATE);
                }
                catch(e: Exception) {
                    setData(
                        WeatherDisplayData(
                            city = sharedPref.getString(CITY_NAME, "Unknown") ?: "Unknown",
                            rain = sharedPref.getFloat(RAIN_DATA, 0f).toDouble(),
                            humidity = sharedPref.getInt(HUMIDITY_DATA, 0),
                            wind = sharedPref.getFloat(WIND_SPEED, 0f).toDouble(),
                            temperature = sharedPref.getFloat(TEMPERATURE_DATA, 0f).toDouble(),
                            weatherCode = sharedPref.getInt(WEATHER_CODE, 0)),
                        SetDataHelper.SKIP_UPDATE);
                }
            }
        }
        // if this- ughh we have to fetch the old data bruh.
        else
        {
            setData(
                WeatherDisplayData(
                    city = sharedPref.getString(CITY_NAME, "Unknown") ?: "Unknown",
                    rain = sharedPref.getFloat(RAIN_DATA, 0f).toDouble(),
                    humidity = sharedPref.getInt(HUMIDITY_DATA, 0),
                    wind = sharedPref.getFloat(WIND_SPEED, 0f).toDouble(),
                    temperature = sharedPref.getFloat(TEMPERATURE_DATA, 0f).toDouble(),
                    weatherCode = sharedPref.getInt(WEATHER_CODE, 0)), SetDataHelper.SKIP_UPDATE);
        }
        // update the data:
        nobaraUtils.setTheme();
        isUpdated = true;
    }
}
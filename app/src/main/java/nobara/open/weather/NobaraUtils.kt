package nobara.open.weather
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.location.LocationManagerCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.properties.Delegates
// app constrains or whatever:
const val SELECTED_THEME: String = "selectedTheme";
const val SETUP_STAT: String = "isSetupOver";
const val SETUP_CORD: String = "cordSetupFinished";
const val BASIC_PREFS: String = "basic_prefs";
const val USED_DEVICE_LOCATION: String = "coordinatesFromLocationData";
const val USED_DATA_FROM_APP: String = "coordinatesFromUserdata";
const val METEO_DATA: String = "temperature_2m,relative_humidity_2m,rain,cloud_cover,weather_code,wind_speed_10m";
const val LATITUDE: String = "latitude_data";
const val LONGITUDE: String = "longitude_data";
// these are saved inside sharedPreferences because we don't
// want the user to- ignore, i want to avoid crashes lol.
const val CITY_NAME: String = "cityName";
const val RAIN_DATA: String = "rain0meterData";
const val HUMIDITY_DATA: String = "humidityData";
const val WIND_SPEED: String = "windSpeedData";
const val TEMPERATURE_DATA: String = "temperatureData";
const val WEATHER_CODE: String = "weatherCloudyData";
// last updated count:
const val LAST_DATE: String = "weatherLastUpdatedDate";
const val LAST_MONTH: String = "weatherLastUpdatedMonth";
const val LAST_YEAR: String = "weatherLastUpdatedYeah";
// for throwing us back at MainScreen():
const val THROWBACK_HOME: String = "throwMeBackToWhereImSupposedToStay";
// enum for SetData():
enum class SetDataHelper { UPDATE_DATA, SKIP_UPDATE; }
// weather data:
data class WeatherDisplayData(
    val city: String,
    val rain: Double,
    val humidity: Int,
    val wind: Double,
    val temperature: Double,
    val weatherCode: Int
);
class NobaraUtils {
    private var sharedPref: SharedPreferences;
    private var rootLayout: View?;
    private var mainActivity: Activity;
    private var context: Context?;
    private var weatherCodes by Delegates.notNull<Int>();
    constructor(sharedPref: SharedPreferences, rootLayout: View?, mainActivity: Activity, context: Context?)
    {
        this.sharedPref = sharedPref;
        this.rootLayout = rootLayout;
        this.mainActivity = mainActivity;
        this.context = context;
        weatherCodes = sharedPref.getInt(WEATHER_CODE, 0);
    }
    fun toastThisShit(message: String)
    {
        if(context == null) return;
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    fun putLocationData() {
        if(context == null) return
        if(ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) return
        //
        if(!sharedPref.getBoolean(USED_DATA_FROM_APP, false) && isLocationEnabled())
        {
            val locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            location?.let {
                sharedPref.edit {
                    putString(LATITUDE, it.latitude.toString())
                    putString(LONGITUDE, it.longitude.toString())
                    putBoolean(USED_DEVICE_LOCATION, true)
                }
            }
        }
    }
    // i want this shit to be isolated from actual activity so i can skip the mess.
    fun setTheme()
    {
        val themeI = sharedPref.getInt(SELECTED_THEME, 0);
        val layout = rootLayout ?: return;
        when(themeI)
        {
            1 -> layout.setBackgroundResource(R.drawable.bg_hailstorm)
            2 -> layout.setBackgroundColor(ContextCompat.getColor(context!!, R.color.pitchBlackBackground))
            else -> {
                when(weatherCodes) {
                    0, 1, 2 -> layout.setBackgroundResource(R.drawable.bg_summer)
                    3, 45, 48 -> layout.setBackgroundResource(R.drawable.bg_foggy)
                    51, 53, 55,
                    56, 57,
                    61, 63, 65,
                    66, 67,
                    71, 73, 75, 77,
                    80, 81, 82,
                    85, 86,
                    95, 96, 99 -> layout.setBackgroundResource(R.drawable.bg_rain)
                    else -> layout.setBackgroundResource(R.drawable.bg_foggy)
                }
            }
        }
    }
    fun isInternetAvailable(): Boolean
    {
        val connectivityManager = mainActivity.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager;
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork);
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true;
    }
    suspend fun getCityName(latitude: Double, longitude: Double): String
    {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context!!, Locale.getDefault());
                val addresses = geocoder.getFromLocation(latitude, longitude, 1);
                addresses?.firstOrNull()?.locality ?: "Unknown";
            }
            catch(e: Exception)
            {
                "Unknown";
            }
        }
    }
    fun isLocationEnabled(): Boolean
    {
        if(context == null) return false;
        val locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager;
        return LocationManagerCompat.isLocationEnabled(locationManager);
    }
    fun hasAccessFinePermission(): Boolean
    {
        return (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }
    fun hasAccessCoarsePermission(): Boolean
    {
        return (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }
    object LocationPermHelper {
        const val PERMREQCODE = 0;
        val basicPermission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        fun requestFinePerm(activity: MainActivity?)
        {
            ActivityCompat.requestPermissions(activity!!, basicPermission, PERMREQCODE);
        }
    }
}
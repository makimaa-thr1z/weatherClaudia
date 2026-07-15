// yeah, this code looks ugly as hell and this shit triggers ocd.
// i'm genuinely sorry.
package nobara.open.weather
import android.Manifest
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    private lateinit var surfacePressure: TextView;
    private lateinit var humidity: TextView;
    private lateinit var rootLayout: View;
    private lateinit var sharedPref: SharedPreferences;
    private lateinit var weatherService: WeatherAPI;
    fun setDummyText()
    {
        countryArea.text = "Japan";
        temperature.text = getString(R.string.temperature_format, 18.5, getString(R.string.degree));
        surfacePressure.text = "100%";
        humidity.text = "100%";
        rainOmeter.text = "100%";
        weatherComment.text = "mostly sunny";
        cloudIcon.setImageResource(R.drawable.mainly_clear);
        //rootLayout.setBackgroundResource(R.drawable.bg_summer);
    }
    fun setTheme()
    {
        sharedPref = requireContext().getSharedPreferences("basic_prefs", MODE_PRIVATE);
        when(sharedPref.getInt("selectedTheme", 0))
        {
            0 -> rootLayout.setBackgroundResource(R.drawable.bg_summer);
            1 -> rootLayout.setBackgroundResource(R.drawable.bg_hailstorm);
            2 -> rootLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pitchBlackBackground));
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_screen, container, false);
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        // lroot layout:
        rootLayout = view.findViewById(R.id.main);
        //icons:
        cloudIcon = view.findViewById(R.id.cloudIcon);
        refreshIcon = view.findViewById(R.id.refreshIcon);
        // texts:
        countryArea = view.findViewById(R.id.chosenCountryArea);
        temperature = view.findViewById(R.id.temperatureText);
        weatherComment = view.findViewById(R.id.weatherComment);
        rainOmeter = view.findViewById(R.id.rainProbability);
        surfacePressure = view.findViewById(R.id.windSpeed);
        humidity = view.findViewById(R.id.humidity);
        // let's change the theme now.
        setTheme();
        setDummyText();
        weatherService = WeatherAPI();
        view.findViewById<ImageButton>(R.id.settingsIcon).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in,R.anim.slide_out)
                .replace(R.id.fragmentContainer, SettingsFragment()).addToBackStack(null).commit();
        }
        // let's check if we have the location permission or not, if we
        // don't, we just request it again and quit once this idiot denies it again.
        if(!LocationPermHelper.hasAccessFinePermission(requireActivity() as MainActivity))
        {
            LocationPermHelper.requestFinePerm(requireActivity() as MainActivity?);
            if(!LocationPermHelper.hasAccessFinePermission(requireActivity() as MainActivity))
            {
                Toast.makeText(context, getString(R.string.locationDenial), Toast.LENGTH_SHORT).show();
                activity?.finish();
            }
        }
    }
    object LocationPermHelper {
        private val basicPermission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        const val PERMREQCODE = 0;
        fun hasAccessFinePermission(activity: MainActivity): Boolean
        {
            return (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        }
        fun requestFinePerm(activity: MainActivity?)
        {
            ActivityCompat.requestPermissions(activity!!, basicPermission, PERMREQCODE);
        }
    }
}
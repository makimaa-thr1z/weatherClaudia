package nobara.open.weather
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class MainScreen : Fragment()
{
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_screen, container, false);
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        // icons:
        cloudIconsArray = intArrayOf(R.drawable.overcast, R.drawable.drizzle_rain_shower, R.drawable.sunny, R.drawable.rain,
            R.drawable.winter, R.drawable.drizzle_rain_shower, R.drawable.partlycloudy);
        cloudIcon = view.findViewById(R.id.cloudIcon);
        refreshIcon = view.findViewById(R.id.refreshIcon);
        // texts:
        countryArea = view.findViewById(R.id.chosenCountryArea);
        temperature = view.findViewById(R.id.temperatureText);
        weatherComment = view.findViewById(R.id.weatherComment);
        rainOmeter = view.findViewById(R.id.rainProbability);
        surfacePressure = view.findViewById(R.id.windSpeed);
        humidity = view.findViewById(R.id.humidity);
        view.findViewById<ImageButton>(R.id.settingsIcon).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SettingsFragment()).addToBackStack(null).commit()
        }
    }
}
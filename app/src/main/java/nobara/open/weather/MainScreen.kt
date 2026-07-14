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
    private lateinit var rootLayout: View;
    fun setDummyText()
    {
        countryArea.text = "Japan";
        temperature.text = getString(R.string.temperature_format, 18.5, getString(R.string.degree));
        surfacePressure.text = "100%";
        humidity.text = "100%";
        rainOmeter.text = "100%";
        weatherComment.text = "mostly sunny";
        cloudIcon.setImageResource(R.drawable.mainly_clear);
        rootLayout.setBackgroundResource(R.drawable.bg_summer);
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_screen, container, false);
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        cloudIcon = view.findViewById(R.id.cloudIcon);
        refreshIcon = view.findViewById(R.id.refreshIcon);
        // texts:
        countryArea = view.findViewById(R.id.chosenCountryArea);
        temperature = view.findViewById(R.id.temperatureText);
        weatherComment = view.findViewById(R.id.weatherComment);
        rainOmeter = view.findViewById(R.id.rainProbability);
        surfacePressure = view.findViewById(R.id.windSpeed);
        humidity = view.findViewById(R.id.humidity);
        // lroot layout:
        rootLayout = view.findViewById(R.id.main);
        // aww hell nah gng
        setDummyText();
        view.findViewById<ImageButton>(R.id.settingsIcon).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in,R.anim.slide_out)
                .replace(R.id.fragmentContainer, SettingsFragment()).addToBackStack(null)
                .commit()
        }
    }
}
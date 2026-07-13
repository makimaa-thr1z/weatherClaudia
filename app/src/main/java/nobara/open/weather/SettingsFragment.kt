package nobara.open.weather
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
class SettingsFragment : Fragment()
{
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById<View>(R.id.creditsWindow).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CreditsScreen()).addToBackStack(null).commit()
        }
    }
    // back nutton:
    // requireActivity().onBackPressedDispatcher.onBackPressed()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings_screen, container, false);
    }
}
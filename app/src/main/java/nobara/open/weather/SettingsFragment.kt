package nobara.open.weather
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat

class SettingsFragment : Fragment()
{
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById<View>(R.id.creditsWindow).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in,R.anim.slide_out)
                .replace(R.id.fragmentContainer, CreditsScreen()).addToBackStack(null).commit()
        }
        val rootWindow = view.findViewById<View>(R.id.settingsWindow);
        val themesRow = view.findViewById<Spinner>(R.id.themesRow);
        val themes = resources.getStringArray(R.array.themes)
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_layout,
            themes
        )
        themesRow.adapter = adapter;
        themesRow.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, itemView: View?, position: Int, id: Long)
            {
                when(position)
                {
                    0 -> rootWindow.setBackgroundResource(R.drawable.bg_summer);
                    1 -> rootWindow.setBackgroundResource(R.drawable.bg_hailstorm);
                    2 -> rootWindow.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pitchBlackBackground));
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // nothing happens so...
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings_screen, container, false);
    }
}
package nobara.open.weather
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.edit

class SettingsFragment : Fragment()
{
    //root window ig
    private lateinit var rootWindow: LinearLayout;
    private lateinit var sharedPref: SharedPreferences;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(requireContext(), "Settings loaded", Toast.LENGTH_SHORT).show()
        rootWindow = view.findViewById(R.id.settingsWindow);
        sharedPref = requireContext().getSharedPreferences("basic_prefs", MODE_PRIVATE);
        val themePosition = sharedPref.getInt("selectedTheme", 0);
        // let's change the theme now.
        when(themePosition)
        {
            0 -> rootWindow.setBackgroundResource(R.drawable.bg_summer);
            1 -> rootWindow.setBackgroundResource(R.drawable.bg_hailstorm);
            2 -> rootWindow.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pitchBlackBackground));
        }
        val themesRow = view.findViewById<Spinner>(R.id.themesRow);
        val themes = resources.getStringArray(R.array.themes)
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_layout,
            themes
        )
        themesRow.adapter = adapter;
        themesRow.setSelection(themePosition, false);
        themesRow.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, itemView: View?, position: Int, id: Long)
            {
                when(position)
                {
                    0 -> rootWindow.setBackgroundResource(R.drawable.bg_summer);
                    1 -> rootWindow.setBackgroundResource(R.drawable.bg_hailstorm);
                    2 -> rootWindow.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pitchBlackBackground));
                }
                sharedPref.edit {
                    this.putInt("selectedTheme", position)
                }
            }
            // nothing happens so...
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        view.findViewById<View>(R.id.creditsWindow).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in,R.anim.slide_out)
                .replace(R.id.fragmentContainer, CreditsScreen()).addToBackStack(null).commit()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings_screen, container, false);
    }
}
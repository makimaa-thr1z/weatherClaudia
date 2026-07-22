package nobara.open.weather
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.edit
import androidx.fragment.app.Fragment
class SettingsFragment : Fragment()
{
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        // some inits
        var rootWindow = view.findViewById<View>(R.id.settingsWindow);
        var sharedPref = requireContext().getSharedPreferences(BASIC_PREFS, MODE_PRIVATE);
        var nobaraUtils = NobaraUtils(sharedPref, rootWindow, requireActivity(), requireContext());
        val themesRow = view.findViewById<Spinner>(R.id.themesRow);
        val themes = resources.getStringArray(R.array.themes);
        val adapterTheme = ArrayAdapter(requireContext(), R.layout.spinner_layout, themes)
        themesRow.adapter = adapterTheme;
        val themePosition = sharedPref.getInt(SELECTED_THEME, 0);
        // set the selected theme:
        nobaraUtils.setTheme();
        themesRow.setSelection(themePosition, false); // we change the text on the spinner
        // themes row tap trigger:
        themesRow.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, itemView: View?, position: Int, id: Long)
            {
                // we don't replace the theme here, we do it inside Nobara.
                sharedPref.edit { this.putInt(SELECTED_THEME, position) };
                nobaraUtils.setTheme();
            }
            // nothing happens so...
            override fun onNothingSelected(parent: AdapterView<*>?) {};
        }
        // view triggers:
        view.findViewById<View>(R.id.creditsWindow).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in,R.anim.slide_out)
                .hide(this)
                .replace(R.id.fragmentContainer, CreditsScreen()).addToBackStack(null).commit()
        }
        view.findViewById<View>(R.id.manageCoordinates).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in,R.anim.slide_out)
                .hide(this)
                .replace(R.id.fragmentContainer, FetchCoordinates()).addToBackStack(null).commit()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings_screen, container, false);
    }
}
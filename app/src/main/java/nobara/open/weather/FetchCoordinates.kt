package nobara.open.weather
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.edit
import androidx.fragment.app.FragmentManager
import com.google.android.material.button.MaterialButton
class FetchCoordinates : Fragment()
{
    private lateinit var sharedPref: SharedPreferences;
    private lateinit var rootLayout: View;
    private lateinit var nobaraUtils: NobaraUtils;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        // nobarar utls init:
        rootLayout = view.findViewById(R.id.managecoordinates);
        sharedPref = requireContext().getSharedPreferences(BASIC_PREFS, MODE_PRIVATE);
        nobaraUtils = NobaraUtils(sharedPref, rootLayout, requireActivity(), requireContext());
        nobaraUtils.setTheme();
        // box id:
        val coorLat = view.findViewById<EditText>(R.id.floatLatitude);
        val coorLon = view.findViewById<EditText>(R.id.floatLongitude);
        val refreshButton = view.findViewById<MaterialButton>(R.id.btnRefresh);
        // trigger:
        refreshButton.setOnClickListener {
            if(coorLat.text.toString().isEmpty() || coorLon.text.toString().isEmpty())
            {
                nobaraUtils.toastThisShit(getString(R.string.pleasePut))
                return@setOnClickListener;
            }
            sharedPref.edit {
                this.putString(LATITUDE, coorLat.text.toString());
                this.putString(LONGITUDE, coorLon.text.toString());
                this.putBoolean(USED_DATA_FROM_APP, true);
            }
            nobaraUtils.toastThisShit(getString(R.string.saved));
            if(sharedPref.getBoolean(THROWBACK_HOME, false))
            {
                sharedPref.edit { putBoolean(THROWBACK_HOME, false); };
                val fm = requireActivity().supportFragmentManager;
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().replace(R.id.fragmentContainer, MainScreen()).commit();
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.managecoordinates, container, false);
    }
}
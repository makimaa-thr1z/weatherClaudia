package nobara.open.weather
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.edit
import androidx.fragment.app.FragmentManager
class KangLocation : Fragment()
{
    private lateinit var nobaraUtils: NobaraUtils;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        var thisShittyCounter = 0;
        view.findViewById<ImageButton>(R.id.locationAgreed).setOnClickListener {
            val shred = requireContext().getSharedPreferences("basic_prefs", MODE_PRIVATE);
            nobaraUtils = NobaraUtils(shred, null, requireActivity() as MainActivity, requireContext());
            // let's check if we have the location permission or not, if we
            // don't, we just request it again and quit once this idiot denies it again.
            NobaraUtils.LocationPermHelper.requestFinePerm(requireActivity() as MainActivity?);
            if(nobaraUtils.isLocationEnabled() && (nobaraUtils.hasAccessCoarsePermission() &&
                    nobaraUtils.hasAccessFinePermission()))
            {
                // mark setup as over and make the pitch black theme default.
                shred.edit {
                    putInt(SELECTED_THEME, 2);
                    putBoolean(SETUP_STAT, true);
                    putBoolean(SETUP_CORD, false);
                    putBoolean(THROWBACK_HOME, false);
                };
                val fm = requireActivity().supportFragmentManager;
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().replace(R.id.fragmentContainer, MainScreen()).commit();
            }
            else 
            {
                shred.edit { putBoolean(THROWBACK_HOME, true); };
                thisShittyCounter++
                if(thisShittyCounter == 2)
                {
                    val fm = requireActivity().supportFragmentManager;
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fm.beginTransaction().replace(R.id.fragmentContainer, FetchCoordinates()).commit();
                }
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.kang_location, container, false);
    }
}
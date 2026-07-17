package nobara.open.weather
import android.Manifest
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.FragmentManager

class KangLocation : Fragment()
{
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById<ImageButton>(R.id.locationAgreed).setOnClickListener {
            val shred = requireContext().getSharedPreferences("basic_prefs", MODE_PRIVATE);
            // mark setup as over and make the pitch black theme default.
            shred.edit { putBoolean("isSetupOver", true); putInt("selectedTheme", 3) };
            // let's check if we have the location permission or not, if we
            // don't, we just request it again and quit once this idiot denies it again.
            LocationPermHelper.requestFinePerm(requireActivity() as MainActivity?);
            val fm = requireActivity().supportFragmentManager;
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction().replace(R.id.fragmentContainer, MainScreen()).commit();
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.kang_location, container, false);
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
package nobara.open.weather
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
class CreditsScreen : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.credits_page, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        NobaraUtils(requireContext().getSharedPreferences(BASIC_PREFS, MODE_PRIVATE),
            view.findViewById(R.id.creditsWindow), requireActivity(), requireContext()).setTheme();
    }
}
package nobara.open.weather
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat

class CreditsScreen : Fragment()
{
    // rootlayout:
    private lateinit var rootLayout: LinearLayout;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.credits_page, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState);
        rootLayout = view.findViewById(R.id.creditsWindow);
        val themePosition = requireContext().getSharedPreferences("basic_prefs", MODE_PRIVATE).getInt("selectedTheme", 0);
        // let's change the theme now.
        when(themePosition)
        {
            0 -> rootLayout.setBackgroundResource(R.drawable.bg_summer);
            1 -> rootLayout.setBackgroundResource(R.drawable.bg_hailstorm);
            2 -> rootLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pitchBlackBackground));
        }
    }
}
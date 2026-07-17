package nobara.open.weather
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null)
        {
            if(!getSharedPreferences("basic_prefs", MODE_PRIVATE).getBoolean("isSetupOver", false))
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, OnBoardFragment()).commit();
            else
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, MainScreen()).commit();
        }
    }
}
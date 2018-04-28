package stuff.steven.notesandjunk

import android.support.v7.app.AppCompatActivity
import android.os.Bundle


class MainActivity : AppCompatActivity(), MainFragment.OnMenuItemSelected {

    override fun onMenuSettingsSelected() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)
        if(fragment == null){
            fragment = MainFragment()
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }
}

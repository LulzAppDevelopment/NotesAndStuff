package stuff.steven.notesandjunk

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.Preference
import android.support.v4.app.FragmentActivity
import android.view.Menu
import android.view.MenuInflater
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val PREF_FILENAME = "stuff.steven.notesandjunk.MainActivity"
    val PREF_SAVED_TEXT = "saved_text"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onPause() {
        super.onPause()
        saveText()
    }

    override fun onResume() {
        super.onResume()
        fetchText()
    }

    fun saveText(){
        val sharedPreferences = getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()){
            putString(PREF_SAVED_TEXT, edit_text.text.toString())
            apply()
        }
    }

    fun fetchText(){
        val sharedPreferences = getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE) ?: return
        edit_text.setText(sharedPreferences.getString(PREF_SAVED_TEXT, ""))
    }
}

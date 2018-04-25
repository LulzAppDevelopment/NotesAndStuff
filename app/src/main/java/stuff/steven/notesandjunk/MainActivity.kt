package stuff.steven.notesandjunk

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val PREF_FILENAME = "stuff.steven.notesandjunk.MainActivity"
    val SAVED_TEXT = "saved_text"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences = getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()){
            putString(SAVED_TEXT, edit_text.text.toString())
            apply()
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE) ?: return
        edit_text.setText(sharedPreferences.getString(SAVED_TEXT, ""))
    }

}

package stuff.steven.notesandjunk

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private val defaultPrefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.settings_menu, menu)
    }

    override fun onPause() {
        super.onPause()
        saveText()
    }

    override fun onResume() {
        super.onResume()
        fetchText()
    }

    private fun saveText(){
//        with(defaultPrefs.edit()){
//            putString(PREF_SAVED_TEXT, edit_text.text.toString())
//            apply()
//        }
        val textToSave = edit_text.text.toString()
        context?.openFileOutput(FILENAME_STORING_TEXT, Context.MODE_PRIVATE).use {
            it?.write(textToSave.toByteArray())
        }
    }

    private fun fetchText(){
        val textSize = defaultPrefs.getString(resources.getString(R.string.pref_text_size_key), resources.getString(R.string.pref_text_size_default))
        val textColor = defaultPrefs.getString(resources.getString(R.string.pref_text_color_key), resources.getString(R.string.pref_text_color_default))
        val backgroundColor = defaultPrefs.getString(resources.getString(R.string.pref_background_color_key), resources.getString(R.string.pref_background_color_default))
        edit_text.setTextColor(Color.parseColor(textColor))
        edit_text.setBackgroundColor(Color.parseColor(backgroundColor))
        edit_text.textSize = textSize.toFloat()
        //edit_text.setText(defaultPrefs.getString(PREF_SAVED_TEXT, ""))

        val file = context?.getFileStreamPath(FILENAME_STORING_TEXT)
        var textToShow = ""
        if (file?.exists() == true) {
            context?.openFileInput(FILENAME_STORING_TEXT).use {
                val bytes = it?.readBytes()
                if (bytes != null) textToShow = String(bytes)
            }
        }
        edit_text.setText(textToShow)
    }

    companion object {
        const val TAG = "MainFragment"

        //const val PREF_FILENAME = "stuff.steven.notesandjunk.MainFragment"
        //const val PREF_SAVED_TEXT = "saved_text"
        const val FILENAME_STORING_TEXT = "edittext_text"
    }
}

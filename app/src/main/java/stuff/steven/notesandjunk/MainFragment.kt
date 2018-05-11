package stuff.steven.notesandjunk

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
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
        saveCursorPosition()
    }

    override fun onResume() {
        super.onResume()
        fetchText()
        fetchCursorPosition()
    }

    private fun saveText(){
        val textToSave = edit_text.text.toString()
        context?.openFileOutput(FILENAME_STORING_TEXT, Context.MODE_PRIVATE).use {
            it?.write(textToSave.toByteArray())
        }
    }

    private fun fetchText(){
        val savedTextSize = defaultPrefs.getString(resources.getString(R.string.pref_key_text_size), resources.getString(R.string.pref_text_size_default))
        val textColor = defaultPrefs.getString(resources.getString(R.string.pref_key_text_color), resources.getString(R.string.pref_text_color_default))
        val backgroundColor = defaultPrefs.getString(resources.getString(R.string.pref_key_background_color), resources.getString(R.string.pref_background_color_default))

        val file = context?.getFileStreamPath(FILENAME_STORING_TEXT)
        var textToShow = ""
        if (file?.exists() == true) {
            context?.openFileInput(FILENAME_STORING_TEXT).use {
                val bytes = it?.readBytes()
                if (bytes != null) textToShow = String(bytes)
            }
        }

        with(edit_text){
            setTextColor(Color.parseColor(textColor))
            setBackgroundColor(Color.parseColor(backgroundColor))
            textSize = savedTextSize.toFloat()
            setText(textToShow)
            setSelection(fetchCursorPosition())
        }
    }

    private fun saveCursorPosition() {
        with(defaultPrefs.edit()){
            putInt(resources.getString(R.string.pref_key_cursor_position), edit_text.selectionEnd)
            commit()
        }
    }

    private fun fetchCursorPosition() : Int{
        return defaultPrefs.getInt(resources.getString(R.string.pref_key_cursor_position), 0)
    }

    companion object {
        const val TAG = "MainFragment"

        const val FILENAME_STORING_TEXT = "edittext_text"
    }
}

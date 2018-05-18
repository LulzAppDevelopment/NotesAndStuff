package stuff.steven.notesandjunk

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.*
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.ref.WeakReference

class MainFragment : Fragment() {
    private lateinit var defaultPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(activity)
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
    }

    private fun saveText(){
        val textToSave = edit_text.text.toString()
        TextSaver(activity).execute(textToSave)
    }

    private fun saveCursorPosition() {
        with(defaultPrefs.edit()){
            putInt(resources.getString(R.string.pref_key_cursor_position), edit_text.selectionEnd)
            apply()
        }
    }

    private fun fetchText(){
        TextFetcher(activity, edit_text, defaultPrefs).execute()
        val savedTextSize = defaultPrefs.getString(resources.getString(R.string.pref_key_text_size), resources.getString(R.string.pref_text_size_default))
        val textColor = defaultPrefs.getString(resources.getString(R.string.pref_key_text_color), resources.getString(R.string.pref_text_color_default))
        val backgroundColor = defaultPrefs.getString(resources.getString(R.string.pref_key_background_color), resources.getString(R.string.pref_background_color_default))

        with(edit_text){
            setTextColor(Color.parseColor(textColor))
            setBackgroundColor(Color.parseColor(backgroundColor))
            textSize = savedTextSize.toFloat()
        }
    }

    private class TextSaver(context: Context?) : AsyncTask<String, Void, Void>() {
        private val weakContext = WeakReference(context)

        override fun doInBackground(vararg params: String): Void? {
            val textToSave = params[0]
            this.weakContext.get()?.openFileOutput(FILENAME_STORING_TEXT, Context.MODE_PRIVATE).use {
                it?.write(textToSave.toByteArray())
            }
            return null
        }
    }

    private class TextFetcher(c: Context?, et: EditText, sp: SharedPreferences) : AsyncTask<Void, Void, String>() {
        private val weakContext = WeakReference(c)
        private val weakEditText = WeakReference(et)
        private val weakSharedPrefs = WeakReference(sp)

        override fun doInBackground(vararg params: Void?): String {
            val context = this.weakContext.get()
            val file = context?.getFileStreamPath(FILENAME_STORING_TEXT)
            var textToShow = ""
            if (file?.exists() == true) {
                context.openFileInput(FILENAME_STORING_TEXT).use {
                    val bytes = it?.readBytes()
                    if (bytes != null) textToShow = String(bytes)
                }
            }
            return textToShow
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            with(weakEditText.get() as EditText){
                setText(result)
                val cursorPosition = fetchCursorPosition(weakSharedPrefs.get())
                if (text.length > 0)
                    setSelection(if (cursorPosition <= text.length) cursorPosition else text.length)
            }
        }

        private fun fetchCursorPosition(prefs: SharedPreferences?) : Int{
            return prefs?.getInt(weakContext.get()?.resources?.getString(R.string.pref_key_cursor_position), 0)!!
        }
    }

    companion object {
        const val TAG = "MainFragment"

        const val FILENAME_STORING_TEXT = "edittext_text.txt"
    }
}

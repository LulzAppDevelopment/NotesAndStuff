package stuff.steven.notesandjunk

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private lateinit var onMenuItemSelected : OnMenuItemSelected
    private val defaultPrefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    interface OnMenuItemSelected{
        fun onMenuSettingsSelected()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            onMenuItemSelected = context as OnMenuItemSelected
        }catch (e : ClassCastException){
            throw ClassCastException(context.toString() + " must implement OnMenuItemSelected")
        }

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.settings -> {
                onMenuItemSelected.onMenuSettingsSelected()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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
        with(defaultPrefs.edit()){
            putString(PREF_SAVED_TEXT, edit_text.text.toString())
            apply()
        }
    }

    private fun fetchText(){
        val textSize = defaultPrefs.getString(resources.getString(R.string.pref_text_size_key), resources.getString(R.string.pref_text_size_default))
        val textColor = defaultPrefs.getString(resources.getString(R.string.pref_text_color_key), resources.getString(R.string.pref_text_color_default))
        val backgroundColor = defaultPrefs.getString(resources.getString(R.string.pref_background_color_key), resources.getString(R.string.pref_background_color_default))
        edit_text.setTextColor(Color.parseColor(textColor))
        edit_text.setBackgroundColor(Color.parseColor(backgroundColor))
        edit_text.setTextSize(textSize.toFloat())
        edit_text.setText(defaultPrefs.getString(PREF_SAVED_TEXT, ""))
    }

    companion object {
        const val TAG = "MainFragment"

        //const val PREF_FILENAME = "stuff.steven.notesandjunk.MainFragment"
        const val PREF_SAVED_TEXT = "saved_text"
    }
}

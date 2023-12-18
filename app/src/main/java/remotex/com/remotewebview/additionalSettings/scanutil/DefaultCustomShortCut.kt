package remotex.com.remotewebview.additionalSettings.scanutil


import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.*
import remotex.com.remotewebview.R
import remotex.com.remotewebview.WebActivity
import remotex.com.remotewebview.additionalSettings.utils.Constants


//Requires api level 25
@RequiresApi(Build.VERSION_CODES.N_MR1)
object DefaultCustomShortCut {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun setUp(context: Context, textView: String) {  // to pass image on the constructor if possible
        val shortcutManager =
            getSystemService<ShortcutManager>(context, ShortcutManager::class.java)


        val intents = arrayOf(
            Intent(Intent.ACTION_VIEW, null, context, WebActivity::class.java),
        )

        val shortcut = ShortcutInfo.Builder(context, Constants.shortcut_website_id)
            .setShortLabel(textView)
            .setLongLabel("Open $textView")
            .setIcon(Icon.createWithResource(context, R.drawable.img_logo_icon))
            .setIntents(intents)
            .build()

        shortcutManager!!.dynamicShortcuts = listOf(shortcut)
    }

}
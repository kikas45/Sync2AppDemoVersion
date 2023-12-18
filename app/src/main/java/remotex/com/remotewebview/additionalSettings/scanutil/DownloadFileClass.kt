package remotex.com.remotewebview.additionalSettings.scanutil

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import remotex.com.remotewebview.additionalSettings.utils.Constants
import java.io.File

/*
object DownloadFileClass {

    fun downloadImage(context: Context, url: String, finalFolderPath: String, fileName: String) {
        val managerDownload = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val pathFolder =  "/Syn2AppLive/$finalFolderPath"
        val folder =  File(Environment.DIRECTORY_DOWNLOADS, pathFolder)

        if (!folder.exists()) {
            folder.mkdirs()
        }


        val destinationFile = File(folder, fileName)

        if (destinationFile.exists()) {
            destinationFile.delete()
        }


        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle(fileName)
        request.setDescription("Downloading $fileName")
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(folder.toString(), fileName);

        val downloadReferenceMain = managerDownload.enqueue(request)

        val sharedPreferences = context.getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putLong(Constants.downloadKey, downloadReferenceMain)
        editor.apply()
    }
}
*/

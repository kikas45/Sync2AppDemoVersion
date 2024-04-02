package sync2app.com.syncapplive.additionalSettings

import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sync2app.com.syncapplive.R
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.databinding.ActivityMyTestDownloadApiBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MyTestDownloadAPI : AppCompatActivity() {
    private lateinit var binding: ActivityMyTestDownloadApiBinding

    private lateinit var myTextView: TextView
    private lateinit var editClo: EditText
    private lateinit var editDemo: EditText

    private val myDownloadClass: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.MY_DOWNLOADER_CLASS,
            Context.MODE_PRIVATE
        )
    }
    private var currentDownloadIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyTestDownloadApiBinding.inflate(layoutInflater)
        setContentView(binding.root)


        myTextView = findViewById(R.id.textDisplayText)
        editClo = findViewById(R.id.editTexyForClO)
        editDemo = findViewById(R.id.editTexyForDeMO)


        binding.apply {
            editTexyForClO.setText("CLO")
            // editTexyForDeMO.setText("DE_MO_2021000")
            editTexyForDeMO.setText("HQ")

            btnDownload.setOnClickListener {

                val getClo = editTexyForClO.text.toString().trim()
                val getDemo = editTexyForDeMO.text.toString().trim()

                if (getClo.isNotEmpty() && getDemo.isNotEmpty()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val csvData = downloadCSV(getClo, getDemo)
                        saveURLPairs(csvData)

                        runOnUiThread {
                            textDisplayText.text = csvData
                        }
                    }

                } else {
                    showToastMessage("field is empty")
                    if (getClo.isEmpty()) {
                        editTexyForClO.error = "input Field"
                    }

                    if (getDemo.isEmpty()) {
                        editTexyForDeMO.error = "input Field"
                    }
                }
            }

        }


        binding.textDisplayText.setOnClickListener {

            val getvalues = myDownloadClass.getString("url1", "").toString()
            val getvalues22 = myDownloadClass.getString("url2", "").toString()
            val getvalues33 = myDownloadClass.getString("url3", "").toString()

            val (url1, fileName1) = extractUrlAndFileName(getvalues)
            val (url2, fileName2) = extractUrlAndFileName(getvalues22)
            val (url3, fileName3) = extractUrlAndFileName(getvalues33)

          //  download(url1, fileName1)
          //  download(url2, fileName2)
          //  download(url3, fileName3)

         //   myTextView.setText("$url1 \n$fileName1")


            val urlsAndFileNames = listOf(
                extractUrlAndFileName(getvalues),
                extractUrlAndFileName(getvalues22),
                extractUrlAndFileName(getvalues33)
            )

            downloadSequentially(urlsAndFileNames)

            myTextView.setText(urlsAndFileNames.toString())



        }
    }


    private fun downloadSequentially(urlsAndFileNames: List<Pair<String, String>>) {
        if (currentDownloadIndex < urlsAndFileNames.size) {
            val (url, fileName) = urlsAndFileNames[currentDownloadIndex]
            download(url, fileName)
        } else {
            showToastMessage("All downloads completed")
        }
    }




    private fun extractUrlAndFileName(input: String): Pair<String, String> {
        // Remove the leading and trailing quotes if present
        val sanitizedInput = input.trim('"')
        // Extract the URL and file name
        val url = sanitizedInput.substringBeforeLast(',')
        val fileName = sanitizedInput.substringAfterLast('/')

        return Pair(url, fileName)
    }

    private fun download(url: String, fileName: String) {
        val path = "/PowellJoe"

        val managerDownload = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(fileName)
        request.allowScanningByMediaScanner()
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS, "$path/$fileName"
        )
        val downloadReferenceMain = managerDownload.enqueue(request)

        val editor = myDownloadClass.edit()
        editor.putLong(Constants.downloadKey, downloadReferenceMain)
        editor.apply()

        currentDownloadIndex++

        showToastMessage(currentDownloadIndex.toString() + "")
    }


    private fun saveURLPairs(csvData: String) {
        val pairs = parseCSV(csvData)

        Log.d("downloadURLs", "saveURLPairs: CSV data - $csvData")
        Log.d("downloadURLs", "saveURLPairs: Parsed pairs - $pairs")

        val editor = myDownloadClass.edit()
        for ((index, pair) in pairs.withIndex()) {
            editor.putString("url${index + 1}", pair)
        }
        editor.apply()
        showToastMessage("URL pairs saved successfully")
    }


    // for no need of comma CSV
    private fun parseCSV(csvData: String): List<String> {
        val pairs = mutableListOf<String>()
        val lines = csvData.split("\n")
        for (line in lines) {
            if (line.isNotBlank()) {
                val url = line.trim()
                pairs.add(url)
            }
        }
        return pairs
    }


    private fun downloadCSV(clo: String, demo: String): String {
        val stringBuilder = StringBuilder()
        try {
            val downloadUrl =
               "https://cloudappserver.co.uk/cp/app_base/public/$clo/$demo/Start/start.csv"
            Log.d("downloadUrl", "downloadUrl: $downloadUrl")

            val url = URL(downloadUrl)
            val connection = url.openConnection() as HttpURLConnection
            val code = connection.responseCode

            if (code == 200) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append("\n")
                }
            } else {
                Log.e("CSVReadError", "Response code: $code")
            }
        } catch (e: Exception) {
            Log.e("CSVReadError", "Error reading CSV: ${e.message}")
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }


}

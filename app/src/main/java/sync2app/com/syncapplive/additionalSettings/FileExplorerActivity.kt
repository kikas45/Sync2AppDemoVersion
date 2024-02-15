package sync2app.com.syncapplive.additionalSettings


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import sync2app.com.syncapplive.MyApplication
import sync2app.com.syncapplive.R
import sync2app.com.syncapplive.additionalSettings.urlchecks.checkStoragePermission
import sync2app.com.syncapplive.additionalSettings.urlchecks.getFilesList
import sync2app.com.syncapplive.additionalSettings.urlchecks.openFile
import sync2app.com.syncapplive.additionalSettings.urlchecks.renderItem
import sync2app.com.syncapplive.additionalSettings.urlchecks.renderParentLink
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.databinding.ActivityFileExplorerBinding
import java.io.File
import java.util.Objects

class FileExplorerActivity : AppCompatActivity() {

    private var hasPermission = false
    private lateinit var binding: ActivityFileExplorerBinding
    private lateinit var currentDirectory: File
    private lateinit var filesList: List<File>
    private lateinit var adapter: ArrayAdapter<String>


    var openCounts = 0

    private val sharedPreferences: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.file_explorer_prefs,
            Context.MODE_PRIVATE
        )
    }


    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_explorer)
        binding = ActivityFileExplorerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyApplication.incrementRunningActivities()

        setupUi()



        binding.closeBs.setOnClickListener {

            val sharedPreferences = getSharedPreferences("file_explorer_prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            finish()

        }
    }


    override fun onResume() {
        super.onResume()
        setEnviroment()
    }

    private fun setEnviroment() {
        hasPermission = checkStoragePermission(this)
        if (hasPermission) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                if (!Environment.isExternalStorageLegacy()) {
                    return
                }
            }

            binding.filesTreeView.visibility = View.VISIBLE

            val lastOpenedFolderPath = sharedPreferences.getString("LAST_OPENED_FOLDER_KEY", "")
            val lastOpenedFolder = if (lastOpenedFolderPath.isNullOrEmpty()) {
                File(Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/")
            } else {
                File(lastOpenedFolderPath)
            }

            open(lastOpenedFolder)

        } else {
            binding.filesTreeView.visibility = View.GONE
        }
    }


    private fun setupUi() {

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())
        binding.filesTreeView.adapter = adapter
        binding.filesTreeView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = filesList[position]


            open(selectedItem)

            openCounts++

        }


        binding.filesTreeView.setOnItemLongClickListener { _, _, position, _ ->
            val selectedItem = filesList[position]
            showDeleteDialog(selectedItem)
            true
        }


    }

    private fun open(selectedItem: File) {
        if (selectedItem.isFile) {
            return openFile(this, selectedItem)
        }

        currentDirectory = selectedItem
        filesList = getFilesList(currentDirectory)

        adapter.clear()
        adapter.addAll(filesList.map {
            if (it.path == selectedItem.parentFile.path) {
                renderParentLink(this)
            } else {
                renderItem(this, it)
            }
        })

        adapter.notifyDataSetChanged()

        binding.textNoFolder.visibility = if (filesList.isEmpty()) View.VISIBLE else View.GONE

        val editor = sharedPreferences.edit()
        editor.putString("LAST_OPENED_FOLDER_KEY", selectedItem.absolutePath);
        editor.apply()
    }


    private fun showDeleteDialog(file: File) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete")
        alertDialog.setMessage("Are you sure you want to delete ${file.name}?")
        alertDialog.setPositiveButton("Yes") { _, _ ->
            val parentDirectory = file.parentFile
            val wasFolderEmpty = parentDirectory?.listFiles()?.isEmpty() ?: true
            if (delete(file)) {
                // Call notifyDataSetChanged after successful deletion
                adapter.notifyDataSetChanged()
                // Navigate back to the parent folder if it was not empty
                if (!wasFolderEmpty) {
                    open(parentDirectory!!)
                }
                // Show toast or perform other actions if needed
                Toast.makeText(this, "${file.name} deleted", Toast.LENGTH_SHORT).show()
            } else {
                showAlert("Failed to delete ${file.name}")
            }
        }
        alertDialog.setNegativeButton("No", null)
        alertDialog.show()
    }


    private fun showAlert(message: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Alert")
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("OK", null)
        alertDialog.show()
    }


    fun delete(file: File): Boolean {
        if (file.isFile) {
            return file.delete()
        } else if (file.isDirectory) {
            for (subFile in Objects.requireNonNull(file.listFiles())) {
                if (!delete(subFile)) return false
            }
            return file.delete()
        }
        return false
    }


    override fun onBackPressed() {
        val directoryPath =
            Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/"
        val syn2AppLiveFolder = File(directoryPath)

        if (currentDirectory == syn2AppLiveFolder) {
            super.onBackPressed()
        } else {
            if (openCounts > 0) {
                val parentDirectory = currentDirectory.parentFile
                if (parentDirectory != null) {
                    open(parentDirectory)
                    openCounts--
                }
            } else {

                val sharedPreferences = getSharedPreferences("file_explorer_prefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()
                super.onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPreferences = getSharedPreferences("file_explorer_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        MyApplication.decrementRunningActivities()
    }



}


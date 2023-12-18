package remotex.com.remotewebview.additionalSettings.utils

import android.Manifest

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity


class PermissionUtils(private val activity: FragmentActivity, private val registry: ActivityResultRegistry) {

    private val requestPermissionLauncher: ActivityResultLauncher<Array<String>> =
        registry.register("request_permissions", activity, ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            handlePermissionsResult(permissions)
        }

    fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        requestPermissionLauncher.launch(permissions)
    }

    private fun handlePermissionsResult(permissions: Map<String, Boolean>) {
        if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true &&
            permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
        ) {
        } else {
            // Show permission dialog under Android R
          //  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
           //     showToastMessage("Version more than")
          //  }
            showPermissionDeniedDialog()
        }
    }

    private fun showPermissionDeniedDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setTitle("Permission Required")
        builder.setMessage("Please grant the required permissions in the app settings to proceed.")
        builder.setPositiveButton("Go to Settings") { dialog: DialogInterface?, which: Int ->
            openAppSettings()
            dialog?.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int ->
            showToastMessage("Permission Denied!")
          //  activity.finish()
        }
        builder.show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivity(intent)
    }

    private fun showToastMessage(message: String) {
        try {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }catch (_:Exception){}
    }
}


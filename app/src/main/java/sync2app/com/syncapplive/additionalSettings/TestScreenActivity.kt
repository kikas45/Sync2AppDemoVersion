package sync2app.com.syncapplive.additionalSettings

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import sync2app.com.syncapplive.R
import sync2app.com.syncapplive.additionalSettings.ApiUrls.ApiUrlViewModel
import sync2app.com.syncapplive.additionalSettings.ApiUrls.DomainUrl
import sync2app.com.syncapplive.additionalSettings.ApiUrls.SavedApiAdapter
import sync2app.com.syncapplive.databinding.ActivityTestScreenBinding
import java.util.Calendar

class TestScreenActivity : AppCompatActivity(){
    private lateinit var binding: ActivityTestScreenBinding


    var hour = 0
    var min = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.textDisplayText.setOnClickListener {

            showSyncDialog()
        }



    }


    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }




    private fun showSyncDialog() {

        //create dialog
        val syncDialog = AlertDialog.Builder(this).create()
        val inflater = this.layoutInflater
        val viewOptions: View = inflater.inflate(R.layout.time_picker_dialog, null)

        //widgets
        val timePicker = viewOptions.findViewById<TimePicker>(R.id.timePicker)
        val cancelBtn = viewOptions.findViewById<Button>(R.id.cancelBtn)
        val setBtn = viewOptions.findViewById<Button>(R.id.setBtn)

        //dialog props
        syncDialog.setView(viewOptions)
        syncDialog.window!!.attributes.windowAnimations = R.style.PauseDialogAnimation
        syncDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //initialize time picker
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour = 0
            timePicker.minute = 0
            timePicker.setIs24HourView(true)
            val calendar = Calendar.getInstance()
            timePicker.setOnTimeChangedListener { view: TimePicker?, hourOfDay: Int, minute: Int ->

                //get time
                hour = hourOfDay
                min = minute
            }
        }

        //cancel
        cancelBtn.setOnClickListener { v: View? ->

            //dismiss
            syncDialog.dismiss()
        }

        //grant access
        setBtn.setOnClickListener { v: View? ->

            //calculate minutes
            val totalMins: Int = hour * 60 + min


            //set on edt
            binding.textDisplayText.setText(String.format("%02d:%02d", hour, min))


            //reset temp data
            hour = 0
            min = 0

            //close dialog
            syncDialog.dismiss()
        }

        //show dialog
        syncDialog.show()
    }


}
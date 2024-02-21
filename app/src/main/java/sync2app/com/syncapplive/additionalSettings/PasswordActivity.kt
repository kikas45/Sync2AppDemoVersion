package sync2app.com.syncapplive.additionalSettings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import sync2app.com.syncapplive.MyApplication
import sync2app.com.syncapplive.R
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.databinding.ActivityPasswordBinding
import sync2app.com.syncapplive.databinding.CustomDefaultEmailSavedLayoutBinding
import sync2app.com.syncapplive.databinding.CustomFailedLayoutBinding
import sync2app.com.syncapplive.databinding.CustomSucessLayoutBinding
import java.util.regex.Pattern

class PasswordActivity : AppCompatActivity() {

    private val sharedBiometric: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SHARED_BIOMETRIC,
            Context.MODE_PRIVATE
        )
    }

    private val simpleSavedPassword: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SIMPLE_SAVED_PASSWORD,
            Context.MODE_PRIVATE
        )
    }

    private var olDPassworEnabled = false;
    private var newPassworEnabled = false;
    private var isEmailReady = false;

    private lateinit var binding: ActivityPasswordBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyApplication.incrementRunningActivities()

        binding.apply {
            closeBs.setOnClickListener {
                onBackPressed()
            }





            imgToggle.setOnClickListener {
                imgToggle.visibility = View.INVISIBLE
                imgToggleNzotVisible.visibility = View.VISIBLE
                eitTextEnterPasswordDia.setTransformationMethod(null)
                eitTextEnterPasswordDia.setSelection(eitTextEnterPasswordDia.length())

            }


            imgToggleNzotVisible.setOnClickListener {
                imgToggle.visibility = View.VISIBLE
                imgToggleNzotVisible.visibility = View.INVISIBLE
                eitTextEnterPasswordDia.setTransformationMethod(PasswordTransformationMethod())
                eitTextEnterPasswordDia.setSelection(eitTextEnterPasswordDia.length())



            }





            imgToggle22.setOnClickListener {
                imgToggle22.visibility = View.INVISIBLE
                imgToggleNzotVisible22.visibility = View.VISIBLE
                eitTextEnterNewPassword.setTransformationMethod(null)
                eitTextEnterNewPassword.setSelection(eitTextEnterNewPassword.length())

            }


            imgToggleNzotVisible22.setOnClickListener {
                imgToggle22.visibility = View.VISIBLE
                imgToggleNzotVisible22.visibility = View.INVISIBLE
                eitTextEnterNewPassword.setTransformationMethod(PasswordTransformationMethod())
                eitTextEnterNewPassword.setSelection(eitTextEnterNewPassword.length())

            }


            val imgPassword = sharedBiometric.getString(Constants.imgEnablePassword, "")
            imagSwtichEnableLaucngOnline.isChecked =
                imgPassword.equals(Constants.imgEnablePassword)


            if (imgPassword.equals(Constants.imgEnablePassword)) {
                textDisableOrEnablePassowrd.setText("Enable password")
            } else {
                textDisableOrEnablePassowrd.setText("Disable password")
            }


            /// is email visibility allowed

            val imgEmailVisibility = sharedBiometric.getString(Constants.imagEnableEmailVisisbility, "")
            imagEnableEmailVisisbility.isChecked = imgEmailVisibility.equals(Constants.imagEnableEmailVisisbility)


            if (imgEmailVisibility.equals(Constants.imagEnableEmailVisisbility)) {
                textEmailVisbility.setText("Enable email visibility")
            } else {

                textEmailVisbility.setText("Disable email visibility")
            }


            imagEnableEmailVisisbility.setOnCheckedChangeListener { compoundButton, isValued ->
                val editor = sharedBiometric.edit()
                if (compoundButton.isChecked) {
                    editor.putString(Constants.imagEnableEmailVisisbility, "imagEnableEmailVisisbility")
                    editor.apply()
                    textEmailVisbility.setText("Enable email visibility")

                } else {
                    editor.remove("imagEnableEmailVisisbility")
                    editor.apply()
                    textEmailVisbility.setText("Disable email visibility")

                }
            }





            old_and_new_PasswordTextChanger()


            val getSavedEmail = simpleSavedPassword.getString(Constants.isSavedEmail, "")

            if (getSavedEmail!!.isNotEmpty()) {
                binding.editTextEmail.setText(getSavedEmail)

            }


            binding.textContinuPasswordDai2.setOnClickListener {

                val savedSimplePassword =
                    simpleSavedPassword.getString(Constants.simpleSavedPassword, "")

                val getOldEditText = binding.eitTextEnterPasswordDia.text.toString()
                val newPasswordText = binding.eitTextEnterNewPassword.text.toString()
                val editTextEmail = binding.editTextEmail.text.toString()

                val editor = simpleSavedPassword.edit()

                if (savedSimplePassword == getOldEditText) {
                    editor.putString(Constants.simpleSavedPassword, newPasswordText)
                    if (isEmailReady) {
                        editor.putString(Constants.isSavedEmail, editTextEmail)
                    }else{
                        showToastMessage("Invalid email format")
                    }
                    editor.apply()
                    showPopForOkayPassword();
                } else {

                    if (getOldEditText.isEmpty() && newPasswordText.isEmpty()) {

                        if (editTextEmail.isNotEmpty() || getSavedEmail.isNotEmpty()) {

                            if (isEmailReady) {
                                showPopYourDefaultEmailSaved()
                                editor.putString(Constants.isSavedEmail, editTextEmail)
                                editor.apply()
                            } else {
                                binding.editTextEmail.error = "Invalid email format"
                            }
                        } else {

                            //  showToastMessage("At least fill an input")
                            showToastMessage("Settings saved")
                            onBackPressed()
                        }

                    } else {

                        eitTextEnterPasswordDia.setError("Wrong password")
                        showPop_For_wrong_Password()
                    }

                }



                saveTooggleButton()


            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.decrementRunningActivities()

    }

    override fun onStart() {
        super.onStart()

    }

    private fun saveTooggleButton() {
        binding.apply {

            // for passowrd
            val editor = sharedBiometric.edit()
            if (imagSwtichEnableLaucngOnline.isChecked) {
                editor.putString(Constants.imgEnablePassword, "imgEnablePassword")
                editor.apply()

                textDisableOrEnablePassowrd.setText("Enable password")
            } else {
                textDisableOrEnablePassowrd.setText("Disable password")
                editor.remove("imgEnablePassword")
                editor.apply()

            }


            //for email visibility
            if (imagEnableEmailVisisbility.isChecked) {
                editor.putString(Constants.imagEnableEmailVisisbility, "imagEnableEmailVisisbility")
                editor.apply()

                textEmailVisbility.setText("Enable email visibility")
            } else {
                textEmailVisbility.setText("Disable email visibility")
                editor.remove("imagEnableEmailVisisbility")
                editor.apply()

            }


        }
    }


    private fun old_and_new_PasswordTextChanger() {
        binding.apply {

            eitTextEnterPasswordDia.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                    if (s.isNotEmpty()) {

                        divider2.setBackgroundResource(R.color.deep_blue)
                        //  eitTextEnterPasswordDia.error = "Invalid Password format"
                        olDPassworEnabled = true
                    } else {

                        divider2.setBackgroundResource(R.color.red)
                        eitTextEnterPasswordDia.error = "Invalid Password format"
                        olDPassworEnabled = false
                    }

                }

                override fun afterTextChanged(s: Editable) {
                    try {


                    } catch (_: Exception) {
                    }
                }
            })



            eitTextEnterNewPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    try {
                        if (s.isNotEmpty()) {
                            divider3.setBackgroundResource(R.color.deep_blue)
                            newPassworEnabled = true
                        } else {

                            divider3.setBackgroundResource(R.color.red)
                            eitTextEnterNewPassword.error = "Invalid Password format"
                            newPassworEnabled = false
                        }


                    } catch (_: Exception) {
                    }
                }

                override fun afterTextChanged(s: Editable) {

                }
            })


            editTextEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    try {

                        divider5.setBackgroundResource(R.color.deep_blue)
                        isEmailReady = isValidEmail(s.toString())


                    } catch (_: Exception) {
                    }
                }

                override fun afterTextChanged(s: Editable) {

                }
            })


        }

    }


    /*
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
*/


    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})?"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }


    @SuppressLint("MissingInflatedId")
    private fun showPopForOkayPassword() {

        val binding: CustomSucessLayoutBinding = CustomSucessLayoutBinding.inflate(layoutInflater)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(binding.root)

        val alertDialog = alertDialogBuilder.create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        binding.apply {

            textContinuPassword2.setOnClickListener {
                onBackPressed()
                alertDialog.dismiss()
            }

        }


        alertDialog.show()


    }


    @SuppressLint("MissingInflatedId")
    private fun showPopYourDefaultEmailSaved() {

        val binding: CustomDefaultEmailSavedLayoutBinding =
            CustomDefaultEmailSavedLayoutBinding.inflate(layoutInflater)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(binding.root)

        val alertDialog = alertDialogBuilder.create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        binding.apply {

            textContinuPassword2.setOnClickListener {
                onBackPressed()
                alertDialog.dismiss()
            }

        }


        alertDialog.show()


    }


    @SuppressLint("MissingInflatedId")
    private fun showPop_For_wrong_Password() {

        val binding: CustomFailedLayoutBinding = CustomFailedLayoutBinding.inflate(layoutInflater)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(binding.root)

        val alertDialog = alertDialogBuilder.create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        binding.apply {

            textContinuPassword2.setOnClickListener {
                alertDialog.dismiss()
            }

        }


        alertDialog.show()


    }


}
package uz.pdp.authpinfinger

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import uz.pdp.authpinfinger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mySharedPreference: MySharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mySharedPreference = MySharedPreference(this)
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        // creating a variable for our Executor
        // creating a variable for our Executor
        val executor = ContextCompat.getMainExecutor(this)
        // this will give us result of AUTHENTICATION
        // this will give us result of AUTHENTICATION
        val biometricPrompt = BiometricPrompt(this@MainActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }

                // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val intent = Intent(this@MainActivity, SecondActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        val promptInfo = PromptInfo.Builder().setTitle("712-guruh")
            .setDescription("Use your fingerprint to auth").setNegativeButtonText("Cancel")
            .build()
        binding.fingerText.setOnClickListener {
            if (mySharedPreference.getRegister()){
                biometricPrompt.authenticate(promptInfo)
            }else{
                Toast.makeText(this, "PIN kodni ro'yxatdan o'tkazing", Toast.LENGTH_SHORT).show()
            }
        }

        if (mySharedPreference.getRegister()) {
            binding.apply {
                pinView.addTextChangedListener{
                    if (it?.length == 4){
                        if (it.toString() == mySharedPreference.getString("pin")){
                            val intent = Intent(this@MainActivity, SecondActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            pinView.text?.clear()
                            Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        } else {
            binding.apply {
                titleText.text = "Yangi PIN kiriting"
                var a = true
                var pin = ""
                pinView.addTextChangedListener {
                    if (it?.length == 4) {
                        if (a) {
                            titleText.text = "PIN ni takrorlang"
                            pin = it.toString()
                            pinView.text?.clear()
                            a = false
                        } else {
                            if (it.toString() == pin) {
                                Toast.makeText(this@MainActivity, pin, Toast.LENGTH_SHORT).show()
                                mySharedPreference.setRegister()
                                mySharedPreference.setString("pin", pin)
                                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                pinView.text?.clear()
                                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }

        }

    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

            // here is one more tricky issue
            // imm.showSoftInputMethod doesn't work well
            // and imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0) doesn't work well for all cases too
            imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }
}
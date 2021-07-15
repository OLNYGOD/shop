package com.example.shop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        signup.setOnClickListener {
            val sEmail= sEmail.text.toString()
            val sPassword= sPassword.text.toString()
            //https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuth
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(sEmail, sPassword)      //在firebase建立使用者email
                .addOnCompleteListener {
                    if (it.isSuccessful()){
                        AlertDialog.Builder(this)   //跳出視窗
                            .setTitle("Sign Up")
                            .setMessage("Account created")
                            .setPositiveButton("ok"){ dialog, which ->
                                setResult(Activity.RESULT_OK)
                                finish()
                            }.show()
                    }else{
                        it.exception?.message
                        AlertDialog.Builder(this)
                            .setTitle("Sign Up")
                            .setMessage(it.exception?.message)
                            .setPositiveButton("ok", null)
                            .show()
                    }
                }

        }
    }
}
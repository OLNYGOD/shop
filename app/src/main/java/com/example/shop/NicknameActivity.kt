package com.example.shop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_nickname.*

class NicknameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)
        done.setOnClickListener {
            //todo: nickname
            //setNickname(nick.text.toString())  //extension 新增setNickname 功能
            //https://firebase.google.com/docs/database/android/read-and-write
            //https://www.programcreek.com/java-api-example s/?class=com.google.firebase.database.FirebaseDatabase&method=getInstance
            FirebaseDatabase.getInstance()  //連結firebasedatabase
                .getReference("users")  //寫入或讀取資料夾
                .child(FirebaseAuth.getInstance().currentUser!!.uid.toString())  //取的id 並寫入firebasedatabase
                .child("nickname")
                .setValue(nick.text.toString())
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
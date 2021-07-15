package com.example.shop

import android.content.ContentResolver
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.jar.Manifest

class ContactActivity : AppCompatActivity() {
    private val RC_CONTACTS = 110
    val TAG = ContactActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)  //危險權限取得
        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                RC_CONTACTS) //產生詢問對話框
        }else {
            readContacts()
        }
    }

    override fun onRequestPermissionsResult(  //當出現詢問使用者是否允許的對話框會跳到onRequestPermissionsResult中
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RC_CONTACTS){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                readContacts()
            }
        }
    }

    private fun readContacts() {
        //https://blog.csdn.net/bzlj2912009596/article/details/80248272
        //https://developer.android.com/guide/topics/providers/content-provider-basics?hl=zh-cn
        var cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null, null
        )
        while (cursor!!.moveToNext()) {
            val name = cursor.getString(
                cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)  //從Contacts的名稱去得到index值
            )
            Log.d(TAG, "onCreate: ${name}")
        }
    }
}
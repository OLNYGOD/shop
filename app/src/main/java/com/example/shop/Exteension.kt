package com.example.shop

import android.app.Activity
import android.content.Context
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_nickname.*

fun Activity.setNickname(nickname: String) {
    //儲存資料  https://thumbb13555.pixnet.net/blog/post/326166590-sharedpreferences
    getSharedPreferences("shop", Context.MODE_PRIVATE)
        .edit()
        .putString("NICKNAME", nickname)
        .apply() // 立即更動
}



fun Activity.getNickname(): String? {
    return getSharedPreferences("shop", Context.MODE_PRIVATE).getString("NICKNAME", "")
}
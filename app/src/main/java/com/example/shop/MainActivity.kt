package com.example.shop

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_function.view.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private val RC_NICKNAME: Int = 210
    private val RC_SIGNUP: Int = 200
    val auth = FirebaseAuth.getInstance() //得到firebase資料夾
    val functions = listOf<String>(
        "Camera",
        "Invite friend",
        "Parking",
        "Movie",
        "Download coupons",
        "News",
        "Map"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*if(!signup){
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGNUP)
        }*/
        //建立firebase 傾聽器
        auth.addAuthStateListener {auth ->
            authChanged(auth)
        }
        //spinner 下拉式選單介面
        //https://hjwang520.pixnet.net/blog/post/405000676-%5Bandroid-studio%5D%E4%B8%8B%E6%8B%89%E5%BC%8F%E9%81%B8%E5%96%AE%28spinner%29%E7%AD%86%E8%A8%98
        val colors = arrayOf("Red", "Blue", "Green")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, colors)  //
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("MainActivity", "onItemSelected: ${colors[position]}")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        //RecyclerView 讓畫面可以滑動
        mainrecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false) //清單式的管理畫面
        mainrecycler.setHasFixedSize(true) //固定大小
        mainrecycler.adapter = FunctionAdapter()
    }

    //https://ithelp.ithome.com.tw/articles/10220196 RecyclerView
    //Adapter串聯data跟view的地方
    
    inner class FunctionAdapter() : RecyclerView.Adapter<FunctionHolder>(){
        // 建立 ViewHolder 的地方，如果有同時支援多種 layout 的需求，
        // 可以複寫 getItemViewType function，
        // 這個 function 就可以拿到不同的 viewType 以供我們識別。
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionHolder {
            return FunctionHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.row_function,parent,false))
        }

        // 因為 ViewHolder 會重複使用，
        // 我們要在這個 function 依據 position
        // 把data跟 ViewHolder 綁定在一起。
         override fun onBindViewHolder(holder: FunctionHolder, position: Int) {
            holder.nametext.text = functions.get(position)
            //itemview指畫面上的textview
            holder.itemView.setOnClickListener { View ->
                functionClicked(holder,position)
            }
        }

        //回傳整個adapter包含幾筆資料
        override fun getItemCount(): Int {
            return functions.size
        }

    }

    private fun functionClicked(holder: FunctionHolder, position: Int) {
        Log.d(TAG, "functionClicked: ${position}")
        when(position){
            1->startActivity(Intent(this, ContactActivity::class.java))
            2->startActivity(Intent(this, ParkingActivity::class.java))
            3->startActivity(Intent(this, MovieActivity::class.java))
        }
    }

    //ViewHolder儲存View reference的地方 view 指layout檔資料
    class FunctionHolder(view: View) : RecyclerView.ViewHolder(view){
        var nametext : TextView = view.name
    }

    //在畫面上去前(生命週期）
    override fun onResume() {
        super.onResume()
        Log.d("MainActivity: ", "currentUser+${auth.currentUser}")
        if (auth.currentUser != null) {
            //nickname.text = getNickname()
            FirebaseDatabase.getInstance()
                .getReference("users")
                .child(auth.currentUser!!.uid)
                .child("nickname")
                ////只做一次從firebase取得資料 ( addListenerForSingleValueEvent
                .addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        nickname.text = snapshot.value as String?
                    }

                    override fun onCancelled(error: DatabaseError) {
                        //TODO("Not yet implemented")
                    }

                })
        }
    }
    private fun authChanged(auth: FirebaseAuth) {
        if(auth.currentUser == null){
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGNUP)
        }else{
            Log.d("MainActivity","authChanged : ${auth.currentUser?.uid}") //firebase 上有紀錄 uid

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGNUP)
            if (resultCode == Activity.RESULT_OK){
                val intent = Intent(this, NicknameActivity::class.java)
                startActivityForResult(intent, RC_NICKNAME)
            }
        if (requestCode == RC_NICKNAME)
            if (requestCode == Activity.RESULT_OK){

            }

    }
}

package com.example.shop

import android.icu.text.IDNA
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_parking.*
import java.net.URI
import java.net.URL

class ParkingActivity : AppCompatActivity() {
    private val TAG = ParkingActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking)
        val parking = "http://tcgbusfs.blob.core.windows.net/dotapp/youbike_ticket_opendata/YouBikeHis.csv"
        ParkingTask().execute(parking)
    }

    inner class ParkingTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            val url = URL(params[0])
            val json = url.readText()
            Log.d(TAG ,"doInBackground: $json")
            return json
        }

        override fun onPostExecute(result: String?) {
            Toast.makeText(this@ParkingActivity,"Got it", Toast.LENGTH_LONG).show()
            info.text = result
        }
    }
}
package com.example.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bus.*
import kotlinx.android.synthetic.main.row_bus.view.*
import org.jetbrains.anko.*
import java.net.URL

class BusActivity : AppCompatActivity(), AnkoLogger {
    var bus : List<Data>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)
        doAsync {
            val json = URL("\thttps://data.tycg.gov.tw/opendata/datalist/datasetMeta/download?id=b3abedf0-aeae-4523-a804-6e807cbad589&rid=bf55b21a-2b7c-4ede-8048-f75420344aed")
                .readText()
            bus = Gson().fromJson(json, Bus::class.java).datas
            bus?.forEach {
                info("${it.BusID} ${it.RouteID} ${it.Speed }")
            }
            uiThread {
                busrecycler.adapter = BusAdapter()
                busrecycler.setHasFixedSize(true)
                busrecycler.layoutManager = LinearLayoutManager(this@BusActivity)
            }
        }
    }inner class BusAdapter : RecyclerView.Adapter<BusViewholder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusViewholder {
            return BusViewholder(LayoutInflater.from(parent.context).inflate(R.layout.row_bus, parent ,false))
        }

        override fun onBindViewHolder(holder: BusViewholder, position: Int) {
            bus?.get(position)?.let { holder.bindbus(it) }
        }

        override fun getItemCount(): Int {
            return bus!!.size
        }
    }

    inner class BusViewholder(view : View) : RecyclerView.ViewHolder(view){
        val busidText = view.bus_BusID
        val busRouteIDText = view.bus_RouteID
        val busSpeedText = view.bus_Speed
        fun bindbus(bus: Data){
            busidText.text = bus.BusID
            busRouteIDText.text = bus.RouteID
            busSpeedText.text = bus.Speed
        }
    }
}

data class Bus(
    val datas: List<Data>
)

data class Data(
    val Azimuth: String,
    val BusID: String,
    val BusStatus: String,
    val DataTime: String,
    val DutyStatus: String,
    val GoBack: String,
    val Latitude: String,
    val Longitude: String,
    val ProviderID: String,
    val RouteID: String,
    val Speed: String,
    val ledstate: String,
    val sections: String
)
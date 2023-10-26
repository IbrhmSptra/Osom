package id.kotlin.osom

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import id.kotlin.osom.Auth.API_profile
import id.kotlin.osom.CoinOsomAPI.API_osom
import id.kotlin.osom.RecycleView.dataLeaderboard
import id.kotlin.osom.RecycleView.leaderboardAdapter
import id.kotlin.osom.databinding.ActivityLeaderboardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class leaderboardActivity : AppCompatActivity() {

    //init rc
    lateinit var dataLeaderboard: ArrayList<dataLeaderboard>
    lateinit var leaderboardAdapter: leaderboardAdapter

    //api
    val apikey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFpdWlocG51bG95anV1eWxycW9pIiwicm9sZSI6ImFub24iLCJpYXQiOjE2OTc0NDkxNzAsImV4cCI6MjAxMzAyNTE3MH0.A6pRhyENfgjKJnNG9o15J2__ljDtjdEOrxgBnpzR5tE"
    val token = "Bearer $apikey"
    val apiProfile = RetorfitHelper.getInstance().create(API_profile::class.java)
    val apiOsom =  RetorfitHelper.getInstance().create(API_osom::class.java)

    lateinit var binding: ActivityLeaderboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        val view = binding.root


        //back btn
        binding.back.setOnClickListener {
            onBackPressed()
        }

        //fetch coin for osom
        val sharedPreferences = getSharedPreferences("osom", Context.MODE_PRIVATE)
        val idosom = sharedPreferences.getString("id","")
        CoroutineScope(Dispatchers.Main).launch {
            val query = "eq.$idosom"
            val response = apiOsom.get(token = token, apiKey = apikey, query = query)
            response.body()?.forEach {
                binding.coinosom.text = it.coin.toString()
            }
        }

        //recycleview
        binding.rcleaderboard.setHasFixedSize(true)
        binding.rcleaderboard.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        CoroutineScope(Dispatchers.Main).launch {
            val response = apiProfile.getall(token = token, apiKey = apikey)
            //add araylist
            dataLeaderboard = ArrayList()
            response.body()?.forEach {
                dataLeaderboard.add(
                    dataLeaderboard(
                        username = it.username,
                        coin = NumberFormat.getNumberInstance(Locale.getDefault()).format(it.coin)
                    )
                )
            }
            binding.rcleaderboard.adapter = leaderboardAdapter(dataLeaderboard,this@leaderboardActivity)
        }


        setContentView(view)
        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }
}
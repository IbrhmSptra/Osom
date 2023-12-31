package id.kotlin.osom

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.Toast
import id.kotlin.osom.Auth.API_profile
import id.kotlin.osom.Auth.dataProfile
import id.kotlin.osom.databinding.ActivityHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.lang.Integer.parseInt
import java.text.NumberFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding


    val apikey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFpdWlocG51bG95anV1eWxycW9pIiwicm9sZSI6ImFub24iLCJpYXQiOjE2OTc0NDkxNzAsImV4cCI6MjAxMzAyNTE3MH0.A6pRhyENfgjKJnNG9o15J2__ljDtjdEOrxgBnpzR5tE"
    val token = "Bearer $apikey"
    val apiProfile = RetorfitHelper.getInstance().create(API_profile::class.java)




    override fun onCreate(savedInstanceState: Bundle?) {
        //SET SHARED PREFERENCE
        val sharedPreferences = getSharedPreferences("osom", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        var email = sharedPreferences.getString("email","").toString()
        val username = sharedPreferences.getString("username","").toString()



        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        //GENERATE RANDOM MASSAGE
        randomtext()

        //SET USERNAME
        binding.username.text = username

        //SET COIN
        val query = "eq.$username"
        CoroutineScope(Dispatchers.Main).launch {
            var coin : Long = 0
            val response = apiProfile.getusername(apiKey = apikey, token = token, query = query)
            response.body()?.forEach{
                coin = it.coin.toString().toLong()
            }
            val format = NumberFormat.getNumberInstance(Locale.getDefault()).format(coin)
            binding.coin.text = format
        }

        //BUTTON LEADERBOARD
        binding.leaderboard.setOnClickListener {
            //sfx
            val clicksound = MediaPlayer.create(this, R.raw.click)
            clicksound.start()
            val intent = Intent(this, leaderboardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // BUTTON LOGOUT
        binding.logout.setOnClickListener {
            //sfx
            val clicksound = MediaPlayer.create(this, R.raw.click)
            clicksound.start()
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.modallogout)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val btncancel: Button = dialog.findViewById(R.id.cancel)
            val btnlogout: Button = dialog.findViewById(R.id.logout)

            btnlogout.setOnClickListener {
                //sfx
                val click = MediaPlayer.create(this, R.raw.click)
                click.start()
                editor?.clear()
                editor.remove("email")
                editor.remove("username")
                editor.commit()

                // INTENT KE LOGIN
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            btncancel.setOnClickListener {
                //sfx
                val click = MediaPlayer.create(this, R.raw.click)
                click.start()
                dialog.dismiss()
            }
            dialog.show()
        }

        // FLOATING EFFECT
        val animate = ObjectAnimator.ofFloat(binding.model, "translationY", 0f, -80f)
        animate.duration = 3500 //duration in ms
        animate.repeatCount = Animation.INFINITE // Repeat the animation infinitely
        animate.repeatMode = ObjectAnimator.REVERSE // Reverse the animation
        // Set an interpolator for smooth animation
        animate.interpolator = LinearInterpolator()
        // Start the animation
        animate.start()


        //PLAY
        binding.playbtn.setOnClickListener {
            //sfx
            val clicksound = MediaPlayer.create(this, R.raw.click)
            clicksound.start()
            //bet cannot be null
            if (binding.betingcoin.text.isNullOrEmpty()) {
                Toast.makeText(this, "Insert Your Coin First", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //get coin and bet
            var bet = binding.betingcoin.text.toString().toLong()
            var coin : Long = 0
            val query = "eq.$username"
            CoroutineScope(Dispatchers.Main).launch {
                val response = apiProfile.getusername(apiKey = apikey, token = token, query = query)
                response.body()?.forEach{
                    coin = it.coin.toString().toLong()
                }
                // bet cannot be more than coin
                if (bet>coin){
                    Toast.makeText(this@HomeActivity, "Not Enough Coin", Toast.LENGTH_SHORT).show()
                }else {
                    //logic coin
                    coin -= bet

                    val intent = Intent(this@HomeActivity, PlayActivity::class.java)
                    intent.putExtra("coin", coin)
                    intent.putExtra("bet", bet)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }
        }

        //HELP
        binding.help.setOnClickListener {
            //sfx
            val clicksound = MediaPlayer.create(this, R.raw.click)
            clicksound.start()
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun randomtext(){
        var rand = (1..5).random()
        when (rand) {
            1 -> binding.msghome.text = "Pala lo sini gw genjreng!"
            2 -> binding.msghome.text = "Khaarrkk kur kur kur kur..."
            3 -> binding.msghome.text = "You cant defeat me!! Muehehehe.."
            4 -> binding.msghome.text = "Spend all of your coins to me.."
            5 -> binding.msghome.text = "Money!! Money!! Money!!"
        }
    }

    override fun onResume() {

        //SET SHARED PREFERENCE
        val sharedPreferences = getSharedPreferences("osom", Context.MODE_PRIVATE)
        var username = sharedPreferences.getString("username","").toString()
        val query = "eq.$username"
        CoroutineScope(Dispatchers.Main).launch {
            var coin : Long = 0
            val response = apiProfile.getusername(apiKey = apikey, token = token, query = query)
            response.body()?.forEach {
                coin = it.coin.toString().toLong()
            }
            val format = NumberFormat.getNumberInstance(Locale.getDefault()).format(coin)
            binding.coin.text = format
        }
        super.onResume()
    }


}
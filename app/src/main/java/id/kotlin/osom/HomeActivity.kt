package id.kotlin.osom

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.Toast
import id.kotlin.osom.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //SET SHARED PREFERENCE
        val sharedPreferences = getSharedPreferences("osom", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()

        //GENERATE RANDOM MASSAGE
        randomtext()

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

            //multiply coin cannot be null
            if (binding.betingcoin.text.isNullOrEmpty()) {
                Toast.makeText(this, "Insert Your Coin First", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, PlayActivity::class.java)
            startActivity(intent)
        }

        //HELP
        binding.help.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun randomtext(){
        var rand = (1..5).random()
        when (rand) {
            1 -> binding.msghome.text = "Pala lo sini gw genjreng!"
            2 -> binding.msghome.text = "Janganlah berjudi dengan uang asli sesungguhnya itu haram"
            3 -> binding.msghome.text = "You cant defeat me!! Muehehehe.."
            4 -> binding.msghome.text = "Spend all of your coins to me.."
            5 -> binding.msghome.text = "Money!! Money!! Money!!"
        }
    }

    override fun onResume() {
        super.onResume()
        randomtext()
    }
}
package id.kotlin.osom

import android.animation.ObjectAnimator
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
            Toast.makeText(this, "ready to play", Toast.LENGTH_SHORT).show()
        }

        //HELP
        binding.help.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }
    }
}
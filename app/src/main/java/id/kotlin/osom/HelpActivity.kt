package id.kotlin.osom

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.kotlin.osom.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {
    lateinit var binding : ActivityHelpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //sfx
        val clicksound = MediaPlayer.create(this, R.raw.click)

        binding.back.setOnClickListener{
            clicksound.start()
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }
}
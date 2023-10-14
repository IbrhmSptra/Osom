package id.kotlin.osom

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.kotlin.osom.databinding.ActivityPlayBinding

class PlayActivity : AppCompatActivity() {
    lateinit var binding : ActivityPlayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val gifthingking = binding.osomthinking.drawable as AnimationDrawable
        gifthingking.start()
        gifthingking.isOneShot = false
    }
}
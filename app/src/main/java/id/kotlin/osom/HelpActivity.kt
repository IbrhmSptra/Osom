package id.kotlin.osom

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

        binding.back.setOnClickListener{
            onBackPressed()
        }
    }
}
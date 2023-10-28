package id.kotlin.osom

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)

        //GET SESSION BY EMAIL FROM SHARED PREFERENCE
        val sharedPreferences = getSharedPreferences("osom", Context.MODE_PRIVATE)
        val session = sharedPreferences.getString("email","").toString()



        Handler().postDelayed({
            if (session.isNullOrEmpty()){
                goToLogin()
            }else{
                goToHome()
            }

        },3500)
    }

    private fun goToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToHome(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
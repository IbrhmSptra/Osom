package id.kotlin.osom

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import id.kotlin.osom.Auth.API_profile
import id.kotlin.osom.Auth.API_user
import id.kotlin.osom.Auth.dataUser
import id.kotlin.osom.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    val apikey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFpdWlocG51bG95anV1eWxycW9pIiwicm9sZSI6ImFub24iLCJpYXQiOjE2OTc0NDkxNzAsImV4cCI6MjAxMzAyNTE3MH0.A6pRhyENfgjKJnNG9o15J2__ljDtjdEOrxgBnpzR5tE"
    val token = "Bearer $apikey"
    val apiUser = RetorfitHelper.getInstance().create(API_user::class.java)
    val apiProfile = RetorfitHelper.getInstance().create(API_profile::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        //login
        binding.loginbtn.setOnClickListener {
            //sfc
            val click = MediaPlayer.create(this, R.raw.click)
            click.start()
            var email = binding.loginemail.text.toString()
            var password = binding.loginpassword.text.toString()
            signIn(email,password)
        }

        binding.registerhere.setOnClickListener {
            //sfc
            val click = MediaPlayer.create(this, R.raw.click)
            click.start()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(Email: String, Password: String) {
        CoroutineScope(Dispatchers.IO).launch {

            val data = dataUser(email = Email, password = Password)
            val response = apiUser.signIn(token = token , apiKey = apikey , data = data)

            val bodyResponse = if(response.code() == 200) {
                response.body()?.string()
            } else {
                response.errorBody()?.string().toString()
            }

            var failed = false
            val jsonResponse = JSONObject(bodyResponse)
            if(jsonResponse.keys().asSequence().toList().contains("error")) {
                failed = true
            }

            var msg = ""
            if (!failed) {
                var email = jsonResponse.getJSONObject("user").get("email").toString()
                //get username
                var username = ""
                val query = "eq.$email"
                val response = apiProfile.getemail(token = token , apiKey = apikey, query = query)
                response.body()?.forEach {
                    username = it.username
                    //set pesan welcome
                    msg = "Successfully login! Hi, ${it.username}"
                }

                //menyimpan email ke shared pref untuk session login
                val sharedPreference =  getSharedPreferences(
                    "osom", Context.MODE_PRIVATE
                )
                var editor = sharedPreference.edit()
                editor.putString("email", email)
                editor.putString("username",username)
                editor.commit()


            } else {
                msg += jsonResponse.get("error_description").toString()
            }

            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(
                    applicationContext,
                    msg,
                    Toast.LENGTH_SHORT
                ).show()

                if (!failed) {
                    //GO TO HOME
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
package id.kotlin.osom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import id.kotlin.osom.Auth.API_profile
import id.kotlin.osom.Auth.API_user
import id.kotlin.osom.Auth.dataProfile
import id.kotlin.osom.Auth.dataUser
import id.kotlin.osom.databinding.ActivityRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    lateinit var binding : ActivityRegisterBinding

    val apikey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFpdWlocG51bG95anV1eWxycW9pIiwicm9sZSI6ImFub24iLCJpYXQiOjE2OTc0NDkxNzAsImV4cCI6MjAxMzAyNTE3MH0.A6pRhyENfgjKJnNG9o15J2__ljDtjdEOrxgBnpzR5tE"
    val token = "Bearer $apikey"
    val apiUser = RetorfitHelper.getInstance().create(API_user::class.java)
    val apiProfile = RetorfitHelper.getInstance().create(API_profile::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //register
        binding.registerbtn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                var email = binding.regemail.text.toString()
                var username = binding.regusername.text.toString()
                var password = binding.regpassword.text.toString()
                var repassword = binding.regrepassword.text.toString()


                //get email and username from database
                var emaildb = ""
                var usernamedb =""
                val queryemail = "eq.$email"
                val queryusername = "eq.$username"
                val responseemail = apiProfile.getemail(apiKey = apikey, token = token, query = queryemail)
                responseemail.body()?.forEach{
                    emaildb = it.email
                }
                val responseusername = apiProfile.getusername(apiKey = apikey, token = token, query = queryusername)
                responseusername.body()?.forEach {
                    usernamedb = it.username
                }
                //validasi kosong
                if (email.isNullOrEmpty() || username.isNullOrEmpty() || password.isNullOrEmpty() || repassword.isNullOrEmpty()){
                    Toast.makeText(this@RegisterActivity, "Fill The Field First", Toast.LENGTH_SHORT).show()
                }
                //validasi password tak sama
                else if (password != repassword){
                    binding.regpassword.error = "Not The Same"
                    binding.regrepassword.error = "Not The Same"
                }
                //validasi email sudah ada
                else if (!emaildb.isNullOrEmpty()){
                    binding.regemail.error = "Email Already Used"
                }
                //validasi username sudah ada
                else if (!usernamedb.isNullOrEmpty()){
                    binding.regusername.error = "Username Already Taken"
                }
                //berhasil register
                else{
                    signUp(email = email, password = password)
                    addProfile(Email = email, Username = username)
                }
            }

        }

        binding.loginhere.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addProfile(Email : String, Username : String){
        CoroutineScope(Dispatchers.IO).launch {
            var profile = dataProfile(username = Username, email = Email)
            val response = apiProfile.create(apiKey = apikey, token = token, data = profile)
        }
    }

    private fun signUp(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {

            var data = dataUser(email = email, password = password)
            var response = apiUser.signUp(token = token , apiKey = apikey , data = data)

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
                msg = "Successfully Created An Account"
            } else {
                var errorMessage = jsonResponse.get("error_description")
                msg += errorMessage
            }

            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(
                    applicationContext,
                    msg,
                    Toast.LENGTH_LONG
                ).show()

                finish()
            }
        }

    }
}
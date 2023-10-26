package id.kotlin.osom

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import id.kotlin.osom.Auth.API_profile
import id.kotlin.osom.Auth.dataProfile
import id.kotlin.osom.databinding.ActivityPlayBinding
import id.kotlin.osom.databinding.ModalBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.Vibrator
import id.kotlin.osom.CoinOsomAPI.API_osom
import id.kotlin.osom.CoinOsomAPI.dataOsom

class PlayActivity : AppCompatActivity() {
    lateinit var binding : ActivityPlayBinding

    val apikey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InFpdWlocG51bG95anV1eWxycW9pIiwicm9sZSI6ImFub24iLCJpYXQiOjE2OTc0NDkxNzAsImV4cCI6MjAxMzAyNTE3MH0.A6pRhyENfgjKJnNG9o15J2__ljDtjdEOrxgBnpzR5tE"
    val token = "Bearer $apikey"
    val apiProfile = RetorfitHelper.getInstance().create(API_profile::class.java)
    val apiOsom = RetorfitHelper.getInstance().create(API_osom::class.java)

    var scoreplayer = 0
    var scoreosom = 0
    var multiplier = 2
    var round = 1
    var notif = "none"
    var coin = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //init
        binding = ActivityPlayBinding.inflate(layoutInflater)
        val view = binding.root
        coin = intent.getIntExtra("coin",0)
        var bet = intent.getIntExtra("bet",0)
        setContentView(view)

        // sync the multiplier and score
        binding.playerscore.text = scoreplayer.toString()
        binding.osomscore.text = scoreosom.toString()
        binding.multiplier.text = "x$multiplier"

        // START GIF FOR OSOM PICK
        val gifthingking = binding.osomthinking.drawable as AnimationDrawable
        gifthingking.start()
        gifthingking.isOneShot = false

        //FlOATING EFFECT FOR PICK
        floatingeffect(binding.rock,0f,60f)
        Handler().postDelayed({
            floatingeffect(binding.scissor,0f,60f)
        },1500)
        floatingeffect(binding.paper,60f,0f)

        //LOGIC GAME
        binding.rock.setOnClickListener {
            var osom = 0
            for (i in 1..5) {
                osom = osompick()
            }
            when (osom) {
                //for notif
                1 -> notif = "draw"
                2 -> notif = "lose"
                3 -> notif = "win"
            }
            when (osom){
                // score
                2 -> scoreosom++
                3 -> scoreplayer++
            }
            if(notif.equals("draw")){
                binding.notif.setImageResource(R.drawable.draw)
                binding.modelexpression.setImageResource(R.drawable.why)
            }
            if (notif.equals("win")){
                binding.notif.setImageResource(R.drawable.win)
                binding.modelexpression.setImageResource(R.drawable.sad)
            }
            if (notif.equals("lose")){
                binding.notif.setImageResource(R.drawable.lose)
                binding.modelexpression.setImageResource(R.drawable.yeay)
            }
            //to stop osom thinking
            when (osom) {
                1 -> binding.osomthinking.setImageResource(R.drawable.rock)
                2 -> binding.osomthinking.setImageResource(R.drawable.paper)
                3 -> binding.osomthinking.setImageResource(R.drawable.scissor)
            }
            //remove player pick
            binding.rock.visibility = View.GONE
            binding.paper.visibility = View.GONE
            binding.scissor.visibility = View.GONE
            binding.playerturn.visibility = View.INVISIBLE

            //show notif
            val popup_anim = AnimationUtils.loadAnimation(this, R.anim.popup_anim)
            binding.notif.visibility = View.VISIBLE
            binding.notif.startAnimation(popup_anim)

            //show players pick
            binding.playerpick.setImageResource(R.drawable.chooserock)
            binding.playerpick.visibility = View.VISIBLE

            Handler().postDelayed({
                //show player pick
                binding.rock.visibility = View.VISIBLE
                binding.paper.visibility = View.VISIBLE
                binding.scissor.visibility = View.VISIBLE
                binding.playerturn.visibility = View.VISIBLE

                //hide notif
                binding.notif.visibility = View.INVISIBLE

                //osom rethingking
                binding.modelexpression.setImageResource(R.drawable.thinkingmodel)
                binding.osomthinking.setImageResource(R.drawable.osompicker)
                val gif = binding.osomthinking.drawable as AnimationDrawable
                gif.start()
                gif.isOneShot = false

                // hide players pick
                binding.playerpick.visibility = View.GONE
                //Reset
                notif = "none"

                //lose dialog show
                if(scoreosom == 3) {
                    val dialogL = Dialog(this)
                    dialogL.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialogL.setCancelable(false)
                    dialogL.setContentView(R.layout.modallose)
                    dialogL.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val btngive: Button = dialogL.findViewById(R.id.give)
                    btngive.setOnClickListener {
                        //give coin to osom
                        coinforosom(bet)
                        //update coin lose and reset then intent
                        updateCoin(coin)
                    }
                    dialogL.show()
                }


                //win dialog show
                if(scoreplayer == 3) {
                    val dialogW = Dialog(this)
                    dialogW.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialogW.setCancelable(false)
                    dialogW.setContentView(R.layout.modal)
                    dialogW.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val btnmore: Button = dialogW.findViewById(R.id.more)
                    val btntake: Button = dialogW.findViewById(R.id.take)
                    //Multiplier Algorithm
                    round++
                    if (round <4){
                        multiplier ++
                    }
                    if (round >=4 && round<7){
                        multiplier +=2
                    }
                    if (round >=7 && round<10){
                        multiplier+=4
                    }
                    if (round >=10){
                        multiplier+=6
                    }
                    btnmore.text = "x$multiplier"
                    btnmore.setOnClickListener {
                        scoreplayer = 0
                        scoreosom = 0
                        binding.playerscore.text = scoreplayer.toString()
                        binding.osomscore.text = scoreosom.toString()
                        binding.multiplier.text = "x$multiplier"
                        if (round>=4){
                            binding.fire.visibility = View.VISIBLE
                        }
                        if (round>=7){
                            startblinktext(binding.multiplier)
                        }
                        dialogW.dismiss()
                    }
                    btntake.setOnClickListener {
                        //calculating and update
                        coin = calculating(coin, bet, multiplier)
                        updateCoin(coin)
                    }
                    dialogW.show()
                }


            },3000)

            binding.osomscore.text = scoreosom.toString()
            binding.playerscore.text = scoreplayer.toString()
        }

        //===================================================================================================================

        binding.paper.setOnClickListener {
            var osom = 0
            for (i in 1..5) {
                osom = osompick()
            }
            when (osom) {
                //for notif
                1 -> notif = "win"
                2 -> notif = "draw"
                3 -> notif = "lose"
            }
            when (osom){
                // score
                3 -> scoreosom++
                1 -> scoreplayer++
            }
            if(notif.equals("draw")){
                binding.notif.setImageResource(R.drawable.draw)
                binding.modelexpression.setImageResource(R.drawable.why)
            }
            if (notif.equals("win")){
                binding.notif.setImageResource(R.drawable.win)
                binding.modelexpression.setImageResource(R.drawable.sad)
            }
            if (notif.equals("lose")){
                binding.notif.setImageResource(R.drawable.lose)
                binding.modelexpression.setImageResource(R.drawable.yeay)
            }
            //to stop osom thinking
            when (osom) {
                1 -> binding.osomthinking.setImageResource(R.drawable.rock)
                2 -> binding.osomthinking.setImageResource(R.drawable.paper)
                3 -> binding.osomthinking.setImageResource(R.drawable.scissor)
            }

            //remove player pick
            binding.rock.visibility = View.GONE
            binding.paper.visibility = View.GONE
            binding.scissor.visibility = View.GONE
            binding.playerturn.visibility = View.INVISIBLE

            //show notif
            val popup_anim = AnimationUtils.loadAnimation(this, R.anim.popup_anim)
            binding.notif.visibility = View.VISIBLE
            binding.notif.startAnimation(popup_anim)

            //show players pick
            binding.playerpick.setImageResource(R.drawable.choosepaper)
            binding.playerpick.visibility = View.VISIBLE

            Handler().postDelayed({
                //show player pick
                binding.rock.visibility = View.VISIBLE
                binding.paper.visibility = View.VISIBLE
                binding.scissor.visibility = View.VISIBLE
                binding.playerturn.visibility = View.VISIBLE

                //hide notif
                binding.notif.visibility = View.INVISIBLE

                //osom rethingking
                binding.modelexpression.setImageResource(R.drawable.thinkingmodel)
                binding.osomthinking.setImageResource(R.drawable.osompicker)
                val gif = binding.osomthinking.drawable as AnimationDrawable
                gif.start()
                gif.isOneShot = false


                //hide playerpick
                binding.playerpick.visibility = View.GONE

                //reset
                notif = "none"

                //lose dialog show
                if(scoreosom == 3) {
                    val dialogL = Dialog(this)
                    dialogL.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialogL.setCancelable(false)
                    dialogL.setContentView(R.layout.modallose)
                    dialogL.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val btngive: Button = dialogL.findViewById(R.id.give)
                    btngive.setOnClickListener {
                        //give coin to osom
                        coinforosom(bet)
                        //update coin lose and reset then intent
                        updateCoin(coin)
                    }
                    dialogL.show()
                }

                //win dialog show
                if(scoreplayer == 3) {
                    val dialogW = Dialog(this)
                    dialogW.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialogW.setCancelable(false)
                    dialogW.setContentView(R.layout.modal)
                    dialogW.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val btnmore: Button = dialogW.findViewById(R.id.more)
                    val btntake: Button = dialogW.findViewById(R.id.take)
                    //Multiplier Algorithm
                    round++
                    if (round <4){
                        multiplier ++
                    }
                    if (round >=4 && round<7){
                        multiplier +=2
                    }
                    if (round >=7 && round<10){
                        multiplier+=4
                    }
                    if (round >=10){
                        multiplier+=6
                    }
                    btnmore.text = "x$multiplier"
                    btnmore.setOnClickListener {
                        scoreplayer = 0
                        scoreosom = 0
                        binding.playerscore.text = scoreplayer.toString()
                        binding.osomscore.text = scoreosom.toString()
                        binding.multiplier.text = "x$multiplier"
                        if (round>=4){
                            binding.fire.visibility = View.VISIBLE
                        }
                        if (round>=7){
                            startblinktext(binding.multiplier)
                        }
                        dialogW.dismiss()
                    }
                    btntake.setOnClickListener {
                        //calculating and update
                        coin = calculating(coin, bet, multiplier)
                        updateCoin(coin)
                    }
                    dialogW.show()
                }

            },3000)

            binding.osomscore.text = scoreosom.toString()
            binding.playerscore.text = scoreplayer.toString()
        }

        //============================================================================================================================

        binding.scissor.setOnClickListener {
            var osom = 0
            for (i in 1..5) {
                osom = osompick()
            }
            when (osom) {
                //for notif
                1 -> notif = "lose"
                2 -> notif = "win"
                3 -> notif = "draw"
            }
            when (osom){
                // score
                1 -> scoreosom++
                2 -> scoreplayer++
            }
            if(notif.equals("draw")){
                binding.notif.setImageResource(R.drawable.draw)
                binding.modelexpression.setImageResource(R.drawable.why)
            }
            if (notif.equals("win")){
                binding.notif.setImageResource(R.drawable.win)
                binding.modelexpression.setImageResource(R.drawable.sad)
            }
            if (notif.equals("lose")){
                binding.notif.setImageResource(R.drawable.lose)
                binding.modelexpression.setImageResource(R.drawable.yeay)
            }
            //to stop osom thinking
            when (osom) {
                1 -> binding.osomthinking.setImageResource(R.drawable.rock)
                2 -> binding.osomthinking.setImageResource(R.drawable.paper)
                3 -> binding.osomthinking.setImageResource(R.drawable.scissor)
            }
            //remove player pick
            binding.rock.visibility = View.GONE
            binding.paper.visibility = View.GONE
            binding.scissor.visibility = View.GONE
            binding.playerturn.visibility = View.INVISIBLE

            //show notif
            val popup_anim = AnimationUtils.loadAnimation(this, R.anim.popup_anim)
            binding.notif.visibility = View.VISIBLE
            binding.notif.startAnimation(popup_anim)

            //show players pick
            binding.playerpick.setImageResource(R.drawable.choosescissors)
            binding.playerpick.visibility = View.VISIBLE

            Handler().postDelayed({
                //show player pick
                binding.rock.visibility = View.VISIBLE
                binding.paper.visibility = View.VISIBLE
                binding.scissor.visibility = View.VISIBLE
                binding.playerturn.visibility = View.VISIBLE

                //hide notif
                binding.notif.visibility = View.INVISIBLE

                //osom rethingking
                binding.modelexpression.setImageResource(R.drawable.thinkingmodel)
                binding.osomthinking.setImageResource(R.drawable.osompicker)
                val gif = binding.osomthinking.drawable as AnimationDrawable
                gif.start()
                gif.isOneShot = false

                //hide players pick
                binding.playerpick.visibility = View.GONE

                //reset
                notif = "none"

                //lose dialog show
                if(scoreosom == 3) {
                    val dialogL = Dialog(this)
                    dialogL.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialogL.setCancelable(false)
                    dialogL.setContentView(R.layout.modallose)
                    dialogL.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val btngive: Button = dialogL.findViewById(R.id.give)
                    btngive.setOnClickListener {
                        //give coin to osom
                        coinforosom(bet)
                        //update coin lose and reset then intent
                        updateCoin(coin)
                    }
                    dialogL.show()
                }

                //win dialog show
                if(scoreplayer == 3) {
                    val dialogW = Dialog(this)
                    dialogW.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialogW.setCancelable(false)
                    dialogW.setContentView(R.layout.modal)
                    dialogW.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    val btnmore: Button = dialogW.findViewById(R.id.more)
                    val btntake: Button = dialogW.findViewById(R.id.take)
                    //Multiplier Algorithm
                    round++
                    if (round <4){
                        multiplier ++
                    }
                    if (round >=4 && round<7){
                        multiplier +=2
                    }
                    if (round >=7 && round<10){
                        multiplier+=4
                    }
                    if (round >=10){
                        multiplier+=6
                    }
                    btnmore.text = "x$multiplier"
                    btnmore.setOnClickListener {
                        scoreplayer = 0
                        scoreosom = 0
                        binding.playerscore.text = scoreplayer.toString()
                        binding.osomscore.text = scoreosom.toString()
                        binding.multiplier.text = "x$multiplier"
                        if (round>=4){
                            binding.fire.visibility = View.VISIBLE
                        }
                        if (round>=7){
                            startblinktext(binding.multiplier)
                        }
                        dialogW.dismiss()
                    }
                    btntake.setOnClickListener {
                        //calculating and update
                        coin = calculating(coin, bet, multiplier)
                        updateCoin(coin)
                    }
                    dialogW.show()
                }

            },3000)

            binding.osomscore.text = scoreosom.toString()
            binding.playerscore.text = scoreplayer.toString()
        }



    }

    private fun floatingeffect(view : View, start : Float, end : Float){
        val animate = ObjectAnimator.ofFloat(view, "translationY", start, end)
        animate.duration = 3000
        animate.repeatCount = Animation.INFINITE // Repeat the animation infinitely
        animate.repeatMode = ObjectAnimator.REVERSE // Reverse the animation
        animate.interpolator = LinearInterpolator()
        animate.start()
    }

    private fun osompick() : Int {
        var rand = (1..3).random()
        return rand
    }

    private fun startblinktext(textView: TextView) {
        val colors = intArrayOf(
            Color.parseColor("#D8EEFE"), Color.parseColor("#094067"))
        var colorIndex = 0
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                textView.setTextColor(colors[colorIndex])
                colorIndex = (colorIndex + 1) % colors.size
                handler.postDelayed(this, 300) // Ganti warna setiap 300ms
            }
        }
        textView.setShadowLayer(2f, 1f, 1f, Color.BLACK)
        handler.post(runnable)
    }

    private fun calculating(coin : Int, bet : Int , multiplier : Int): Int {
        val x = multiplier - 1
        var result = bet * x
        Log.d("check", "result perkalian calculate $result")
        Log.d("check", "multiplier calculate $x")
        result += coin
        Log.d("check", "result pertambahan calculate $result")
        return result
    }

    private fun reset(){
        //reset
        scoreplayer = 0
        scoreosom = 0
        round = 1
        multiplier = 2
    }

    private fun updateCoin(coin : Int){
        //SET SHARED PREFERENCE
        val sharedPreferences = getSharedPreferences("osom", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val email = sharedPreferences.getString("email","")
        val username = sharedPreferences.getString("username", "")
        val query = "eq.$email"
        val data = dataProfile(username = username!!, coin = coin, email = email!!)
        CoroutineScope(Dispatchers.Main).launch {
            val response = apiProfile.update(apiKey = apikey, token = token, query = query, data = data)
        }
        editor.putInt("coin",coin)
        editor.apply()
        reset()
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun coinforosom(coinbet : Int) {
        //SET SHARED PREFERENCE
        val sharedPreferences = getSharedPreferences("osom", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id","")
        val query = "eq.$id"
        var coin = 0
        CoroutineScope(Dispatchers.Main).launch {
            val responseget = apiOsom.get(token = token, apiKey = apikey, query = query)
            responseget.body()?.forEach{
                coin = it.coin
            }
            var total = coin + coinbet
            val data = dataOsom(coin = total)
            val responseupdate = apiOsom.update(token = token, apiKey = apikey, query = query, data = data)
        }
    }


    override fun onBackPressed() {
        vibratePhone()
    }

    private fun vibratePhone() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Check if the device has a vibrator
        if (vibrator.hasVibrator()) {
            // Vibrate for 500 milliseconds (0.5 seconds)
            vibrator.vibrate(500)
        }
    }

    override fun onDestroy() {
        coin = intent.getIntExtra("coin",0)
        var bet = intent.getIntExtra("bet",0)
        //coin for osom
        coinforosom(bet)
        //update coin
        updateCoin(coin)
        super.onDestroy()
    }

    override fun onStop() {
        coin = intent.getIntExtra("coin",0)
        var bet = intent.getIntExtra("bet",0)
        //coin for osom
        coinforosom(bet)
        //update coin
        updateCoin(coin)
        super.onStop()
    }
}
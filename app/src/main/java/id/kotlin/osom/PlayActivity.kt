package id.kotlin.osom

import android.animation.ObjectAnimator
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import id.kotlin.osom.databinding.ActivityPlayBinding

class PlayActivity : AppCompatActivity() {
    lateinit var binding : ActivityPlayBinding
    lateinit var settime : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        var scoreplayer = 0
        var scoreosom = 0
        var notif = "none"


        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
            binding.notif.visibility = View.VISIBLE

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

                notif = "none"

            },3000)

            binding.osomscore.text = scoreosom.toString()
            binding.playerscore.text = scoreplayer.toString()
        }

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
            binding.notif.visibility = View.VISIBLE

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

                notif = "none"

            },3000)

            binding.osomscore.text = scoreosom.toString()
            binding.playerscore.text = scoreplayer.toString()
        }

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
            binding.notif.visibility = View.VISIBLE

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

                notif = "none"

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

}
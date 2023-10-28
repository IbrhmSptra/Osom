package id.kotlin.osom.RecycleView

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import id.kotlin.osom.R
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class leaderboardAdapter (private val userlist : ArrayList<dataLeaderboard>, private val context: Context)
    : RecyclerView.Adapter<leaderboardAdapter.leaderboardviewholder>() {

    class leaderboardviewholder(view: View) : RecyclerView.ViewHolder(view) {
        val card : ConstraintLayout = view.findViewById(R.id.card)
        val username: TextView = view.findViewById(R.id.username)
        val coin: TextView = view.findViewById(R.id.coin)
        val rank: TextView = view.findViewById(R.id.rank)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): leaderboardviewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rc_leaderboard, parent, false)
        return leaderboardviewholder(view)
    }

    override fun onBindViewHolder(holder: leaderboardviewholder, position: Int,) {
        val sharedPreferences = context.getSharedPreferences("osom",Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username","")
        val user = userlist[position]
        holder.rank.text = (position + 1).toString()
        holder.username.text = user.username
        holder.coin.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(user.coin)
        if (username == user.username){
            holder.card.setBackgroundResource(R.drawable.shaperc)
        }
        if ((position + 1)>3){
            holder.rank.textSize = 50f
            holder.rank.typeface = ResourcesCompat.getFont(context, R.font.poppinbold)
            holder.rank.setPadding(0,0,0,0)
        }
    }

    override fun getItemCount(): Int {
        return userlist.size
    }
}
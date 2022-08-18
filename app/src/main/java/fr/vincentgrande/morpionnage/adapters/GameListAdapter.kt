package fr.vincentgrande.morpionnage.adapters

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.vincentgrande.morpionnage.Game
import fr.vincentgrande.morpionnage.databinding.GameListItemBinding

class GameListAdapter(val games: MutableList<Game>): RecyclerView.Adapter<GameViewHolder>() {
    private lateinit var database: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder =
        GameViewHolder(
            GameListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            parent.context
        )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]


        database = Firebase.database.reference
        database.child("/users/${game.playerId}/username").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val playername = dataSnapshot.value.toString()
                database.child("/users/${game.ownerId}/username").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val ownername = dataSnapshot.value.toString()
                        holder.gameid.text = "ID : ${game.id}"
                        holder.adverse.text = "Adversaires : $ownername VS $playername"
                        if(game.winnerId == game.playerId)
                            holder.winner.text = "Vainqueur : $playername"
                        else if(game.winnerId == game.ownerId)
                            holder.winner.text = "Vainqueur : $ownername"
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                    }
                })
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    override fun getItemCount(): Int = games.size

}
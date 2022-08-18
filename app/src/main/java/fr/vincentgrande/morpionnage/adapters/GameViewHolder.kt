package fr.vincentgrande.morpionnage.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import fr.vincentgrande.morpionnage.databinding.GameListItemBinding

class GameViewHolder (binding: GameListItemBinding, context: Context):
    RecyclerView.ViewHolder(binding.root) {
    val gameid = binding.tvGameid
    val adverse = binding.tvAdverse
    var winner = binding.tvWinner
}
package fr.vincentgrande.morpionnage.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.protobuf.Parser
import fr.vincentgrande.morpionnage.Game
import fr.vincentgrande.morpionnage.R
import fr.vincentgrande.morpionnage.adapters.GameListAdapter
import fr.vincentgrande.morpionnage.databinding.FragmentHistoryBinding
import org.json.JSONObject


class HistoryFragment: Fragment() {
    private lateinit var _binding: FragmentHistoryBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        database = Firebase.database.reference
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        _binding.gameListRecyclerView.layoutManager = LinearLayoutManager(context)

        database.child("games").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var games: MutableList<Game> = mutableListOf()
                    for (ds in dataSnapshot.children) {
                        for (i in 1..ds.children.count() step 6) {
                            if( ds.child("ownerId").value.toString() == auth.currentUser?.uid.toString() || ds.child("playerId").value.toString() == auth.currentUser?.uid.toString()){
                                games.add(
                                    Game(
                                        ds.child("id").value.toString(),
                                        ds.child("ownerId").value.toString(),
                                        null,
                                        ds.child("playerId").value.toString(),
                                        ds.child("winnerId").value.toString(),
                                        ds.child("lapId").value.toString())
                                )
                            }
                        }
                    }
                    _binding.gameListRecyclerView.adapter = GameListAdapter(games.asReversed())

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }
            })
        _binding.btnHistoryGotomenu.setOnClickListener {
            findNavController().navigate(R.id.action_historyFragment_to_menuFragment)
        }

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
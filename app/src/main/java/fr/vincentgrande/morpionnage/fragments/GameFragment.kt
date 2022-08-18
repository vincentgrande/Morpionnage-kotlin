package fr.vincentgrande.morpionnage.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import fr.vincentgrande.morpionnage.Game
import fr.vincentgrande.morpionnage.GameBoard
import fr.vincentgrande.morpionnage.R
import fr.vincentgrande.morpionnage.User
import fr.vincentgrande.morpionnage.databinding.FragmentGameBinding
import java.util.*

class GameFragment: Fragment() {
    private lateinit var _binding: FragmentGameBinding
    private lateinit var auth: FirebaseAuth
    private val args: GameFragmentArgs by navArgs()
    private lateinit var uid:String
    private lateinit var currentUser:User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth

        _binding = FragmentGameBinding.inflate(inflater, container, false)

        _binding.btnGotomenu.setOnClickListener {
            findNavController().navigate(R.id.action_gameFragment_to_menuFragment)
        }
        val userRef = FirebaseDatabase.getInstance().getReference("/users/${auth.currentUser!!.uid}/")
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<User>()
                if (user != null) {
                    currentUser = user
                    if(args.gameId == "null"){
                        uid = UUID.randomUUID().toString().take(8)
                        val gameRef = FirebaseDatabase.getInstance().getReference("/games/$uid/")
                        gameRef.setValue(Game(uid, auth.currentUser?.uid.toString(), GameBoard(), "", "", auth.currentUser?.uid))
                            .addOnSuccessListener {
                                Toast.makeText(
                                    context, "Partie créée.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        gameRef.addValueEventListener(object : ValueEventListener {
                            @SuppressLint("SetTextI18n")
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val game: Game? = dataSnapshot.getValue(Game::class.java)
                                _binding.tvGameid.text = "ID du jeu : ${game?.id}"
                                val playerRef = FirebaseDatabase.getInstance().getReference("/users/${game?.playerId}/")
                                playerRef.addValueEventListener(object : ValueEventListener {
                                    @SuppressLint("SetTextI18n")
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val player: User? = dataSnapshot.getValue(User::class.java)
                                        if(player != null) {
                                            _binding.tvPlayer.text = "Adversaire : ${player.username}"
                                            if(game?.lapId == auth.currentUser?.uid){
                                                _binding.tvLap.text = "Au tour de : ${currentUser.username}"
                                            }else{
                                                _binding.tvLap.text = "Au tour de : ${player.username}"
                                            }
                                            //buttons binding
                                            _binding.aa.setOnClickListener {
                                                sendToDB("aa", 1, gameRef, game?.lapId, game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.playerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.ab.setOnClickListener {
                                                sendToDB("ab", 1, gameRef, game?.lapId, game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.playerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.ac.setOnClickListener {
                                                sendToDB("ac",1,gameRef,game?.lapId,game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.playerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.ba.setOnClickListener {
                                                sendToDB("ba",1,gameRef,game?.lapId,game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.playerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.bb.setOnClickListener {
                                                sendToDB("bb",1,gameRef,game?.lapId,game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.playerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.bc.setOnClickListener {
                                                sendToDB("bc",1,gameRef,game?.lapId,game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.playerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.ca.setOnClickListener {
                                                sendToDB("ca",1,gameRef,game?.lapId,game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.playerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.cb.setOnClickListener {
                                                sendToDB("cb",1,gameRef,game?.lapId,game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.playerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.cc.setOnClickListener {
                                                sendToDB("cc",1,gameRef,game?.lapId,game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.playerId.toString() ) as Map<String, Any>)
                                            }
                                            //end
                                        }
                                    }
                                    override fun onCancelled(databaseError: DatabaseError) {
                                        println("The read failed: " + databaseError.code)
                                    }
                                })
                                game?.gameBoard?.let {
                                    updateBoard(it)
                                    game.ownerId?.let { it1 -> game.playerId?.let { it2 ->
                                        game.id?.let { it3 ->
                                            checkWin(it, it1,
                                                it2,
                                                it3
                                            )
                                        }
                                    }
                                    }
                                }
                            }
                            override fun onCancelled(databaseError: DatabaseError) {
                                println("The read failed: " + databaseError.code)
                            }
                        })
                    }
                    else{
                        uid = args.gameId.replace(" ","")
                        val gameRef = FirebaseDatabase.getInstance().getReference("/games/$uid/")
                        gameRef.updateChildren(hashMapOf( "playerId" to auth.currentUser?.uid ) as Map<String, Any>)

                        gameRef.addValueEventListener(object : ValueEventListener {
                            @SuppressLint("SetTextI18n")
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val game: Game? = dataSnapshot.getValue(Game::class.java)
                                _binding.tvGameid.text = "ID du jeu : ${game?.id}"
                                val ownerRef = FirebaseDatabase.getInstance().getReference("/users/${game?.ownerId}/")
                                ownerRef.addValueEventListener(object : ValueEventListener {
                                    @SuppressLint("SetTextI18n")
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val player: User? = dataSnapshot.getValue(User::class.java)
                                        if(player != null) {
                                            _binding.tvPlayer.text = "Adversaire : ${player.username}"
                                            if(game?.lapId == auth.currentUser?.uid){
                                                _binding.tvLap.text = "Au tour de : ${currentUser.username}"
                                            }else{
                                                _binding.tvLap.text = "Au tour de : ${player.username}"
                                            }
                                            //buttons binding
                                            _binding.aa.setOnClickListener {
                                                sendToDB("aa", 2, gameRef, game?.lapId, game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.ownerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.ab.setOnClickListener {
                                                sendToDB("ab", 2, gameRef, game?.lapId, game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.ownerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.ac.setOnClickListener {
                                                sendToDB("ac", 2, gameRef, game?.lapId, game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.ownerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.ba.setOnClickListener {
                                                sendToDB("ba",2,gameRef,game?.lapId,game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.ownerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.bb.setOnClickListener {
                                                sendToDB("bb",2,gameRef,game?.lapId,game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.ownerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.bc.setOnClickListener {
                                                sendToDB("bc",2,gameRef,game?.lapId,game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.ownerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.ca.setOnClickListener {
                                                sendToDB("ca",2,gameRef,game?.lapId,game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.ownerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.cb.setOnClickListener {
                                                sendToDB("cb",2,gameRef,game?.lapId,game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.ownerId.toString() ) as Map<String, Any>)
                                            }
                                            _binding.cc.setOnClickListener {
                                                sendToDB("cc", 2, gameRef, game?.lapId, game?.gameBoard)
                                                gameRef.updateChildren(hashMapOf( "lapId" to game!!.ownerId.toString() ) as Map<String, Any>)
                                            }
                                            //end
                                        }
                                    }
                                    override fun onCancelled(databaseError: DatabaseError) {
                                        println("The read failed: " + databaseError.code)
                                    }
                                })
                                game?.gameBoard?.let {
                                    updateBoard(it)
                                    game.ownerId?.let { it1 -> game.playerId?.let { it2 ->
                                        game.id?.let { it3 ->
                                            checkWin(it, it1,
                                                it2,
                                                it3
                                            )
                                        }
                                    } }
                                }
                            }
                            override fun onCancelled(databaseError: DatabaseError) {
                                println("The read failed: " + databaseError.code)
                            }
                        })
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
        return _binding.root
    }

    private fun sendToDB(coord: String, symbol: Int, ref: DatabaseReference, lapId: String?, gameBoard: GameBoard?){

        if(lapId == auth.currentUser!!.uid){
            if(coord == "aa") {
                if(gameBoard?.topLine?.get(0) == 0) {
                    val topLine = listOf<Int>(symbol, gameBoard.topLine!!.get(1),gameBoard.topLine!!.get(2))
                    gameBoard.topLine = topLine
                }
            }
            if(coord == "ab") {
                if(gameBoard?.topLine?.get(1) == 0) {
                    val topLine = listOf<Int>(gameBoard.topLine!!.get(0),symbol,gameBoard.topLine!!.get(2))
                    gameBoard.topLine = topLine
                }
            }
            if(coord == "ac") {
                if(gameBoard?.topLine?.get(2) == 0) {
                    val topLine = listOf<Int>(gameBoard.topLine!!.get(0),gameBoard.topLine!!.get(1),symbol)
                    gameBoard.topLine = topLine
                }
            }
            if(coord == "ba") {
                if(gameBoard?.middleLine?.get(0) == 0) {
                    val middleLine = listOf<Int>(symbol,gameBoard.middleLine!!.get(1),gameBoard.middleLine!!.get(2))
                    gameBoard.middleLine = middleLine
                }
            }
            if(coord == "bb") {
                if(gameBoard?.middleLine?.get(1) == 0) {
                    val middleLine = listOf<Int>(gameBoard.middleLine!!.get(0),symbol,gameBoard.middleLine!!.get(2))
                    gameBoard.middleLine = middleLine
                }
            }
            if(coord == "bc") {
                if(gameBoard?.middleLine?.get(2) == 0) {
                    val middleLine = listOf<Int>(gameBoard.middleLine!!.get(0),gameBoard.middleLine!!.get(1),symbol)
                    gameBoard.middleLine = middleLine
                }
            }
            if(coord == "ca") {
                if(gameBoard?.bottomLine?.get(0) == 0) {
                    val bottomLine = listOf<Int>(symbol,gameBoard.bottomLine!!.get(1),gameBoard.bottomLine!!.get(2))
                    gameBoard.bottomLine = bottomLine
                }
            }
            if(coord == "cb") {
                if(gameBoard?.bottomLine?.get(1) == 0) {
                    val bottomLine = listOf<Int>(gameBoard.bottomLine!!.get(0),symbol,gameBoard.bottomLine!!.get(2))
                    gameBoard.bottomLine = bottomLine
                }
            }
            if(coord == "cc") {
                if(gameBoard?.bottomLine?.get(2) == 0) {
                    val bottomLine = listOf<Int>(gameBoard.bottomLine!!.get(0),gameBoard.bottomLine!!.get(1),symbol)
                    gameBoard.bottomLine = bottomLine
                }
            }
            ref.updateChildren(hashMapOf( "gameBoard" to gameBoard) as Map<String, Any?>)
        }
    }

    private fun updateBoard(gameBoard: GameBoard){
        if(gameBoard.topLine?.get(0).toString() == "1") {
            _binding.aa.text = "O"
            _binding.aa.isEnabled = false
        }
        else if(gameBoard.topLine?.get(0).toString() == "2") {
            _binding.aa.text = "X"
            _binding.aa.isEnabled = false
        }
        else {
            _binding.aa.text = ""
        }
        if(gameBoard.topLine?.get(1).toString() == "1") {
            _binding.ab.text = "O"
            _binding.ab.isEnabled = false
        }
        else if(gameBoard.topLine?.get(1).toString() == "2") {
            _binding.ab.text = "X"
            _binding.ab.isEnabled = false
        }
        else {
            _binding.ab.text = ""
        }
        if(gameBoard.topLine?.get(2).toString()== "1") {
            _binding.ac.text = "O"
            _binding.ac.isEnabled = false
        }
        else if(gameBoard.topLine?.get(2).toString() == "2") {
            _binding.ac.text = "X"
            _binding.ac.isEnabled = false
        }
        else {
            _binding.ac.text = ""
        }

        if(gameBoard.middleLine?.get(0).toString() == "1") {
            _binding.ba.text = "O"
            _binding.ba.isEnabled = false
        }
        else if(gameBoard.middleLine?.get(0).toString() == "2") {
            _binding.ba.text = "X"
            _binding.ba.isEnabled = false
        }
        else {
            _binding.ba.text = ""
        }
        if(gameBoard.middleLine?.get(1).toString() == "1") {
            _binding.bb.text = "O"
            _binding.bb.isEnabled = false
        }
        else if(gameBoard.middleLine?.get(1).toString() == "2") {
            _binding.bb.text = "X"
            _binding.bb.isEnabled = false
        }
        else {
            _binding.bb.text = ""
        }
        if(gameBoard.middleLine?.get(2).toString() == "1") {
            _binding.bc.text = "O"
            _binding.bc.isEnabled = false
        }
        else if(gameBoard.middleLine?.get(2).toString() == "2") {
            _binding.bc.text = "X"
            _binding.bc.isEnabled = false
        }
        else {
            _binding.bc.text = ""
        }

        if(gameBoard.bottomLine?.get(0).toString() == "1") {
            _binding.ca.text = "O"
            _binding.ca.isEnabled = false
        }
        else if(gameBoard.bottomLine?.get(0).toString() == "2") {
            _binding.ca.text = "X"
            _binding.ca.isEnabled = false
        }
        else {
            _binding.ca.text = ""
        }
        if(gameBoard.bottomLine?.get(1).toString() == "1") {
            _binding.cb.text = "O"
            _binding.cb.isEnabled = false
        }
        else if(gameBoard.bottomLine?.get(1).toString() == "2") {
            _binding.cb.text = "X"
            _binding.cb.isEnabled = false
        }
        else {
            _binding.cb.text = ""
        }
        if(gameBoard.bottomLine?.get(2).toString() == "1") {
            _binding.cc.text = "O"
            _binding.cc.isEnabled = false
        }
        else if(gameBoard.bottomLine?.get(2).toString() == "2") {
            _binding.cc.text = "X"
            _binding.cc.isEnabled = false
        }
        else {
            _binding.cc.text = ""
        }

    }

    private fun checkWin(gameBoard: GameBoard, ownerId: String, playerId: String, gameId: String){

        if(
            gameBoard.topLine == listOf<Int>(1,1,1) ||
            gameBoard.middleLine == listOf<Int>(1,1,1) ||
            gameBoard.bottomLine == listOf<Int>(1,1,1) ||
            (gameBoard.topLine?.get(0) == 1 && gameBoard.middleLine?.get(0) == 1 && gameBoard.bottomLine?.get(0) == 1) ||
            (gameBoard.topLine?.get(1) == 1 && gameBoard.middleLine?.get(1) == 1 && gameBoard.bottomLine?.get(1) == 1) ||
            (gameBoard.topLine?.get(2) == 1 && gameBoard.middleLine?.get(2) == 1 && gameBoard.bottomLine?.get(2) == 1) ||
            (gameBoard.topLine?.get(0) == 1 && gameBoard.middleLine?.get(1) == 1 && gameBoard.bottomLine?.get(2) == 1) ||
            (gameBoard.topLine?.get(2) == 1 && gameBoard.middleLine?.get(1) == 1 && gameBoard.bottomLine?.get(0) == 1)
                    ){
            setWinner(ownerId, gameId)
        }
        else if(
            gameBoard.topLine == listOf<Int>(2,2,2) ||
            gameBoard.middleLine == listOf<Int>(2,2,2) ||
            gameBoard.bottomLine == listOf<Int>(2,2,2) ||
            (gameBoard.topLine?.get(0) == 2 && gameBoard.middleLine?.get(0) == 2 && gameBoard.bottomLine?.get(0) == 2) ||
            (gameBoard.topLine?.get(1) == 2 && gameBoard.middleLine?.get(1) == 2 && gameBoard.bottomLine?.get(1) == 2) ||
            (gameBoard.topLine?.get(2) == 2 && gameBoard.middleLine?.get(2) == 2 && gameBoard.bottomLine?.get(2) == 2) ||
            (gameBoard.topLine?.get(0) == 2 && gameBoard.middleLine?.get(1) == 2 && gameBoard.bottomLine?.get(2) == 2) ||
            (gameBoard.topLine?.get(2) == 2 && gameBoard.middleLine?.get(1) == 2 && gameBoard.bottomLine?.get(0) == 2)
        ) {
            setWinner(playerId, gameId)
        }
    }
    private fun setWinner(winnerId:String, gameId:String){
        val userRef = FirebaseDatabase.getInstance().getReference("/users/$winnerId/")
        userRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<User>()
                _binding.tvGameWinner.text = "Vainqueur : ${user!!.username}"
                Toast.makeText(context,"${user.username} a gagné",Toast.LENGTH_SHORT).show()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
        val gameRef = FirebaseDatabase.getInstance().getReference("/games/$gameId/")
        gameRef.updateChildren(hashMapOf( "winnerId" to winnerId ) as Map<String, Any>)

    }

}
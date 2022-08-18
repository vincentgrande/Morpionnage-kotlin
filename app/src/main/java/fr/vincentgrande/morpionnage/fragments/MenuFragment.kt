package fr.vincentgrande.morpionnage.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import fr.vincentgrande.morpionnage.R
import fr.vincentgrande.morpionnage.User
import fr.vincentgrande.morpionnage.databinding.FragmentMenuBinding
import fr.vincentgrande.morpionnage.dialogs.JoinGameDialogFragment
import fr.vincentgrande.morpionnage.dialogs.UsernameDialogFragment
import kotlin.system.exitProcess


class MenuFragment: Fragment() {

    private lateinit var _binding: FragmentMenuBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMenuBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        val currentUser = auth.currentUser
        if(currentUser == null){
            findNavController().navigate(R.id.action_menuFragment_to_loginFragment)
        }


        return _binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reference = FirebaseDatabase.getInstance().getReference("/users/${auth.currentUser?.uid}/")


        reference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue(User::class.java)
                if(user?.username != null){
                    _binding.tvUsername.text = "Bienvenue ${user.username} !"
                }else{
                    //findNavController().navigate(R.id.action_menuFragment_to_loginFragment)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

        _binding.btnSetUsername.setOnClickListener {
            val dialog = UsernameDialogFragment()
            dialog.show(requireActivity().supportFragmentManager, "UsernameDialogListener")
        }

        _binding.btnLogout.setOnClickListener{
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_menuFragment_to_loginFragment)
        }
        _binding.btnGotosettings.setOnClickListener{
            findNavController().navigate((R.id.action_menuFragment_to_settingsFragment))
        }
        _binding.btnLeave.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Es-tu sur de vouloir quitter ?")
            builder.setPositiveButton("OUI") { _, _ ->
                requireActivity().finish()
                exitProcess(0)
            }
            builder.setNeutralButton("NON") { _, _ ->
            }
            builder.show()
        }

        _binding.btnCreateGame.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToGameFragment()
            findNavController().navigate(action)
        }
        _binding.btnJoinGame.setOnClickListener {
            val dialog = JoinGameDialogFragment()
            dialog.show(requireActivity().supportFragmentManager, "JoinGameDialogListener")
        }
        _binding.btnHistory.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_historyFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
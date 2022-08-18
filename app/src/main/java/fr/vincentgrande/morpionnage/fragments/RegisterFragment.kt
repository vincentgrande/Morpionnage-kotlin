package fr.vincentgrande.morpionnage.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.vincentgrande.morpionnage.R
import fr.vincentgrande.morpionnage.User
import fr.vincentgrande.morpionnage.databinding.FragmentRegisterBinding


class RegisterFragment:Fragment() {

    private lateinit var _binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        _binding.btnGotologin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        _binding.btnRegister.setOnClickListener {
            val email:String = _binding.editTextEmail.text.toString()
            val password:String = _binding.editTextPassword.text.toString()
            if (email != "" && password != "") {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = User("Morpion",auth.currentUser?.email.toString())

                            val reference = FirebaseDatabase.getInstance().getReference("/users/")
                            Log.d("FATAL",reference.toString())
                            reference.child(auth.currentUser!!.uid).setValue(user)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Compte créé !",
                                        Toast.LENGTH_SHORT).show()
                                }

                            findNavController().navigate(R.id.action_registerFragment_to_menuFragment)

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(context, "Erreur veuillez réessayer.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                Toast.makeText(context, "Veuillez renseigner un email et un mot de passe.",
                    Toast.LENGTH_SHORT).show()
            }
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
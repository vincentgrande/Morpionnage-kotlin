package fr.vincentgrande.morpionnage.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.vincentgrande.morpionnage.R
import fr.vincentgrande.morpionnage.databinding.FragmentLoginBinding

class LoginFragment: Fragment() {

    private lateinit var _binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        _binding.btnLogin.setOnClickListener {
            val email:String = _binding.editTextLoginEmail.text.toString()
            val password:String = _binding.editTextLoginPassword.text.toString()
            if (email != "" && password != "") {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            Toast.makeText(context, "Connexion réussie",
                                Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_loginFragment_to_menuFragment)

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(context, "Erreur lors de la connexion.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                Toast.makeText(context, "Veuillez renseigner vos informations de connexion.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        _binding.btnGotoregister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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
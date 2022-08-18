package fr.vincentgrande.morpionnage.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import fr.vincentgrande.morpionnage.R
import fr.vincentgrande.morpionnage.databinding.FragmentSettingsBinding
import kotlin.system.exitProcess

class SettingsFragment: Fragment(){

    private lateinit var _binding: FragmentSettingsBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.btnGotomenu.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_menuFragment)
        }
        _binding.editTextOldEmail.append(auth.currentUser?.email)

        _binding.btnUpdateProfile.setOnClickListener {
            Log.d("fatal","click")
            val user = auth.currentUser
            val credential = EmailAuthProvider.getCredential(_binding.editTextOldEmail.text.toString(), _binding.editTextOldPassword.text.toString())
            auth.currentUser!!.reauthenticate(credential).addOnCompleteListener {
                Log.d("fatal","on reauth")
                if(auth.currentUser?.email != _binding.editTextSettingsEmail.text.toString() && _binding.editTextSettingsEmail.text.isNotEmpty()) {
                    Log.d("fatal","go changer le mail")
                    auth.currentUser!!.updateEmail(_binding.editTextSettingsEmail.text.toString())
                            .addOnCompleteListener { task ->
                                Log.d("fatal","est ce que c'est reussi ? ${task.result}")
                                if (task.isSuccessful) {
                                    Log.d("fatal","ah oui oui oui")
                                    Toast.makeText(context,"Email modifié avec succès !",
                                        Toast.LENGTH_SHORT).show()
                                    val reference = FirebaseDatabase.getInstance().getReference("/users/${user!!.uid}/")
                                    reference.updateChildren(hashMapOf("email" to _binding.editTextSettingsEmail.text.toString()) as Map<String, Any>)
                                        .addOnCompleteListener { Log.d("fatal","jai mis a jour la db ${it.result}") }

                                }
                            }
                            .addOnFailureListener {
                                Log.d("fatal","bon bah non enfaite")
                                Toast.makeText(context,"Erreur veuillez réessayer.", Toast.LENGTH_SHORT).show()
                            }
                    }
                Log.d("fatal","allez au suivant")
                if(_binding.editTextSettingsPassword.text.toString().isNotEmpty()) {
                    Log.d("fatal","du coup je veux changer le password")
                    auth.currentUser!!.updatePassword(_binding.editTextSettingsPassword.text.toString() )
                            .addOnCompleteListener { task ->
                                Log.d("fatal","peut etre que c bon")
                                if (task.isSuccessful) {
                                    Log.d("fatal","c boooon")
                                    Toast.makeText(context,"Mot de passe modifié avec succès !",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                    }

                    findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
                }
        }

        _binding.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Es-tu sur de vouloir supprimer ton compte ?")
            builder.setPositiveButton("OUI") { _, _ ->
                auth.currentUser!!.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User account deleted.")
                            auth.signOut()
                            findNavController().navigate(R.id.action_settingsFragment_to_registerFragment)
                        }
                    }

            }
            builder.setNeutralButton("NON") { _, _ ->
            }
            builder.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }



}
/**/
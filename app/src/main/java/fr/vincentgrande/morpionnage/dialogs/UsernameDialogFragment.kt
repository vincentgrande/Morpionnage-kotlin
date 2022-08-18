package fr.vincentgrande.morpionnage.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import fr.vincentgrande.morpionnage.R
import fr.vincentgrande.morpionnage.User
import fr.vincentgrande.morpionnage.databinding.DialogNameBinding


class UsernameDialogFragment : DialogFragment() {

    private lateinit var _binding: DialogNameBinding;
    private lateinit var auth: FirebaseAuth

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogNameBinding.inflate(layoutInflater)
        auth = Firebase.auth
        val reference = FirebaseDatabase.getInstance().getReference("/users/${auth.currentUser?.uid}/")
        reference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User? = dataSnapshot.getValue(User::class.java)
                _binding.edittextUsername.hint = "${user?.username}"
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Change de pseudo")
            val inflater = requireActivity().layoutInflater;
            builder.setView(inflater.inflate(R.layout.dialog_name, null))
                .setPositiveButton("Confirmer"
                ) { dialog, id ->
                    reference.updateChildren(hashMapOf( "username" to _binding.edittextUsername.text.toString(), "email" to auth.currentUser?.email ) as Map<String, Any>)
                }
                .setNegativeButton("Annuler"
                ) { dialog, id ->
                    getDialog()?.cancel()
                }
            builder.setView(_binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
package fr.vincentgrande.morpionnage.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import fr.vincentgrande.morpionnage.R
import fr.vincentgrande.morpionnage.databinding.DialogJoinGameBinding
import fr.vincentgrande.morpionnage.fragments.MenuFragmentDirections

class JoinGameDialogFragment: DialogFragment()  {

    private lateinit var _binding: DialogJoinGameBinding


    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogJoinGameBinding.inflate(layoutInflater)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(" Rejoindre une partie")
            val inflater = requireActivity().layoutInflater;
            builder.setView(inflater.inflate(R.layout.dialog_name, null))
                .setPositiveButton("Jouer !"
                ) { dialog, id ->
                    val action =
                        MenuFragmentDirections.actionMenuFragmentToGameFragment().setGameId(_binding.gameId.text.toString())
                    findNavController().navigate(action)
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
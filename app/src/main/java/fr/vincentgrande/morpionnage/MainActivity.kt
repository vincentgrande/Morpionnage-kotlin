package fr.vincentgrande.morpionnage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.vincentgrande.morpionnage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){

    private lateinit var _binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }

}
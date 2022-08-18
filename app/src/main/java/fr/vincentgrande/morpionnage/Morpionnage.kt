package fr.vincentgrande.morpionnage

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class Morpionnage: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
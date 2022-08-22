package com.example.piecehub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth
    private var pressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        val firebaseUser : FirebaseUser? = firebaseAuth.currentUser
        if (firebaseUser != null) {
            val intent = Intent(this, UserActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        if(pressedTime + 2000 > System.currentTimeMillis())
            finish()
        else
            Toast.makeText(applicationContext, "Naciśnij jeszcze raz aby wyjść", Toast.LENGTH_SHORT).show()
        pressedTime = System.currentTimeMillis()
    }

}
package com.example.piecehub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class UserActivity : AppCompatActivity() {

    private var pressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
    }

    override fun onBackPressed() {
        if(pressedTime + 2000 > System.currentTimeMillis())
            finish()
        else
            Toast.makeText(applicationContext, "Naciśnij jeszcze raz aby wyjść", Toast.LENGTH_SHORT).show()
        pressedTime = System.currentTimeMillis()
    }

}
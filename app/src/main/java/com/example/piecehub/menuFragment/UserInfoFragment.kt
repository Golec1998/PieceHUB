package com.example.piecehub.menuFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.piecehub.MainActivity
import com.example.piecehub.R
import com.example.piecehub.UserActivity
import com.google.firebase.auth.FirebaseAuth

class UserInfoFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_info, container, false)

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        view.findViewById<TextView>(R.id.name).text = firebaseUser?.email!!.split("@")[0].uppercase()
        view.findViewById<TextView>(R.id.email).text = firebaseUser?.email

        view.findViewById<Button>(R.id.logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            UserActivity().finish()
        }

        return view
    }

}
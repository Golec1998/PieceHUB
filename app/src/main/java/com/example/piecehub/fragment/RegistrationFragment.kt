package com.example.piecehub.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.piecehub.MainActivity
import com.example.piecehub.R
import com.example.piecehub.UserActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegistrationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_registration, container, false)

        view.findViewById<FloatingActionButton>(R.id.backButton).setOnClickListener { view.findNavController().navigate(R.id.action_registrationFragment_to_loginFragment) }
        view.findViewById<Button>(R.id.register).setOnClickListener { confirmRegistration(view) }

        return view
    }

    private fun confirmRegistration(view : View) {
        when {
            TextUtils.isEmpty(view.findViewById<EditText>(R.id.emailAddress).text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(context, "Proszę podać adres e-mail", Toast.LENGTH_SHORT).show()
                view.findViewById<EditText>(R.id.password).setText("")
                view.findViewById<EditText>(R.id.confirmPassword).setText("")
            }

            TextUtils.isEmpty(view.findViewById<EditText>(R.id.password).text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(context, "Proszę podać hasło", Toast.LENGTH_SHORT).show()
                view.findViewById<EditText>(R.id.confirmPassword).setText("")
            }

            TextUtils.isEmpty(view.findViewById<EditText>(R.id.confirmPassword).text.toString().trim { it <= ' ' }) || (view.findViewById<EditText>(R.id.password).text.toString().trim { it <= ' ' } != view.findViewById<EditText>(R.id.confirmPassword).text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(context, "Hasła nie są zgodne", Toast.LENGTH_SHORT).show()
                view.findViewById<EditText>(R.id.password).setText("")
                view.findViewById<EditText>(R.id.confirmPassword).setText("")
            }

            else -> {
                val email = view.findViewById<EditText>(R.id.emailAddress).text.toString().trim { it <= ' ' }
                val password = view.findViewById<EditText>(R.id.password).text.toString().trim { it <= ' ' }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        OnCompleteListener<AuthResult> { task ->
                            if(task.isSuccessful) {
                                val firebaseUser : FirebaseUser = task.result!!.user!!
                                Toast.makeText(context, "Zarejestrowano pomyślnie", Toast.LENGTH_SHORT).show()

                                val intent = Intent(context, UserActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("uid", firebaseUser.uid)
                                intent.putExtra("email", firebaseUser.email)
                                startActivity(intent)
                                MainActivity().finish()
                            }
                            else {
                                Toast.makeText(context, "Wystąpił błąd", Toast.LENGTH_SHORT).show()
                                view.findViewById<EditText>(R.id.password).setText("")
                                view.findViewById<EditText>(R.id.confirmPassword).setText("")
                            }
                        }
                    )
            }
        }
    }

}
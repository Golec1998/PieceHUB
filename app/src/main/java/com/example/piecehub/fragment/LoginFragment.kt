package com.example.piecehub.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.example.piecehub.MainActivity
import com.example.piecehub.R
import com.example.piecehub.UserActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment() {

    val REQUEST_CODE_SIGN_IN = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        makeClickableRegisterLink(view)

        view.findViewById<Button>(R.id.login).setOnClickListener { confirmLogin(view) }

        return view
    }

    private fun makeClickableRegisterLink(view : View) {
        val registerLink = view.findViewById<TextView>(R.id.register)
        val registerLinkText = SpannableString(registerLink.text.toString())

        val clickableRegisterLink = object : ClickableSpan() {

            override fun onClick(widget : View) {
                view.findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }

        }

        registerLinkText.setSpan(clickableRegisterLink, 16, 31, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        registerLink.text = registerLinkText
        registerLink.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun confirmLogin(view : View) {
        when {
            TextUtils.isEmpty(view.findViewById<EditText>(R.id.emailAddress).text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(context, "Proszę podać adres e-mail", Toast.LENGTH_SHORT).show()
                view.findViewById<EditText>(R.id.password).setText("")
            }

            TextUtils.isEmpty(view.findViewById<EditText>(R.id.password).text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(context, "Proszę podać hasło", Toast.LENGTH_SHORT).show()
            }

            else -> {
                val email = view.findViewById<EditText>(R.id.emailAddress).text.toString().trim { it <= ' ' }
                val password = view.findViewById<EditText>(R.id.password).text.toString().trim { it <= ' ' }

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        OnCompleteListener<AuthResult> { task ->
                            if(task.isSuccessful) {
                                Toast.makeText(context, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show()

                                val intent = Intent(context, UserActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                MainActivity().finish()
                            }
                            else {
                                Toast.makeText(context, "Wystąpił błąd", Toast.LENGTH_SHORT).show()
                                view.findViewById<EditText>(R.id.password).setText("")
                            }
                        }
                    )
            }
        }
    }

}
package com.example.piecehub.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.piecehub.R
import com.example.piecehub.data.Piece
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class InsertDataFragment : Fragment() {

    private val args by navArgs<InsertDataFragmentArgs>()
    private lateinit var targetKey : String
    private lateinit var user : FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_insert_data, container, false)

        user = FirebaseAuth.getInstance().currentUser!!

        val dbReference = FirebaseDatabase.getInstance().getReference("Pieces").child(user.uid)
        targetKey = args.itemKey

        if (args.type == "book")
            view.findViewById<ImageView>(R.id.type).setImageResource(R.drawable.ic_book)
        else if (args.type == "movie")
            view.findViewById<ImageView>(R.id.type).setImageResource(R.drawable.ic_movie)

        view.findViewById<RadioGroup>(R.id.status).check(R.id.finished)

        if(targetKey != "") {

            var data : Piece
            dbReference.child(targetKey).addValueEventListener(object : ValueEventListener {

                override fun onCancelled(error : DatabaseError) {}

                override fun onDataChange(snapshot : DataSnapshot) {
                    if (snapshot.getValue() != null) {
                        data = snapshot.getValue(Piece::class.java)!!

                        view.findViewById<EditText>(R.id.title).setText(data.title)
                        view.findViewById<EditText>(R.id.author).setText(data.author)

                        if (data.progress != "Ukończono") {
                            view.findViewById<RadioGroup>(R.id.status).check(R.id.notFinished)
                            view.findViewById<EditText>(R.id.progress).setText(data.progress)
                        }
                    }
                }

            })

        }
        else {
            view.findViewById<FloatingActionButton>(R.id.delete).hide()
            targetKey = Date().time.toString()
        }

        view.findViewById<ImageButton>(R.id.backButton).setOnClickListener { view.findNavController().navigate(R.id.action_insertDataFragment_to_listFragment) }
        view.findViewById<FloatingActionButton>(R.id.finish).setOnClickListener { setData(view) }
        view.findViewById<FloatingActionButton>(R.id.delete).setOnClickListener { deleteData(view) }

        return view
    }

    private fun setData(view : View) {
        val title = view.findViewById<EditText>(R.id.title).text.toString()
        val author = view.findViewById<EditText>(R.id.author).text.toString()
        val type = args.type
        var progress = "Ukończono"

        if (view.findViewById<RadioGroup>(R.id.status).checkedRadioButtonId == R.id.notFinished)
            progress = view.findViewById<EditText>(R.id.progress).text.toString()

        if (title.isNotEmpty() && progress.isNotEmpty()) {
            val newPiece = Piece(targetKey, title, author, type, progress)
            FirebaseDatabase.getInstance().getReference("Pieces").child(user.uid).child(targetKey).setValue(newPiece).addOnCompleteListener {
                view.findNavController().navigate(R.id.action_insertDataFragment_to_listFragment)
            }.addOnFailureListener {
                Toast.makeText(context, "Wystąpił błąd", Toast.LENGTH_SHORT).show()
            }
        }
        else if (title.isEmpty())
            Toast.makeText(context, "Uzupełnij tytuł", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Uzupełnij stopień ukończenia", Toast.LENGTH_SHORT).show()
    }

    private fun deleteData(view : View) {
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Tak") { _, _ ->

            FirebaseDatabase.getInstance().getReference("Pieces").child(user.uid).child(targetKey).removeValue().addOnCompleteListener {
                view.findNavController().navigate(R.id.action_insertDataFragment_to_listFragment)
            }.addOnFailureListener {
                Toast.makeText(context, "Wystąpił błąd", Toast.LENGTH_SHORT).show()
            }

        }
        builder.setNegativeButton("Nie") { _, _ -> }
        builder.setTitle("Usuń wpis")
        builder.setMessage("Czy chcesz usunąć wpis?")
        builder.create().show()
    }

}
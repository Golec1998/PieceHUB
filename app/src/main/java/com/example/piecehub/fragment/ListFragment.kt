package com.example.piecehub.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piecehub.R
import com.example.piecehub.data.Adapter
import com.example.piecehub.data.Piece
import com.example.piecehub.menuFragment.FilterFragment
import com.example.piecehub.menuFragment.UserInfoFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList

class ListFragment : Fragment() {

    private lateinit var user : FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        user = FirebaseAuth.getInstance().currentUser!!

        view.findViewById<ImageButton>(R.id.userInfo).setOnClickListener { showUserData() }
        view.findViewById<FloatingActionButton>(R.id.addPiece).setOnClickListener { addPiece(view) }

        val dbReference = FirebaseDatabase.getInstance().getReference("Pieces").child(user.uid)
        var dataArray : MutableList<Piece>
        dbReference.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error : DatabaseError) {}

            override fun onDataChange(snapshot : DataSnapshot) {
                dataArray = ArrayList()
                for (row in snapshot.children) {
                    val newRow = row.getValue(Piece::class.java)
                    dataArray.add(newRow!!)
                }
                setupAdapter(view, dataArray)
            }

        })

        return view
    }

    private fun setupAdapter(view : View, dataArray : MutableList<Piece>) {
        view.findViewById<RecyclerView>(R.id.itemList).adapter = Adapter(dataArray)
        view.findViewById<RecyclerView>(R.id.itemList).layoutManager = LinearLayoutManager(context)
    }

    private fun addPiece(view : View) {
        val filterDialog = FilterFragment(view)
        filterDialog.show(parentFragmentManager, "filterFragment")
    }

    private fun showUserData() {
        val userInfoDialog = UserInfoFragment()
        userInfoDialog.show(parentFragmentManager, "userInfoFragment")
    }

}
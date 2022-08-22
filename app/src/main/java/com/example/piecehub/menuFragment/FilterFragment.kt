package com.example.piecehub.menuFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import com.example.piecehub.R
import com.example.piecehub.fragment.ListFragmentDirections

class FilterFragment(mainView : View) : DialogFragment() {

    val mainView : View = mainView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter, container, false)

        view.findViewById<Button>(R.id.game).setOnClickListener { openInsertFragment("game") }
        view.findViewById<Button>(R.id.book).setOnClickListener { openInsertFragment("book") }
        view.findViewById<Button>(R.id.movie).setOnClickListener { openInsertFragment("movie") }

        return view
    }

    private fun openInsertFragment(type : String) {
        mainView.findNavController().navigate(ListFragmentDirections.actionListFragmentToInsertDataFragment(type, ""))
        dismiss()
    }

}
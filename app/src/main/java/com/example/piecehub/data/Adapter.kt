package com.example.piecehub.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.piecehub.R
import com.example.piecehub.fragment.ListFragmentDirections

class Adapter (private val dataArray : MutableList<Piece>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
            val inflater : LayoutInflater = LayoutInflater.from(parent.context)
            val view : View = inflater.inflate(R.layout.list_item, parent, false)
            return ViewHolder(view)
        }

    override fun onBindViewHolder(holder : Adapter.ViewHolder, position : Int) {
        holder.titleTV.text = dataArray[holder.adapterPosition].title
        holder.authorTV.text = dataArray[holder.adapterPosition].author
        if(dataArray[holder.adapterPosition].type == "book")
            holder.typeImg.setImageResource(R.drawable.ic_book)
        else if(dataArray[holder.adapterPosition].type == "movie")
            holder.typeImg.setImageResource(R.drawable.ic_movie)
        if(dataArray[holder.adapterPosition].progress != "Ukończono") {
            holder.progressTV.text = dataArray[holder.adapterPosition].progress
            holder.progressImg.setImageResource(R.drawable.ic_in_progress)
        }
        holder.titleTV.setOnClickListener {
            holder.itemView.findNavController().navigate(ListFragmentDirections.actionListFragmentToInsertDataFragment(dataArray[holder.adapterPosition].type, dataArray[holder.adapterPosition].key))
        }
    }

    override fun getItemCount() : Int {
        return dataArray.count()
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val titleTV = view.findViewById<TextView>(R.id.title)
        val authorTV = view.findViewById<TextView>(R.id.author)
        val typeImg = view.findViewById<ImageView>(R.id.type)
        val progressTV = view.findViewById<TextView>(R.id.progress)
        val progressImg = view.findViewById<ImageView>(R.id.progressImage)
    }

}
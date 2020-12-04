package ru.lemaitre.quiz.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.lemaitre.quiz.R
import ru.lemaitre.quiz.`interface`.ItemClickListener

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private lateinit var itemClickListener: ItemClickListener

    val category_image = itemView.findViewById<ImageView>(R.id.category_image)
    val category_name = itemView.findViewById<TextView>(R.id.category_name)

    init {
        itemView.setOnClickListener(this)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }

    override fun onClick(v: View?) {
        itemClickListener.onClick(itemView, adapterPosition, false)
    }
}
package ru.lemaitre.quiz


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import ru.lemaitre.quiz.ViewHolder.CategoryViewHolder
import ru.lemaitre.quiz.`interface`.ItemClickListener
import ru.lemaitre.quiz.common.Common
import ru.lemaitre.quiz.model.Category


class CategoryFragment private constructor(): Fragment() {
    lateinit var myFragment: View
    lateinit var listCategory:RecyclerView
    lateinit var adapter: FirebaseRecyclerAdapter<Category, CategoryViewHolder>

    lateinit var database:FirebaseDatabase
    lateinit var categories:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = FirebaseDatabase.getInstance()
        categories = database.getReference("category")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myFragment = inflater.inflate(R.layout.fragment_category, container, false)

        listCategory = myFragment.findViewById(R.id.listCategory)
        listCategory.setHasFixedSize(true)
        listCategory.layoutManager = LinearLayoutManager(container?.context)

        loadCategories()

        return myFragment
    }

    private fun loadCategories() {

        adapter = object:FirebaseRecyclerAdapter<Category, CategoryViewHolder>(
            Category::class.java,
            R.layout.category_layout,
            CategoryViewHolder::class.java,
            categories
        ){
            override fun populateViewHolder(viewHolder: CategoryViewHolder, model: Category, position: Int){
                viewHolder.category_name.text = model.Name
                Picasso.with(context)
                    .load(model.Image)
                    .into(viewHolder.category_image)

                viewHolder.setItemClickListener(object :ItemClickListener{
                    override fun onClick(view: View, position: Int, isLongClick: Boolean) {
//                        Toast.makeText(context, String.format("%s|%s", adapter.getRef(position).key, model.Name), Toast.LENGTH_SHORT).show()
                        val startGame = Intent(context, Start::class.java)
                        Common.categoryId = adapter.getRef(position).key
                        startActivity(startGame)
                    }

                })
            }
        }
        adapter.notifyDataSetChanged()
        listCategory.adapter = adapter
    }

    companion object{
        fun newInstance():CategoryFragment{
            return CategoryFragment()
        }
    }
}
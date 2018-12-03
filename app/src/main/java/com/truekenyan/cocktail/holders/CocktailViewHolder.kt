package com.truekenyan.cocktail.holders

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.activities.CocktailActivity
import com.truekenyan.cocktail.models.CocktailModel
import com.truekenyan.cocktail.utils.Commons

class CocktailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView = itemView.findViewById<ImageView>(R.id.cocktail_image)
    private val cocktailName = itemView.findViewById<TextView>(R.id.cocktail_name)
    private val mainPanel = itemView.findViewById<LinearLayout>(R.id.parentPanel)
    private val mainParent = itemView.findViewById<CardView>(R.id.main_parent)

    fun bind(cocktail: CocktailModel){
        Picasso.get()
                .load(cocktail.strDrinkThumb)
                .placeholder(R.drawable.placeholder)
                .into(imageView)
        cocktailName.text = cocktail.strDrink
        mainPanel.setOnClickListener(clickListener(cocktail.idDrink, itemView.context))
        mainParent.setOnClickListener(clickListener(cocktail.idDrink, itemView.context))
        imageView.setOnClickListener(clickListener(cocktail.idDrink, itemView.context))
        cocktailName.setOnClickListener(clickListener(cocktail.idDrink, itemView.context))
    }

    private fun clickListener(id: String?, context: Context) : View.OnClickListener{
        return View.OnClickListener { _ ->
            val i = Intent(context, CocktailActivity::class.java).apply {
                putExtra(Commons.DRINK_ID, id)
            }
            context.startActivity(i)
        }
    }
}
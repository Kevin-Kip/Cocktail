package com.truekenyan.cocktail.holders

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.activities.CocktailActivity
import com.truekenyan.cocktail.models.CocktailModel
import com.truekenyan.cocktail.utils.Commons

class CocktailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView = itemView.findViewById<ImageView>(R.id.cocktail_image)
    private val cocktailName = itemView.findViewById<TextView>(R.id.cocktail_name)
    private val mainPanel = itemView.findViewById<ConstraintLayout>(R.id.parentPanel)
    private val mainParent = itemView.findViewById<CardView>(R.id.main_parent)
    private var isMain: Boolean? = null

    fun bind(cocktail: CocktailModel, cocktails: List<CocktailModel>, main: Boolean?){
        isMain = main
        when {
            isMain!! -> {
                Picasso.get()
                        .load(cocktail.strDrinkThumb)
                        .fit()
                        .placeholder(R.drawable.placeholder)
                        .into(imageView)
            }
            else -> {
                Picasso.get()
                        .load(cocktail.strDrinkThumb)
                        .resize(250, 250)
                        .placeholder(R.drawable.placeholder)
                        .into(imageView)
            }
        }
        cocktailName.text = cocktail.strDrink
        mainPanel.setOnClickListener(clickListener(cocktail.idDrink, itemView.context, cocktails))
        mainParent.setOnClickListener(clickListener(cocktail.idDrink, itemView.context, cocktails))
        imageView.setOnClickListener(clickListener(cocktail.idDrink, itemView.context, cocktails))
        cocktailName.setOnClickListener(clickListener(cocktail.idDrink, itemView.context, cocktails))
    }

    private fun clickListener(id: String?, context: Context, cocktails: List<CocktailModel>) : View.OnClickListener{
        return View.OnClickListener { _ ->
            val i = Intent(context, CocktailActivity::class.java).apply {
                putExtra(Commons.DRINK_ID, id)
                putExtra(Commons.COCKTAILS, cocktails as ArrayList)
            }
            context.startActivity(i)
        }
    }
}
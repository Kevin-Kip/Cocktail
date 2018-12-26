package com.truekenyan.cocktail.holders

import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.activities.CocktailActivity
import com.truekenyan.cocktail.models.Fav
import com.truekenyan.cocktail.utils.Commons

class FavsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val parent: ConstraintLayout = itemView.findViewById(R.id.fav_parent)
    private val drinkImage: ImageView = itemView.findViewById(R.id.fav_image)
    private val deleteIcon: ImageView = itemView.findViewById(R.id.remove_fav)
    private val drinkName: TextView = itemView.findViewById(R.id.fav_name)
    private var favoriteId: Int? = null
    fun bind(fav: Fav) {
        favoriteId = fav.favId
        Picasso.get()
                .load(fav.drinkPhoto)
                .placeholder(R.drawable.placeholder)
                .into(drinkImage)
        drinkName.text = fav.drinkName

        drinkImage.setOnClickListener(clickListener(fav.drinkId, false))
        drinkName.setOnClickListener(clickListener(fav.drinkId, false))
        deleteIcon.setOnClickListener(clickListener(fav.drinkId, true))
        parent.setOnClickListener(clickListener(fav.drinkId, false))
    }

    private fun clickListener(drinkId: String?,  remove: Boolean?): View.OnClickListener{
        return View.OnClickListener {
            if (remove!!){
//                TODO delete
            } else {
                val i = Intent(itemView.context, CocktailActivity::class.java).apply {
                    putExtra(Commons.DRINK_ID, drinkId)
                }
                itemView.context.startActivity(i)
            }
        }
    }
}
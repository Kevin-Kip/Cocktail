package com.truekenyan.cocktail.models

import android.os.Parcel
import android.os.Parcelable

data class CocktailModel(
        var idDrink: String? = "",
        var strDrinkThumb: String? = "",
        var strDrink: String? = "",
        var strInstructions: String? = "",
        var strAlcoholic: String? = "",
        var strCategory: String? = "",
        var strGlass: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idDrink)
        parcel.writeString(strDrinkThumb)
        parcel.writeString(strDrink)
        parcel.writeString(strInstructions)
        parcel.writeString(strAlcoholic)
        parcel.writeString(strCategory)
        parcel.writeString(strGlass)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CocktailModel> {
        override fun createFromParcel(parcel: Parcel): CocktailModel {
            return CocktailModel(parcel)
        }

        override fun newArray(size: Int): Array<CocktailModel?> {
            return arrayOfNulls(size)
        }
    }
}

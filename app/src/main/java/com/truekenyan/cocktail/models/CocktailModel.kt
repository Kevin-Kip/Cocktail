package com.truekenyan.cocktail.models

import android.os.Parcel
import android.os.Parcelable

data class CocktailModel(var idDrink: String?,
                    var strDrinkThumb: String?,
                    var strDrink: String?,
                    var strInstructions: String?,
                    var strAlcoholic: String?,
                    var strCategory: String?,
                    var strGlass: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(idDrink)
        dest.writeString(strDrinkThumb)
        dest.writeString(strDrink)
        dest.writeString(strInstructions)
        dest.writeString(strAlcoholic)
        dest.writeString(strCategory)
        dest.writeString(strGlass)
    }

    override fun describeContents(): Int {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR
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

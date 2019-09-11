package com.truekenyan.cocktail.ui.custom

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.truekenyan.cocktail.R

class NoConnectionDialog {
    private fun create(activity: AppCompatActivity): AlertDialog {
        val parentView = activity.findViewById<ViewGroup>(R.id.container)
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_no_connection, parentView, false)

        val builder = AlertDialog.Builder(activity).apply {
            setView(dialogView)
        }
        return builder.create()
    }
}
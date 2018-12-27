package com.truekenyan.cocktail.fragments

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.utils.Commons

class FragmentInfo: Fragment() {
    enum class Action{
        ABOUT, SHARE, CONTACT
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_info, container, false)
//        about_parent.setOnClickListener(clickListener(Action.ABOUT))
//        about_title.setOnClickListener(clickListener(Action.ABOUT))
//        about_text.setOnClickListener(clickListener(Action.ABOUT))
//        contact_parent.setOnClickListener(clickListener(Action.CONTACT))
//        contact_title.setOnClickListener(clickListener(Action.CONTACT))
//        contact_text.setOnClickListener(clickListener(Action.CONTACT))
//        share_parent.setOnClickListener(clickListener(Action.SHARE))
//        share_title.setOnClickListener(clickListener(Action.SHARE))
//        share_text.setOnClickListener(clickListener(Action.SHARE))
        return rootView
    }

    private fun clickListener(a: Action): View.OnClickListener{
        return View.OnClickListener {
            when (a){
                Action.ABOUT -> {
                    AlertDialog.Builder(activity!!.applicationContext)
                            .setTitle(R.string.about)
                            .setMessage(R.string.about_body)
                            .setNeutralButton(R.string.dismiss) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                }
                Action.SHARE -> {
                    val string = "Check out this Cocktail app by Kevin. Download from ${Commons.DOWNLOAD_URL}"
                    val i = Intent().apply {
                        type = "text/plain"
                        action = ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, string)
                    }
                    startActivity(Intent.createChooser(i, getString(R.string.share_using)))
                }
                Action.CONTACT -> {
                    var string = "mailto:kevkiprotich@gmail.com" +
                            "&subject=Android%20Development" +
                            "&body=Hello%20Kevin.%20I%20found%20your%20Cocktail%20App."
                    string = Uri.encode(string)
                    val i = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse(string)
                    }
                    startActivity(i)
                }
            }
        }
    }
}
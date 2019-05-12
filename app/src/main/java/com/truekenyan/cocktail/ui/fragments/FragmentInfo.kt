package com.truekenyan.cocktail.ui.fragments

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.createChooser
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.utils.Commons
import kotlinx.android.synthetic.main.fragment_info.*

class FragmentInfo: Fragment() {

    enum class Action{
        ABOUT, SHARE, CONTACT
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews(){
        about_parent.setOnClickListener(clickListener(Action.ABOUT))
        about_title.setOnClickListener(clickListener(Action.ABOUT))
        about_text.setOnClickListener(clickListener(Action.ABOUT))
        contact_parent.setOnClickListener(clickListener(Action.CONTACT))
        contact_title.setOnClickListener(clickListener(Action.CONTACT))
        contact_text.setOnClickListener(clickListener(Action.CONTACT))
        share_parent.setOnClickListener(clickListener(Action.SHARE))
        share_title.setOnClickListener(clickListener(Action.SHARE))
        share_text.setOnClickListener(clickListener(Action.SHARE))
    }

    private fun clickListener(a: Action): View.OnClickListener{
        return View.OnClickListener {
            when (a) {
                Action.ABOUT -> AlertDialog.Builder(it.context).setTitle(R.string.about)
                        .setMessage(R.string.about_body)
                        .setPositiveButton(R.string.dismiss) { dialog, _ -> dialog.dismiss() }
                        .create().show()
                Action.SHARE -> {
                    val i = Intent().apply {
                        type = "text/plain"
                        action = ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Check out this Cocktail app by Kevin. Download from ${Commons.DOWNLOAD_URL}")
                    }
                    startActivity(createChooser(i, getString(R.string.share_using)))
                }
                Action.CONTACT -> {
                    val i = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto: kevkiprotich@gmail.com")
                        putExtra(Intent.EXTRA_SUBJECT, "Cocktail App")
                    }
                    startActivity(createChooser(i, getString(R.string.send_email_using)))
                }
            }
        }
    }
}
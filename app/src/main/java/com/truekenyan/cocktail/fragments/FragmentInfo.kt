package com.truekenyan.cocktail.fragments

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
import android.widget.LinearLayout
import android.widget.TextView
import com.truekenyan.cocktail.R
import com.truekenyan.cocktail.utils.Commons

class FragmentInfo: Fragment() {

    private lateinit var aboutParent: LinearLayout
    private lateinit var aboutTitle: TextView
    private lateinit var aboutText: TextView
    private lateinit var contactParent: LinearLayout
    private lateinit var contactTitle: TextView
    private lateinit var contactText: TextView
    private lateinit var shareParent: LinearLayout
    private lateinit var shareTitle: TextView
    private lateinit var shareText: TextView

    enum class Action{
        ABOUT, SHARE, CONTACT
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_info, container, false)
        initViews(rootView)
        return rootView
    }

    private fun initViews(rootView: View){
        aboutParent = rootView.findViewById(R.id.about_parent)
        aboutTitle = rootView.findViewById(R.id.about_title)
        aboutText = rootView.findViewById(R.id.about_text)
        contactParent = rootView.findViewById(R.id.contact_parent)
        contactTitle = rootView.findViewById(R.id.contact_title)
        contactText = rootView.findViewById(R.id.contact_text)
        shareParent = rootView.findViewById(R.id.share_parent)
        shareTitle = rootView.findViewById(R.id.share_title)
        shareText = rootView.findViewById(R.id.share_text)

        aboutParent.setOnClickListener(clickListener(Action.ABOUT))
        aboutTitle.setOnClickListener(clickListener(Action.ABOUT))
        aboutText.setOnClickListener(clickListener(Action.ABOUT))
        contactParent.setOnClickListener(clickListener(Action.CONTACT))
        contactTitle.setOnClickListener(clickListener(Action.CONTACT))
        contactText.setOnClickListener(clickListener(Action.CONTACT))
        shareParent.setOnClickListener(clickListener(Action.SHARE))
        shareTitle.setOnClickListener(clickListener(Action.SHARE))
        shareText.setOnClickListener(clickListener(Action.SHARE))
    }

    private fun clickListener(a: Action): View.OnClickListener{
        return View.OnClickListener {
            when (a){
                Action.ABOUT -> {
                    AlertDialog.Builder(it.context)
                            .setTitle(R.string.about)
                            .setMessage(R.string.about_body)
                            .setPositiveButton(R.string.dismiss) { dialog, _ ->
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
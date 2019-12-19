package com.mpcreativesoftware.dit.Fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView

import com.mpcreativesoftware.dit.R

class Rules_of_TreadingFragment : Fragment() {

  override fun onCreateView(
          inflater: LayoutInflater, container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    var view= inflater.inflate(R.layout.fragment_rules_of__treading, container, false)

    //  var web = WebView(activity)
    var web =view.findViewById(R.id.webView)as WebView
    web.loadUrl("file:///android_asset/rules_intraday.html")
    return view
  }

}


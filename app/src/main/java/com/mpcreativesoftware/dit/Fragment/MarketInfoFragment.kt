package com.mpcreativesoftware.dit.Fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.mpcreativesoftware.dit.R


class MarketInfoFragment : Fragment() {

  override fun onCreateView(
          inflater: LayoutInflater, container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment

    val view = inflater.inflate(R.layout.fragment_market_info, container, false)
    val web =view.findViewById(R.id.webView)as WebView
    web.loadUrl("file:///android_asset/about.html")
    return view
  }

}

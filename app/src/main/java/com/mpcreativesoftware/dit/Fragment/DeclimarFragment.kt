package com.mpcreativesoftware.dit.Fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.mpcreativesoftware.dit.R


class DeclimarFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        var view= inflater.inflate(R.layout.fragment_declimar, container, false)
        var web =view.findViewById(R.id.webView)as WebView
        web.loadUrl("file:///android_asset/information.html")
        return view
    }
}

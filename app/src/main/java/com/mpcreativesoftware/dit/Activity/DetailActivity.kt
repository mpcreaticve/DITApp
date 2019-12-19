package com.mpcreativesoftware.dit.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.mpcreativesoftware.dit.Model.LiveData
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.mpcreativesoftware.dit.R
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {
    val text = StringBuilder()

    private var mAdView: AdView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        MobileAds.initialize(this, "ca-app-pub-4134787945289593~7228976307")
        mAdView = findViewById(R.id.adViewDetail)
        val adRequest = AdRequest.Builder().build()
        mAdView!!.loadAd(adRequest)



        toolbar.setNavigationOnClickListener {
            finish()
        }

        val model = intent.getSerializableExtra("Array") as LiveData

        tv_buyPrice.text = "₹" + model.buyPrice
        tv_sellPrice.text = "₹" + model.sellRate
        tv_name.text = model.company
        tv_type.text = model.type
        tv_buy.text = model.bs
        tv_sellRangePrice.text = model.sellRange
        val Col = 1

        for (i in 0 until model.price!!.size) {
            var row = TableRow(this)
            var j = 1
            while (j <= Col) {
                val cell = TextView(this)
                val cell1 = TextView(this)
                var target = TextView(this)

                row.weightSum = 3f
                cell.text = "₹" + model.price?.get(i).toString()
                if (model.statusTarget?.get(i).toString().equals(null)) {
                } else {
                    cell1.text = model.statusTarget?.get(i).toString()
                }

                var tar = i + 1
                target.text = tar.toString()


                val params2 = TableRow.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                val params = TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )

                cell.gravity = Gravity.CENTER
                cell1.gravity = Gravity.CENTER
                target.gravity = Gravity.CENTER

                cell.layoutParams = params2
                cell1.layoutParams = params2
                target.layoutParams = params2

                cell.setTextColor(Color.BLACK)
                cell1.setTextColor(Color.BLACK)
                target.setTextColor(Color.BLACK)

                row.layoutParams = params
                row.addView(target)
                row.addView(cell)
                row.addView(cell1)
                j++
            }
            tbl_Rate.addView(row)
        }

        var reader: BufferedReader? = null

        try {
            reader = BufferedReader(
                InputStreamReader(assets.open("detail.txt"))
            )
            // do reading, usually loop until end of file reading
            var mLine: String?
            while (reader.readLine().also { mLine = it } != null) {
                text.append(mLine)
                text.append('\n')
            }
        } catch (e: IOException) {
            Toast.makeText(applicationContext, "Error reading file!", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) { //log the exception
                }
            }

            tv_note.text = text


        }
        val str_share = StringBuilder()
        str_share.append(model.company + "\n" + model.bs + ":" + model.buyPrice + "\n" + "SellRange" + ":" + model.sellRange + "\n" + "StopLoss" + ":" + model.sellRate + "\n" + "Follow this link : " + "https://play.google.com/store/apps/details?id=com.mpcreativesoftware.dit")
        btn_share.setOnClickListener {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.")
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                str_share.toString()
            )
            sendIntent.type = "text/plain"
            startActivity(sendIntent)

            /*       */
        }
    }

}

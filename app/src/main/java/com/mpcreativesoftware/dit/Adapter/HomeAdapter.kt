package com.mpcreativesoftware.dit.Adapter


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mpcreativesoftware.dit.Activity.DetailActivity
import com.mpcreativesoftware.dit.Model.LiveData
import com.mpcreativesoftware.dit.R


class HomeAdapter(
        private val mCtx: Context,
        private var taskList: List<LiveData>
) :
        RecyclerView.Adapter<HomeAdapter.TasksViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(mCtx)
                .inflate(com.mpcreativesoftware.dit.R.layout.recyclerview_home, parent, false)
        return TasksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val t = taskList[position]
        holder.tv_type.text = t.type
        holder.tv_BuyPrice.text = "₹" + t.buyPrice
        holder.tv_comapny.text = t.company
        holder.tv_SellPrice.text = "₹" + t.sellRate
        holder.tv_sellRangePrice.text = t.sellRange
        holder.textViewStatus.text = t.status
        holder.tv_Date.text = t.date
        if (t.bs.equals("BUY")) {
            holder.tv_buy.setTextColor(Color.BLUE)
            holder.tv_buy.text = t.bs
            holder.tv_SellRange.text="Sell Range"

        } else {
            holder.tv_buy.setTextColor(Color.RED)
            holder.tv_buy.text = t.bs
            holder.tv_SellRange.text="Buy Range"
        }
        Log.e("Price", t.price?.size.toString())
        val Row = 3
        val Col = 1

        for (i in 0 until t.price!!.size) {
            var row = TableRow(mCtx)
            var j = 1
            while (j <= Col) {
                val cell = TextView(mCtx)
                val cell1 = TextView(mCtx)
                var target = TextView(mCtx)

                row.weightSum = 3f
                cell.text = "₹" + t.price?.get(i).toString()
                if (t.statusTarget?.get(i).toString().equals(null)) {
                } else {
                    cell1.text = t.statusTarget?.get(i).toString()
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
            holder.table.addView(row)
        }

        holder.itemView.setOnClickListener {

            val intent = Intent(mCtx, DetailActivity::class.java)
            intent.putExtra("Array",t)
            mCtx.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class TasksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textViewStatus: TextView
        var tv_SellPrice: TextView
        var tv_BuyPrice: TextView
        var tv_SellRange: TextView
        var tv_sellRangePrice: TextView

        var tv_Date: TextView
        var tv_type: TextView
        var tv_comapny: TextView
        var tv_nmae: TextView
        var tv_buy: TextView
        var table: TableLayout

        init {
            textViewStatus = itemView.findViewById(R.id.tv_status)
            tv_SellPrice = itemView.findViewById(R.id.tv_sellPrice)
            tv_BuyPrice = itemView.findViewById(R.id.tv_buyPrice)
            tv_sellRangePrice = itemView.findViewById(R.id.tv_sellRangePrice)
            tv_SellRange = itemView.findViewById(R.id.tv_sellRange)

            tv_nmae = itemView.findViewById(R.id.tv_type)
            tv_comapny = itemView.findViewById(R.id.tv_name)
            tv_type = itemView.findViewById(R.id.tv_type)
            table = itemView.findViewById(R.id.tbl_Rate)
            tv_Date = itemView.findViewById(R.id.tv_date)
            tv_buy = itemView.findViewById(R.id.tv_buy)

        }

    }

}
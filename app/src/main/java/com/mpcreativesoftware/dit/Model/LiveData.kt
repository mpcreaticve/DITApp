package com.mpcreativesoftware.dit.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LiveData : Serializable{
     var id: String = ""
     var company:String=""
     var status: String = ""
     var sellRate: String = ""
     var sellRange: String = ""
     var type: String = ""
     var buyPrice: String = ""
     var date: String = ""
     var bs: String = ""
     var number: List<Int>? = null
     var price: List<String>? = null
     var statusTarget: List<String>? = null

     constructor()
     constructor(
         id: String,
         status: String,
         sellRate: String,
         sellRange: String,
         type: String,
         number: List<Int>?,
         price: List<String>?,
         statusTarget: List<String>?
     ) {
         this.id=id
         this.status = status
         this.sellRate = sellRate
         this.sellRange = sellRange
         this.type = type
         this.number = number
         this.price = price
         this.statusTarget = statusTarget
     }


 }



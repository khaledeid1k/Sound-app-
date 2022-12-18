package com.example.soundapp.DataBase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
 class QuaranFiles(var Path: String?=null,
                  var title:String?=null,
                  var Duration :String?=null,
                  var artist :String ?=null,
                  var albums :String ?=null,
                  var albums_id :String ?=null,
                  var albums_ALBUM :String ?=null,
                  var id :Int
                 ) : Parcelable {



}
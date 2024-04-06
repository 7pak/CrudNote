package io.abood.firebase


  data class Note(
    var id: String?, var title: String?,
    var note: String?, var time:String?
  ){
    constructor() : this(null, null, null,null)
  }

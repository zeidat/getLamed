package com.iconnect.dal.models.submodels

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class GreetingDAO():RealmObject() {
    @PrimaryKey
    var greetingId:Long? = null
    var greetingDetails:String? = null

    constructor(greetingId: Long?, greetingDetails: String?) : this() {
        this.greetingId = greetingId
        this.greetingDetails = greetingDetails
    }
}
package com.iconnect.dal.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class AppointmentDAO() : RealmObject() {

    @PrimaryKey
    var id: Long = 0
    var appointmentdate: Date? = null
    var beginingTime: String? = null
    var endingTime: String? = null
    var lessons: String? = null
    var tests: String? = null
    var trialTests: String? = null
    var greetings: String? = null

    constructor(id: Long,
                appointmentdate: Date?,
                beginingTime: String?,
                endingTime: String?,
                lessons: String?,
                tests: String?,
                trialTests: String?,
                greetings: String?) : this() {

        this.id = id
        this.appointmentdate = appointmentdate
        this.beginingTime = beginingTime
        this.endingTime = endingTime
        this.lessons = lessons
        this.tests = tests
        this.trialTests = trialTests
        this.greetings = greetings
    }

    //i can get the lesson and tests by id insted of saving it in DB

}
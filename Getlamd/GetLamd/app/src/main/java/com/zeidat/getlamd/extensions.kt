package com.zeidat.getlamd

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import com.getlamd.entity.buisness.Appointment
import com.getlamd.entity.buisness.Lesson
import com.getlamd.entity.management.Phone
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import java.util.*
import org.joda.time.LocalTime
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


fun <T> Single<T>.androidSingle(): Single<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(Schedulers.io())
}

fun <T> Observable<T>.androidObservable(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(Schedulers.io())
}


fun <T> Flowable<T>.androidFlowable(): Flowable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(Schedulers.io())
}

fun Date.getDateString(): String {
    var pattern = "dd MMM"
    var formatter = SimpleDateFormat(pattern)
    return formatter.format(this)
}

fun LocalTime.getTimeString(): String {
    return this.toString("H:mm a")
}

fun Set<Phone>.applyAllPhones(): String {
    var phones: String? = null
    if (this == null)
        return "0595224244"
    this.forEach {
        phones =
                  "/ ".plus(it)
    }
    return phones!!
}

fun List<Appointment>.sortAppointmentsByDate(): List<Appointment> {
    var nah = this.sortedByDescending {
        it.appointmentdate
    }

    var list = ArrayList<Appointment>()
    for (obj in nah) {
        list.add(obj)
    }

    return list
}

fun Any.provideDefultValueIfNull(): String {
    if (this == null) {
        return ""
    }
    return this.toString()
}

fun Context.runDialog(message: String, title: String): AlertDialog.Builder {
    var dialog = AlertDialog.Builder(this)
    dialog.setTitle(title)
    dialog.setMessage(message)
    dialog.show()
    return dialog
}


fun List<Appointment>.displayAppointmentByDate(dateTime: DateTime): List<Appointment> {
    var appointments = ArrayList<Appointment>()
    var dateTimeString = dateTime.getDateStringDatePicker()
    this.forEach { appointment ->
        if (appointment.appointmentdate.getDateString() == dateTimeString)
            appointments.add(appointment)
    }
    return appointments

}

private fun DateTime.getDateStringDatePicker(): String {
    return this.toString("dd MMM")
}



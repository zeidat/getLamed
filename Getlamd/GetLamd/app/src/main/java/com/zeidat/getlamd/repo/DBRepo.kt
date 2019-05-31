package com.zeidat.getlamd.repo

import com.getlamd.entity.buisness.Appointment
import com.getlamd.entity.buisness.Test
import com.iconnect.dal.DBManger
import com.iconnect.dal.models.AppointmentDAO
import com.iconnect.dal.models.submodels.GreetingDAO
import com.iconnect.dal.models.submodels.LessonDAO
import com.iconnect.dal.models.submodels.TestDAO
import com.iconnect.dal.models.submodels.TrialTestDAO
import com.zeidat.getlamd.provideDefultValueIfNull
import io.reactivex.Single

object DBRepo {

    fun insertAppointmentsDB(appointments: List<Appointment>):List<Appointment> {
        var appointmentsDAO = appointments.map {
            var lessons = it.lessons.orEmpty().toString()
            var tests = it.tests.orEmpty().toString()
            var trialTests = it.trialTests.orEmpty().toString()
            var greetings = it.greetings.orEmpty().toString()
            AppointmentDAO(it.id,
                      it.appointmentdate,
                      it.beginingTime.toString(),
                      it.endingTime.toString(),
                      lessons,
                      tests,
                      trialTests ,
                      greetings)
        }

        DBManger.insert(appointmentsDAO ,AppointmentDAO::class.java)
        return appointments
    }

    fun getAllAppointmentsDB():Single<List<AppointmentDAO>>{
        return Single.create<List<AppointmentDAO>>{
            var result = DBManger.getAll(AppointmentDAO::class.java) as List<AppointmentDAO>
            it.onSuccess(result)

        }

    }






}
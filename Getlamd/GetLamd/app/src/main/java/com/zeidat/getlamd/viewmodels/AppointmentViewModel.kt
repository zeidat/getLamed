package com.zeidat.getlamd.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.getlamd.entity.buisness.Appointment

class AppointmentViewModel:ViewModel(){

    private var appointmentList = MutableLiveData<List<Appointment>>()
    private var selectedAppointment = MutableLiveData<Appointment>()

    fun saveAppointments(appointments:List<Appointment>){
        this.appointmentList.value = appointments
    }

    fun provideAppointments():MutableLiveData<List<Appointment>>{
        return this.appointmentList
    }

    fun addSelectedAppointment(appointment: Appointment){
        this.selectedAppointment.value = appointment

    }

    fun provideSelectedAppointment():MutableLiveData<Appointment>{
        return this.selectedAppointment
    }
}

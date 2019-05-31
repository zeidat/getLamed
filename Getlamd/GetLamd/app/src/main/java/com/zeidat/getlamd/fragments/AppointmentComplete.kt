package com.zeidat.getlamd.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import com.getlamd.entity.buisness.Appointment
import com.getlamd.entity.buisness.AppointmentStatus

import com.zeidat.getlamd.R
import com.zeidat.getlamd.activites.MainActivity
import com.zeidat.getlamd.repo.SharingViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AppointmentComplete : Fragment() {
    private val compositeDisposable = CompositeDisposable()
    private var sharedViewModel:SharingViewModel? = null
    private var appointmentStatus:TextView? =null
    private var appointmentSelect:Spinner? = null
    private var next:Button? = null
    private var selectedAppointment:Appointment?  = null
    private var flag = 0

    @Inject lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).getApplicationCustomComponent().inject(this)
        this.sharedViewModel  =ViewModelProviders.of(activity as MainActivity).get(SharingViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_appointment_complete, container, false)
        this.appointmentStatus = view.findViewById(R.id.AppointmentCompleteStatus)
        this.appointmentSelect = view.findViewById(R.id.AppointmentCompleteSelect)
        this.next = view.findViewById(R.id.AppointmentCompleteNext)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.appointmentSelect!!.adapter = ArrayAdapter<AppointmentStatus>(activity as MainActivity , android.R.layout.simple_spinner_item, AppointmentStatus.values())
        this.sharedViewModel!!.getSelectedAppointment().observe(this, Observer { appointment->
            appointment?.let {
                this.selectedAppointment = it
            }
        } )

        this.next!!.setOnClickListener {
            if(this.flag == 0){
                this.selectedAppointment!!.status = this.appointmentSelect!!.selectedItem as AppointmentStatus
                if(this.selectedAppointment!!.lessons.size>0){
                    flag=1
                }
            }
            if(flag == 1){
                var lessons = this.selectedAppointment!!.lessons.toList().forEach {
                    this.appointmentStatus!!.text  = "Lessson, ".plus(it.student.getLamdUser.arabicName)
                }
            }

        }
    }

    override fun onDestroy() {
        this.compositeDisposable.dispose()
        super.onDestroy()
    }

}
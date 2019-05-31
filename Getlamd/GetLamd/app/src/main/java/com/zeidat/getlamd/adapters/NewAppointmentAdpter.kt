package com.zeidat.getlamd.adapters

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.zeidat.getlamd.R
import com.getlamd.entity.buisness.Appointment
import com.makeramen.roundedimageview.RoundedImageView
import com.zeidat.getlamd.activites.MainActivity
import com.zeidat.getlamd.androidObservable
import com.zeidat.getlamd.getDateString
import com.zeidat.getlamd.getTimeString
import com.zeidat.getlamd.repo.SharingViewModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.*
import kotlin.collections.ArrayList

class NewAppointmentAdpter(private val mContext: Context, private var appointments: List<Appointment>) : RecyclerView.Adapter<ViewHolder>() {

    var subjectAppointment  = BehaviorSubject.create<Appointment>()
    var clickEvent: Observable<Appointment> = subjectAppointment
    private var sharingViewModel = ViewModelProviders.of(mContext as MainActivity).get(SharingViewModel::class.java)


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.custom_appointment_cell, p0, false))
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.type.text = ""
        var appointment = appointments[position]
        var nowDate= Date().getDateString()
        if(nowDate == appointment.appointmentdate.getDateString()){
            holder.color.setBackgroundColor((mContext).getColor(R.color.red))
        }else {
            holder.color.setBackgroundColor((mContext).getColor(R.color.cellColor))
        }

        if(appointment.lessons!= null && appointment.lessons.size > 0)
            holder.type.append("Lesson, ")

        if(appointment.tests!= null&&appointment.tests.size>0)
            holder.type.append("Test, ")

        if(appointment.greetings!= null&&appointment.greetings.size>0)
            holder.type.append("Greeting, ")

        if(appointment.trialTests!= null&&appointment.trialTests.size>0)
            holder.type.append("Trial-Test, ")




        holder.date.text = appointment.appointmentdate.getDateString()
        holder.time.text = appointment.beginingTime.getTimeString().plus(" - ").plus(appointment.endingTime.getTimeString())

        holder.layout.setOnClickListener{
            sharingViewModel.selectAppointment(appointment)
            clickEvent  = subjectAppointment
            subjectAppointment.onNext(appointment)
        }
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var date = view.findViewById<TextView>(R.id.appointmentCellDate)
    var time = view.findViewById<TextView>(R.id.appointmentCellTime)
    var type = view.findViewById<TextView>(R.id.appointmentCellType)
    var color = view.findViewById<ImageView>(R.id.appointmentCellColor)
    var layout = view.findViewById<RoundedImageView>(R.id.imageView2)

}
package com.zeidat.getlamd.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.getlamd.entity.buisness.Appointment

import com.getlamd.entity.user.Student
import com.zeidat.getlamd.*
import java.util.*
import kotlin.collections.ArrayList

class HomeAdapter(private val mContext: Context, appointments: ArrayList<Appointment>) : ArrayAdapter<Appointment>(mContext, 0, appointments) {
    //private var title: TextView? = null
    private var date: TextView? = null
    private var time:TextView? = null
    private var type:TextView? = null
    private var color:ImageView? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView

        if (v == null) {
            val vi = LayoutInflater.from(mContext)
            v = vi.inflate(R.layout.custom_appointment_cell, null)
        }

        val appointment = getItem(position)!!
        date = v!!.findViewById<TextView>(R.id.appointmentCellDate)
        time = v.findViewById<TextView>(R.id.appointmentCellTime)
        type = v.findViewById<TextView>(R.id.appointmentCellType)
        color = v.findViewById<ImageView>(R.id.appointmentCellColor)
        if(appointment.lessons!= null && appointment.lessons.size > 0)
            this.type!!.append("Lesson ,")
        if(appointment.tests!= null&&appointment.tests.size>0)
            this.type!!.append("Test ,")
        if(appointment.greetings!= null&&appointment.greetings.size>0)
            this.type!!.append("Greeting ,")
        if(appointment.trialTests!= null&&appointment.trialTests.size>0)
            this.type!!.append("Trial Test ,")



        //title!!.text = "Title"
        var nowDate= Date().getDateString()
        print(nowDate)
        var appointmetDate = appointment.appointmentdate.getDateString()
        if(nowDate == appointment.appointmentdate.getDateString()){
            this.color!!.setBackgroundColor(context.getColor(R.color.red))
        }
        this.date!!.text = appointment.appointmentdate.getDateString()
        this.time!!.text = appointment.beginingTime.getTimeString().plus(" - ").plus(appointment.endingTime.getTimeString())

        return v

    }

    override fun getPosition(item: Appointment?): Int {

        return super.getPosition(item)
    }
}

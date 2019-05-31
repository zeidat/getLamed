package com.zeidat.getlamd.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.getlamd.entity.buisness.AppointmentStatus
import com.getlamd.entity.management.Vehicle
import com.getlamd.entity.user.Student

class StudentSpinnerAdapter(private val mContext:Context ,private val resource:Int, private val students:ArrayList<Student>):ArrayAdapter<Student>(mContext ,resource ,students) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var textView = super.getView(position, convertView, parent) as TextView
        textView.text =students[position].getLamdUser.arabicName

        return textView

    }

    override fun getDropDownView(position: Int, convertView: View?,
                                 parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.text = students[position].getLamdUser.arabicName
        return label
    }
}

class VehicleSpinnerAdapter(private val mContext:Context ,private val resource:Int, private val vehicles:ArrayList<Vehicle>):ArrayAdapter<Vehicle>(mContext ,resource ,vehicles) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var textView = super.getView(position, convertView, parent) as TextView
        textView.text =vehicles[position].make

        return textView

    }

    override fun getDropDownView(position: Int, convertView: View?,
                                 parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.text = vehicles[position].make
        return label
    }

}
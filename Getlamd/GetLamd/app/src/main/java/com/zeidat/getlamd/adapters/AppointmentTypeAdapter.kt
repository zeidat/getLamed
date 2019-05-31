package com.zeidat.getlamd.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.makeramen.roundedimageview.RoundedImageView
import com.zeidat.getlamd.R
import com.zeidat.getlamd.models.AppointmentType
import org.w3c.dom.Text

class AppointmentTypeAdapter(private val mContext:Context ,var appointmentType:ArrayList<AppointmentType>):RecyclerView.Adapter<AppointmentTypeViewHolder>() {
    override fun onCreateViewHolder(group: ViewGroup, position: Int): AppointmentTypeViewHolder {
       return AppointmentTypeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.appointment_type_layout ,group ,false))
    }

    override fun getItemCount(): Int {
        return appointmentType.size
    }

    override fun onBindViewHolder(holder: AppointmentTypeViewHolder, posistion: Int) {
        var item = appointmentType[posistion]
        holder.typeName.text = item.type
        holder.student.text = item.student!!.getLamdUser.arabicName
        holder.delete.setOnClickListener {
            appointmentType.remove(item)
            this.notifyDataSetChanged()
        }
    }

    fun addItem(appointmentTypeItem: AppointmentType){
        appointmentType.add(appointmentTypeItem)
        this.notifyDataSetChanged()
    }

    fun getAllItems():ArrayList<AppointmentType>{
        return appointmentType
    }
}


class AppointmentTypeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var typeName  = view.findViewById<TextView>(R.id.appointmentType)
    var student  = view.findViewById<TextView>(R.id.studentName)
    var delete = view.findViewById<ImageButton>(R.id.appointmetTypeDeleteButton)

}


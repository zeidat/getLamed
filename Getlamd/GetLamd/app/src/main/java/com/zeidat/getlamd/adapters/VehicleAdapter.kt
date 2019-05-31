package com.zeidat.getlamd.adapters

import android.content.Context
import android.content.SharedPreferences
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.getlamd.entity.buisness.StudentCardItem
import com.getlamd.entity.management.Vehicle

import com.getlamd.entity.user.Student
import com.zeidat.getlamd.*
import com.zeidat.getlamd.activites.MainActivity
import com.zeidat.getlamd.fragments.StudentCard
import io.reactivex.Single
import kotlin.collections.ArrayList

class VehicleAdapter(private val mContext: Context, vehicles: ArrayList<Vehicle>) : ArrayAdapter<Vehicle>(mContext, 0, vehicles) {
    private var type: TextView? = null
    private var number: TextView? = null
    private var details: TextView? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView

        if (v == null) {
            val vi = LayoutInflater.from(mContext)
            v = vi.inflate(R.layout.custom_vehicle_cell, null)
        }
        var veh = getItem(position)
        this.type = v!!.findViewById(R.id.VCellType)
        this.number = v.findViewById(R.id.VCellNumber)
        this.details = v.findViewById(R.id.VCellDetails)

        this.type!!.text = veh.make
        this.number!!.text = veh.model
        this.details!!.text  =veh.color

        return v

    }
}

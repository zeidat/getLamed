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

import com.getlamd.entity.user.Student
import com.zeidat.getlamd.*
import com.zeidat.getlamd.activites.MainActivity
import com.zeidat.getlamd.fragments.StudentCard
import io.reactivex.Single
import kotlin.collections.ArrayList

class StudentListAdapter(private val mContext: Context, students: ArrayList<Student>) : ArrayAdapter<Student>(mContext, 0, students) {
    private var name: TextView? = null
    private var phone: TextView? = null
    private var email: TextView? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView

        if (v == null) {
            val vi = LayoutInflater.from(mContext)
            v = vi.inflate(R.layout.students_list_items, null)
        }

        val user = getItem(position)!!.getLamdUser
        this.name = v!!.findViewById(R.id.studentCellName)
        this.phone = v.findViewById(R.id.studentCellPhone)
        this.email = v.findViewById(R.id.studentCellEmail)


        this.name!!.text = user.arabicName
        //this.phone!!.text = user.phones.applyAllPhones()
        this.email!!.text = user.email

        return v

    }
}

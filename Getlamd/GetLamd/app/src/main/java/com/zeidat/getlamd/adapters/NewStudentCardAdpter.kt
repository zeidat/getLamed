package com.zeidat.getlamd.adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.getlamd.entity.buisness.Appointment
import com.getlamd.entity.buisness.StudentCardItem
import com.zeidat.getlamd.R
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class NewStudentCardAdpter(private val mContext: Context, private var items: List<StudentCardItem>) : RecyclerView.Adapter<ViewHolderCard>() {
    var subjectCard = BehaviorSubject.create<Int>()
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolderCard {
        return ViewHolderCard(LayoutInflater.from(mContext).inflate(R.layout.student_card_listitems, p0, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolderCard, position: Int) {
        var item = items[position]
        holder.checkBox.isChecked = item.isCompleted
        holder.content.text = item.actionArabicName
        holder.checkBox.setOnClickListener{
            subjectCard.onNext(position)
            items[position].isCompleted = !item.isCompleted
        }
        holder.layout.setOnClickListener {
            subjectCard.onNext(position)
        }

    }

     fun getAllItems():ArrayList<StudentCardItem>{
        return items as ArrayList
    }
}

class ViewHolderCard(view: View) : RecyclerView.ViewHolder(view) {
    var checkBox = view.findViewById<CheckBox>(R.id.cardItemCheckBox)
    var content = view.findViewById<TextView>(R.id.cardItemContent)
    var layout  = view.findViewById<ConstraintLayout>(R.id.cardItemLayout)
}

package com.zeidat.getlamd.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import com.getlamd.entity.buisness.StudentCard
import com.getlamd.entity.buisness.StudentCardItem
import com.zeidat.getlamd.BaseActivity
import com.zeidat.getlamd.R
import com.zeidat.getlamd.activites.MainActivity
import com.zeidat.getlamd.adapters.NewStudentCardAdpter
import com.zeidat.getlamd.repo.ServiceRepo
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class StudentCard : Fragment() {
    private var compositeDisposable: CompositeDisposable? = null

    private var listOfItems: RecyclerView? = null
    private var studentCard: ArrayList<StudentCardItem> = ArrayList()
    private var putStudentCard: StudentCard? = null
    private var fab:FloatingActionButton? = null
    //private var studentCardAdapter: StudentCardAdapter? = null
    private var newCardViewAdapter:NewStudentCardAdpter? = null
    private var bar:ProgressBar? =null


    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var studentsFragment: StudentsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        this.compositeDisposable = CompositeDisposable()
        this.putStudentCard = StudentCard()
        (activity as BaseActivity).getApplicationCustomComponent().inject(this)
       // this.studentCardAdapter = StudentCardAdapter(activity as BaseActivity, studentCard)
        this.newCardViewAdapter = NewStudentCardAdpter(activity as MainActivity , ArrayList())

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_student_card, container, false)
        this.fab = (activity as BaseActivity).findViewById(R.id.fab)
        this.listOfItems = view.findViewById(R.id.studentCardListOfItems)
        this.bar = view.findViewById(R.id.cardProgressbar)
        this.bar!!.visibility = View.INVISIBLE
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.fab!!.hide()
        var studentID = this.sharedPreferences.getLong("studentId", -1)
        var token = sharedPreferences.getString("token", "no")
        if (studentID != -1L) {
            this.compositeDisposable!!.add(ServiceRepo.getStudentCard(studentID, token!!).subscribe({
                this.studentCard = it.items
                this.newCardViewAdapter = NewStudentCardAdpter(activity as MainActivity ,it.items)
                this.listOfItems!!.setHasFixedSize(true)
                this.listOfItems!!.layoutManager = LinearLayoutManager(activity as MainActivity)
                this.listOfItems!!.adapter  =this.newCardViewAdapter
                this.putStudentCard = it
                //handleClick()
            }, {
                print(it.localizedMessage)
            }))

        }


        this.newCardViewAdapter!!.subjectCard.subscribe{ position->

        }


//        this.listOfItems!!.setOnItemClickListener { _, view, position, _ ->
//            var isCompleted = view.findViewById<TextView>(R.id.studentCardCheckBox)
//            var item = this.studentCardAdapter!!.getItem(position)
//            if (item!!.isCompleted) {
//                isCompleted.text = "Not CompletedZ"
//                this.studentCard[position].isCompleted = false
//            } else {
//                isCompleted.text = "CompletedZ"
//                this.studentCard[position].isCompleted = true
//            }
//            this.studentCardAdapter!!.clear()
//            this.studentCardAdapter!!.addAll(this.studentCard)
//            this.studentCardAdapter!!.notifyDataSetChanged()
//            this.listOfItems!!.adapter = this.studentCardAdapter
//        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.submit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        this.studentCard = this.newCardViewAdapter!!.getAllItems()
        this.bar!!.visibility = View.VISIBLE
        this.putStudentCard!!.items = this.studentCard
        var token = this.sharedPreferences.getString("token", "no")
        var studentID = this.sharedPreferences.getLong("studentId", -1)
        this.compositeDisposable!!.add(ServiceRepo.putStudentCard(studentID ,token!!,this.putStudentCard!!).subscribe({
            print(it.string())
            this.bar!!.visibility = View.INVISIBLE
            (activity as BaseActivity).supportFragmentManager
                      .beginTransaction()
                      .replace(R.id.fragmentContainer,this.studentsFragment)
                      .commit()
        },{
            print(it)
            this.bar!!.visibility = View.INVISIBLE
        }))
        return true
    }

    override fun onDestroy() {
        this.compositeDisposable!!.dispose()
        this.fab!!.show()
        super.onDestroy()

    }

//    private fun handleClick(){
//        var subject = this.newCardViewAdapter!!.subjectCard.subscribe({position->
//            this.studentCard[position].isCompleted  = !this.studentCard[position].isCompleted
//            //this.newCardViewAdapter = NewStudentCardAdpter(activity as MainActivity , this.studentCard)
//            this.newCardViewAdapter!!.notifyDataSetChanged()
//            this.listOfItems!!.setHasFixedSize(true)
//            this.listOfItems!!.layoutManager = LinearLayoutManager(activity as MainActivity)
//            this.listOfItems!!.adapter  =this.newCardViewAdapter
//            //handleClick()
//
//        },{
//            print(it)
//        })
//    }

}
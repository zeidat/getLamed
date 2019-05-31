package com.zeidat.getlamd.fragments

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.getlamd.entity.user.Student

import com.zeidat.getlamd.R
import com.zeidat.getlamd.activites.MainActivity
import com.zeidat.getlamd.adapters.StudentListAdapter
import com.zeidat.getlamd.repo.ServiceRepo
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class StudentsFragment : Fragment() {

    private var fab: FloatingActionButton? = null
    @Inject
    lateinit var addStudentFragment: AddStudent
    private var studentListAdapter: StudentListAdapter? = null
    private var studentsList: ArrayList<Student>? = null
    private var compositeDisposable: CompositeDisposable? = null
    private var listView: ListView? = null

    @Inject
    lateinit var sharedPreferences:SharedPreferences
    @Inject
    lateinit var studentCard: StudentCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).getApplicationCustomComponent().inject(this)
        this.addStudentFragment = AddStudent()
        this.studentsList = ArrayList()
        this.studentListAdapter = StudentListAdapter((activity as MainActivity), studentsList!!)
        this.compositeDisposable = CompositeDisposable()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab!!.setOnClickListener {
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, addStudentFragment!!).commit()
        }
        listView!!.adapter = this.studentListAdapter

        getAllStudents(sharedPreferences!!.getString("token", "no"))

        this.listView!!.setOnItemClickListener{_ ,_ ,postion,_ ->
            var student = this.listView!!.adapter.getItem(postion) as Student
            this.sharedPreferences.edit().putLong("studentId",student.id).commit()
            (activity as MainActivity).supportFragmentManager
                      .beginTransaction()
                      .replace(R.id.fragmentContainer ,this.studentCard )
                      .addToBackStack(null)
                      .commit()
        }
    }

    override fun onDestroy() {
        compositeDisposable!!.dispose()
        super.onDestroy()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_students, container, false)
        listView = view.findViewById(R.id.StudentsFragmentListOfStudents)
        this.fab = activity!!.findViewById(R.id.fab)
        return view
    }


    private fun getAllStudents(token: String) {
        this.compositeDisposable!!.add(ServiceRepo.getAllStudents(token).subscribe({ students ->
            if (students.isNotEmpty()) {
                this.studentListAdapter!!.clear()
                this.studentListAdapter!!.addAll(students)
                this.studentListAdapter!!.notifyDataSetChanged()
                this.listView!!.adapter = this.studentListAdapter
            }
        }, {
            print(it.cause)
        }))
    }


}

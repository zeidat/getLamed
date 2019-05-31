package com.zeidat.getlamd.activites

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.TabItem
import android.support.design.widget.TabLayout
import android.widget.TextView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.zeidat.getlamd.BaseActivity
import android.view.Menu
import android.widget.LinearLayout
import android.widget.ListView
import com.getlamd.entity.buisness.*
import com.getlamd.entity.user.Student
import com.zeidat.getlamd.repo.ServiceRepo
import com.zeidat.getlamd.R
import com.zeidat.getlamd.adapters.StudentListAdapter
import io.reactivex.disposables.CompositeDisposable
import org.joda.time.LocalTime
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


class NewAppointment : BaseActivity() {

    private var lesson: TabItem? = null
    private var greeting: TabItem? = null
    private var test: TabItem? = null
    private var trialTest: TabItem? = null
    private var toolbar: Toolbar? = null
    private var description: TextView? = null
    private var startTime: TextView? = null
    private var endTime: TextView? = null
    private var tabLayout:TabLayout? = null
    private var postion:Int = 0
    private var viewContainer:LinearLayout? = null
    private var tabToSelect:TextView? = null
    private var dialog:Dialog? = null
    private var studentListView:ListView? = null

    private var compositeDisposable:CompositeDisposable? = null
    private var listofStudents:ArrayList<Student>? = null
    private var studentListAdapter:StudentListAdapter? = null
    //private var selectedStudent:Student? = null



     var sharedPreferences: SharedPreferences? = null ;

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.NewAppointmentTheme)
        super.onCreate(savedInstanceState)



        title = "New Appointment"
        this.compositeDisposable = CompositeDisposable()
        this.listofStudents = ArrayList()
        this.studentListAdapter = StudentListAdapter(this,listofStudents!!)
        sharedPreferences = provideSharedPreferences() ;
        setContentView(R.layout.activity_new_appointment)
        toolbar = findViewById(R.id.newAppointmentToolbar)
        setSupportActionBar(toolbar)


        /**
         * initialize Components
         */

        lesson = findViewById(R.id.tabLesson)
        greeting = findViewById(R.id.tabGreeting)
        test = findViewById(R.id.tabTest)
        trialTest = findViewById(R.id.tabTrialTest)
        description = findViewById(R.id.newAppointmentDescription)
        startTime = findViewById(R.id.newAppointmentStartTime)
        endTime = findViewById(R.id.newAppointmentEndTime)
        tabLayout = findViewById(R.id.newAppointmentTabLayout)
        viewContainer = findViewById(R.id.appointmentTypeContainer)

        var view = layoutInflater.inflate(R.layout.lesson_appointment,null)
        tabToSelect = view.findViewById(R.id.lessonLayoutStudentText)

        viewContainer!!.addView(view)
        this.dialog = Dialog(this)
        dialog!!.setContentView(R.layout.custom_dialog)
        dialog!!.setTitle("Select Student")
        this.studentListView = dialog!!.findViewById<ListView>(R.id.customDialogListView)

        /**
         * tab to select student
         */

        tabToSelect!!.setOnClickListener {
            getAllStudent(this.dialog!!,this.studentListView!!)
        }

//        this.studentListView!!.setOnItemClickListener{arg0,arg1,postion ,arg2 ->
//            this.selectedStudent = this.studentListAdapter!!.getItem(postion)
//            this.tabToSelect!!.text = this.selectedStudent!!.getLamdUser.arabicName
//        }

        /**
         * tabLayout handle selected item postion
         */
       tabLayout!!.setOnClickListener { tab->
           print("anthisg")

       }

       tabLayout!!.addOnTabSelectedListener( object:TabLayout.OnTabSelectedListener {
           override fun onTabReselected(p0: TabLayout.Tab?) {
               print("AnyThing")
           }

           override fun onTabUnselected(p0: TabLayout.Tab?) {
               print("AnyThing")
           }

           override fun onTabSelected(p0: TabLayout.Tab?) {
               /**
                * include the custom layout for the appointment type
                */
//               when(p0!!.position){
//                   0->{
//                       var view = layoutInflater.inflate(R.layout.lesson_appointment,null)
//                       tabToSelect = view.findViewById(R.id.lessonLayoutStudentText)
//                       viewContainer!!.removeAllViews()
//                       viewContainer!!.addView(view)
//                   }
//                   1->{
//                       var view = layoutInflater.inflate(R.layout.greeting_appointment,null)
//                       tabToSelect = view.findViewById(R.id.greetingSelectStudent)
//                       viewContainer!!.removeAllViews()
//                       viewContainer!!.addView(view)
//                   }
//                   2->{
//                       var view = layoutInflater.inflate(R.layout.test_appointment,null)
//                       tabToSelect = view.findViewById(R.id.testSelectStudent)
//                       viewContainer!!.removeAllViews()
//                       viewContainer!!.addView(view)
//                   }
//                   3->{
//                       var view = layoutInflater.inflate(R.layout.trialtest_appointment,null)
//                       tabToSelect = view.findViewById(R.id.trialSelectStudent)
//                       viewContainer!!.removeAllViews()
//                       viewContainer!!.addView(view)
//                   }
//
//               }
           }


       }

       )


        startTime!!.setOnClickListener{
            var calender = Calendar.getInstance()
            var hours = calender.get(Calendar.HOUR)
            var min = calender.get(Calendar.MINUTE)
            var timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{ datepicker, selectedHours, selectedMin ->
               // datepicker.hour = Calendar.HOUR
                //datepicker.minute = Calendar.MINUTE
                startTime!!.text = "".plus(selectedHours).plus(":").plus(selectedMin).plus("")
            }, hours, min, false)
            timePicker.setTitle("Select Time")
            timePicker.show()

        }
//
        endTime!!.setOnClickListener{
            var calender = Calendar.getInstance()
            var hours = calender.get(Calendar.HOUR_OF_DAY)
            var min = calender.get(Calendar.MINUTE)
            var timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{ datePicker, selectedHours, selectedMin ->
                datePicker.hour = (Calendar.HOUR.plus(1) )
                datePicker.minute = Calendar.MINUTE .plus(1)
                endTime!!.text = "".plus(selectedHours).plus(":").plus(selectedMin).plus("")
            }, hours, min, false)
            timePicker.setTitle("Select Time")
            timePicker.show()

        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.submit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if (id == R.id.action_submit) {
            var appointment = Appointment()
            var dateTimeString = dateSahredPreferences.getString("selectedDate","no")
            var format = SimpleDateFormat("yyyy-mm-dd")
            var dateTime = format.parse(dateTimeString)
            postion =  tabLayout!!.selectedTabPosition

            appointment.appointmentdate = dateTime
            appointment.beginingTime = LocalTime(this.startTime!!.text.toString())
            appointment.endingTime = LocalTime(this.endTime!!.text.toString())

            when(postion)
            {
                0 -> {
                    var appointmentLesson = Lesson()
                   // lesson.
                   // if(this.selectedStudent != null)
                       // appointmentLesson.student = this.selectedStudent


                }
                1 -> {
                    var appointmentGreeting = Greeting()
//                    if(this.selectedStudent != null){
//
//                    }

                }
                2 -> {
                    var appointmentTest = Test()
//                    if(this.selectedStudent != null)
                        //appointmentTest.student = this.selectedStudent


                }
                3 -> {
                    var appintmentTrialTest = TrialTest()
//                    if(this.selectedStudent != null)
                        //appintmentTrialTest.student = this.selectedStudent

                }

                else ->{
                    print("Error")
                }
            }


            ServiceRepo.postAppointment(appointment, sharedPreferences!!.getString("token","no")).subscribe({ response->
                print(response)
                finish()
            },{
                print(it.localizedMessage)
            })



            //finish this activity

        }

        return true


    }

    override fun onDestroy() {
        super.onDestroy()
        /**
         * clear all network requests
         */
        this.compositeDisposable!!.dispose()
    }

   private fun getAllStudent(dialog: Dialog,listView:ListView){
        this.compositeDisposable!!.add(ServiceRepo.getAllStudents(this.sharedPreferences!!.getString("token","no")).subscribe({
            this.studentListAdapter = StudentListAdapter(this,it)
            listView.adapter = this.studentListAdapter
            dialog.show()
        },{
            print(it.localizedMessage)

        }))
    }

}


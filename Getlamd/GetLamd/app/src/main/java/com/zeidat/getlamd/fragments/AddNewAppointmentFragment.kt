package com.zeidat.getlamd.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import com.getlamd.entity.buisness.*
import com.getlamd.entity.management.Vehicle
import com.getlamd.entity.user.Student
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu

import com.zeidat.getlamd.R
import com.zeidat.getlamd.activites.MainActivity
import com.zeidat.getlamd.adapters.AppointmentTypeAdapter
import com.zeidat.getlamd.adapters.StudentListAdapter
import com.zeidat.getlamd.adapters.StudentSpinnerAdapter
import com.zeidat.getlamd.adapters.VehicleAdapter
import com.zeidat.getlamd.models.AppointmentType
import com.zeidat.getlamd.repo.ServiceRepo
import com.zeidat.getlamd.viewmodels.NewAppointmentViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.lesson_appointment.*
import org.joda.time.LocalTime
import org.w3c.dom.Text
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AddNewAppointmentFragment : Fragment() {
    private var compositeDisposable: CompositeDisposable? = null
    private var fab: android.support.design.widget.FloatingActionButton? = null
    private var beginTime: TextView? = null
    private var endTime: TextView? = null
    private var vehicleAdapter: VehicleAdapter? = null
    private var bar: ProgressBar? = null

    private var appointmentTypeRecyclerView: RecyclerView? = null
    private var typesList = ArrayList<String>()
    private var newAppointmentViewModel: NewAppointmentViewModel? = null
    @Inject lateinit var appointmentFragment: AppointmentFragment



    /**
     * this is for appointment Post
     */

    private var listOfLessons: ArrayList<Lesson>? = null
    private var listOfTests: ArrayList<Test>? = null
    private var listOfGreeting: ArrayList<Greeting>? = null
    private var listOfTrialTest: ArrayList<TrialTest>? = null
    private var appointmentTypeAdapter: AppointmentTypeAdapter? = null


    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.compositeDisposable = CompositeDisposable()
        this.listOfLessons = ArrayList()
        this.listOfTests = ArrayList()
        this.listOfTrialTest = ArrayList()
        this.listOfGreeting = ArrayList()
        this.vehicleAdapter = VehicleAdapter(activity as MainActivity, ArrayList<Vehicle>())
        (activity as MainActivity).getApplicationCustomComponent().inject(this)
        this.appointmentTypeAdapter = AppointmentTypeAdapter(activity as MainActivity, ArrayList())
        this.typesList.add("Lesson")
        this.typesList.add("Trial-Test")
        this.typesList.add("Test")
        this.typesList.add("Greeting")
        this.newAppointmentViewModel = ViewModelProviders.of(this).get(NewAppointmentViewModel::class.java)

        setHasOptionsMenu(true)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_add_new_appointment, container, false)
        initViews(view)
        this.bar!!.visibility = View.INVISIBLE
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.submit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val appointment = Appointment()
        this.bar!!.visibility  =View.VISIBLE
        this.appointmentTypeAdapter!!.getAllItems().forEach {
            when {
                it.type == "Lesson" -> {
                    val lesson = Lesson()
                    lesson.student = it.student
                    //lesson.vehicle = it.student!!.vehicle
                    this.listOfLessons!!.add(lesson)
                }
                it.type == "Test" -> {
                    val test = Test()
                    test.student = it.student
                    //test.vehicle = it.student!!.vehicle
                    this.listOfTests!!.add(test)
                }
                it.type == "Trial-Test" -> {
                    val trialTest = TrialTest()
                    trialTest.student = it.student
                   // trialTest.vehicle = it.student!!.vehicle
                    this.listOfTrialTest!!.add(trialTest)
                }
                else -> {
                    val greeting = Greeting()
                    this.listOfGreeting!!.add(greeting)
                }
            }

            val dateTimeString = sharedPreferences.getString("selectedDate", "no")
            if (dateTimeString == "no") {
                return false
            }
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date = formatter.parse(dateTimeString)

            appointment.appointmentdate = date
            appointment.beginingTime = LocalTime(this.beginTime!!.text.toString())
            appointment.endingTime = LocalTime(this.endTime!!.text.toString())
            appointment.lessons = this.listOfLessons!!.toSet()
            appointment.greetings = this.listOfGreeting!!.toSet()
            appointment.tests = this.listOfTests!!.toSet()
            appointment.trialTests = this.listOfTrialTest!!.toSet()
            addAppointment(appointment, sharedPreferences.getString("token", "no"))

        }

        return super.onOptionsItemSelected(item)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        this.beginTime!!.setOnClickListener {
            runTimePickerDialog(this.beginTime!!)
        }
        this.endTime!!.setOnClickListener {
            runTimePickerDialog(it as TextView)
        }
        getAllStudent()
        fab!!.setOnClickListener {
            var dialog = runDialogForAddAppointmentType()
            var selectTypeSpinner = dialog.findViewById<Spinner>(R.id.CustomDialogSelectType)
            var selectStudent = dialog.findViewById<Spinner>(R.id.CustomDialogSelectStudent)
            var done = dialog.findViewById<Button>(R.id.CustomDialogDone)
            var typeAdapter = ArrayAdapter<String>(activity as MainActivity, android.R.layout.simple_spinner_item, this.typesList)
            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            selectTypeSpinner.adapter = typeAdapter
            this.newAppointmentViewModel!!.provideStudents().observe(this, android.arch.lifecycle.Observer { students ->

                var studentsAdapter = StudentSpinnerAdapter(activity as MainActivity, android.R.layout.simple_spinner_item, students as ArrayList<Student>)
                studentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                selectStudent.adapter = studentsAdapter
                this.appointmentTypeRecyclerView!!.adapter = this.appointmentTypeAdapter
                dialog.show()
            })

            done.setOnClickListener {
                var type = selectTypeSpinner.selectedItem as String
                var selectedStudent = selectStudent.selectedItem as Student
                var appointmentType = AppointmentType(type, selectedStudent)
                this.appointmentTypeAdapter!!.addItem(appointmentType)
                this.appointmentTypeRecyclerView!!.adapter = this.appointmentTypeAdapter
                dialog.dismiss()
            }


        }


    }

    override fun onDestroy() {
        this.compositeDisposable!!.dispose()
        super.onDestroy()
    }

    private fun initViews(view: View) {
        this.beginTime = view.findViewById<TextView>(R.id.newAppointmentStartTime)
        this.endTime = view.findViewById<TextView>(R.id.newAppointmentEndTime)
        this.bar = view.findViewById<ProgressBar>(R.id.addNewAppointmentProgressBar)
        this.fab = (activity as MainActivity).findViewById(R.id.fab)
        this.appointmentTypeRecyclerView = view.findViewById(R.id.AddNewAppointmentRecycleView)
        this.appointmentTypeRecyclerView!!.setHasFixedSize(true)
        this.appointmentTypeRecyclerView!!.layoutManager = LinearLayoutManager(activity as MainActivity)
    }


    private fun getAllStudent() {
        this.compositeDisposable!!.add(ServiceRepo.getAllStudents(this.sharedPreferences!!.getString("token", "no")).subscribe({
            this.newAppointmentViewModel!!.saveStudents(it)
        }, {
            print(it.localizedMessage)
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
        }))
    }

    private fun addAppointment(appointment: Appointment, token: String) {
        this.compositeDisposable!!.add(ServiceRepo.postAppointment(appointment, token).subscribe({
            print(it)
            this.bar!!.visibility = View.INVISIBLE
            Toast.makeText(activity, "Ok", Toast.LENGTH_LONG).show()
            (activity as MainActivity).supportFragmentManager
                      .beginTransaction()
                      .replace(R.id.fragmentContainer , this.appointmentFragment)
                      .commit()
        }, {
            Toast.makeText(activity, "Conflict", Toast.LENGTH_LONG).show()
            this.bar!!.visibility = View.INVISIBLE
            print(it)
        }))
    }

    private fun runDialogForAddAppointmentType(): Dialog {
        var dialog = Dialog(activity as MainActivity)
        dialog.setContentView(R.layout.appointment_type_custom_dialog)
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return dialog

    }

    private fun runTimePickerDialog(view:TextView){
        val mCurrentTime = Calendar.getInstance()
        val hour = mCurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mCurrentTime.get(Calendar.MINUTE)

        val mTimePicker = TimePickerDialog(activity as MainActivity,TimePickerDialog.OnTimeSetListener(function = { _, selectedHour, selectedMinute ->
            view.text = "$selectedHour:$selectedMinute"
        }),hour,minute,false).show()

    }


}
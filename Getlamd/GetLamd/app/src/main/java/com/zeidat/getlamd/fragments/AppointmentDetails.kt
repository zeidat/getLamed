package com.zeidat.getlamd.fragments

import android.app.Dialog
import android.app.Service
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.*
import com.getlamd.entity.buisness.Appointment

import com.zeidat.getlamd.R
import com.zeidat.getlamd.activites.MainActivity
import com.zeidat.getlamd.adapters.GreetingAdapter
import com.zeidat.getlamd.adapters.LessonAdapter
import com.zeidat.getlamd.adapters.TestsAdapter
import com.zeidat.getlamd.adapters.TrialAdapter
import com.zeidat.getlamd.getDateString
import com.zeidat.getlamd.repo.ServiceRepo
import com.zeidat.getlamd.repo.SharingViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_appointment.*
import kotlinx.android.synthetic.main.fragment_appointment_details.*
import kotlinx.android.synthetic.main.fragment_appointment_details.view.*
import org.w3c.dom.Text
import javax.inject.Inject


class AppointmentDetails : Fragment() {
    private var compositeDisposable: CompositeDisposable? = null

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var homeFragment: HomeFragment

    @Inject
    lateinit var addNewAppointmentFragment: AddNewAppointmentFragment
    private var sharingViewModel: SharingViewModel? = null

    @Inject
    lateinit var appointmentCompleteFragment: AppointmentComplete

    //declaring views
    private var appointmentDate: TextView? = null
    private var beginingTime: TextView? = null
    private var endingTime: TextView? = null
    //private var lessonsHolder: LinearLayout? = null
    //private var trialHolder: LinearLayout? = null
    //private var testsHolder: LinearLayout? = null
    //private var greetingsHolder: LinearLayout? = null
    private var deleteAppointment: Button? = null
    private var appointmentID: Int? = null
    private var bar: ProgressBar? = null
    private var appointment: Appointment? = null
    private var fab: FloatingActionButton? = null
    private var appointmentComplete: Button? = null

    /**
     * adapters
     */

    private var lessonAdapter: LessonAdapter? = null
    private var testAdapter: TestsAdapter? = null
    private var trialAdapter: TrialAdapter? = null
    private var greetingAdapter: GreetingAdapter? = null

    private var lessonRecyclerView: RecyclerView? = null
    private var testRecyclerView: RecyclerView? = null
    private var trialRecyclerView: RecyclerView? = null
    private var greetingRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.compositeDisposable = CompositeDisposable()
        (activity as MainActivity).getApplicationCustomComponent().inject(this)
        this.sharingViewModel = ViewModelProviders.of(activity as MainActivity).get(SharingViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.submit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val lessons = this.lessonAdapter!!.getAllItem()
        val tests = this.testAdapter!!.getAllItem()
        val trialTest = this.trialAdapter!!.getAllItem()
        val greeting = this.greetingAdapter!!.getAllItem()
        this.sharingViewModel!!.getSelectedAppointment().observe(this, Observer {
            it?.let {appointment->
                appointment.lessons = lessons
                appointment.tests = tests
                appointment.trialTests = trialTest
                appointment.greetings = greeting
            }
        })

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_appointment_details, container, false)
        this.appointmentDate = view.findViewById(R.id.AppointmentDetailsDateContent)
        this.beginingTime = view.findViewById(R.id.AppointmentDetailsStartTimeContent)
        this.endingTime = view.findViewById(R.id.AppointmentDetailsEndTimeContent)
        this.lessonRecyclerView = view.findViewById(R.id.AppointmentDetailslessons)
        this.testRecyclerView = view.findViewById(R.id.appointmentDetailsTests)
        this.trialRecyclerView = view.findViewById(R.id.appointmentDetailsTrial)
        this.greetingRecyclerView = view.findViewById(R.id.appointmentDetailsGreeting)
//        this.lessonsHolder =zview.findViewById(R.id.lessonsHolder)
//        this.testsHolder = vzew.findViewById(R.id.testsHolder)
//        this.trialHolder = view.findViewById(R.id.trialTestsHolder)
//        this.greetingsHolder = view.findViewById(R.id.GreetingsHolder)
        this.deleteAppointment = view.findViewById(R.id.appointmentDetailsDelete)
        this.bar = view.findViewById(R.id.progressBar)
        this.bar!!.visibility = View.INVISIBLE
        this.fab = (activity as MainActivity).findViewById(R.id.fab)
        this.appointmentComplete = view.findViewById(R.id.appointmentDetailsComplete)
        this.fab!!.hide()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var id = this.sharedPreferences.getLong("appointmentID", -1L)
        val token = this.sharedPreferences.getString("token", "no")
        //getAppointmentById(id, token)

//        this.sharingViewModel!!.getSelectedAppointment().observeForever {
//            this.appointmentID = it!!.id.toInt()
//            this.appointment = it
//        }

        this.sharingViewModel!!.getSelectedAppointment().observe(this, Observer {
            it?.let { appointment ->
                this.lessonAdapter = LessonAdapter(activity as MainActivity, appointment.lessons.toList())
                this.lessonRecyclerView!!.setHasFixedSize(true)
                this.lessonRecyclerView!!.layoutManager = LinearLayoutManager(activity as MainActivity)
                this.lessonRecyclerView!!.adapter = this.lessonAdapter

                this.testAdapter = TestsAdapter(activity as MainActivity, appointment.tests.toList())
                this.testRecyclerView!!.setHasFixedSize(true)
                this.testRecyclerView!!.layoutManager = LinearLayoutManager(activity as MainActivity)
                this.testRecyclerView!!.adapter = this.testAdapter

                this.trialAdapter = TrialAdapter(activity as MainActivity, appointment.trialTests.toList())
                this.trialRecyclerView!!.setHasFixedSize(true)
                this.trialRecyclerView!!.layoutManager = LinearLayoutManager(activity as MainActivity)
                this.trialRecyclerView!!.adapter = this.trialAdapter

                this.greetingAdapter = GreetingAdapter(activity as MainActivity ,appointment.greetings.toList())
                this.greetingRecyclerView!!.setHasFixedSize(true)
                this.greetingRecyclerView!!.layoutManager = LinearLayoutManager(activity as MainActivity )
                this.greetingRecyclerView!!.adapter = this.greetingAdapter

            }
        })

//        if (this.appointment != null) {
//            //updateUI(this.appointment!!)
//        }

        this.deleteAppointment!!.setOnClickListener {
            this.bar!!.visibility = View.VISIBLE
            var token = this.sharedPreferences.getString("token", "no")
            val view = layoutInflater.inflate(R.layout.cancellation_reason_dialog, null)
            var dialog = Dialog(activity)
            dialog.setTitle("Delete Appointment")
            dialog.setContentView(R.layout.cancellation_reason_dialog)
            dialog.show()
            var textView = dialog.findViewById<EditText>(R.id.cancellation)
            var cancel = dialog.findViewById<Button>(R.id.cancel)
            var confirm = dialog.findViewById<Button>(R.id.confirm)
            confirm.setOnClickListener {
                this.compositeDisposable!!.add(ServiceRepo.deleteAppointment(appointmentID!!, token!!, textView.text.toString()).subscribe({
                    this.bar!!.visibility = View.INVISIBLE
                    (activity as MainActivity).supportFragmentManager
                              .beginTransaction()
                              .replace(R.id.fragmentContainer, this.homeFragment)
                              .commit()
                }, {
                    dialog.dismiss()
                    this.bar!!.visibility = View.INVISIBLE
                }))
            }
            cancel.setOnClickListener {
                dialog.dismiss()
                this.bar!!.visibility = View.INVISIBLE
            }

        }

        this.appointmentComplete!!.setOnClickListener {
            (activity as MainActivity).supportFragmentManager
                      .beginTransaction()
                      .addToBackStack(null)
                      .replace(R.id.fragmentContainer, this.appointmentCompleteFragment)
                      .commit()
        }


    }

//    private fun getAppointmentById(id: Long, token: String) {
//        this.compositeDisposable!!.add(ServiceRepo.getAppointmentById(id, token).subscribe({
//            print(it)
//        }, {
//            print(it)
//        }))
//    }

//    private fun updateUI(appointment: Appointment) {
//        this.appointmentDate!!.text = appointment.appointmentdate.getDateString()
//        this.beginingTime!!.text = appointment.beginingTime.toString("HH:mm a")
//        this.endingTime!!.text = appointment.endingTime.toString("HH:mm a")
//
//        if (appointment.lessons.size > 0) {
//            appointment.lessons.forEach {
//                val view = layoutInflater.inflate(R.layout.lessons_details, null)
//                var student = view.findViewById<TextView>(R.id.lessonDetailsStudent)
//                var vehicle = view.findViewById<TextView>(R.id.lessonDetailsVehicle)
//                var details = view.findViewById<TextView>(R.id.lessonDetailsDet)
//                student.text = it.student.getLamdUser.arabicName
//                //vehicle.text = it.vehicle.make
//                details.text = it.details
//                this.lessonsHolder!!.addView(view)
//            }
//        }
//
//        if (appointment.tests.size > 0) {
//            appointment.tests.forEach {
//                val view = layoutInflater.inflate(R.layout.tests_details, null)
//                var student = view.findViewById<TextView>(R.id.testDetailsStudent)
//                var vehicle = view.findViewById<TextView>(R.id.testDetailsVehicle)
//                var details = view.findViewById<TextView>(R.id.testDetailsDet)
//                student.text = it.student.getLamdUser.arabicName
//                // vehicle.text = it.vehicle.make
//                details.text = it.details
//                this.testsHolder!!.addView(view)
//            }
//
//        }
//
//        if (appointment.trialTests.size > 0) {
//            appointment.trialTests.forEach {
//                val view = layoutInflater.inflate(R.layout.trial_tests_details, null)
//                var student = view.findViewById<TextView>(R.id.trialDetailsStudent)
//                var vehicle = view.findViewById<TextView>(R.id.trialDetailsVehicle)
//                var details = view.findViewById<TextView>(R.id.trialDetailsDet)
//                student.text = it.student.getLamdUser.arabicName
//                //
//                //vehicle.text = it.vehicle.make
//                details.text = it.details
//                this.trialHolder!!.addView(view)
//            }
//
//        }
//
//        if (appointment.greetings.size > 0) {
//            appointment.greetings.forEach {
//                val view = layoutInflater.inflate(R.layout.greetings_details, null)
//                var details = view.findViewById<TextView>(R.id.greetingDetailsDet)
//                details.text = it.details
//                this.greetingsHolder!!.addView(view)
//            }
//
//        }
//    }


    override fun onDestroy() {
        this.compositeDisposable!!.dispose()
        this.fab!!.show()
        super.onDestroy()
    }

}
package com.zeidat.getlamd.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.getlamd.entity.buisness.Appointment

import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker
import com.zeidat.getlamd.*
import com.zeidat.getlamd.activites.MainActivity
import com.zeidat.getlamd.adapters.NewAppointmentAdpter
import com.zeidat.getlamd.repo.ServiceRepo
import com.zeidat.getlamd.repo.SharingViewModel
import com.zeidat.getlamd.viewmodels.AppointmentViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

import org.joda.time.DateTime

import javax.inject.Inject

//    @Inject
//    SharedPreferences sharedPreferences ;
//
//    SharedPreferences.Editor editor ;

class AppointmentFragment : Fragment(), DatePickerListener {

    private val date: EditText? = null
    private val type: EditText? = null
    private val time: EditText? = null
    private var picker: HorizontalPicker? = null
    private var fab: FloatingActionButton? = null
    private var listView: RecyclerView? = null
    private var compositeDisposable:CompositeDisposable? = null
    private var listOfAppointments:ArrayList<Appointment>? = null
    private var appointmentAdapter:NewAppointmentAdpter?  =null
    private  var sharedViewModel:SharingViewModel? = null
    private var tabLayout:TabLayout? = null
    private var _selectedDateTime:DateTime? = null
    private var appointmentViewModel:AppointmentViewModel? = null


    @Inject
    lateinit var addNewAppointmentFragment: AddNewAppointmentFragment
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @Inject lateinit var  appointmentDetails: AppointmentDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).getApplicationCustomComponent().inject(this)
        this.compositeDisposable = CompositeDisposable()
        this.sharedPreferences = (activity as MainActivity).provideSharedPreferences()
        this.listOfAppointments = ArrayList()
        this.appointmentAdapter = NewAppointmentAdpter(activity as MainActivity , this.listOfAppointments!!)
        this.sharedViewModel = ViewModelProviders.of(activity as MainActivity).get(SharingViewModel::class.java)
        this.appointmentViewModel = ViewModelProviders.of(this).get(AppointmentViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllAppointments(this.sharedPreferences!!.getString("token", "no"))
//        this.sharedViewModel!!.getAllAppointments().observe(this, Observer { appointments->
//            appointments?.let {
//               var appointment =  it.displayAppointmentByDate(this._selectedDateTime!!)
//                this.appointmentAdapter  = NewAppointmentAdpter(activity as MainActivity , appointment)
//                this.listView!!.setHasFixedSize(true)
//                this.listView!!.adapter = this.appointmentAdapter
//                onItemClick()
//
//            }
//        })

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_appointment, container, false)
        //  date = (EditText)getActivity().findViewById(R.id.appointmentFragmentDate);
        picker = view.findViewById<View>(R.id.appointmentDatePicker) as HorizontalPicker
        fab = (activity as MainActivity).findViewById(R.id.fab)
        this.tabLayout  = view.findViewById(R.id.AppointmentFragmentTabLayout)
        if(!(fab!!.isShown))
            fab!!.show()

        this.listView = view.findViewById(R.id.appointmentListOf)
        this.listView!!.layoutManager = LinearLayoutManager(activity as MainActivity)


        picker!!
                .setListener { this.onDateSelected(it) }
                .setDays(20)
                .setOffset(10)
                .setDateSelectedColor(Color.DKGRAY)
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(Color.DKGRAY)
                .setTodayButtonTextColor(activity!!.getColor(R.color.colorPrimary))
                .setTodayDateTextColor(activity!!.getColor(R.color.colorPrimary))
                .setUnselectedDayTextColor(Color.DKGRAY)
                .setDayOfWeekTextColor(Color.DKGRAY)
                .setUnselectedDayTextColor(activity!!.getColor(R.color.mainColor))
                .showTodayButton(false)
                .init()

        // or on the View directly after init was completed
        picker!!.backgroundColor = activity!!.getColor(R.color.white)
        picker!!.setDate(DateTime().plusDays(1))

        /**
         * handle fab action for AppointmentFragment
         */
        fab!!.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                      .replace(R.id.fragmentContainer ,addNewAppointmentFragment).commit()
        }

        return view
    }

    override fun onDateSelected(dateSelected: DateTime) {
        var dateString = dateSelected.toString()
        print(dateSelected)
        var editor = this.sharedPreferences!!.edit()
        editor.putString("selectedDate",dateString).commit()
        this._selectedDateTime = dateSelected
       this.appointmentViewModel!!.provideAppointments().observe(this , Observer {value->
            value?.let {
                var appointment =  it.displayAppointmentByDate(this._selectedDateTime!!)
                this.appointmentAdapter  = NewAppointmentAdpter(activity as MainActivity , appointment)
                this.listView!!.setHasFixedSize(true)
                this.listView!!.adapter = this.appointmentAdapter
                onItemClick()
            }

        })

    }

    private fun getAllAppointments(token:String){
        this.compositeDisposable!!.add(ServiceRepo.getAllAppointments(token).subscribe({appointments->
            this.appointmentViewModel!!.saveAppointments(appointments)

        },{
            print(it.cause)
        }))
    }

    private  fun onItemClick(){
        this.compositeDisposable!!.add(this.appointmentAdapter!!.clickEvent.androidObservable().subscribe{ appointment->
            (activity as MainActivity).supportFragmentManager
                      .beginTransaction()
                      .replace(R.id.fragmentContainer , this.appointmentDetails)
                      .addToBackStack(null)
                      .commit()

            this.sharedViewModel!!.selectAppointment(appointment)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        this.compositeDisposable!!.dispose()
    }
}

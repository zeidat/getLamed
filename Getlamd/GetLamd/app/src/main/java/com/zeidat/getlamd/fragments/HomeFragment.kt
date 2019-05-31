package com.zeidat.getlamd.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextClock
import com.getlamd.entity.buisness.Appointment
import com.zeidat.getlamd.*

import com.zeidat.getlamd.activites.MainActivity
import com.zeidat.getlamd.adapters.HomeAdapter
import com.zeidat.getlamd.adapters.NewAppointmentAdpter
import com.zeidat.getlamd.repo.ServiceRepo
import com.zeidat.getlamd.repo.SharingViewModel
import io.reactivex.disposables.CompositeDisposable
import org.joda.time.DateTime
import javax.inject.Inject
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    private var sharingViewModel: SharingViewModel? = null
    private var clock: TextClock? = null
    private var listView: RecyclerView? = null
    private var token: String? = null


    private var sharedPreferences: SharedPreferences? = null
    private var compositeDisposable: CompositeDisposable? = null
    private var appointmentList: ArrayList<Appointment>? = null
    private var homeAdapter: HomeAdapter? = null
    private var newAppointmentAdapter: NewAppointmentAdpter? = null
    private var fab:FloatingActionButton? = null

    @Inject
    lateinit var appointmentDetails: AppointmentDetails
    @Inject
    lateinit var appointmentFragment: AppointmentFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = (activity as MainActivity).provideSharedPreferences()
        this.token = sharedPreferences!!.getString("token", "no")
        this.compositeDisposable = CompositeDisposable()
        this.appointmentList = ArrayList()
        this.homeAdapter = HomeAdapter(activity as MainActivity, appointmentList!!)
        (activity as MainActivity).getApplicationCustomComponent().inject(this)
        this.newAppointmentAdapter = NewAppointmentAdpter(activity as MainActivity, this.appointmentList!!)
        this.sharingViewModel = ViewModelProviders.of(activity as MainActivity).get(SharingViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        clock = view.findViewById<View>(R.id.mainClock) as TextClock
        listView = view.findViewById<RecyclerView>(R.id.HomeFragmentList)
        this.fab = (activity as MainActivity).findViewById(R.id.fab)
        this.listView!!.setHasFixedSize(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.fab!!.setOnClickListener {
            (activity as MainActivity).supportFragmentManager
                      .beginTransaction()
                      .replace(R.id.fragmentContainer ,this.appointmentFragment)
                      .addToBackStack(null)
                      .commit()
        }

        this.sharingViewModel!!.getAllAppointments().observeForever { appointments ->
            this.newAppointmentAdapter = NewAppointmentAdpter(activity as MainActivity, (appointments!!.displayAppointmentByDate(DateTime())) as ArrayList<Appointment>)
            this.listView!!.setHasFixedSize(true)
            this.listView!!.layoutManager = LinearLayoutManager(activity as MainActivity)
            this.listView!!.adapter = this.newAppointmentAdapter
            handleEventClick()
        }
        getAllAppointments()
    }

    private fun getAllAppointments() {
        this.compositeDisposable!!.add(ServiceRepo.getAllAppointments(this.token!!).subscribe({ appointments ->
            if (appointments.isNotEmpty()) {
                this.sharingViewModel!!.saveAppointments(appointments)
            }
        }, {
            print(it.localizedMessage)
        }))
    }

    private fun handleEventClick() {
        this.compositeDisposable!!.add(this.newAppointmentAdapter!!.clickEvent.androidObservable().subscribe({ appointment ->
            (activity as MainActivity).supportFragmentManager
                      .beginTransaction()
                      .replace(R.id.fragmentContainer, this.appointmentDetails)
                      .addToBackStack(null)
                      .commit()


        }, {
            print(it)
        }))
    }

    override fun onDestroy() {
        this.compositeDisposable!!.dispose()
        super.onDestroy()
    }
}

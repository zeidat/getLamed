package com.zeidat.getlamd.fragments

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.getlamd.entity.management.Vehicle

import com.zeidat.getlamd.R
import com.zeidat.getlamd.activites.MainActivity
import com.zeidat.getlamd.adapters.VehicleAdapter
import com.zeidat.getlamd.repo.ServiceRepo
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class VehicleFragment : Fragment() {
    private var compositeDisposable: CompositeDisposable? = null
    private var listOfVehicles: ArrayList<Vehicle>? = null
    private var vehicleAdapter: VehicleAdapter? = null
    private var listView:ListView? = null
    private var fab:FloatingActionButton? = null
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var addNewVehicleFragment: AddNewVehicleFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).getApplicationCustomComponent().inject(this)
        this.compositeDisposable = CompositeDisposable()
        this.listOfVehicles = ArrayList()
        this.vehicleAdapter = VehicleAdapter(activity as MainActivity , listOfVehicles!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view  = inflater.inflate(R.layout.fragment_vehicle, container, false)
        this.listView = view.findViewById(R.id.VehicleFragmentListOfVehicles)
        this.listView!!.adapter = this.vehicleAdapter
        fab = (activity as MainActivity).findViewById(R.id.fab)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab!!.setOnClickListener {
            (activity as MainActivity).supportFragmentManager
                      .beginTransaction()
                      .replace(R.id.fragmentContainer ,this.addNewVehicleFragment).commit()
        }
        getAllVehicles()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.compositeDisposable!!.dispose()
    }

    private fun getAllVehicles(){
        var token = this.sharedPreferences.getString("token" , "no")
        if(token == "no")
            return

        this.compositeDisposable!!.add(ServiceRepo.getAllVehicles(token).subscribe({ vehicles ->
            if(vehicles.isNotEmpty()){
                this.vehicleAdapter!!.addAll(vehicles)
                this.vehicleAdapter!!.notifyDataSetChanged()
                this.listView!!.adapter = this.vehicleAdapter
            }
        },{
            print(it.localizedMessage)
        }))

    }


}// Required empty public constructor

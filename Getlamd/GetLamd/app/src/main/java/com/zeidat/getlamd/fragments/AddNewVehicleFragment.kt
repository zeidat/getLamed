package com.zeidat.getlamd.fragments

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.getlamd.entity.management.Vehicle

import com.zeidat.getlamd.R
import com.zeidat.getlamd.activites.MainActivity
import com.zeidat.getlamd.repo.ServiceRepo
import io.reactivex.disposables.CompositeDisposable
import org.w3c.dom.Text
import javax.inject.Inject

class AddNewVehicleFragment : Fragment() {
    private var compositeDisposable:CompositeDisposable? = null
    /**
     * views
     */
    private var make:TextView? = null
    private var model:TextView? =null
    private var color:TextView? = null
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.compositeDisposable = CompositeDisposable()
        (activity as MainActivity).getApplicationCustomComponent().inject(this)
        setHasOptionsMenu(true)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_add_new_vehicle, container, false)
        this.make = view.findViewById(R.id.addVMake)
        this.model = view.findViewById(R.id.AddVModel)
        this.color = view.findViewById(R.id.addVColor)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.submit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var vehicle  =Vehicle()
        vehicle.make = this.make!!.text.toString()
        vehicle.model = this.model!!.text.toString()
        vehicle.color = this.color!!.text.toString()
        var token = this.sharedPreferences.getString("token" ,"no")
        if(token == "no")
            return false
        addVehicle(token , vehicle)
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        this.compositeDisposable!!.dispose()
    }

    private fun addVehicle(token:String , vehicle:Vehicle){
        this.compositeDisposable!!.add(ServiceRepo.addNewVehicle(token ,vehicle ).subscribe({
            print(it)
            Toast.makeText(activity as MainActivity , "OK!",Toast.LENGTH_LONG ).show()
        },{
            print(it.localizedMessage)
        }))
    }
}
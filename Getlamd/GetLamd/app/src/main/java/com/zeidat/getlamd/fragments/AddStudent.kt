package com.zeidat.getlamd.fragments

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.*
import android.widget.*
import com.getlamd.entity.management.Phone
import com.getlamd.entity.management.Vehicle
import com.getlamd.entity.security.GetLamdUser
import com.getlamd.entity.user.Student
import com.zeidat.getlamd.BaseActivity

import com.zeidat.getlamd.R
import com.zeidat.getlamd.activites.MainActivity
import com.zeidat.getlamd.adapters.StudentListAdapter
import com.zeidat.getlamd.adapters.VehicleSpinnerAdapter
import com.zeidat.getlamd.repo.ServiceRepo
import com.zeidat.getlamd.viewmodels.AddStudentViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class AddStudent : Fragment() {
    var name: EditText? = null
    var nationalId: EditText? = null
    var email: EditText? = null
    var phone: EditText? = null
    var fab: FloatingActionButton? = null
    var studentsList: ArrayList<Student>? = null
    var back: Button? = null
    private var submit: Button? = null
    private var transaction: FragmentTransaction? = null
    private var bar: ProgressBar? = null
    private var vehiclesSpinner: Spinner? = null
    private var addStudentViewModel:AddStudentViewModel? = null


    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var getLamdUser: GetLamdUser? = null
    private var studentListAdapter: StudentListAdapter? = null

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var homeFragment: HomeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.sharedPreferences = (activity as MainActivity).provideSharedPreferences()
        this.getLamdUser = GetLamdUser()
        this.studentsList = ArrayList()
        this.studentListAdapter = StudentListAdapter((activity as MainActivity), studentsList!!)
        this.transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
        setHasOptionsMenu(true)
        (activity as MainActivity).getApplicationCustomComponent().inject(this)
        this.addStudentViewModel = ViewModelProviders.of(this).get(AddStudentViewModel::class.java)
    }

    @SuppressLint("RestrictedApi")
    override fun onDestroy() {
        super.onDestroy()
        this.compositeDisposable!!.dispose()
        this.fab!!.show()
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_add_student, container, false)
        this.name = view.findViewById(R.id.AddStudentName)
        this.nationalId = view.findViewById(R.id.AddStudentNationalId)
        this.email = view.findViewById(R.id.AddStudentEmail)
        this.phone = view.findViewById(R.id.AddStudentPhonenumber)
        this.bar = view.findViewById(R.id.addStudentBar)
        this.fab = activity!!.findViewById(R.id.fab)
        this.fab!!.hide()
        this.bar!!.visibility = View.INVISIBLE
        this.vehiclesSpinner = view.findViewById(R.id.AddStudentSpinner)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllVehicles(this.sharedPreferences.getString("token", "no"))

        this.vehiclesSpinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.submit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (!fieldsAreValid()) {
            Toast.makeText(activity, "Please Fill all fields, and check the email", Toast.LENGTH_LONG).show()
        } else {

            addStudent(this.vehiclesSpinner!!.selectedItem as Vehicle)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun GetLamdUser.initGetLamdUser(name: String, nationalId: String, email: String, phone: Phone): GetLamdUser {
        var phones = HashSet<Phone>()
        phones.add(phone)
        this.arabicName = name
        this.nationalId = nationalId
        this.email = email
        this.phones = phones
        return this
    }

    private fun addStudent(vehicle: Vehicle) {
        var phone = Phone()
        this.bar!!.visibility = View.VISIBLE
        val token = this.sharedPreferences.getString("token", "no")!!
        phone.number = this.phone!!.text.toString()
        this.compositeDisposable.add(ServiceRepo.postStudent(token, getLamdUser!!.initGetLamdUser(name!!.text.toString(),
                  nationalId!!.text.toString(), email!!.text.toString(), phone))
                  .subscribe({ response ->
                      Toast.makeText(activity as MainActivity, "Done", Toast.LENGTH_LONG).show()
                      print(response)
                      response.vehicle = vehicle
                      putStudent(token ,response.id,response)
                  }, {
                      print(it.cause)
                  }))
    }

    private fun getAllVehicles(token: String) {
        this.compositeDisposable.add(ServiceRepo.getAllVehicles(token).subscribe({ vehicles ->
            val vehicleSpinnerAdapter = VehicleSpinnerAdapter(activity as MainActivity, android.R.layout.simple_spinner_item, vehicles as ArrayList<Vehicle>)
            vehicleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            this.vehiclesSpinner!!.adapter = vehicleSpinnerAdapter
            this.addStudentViewModel!!.saveVehicle(vehicles)

        }, {
            Toast.makeText(activity as MainActivity ,"Error while getting Vehicles", Toast.LENGTH_LONG).show()
        }))
    }


    private fun fieldsAreValid(): Boolean {
        if (this.name!!.text.toString() == "") {
            return false
        } else if (this.nationalId!!.text.toString() == "") {
            return false
        } else if (this.email!!.text.toString() == "") {
            return false
        } else if (this.phone!!.text.toString() == "") {
            return false
        } else if (!(this.email!!.text.contains("@"))) {
            return false
        }
        return true
    }

    private fun putStudent(token: String,studentId: Long , student:Student ){
        this.compositeDisposable.add(ServiceRepo.putStudent(token ,studentId , student).subscribe({ student->
            student?.let {
                print(it)
                this.bar!!.visibility = View.INVISIBLE
                Toast.makeText(activity as MainActivity,"Student Added",Toast.LENGTH_LONG).show()
                (activity as BaseActivity).supportFragmentManager
                          .beginTransaction()
                          .replace(R.id.fragmentContainer , this.homeFragment)
                          .commit()

            }

        },{
            Toast.makeText(activity as MainActivity,"Error Wihle Attaching Vehicle To Student",Toast.LENGTH_LONG).show()
            this.bar!!.visibility = View.INVISIBLE
        }))

    }


}
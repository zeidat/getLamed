package com.zeidat.getlamd.activites

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.zeidat.getlamd.BaseActivity
import com.zeidat.getlamd.R

import com.zeidat.getlamd.dagger.AppComponent
import com.zeidat.getlamd.dagger.AppModule
import com.zeidat.getlamd.dagger.DaggerAppComponent
import com.zeidat.getlamd.dagger.FragmentModule
import com.zeidat.getlamd.fragments.*
import com.zeidat.getlamd.repo.ServiceRepo
import io.reactivex.disposables.CompositeDisposable
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var appointmentFragment:AppointmentFragment
    @Inject
    lateinit var homeFragment: HomeFragment
    @Inject
    lateinit var profileFragment: ProfileFragment
    @Inject
    lateinit var settings: Settings
    @Inject
    lateinit var studentsFragment: StudentsFragment
    @Inject
    lateinit var vehicleFragment: VehicleFragment
    @Inject
    lateinit var addNewAppointmentFragment: AddNewAppointmentFragment
    @Inject
    lateinit var addStudent: AddStudent

    private var compositeDisposable:CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getApplicationCustomComponent().inject(this)
        // init RealM object
        Realm.init(this)


        this.compositeDisposable = CompositeDisposable()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        /**
         * get Instructor Info
         */
        val token = this.sharedPreferences!!.getString("token","no")
        this.compositeDisposable!!.add(ServiceRepo.getInstructorInformation(token).subscribe({
            print(it)
        },{
            print(it.cause)
        }))

        val fab = findViewById<FloatingActionButton>(R.id.fab)
//        fab.setOnClickListener { view ->
//           supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer , appointmentFragment).commit()
//        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        var transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer ,homeFragment).commit()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)

        } else {
            super.onBackPressed()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        val id = item.itemId
//
//        if(id == R.id.action_settings){
//            setTitle("Settings")
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragmentContainer , Settings()).commit()
//        }
//        return if (id == R.id.action_settings) {
//            true
//        } else super.onOptionsItemSelected(item)
//
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragment = Fragment()
        val id = item.itemId

        when (id) {
            R.id.nav_home -> {
                title = "Home"
                fragment = this.homeFragment
                // Handle the camera action
            }
            R.id.nav_add -> {
                title = "Add Appointment"
                fragment = this.appointmentFragment
            }
            R.id.nav_students -> {
                title = "Students"
                fragment = this.studentsFragment
            }
            R.id.nav_car -> {
                title = "Vehicles"
                fragment = this.vehicleFragment
            }
            R.id.nav_calender -> title = "calender"
            R.id.logout -> {
                sharedPreferences!!.edit().putString("token" ,"no").commit()
                var intent = Intent(this ,Login::class.java)
                startActivity(intent)
                finish()
            }
        }
        supportFragmentManager.popBackStack()
        supportFragmentManager
                  .beginTransaction()
                  .replace(R.id.fragmentContainer , fragment)
                  .commit()


        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

}

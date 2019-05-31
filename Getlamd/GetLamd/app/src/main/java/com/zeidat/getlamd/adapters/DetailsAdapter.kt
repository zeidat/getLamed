package com.zeidat.getlamd.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.getlamd.entity.buisness.*
import com.zeidat.getlamd.R
import com.zeidat.getlamd.repo.ServiceRepo
import com.zeidat.service.models.Token
import io.reactivex.disposables.CompositeDisposable

class LessonAdapter(private val mContext: Context, private val lessons: List<Lesson>) : RecyclerView.Adapter<TypeViewHolder>() {
    private val compositeDisposable = CompositeDisposable()
    private val sharedPreferences = mContext.getSharedPreferences("login",Context.MODE_PRIVATE)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TypeViewHolder {
        return TypeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.lessons_details, p0, false))
    }

    override fun getItemCount(): Int {
        return lessons.size
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        var lesson = lessons[position] as Lesson
        holder.studentTextView.text = lesson.student.getLamdUser.arabicName
        //TODO first clear all student // then uncomment this line
        //holder.vehicleTextView.text = lesson.student.vehicle.make
        holder.detailsView.text = lesson.details
        holder.statusSpinner.adapter = ArrayAdapter<AppointmentStatus>(mContext, android.R.layout.simple_spinner_item, AppointmentStatus.values())
        holder.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                print("any")

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                lessons[position].status = holder.statusSpinner.selectedItem as AppointmentStatus
                putLesson(sharedPreferences.getString("token","no" ),lessons[position].id ,lessons[position] )
            }
        }
    }

    fun getAllItem():Set<Lesson>{
        return this.lessons.toSet()
    }

    fun putLesson(token: String ,lessonId:Long ,lesson: Lesson){
        this.compositeDisposable.add(ServiceRepo.putLesson(token ,lessonId ,lesson).subscribe({
            print(it)
        },{
            print(it)
        }))
    }

}

class TestsAdapter(private val mContext: Context, private val tests: List<Test>) : RecyclerView.Adapter<TypeViewHolder>() {
    private val compositeDisposable = CompositeDisposable()
    private val sharedPreferences = mContext.getSharedPreferences("login",Context.MODE_PRIVATE)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TypeViewHolder {
        return TypeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.lessons_details, p0, false))
    }

    override fun getItemCount(): Int {
        return tests.size
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val test = tests[position] as Test
        holder.studentTextView.text = test.student.getLamdUser.arabicName
        //TODO first clear all student // then uncomment this line
        //holder.vehicleTextView.text = test.student.vehicle.make
        holder.detailsView.text = test.details
        holder.statusSpinner.adapter = ArrayAdapter<AppointmentStatus>(mContext, android.R.layout.simple_spinner_item, AppointmentStatus.values())
        holder.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, p: Int, id: Long) {
                tests[position].status = holder.statusSpinner.selectedItem as AppointmentStatus
                putTest(sharedPreferences.getString("token","no"),tests[position].id , tests[position])
            }
        }

    }
    fun getAllItem():Set<Test>{
        return this.tests.toSet()
    }

    private fun putTest(token: String , testID:Long , test: Test){
        this.compositeDisposable.add(ServiceRepo.putTest(token ,testID ,test ).subscribe({
            print(it)
        },{
            print(it)
        }))

    }
}

class TrialAdapter(private val mContext: Context, private val trials: List<TrialTest>) : RecyclerView.Adapter<TypeViewHolder>() {
    private val compositeDisposable = CompositeDisposable()
    private val sharedPreferences = mContext.getSharedPreferences("login",Context.MODE_PRIVATE)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TypeViewHolder {
        return TypeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.lessons_details, p0, false))
    }

    override fun getItemCount(): Int {
        return trials.size
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        var trial = trials[position] as TrialTest
        holder.studentTextView.text = trial.student.getLamdUser.arabicName
        //TODO first clear all student from server // then uncomment this line
        //holder.vehicleTextView.text = trial.student.vehicle.make
        holder.detailsView.text = trial.details
        holder.statusSpinner.adapter = ArrayAdapter<AppointmentStatus>(mContext, android.R.layout.simple_spinner_item, AppointmentStatus.values())
        holder.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var item = holder.statusSpinner.selectedItem
                print(holder.statusSpinner.selectedItem)
                trials[position].status = holder.statusSpinner.selectedItem as AppointmentStatus
                putTrialTest(sharedPreferences.getString("token","no"),trials[position].id ,trials[position])

            }
        }
    }

    fun getAllItem():Set<TrialTest>{
        return this.trials.toSet()
    }

    private fun putTrialTest(token: String ,trialId :Long ,trialTest: TrialTest){
        this.compositeDisposable.add(ServiceRepo.putTrialTest(token ,trialId ,trialTest).subscribe({
            print(it)
        },{
            print(it)
        }))
    }
}

class GreetingAdapter(private val mContext: Context, private val greetings: List<Greeting>) : RecyclerView.Adapter<GreetingViewHolder>() {
    private val compositeDisposable = CompositeDisposable()
    private val sharedPreferences = mContext.getSharedPreferences("login",Context.MODE_PRIVATE)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GreetingViewHolder {
        return GreetingViewHolder(LayoutInflater.from(mContext).inflate(R.layout.greetings_details, p0, false))
    }

    override fun getItemCount(): Int {
        return greetings.size
    }

    override fun onBindViewHolder(holder: GreetingViewHolder, position: Int) {
        var greeting = greetings[position]
        holder.detailsView.text = greeting.details
        holder.statusSpinner.adapter = ArrayAdapter<AppointmentStatus>(mContext, android.R.layout.simple_spinner_item, AppointmentStatus.values())
        holder.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                greetings[position].status = holder.statusSpinner.selectedItem as AppointmentStatus
                putGreeting(sharedPreferences.getString("token","no"),greetings[position].id ,greetings[position])
            }
        }
    }

    fun getAllItem():Set<Greeting>{
        return this.greetings.toSet()
    }

    private fun putGreeting(token: String ,greetingID:Long , greeting: Greeting){
        this.compositeDisposable.add(ServiceRepo.putGreeting(token ,greetingID ,greeting).subscribe({
            print(it)
        },{
            print(it)
        }))
    }
}


class TypeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var studentTextView = view.findViewById<TextView>(R.id.lessonDetailsStudent)
    var vehicleTextView = view.findViewById<TextView>(R.id.lessonDetailsVehicle)
    var detailsView = view.findViewById<TextView>(R.id.lessonDetailsDet)
    var statusSpinner = view.findViewById<Spinner>(R.id.lessonDetailsSpinner)
}


class GreetingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var detailsView = view.findViewById<TextView>(R.id.greetingDetailsDet)
    var statusSpinner = view.findViewById<Spinner>(R.id.greetingDetailsSpinner)
}

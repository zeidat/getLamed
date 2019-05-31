package com.zeidat.getlamd.repo

import com.getlamd.entity.buisness.*
import com.getlamd.entity.management.Vehicle
import com.getlamd.entity.security.GetLamdUser
import com.getlamd.entity.user.Instructor
import com.getlamd.entity.user.Student
import com.zeidat.getlamd.androidSingle
import com.zeidat.service.ServiceGenerator
import com.zeidat.service.interfaces.API
import com.zeidat.service.models.LoginUser
import io.reactivex.Single
import okhttp3.ResponseBody
import kotlin.collections.ArrayList

object ServiceRepo {

    fun getToken(user:LoginUser):Single<String>
    {
        return ServiceGenerator.createService(API::class.java).getCookie(user)
                .map {
                    it.token
                }.androidSingle()
    }

    fun getAllAppointments(token:String):Single<List<Appointment>>{
        var service = ServiceGenerator.createService(API::class.java)
        return service.getAppointments(token).map {
            DBRepo.insertAppointmentsDB(it)
        }.androidSingle()
    }

    fun postAppointment(appointment:Appointment, token: String):Single<Appointment> {
        var service = ServiceGenerator.createService(API::class.java)
        return service.postAppointment(token,appointment).androidSingle()
    }


    fun postStudent(token:String,getLamdUser: GetLamdUser):Single<Student> {
        val service  = ServiceGenerator.createService(API::class.java)
        return service.postStudent(token,getLamdUser).androidSingle()
    }

    fun getAllStudents(token: String):Single<ArrayList<Student>>{
        val service= ServiceGenerator.createService(API::class.java)
        return service.getStudents(token).androidSingle()
    }

    fun getStudentCard(id:Long,token:String):Single<StudentCard>{
        val service  = ServiceGenerator.createService(API::class.java)
        return service.getStudentCard(token,id).androidSingle()

    }

    fun putStudentCard(id:Long, token:String, card:StudentCard):Single<ResponseBody>{
        val service  = ServiceGenerator.createService(API::class.java)
        return service.putStudentCard(token,id,card).androidSingle()
    }

    fun getInstructorInformation(token:String):Single<Instructor>{
        val service  = ServiceGenerator.createService(API::class.java)
        return service.getInstructorInformation(token).androidSingle()
    }

    fun getAllVehicles(token: String):Single<List<Vehicle>>{
        val service  = ServiceGenerator.createService(API::class.java)
        return service.getVehicles(token).androidSingle()
    }

    fun addNewVehicle(token: String , vehicle: Vehicle):Single<Vehicle> {
        val service  = ServiceGenerator.createService(API::class.java)
        return service.postVehicle(token, vehicle).androidSingle()
    }

    fun getAppointmentById(id:Long , token: String):Single<Appointment> {
        val service  = ServiceGenerator.createService(API::class.java)
        return service.getAppointmentById(token,id).androidSingle()

    }

    fun deleteAppointment(id:Int,token: String ,cancellationReason:String):Single<ResponseBody>{
        val service  = ServiceGenerator.createService(API::class.java)
        return service.deleteAppointment(token ,id, cancellationReason).androidSingle()
    }

    fun putStudent(token: String ,studentId:Long,student: Student):Single<ResponseBody>{
        val service = ServiceGenerator.createService(API::class.java)
        return service.putStudent(token ,studentId ,student).androidSingle()
    }

    fun putLesson(token: String ,lessonId:Long , lesson:Lesson):Single<ResponseBody>{
        val service = ServiceGenerator.createService(API::class.java)
        return service.putLesson(token ,lessonId,lesson).androidSingle()
    }
    fun putTest(token: String ,testId:Long,test: Test):Single<ResponseBody>{
        val service = ServiceGenerator.createService(API::class.java)
        return service.putTest(token ,testId ,test).androidSingle()

    }

    fun putTrialTest(token: String  ,trialID:Long , trialTest: TrialTest):Single<ResponseBody>{
        val service = ServiceGenerator.createService(API::class.java)
        return service.putTrialTest(token ,trialID ,trialTest).androidSingle()

    }

    fun putGreeting(token: String ,greetingId:Long ,greeting: Greeting):Single<ResponseBody>{
        val service = ServiceGenerator.createService(API::class.java)
        return service.putGreeting(token,greetingId ,greeting).androidSingle()
    }



//    fun postVehicles(token: String){
//    val service = com.zeidat.service.ServiceGenerator.createService(API::class.java)
//        return service
//
//    }

}
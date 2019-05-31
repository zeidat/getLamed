package com.zeidat.service.interfaces;


import com.getlamd.entity.buisness.Appointment;
import com.getlamd.entity.buisness.Greeting;
import com.getlamd.entity.buisness.Lesson;
import com.getlamd.entity.buisness.StudentCard;
import com.getlamd.entity.buisness.Test;
import com.getlamd.entity.buisness.TrialTest;
import com.getlamd.entity.management.Vehicle;
import com.getlamd.entity.security.GetLamdUser;
import com.getlamd.entity.security.Token;
import com.getlamd.entity.user.Instructor;
import com.getlamd.entity.user.Student;
import com.zeidat.service.models.LoginUser;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

    @POST("auth")
    Single<Token> getCookie(@Body LoginUser user) ;

    @GET("appointment")
    Single<List<Appointment>> getAppointments(@Header("Authorization") String token);

    @GET("student")
    Single<ArrayList<Student>> getStudents(@Header("Authorization") String token);

    @GET("Appointments/{Appointment_id}")
    Single<Appointment> getAppointmentById(@Header("Cookie") String token,@Path("Appointment_id") long id);

    @GET("vehicle")
    Single<List<Vehicle>> getVehicles(@Header("Authorization")String token);

    @POST("appointment")
    Single<Appointment> postAppointment(@Header("Authorization")String token , @Body Appointment appointment);

    @POST("student")
    Single<Student> postStudent(@Header("Authorization")String token, @Body GetLamdUser getLamdUser);

    @POST("vehicle")
    Single<Vehicle> postVehicle(@Header("Authorization")String token, @Body Vehicle vehicle);

    @GET("student/card/{student_id}")
    Single<StudentCard> getStudentCard(@Header("Authorization")String token, @Path("student_id") Long id);

    @PUT("student/card/{student_id}")
    Single<ResponseBody> putStudentCard(@Header("Authorization")String token, @Path("student_id") Long id , @Body StudentCard card);

    @GET("system/instructor")
    Single<Instructor> getInstructorInformation(@Header("Authorization") String token) ;

    @DELETE("appointment/{appointment_id}")
    Single<ResponseBody> deleteAppointment(@Header("Authorization")String token, @Path("appointment_id") int id, @Query("cancellationReason")String cancellationReason);

    @PUT("student/{student_id}")
    Single<ResponseBody> putStudent(@Header("Authorization") String token ,@Path("student_id") long id
            ,@Body Student student) ;

    @PUT("appointment/lesson/{lesson_id}")
    Single<ResponseBody> putLesson(@Header("Authorization") String token , @Path("lesson_id")long lessonID  , @Body Lesson lesson) ;

    @PUT("appointment/test/{test_id}")
    Single<ResponseBody> putTest(@Header("Authorization") String token , @Path("test_id") long testID , @Body Test test) ;

    @PUT("appointment/greeting/{greeting_id}")
    Single<ResponseBody> putGreeting(@Header("Authorization")String token , @Path("greeting_id") long greetingId , @Body Greeting greeting) ;

    @PUT("appointment/trialtest/{trial_test}")
    Single<ResponseBody> putTrialTest(@Header("Authorization")String token ,@Path("trail_test_id") long trialTestID , @Body TrialTest trialTest);




}




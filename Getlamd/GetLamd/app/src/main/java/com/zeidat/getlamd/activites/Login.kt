package com.zeidat.getlamd.activites

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.zeidat.getlamd.BaseActivity
import com.zeidat.getlamd.R
import com.zeidat.getlamd.repo.ServiceRepo
import com.zeidat.service.models.LoginUser
import android.net.NetworkInfo
import android.net.ConnectivityManager
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class Login : BaseActivity() {
    private var userID: EditText? = null
    private var password: EditText? = null
    private var login: Button? = null
    private var bar: ProgressBar? = null
    private var editor: SharedPreferences.Editor? = null

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private var compositeDisposable:CompositeDisposable? = null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.loginTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //provide app component
        getApplicationCustomComponent().inject(this)
        this.compositeDisposable = CompositeDisposable()

        /**
         * initialize components (views)
         *
         */
        this.userID = findViewById<EditText>(R.id.loginID)
        this.password = findViewById<EditText>(R.id.loginPassword)
        this.login = findViewById<Button>(R.id.loginButtonLogin)
        this.bar = findViewById<ProgressBar>(R.id.loginBar)

        this.bar!!.visibility = View.INVISIBLE
        /**
         * check if cookie saved
         */

        this.editor = this.sharedPreferences!!.edit()
        if (this.sharedPreferences!!.getString("token", "no") != "no") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        /**
         * if user click login in he the token will be generated
         */
        login!!.setOnClickListener {
            if (!isNetworkOnline()) {
                Toast.makeText(this, "Please Check your Internet", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            bar!!.visibility = View.VISIBLE
            this.compositeDisposable!!.add(ServiceRepo.getToken(LoginUser(userID!!.text.toString(), password!!.text.toString()))
                    .subscribe({ response ->
                        var token = "Bearer ".plus(response)
                        this.editor!!.putString("token", token).apply()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }, {
                        print(it.localizedMessage)
                        bar!!.visibility = View.INVISIBLE
                        Toast.makeText(this, "check Your Internet Connection", Toast.LENGTH_LONG).show()
                    }))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        this.compositeDisposable!!.dispose()
    }

    /**
     * check if wifi is connected
     */
    private fun isNetworkOnline(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected!!
    }
}

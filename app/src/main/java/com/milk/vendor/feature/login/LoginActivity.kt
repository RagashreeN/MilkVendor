package com.milk.vendor.feature.login

import android.content.Intent
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.milk.vendor.R
import com.milk.vendor.feature.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginPresenter.LoginView{


    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1000

    private var loginPresenter : LoginPresenter = LoginPresenter(this)

    private val sharedPrefFile = "Loginsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initVars()
    }

    private fun initVars(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("352982382191-otv1k6of6fnrq2hchj6eflcnp5vg9s7j.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        ll_google_signin_button.setOnClickListener {
            signIn()
        }

    }

    private fun signIn() {
        progressbar_login.visibility = View.VISIBLE
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, RC_SIGN_IN
        )
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                // Update your UI here
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                ApiException::class.java
            )
            // Signed in successfully
            val googleId = account?.id ?: ""
            val googleFirstName = account?.givenName ?: ""
            val googleEmail = account?.email ?: ""
            progressbar_login.visibility = View.GONE
            signOut()
            var intent : Intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
            //showMessage("User Logged in successfully")
            //loginPresenter.doLogin(googleEmail,googleFirstName)
            //loginPresenter.doLogin("karthikns@iglulabs.com","Karthick")
        } catch (e: ApiException) {
            signOut()
            // Sign in was unsuccessful
            showMessage("failed code="+e.statusCode.toString())
        }
    }

    /*override fun showLoginResponse(responose: LoginUserModel) {
        //snack("Response: "+responose.message,this)

        var sharedPref : SharedPref = SharedPref(this)
        sharedPref.addAuthToken(responose.auth_token)
        sharedPref.addUsername(responose.data[0].first_name)
        sharedPref.addUserGroup(responose.data[0].group_id)

        if(responose.data[0].group_id=="1"){
            //startActivity(Intent(this, AdminHomeActivity::class.java))
            var intent : Intent = Intent(this, AdminHomeActivity::class.java)
            intent.putExtra("isFromAddProjectSuccess",false)
            startActivity(intent)
            finish()
        }else{
            startActivity(Intent(this, UserHomeActivity::class.java))
            finish()
        }

    }*/

    override fun hideShowProgressbar(visible: Boolean) {
        /*if(visible){ progressbar_login.visibility = View.VISIBLE }
        else{progressbar_login.visibility = View.GONE}*/
    }

    override fun showMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG)
    }
}
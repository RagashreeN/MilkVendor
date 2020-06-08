package com.milk.vendor.feature.login

class LoginPresenter (private val loginView: LoginPresenter.LoginView){


    interface LoginView{
        fun hideShowProgressbar(boolean: Boolean)
        fun showMessage(message : String)
    }
}
package org.classapp.whatsup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView

class LoginViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_view)

        val signInBtn:Button = findViewById(R.id.signInBtn)
        val signInPanel:CardView = findViewById(R.id.signInPanel)
        val usernameText:EditText = findViewById(R.id.usernameTxt)
        val pwdText:EditText = findViewById(R.id.pwdTxt)
        val enterBtn:Button = findViewById(R.id.enterBtn)

        signInBtn.setOnClickListener {
            if (signInPanel.visibility == View.VISIBLE) {
                signInPanel.visibility = View.GONE
            } else {
                signInPanel.visibility = View.VISIBLE
            }
        }

        var username:String; var password:String
        enterBtn.setOnClickListener {
            username = usernameText.text.toString()
            password = pwdText.text.toString()
            if (username.equals("admin") && password.equals("admin")) {
                Toast.makeText(this, "Welcome to WhatUp!", Toast.LENGTH_LONG).show()
                usernameText.setText("")
                pwdText.setText("")
                signInPanel.visibility = View.GONE

                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
            } else {
                Toast.makeText(this, "Try Again!", Toast.LENGTH_LONG).show()
            }

        }

    }
}
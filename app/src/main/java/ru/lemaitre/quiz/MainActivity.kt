package ru.lemaitre.quiz

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sign_up_layout.*
import ru.lemaitre.quiz.common.Common
import ru.lemaitre.quiz.model.User

class MainActivity : AppCompatActivity() {
    lateinit var users: DatabaseReference

    //sign Up
    lateinit var edtNewUserName: MaterialEditText
    lateinit var edtNewEmail: MaterialEditText
    lateinit var edtNewPassword: MaterialEditText

    //sign in
    lateinit var edtUser: MaterialEditText
    lateinit var edtPassword: MaterialEditText

    //Btn
    lateinit var btnSignUp: Button
    lateinit var btnSignIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Firebase
        val database = FirebaseDatabase.getInstance()
        users = database.getReference("Users")

        btnSignUp = btn_sign_up
        btnSignUp.setOnClickListener {
            showSignUpDialog()
        }

        btnSignIn = btn_sign_in
        edtUser = findViewById(R.id.edtUser)
        edtPassword = findViewById(R.id.edtPassword)

        btnSignIn.setOnClickListener {
            signIn(edtUser.text.toString(), edtPassword.text.toString())
        }
    }

    private fun signIn(user: String, pwd: String) {
        users.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                if (p0 != null) {
                    if (p0.child(user).exists()) {
                        if (!user.isEmpty()) {

                            val login = p0.child(user).getValue(User::class.java)
                            if (login.password.equals(pwd)) {

                                val homeActivity = Intent(this@MainActivity, Home::class.java)
                                Common.currentUser = login
                                startActivity(homeActivity)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Wrong password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }else{
                            Toast.makeText(
                                this@MainActivity,
                                "Please enter yor user name",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }else{
                        Toast.makeText(
                            this@MainActivity,
                            "User is not exists !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }

            override fun onCancelled(p0: DatabaseError?) {
            }
        })
    }

    private fun showSignUpDialog() {
        val alertDialog = AlertDialog.Builder(MainActivity@ this)
        alertDialog.setTitle("Sign Up")
        alertDialog.setMessage("Please fill full information")

        val inflater = this.layoutInflater
        val signUpLayout = inflater.inflate(R.layout.sign_up_layout, null)

        edtNewUserName = signUpLayout.findViewById(R.id.edtNewUserName)
        edtNewEmail = signUpLayout.findViewById(R.id.edtNewEmail)
        edtNewPassword = signUpLayout.findViewById(R.id.edtNewPassword)

        alertDialog.setView(signUpLayout)
        alertDialog.setIcon(R.drawable.ic_baseline_account_circle_24)

        alertDialog.setNegativeButton("NO", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.dismiss()
            }
        })

        alertDialog.setPositiveButton("YES", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val user = User(
                    edtNewUserName.text.toString(),
                    edtNewPassword.text.toString(),
                    edtNewEmail.text.toString()
                )

                users.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot?) {
                        if (p0?.child(user.userName)?.exists()!!) {
                            Toast.makeText(
                                this@MainActivity,
                                "User already exists!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            users.child(user.userName).setValue(user)
                            Toast.makeText(
                                this@MainActivity,
                                "User reistration success",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(p0: DatabaseError?) {

                    }

                })
                dialog?.dismiss()
            }
        })
        alertDialog.show()
    }


}
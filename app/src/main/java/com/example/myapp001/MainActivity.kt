package com.example.myapp001

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val TAG = "MainActivity"
        var IDText: String
        var InfoText: String
        var pref = getSharedPreferences("pref",0)
        var editor = pref.edit()

        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()


        btnLOGIN.setOnClickListener {

            db.collection("UserData")
                .get()
                .addOnSuccessListener { result ->

                    for (document in result) {
                        if(editTextID.text.toString() ==  document.data.get("ID")){
                            Log.d(TAG, "아이디 일치")
                            if(editTextINFO.text.toString() == document.data.get("Password")){
                                Log.d(TAG, "비밀번호 일치")

                                //로컬에 저
                                editor.putString("IDTEXT",editTextID.text.toString())
                                editor.putString("INFOTEXT",editTextINFO.text.toString())
                                editor.apply()

                                Log.d(TAG, "Pref : " + pref.getString("IDTEXT","")+ " " + pref.getString("INFOTEXT","") )

                                val nextIntent = Intent(this, MainPage::class.java)
                                intent.putExtra("ID",editTextID.text.toString())
                                startActivity(nextIntent)
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
        }
    }
}
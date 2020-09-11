package com.example.myapp001.ui.notifications

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.myapp001.R
import com.example.myapp001.ui.home.HomeFragment
import com.google.firebase.firestore.FirebaseFirestore

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private val TAG = "HomeFragment"
    var array: Array<String> = Array(255,{i->""})
    val pref = activity?.getSharedPreferences("pref",0)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        val listVieqw : ListView = root.findViewById(R.id.listviewMyProd)
        val db = FirebaseFirestore.getInstance()

        db.collection("TableData")
            .get()
            .addOnSuccessListener { result ->
                Log.d(TAG, "크기 ${result.documents.size} : ${result.documents.get(0).get("Title")}, ${result.documents.get(1).get("Title")}")

                var j = 0
                for(i in 0..result.documents.size-1){

                    Log.d(TAG, "로그인 이름 : " + pref?.getString("IDTEXT","") +" 게시글 작성자 : ${result.documents.get(i).get("Writer")}")
                    if(pref?.getString("IDTEXT","") == "${result.documents.get(i).get("Writer")}"){
                        array.set(j,"${result.documents.get(i).get("Title")}")
                        j++
                    }
                }


                listVieqw.adapter = this.context?.let { MyAdapoter(it, array) }

                listVieqw.setOnItemClickListener { adapterView, view, i, l ->
                    val getitem = adapterView.getItemAtPosition(i)
                    val writer = result.documents.get(i).get("Writer")
                    Log.d(TAG, "아이템 클릭 : " + getitem + " 작성자 : "+ writer)
                }

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }


        return root
    }

    private class MyAdapoter(context: Context, array: Array<String>) : BaseAdapter() {
        private val mContext: Context
        private val TAG = "HomeFragment_Adapter"
        private val marray: Array<String>

        init {
            mContext = context
            marray = array
        }

        override fun getCount(): Int {
            var i = 0
            var countNum = 0

            for (i in 0..marray.size) {
                if (marray.get(i).length == 0) {
                    break
                }
                countNum++
            }

            return countNum
        }

        override fun getItem(p0: Int): Any {

            var title = marray.get(p0)

            return title
        }

        override fun getItemId(p0: Int): Long {

            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.rowlayout, p2, false)

            val image: ImageView = rowMain.findViewById(R.id.imageView2)
            val titleText: TextView = rowMain.findViewById(R.id.textView)

            image.setImageResource(R.drawable.googleg_standard_color_18)
            titleText.text = marray.get(p0)


            return rowMain
        }
    }
}
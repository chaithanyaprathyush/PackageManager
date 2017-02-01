package com.chaithanyaprathyush.packagemanager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.*

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val relativeLayout = RelativeLayout(this)
        relativeLayout.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)



    }


    private inner class ExampleAdapter: BaseAdapter() {

        var exampleData: ArrayList<String>? = null

        override fun getCount(): Int {
            return exampleData?.size ?: 0
        }

        override fun getItem(p0: Int): String {
            return exampleData?.get(p0) ?: ""
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

            val view = TextView(this@MainActivity)
            view.text = getItem(p0)
            return view
        }
    }

}

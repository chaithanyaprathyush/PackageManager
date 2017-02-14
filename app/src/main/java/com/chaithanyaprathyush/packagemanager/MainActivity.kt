package com.chaithanyaprathyush.packagemanager

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.database.DataSetObservable
import android.database.DataSetObserver
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.*

data class Application(val application: ApplicationInfo, val name: String, var uid: Int)

class MainActivity: AppCompatActivity(), ListAdapter {
        var arrayList = ArrayList<Application>()
        var applicationManager: PackageManager? = null
        var applicationListView: ListView? = null
        private val mDataSetObservable = DataSetObservable()
    // Activity Overrides.
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            applicationManager = this.packageManager
            applicationListView = ListView(this)
            applicationListView?.adapter = this
            setContentView(applicationListView)
            Thread(Runnable { getAppsList() }).start()
        }
        // Adapter Overrides.
        override fun isEmpty(): Boolean { return arrayList.isEmpty() }

        override fun getCount(): Int { return arrayList.count() }

        override fun hasStableIds(): Boolean { return true }

        override fun getItem(p0: Int): Application { return arrayList[p0] }

        override fun getItemId(p0: Int): Long { return arrayList[p0].uid.toLong() }

        override fun getViewTypeCount(): Int {  return 1 }

        override fun getItemViewType(p0: Int): Int { return 0 }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View? {
            // Obtain the item associated with the position of this row in the list
            // Create a view to show the data associated with the item.

            if (p1 is TextView) {
                Log.i("Adapter", "Reused A View")
                p1.text = getItem(p0).name
                return p1
            } else {
                Log.i("Adapter", "Created A View")
                val textView = TextView(this)
                textView.setPadding(25, 25, 25, 25)
                textView.text = getItem(p0).name
                return textView
            }
        }

        override fun registerDataSetObserver(p0: DataSetObserver?) {
            mDataSetObservable.registerObserver(p0)
        }

        override fun unregisterDataSetObserver(p0: DataSetObserver?) {
            mDataSetObservable.unregisterObserver(p0)
        }

        override fun areAllItemsEnabled(): Boolean { return true }

        // List Adapter Overrides
        override fun isEnabled(p0: Int): Boolean { return true }

        private fun getAppsList() {
            val applicationList =
                    applicationManager?.getInstalledApplications(PackageManager.GET_META_DATA)
            arrayList = applicationList?.map {
                val applicationName = applicationManager?.getApplicationLabel(it).toString()
                Log.i("Application Info", "Application Name: $applicationName, Package Name: ${it.packageName}")
                Application(it, applicationName, it.uid)
            } as? ArrayList<Application> ?: ArrayList<Application>()
            runOnUiThread { mDataSetObservable.notifyChanged() }
        }
}
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

data class Application(val application: ApplicationInfo, val name: String, val isSystemApp: Boolean, var uid: Int)

class MainActivity: AppCompatActivity(), ListAdapter {

        private lateinit var applicationManager: PackageManager
        private lateinit var applicationListView: ListView
        private var applicationList: List<Application> = emptyList()
        private val dataSetObservable = DataSetObservable()

    // Activity Overrides.
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            applicationManager = this.packageManager
            applicationListView = ListView(this)
            applicationListView.adapter = this
            setContentView(applicationListView)
            Thread(Runnable { getAppsList() }).start()
        }
        // Adapter Overrides.
        override fun isEmpty() = applicationList.isEmpty()

        override fun getCount(): Int = applicationList.count()

        override fun hasStableIds() = true

        override fun getItem(p0: Int) = applicationList[p0]

        override fun getItemId(p0: Int) =  applicationList[p0].uid.toLong()

        override fun getViewTypeCount() = 1

        override fun getItemViewType(p0: Int) = 0

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View? {
            // Obtain the item associated with the position of this row in the list
            // Create a view to show the data associated with the item.
            val application = getItem(p0)
            val isSystemApp = if (application.isSystemApp) "${application.name} is a system app"
                                            else "${application.name} is not a system app"
            if (p1 is TextView) {
                Log.i("Adapter", "Reused A View")
                p1.text = isSystemApp
                return p1
            } else {
                Log.i("Adapter", "Created A View")
                val textView = TextView(this)
                textView.setPadding(25, 25, 25, 25)
                textView.text = isSystemApp
                return textView
            }
        }

        override fun registerDataSetObserver(p0: DataSetObserver?) = dataSetObservable.
                                                                            registerObserver(p0)

        override fun unregisterDataSetObserver(p0: DataSetObserver?) = dataSetObservable.
                                                                            unregisterObserver(p0)

        override fun areAllItemsEnabled() = true

        // List Adapter Overrides
        override fun isEnabled(p0: Int) = true

        private fun getAppsList() {
            val applicationData =
                    applicationManager.getInstalledApplications(PackageManager.GET_META_DATA)
             applicationList = applicationData?.map {
                val applicationName = applicationManager.getApplicationLabel(it).toString()
                Log.i("Application Info", "Application Name: $applicationName, Package Name: ${it.packageName}")
                if (it.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
                    Log.i("Application Info", "$applicationName is system app")
                    Application(it, applicationName, true, it.uid)
                } else {
                    Log.i("Application Info", "$applicationName is not a system app")
                    Application(it, applicationName, false, it.uid)
                }
            } ?: emptyList()
            runOnUiThread { dataSetObservable.notifyChanged() }
        }
}
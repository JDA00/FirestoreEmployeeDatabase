package com.jda00.android.firestoreemployeedatabase

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot


data class Employee(
    val id: String,
    val name: String,
    val position: String,
//    val number: String,
//    val imageUrl: String
) {


//    companion object {
//        fun DocumentSnapshot.toEmployee(): Employee? {
//            try {
//                val name = getString("name")!!
//                val position = getString("position")!!
//                val contactNumber = getString("contact_number")!!
//                val imageUrl = getString("employee_picture")!!
//                return Employee(id, name, position)
//
//            } catch (e: Exception) {
//                Log.e(TAG, "Error converting user profile", e)
//                FirebaseCrashlytics.getInstance().log("Error converting user profile")
//                FirebaseCrashlytics.getInstance().setCustomKey("userId", id)
//                FirebaseCrashlytics.getInstance().recordException(e)
//                return null
//            }
//        }
//
//        private const val TAG = "Employee"
//    }
}

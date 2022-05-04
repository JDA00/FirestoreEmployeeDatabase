package com.jda00.android.firestoreemployeedatabase.repo

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.jda00.android.firestoreemployeedatabase.Employee
import com.jda00.android.firestoreemployeedatabase.Response.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@ExperimentalCoroutinesApi
class RepoUtility @Inject constructor(
    private val employeeRef: CollectionReference,
    private val employeeQuery: Query,
) : EmployeeRepo {
    override fun getEmployee() = callbackFlow {
        val snapshotListener = employeeQuery.addSnapshotListener { snapshot, e ->
            val response = if (snapshot != null) {
                val employee = snapshot.toObjects(Employee::class.java)
                Success(employee)
            } else {
                Error(e?.message ?: e.toString())
            }
            trySend(response).isSuccess
        }
        awaitClose {
            snapshotListener.remove()
        }
    }


    override suspend fun addEmployee(name: String, position: String) = flow {
        try {
            emit(Loading)
            val employeeId = employeeRef.document().id
            val employee = Employee(
                id = employeeId,
                name = name,
                position = position

            )
            val addition = employeeRef.document(employeeId).set(employee).await()
            emit(Success(addition))
        } catch (e: Exception) {
            emit(Error(e.message ?: e.toString()))
        }
    }

    override suspend fun deleteEmployee(employeeId: String) = flow {
        try {
            emit(Loading)
            val deletion = employeeRef.document(employeeId).delete().await()
            emit(Success(deletion))
        } catch (e: Exception) {
            emit(Error(e.message ?: e.toString()))
        }
    }
}
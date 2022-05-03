package com.jda00.android.firestoreemployeedatabase.repo

import kotlinx.coroutines.flow.Flow
import com.jda00.android.firestoreemployeedatabase.Employee
import com.jda00.android.firestoreemployeedatabase.Response


interface EmployeeRepo {
    fun getEmployee(): Flow<Response<List<Employee>>>

    suspend fun addEmployee(name: String, position: String): Flow<Response<Void?>>

    suspend fun deleteEmployee(id: String): Flow<Response<Void?>>
}
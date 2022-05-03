package com.jda00.android.firestoreemployeedatabase

import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jda00.android.firestoreemployeedatabase.repo.EmployeeRepo
import com.jda00.android.firestoreemployeedatabase.repo.RepoUtility
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@ExperimentalCoroutinesApi
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun firebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides
    fun employeeRef(db: FirebaseFirestore) = db.collection("employees")

    @Provides
    fun employeeQuery(employeeRef: CollectionReference) = employeeRef.orderBy("name")

    @Provides
    fun employeeRepository(
        employeeRef: CollectionReference,
        employeeQuery: Query
    ): EmployeeRepo = RepoUtility(employeeRef, employeeQuery)

    @Provides
    fun provideCRUDUtils(repository: EmployeeRepo) = CRUDUtils(
        getEmployee = GetEmployee(repository),
        addEmployee = AddEmployee(repository),
        deleteEmployee = DeleteEmployee(repository)
    )
}
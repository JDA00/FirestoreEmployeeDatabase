package com.jda00.android.firestoreemployeedatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jda00.android.firestoreemployeedatabase.repo.EmployeeRepo
import com.jda00.android.firestoreemployeedatabase.repo.RepoUtility

import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@ExperimentalCoroutinesApi
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides
    fun provideEmployeeRef(db: FirebaseFirestore) = db.collection("employees")

    @Provides
    fun provideEmployeeQuery(employeeRef: CollectionReference) = employeeRef.orderBy("name")

    @Provides
    fun provideEmployeeRepository(
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
package com.jda00.android.firestoreemployeedatabase

import com.jda00.android.firestoreemployeedatabase.repo.EmployeeRepo


class GetEmployee(
    private val repository: EmployeeRepo
) {
    operator fun invoke() = repository.getEmployee()
}

class AddEmployee(
    private val repository: EmployeeRepo
) {
    suspend operator fun invoke(
        name: String,
        position: String,
    ) = repository.addEmployee(name, position)
}

class DeleteEmployee(
    private val repository: EmployeeRepo
) {
    suspend operator fun invoke(employeeId: String) = repository.deleteEmployee(employeeId)
}

data class CRUDUtils (
    val getEmployee: GetEmployee,
    val addEmployee: AddEmployee,
    val deleteEmployee: DeleteEmployee
)
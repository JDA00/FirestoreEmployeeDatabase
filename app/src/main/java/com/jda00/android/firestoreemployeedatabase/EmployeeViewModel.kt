package com.jda00.android.firestoreemployeedatabase


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

import javax.inject.Inject
import com.jda00.android.firestoreemployeedatabase.repo.EmployeeRepo

@HiltViewModel
class EmployeeViewModel @Inject constructor(

    private val addEmployee: AddEmployee,
    private val getEmployee: GetEmployee,
    private val deleteEmployee: DeleteEmployee


) : ViewModel() {
    private val _employeeState = mutableStateOf<Response<List<Employee>>>(Response.Loading)
    val employeeState: State<Response<List<Employee>>> = _employeeState

    private val _isEmployeeAddedState = mutableStateOf<Response<Void?>>(Response.Success(null))
    val isEmployeeAddedState: State<Response<Void?>> = _isEmployeeAddedState
    var openDialogState = mutableStateOf(false)

    private val _isEmployeeDeletedState = mutableStateOf<Response<Void?>>(Response.Success(null))
    val isEmployeeDeletedState: State<Response<Void?>> = _isEmployeeDeletedState

    init {
        getEmployees()
    }

    private fun getEmployees() {
        viewModelScope.launch {
            getEmployee().collect { response ->
                _employeeState.value = response
            }
        }
    }

    fun addEmployees(name: String, position: String) {
        viewModelScope.launch {
            addEmployee(name, position).collect { response ->
                _isEmployeeAddedState.value = response
            }
        }
    }

    fun deleteEmployees(employeeId: String) {
        viewModelScope.launch {
            deleteEmployee(employeeId).collect { response ->
                _isEmployeeDeletedState.value = response
            }
        }
    }
}

//TODO: Move these out of ViewModel

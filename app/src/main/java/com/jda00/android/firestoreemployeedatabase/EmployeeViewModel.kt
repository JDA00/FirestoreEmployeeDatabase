package com.jda00.android.firestoreemployeedatabase


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EmployeeViewModel @Inject constructor(

    private val crudUtils : CRUDUtils


) : ViewModel() {
    private val _employeeState = mutableStateOf<Response<List<Employee>>>(Response.Loading)
    val employeeState: State<Response<List<Employee>>> = _employeeState

    private val _isEmployeeAddedState = mutableStateOf<Response<Void?>>(Response.Success(null))
    val isEmployeeAddedState: State<Response<Void?>> = _isEmployeeAddedState
    var openDialogState = mutableStateOf(false)

    private val _isEmployeeDeletedState = mutableStateOf<Response<Void?>>(Response.Success(null))
    val isEmployeeDeletedState: State<Response<Void?>> = _isEmployeeDeletedState

    init {
        getEmployee()
    }

    private fun getEmployee() {
        viewModelScope.launch {
            crudUtils.getEmployee().collect { response ->
                _employeeState.value = response
            }
        }
    }

    fun addEmployee(name: String, position: String) {
        viewModelScope.launch {
           crudUtils.addEmployee(name, position).collect { response ->
                _isEmployeeAddedState.value = response
            }
        }
    }

    fun deleteEmployee(employeeId: String) {
        viewModelScope.launch {
           crudUtils.deleteEmployee(employeeId).collect { response ->
                _isEmployeeDeletedState.value = response
            }
        }
    }
}


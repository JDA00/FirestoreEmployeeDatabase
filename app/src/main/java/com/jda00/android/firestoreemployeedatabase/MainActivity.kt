package com.jda00.android.firestoreemployeedatabase

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jda00.android.firestoreemployeedatabase.ui.theme.FirestoreEmployeeDatabaseTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import java.lang.IllegalStateException
import com.jda00.android.firestoreemployeedatabase.Response.*
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.job


@AndroidEntryPoint
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            EmployeeScreen()

        }
    }
}


@Composable
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun EmployeeScreen(
    viewModel: EmployeeViewModel = hiltViewModel()
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.openDialogState.value = true
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add an Employee"
                )
            }
        }
    ) {
        if (viewModel.openDialogState.value) {
            AddEmployeeDialog()
        }
        when (val employeeResponse = viewModel.employeeState.value) {
            is Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            is Success -> Box(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn {
                    items(
                        items = employeeResponse.data
                    ) { employee ->
                        EmployeeCard(
                            employee = employee
                        )
                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (val additionResponse = viewModel.isEmployeeAddedState.value) {
                        is Loading -> CircularProgressIndicator()
                        is Success -> Unit
                        is Error -> printError(additionResponse.message)
                    }
                }
            }
            is Error -> printError(employeeResponse.message)
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val deletionResponse = viewModel.isEmployeeDeletedState.value) {
                is Loading -> CircularProgressIndicator()
                is Success -> Unit
                is Error -> printError(deletionResponse.message)
            }
        }
    }
}

@Composable
@InternalCoroutinesApi
fun AddEmployeeDialog(
    viewModel: EmployeeViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    val focusRequester = FocusRequester()

    if (viewModel.openDialogState.value) {
        AlertDialog(
            onDismissRequest = {
                viewModel.openDialogState.value = false
            },
            title = {
                Text(
                    text = "Add Employee"
                )
            },
            text = {
                Column {
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = {
                            Text(
                                text = "Employee Name"
                            )
                        },
                        modifier = Modifier.focusRequester(focusRequester)
                    )
                    LaunchedEffect(Unit) {
                        coroutineContext.job.invokeOnCompletion {
                            focusRequester.requestFocus()
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                    TextField(
                        value = position,
                        onValueChange = { position = it },
                        placeholder = {
                            Text(
                                text = "Position"
                            )
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.openDialogState.value = false
                        viewModel.addEmployee(name, position)
                    }
                ) {
                    Text(
                        text = "Add"
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.openDialogState.value = false
                    }
                ) {
                    Text(
                        text = "Dismiss"
                    )
                }
            }
        )
    }
}

@Composable
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun EmployeeCard(
    employee: Employee,
    viewModel: EmployeeViewModel = hiltViewModel()
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(
                start = 12.dp,
                end = 12.dp,
                top = 8.dp,
                bottom = 8.dp
            )
            .fillMaxWidth(),
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.90f)
            ) {
                employee.name?.let { name ->
                    Text(
                        text = name,
                        color = Color.DarkGray,
                        fontSize = 20.sp
                    )
                }
                employee.position?.let { position ->
                    Text(
                        text = position,
                        color = Color.DarkGray,
                        fontSize = 16.sp,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
            IconButton(
                onClick = {
                    employee.id?.let { employeeId ->
                        viewModel.deleteEmployee(employeeId)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Employee",
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}

fun printError(message: String) {
    Log.d("APP ERROR", message)
}


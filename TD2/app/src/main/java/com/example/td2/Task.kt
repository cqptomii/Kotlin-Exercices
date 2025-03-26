package com.example.td2

import android.annotation.SuppressLint
import android.app.ActivityManager.TaskDescription
import android.app.DatePickerDialog
import android.view.RoundedCorner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.nio.file.WatchEvent
import java.util.Calendar
import java.util.Date

data class Task(
    val title:String,
    val description: String,
    val isCompleted:MutableState<Boolean> = mutableStateOf(false),
    val dueDate: String
)

@Composable
fun TaskListScreen(){
    val tasks = remember {
        mutableStateListOf<Task>(
        )
    }
    var taskTitle = remember { mutableStateOf("") }
    var taskDesc = remember { mutableStateOf("") }
    var taskDueDate = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
        .padding(top = 40.dp)
        .padding(10.dp)
    ){
        Text("TO DO LIST", fontWeight = FontWeight.Bold, fontSize = 30.sp, modifier = Modifier.padding(10.dp))


        Box(modifier = Modifier.padding(10.dp).background(color = Color.White, shape = RoundedCornerShape(10.dp))){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Add a task in the list",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 20.dp).padding(10.dp)
                )

                TextField(
                    value = taskTitle.value,
                    onValueChange = { taskTitle.value = it },
                    label = { Text("Task title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                TextField(
                    value = taskDesc.value,
                    onValueChange = { taskDesc.value = it },
                    label = { Text("Task description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                FirstDatePicker(taskDueDate)

                Button(
                    onClick = {
                        AddTask(taskTitle.value, taskDesc.value,taskDueDate.value, tasks)
                        taskTitle.value = ""
                        taskDesc.value = ""
                        taskDueDate.value = ""
                              },
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.6f)
                        .padding(10.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    content = {Text("Create Task")},
                    enabled = taskTitle.value != "",
                )
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(9.dp))
        {
            items(tasks){
                task -> TaskItem(task)
            }
        }
    }

}
@Composable
fun FirstDatePicker(date: MutableState<String>){
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            date.value = "$selectedDay/${selectedMonth + 1}/$selectedYear"
        },
        year, month, day
    )
    Text(text = date.value, fontSize = 18.sp)

    Button(onClick = { datePickerDialog.show() }) {
        Text("Choisir une date")
    }
}

fun AddTask(title: String, desc: String,dueDate : String, tasks : MutableList<Task>){
    var newTask = Task(title,desc,mutableStateOf(false),dueDate)
    tasks.add(newTask)
}

@Composable
fun TaskItem(task:Task){

    Row(modifier = Modifier.background(shape = RoundedCornerShape(8.dp),
        color = Color.White
    )
        .fillMaxWidth()
        .padding(3.dp)
        .height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
        Checkbox(
            checked = task.isCompleted.value,
            onCheckedChange = {
                isChecked -> task.isCompleted.value = isChecked
            }
        )
        Column (modifier = Modifier.fillMaxWidth(0.7f).padding(0.5.dp),
            verticalArrangement = Arrangement.Center
        ){
            Text(task.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(task.description)

        }
        Text(text = task.dueDate, fontWeight = FontWeight.Bold, fontSize = 16.sp)

    }
}
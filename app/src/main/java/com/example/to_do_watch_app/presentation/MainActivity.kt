/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.to_do_watch_app.presentation
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Scaffold
import androidx.wear.tiles.material.Text
import com.example.to_do_watch_app.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainActivityContent(viewModel = viewModel, context = this)
        }
    }
}

@Composable
fun MainActivityContent(viewModel: MainViewModel,context: Context) {
    val alarmTime by viewModel.alarmTime.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Wear OS Alarm") }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(
                    selectedHour = alarmTime?.first ?: 9,
                    selectedMinute = alarmTime?.second ?: 0,
                    onTimeSelected = { hour, minute ->
                        viewModel.setAlarmTime(hour, minute)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                NumberPicker(
                    value = alarmTime?.first ?: 9,
                    onValueChange = { hour ->
                        viewModel.setAlarmTime(hour, alarmTime?.second ?: 0)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                NumberPicker(
                    value = alarmTime?.second ?: 0,
                    onValueChange = { minute ->
                        viewModel.setAlarmTime(alarmTime?.first ?: 9, minute)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.setAlarm(context) },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Set Alarm")
                }
            }
        }
    )
}

@Composable
fun TimePicker(
    selectedHour: Int,
    selectedMinute: Int,
    onTimeSelected: (Int, Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        NumberPicker(
            value = selectedHour,
            onValueChange = { hour ->
                onTimeSelected(hour, selectedMinute)
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
        NumberPicker(
            value = selectedMinute,
            onValueChange = { minute ->
                onTimeSelected(selectedHour, minute)
            }
        )
    }
}

@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit
) {
    val numbers = (0..59).toList()

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (number in numbers) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .clickable {
                        onValueChange(number)
                    }
                    .selectable(
                        selected = (number == value),
                        onClick = { onValueChange(number) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    color = if (number == value) Color.White else Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
//    MainActivityContent(viewModel = MainViewModel(), context = Context)
}

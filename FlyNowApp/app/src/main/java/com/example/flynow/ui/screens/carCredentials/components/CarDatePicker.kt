package com.example.flynow.ui.screens.carCredentials.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flynow.R
import com.example.flynow.ui.SharedViewModel
import com.example.flynow.ui.screens.carCredentials.CarCredentialsViewModel
import com.example.flynow.utils.Converters

//component that creates the calendar for the car date selection for pick up and return
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDatePicker(
    carCredentialsViewModel: CarCredentialsViewModel,
    sharedViewModel: SharedViewModel,
    pickUpOrReturn: Int,
    onSelectedDate: (String) -> Unit,
    onDismiss: () -> Unit
) {

    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val utcDate = Converters.convertMillisToDate(utcTimeMillis)
                val currentDate = Converters.convertMillisToDate(System.currentTimeMillis())

                //pick up and return must have tje maximum difference of ten days
                return (
                    if(sharedViewModel.pickUpDateCar == "" ||
                        (((pickUpOrReturn == 0 && carCredentialsViewModel.pickUpBool)
                                ||(pickUpOrReturn == 1 && carCredentialsViewModel.returnBool)) && sharedViewModel.returnDateCar == "")){
                        (
                            ((utcDate.substring(3,5).toInt() == currentDate.substring(3,5).toInt()
                            &&
                            utcDate.substring(0,2).toInt() > currentDate.substring(0,2).toInt()
                            ||
                            utcDate.substring(3,5).toInt() > currentDate.substring(3,5).toInt())
                            &&
                            utcDate.substring(6,10).toInt() == currentDate.substring(6,10).toInt())
                            ||
                            utcDate.substring(6,10).toInt() > currentDate.substring(6,10).toInt()
                        )
                    }
                    else{
                        if(!((pickUpOrReturn == 0 && carCredentialsViewModel.pickUpBool)
                                    ||(pickUpOrReturn == 1 && carCredentialsViewModel.returnBool))
                            && sharedViewModel.pickUpDateCar != ""){
                            val pickUpDateCarInMillis = Converters.convertDateToMillis(sharedViewModel.pickUpDateCar)
                            (
                                (((utcDate.substring(3,5).toInt() == currentDate.substring(3,5).toInt()
                                &&
                                utcDate.substring(0,2).toInt() > currentDate.substring(0,2).toInt()
                                ||
                                utcDate.substring(3,5).toInt() > currentDate.substring(3,5).toInt())
                                &&
                                utcDate.substring(6,10).toInt() == currentDate.substring(6,10).toInt())
                                ||
                                utcDate.substring(6,10).toInt() > currentDate.substring(6,10).toInt())
                                &&
                                (
                                    (
                                    utcDate.substring(6,10).toInt() == sharedViewModel.pickUpDateCar.substring(6,10).toInt()
                                    && utcDate.substring(3,5).toInt() == sharedViewModel.pickUpDateCar.substring(3,5).toInt()
                                    && utcDate.substring(0,2).toInt() <= sharedViewModel.pickUpDateCar.substring(0,2).toInt() + 10
                                    && utcDate.substring(0,2).toInt() >= sharedViewModel.pickUpDateCar.substring(0,2).toInt()
                                    )
                                    ||
                                    (
                                    utcDate.substring(6,10).toInt() >= sharedViewModel.pickUpDateCar.substring(6,10).toInt()
                                    &&
                                    sharedViewModel.pickUpDateCar.substring(0,2).toInt() >= 19
                                    && utcTimeMillis <= pickUpDateCarInMillis + 10 * 24 * 60 * 60 * 1000
                                    && utcTimeMillis >= pickUpDateCarInMillis
                                    )
                                )
                            )
                        }
                        else{//if the return date is filled and the pick up date changes
                            var returnDateCarInMillis: Long = 0
                            if(sharedViewModel.returnDateCar!="") {
                                returnDateCarInMillis = Converters.convertDateToMillis(sharedViewModel.returnDateCar)
                            }
                            (
                                ((
                                utcDate.substring(3,5).toInt() == currentDate.substring(3,5).toInt()
                                        &&
                                        utcDate.substring(0,2).toInt() > currentDate.substring(0,2).toInt()
                                        ||
                                        utcDate.substring(3,5).toInt() > currentDate.substring(3,5).toInt())
                                &&
                                utcDate.substring(6,10).toInt() == currentDate.substring(6,10).toInt())
                                ||
                                utcDate.substring(6,10).toInt() > currentDate.substring(6,10).toInt()
                            )
                            &&
                            (
                                (
                                sharedViewModel.returnDateCar !=""
                                        &&
                                        utcDate.substring(3,5).toInt() == sharedViewModel.returnDateCar.substring(3,5).toInt()
                                        &&
                                        utcDate.substring(6,10).toInt() == sharedViewModel.returnDateCar.substring(6,10).toInt()
                                        &&
                                        utcDate.substring(0,2).toInt() <= sharedViewModel.returnDateCar.substring(0,2).toInt()
                                        &&
                                        utcDate.substring(0,2).toInt() >= sharedViewModel.returnDateCar.substring(0,2).toInt() - 10
                                )
                                ||(
                                sharedViewModel.returnDateCar!="" &&
                                        utcDate.substring(6,10).toInt() <= sharedViewModel.returnDateCar.substring(6,10).toInt()
                                        &&
                                        sharedViewModel.returnDateCar.substring(0,2).toInt() <= 10
                                        &&
                                        utcTimeMillis >= returnDateCarInMillis - 10 * 24 * 60 * 60 * 1000
                                        &&
                                        utcTimeMillis <= returnDateCarInMillis
                                )
                            )
                        }
                    }
                )
            }
        }
    )
    val selectedDate = datePickerState.selectedDateMillis?.let {
        Converters.convertMillisToDate(it)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    carCredentialsViewModel.rentingTimeError = false
                    onSelectedDate(selectedDate)
                    onDismiss()
                },
                modifier = Modifier
                    .height(40.dp)
                    .padding(end = 90.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF023E8A)
                )
            ) {
                Text(text = "OK",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(
                        fonts = listOf(
                            Font(
                                resId = R.font.opensans
                            )
                        )
                    )
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                },
                modifier = Modifier
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF023E8A)
                )
            ) {
                Text(text = "Cancel",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(
                        fonts = listOf(
                            Font(
                                resId = R.font.opensans
                            )
                        )
                    )
                )
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = Color(0xFFEBF2FA)
        )
    ) {
        DatePicker(
            state = datePickerState,
            modifier = Modifier
                .background(color = Color(0xFFEBF2FA))
                .size(520.dp, 250.dp),
            title = {
                Text(
                    if(((pickUpOrReturn == 0 && carCredentialsViewModel.pickUpBool)
                                ||(pickUpOrReturn == 1 && carCredentialsViewModel.returnBool)))
                        "Select pick up date"
                    else
                        "Select return date",
                    fontFamily = FontFamily(
                        fonts = listOf(
                            Font(
                                resId = R.font.opensans
                            )
                        )
                    ),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            },
            headline = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (datePickerState.selectedDateMillis != null) selectedDate else "No Date",
                        fontSize = 20.sp
                    )
                }
            },
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                titleContentColor = Color(0xFF4361EE),
                headlineContentColor = Color(0xFF4361EE),
                weekdayContentColor = Color(0xFF023E8A),
                navigationContentColor = Color(0xFF023E8A),
                yearContentColor = Color(0xFF023E8A),
                dayContentColor = Color(0xFF023E8A),
                todayDateBorderColor = Color(0xFF4361EE),
                todayContentColor = Color(0xFF023E8A),
                subheadContentColor = Color(0xFF4361EE),
                dividerColor = Color(0xFF4361EE),
                selectedDayContainerColor = Color(0xFF023E8A),
                selectedYearContainerColor = Color(0xFF023E8A),
                containerColor = Color(0xFFEBF2FA),
                currentYearContentColor = Color(0xFF023E8A),
                dateTextFieldColors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = Color(0xFF023E8A),
                    focusedBorderColor = Color(0xFF023E8A),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFF4361EE),
                    cursorColor = Color(0xFF023E8A)
                )
            )
        )
    }
}
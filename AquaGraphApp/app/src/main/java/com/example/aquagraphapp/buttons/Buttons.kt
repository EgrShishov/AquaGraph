package com.example.aquagraphapp.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AddNewAdressButton(){
    ExtendedFloatingActionButton(
        expanded = true,
        icon = { Icon(Icons.Outlined.Add,"desc") },
        text = { Text(text = "Добавить адрес")},
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        onClick = {
            AddNewAdress()
        }
    )
}

//implement with dialog screen and mapkit SDK
fun AddNewAdress(){

}
package com.example.aquagraphapp.models

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    var route : String,
    val title : String,
    val selectedItem : ImageVector,
    val unselectedItem : ImageVector,
    var hasNotifications : Boolean
)

var ListOfMonth : List<String> = listOf(
    "Янв", "Фев",
    "Март", "Апр", "Май",
    "Июнь", "Июль", "Авг",
    "Сент", "Окт", "Нояб",
    "Декаб"
)
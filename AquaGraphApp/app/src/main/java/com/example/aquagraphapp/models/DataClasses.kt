package com.example.aquagraphapp.models

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    var route : String,
    val title : String,
    val selectedItem : ImageVector,
    val unselectedItem : ImageVector,
    var hasNotifications : Boolean
)
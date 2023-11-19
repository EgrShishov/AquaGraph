package com.example.aquagraphapp.navigation

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aquagraphapp.models.BottomNavigationItem
import com.example.aquagraphapp.screens.HomeScreen
import com.example.aquagraphapp.screens.InfoScreen
import com.example.aquagraphapp.screens.NotificationScreen
import com.example.aquagraphapp.screens.ProblemsScreen
import com.example.aquagraphapp.screens.Screens
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.aquagraphapp.models.MarkModel
import com.example.aquagraphapp.models.ResponseModel
import com.example.aquagraphapp.models.ScheduledWork
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBar(
    qualityData : List<ResponseModel>,
    worksData: List<ScheduledWork>,
    workMarks: List<MarkModel>,
    applicationContext: Context
) {
    val items = listOf(
        BottomNavigationItem(
            route = "HomeScreen",
            title = "Главная",
            selectedItem = Icons.Filled.Home,
            unselectedItem = Icons.Outlined.Home,
            hasNotifications = false
        ),
        BottomNavigationItem(
            route = "InfoScreen",
            title = "Инфо",
            selectedItem = Icons.Filled.Info,
            unselectedItem = Icons.Outlined.Info,
            hasNotifications = false
        ),
        BottomNavigationItem(
            route = "ProblemsScreen",
            title = "Проблемы?",
            selectedItem = Icons.Filled.Build,
            unselectedItem = Icons.Outlined.Build,
            hasNotifications = false
        ),
        BottomNavigationItem(
            route = "NotificationScreen",
            title = "Уведомления",
            selectedItem = Icons.Filled.Notifications,
            unselectedItem = Icons.Outlined.Notifications,
            hasNotifications = true
        )
    )

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()

    var loading = remember {mutableStateOf(true)}

    Scaffold(
        bottomBar = {
            androidx.compose.material3.NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingFromBaseline(bottom = 0.dp),
                containerColor = MaterialTheme.colorScheme.background,
            )
            {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.hasNotifications) {
                                        Badge(contentColor = Color.Red)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedItem
                                    } else {
                                        item.unselectedItem
                                    },
                                    contentDescription = item.title
                                )
                            }
                        },
                        label = { Text(text = item.title) },
                        selected = selectedItemIndex == index,
                        onClick =
                        {
                            selectedItemIndex = index
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.height(90.dp),
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.HomeScreen.name,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable("HomeScreen") {
                LaunchedEffect(Unit) {
                    loading.value = true
                    delay(1000)
                    loading.value = false
                }
                com.example.aquagraphapp.loading.ShowLoadingCircle(loading = loading.value)
                HomeScreen(applicationContext, workMarks)
            }
            composable("InfoScreen") {
                LaunchedEffect(Unit) {
                    loading.value = true
                    delay(1200)
                    loading.value = false
                }
                com.example.aquagraphapp.loading.ShowLoadingCircle(loading = loading.value)
                InfoScreen(qualityData)
            }
            composable("ProblemsScreen") {
                LaunchedEffect(Unit) {
                    loading.value = true
                    //delay(1000)
                    loading.value = false
                }
                com.example.aquagraphapp.loading.ShowLoadingCircle(loading = loading.value)
                ProblemsScreen(applicationContext)
            }
            composable("NotificationScreen") {
                LaunchedEffect(Unit) {
                    loading.value = true
                    delay(900)
                    loading.value = false
                }
                com.example.aquagraphapp.loading.ShowLoadingCircle(loading = loading.value)
                NotificationScreen(worksData)
            }
        }
    }
}
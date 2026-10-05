@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.busschedule.ui

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.busschedule.R
import com.example.busschedule.data.BusSchedule
import com.example.busschedule.ui.theme.BusScheduleTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class BusScheduleScreens {
    FullSchedule,
    RouteSchedule
}

@Composable
fun BusScheduleApp(viewModel: BusScheduleViewModel = viewModel(factory = BusScheduleViewModel.factory)) {
    val navController = rememberNavController()
    val fullScheduleTitle = stringResource(R.string.full_schedule)
    var topAppBarTitle by remember { mutableStateOf(fullScheduleTitle) }
    val onBackHandler = {
        topAppBarTitle = fullScheduleTitle
        navController.navigateUp()
    }

    Scaffold(
        topBar = {
            BusScheduleTopAppBar(
                title = topAppBarTitle,
                canNavigateBack = navController.previousBackStackEntry != null,
                onBackClick = { onBackHandler() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BusScheduleScreens.FullSchedule.name
        ) {
            composable(BusScheduleScreens.FullSchedule.name) {
                // remember evita crear un Flow nuevo en cada recomposición
                val fullSchedule by remember { viewModel.getFullSchedule() }
                    .collectAsState(initial = emptyList())
                FullScheduleScreen(
                    busSchedules = fullSchedule,
                    contentPadding = innerPadding,
                    onScheduleClick = { busStopName ->
                        // Uri.encode: los nombres tienen espacios y tildes
                        navController.navigate("${BusScheduleScreens.RouteSchedule.name}/${Uri.encode(busStopName)}")
                        topAppBarTitle = busStopName
                    }
                )
            }
            val busRouteArgument = "busRoute"
            composable(
                route = BusScheduleScreens.RouteSchedule.name + "/{$busRouteArgument}",
                arguments = listOf(navArgument(busRouteArgument) { type = NavType.StringType })
            ) { backStackEntry ->
                val stopName = backStackEntry.arguments?.getString(busRouteArgument)
                    ?: error("busRouteArgument cannot be null")
                val routeSchedule by remember(stopName) { viewModel.getScheduleFor(stopName) }
                    .collectAsState(initial = emptyList())
                RouteScheduleScreen(
                    stopName = stopName,
                    busSchedules = routeSchedule,
                    contentPadding = innerPadding,
                    onBack = { onBackHandler() }
                )
            }
        }
    }
}

@Composable
fun FullScheduleScreen(
    busSchedules: List<BusSchedule>,
    onScheduleClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    BusScheduleDetails(
        contentPadding = contentPadding,
        busSchedules = busSchedules,
        onScheduleClick = onScheduleClick,
        modifier = modifier
    )
}

@Composable
fun RouteScheduleScreen(
    stopName: String,
    busSchedules: List<BusSchedule>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onBack: () -> Unit = {}
) {
    BackHandler { onBack() }
    BusScheduleDetails(
        busSchedules = busSchedules,
        modifier = modifier,
        contentPadding = contentPadding,
        stopName = stopName
    )
}

@Composable
fun BusScheduleDetails(
    busSchedules: List<BusSchedule>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    stopName: String? = null,
    onScheduleClick: ((String) -> Unit)? = null,
) {
    Column(modifier = modifier.padding(contentPadding)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stopName ?: stringResource(R.string.stop_name), style = MaterialTheme.typography.titleMedium)
            Text(stringResource(R.string.arrival_time), style = MaterialTheme.typography.titleMedium)
        }
        HorizontalDivider()
        if (busSchedules.isEmpty()) {
            Text(stringResource(R.string.no_data), modifier = Modifier.padding(16.dp))
        }
        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            items(items = busSchedules, key = { it.id }) { schedule ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = onScheduleClick != null) {
                            onScheduleClick?.invoke(schedule.stopName)
                        }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (stopName == null) {
                        Text(text = schedule.stopName, style = MaterialTheme.typography.bodyLarge)
                    } else {
                        Text(text = "")
                    }
                    Text(
                        text = formatTime(schedule.arrivalTimeInMillis),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

/** Convierte segundos Unix a "h:mm a" (ej. 8:30 a. m.). */
fun formatTime(seconds: Int): String =
    SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(seconds.toLong() * 1000))

@Composable
fun BusScheduleTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (canNavigateBack) {
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            },
            modifier = modifier
        )
    } else {
        TopAppBar(title = { Text(title) }, modifier = modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun FullSchedulePreview() {
    BusScheduleTheme {
        FullScheduleScreen(
            busSchedules = List(3) { BusSchedule(it, "Paradero $it", 1735736400 + it * 600) },
            onScheduleClick = {}
        )
    }
}

package com.example.mui6.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    var showMap by remember { mutableStateOf(false) }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    val textColor by animateColorAsState(
        targetValue = if (!showMap) MaterialTheme.colorScheme.onSurface else Color.Red,
        animationSpec = tween(durationMillis = 600)
    )
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Tile previewer",
                    color = textColor,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.W500),
                    modifier = Modifier.clickable {
                        showMap = false
                        latitude = ""
                        longitude = ""
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (!showMap) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = latitude,
                        onValueChange = { latitude = it },
                        label = { Text("Latitude") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = longitude,
                        onValueChange = { longitude = it },
                        label = { Text("Longitude") }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = {
                        if (latitude.isNotEmpty() && longitude.isNotEmpty()) {
                            showMap = true
                        }
                    }) {
                        Text("Check")
                    }
                }
            } else {
                val lat = latitude.toDoubleOrNull() ?: 0.0
                val lng = longitude.toDoubleOrNull() ?: 0.0
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(lat, lng), 12f)
                }

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    val markerState = rememberMarkerState(position = LatLng(lat, lng))
                    Marker(
                        state = markerState,
                        title = "Selected Location"
                    )
                }
            }
        }
    }
}
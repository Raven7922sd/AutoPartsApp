package com.autoparts.presentation.serviciodetalle

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autoparts.ui.theme.AutoPartsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicioDetalleScreen(
    servicioId: Int,
    navController: NavController,
    viewModel: ServicioDetalleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(servicioId) {
        viewModel.onEvent(ServicioDetalleUiEvent.LoadServicio(servicioId))
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ServicioDetalleUiEffect.NavigateBack -> {
                    navController.popBackStack()
                }
                is ServicioDetalleUiEffect.NavigateToAgendarCita -> {
                    navController.navigate(
                        com.autoparts.presentation.navigation.Screen.CrearCita.createRoute(effect.servicioId)
                    )
                }
                is ServicioDetalleUiEffect.NavigateToLogin -> {
                    navController.navigate(
                        com.autoparts.presentation.navigation.Screen.Login.createRoute(
                            "Inicie sesión para agendar el servicio"
                        )
                    ) {
                        launchSingleTop = true
                    }
                }
                is ServicioDetalleUiEffect.ShowMessage -> {
                }
            }
        }
    }

    ServicioDetalleScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackClick = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicioDetalleScreenContent(
    uiState: ServicioDetalleUiState,
    onEvent: (ServicioDetalleUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Servicio") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.error,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onBackClick) {
                            Text("Volver")
                        }
                    }
                }
            }
            uiState.servicio != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val imageBitmap = remember(uiState.servicio.servicioImagenBase64) {
                        uiState.servicio.servicioImagenBase64?.let { imageBase64 ->
                            try {
                                val decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT)
                                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                                bitmap?.asImageBitmap()
                            } catch (_: Exception) {
                                null
                            }
                        }
                    }

                    imageBitmap?.let { bitmap ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            Image(
                                bitmap = bitmap,
                                contentDescription = uiState.servicio.nombre,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = uiState.servicio.nombre,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$${uiState.servicio.precio}",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = uiState.servicio.descripcion,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Button(
                        onClick = { onEvent(ServicioDetalleUiEvent.OnAgendarCita) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.DateRange, "Agendar", modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Agendar Cita")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServicioDetalleScreenPreview() {
    AutoPartsAppTheme {
        ServicioDetalleScreenContent(
            uiState = ServicioDetalleUiState(
                servicio = com.autoparts.domain.model.Servicio(
                    servicioId = 1,
                    nombre = "Cambio de Aceite",
                    precio = 1500.0,
                    descripcion = "Cambio completo de aceite del motor con filtro incluido",
                    duracionEstimada = 1.0,
                    servicioImagenBase64 = null,
                    solicitados = 15,
                    fechaServicio = "2024-01-15"
                )
            ),
            onEvent = {},
            onBackClick = {}
        )
    }
}
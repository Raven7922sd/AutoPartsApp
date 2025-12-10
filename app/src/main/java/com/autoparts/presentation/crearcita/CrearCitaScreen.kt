package com.autoparts.presentation.crearcita

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.autoparts.ui.theme.AutoPartsAppTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearCitaScreen(
    servicioId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToMisCitas: () -> Unit,
    viewModel: CrearCitaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(servicioId) {
        viewModel.onEvent(CrearCitaUiEvent.LoadServicio(servicioId))
    }

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is CrearCitaUiEffect.NavigateBack -> onNavigateBack()
                is CrearCitaUiEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is CrearCitaUiEffect.NavigateToMisCitas -> onNavigateToMisCitas()
            }
        }
    }

    CrearCitaScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackClick = onNavigateBack,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearCitaScreenContent(
    uiState: CrearCitaUiState,
    onEvent: (CrearCitaUiEvent) -> Unit,
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text("Agendar Cita") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        CrearCitaContent(
            uiState = uiState,
            onEvent = onEvent,
            onBackClick = onBackClick,
            padding = padding
        )
    }

    CitaCreadaDialog(
        visible = uiState.citaCreada,
        codigoConfirmacion = uiState.codigoConfirmacion,
        onDismiss = { onEvent(CrearCitaUiEvent.OnDismissDialog) }
    )
}

@Composable
private fun CrearCitaContent(
    uiState: CrearCitaUiState,
    onEvent: (CrearCitaUiEvent) -> Unit,
    onBackClick: () -> Unit,
    padding: PaddingValues
) {
    when {
        uiState.isLoading && uiState.servicio == null -> LoadingState(padding)
        uiState.error != null && uiState.servicio == null -> ErrorState(uiState.error, onBackClick, padding)
        uiState.servicio != null -> CitaFormContent(uiState, onEvent, padding)
    }
}

@Composable
private fun LoadingState(padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(error: String, onBackClick: () -> Unit, padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
            Button(onClick = onBackClick) {
                Text("Volver")
            }
        }
    }
}

@Composable
private fun CitaFormContent(
    uiState: CrearCitaUiState,
    onEvent: (CrearCitaUiEvent) -> Unit,
    padding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ServicioInfoCard(uiState.servicio!!)

        Text(
            text = "Datos de la Cita",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        NombreField(uiState, onEvent)
        DatePickerField(
            selectedDate = uiState.fechaCita,
            onDateSelected = { onEvent(CrearCitaUiEvent.OnFechaChanged(it)) },
            isError = uiState.fechaError != null,
            errorMessage = uiState.fechaError
        )
        TimePickerField(
            selectedTime = uiState.horaCita,
            onTimeSelected = { onEvent(CrearCitaUiEvent.OnHoraChanged(it)) },
            isError = uiState.horaError != null,
            errorMessage = uiState.horaError
        )

        Spacer(modifier = Modifier.height(8.dp))
        SubmitButton(uiState, onEvent)
    }
}

@Composable
private fun ServicioInfoCard(servicio: com.autoparts.domain.model.Servicio) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Servicio Seleccionado",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = servicio.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Precio: RD$ ${String.format(Locale.US, "%.2f", servicio.precio)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun NombreField(uiState: CrearCitaUiState, onEvent: (CrearCitaUiEvent) -> Unit) {
    OutlinedTextField(
        value = uiState.clienteNombre,
        onValueChange = { onEvent(CrearCitaUiEvent.OnNombreChanged(it)) },
        label = { Text("Nombre Completo") },
        leadingIcon = {
            Icon(Icons.Default.Person, "Nombre")
        },
        modifier = Modifier.fillMaxWidth(),
        isError = uiState.nombreError != null,
        supportingText = {
            uiState.nombreError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        singleLine = true
    )
}

@Composable
private fun SubmitButton(uiState: CrearCitaUiState, onEvent: (CrearCitaUiEvent) -> Unit) {
    Button(
        onClick = { onEvent(CrearCitaUiEvent.OnSubmitCita) },
        modifier = Modifier.fillMaxWidth(),
        enabled = !uiState.isLoading
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Agendando...")
        } else {
            Icon(Icons.Default.CheckCircle, "Confirmar")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Confirmar Cita")
        }
    }
}

@Composable
private fun CitaCreadaDialog(
    visible: Boolean,
    codigoConfirmacion: String?,
    onDismiss: () -> Unit
) {
    if (!visible) return

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Éxito",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = "¡Cita Agendada!",
                textAlign = TextAlign.Center
            )
        },
        text = {
            CitaCreadaDialogContent(codigoConfirmacion)
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Aceptar")
            }
        }
    )
}

@Composable
private fun CitaCreadaDialogContent(codigoConfirmacion: String?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Tu cita ha sido agendada exitosamente.",
            textAlign = TextAlign.Center
        )
        codigoConfirmacion?.let { codigo ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Código de Confirmación",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = codigo,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Text(
            text = "Guarda tu código de confirmación para futuras referencias.",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CrearCitaScreenPreview() {
    AutoPartsAppTheme {
        CrearCitaScreenContent(
            uiState = CrearCitaUiState(
                servicio = com.autoparts.domain.model.Servicio(
                    servicioId = 1,
                    nombre = "Cambio de Aceite",
                    precio = 1500.0,
                    descripcion = "Servicio completo",
                    duracionEstimada = 1.0,
                    servicioImagenBase64 = null,
                    solicitados = 10,
                    fechaServicio = "2024-01-01"
                )
            ),
            onEvent = {},
            onBackClick = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        label = { Text("Fecha de la Cita") },
        leadingIcon = {
            Icon(Icons.Default.CalendarToday, "Fecha")
        },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        isError = isError,
        supportingText = {
            if (isError && errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text("Toca para seleccionar la fecha")
            }
        },
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.CalendarToday, "Seleccionar fecha")
            }
        },
        placeholder = { Text("Selecciona una fecha") }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val calendar = Calendar.getInstance().apply {
                                timeInMillis = millis
                            }
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val formattedDate = dateFormat.format(calendar.time)
                            onDateSelected(formattedDate)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(
    selectedTime: String,
    onTimeSelected: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = 9,
        initialMinute = 0,
        is24Hour = true
    )

    OutlinedTextField(
        value = selectedTime,
        onValueChange = {},
        label = { Text("Hora de la Cita") },
        leadingIcon = {
            Icon(Icons.Default.Schedule, "Hora")
        },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        isError = isError,
        supportingText = {
            if (isError && errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text("Toca para seleccionar la hora")
            }
        },
        trailingIcon = {
            IconButton(onClick = { showTimePicker = true }) {
                Icon(Icons.Default.Schedule, "Seleccionar hora")
            }
        },
        placeholder = { Text("Selecciona una hora") }
    )

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val hour = timePickerState.hour.toString().padStart(2, '0')
                        val minute = timePickerState.minute.toString().padStart(2, '0')
                        val formattedTime = "$hour:$minute"
                        onTimeSelected(formattedTime)
                        showTimePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}
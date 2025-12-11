package com.autoparts.presentation.checkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onNavigateBack: () -> Unit,
    onNavigateToVenta: (Int) -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onEvent(CheckoutUiEvent.UserMessageShown)
        }
    }

    if (state.checkoutSuccess && state.venta != null) {
        SuccessDialog(
            venta = state.venta!!,
            onDismiss = {
                viewModel.onEvent(CheckoutUiEvent.DismissSuccess)
                onNavigateToVenta(state.venta!!.ventaId)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de Pago") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        CheckoutContent(
            state = state,
            onEvent = viewModel::onEvent,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun CheckoutContent(
    state: CheckoutUiState,
    onEvent: (CheckoutUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = "Información de Pago",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Ingresa los datos de tu tarjeta",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        OutlinedTextField(
            value = state.nombreTitular,
            onValueChange = { onEvent(CheckoutUiEvent.NombreTitularChanged(it)) },
            label = { Text("Nombre del Titular") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.nombreTitularError != null,
            supportingText = {
                state.nombreTitularError?.let { Text(it) }
            },
            enabled = !state.isProcessing
        )

        OutlinedTextField(
            value = state.numeroTarjeta,
            onValueChange = { onEvent(CheckoutUiEvent.NumeroTarjetaChanged(it)) },
            label = { Text("Número de Tarjeta") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = state.numeroTarjetaError != null,
            supportingText = {
                state.numeroTarjetaError?.let { Text(it) }
            },
            placeholder = { Text("1234 5678 9012 3456") },
            enabled = !state.isProcessing
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.fechaExpiracion,
                onValueChange = { onEvent(CheckoutUiEvent.FechaExpiracionChanged(it)) },
                label = { Text("Vencimiento") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.fechaExpiracionError != null,
                supportingText = {
                    state.fechaExpiracionError?.let { Text(it) }
                },
                placeholder = { Text("MM/AA") },
                enabled = !state.isProcessing
            )

            OutlinedTextField(
                value = state.cvv,
                onValueChange = { onEvent(CheckoutUiEvent.CvvChanged(it)) },
                label = { Text("CVV") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                isError = state.cvvError != null,
                supportingText = {
                    state.cvvError?.let { Text(it) }
                },
                placeholder = { Text("123") },
                enabled = !state.isProcessing
            )
        }

        OutlinedTextField(
            value = state.direccion,
            onValueChange = { onEvent(CheckoutUiEvent.DireccionChanged(it)) },
            label = { Text("Dirección de Facturación") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.direccionError != null,
            supportingText = {
                state.direccionError?.let { Text(it) }
            },
            minLines = 2,
            maxLines = 3,
            enabled = !state.isProcessing
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onEvent(CheckoutUiEvent.ProcessCheckout) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !state.isProcessing
        ) {
            if (state.isProcessing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Procesando...")
            } else {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Confirmar Compra")
            }
        }
    }
}

@Composable
private fun SuccessDialog(
    venta: com.autoparts.dominio.model.Venta,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = "¡Compra Exitosa!",
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Tu compra ha sido procesada correctamente",
                    textAlign = TextAlign.Center
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = "Venta #${venta.ventaId}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Total: ${formatCurrency(venta.total)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Factura")
            }
        }
    )
}

private fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "DO"))
    return format.format(amount)
}
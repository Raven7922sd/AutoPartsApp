package com.autoparts.presentation.miscitas


sealed class MisCitasIntent {
    data object CargarCitas : MisCitasIntent()
    data class CancelarCita(val citaId: Int) : MisCitasIntent()
    data class FiltrarPorEstado(val soloConfirmadas: Boolean?) : MisCitasIntent()
}
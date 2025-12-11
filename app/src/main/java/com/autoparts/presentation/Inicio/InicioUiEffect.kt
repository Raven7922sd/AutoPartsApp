package com.autoparts.presentation.inicio

sealed interface InicioUiEffect {
    data object NavigateToCarrito : InicioUiEffect
    data object NavigateToLogin : InicioUiEffect
    data object NavigateToPerfil : InicioUiEffect
    data object NavigateToVentas : InicioUiEffect
    data object NavigateToMisCitas : InicioUiEffect
    data object NavigateToAdminCitas : InicioUiEffect
    data object NavigateToAdminVentas : InicioUiEffect
    data class NavigateToProductoDetalle(val productoId: Int) : InicioUiEffect
    data class NavigateToCategoria(val categoria: String) : InicioUiEffect
    data class ShowMessage(val message: String) : InicioUiEffect
}
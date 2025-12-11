package com.autoparts.presentation.productos

interface ProductosUiEvent {
    data object LoadProductos : ProductosUiEvent
    data class SearchQueryChanged(val query: String) : ProductosUiEvent
    data object UserMessageShown : ProductosUiEvent
}


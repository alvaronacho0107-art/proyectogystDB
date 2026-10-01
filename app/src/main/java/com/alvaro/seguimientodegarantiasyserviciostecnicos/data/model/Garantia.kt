package com.alvaro.seguimientodegarantiasyserviciostecnicos.data.model

data class Garantia(
    val id: Int,
    val codigo: String,
    val tipo: String,
    val estado: String
) {
    companion object {
        val TIPOS_DISPONIBLES = listOf(
            "Producto",
            "Reparación",
            "Cambio",
            "Otro"
        )

        val ESTADOS_DISPONIBLES = listOf(
            "Pendiente",
            "En proceso",
            "Aprobada",
            "Rechazada",
            "Finalizada"
        )
    }
}

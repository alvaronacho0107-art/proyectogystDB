package com.alvaro.seguimientodegarantiasyserviciostecnicos.data.repository


import com.alvaro.seguimientodegarantiasyserviciostecnicos.data.model.Cliente
import com.alvaro.seguimientodegarantiasyserviciostecnicos.data.model.Garantia
import com.alvaro.seguimientodegarantiasyserviciostecnicos.data.model.ServicioTecnico

object InMemoryDataRepository {

    val clientes = mutableListOf(
        Cliente(id = 1, nombre = "Juan Pérez", telefono = "+56912345678", email = "juan@email.com"),
        Cliente(id = 2, nombre = "María González", telefono = "+56987654321", email = "maria.gonzalez@email.com"),
        Cliente(id = 3, nombre = "Pedro Soto", telefono = "+56911223344", email = "pedro.soto@email.com")
    )

    fun addCliente(cliente: Cliente) {
        clientes.add(cliente)
    }

    fun updateCliente(cliente: Cliente) {
        val index = clientes.indexOfFirst { it.id == cliente.id }
        if (index != -1) {
            clientes[index] = cliente
        }
    }

    fun deleteCliente(id: Int) {
        clientes.removeAll { it.id == id }
    }

    fun getNextClienteId(): Int {
        return (clientes.maxOfOrNull { it.id } ?: 0) + 1
    }

    val garantias = mutableListOf(
        Garantia(id = 1, codigo = "GAR-001", tipo = "Producto", estado = "En proceso"),
        Garantia(id = 2, codigo = "GAR-002", tipo = "Reparación", estado = "Pendiente"),
        Garantia(id = 3, codigo = "GAR-003", tipo = "Cambio", estado = "Aprobada")
    )

    fun addGarantia(garantia: Garantia) {
        garantias.add(garantia)
    }

    fun updateGarantia(garantia: Garantia) {
        val index = garantias.indexOfFirst { it.id == garantia.id }
        if (index != -1) {
            garantias[index] = garantia
        }
    }

    fun deleteGarantia(id: Int) {
        garantias.removeAll { it.id == id }
    }

    fun getNextGarantiaId(): Int {
        return (garantias.maxOfOrNull { it.id } ?: 0) + 1
    }

    val serviciosTecnicos = mutableListOf(
        ServicioTecnico(id = 1, nombre = "Servicio Técnico ABC", telefono = "+56912345678", email = "contacto@abc.cl"),
        ServicioTecnico(id = 2, nombre = "Servicio Técnico XYZ", telefono = "+56923456789", email = "soporte@xyz.cl"),
        ServicioTecnico(id = 3, nombre = "Servicio Técnico Rancagua", telefono = "+56934567890", email = "rancagua@serviciotecnico.cl")
    )

    fun addServicioTecnico(servicio: ServicioTecnico) {
        serviciosTecnicos.add(servicio)
    }

    fun updateServicioTecnico(servicio: ServicioTecnico) {
        val index = serviciosTecnicos.indexOfFirst { it.id == servicio.id }
        if (index != -1) {
            serviciosTecnicos[index] = servicio
        }
    }

    fun deleteServicioTecnico(id: Int) {
        serviciosTecnicos.removeAll { it.id == id }
    }

    fun getNextServicioTecnicoId(): Int {
        return (serviciosTecnicos.maxOfOrNull { it.id } ?: 0) + 1
    }
}


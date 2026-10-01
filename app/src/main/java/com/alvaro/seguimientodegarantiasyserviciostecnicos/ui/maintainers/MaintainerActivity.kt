package com.alvaro.seguimientodegarantiasyserviciostecnicos.ui.maintainers

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.alvaro.seguimientodegarantiasyserviciostecnicos.R
import com.alvaro.seguimientodegarantiasyserviciostecnicos.data.model.Cliente
import com.alvaro.seguimientodegarantiasyserviciostecnicos.data.model.Garantia
import com.alvaro.seguimientodegarantiasyserviciostecnicos.data.model.ServicioTecnico
import com.alvaro.seguimientodegarantiasyserviciostecnicos.data.repository.InMemoryDataRepository

abstract class MaintainerActivity : AppCompatActivity() {
    protected abstract val entity: String
    protected abstract val layoutResource: Int


    private lateinit var list: RecyclerView
    private lateinit var emptyState: View
    private lateinit var adapter: EntryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource)

        val (toolbarId, listId, emptyId, fabId, title) = when (entity) {
            "garantias" -> Quint(R.id.toolbarGarantias, R.id.recyclerGarantias, R.id.emptyGarantias, R.id.fabAgregarGarantia, "Mantenedor de Garantías")
            "servicios" -> Quint(R.id.toolbarServiciosTecnicos, R.id.recyclerServiciosTecnicos, R.id.emptyServiciosTecnicos, R.id.fabAgregarServicioTecnico, "Servicios Técnicos")
            else -> Quint(R.id.toolbarClientes, R.id.recyclerClientes, R.id.emptyClientes, R.id.fabAgregarCliente, "Mantenedor de Clientes")
        }
        findViewById<MaterialToolbar>(toolbarId).apply {
            this.title = title
            setNavigationOnClickListener { finish() }
        }
        list = findViewById(listId)
        emptyState = findViewById(emptyId)
        adapter = EntryAdapter(
            onEdit = { position -> showEditor(position) },
            onDelete = { position -> confirmDelete(position) }
        )
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter
        findViewById<FloatingActionButton>(fabId).setOnClickListener { showEditor(null) }
        refresh()
    }

    private fun entries(): List<String> = when (entity) {
        "garantias" -> InMemoryDataRepository.garantias.map { "ID: ${it.id}\n${it.codigo} · ${it.tipo}\nEstado: ${it.estado}" }
        "servicios" -> InMemoryDataRepository.serviciosTecnicos.map { "ID: ${it.id}\n${it.nombre}\n${it.telefono} · ${it.email}" }
        else -> InMemoryDataRepository.clientes.map { "ID: ${it.id}\n${it.nombre}\n${it.telefono} · ${it.email}" }
    }

    private fun refresh() {
        val rows = entries()
        adapter.submit(rows)
        emptyState.visibility = if (rows.isEmpty()) View.VISIBLE else View.GONE
        list.visibility = if (rows.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun showEditor(position: Int?) {
        val fields = when (entity) {
            "garantias" -> listOf("ID", "Código", "Tipo", "Estado")
            else -> listOf("ID", "Nombre", "Teléfono", "Email")
        }
        val old = position?.let { currentValues(it) }
        val container = layoutInflater.inflate(R.layout.dialog_registro, null)
        val inputs = listOf<EditText>(
            container.findViewById(R.id.inputId),
            container.findViewById(R.id.inputCampo1),
            container.findViewById(R.id.inputCampo2),
            container.findViewById(R.id.inputCampo3)
        )
        inputs.forEachIndexed { index, input ->
            input.hint = fields[index]
            input.inputType = if (index == 0) InputType.TYPE_CLASS_NUMBER else when (fields[index]) {
                "Teléfono" -> InputType.TYPE_CLASS_PHONE
                "Email" -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                else -> InputType.TYPE_CLASS_TEXT
            }
            input.setText(old?.getOrNull(index) ?: if (index == 0) nextId().toString() else "")
        }
        inputs[0].isEnabled = position == null
        AlertDialog.Builder(this)
            .setTitle(if (position == null) "Nuevo registro" else "Editar registro")
            .setView(container)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Guardar") { _, _ ->
                val values = inputs.map { it.text.toString().trim() }
                val id = values[0].toIntOrNull() ?: nextId()
                if (values.drop(1).any { it.isBlank() }) {
                    android.widget.Toast.makeText(this, "Completa todos los campos.", android.widget.Toast.LENGTH_SHORT).show()
                } else {
                    save(values, id, position != null)
                    refresh()
                }
            }.show()
    }

    private fun currentValues(position: Int): List<String> = when (entity) {
        "garantias" -> InMemoryDataRepository.garantias[position].let { listOf(it.id.toString(), it.codigo, it.tipo, it.estado) }
        "servicios" -> InMemoryDataRepository.serviciosTecnicos[position].let { listOf(it.id.toString(), it.nombre, it.telefono, it.email) }
        else -> InMemoryDataRepository.clientes[position].let { listOf(it.id.toString(), it.nombre, it.telefono, it.email) }
    }

    private fun nextId(): Int = when (entity) {
        "garantias" -> InMemoryDataRepository.getNextGarantiaId()
        "servicios" -> InMemoryDataRepository.getNextServicioTecnicoId()
        else -> InMemoryDataRepository.getNextClienteId()
    }

    private fun save(values: List<String>, id: Int, editing: Boolean) {
        when (entity) {
            "garantias" -> {
                val item = Garantia(id, values[1], values[2], values[3])
                if (editing) InMemoryDataRepository.updateGarantia(item) else InMemoryDataRepository.addGarantia(item)
            }
            "servicios" -> {
                val item = ServicioTecnico(id, values[1], values[2], values[3])
                if (editing) InMemoryDataRepository.updateServicioTecnico(item) else InMemoryDataRepository.addServicioTecnico(item)
            }
            else -> {
                val item = Cliente(id, values[1], values[2], values[3])
                if (editing) InMemoryDataRepository.updateCliente(item) else InMemoryDataRepository.addCliente(item)
            }
        }
    }

    private fun confirmDelete(position: Int) {
        val title = currentValues(position).getOrElse(1) { "registro" }
        AlertDialog.Builder(this)
            .setTitle("Eliminar registro")
            .setMessage("¿Deseas eliminar $title?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                when (entity) {
                    "garantias" -> InMemoryDataRepository.deleteGarantia(InMemoryDataRepository.garantias[position].id)
                    "servicios" -> InMemoryDataRepository.deleteServicioTecnico(InMemoryDataRepository.serviciosTecnicos[position].id)
                    else -> InMemoryDataRepository.deleteCliente(InMemoryDataRepository.clientes[position].id)
                }
                refresh()
            }.show()
    }

    private data class Quint(val toolbar: Int, val list: Int, val empty: Int, val fab: Int, val title: String)

    private class EntryAdapter(
        private val onEdit: (Int) -> Unit,
        private val onDelete: (Int) -> Unit
    ) : RecyclerView.Adapter<EntryAdapter.Holder>() {
        private var rows: List<String> = emptyList()
        fun submit(items: List<String>) { rows = items; notifyDataSetChanged() }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val root = LayoutInflater.from(parent.context).inflate(R.layout.item_registro, parent, false)
            val label = root.findViewById<TextView>(R.id.textoRegistro)
            val edit = root.findViewById<View>(R.id.botonEditar)
            val delete = root.findViewById<View>(R.id.botonEliminar)
            return Holder(root, label, edit, delete)
        }
        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.label.text = rows[position]
            holder.edit.setOnClickListener { holder.bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let(onEdit) }
            holder.delete.setOnClickListener { holder.bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let(onDelete) }
        }
        override fun getItemCount() = rows.size
        class Holder(view: View, val label: TextView, val edit: View, val delete: View) : RecyclerView.ViewHolder(view)
    }
}


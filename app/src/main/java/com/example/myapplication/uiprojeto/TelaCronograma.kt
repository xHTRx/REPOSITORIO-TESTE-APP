package com.example.myapplication.uiprojeto

// --- IMPORTS NECESSÁRIOS ---
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Info
// NOVOS IMPORTS
import com.example.myapplication.data.database.dao.CronogramaDAO
import com.example.myapplication.data.database.entities.Cronograma
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete

// Imports existentes
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import com.example.myapplication.data.database.AppDatabase
import kotlinx.coroutines.withContext
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// -------------------------------------------------------------------------
// FUNÇÕES SUSPEND CRUD (SEM ALTERAÇÕES)
// -------------------------------------------------------------------------

suspend fun buscarOuCriarCronograma(cronogramaDao: CronogramaDAO): List<Cronograma> {
    val itemsExistentes = cronogramaDao.getAll()

    if (itemsExistentes.size < 30) {
        val novosDias = (1..30).map { dia ->
            Cronograma(
                diaDoMes = dia,
                nome = null,
                horario = null,
                descricao = null
            )
        }
        cronogramaDao.insertAll(novosDias)
        return cronogramaDao.getAll()
    }
    return itemsExistentes
}

suspend fun atualizarCronograma(item: Cronograma, cronogramaDao: CronogramaDAO) {
    try {
        cronogramaDao.update(item)
    } catch (e: Exception) {
        Log.e("Erro ao atualizar", "Cronograma: ${e.message}")
    }
}


// -------------------------------------------------------------------------
// COMPOSABLE INDIVIDUAL DO CARD DO DIA
// -------------------------------------------------------------------------

@Composable
fun ItemDiaCronograma(
    dia: Cronograma,
    onEditClick: (Cronograma) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onEditClick(dia) },
        elevation = CardDefaults.cardElevation(3.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "DIA ${dia.diaDoMes}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (!dia.nome.isNullOrBlank()) {
                    Text(text = "${dia.nome} (${dia.horario ?: ""})", style = MaterialTheme.typography.bodyMedium)
                    dia.descricao?.let { Text(text = it, style = MaterialTheme.typography.bodySmall, maxLines = 1) }
                } else {
                    Text(text = "Nenhum evento agendado", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
            }
        }
    }
}


// -------------------------------------------------------------------------
// COMPOSABLE TELA DO CRONOGRAMA PRINCIPAL
// -------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCronograma(modifier: Modifier = Modifier) {

    // ... (Estados, Contexto e Carregamento)
    var listaCronograma by remember { mutableStateOf(emptyList<Cronograma>()) }
    var itemSelecionado by remember { mutableStateOf<Cronograma?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val cronogramaDao = db.cronogramaDAO()

    val snackbarHostState = remember { SnackbarHostState() }

    fun carregarCronograma() {
        CoroutineScope(Dispatchers.IO).launch {
            val listaCarregada = buscarOuCriarCronograma(cronogramaDao)
            withContext(Dispatchers.Main) {
                listaCronograma = listaCarregada
            }
        }
    }

    LaunchedEffect(Unit) {
        carregarCronograma()
    }

    // --- UI PRINCIPAL ---
    Scaffold(
        topBar = { TopAppBar(title = { Text("Agenda Mensal") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(listaCronograma, key = { it.diaDoMes }) { dia ->
                ItemDiaCronograma(
                    dia = dia,
                    onEditClick = {
                        itemSelecionado = it
                        showEditDialog = true
                    }
                )
            }
        }
    }

    // --- DIALOG DE EDIÇÃO (POUP-UP) ---
    if (showEditDialog && itemSelecionado != null) {
        EditarCronogramaDialog(
            itemInicial = itemSelecionado!!,
            onDismiss = { showEditDialog = false },
            onSave = { itemEditado ->
                CoroutineScope(Dispatchers.IO).launch {
                    atualizarCronograma(itemEditado, cronogramaDao)

                    val mensagem = if (itemEditado.nome == null && itemEditado.horario == null)
                        "Dia ${itemEditado.diaDoMes} limpo!"
                    else
                        "Dia ${itemEditado.diaDoMes} atualizado!"

                    carregarCronograma()
                    withContext(Dispatchers.Main) {
                        snackbarHostState.showSnackbar(
                            message = mensagem,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                showEditDialog = false
            }
        )
    }
}


// -------------------------------------------------------------------------
// COMPOSABLE DIALOG DE EDIÇÃO
// -------------------------------------------------------------------------

@Composable
fun EditarCronogramaDialog(
    itemInicial: Cronograma,
    onDismiss: () -> Unit,
    onSave: (Cronograma) -> Unit
) {
    var nome by remember { mutableStateOf(itemInicial.nome) }
    var horario by remember { mutableStateOf(itemInicial.horario) }
    var descricao by remember { mutableStateOf(itemInicial.descricao) }

    // Cria um item VAZIO para ser usado no botão Limpar
    val itemVazio = itemInicial.copy(nome = null, horario = null, descricao = null)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Dia ${itemInicial.diaDoMes}") },
        text = {
            Column {
                // CAMPO NOME
                TextField(
                    value = nome ?: "",
                    onValueChange = { nome = it },
                    label = { Text("Nome do Evento/Tarefa") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                // CAMPO HORÁRIO
                TextField(
                    value = horario ?: "",
                    onValueChange = { horario = it },
                    label = { Text("Horário (Ex: 14:30)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // CAMPO DESCRIÇÃO
                TextField(
                    value = descricao ?: "",
                    onValueChange = { descricao = it },
                    label = { Text("Descrição detalhada") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        // Botão de Confirmação: SALVAR
        confirmButton = {
            Button(
                onClick = {
                    val itemSalvo = itemInicial.copy(
                        nome = nome?.ifBlank { null },
                        horario = horario?.ifBlank { null },
                        descricao = descricao?.ifBlank { null }
                    )
                    onSave(itemSalvo)
                }
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Espaçamento entre os lados
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Botão Limpar Evento
                TextButton(
                    onClick = { onSave(itemVazio) }, // Salva o item com todos os campos nulos
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Limpar Evento")
                }

                // 2. Botão Cancelar
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}
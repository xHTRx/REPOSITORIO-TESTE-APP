package com.example.myapplication.uiprojeto

// --- IMPORTS OBRIGATÓRIOS ---
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.database.dao.UsuarioDAO
import com.example.myapplication.data.database.entities.Usuario

// --- NOVOS IMPORTS PARA O SNACKBAR ---
// Removido o import android.widget.Toast
import kotlinx.coroutines.withContext

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// -------------------------------------------------------------------------
// FUNÇÕES SUSPEND DE CRUD PARA USUÁRIO (Mantidas)
// -------------------------------------------------------------------------

suspend fun deletarUsuario(usuario: Usuario, usuarioDao: UsuarioDAO) {
    try {
        usuarioDao.deletar(usuario)
    } catch (e: Exception) {
        Log.e("Erro ao deletar", "Usuário: ${e.message}")
    }
}

suspend fun atualizarUsuario(usuario: Usuario, usuarioDao: UsuarioDAO) {
    try {
        usuarioDao.atualizar(usuario)
    } catch (e: Exception) {
        Log.e("Erro ao atualizar", "Usuário: ${e.message}")
    }
}

suspend fun inserirUsuario(nome: String, email: String, senha: String, cpf: String, telefone: String?, usuarioDao: UsuarioDAO) {
    try {
        val novoUsuario = Usuario(nome = nome, email = email, senha = senha, cpf = cpf, telefone = telefone)
        usuarioDao.inserir(novoUsuario)
    } catch (e: Exception) {
        Log.e("Erro ao adicionar", "Usuário: ${e.message}")
    }
}

suspend fun buscarUsuarioUnico(usuarioDao: UsuarioDAO): Usuario? {
    return try {
        usuarioDao.buscarUsuarioUnico()
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "Usuário Único: ${e.message}")
        null
    }
}


// -------------------------------------------------------------------------
// COMPOSABLE INDIVIDUAL DO ITEM DA LISTA (Mantido, mas não usado)
// -------------------------------------------------------------------------

@Composable
fun UmUsuario(
    usuario: Usuario,
    displayIndex: Int,
    onEditClick: (Usuario) -> Unit,
    onDeleteClick: (Usuario) -> Unit
) {
    Card(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        border = BorderStroke(2.dp, Color.Black)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "$displayIndex", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)

            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            ) {
                Text(text = usuario.nome, style = MaterialTheme.typography.titleMedium)
                Text(text = usuario.email, style = MaterialTheme.typography.bodySmall)
                Text(text = usuario.cpf, style = MaterialTheme.typography.titleMedium)
                usuario.telefone?.let { Text(text = it, style = MaterialTheme.typography.titleMedium) }
            }

            // ÍCONE DE EDITAR
            Icon(
                Icons.Default.Edit, "Editar",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onEditClick(usuario) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            // ÍCONE DE EXCLUIR
            Icon(
                Icons.Default.Close, "Excluir",
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onDeleteClick(usuario) }
            )
        }
    }
}


// -------------------------------------------------------------------------
// COMPOSABLE TELA DE CADASTRO PRINCIPAL (Com Snackbar adicionado)
// -------------------------------------------------------------------------
@Composable
fun TelaCadastroUsuario(modifier: Modifier = Modifier) {

    // ESTADOS
    var usuarioPrincipal by remember { mutableStateOf<Usuario?>(null) }
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val usuarioDao = db.usuarioDAO()

    // 1. ESTADO PARA CONTROLAR O SNACKBAR
    val snackbarHostState = remember { SnackbarHostState() }

    // Lógica para carregar o usuário e preencher os campos (mantida)
    suspend fun atualizarEstadoUsuario(usuarioDao: UsuarioDAO, setUsuarioPrincipal: (Usuario?) -> Unit,
                                       setNome: (String) -> Unit, setEmail: (String) -> Unit,
                                       setSenha: (String) -> Unit, setCpf: (String) -> Unit,
                                       setTelefone: (String) -> Unit) {
        val usuarioCarregado = withContext(Dispatchers.IO) {
            buscarUsuarioUnico(usuarioDao)
        }
        withContext(Dispatchers.Main) {
            setUsuarioPrincipal(usuarioCarregado)
            if (usuarioCarregado != null) {
                setNome(usuarioCarregado.nome)
                setEmail(usuarioCarregado.email)
                setSenha(usuarioCarregado.senha)
                setCpf(usuarioCarregado.cpf)
                setTelefone(usuarioCarregado.telefone ?: "")
            } else {
                setNome("")
                setEmail("")
                setSenha("")
                setCpf("")
                setTelefone("")
            }
        }
    }

    // EFEITO: Carrega os dados na inicialização (mantido)
    LaunchedEffect(Unit) {
        atualizarEstadoUsuario(
            usuarioDao,
            { usuarioPrincipal = it },
            { nome = it },
            { email = it },
            { senha = it },
            { cpf = it },
            { telefone = it }
        )
    }

    // 2. ADICIONADO O SNACKBAR HOST NO SCAFFOLD
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                // LÓGICA PARA ESCOLHER A COR DE FUNDO
                val containerColor = when (data.visuals.actionLabel) {
                    "EDIT" -> MaterialTheme.colorScheme.primary // Cor do tema (azul do cabeçalho)
                    "DELETE" -> Color.Red // Cor vermelha para exclusão
                    "CREATE" -> Color(0xFF006400) // Verde para criação
                    else -> MaterialTheme.colorScheme.tertiaryContainer
                }

                Snackbar(
                    snackbarData = data,
                    // 1. COR DO FUNDO
                    containerColor = containerColor,
                    // 2. COR DO TEXTO
                    contentColor = Color.White
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // CARD DO FORMULÁRIO DE CADASTRO/EDIÇÃO (mantido)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color.LightGray, RectangleShape),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Column(Modifier.padding(16.dp)) {
                    // ... (TextFields)

                    TextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome Completo") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(value = senha, onValueChange = { senha = it }, label = { Text("Senha") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(value = cpf, onValueChange = { cpf = it }, label = { Text("CPF") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(value = telefone, onValueChange = { telefone = it }, label = { Text("Telefone (Opcional)") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(20.dp))

                    // BOTÃO DE AÇÃO PRINCIPAL
                    Button(
                        onClick = {
                            if (nome.isNotBlank() && email.isNotBlank() && senha.isNotBlank() && cpf.isNotBlank()) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    if (usuarioPrincipal == null) {
                                        // MODO CRIAR CONTA (CREATE)
                                        inserirUsuario(nome = nome, email = email, senha = senha, cpf = cpf, telefone = telefone.ifBlank { null }, usuarioDao = usuarioDao)
                                        atualizarEstadoUsuario(usuarioDao, { usuarioPrincipal = it }, { nome = it }, { email = it }, { senha = it }, { cpf = it }, { telefone = it })

                                        // ⭐️ SNACKBAR: Conta Criada (com actionLabel="CREATE")
                                        withContext(Dispatchers.Main) {
                                            snackbarHostState.showSnackbar(
                                                message = "Conta criada com sucesso!",
                                                actionLabel = "CREATE", // Código para a cor verde
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    } else {
                                        // MODO SALVAR EDIÇÃO (UPDATE)
                                        val usuarioAtualizado = usuarioPrincipal!!.copy(nome = nome, email = email, senha = senha, cpf = cpf, telefone = telefone.ifBlank { null })
                                        atualizarUsuario(usuarioAtualizado, usuarioDao)

                                        // ⭐️ SNACKBAR: Conta Alterada (com actionLabel="EDIT")
                                        withContext(Dispatchers.Main) {
                                            snackbarHostState.showSnackbar(
                                                message = "Conta editada com sucesso!",
                                                actionLabel = "EDIT", // Código para a cor azul
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (usuarioPrincipal == null) "Criar Conta" else "Salvar Alterações"
                        )
                    }

                    // BOTÃO DE DELETAR (DELETE) - Opcional
                    if (usuarioPrincipal != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    deletarUsuario(usuarioPrincipal!!, usuarioDao)
                                    atualizarEstadoUsuario(usuarioDao, { usuarioPrincipal = it }, { nome = it }, { email = it }, { senha = it }, { cpf = it }, { telefone = it })

                                    // ⭐️ SNACKBAR: Conta Excluída (com actionLabel="DELETE")
                                    withContext(Dispatchers.Main) {
                                        snackbarHostState.showSnackbar(
                                            message = "Conta excluída com sucesso!",
                                            actionLabel = "DELETE", // Código para a cor vermelha
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Deletar Conta")
                        }
                    }
                }
            }
        }
    }
}
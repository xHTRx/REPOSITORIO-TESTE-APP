package com.example.myapplication.uiprojeto

// --- IMPORTS OBRIGATÓRIOS ---
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.database.dao.UsuarioDAO
import com.example.myapplication.data.database.entities.Usuario

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// -------------------------------------------------------------------------
// FUNÇÕES SUSPEND DE CRUD PARA USUÁRIO (MANTIDAS)
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
// COMPOSABLE PARA EXIBIR UM CAMPO NO FORMATO DO TEMA
// -------------------------------------------------------------------------

@Composable
fun CampoInfoTema(label: String, value: String, isLast: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.DarkGray,
        )
        Spacer(modifier = Modifier.height(4.dp))


        Text(
            text = value.uppercase(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )
    }
    // Separador (Divider), exceto no último
    if (!isLast) {

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = Color.LightGray.copy(alpha = 0.7f)
        )
    }
}


// -------------------------------------------------------------------------
// COMPOSABLE TELA DE CADASTRO PRINCIPAL
// -------------------------------------------------------------------------
@Composable
fun TelaCadastroUsuario(modifier: Modifier = Modifier) {

    // ESTADOS (MANTIDOS)
    var usuarioPrincipal by remember { mutableStateOf<Usuario?>(null) }
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }

    // Estado para alternar entre MODO EDIÇÃO (TextFields) e MODO VISUALIZAÇÃO (CampoInfoTema)
    var isEditing by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val usuarioDao = db.usuarioDAO()
    val snackbarHostState = remember { SnackbarHostState() }

    // ... (Lógica para carregar o usuário e preencher os campos - MANTIDA)
    suspend fun atualizarEstadoUsuario(usuarioDao: UsuarioDAO, setUsuarioPrincipal: (Usuario?) -> Unit,
                                       setNome: (String) -> Unit, setEmail: (String) -> Unit,
                                       setSenha: (String) -> Unit, setCpf: (String) -> Unit,
                                       setTelefone: (String) -> Unit) {
        val usuarioCarregado = withContext(Dispatchers.IO) {
            buscarUsuarioUnico(usuarioDao)
        }
        withContext(Dispatchers.Main) {
            setUsuarioPrincipal(usuarioCarregado)
            // Se carregou, entra no modo visualização, senão entra no modo edição/cadastro
            isEditing = (usuarioCarregado == null)

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
                val containerColor = when (data.visuals.actionLabel) {
                    "EDIT" -> Color(0xFF1976D2) // Azul tema
                    "DELETE" -> Color.Red
                    "CREATE" -> Color(0xFF006400) // Verde
                    else -> MaterialTheme.colorScheme.primary
                }

                Snackbar(
                    snackbarData = data,
                    containerColor = containerColor,
                    contentColor = Color.White
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título Principal
            Text(
                text = if (usuarioPrincipal == null) "CADASTRO DE USUÁRIO" else "INFORMAÇÕES CADASTRAIS",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp, bottom = 8.dp)
            )

            // Mensagem de Aviso/Contexto
            Text(
                text = if (usuarioPrincipal == null)
                    "Preencha os dados abaixo para criar sua conta."
                else if (isEditing)
                    "Edite os campos e salve as alterações."
                else
                    "Clique no lápis para editar as informações.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray, // Cinza escuro
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )


            // ------------------------------------------------------------------
            // MODO EDIÇÃO (TextFields para Cadastro/Alteração)
            // ------------------------------------------------------------------
            if (usuarioPrincipal == null || isEditing) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    // Removido background e border, usando a cor do tema
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
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

                        // BOTÃO DE AÇÃO PRINCIPAL (Criar Conta / Salvar Alterações)
                        Button(
                            onClick = {
                                if (nome.isNotBlank() && email.isNotBlank() && senha.isNotBlank() && cpf.isNotBlank()) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        val isCreate = (usuarioPrincipal == null)

                                        if (isCreate) {
                                            inserirUsuario(nome = nome, email = email, senha = senha, cpf = cpf, telefone = telefone.ifBlank { null }, usuarioDao = usuarioDao)
                                        } else {
                                            val usuarioAtualizado = usuarioPrincipal!!.copy(nome = nome, email = email, senha = senha, cpf = cpf, telefone = telefone.ifBlank { null })
                                            atualizarUsuario(usuarioAtualizado, usuarioDao)
                                        }

                                        atualizarEstadoUsuario(usuarioDao, { usuarioPrincipal = it }, { nome = it }, { email = it }, { senha = it }, { cpf = it }, { telefone = it })

                                        withContext(Dispatchers.Main) {
                                            snackbarHostState.showSnackbar(
                                                message = if (isCreate) "Conta criada com sucesso!" else "Conta editada com sucesso!",
                                                actionLabel = if (isCreate) "CREATE" else "EDIT",
                                                duration = SnackbarDuration.Short
                                            )
                                            // Sai do modo de edição após salvar
                                            if (!isCreate) isEditing = false
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (usuarioPrincipal == null) "Criar Conta" else "Salvar Alterações")
                        }

                        // BOTÃO DELETAR SÓ VISÍVEL NO MODO EDIÇÃO
                        if (usuarioPrincipal != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        deletarUsuario(usuarioPrincipal!!, usuarioDao)
                                        atualizarEstadoUsuario(usuarioDao, { usuarioPrincipal = it }, { nome = it }, { email = it }, { senha = it }, { cpf = it }, { telefone = it })

                                        withContext(Dispatchers.Main) {
                                            snackbarHostState.showSnackbar(
                                                message = "Conta excluída com sucesso!",
                                                actionLabel = "DELETE",
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
            // ------------------------------------------------------------------
            // MODO VISUALIZAÇÃO (Novo Formato de Exibição)
            // ------------------------------------------------------------------
            else {
                // Ícone de Edição (Lápis)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        Icons.Default.Edit, "Editar",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { isEditing = true } // Entra no modo edição
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Exibição dos Campos no Novo Formato
                usuarioPrincipal?.let { usuario ->
                    // Nome Completo
                    CampoInfoTema(label = "Nome Completo", value = usuario.nome)

                    // Email
                    CampoInfoTema(label = "E-mail", value = usuario.email)

                    // CPF
                    CampoInfoTema(label = "CPF", value = usuario.cpf)

                    // Telefone (opcional)
                    CampoInfoTema(label = "Telefone", value = usuario.telefone ?: "Não informado", isLast = true)

                }
            }
        }
    }
}
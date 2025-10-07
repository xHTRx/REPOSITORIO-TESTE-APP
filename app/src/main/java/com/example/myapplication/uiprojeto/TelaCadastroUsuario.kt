package com.example.myapplication.uiprojeto

// --- IMPORTS OBRIGATÓRIOS ---
import android.util.Log // Para a função Log
import androidx.compose.runtime.LaunchedEffect // Para carregar os dados na inicialização
import com.example.myapplication.data.database.AppDatabase // <--- Import do AppDatabase
import com.example.myapplication.data.database.dao.UsuarioDAO // <--- Import do UserDAO
import com.example.myapplication.data.database.entities.Usuario // <--- Import da entidade User (Usei 'User' baseado na sua estrutura de pastas, não 'Usuario')

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
// FUNÇÕES SUSPEND DE CRUD PARA USUÁRIO (Movidas para o topo para fácil acesso)
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

suspend fun inserirUsuario(nome: String, email: String, senha: String, usuarioDao: UsuarioDAO) {
    try {
        // Cria um novo objeto User (o ID será autogerado)
        val novoUsuario = Usuario(nome = nome, email = email, senha = senha)
        usuarioDao.inserir(novoUsuario)
    } catch (e: Exception) {
        Log.e("Erro ao adicionar", "Usuário: ${e.message}")
    }
}

suspend fun buscarUsuarios(usuarioDao: UsuarioDAO): List<Usuario> {
    return try {
        usuarioDao.buscarTodos()
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "Usuários: ${e.message}")
        emptyList()
    }
}


// -------------------------------------------------------------------------
// COMPOSABLE INDIVIDUAL DO ITEM DA LISTA
// -------------------------------------------------------------------------

@Composable
fun UmUsuario(
    usuario: Usuario, // CORRIGIDO: Usando a entidade User
    displayIndex: Int,
    onEditClick: (Usuario) -> Unit, // CORRIGIDO: Usando a entidade User
    onDeleteClick: (Usuario) -> Unit // CORRIGIDO: Usando a entidade User
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
// COMPOSABLE TELA DE CADASTRO PRINCIPAL
// -------------------------------------------------------------------------

@Composable
fun TelaCadastroUsuario(modifier: Modifier = Modifier) {
    // ESTADOS DO FORMULÁRIO
    var listaUsuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) } // CORRIGIDO: Usando a entidade User
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var usuarioEditando by remember { mutableStateOf<Usuario?>(null) } // CORRIGIDO: Usando a entidade User

    val context = LocalContext.current
    // Acesso ao banco de dados Room
    // CORRIGIDO: Usando o nome da classe AppDatabase
    val db = AppDatabase.getDatabase(context)
    val usuarioDao = db.usuarioDAO() // CORRIGIDO: Garantindo que o nome da função no AppDatabase é 'userDAO()'

    // Lógica para recarregar a lista
    fun recarregarUsuarios() {
        CoroutineScope(Dispatchers.Main).launch {
            listaUsuarios = buscarUsuarios(usuarioDao)
        }
    }

    // EFEITO: Carrega os dados na inicialização
    LaunchedEffect(Unit) {
        recarregarUsuarios()
    }

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // CARD DO FORMULÁRIO DE CADASTRO
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color.LightGray, RectangleShape),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Column(Modifier.padding(16.dp)) {

                    TextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome Completo") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(
                        value = senha,
                        onValueChange = { senha = it },
                        label = { Text("Senha") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // BOTÃO DE ADIÇÃO/EDIÇÃO
                    Button(
                        onClick = {
                            if (nome.isNotBlank() && email.isNotBlank() && senha.isNotBlank()) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    if (usuarioEditando == null) {
                                        // MODO ADICIONAR
                                        inserirUsuario(nome, email, senha, usuarioDao)
                                    } else {
                                        // MODO EDITAR
                                        val usuarioAtualizado = usuarioEditando!!.copy(
                                            nome = nome,
                                            email = email,
                                            senha = senha
                                        )
                                        atualizarUsuario(usuarioAtualizado, usuarioDao)
                                        usuarioEditando = null // Sai do modo de edição
                                    }
                                    // Limpa os campos e recarrega após a operação
                                    nome = ""
                                    email = ""
                                    senha = ""
                                    recarregarUsuarios()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (usuarioEditando == null) "Cadastrar Usuário" else "Salvar Edição")
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // LISTA DE USUÁRIOS
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(listaUsuarios) { index, user ->
                    UmUsuario(
                        usuario = user,
                        displayIndex = index + 1,
                        onEditClick = { itemAEditar ->
                            // Lógica de Edição: Preenche o formulário
                            nome = itemAEditar.nome
                            email = itemAEditar.email
                            senha = itemAEditar.senha
                            usuarioEditando = itemAEditar
                        },
                        onDeleteClick = { itemADeletar ->
                            // Lógica de Exclusão
                            CoroutineScope(Dispatchers.IO).launch {
                                deletarUsuario(itemADeletar, usuarioDao)
                                recarregarUsuarios() // Atualiza a lista
                            }
                        }
                    )
                }
            }
        }
    }
}
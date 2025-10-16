package com.example.myapplication.mvvm.data

import com.example.myapplication.data.database.dao.UsuarioDAO
import com.example.myapplication.data.database.entities.Usuario
import kotlinx.coroutines.flow.Flow

// Repository do Usuário, focado em operações CRUD
class UsuarioRepository(private val usuarioDAO: UsuarioDAO) {

    // 1. Busca reativa: Usamos Flow para que o ViewModel observe automaticamente
    // qualquer alteração no único usuário cadastrado.
    fun buscarUsuarioUnicoFlow(): Flow<Usuario?> {
        return usuarioDAO.buscarUsuarioUnicoFlow()
    }

    // 2. Funções CRUD (suspend para serem chamadas em Coroutines)
    suspend fun inserir(usuario: Usuario) {
        usuarioDAO.inserir(usuario)
    }

    suspend fun atualizar(usuario: Usuario) {
        usuarioDAO.atualizar(usuario)
    }

    suspend fun deletar(usuario: Usuario) {
        usuarioDAO.deletar(usuario)
    }

    // Nota: Você precisará adicionar o método 'fun buscarUsuarioUnicoFlow(): Flow<Usuario?>'
    // na sua interface UsuarioDAO para que o Flow funcione.
}
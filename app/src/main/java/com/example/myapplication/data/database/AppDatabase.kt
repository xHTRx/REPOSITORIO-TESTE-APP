package com.example.myapplication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.data.database.dao.UsuarioDAO
import com.example.myapplication.data.database.entities.Usuario

// 1. Definição do banco de dados: inclui a entidade Usuario e incrementa a versão
@Database(
    entities = [Usuario::class],
    version = 3, // Versão 2 porque o esquema mudou do projeto original para este
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // 2. Método para acessar o DAO do Usuario
    // Este método é o que a sua TelaCadastroUsuario.kt chama: db.usuarioDAO()
    abstract fun usuarioDAO(): UsuarioDAO

    // 3. Companion Object para o padrão Singleton
    companion object {
        @Volatile // Garante que a variável seja sempre a mais atualizada
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Se a instância já existir, a retorna
            return INSTANCE ?: synchronized(this) {
                // Se for a primeira vez, cria o banco
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cateirinha_database" // Nome do seu arquivo de banco de dados
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
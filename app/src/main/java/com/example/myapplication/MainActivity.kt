package com.example.myapplication


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.uiprojeto.Cabecalho
import com.example.myapplication.uiprojeto.Rodape
import com.example.myapplication.uiprojeto.TelaCarteirinha
import com.example.myapplication.uiprojeto.TelaHome
import com.example.myapplication.uiprojeto.TelaOfertas
import com.example.myapplication.uiprojeto.Telaqr

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AppScreen()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppScreen() {

    val navController = rememberNavController()

    Scaffold(
        topBar = {
            Box(modifier = Modifier.padding(top = 0.dp)) {
                Cabecalho(titulo = "Benefícios e Ofertas", mostrarIconeDeRosto = true)
            }
        },
        bottomBar = {
            Box(modifier = Modifier.padding(bottom = 25.dp)) {
                // 2. Passamos o controlador para o Rodapé
                Rodape(navController = navController)
            }
        }
    ) { innerPadding ->
        // 3. O NavHost gerencia as telas
        NavHost(
            navController = navController,
            startDestination = "home", // Define a rota inicial
            modifier = Modifier.padding(innerPadding)
        ) {
            // 4. Cada composable define uma tela
            composable("home") {
                // Passa o navController para a TelaHome
                TelaHome(navController = navController)
            }
            composable("carteirinha") {
                TelaCarteirinha()
            }
            composable("ofertas") {
                TelaOfertas()
            }
            composable("qrcode") {
                Telaqr()
            }
        }
    }
}
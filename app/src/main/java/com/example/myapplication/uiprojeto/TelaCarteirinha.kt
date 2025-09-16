package com.example.myapplication.uiprojeto

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Preview(showBackground = true)
@Composable
fun TelaCarteirinha() {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(modifier = Modifier.padding(top = 0.dp)) {
                Cabecalho(titulo = "Carteirinha", mostrarIconeDeRosto = false)
            }
        },
        bottomBar = {
            Box(modifier = Modifier.padding(bottom = 25.dp)) {
                Rodape(context = context)
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
                Image(
                    painter = painterResource(id = R.drawable.carteira),
                    contentDescription = "Carteirinha Universitária",
                    modifier = Modifier
                        .size(550.dp)
                )
        }
    }
}


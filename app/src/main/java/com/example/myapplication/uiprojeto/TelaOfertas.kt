package com.example.myapplication.uiprojeto

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R



@Preview(showBackground = true)
@Composable
fun TelaOfertas() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val context = LocalContext.current
            Cabecalho(titulo = "Benefícios e Ofertas")
            SecaoCentralO()
            Rodape(context = context)
        }
    }
}

@Composable
fun SecaoCentralO(modifier: Modifier = Modifier) {

}
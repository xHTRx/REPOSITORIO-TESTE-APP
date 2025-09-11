package com.example.myapplication.uiprojeto

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun Telaqr() {
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
            Cabecalho(titulo= "QR Code")
            SecaoCentralqr(modifier = Modifier.weight(1f))
            Rodape(context = context)
        }
    }
}

@Composable
fun SecaoCentralqr(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Escanei o QR Code!!",
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.height(40.dp))
        DynamicQrCodeImage() // Chamando o novo Composable
    }
}

@Composable
fun DynamicQrCodeImage() {
    val qrCodeImages = listOf(
        R.drawable.myqrcode,
        R.drawable.myqrcode2,
        R.drawable.myqrcode3,
    )

    // 2. Variável de estado para armazenar o índice da imagem atual
    var currentImageIndex by remember { mutableStateOf(0) }

    // 3. LaunchedEffect para criar um temporizador que atualiza o índice
    LaunchedEffect(key1 = Unit) {
        while (true) {
            // Espera 10 segundos
            delay(10000L)
            // Atualiza o índice para o próximo, usando o operador de módulo (%) para voltar ao início da lista
            currentImageIndex = (currentImageIndex + 1) % qrCodeImages.size
        }
    }

    // 4. Interface que usa a variável de estado
    Box(
        modifier = Modifier
            .size(350.dp)
            .border(BorderStroke(8.dp, Color.Red), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = qrCodeImages[currentImageIndex]),
            contentDescription = "QR Code Dinâmico",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
        )
    }
}
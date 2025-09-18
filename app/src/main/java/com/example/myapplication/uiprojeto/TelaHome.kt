package com.example.myapplication.uiprojeto

import android.content.Intent
import android.net.Uri
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
fun TelaHome() {
    // O Scaffold não precisa mais de topBar e bottomBar.
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            SecaoCentralH()
        }
    }
}

@Composable
fun SecaoCentralH(modifier: Modifier = Modifier) {
    // Obtenha o contexto atual para poder iniciar uma nova Activity (navegador)
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Image(
            painter = painterResource(id = R.drawable.logoup),
            contentDescription = "Logo da aplicação",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 350.dp)
        )
        Spacer(modifier = Modifier.height(100.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CardSecao(
                texto = "",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                cor = Color(0xFFD80E0E),
                imagemResId = R.drawable.celularcard,
                onClick = {
                    val url = "https://www.google.com" // Substitua pela URL desejada
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            CardSecao(
                texto = "",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                cor = Color(0xFF08259D),
                imagemResId = R.drawable.qrcode,
                onClick = {
                    val url = "https://www.bing.com" // Substitua pela URL desejada
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }
            )
        }
        Spacer(modifier = Modifier.height(9.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CardSecao(
                texto = "",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                cor = Color(0xFFEAB505),
                imagemResId = R.drawable.identidade,
                onClick = {
                    // Adicione sua URL aqui
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            CardSecao(
                texto = "",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                cor = Color(0xFF1D9E99),
                imagemResId = R.drawable.presente,
                onClick = {
                    // Adicione sua URL aqui
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CardSecao(
                texto = "",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                cor = Color(0xFF7C33A8),
                imagemResId = R.drawable.user,
                onClick = {
                    // Adicione sua URL aqui
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            CardSecao(
                texto = "",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                cor = Color(0xFFFF4500),
                imagemResId = R.drawable.camera,
                onClick = {
                    // Adicione sua URL aqui
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            CardSecao(
                texto = "",
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                cor = Color(0xFF16703C),
                imagemResId = R.drawable.sino,
                onClick = {
                    // Adicione sua URL aqui
                }
            )
        }
    }
}


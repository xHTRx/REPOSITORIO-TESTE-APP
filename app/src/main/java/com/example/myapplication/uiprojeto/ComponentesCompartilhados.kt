package com.example.myapplication.uiprojeto

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import android.content.Context
import com.example.myapplication.uiprojeto.telasactivity.TelahomeActivity
import com.example.myapplication.uiprojeto.telasactivity.TelaqrActivity
import com.example.myapplication.uiprojeto.telasactivity.TelaofertasActivity



@Composable
fun Cabecalho(titulo: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Blue),
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth(),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.iconecabecalho),
                contentDescription = "Imagem de Perfil",
                modifier = Modifier.size(50.dp)
            )
            Column {
                Text(
                    titulo,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(80.dp))
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = "Ícone de Perfil",
                modifier = Modifier.size(50.dp),
                tint = Color.White
            )
        }
    }
}


@Composable
fun Rodape(context: Context) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(),
        shape = RectangleShape
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.casalogo),
                    contentDescription = "Ícone de casa",
                    modifier = Modifier.size(100.dp)
                        .clickable {
                            val intent = Intent(context, TelahomeActivity::class.java)
                            context.startActivity(intent)
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.celularicone),
                    contentDescription = "Ícone de celular",
                    modifier = Modifier.size(100.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.ofertasicone),
                    contentDescription = "Ícone de ofertas",
                    modifier = Modifier.size(110.dp)
                        .clickable {
                            val intent = Intent(context, TelaofertasActivity::class.java)
                            context.startActivity(intent)
                        }
                )
                Image(
                    painter = painterResource(id = R.drawable.qrcodeicone),
                    contentDescription = "Ícone de QR Code",
                    modifier = Modifier
                        .size(80.dp)
                        .clickable {
                            val intent = Intent(context, TelaqrActivity::class.java)
                            context.startActivity(intent)
                        }
                )
            }
        }
    }
}


@Composable
fun CardSecao(
    texto: String,
    modifier: Modifier = Modifier,
    cor: Color = MaterialTheme.colorScheme.surfaceVariant,
    @DrawableRes imagemResId: Int
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = cor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = imagemResId),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            if (texto.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = texto)
            }
        }
    }
}
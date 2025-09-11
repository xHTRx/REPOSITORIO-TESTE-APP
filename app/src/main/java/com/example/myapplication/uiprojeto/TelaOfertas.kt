package com.example.myapplication.uiprojeto


import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R


// --- Novas classes de dados para os itens ---
data class CashbackItemData(
    val percentage: String,
    val description: String,
    val imageResId: Int
)


data class ProductItemData(
    val title: String,
    val originalPrice: String,
    val currentPrice: String,
    val imageResId: Int
)


@Preview(showBackground = true)
@Composable
fun TelaOfertas() {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(modifier = Modifier.padding(top = 25.dp)) {
                Cabecalho(titulo = "Benefícios e Ofertas")
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SecaoCentralO()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecaoCentralO() {
    // Dados para os itens de cashback
    val cashbackItems = listOf(
        CashbackItemData("11%", "de cashback", R.drawable.vivara),
        CashbackItemData("10%", "de cashback", R.drawable.life),
        CashbackItemData("9%", "de cashback", R.drawable.kipling),
        CashbackItemData("12%", "de cashback", R.drawable.lor),
        CashbackItemData("15%", "de cashback", R.drawable.evino),
        CashbackItemData("8%", "de cashback", R.drawable.dolce),
        CashbackItemData("20%", "de cashback", R.drawable.fotoregistro)
    )


    // Dados para os cards de produto
    val productItems = listOf(
        ProductItemData(
            "Smartphone Motorola Moto G05",
            "R$ 1.099,00",
            "R$ 599,00 à vista",
            R.drawable.julius
        ),
        ProductItemData(
            "Sanduicheira Elétrica Cadence Click",
            "R$ 159,90",
            "R$ 91,90 à vista",
            R.drawable.economia
        ),
        ProductItemData(
            "Outro Produto",
            "R$ 200,00",
            "R$ 150,00",
            R.drawable.unifrome
        )
    )


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // --- Barra de Pesquisa
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("O que você procura?") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Ícone de busca"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.LightGray
            )
        )


        Spacer(modifier = Modifier.height(16.dp))


        // --- Carrossel de Cashback ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            cashbackItems.forEach { item ->
                CashbackItem(item)
            }
        }


        Spacer(modifier = Modifier.height(24.dp))



        Text(
            text = "Julius Shop indica! ❤️",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            productItems.forEach { item ->
                ProductCard(item)
            }
        }


        Spacer(modifier = Modifier.height(24.dp))


        // Botão "Mostrar mais"
        Button(
            onClick = { /* Ação do botão */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text(text = "Mostrar mais", color = Color.White)
        }


        Spacer(modifier = Modifier.height(24.dp))


        // --- Seção de Banner de Ofertas Especiais ---
        Text(
            text = "Ofertas Especiais",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.meme),
            contentDescription = "Banner de Oferta Especial",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )


        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Ofertas no precinho!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}


@Composable
fun CashbackItem(data: CashbackItemData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = data.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(30.dp))
        )
        Text(text = "SUPER", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(text = "CASHBACK", color = Color.Gray, fontSize = 10.sp)
        Text(text = "Até ${data.percentage}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(text = data.description, fontSize = 12.sp, color = Color.Gray)
    }
}


@Composable
fun ProductCard(data: ProductItemData) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = data.imageResId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = data.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = data.originalPrice, color = Color.Gray, fontSize = 12.sp)
            Text(text = data.currentPrice, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}




package com.example.myapplication.uiprojeto




import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.core.net.toUri




// --- Novas classes de dados para os itens ---
data class CashbackItemData(
    val percentage: String,
    val description: String,
    val imageResId: Int,
    val url: String // Adicionamos este campo para armazenar o link
)




data class ProductItemData(
    val title: String,
    val originalPrice: String,
    val currentPrice: String,
    val imageResId: Int,
    val url: String
)




@Preview(showBackground = true)
@Composable
fun TelaOfertas() {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(modifier = Modifier.padding(top = 25.dp)) {
                Cabecalho(titulo = "Benefícios e Ofertas" ,mostrarIconeDeRosto = false)
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
        CashbackItemData("11%", "de cashback", R.drawable.vivara, "https://www.vivara.com.br"),
        CashbackItemData("10%", "de cashback", R.drawable.life, "https://www.vivara.com.br"),
        CashbackItemData("9%", "de cashback", R.drawable.kipling, "https://www.kipling.com.br"),
        CashbackItemData("12%", "de cashback", R.drawable.lor, "https://www.cafelor.com.br/?srsltid=AfmBOooO4eiVk2EyPlSrVAD6m01KlqVUnotNRGpYB6Ts_c5Z4hCf5iZe"),
        CashbackItemData("15%", "de cashback", R.drawable.evino, "https://www.evino.com.br"),
        CashbackItemData("8%", "de cashback", R.drawable.dolce, "https://www.dolcegusto.com.br"),
        CashbackItemData("20%", "de cashback", R.drawable.fotoregistro, "https://www.fotoregistro.com.br")
    )




    // Dados para os cards de produto
    val productItems = listOf(
        ProductItemData(
            "Curso de Finances com Julius",
            "R$ 1.099,00",
            "R$ 599,00 à vista",
            R.drawable.julius,
            "https://www.ev.org.br/trilhas-de-conhecimento/financas"
        ),
        ProductItemData(
            "E-Book de Economia Avançado - Julius",
            "R$ 159,90",
            "R$ 91,90 à vista",
            R.drawable.economia,
            "https://www.amazon.com.br/Economia-Avan%C3%A7ada-Tomislav-R-Femenick/dp/6556058149?source=ps-sl-shoppingads-lpcontext&ref_=fplfs&psc=1&smid=A1ZZFT5FULY4LN"

        ),
        ProductItemData(
            "Uniforme Julius",
            "R$ 200,00",
            "R$ 150,00",
            R.drawable.unifrome,
            "https://www.amazon.com.br/Fantasia-Macac%C3%A3o-Julius-Mundo-Cosplay/dp/B0C1HBQ6TM"
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
    }
}




@Composable
fun CashbackItem(data: CashbackItemData) {
    val context = LocalContext.current


    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = data.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(30.dp))
                .clickable {
                    // Ação ao clicar na imagem
                    val intent = Intent(Intent.ACTION_VIEW, data.url.toUri())
                    context.startActivity(intent)
                }
        )
        Text(text = "SUPER", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(text = "CASHBACK", color = Color.Gray, fontSize = 10.sp)
        Text(text = "Até ${data.percentage}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(text = data.description, fontSize = 12.sp, color = Color.Gray)
    }
}




@Composable
fun ProductCard(data: ProductItemData) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(8.dp)
            .clickable {
                // Ação: Abrir o link no navegador
                val intent = Intent(Intent.ACTION_VIEW, data.url.toUri())
            context.startActivity(intent)
         },
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






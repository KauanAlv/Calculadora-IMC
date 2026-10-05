package com.example.calculadoraimc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraimc.ui.theme.CalculadoraIMCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraIMCTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IMCScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun IMCScreen(modifier: Modifier = Modifier) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var alturaInput by remember {
        mutableStateOf("")
    }
    var pesoInput by remember {
        mutableStateOf("")
    }

    var isCardVisible by remember {
        mutableStateOf(false)
    }

    // Váriavel que guarda o imc
    var imc by remember {
        mutableStateOf(0.0)
    }
    // Variável para alterar o status do IMC
    var statusIMC by remember {
        mutableStateOf("")
    }

    var corStatusIMC by remember {
        mutableStateOf(Color.White)
    }

    // Variável para alterar a cor do status do IMC
    val corStatusIdeal = Color(0xFF409A66)
    val corStatusLevementeAcima = Color(0xFFFFA500)
    val corStatusAbaixo = Color(0xFFFF0000)

    Column(
        modifier = modifier
            .fillMaxSize()
            .focusTarget()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus(force = true)
                    keyboardController?.hide()
                })
            }
    ) {
//     --- Header ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(color = colorResource(id = R.color.cor_app)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.bmi),
                contentDescription = "Logo do APP",
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .size(60.dp)
            )

            Text(
                text = "Calculadora IMC",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

//     --- form ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .offset(y = (-30).dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF9F6F6)
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = "Seus dados",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.cor_app)
                    )

                    OutlinedTextField(
                        value = alturaInput,
                        onValueChange = { novoValor ->
                            // Aceita apenas números e até no máximo 3 dígitos (ex: 175)
                            val filtrado = novoValor.filter { it.isDigit() }
                            if (filtrado.length <= 3) {
                                alturaInput = novoValor
                            }
                            if (alturaInput.isBlank() || pesoInput.isBlank()) {
                                isCardVisible = false
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        label = {
                            Text(
                                text = "Altura",
                                color = colorResource(R.color.cor_app),
                                fontWeight = FontWeight.Medium
                            )
                        },
                        trailingIcon = {
                            Text(
                                text = "cm",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        },
                        shape = RoundedCornerShape(
                            size = 15.dp
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            cursorColor = colorResource(R.color.cor_app),
                            focusedBorderColor = colorResource(R.color.cor_app),
                            unfocusedBorderColor = colorResource(R.color.cor_app),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )

                    OutlinedTextField(
                        value = pesoInput,
                        onValueChange = { novoValor ->
                            // Garante o formato decimal correto (ex: 60.00 ou 60,00)
                            val normalizado = novoValor.replace(",", ".")
                            if (normalizado.count { it == '.' } <= 1
                                && normalizado.all { it.isDigit() || it == '.' }
                            ) {
                                // Valida se há no máximo 2 casas decimais
                                val partes = normalizado.split(".")
                                val casasDecimais = if (partes.size > 1) partes[1].length else 0
                                if (casasDecimais <= 2) {
                                    pesoInput = novoValor
                                }
                            }
                            if (alturaInput.isBlank() || pesoInput.isBlank()) {
                                isCardVisible = false
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        label = {
                            Text(
                                text = "Peso",
                                color = colorResource(R.color.cor_app),
                                fontWeight = FontWeight.Medium
                            )
                        },
                        trailingIcon = {
                            Text(
                                text = "kg",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        },
                        shape = RoundedCornerShape(
                            size = 15.dp
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            cursorColor = colorResource(R.color.cor_app),
                            focusedBorderColor = colorResource(R.color.cor_app),
                            unfocusedBorderColor = colorResource(R.color.cor_app),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = {
                            keyboardController?.hide()

                            if (alturaInput.isNotBlank() && pesoInput.isNotBlank()) {
                                val altura = alturaInput.toDoubleOrNull() ?: 0.0
                                val alturaMetro = altura / 100
                                val peso = pesoInput.replace(",", ".1").toDoubleOrNull() ?: 0.0

                                if (alturaMetro > 0.0 && peso > 0.0) {
                                    imc = peso / (alturaMetro * alturaMetro)

                                    if (imc < 18.5) {
                                        statusIMC = "Abaixo do peso"
                                        corStatusIMC = corStatusAbaixo
                                    } else if (imc >= 18.5 && imc < 25.0) {
                                        statusIMC = "Peso ideal"
                                        corStatusIMC = corStatusIdeal
                                    } else if (imc >= 25.0 && imc < 30.0) {
                                        statusIMC = "Levemente acima do peso"
                                        corStatusIMC = corStatusLevementeAcima
                                    } else if (imc >= 30.0 && imc < 35.0) {
                                        statusIMC = "Obesidade grau 1"
                                        corStatusIMC = corStatusAbaixo
                                    } else if (imc >= 35.0 && imc < 40.0) {
                                        statusIMC = "Obesidade grau 2"
                                        corStatusIMC = corStatusAbaixo
                                    } else {
                                        statusIMC = "Obesidade grau 3"
                                        corStatusIMC = corStatusAbaixo
                                    }
                                    isCardVisible = true
                                } else {
                                    isCardVisible = false
                                }
                            } else {
                                isCardVisible = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.cor_app),
                            contentColor = colorResource(R.color.cor_app)
                        ),

                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "CALCULAR",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus(force = true)
                            alturaInput = ""
                            pesoInput = ""
                            isCardVisible = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF9A4300),
                            contentColor = Color(0xFF9A4300)
                        ),

                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "LIMPAR",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(
            visible = isCardVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {


            // Card para informar o status do IMC
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 32.dp),

                colors = CardDefaults.cardColors(
                    containerColor = corStatusIMC
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(
                        15.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = String.format("%.2f", imc),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$statusIMC",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
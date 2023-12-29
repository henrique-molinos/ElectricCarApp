package com.henrique.electriccarapp.data

import com.henrique.electriccarapp.domain.Carro

object CarFactory {
    val list = listOf(
        Carro(
            id = 1,
            preco = "R$ 300.000,00",
            bateria = "300kWh",
            potencia = "200cv",
            recarga = "30min",
            urlPhoto = "www.google.com"
        ),
        Carro(
            id = 2,
            preco = "R$ 450.000,00",
            bateria = "400kWh",
            potencia = "370cv",
            recarga = "40 min",
            urlPhoto = "www.uol.com.br"
        )
    )
}
package com.henrique.electriccarapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.henrique.electriccarapp.R

class CalcularAutonomiaActivity : AppCompatActivity() {
    // Variáveis declaradas como lateinit, pois serão inicializadas posteriormente
    lateinit var precoKWh : EditText   // Criação da variável precoKWh
    lateinit var kmPercorrido : EditText // Criação da variável kmPercorrido
    lateinit var btnCalcular : Button // Criação da variável btnCalcular
    lateinit var resultado : TextView // Criação da variável que exibirá o resultado
    lateinit var btnClose : ImageView // Criação da variável que fechará a tela de cálculo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcular_autonomia)
        setupViews()    // Chamada da função que setará as Views
        setupListeners()    // Chamada da função que executa o Listener do click do botão
        setupCachedResult() // Chamada da função que coleta o resultado armazenado em Cache
    }

    private fun setupCachedResult() {
        val valorCalculado = getSharedPref()
        resultado.text = valorCalculado.toString()
    }

    fun setupViews() {
        // Recuperando valores: utilizaremos o método findViewById para buscar uma View.
        // Esse método recebe como parâmetro um Resource. No Android, tudo que é referente à tela gera um identificador único dentro dos Resources
        // Quando trabalhamos com Android utilizando Kotlin, ao utilizar o método findViewById, precisamos informar o componente que estamos buscando com o <>, neste caso, EditText
        // No entanto, como a variável já foi criada anteriormente com a definição de seu tipo, não precisamos informar ao método o seguinte: findViewById<EditText>()
        precoKWh = findViewById(R.id.et_preco_kw)  // Encontrando a View de Preço do kWh
        kmPercorrido = findViewById(R.id.et_km_percorrido) // Encontrando a View de Kms Percorridos
        btnCalcular = findViewById(R.id.btn_calcular)   // Encontrando o Button de cálculo
        resultado = findViewById(R.id.tv_resultado_value) // Encontrando a View de resultado
        btnClose = findViewById(R.id.iv_close) // Encontrando a ImageView do botão de fechar
    }

    fun setupListeners() {
        // Criando um Listener para "ouvir" o clique do botão de calcular, e executar a função calcularAutonomia()
        btnCalcular.setOnClickListener {
            val inputPrecoKWh = precoKWh.text.toString()  // Acessando o texto da variável precoKWh (View et_preco_kw) digitado pelo usuário
            Log.d("Input [precoKWh] ->", inputPrecoKWh)   // Gerando log do texto digitado. Verificar no Logcat
            val inputKmPercorrido = kmPercorrido.text.toString() // Acessando o texto da variável KmPercorrido (View et_km_percorrido) digitado pelo usuário
            Log.d("Input [kmPercorrido] ->", inputKmPercorrido) // Gerando log do texto digitado. Verificar no Logcat

            val autonomia = calcularAutonomia(inputPrecoKWh.toFloat(), inputKmPercorrido.toFloat()) // A variável autonomia recebe o resultado do cálculo
            resultado.text = autonomia.toString()   // Aplicando o valor do resultado à TextView resultado para exibição em tela
        }
        // Criando um Listener para "ouvir" o clique na imagem e executar a função finish() para matar a tela.
        btnClose.setOnClickListener {
            finish()    // Irá desempilhar a activity e retornar para a tela anterior
        }
    }

    fun calcularAutonomia(precoKWh: Float, kmPercorrido: Float): Float {
        val result = precoKWh / kmPercorrido
        saveSharedPref(result)
        return precoKWh / kmPercorrido  // Cálculo da autonomia
    }

    fun saveSharedPref(resultado : Float) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putFloat(getString(R.string.saved_calc), resultado)
            apply()
        }
    }

    fun getSharedPref() : Float {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val calculo = sharedPref.getFloat(getString(R.string.saved_calc), 0.0f)
        return calculo
    }

}
package com.henrique.electriccarapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.henrique.electriccarapp.R

class MainActivity : AppCompatActivity() {
    // Variáveis declaradas como lateinit, pois serão inicializadas posteriormente
    lateinit var btnRedirect : Button // Criação do botão de troca de tela

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()    // Chamada da função que setará as Views
        setupListeners()    // Chamada da função que executa o Listener do click do botão
    }

    fun setupViews() {
        // Recuperando valores: utilizaremos o método findViewById para buscar uma View.
        // Esse método recebe como parâmetro um Resource. No Android, tudo que é referente à tela gera um identificador único dentro dos Resources
        // Quando trabalhamos com Android utilizando Kotlin, ao utilizar o método findViewById, precisamos informar o componente que estamos buscando com o <>, neste caso, EditText
        // No entanto, como a variável já foi criada anteriormente com a definição de seu tipo, não precisamos informar ao método o seguinte: findViewById<EditText>()
        btnRedirect = findViewById(R.id.btn_redirect) // Encontrando o Button de trocar de tela
    }

    fun setupListeners() {
        // Construindo a outra tela através do método startActivity(), que recebe como parâmetro uma intenção.
        // A intenção, por sua vez, recebe como argumento a origem e o destino, a fim de redirecionar para esta outra tela.
        btnRedirect.setOnClickListener {startActivity(Intent(this, CalcularAutonomiaActivity::class.java)) }

    }

}
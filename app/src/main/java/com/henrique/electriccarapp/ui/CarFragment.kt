package com.henrique.electriccarapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.henrique.electriccarapp.R
import com.henrique.electriccarapp.data.CarFactory
import com.henrique.electriccarapp.ui.adapter.CarAdapter

class CarFragment : Fragment() {

    lateinit var listaCarros : RecyclerView // Criação da lista
    lateinit var fabCalcular : FloatingActionButton // Criação do botão

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupList() // Chamada de função que executa a inserção do Array de dados na lista
        setupListeners()    // Chamada da função que executa o Listener do click do botão
    }

    fun setupView(view: View) {
        view.apply {
            listaCarros = findViewById(R.id.rv_lista_carros) // Encontrando a View reciclada
            fabCalcular = findViewById(R.id.fab_calcular)   // Encontrando o botão
        }
    }

    fun setupList() {
        val adapter = CarAdapter(CarFactory.list) // Utilizando o adapter personalizado
        listaCarros.adapter = adapter
    }

    fun setupListeners() {
        // Construindo a outra tela através do método startActivity(), que recebe como parâmetro uma intenção.
        // A intenção, por sua vez, recebe como argumento a origem e o destino, a fim de redirecionar para esta outra tela.
        fabCalcular.setOnClickListener {
            startActivity(Intent(context, CalcularAutonomiaActivity::class.java))
        }
    }
}
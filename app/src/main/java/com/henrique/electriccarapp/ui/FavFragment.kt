package com.henrique.electriccarapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.henrique.electriccarapp.R
import com.henrique.electriccarapp.data.local.CarRepository
import com.henrique.electriccarapp.domain.Carro
import com.henrique.electriccarapp.ui.adapter.CarAdapter

class FavFragment : Fragment() {

    lateinit var listaCarrosFavoritos: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fav_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupList()
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifeCycle", "ON RESUME FavFragment")
        setupList()
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycle", "ON PAUSE FavFragment")
    }

    private fun getCarsOnLocalDb(): List<Carro> {
        val repository = CarRepository(requireContext())
        val carList = repository.getAll()
        return carList
    }

    fun setupView(view: View) {
        view.apply {
            listaCarrosFavoritos = findViewById(R.id.rv_lista_carros_favoritos)
        }
    }

    fun setupList() {
        val carros = getCarsOnLocalDb()
        val carroAdapter =
            CarAdapter(carros, isFavoriteScreen = true) // Utilizando o adapter personalizado com os dados da array populada
        listaCarrosFavoritos.apply {
            adapter = carroAdapter
            isVisible = true   // Deixando a lista de carros visível
        }
        carroAdapter.carItemLister = { carro ->
            val isDeleted = CarRepository(requireContext()).delete(carro)
            Log.d("Carro deletado ->", carro.id.toString())
        }
    }

}
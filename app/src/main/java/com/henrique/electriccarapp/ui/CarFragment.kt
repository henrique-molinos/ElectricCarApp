package com.henrique.electriccarapp.ui

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.henrique.electriccarapp.R
import com.henrique.electriccarapp.data.CarFactory
import com.henrique.electriccarapp.domain.Carro
import com.henrique.electriccarapp.ui.adapter.CarAdapter
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CarFragment : Fragment() {

    lateinit var listaCarros: RecyclerView // Criação da lista
    lateinit var fabCalcular: FloatingActionButton // Criação do botão

    var carrosArray: ArrayList<Carro> = ArrayList()

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
        callService()
        setupListeners()    // Chamada da função que executa o Listener do click do botão
    }

    fun setupView(view: View) {
        view.apply {
            listaCarros = findViewById(R.id.rv_lista_carros) // Encontrando a View reciclada
            fabCalcular = findViewById(R.id.fab_calcular)   // Encontrando o botão
        }
    }

    fun setupList() {
        val adapter =
            CarAdapter(carrosArray) // Utilizando o adapter personalizado com os dados da array populada
        listaCarros.adapter = adapter
    }

    fun setupListeners() {
        // Construindo a outra tela através do método startActivity(), que recebe como parâmetro uma intenção.
        // A intenção, por sua vez, recebe como argumento a origem e o destino, a fim de redirecionar para esta outra tela.
        fabCalcular.setOnClickListener {
            startActivity(Intent(context, CalcularAutonomiaActivity::class.java))
        }
    }

    fun callService() {
        val urlBase = "https://raw.githubusercontent.com/henrique-molinos/cars-api/main/cars.json"
        MyTask().execute(urlBase)
    }

    inner class MyTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("MyTask", "Iniciando...")
        }

        override fun doInBackground(vararg url: String?): String {
            var urlConnection: HttpURLConnection? = null

            try {
                val urlBase = URL(url[0])

                urlConnection = urlBase.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 60000    // ms
                urlConnection.readTimeout = 60000
                urlConnection.setRequestProperty("Accept", "application/json")

                val responseCode = urlConnection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    var response = urlConnection.inputStream.bufferedReader().use { it.readText() }
                    publishProgress(response)
                } else {
                    Log.e("Erro", "Serviço indisponível no momento")
                }

            } catch (ex: Exception) {
                Log.e("Erro", "Erro ao realizar processamento")
            } finally {
                urlConnection?.disconnect() // if (urlConnection != null) { urlConecction.disconnect }
            }
            return ""
        }

        @Deprecated("Deprecated in Java")
        override fun onProgressUpdate(vararg values: String?) {
            try {
                val jsonArray = JSONTokener(values[0]).nextValue() as JSONArray

                for (i in 0 until jsonArray.length()) {
                    val id = jsonArray.getJSONObject(i).getString("id")
                    Log.d("Id ->", id)

                    val preco = jsonArray.getJSONObject(i).getString("preco")
                    Log.d("Preço ->", preco)

                    val bateria = jsonArray.getJSONObject(i).getString("bateria")
                    Log.d("Bateria ->", bateria)

                    val potencia = jsonArray.getJSONObject(i).getString("potencia")
                    Log.d("Potencia ->", potencia)

                    val recarga = jsonArray.getJSONObject(i).getString("recarga")
                    Log.d("Recarga ->", recarga)

                    val urlPhoto = jsonArray.getJSONObject(i).getString("urlPhoto")
                    Log.d("urlPhoto ->", urlPhoto)

                    val model = Carro(
                        id = id.toInt(),
                        preco = preco,
                        bateria = bateria,
                        potencia = potencia,
                        recarga = recarga,
                        urlPhoto = urlPhoto
                    )
                    carrosArray.add(model)
                }
                setupList() // Depois de popular o carrosArray, executa a setupList() para mostrar os dados
            } catch (ex: Exception) {
                Log.e("Erro ->", ex.message.toString())
            }
        }
    }
}
package com.henrique.electriccarapp.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.henrique.electriccarapp.R
import com.henrique.electriccarapp.data.CarFactory
import com.henrique.electriccarapp.data.CarsApi
import com.henrique.electriccarapp.domain.Carro
import com.henrique.electriccarapp.ui.adapter.CarAdapter
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CarFragment : Fragment() {

    lateinit var listaCarros: RecyclerView // Criação da lista
    lateinit var fabCalcular: FloatingActionButton // Criação do botão
    lateinit var progress: ProgressBar  // Criação da barra de progresso
    lateinit var noWifiIcon: ImageView  // Criação do ícone de falta de conexão
    lateinit var noWifiText: TextView   // Criação do texto de falta de conexão
    lateinit var carsApi: CarsApi   // Criação da API

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
        setupRetrofit()
        setupView(view)
        setupListeners()    // Chamada da função que executa o Listener do click do botão
    }

    override fun onResume() {
        super.onResume()
        if(checkForInternet(context)) {
            // callService() -> Esse é outra forma de chamar o serviço
            getAllCars()
        } else {
            emptyState()
        }
    }

    fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/henrique-molinos/cars-api/main/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        carsApi = retrofit.create(CarsApi::class.java)
    }

    fun getAllCars() {
        carsApi.getAllCars().enqueue(object : Callback<List<Carro>> {
            override fun onResponse(call: Call<List<Carro>>, response: Response<List<Carro>>) {
                if (response.isSuccessful) {
                    progress.isVisible = false // Deixando a barra invisível após o retorno do serviço
                    noWifiIcon.isVisible = false   // Deixando o ícone de falta de conexão invisível após o retorno
                    noWifiText.isVisible = false   // Deixando o texto de falta de conexão invisível após o retorno

                    response.body()?.let { setupList(it) } // Depois de popular o carrosArray, executa a setupList() para mostrar os dados
                } else {
                    Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Carro>>, t: Throwable) {
                Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
            }

        })
    }

    fun emptyState() {
        progress.visibility = View.GONE
        listaCarros.visibility = View.GONE
        noWifiIcon.visibility = View.VISIBLE
        noWifiText.visibility = View.VISIBLE
    }

    fun setupView(view: View) {
        view.apply {
            listaCarros = findViewById(R.id.rv_lista_carros) // Encontrando a View reciclada
            fabCalcular = findViewById(R.id.fab_calcular)   // Encontrando o botão
            progress = findViewById(R.id.pb_loader) // Encontrando a barra de progresso
            noWifiIcon = findViewById(R.id.iv_empty_state)  // Encontrando o ícone de falta de conexão
            noWifiText = findViewById(R.id.tv_no_wifi)  // Encontrando o texto de falta de conexão
        }
    }

    fun setupList(lista: List<Carro>) {
        val carroAdapter =
            CarAdapter(lista) // Utilizando o adapter personalizado com os dados da array populada
        listaCarros.apply {
            adapter = carroAdapter
            isVisible = true   // Deixando a lista de carros visível
        }
        carroAdapter.carItemListener = { carro ->
            val bateria = carro.bateria
        }
    }

    fun setupListeners() {
        // Construindo a outra tela através do método startActivity(), que recebe como parâmetro uma intenção.
        // A intenção, por sua vez, recebe como argumento a origem e o destino, a fim de redirecionar para esta outra tela.
        fabCalcular.setOnClickListener {
            startActivity(Intent(context, CalcularAutonomiaActivity::class.java))
        }
    }

    fun checkForInternet(context: Context?) : Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun callService() {
        val urlBase = "https://raw.githubusercontent.com/henrique-molinos/cars-api/main/cars.json"
        MyTask().execute(urlBase)
        progress.visibility = View.VISIBLE  // Trazendo a barra de progresso na tela
    }

    // Utilizar o retrofit como abstração do AsyncTask! :)
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
                        urlPhoto = urlPhoto,
                        isFavorite = false
                    )
                    carrosArray.add(model)
                }
                progress.isVisible = false // Deixando a barra invisível após o retorno do serviço
                noWifiIcon.isVisible = false   // Deixando o ícone de falta de conexão invisível após o retorno
                noWifiText.isVisible = false   // Deixando o texto de falta de conexão invisível após o retorno
                // setupList() // Depois de popular o carrosArray, executa a setupList() para mostrar os dados
            } catch (ex: Exception) {
                Log.e("Erro ->", ex.message.toString())
            }
        }
    }
}
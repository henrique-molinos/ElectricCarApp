package com.henrique.electriccarapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.henrique.electriccarapp.R
import com.henrique.electriccarapp.data.CarFactory
import com.henrique.electriccarapp.ui.adapter.CarAdapter
import com.henrique.electriccarapp.ui.adapter.TabAdapter

class MainActivity : AppCompatActivity() {
    // Variáveis declaradas como lateinit, pois serão inicializadas posteriormente
    lateinit var tabLayout : TabLayout // Criação do TabLayout
    lateinit var viewPager : ViewPager2 // Criação do ViewPager para o Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()    // Chamada da função que setará as Views
        setupTabs() // Chamada da função que puxa as Tabs
    }

    // Implementação dos métodos de ciclos de vida
    override fun onResume() {
        super.onResume()
        Log.d("LifeCycle", "RESUME")
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle", "START")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycle", "PAUSE")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifeCycle", "STOP")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifeCycle", "DESTROY")
    }

    fun setupViews() {
        // Recuperando valores: utilizaremos o método findViewById para buscar uma View.
        // Esse método recebe como parâmetro um Resource. No Android, tudo que é referente à tela gera um identificador único dentro dos Resources
        // Quando trabalhamos com Android utilizando Kotlin, ao utilizar o método findViewById, precisamos informar o componente que estamos buscando com o <>, neste caso, EditText
        // No entanto, como a variável já foi criada anteriormente com a definição de seu tipo, não precisamos informar ao método o seguinte: findViewById<EditText>()
        tabLayout = findViewById(R.id.tab_layout)   // Encontrando o TabLayout
        viewPager = findViewById(R.id.vp_view_pager)    // Encontrando o ViewPager
    }

    fun setupTabs() {
        val tabsAdapter = TabAdapter(this)
        viewPager.adapter = tabsAdapter
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewPager.currentItem = it.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })
    }
}
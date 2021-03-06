package com.example.listadecontatos.feature.listacontatos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadecontatos.R
import com.example.listadecontatos.bases.BaseActivity
import com.example.listadecontatos.application.ContatoApplication
import com.example.listadecontatos.feature.contato.ContatoActivity
import com.example.listadecontatos.feature.listacontatos.adapter.ContatoAdapter
import com.example.listadecontatos.feature.listacontatos.model.ContatosVO
import com.example.listadecontatos.singleton.ContatoSingleton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private var adapter:ContatoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolBar(toolBar, "Lista de contatos",false)
        setupListView()
        setupOnClicks()
    }

    private fun setupOnClicks(){
        fab.setOnClickListener { onClickAdd() }
        ivBuscar.setOnClickListener { onClickBuscar() }
    }

    private fun setupListView(){
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onResume() {
        super.onResume()
        onClickBuscar()
    }

    private fun onClickAdd(){
        val intent = Intent(this,ContatoActivity::class.java)
        startActivity(intent)
    }

    private fun onClickItemRecyclerView(index: Int){
        val intent = Intent(this,ContatoActivity::class.java)
        intent.putExtra("index", index)
        startActivity(intent)
    }

    private fun onClickBuscar() {
        val busca = etBuscar.text.toString()
        progress.visibility = View.VISIBLE

        Thread(Runnable {
            Thread.sleep(500)

            var listaFiltrada: MutableList<ContatosVO> = mutableListOf()
            try {
                listaFiltrada = (ContatoApplication.instance.helperDB?.buscarContatos(busca) ?: mutableListOf()) as MutableList<ContatosVO>
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            runOnUiThread {
                adapter = ContatoAdapter(this, listaFiltrada) {onClickItemRecyclerView(it)}
                recyclerView.adapter = adapter
                progress.visibility = View.GONE
                Toast.makeText(this, "Buscando por $busca", Toast.LENGTH_SHORT).show()
            }

        }).start()
    }

}
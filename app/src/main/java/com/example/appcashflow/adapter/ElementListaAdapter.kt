package com.example.appcashflow.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.appcashflow.MainActivity
import com.example.appcashflow.R
import com.example.appcashflow.entity.Cadastro
import java.text.NumberFormat
import java.util.Locale

class ElementoListaAdapter(val context: Context, val registros: MutableList<Cadastro>) : BaseAdapter() {

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())

    override fun getCount(): Int {
        return registros.size
    }

    override fun getItem(pos: Int): Any {
        return registros[pos]
    }

    override fun getItemId(pos: Int): Long {
        return registros[pos].id.toLong()
    }

    override fun getView(
        pos: Int,
        componenteOrigem: View?,
        rootComponent: ViewGroup?
    ): View {
        val v = componenteOrigem ?: LayoutInflater.from(context).inflate(R.layout.elemento_lista, rootComponent, false)

        val tvNomeElementoLista: TextView = v.findViewById(R.id.tvNomeElementoLista)
        val tvDataElementoLista: TextView = v.findViewById(R.id.tvDataElementoLista)
        val tvValorElementoLista: TextView = v.findViewById(R.id.tvValorElementoLista)
        val imageView: ImageView = v.findViewById(R.id.imageView)
        val btEditarElementoLista: ImageButton = v.findViewById(R.id.btEditarElementoLista)

        val cadastro = registros[pos]

        tvNomeElementoLista.text = cadastro.nome
        tvDataElementoLista.text = cadastro.data

        val valorFormatado = currencyFormat.format(cadastro.valor)

        if (cadastro.despesa) {
            "- $valorFormatado".also { tvValorElementoLista.text = it }
            tvValorElementoLista.setTextColor(context.getColor(android.R.color.holo_red_dark))
            imageView.setImageResource(android.R.drawable.arrow_down_float)
            imageView.setColorFilter(context.getColor(android.R.color.holo_red_dark))
        } else {
            "+ $valorFormatado".also { tvValorElementoLista.text = it }
            tvValorElementoLista.setTextColor(context.getColor(android.R.color.holo_green_dark))
            imageView.setImageResource(android.R.drawable.arrow_up_float)
            imageView.setColorFilter(context.getColor(android.R.color.holo_green_dark))
        }

        btEditarElementoLista.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("id", cadastro.id)
                putExtra("nome", cadastro.nome)
                putExtra("valor", cadastro.valor)
                putExtra("data", cadastro.data)
                putExtra("despesa", cadastro.despesa)
            }
            context.startActivity(intent)
        }

        return v
    }
}
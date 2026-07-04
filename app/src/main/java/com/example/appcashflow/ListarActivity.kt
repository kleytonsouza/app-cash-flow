package com.example.appcashflow

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcashflow.adapter.ElementoListaAdapter
import com.example.appcashflow.database.DatabaseHandler
import com.example.appcashflow.databinding.ActivityListarBinding
import java.text.NumberFormat
import java.util.Locale

class ListarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListarBinding
    private lateinit var banco: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListarBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        banco = DatabaseHandler(this)

        binding.btIncluir.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        carregarRegistros()
    }

    private fun carregarRegistros() {
        val registros = banco.listar()

        if (registros.isEmpty()) {
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.lvCadastro.visibility = View.GONE
        } else {
            binding.tvEmptyState.visibility = View.GONE
            binding.lvCadastro.visibility = View.VISIBLE

            val adapter = ElementoListaAdapter(this, registros)
            binding.lvCadastro.adapter = adapter
        }

        var saldo = 0.0
        for (item in registros) {
            if (item.despesa) {
                saldo -= item.valor
            } else {
                saldo += item.valor
            }
        }

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        binding.tvSaldo.text = currencyFormat.format(saldo)

        if (saldo >= 0) {
            binding.tvSaldo.setTextColor(getColor(android.R.color.holo_green_dark))
        } else {
            binding.tvSaldo.setTextColor(getColor(android.R.color.holo_red_dark))
        }
    }
}

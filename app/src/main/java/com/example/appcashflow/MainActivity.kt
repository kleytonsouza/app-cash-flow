package com.example.appcashflow

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcashflow.database.DatabaseHandler
import com.example.appcashflow.databinding.ActivityMainBinding
import com.example.appcashflow.entity.Cadastro
import android.text.method.DigitsKeyListener
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var banco: DatabaseHandler
    private var editId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        banco = DatabaseHandler(this)

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        val symbol = currencyFormat.currency?.symbol ?: "R$"
        binding.tilValor.prefixText = "$symbol "
        binding.etValor.keyListener = DigitsKeyListener.getInstance(Locale.getDefault(), false, true)

        setupDatePicker()
        checkEditMode()

        binding.btnSalvar.setOnClickListener {
            salvar()
        }

        binding.btnVerLancamentos.setOnClickListener {
            val intent = Intent(this, ListarActivity::class.java)
            startActivity(intent)
        }

        binding.btnAlterar.setOnClickListener {
            alterar()
        }

        binding.btnExcluir.setOnClickListener {
            confirmarExclusao()
        }

        binding.btnCancelarEdicao.setOnClickListener {
            finish()
        }
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.etData.setText(sdf.format(calendar.time))
            binding.tilData.error = null
        }

        binding.etData.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkEditMode() {
        if (intent.hasExtra("id")) {
            editId = intent.getIntExtra("id", -1)
            val nome = intent.getStringExtra("nome") ?: ""
            val valor = intent.getDoubleExtra("valor", 0.0)
            val data = intent.getStringExtra("data") ?: ""
            val despesa = intent.getBooleanExtra("despesa", false)

            binding.tvSubTitle.text = "Editar Lançamento"
            binding.etDescricao.setText(nome)
            binding.etValor.setText(if (valor > 0) valor.toString() else "")
            binding.etData.setText(data)

            if (despesa) {
                binding.rbDespesa.isChecked = true
            } else {
                binding.rbReceita.isChecked = true
            }

            binding.layoutRegularActions.visibility = View.GONE
            binding.layoutEditActions.visibility = View.VISIBLE
        } else {
            editId = -1
            binding.tvSubTitle.text = "Novo Lançamento"
            binding.layoutRegularActions.visibility = View.VISIBLE
            binding.layoutEditActions.visibility = View.GONE
        }
    }

    private fun validarCampos(): Boolean {
        var valido = true

        val descricao = binding.etDescricao.text.toString().trim()
        if (descricao.isEmpty()) {
            binding.tilDescricao.error = getString(R.string.descricao_invalida)
            valido = false
        } else {
            binding.tilDescricao.error = null
        }

        val valorStr = binding.etValor.text.toString().trim().replace(",", ".")
        val valor = valorStr.toDoubleOrNull()
        if (valor == null || valor <= 0) {
            binding.tilValor.error = getString(R.string.valor_invalido)
            valido = false
        } else {
            binding.tilValor.error = null
        }

        val data = binding.etData.text.toString().trim()
        if (data.isEmpty()) {
            binding.tilData.error = getString(R.string.data_invalida)
            valido = false
        } else {
            binding.tilData.error = null
        }

        return valido
    }

    private fun salvar() {
        if (!validarCampos()) return

        val descricao = binding.etDescricao.text.toString().trim()
        val valorStr = binding.etValor.text.toString().trim().replace(",", ".")
        val valor = valorStr.toDouble()
        val data = binding.etData.text.toString()
        val isDespesa = binding.rbDespesa.isChecked

        val cadastro = Cadastro(
            nome = descricao,
            valor = valor,
            data = data,
            despesa = isDespesa
        )

        banco.incluir(cadastro)

        Toast.makeText(this, getString(R.string.salvar_sucesso), Toast.LENGTH_SHORT).show()
        
        limparCampos()

        val intent = Intent(this, ListarActivity::class.java)
        startActivity(intent)
    }

    private fun alterar() {
        if (!validarCampos() || editId == -1) return

        val descricao = binding.etDescricao.text.toString().trim()
        val valorStr = binding.etValor.text.toString().trim().replace(",", ".")
        val valor = valorStr.toDouble()
        val data = binding.etData.text.toString()
        val isDespesa = binding.rbDespesa.isChecked

        val cadastro = Cadastro(
            id = editId,
            nome = descricao,
            valor = valor,
            data = data,
            despesa = isDespesa
        )

        banco.alterar(cadastro)

        Toast.makeText(this, "Lançamento atualizado com sucesso!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun confirmarExclusao() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Exclusão")
            .setMessage(getString(R.string.excluir_confirmacao))
            .setPositiveButton("Excluir") { _, _ ->
                banco.excluir(editId)
                Toast.makeText(this, getString(R.string.excluir_sucesso), Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun limparCampos() {
        binding.etDescricao.text?.clear()
        binding.etValor.text?.clear()
        binding.etData.text?.clear()
        binding.rbReceita.isChecked = true
        binding.tilDescricao.error = null
        binding.tilValor.error = null
        binding.tilData.error = null
    }
}
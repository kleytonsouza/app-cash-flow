package com.example.appcashflow.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.appcashflow.entity.Cadastro

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(bd: SQLiteDatabase?) {
        bd?.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT, " +
                    "valor REAL, " +
                    "data TEXT, " +
                    "despesa INTEGER)"
        )
    }

    override fun onUpgrade(bd: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        bd?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(bd)
    }

    fun incluir(cadastro: Cadastro) {
        val banco = this.writableDatabase
        val registro = ContentValues().apply {
            put("nome", cadastro.nome)
            put("valor", cadastro.valor)
            put("data", cadastro.data)
            put("despesa", if (cadastro.despesa) 1 else 0)
        }
        banco.insert(TABLE_NAME, null, registro)
    }

    fun alterar(cadastro: Cadastro) {
        val banco = this.writableDatabase
        val registro = ContentValues().apply {
            put("nome", cadastro.nome)
            put("valor", cadastro.valor)
            put("data", cadastro.data)
            put("despesa", if (cadastro.despesa) 1 else 0)
        }
        banco.update(
            TABLE_NAME,
            registro,
            "_id = ?",
            arrayOf(cadastro.id.toString())
        )
    }

    fun excluir(id: Int) {
        val banco = this.writableDatabase
        banco.delete(
            TABLE_NAME,
            "_id = ?",
            arrayOf(id.toString())
        )
    }

    fun pesquisar(id: Int): Cadastro? {
        val banco = this.readableDatabase
        val cursor = banco.query(
            TABLE_NAME,
            null,
            "_id = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        var cadastro: Cadastro? = null
        if (cursor.moveToNext()) {
            cadastro = Cadastro(
                id = cursor.getInt(ID),
                nome = cursor.getString(NOME),
                valor = cursor.getDouble(VALOR),
                data = cursor.getString(DATA),
                despesa = cursor.getInt(DESPESA) == 1
            )
        }
        cursor.close()
        return cadastro
    }

    fun listar(): MutableList<Cadastro> {
        val banco = this.readableDatabase
        val cursor = banco.query(
            TABLE_NAME,
            null,
            null,
            null,
            null, null,
            "data DESC, _id DESC"
        )

        val lista = mutableListOf<Cadastro>()
        while (cursor.moveToNext()) {
            val cadastro = Cadastro(
                id = cursor.getInt(ID),
                nome = cursor.getString(NOME),
                valor = cursor.getDouble(VALOR),
                data = cursor.getString(DATA),
                despesa = cursor.getInt(DESPESA) == 1
            )
            lista.add(cadastro)
        }
        cursor.close()
        return lista
    }

    companion object {
        private const val DATABASE_NAME = "banco.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_NAME = "cadastro"
        private const val ID = 0
        private const val NOME = 1
        private const val VALOR = 2
        private const val DATA = 3
        private const val DESPESA = 4
    }
}
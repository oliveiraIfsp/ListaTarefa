package br.edu.ifsp.listatarefa

import android.app.Application
import br.edu.ifsp.listatarefa.data.ListaDatabase
import br.edu.ifsp.listatarefa.repository.ListaRepository

class ListaApplication:Application() {
    val database by lazy { ListaDatabase.getDatabase(this) }
    val repository by lazy { ListaRepository(database.listaDAO()) }
}
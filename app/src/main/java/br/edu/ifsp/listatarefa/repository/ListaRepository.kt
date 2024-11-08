package br.edu.ifsp.listatarefa.repository


import br.edu.ifsp.listatarefa.data.ListaDAO
import br.edu.ifsp.listatarefa.domain.Lista
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map


class ListaRepository (private val listaDAO: ListaDAO) {

    suspend fun insert(lista: Lista){
        listaDAO.insert(lista.toEntity())
    }

    suspend fun update(lista: Lista){
        listaDAO.update(lista.toEntity())
    }

    suspend fun delete(lista: Lista){
        listaDAO.delete(lista.toEntity())
    }

    fun getAllContacts(): Flow<List<Lista>> {
        return listaDAO.getAllContacts().map { list ->
            list.map {
                it.toDomain()
            }
        }

    }


    fun getContactById(id: Int): Flow<Lista>{
        return listaDAO.getContactById(id).filterNotNull().map {it.toDomain()}
    }


}

package br.edu.ifsp.listatarefa.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ListaDAO {
    @Insert
    suspend fun insert(listaEntity: ListaEntity)

    @Update
    suspend fun update (listaEntity: ListaEntity)

    @Delete
    suspend fun delete(listaEntity: ListaEntity)

    @Query("SELECT * FROM listas ORDER BY tarefa")
    fun getAllContacts(): Flow<List<ListaEntity>>

    @Query("SELECT * FROM listas WHERE id=:id")
    fun getContactById(id: Int): Flow<ListaEntity>


}
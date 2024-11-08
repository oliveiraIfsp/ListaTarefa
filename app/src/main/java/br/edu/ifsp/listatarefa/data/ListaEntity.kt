package br.edu.ifsp.listatarefa.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.edu.ifsp.listatarefa.domain.Lista


@Entity(tableName = "Listas")
data class ListaEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val tarefa:String,
    val descricao:String,
    val observacao:String
)
{
    fun toDomain():Lista{
        return Lista(id, tarefa, descricao,observacao)
    }
}
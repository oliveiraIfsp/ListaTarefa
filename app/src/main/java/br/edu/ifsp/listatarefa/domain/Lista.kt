package br.edu.ifsp.listatarefa.domain

import br.edu.ifsp.listatarefa.data.ListaEntity

data class Lista (
    var id:Int=0,
    var tarefa:String,
    var descricao:String,
    var observacao:String

){
    fun toEntity():ListaEntity{
        return ListaEntity(id,tarefa,descricao,observacao)
    }
}


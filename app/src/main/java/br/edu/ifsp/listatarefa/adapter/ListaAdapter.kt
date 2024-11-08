package br.edu.ifsp.listatarefa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.listatarefa.databinding.ListaCelulaBinding
import br.edu.ifsp.listatarefa.domain.Lista

class ListaAdapter: RecyclerView.Adapter<ListaAdapter.ListaViewHolder>(),
    Filterable {
    var onItemClick: ((Lista)->Unit) ?= null
    var listasLista = ArrayList<Lista>()
    var listasListaFilterable = ArrayList<Lista>()
    private lateinit var binding: ListaCelulaBinding

  fun updateList(newList: List<Lista> ){
        listasLista = newList as ArrayList<Lista>
        listasListaFilterable = listasLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListaViewHolder {
        binding = ListaCelulaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  ListaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListaViewHolder, position: Int) {
        holder.tarefaVH.text = listasListaFilterable[position].tarefa
        holder.descricaoVH.text = listasListaFilterable[position].descricao
    }
    override fun getItemCount(): Int {
        return listasListaFilterable.size
    }

    inner class ListaViewHolder(view: ListaCelulaBinding): RecyclerView.ViewHolder(view.root)
    {
        val tarefaVH = view.tarefa
        val descricaoVH = view.descricao

        init {
            view.root.setOnClickListener {
                onItemClick?.invoke(listasListaFilterable[adapterPosition])
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                if (p0.toString().isEmpty())
                    listasListaFilterable = listasLista
                else
                {
                    val resultList = ArrayList<Lista>()
                    for (row in listasLista)
                        if (row.tarefa.lowercase().contains(p0.toString().lowercase()))
                            resultList.add(row)
                    listasListaFilterable = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = listasListaFilterable
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                listasListaFilterable = p1?.values as ArrayList<Lista>
                notifyDataSetChanged()
            }
        }
    }
}
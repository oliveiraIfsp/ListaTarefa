package br.edu.ifsp.listatarefa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.edu.ifsp.listatarefa.R
import br.edu.ifsp.listatarefa.adapter.ListaAdapter
import br.edu.ifsp.listatarefa.databinding.FragmentListaListasBinding
import br.edu.ifsp.listatarefa.domain.Lista
import br.edu.ifsp.listatarefa.viewmodel.ListaViewModel
import br.edu.ifsp.listatarefa.viewmodel.ListaState
import kotlinx.coroutines.launch

class ListaListasFragment : Fragment(){

    private var _binding: FragmentListaListasBinding? = null
    private val binding get() = _binding!!
    lateinit var listaAdapter: ListaAdapter
    val viewModel : ListaViewModel by viewModels { ListaViewModel.listaViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        _binding = FragmentListaListasBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener { findNavController().navigate(R.id.action_listaListasFragment_to_cadastroFragment) }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllContacts()
        setupViewModel()
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)

                val searchView = menu.findItem(R.id.action_search).actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        TODO("Not yet implemented")
                    }
                    override fun onQueryTextChange(p0: String?): Boolean {
                        listaAdapter.filter.filter(p0)
                        return true
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                TODO("Not yet implemented")
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
    private fun setupViewModel(){
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.stateList.collect{
                when(it){
                    is ListaState.SearchAllSuccess -> {
                        setupRecyclerView(it.listas)
                    }
                    ListaState.ShowLoading -> {}
                    ListaState.EmptyState -> {binding.textEmptyList.visibility=View.VISIBLE}
                }
            }
        }
    }
    private fun setupRecyclerView(contactList: List<Lista>)
    {
        listaAdapter = ListaAdapter().apply { updateList(contactList) }
        binding.recyclerview.adapter = listaAdapter
        listaAdapter.onItemClick = {it ->
            val bundle = Bundle()
            bundle.putInt("idLista", it.id)
            findNavController().navigate(
                R.id.action_listaListasFragment_to_detalheFragment,
                bundle
            )
        }
    }
}

package br.edu.ifsp.listatarefa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.edu.ifsp.listatarefa.R
import br.edu.ifsp.listatarefa.databinding.FragmentDetalheBinding
import br.edu.ifsp.listatarefa.domain.Lista
import br.edu.ifsp.listatarefa.viewmodel.ListaViewModel
import br.edu.ifsp.listatarefa.viewmodel.DetalheState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class DetalheFragment : Fragment() {
    private var _binding: FragmentDetalheBinding? = null
    private val binding get() = _binding!!
    lateinit var lista: Lista
    lateinit var tarefaEditText: EditText
    lateinit var descricaoEditText: EditText
    lateinit var observacaoEditText: EditText

    val viewModel : ListaViewModel by viewModels { ListaViewModel.listaViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetalheBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tarefaEditText = binding.commonLayout.editTextTarefa
        descricaoEditText = binding.commonLayout.editTextDescricao
        observacaoEditText = binding.commonLayout.editTextObservacao

        val idLista = requireArguments().getInt("idLista")
        viewModel.getContactById(idLista)

        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.stateDetail.collect {
                when (it) {
                    DetalheState.DeleteSuccess -> {
                        Snackbar.make(
                            binding.root,
                            "Tarefa removida com sucesso",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }

                    is DetalheState.GetByIdSuccess -> {
                        fillFields(it.c)
                    }
                    DetalheState.ShowLoading -> {}
                    DetalheState.UpdateSuccess -> {
                        Snackbar.make(
                            binding.root,
                            "Tarefa alterada com sucesso",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
                }
            }
        }
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
               menuInflater.inflate(R.menu.detalhe_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
               return when (menuItem.itemId) {
                    R.id.action_alterarLista -> {
                        lista.tarefa=tarefaEditText.text.toString()
                        lista.descricao=descricaoEditText.text.toString()
                        lista.observacao=observacaoEditText.text.toString()
                        viewModel.update(lista)
                        true
                    }
                    R.id.action_excluirLista ->{
                        viewModel.delete(lista)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
    private fun fillFields(c: Lista) {
        lista=c
        tarefaEditText.setText(lista.tarefa)
        descricaoEditText.setText(lista.descricao)
        observacaoEditText.setText(lista.observacao)
    }
}

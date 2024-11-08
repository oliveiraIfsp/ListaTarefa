package br.edu.ifsp.listatarefa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.edu.ifsp.listatarefa.R
import br.edu.ifsp.listatarefa.databinding.FragmentCadastroBinding
import br.edu.ifsp.listatarefa.domain.Lista
import br.edu.ifsp.listatarefa.viewmodel.ListaViewModel
import br.edu.ifsp.listatarefa.viewmodel.CadastroState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class CadastroFragment : Fragment() {
    private var _binding: FragmentCadastroBinding? = null
    private val binding get() = _binding!!

    val viewModel : ListaViewModel by viewModels { ListaViewModel.listaViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateCadastro.collect {
                when (it) {
                    CadastroState.InsertSuccess -> {
                        Snackbar.make(
                            binding.root,
                            "Tarefa inserida com sucesso",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }

                    CadastroState.ShowLoading -> {}
                }
            }
        }


        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.cadastro_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return when (menuItem.itemId) {
                    R.id.action_salvarLista -> {
                        val tarefa = binding.commonLayout.editTextTarefa.text.toString()
                        val descricao = binding.commonLayout.editTextDescricao.text.toString()
                        val observacao = binding.commonLayout.editTextObservacao.text.toString()

                        val lista = Lista(tarefa=tarefa, descricao=descricao, observacao=observacao)
                        viewModel.insert(lista)

                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }
}

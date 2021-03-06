package jp.kaleidot725.emomemo.ui.memo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import jp.kaleidot725.emomemo.R
import jp.kaleidot725.emomemo.databinding.FragmentEditMemoBinding
import jp.kaleidot725.emomemo.ui.extension.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditMemoDialogFragment : DialogFragment(R.layout.fragment_edit_memo) {
    private val navController: NavController get() = findNavController()
    private val binding: FragmentEditMemoBinding by viewBinding()
    private val editMemoDialogViewModel: EditMemoDialogViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = editMemoDialogViewModel
        editMemoDialogViewModel.isCompleted.observe(viewLifecycleOwner, Observer { navController.popBackStack() })
    }
}

package ru.catstack.asktrump.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.ask_trump_fragment.*
import ru.catstack.asktrump.R
import ru.catstack.asktrump.viewmodels.TrumpImageState
import ru.catstack.asktrump.viewmodels.AskTrumpViewModel

class AskTrumpFragment : Fragment() {

    private val viewModel = AskTrumpViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ask_trump_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.subscribe {
            val isFieldsBlocked = it == TrumpImageState.LOADING
            blockFields(isFieldsBlocked)

            setTrumpImage(it)
        }

        askButton.setOnClickListener { viewModel.getAnswer() }

        questionTextField.addTextChangedListener { viewModel.textFieldChanged() }

        viewModel.adRequest.subscribe(adView::loadAd)
        viewModel.loadAd()
    }

    private fun setTrumpImage(state: TrumpImageState) {
        trumpImageView.setImageResource(
            when(state) {
                TrumpImageState.NO -> R.drawable.trump_no
                TrumpImageState.YES -> R.drawable.trump_yes
                TrumpImageState.DUNNO -> R.drawable.trump_idk
                TrumpImageState.LOADING -> R.drawable.trump_thinking
                TrumpImageState.NO_QUESTION -> R.drawable.trump_waiting
            }
        )
    }

    private fun blockFields(isBlocked: Boolean) {
        askButton.isEnabled = !isBlocked
        askButton.isCursorVisible = !isBlocked
        questionTextField.isEnabled = !isBlocked
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuInfoButton -> {
                onMenuInfoButtonClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onMenuInfoButtonClicked() {
        val dialogBuilder = AlertDialog.Builder(context!!)

        dialogBuilder.apply {
            setTitle(R.string.info_title)
            setMessage(R.string.info_message)
            show()
        }
    }
}
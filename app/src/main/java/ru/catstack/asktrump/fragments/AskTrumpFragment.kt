package ru.catstack.asktrump.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.ask_trump_fragment.*
import ru.catstack.asktrump.R
import ru.catstack.asktrump.viewmodels.AskTrumpFragmentState
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

        viewModel.state.subscribe(::questionStateProcessing)

        askButton.setOnClickListener { viewModel.getAnswer() }

        questionTextField.addTextChangedListener { viewModel.textFieldChanged() }

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun questionStateProcessing(state: AskTrumpFragmentState) {
        when (state) {
            AskTrumpFragmentState.NO_QUESTION -> {
                unblockFields()
                trumpImageView.setImageResource(R.drawable.trump_waiting)
            }
            AskTrumpFragmentState.YES -> {
                unblockFields()
                trumpImageView.setImageResource(R.drawable.trump_yes)
            }
            AskTrumpFragmentState.NO -> {
                unblockFields()
                trumpImageView.setImageResource(R.drawable.trump_no)
            }
            AskTrumpFragmentState.DUNNO -> {
                unblockFields()
                trumpImageView.setImageResource(R.drawable.trump_idk)
            }
            AskTrumpFragmentState.LOADING -> {
                blockFields()
                trumpImageView.setImageResource(R.drawable.trump_thinking)
            }
        }
    }

    private fun unblockFields() {
        askButton.isEnabled = true
        askButton.isCursorVisible = true
        questionTextField.isEnabled = true
    }

    private fun blockFields() {
        askButton.isEnabled = false
        askButton.isCursorVisible = false
        questionTextField.isEnabled = false
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
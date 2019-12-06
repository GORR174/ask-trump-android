package ru.catstack.asktrump.presentation.asktrump

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.ask_trump_fragment.*
import ru.catstack.asktrump.R

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

        viewModel.questionAnswer.observe(this, Observer(this::setTrumpImage))

        askButton.setOnClickListener { viewModel.getAnswer(questionTextField.text.toString()) }

        questionTextField.addTextChangedListener { viewModel.textFieldChanged() }

        viewModel.adRequest.observe(this, Observer(adView::loadAd))
        viewModel.loadAd()
    }

    private fun setTrumpImage(state: AnswerState) {
        val imageResource = when (state) {
            AnswerState.NO -> R.drawable.trump_no
            AnswerState.YES -> R.drawable.trump_yes
            AnswerState.DUNNO -> R.drawable.trump_idk
            AnswerState.NO_QUESTION -> R.drawable.trump_waiting
        }

        if (state == AnswerState.NO_QUESTION) {
            trumpImageView.setImageResource(imageResource)
        } else {
            blockFields(true)
            trumpImageView.setImageResource(R.drawable.trump_thinking)

            Handler().postDelayed({
                trumpImageView.setImageResource(imageResource)
                blockFields(false)
            }, 2500)
        }
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
        AlertDialog.Builder(context!!).apply {
            setTitle(R.string.info_title)
            setMessage(R.string.info_message)
            setPositiveButton(R.string.info_cancel_button) { _, _ -> }
            setNegativeButton(R.string.info_rate_button) { _, _ ->
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=ru.catstack.asktrump")
                )
                startActivity(browserIntent)
            }
            show()
        }
    }
}
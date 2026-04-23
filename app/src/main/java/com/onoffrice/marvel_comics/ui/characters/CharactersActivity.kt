package com.onoffrice.marvel_comics.ui.characters

import android.content.Context
import android.content.Intent
import android.nfc.tech.MifareUltralight
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onoffrice.marvel_comics.R
import com.onoffrice.marvel_comics.data.remote.model.Character
import com.onoffrice.marvel_comics.databinding.ActivityCharactersBinding
import com.onoffrice.marvel_comics.ui.base.BaseActivity
import com.onoffrice.marvel_comics.ui.characterDetail.createCharacterDetailActivityIntent
import com.onoffrice.marvel_comics.utils.extensions.isNetworkConnected
import com.onoffrice.marvel_comics.utils.extensions.startActivitySlideTransition
import org.koin.android.ext.android.inject

class CharactersActivity : BaseActivity(null) {

    private val viewModel by inject<CharactersViewModel>()
    private lateinit var binding: ActivityCharactersBinding

    private var isLoading: Boolean = false

    private val charactersAdapter: CharactersAdapter by lazy {
        val adapter = CharactersAdapter(object : CharactersAdapter.CharacterClickListener{
            override fun onClickCharacter(character: Character) {
                startActivitySlideTransition(createCharacterDetailActivityIntent(character)
                )
            }
        })

        val layoutManager          = GridLayoutManager(this, 2)
        binding.charactersRv.layoutManager = layoutManager
        binding.charactersRv.adapter       = adapter
        binding.charactersRv.addOnScrollListener(onScrollListener())
        adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharactersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setObservers()
        setListeners()
        getCharacters()

    }

    private fun setListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            charactersAdapter.resetList()
            getCharacters()
        }
    }

    private fun onScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount         = recyclerView.layoutManager?.childCount ?: 0
                val totalItemCount           = recyclerView.layoutManager?.itemCount ?: 0
                val firstVisibleItemPosition = (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()

                if (!isLoading && visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= MifareUltralight.PAGE_SIZE
                ) {
                    isLoading = true
                    displayLoading(isLoading)
                    getCharacters(totalItemCount)
                }
            }
        }
    }

    private fun getCharacters(totalItemCount: Int? = 0) {
        if (isNetworkConnected()) {
            viewModel.getCharacters(totalItemCount)
        } else {
            Toast.makeText(this, getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setObservers() {
        viewModel.run {
            characters.observe(this@CharactersActivity,  Observer { displayCharactersList(it)})
            loadingEvent.observe(this@CharactersActivity,Observer { displayLoading(it) })
            errorEvent.observe(this@CharactersActivity,  Observer { displayError(it) })
        }
    }

    private fun displayCharactersList(characters: List<Character>) {
        isLoading = false
        charactersAdapter.setCharacters(characters)
    }

    private fun displayLoading(loading: Boolean) {
        binding.swipeRefresh.isRefreshing = loading
    }

    private fun displayError(message: String) {
      Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
fun Context.createCharactersActivityIntent() = Intent(this, CharactersActivity::class.java)

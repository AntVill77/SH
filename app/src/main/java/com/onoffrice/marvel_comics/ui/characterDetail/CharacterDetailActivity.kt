package com.onoffrice.marvel_comics.ui.characterDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.onoffrice.marvel_comics.Constants
import com.onoffrice.marvel_comics.R
import com.onoffrice.marvel_comics.data.remote.model.Character
import com.onoffrice.marvel_comics.data.remote.model.ComicModel
import com.onoffrice.marvel_comics.databinding.ActivityCharacterDetailBinding
import com.onoffrice.marvel_comics.ui.base.BaseActivity
import com.onoffrice.marvel_comics.ui.mostExpensiveComic.createMostExpensiveComicIntent
import com.onoffrice.marvel_comics.utils.extensions.isNetworkConnected
import com.onoffrice.marvel_comics.utils.extensions.loadImage
import com.onoffrice.marvel_comics.utils.extensions.setVisible
import com.onoffrice.marvel_comics.utils.extensions.startActivitySlideTransition
import org.koin.android.ext.android.inject

class CharacterDetailActivity : BaseActivity(null) {

    private val viewModel by inject<CharacterDetailViewModel>()
    private lateinit var binding: ActivityCharacterDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel.getExtras(intent.extras)
        setObservers()
        setListeners()
    }

    private fun setListeners() {
        binding.mostExpensiveComicBtn.setOnClickListener {
            if (isNetworkConnected()) {
                viewModel.getCharacterComics()
            } else {
                Toast.makeText(this, getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setObservers() {
        viewModel.run {
            character.observe(this@CharacterDetailActivity,    Observer { displayCharactersDetail(it) })
            comic.observe(this@CharacterDetailActivity,        Observer { openMostExpensiveCharacterComics(it) })
            loadingEvent.observe(this@CharacterDetailActivity, Observer { displayLoading(it) })
            errorEvent.observe(this@CharacterDetailActivity,   Observer { displayError(it) })
        }
    }

    private fun openMostExpensiveCharacterComics(comic: ComicModel) {
        startActivitySlideTransition(createMostExpensiveComicIntent(comic))
    }

    private fun displayCharactersDetail(character: Character) {
        setToolbar(character.name,true)
        binding.characterBio.text  = character.description

        //Load's the character poster using Picasso
        binding.characterPoster.loadImage(character.thumbnail.getPathExtension())
    }

    private fun displayLoading(loading: Boolean) {
        binding.progressBar.setVisible(loading)
    }

    private fun displayError(message: String) {
      Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
fun Context.createCharacterDetailActivityIntent(character: Character) =
    Intent(this, CharacterDetailActivity::class.java).apply {
        putExtra(Constants.EXTRA_CHARACTER_DETAIL, character)
    }

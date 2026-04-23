package com.onoffrice.marvel_comics.ui.mostExpensiveComic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.onoffrice.marvel_comics.Constants
import com.onoffrice.marvel_comics.R
import com.onoffrice.marvel_comics.data.remote.model.ComicModel
import com.onoffrice.marvel_comics.databinding.ActivityMostExpensiveComicBinding
import com.onoffrice.marvel_comics.ui.base.BaseActivity
import com.onoffrice.marvel_comics.utils.extensions.loadImage
import com.onoffrice.marvel_comics.utils.extensions.setVisible
import org.koin.android.ext.android.inject

class MostExpensiveComicActivity : BaseActivity(null) {

    private val viewModel by inject<MostExpensiveComicViewModel>()
    private lateinit var binding: ActivityMostExpensiveComicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMostExpensiveComicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel.getExtras(intent.extras)
        setObservers()
        setListeners()
    }

    private fun setListeners() {
    }

    private fun setObservers() {
        viewModel.run {
            comic.observe(this@MostExpensiveComicActivity, Observer { displayComicDetail(it)})
            loadingEvent.observe(this@MostExpensiveComicActivity,Observer { displayLoading(it) })
            errorEvent.observe(this@MostExpensiveComicActivity, Observer { displayError(it) })
        }
    }

    private fun displayComicDetail(comic: ComicModel) {
        binding.comicPoster.loadImage(comic.thumbnail.getPathExtension())
        binding.comicTitle.text       = comic.title
        binding.comicPrice.text       = getString(R.string.comic_price, comic.prices[0].price.toString())
        binding.comicDescription.text = comic.description
    }

    private fun displayLoading(loading: Boolean) {
        binding.progressBar.setVisible(loading)
    }

    private fun displayError(message: String) {
      Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
fun Context.createMostExpensiveComicIntent(comic: ComicModel) =
    Intent(this, MostExpensiveComicActivity::class.java).apply {
        putExtra(Constants.EXTRA_COMIC, comic)
    }

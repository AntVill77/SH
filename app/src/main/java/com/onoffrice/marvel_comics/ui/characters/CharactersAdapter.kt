package com.onoffrice.marvel_comics.ui.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onoffrice.marvel_comics.data.remote.model.Character
import com.onoffrice.marvel_comics.databinding.AdapterCharacterItemBinding
import com.onoffrice.marvel_comics.utils.extensions.fadeUpItemListAnimation
import com.onoffrice.marvel_comics.utils.extensions.loadImage

class CharactersAdapter (private val listener: CharacterClickListener?): RecyclerView.Adapter<CharactersAdapter.GameViewHolderItem>() {

    interface CharacterClickListener {
        fun onClickCharacter(character: Character)
    }

    var list: MutableList<Character> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun setCharacters(characters: List<Character>) {
        list.addAll(characters)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): GameViewHolderItem {
        val binding = AdapterCharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolderItem(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GameViewHolderItem, position: Int) {
        holder.itemView.fadeUpItemListAnimation(position,200)

        val characterItem = list[position]

        characterItem.let {
            holder.binding.title.text = characterItem.name
            holder.binding.poster.loadImage(characterItem.thumbnail.getPathExtension())

            // Character click listener
            holder.itemView.setOnClickListener {
                listener?.onClickCharacter(characterItem)
            }
        }
    }

    fun resetList() {
        list.clear()
        notifyDataSetChanged()
    }

    class GameViewHolderItem(val binding: AdapterCharacterItemBinding) : RecyclerView.ViewHolder(binding.root)
}

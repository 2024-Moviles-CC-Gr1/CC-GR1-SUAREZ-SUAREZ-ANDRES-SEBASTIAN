package com.sebitas.spotify

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sebitas.spotify.databinding.ItemSongBinding
import com.squareup.picasso.Picasso

class SongAdapter(private val songs: List<Song>) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song)
    }

    override fun getItemCount() = songs.size

    class SongViewHolder(private val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.songTitle.text = song.title
            binding.songArtist.text = song.artist
            Picasso.get().load(song.imageUrl).into(binding.songImage)
        }
    }
}

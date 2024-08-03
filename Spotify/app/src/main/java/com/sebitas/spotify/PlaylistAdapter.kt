package com.sebitas.spotify

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sebitas.spotify.databinding.ItemPlaylistBinding
import com.squareup.picasso.Picasso

class PlaylistAdapter(private val playlists: List<Playlist>) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
    }

    override fun getItemCount() = playlists.size

    class PlaylistViewHolder(private val binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) {
            binding.playlistName.text = playlist.name
            Picasso.get().load(playlist.imageUrl).into(binding.playlistImage)
        }
    }
}

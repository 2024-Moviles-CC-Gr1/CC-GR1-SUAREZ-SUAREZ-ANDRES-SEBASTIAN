package com.sebitas.spotify

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sebitas.spotify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var songAdapter: SongAdapter
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val songs = listOf(
            Song("Se que te duele", "Alejandro Fernandez", "https://i.ytimg.com/vi/TqGN9Wbitfg/maxresdefault.jpg"),
            Song("Ella me levanto", "Daddy Yankee", "https://i.ytimg.com/vi/0rzyNkfwlAA/maxresdefault.jpg"),
            Song("It will rain", "Bruno Mars", "https://images.rapgenius.com/9e8626ac8067fa31e46d2d31e25034f0.600x600x1.jpg"),
            Song("Kilometros", "Los Caligaris", "https://i.ytimg.com/vi/pW9MJdTnl5E/maxresdefault.jpg"),
            Song("Ya no me acuerdo", "Estopa", "https://i.ytimg.com/vi/DRM6BTcZ8xQ/maxresdefault.jpg"),
            Song("Don't Cry", "Guns N' Roses", "https://cdn.wallpapersafari.com/86/68/riN340.jpg"),
            Song("La planta", "Caos", "https://i.ytimg.com/vi/nPY9ZGg34so/maxresdefault.jpg"),
            Song("Poli", "Zoe", "https://i.ytimg.com/vi/aQi2GsX5WQg/hqdefault.jpg"),
            Song("Never Better", "Fuller", "https://i.scdn.co/image/ab67616d0000b273716bd476e5a464fc5ca9a719")
        )

        val playlists = listOf(
            Playlist("Playlist Rock", "https://routenote.com/blog/wp-content/uploads/2021/08/Top-10-rock-copy-scaled.jpg"),
            Playlist("Playlist Reggae", "https://i.ytimg.com/vi/YCwJfYAxkg4/maxresdefault.jpg"),
            Playlist("Playlist Pop", "https://routenote.com/blog/wp-content/uploads/2021/07/Top-10-pop-playlists-1-scaled.jpg")
        )

        songAdapter = SongAdapter(songs)
        playlistAdapter = PlaylistAdapter(playlists)

        binding.recyclerViewSongs.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = songAdapter
        }

        binding.recyclerViewPlaylists.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = playlistAdapter
        }
    }
}

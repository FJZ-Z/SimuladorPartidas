package com.fjz.simuladorpartidas.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fjz.simuladorpartidas.databinding.ActivityDetailBinding
import com.fjz.simuladorpartidas.domain.Match

class DetailActivity : AppCompatActivity() {
    object Extras {
        const val MATCH = "EXTRA_MATCH"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadMatchFromExtra()
    }

    private fun loadMatchFromExtra() {
        intent?.extras?.getParcelable<Match>(Extras.MATCH)?.let {
            Glide.with(this).load(it.place.image).into(binding.ivPlace)
            supportActionBar?.title = it.place.name

            binding.tvTimeDeCasaNome.text = it.homeTeam.name
            binding.rbHomeTeamStars.rating = it.homeTeam.stars.toFloat()
            Glide.with(this).load(it.homeTeam.image).into(binding.ivTimeDeCasa)
            if (it.homeTeam.score != null) {
                binding.tvTimeDeCasaGols.text = it.homeTeam.score.toString()
            }

            binding.rbAwayTeamStars.rating = it.awayTeam.stars.toFloat()
            binding.tvTimeContraNome.text = it.awayTeam.name
            Glide.with(this).load(it.awayTeam.image).into(binding.ivTimeContra)
            if (it.awayTeam.score != null) {
                binding.tvTimeContraGols.text = it.awayTeam.score.toString()
            }
        }
    }
}
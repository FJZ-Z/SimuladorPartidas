package com.fjz.simuladorpartidas.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fjz.simuladorpartidas.R
import com.fjz.simuladorpartidas.data.MatchesAPI
import com.fjz.simuladorpartidas.databinding.ActivityMainBinding
import com.fjz.simuladorpartidas.domain.Match
import com.fjz.simuladorpartidas.ui.adapter.MatchesAdapter
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var matchesApi: MatchesAPI
    private lateinit var binding: ActivityMainBinding
    private var matchesAdapter = MatchesAdapter(emptyList())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupHttpClient()
        setupMatchesList()
        setupMatchRefresh()
        setupFloatingActionButton()
    }

    private fun setupHttpClient() {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://digitalinnovationone.github.io/matches-simulator-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        matchesApi = retrofit.create(MatchesAPI::class.java)
    }

    private fun setupMatchesList() {
        binding.rvPartidas.setHasFixedSize(true)
        binding.rvPartidas.layoutManager = LinearLayoutManager(this)
        binding.rvPartidas.adapter = matchesAdapter
        findMacthesFromApi()
    }

    private fun setupMatchRefresh() {
        binding.srlPartidas.setOnRefreshListener(this::findMacthesFromApi)
    }

    private fun setupFloatingActionButton() {
        binding.fabSimular.setOnClickListener { view ->
            view.animate().rotationBy(360f).setDuration(100)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        for (i in 0 until matchesAdapter.itemCount) {
                            val match: Match = matchesAdapter.matches[i]
                            match.homeTeam.score = (0..match.homeTeam.stars).random()
                            match.awayTeam.score = (0..match.awayTeam.stars).random()
                            matchesAdapter.notifyItemChanged(i)
                        }

                    }
                })
        }
    }

    private fun findMacthesFromApi() {
        binding.srlPartidas.isRefreshing = true
        val callback: Call<List<Match>> = matchesApi.getMatches()
        callback.enqueue(object : Callback<List<Match>> {
            override fun onResponse(call: Call<List<Match>>, response: Response<List<Match>>) {
                if (response.isSuccessful) {
                    val matches = response.body()
                    matchesAdapter = MatchesAdapter(matches as List<Match>)
                    binding.rvPartidas.adapter = matchesAdapter
                } else {
                    showErrorMessage()
                }
                binding.srlPartidas.isRefreshing = false
            }

            override fun onFailure(call: Call<List<Match>>, t: Throwable) {
                showErrorMessage()
                binding.srlPartidas.isRefreshing = false
            }
        })
    }

    private fun showErrorMessage() {
        Snackbar.make(binding.fabSimular, R.string.error_api, Snackbar.LENGTH_SHORT).show()
    }
}
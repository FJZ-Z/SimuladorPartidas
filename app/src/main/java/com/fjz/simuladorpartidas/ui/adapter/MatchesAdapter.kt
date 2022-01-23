package com.fjz.simuladorpartidas.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fjz.simuladorpartidas.databinding.ItemPartidaBinding
import com.fjz.simuladorpartidas.domain.Match
import com.fjz.simuladorpartidas.ui.DetailActivity

class MatchesAdapter(val matches: List<Match>) : RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPartidaBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val match: Match = matches[position]
        holder.binding.tvTimeDeCasaNome.text = match.homeTeam.name
        if (match.homeTeam.score != null) {
            holder.binding.tvTimeDeCasaGols.text = match.homeTeam.score.toString()
        }
        //imagem normal
        //Glide.with(context).load(match.homeTeam.image).into(holder.binding.ivTimeDeCasa)
        //imagem redonda
        Glide.with(context).load(match.homeTeam.image).circleCrop()
            .into(holder.binding.ivTimeDeCasa)

        holder.binding.tvTimeContraNome.text = match.awayTeam.name
        if (match.awayTeam.score != null) {
            holder.binding.tvTimeContraGols.text = match.awayTeam.score.toString()
        }
        //imagem normal
        //Glide.with(context).load(match.awayTeam.image).into(holder.binding.ivTimeContra)
        //imagem redonda
        Glide.with(context).load(match.awayTeam.image).circleCrop()
            .into(holder.binding.ivTimeContra)
        holder.itemView.setOnClickListener { view: View ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.Extras.MATCH, match)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return matches.size
    }

    class ViewHolder(binding: ItemPartidaBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding: ItemPartidaBinding = binding
    }
}
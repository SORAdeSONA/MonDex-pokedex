package com.sds.pokemonguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sds.pokemonguide.R
import com.sds.pokemonguide.model.CommonDamageList


class CommonDamageRelationsAdapter : RecyclerView.Adapter<CommonDamageRelationsAdapter.DataViewHolder>() {

    private var dmg: ArrayList<CommonDamageList> = ArrayList()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.damage_from_item_rv, parent,
                false
            )
        )

    override fun getItemCount(): Int = dmg.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val everyDmgName = dmg[position].name

        fun getImageByType(type: String): Int {
            when (type) {
                "grass" -> return R.drawable.icon_grass
                "fire" -> return R.drawable.type_fire
                "poison" -> return R.drawable.type_poison
                "fighting" -> return R.drawable.type_fighting
                "flying" -> return R.drawable.type_flying
                "ground" -> return R.drawable.type_ground
                "rock" -> return R.drawable.type_rock
                "bug" -> return R.drawable.type_bug
                "ghost" -> return R.drawable.type_ghost
                "steel" -> return R.drawable.type_steel
                "water" -> return R.drawable.type_water
                "electric" -> return R.drawable.electir_type_
                "psychic" -> return R.drawable.type_psychic
                "ice" -> return R.drawable.type_ice
                "dragon" -> return R.drawable.type_dragon
                "dark" -> return R.drawable.type_dark
                "fairy" -> return R.drawable.type_fairy
                "normal" -> return R.drawable.type_normal
            }
            return R.drawable.type_normal
        }

        holder.itemView.setOnClickListener {
            onItemClickListenerUrl?.let {
                it(dmg[position].url)
            }
        }

        holder.itemView.findViewById<TextView>(R.id.type_name).text = everyDmgName.replace("-", " ")

        holder.itemView.findViewById<ImageView>(R.id.type_image).setImageResource(getImageByType(everyDmgName))
    }

    private var onItemClickListenerUrl: ((String) -> Unit)? = null

    fun setOnItemClickListenerUrlCommonType(listener: (String) -> Unit) {
        onItemClickListenerUrl = listener
    }

    fun addTypes(dmgFromTypeFragment: List<CommonDamageList>) {
        this.dmg.apply {
            addAll(dmgFromTypeFragment)
            notifyItemInserted(0)
        }
    }




}
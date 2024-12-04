package com.example.parcial2.farmacia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.parcial2.R

class FarmaciaAdapter(private val context: Context, private val farmacias: List<Farmacia>) : BaseAdapter() {

    override fun getCount(): Int = farmacias.size

    override fun getItem(position: Int): Farmacia = farmacias[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_farmacia, parent, false)

        val imgFarmacia = view.findViewById<ImageView>(R.id.imgFarmacia)
        val tvNombreFarmacia = view.findViewById<TextView>(R.id.tvNombreFarmacia)
        val tvTelefonoFarmacia = view.findViewById<TextView>(R.id.tvTelefonoFarmacia)

        val farmacia = getItem(position)
        tvNombreFarmacia.text = farmacia.nombre
        tvTelefonoFarmacia.text = farmacia.telefono

        // Asegurar el color del texto negro
        tvNombreFarmacia.setTextColor(context.getColor(R.color.text_black))
        tvTelefonoFarmacia.setTextColor(context.getColor(R.color.text_black))

        imgFarmacia.setImageResource(R.drawable.ic_pharmacy)

        return view
    }
}

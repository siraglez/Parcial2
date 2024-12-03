package com.example.parcial2.eventos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.parcial2.R

class EventoAdapter(private val context: Context, private val eventos: List<Map<String, Any>>) : BaseAdapter() {

    override fun getCount(): Int = eventos.size

    override fun getItem(position: Int): Map<String, Any> = eventos[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_evento, parent, false)

        val imgEvento = view.findViewById<ImageView>(R.id.imgEvento)
        val tvNombreEvento = view.findViewById<TextView>(R.id.tvNombreEvento)
        val tvDescripcionEvento = view.findViewById<TextView>(R.id.tvDescripcionEvento)
        val tvPrecioEvento = view.findViewById<TextView>(R.id.tvPrecioEvento)

        val evento = getItem(position)
        tvNombreEvento.text = evento["nombre"] as String
        tvDescripcionEvento.text = evento["descripcion"] as String
        tvPrecioEvento.text = "${evento["precio"]} €"
        imgEvento.setImageResource(R.drawable.ic_user_placeholder)

        return view
    }
}

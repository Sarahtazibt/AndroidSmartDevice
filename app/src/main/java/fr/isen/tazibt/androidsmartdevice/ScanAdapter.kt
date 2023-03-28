package fr.isen.tazibt.androidsmartdevice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ScanAdapter( var devices: ArrayList<String>) :  RecyclerView.Adapter<ScanAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cellName = view.findViewById<TextView>(R.id.test1)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType : Int): ScanAdapter.CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cellscan, parent, false)


        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.cellName.text = devices[position]

    }


    override fun getItemCount() = devices.size


}






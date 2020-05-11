package com.example.animalapi.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.animalapi.R
import com.example.animalapi.databinding.ItemAnimalBinding
import com.example.animalapi.model.Animal

class AnimalClickAdapter(val animals: ArrayList<Animal>) :
    RecyclerView.Adapter<AnimalClickAdapter.AnimalViewHolder>(), AnimalClickListener {

    class AnimalViewHolder(var view: ItemAnimalBinding) : RecyclerView.ViewHolder(view.root) {
//        private val tvName = view.tvName
//        private val imgAnimal = view.imgAnimal

        fun bind(animal: Animal) {
//            tvName.text = animal.name
//            imgAnimal.loadUrl(animal.imageUrl)
//            view.animal = animal
//            itemView.setOnClickListener {
//                val action = ListFragmentDirections.actionToDetailFragment(animal)
//                Navigation.findNavController(it).navigate(action)
//            }
        }

    }

    fun updateAnimals(animallist: List<Animal>) {
        animals.clear()
        animals.addAll(animallist)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemAnimalBinding = DataBindingUtil.inflate<ItemAnimalBinding>(
            inflater,
            R.layout.item_animal,
            parent,
            false
        )
        return AnimalViewHolder(
            itemAnimalBinding
        )
    }

    override fun getItemCount() = animals.size

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animals[position]
        holder.view.animal = animal
        holder.view.listener = this
    }

    override fun onClick(v: View) {
        val animalName = v.tag.toString()
        animals.find { it.name == animalName }?.let {
            Navigation.findNavController(v)
                .navigate(ListFragmentDirections.actionToDetailFragment(it))
        }


    }
}
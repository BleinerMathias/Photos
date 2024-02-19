package br.edu.ifsp.scl.sdm.photos.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.ifsp.scl.sdm.photos.model.Photo

class PhotoAdapter(
    private val activityContext: Context,
    private val photoList:MutableList<Photo>
):ArrayAdapter<Photo>(activityContext, android.R.layout.simple_list_item_1, photoList) {

    private data class PhotoViewHolder(val photoTitle:TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val productView = convertView?: LayoutInflater.from(activityContext)
            .inflate(android.R.layout.simple_list_item_1, parent, false).apply {
                tag = PhotoViewHolder(findViewById(android.R.id.text1))
            }

        (productView.tag as PhotoViewHolder).photoTitle.text = photoList[position].title

        return productView
    }

}
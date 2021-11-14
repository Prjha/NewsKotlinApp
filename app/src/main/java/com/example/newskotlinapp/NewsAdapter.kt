package com.example.newskotlinapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso

class NewsAdapter(var arrayList: ArrayList<News>, val  context: Context): BaseAdapter(), Filterable {
    private lateinit var title: TextView
    private lateinit var desc: TextView
    private lateinit var time: TextView
    private lateinit var source: TextView
    private lateinit var url: ImageView
    var temArray = ArrayList<News>()

    init {
        temArray = arrayList as ArrayList<News>
    }

    override fun getCount(): Int {
        return temArray.size
    }

    override fun getItem(p0: Int): Any {
        return temArray.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, view: View?, p2: ViewGroup?): View {
        var view = view
        view = LayoutInflater.from(context).inflate(R.layout.layout, p2, false)
        time = view.findViewById(R.id.time)
        title = view.findViewById(R.id.titleTv)
        desc = view.findViewById(R.id.desriptionTv)
        url = view.findViewById(R.id.iV)
        source = view.findViewById(R.id.source_news)

        title.text = temArray.get(p0).title
        desc.text = temArray.get(p0).Description
        time.text = temArray.get(p0).postTime
        source.text = temArray.get(p0).sourceName

        if (temArray.get(p0).url.equals("null")){
            url.setImageResource(R.drawable.ic_baseline_image_24)
            url.setColorFilter(Color.argb(100, 25, 121, 225))
        }
        else{
            Picasso.get().load(temArray.get(p0).url).into(url)
        }

        return view

    }

    fun updateNews(updatedNews: ArrayList<News>) {
        arrayList.clear()
        arrayList.addAll(updatedNews)

        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return CustomFilter
    }

    private val CustomFilter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val charSearch = constraint.toString()
            if (charSearch.isEmpty()) {
                temArray = arrayList as ArrayList<News>
            } else {
                val resultList = ArrayList<News>()
                for (row in arrayList) {
                    if (row.title.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        resultList.add(row)
                    }
                }
                temArray = resultList
            }
            val filterResults = FilterResults()
            filterResults.values = temArray
            return filterResults
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            temArray = p1?.values as ArrayList<News>
            notifyDataSetChanged()
        }

    }

}
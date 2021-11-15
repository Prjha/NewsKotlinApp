package com.example.newskotlinapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var list: ArrayList<News>
    private lateinit var madapter: NewsAdapter
    private var queue: RequestQueue? = null
    private lateinit var totalPost: TextView
    private lateinit var searchItem: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.ListView)
        totalPost = findViewById(R.id.total_post)
        searchItem = findViewById(R.id.search_item)

        queue = Volley.newRequestQueue(this)

        list = ArrayList()

        fetchAllNewsData()

        searchNews()

    }

    private fun searchNews() {
        searchItem.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                madapter?.filter?.filter(p0)
                totalPost.text = listView.count.toString()


            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun fetchAllNewsData() {

        val newsArray = arrayListOf<News>()
        val url = "https://newsapi.org/v2/everything?q=tesla&from=2021-10-14&sortBy=publishedAt&language=en&apiKey=6186a17c4898458ca4de460dbf733721"
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {

                val newsJsonArray = it.getJSONArray("articles")

                for(i in 0 until newsJsonArray.length()) {
                    val newsJsonObject: JSONObject = newsJsonArray.getJSONObject(i)
                    val sourceObj = newsJsonObject.getJSONObject("source")
                    val SourceName = sourceObj.getString("name")
                    val timeConvert = getTime(newsJsonObject.getString("publishedAt"))
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("description"),
                        newsJsonObject.getString("urlToImage"),
                        timeConvert,
                        SourceName
                    )
                    newsArray.add(news)

                    madapter = NewsAdapter(newsArray, this)
                    listView.adapter = madapter
                }
                totalPost.text = newsArray.size.toString()

//                madapter.updateNews(newsArray)
            },
            {

            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        queue!!.add(jsonObjectRequest)
//        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun getTime(time: String): String {
        var formattedDate: String = ""
        val inputFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val outputFormat: SimpleDateFormat = SimpleDateFormat("HH")
        val om: SimpleDateFormat = SimpleDateFormat("mm")
        val parsedDate: Date = inputFormat.parse(time)
        val m: Date = inputFormat.parse(time)
        val min: Int = Integer.parseInt(om.format(m).toString())
        val sdf: SimpleDateFormat = SimpleDateFormat("HH", Locale.getDefault())
        val currentTime: Int = Integer.parseInt(sdf.format(Date()))
        val pastTime: Int = Integer.parseInt(outputFormat.format(parsedDate))
        val timeDiff: Int = currentTime-pastTime
        if (timeDiff<1){
            val sdf1: SimpleDateFormat = SimpleDateFormat("mm")
            val minutes: Date = inputFormat.parse(time)
            val cM: Int = Integer.parseInt(outputFormat.format(minutes))
            val oM: Int = cM-min
            formattedDate = "$oM minutes ago"
        }
        else{
            formattedDate = "$timeDiff hours ago"
        }
        return formattedDate
    }

}

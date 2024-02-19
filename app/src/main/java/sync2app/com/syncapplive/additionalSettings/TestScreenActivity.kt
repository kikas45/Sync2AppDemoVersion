package sync2app.com.syncapplive.additionalSettings

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import sync2app.com.syncapplive.additionalSettings.ApiUrls.ApiUrlViewModel
import sync2app.com.syncapplive.additionalSettings.ApiUrls.DomainUrl
import sync2app.com.syncapplive.additionalSettings.ApiUrls.SavedApiAdapter
import sync2app.com.syncapplive.databinding.ActivityTestScreenBinding

class TestScreenActivity : AppCompatActivity(), SavedApiAdapter.OnItemClickListener {
    private lateinit var binding: ActivityTestScreenBinding

    private val mApiViewModel by viewModels<ApiUrlViewModel>()

    private val adapterApi by lazy {
        SavedApiAdapter(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.btnRequest.setOnClickListener {
            mApiViewModel.fetchApiUrls()
        }


        fectdata()



    }

    private fun fectdata() {

        binding.recyclerView.adapter = adapterApi
            binding.recyclerView.layoutManager = LinearLayoutManager(this)

            mApiViewModel.apiUrls.observe(this, Observer { apiUrls ->
                apiUrls?.let {
                    adapterApi.setData(it.DomainUrls)
                }
            })


        }


    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }


    override fun onItemClicked(domainUrl: DomainUrl) {
        showToastMessage("${domainUrl.url}")
    }


}
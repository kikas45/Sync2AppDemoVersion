package remotex.com.remotewebview.additionalSettings

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import remotex.com.remotewebview.R
import remotex.com.remotewebview.WebActivity
import remotex.com.remotewebview.databinding.ActivityReSyncBinding
import remotex.com.remotewebview.databinding.ActivitySynCmangerPlusBinding
import remotex.com.remotewebview.databinding.ActivityTvOrAppModeBinding

class TvActivityOrAppMode : AppCompatActivity() {
    private lateinit var binding: ActivityTvOrAppModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvOrAppModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            textAppMode.setOnClickListener {
                startActivity(Intent(applicationContext, RequiredBioActivity::class.java))
            }


            textTvMode.setOnClickListener {
                startActivity(Intent(applicationContext, ReSyncActivity::class.java))
            }
        }
    }
}
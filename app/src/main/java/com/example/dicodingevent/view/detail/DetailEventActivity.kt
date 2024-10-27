package com.example.dicodingevent.view.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.model.FavoriteEvent
import com.example.dicodingevent.databinding.ActivityDetailEventBinding
import com.example.dicodingevent.viewmodel.MainViewModel
import com.example.dicodingevent.viewmodel.ViewModelFactory

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this) as ViewModelProvider.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent?.getIntExtra("eventId", -1) ?: -1
        Log.d("DetailEventActivity", "Event ID diterima: $eventId")

        if (eventId != -1) {
            mainViewModel.getDetailEvent(eventId)
        }

        mainViewModel.getFavoriteEventById(eventId).observe(this) { favoriteEvent ->
            binding.favoriteButton.setImageResource(
                if (favoriteEvent == null) R.drawable.ic_favorite_border_24 else R.drawable.ic_favorite_24
            )

            binding.favoriteButton.setOnClickListener {
                val currentEvent = mainViewModel.detailEvent.value
                currentEvent?.let { event ->
                    if (favoriteEvent == null) {
                        val favorite = FavoriteEvent(
                            eventId = event.id,
                            name = event.name,
                            description = event.summary,
                            ownerName = event.ownerName,
                            cityName = event.cityName,
                            image = event.imageLogo
                        )
                        mainViewModel.insertFavoriteEvent(favorite)
                    } else {
                        mainViewModel.deleteFavoriteEvent(favoriteEvent)
                    }
                }
            }
        }

        mainViewModel.detailEvent.observe(this) { event ->
            event?.let {
                Glide.with(this).load(it.mediaCover).into(binding.imageView)
                binding.tvName.text = it.name
                binding.tvOwner.text = it.ownerName
                binding.tvLocation.text = it.cityName
                binding.tvQuota.text = "${it.quota?.minus(it.registrants ?: 0)}"
                binding.tvCategory.text = it.category
                binding.tvDescription.text = it.description?.let { it1 ->
                    HtmlCompat.fromHtml(
                        it1,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }
                binding.tvBegin.text = it.beginTime
                val link = it.link
                binding.btnRegist.setOnClickListener {
                    val web = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    startActivity(web)
                }
            }
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        mainViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                mainViewModel.zeroErrorMessage()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

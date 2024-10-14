package com.example.dicodingevent.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.response.Event
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)

        val event = intent.getParcelableExtra<Event>("EXTRA_EVENT")
        event?.let {
            Glide.with(this).load(it.mediaCover).into(binding.imageView)
            binding.tvName.text = it.name
            binding.tvOwner.text = it.ownerName
            binding.tvLocation.text = it.cityName
            binding.tvQuota.text = "${it.quota?.minus(it.registrants!!)}"
            binding.tvCategory.text = it.category
            binding.tvDescription.text = HtmlCompat.fromHtml(
                it.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            binding.tvBegin.text = it.beginTime
            val link = it.link;
            binding.btnRegist.setOnClickListener {
                val web = Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(web);
            }
        }
        showLoading(false)

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
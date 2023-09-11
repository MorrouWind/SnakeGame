package com.example.snakegame.screen

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import com.example.snakegame.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAboutBinding.inflate(inflater, container, false)

        binding.aboutLink.setOnClickListener {
            CustomTabsIntent.Builder().build().launchUrl(requireContext(), Uri.parse("https://github.com/MorrouWind/SnakeGame"))
        }

        return binding.root
    }

}
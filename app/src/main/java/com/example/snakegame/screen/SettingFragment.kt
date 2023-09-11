package com.example.snakegame.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.snakegame.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    var currentSpeed: Long? = null

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingBinding.inflate(inflater, container, false)

        with(binding) {

            speed1.isChecked = true
            speed1.setOnClickListener {
                isNotChecked(it)
                getSelectedSpeed(500L)
            }
            speed2.setOnClickListener {
                isNotChecked(it)
                getSelectedSpeed(400L)
            }
            speed3.setOnClickListener {
                isNotChecked(it)
                getSelectedSpeed(350L)
            }
            speed4.setOnClickListener {
                isNotChecked(it)
                getSelectedSpeed(250L)
            }
            speed5.setOnClickListener {
                isNotChecked(it)
                getSelectedSpeed(200L)
            }
        }

        return binding.root
    }

    private fun getSelectedSpeed(speed: Long) {
        currentSpeed = speed
    }

    private fun isNotChecked(view: View) {
        with(binding) {
            when (view) {
                speed1 -> {
                    speed2.isChecked = false
                    speed3.isChecked = false
                    speed4.isChecked = false
                    speed5.isChecked = false
                }

                speed2 -> {
                    speed1.isChecked = false
                    speed3.isChecked = false
                    speed4.isChecked = false
                    speed5.isChecked = false
                }

                speed3 -> {
                    speed1.isChecked = false
                    speed2.isChecked = false
                    speed4.isChecked = false
                    speed5.isChecked = false
                }

                speed4 -> {
                    speed1.isChecked = false
                    speed2.isChecked = false
                    speed3.isChecked = false
                    speed5.isChecked = false
                }

                speed5 -> {
                    speed1.isChecked = false
                    speed2.isChecked = false
                    speed3.isChecked = false
                    speed4.isChecked = false
                }
            }
        }
    }

}
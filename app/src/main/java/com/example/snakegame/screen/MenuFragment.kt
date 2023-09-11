package com.example.snakegame.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.snakegame.databinding.FragmentMenuBinding


class MenuFragment : Fragment() {

    lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        with(binding) {
            gameMenu.setOnClickListener {
                val direction = MenuFragmentDirections.actionMenuFragmentToGameFragment()
                findNavController().navigate(direction)
            }
            settingMenu.setOnClickListener {
                val direction = MenuFragmentDirections.actionMenuFragmentToSettingFragment()
                findNavController().navigate(direction)
            }
            aboutMenu.setOnClickListener {
                val direction = MenuFragmentDirections.actionMenuFragmentToAboutFragment()
                findNavController().navigate(direction)
            }
            exitMenu.setOnClickListener {
                activity?.finishAffinity()
                android.os.Process.killProcess(android.os.Process.myPid())

            }
        }

        return binding.root
    }

 }
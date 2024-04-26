package com.sds.pokemonguide.fragments

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sds.pokemonguide.R
import com.sds.pokemonguide.SharedPrefsManager
import com.sds.pokemonguide.databinding.SettingsFragmentBinding


class SettingsFragment : Fragment() {

    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    private var sharedPrefsManager : SharedPrefsManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefsManager = SharedPrefsManager(requireActivity())

        setButtons()
        checkTheme()

    }

    private fun checkTheme(){

        val isDarkTheme = sharedPrefsManager?.getTheme()
        if (isDarkTheme == true){
            binding.darkThemeButton.visibility = View.GONE
            binding.lightThemeButton.visibility = View.VISIBLE
        }else {
            binding.lightThemeButton.visibility = View.GONE
            binding.darkThemeButton.visibility = View.VISIBLE
        }
    }

    private fun setButtons(){

        binding.darkThemeButton.setOnClickListener {
            it.visibility = View.GONE
            binding.lightThemeButton.visibility = View.VISIBLE

            sharedPrefsManager?.setTheme(true)

            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
        }

        binding.lightThemeButton.setOnClickListener {
            it.visibility = View.GONE
            binding.darkThemeButton.visibility = View.VISIBLE

            sharedPrefsManager?.setTheme(false)

            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )

        }

        binding.gmail.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "sdsvelop@gmail.com"))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                alertDialog("gmail is not found on your device", "")
            }
        }


        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun alertDialog(title: String, description: String) {

        val builder =
            AlertDialog.Builder(ContextThemeWrapper(this.context, R.style.AlertDialogCustom))

        with(builder) {
            setTitle(title)
            setMessage(description)
            show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        sharedPrefsManager = null

        _binding = null
    }
}
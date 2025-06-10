package com.exemplo.appweatherinformation

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.exemplo.appweatherinformation.databinding.ActivityMainBinding
import com.exemplo.appweatherinformation.ui.WeatherViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainLayout.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN && binding.searchView.hasFocus()) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
                binding.searchView.clearFocus()
            }
            false // Retorne false para permitir que outros eventos de toque sejam processados
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.getWeather(it) }

                // Obtém o serviço de InputMethodManager
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                // Oculta o teclado associado à janela atual (da SearchView)
                imm?.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        binding.searchView.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        showNotFoundMessage()

        viewModel.weatherData.observe(this) { weatherData ->

            weatherData?.let { weather ->
                val temp = viewModel.convertKelvinToCelsius(weather?.weatherCondition?.temp)
                val feels_like = viewModel.convertKelvinToCelsius(weather?.weatherCondition?.feels_like)
                val temp_min = viewModel.convertKelvinToCelsius(weather?.weatherCondition?.temp_min)
                val temp_max = viewModel.convertKelvinToCelsius(weather?.weatherCondition?.temp_max)

                binding.apply {
                    tvStatus.text = weather?.weatherDescriptions?.get(0)?.description?.uppercase() ?: "No information"
                    tvLocation.text = weather?.name  ?: "No information"
                    tvTemp.text = "$temp°C"
                    tvFeelsLike.text = "Feels like: $feels_like°C"
                    tvMinTemp.text = "Min temp: $temp_min°C "
                    tvMaxTemp.text = "Max temp: $temp_max°C"
                    tvUpdateTime.text = "Last Update: ${
                        SimpleDateFormat(
                            "hh:mm a",
                            Locale.ENGLISH
                        ).format(weather?.time)
                    }"
                }
            }
        }

        viewModel.dataSearchState.observe(this) { state ->
            if (state) {
                showWeatherResult()

            } else {
                showNotFoundMessage()

                Snackbar.make(
                    binding.root,
                    "An error occurred! Please try again.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showNotFoundMessage() {
        binding.linearLayout.visibility = View.GONE
        binding.constraintLayout.visibility = View.GONE

        binding.tvZeroHits.visibility = View.VISIBLE
    }

    private fun showWeatherResult() {
        binding.linearLayout.visibility = View.VISIBLE
        binding.constraintLayout.visibility = View.VISIBLE

        binding.tvZeroHits.visibility = View.GONE
    }
}
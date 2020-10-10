package com.wisdom.progressbar

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        radio_radius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                progressBar.setRadius(progress.dp().toInt())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        bar_height.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                progressBar.setBarHeight(progress.dp().toInt())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        border_size.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                progressBar.setBorderSize(progress.dp().toInt())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        progress_color.setOnColorChangeListener { colorBarPosition, alphaBarPosition, color ->
            progressBar.setProgressColor(color)
        }

        val intArray = IntArray(2)
        intArray[0] = Color.WHITE
        intArray[1] = Color.BLACK
        border_color.setColorSeeds(intArray)
        border_color.setOnColorChangeListener { colorBarPosition, alphaBarPosition, color ->
            progressBar.setBorderColor(color)
        }

        background_color.setOnColorChangeListener { colorBarPosition, alphaBarPosition, color ->
            progressBar.setBackgroundColor(color)
        }

        remain_mode.setOnCheckedChangeListener { buttonView, isChecked ->
            progressBar.setRemainMode(isChecked)
        }

    }

    fun Int.dp(): Float {
        val scale: Float = resources.displayMetrics.density
        return (this * scale + 0.5f)
    }
}
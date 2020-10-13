package com.wisdom.progressbar

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.rtugeek.percentprogressbar.PercentProgressBar
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

        radio_progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                progressBar.setPercent(progress / 100.0)
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
        intArray[0] = Color.LTGRAY
        intArray[1] = Color.BLACK
        border_color.setColorSeeds(intArray)
        border_color.setOnColorChangeListener { colorBarPosition, alphaBarPosition, color ->
            progressBar.setBorderColor(color)
        }

        background_color.setOnColorChangeListener { colorBarPosition, alphaBarPosition, color ->
            progressBar.setBackgroundColor(color)
        }

        radio_direction.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radio_left) {
                progressBar.direction = PercentProgressBar.Direction.LEFT_TO_RIGHT
            } else {
                progressBar.direction = PercentProgressBar.Direction.RIGHT_TO_LEFT
            }
        }
    }

    fun Int.dp(): Float {
        val scale: Float = resources.displayMetrics.density
        return (this * scale + 0.5f)
    }
}
package com.getir.patika.foodcouriers.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import com.getir.patika.foodcouriers.R

class CustomRatingBar(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    var rating: Int = 0
        private set

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_rating_bar, this, true)
        updateStars()
    }

    private fun updateStars() {
        val stars = listOf<ImageView>(
            findViewById(R.id.star1),
            findViewById(R.id.star2),
            findViewById(R.id.star3),
            findViewById(R.id.star4),
            findViewById(R.id.star5)
        )

        for (i in 0 until stars.size) {
            val star = stars[i]
            if (i < rating) {
                if (i == rating - 1) {
                    star.setImageResource(R.drawable.ic_star_last)

                } else {
                    star.setImageResource(R.drawable.ic_star_filled)
                }
            } else {
                star.setImageResource(R.drawable.ic_star_empty)
            }
            star.setOnClickListener {
                setRating(i + 1)
            }
        }
    }

    fun setRating(rating: Int) {
        this.rating = rating
        updateStars()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val action = it.action
            val x = it.x

            val starWidth = width / 5
            val clickedStarIndex = (x / starWidth).toInt()

            when (action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    setRating(clickedStarIndex + 1)
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    setRating(clickedStarIndex + 1)
                    performClick()
                    return true
                }

                else -> {}
            }
        }
        return super.onTouchEvent(event)
    }
}
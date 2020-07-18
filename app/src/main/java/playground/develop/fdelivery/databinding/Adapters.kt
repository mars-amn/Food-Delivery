package playground.develop.fdelivery.databinding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.api.load

object Adapters {
    @BindingAdapter("loadImage")
    @JvmStatic
    fun loadImage(image: ImageView, icon: Int) {
        image.load(icon)
    }

    @BindingAdapter("android:text")
    @JvmStatic
    fun setPriceValue(textView: TextView, price: Float) {
        textView.text = if (price.isNaN()) {
            ""
        } else {
            price.toString()
        }
    }
}
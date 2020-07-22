package playground.develop.fdelivery.databinding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.api.load
import coil.transform.CircleCropTransformation

object Adapters {
    @BindingAdapter("loadImage")
    @JvmStatic
    fun loadImage(image: ImageView, icon: Int) {
        image.load(icon)
    }

    @BindingAdapter("loadCartImage")
    @JvmStatic
    fun loadCartImage(image: ImageView, icon: Int) {
        image.load(icon) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
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
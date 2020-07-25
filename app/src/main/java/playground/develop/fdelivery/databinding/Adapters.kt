package playground.develop.fdelivery.databinding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.api.load
import coil.transform.CircleCropTransformation

object Adapters {
    @BindingAdapter("loadImage")
    @JvmStatic
    fun loadImage(image: ImageView, url: String) {
        image.load(url)
    }

    @BindingAdapter("loadCategory")
    @JvmStatic
    fun loadImage(image: ImageView, url: Int) {
        image.load(url)
    }

    @BindingAdapter("loadCartImage")
    @JvmStatic
    fun loadCartImage(image: ImageView, url: String) {
        image.load(url) {
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
package playground.develop.fdelivery.utils

import playground.develop.fdelivery.R
import playground.develop.fdelivery.data.Category

object DataUtils {
    fun getCategories(): List<Category> {
        val categories = ArrayList<Category>()
        categories.add(Category(R.drawable.ic_pizza, "Pizza"))
        categories.add(Category(R.drawable.ic_shawarma, "Shawarma"))
        categories.add(Category(R.drawable.ic_crepe, "Crepe"))
        categories.add(Category(R.drawable.ic_dessert, "Desserts"))
        categories.add(Category(R.drawable.ic_refreshments, "Refreshments"))
        return categories
    }
}
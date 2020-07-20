package playground.develop.fdelivery.utils

import playground.develop.fdelivery.R
import playground.develop.fdelivery.data.Category
import playground.develop.fdelivery.data.Product

object DataUtils {
    fun getCategories(): List<Category> {
        val categories = ArrayList<Category>()
        categories.add(Category(R.drawable.ic_orange, "JUICE"))
        categories.add(Category(R.drawable.ic_shake, "SHAKE"))
        categories.add(Category(R.drawable.ic_smoothie, "SMOOTHIE"))
        return categories
    }

    fun getProducts(): List<Product> {
        val products = ArrayList<Product>()
        products.add(Product(R.drawable.orange_juice, "Drinky Orange Juice",
            "Made with love and fresh seasonal fruits.", 12.3f, 40))
        products.add(Product(R.drawable.orange_juice, "Drinky Lemon & Mint Juice",
            "Made with love and fresh seasonal fruits.", 70.55f, 55))
        products.add(Product(R.drawable.orange_juice, "Drinky Lemon & Mint Juice",
            "Made with love and fresh seasonal fruits.", 7.525f, 88))
        return products
    }
}
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
        categories.add(Category(R.drawable.ic_shake, "SMOOTHIE"))
        categories.add(Category(R.drawable.ic_smoothie, "SMOOTHIE"))
        categories.add(Category(R.drawable.ic_orange, "SMOOTHIE"))
        return categories
    }

    fun getProducts(): List<Product> {
        val products = ArrayList<Product>()
        products.add(Product(R.drawable.orange_juice, "Drinky Orange Juice",
            "Made with love and fresh seasonal fruits.", 12.3f, 40))
        products.add(Product(R.drawable.orange_juice, "Drinky Lemon & Mint Juice",
            "Made with love and fresh seasonal fruits.", 70.55f, 55))
        products.add(Product(R.drawable.orange_juice, "Drinky Lemon & Mint Juice",
            "Made with love and fresh seasonal fruits.", 7.525f, 2245))
        products.add(Product(R.drawable.orange_juice, "Drinky Lemon & Mint Juice",
            "Made with love and fresh seasonal fruits.", 7.525f, 348))
        products.add(Product(R.drawable.orange_juice, "Drinky Lemon & Mint Juice",
            "Made with love and fresh seasonal fruits.", 7.525f, 543))
        products.add(Product(R.drawable.orange_juice, "Drinky Lemon & Mint Juice",
            "Made with love and fresh seasonal fruits.", 7.525f, 798))
        products.add(Product(R.drawable.orange_juice, "Drinky Lemon & Mint Juice",
            "Made with love and fresh seasonal fruits.", 7.525f, 3512353))
        products.add(Product(R.drawable.orange_juice, "Drinky Lemon & Mint Juice",
            "Made with love and fresh seasonal fruits.", 7.525f, 7777777))
        products.add(Product(R.drawable.orange_juice, "Drinky Lemon & Mint Juice",
            "Made with love and fresh seasonal fruits.", 7.525f, 6782))
        products.add(Product(R.drawable.orange_juice, "Drinky Lemon & Mint Juice",
            "Made with love and fresh seasonal fruits.", 7.525f, 6123))
        return products
    }
}
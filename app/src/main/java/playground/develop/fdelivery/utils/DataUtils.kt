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
        products.add(Product(R.drawable.orange_juice,
                "Drinky Orange Juice",
                "Made with love and fresh seasonal fruits.",
                12.3f,
                40))
        products.add(Product(R.drawable.orange_juice,
                "Drinky Lemon & Mint Juice",
                "Made with love and fresh seasonal fruits.",
                6.50f,
                55))
        products.add(Product(R.drawable.orange_juice,
                "Drinky Lemon & Mint Juice",
                "Made with love and fresh seasonal fruits.",
                20.25f,
                2245))
        products.add(Product(R.drawable.orange_juice,
                "Drinky Lemon & Mint Juice",
                "Made with love and fresh seasonal fruits.",
                1.70f,
                348))
        products.add(Product(R.drawable.orange_juice,
                "Drinky Lemon & Mint Juice",
                "Made with love and fresh seasonal fruits.",
                8.20f,
                543))
        products.add(Product(R.drawable.orange_juice,
                "Drinky Lemon & Mint Juice",
                "Made with love and fresh seasonal fruits.",
                66.40f,
                798))
        products.add(Product(R.drawable.orange_juice,
                "Drinky Lemon & Mint Juice",
                "Made with love and fresh seasonal fruits.",
                7f,
                3512353))
        products.add(Product(R.drawable.orange_juice,
                "Drinky Lemon & Mint Juice",
                "Made with love and fresh seasonal fruits.",
                0.4f,
                7777777))
        products.add(Product(R.drawable.orange_juice,
                "Drinky Lemon & Mint Juice",
                "Made with love and fresh seasonal fruits.",
                2.10f,
                6782))
        products.add(Product(R.drawable.orange_juice,
                "Drinky Lemon & Mint Juice",
                "Made with love and fresh seasonal fruits.",
                3.60f,
                6123))
        return products
    }
}
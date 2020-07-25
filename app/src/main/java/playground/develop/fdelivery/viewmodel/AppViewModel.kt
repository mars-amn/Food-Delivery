package playground.develop.fdelivery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import playground.develop.fdelivery.data.Category
import playground.develop.fdelivery.data.Product
import playground.develop.fdelivery.repository.DataRepository

class AppViewModel : ViewModel(), KoinComponent {
    private val mRepository: DataRepository by inject()

    fun loadCategories(): LiveData<List<Category>> {
        return mRepository.getCategories()
    }

    //    fun loadProducts(): LiveData<List<Product>> {
    //        return mRepository.getProducts()
    //    }

    override fun onCleared() {
        super.onCleared()
        mRepository.dispose()
    }

    fun loadProducts(category: String): LiveData<List<Product>> {
        return mRepository.getProducts(category)
    }

    fun searchFor(query: String, category: String): LiveData<List<Product>> {
        return mRepository.searchFor(query, category)
    }
}
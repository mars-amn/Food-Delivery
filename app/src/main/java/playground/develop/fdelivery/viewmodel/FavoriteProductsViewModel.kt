package playground.develop.fdelivery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.koin.core.KoinComponent
import org.koin.core.inject
import playground.develop.fdelivery.database.locale.favorite.FavProducts
import playground.develop.fdelivery.repository.FavoriteProductsRepository

class FavoriteProductsViewModel : ViewModel(), KoinComponent {
    private val mRepository: FavoriteProductsRepository by inject()

    fun getFavoriteProducts(): LiveData<PagedList<FavProducts>> {
        return mRepository.loadProducts()
    }

    fun addProductToFavorite(product: FavProducts): LiveData<Long> {
        return mRepository.addProduct(product)
    }

    override fun onCleared() {
        super.onCleared()
        mRepository.dispose()
    }
}
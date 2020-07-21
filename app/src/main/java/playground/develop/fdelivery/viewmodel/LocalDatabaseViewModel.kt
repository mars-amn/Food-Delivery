package playground.develop.fdelivery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.koin.core.KoinComponent
import org.koin.core.inject
import playground.develop.fdelivery.database.local.cart.CartProducts
import playground.develop.fdelivery.database.local.favorite.FavProducts
import playground.develop.fdelivery.repository.LocalDatabaseRepository

class LocalDatabaseViewModel : ViewModel(), KoinComponent {
    private val mRepository: LocalDatabaseRepository by inject()

    fun getFavoriteProducts(): LiveData<PagedList<FavProducts>> {
        return mRepository.loadFavoriteProducts()
    }

    fun addProductToFavorite(product: FavProducts): LiveData<Long> {
        return mRepository.addProductToFavorite(product)
    }

    fun deleteFavoriteProduct(product: FavProducts) {
        mRepository.deleteFavoriteProduct(product)
    }

    fun getCart(): LiveData<PagedList<CartProducts>> {
        return mRepository.loadCartProducts()
    }

    fun addToCart(product: CartProducts): LiveData<Long> {
        return mRepository.addProductToCart(product)
    }

    fun deleteProductOfCart(product: CartProducts) {
        mRepository.deleteProductOfCart(product)
    }

    override fun onCleared() {
        super.onCleared()
        mRepository.dispose()
    }

}
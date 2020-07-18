package playground.develop.fdelivery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import playground.develop.fdelivery.database.locale.favorite.FavProducts
import playground.develop.fdelivery.repository.FavoriteRepository

class FavoriteViewModel : ViewModel(), KoinComponent {
    private val mDataRepository: FavoriteRepository by inject()

    fun insertFavoriteProduct(favorite: FavProducts): LiveData<Long> {
        return mDataRepository.addToFavorite(favorite)
    }

    override fun onCleared() {
        mDataRepository.dispose()
        super.onCleared()
    }
}
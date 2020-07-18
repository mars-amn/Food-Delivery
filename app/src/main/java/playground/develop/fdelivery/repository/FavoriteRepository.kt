package playground.develop.fdelivery.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import playground.develop.fdelivery.database.locale.favorite.FavProducts
import playground.develop.fdelivery.database.locale.favorite.FavoriteDao

class FavoriteRepository : KoinComponent {
    private val mFavDao: FavoriteDao by inject()
    private val mDisposables = CompositeDisposable()
    fun addToFavorite(favorite: FavProducts): LiveData<Long> {
        val productId = MutableLiveData<Long>()
        mDisposables.add(mFavDao.insertFavProduct(favorite).subscribeOn(Schedulers.io())
                             .observeOn(AndroidSchedulers.mainThread())
                             .subscribeWith(object : DisposableSingleObserver<Long>() {
                                 override fun onSuccess(t: Long) {
                                     productId.value = t
                                 }

                                 override fun onError(e: Throwable) {
                                 }


                             }))
        return productId
    }

    fun dispose() {
        mDisposables.dispose()
    }
}
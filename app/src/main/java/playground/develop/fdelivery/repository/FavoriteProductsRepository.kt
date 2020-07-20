package playground.develop.fdelivery.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import playground.develop.fdelivery.database.locale.favorite.FavProducts
import playground.develop.fdelivery.database.locale.favorite.FavoriteDao
import playground.develop.fdelivery.utils.Extensions.debug

class FavoriteProductsRepository : KoinComponent {
    private val mDisposables = CompositeDisposable()
    private val mDao: FavoriteDao by inject()
    private val TAG = "FavoriteProductsRepository"
    fun loadProducts(): LiveData<PagedList<FavProducts>> {
        val factory: DataSource.Factory<Int, FavProducts> = mDao.getFavoriteProducts()
        val pagedList = MutableLiveData<PagedList<FavProducts>>()
        val pagedListBuilder = RxPagedListBuilder(factory,
            PagedList.Config.Builder().setPageSize(25).setEnablePlaceholders(true)
                .build()).buildFlowable(BackpressureStrategy.LATEST)

        mDisposables.add(
            pagedListBuilder.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list -> pagedList.value = list }, { error ->
                    debug(TAG, error.message!!)
                }))
        return pagedList
    }

    fun addProduct(product: FavProducts): LiveData<Long> {
        val productId = MutableLiveData<Long>()
        mDisposables.add(mDao.insertFavProduct(product).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Long>() {
                override fun onSuccess(id: Long) {
                    productId.value = id
                }

                override fun onError(e: Throwable) {
                    debug(TAG, e.message!!)
                }

            }))
        return productId
    }

    fun deleteProduct(product: FavProducts) {
        mDisposables.add(
            Observable.fromCallable { mDao.deleteProduct(product) }.subscribeOn(Schedulers.io())
                .subscribe())
    }

    fun dispose() {
        mDisposables.dispose()
    }


}
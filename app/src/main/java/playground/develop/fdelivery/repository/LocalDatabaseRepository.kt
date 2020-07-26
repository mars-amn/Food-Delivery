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
import playground.develop.fdelivery.database.local.cart.CartDao
import playground.develop.fdelivery.database.local.cart.CartProducts
import playground.develop.fdelivery.database.local.favorite.FavProducts
import playground.develop.fdelivery.database.local.favorite.FavoriteDao
import playground.develop.fdelivery.utils.Extensions.debug

class LocalDatabaseRepository : KoinComponent {
    private val mDisposables = CompositeDisposable()
    private val mFavoriteDao: FavoriteDao by inject()
    private val mCartDao: CartDao by inject()
    private val TAG = "LocalDBRepository"

    /**
     * Favorite table related
     */
    fun loadFavoriteProducts(): LiveData<PagedList<FavProducts>> {
        val factory: DataSource.Factory<Int, FavProducts> = mFavoriteDao.getFavoriteProducts()
        val pagedList = MutableLiveData<PagedList<FavProducts>>()
        val pagedListBuilder = RxPagedListBuilder(factory,
                PagedList.Config.Builder()
                    .setPageSize(25)
                    .setEnablePlaceholders(true)
                    .build()).buildFlowable(BackpressureStrategy.LATEST)

        mDisposables.add(pagedListBuilder.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list -> pagedList.value = list }, { error ->
                debug(TAG, error.message!!)
            }))
        return pagedList
    }

    fun addProductToFavorite(product: FavProducts): LiveData<Long> {
        val productId = MutableLiveData<Long>()
        mDisposables.add(mFavoriteDao.insertFavProduct(product)
            .subscribeOn(Schedulers.io())
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

    fun deleteFavoriteProduct(product: FavProducts) {
        mDisposables.add(Observable.fromCallable { mFavoriteDao.deleteProduct(product) }
            .subscribeOn(Schedulers.io())
            .subscribe())
    }

    /**
     * Cart table related
     */
    fun addProductToCart(product: CartProducts): LiveData<Long> {
        val productId = MutableLiveData<Long>()
        mDisposables.add(mCartDao.insertProductToCart(product)
            .subscribeOn(Schedulers.io())
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

    fun deleteProductOfCart(product: CartProducts) {
        mDisposables.add(Observable.fromCallable { mCartDao.deleteProductOfCart(product) }
            .subscribeOn(Schedulers.io())
            .subscribe())
    }

    fun loadCartProducts(): LiveData<PagedList<CartProducts>> {
        val factory: DataSource.Factory<Int, CartProducts> = mCartDao.getCart()
        val pagedList = MutableLiveData<PagedList<CartProducts>>()
        val pagedListBuilder = RxPagedListBuilder(factory,
                PagedList.Config.Builder()
                    .setPageSize(25)
                    .setEnablePlaceholders(true)
                    .build()).buildFlowable(BackpressureStrategy.BUFFER)

        mDisposables.add(pagedListBuilder.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list -> pagedList.value = list }, { error ->
                debug(TAG, error.message!!)
            }))
        return pagedList
    }

    fun updateProductInCart(product: CartProducts) {
        mDisposables.add(Observable.fromCallable { mCartDao.updateProductInCart(product) }
            .subscribeOn(Schedulers.io())
            .subscribe())
    }

    fun nukeCart() {
        mDisposables.add(Observable.fromCallable { mCartDao.nukeCart() }
            .subscribeOn(Schedulers.io())
            .subscribe())
    }

    fun dispose() {
        mDisposables.dispose()
    }

}
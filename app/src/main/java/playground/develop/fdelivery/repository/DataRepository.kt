package playground.develop.fdelivery.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import playground.develop.fdelivery.data.Category
import playground.develop.fdelivery.data.Product
import playground.develop.fdelivery.utils.DataUtils

class DataRepository {
    private val mDisposables = CompositeDisposable()

    fun getCategories(): LiveData<List<Category>> {
        val categories = MutableLiveData<List<Category>>()
        mDisposables.add(
            Observable.fromCallable { DataUtils.getCategories() }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    categories.value = DataUtils.getCategories()
                })
        return categories

    }

    fun getProducts(): LiveData<List<Product>> {
        val products = MutableLiveData<List<Product>>()
        mDisposables.add(
            Observable.fromCallable { DataUtils.getProducts() }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    products.value = DataUtils.getProducts()
                })
        return products
    }

    fun dispose() {
        mDisposables.dispose()
    }
}
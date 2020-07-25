package playground.develop.fdelivery.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import playground.develop.fdelivery.data.Category
import playground.develop.fdelivery.data.Product
import playground.develop.fdelivery.utils.DataUtils

class DataRepository : KoinComponent {
    private val mDisposables = CompositeDisposable()
    private val mDB: FirebaseFirestore by inject()
    fun getCategories(): LiveData<List<Category>> {
        val categories = MutableLiveData<List<Category>>()
        mDisposables.add(Observable.fromCallable { DataUtils.getCategories() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                categories.value = DataUtils.getCategories()
            })
        return categories

    }

    fun dispose() {
        mDisposables.dispose()
    }

    fun getProducts(category: String): LiveData<List<Product>> {
        val products = MutableLiveData<List<Product>>()
        mDB.collection(category.toLowerCase()).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val list = ArrayList<Product>()
                task.result?.forEach { document ->
                    list.add(document.toObject(Product::class.java))
                }
                products.value = list
            }
        }
        return products
    }

    fun searchFor(query: String, category: String): LiveData<List<Product>> {
        val products = MutableLiveData<List<Product>>()
        val list = ArrayList<Product>()
        mDB.collection(category)
            .whereLessThanOrEqualTo("name", query)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mDisposables.add(Observable.fromIterable(task.result?.toObjects(Product::class.java))
                        .filter {
                            it.name.toLowerCase().contains(query)
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { product ->
                            list.add(product)
                            products.value = list
                        })


                } else {
                    Log.d("DataRepository", task.exception.toString())
                }
            }
        return products
    }
}
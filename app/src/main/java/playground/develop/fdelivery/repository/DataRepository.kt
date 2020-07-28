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
import playground.develop.fdelivery.database.remote.Order
import playground.develop.fdelivery.database.remote.Product
import playground.develop.fdelivery.utils.Constants.ORDERS_COLLECTION
import playground.develop.fdelivery.utils.Constants.ORDER_ADDRESS
import playground.develop.fdelivery.utils.Constants.ORDER_ID
import playground.develop.fdelivery.utils.Constants.ORDER_PAYMENT_TYPE
import playground.develop.fdelivery.utils.Constants.ORDER_PHONE
import playground.develop.fdelivery.utils.Constants.ORDER_PRODUCTS
import playground.develop.fdelivery.utils.Constants.ORDER_STATUS_PROCESS
import playground.develop.fdelivery.utils.Constants.ORDER_USER_NAME
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

    fun createOrder(order: Order): LiveData<Boolean> {
        val orderSubmittingStatus = MutableLiveData<Boolean>()
        mDB.collection(ORDERS_COLLECTION)
            .document(order.orderId)
            .set(getMappedOrder(order))
            .addOnCompleteListener {
                orderSubmittingStatus.value = it.isSuccessful
            }
        return orderSubmittingStatus
    }

    private fun getMappedOrder(order: Order): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map[ORDER_PRODUCTS] = order.products
        map[ORDER_USER_NAME] = order.name
        map[ORDER_ADDRESS] = order.address
        map[ORDER_PAYMENT_TYPE] = order.paymentType
        map[ORDER_PHONE] = order.phone
        map[ORDER_STATUS_PROCESS] = order.status
        map[ORDER_ID] = order.orderId
        return map
    }
}
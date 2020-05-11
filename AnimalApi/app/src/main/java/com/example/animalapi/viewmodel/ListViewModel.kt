package com.example.animalapi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.animalapi.DI.ApplicationModule
import com.example.animalapi.DI.CONTEXT_APP
import com.example.animalapi.DI.DaggerViewModelComponent
import com.example.animalapi.DI.TypeOfContext
import com.example.animalapi.Util.SharePreferencesHelper
import com.example.animalapi.model.Animal
import com.example.animalapi.model.ApiKey
import com.example.animalapi.service.AnimalService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel(application: Application) : AndroidViewModel(application) {

    constructor(application: Application, isInjected: Boolean = true) : this(application) {
        this.isInjected = isInjected
    }

    val animals by lazy { MutableLiveData<List<Animal>>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val disposable = CompositeDisposable()

    @Inject
    lateinit var animalApi: AnimalService

    @Inject
    @field:TypeOfContext(CONTEXT_APP)
    lateinit var prefs: SharePreferencesHelper

    private var isInvalidKey = false
    private var isInjected = false

    fun inject() {
        if (!isInjected) {
            DaggerViewModelComponent
                .builder()
                .applicationModule(ApplicationModule(getApplication()))
                .build().inject(this)
        }

    }

    fun refresh() {
        inject()
        loading.value = true
        isInvalidKey = false
        val key = prefs.getUpdateKey()
        if (key.isNullOrEmpty()) getKey() else getAnimals(key)
    }

    fun hardRefresh() {
        inject()
        loading.value = true
        getKey()
    }

     fun getKey() {
        disposable.add(
            animalApi.getApiKey()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApiKey>() {
                    override fun onSuccess(apikey: ApiKey) {
                        val newKey = apikey.key
                        if (newKey.isNullOrEmpty()) {
                            loadError.value = true
                            loading.value = false
                        } else {
                            prefs.saveUpdateKey(newKey)
                            getAnimals(newKey)
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loadError.value = true
                        loading.value = false
                    }

                })
        )
    }

     fun getAnimals(key: String) {
        disposable.add(
            animalApi.getAnimals(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Animal>>() {
                    override fun onSuccess(t: List<Animal>) {
                        animals.value = t
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (!isInvalidKey) {
                            getKey()
                            isInvalidKey = true
                        } else {
                            e.printStackTrace()
                            animals.value = null
                            loadError.value = true
                            loading.value = false
                        }
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
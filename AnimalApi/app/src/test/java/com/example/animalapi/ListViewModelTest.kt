package com.example.animalapi

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.animalapi.DI.ApplicationModule
import com.example.animalapi.DI.DaggerViewModelComponent
import com.example.animalapi.Util.SharePreferencesHelper
import com.example.animalapi.model.Animal
import com.example.animalapi.model.ApiKey
import com.example.animalapi.service.AnimalService
import com.example.animalapi.viewmodel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor


class ListViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var animalService: AnimalService

    @Mock
    lateinit var prefs: SharePreferencesHelper

    val application = Mockito.mock(Application::class.java)

    var listViewModel = ListViewModel(application, true)

    private val key = "Test Key"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        DaggerViewModelComponent
            .builder()
            .applicationModule(ApplicationModule(application))
            .apiModule(ApiModuleTest(animalService))
            .sharePrefsModule(PrefsModuleTest(prefs))
            .build()
            .inject(listViewModel)
    }


    @Test
    fun getAnimalSuccessPath() {
        Mockito.`when`(prefs.getUpdateKey()).thenReturn(key)
        val animal = Animal("Dragon", null, null, null, null, null, null)
        val animalList = listOf(animal)
        val single = Single.just(animalList)
        Mockito.`when`(animalService.getAnimals(key)).thenReturn(single)
        listViewModel.refresh()

        Assert.assertEquals(1, listViewModel.animals.value?.size)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getAnimalFailurePath() {
        Mockito.`when`(prefs.getUpdateKey()).thenReturn(key)
        val testSingle = Single.error<List<Animal>>(Throwable())
        val keySingle = Single.just(ApiKey("OK", key))

        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)
        Mockito.`when`(animalService.getApiKey()).thenReturn(keySingle)

        listViewModel.refresh()

        Assert.assertEquals(null, listViewModel.animals.value)
        Assert.assertEquals(false, listViewModel.loading.value)
        Assert.assertEquals(true, listViewModel.loadError.value)
    }

    @Test
    fun getKeyFailurePath() {
        Mockito.`when`(prefs.getUpdateKey()).thenReturn(null)
        val keySingle = Single.error<ApiKey>(Throwable())
        Mockito.`when`(animalService.getApiKey()).thenReturn(keySingle)
        listViewModel.refresh()
        listViewModel.loading.value?.let { Assert.assertFalse(it) }
        listViewModel.loadError.value?.let { Assert.assertTrue(it) }
        Assert.assertNull(listViewModel.animals.value)
    }

    @Test
    fun getKeySuccessPath() {
        Mockito.`when`(prefs.getUpdateKey()).thenReturn(null)
        val keySingle = Single.just(ApiKey("OK", key))
        Mockito.`when`(animalService.getApiKey()).thenReturn(keySingle)

        val animal = Animal("Dragon", null, null, null, null, null, null)
        val animalList = listOf(animal)
        val testSingle = Single.just(animalList)
        Mockito.`when`(animalService.getAnimals(key)).thenReturn(testSingle)
        listViewModel.refresh()
        Assert.assertEquals(1, listViewModel.animals.value?.size)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Before
    fun setUpRxschedulers() {
        val immediate = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }


    }
}
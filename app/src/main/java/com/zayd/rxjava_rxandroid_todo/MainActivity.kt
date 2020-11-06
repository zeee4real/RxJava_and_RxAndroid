package com.zayd.rxjava_rxandroid_todo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.qualifiedName
    private val disposables = CompositeDisposable() //should be kept in viewModels

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * basic observable example
         */

        basicObservableExample()

        /**
         * create operator example
         * one task and list of tasks
         */

        createOperatorExample()

        /**
         * just operator example
         * there can be a max of 10 values only using just operator
         */
        justOperatorExample()

        /**
         * range operator example
         * best way to use this when doing heavy expensive operation on loops
         */

        rangeOperatorExample()

        /**
         * repeats tasks assigned to it
         */
        repeatOperatorExample()
    }

    private fun basicObservableExample() {
        val taskObservable: Observable<Task> = Observable
            .fromIterable(DataSource.createTaskList())
            .subscribeOn(Schedulers.io())
            .filter {
                Log.d(TAG, Thread.currentThread().name)
                Thread.sleep(1000)
                it.isComplete
            }
            .observeOn(AndroidSchedulers.mainThread())

        taskObservable.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, "onSubscribe: called ")
                disposables.add(d)
            }

            override fun onNext(t: Task?) {
                Log.d(TAG, "OnNext: " + Thread.currentThread().name)
                Log.d(TAG, "OnNext: ${t?.description}")
                Thread.sleep(1000)
            }

            override fun onError(e: Throwable?) {
                Log.e(TAG, "onError: ", e)
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete: called")
            }
        })

        taskObservable.subscribe(Consumer {

        })
    }

    private fun createOperatorExample() {
        //val task = Task("Walk the dog", false, 3)

        val task = DataSource.createTaskList()
        val singleTaskObservable = Observable
            .create<Task> {
                for (t in task) {
                    if (!it.isDisposed) {
                        it.onNext(t)
                    }
                }
                if (!it.isDisposed) {
                    it.onComplete()
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        singleTaskObservable.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable?) {
            }

            override fun onNext(t: Task?) {
                Log.d(TAG, "onNext: " + t?.description)
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {

            }

        })
    }

    private fun justOperatorExample() {
        val task = Task("Walk the dog", false, 3)

//        val task = DataSource.createTaskList()
        val singleTaskObservable = Observable
            .just(task)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        singleTaskObservable.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable?) {
            }

            override fun onNext(t: Task?) {
                Log.d(TAG, "onNext: " + t?.description)
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {

            }

        })
    }

    private fun rangeOperatorExample() {
        /**
         * basic example
         */
        /*val observable: Observable<Int> = Observable
            .range(0, 9)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        observable.subscribe(object : Observer<Int> {
            override fun onSubscribe(d: Disposable?) {

            }

            override fun onNext(t: Int?) {
                Log.d(TAG, "onNext: $t")
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {
            }

        })*/

        val observable: Observable<Task> = Observable
            .range(0, 9)
            .subscribeOn(Schedulers.io())
            .map {
                Log.d(TAG, "apply: ${Thread.currentThread().name}")
                return@map Task("This is a task with priority: $it", false, it)
            }
            .takeWhile {
                return@takeWhile it.priority < 9
            }
            .observeOn(AndroidSchedulers.mainThread())

        observable.subscribe(object : Observer<Task> {
            override fun onSubscribe(d: Disposable?) {

            }

            override fun onNext(t: Task?) {
                Log.d(TAG, "onNext: ${t?.priority}")
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {
            }
        })
    }

    private fun repeatOperatorExample() {
        val observable: Observable<Int> = Observable
            .range(0, 3)
            .subscribeOn(Schedulers.io())
            .repeat(3)
            .observeOn(AndroidSchedulers.mainThread())

        observable.subscribe(object : Observer<Int> {
            override fun onSubscribe(d: Disposable?) {

            }

            override fun onNext(t: Int?) {
                Log.d(TAG, "onNext: $t")
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}
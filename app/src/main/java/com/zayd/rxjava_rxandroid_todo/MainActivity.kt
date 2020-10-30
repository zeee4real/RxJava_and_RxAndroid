package com.zayd.rxjava_rxandroid_todo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskObservable: Observable<Task> = Observable
            .fromIterable(DataSource.createTaskList())
            .subscribeOn(Schedulers.io())
            .filter {
                Log.d(TAG, Thread.currentThread().name)
                Thread.sleep(1000)
                it.isComplete
            }
            .observeOn(AndroidSchedulers.mainThread())

        taskObservable.subscribe(object : Observer<Task>{
            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, "onSubscribe: called ")
            }

            override fun onNext(t: Task?) {
                Log.d(TAG, "OnNext: "+Thread.currentThread().name)
                Log.d(TAG, "OnNext: ${t?.description}")
                Thread.sleep(1000)
            }

            override fun onError(e: Throwable?) {
                Log.e(TAG,"onError: ", e)
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete: called")
            }

        })
    }
}
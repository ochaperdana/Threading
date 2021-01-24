package com.example.threading

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.threading.databinding.ActivityMainBinding
import com.example.threading.injection.DaggerMainComponent
import com.example.threading.viewmodel.MainViewModel
import kotlinx.coroutines.*
import java.math.BigInteger
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
  private lateinit var mBinding: ActivityMainBinding
  private val mViewModel: MainViewModel by viewModels()
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job
  private lateinit var job: Job

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mBinding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(mBinding.root)
    DaggerMainComponent.create().injectMainActivity(this)
    job = Job()
    initView()
  }

  private fun initView() {
    with(mBinding) {
      Glide.with(this@MainActivity).asGif().load(R.raw.spiderman).into(ivPerformer)

      /*-- place button click listener below --*/
      btNormal.setOnClickListener {
        val input = etInput.text?.toString()?.toInt() ?: 1
        val result = calculateFactorialInMainThread(input)
        setFactorialResult(result)
      }

      /*-- end of button click listener --*/

      btNew.setOnClickListener {
        // Using Thread
        /*Thread(Runnable {
          runOnUiThread{
            setFactorialResult(calculateFactorialInMainThread(etInput.text?.toString()?.toInt() ?: 1))
          }
        }).start()*/

        // Using Coroutine
        launch {
          withContext(Dispatchers.Main) {
            runOnUiThread {
              setFactorialResult(
                calculateFactorialInMainThread(
                  etInput.text?.toString()?.toInt() ?: 1
                )
              )
            }
          }
        }
      }

      btLoading.setOnClickListener {
        val intent = Intent(this@MainActivity, LoadingSimulatorActivity::class.java)
        startActivity(intent)
      }
    }
  }

  private fun calculateFactorialInMainThread(number: Int): BigInteger {
    var factorial = BigInteger.ONE
    for (i in 1..number) {
      factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
    }
    return factorial
  }

  private fun setFactorialResult(number: BigInteger) {
    mBinding.tvResult.text = "$number".substring(0, 9)
  }

  override fun onDestroy() {

    // Using Thread
    //mHandlerThread.quitSafely()

    // Using Coroutine
    job.cancel()

    super.onDestroy()
  }


}
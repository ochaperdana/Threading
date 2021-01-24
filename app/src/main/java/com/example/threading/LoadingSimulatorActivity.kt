package com.example.threading

import android.os.Bundle
import android.os.HandlerThread
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.threading.databinding.ActivityLoadingSimulatorBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoadingSimulatorActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivityLoadingSimulatorBinding
    private val mHandlerThread = HandlerThread("Name")
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_loading_simulator)

        binding = ActivityLoadingSimulatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        job = Job()
        initView()
    }

    private fun initView() {
        with(binding) {
            /*if (!mHandlerThread.isAlive){
                mHandlerThread.start()
            }*/

            btnLoad.setOnClickListener {
                // Using Thread
                /*Handler(mHandlerThread.looper).post{
                    for (i in 1 .. 100){
                        Thread.sleep(100L)
                        runOnUiThread {
                            binding.progressBar.setProgress(i)
                        }
                    }
                    Toast.makeText(this@LoadingSimulatorActivity, "Finished!", Toast.LENGTH_LONG).show()
                }*/

                // Using Coroutine
                launch {
                    withContext(Dispatchers.IO) {
                        for (i in 1..100) {
                            delay(100L)
                            runOnUiThread {
                                binding.progressBar.progress = i
                            }
                        }
                    }
                    Toast.makeText(this@LoadingSimulatorActivity, "Finished!", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    override fun onDestroy() {
        // Using Thread
        //mHandlerThread.quitSafely()

        // Using Coroutine
        job.cancel()


        super.onDestroy()
    }
}
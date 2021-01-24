package com.example.threading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.threading.databinding.ActivityMainBinding
import com.example.threading.injection.DaggerMainComponent
import com.example.threading.viewmodel.MainViewModel
import java.math.BigInteger

class MainActivity : AppCompatActivity() {
  private lateinit var mBinding: ActivityMainBinding
  private val mViewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mBinding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(mBinding.root)
    DaggerMainComponent.create().injectMainActivity(this)
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
    }
  }

  private fun calculateFactorialInMainThread(number: Int): BigInteger {
    var factorial = BigInteger.ONE
    for (i in 1 .. number) {
      factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
    }
    return factorial
  }

  private fun setFactorialResult(number: BigInteger) {
    mBinding.tvResult.text = "$number".substring(0,9)
  }
}
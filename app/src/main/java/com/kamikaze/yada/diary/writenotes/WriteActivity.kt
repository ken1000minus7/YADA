package com.kamikaze.yada.diary.writenotes

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.kamikaze.yada.R
import com.kamikaze.yada.R.*
import com.kamikaze.yada.databinding.ActivityMainBinding
import com.kamikaze.yada.databinding.ActivityWriteBinding
import com.squareup.picasso.Picasso
import uk.co.senab.photoview.PhotoView
import uk.co.senab.photoview.PhotoViewAttacher

class WriteActivity : AppCompatActivity() {
    private lateinit var navController: NavController
   public var position = 0
    var title : String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setTheme(R.style.Theme_YADA)
        val binding = ActivityWriteBinding.inflate(layoutInflater)

        val navHostFragment = supportFragmentManager.findFragmentById(id.myNavHostFragment2) as NavHostFragment
        navController = navHostFragment.navController
        position=intent.getIntExtra("position",0)
        title = intent.getStringExtra("title")
        Log.d("position", position.toString());
        title?.let { Log.d("tit", it) }
        setContentView(binding.root)
        animDuration=resources.getInteger(android.R.integer.config_shortAnimTime)
        zoomedImg=findViewById(R.id.zoom_img)
        container=findViewById(R.id.myNavHostFragment2)
        val attacher=PhotoViewAttacher(zoomedImg)
        attacher.update()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    override fun onBackPressed() {

        val et = findViewById<EditText>(R.id.edithere)
        val tv = findViewById<TextView>(R.id.seehere)
        val custops=findViewById<View>(R.id.customize_options)
        val editimg=findViewById<ImageView>(R.id.edit_diary)
        val doneimg=findViewById<ImageView>(R.id.done_edit_diary)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        val bottomSheetBehavior = BottomSheetBehavior.from(custops)
        if(zoomedImg.visibility==View.VISIBLE)
        {
            currAnim?.cancel()

            val startBoundsInt = Rect()
            val finalBoundsInt = Rect()
            val globalOffset = Point()

            originalImg?.getGlobalVisibleRect(startBoundsInt)
            container.getGlobalVisibleRect(finalBoundsInt, globalOffset)
            startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
            finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

            val startBounds = RectF(startBoundsInt)
            val finalBounds = RectF(finalBoundsInt)

            val startScale: Float
            if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
                startScale = startBounds.height() / finalBounds.height()
                val startWidth: Float = startScale * finalBounds.width()
                val deltaWidth: Float = (startWidth - startBounds.width()) / 2
                startBounds.left -= deltaWidth.toInt()
                startBounds.right += deltaWidth.toInt()
            } else {
                startScale = startBounds.width() / finalBounds.width()
                val startHeight: Float = startScale * finalBounds.height()
                val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
                startBounds.top -= deltaHeight.toInt()
                startBounds.bottom += deltaHeight.toInt()
            }

            currAnim = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(zoomedImg, View.X, startBounds.left)).apply {
                    with(ObjectAnimator.ofFloat(zoomedImg, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(zoomedImg, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(zoomedImg, View.SCALE_Y, startScale))
                }
                duration = animDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        originalImg?.alpha = 1f
                        zoomedImg.visibility = View.GONE
                        currAnim = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        originalImg?.alpha = 1f
                        zoomedImg.visibility = View.GONE
                        currAnim = null
                    }
                })
                start()
            }
        }
        else if(bottomSheetBehavior.state==BottomSheetBehavior.STATE_EXPANDED)
        {
            bottomSheetBehavior.state=BottomSheetBehavior.STATE_COLLAPSED;
        }
        else if (et.isVisible){
            et.visibility = View.GONE
            tv.visibility = View.VISIBLE
            custops.visibility=View.GONE
            editimg.visibility=View.VISIBLE
            doneimg.visibility=View.GONE
            fab.visibility=View.GONE
            val fragment =WriteDiaryFragment()
            if(fragment==null) Log.d("fragment","sadge")
            else Log.d("fragment", fragment.currcolor.toString())
            fragment.restoreBg()
        }
        else{
            super.onBackPressed()
        }

    }

    companion object{
        var currAnim: Animator? = null
        var animDuration : Int = 1
        lateinit var zoomedImg : PhotoView
        lateinit var container : View
        var originalImg : ImageView? = null
        fun openZoomed(originalImg : ImageView,imgUrl:String)
        {
            currAnim?.cancel()
            this.originalImg=originalImg
            Picasso.get().load(imgUrl).into(zoomedImg)

            val startBoundsInt = Rect()
            val finalBoundsInt = Rect()
            val globalOffset = Point()

            originalImg.getGlobalVisibleRect(startBoundsInt)
            container.getGlobalVisibleRect(finalBoundsInt, globalOffset)
            startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
            finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

            val startBounds = RectF(startBoundsInt)
            val finalBounds = RectF(finalBoundsInt)

            val startScale: Float
            if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
                startScale = startBounds.height() / finalBounds.height()
                val startWidth: Float = startScale * finalBounds.width()
                val deltaWidth: Float = (startWidth - startBounds.width()) / 2
                startBounds.left -= deltaWidth.toInt()
                startBounds.right += deltaWidth.toInt()
            } else {
                startScale = startBounds.width() / finalBounds.width()
                val startHeight: Float = startScale * finalBounds.height()
                val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
                startBounds.top -= deltaHeight.toInt()
                startBounds.bottom += deltaHeight.toInt()
            }


            originalImg.alpha = 0f
            zoomedImg.visibility = View.VISIBLE


            zoomedImg.pivotX = 0f
            zoomedImg.pivotY = 0f

            currAnim = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(
                    zoomedImg,
                    View.X,
                    startBounds.left,
                    finalBounds.left)
                ).apply {
                    with(ObjectAnimator.ofFloat(zoomedImg, View.Y, startBounds.top, finalBounds.top))
                    with(ObjectAnimator.ofFloat(zoomedImg, View.SCALE_X, startScale, 1f))
                    with(ObjectAnimator.ofFloat(zoomedImg, View.SCALE_Y, startScale, 1f))
                }
                duration = animDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        currAnim = null

                    }

                    override fun onAnimationCancel(animation: Animator) {
                        currAnim = null
                    }
                })
                start()
            }
        }
    }

}
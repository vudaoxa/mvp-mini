package vn.tieudieu.fragmentstackmanager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by chienchieu on 28/01/2016.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(resLayout)
        initVariables()
        initViews(savedInstanceState)
    }

    /**
     * return res of layout for activity
     * return
     */
    protected abstract val resLayout: Int

    /**
     * initial variables
     */
    protected abstract fun initVariables()

    /**
     * initial views
     * param savedInstanceState
     */
    protected abstract fun initViews(savedInstanceState: Bundle?)
}

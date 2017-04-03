//package net.mfilm.ui.login
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import net.mfilm.R
//import net.mfilm.ui.base.stack.BaseStackFragment
//import io.reactivex.disposables.CompositeDisposable
//import kotlinx.android.synthetic.main.activity_login.*
//import javax.inject.Inject
//
///**
// * Created by tusi on 3/21/17.
// */
//class LoginFragment : BaseStackFragment(), LoginMvpView{
//    @Inject
//    lateinit var mPresenter: LoginMvpPresenter<LoginMvpView>
//
//    companion object{
//        fun newInstance():LoginFragment{
//            val fragment = LoginFragment()
//            return fragment
//        }
//    }
//
//    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?)=
//            inflater?.inflate(R.layout.activity_login, container, false)
//
//    override fun initField() {
//        activityComponent.inject(this)
//        mPresenter.onAttach(this)
//    }
//
//    override fun initView() {
//        btn_server_login.setOnClickListener {
////            mPresenter.onServerLoginClick()
//        }
//    }
//
//    override fun openMainActivity() {
//        hideLoading()
//    }
//}
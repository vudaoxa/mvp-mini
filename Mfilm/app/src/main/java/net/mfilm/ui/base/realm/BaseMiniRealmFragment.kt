package net.mfilm.ui.base.realm

import com.afollestad.materialdialogs.MaterialDialog
import io.realm.RealmObject
import net.mfilm.R
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.holders.TYPE_ITEM_FILTER
import net.mfilm.ui.base.rv.wrappers.LinearLayoutManagerWrapper
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.manga.UndoBtn
import net.mfilm.ui.manga.rv.BaseRvRealmAdapter
import net.mfilm.utils.DialogUtil
import net.mfilm.utils.enable
import net.mfilm.utils.show
import timber.log.Timber

/**
 * Created by MRVU on 7/3/2017.
 */
abstract class BaseMiniRealmFragment<V : RealmObject> : BaseStackFragment(), RealmMiniMvpView<V> {
    private var mAllSelected: Boolean? = null
    override var allSelected: Boolean?
        get() = mAllSelected
        set(value) {
            mAllSelected = value
            var text = R.string.select_all
            if (mAllSelected == true)
                text = R.string.deselect_all
            btnSelect.setText(text)
            mAllSelected?.run {
                //start selected
                bottomFunView.show(true)
                btnDone.show(true)
                updateBtnSubmit(adapterMain)
            } ?: let {
                //no selection
                adapterMain?.onOriginal()
            }
        }

    override fun initViews() {
        initRv()
        initSearch()
        initSpnFilters()
        initEmptyDataView()
        initBottomFun()
        initBtnDone()
    }

    override fun initRv() {
        rvMain.run {
            layoutManagerMain = LinearLayoutManagerWrapper(context)
            layoutManager = layoutManagerMain
        }
    }

    fun updateBtnSubmit(adapter: BaseRvRealmAdapter<V>?) {
        adapter?.run {
            btnSubmit.enable(countSelected > 0)
        }
    }

    fun onOriginal(ad: BaseRvRealmAdapter<V>, mangaFavoriteRealms: List<V>? = null) {
        ad.clear()
        val x = mangaFavoriteRealms == null
        Timber.e("-----onOriginal-------------$x-----------------")
        showEmptyDataView(x)
        setScrollToolbarFlag(x)
        mangaFavoriteRealms?.run {
            ad.addAll(this)
        }
        ad.notifyDataSetChanged()
    }

    protected var selectedItems: List<V>? = null
    fun doByAllSelected(ad: BaseRvRealmAdapter<V>, mangaFavoriteRealms: List<V>? = null) {
        allSelected?.run {
            if (!isVisible) return
            val x = mangaFavoriteRealms == null
            showEmptyDataView(x)
            setScrollToolbarFlag(x)
            undoBtn?.onSelected(
                    {
                        val y = ad.recoverAll(selectedItems)
                        Timber.e("----------recoverAll----------------$y-------${ad.itemCount}-----------")
                        ad.notifyDataSetChanged()
                        btnSubmit.enable(ad.countSelected > 0)
                    },
                    {
                        val z = ad.removeAll(selectedItems)
                        Timber.e("----------removeAll----------------$z------------------")
                        ad.notifyDataSetChanged()
                        btnSubmit.enable(ad.countSelected > 0)
                    },
                    {
                        deleteAll()
                    }
            )
        } ?: let {
            onOriginal(ad, mangaFavoriteRealms)
        }
    }

    override fun deleteAll(f: (() -> Unit)?) {
        Timber.e("---deleteAll-------allSelected--------- $allSelected-----------------")
        if (allSelected == true && isDataEmpty()) {
            f?.invoke()
        }
    }

    override fun initBtnDone() {
        btnDone.setOnClickListener { done() }
    }

    override fun done() {
        allSelected = null
        undoBtn?.reset({ deleteAll() })
        btnDone.show(false)
        bottomFunView.show(false)
    }

    override fun toggleSelectAll() {
        Timber.e("-----toggleSelectAll------allSelected------$allSelected------------------------")
        adapterMain?.run {
            allSelected = onSelected(-1, !allSelected!!)
        }
    }

    override fun initBottomFun() {
        btnSelect.setOnClickListener { toggleSelectAll() }
        btnSubmit.setOnClickListener { submit() }
        undoBtn = UndoBtn(null, btnUndo, { undo() })
    }


    override fun toggleEdit(edit: Boolean) {
        if (!edit)
            done()
        else {
            adapterMain?.run {
                allSelected = onSelected(-1)
            }
        }
    }


    override fun submit() {
        Timber.e("-------submit------------------------------------")

        val items = adapterMain?.selectedItems()?.map { it.value }
        items?.run {
            Timber.e("------selectedItems--------$indices---------------------")
            if (isNotEmpty()) {
                selectedItems = this
                DialogUtil.showMessageConfirm(context, R.string.notifications, R.string.confirm_delete,
                        MaterialDialog.SingleButtonCallback { _, _ -> doIt() })
            } else {
                updateBtnSubmit(adapter)
            }
        } ?: let {
            updateBtnSubmit(adapter)
        }
    }

    private fun doIt() {
        Timber.e("-------doIt--------------------")
        undoBtn?.onUndo(false)
        onToggle()
    }

    override fun undo() {
        selectedItems?.run {
            undoBtn?.onUndo(true)
            onToggle()
        }
    }

    override fun isDataEmpty(): Boolean {
        adapterMain?.run {
            return itemCount == 0
        }
        return true
    }

    override fun adapterClicked(ad: BaseRvRealmAdapter<V>, position: Int, f: (() -> Unit)?) {
        ad.itemsSelectable?.run {
            allSelected = ad.onSelected(position)
        } ?: let {
            f?.invoke()
        }
    }

    override fun onClick(position: Int, event: Int) {
        Timber.e("---------------------onClick--------------------$position")
        when (event) {
            TYPE_ITEM -> {
                adapterMain?.run {
                    adapterClicked(this, position)
                }
            }
            TYPE_ITEM_FILTER -> {
                adapterFilter?.run {
                    adapterClicked(this, position)
                }
            }
        }
    }

    override fun onLongClick(position: Int, event: Int) {
        Timber.e("---------------------onLongClick--------------------$position")
        when (event) {
            TYPE_ITEM -> {
                adapterMain?.run {
                    allSelected = onSelected(position)
                }
            }
            TYPE_ITEM_FILTER -> {
                adapterFilter?.run {
                    allSelected = onSelected(position)
                }
            }
        }
    }
}
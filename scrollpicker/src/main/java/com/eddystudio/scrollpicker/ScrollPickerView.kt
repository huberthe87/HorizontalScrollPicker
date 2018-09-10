package com.eddystudio.scrollpicker

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

class ScrollPickerView<T> @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int? = 0) :
    RelativeLayout(context, attributeSet, defStyleAttr!!) {
  private val recyclerView by lazy { findViewById<RecyclerView>(R.id.picker_recyclerview) }
  private val centerView by lazy { findViewById<View>(R.id.center_view) }

  private var scrollPickerAdapter: ScrollPickerAdapter<T>? = null
  private var onItemSelectedListener: OnItemSelectedListener? = null
  private var onItemUnselectedListener: OnItemUnselectedListener? = null

  init {
    LayoutInflater.from(context).inflate(R.layout.layout_scroll_picker_view, this, true)
  }

  private fun setup() {
    scrollPickerAdapter!!.onItemClickListener = object : ScrollPickerAdapter.OnItemClickListener {
      override fun onClicked(view: View, position: Int) {
        recyclerView.smoothScrollToPosition(position)
      }
    }

    recyclerView.adapter = scrollPickerAdapter
    val layoutManager = ScrollPickerLayoutManager(context)

    layoutManager.onItemSelectedListener = onItemSelectedListener
    layoutManager.onItemUnselectedListener = onItemUnselectedListener

    recyclerView.layoutManager = layoutManager
    LinearSnapHelper().attachToRecyclerView(recyclerView)
    recyclerView.smoothScrollToPosition(0)
  }

  private fun convertDpToPixel(dp: Float, context: Context): Float {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    val padding = w / 2 - convertDpToPixel(32f, context).toInt()
    recyclerView.setPadding(padding, 0, padding, 0)
  }

  class Builder<T>(val scrollPickerView: ScrollPickerView<T>) {
    public fun build() {
      if(scrollPickerView.scrollPickerAdapter != null)
        scrollPickerView.setup()
    }

    public fun qucickRecyclerViewAdapter(adapter: ScrollPickerAdapter<T>): Builder<T> {
      scrollPickerView.scrollPickerAdapter = adapter
      return this
    }

    public fun onItemSelectedListener(onItemSelectedListener: OnItemSelectedListener): Builder<T> {
      scrollPickerView.onItemSelectedListener = onItemSelectedListener
      return this
    }

    public fun onItemUnselectedListener(onItemUnselectedListener: OnItemUnselectedListener): Builder<T> {
      scrollPickerView.onItemUnselectedListener = onItemUnselectedListener
      return this
    }

  }
}
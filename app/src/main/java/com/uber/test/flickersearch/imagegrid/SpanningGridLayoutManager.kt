package com.glowroad.test.recyclerviewcomponents

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uber.test.flickersearch.R

open class SpanningGridLayoutManager constructor(context: Context, spanCount: Int, rvWidth: Int) : GridLayoutManager(context, spanCount) {
    private val recyclerViewWidth = rvWidth
    protected open val margin = context.resources.getDimensionPixelSize(R.dimen.default_margin)

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams = spanLayoutSize(super.generateDefaultLayoutParams())

    override fun generateLayoutParams(c: Context?, attrs: AttributeSet?): RecyclerView.LayoutParams = spanLayoutSize(super.generateLayoutParams(c, attrs))

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): RecyclerView.LayoutParams = spanLayoutSize(super.generateLayoutParams(lp))

    private fun spanLayoutSize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
        layoutParams.width = horizontalSpace
        layoutParams.height = (horizontalSpace * ASPECT_RATIO).toInt()
        layoutParams.setMargins(margin, margin, margin, margin)
        return layoutParams
    }

    private val horizontalSpace: Int
        get() = (recyclerViewWidth.toFloat() / spanCount).toInt() - paddingRight - paddingLeft - 2 * margin

    companion object {
        private const val ASPECT_RATIO = 0.75f
    }
}
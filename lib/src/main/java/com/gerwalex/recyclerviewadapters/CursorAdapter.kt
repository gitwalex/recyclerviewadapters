package com.gerwalex.recyclerviewadapters

import android.database.Cursor
import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter fuer RecyclerView mit Cursor.
 */
abstract class CursorAdapter : BaseSwipeDragDropAdapter() {

    protected var cursor: Cursor? = null
    private var mRowIdColumnIndex = RecyclerView.NO_POSITION

    /**
     * @return Anzahl der im ExcludedCatsAdapter vorhandenen Items abzueglich der bereits entfernten
     * Items
     */
    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    /**
     * @param view ChildView der RecyclerView
     * @return ID des Items an dieser Psoition oder NO_ID
     */
    fun getItemId(view: View): Long {
        return recyclerView?.let {
            val position = it.getChildAdapterPosition(view)
            if (position != RecyclerView.NO_POSITION)
                getItemId(position) else RecyclerView.NO_ID
        } ?: RecyclerView.NO_ID
    }

    /**
     * @param position Position in der RecyclerView
     * @return ID des Items an dieser Psoition
     */
    override fun getItemId(position: Int): Long {
        return cursor?.let {
            if (mRowIdColumnIndex != RecyclerView.NO_POSITION && it.moveToPosition(position))
                it.getLong(mRowIdColumnIndex) else RecyclerView.NO_ID
        } ?: RecyclerView.NO_ID
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cursor?.let {
            it.moveToPosition(position)
            onBindViewHolder(holder, it, position)
        }
    }

    protected abstract fun onBindViewHolder(holder: ViewHolder, mCursor: Cursor, position: Int)

    /**
     * Swap in a new Cursor. The old Cursor is *not* closed.
     *
     * @param c new Cursor
     */
    @CallSuper
    open fun swap(c: Cursor) {
        cursor = c
        mRowIdColumnIndex = c.getColumnIndex("_id")
        notifyDataSetChanged()
    }

    /**
     *
     */
    init {
        this.setHasStableIds(true)
    }
}
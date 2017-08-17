package com.example.android.myalbum;

import android.view.View;

/**
 * Created by android on 17-8-17.
 */

public interface OnRecyclerViewItemListener {
    void onItemClick(View view, int position);
    void onItemLongClick(View view, int position);
}

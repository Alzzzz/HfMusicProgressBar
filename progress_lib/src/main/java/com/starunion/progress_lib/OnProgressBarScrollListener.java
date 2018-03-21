package com.starunion.progress_lib;

/**
 * Discription:
 * Created by sz on 2018/3/21.
 */

public interface OnProgressBarScrollListener {
    void onScrollStart();

    /**
     * 活动结束
     * @param startLength 结束时的开始时间
     */
    void onScrollEnd(long startLength);
}

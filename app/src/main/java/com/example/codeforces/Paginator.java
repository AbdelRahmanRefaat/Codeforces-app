package com.example.codeforces;

import androidx.recyclerview.widget.LinearLayoutManager;

public class Paginator {
    private boolean isLoading = false;
    int pageNumber = 0;
    int itemsCount = 0;
    private int visibleItemsCount = 0;
    private int totalItemsCount = 0;
    private int pastVisibleItems = 0;
    private int previousTotal = 0;
    private int threshold = 0;
    boolean isExtraData = false;
    public void init(int pageNumber,int itemsCount) {
        this.pageNumber = pageNumber;
        this.itemsCount = itemsCount;
        visibleItemsCount = 0;
        totalItemsCount = 0;
        pastVisibleItems = 0;
        previousTotal = 0;
        threshold = 0;
        isLoading = true;
        isExtraData = true;
    }
    public void performChanged(LinearLayoutManager linearLayoutManager) {
        visibleItemsCount = linearLayoutManager.getChildCount();
        totalItemsCount = linearLayoutManager.getItemCount();
        pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
    }
    public boolean loadMore() {
        if (!isLoading && totalItemsCount - visibleItemsCount <= pastVisibleItems + threshold) {
            pageNumber++;
            isLoading = true;
            return true;
        } else if (isLoading && totalItemsCount > previousTotal) {
            isLoading = false;
            previousTotal = totalItemsCount;
        }
        return false;
    }
    public void increasePageNumber() {
        pageNumber++;
    }
}
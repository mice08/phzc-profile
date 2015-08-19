package com.phzc.profile.web.form;

/**
 *
 */
public class PageSet {
	public int curPage=1; // 当前页  
    public int pageSize; // 一共有多少页  
    public int rowCount; // 一共有多少行  
    public int rowSize = 10; // 每页多少行  
  
    // 根据总行数计算总页数  
    public void countMaxPage() {  
        if (this.rowCount % this.rowSize == 0) {  
            this.pageSize = this.rowCount / this.rowSize;  
        } else {  
            this.pageSize = this.rowCount / this.rowSize + 1;  
        }  
    }  
  
    public int getCurPage() {  
        return curPage;  
    }  
  
    public void setCurPage(int curPage) {  
        this.curPage = curPage;  
    }  
  
    public int getPageSize() {  
        return pageSize;  
    }  
  
    public void setPageSize(int pageSize) {  
        this.pageSize = pageSize;  
    }  
  
    public int getRowCount() {  
        return rowCount;  
    }  
  
    public void setRowCount(int rowCount) {  
        this.rowCount = rowCount;  
    }  
  
    public int getRowSize() {  
        return rowSize;  
    }  
  
    public void setRowSize(int rowSize) {  
        this.rowSize = rowSize;  
    }  
}

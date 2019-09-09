package com.wlison.view.nicelibrary.model;

/**
 * Author: Simon
 * Date：9/4/2019 15:45
 * Desc:
 */
public class NiceItemString {
    private String title;       // item Context
    private int selectType;     // 选中类型
    private boolean imgPic;     // 图片选中
    private boolean background; // 背景选中

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSelectType() {
        return selectType;
    }

    public void setSelectType(int selectType) {
        this.selectType = selectType;
    }

    public boolean isImgPic() {
        return imgPic;
    }

    public void setImgPic(boolean imgPic) {
        this.imgPic = imgPic;
    }

    public boolean isBackground() {
        return background;
    }

    public void setBackground(boolean background) {
        this.background = background;
    }
}

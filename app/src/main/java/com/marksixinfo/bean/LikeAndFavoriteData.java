package com.marksixinfo.bean;

/**
 *  点赞,收藏,关注,回复操作实体
 *
 * @Auther: Administrator
 * @Date: 2019/4/6 0006 19:24
 * @Description:
 */
public class LikeAndFavoriteData {


    /**
     * num : 1
     * type : 1
     */

    private String id;
    private int num;
    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

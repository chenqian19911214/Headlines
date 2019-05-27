package com.marksixinfo.bean;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/3/23 0023 12:34
 * @Description:
 */
public class DetailCommentListData {


    /**
     * total : 3
     * size : 10
     * page_num : 1
     * list : [{"Id":"28","Content":"高手，就是踏实，值得一顶!！我永远支持你！实力派高手！！","Nickname":"龙游四海","Add_Time":"2019/03/10 05:53","Face":"/static/img/face/default.jpeg"},{"Id":"43","Content":"相信你的实力,_你是最棒的_彩民有你发大财.彩民太需要你这样的高手了．期望你能继续发好料 ","Nickname":"纵横天下","Add_Time":"2019/03/10 06:28","Face":"/static/img/face/default.jpeg"},{"Id":"44","Content":"金不怕火来炼，好料不怕没人顶，诚心实意帮彩民，真正高手就是你","Nickname":"纵横天下","Add_Time":"2019/03/10 06:28","Face":"/static/img/face/default.jpeg"}]
     */

    private int total;
    private int size;
    private int page_num;
    private List<DetailCommentData> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage_num() {
        return page_num;
    }

    public void setPage_num(int page_num) {
        this.page_num = page_num;
    }

    public List<DetailCommentData> getList() {
        return list;
    }

    public void setList(List<DetailCommentData> list) {
        this.list = list;
    }


    @Override
    public String toString() {
        return "DetailCommentListData{" +
                "total='" + total + '\'' +
                ", size=" + size +
                ", page_num=" + page_num +
                ", list=" + list +
                '}';
    }
}

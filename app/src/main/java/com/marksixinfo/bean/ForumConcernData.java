package com.marksixinfo.bean;

import java.util.List;

/**
 *  一期论坛
 *
 * @Auther: Administrator
 * @Date: 2019/4/1 0001 14:32
 * @Description:
 */
public class ForumConcernData {

    /**
     * total : 32
     * size : 10
     * page_num : 4
     * list : [{"Id":"4880","Title":"038期：香港人【三肖中特】已公开","Nickname":"香港人","Add_Time":"2019/03/31 13:59","Face":"https://images.34399.com//files/20190226/074137.png","Color":null,"Is_Top":"0","Is_Predict":"0","View":"5467","Like":"580","Reply":"38"},{"Id":"4878","Title":"038期：香港人【精准四肖】已公开","Nickname":"香港人","Add_Time":"2019/03/31 13:59","Face":"https://images.34399.com//files/20190226/074137.png","Color":null,"Is_Top":"0","Is_Predict":"0","View":"2862","Like":"480","Reply":"28"},{"Id":"4864","Title":"038期：香港人【大仙六肖】已公开","Nickname":"香港人","Add_Time":"2019/03/31 13:58","Face":"https://images.34399.com//files/20190226/074137.png","Color":null,"Is_Top":"0","Is_Predict":"0","View":"1044","Like":"338","Reply":"22"},{"Id":"4853","Title":"038期：香港人【精准⑳码】已公开","Nickname":"香港人","Add_Time":"2019/03/31 13:55","Face":"https://images.34399.com//files/20190226/074137.png","Color":null,"Is_Top":"0","Is_Predict":"0","View":"994","Like":"416","Reply":"24"},{"Id":"4859","Title":"038期：香港人【三期必开】已公开","Nickname":"香港人","Add_Time":"2019/03/31 13:52","Face":"https://images.34399.com//files/20190226/074137.png","Color":null,"Is_Top":"0","Is_Predict":"0","View":"2010","Like":"318","Reply":"18"},{"Id":"4879","Title":"038期：香港人【绝杀三肖】已公开","Nickname":"香港人","Add_Time":"2019/03/31 13:51","Face":"https://images.34399.com//files/20190226/074137.png","Color":null,"Is_Top":"0","Is_Predict":"0","View":"2094","Like":"527","Reply":"28"},{"Id":"4877","Title":"038期：香港人【三头中特】已公开","Nickname":"香港人","Add_Time":"2019/03/31 13:50","Face":"https://images.34399.com//files/20190226/074137.png","Color":null,"Is_Top":"0","Is_Predict":"0","View":"1974","Like":"322","Reply":"26"},{"Id":"4855","Title":"038期：香港人【五码平特】已公开","Nickname":"香港人","Add_Time":"2019/03/31 13:49","Face":"https://images.34399.com//files/20190226/074137.png","Color":null,"Is_Top":"0","Is_Predict":"0","View":"1061","Like":"290","Reply":"22"},{"Id":"4860","Title":"038期：香港人【四季中特】已公开","Nickname":"香港人","Add_Time":"2019/03/31 13:48","Face":"https://images.34399.com//files/20190226/074137.png","Color":null,"Is_Top":"0","Is_Predict":"0","View":"1135","Like":"305","Reply":"19"},{"Id":"4873","Title":"038期：香港人【火爆家野】已公开","Nickname":"香港人","Add_Time":"2019/03/31 13:47","Face":"https://images.34399.com//files/20190226/074137.png","Color":null,"Is_Top":"0","Is_Predict":"0","View":"1264","Like":"307","Reply":"27"}]
     */

    private String total;
    private int size;
    private int page_num;
    private List<ForumCommListBean> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
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

    public List<ForumCommListBean> getList() {
        return list;
    }

    public void setList(List<ForumCommListBean> list) {
        this.list = list;
    }


}

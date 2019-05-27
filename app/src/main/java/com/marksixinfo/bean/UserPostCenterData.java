package com.marksixinfo.bean;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/3/24 0024 11:35
 * @Description:
 */
public class UserPostCenterData {


    /**
     * member : {"Id":"21","Nickname":"戒不掉你","Face":"https://images.34399.com//files/20190324/0307379085788.png","Remark":"官方网址 http://www.6631.com","Level":"1"}
     * look : ["1","0"]
     * categroy : [{"Id":"38","Name":"综合","Parent":"0","Seq":"7","User_Id":"0"},{"Id":"34","Name":"特码","Parent":"0","Seq":"6","User_Id":"0"},{"Id":"32","Name":"生肖","Parent":"0","Seq":"5","User_Id":"0"},{"Id":"36","Name":"单双","Parent":"0","Seq":"4","User_Id":"0"},{"Id":"33","Name":"波色","Parent":"0","Seq":"3","User_Id":"0"},{"Id":"35","Name":"大小","Parent":"0","Seq":"2","User_Id":"0"},{"Id":"37","Name":"尾数","Parent":"0","Seq":"1","User_Id":"0"},{"Id":"71","Name":"这么11","Parent":null,"Seq":"0","User_Id":"21"},{"Id":"73","Name":"木头","Parent":null,"Seq":"0","User_Id":"21"}]
     * is_look : 0
     */

    private UserPostCenterMember member;
    private int is_look;
    private List<Integer> look;
    private List<UserPostCenterCategory> categroy;

    public UserPostCenterMember getMember() {
        return member;
    }

    public void setMember(UserPostCenterMember member) {
        this.member = member;
    }

    public int getIs_look() {
        return is_look;
    }

    public void setIs_look(int is_look) {
        this.is_look = is_look;
    }

    public List<Integer> getLook() {
        return look;
    }

    public void setLook(List<Integer> look) {
        this.look = look;
    }

    public List<UserPostCenterCategory> getCategroy() {
        return categroy;
    }

    public void setCategroy(List<UserPostCenterCategory> categroy) {
        this.categroy = categroy;
    }
}

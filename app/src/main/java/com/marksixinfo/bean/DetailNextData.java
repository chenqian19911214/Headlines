package com.marksixinfo.bean;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/3/24 0024 23:49
 * @Description:
 */
public class DetailNextData {


    /**
     * Id : 5144
     * User_Id : 21
     * Content : 咬定目标不放松 转变作风不松懈——落实两会精神决战脱贫攻坚述评<br><img src='https://images.34399.com//files/20190323/125443.png' /><br>咬定目标不放松 转变作风不松懈——落实两会精神决战脱贫攻坚述评咬定目标不放松 转变作风不松懈——落实两会精神决战脱贫攻坚述评咬定目标不放松 转变作风不松懈——落实两会精神决战脱贫攻坚述评<img src='https://images.34399.com//files/20190323/125511.png' /><br>咬定目标不放松 转变作风不松懈——落实两会精神决战脱贫攻坚述评咬定目标不放松 转变作风不松懈——落实两会精神决战脱贫攻坚述评<br><img src='https://images.34399.com//files/20190323/125521.png' />
     * Prev : 5145
     * Next : 5143
     * <p>
     * 如果返回的prev 或者 next 为空 则表示 没有上一期或者下一期
     */

    private int Id;
    private String User_Id;
    private String Content;
    private String Prev;
    private String Next;
    private List<String> Pic;

    public List<String> getPic() {
        return Pic;
    }

    public void setPic(List<String> pic) {
        Pic = pic;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String User_Id) {
        this.User_Id = User_Id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getPrev() {
        return Prev;
    }

    public void setPrev(String Prev) {
        this.Prev = Prev;
    }

    public String getNext() {
        return Next;
    }

    public void setNext(String Next) {
        this.Next = Next;
    }
}

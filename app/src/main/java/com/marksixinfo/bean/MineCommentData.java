package com.marksixinfo.bean;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/3/26 0026 16:23
 * @Description:
 */
public class MineCommentData {


    /**
     * Id : 5708
     * Nickname : 测试2
     * Face : /static/img/face/default.jpeg
     * Title : 广东大学生打顺风车身亡 其父平台申诉账号被封：妖言惑众
     * Add_Time : 22小时前
     * Content : 近日，有媒体报道称，今年3月1日，广东大学生小王乘坐“一喂”顺风车回校途中遭遇车祸身亡。东莞交警支队太平高速公路大队认定，顺风车司机负事故主责。 交通事故处理具体内容 3月29日下午，小王的父亲王先生告诉红星新闻，事发至今，“一喂”顺风车都...
     * Pic : ["https://images.34399.com/files/20190330/0600126567154?640_1387","https://images.34399.com/files/20190330/060033465461?640_704"]
     * reply : [{"Id":"256","Add_Time":"1小时前","Content":"4565634653452135423"},{"Id":"255","Add_Time":"1小时前","Content":"1"},{"Id":"254","Add_Time":"1小时前","Content":"234523452345324523452345"},{"Id":"253","Add_Time":"1小时前","Content":"324532453452345"},{"Id":"252","Add_Time":"1小时前","Content":"2143235356345"},{"Id":"251","Add_Time":"1小时前","Content":"5675686797809870"},{"Id":"250","Add_Time":"1小时前","Content":"567967978078989"},{"Id":"249","Add_Time":"1小时前","Content":"5467568589775"},{"Id":"248","Add_Time":"1小时前","Content":"34653467356"},{"Id":"245","Add_Time":"1小时前","Content":"1234235363463"},{"Id":"244","Add_Time":"1小时前","Content":"245324523464374"},{"Id":"243","Add_Time":"1小时前","Content":"43254235423624"},{"Id":"242","Add_Time":"1小时前","Content":"3453463467"},{"Id":"241","Add_Time":"3小时前","Content":"2353464746"},{"Id":"240","Add_Time":"3小时前","Content":"65856987697"},{"Id":"239","Add_Time":"3小时前","Content":"23542352345"},{"Id":"231","Add_Time":"21小时前","Content":"84654564"},{"Id":"230","Add_Time":"21小时前","Content":"78786767867"},{"Id":"229","Add_Time":"21小时前","Content":"4563456565"}]
     */

    private String Id;
    private String Nickname;
    private String Face;
    private String Title;
    private String Add_Time;
    private String Content;
    private List<String> Pic;
    private List<ReplyBean> reply;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String Nickname) {
        this.Nickname = Nickname;
    }

    public String getFace() {
        return Face;
    }

    public void setFace(String Face) {
        this.Face = Face;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getAdd_Time() {
        return Add_Time;
    }

    public void setAdd_Time(String Add_Time) {
        this.Add_Time = Add_Time;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public List<String> getPic() {
        return Pic;
    }

    public void setPic(List<String> Pic) {
        this.Pic = Pic;
    }

    public List<ReplyBean> getReply() {
        return reply;
    }

    public void setReply(List<ReplyBean> reply) {
        this.reply = reply;
    }

    public static class ReplyBean {
        /**
         * Id : 256
         * Add_Time : 1小时前
         * Content : 4565634653452135423
         */

        private String Id;
        private String Add_Time;
        private String Content;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getAdd_Time() {
            return Add_Time;
        }

        public void setAdd_Time(String Add_Time) {
            this.Add_Time = Add_Time;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }
    }
}

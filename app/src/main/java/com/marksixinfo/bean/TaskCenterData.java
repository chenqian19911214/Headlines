package com.marksixinfo.bean;

import java.util.List;

/**
 * 任务中心
 *
 * @Auther: Administrator
 * @Date: 2019/4/20 0020 15:25
 * @Description:
 */
public class TaskCenterData {


    /**
     * info : {"Uid":"LT003796","Blance":"0.00","Gold":"0"}
     * task : [[{"Id":"3","Icon":"0","Name":"我的点赞","Note":"首次点赞达到100次,奖励10元","Bonus":"10.00","Status":0},{"Id":"6","Icon":"0","Name":"优质评论","Note":"首次评论获赞达到50次","Bonus":"5.00","Status":0},{"Id":"7","Icon":"0","Name":"首次分享","Note":"首次分享给好友奖励5元","Bonus":"5.00","Status":0}],[{"Id":"1","Icon":"0","Name":"首次邀请好友","Note":"首次邀请好友,奖励32元","Bonus":"32.00","Status":0},{"Id":"2","Icon":"0","Name":"输入邀请码","Note":"新用户首次使用10天内可填写","Bonus":"1.00","Status":0}]]
     * checkin : [{"Bonus":"100","Date":"04月20日","Status":0},{"Bonus":"200","Date":"04月21日","Status":0},{"Bonus":"300","Date":"04月22日","Status":0},{"Bonus":"400","Date":"04月23日","Status":0},{"Bonus":"500","Date":"04月24日","Status":0},{"Bonus":"600","Date":"04月25日","Status":0},{"Bonus":"700","Date":"04月26日","Status":0}]
     */

    private InfoBean info;
    private List<List<TaskBean>> task;
    private List<CheckinBean> checkin;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<List<TaskBean>> getTask() {
        return task;
    }

    public void setTask(List<List<TaskBean>> task) {
        this.task = task;
    }

    public List<CheckinBean> getCheckin() {
        return checkin;
    }

    public void setCheckin(List<CheckinBean> checkin) {
        this.checkin = checkin;
    }

    public static class InfoBean {
        /**
         * Uid : LT003796
         * Blance : 0.00
         * Gold : 0
         * Withdraw_Min=20 提现最小金额
         * Exchange_Ratio=10 金币兑换比例
         */

        private String Uid;
        private String Blance;
        private String Gold;
        private int Exchange_Ratio;
        private int Withdraw_Min;

        public int getExchange_Ratio() {
            return Exchange_Ratio;
        }

        public void setExchange_Ratio(int exchange_Ratio) {
            Exchange_Ratio = exchange_Ratio;
        }

        public int getWithdraw_Min() {
            return Withdraw_Min;
        }

        public void setWithdraw_Min(int withdraw_Min) {
            Withdraw_Min = withdraw_Min;
        }

        public String getUid() {
            return Uid;
        }

        public void setUid(String Uid) {
            this.Uid = Uid;
        }

        public String getBlance() {
            return Blance;
        }

        public void setBlance(String Blance) {
            this.Blance = Blance;
        }

        public String getGold() {
            return Gold;
        }

        public void setGold(String Gold) {
            this.Gold = Gold;
        }
    }

    public static class TaskBean {
        /**
         * Id : 3
         * Icon : 0
         * Name : 我的点赞
         * Note : 首次点赞达到100次,奖励10元
         * Bonus : 10.00
         * Status : 0
         */

        private String Id;
        private int Icon;
        private String Name;
        private String Note;
        private String Bonus;
        private String Tip_Bonus;
        private int Status;
        private List<Integer> Num;
        private int Bonus_Type;// 1:余额 0:金币

        public String getTip_Bonus() {
            return Tip_Bonus;
        }

        public void setTip_Bonus(String tip_Bonus) {
            Tip_Bonus = tip_Bonus;
        }

        public int getBonus_Type() {
            return Bonus_Type;
        }

        public void setBonus_Type(int bonus_Type) {
            Bonus_Type = bonus_Type;
        }

        public List<Integer> getNum() {
            return Num;
        }

        public void setNum(List<Integer> num) {
            Num = num;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public int getIcon() {
            return Icon;
        }

        public void setIcon(int Icon) {
            this.Icon = Icon;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getNote() {
            return Note;
        }

        public void setNote(String Note) {
            this.Note = Note;
        }

        public String getBonus() {
            return Bonus;
        }

        public void setBonus(String Bonus) {
            this.Bonus = Bonus;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }
    }

    public static class CheckinBean {
        /**
         * Bonus : 100
         * Date : 04月20日
         * Status : 0
         */

        private String Bonus;
        private String Date;
        private int Status;//签到状态 1为已签
        private int Today;//是否是今天 1,是  0,否

        public int getToday() {
            return Today;
        }

        public void setToday(int today) {
            Today = today;
        }

        public String getBonus() {
            return Bonus;
        }

        public void setBonus(String Bonus) {
            this.Bonus = Bonus;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String Date) {
            this.Date = Date;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }
    }
}

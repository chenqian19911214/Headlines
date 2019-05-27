package com.marksixinfo.bean;

import java.util.List;

/**
 * 提现兑换
 *
 * @Auther: Administrator
 * @Date: 2019/4/23 0023 12:52
 * @Description:
 */
public class WithdrawDepositData {


    /**
     * Blance : 9315826.00
     * Withdraw_Min : 20
     * Channel : [{"name":"网站平台","list":[{"Id":"7","Name":"优7彩票【1.5倍】","Seq":"0"},{"Id":"8","Name":"支付宝","Seq":"0"},{"Id":"9","Name":"中国银行","Seq":"0"},{"Id":"10","Name":"交通银行","Seq":"0"},{"Id":"11","Name":"工商银行","Seq":"0"}]},{"name":"银行账户","list":[{"Id":"1","Name":"中国银行","Seq":"0"},{"Id":"2","Name":"农业银行","Seq":"0"},{"Id":"3","Name":"招商银行","Seq":"0"},{"Id":"4","Name":"工商银行","Seq":"0"},{"Id":"5","Name":"广大银行","Seq":"0"},{"Id":"6","Name":"浦发银行","Seq":"0"}]},{"name":"支付宝","list":[]}]
     */

    private String Blance;
    private int Withdraw_Min;
    private List<ChannelBean> Channel;

    public String getBlance() {
        return Blance;
    }

    public void setBlance(String Blance) {
        this.Blance = Blance;
    }

    public int getWithdraw_Min() {
        return Withdraw_Min;
    }

    public void setWithdraw_Min(int Withdraw_Min) {
        this.Withdraw_Min = Withdraw_Min;
    }

    public List<ChannelBean> getChannel() {
        return Channel;
    }

    public void setChannel(List<ChannelBean> Channel) {
        this.Channel = Channel;
    }

    public static class ChannelBean {
        /**
         * name : 网站平台
         * list : [{"Id":"7","Name":"优7彩票【1.5倍】","Seq":"0"},{"Id":"8","Name":"支付宝","Seq":"0"},{"Id":"9","Name":"中国银行","Seq":"0"},{"Id":"10","Name":"交通银行","Seq":"0"},{"Id":"11","Name":"工商银行","Seq":"0"}]
         */

        private String name;
        private List<ListBean> list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * Id : 7
             * Name : 优7彩票【1.5倍】
             * Seq : 0
             */

            private String Id;
            private String Name;
            private String Seq;
            private int type; //type  是  int  类型 [0:网站 1:银行卡 2:支付宝]

            public ListBean() {
            }

            public ListBean(String id, String name, String seq, int type) {
                Id = id;
                Name = name;
                Seq = seq;
                this.type = type;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getId() {
                return Id;
            }

            public void setId(String Id) {
                this.Id = Id;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public String getSeq() {
                return Seq;
            }

            public void setSeq(String Seq) {
                this.Seq = Seq;
            }
        }
    }
}

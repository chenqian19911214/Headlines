package com.marksixinfo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 *  一期论坛
 *
 * @Auther: Administrator
 * @Date: 2019/4/1 0001 14:32
 * @Description:
 */
public class ForumConcernListData implements Parcelable {


    /**
     * look_new : 0
     * ace : [{"Id":"4770","Title":"2019年香港六合三月份必开特码！请验证！","Nickname":"二四六开","Add_Time":"2019/03/28 21:50","Face":"/static/img/face/default.jpeg","Color":"","View":"2216","Like":"247","Reply":"23"},{"Id":"9507","Title":"038期《4肖中特》下一个百万就是你啦!","Nickname":"喜笑颜开","Add_Time":"2019/03/30 22:05","Face":"/static/img/face/default.jpeg","Color":null,"View":"2589","Like":"70","Reply":"8"},{"Id":"4784","Title":"038期:【精英看三头】资料已免费公開！","Nickname":"王者神算","Add_Time":"2019/03/30 21:56","Face":"/static/img/face/default.jpeg","Color":"","View":"2117","Like":"142","Reply":"25"},{"Id":"10928","Title":"037期十码中特","Nickname":"期期干","Add_Time":"2019/03/30 20:51","Face":"/static/img/face/default.jpeg","Color":null,"View":"544","Like":"68","Reply":"3"}]
     * type : [{"Id":"38","Name":"综合","Parent":null,"Seq":"7"},{"Id":"34","Name":"特码","Parent":"0","Seq":"6"},{"Id":"32","Name":"生肖","Parent":"0","Seq":"5"},{"Id":"36","Name":"单双","Parent":"0","Seq":"4"},{"Id":"33","Name":"波色","Parent":"0","Seq":"3"},{"Id":"35","Name":"大小","Parent":"0","Seq":"2"},{"Id":"37","Name":"尾数","Parent":"0","Seq":"1"}]
     * is_login : 1
     */

    private int look_new;
    private int is_login;
    private List<AceBean> ace;
    private List<TypeBean> type;

    public int getLook_new() {
        return look_new;
    }

    public void setLook_new(int look_new) {
        this.look_new = look_new;
    }

    public int getIs_login() {
        return is_login;
    }

    public void setIs_login(int is_login) {
        this.is_login = is_login;
    }

    public List<AceBean> getAce() {
        return ace;
    }

    public void setAce(List<AceBean> ace) {
        this.ace = ace;
    }

    public List<TypeBean> getType() {
        return type;
    }

    public void setType(List<TypeBean> type) {
        this.type = type;
    }

    public static class AceBean implements Parcelable {
        /**
         * Id : 4770
         * Title : 2019年香港六合三月份必开特码！请验证！
         * Nickname : 二四六开
         * Add_Time : 2019/03/28 21:50
         * Face : /static/img/face/default.jpeg
         * Color :
         * View : 2216
         * Like : 247
         * Reply : 23
         */

        private String Id;
        private String Title;
        private String Nickname;
        private String Add_Time;
        private String Face;
        private String Color;
        private String View;
        private int Like;
        private int Reply;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getNickname() {
            return Nickname;
        }

        public void setNickname(String Nickname) {
            this.Nickname = Nickname;
        }

        public String getAdd_Time() {
            return Add_Time;
        }

        public void setAdd_Time(String Add_Time) {
            this.Add_Time = Add_Time;
        }

        public String getFace() {
            return Face;
        }

        public void setFace(String Face) {
            this.Face = Face;
        }

        public String getColor() {
            return Color;
        }

        public void setColor(String Color) {
            this.Color = Color;
        }

        public String getView() {
            return View;
        }

        public void setView(String View) {
            this.View = View;
        }

        public int getLike() {
            return Like;
        }

        public void setLike(int Like) {
            this.Like = Like;
        }

        public int getReply() {
            return Reply;
        }

        public void setReply(int Reply) {
            this.Reply = Reply;
        }

        @Override
        public String toString() {
            return "AceBean{" +
                    "Id='" + Id + '\'' +
                    ", Title='" + Title + '\'' +
                    ", Nickname='" + Nickname + '\'' +
                    ", Add_Time='" + Add_Time + '\'' +
                    ", Face='" + Face + '\'' +
                    ", Color='" + Color + '\'' +
                    ", View='" + View + '\'' +
                    ", Like='" + Like + '\'' +
                    ", Reply='" + Reply + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.Id);
            dest.writeString(this.Title);
            dest.writeString(this.Nickname);
            dest.writeString(this.Add_Time);
            dest.writeString(this.Face);
            dest.writeString(this.Color);
            dest.writeString(this.View);
            dest.writeInt(this.Like);
            dest.writeInt(this.Reply);
        }

        public AceBean() {
        }

        protected AceBean(Parcel in) {
            this.Id = in.readString();
            this.Title = in.readString();
            this.Nickname = in.readString();
            this.Add_Time = in.readString();
            this.Face = in.readString();
            this.Color = in.readString();
            this.View = in.readString();
            this.Like = in.readInt();
            this.Reply = in.readInt();
        }

        public static final Creator<AceBean> CREATOR = new Creator<AceBean>() {
            @Override
            public AceBean createFromParcel(Parcel source) {
                return new AceBean(source);
            }

            @Override
            public AceBean[] newArray(int size) {
                return new AceBean[size];
            }
        };
    }

    public static class TypeBean implements Parcelable {
        /**
         * Id : 38
         * Name : 综合
         * Parent : null
         * Seq : 7
         */

        private String Id;
        private String Name;
        private String Seq;

        public TypeBean() {
        }

        public TypeBean(String id, String name) {
            Id = id;
            Name = name;
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

        @Override
        public String toString() {
            return "TypeBean{" +
                    "Id='" + Id + '\'' +
                    ", Name='" + Name + '\'' +
                    ", Seq='" + Seq + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.Id);
            dest.writeString(this.Name);
            dest.writeString(this.Seq);
        }

        protected TypeBean(Parcel in) {
            this.Id = in.readString();
            this.Name = in.readString();
            this.Seq = in.readString();
        }

        public static final Creator<TypeBean> CREATOR = new Creator<TypeBean>() {
            @Override
            public TypeBean createFromParcel(Parcel source) {
                return new TypeBean(source);
            }

            @Override
            public TypeBean[] newArray(int size) {
                return new TypeBean[size];
            }
        };
    }


    @Override
    public String toString() {
        return "ForumConcernListData{" +
                "look_new=" + look_new +
                ", is_login=" + is_login +
                ", ace=" + ace +
                ", type=" + type +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.look_new);
        dest.writeInt(this.is_login);
        dest.writeTypedList(this.ace);
        dest.writeTypedList(this.type);
    }

    public ForumConcernListData() {
    }

    protected ForumConcernListData(Parcel in) {
        this.look_new = in.readInt();
        this.is_login = in.readInt();
        this.ace = in.createTypedArrayList(AceBean.CREATOR);
        this.type = in.createTypedArrayList(TypeBean.CREATOR);
    }

    public static final Creator<ForumConcernListData> CREATOR = new Creator<ForumConcernListData>() {
        @Override
        public ForumConcernListData createFromParcel(Parcel source) {
            return new ForumConcernListData(source);
        }

        @Override
        public ForumConcernListData[] newArray(int size) {
            return new ForumConcernListData[size];
        }
    };
}
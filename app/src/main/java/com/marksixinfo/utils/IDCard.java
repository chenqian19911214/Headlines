package com.marksixinfo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** <p><b>身份证</b><br>
 *        1、号码的结构
  公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。<br>排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。<br>

 <p>2、地址码(前六位数）
 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。<br>

<p> 3、出生日期码（第七位至十四位）
 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。<br>

<p> 4、顺序码（第十五位至十七位）
 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配给女性。<br>

<p> 5、校验码（第十八位数）<br>
 （1）十七位数字本体码加权求和公式<br>
 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和<br>
 Ai:表示第i位置上的身份证号码数字值<br>
 Wi:表示第i位置上的加权因子<br>
 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 <br>
 （2）计算模<br>
 Y = mod(S, 11)<br>
 （3）通过模得到对应的校验码<br>
 Y: 0 1 2 3 4 5 6 7 8 9 10<br>
 校验码: 1 0 X 9 8 7 6 5 4 3 2<br>

 所以我们就可以大致写一个函数来校验是否正确了。<br>
 * */

public class IDCard {

private String errString;

public String getErrString() {
	return errString;
}
/**
 * 身份证错误信息
 * @param errString
 */
public void setErrString(String errString) {
	this.errString = errString;
}

/** *//**======================================================================
        * 功能：身份证的有效验证
        * @param IDStr 身份证号
        * @return 有效：true        无效：false
        * @throws ParseException 
        */
public boolean IDCardValidate(String IDStr) throws ParseException
{
        String[] ValCodeArr = {"1","0","X","9","8","7","6","5","4","3","2"};
        //钱17位每位对应的权值
        String[] Wi = {"7","9","10","5","8","4","2","1","6","3","7","9","10","5","8","4","2"};
        //String[] Checker = {"1","9","8","7","6","5","4","3","2","1","1"};
        String Ai="";
  
        //================ 号码的长度 15位或18位 ================
        if(IDStr.length()!=15 && IDStr.length()!=18)
        {
	         errString="身份证号码长度应该为15位或18位。";
	         System.out.println(errString);
	         return false;
        }
        //=======================(end)======================== 
  
  
        //================ 数字 除最后以为都为数字 ================
        if(IDStr.length()==18)
        {
        	Ai=IDStr.substring(0,17);
        }
        else if(IDStr.length()==15)
        {
        	Ai=IDStr.substring(0,6)+"19"+IDStr.substring(6,15);   
        }
        if(isNumeric(Ai)==false)
        {
	         errString="身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
	         System.out.println(errString);
	         return false;
        }
        //=======================(end)========================
  
  
        //================ 出生年月是否有效 ================
        String strYear  =Ai.substring(6 ,10);//年份
        String strMonth =Ai.substring(10,12);//月份
        String strDay   =Ai.substring(12,14);//月份
  
        if(this.isDate(strYear+"-"+strMonth+"-"+strDay)==false)
        {
	         errString="身份证生日段无效。";
	         System.out.println(errString);
	         return false;
        }
  
        GregorianCalendar gc=new GregorianCalendar();  
        SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
        if((gc.get(Calendar.YEAR)-Integer.parseInt(strYear))>150 || (gc.getTime().getTime()-s.parse(strYear+"-"+strMonth+"-"+strDay).getTime())<0)
        {
	         errString="身份证生日段不在有效范围。";
	         System.out.println(errString);
	         return false;
        }
        if(Integer.parseInt(strMonth)>12 || Integer.parseInt(strMonth)==0)
        {
	         errString="身份证月份段无效";
	         System.out.println(errString);
	         return false;
        }
        if(Integer.parseInt(strDay)>31 || Integer.parseInt(strDay)==0)
        {
	         errString="身份证日期段无效";
	         System.out.println(errString);
	         return false;
        }
        //=====================(end)=====================
  
  
        //================ 地区码时候有效 ================
        Hashtable<String,String > h=GetAreaCode();
        if(h.get(Ai.substring(0,2))==null)
        {
	         errString="身份证地区编码错误。";
	         System.out.println(errString);
	         return false;
        }
        //==============================================
  
  
        //================ 判断最后一位的值 ================
        int TotalmulAiWi=0;
        for(int i=0 ; i<17 ; i++)
        {
        	TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue=TotalmulAiWi % 11;
        String strVerifyCode=ValCodeArr[modValue];
        Ai=Ai+strVerifyCode;
  
        if(IDStr.length()==18)
        {   
	         if(Ai.equals(IDStr)==false)
	         {
		          errString="身份证无效，最后一位字母错误";
		          System.out.println(errString);
		          return false;
	         }
        }
        else
        { 
	         System.out.println("所在地区:"+h.get(Ai.substring(0,2)));
	         System.out.println("新身份证号:"+Ai);
	         return true;
        }
        //=====================(end)=====================
        System.out.println("所在地区:"+h.get(Ai.substring(0,2)));
        return true;
}
//**======================================================================
/** *
        * 功能：设置地区编码
        * @return Hashtable 对象
        */
	@SuppressWarnings("unchecked")
	private Hashtable<String,String> GetAreaCode()
	{
	        Hashtable hashtable=new Hashtable<>();
	        hashtable.put("11","北京");
	        hashtable.put("12","天津");
	        hashtable.put("13","河北");
	        hashtable.put("14","山西");
	        hashtable.put("15","内蒙古");
	        hashtable.put("21","辽宁");
	        hashtable.put("22","吉林");
	        hashtable.put("23","黑龙江");
	        hashtable.put("31","上海");
	        hashtable.put("32","江苏");
	        hashtable.put("33","浙江");
	        hashtable.put("34","安徽");
	        hashtable.put("35","福建");
	        hashtable.put("36","江西");
	        hashtable.put("37","山东");
	        hashtable.put("41","河南");
	        hashtable.put("42","湖北");
	        hashtable.put("43","湖南");
	        hashtable.put("44","广东");
	        hashtable.put("45","广西");
	        hashtable.put("46","海南");
	        hashtable.put("50","重庆");
	        hashtable.put("51","四川");
	        hashtable.put("52","贵州");
	        hashtable.put("53","云南");
	        hashtable.put("54","西藏");
	        hashtable.put("61","陕西");
	        hashtable.put("62","甘肃");
	        hashtable.put("63","青海");
	        hashtable.put("64","宁夏");
	        hashtable.put("65","新疆");
	        hashtable.put("71","台湾");
	        hashtable.put("81","香港");
	        hashtable.put("82","澳门");
	        hashtable.put("91","国外");
	        return hashtable;
	}

	//**======================================================================
/** *
        * 功能：判断字符串是否为数字
        * @param str
        * @return
        */
	private boolean isNumeric(String str)
	{
	        Pattern pattern=Pattern.compile("[0-9]*");
	        Matcher isNum=pattern.matcher(str);
		return isNum.matches();
	}

//**======================================================================
/** *
        * 功能：判断字符串是否为日期格式
        * @param strDate
        * @return
        */
	public boolean isDate(String strDate)
	{
		
		Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|" +
				"([13579][26]))[\\-\\s]?((((0?[13578])|" +
				"(1[02]))[\\-\\s]?((0?[1-9])|" +
				"([1-2][0-9])|(3[01])))|" +
				"(((0?[469])|" +
				"(11))[\\-\\s]?((0?[1-9])|" +
				"([1-2][0-9])|" +
				"(30)))|(0?2[\\-\\s]?((0?[1-9])|" +
				"([1-2][0-9])))))|" +
				"(\\d{2}(([02468][1235679])|" +
				"([13579][01345789]))[\\-\\s]?((((0?[13578])|" +
				"(1[02]))[\\-\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|" +
				"(11))[\\-\\s]?((0?[1-9])|([1-2][0-9])|" +
				"(30)))|(0?2[\\-\\s]?((0?[1-9])|(1[0-9])|" +
				"(2[0-8]))))))(\\s(((0?[0-9])|" +
				"([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|" +
				"(\\:([0-5]?[0-9])))))?$");
	
	        
	        Matcher m=pattern.matcher(strDate);
		return m.matches();
	}

/**
        *功能:在判定已经是正确的身份证号码之后,查找出此人性别
        *@param idCard 身份证号码
        *@return 男或者女
        */
	public String GetSex(String idCard){
		    String sex = "";
		    if(idCard.length()==15)
		     sex = idCard.substring(idCard.length()-3,idCard.length());
		    
		    if(idCard.length()==18)
		     sex = idCard.substring(idCard.length()-4,idCard.length()-1);
		    
		        System.out.println(sex);
		    int sexNum = Integer.parseInt(sex)%2;
		    if(sexNum == 0){
		        return "女";
		    }
		    return "男";
	}
	//*=======================================================================

/**
        *功能:在判定已经是正确的身份证号码之后,查找出此人出生日期
        *@param idCard 身份证号码
        *@return 出生日期 XXXX MM-DD
        */

	public String GetBirthday(String idCard){
	    String Ain = "";
	    if(idCard.length()==18) {
	            Ain=idCard.substring(0,17);
	        } else if(idCard.length()==15) {
	            Ain=idCard.substring(0,6)+"19"+idCard.substring(6,15);
	        }
	        
	        //================ 出生年月是否有效 ================
	        String strYear =Ain.substring(6 ,10);//年份
	        String strMonth=Ain.substring(10,12);//月份
	        String strDay        =Ain.substring(12,14);//日期
	        return strYear+"-"+strMonth+"-"+strDay;
	}
	/**
	 *  判断用户身份证是否正确 ，正确返回“”错误返回错误信息
	 * @param IDStr
	 * @return
	 */
	public static String isEnble(String IDStr)
	{
		if(!CommonUtils.StringNotNull(IDStr))
			return "身份证不能为空";
		IDStr=IDStr.replace("x", "X");
		IDCard i=new IDCard();
		try {
			if (i.IDCardValidate(IDStr)) {
				return "";
			}
		} catch (ParseException e) {
			e.printStackTrace();
			i.setErrString("未知错误");
		}
		return i.getErrString();
	}
/** *//**
        * @param args
        * @throws ParseException 
        */
public static void main(String[] args) throws ParseException {
        String IDCardNum="420704199205154250";
        //String IDCardNum="210102198208264114";
        //String IDCardNum="210103970228123";
       // String IDCardNum="421122198709137899";
        
        //String IDCardNum="42092119860725576x";
        IDCard cc=new IDCard();
        System.out.println(cc.IDCardValidate(IDCardNum));
        System.out.println(cc.GetBirthday(IDCardNum));
        System.out.println(cc.GetSex(IDCardNum));
        //System.out.println(cc.isDate("1996-02-29"));
}

}


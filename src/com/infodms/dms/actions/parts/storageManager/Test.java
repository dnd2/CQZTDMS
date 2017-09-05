package com.infodms.dms.actions.parts.storageManager;


import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class Test
{
//  static Logger logger = Logger.getLogger(ExportToTxt.class.getName());

  public static void main(String[] args)
  {
	  String inputNum = "112s00.00";
	  String regex = "((^[0]([.]{1}(\\d)*)?$)|(^[1-9]+(\\d)*([.]{1}(\\d)*)?$))";
	  
	  Pattern pattern = Pattern.compile(regex);
	  
	  Matcher matcher = pattern.matcher(inputNum);
	  
	  if(matcher.find())
	  {
		  System.out.println("OK");
	  }
	  else
	  {
		  System.out.println("Fail");
	  }
    /*String sdate = "20110601";
//    sdate.replaceAll("-", "");
//    System.out.println("KKK  sdate: " + sdate);
//    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat odf = new SimpleDateFormat("yyyy-MM-dd");
//    String ss = sdf.format(sdate);
//    System.out.println(ss);
    try {
		Date date = sdf.parse(sdate);
		System.out.println(" AAA;: " + odf.format(date));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
*/
    /*String[] s = { 
      "1 组合仪表总成（电子式，南京）~~件~~ ~~2~~284.63~~0.17~~48.39", 
      "2 组合仪表总成（6378D，增配，南京）~~件~~ ~~1~~171.68~~0.17~~29.19", 
      "3 后轮眉总成(左.6378.南京)~~件~~ ~~10~~715.34~~0.17~~121.61", 
      "4 中门内饰板总成(右)(浅色)~~件~~ ~~1~~60.24~~0.17~~10.24", 
      "5 NO.1电线束总成（电子组合仪表用，南京）~~件~~ ~~5~~1242.44~~0.17~~211.21", 
      "6 前雾灯开关总成~~件~~ ~~300~~1468.33~~0.17~~249.62", 
      "7 前置蒸发器流出管总成(英特尔)~~件~~ ~~1~~21.08~~0.17~~3.59", 
      "8 No.1电线束总成（电子式组合仪表用，伟世通6363AJ3，南京）~~件~~ ~~1~~237.19~~0.17~~40.32", 
      "9 NO.2电线束总成(6363B4.联电电喷.南京)~~件~~ ~~1~~306.47~~0.17~~52.1" };
    long[] l = { 
      1222L, 22222L, 3L, 4L, 5L, 6L };

    String TTT = Arrays.toString(l);
    String file = "d:\\T.txt";
    Arrays.toString(l);
    System.out.println("TTT===" + TTT);

    PrintWriter pw = null;
    StringBuffer sb = new StringBuffer();
    try {
      pw = new PrintWriter(file, "gbk");
      for (int i = 0; i < s.length; ++i) {
        sb.append(s[i].toString());
        sb.append("\r\n");
      }
      pw.println(sb.substring(0, sb.length() - 1));
      pw.flush();
      pw.close();
    } catch (Exception e) {
      e.printStackTrace();
    }*/
  }
}
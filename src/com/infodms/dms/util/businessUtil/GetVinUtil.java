package com.infodms.dms.util.businessUtil;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GetVinUtil {

	/* 入参：
	 * 1.vin:要查询的vin
	 * 2.alias：TM_VEHICLE表的别名
	 * */
	public static String getVins(String vin,String alias){
		String returnSql = "";
		if (null != vin && !"".equals(vin)) {
			vin = vin.trim();
			//IN查询
			StringBuffer inbuffer = new StringBuffer();
			inbuffer.append("   AND ").append(alias).append(".VIN");
			//LIKE查询
			StringBuffer likeBuffer = new StringBuffer();
			likeBuffer.append("   AND (");
			//对 VIN进行拆分
			String[] vins = vin.split("\\r\\n");
			//得到VIN长度：strLength  非17位   ：LIKE查询；17位：IN查询
			int strLength = vins[0].length();
			//对VIN进行重复数据过滤
			Set<String> set = new HashSet<String>();
			for (int j = 0; j < vins.length; j++) {
				set.add(vins[j]);
			}
			//对VIN进行组合
			StringBuffer buffer = new StringBuffer();
			Iterator<String> i = set.iterator();
			while (i.hasNext()) {
				String s = i.next();
				if (strLength == 17) {//如果是IN查询
					buffer.append("'").append(s).append("'").append(",");
				}else {//如果是LIKE查询
					buffer.append(alias).append(".VIN LIKE ").append("'").append("%").append(s).append("%").append("'").append(" OR ");
				}
			}
			//将VIN封装成SQL
			//IN查询
			if (strLength == 17) {
				buffer=buffer.deleteCharAt(buffer.length()-1);
				returnSql = inbuffer.append(" IN (").append(buffer).append(")\n").toString();
			}else{
				buffer=buffer.delete(buffer.length()-3, buffer.length());
				likeBuffer.append(buffer).append(")\n");
				returnSql = likeBuffer.toString();
			}
		}
		return returnSql;
	}
	
	public static String getVinsNew(String vin,String alias){
		String returnSql = "";
		if (null != vin && !"".equals(vin)) {
			vin = vin.trim();
			//IN查询
			StringBuffer inbuffer = new StringBuffer();
			inbuffer.append("   AND ").append(alias).append(".OLD_VIN");
			//LIKE查询
			StringBuffer likeBuffer = new StringBuffer();
			likeBuffer.append("   AND (");
			//对 VIN进行拆分
			String[] vins = vin.split("\\r\\n");
			//得到VIN长度：strLength  非17位   ：LIKE查询；17位：IN查询
			int strLength = vins[0].length();
			//对VIN进行重复数据过滤
			Set<String> set = new HashSet<String>();
			for (int j = 0; j < vins.length; j++) {
				set.add(vins[j]);
			}
			//对VIN进行组合
			StringBuffer buffer = new StringBuffer();
			Iterator<String> i = set.iterator();
			while (i.hasNext()) {
				String s = i.next();
				if (strLength == 17) {//如果是IN查询
					buffer.append("'").append(s).append("'").append(",");
				}else {//如果是LIKE查询
					buffer.append(alias).append(".OLD_VIN LIKE ").append("'").append("%").append(s).append("%").append("'").append(" OR ");
				}
			}
			//将VIN封装成SQL
			//IN查询
			if (strLength == 17) {
				buffer=buffer.deleteCharAt(buffer.length()-1);
				returnSql = inbuffer.append(" IN (").append(buffer).append(")\n").toString();
			}else{
				buffer=buffer.delete(buffer.length()-3, buffer.length());
				likeBuffer.append(buffer).append(")\n");
				returnSql = likeBuffer.toString();
			}
		}
		return returnSql;
	}
	
	
	
	
	/* 入参：
	 * 1.vin:要查询的vin
	 * 2.alias：TM_VEHICLE表的别名
	 * */
	public static String getVinEnds(String vin,String alias){
		String returnSql = "";
		if (null != vin && !"".equals(vin)) {
			vin = vin.trim();
			//IN查询
			StringBuffer inbuffer = new StringBuffer();
			inbuffer.append("   AND ").append(alias).append(".VIN");
			//LIKE查询
			StringBuffer likeBuffer = new StringBuffer();
			likeBuffer.append("   AND (");
			//对 VIN进行拆分
			String[] vins = vin.split("\\r\\n");
			//得到VIN长度：strLength  非17位   ：LIKE查询；17位：IN查询
			int strLength = vins[0].length();
			//对VIN进行重复数据过滤
			Set<String> set = new HashSet<String>();
			for (int j = 0; j < vins.length; j++) {
				set.add(vins[j]);
			}
			//对VIN进行组合
			StringBuffer buffer = new StringBuffer();
			Iterator<String> i = set.iterator();
			while (i.hasNext()) {
				String s = i.next();
				if (strLength == 17) {//如果是IN查询
					buffer.append("'").append(s).append("'").append(",");
				}else {//如果是LIKE查询
					buffer.append(alias).append(".VIN LIKE ").append("'").append(s).append("%").append("'").append(" OR ");
				}
			}
			//将VIN封装成SQL
			//IN查询
			if (strLength == 17) {
				buffer=buffer.deleteCharAt(buffer.length()-1);
				returnSql = inbuffer.append(" IN (").append(buffer).append(")\n").toString();
			}else{
				buffer=buffer.delete(buffer.length()-3, buffer.length());
				likeBuffer.append(buffer).append(")\n");
				returnSql = likeBuffer.toString();
			}
		}
		return returnSql;
	}
	
	/* 入参：
	 * 1.vin:要查询的vin
	 * 2.alias：TM_VEHICLE表的别名
	 * */
	public static String getMyVins(String vin,String alias){
		String returnSql = "";
		if (null != vin && !"".equals(vin)) {
			vin = vin.trim();
			//IN查询
			StringBuffer inbuffer = new StringBuffer();
			inbuffer.append("   AND ").append(alias).append(".VIN");
			//LIKE查询
			StringBuffer likeBuffer = new StringBuffer();
			likeBuffer.append("   AND (");
			//对 VIN进行拆分
			String[] vins = vin.split("\\r\\n");
			//得到VIN长度：strLength  非17位   ：LIKE查询；17位：IN查询
			int strLength = vins[0].length();
			//对VIN进行重复数据过滤
			Set<String> set = new HashSet<String>();
			for (int j = 0; j < vins.length; j++) {
				set.add(vins[j]);
			}
			//对VIN进行组合
			StringBuffer buffer = new StringBuffer();
			Iterator<String> i = set.iterator();
			while (i.hasNext()) {
				String s = i.next();
				if (strLength == 17) {//如果是IN查询
					buffer.append("'").append(s).append("'").append(",");
				}else {//如果是LIKE查询
					buffer.append(alias).append(".VIN = ").append("'").append("%").append(s).append("%").append("'").append(" OR ");
				}
			}
			//将VIN封装成SQL
			//IN查询
			if (strLength == 17) {
				buffer=buffer.deleteCharAt(buffer.length()-1);
				returnSql = inbuffer.append(" IN (").append(buffer).append(")\n").toString();
			}else{
				buffer=buffer.delete(buffer.length()-3, buffer.length());
				likeBuffer.append(buffer).append(")\n");
				returnSql = likeBuffer.toString();
			}
		}
		return returnSql;
	}
	
	
}

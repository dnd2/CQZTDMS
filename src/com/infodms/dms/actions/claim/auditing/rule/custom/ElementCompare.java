package com.infodms.dms.actions.claim.auditing.rule.custom;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/**
 * 按指定操作符比较两个值
 * @author XZM
 */
public class ElementCompare {

	private Logger logger = Logger.getLogger(ElementCompare.class);
	/** 算术运算符 */
	private final String operCheck = "#=#<#<=#>#>=#";
	/** 逻辑运算符 */
	private final String logicOperCheck = "#BEGIN#NOTBEGIN#EQUAL#NOTEQUAL#";
	
	/**
	 * 对两个布尔值进行逻辑运算
	 * @param A
	 * @param comOP 操作值（现在：AND || OR）
	 * @param B
	 * @return boolean
	 */
	public boolean booleanCompare(boolean A, String comOP, boolean B) {
		
		if (null == comOP || "".equals(comOP))
			return B;
		
		comOP = comOP.trim();
		
		if ("AND".equalsIgnoreCase(comOP))
			return (A && B);
		else if("OR".equalsIgnoreCase(comOP))
			return (A || B);
		else 
			return B;
	}

	/**
	 * 比较两个值的范围
	 * 情况：
	 *     1、当比较符为空时 返回不满足条件
	 *     2、当授权项为空时 返回不满足条件
	 *     3、当目标值为空时 返回不满足条件
	 * 支持授权类型：
	 *     1、String,Double,Integer,Long
	 *     2、List<String>,List<Double>,List<Integer>,List<Long>
	 * @param eleA 授权项对应值
	 * @param comOP 比较符
	 * @param eleB 目标值（当为数字比较时为一个值，为字符比较时可以为多个值，以","分隔）
	 * @return boolean false ：不满足条件 true :满足条件
	 */
	public boolean elementCompare(Object eleA, String comOP, Object eleB) {
		boolean rs = false;
		
		if(comOP==null || eleA==null || eleB==null){
			logger.error("比较符、比较的授权项或目标值为空");
			rs = false;
		}else{ 
			if(eleA instanceof List){
				List<?> result = (List<?>) eleA;
				for (int i = 0; i < result.size(); i++) {
					String eleAValue = result.get(i).toString();
					boolean flag = this.subCompare(eleAValue, comOP, eleB);
					if(flag){//当存在某一条记录满足对应条件时，返回true
						rs = flag;
						break;
					}
				}
			}else{
				rs = this.subCompare(eleA, comOP, eleB);
			}
		}

		return rs;
	}
	
	/**
	 * 比较单个数据
	 * @param eleA 授权项对应值
	 * @param comOP 比较符
	 * @param eleB 目标值（当为数字比较时为一个值，为字符比较时可以为多个值，以","分隔）
	 * @return boolean false ：不满足条件 true :满足条件
	 */
	private boolean subCompare(Object eleA, String comOP, Object eleB){
		boolean res = false;
		if(this.operCheck.indexOf(comOP.toUpperCase())>-1){//算术运算符比较
			res = this.valueCompare(eleA.toString(), comOP, eleB.toString());
		}else if(this.logicOperCheck.indexOf(comOP.toUpperCase())>-1){//逻辑运算符比较
			res = this.stringCompare(eleA.toString(), comOP, eleB.toString());
		}else{
			logger.error("系统不支持操作符[" + comOP + "]，支持类型[" + this.operCheck + "]！");
		}
		return res;
	}
	
	public static void main(String[] args) {
		String test = ">=";
		ElementCompare ec = new ElementCompare();
		if(ec.operCheck.indexOf(test)>-1){
			System.out.println("H");
		}
	}

	/**
	 * 比较两个值的范围
	 * @param eleA 授权项对应值
	 * @param comOP 比较符
	 * @param eleB 目标值（当为数字比较时为一个值，为字符比较时可以为多个值，以","分隔）
	 * @return boolean false :不满足条件 true :满足条件
	 */
	public boolean stringCompare(String eleA, String comOP, String eleB) {
		boolean rs = false;
		comOP = comOP.trim();

		// 把eleB中的多个值分解出来
		StringTokenizer st = new StringTokenizer(eleB, ",");
		String[] objs = new String[st.countTokens()];
		for (int i = 0; i < objs.length; i++) {
			objs[i] = st.nextToken();
			if(objs[i]!=null)
				objs[i] = objs[i].trim();
		}

		// 设定字符型的比较符为两种"Begin"和"Equal","notBegin"和"notEqual"
		if ("Begin".equalsIgnoreCase(comOP)) {
			for (int i = 0; i < objs.length; i++)
				if (eleA.toUpperCase().startsWith(objs[i].toUpperCase())) {
					rs = true;
					break;
				}
		} else if ("Equal".equalsIgnoreCase(comOP)) {
			for (int i = 0; i < objs.length; i++)
				if (eleA.equalsIgnoreCase(objs[i])) {
					rs = true;
					break;
				}
		} else if ("notBegin".equalsIgnoreCase(comOP)) {
			for (int i = 0; i < objs.length; i++)
				if (!eleA.toUpperCase().startsWith(objs[i].toUpperCase())) {
					rs = true;
					break;
				}
		} else if ("notEqual".equalsIgnoreCase(comOP)) {
			for (int i = 0; i < objs.length; i++)
				if (!eleA.equalsIgnoreCase(objs[i])) {
					rs = true;
					break;
				}
		} 
		
		return rs;
	}
	
	/**
	 * 设定数值型的比较符为五种"=","<","<=",">"和">="， 数据四舍五入到小数点第二位
	 * @param obj1
	 * @param comOP
	 * @param obj2
	 * @param rs
	 * @return boolean true : 满足检测条件 false :不满足检测条件
	 */
	private boolean valueCompare(String obj1, String comOP, String obj2) {
		double int1 = 0;
		double int2 = 0;
		boolean rs = false;
		try {
			
			if(obj1==null || obj2==null || "".equals(obj1) || "".equals(obj2))//当需要比较值为空时返回错误
				return false;
			int1 = Double.parseDouble(obj1);
			int2 = Double.parseDouble(obj2);

		} catch (NumberFormatException numberFormatException) {
			return false;
		}

		if ("=".equals(comOP)) {
			if (int1 == int2)
				rs = true;
		} else if ("<".equals(comOP)) {
			if (int1 < int2)
				rs = true;
		} else if ("<=".equals(comOP)) {
			if (int1 <= int2)
				rs = true;
		} else if (">".equals(comOP)) {
			if (int1 > int2)
				rs = true;
		} else if (">=".equals(comOP)) {
			if (int1 >= int2)
				rs = true;
		} else if ("<>".equals(comOP)) {
			if (int1 != int2)
				rs = true;
		}

		return rs;
	}
}

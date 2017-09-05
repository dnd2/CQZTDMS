package com.infodms.dms.dao.orgmng;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;

/**
 * @Title: CHANADMS
 *
 * @Description:
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company:  www.infoservice.com.cn
 * @Date: 2010-6-10
 *
 * @author zjy 
 * @mail   zhaojinyu@infoservice.com.cn
 * @version 1.0
 * @remark 
 */
public class GetOrgIdsOrDealerIdsDAO {
	public static Logger logger = Logger.getLogger(GetOrgIdsOrDealerIdsDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	/**
	 * Function : 根据登录的组织ID查询出下属的所有经销商
	 * @param :  orgId 登录人的组组织ID
	 * @param :  parentOrgId 登录人的上级组织ID
	 * @param :  dutyType 组织类型（Constant中的组织层级）
	 * @return : 满足条件经销商ID集合。
	 */
	public static String getDealerIds(String orgId,String parentOrgId,String dutyType) 
	{
		if("".equals(dutyType)||dutyType==null)
			return "";
		StringBuffer sql=new StringBuffer();
		if(String.valueOf(Constant.DUTY_TYPE_COMPANY).equals(dutyType)
				||String.valueOf(Constant.DUTY_TYPE_LARGEREGION).equals(dutyType)
				||String.valueOf(Constant.DUTY_TYPE_SMALLREGION).equals(dutyType))//登录为车厂公司,大区，小区时
		{
			sql.append("SELECT  TD.DEALER_ID\n");
			sql.append("  FROM TM_DEALER TD\n");  
			sql.append(" WHERE TD.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("AND TD.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+")\n");
			sql.append(" START WITH DEALER_ID IN\n");  
			sql.append("            (SELECT TDOR.DEALER_ID\n");  
			sql.append("               FROM TM_DEALER_ORG_RELATION TDOR\n");  
			sql.append("              WHERE TDOR.ORG_ID IN\n");  
			sql.append("                    (SELECT ORG_ID\n");  
			sql.append("                       FROM TM_ORG TMO\n");  
			sql.append("                      WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                      START WITH TMO.ORG_ID = "+orgId+"\n");  
			sql.append("                     CONNECT BY PRIOR TMO.ORG_ID = TMO.PARENT_ORG_ID))\n");  
			sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D\n");

		}else if(String.valueOf(Constant.DUTY_TYPE_DEPT).equals(dutyType))//部门
		{
			sql.append("SELECT  TD.DEALER_ID\n");
			sql.append("  FROM TM_DEALER TD\n");  
			sql.append(" WHERE TD.STATUS = "+Constant.STATUS_ENABLE+"\n"); 
			sql.append("AND TD.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+")\n");
			sql.append(" START WITH DEALER_ID IN\n");  
			sql.append("            (SELECT TDOR.DEALER_ID\n");  
			sql.append("               FROM TM_DEALER_ORG_RELATION TDOR\n");  
			sql.append("              WHERE TDOR.ORG_ID IN\n");  
			sql.append("                    (SELECT ORG_ID\n");  
			sql.append("                       FROM TM_ORG TMO\n");  
			sql.append("                      WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                      START WITH TMO.ORG_ID = "+parentOrgId+"\n");  
			sql.append("                     CONNECT BY PRIOR TMO.ORG_ID = TMO.PARENT_ORG_ID))\n");  
			sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D\n");
		}else if(String.valueOf(Constant.DUTY_TYPE_DEALER).equals(dutyType))//经销商,可能不起作用，这里只是考虑这种情况出现时的处理办法。
		{
			sql.append("SELECT TD.DEALER_ID\n");
			sql.append("  FROM TM_DEALER TD\n");  
			sql.append(" WHERE TD.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append(" START WITH TD.DEALER_ORG_ID = "+orgId+"\n");  
			sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D\n");

		}else//开放其他类型代码，如有其它情况在这里维护代码。
		{
			
		}
		return sql.toString();
	}
	
	public static String getDealerIdsStr(String orgId,String parentOrgId,String dutyType, String tableName) 
	{
		if("".equals(dutyType)||dutyType==null)
			return "";
		StringBuffer sql=new StringBuffer();
		if(String.valueOf(Constant.DUTY_TYPE_COMPANY).equals(dutyType)
				||String.valueOf(Constant.DUTY_TYPE_LARGEREGION).equals(dutyType)
				||String.valueOf(Constant.DUTY_TYPE_SMALLREGION).equals(dutyType))//登录为车厂公司,大区，小区时
		{
			sql.append("SELECT  TD.DEALER_ID\n");
			sql.append("  FROM TM_DEALER TD\n");  
			sql.append(" WHERE TD.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append(" and td.dealer_id = ").append(tableName).append(".dealer_id\n");  
			//sql.append("AND TD.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+")\n");
			sql.append(" START WITH DEALER_ID IN\n");  
			sql.append("            (SELECT TDOR.DEALER_ID\n");  
			sql.append("               FROM TM_DEALER_ORG_RELATION TDOR\n");  
			sql.append("              WHERE TDOR.ORG_ID IN\n");  
			sql.append("                    (SELECT ORG_ID\n");  
			sql.append("                       FROM TM_ORG TMO\n");  
			sql.append("                      WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                      START WITH TMO.ORG_ID = "+orgId+"\n");  
			sql.append("                     CONNECT BY PRIOR TMO.ORG_ID = TMO.PARENT_ORG_ID))\n");  
			sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D\n");

		}else if(String.valueOf(Constant.DUTY_TYPE_DEPT).equals(dutyType))//部门
		{
			sql.append("SELECT  TD.DEALER_ID\n");
			sql.append("  FROM TM_DEALER TD\n");  
			sql.append(" WHERE TD.STATUS = "+Constant.STATUS_ENABLE+"\n"); 
			sql.append(" and td.dealer_id = ").append(tableName).append(".dealer_id\n");  
			//sql.append("AND TD.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+")\n");
			sql.append(" START WITH DEALER_ID IN\n");  
			sql.append("            (SELECT TDOR.DEALER_ID\n");  
			sql.append("               FROM TM_DEALER_ORG_RELATION TDOR\n");  
			sql.append("              WHERE TDOR.ORG_ID IN\n");  
			sql.append("                    (SELECT ORG_ID\n");  
			sql.append("                       FROM TM_ORG TMO\n");  
			sql.append("                      WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                      START WITH TMO.ORG_ID = "+parentOrgId+"\n");  
			sql.append("                     CONNECT BY PRIOR TMO.ORG_ID = TMO.PARENT_ORG_ID))\n");  
			sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D\n");
		}else if(String.valueOf(Constant.DUTY_TYPE_DEALER).equals(dutyType))//经销商,可能不起作用，这里只是考虑这种情况出现时的处理办法。
		{
			sql.append("SELECT TD.DEALER_ID\n");
			sql.append("  FROM TM_DEALER TD\n");  
			sql.append(" WHERE TD.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append(" and td.dealer_id = ").append(tableName).append(".dealer_id\n");  
			sql.append(" START WITH TD.DEALER_ORG_ID = "+orgId+"\n");  
			sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D\n");

		}else//开放其他类型代码，如有其它情况在这里维护代码。
		{
			
		}
		return sql.toString();
	}
	
	/**
	 * Function : 根据登录的组织ID查询出下属的所有组织不包括本级组织
	 * @param :  orgId 登录人的组组织ID
	 * @param :  parentOrgId 登录人的上级组织ID
	 * @param :  dutyType 组织类型（Constant中的组织层级）
	 * @return : 满足条件组织ID集合。
	 */
	public static String getOrgIds(String orgId,String parentOrgId,String dutyType)  
	{
		if("".equals(dutyType)||dutyType==null)
			return "";
		StringBuffer sql=new StringBuffer();
		if(String.valueOf(Constant.DUTY_TYPE_COMPANY).equals(dutyType)
				||String.valueOf(Constant.DUTY_TYPE_LARGEREGION).equals(dutyType)
				||String.valueOf(Constant.DUTY_TYPE_SMALLREGION).equals(dutyType))//登录为车厂公司,大区，小区时
		{
			sql.append("SELECT IDS.ORG_ID\n");
			sql.append("  FROM (SELECT TMO.ORG_ID\n");  
			sql.append("          FROM TM_ORG TMO\n");  
			sql.append("         WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("         START WITH TMO.ORG_ID = "+orgId+"\n");  
			sql.append("        CONNECT BY PRIOR TMO.ORG_ID = TMO.PARENT_ORG_ID) IDS\n");  
			sql.append(" WHERE IDS.ORG_ID <> "+orgId+"\n");

		}else if(String.valueOf(Constant.DUTY_TYPE_DEPT).equals(dutyType))//部门
		{
			sql.append("SELECT IDS.ORG_ID\n");
			sql.append("  FROM (SELECT TMO.ORG_ID\n");  
			sql.append("          FROM TM_ORG TMO\n");  
			sql.append("         WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("         START WITH TMO.ORG_ID = "+parentOrgId+"\n");  
			sql.append("        CONNECT BY PRIOR TMO.ORG_ID = TMO.PARENT_ORG_ID) IDS\n");  
			sql.append(" WHERE IDS.ORG_ID <> "+parentOrgId+"\n");
		}else//开放其他类型代码，如有其它情况在这里维护代码。
		{
			
		}
		return sql.toString();
	}
	/**
	 * Function : 根据登录的组织ID查询出下属的所有经销商
	 * @param :  orgId 登录人的组组织ID
	 * @param :  parentOrgId 登录人的上级组织ID
	 * @param :  dutyType 组织类型（Constant中的组织层级）
	 * @param :  tableName 经销商表别名
	 * @return : 满足条件经销商ID集合。
	 */
	public static String getDealerIdsForOrder(String orgId,String parentOrgId,String dutyType,String tableName) 
	{
		if("".equals(dutyType)||dutyType==null)
			return "";
		if("".equals(tableName)||tableName==null)
		{
			tableName="TM_DEALER";
		}else
		{
			tableName=tableName.trim();
		}
			
		StringBuffer sql=new StringBuffer();
		if(String.valueOf(Constant.DUTY_TYPE_LARGEREGION).equals(dutyType)
		 ||String.valueOf(Constant.DUTY_TYPE_SMALLREGION).equals(dutyType))//大区，小区时
		{
			sql.append(tableName+".DEALER_ID IN(\n");
			sql.append("SELECT  TD.DEALER_ID\n");
			sql.append("  FROM TM_DEALER TD\n");  
			sql.append(" WHERE TD.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("AND TD.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+","+Constant.DEALER_TYPE_JSZX+")\n");
			sql.append(" START WITH DEALER_ID IN\n");  
			sql.append("            (SELECT TDOR.DEALER_ID\n");  
			sql.append("               FROM TM_DEALER_ORG_RELATION TDOR\n");  
			sql.append("              WHERE TDOR.ORG_ID IN\n");  
			sql.append("                    (SELECT ORG_ID\n");  
			sql.append("                       FROM TM_ORG TMO\n");  
			sql.append("                      WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                      START WITH TMO.ORG_ID = "+orgId+"\n");  
			sql.append("                     CONNECT BY PRIOR TMO.ORG_ID = TMO.PARENT_ORG_ID))\n");  
			sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D)\n");

		}else if(String.valueOf(Constant.DUTY_TYPE_COMPANY).equals(dutyType)||String.valueOf(Constant.DUTY_TYPE_DEPT).equals(dutyType))//登录为车厂公司,部门
		{  
			sql.append(tableName+".DEALER_ID>0 AND "+tableName+".DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+","+Constant.DEALER_TYPE_JSZX+")\n");
		}else if(String.valueOf(Constant.DUTY_TYPE_DEALER).equals(dutyType))//经销商,可能不起作用，这里只是考虑这种情况出现时的处理办法。
		{
			sql.append("SELECT TD.DEALER_ID\n");
			sql.append("  FROM TM_DEALER TD\n");  
			sql.append(" WHERE TD.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append(" START WITH TD.DEALER_ORG_ID = "+orgId+"\n");  
			sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D\n");

		}else//开放其他类型代码，如有其它情况在这里维护代码。
		{
			
		}
		return sql.toString();
	}
	
	public static String getDealerIdsForOrderNew(String orgId,String parentOrgId,String dutyType,String tableName) 
	{
		if("".equals(dutyType)||dutyType==null)
			return "";
		if("".equals(tableName)||tableName==null)
		{
			tableName="TM_DEALER";
		}else
		{
			tableName=tableName.trim();
		}
			
		StringBuffer sql=new StringBuffer();
		if(String.valueOf(Constant.DUTY_TYPE_LARGEREGION).equals(dutyType)
		 ||String.valueOf(Constant.DUTY_TYPE_SMALLREGION).equals(dutyType))//大区，小区时
		{
			sql.append("exists (\n");
			sql.append("SELECT  TD.DEALER_ID\n");
			sql.append("  FROM TM_DEALER TD\n");  
			sql.append(" WHERE TD.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append(" and td.dealer_id = ").append(tableName).append(".dealer_id\n");  
			sql.append("AND TD.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+","+Constant.DEALER_TYPE_JSZX+")\n");
			sql.append(" START WITH DEALER_ID IN\n");  
			sql.append("            (SELECT TDOR.DEALER_ID\n");  
			sql.append("               FROM TM_DEALER_ORG_RELATION TDOR\n");  
			sql.append("              WHERE TDOR.ORG_ID IN\n");  
			sql.append("                    (SELECT ORG_ID\n");  
			sql.append("                       FROM TM_ORG TMO\n");  
			sql.append("                      WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                      START WITH TMO.ORG_ID = "+orgId+"\n");  
			sql.append("                     CONNECT BY PRIOR TMO.ORG_ID = TMO.PARENT_ORG_ID))\n");  
			sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D)\n");

		}else if(String.valueOf(Constant.DUTY_TYPE_COMPANY).equals(dutyType)||String.valueOf(Constant.DUTY_TYPE_DEPT).equals(dutyType))//登录为车厂公司,部门
		{  
			sql.append(tableName+".DEALER_ID>0 AND "+tableName+".DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+","+Constant.DEALER_TYPE_JSZX+")\n");
		}else if(String.valueOf(Constant.DUTY_TYPE_DEALER).equals(dutyType))//经销商,可能不起作用，这里只是考虑这种情况出现时的处理办法。
		{
			sql.append("SELECT TD.DEALER_ID\n");
			sql.append("  FROM TM_DEALER TD\n");  
			sql.append(" WHERE TD.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append(" START WITH TD.DEALER_ORG_ID = "+orgId+"\n");  
			sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D\n");

		}else//开放其他类型代码，如有其它情况在这里维护代码。
		{
			
		}
		return sql.toString();
	}
	
	/**
	 * Function : 根据登录的组织ID查询出下属的所有区域
	 * @param :  orgId 登录人的组组织ID
	 * @param :  parentOrgId 登录人的上级组织ID
	 * @param :  dutyType 组织类型（Constant中的组织层级）
	 * @param :  tableName 经销商表别名
	 * @return : 满足条件区域ID集合。
	 */
	public static String getOrgIdsForOrder(String orgId,String parentOrgId,String dutyType,String tableName) 
	{
		if("".equals(dutyType)||dutyType==null)
			return "";
		if("".equals(tableName)||tableName==null)
		{
			tableName="TM_ORG";
		}else
		{
			tableName=tableName.trim();
		}
			
		StringBuffer sql=new StringBuffer();
		if(String.valueOf(Constant.DUTY_TYPE_LARGEREGION).equals(dutyType) ||String.valueOf(Constant.DUTY_TYPE_SMALLREGION).equals(dutyType))//大区，小区时
		{
			sql.append(tableName+".ORG_ID = "+orgId+"\n");
		}else if(String.valueOf(Constant.DUTY_TYPE_COMPANY).equals(dutyType)||String.valueOf(Constant.DUTY_TYPE_DEPT).equals(dutyType))//登录为车厂公司,部门
		{  
			sql.append(tableName+".ORG_ID>0 AND "+tableName+".ORG_TYPE="+Constant.ORG_TYPE_OEM+"\n");
		}else if(String.valueOf(Constant.DUTY_TYPE_DEALER).equals(dutyType))//经销商,可能不起作用，这里只是考虑这种情况出现时的处理办法。
		{

		}else//开放其他类型代码，如有其它情况在这里维护代码。
		{
			
		}
		return sql.toString();
	}
	
	/**
	 * create by xiayanpeng copy by zjy 
	 * @param logonUser SESSION
	 * @return
	 * 加入dealer_type，用于取销售或售后经销商id集合
	 */
	public static String getDealerIds(AclUserBean logonUser,int dealerType) 
	{
		String orgId = logonUser.getOrgId().toString();
		String parentOrgId = logonUser.getParentOrgId().toString();
		String dutyType = logonUser.getDutyType().toString();
		if("".equals(dutyType)||dutyType==null)
			return "";
		StringBuffer sql=new StringBuffer();
		if(String.valueOf(Constant.DUTY_TYPE_COMPANY).equals(dutyType)||String.valueOf(Constant.DUTY_TYPE_DEPT).equals(dutyType)){
			return "";
		}
		if(String.valueOf(Constant.DUTY_TYPE_COMPANY).equals(dutyType)
				||String.valueOf(Constant.DUTY_TYPE_LARGEREGION).equals(dutyType)
				||String.valueOf(Constant.DUTY_TYPE_SMALLREGION).equals(dutyType))//登录为车厂公司,大区，小区时
		{
			sql.append("SELECT  TD.DEALER_ID\n");
			sql.append("  FROM TM_DEALER TD\n");  
			sql.append(" WHERE TD.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("AND TD.DEALER_TYPE = "+dealerType+"\n");
			sql.append(" START WITH DEALER_ID IN\n");  
			sql.append("            (SELECT TDOR.DEALER_ID\n");  
			sql.append("               FROM TM_DEALER_ORG_RELATION TDOR\n");  
			sql.append("              WHERE TDOR.ORG_ID IN\n");  
			sql.append("                    (SELECT ORG_ID\n");  
			sql.append("                       FROM TM_ORG TMO\n");  
			sql.append("                      WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                      START WITH TMO.ORG_ID = "+orgId+"\n");  
			sql.append("                     CONNECT BY PRIOR TMO.ORG_ID = TMO.PARENT_ORG_ID))\n");  
			sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D\n");

		}
//			else if(String.valueOf(Constant.DUTY_TYPE_DEPT).equals(dutyType))//部门
//		{
//			sql.append("SELECT  TD.DEALER_ID\n");
//			sql.append("  FROM TM_DEALER TD\n");  
//			sql.append(" WHERE TD.STATUS = "+Constant.STATUS_ENABLE+"\n"); 
//			sql.append("AND TD.DEALER_TYPE = "+dealerType+"\n");
//			sql.append(" START WITH DEALER_ID IN\n");  
//			sql.append("            (SELECT TDOR.DEALER_ID\n");  
//			sql.append("               FROM TM_DEALER_ORG_RELATION TDOR\n");  
//			sql.append("              WHERE TDOR.ORG_ID IN\n");  
//			sql.append("                    (SELECT ORG_ID\n");  
//			sql.append("                       FROM TM_ORG TMO\n");  
//			sql.append("                      WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+"\n");  
//			sql.append("                      START WITH TMO.ORG_ID = "+parentOrgId+"\n");  
//			sql.append("                     CONNECT BY PRIOR TMO.ORG_ID = TMO.PARENT_ORG_ID))\n");  
//			sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D\n");
//		}
		else if(String.valueOf(Constant.DUTY_TYPE_DEALER).equals(dutyType))//经销商,可能不起作用，这里只是考虑这种情况出现时的处理办法。
		{
			sql.append("SELECT TD.DEALER_ID\n");
			sql.append("  FROM TM_DEALER TD\n");  
			sql.append(" WHERE TD.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append(" START WITH TD.DEALER_ORG_ID = "+orgId+"\n");  
			sql.append("CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D\n");

		}else//开放其他类型代码，如有其它情况在这里维护代码。
		{
			
		}
		return sql.toString();
	}
}

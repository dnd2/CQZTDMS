/**
 * 
 */
package com.infodms.dms.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.RightMessageConstant;
import com.infodms.dms.common.component.message.MessageComponent;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

/**
 * @author xianchao zhang
 *
 */
public class ActionUtil {
	
//	/**
//	 * 初使化数据库中创建人，创建时间，更新人和更新时间，主要在增加记录时使用。
//	 * @param po
//	 * @param logonUser
//	 * @throws Exception 
//	 */
//	public static void setCreatePOTrack(PO po,ActionContext act) throws Exception{
//		Class<? extends PO> poClass = po.getClass();
//		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
//		try {
//			if(logonUser!=null&&logonUser.getUserId()!=null){
//				Method createBymethod = poClass.getMethod("setCreateBy", String.class);
//				createBymethod.invoke(po, logonUser.getUserId());
//			}
//			Method createDate = poClass.getMethod("setCreateDate", Date.class);
//			createDate.invoke(po, new Date());
//			if(logonUser!=null){
//				Method updateBymethod = poClass.getMethod("setUpdateBy", String.class);
//				updateBymethod.invoke(po, logonUser.getUserId());
//			}
//			Method updateDate = poClass.getMethod("setUpdateDate", Date.class);
//			updateDate.invoke(po, new Date());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			throw e;
//		}
//	}
	
	/**
	 * 初使化数据库中创建人，创建时间，更新人和更新时间，主要在增加记录时使用。
	 * @param po
	 * @param act
	 * @throws Exception 
	 */
	public static void setCreatePOTrack(PO po,AclUserBean logonUser) throws Exception{
		Class<? extends PO> poClass = po.getClass();
		try {
			if(logonUser!=null&&logonUser.getUserId()!=null){
				Method createBymethod = poClass.getMethod("setCreateBy", String.class);
				createBymethod.invoke(po, logonUser.getUserId());
			}
			Method createDate = poClass.getMethod("setCreateDate", Date.class);
			createDate.invoke(po, new Date());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
	/**
	 * 初使化数据库中创建人，创建时间主要在增加记录时使用。
	 * @param po
	 * @param logonUser
	 * @throws Exception
	 */
	public static void setCreatePO(PO po,AclUserBean logonUser) throws Exception{
		Class<? extends PO> poClass = po.getClass();
		try {
			if(logonUser!=null&&logonUser.getUserId()!=null){
				Method createBymethod = poClass.getMethod("setCreateBy", Long.class);
				createBymethod.invoke(po, Long.valueOf(logonUser.getUserId()));
			}
			Method createDate = poClass.getMethod("setCreateDate", Date.class);
			createDate.invoke(po, new Date());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
//	/**
//	 * 初使化数据库中更新人和更新时间，主要在更新记录时使用。
//	 * @param po
//	 * @param logonUser
//	 * @throws Exception 
//	 */
//	public static void setUpatePOTrack(PO po,ActionContext act) throws Exception{
//		Class<? extends PO> poClass = po.getClass();
//		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
//		try {
//			if(logonUser!=null&&logonUser.getUserId()!=null){
//				Method updateBymethod = poClass.getMethod("setUpdateBy", String.class);
//				updateBymethod.invoke(po, logonUser.getUserId());
//			}
//			Method updateDatemethod = poClass.getMethod("setUpdateDate", Date.class);
//			updateDatemethod.invoke(po, new Date());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			throw e;
//		}
//	}
	/**
	 * 初使化数据库中更新人和更新时间，主要在更新记录时使用。
	 * @param po
	 * @param act
	 * @throws Exception 
	 */
	public static void setUpatePOTrack(PO po,AclUserBean logonUser) throws Exception{
		Class<? extends PO> poClass = po.getClass();
		try {
			if(logonUser!=null&&logonUser.getUserId()!=null){
				Method updateBymethod = poClass.getMethod("setUpdateBy", String.class);
				updateBymethod.invoke(po, logonUser.getUserId());
			}
			Method updateDatemethod = poClass.getMethod("setUpdateDate", Date.class);
			updateDatemethod.invoke(po, new Date());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
	/**
	 * 初使化数据库中更新人和更新时间，主要在更新记录时使用。
	 * @param po
	 * @param logonUser
	 * @throws Exception
	 */
	public static void setUpatePO(PO po,AclUserBean logonUser) throws Exception{
		Class<? extends PO> poClass = po.getClass();
		try {
			if(logonUser!=null&&logonUser.getUserId()!=null){
				Method updateBymethod = poClass.getMethod("setUpdateBy", Long.class);
				updateBymethod.invoke(po, Long.valueOf(logonUser.getUserId()));
			}
			Method updateDatemethod = poClass.getMethod("setUpdateDate", Date.class);
			updateDatemethod.invoke(po, new Date());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
//	/**
//	 * 
//	* 功能说明：写异常堆栈写入日志中
//	* @param e：异常对象
//	* @param logger：日志对象
//	* 最后修改时间：Aug 25, 2009
//	 */
//	public static void writeErrorStack(Exception e,Logger logger,ActionContext act){
//		AclUserBean logonUser = (AclUserBean)(act.getSession().get(Constant.LOGON_USER));
//		StringBuffer sb =  new StringBuffer();
//		sb.append("用户Id："+logonUser.getUserId());
//		sb.append("-------用户名:"+logonUser.getName());
//		sb.append("-------经销商Id:"+logonUser.getDealerId());
//		//打印出错的用户的信息
//		logger.error(sb.toString());
//		if(e instanceof BizException){
//			BizException be = (BizException)e;
//			logger.error("errorCode:"+be.getErrCode()+"---------------错误信息："+be.getMessage());
//		}
//    	if(e.getCause()!=null){
//    		StringWriter   sw=new   StringWriter();   
//        	PrintWriter   pw=new   PrintWriter(sw);
//    		e.getCause().printStackTrace(pw);
//    		logger.error(sw.toString());
//    	}
//	}
	/**
	 * 
	* 功能说明：将正确的提示信息封装
	* @param rightCode
	* @param act
	* 最后修改时间：Aug 25, 2009
	 * @throws Exception 
	 */
	public static void presentMessage(String rightCode,ActionContext act,Object... param) throws Exception{
		String message = MessageComponent.getInstance().getMessage(rightCode,param);
		act.setOutData(RightMessageConstant.SUCCESS_MESSAGE, message);
	}
	/**
	 * 
	* 功能说明：处理特殊字符，如果含有特殊车辆，就在特殊车辆前面加上“\”,来进行转义
	* @param content
	* @return
	* 最后修改时间：Aug 28, 2009
	 */
	public static String specialChar(String content){
		char[] specialChar = {'%'};
		StringBuffer sb = new StringBuffer();
		char temp ;
		for(int i=0;i<content.length();i++){
			temp = content.charAt(i);
			for(int j=0;j<specialChar.length;j++){
				if((content.charAt(i)==specialChar[j])){
					sb.append("\\").append(temp);
				}else{
					sb.append(temp);
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 
	* 功能说明：返回数据的大写格式
	* @param content
	* @return
	* 最后修改时间：Nov 11, 2009
	 */
	public static String getUpperString(String content){
		return specialChar(content.toUpperCase());
	}
	

	/**
	 * 从访问请求中查找pageSize，如果没有pageSize，则用系统默认的Constant.PAGE_SIZE
	 * @param request
	 * @return
	 * @author chenyub@yonyou.com
	 */
	public static Integer getPageSize(RequestWrapper request){
		String pageSize = request.getParamValue("mainPageSize");
		if(null==pageSize||pageSize.trim().length()<1){
			return Constant.PAGE_SIZE;
		}else{
			return Integer.parseInt(pageSize);
		}
	}
	/**
	 * 从访问请求中查找curPage,如果没有,则默认为1;
	 * @param request
	 * @return
	 * @author chenyub@yonyou.com
	 */
	public static int getCurPage(RequestWrapper request){
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage")):1;
		return curPage;
	}

	/**
	 * 从访问请求中查找用户选中的列表复选框值
	 * @param request
	 * @return
	 * @throws Exception 
	 * @author chenyub@yonyou.com
	 */
	public static String getParamValue(RequestWrapper request, String paramName)
			throws Exception {
		try {
			String rtn = null;
			String checkedValue = request.getParamValue(paramName);
			if (null == checkedValue || checkedValue.trim().length() < 1) {
				rtn = null;
			} else {
				rtn = checkedValue;
			}
			return rtn;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 直接将用户选中的列表复选框值放到返回的数据中
	 * @param act
	 * @throws Exception
	 * @author chenyub@yonyou.com
	 */
	public static void setCheckedValueToOutData(ActionContext act)
			throws Exception {
		try {
			if (null != act) {
				RequestWrapper request = act.getRequest();
				String[] checkParams = new String[] { "hideCheckedId",
						"hideCheckedRegionId","hideCheckedDealerId","hideCheckedMaterialId","hideCheckedMaterialGroupId" };
				List<String> params = new ArrayList<String>();
				for (int i = 0; i < checkParams.length; i++) {
					String checkedValue = getParamValue(request, checkParams[i]);
					if (!StringUtil.isNull(checkedValue)&&!"null".equals(checkedValue)) {
						checkedValue = new String(checkedValue.getBytes("ISO-8859-1"), "UTF-8");
						act.setOutData(checkParams[i], checkedValue);
						params.add(checkParams[i]);
					}
				}
				if (params.size() > 0) {
					act.setOutData("checkParams", params);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 直接将用户选中的列表复选框值放到返回的数据中
	 * @param act
	 * @throws Exception
	 * @author chenyub@yonyou.com
	 */
	public static void removeCheckedValueToOutData(ActionContext act)
			throws Exception {
		try {
			if (null != act) {
				RequestWrapper request = act.getRequest();
				String[] checkParams = new String[] { "hideCheckedId",
						"hideCheckedRegionId","hideCheckedDealerId","hideCheckedMaterialId","hideCheckedMaterialGroupId" };
				for (int i = 0; i < checkParams.length; i++) {
					String checkedValue = getParamValue(request, checkParams[i]);
					if (!StringUtil.isNull(checkedValue)&&!"null".equals(checkedValue)) {
						checkedValue = new String(checkedValue.getBytes("ISO-8859-1"), "UTF-8");
						act.setOutData(checkParams[i], null);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 设置自定义每页条数开关,默认不设置的可以不调用此方法
	 * 
	 * @param act
	 * @throws Exception
	 * @author chenyub@yonyou.com
	 */
	public static void setCustomPageSizeFlag(ActionContext act,boolean flag) throws Exception{
		try {
			if (null != act) {
				act.setOutData("customPageSizeFlag", flag);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 调整列宽度的开关,不打开可以不调用此方法
	 * @param act
	 * @param flag
	 * @throws Exception
	 * @author chenyub@yonyou.com
	 */
	public static void setResizeColumnWidthFlag(ActionContext act,boolean flag) throws Exception{
		try {
			if (null != act) {
				act.setOutData("resizeColumnWidthFlag", flag);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 表格列排序开关,不打开可以不调用此方法
	 * @param act
	 * @param flag
	 * @throws Exception
	 * @author chenyub@yonyou.com
	 */
	public static void setTableSortFlag(ActionContext act,boolean flag) throws Exception{
		try {
			if (null != act) {
				act.setOutData("tableSortflag", flag);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 表格列交换开关,不打开可以不调用此方法
	 * @param act
	 * @param flag
	 * @throws Exception
	 * @author chenyub@yonyou.com
	 */
	public static void setSwapColumnFlag(ActionContext act,boolean flag) throws Exception{
		try {
			if (null != act) {
				act.setOutData("swapColumnFlag", flag);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
}

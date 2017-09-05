package com.infodms.dms.common.tag;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.RequestWrapper;

public class DaoFactory {
	
	public static void delAndReinsetFile(AclUserBean loginUser, String[] fjids,String pkId) throws SQLException {
		FileUploadManager.delAllFilesUploadByBusiness(pkId, fjids);
		FileUploadManager.fileUploadByBusiness(pkId, fjids, loginUser);
	}
	/**
	 * 检测List<Map<String, Object>>是否为空
	 * @param list
	 * @return
	 */
	public static boolean checkListNull(List<?> list){
		boolean flag=false;
		if(list!=null&&list.size()>0){
			flag=true;
		}
		return flag;
	}
	/**
	 * 取主键ID
	 * @return
	 */
	public static Long getPkId(){
		return BaseUtils.ConvertLong(SequenceManager.getSequence(""));
	} 
	/**
	 * 取主键ID
	 * @return
	 */
	public static Long getPkId(String str){
		return BaseUtils.ConvertLong(SequenceManager.getSequence(str));
	} 
	/**
	 * 返回一个req 的参数
	 * @param request
	 * @param param
	 * @return
	 */
	public static String getParam(RequestWrapper request,String param){
		return BaseUtils.checkNull(request.getParamValue(param));
	}
	/**
	 * 返回一个req 的参数
	 * @param request
	 * @param param
	 * @return
	 */
	public static String[] getParams(RequestWrapper request,String param){
		return request.getParamValues(param);
	}
	/**
	 * 检测并拼接
	 * @param request
	 * @param sb
	 * @param params
	 * @param sql
	 * @param type 1 normal 2 like 3  >=日期 <=
	 * @return
	 * @throws BizException 
	 */
	public static StringBuffer checkMontage(RequestWrapper request,StringBuffer sb,String params,String sqls,String types) throws BizException{
		String[] splitParam = params.split(",");
		String[] splitSql = sqls.split(",");
		String[] splitType = types.split(",");
		int paramLen = splitParam.length;
		int typeLen = splitType.length;
		int sqlLen = splitSql.length;
		if(paramLen!=typeLen || paramLen!=sqlLen){
		    throw new BizException("传入的参数不对应！"+paramLen +"<>"+typeLen);
		}else{
			sb.append(" where 1=1 \n");
		}
		int temp=0;
		for (String param : splitParam) {
			String tempParam = BaseUtils.checkNull(request.getParamValue(param));
			if(BaseUtils.notNull(tempParam)){
				if(BaseUtils.ConvertInt(splitType[temp])==1){
					sb.append(" and "+splitSql[temp]+"='"+tempParam+"'\n");
				}
				if(BaseUtils.ConvertInt(splitType[temp])==2){
					sb.append(" and "+splitSql[temp]+" like '%"+tempParam+"%'\n");
				}
				if(BaseUtils.ConvertInt(splitType[temp])==3){
					sb.append(" and "+splitSql[temp]+" >= to_date('"+tempParam+" 00:00:00','yyyy-mm-dd hh24:mi:ss') \n"); 
					sb.append(" and "+splitSql[temp]+" <= to_date('"+tempParam+" 23:59:59','yyyy-mm-dd hh24:mi:ss') \n"); 
				}
			}
			temp++;
		}
		return sb;
	}
	/***
	 * 拼接stringbuffer的工具
	 * @param sb
	 * @param tempParam
	 * @param type
	 * @return
	 */
	public static void getsql(StringBuffer sb,String temp,String tempParam,int type){
		if(BaseUtils.notNull(tempParam)){
			tempParam = StringEscapeUtils.escapeSql(tempParam);
			if(type==1){ //等于条件
				sb.append(" and "+temp+"='"+tempParam+"'\n");
			}
			if(type==2){//模糊条件
				sb.append(" and "+temp+" like '%"+tempParam+"%'\n");
			}
			if(type==3){//时间大于 不需要转化的date
				if(tempParam.length()>10){
					sb.append(" and "+temp+" >= to_date('"+tempParam+"','yyyy-mm-dd hh24:mi:ss') \n"); 
				}else{
					sb.append(" and "+temp+" >= to_date('"+tempParam+" 00:00:00','yyyy-mm-dd hh24:mi:ss') \n"); 
				}
			}
			if(type==31){//时间大于 需要string转date的
				if(tempParam.length()>10){
					sb.append(" and to_date("+temp+", 'yyyy-mm-dd hh24:mi:ss')>= to_date('"+tempParam+"','yyyy-mm-dd hh24:mi:ss') \n"); 
				}else{
					sb.append(" and to_date("+temp+", 'yyyy-mm-dd hh24:mi:ss') >= to_date('"+tempParam+" 00:00:00','yyyy-mm-dd hh24:mi:ss') \n"); 
				}
			}
			if(type==4){//时间小于 不需要转化的date
				if(tempParam.length()>10){
					sb.append(" and "+temp+" <= to_date('"+tempParam+"','yyyy-mm-dd hh24:mi:ss') \n"); 
				}else{
					sb.append(" and "+temp+" <= to_date('"+tempParam+" 23:59:59','yyyy-mm-dd hh24:mi:ss') \n"); 
				}
			}
			if(type==41){//时间小于 需要string转date的
				if(tempParam.length()>10){
					sb.append(" and to_date("+temp+", 'yyyy-mm-dd hh24:mi:ss') <= to_date('"+tempParam+"','yyyy-mm-dd hh24:mi:ss') \n"); 
				}else{
					sb.append(" and to_date("+temp+", 'yyyy-mm-dd hh24:mi:ss') <= to_date('"+tempParam+" 23:59:59','yyyy-mm-dd hh24:mi:ss') \n"); 
				}
			}
			if(type==5){//单一时间大小于 
				if(tempParam.length()>10){
					sb.append(" and "+temp+" >= to_date('"+tempParam+"','yyyy-mm-dd hh24:mi:ss') \n"); 
					sb.append(" and "+temp+" <= to_date('"+tempParam+"','yyyy-mm-dd hh24:mi:ss') \n"); 
				}else{
					sb.append(" and "+temp+" >= to_date('"+tempParam+" 00:00:00','yyyy-mm-dd hh24:mi:ss') \n"); 
					sb.append(" and "+temp+" <= to_date('"+tempParam+" 23:59:59','yyyy-mm-dd hh24:mi:ss') \n"); 
				}
			}
			if(type==51){//单一时间大小于 需要string转date的
				if(tempParam.length()>10){
					sb.append(" and to_date("+temp+", 'yyyy-mm-dd hh24:mi:ss') >= to_date('"+tempParam+"','yyyy-mm-dd hh24:mi:ss') \n"); 
					sb.append(" and to_date("+temp+", 'yyyy-mm-dd hh24:mi:ss') <= to_date('"+tempParam+"','yyyy-mm-dd hh24:mi:ss') \n"); 
				}else{
					sb.append(" and to_date("+temp+", 'yyyy-mm-dd hh24:mi:ss') >= to_date('"+tempParam+"','yyyy-mm-dd hh24:mi:ss') \n"); 
					sb.append(" and to_date("+temp+", 'yyyy-mm-dd hh24:mi:ss') <= to_date('"+tempParam+"','yyyy-mm-dd hh24:mi:ss') \n"); 
				}
			}
			if(type==6){// in 条件
				String[] split = tempParam.split(",");
				StringBuffer sbTemp=new StringBuffer(); 
				int length = split.length;
				if(length>0){
					for (int i = 0; i < length; i++) {
						String str = split[i];
						if(i==length-1){
							sbTemp.append("'"+str+"'");
						}else{
							sbTemp.append("'"+str+"',");
						}
					}
					sb.append(" and "+temp+" in ("+sbTemp.toString()+")\n");
				}
			}
			if(type==8){ // not in 条件
				String[] split = tempParam.split(",");
				StringBuffer sbTemp=new StringBuffer(); 
				int length = split.length;
				for (int i = 0; i < length; i++) {
					String str = split[i];
					if(i==length-1){
						sbTemp.append("'"+str+"'");
					}else{
						sbTemp.append("'"+str+"',");
					}
				}
				sb.append(" and "+temp+" not in ("+sbTemp.toString()+")\n");
			}
			if(type==7){ // != 条件
				sb.append(" and "+temp+"!='"+tempParam+"'\n");
			}
		}
	}
	/***
	 * 专门为拼接字符in ('a','b')而上(数组)
	 * @param arr
	 * @return
	 */
	public static String getSqlByarrIn(String [] arr){
		StringBuffer sbTemp=new StringBuffer(); 
		if(arr!=null && arr.length>0){
			int length =arr.length ;
			for (int i = 0; i < length; i++) {
				if(i==length-1){
					sbTemp.append(arr[i]);
				}else{
					sbTemp.append(arr[i]+",");
				}
			}
		}
		return sbTemp.toString();
	}
	/***
	 * 专门为拼接字符in ('a','b')而上(字符)
	 * @param str
	 * @return
	 */
	public static String getSqlByarrIn(String str){
		StringBuffer sbTemp=new StringBuffer(); 
		String[] split = str.split(",");
		int length = split.length;
		if(length>0 && !"".equals(str)){
			for (int i = 0; i < length; i++) {
				if(i==length-1){
					sbTemp.append(""+split[i]+"");
				}else{
					sbTemp.append(""+split[i]+",");
				}
			}
		}
		return sbTemp.toString();
	}
	
}

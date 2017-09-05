/**********************************************************************
 * <pre>
 * FILE : SysLogDownLoad.java
 * CLASS : SysLogDownLoad
 *
 * AUTHOR : LiuSha
 *
 * FUNCTION :  系统日志查看Action.
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2009-09-02| LiuSha  | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
/**
 * $Id: SysLogDownLoad.java,v 1.1 2010/08/16 01:44:24 yuch Exp $
 */

package com.infodms.dms.actions.sysmng.sysLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Appender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
/**
 * Function    : 系统日志查看  
 * @throws     : Exception
 * @return     : 满足条件的车辆信息列表 
 * LastUpdate  : 2009-09-02
 */
public class SysLogDownLoad {

	public Logger logger = Logger.getLogger(SysLogDownLoad.class); 
	public void initializePage()
	{
		ActionContext act = ActionContext.getContext();
		act.setForword("/jsp/systemMng/baseData/logSearch.jsp");
	}
	/** 
	 * 功能说明：下载日志文件 
	 * 最后修改时间：Sep 21, 2009
	 */
	public void logDownLoad() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = null;
		AclUserBean logonUser = null;
		try {
			request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dataStart = CommonUtils.checkNull(request
					.getParamValue("date1"));// 查询日志文件的开始时间
			
			
			String dataEnd = CommonUtils.checkNull(request
					.getParamValue("date2")); // 结束时间
			//String datestr = dataStart.substring(dataEnd.lastIndexOf(5,dataEnd.length())+1);
			String logtype = CommonUtils.checkNull(request
					.getParamValue("logtype")); // 日志类型
			int retValue = parseFile(dataStart,dataEnd,logtype);
			act.setOutData("logtype",logtype);
			if(retValue==1){
				act.setForword("/jsp/systemMng/baseData/logSearch.jsp");
				throw new BizException(act ,ErrorCodeConstant.NO_EXIST_CODE,"日志文件",dataStart+"至"+dataEnd);
			}
			if(retValue==2)
			{
				act.setForword("/jsp/systemMng/baseData/logSearch.jsp");
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG ,"一次最多下载10个日志文件","");
			}
		} catch (Exception e) { 
			BizException e1 = new BizException(act ,e,ErrorCodeConstant.FAILURE_CODE,"下载");
			logger.error(logonUser,e1);
			act.setException(e);
		}
	}
	/**
	 * 
	* 功能说明：解析当前日志文件
	* @param dataStart
	* @param dataEnd
	* @param logtype
	* @throws Exception
	* 最后修改时间：Sep 21, 2009
	 */
	private int parseFile(String dataStart, String dataEnd, String logtype) throws Exception {
		 List<File> fileList = null;
		 try {
			 // 解析配置文件获取log4j中的路俓
			 Enumeration<Appender> enume = logger.getLoggerRepository().getRootLogger().getAllAppenders();
			 //生成所需要下载的日志文件的集合
			 while(enume.hasMoreElements()){
				 Appender app =enume.nextElement();
				 if(app instanceof DailyRollingFileAppender){
					 String appenderName= ((DailyRollingFileAppender)app).getName();//日志类型
					 if(appenderName!=null&&appenderName.equals(logtype)){
						 String appenderFile= ((DailyRollingFileAppender)app).getFile();
						 logger.debug("File:"+appenderFile);
						 String datePatten = ((DailyRollingFileAppender)app).getDatePattern();
						 logger.debug("DatePatten:"+datePatten);
						 fileList = generateDownLoadFile(appenderFile,datePatten,dataStart,dataEnd);
					 }
				 }
			 }
			 if(fileList!=null&&fileList.size()>0&&fileList.size()<10){
				 wirteZipOutputStream(fileList,dataStart,dataEnd);
				 return 0;
			 }
			 else if(fileList.size()>10)
			 {
				 return 2;
			 }else{
				 return 1;
			 }
		 } catch (Exception e) {
			throw e;
		 }
	}
	private void wirteZipOutputStream(List<File> fileList,String dataStart, String dataEnd) throws Exception{
		BufferedReader br = null;
		ZipOutputStream zip = null;
		PrintWriter pw = null;
		OutputStream os = null;
		try {
			String filename = dataStart+"-"+dataEnd+"log.zip";
			ActionContext act = ActionContext.getContext();
			ResponseWrapper response = act.getResponse();
			filename = new String(filename.getBytes("GB2312"), "ISO8859-1"); // 导出的文字编码
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ filename);
			os = response.getOutputStream();
			zip = new ZipOutputStream(os);
			pw = new PrintWriter(zip); 
			if(fileList!=null&&fileList.size()>0){ 
				for(int i=0;i<fileList.size();i++){
					File fileTemp = fileList.get(i);
					br = new BufferedReader(new FileReader(fileTemp));
					logger.debug("fileName:"+fileTemp.getName());
					zip.putNextEntry(new ZipEntry(fileTemp.getName()));
					String temp = null;
					while ((temp =br.readLine())!= null) {
						pw.println(temp);
					}
					zip.closeEntry();
					try{
						if(br!=null){  
							br.close();
						}						
					}catch(Exception e){
						throw e;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}finally{
			try{
				if(pw!=null){
					pw.flush();
					pw.close();
				}
				if(zip!=null){
					zip.flush();
					zip.close();
				}
				if(os!=null){
					os.flush();
					os.close();
				}
			}catch(Exception e){
				throw e;
			}
		}
	}
	/**
	 * 
	* 功能说明：生成所需要下载的文件对象
	* @param appenderFile
	* @param datePatten
	* @param dataStart
	* @param dataEnd
	* @return
	* @throws Exception
	* 最后修改时间：Sep 21, 2009
	 */
	private List<File> generateDownLoadFile(String appenderFile,String datePatten, String dataStart, String dataEnd) throws Exception {
		Date nowDate = DateTimeUtil.stringToDate(DateTimeUtil.parseDateToDate(new Date()));
		Date startDate = DateTimeUtil.stringToDate(dataStart);
		Date endDate =DateTimeUtil.stringToDate(dataEnd);
		GregorianCalendar sgc = new GregorianCalendar();
		GregorianCalendar egc = new GregorianCalendar();
		//设置日志开始统计的开始日期和结束日期
		if(nowDate.compareTo(endDate)>=0){
			sgc.setTime(startDate);
			egc.setTime(endDate);
		}else{
			sgc.setTime(startDate);
			egc.setTime(DateTimeUtil.stringToDate(DateTimeUtil.parseDateToDate(new Date())));
		}
		//创建下载文件的列表
		List<File> fileList = new ArrayList<File>();
		File fileTemp = null;
		do{
			if(sgc.getTime().compareTo(nowDate)==0){
				doWithIfContainCurrDay(fileList,appenderFile);
			}else{
				//当发现有的文件不存在时，不跳出循环，继续执行
				try{
					fileTemp = new File(appenderFile+"."+DateTimeUtil.parseDateToDate(sgc.getTime())+".log");
					if(fileTemp.exists()){
						fileList.add(fileTemp);
					}
				}catch(Exception e){
					
				}
			}
			sgc.add(Calendar.DATE, 1);
		}while(sgc.compareTo(egc)<=0);
		return fileList;
	}
	/**
	 * 
	* 功能说明：对如果包括当天进行处理
	* @param fileList
	* 最后修改时间：Sep 21, 2009
	 */
	private void  doWithIfContainCurrDay(List<File> fileList,String appenderFile){
		fileList.add(new File(appenderFile));
	}
}

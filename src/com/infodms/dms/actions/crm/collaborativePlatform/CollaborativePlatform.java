package com.infodms.dms.actions.crm.collaborativePlatform;

import java.io.BufferedInputStream;
 
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

/**
 * @Title: 协同平台excel导出展示
 * @Description: 
 * @Copyright: 
 * @Company:  
 * @Date:  
 * @author 
 * @mail   
 * @version 1.0
 * @remark 
 */
public class CollaborativePlatform {
	
	public Logger logger = Logger.getLogger(CollaborativePlatform.class);  
	private final String FILEURL = "http://fdms.changansuzuki.com:8082/dms/collaborativePlatform/";//服务器URL
	
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	/**
	 * KPI及基础数据定义-导出
	 * 话务与技巧
	 */
	public void basicData(){
		ActionContext act = ActionContext.getContext();
		
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		try {
			ResponseWrapper response=act.getResponse();
			
			//Properties props = new Properties();
			
			//props.load(ServiceActivityVinImport.class.getClassLoader().getResourceAsStream("FileStore.properties"));
    		
			
			String customerCareVisit="customerCareVisit"; //客户关怀回访
			
			String  cluesDevelop="cluesDevelop";  //线索开拓
			
			String  demandAnalysis="demandAnalysis";  // 需求分析
			
			String  invitation="invitation"; //邀约
			
			String  receptionHall="receptionHall"; //展厅接待
			
			String KPIorBasicData = "KPIorBasicData";//KPI及基础数据定义
			
			String customerAssistance ="customerAssis";//客户辅助判断模型
			
			String testDriveStandard = "testDriveStandard";//试乘试驾标准
			 
			String fileTitle = CommonUtils.checkNull(request.getParamValue("fileTitle"));
			
			String old="";
			
			if(fileTitle.equals(KPIorBasicData))
			{
				old = KPIorBasicData+".xlsx";
				
			}else if(fileTitle.equals(customerAssistance))
			{
				old= customerAssistance+".xlsx";
				
			}else if(fileTitle.equals(testDriveStandard))
			{
				old = testDriveStandard+".xlsx";
				
			}else if(fileTitle.equals(customerCareVisit))
			{
				old = customerCareVisit+".docx";
				
			}else if(fileTitle.equals(cluesDevelop))
			{
				old = cluesDevelop+".docx";
				
			}else if(fileTitle.equals(demandAnalysis))
			{
				old = demandAnalysis+".docx";
				
			}else if(fileTitle.equals(invitation))
			{
				old = invitation+".docx";
				
			}else if(fileTitle.equals(receptionHall))
			{
				old = receptionHall+".docx";
				
			} 
			
			String filePath= FILEURL+old;
 
			
			String fileName="";
			
			openExls(response, fileName, filePath, old);
			
		} catch (Exception e) 
		{
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			
			logger.error(logonUser,e1);
			
			act.setException(e1);
		}
	}
	
	
	public static boolean openExls(ResponseWrapper response,String fileName,String filePath,String old) throws Exception {
		
		boolean flag = false;
		
		old=new String(old.getBytes(),"UTF-8");
		
		String pathStr="";
		
		if("".equals(fileName)&&filePath.indexOf(".")!=-1)
		{
			pathStr=filePath;
		}else
		{
			pathStr=filePath+"\\"+fileName;
		}
		
		BufferedInputStream bis = null;
		
		OutputStream os = null;
		
		try {
			
			URL url = new URL(pathStr);
			
			InputStream ism=url.openStream();
			
			bis = new BufferedInputStream(ism);
			
			byte[] buffer = new byte[2048];
			
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			
			response.addHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(old, "UTF-8"));
			
			os = response.getOutputStream();
			
			Thread.sleep(500);
			
			int len = 0;
			
			while((len = bis.read(buffer))!=-1 )
			{
				os.write(buffer,0,len);
				
				os.flush();
			}
			 
			bis.close();
			
			os.close();
			
			flag = true;
		} catch (Exception e) 
		{
			e.printStackTrace();
			throw e;
		} finally 
		{
			if (null != bis) 
			{
				bis.close();
			} 
			if (null != os) 
			{
				os.close();
			}
		}
		return flag;
	}
}

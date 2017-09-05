/**********************************************************************
 * <pre>
 * FILE : FileDownloadAction.java
 * CLASS : FileDownloadAction
 *
 * AUTHOR : SuMMeR
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		  |2009-8-31| SuMMeR| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
/**
 * $Id: FileDownloadAction.java,v 1.1 2010/08/16 01:44:27 yuch Exp $
 */

package com.infodms.dms.actions.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileDownloadAction;
import com.infoservice.filestore.FileStore;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

/**
 * @author SuMMeR
 */
public class FileDownloadAction
{
	public Logger logger = Logger.getLogger(FileDownloadAction.class);

	public static void download() throws Exception
	{
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = null;
		ResponseWrapper response = null;
		OutputStream out = null;
		try
		{
			request = act.getRequest();
			response = act.getResponse();
			out = response.getOutputStream();
			String fileId = request.getParamValue("fileId");
			FileStore fs = FileStore.getInstance();
			byte[] bytes = fs.read(fileId);
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment;filename = " + fileId);
			out.write(bytes);
			out.flush();
		}
		catch (Exception e)
		{
			act.setException(e);
			throw e;
		}
		finally
		{
			if (null != out)
			{
				out.close();
			}
		}
	}
	/*
	 * 下载模板
	 * fileName：文件服务器上文件名
	 * filePath：文件路径，如D:/....或者//http://192.168.1.24:8099/dms/
	 * outPutName：下载得到文件的名称
	 */
   public static boolean downloadTemplate(ResponseWrapper response,String fileName,String filePath,String outPutName) throws Exception{
			 
			boolean flag = false;
			String pathStr="";
			if(filePath.lastIndexOf("\\")==filePath.length()-1){
				filePath=filePath.substring(0,filePath.length()-1);
			}
			if("".equals(fileName)&&filePath.indexOf(".")==-1){
				return flag;
			}
			if(!"".equals(fileName)&&fileName.indexOf(".")==-1){
				return flag;
			}
			if(filePath.indexOf(".")!=-1&&fileName.indexOf(".")!=-1){
				return flag;
			}
			if("".equals(fileName)&&filePath.indexOf(".")!=-1){
				pathStr=filePath;
			}else{
				pathStr=filePath+"\\"+fileName;
			}
			System.out.println(pathStr);
			File file = new File(pathStr);
			URL url = new URL(pathStr);
			InputStream ism=url.openStream();
			BufferedInputStream bis = new BufferedInputStream(ism);
			//String fileURL = "file:/" + file.getAbsolutePath();
			//URL url = new URL(fileURL);
			//if(file.exists()){
			//FileInputStream bis = new FileInputStream(pathStr);
				    byte[] buffer = new byte[1024];
				    response.setContentType("application/x-rar-compressed");
				    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(outPutName, "utf-8"));
				    OutputStream os = response.getOutputStream();
				    while(bis.read(buffer) > 0){
				     os.write(buffer);
				    }
				    bis.close();
				    os.close();
				    flag = true;
//			}
			return flag;
		}
}

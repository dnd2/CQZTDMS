package com.infodms.dms.actions.common;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.util.NewsFileStoreImple;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.FileUpLoadDAO;
import com.infodms.dms.po.FsFileuploadPO;
import com.infoservice.filestore.utils.IdUtil2;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;


/**
 * mofied by andy.ten@tom.com
 */
public class dealerFileUploadAction {
	public Logger logger = Logger.getLogger(dealerFileUploadAction.class);

	public void fileUpload() throws Exception {
		
		ActionContext act = ActionContext.getContext();
		try 
		{
			//added by andy.ten@tom.com
			if(act.getException() != null)
			{
				throw new Exception(act.getException().getMessage());
			}
			//end
			RequestWrapper request = act.getRequest();
			FileObject uploadFile = request.getParamObject("uploadFile");
			//System.out.println(uploadFile.getLength());
			/***将附件大小由之前的2M 改为最大只能上传50M  YH 2011.3.30***/
			if(uploadFile.getLength() > 1024*1000*50)
			{
				throw new Exception("上传文件限制大小为50M，服务器资源有限，请节约使用！");
			}
			AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String fileName = uploadFile.getFileName();
			fileName = fileName.substring(fileName.lastIndexOf("\\")+1,fileName.length());
			//ByteArrayInputStream is = new ByteArrayInputStream(uploadFile.getContent());
				//TODO 修改新闻的文件服务器存储位置为tmp/news目录下 2013-01-23 韩晓宇 
				NewsFileStoreImple store = NewsFileStoreImple.getInstance();
				// 上传到文件服务器并获取文件ID
				String fileid = store.write(null, null, uploadFile.getFileName(), new ByteArrayInputStream(uploadFile.getContent()), "news");
				// 通过文件ID获取文件URL
				String fileUrl = store.getDomainURL(fileid, "news");
				FsFileuploadPO po = new FsFileuploadPO();
				po.setFileid(fileid);
				po.setFileurl(fileUrl);
				po.setFilename(fileName);
				FileUpLoadDAO dao = new FileUpLoadDAO();
				dao.addDisableFile(po, logonUser);
				//end
				act.setOutData("fileId", fileid);
				act.setOutData("fileUrl", fileUrl);
				act.setOutData("fileName", fileName);
				act.setOutData("fjid", po.getFjid());
				act.setForword("/dealerCommonUploadIF.jsp");
                
			
		} catch (Exception e) {
			act.setException(e);
			act.setOutData("errMsg", e.getMessage());
			act.setForword("/dealerCommonUploadIF.jsp");
		}
	}
	

}

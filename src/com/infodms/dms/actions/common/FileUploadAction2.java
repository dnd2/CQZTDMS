package com.infodms.dms.actions.common;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.FileUpLoadDAO;
import com.infodms.dms.po.FsFileuploadPO;
import com.infoservice.filestore.FileStore;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;


/**
 * mofied by andy.ten@tom.com
 */
public class FileUploadAction2 {
	public Logger logger = Logger.getLogger(FileUploadAction2.class);

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
				FileStore store = FileStore.getInstance();
				// 上传到文件服务器并获取文件ID
				String fileid = store.write(uploadFile.getFileName(), uploadFile.getContent());
				// 通过文件ID获取文件URL
				String fileUrl = store.getDomainURL(fileid);
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
				act.setForword("/commonUploadIF2.jsp");
                
			
		} catch (Exception e) {
			act.setException(e);
			act.setOutData("errMsg", e.getMessage());
			act.setForword("/commonUploadIF2.jsp");
		}
	}
	

}

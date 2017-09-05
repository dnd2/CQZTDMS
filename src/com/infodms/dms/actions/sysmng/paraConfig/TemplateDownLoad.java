package com.infodms.dms.actions.sysmng.paraConfig;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileDownloadAction;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.sales.paraConfigDao.TemplateDownLoadDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class TemplateDownLoad {

	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	
	private final String templateDownLoadInitUrl = "/jsp/systemMng/paraConfig/templateDownLoad.jsp";
	
	public void templateDownLoadInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TemplateDownLoadDao dao=TemplateDownLoadDao.getInstance();
		try {
			String companyId=GetOemcompanyId.getOemCompanyId(logonUser).toString();
			List<Map<String, Object>> list=dao.selectTemplateParaConfig("2014",companyId);
			act.setOutData("list", list);
			act.setForword(templateDownLoadInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 下载模板
	 */
	public void templateDownLoad(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			TemplateDownLoadDao dao=TemplateDownLoadDao.getInstance();
			RequestWrapper request=act.getRequest();
			ResponseWrapper response=act.getResponse();
			String fileId=request.getParamValue("fielId");
			String companyId=GetOemcompanyId.getOemCompanyId(logonUser).toString();
			Map<String, Object> map=dao.selectTemplateParaConfigMap(fileId,companyId);
			
			String filePath=map.get("PARA_VALUE").toString()+"\\";
			String fileName="";
			String old=map.get("PARA_NAME").toString()+".xls";
			FileDownloadAction.downloadTemplate(response, fileName, filePath, old);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}

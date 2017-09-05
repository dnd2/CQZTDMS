package com.infodms.dms.actions.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.serviceActivity.Download;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.claim.basicData.HomePageNewsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

public class FileDownLoad {
	private Logger logger = Logger.getLogger(this.getClass());

	private ActionContext act = ActionContext.getContext();
	private RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private final FileDownLoadDao dao = FileDownLoadDao.getInstance();
	
	/**
	 * @author wizard_lee
	 * @param
	 * @for:页面下载附件,隐藏文件服务器地址,下载文件名与上传一致
	 */
	public void fileDownloadQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		FsFileuploadPO detail = new FsFileuploadPO();
		String fjid = request.getParamValue("fjid");// 获取附件ID
		detail.setFjid(Long.valueOf(fjid));
		List<FsFileuploadPO> lists = dao.select(detail);
		try {
			for (int i = 0; i < lists.size(); i++) {
				Download.downloadAttachment(response, "", lists.get(i)
						.getFileurl().toString(), lists.get(i).getFilename()
						.toString());
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.FAILURE_CODE, "下载附件出错");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}

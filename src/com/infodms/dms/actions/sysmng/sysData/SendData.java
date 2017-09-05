package com.infodms.dms.actions.sysmng.sysData;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.actions.partsmanage.partclaim.PartClaimApply;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.dms.chana.actions.OSA11;
import com.infoservice.dms.chana.actions.OSA12;
import com.infoservice.dms.chana.actions.OSC44;
import com.infoservice.dms.chana.actions.OSS01;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

/**
 * 
* @ClassName: SendData 
* @Description: TODO(下发主数据) 
* @author liuqiang 
* @date Oct 10, 2010 6:24:42 PM 
*
 */
public class SendData {
	public static final Logger logger = Logger.getLogger(PartClaimApply.class);
	private final String SEND_DATA_INIT_URL = "/jsp/systemMng/baseData/baseDataSend.jsp";
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private RequestWrapper request = act.getRequest();
	/**下发失败的经销商记录**/
	private  Map<Long, List<String>> errors = new HashMap<Long, List<String>>();
	private final String ERR_FILE = "error.txt";
	
	/**
	 * 
	* @Title: sendDataInit 
	* @Description: TODO(发送主数据页面) 
	* @return void    返回类型 
	* @throws
	 */
	public void sendDataInit() {
		try {
			act.setForword(SEND_DATA_INIT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: sendPart 
	* @Description: TODO(下发配件) 
	* @return void    返回类型 
	* @throws
	 */
	public void sendPart() {
		try {
			List<String> dcl = assembleDealerCode();
			String partFlag = CommonUtils.checkNull(request.getParamValue("partFlag"));
			OSC44 osc = new OSC44();
			List<String> errCodes = osc.sendData(dcl,partFlag);
			recordError(errCodes);
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.FAILURE_CODE,"配件主数据下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: sendMaterialGroup 
	* @Description: TODO(物料组下发) 
	* @return void    返回类型 
	* @throws
	 */
	public void sendMaterialGroup() {
		try {
			List<String> dcl = assembleDealerCode();
			OSA11 osc = new OSA11();
			List<String> errCodes = osc.sendData(dcl);
			recordError(errCodes);
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.FAILURE_CODE, "物料组下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: sendMaterial 
	* @Description: TODO(物料下发) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void sendMaterial() {
		try {
			List<String> dcl = assembleDealerCode();
			OSA12 osc = new OSA12();
			List<String> errCodes = osc.sendData(dcl);
			recordError(errCodes);
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.FAILURE_CODE, "物料下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: sendUrlFunc 
	* @Description: TODO(上端URL功能列表下发) 
	* @return void    返回类型 
	* @throws
	 */
	public void sendUrlFunc() {
		try {
			List<String> dcl = assembleDealerCode();
			OSS01 osc = new OSS01();
			List<String> errCodes = osc.sendData(dcl);
			recordError(errCodes);
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.FAILURE_CODE, "功能列表下发");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: assembleDealerCode 
	* @Description: TODO(组装dealerCode List) 
	* @return List<String> 经销商代码列表
	* @throws
	 */
	private List<String> assembleDealerCode() {
		String dealerCodes = CommonUtils.checkNull(request.getParamValue("dealerCode"));
		String[] dcs = dealerCodes.split(",");
		List<String> dcl = new ArrayList<String>();
		for (String dealerCode : dcs) {
			dcl.add(dealerCode);
		}
		return dcl;
	}
	/**
	 * 
	* @Title: recordError 
	* @Description: TODO(记录错误的发送失败的经销商) 
	* @param @param dealerCodes    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void recordError(List<String> dealerCodes) {
		List<String> err = errors.get(logonUser.getUserId());
		if (null != err && err.size() > 0) {
			err.clear();//清空错误记录
		}
		errors.put(logonUser.getUserId(), dealerCodes);
	}
	/**
	 * 
	* @Title: expError 
	* @Description: TODO(导出发送失败的经销商列表) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void expError() {
		PrintWriter pw = null;
		try {
			ResponseWrapper response = act.getResponse();
			response.setContentType("application/text");
			response.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(ERR_FILE, "utf-8"));
			OutputStream os = response.getOutputStream();
			pw = new PrintWriter(os);
			List<String> errs = errors.get(logonUser.getUserId());
			if (null != errs && errs.size() > 0) {
				for (int i = 0; i < errs.size(); i++) {
					pw.print(String.valueOf(i + 1));
					pw.print(". ");
					pw.println(errs.get(i));
				}
			}
			pw.flush();
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"错误信息下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != pw) {
				pw.close();
			}
		}
	}
}

package com.infodms.dms.actions.sysbusinesparams.businesparamsmanage;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sysbusinesparams.businesparamsmanage.SysParameterManageDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**   
 * @Title  : SysParameterManage.java
 * @Package: com.infodms.dms.actions.sysbusinesparams.businesparamsmanage
 * @Description: 系统可变参数表维护
 * @date   : 2010-7-6 
 * @version: V1.0   
 */
public class SysParameterManage extends BaseDao{
	public Logger logger = Logger.getLogger(SysParameterManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final SysParameterManage dao = new SysParameterManage ();
	public static final SysParameterManage getInstance() {
		return dao;
	}
	
	private final String  sysParameterManageInit = "/jsp/sysbusinesparams/businesparamsmanage/sysParameterManageInit.jsp";
	private final String  sysParameterDetailURL = "/jsp/sysbusinesparams/businesparamsmanage/sysParameterDetail.jsp";
	
	/** 
	* @Title	  : sysParameterManageInit 
	* @Description: 系统可变参数表维护:页面初始化
	* @throws 
	* @LastUpdate :2010-7-5
	*/
	public void sysParameterManageInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			Long companyId = logonUser.getCompanyId();
			List<Map<String,Object>> typeNameList = SysParameterManageDAO.getAllTypeList(companyId);
			act.setOutData("typeNameList", typeNameList);
			act.setForword(sysParameterManageInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "系统可变参数表维护:页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	/** 
	* @Title	  : sysParameterList 
	* @Description: 可变参数信息列表展示
	* @throws 
	* @LastUpdate :2010-7-6
	*/
	public void sysParameterList(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String typeName = CommonUtils.checkNull(request.getParamValue("typeName"));
			String paraName = CommonUtils.checkNull(request.getParamValue("paraName"));
			Long companyId = logonUser.getCompanyId();
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SysParameterManageDAO.getSysParameterList(typeName, paraName, ActionUtil.getPageSize(request), curPage,companyId);
			act.setOutData("ps", ps);
			ActionUtil.setCustomPageSizeFlag(act, true);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "可变参数信息列表展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : sysParameterDetail 
	* @Description: 查看某参数详细信息
	* @throws 
	* @LastUpdate :2010-7-6
	*/
	public void sysParameterDetail(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String para_id = CommonUtils.checkNull(request.getParamValue("para_id"));
			Map<String, Object> sysParameter = SysParameterManageDAO.getSysParameterInfo(para_id);
			act.setOutData("sysParameter", sysParameter);
			act.setForword(sysParameterDetailURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "查看某参数详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : editSysParmSubmit 
	* @Description: 修改系统参数：提交
	* @param      : 设定文件 
	* @return     : void返回类型 
	* @throws 
	* @LastUpdate :2010-7-6
	*/
	public void editSysParmSubmit(){
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String para_value = CommonUtils.checkNull(request.getParamValue("para_value"));
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			String para_id = CommonUtils.checkNull(request.getParamValue("para_id"));
			TmBusinessParaPO tempPO = new TmBusinessParaPO();
			tempPO.setParaId(Integer.parseInt(para_id));
			TmBusinessParaPO valuePO = new TmBusinessParaPO();
			valuePO.setParaValue(para_value.trim());
			if (null != remark && !"".equals(remark)) {
				valuePO.setRemark(remark.trim());
			}
			valuePO.setUpdateBy(logonUser.getUserId());
			valuePO.setUpdateDate(new Date());
			dao.update(tempPO, valuePO);
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "修改系统参数：提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.infodms.dms.actions.sales.customerInfoManage;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("rawtypes")
public class SalesReportQuery  extends BaseDao{
	public Logger logger = Logger.getLogger(SalesReportQuery.class);
	private ActionContext act = ActionContext.getContext();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO ();
	public static final CheckVehicleDAO getInstance() {
		return dao;
	}
	private final String  salesReportQueryInitUrl = "/jsp/sales/customerInfoManage/salesReportQueryInit.jsp";
	private final String  salesReportQueryInitUrl_CVS = "/jsp/sales/customerInfoManage/salesReportQueryInit_CVS.jsp";
	private final String  salesReportDealerQueryInitUrl = "/jsp/sales/customerInfoManage/salesDealerReportQueryInit.jsp";
	/**
	 * FUNCTION		:	实销信息上报查询面初始化
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-6-7
	 */
	public void salesReportQueryInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			Date date_ = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList1 = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());
			act.setOutData("areaList", areaList1);
			act.setOutData("date", date);
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
			
			if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				act.setForword(salesReportQueryInitUrl);
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				act.setForword(salesReportQueryInitUrl_CVS);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！") ;
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报查询面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	实销信息上报查询面初始化
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-6-7
	 */
	public void salesReportDealerQueryInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			Date date_ = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(date_);
			Long poseId = logonUser.getPoseId();
			List<Map<String, Object>> areaList1 = MaterialGroupManagerDao.getDealerBusiness(poseId.toString());
			act.setOutData("areaList", areaList1);
			act.setOutData("date", date);
			act.setForword(salesReportDealerQueryInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报查询面初始化");
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

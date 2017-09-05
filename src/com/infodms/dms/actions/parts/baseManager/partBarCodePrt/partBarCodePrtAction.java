package com.infodms.dms.actions.parts.baseManager.partBarCodePrt;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.parts.baseManager.partBarCodePrt.partBarCodePrtDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderInDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 处理配件条码打印业务
 * 
 * @Date: 2013-6-16
 * 
 * @author huhcao
 * @version 1.0
 * @remark
 */
public class partBarCodePrtAction extends BaseImport {
	public Logger logger = Logger.getLogger(partBarCodePrtAction.class);
	private static final partBarCodePrtDao dao =  partBarCodePrtDao.getInstance();
	private PurchaseOrderInDao dao1 = PurchaseOrderInDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	//配件条码打印
	private static final String PART_BAR_CODE_PRT = "/jsp/parts/baseManager/partBarCodePrt/partBarCodePrt.jsp";//配件条码打印首页
	private static final String PART_SELECT_PG = "/jsp/parts/baseManager/partBarCodePrt/partSelectPg.jsp";//配件选择页面
	private static final String VENDER_SELECT_PG = "/jsp/parts/baseManager/partBarCodePrt/venderSelectPg.jsp";//供应商选择页面

	/**
	 * 
	 * @Title : 跳转至配件条码打印页面
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-6-16
	 */
	public void partBarCodePrtInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//初始的时候把仓库带出来
			List list = dao1.getPartWareHouseList(logonUser);//获取配件库房信息
			act.setOutData("wareHouses", list);
			act.setForword(PART_BAR_CODE_PRT);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件条码打印页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title : 配件查询
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-6-16
	 */
	public void partQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if ("1".equals(request.getParamValue("query"))) { // 开始查询
				String partOlcode = request.getParamValue("partolcode");// 配件编码
				String partCName = request.getParamValue("partcname");// 配件名称
				String WH_ID = request.getParamValue("WH_ID");
				if (Utility.testString(partOlcode)) {
					sb.append(" and T.PART_OLDCODE like '%" + partOlcode + "%' \n");
				}
				if (Utility.testString(partCName)) {
					sb.append(" and T.PART_CNAME like '%" + partCName + "%' \n");
				}
				if (Utility.testString(WH_ID)) {
					sb.append(" and b.WH_ID = " + WH_ID + " \n");
				}
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.getPartOLCode(Constant.PAGE_SIZE, curPage, sb.toString());
				act.setOutData("ps", ps);
			} else {
				List list = dao1.getPartWareHouseList(logonUser);//获取配件库房信息
				act.setOutData("wareHouses", list);
				act.setForword(PART_SELECT_PG);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件编码查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title : 配件供应商查询
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-6-16
	 */
	public void venderQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if ("1".equals(request.getParamValue("query"))) { // 开始查询
				String venderCode = request.getParamValue("venderCode");// 供应商编码
				String partVender = request.getParamValue("partVender");// 供应商名称
				
				String partId = request.getParamValue("partId");
				
				if (Utility.testString(venderCode)) {
					sb.append(" and md.MAKER_CODE like '%" + venderCode + "%' \n");
				}
				if (Utility.testString(partVender)) {
					sb.append(" and md.MAKER_NAME like '%" + partVender + "%' \n");
				}
				
				if (Utility.testString(partId)) {
					sb.append(" and d.part_id = " + partId + " \n");
				}
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.queryPartVender(sb.toString(), curPage, Constant.PAGE_SIZE );
				act.setOutData("ps", ps);
			} else {
				String partId = request.getParamValue("partId");
				act.setOutData("partId", partId);
				act.setForword(VENDER_SELECT_PG);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}

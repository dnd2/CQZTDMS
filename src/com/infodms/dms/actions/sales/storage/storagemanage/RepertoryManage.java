package com.infodms.dms.actions.sales.storage.storagemanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 车辆库存查询
 * @author Administrator
 *
 */
public class RepertoryManage {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final CarSubmissionQueryDao queryDao = CarSubmissionQueryDao.getInstance();
	private final String reportQuery = "/jsp/sales/storage/storagemanage/repertoryManage/repertoryQuery.jsp";


	/**
	 * 车辆库存查询初始化
	 * @author liufazhong
	 */
	public void repertoryQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			act.setForword(reportQuery);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"车辆信息查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询
	 * @author liufazhong
	 */
	public void queryResourceAmount()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));
			String packageCode = CommonUtils.checkNull(request.getParamValue("packageCode"));
			String colorName = CommonUtils.checkNull(request.getParamValue("colorName"));
			String createType = CommonUtils.checkNull(request.getParamValue("createType"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY"));//仓库名称
			String queryType =  request.getParamValue("queryType");//1为查询，否则为导出
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("groupCode", groupCode);
			map.put("materialCode", materialCode);
			map.put("modelCode", modelCode);
			map.put("packageCode", packageCode);
			map.put("colorName", colorName);
			map.put("poseId", logonUser.getPoseId());
			map.put("logonUser", logonUser);
			map.put("createType", createType);
			map.put("orgId", orgId);
			map.put("yieldly", yieldly);
			if("1".equals(queryType)){
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<Map<String, Object>> ps = queryDao.getResourceAmountRepertory(map, curPage, 5000);
				act.setOutData("ps", ps);
			}else{
				List<Map<String, Object>> maps = queryDao.exportResourceAmountRepertory(map);
				String[] head = {"仓库","车型","配置","物料编码","颜色","可用库存","锁定库存","借出库存","质损库存","实物库存"};
				String[] columns={"WAREHOUSE_NAME","MODEL_NAME","PACKAGE_NAME","MATERIAL_CODE","COLOR_NAME","RES_AMOUNT","KEEP_AMOUNT","BORROW_AMOUNT","MATTER_AMOUNT","STOCK_AMOUNT"};
				try {
					ToExcel.toReportExcel(ActionContext.getContext().getResponse(), request,"车厂库存查询.xls", head,columns,maps);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
			
			
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实时库存查询");
			logger.error(logonUser, e1);
			act.setException(e1);
			e.printStackTrace();
		}
	}
}

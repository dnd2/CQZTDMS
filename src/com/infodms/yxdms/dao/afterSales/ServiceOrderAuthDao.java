package com.infodms.yxdms.dao.afterSales;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TtAsServiceAuthContentPO;
import com.infodms.dms.po.TtAsServiceOrderPO;
import com.infodms.dms.po.TtAsServicePartPO;
import com.infodms.dms.po.TtAsServiceProjectPO;
import com.infodms.dms.po.TtAsServiceWarnDayPO;
import com.infodms.dms.po.TtAsServiceWarnNumPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.dao.IBaseDao;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ServiceOrderAuthDao extends IBaseDao<PO>{

	private static final ServiceOrderAuthDao dao = new ServiceOrderAuthDao();
	public static final ServiceOrderAuthDao getInstance(){
		if (dao == null) {
			return new ServiceOrderAuthDao();
		}
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
    
	/**
	 * 售后服务工单预授权审核查询
	 * 
	 * @param parMap
	 * @return boolean
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> serviceOrderAuthQuery(
			Map<String, Object> params, Integer pageSize, Integer curPage)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		String serviceOrderCode = CommonUtils.checkNull(params.get("serviceOrderCode"));
		String vin = CommonUtils.checkNull(params.get("vin"));
		String licenseNo = CommonUtils.checkNull(params.get("licenseNo"));
		String dealerId = CommonUtils.checkNull(params.get("dealerId"));
		String repairType = CommonUtils.checkNull(params.get("repairType"));
		String authAuditStatus = CommonUtils.checkNull(params.get("authAuditStatus"));
		
		String loginUserId = params.get("loginUserId").toString();
		String loginDealerId = params.get("loginDealerId").toString();
		
		sql.append( "SELECT A.AUTH_AUDIT_ID,\n" +
					"       A.SERVICE_ORDER_ID,\n" + 
					"       C.SERVICE_ORDER_CODE,\n" + 
					"       C.VIN,\n" + 
					"       D.LICENSE_NO,\n" + 
					"       C.DEALER_ID,\n" + 
					"       E.DEALER_NAME,\n" + 
					"       C.REPAIR_TYPE,\n" + 
					"       F_GET_TC_CODE(C.REPAIR_TYPE) REPAIR_TYPE_NAME,\n" + 
					"       A.AUTH_AUDIT_STATUS,\n" + 
					"       F_GET_TC_CODE(A.AUTH_AUDIT_STATUS) AUTH_AUDIT_STATUS_NAME,\n" + 
					"       A.AUTH_AUDIT_BY,--\n" + 
					"       NVL(F.NAME,'-/-') AUTH_AUDIT_BY_NAME,\n" + 
					"       DECODE(A.AUTH_AUDIT_DATE,NULL,'-/-',TO_CHAR(A.AUTH_AUDIT_DATE,'YYYY-MM-DD')) AUTH_AUDIT_DATE,--审核时间\n" + 
					"       TO_CHAR(A.CREATE_DATE,'YYYY-MM-DD') CREATE_DATE\n" + 
					"  FROM TT_AS_SERVICE_AUTH_AUDIT A\n" + 
					"  JOIN TC_USER B\n" + 
					"    ON A.APPROVAL_LEVEL = B.APPROVAL_LEVEL_CODE\n" + 
					"  JOIN TT_AS_SERVICE_ORDER C\n" + 
					"    ON A.SERVICE_ORDER_ID = C.SERVICE_ORDER_ID\n" + 
					"  LEFT JOIN TM_VEHICLE D\n" + 
					"    ON C.VIN = D.VIN\n" + 
					"  LEFT JOIN TM_DEALER E\n" + 
					"    ON C.DEALER_ID = E.DEALER_ID\n" + 
					"  LEFT JOIN TC_USER F\n" + 
					"    ON A.AUTH_AUDIT_BY = F.USER_ID\n" +
					" WHERE 1=1\n" +
					"   AND A.AUTH_AUDIT_STATUS IS NOT NULL\n");//预授权审核状态不为空,为空表示还没走到这一步
        
		if(!loginUserId.equals("")){
			sql.append(" AND B.USER_ID = "+loginUserId+" \n") ;  
		}
		if (!"".equals(serviceOrderCode)) {
			sql.append(" AND UPPER(C.SERVICE_ORDER_CODE) LIKE UPPER('%"+serviceOrderCode+"%') \n") ;  
		}
		if (!"".equals(vin)) {
			sql.append(" AND UPPER(C.VIN) LIKE UPPER('%"+vin+"%') \n") ;  
		}
		if (!"".equals(licenseNo)) {
			sql.append(" AND UPPER(D.LICENSE_NO) LIKE UPPER('%"+licenseNo+"%') \n") ;  
		}
		if (!"".equals(dealerId)) {
			sql.append(" AND C.DEALER_ID IN ("+dealerId+") \n"); 
		}
		if (!"".equals(repairType)) {
			sql.append(" AND C.REPAIR_TYPE = "+repairType+" \n") ;  
		}
		if (!"".equals(authAuditStatus)) {
			sql.append(" AND A.AUTH_AUDIT_STATUS = "+authAuditStatus+" \n") ;  
		}
		
		sql.append(" ORDER BY A.CREATE_DATE DESC NULLS LAST ");
		
		System.out.println("sql:"+sql);
		
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	/**
	 * 获取预授权授权历史列表
	 * 
	 * @param vin
	 * @return boolean
	 * @throws Exception
	 */  
	public List<Map<String, Object>> getServiceOrderAuthAuditList(Map<String, Object> params)
			throws Exception {
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		
		StringBuffer sql = new StringBuffer();
		sql.append( "SELECT A.AUTH_AUDIT_ID, --授权ID\n" +
					"       A.SERVICE_ORDER_ID, --售后服务工单ID\n" + 
					"       A.APPROVAL_LEVEL, --授权级别\n" + 
					"       B.APPROVAL_LEVEL_NAME, --授权级别名称\n" + 
					"       A.AUTH_AUDIT_BY, --授权人\n" + 
					"       NVL(C.NAME, '-/-') AUTH_AUDIT_BY_NAME, --授权人名称\n" + 
					"       NVL(TO_CHAR(A.AUTH_AUDIT_DATE, 'YYYY-MM-DD'), '-/-') AUTH_AUDIT_DATE, --授权时间\n" + 
					"       A.AUTH_AUDIT_REMARK, --授权备注\n" + 
					"       A.AUTH_AUDIT_STATUS, --授权状态\n" + 
					"       NVL(F_GET_TC_CODE(A.AUTH_AUDIT_STATUS), '-/-') AUTH_AUDIT_STATUS_NAME --授权状态名称\n" + 
					"  FROM TT_AS_SERVICE_AUTH_AUDIT A --售后服务工单-预授权审核表\n" + 
					"  LEFT JOIN TT_AS_WR_AUTHINFO B --索赔授权级别信息\n" + 
					"    ON A.APPROVAL_LEVEL = B.APPROVAL_LEVEL_CODE\n" + 
					"  LEFT JOIN TC_USER C --用户表\n" + 
					"    ON A.AUTH_AUDIT_BY = C.USER_ID\n" + 
					" WHERE 1 = 1\n" +
					"   AND NVL(A.AUTH_AUDIT_STATUS,"+Constant.AUTH_AUDIT_STATUS_01+") <> "+Constant.AUTH_AUDIT_STATUS_01+"\n");
		if (!"".equals(serviceOrderId)) {
			sql.append(" AND A.SERVICE_ORDER_ID = "+serviceOrderId+" \n") ;  
		}else{
			sql.append(" AND 1=2 \n") ;  
		}
		sql.append(" ORDER BY A.AUTH_AUDIT_DATE DESC \n");
	    System.out.println("sql:"+sql);
	    List<Map<String,Object>> result  = dao.pageQuery(sql.toString(), null, getFunName());
		return result;
	}
	
	/**
	 * 获取预授权内容列表
	 * 
	 * @param vin
	 * @return boolean
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> serviceOrderAuthContentQuery(
			Map<String, Object> params, Integer pageSize, Integer curPage)
			throws Exception {
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		
		StringBuffer sql = new StringBuffer();
		sql.append( "SELECT A.AUTH_CONTENT_ID,--工单预授权内容ID\n" +
					"       A.AUTH_CONTENT_TYPE,--工单预授权类型\n" + 
					"       F_GET_TC_CODE(A.AUTH_CONTENT_TYPE) AUTH_CONTENT_TYPE_NAME,--预授权类型名称\n" + 
					"       A.AUTH_CONTENT_VALUE,--预授权规则ID\n" + 
					"       B.AUTH_CONTENT_DESC--预授权规则说明\n" + 
					"  FROM TT_AS_SERVICE_AUTH_CONTENT A --售后服务工单-预授权内容表\n" + 
					"  JOIN (SELECT ID AUTH_CONTENT_VALUE, CODE_DESC AUTH_CONTENT_DESC\n" + 
					"          FROM TT_AS_WR_WOOR_LEVEL --索赔授权监控工单类型\n" + 
					"        UNION\n" + 
					"        SELECT ID, LABOUR_OPERATION_NAME\n" + 
					"          FROM TT_AS_WR_AUTHMONITORLAB --索赔授权监控工时\n" + 
					"        UNION\n" + 
					"        SELECT ID, PART_NAME\n" + 
					"          FROM TT_AS_WR_AUTHMONITORPART --索赔授权监控配件\n" + 
					"        UNION\n" + 
					"        SELECT RULE_ELEMENT, PRIOR_LEVEL\n" + 
					"          FROM TT_AS_WR_RULEMAPPING --索赔授权监控规则\n" + 
					"        UNION\n" + 
					"        SELECT ID, WAINING_REMARK\n" + 
					"          FROM TT_AS_WARNING_TIME --预警时间规则维护\n" + 
					"        UNION\n" + 
					"        SELECT WARNING_REPAIR_ID, WAINING_REMARK\n" + 
					"          FROM TT_AS_WARNING_REPAIR --三包预警规则维护\n" + 
					"        ) B\n" + 
					"    ON A.AUTH_CONTENT_VALUE = B.AUTH_CONTENT_VALUE\n" + 
					" WHERE 1 = 1");
		if (!"".equals(serviceOrderId)) {
			sql.append(" AND A.SERVICE_ORDER_ID = "+serviceOrderId+" \n") ;  
		}else{
			sql.append(" AND 1=2 \n") ;  
		}
		sql.append(" ORDER BY A.AUTH_CONTENT_TYPE ASC \n");
	    System.out.println("sql:"+sql);
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	/**
	 * 获取预授权内容列表
	 * 
	 * @param vin
	 * @return boolean
	 * @throws Exception
	 
	public List<Map<String, Object>> getServiceOrderAuthContentList(Map<String, Object> params)
			throws Exception {
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		
		StringBuffer sql = new StringBuffer();
		sql.append( "SELECT A.AUTH_CONTENT_ID,--工单预授权内容ID\n" +
					"       A.AUTH_CONTENT_TYPE,--工单预授权类型\n" + 
					"       F_GET_TC_CODE(A.AUTH_CONTENT_TYPE) AUTH_CONTENT_TYPE_NAME,--预授权类型名称\n" + 
					"       A.AUTH_CONTENT_VALUE,--预授权规则ID\n" + 
					"       B.AUTH_CONTENT_DESC--预授权规则说明\n" + 
					"  FROM TT_AS_SERVICE_AUTH_CONTENT A --售后服务工单-预授权内容表\n" + 
					"  JOIN (SELECT ID AUTH_CONTENT_VALUE, CODE_DESC AUTH_CONTENT_DESC\n" + 
					"          FROM TT_AS_WR_WOOR_LEVEL --索赔授权监控工单类型\n" + 
					"        UNION\n" + 
					"        SELECT ID, LABOUR_OPERATION_NAME\n" + 
					"          FROM TT_AS_WR_AUTHMONITORLAB --索赔授权监控工时\n" + 
					"        UNION\n" + 
					"        SELECT ID, PART_NAME\n" + 
					"          FROM TT_AS_WR_AUTHMONITORPART --索赔授权监控配件\n" + 
					"        UNION\n" + 
					"        SELECT RULE_ELEMENT, PRIOR_LEVEL\n" + 
					"          FROM TT_AS_WR_RULEMAPPING --索赔授权监控规则\n" + 
					"        UNION\n" + 
					"        SELECT ID, WAINING_REMARK\n" + 
					"          FROM TT_AS_WARNING_TIME --预警时间规则维护\n" + 
					"        UNION\n" + 
					"        SELECT WARNING_REPAIR_ID, WAINING_REMARK\n" + 
					"          FROM TT_AS_WARNING_REPAIR --三包预警规则维护\n" + 
					"        ) B\n" + 
					"    ON A.AUTH_CONTENT_VALUE = B.AUTH_CONTENT_VALUE\n" + 
					" WHERE 1 = 1");
		if (!"".equals(serviceOrderId)) {
			sql.append(" AND A.SERVICE_ORDER_ID = "+serviceOrderId+" \n") ;  
		}else{
			sql.append(" AND 1=2 \n") ;  
		}
		sql.append(" ORDER BY A.AUTH_CONTENT_TYPE ASC \n");
	    System.out.println("sql:"+sql);
	    List<Map<String,Object>> result  = dao.pageQuery(sql.toString(), null, getFunName());
		return result;
	}
	*/
	
	/**
	 * @description 售后服务工单预授权审核-保存
	 * @Date 2017-08-16
	 * @author free.AI
	 * @param void
	 * @version 1.0
	 * */
	public void serviceOrderAuthAuditSave(Map<String, Object> params) throws Exception {
		
		String authAuditId = CommonUtils.checkNull(params.get("authAuditId"));
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		String authAuditStatus = CommonUtils.checkNull(params.get("authAuditStatus"));
		String authAuditRemark = CommonUtils.checkNull(params.get("authAuditRemark"));
		String authAuditBy = CommonUtils.checkNull(params.get("authAuditBy"));
		
		String repairType = CommonUtils.checkNull(params.get("repairType"));
		String egressId = CommonUtils.checkNull(params.get("egressId"));
		String agreeMaintainCost = CommonUtils.checkNull(params.get("agreeMaintainCost"));
		
		String vin = CommonUtils.checkNull(params.get("vin"));
		
		String status = "";
		
		StringBuffer sql = new StringBuffer();
		//更新售后服务工单-预授权审核表
		sql.append( "UPDATE TT_AS_SERVICE_AUTH_AUDIT A\n" +
					"   SET A.AUTH_AUDIT_BY = ?,\n" + 
				    "       A.AUTH_AUDIT_DATE = SYSDATE,\n" +
					"       A.AUTH_AUDIT_REMARK = ?,\n" +
				    "       A.AUTH_AUDIT_STATUS = ?\n" +
					" WHERE A.AUTH_AUDIT_ID = ?\n");
		List<Object> parList = new ArrayList<Object>();
		parList.add(authAuditBy);
		parList.add(authAuditRemark);
		parList.add(authAuditStatus);
		parList.add(authAuditId);
		dao.update(sql.toString(),parList);
		
		if(authAuditStatus.equals(Constant.AUTH_AUDIT_STATUS_02.toString())){
			//如果预授权审核通过，则判断是否还需审核，如还需审核则更新下一审核级别的审核状态，否则更新售后服务工单状态为审核通过
			String approvalLevel = "";
			//得到预授权审核最小审核级别
		    sql = new StringBuffer();
			sql.append( "SELECT MIN(TO_NUMBER(A.APPROVAL_LEVEL)) APPROVAL_LEVEL\n" +
						"  FROM TT_AS_SERVICE_AUTH_AUDIT A\n" + 
						" WHERE 1 = 1\n" + 
						"   AND A.AUTH_AUDIT_STATUS IS NULL\n" + 
						"   AND A.SERVICE_ORDER_ID = "+serviceOrderId+"\n");
			Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, getFunName());
			if(map!=null&&!CommonUtils.checkNull(map.get("APPROVAL_LEVEL")).equals("")){//存在预授权审核最小级别，还需审核
				approvalLevel = CommonUtils.checkNull(map.get("APPROVAL_LEVEL"));
				status = Constant.SERVICE_ORDER_STATUS_03.toString();//预授权审核中
				//更新预授权审核表审核状态
				sql = new StringBuffer();
				sql.append( "UPDATE TT_AS_SERVICE_AUTH_AUDIT A\n" +
							"   SET A.AUTH_AUDIT_STATUS = ?\n" + 
							" WHERE A.SERVICE_ORDER_ID = ?\n" + 
							"   AND A.APPROVAL_LEVEL = ?\n");
				parList.clear();
				parList.add(Constant.AUTH_AUDIT_STATUS_01);
				parList.add(serviceOrderId);
				parList.add(approvalLevel);
				dao.update(sql.toString(),parList);
			}else{//不存在预授权审核最小级别
				status = Constant.SERVICE_ORDER_STATUS_04.toString();//审核通过
			}
		}else{//如果为审核驳回或者审核拒绝，则修改售后服务工单状态
			if(authAuditStatus.equals(Constant.AUTH_AUDIT_STATUS_03.toString())){//审核驳回
				status = Constant.SERVICE_ORDER_STATUS_05.toString();
			}else if(authAuditStatus.equals(Constant.AUTH_AUDIT_STATUS_04.toString())){//审核拒绝
				status = Constant.SERVICE_ORDER_STATUS_06.toString();
				//根据维修类型不同，更新不同数据
				ServiceOrderDao serviceOrderDao= new ServiceOrderDao();
				//外出维修、售前维修 更新外出维修单关联工单状态为未关联
				if(repairType.equals(Constant.REPAIR_TYPE_02.toString())||repairType.equals(Constant.REPAIR_TYPE_03.toString())){
					params.clear();
					params.put("egressId", egressId);
					params.put("isRlationOrder", Constant.IF_TYPE_NO);
					params.put("updateBy", authAuditBy);
					serviceOrderDao.serviceOrderEgressUpdate(params);
				}
				//PDI，更新车辆表的是否PDI为否
				if(repairType.equals(Constant.REPAIR_TYPE_08.toString())){
					params.clear();
					params.put("vin", vin);
					params.put("isPdi", Constant.IF_TYPE_NO);
					params.put("updateBy", authAuditBy);
					serviceOrderDao.vehicleIsPdiUpdate(params);
				}
			}
			//根据维修类型不同，更新不同数据
			//正常维修、外出维修 清空预警信息
			if(repairType.equals(Constant.REPAIR_TYPE_01.toString())||repairType.equals(Constant.REPAIR_TYPE_02.toString())){
				//清空预警天数记录
				TtAsServiceWarnDayPO warnDay = new TtAsServiceWarnDayPO();
				warnDay.setServiceOrderId(Long.parseLong(serviceOrderId));
				dao.delete(warnDay);
				//清空预警次数记录
				TtAsServiceWarnNumPO warnNumdelPo = new TtAsServiceWarnNumPO();
				warnNumdelPo.setServiceOrderId(Long.parseLong(serviceOrderId));
				dao.delete(warnNumdelPo);
			}
			//清空预授权内容
			TtAsServiceAuthContentPO authContentdelPo = new TtAsServiceAuthContentPO();
			authContentdelPo.setServiceOrderId(Long.parseLong(serviceOrderId));
			dao.delete(authContentdelPo);
			//清空未审核的授权记录
			sql = new StringBuffer();
			sql.append( "DELETE TT_AS_SERVICE_AUTH_AUDIT A\n" +
					    " WHERE A.SERVICE_ORDER_ID = " + serviceOrderId + "\n" +
					    "   AND NVL(A.AUTH_AUDIT_STATUS,"+Constant.AUTH_AUDIT_STATUS_01+") = "+Constant.AUTH_AUDIT_STATUS_01+"\n");
			System.out.println("sql:"+sql);
			dao.delete(sql.toString(), null);
		}
		//更新售后服务工单表状态
		sql = new StringBuffer();
		sql.append( "UPDATE TT_AS_SERVICE_ORDER A\n" +
					"   SET A.STATUS = ?,\n");
		if(repairType.equals(Constant.REPAIR_TYPE_04.toString())){//保养需更新是否同意首保金额
			sql.append( "   A.AGREE_MAINTAIN_COST = "+agreeMaintainCost+",\n" );
			//更新是否可以转索赔单
			if(agreeMaintainCost.equals(Constant.AGREE_MAINTAIN_COST_01.toString())){//同意索赔
				sql.append( "   A.IS_CAN_CLAIM = "+Constant.IF_TYPE_YES+",\n" );
			}else{
				sql.append( "   A.IS_CAN_CLAIM = "+Constant.IF_TYPE_NO+",\n" );
			}
		}
		sql.append( "       A.UPDATE_BY = ?,\n" +
				    "       A.UPDATE_DATE = SYSDATE\n" +
					" WHERE A.SERVICE_ORDER_ID = ?\n");
		parList.clear();
		parList.add(status);
		parList.add(authAuditBy);
		parList.add(serviceOrderId);
		dao.update(sql.toString(),parList);
	}
	
}

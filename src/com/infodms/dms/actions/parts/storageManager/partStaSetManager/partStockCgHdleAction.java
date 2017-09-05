package com.infodms.dms.actions.parts.storageManager.partStaSetManager;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.util.CheckUtil;
import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.storageManager.partStaSetManager.partStockCgHdleDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartChgStateDtlPO;
import com.infodms.dms.po.TtPartChgStateMainPO;
import com.infodms.dms.po.TtPartRecordPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 处理库存状态变更处理业务
 * 
 * @Date: 2013-4-24
 * 
 * @author huhcao
 * @version 1.0
 * @remark
 */
public class partStockCgHdleAction extends BaseImport {
	public Logger logger = Logger.getLogger(partStockCgHdleAction.class);
	private static final partStockCgHdleDao dao = partStockCgHdleDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	//配件来货错误处理
	private static final String PART_STOCK_SETTING = "/jsp/parts/storageManager/partStaSetManager/partStockCgHdle/partStockCgHdle.jsp";//库存状态变更处理首页

	
	/**
	 * 
	 * @Title : 跳转至库存状态变更处理页面
	 * @param :
	 * @return :
	 * @throws : LastDate : 2013-4-24
	 */
	public void partStockCgHdleInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String parentOrgId = "";//父机构（销售单位）ID
			String parentOrgCode = "";//父机构（销售单位）编码
			String companyName = ""; //制单单位
			//判断主机厂与服务商
			String comp = logonUser.getOemCompanyId();
			if (null == comp ){
				
				parentOrgId = Constant.OEM_ACTIVITIES;
				parentOrgCode = Constant.ORG_ROOT_CODE;
				companyName = dao.getMainCompanyName(parentOrgId);
			}else {
				parentOrgId = logonUser.getDealerId();
				parentOrgCode = logonUser.getDealerCode();
				companyName = dao.getDealerName(parentOrgId);
			}
			StringBuffer sbString = new StringBuffer();
			sbString.append(" AND TM.ORG_ID = '" + parentOrgId + "' ");
			List<Map<String, Object>> WHList = dao.getWareHouses(sbString.toString());
			
			act.setOutData("parentOrgId", parentOrgId);
			act.setOutData("parentOrgCode", parentOrgCode);
			act.setOutData("companyName", companyName);
			act.setOutData("WHList", WHList);
			act.setForword(PART_STOCK_SETTING);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存状态变更处理页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 查询库存状态变更信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-24
	 */
	public void partStockCgHdleSearch() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
			
			String partCode = CommonUtils.checkNull(request
					.getParamValue("partCode")); // 件号
			String partOldcode = CommonUtils.checkNull(request
					.getParamValue("partOldcode")); // 配件编码
			String partCname = CommonUtils.checkNull(request
					.getParamValue("partCname")); // 配件名称
			String handleSDate = CommonUtils.checkNull(request
					.getParamValue("handleSDate")); // 开始时间
			String handleEDate = CommonUtils.checkNull(request
					.getParamValue("handleEDate")); // 截止时间
			String changeType = CommonUtils.checkNull(request
					.getParamValue("changeType")); // 调整类型
			String businessType = CommonUtils.checkNull(request
					.getParamValue("businessType")); // 业务类型
			String remark = CommonUtils.checkNull(request
					.getParamValue("remark")); // 备注
			String isFinish = CommonUtils.checkNull(request
					.getParamValue("isFinish")); // 完成状态
			String whId = CommonUtils.checkNull(request
					.getParamValue("whId"));//仓库ID
			String maker = CommonUtils.checkNull(request
					.getParamValue("maker")); // 制单人
			
			StringBuffer sbString = new StringBuffer();
			if(null != partCode && !"".equals(partCode))
			{
				sbString.append(" AND UPPER(TD.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
			}
			if(null != partOldcode && !"".equals(partOldcode))
			{
				sbString.append(" AND UPPER(TD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
			}
			if(null != partCname && !"".equals(partCname))
			{
				sbString.append(" AND UPPER(TD.PART_CNAME) LIKE '%" + partCname.toUpperCase() + "%' ");
			}
			if(null != remark && !"".equals(remark))
			{
				sbString.append(" AND UPPER(TD.REMARK) LIKE '%" + remark.toUpperCase() + "%' ");
			}
			if(null != handleSDate && !"".equals(handleSDate))
			{
				sbString.append(" AND TO_CHAR(TD.UPDATE_DATE,'yyyy-MM-dd') >= '" + handleSDate + "' ");
			}
			if(null != handleEDate && !"".equals(handleEDate))
			{
				sbString.append(" AND TO_CHAR(TD.UPDATE_DATE,'yyyy-MM-dd') <= '" + handleEDate + "' ");
			}
			if(null != changeType && !"".equals(changeType))
			{
				sbString.append(" AND TD.CHANGE_TYPE = '" + changeType + "' ");
			}
			if(null != businessType && !"".equals(businessType))
			{
				sbString.append(" AND TD.CHANGE_REASON = '" + businessType + "' ");
			}
			if(null != parentOrgId && !"".equals(parentOrgId))
			{
				sbString.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
			}
			if(null != isFinish && !"".equals(isFinish))
			{
				if("1".equals(isFinish))
				{
					sbString.append(" AND TD.CHANGE_TYPE = '" + Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 + "' ");
				}
				sbString.append(" AND TD.STATUS = '" + isFinish + "' ");
			}
			if(null != whId && !"".equals(whId))
			{
				sbString.append(" AND TM.WH_ID = '" + whId + "' ");
			}
			if(null != maker && !"".equals(maker))
			{
				sbString.append(" AND U.NAME LIKE '%" + maker.trim() + "%' ");
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.staSetCntQuery(
					sbString.toString(), Constant.PAGE_SIZE, curPage);

			act.setOutData("ps", ps);
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询库存状态变更信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 
	 * @Title      :库存状态变更详情查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-24
	 */
	public void partStockDetailSearch() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String changeId = CommonUtils.checkNull(request.getParamValue("changeId")); // 变更单ID
			
			StringBuffer sbString = new StringBuffer();
			if(null != changeId && !"".equals(changeId))
			{
				sbString.append(" AND TD.CHANGE_ID = '" + changeId + "' ");
			}
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartStockDeatil(
					sbString.toString(), Constant.PAGE_SIZE, curPage);

			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "库存状态变更详细信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 状态变更处理
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-25
	 */
	public void commitHandleResult(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		act.getResponse().setContentType("application/json");
		RequestWrapper req = act.getRequest();
		String success = "";
		String errorExist = "";
	try {
		String parentOrgId = CommonUtils.checkNull(req.getParamValue("parentOrgId")); //制单单位ID
		String parentOrgCode = CommonUtils.checkNull(req.getParamValue("parentOrgCode")); //制单单位编码
		String chgorgCname = CommonUtils.checkNull(req.getParamValue("chgorgCname")); //制单单位名称
		String whId = CommonUtils.checkNull(req.getParamValue("whIdSel")); //仓库ID
		String whName = CommonUtils.checkNull(req.getParamValue("whNameSel")); //仓库名称
		String configId = Constant.PART_CODE_RELATION_19 + "";//配置ID
//		String partBatch = Constant.PART_RECORD_BATCH;//配件批次
//		String partVenId = Constant.PART_RECORD_VENDER_ID;//供应商ID
		int bzType1 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01;//正常
		int bzType2 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02;//盘盈
		int bzType3 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03;//盘亏
		int bzType4 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_04;//来货错误
		int bzType5 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_05;//质量问题
		int bzType6 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_06;//借条
		int bzType7 = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_07;//现场BO
		int cgType1 = Constant.PART_STOCK_STATUS_CHANGE_TYPE_01;//封存
		int cgType2 = Constant.PART_STOCK_STATUS_CHANGE_TYPE_02;//解封
		
		TtPartRecordPO insertPRPo = null;
        List ins = null;
        int inOrOutFlag = 1; ///配件出入库状态  默认：入库
        int partRecState = 1; //配件出入库记录状态  默认：正常
        
		String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
		if("".equals(curPage)){
			curPage = "1";
		}
		Long userId = logonUser.getUserId(); //操作人ID
		String name = logonUser.getName();
		Date date = new Date();
		String dtlIds = CommonUtils.checkNull(req.getParamValue("dtlIds")); //明细ID
		String dtlIdArr[] = null;
		if(null != dtlIds && !"".equals(dtlIds))
		{
			dtlIdArr = dtlIds.trim().split(",");
		}
		
		if(null != dtlIdArr)
		{
			int leth = dtlIdArr.length;
			TtPartChgStateMainPO selBMPo = null;
			TtPartChgStateMainPO updBMPo = null;
			TtPartChgStateDtlPO selBDPo = null;
			TtPartChgStateDtlPO updBDPo = null;
			long changeId = 0;
			String sbStr = "";
			List<Map<String, Object>> chgDtlList = null;
			
			for(int i = 0; i < leth; i ++)
			{
				String dtlId = dtlIdArr[i].split(":")[0];//明细ID
				String clsQty = dtlIdArr[i].split(":")[1];//处理数量
				String remark = CommonUtils.checkNull(request.getParamValue("clsRmk_"+dtlId));//处理备注
				int chgType = Integer.parseInt(request.getParamValue("chgType_"+dtlId));//调整类型
				int bzType = Integer.parseInt(request.getParamValue("chgReason_"+dtlId));//业务类型
				int cgType = cgType2; //默认解封
				//如果原单据为解封，调整类型则为封存
				if(cgType2 == chgType)
				{
					cgType = cgType1;
				}
				
				String sql = " AND TD.DTL_ID = '" + dtlId + "'";
				List<Map<String, Object>> dtlList = dao.queryPartStockDeatil(sql);
				
				if(null != dtlList && dtlList.size() == 1)
				{
					String unClsNum = dtlList.get(0).get("UNCLS_QTY").toString();//未（可）处理数量
					String closeQty = dtlList.get(0).get("COLSE_QTY").toString();//已处理数量
					changeId = Long.parseLong(dtlList.get(0).get("CHANGE_ID").toString()); //变更单ID
					
					long partId = Long.parseLong(dtlList.get(0).get("PART_ID").toString());//配件ID
					String partCode = dtlList.get(0).get("PART_CODE").toString();//配件件号
					String partOldcode = dtlList.get(0).get("PART_OLDCODE").toString();//配件编码
					String partCname = dtlList.get(0).get("PART_CNAME").toString();//配件名称
					
					String partBatch = dtlList.get(0).get("BATCH_NO").toString();//批次号
					String partVenId = dtlList.get(0).get("VENDER_ID").toString();//供应商id
					
					
					StringBuffer sb = new StringBuffer();
					sb.append(" AND LD.PART_ID = '" + partId + "' ");
					sb.append(" AND WD.WH_ID = '" + whId + "' ");
					sb.append(" AND WD.ORG_ID = '" + parentOrgId + "' ");
					sb.append(" AND CSD.DTL_ID = '" + dtlId + "' ");
					List<Map<String,Object>> warLocList = dao.getWareLocaInfos(sb.toString());
					String whCode = warLocList.get(0).get("WH_CODE").toString();
					Long locId = Long.parseLong(warLocList.get(0).get("LOC_ID").toString());
					String locCode = warLocList.get(0).get("LOC_CODE").toString();
					String locName = warLocList.get(0).get("LOC_NAME").toString();
					
					List<Map<String,Object>> partList = dao.getPartStockInfos(partOldcode, parentOrgId, whId, locId);
					
					int zcfcQty = 0;//正常封存数量
					int pkfcQty = 0;//盘亏封存数量
					int pyfcQty = 0;//盘盈封存数量
					
					if(null != partList && partList.size() > 0)
					{
						zcfcQty = Integer.parseInt(partList.get(0).get("ZCFC_QTY").toString());
						pkfcQty = Integer.parseInt(partList.get(0).get("PKFC_QTY").toString());
						pyfcQty = Integer.parseInt(partList.get(0).get("PDFC_QTY").toString());
					}
					
					long clsNum = 0;//处理数量
					long returnQty = 0;
					long canClsNum = 0;//可以处理数量
					long closedNum = 0;//已处理数量
					if(null != clsQty && !"".equals(clsQty))
					{
						clsNum = Long.parseLong(clsQty);
						returnQty = clsNum;
					}
					if(null != unClsNum && !"".equals(unClsNum))
					{
						canClsNum = Long.parseLong(unClsNum);
					}
					if(null != closeQty && !"".equals(closeQty))
					{
						closedNum = Long.parseLong(closeQty);
					}
					
					if(clsNum <= canClsNum)
					{
						if((bzType1 == bzType || bzType4 == bzType || bzType5 == bzType || bzType6 == bzType || bzType7 == bzType) && cgType2 == cgType && clsNum > zcfcQty)
						{
							errorExist = "处理数量不能大于正常封存数量： "+zcfcQty+" !";
						}
						else if(bzType2 == bzType && cgType2 == cgType && clsNum > pyfcQty)
						{
							errorExist = "处理数量不能大于盘盈封存数量： "+pyfcQty+" !";
						}
						else if(bzType3 == bzType && cgType2 == cgType && clsNum > pkfcQty)
						{
							errorExist = "处理数量不能大于盘亏封存数量： "+pkfcQty+" !";
						}
						else
						{
							selBDPo = new TtPartChgStateDtlPO();
							updBDPo = new TtPartChgStateDtlPO();
							
							selBDPo.setDtlId(Long.parseLong(dtlId));
							
							updBDPo.setColseQty(closedNum + clsNum);
							
							if(null != remark && !"".equals(remark))
							{
								updBDPo.setRemark2(remark);//处理备注
							}
							updBDPo.setUpdateBy(userId);
							updBDPo.setUpdateDate(date);
							
							if(0 == (canClsNum - clsNum))
							{
								updBDPo.setStatus(0);//已完成
							}
							
							dao.update(selBDPo, updBDPo);
							
							//正常/来货错误/质量问题/借条/现场BO  + 封存 (出入库数量为 正数)
							if((bzType1 == bzType || bzType4 == bzType || bzType5 == bzType || bzType6 == bzType || bzType7 == bzType) && cgType1 == cgType)
							{
								inOrOutFlag = 2; //出库标识
								partRecState = 1; //正常
								
								//1.配件正常出库
								insertPRPo = new TtPartRecordPO();
								
								Long recId = Long.parseLong(SequenceManager.getSequence("")); 
								insertPRPo.setRecordId(recId);
								insertPRPo.setAddFlag(inOrOutFlag);//出入库标识
								insertPRPo.setPartId(partId);
								insertPRPo.setPartCode(partCode);
								insertPRPo.setPartOldcode(partOldcode);
								insertPRPo.setPartName(partCname);
								insertPRPo.setPartBatch(partBatch);
								insertPRPo.setVenderId(Long.parseLong(partVenId));
								insertPRPo.setPartNum(returnQty);//出入库数量
								insertPRPo.setConfigId(Long.parseLong(configId));
								insertPRPo.setOrderId(changeId);//变更单ID
								insertPRPo.setLineId(Long.parseLong(dtlId));//变更单详情ID
								insertPRPo.setOrgId(Long.parseLong(parentOrgId));
								insertPRPo.setOrgCode(parentOrgCode);
								insertPRPo.setOrgName(chgorgCname);
								insertPRPo.setWhId(Long.parseLong(whId));
								insertPRPo.setWhCode(whCode);
								insertPRPo.setWhName(whName);
								insertPRPo.setLocId(locId);
								insertPRPo.setLocCode(locCode);
								insertPRPo.setLocName(locName);
								insertPRPo.setOptDate(date);
								insertPRPo.setCreateDate(date);
								insertPRPo.setPersonId(userId);
								insertPRPo.setPersonName(name);
								insertPRPo.setPartState(partRecState);//配件状态
								
								dao.insert(insertPRPo);
								
								//调用出入库逻辑
								ins = new LinkedList<Object>();
				                ins.add(0, changeId);
				                ins.add(1, configId);
				                
				                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);
				                
				                
				                inOrOutFlag = 1; //入库标识
				                partRecState = 2; //正常封存
								
								//2.配件正常封存入库
								insertPRPo = new TtPartRecordPO();
								
								Long recId1 = Long.parseLong(SequenceManager.getSequence("")); 
								insertPRPo.setRecordId(recId1);
								insertPRPo.setAddFlag(inOrOutFlag);//出入库标识
								insertPRPo.setPartId(partId);
								insertPRPo.setPartCode(partCode);
								insertPRPo.setPartOldcode(partOldcode);
								insertPRPo.setPartName(partCname);
								insertPRPo.setPartBatch(partBatch);
								insertPRPo.setVenderId(Long.parseLong(partVenId));
								insertPRPo.setPartNum(returnQty);//出入库数量
								insertPRPo.setConfigId(Long.parseLong(configId));
								insertPRPo.setOrderId(changeId);//变更单ID
								insertPRPo.setLineId(Long.parseLong(dtlId));//变更单详情ID
								insertPRPo.setOrgId(Long.parseLong(parentOrgId));
								insertPRPo.setOrgCode(parentOrgCode);
								insertPRPo.setOrgName(chgorgCname);
								insertPRPo.setWhId(Long.parseLong(whId));
								insertPRPo.setWhCode(whCode);
								insertPRPo.setWhName(whName);
								insertPRPo.setLocId(locId);
								insertPRPo.setLocCode(locCode);
								insertPRPo.setLocName(locName);
								insertPRPo.setOptDate(date);
								insertPRPo.setCreateDate(date);
								insertPRPo.setPersonId(userId);
								insertPRPo.setPersonName(name);
								insertPRPo.setPartState(partRecState);//配件状态
								
								dao.insert(insertPRPo);
								
								//调用出入库逻辑
								ins = new LinkedList<Object>();
				                ins.add(0, changeId);
				                ins.add(1, configId);
				                
				                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);
							}
							//正常/来货错误/质量问题/借条/现场BO + 解封(出入库数量为 正数)
							else if((bzType1 == bzType || bzType4 == bzType || bzType5 == bzType || bzType6 == bzType || bzType7 == bzType) && cgType2 == cgType)
							{
								inOrOutFlag = 2; //出库标识
								partRecState = 2; //正常封存
								
								//1.配件正常封存出库
								insertPRPo = new TtPartRecordPO();
								
								Long recId = Long.parseLong(SequenceManager.getSequence("")); 
								insertPRPo.setRecordId(recId);
								insertPRPo.setAddFlag(inOrOutFlag);//出入库标识
								insertPRPo.setPartId(partId);
								insertPRPo.setPartCode(partCode);
								insertPRPo.setPartOldcode(partOldcode);
								insertPRPo.setPartName(partCname);
								insertPRPo.setPartBatch(partBatch);
								insertPRPo.setVenderId(Long.parseLong(partVenId));
								insertPRPo.setPartNum(returnQty);//出入库数量
								insertPRPo.setConfigId(Long.parseLong(configId));
								insertPRPo.setOrderId(changeId);//变更单ID
								insertPRPo.setLineId(Long.parseLong(dtlId));//变更单详情ID
								insertPRPo.setOrgId(Long.parseLong(parentOrgId));
								insertPRPo.setOrgCode(parentOrgCode);
								insertPRPo.setOrgName(chgorgCname);
								insertPRPo.setWhId(Long.parseLong(whId));
								insertPRPo.setWhCode(whCode);
								insertPRPo.setWhName(whName);
								insertPRPo.setLocId(locId);
								insertPRPo.setLocCode(locCode);
								insertPRPo.setLocName(locName);
								insertPRPo.setOptDate(date);
								insertPRPo.setCreateDate(date);
								insertPRPo.setPersonId(userId);
								insertPRPo.setPersonName(name);
								insertPRPo.setPartState(partRecState);//配件状态
								
								dao.insert(insertPRPo);
								
								//调用出入库逻辑
								ins = new LinkedList<Object>();
				                ins.add(0, changeId);
				                ins.add(1, configId);
				                
				                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);
				                
				                
				                inOrOutFlag = 1; //入库标识
				                partRecState = 1; //正常
								
								//2.配件正常入库
								insertPRPo = new TtPartRecordPO();
								
								Long recId1 = Long.parseLong(SequenceManager.getSequence("")); 
								insertPRPo.setRecordId(recId1);
								insertPRPo.setAddFlag(inOrOutFlag);//出入库标识
								insertPRPo.setPartId(partId);
								insertPRPo.setPartCode(partCode);
								insertPRPo.setPartOldcode(partOldcode);
								insertPRPo.setPartName(partCname);
								insertPRPo.setPartBatch(partBatch);
								insertPRPo.setVenderId(Long.parseLong(partVenId));
								insertPRPo.setPartNum(returnQty);//出入库数量
								insertPRPo.setConfigId(Long.parseLong(configId));
								insertPRPo.setOrderId(changeId);//变更单ID
								insertPRPo.setLineId(Long.parseLong(dtlId));//变更单详情ID
								insertPRPo.setOrgId(Long.parseLong(parentOrgId));
								insertPRPo.setOrgCode(parentOrgCode);
								insertPRPo.setOrgName(chgorgCname);
								insertPRPo.setWhId(Long.parseLong(whId));
								insertPRPo.setWhCode(whCode);
								insertPRPo.setWhName(whName);
								insertPRPo.setLocId(locId);
								insertPRPo.setLocCode(locCode);
								insertPRPo.setLocName(locName);
								insertPRPo.setOptDate(date);
								insertPRPo.setCreateDate(date);
								insertPRPo.setPersonId(userId);
								insertPRPo.setPersonName(name);
								insertPRPo.setPartState(partRecState);//配件状态
								
								dao.insert(insertPRPo);
								
								//调用出入库逻辑
								ins = new LinkedList<Object>();
				                ins.add(0, changeId);
				                ins.add(1, configId);
				                
				                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);
							}
							//盘盈 + 封存(出入库数量为 正数)
							else if(bzType2 == bzType && cgType1 == cgType)
							{
								inOrOutFlag = 1; //入库标识
								partRecState = 3; //盘点封存
								
								//1.配件盘点封存入库
								insertPRPo = new TtPartRecordPO();
								
								Long recId = Long.parseLong(SequenceManager.getSequence("")); 
								insertPRPo.setRecordId(recId);
								insertPRPo.setAddFlag(inOrOutFlag);//出入库标识
								insertPRPo.setPartId(partId);
								insertPRPo.setPartCode(partCode);
								insertPRPo.setPartOldcode(partOldcode);
								insertPRPo.setPartName(partCname);
								insertPRPo.setPartBatch(partBatch);
								insertPRPo.setVenderId(Long.parseLong(partVenId));
								insertPRPo.setPartNum(returnQty);//出入库数量
								insertPRPo.setConfigId(Long.parseLong(configId));
								insertPRPo.setOrderId(changeId);//变更单ID
								insertPRPo.setLineId(Long.parseLong(dtlId));//变更单详情ID
								insertPRPo.setOrgId(Long.parseLong(parentOrgId));
								insertPRPo.setOrgCode(parentOrgCode);
								insertPRPo.setOrgName(chgorgCname);
								insertPRPo.setWhId(Long.parseLong(whId));
								insertPRPo.setWhCode(whCode);
								insertPRPo.setWhName(whName);
								insertPRPo.setLocId(locId);
								insertPRPo.setLocCode(locCode);
								insertPRPo.setLocName(locName);
								insertPRPo.setOptDate(date);
								insertPRPo.setCreateDate(date);
								insertPRPo.setPersonId(userId);
								insertPRPo.setPersonName(name);
								insertPRPo.setPartState(partRecState);//配件状态
								
								dao.insert(insertPRPo);
								
								//调用出入库逻辑
								ins = new LinkedList<Object>();
				                ins.add(0, changeId);
				                ins.add(1, configId);
				                
				                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);
							}
							//盘盈 + 解封(出入库数量为 正数)
							else if(bzType2 == bzType && cgType2 == cgType)
							{
								inOrOutFlag = 2; //出库标识
								partRecState = 3; //盘盈封存
								
								//1.配件盘盈封存出库
								insertPRPo = new TtPartRecordPO();
								
								Long recId = Long.parseLong(SequenceManager.getSequence("")); 
								insertPRPo.setRecordId(recId);
								insertPRPo.setAddFlag(inOrOutFlag);//出入库标识
								insertPRPo.setPartId(partId);
								insertPRPo.setPartCode(partCode);
								insertPRPo.setPartOldcode(partOldcode);
								insertPRPo.setPartName(partCname);
								insertPRPo.setPartBatch(partBatch);
								insertPRPo.setVenderId(Long.parseLong(partVenId));
								insertPRPo.setPartNum(returnQty);//出入库数量
								insertPRPo.setConfigId(Long.parseLong(configId));
								insertPRPo.setOrderId(changeId);//变更单ID
								insertPRPo.setLineId(Long.parseLong(dtlId));//变更单详情ID
								insertPRPo.setOrgId(Long.parseLong(parentOrgId));
								insertPRPo.setOrgCode(parentOrgCode);
								insertPRPo.setOrgName(chgorgCname);
								insertPRPo.setWhId(Long.parseLong(whId));
								insertPRPo.setWhCode(whCode);
								insertPRPo.setWhName(whName);
								insertPRPo.setLocId(locId);
								insertPRPo.setLocCode(locCode);
								insertPRPo.setLocName(locName);
								insertPRPo.setOptDate(date);
								insertPRPo.setCreateDate(date);
								insertPRPo.setPersonId(userId);
								insertPRPo.setPersonName(name);
								insertPRPo.setPartState(partRecState);//配件状态
								
								dao.insert(insertPRPo);
								
								//调用出入库逻辑
								ins = new LinkedList<Object>();
				                ins.add(0, changeId);
				                ins.add(1, configId);
				                
				                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);
				                
				                
				                inOrOutFlag = 1; //入库标识
				                partRecState = 1; //正常
								
								//2.配件正常入库
								insertPRPo = new TtPartRecordPO();
								
								Long recId1 = Long.parseLong(SequenceManager.getSequence("")); 
								insertPRPo.setRecordId(recId1);
								insertPRPo.setAddFlag(inOrOutFlag);//出入库标识
								insertPRPo.setPartId(partId);
								insertPRPo.setPartCode(partCode);
								insertPRPo.setPartOldcode(partOldcode);
								insertPRPo.setPartName(partCname);
								insertPRPo.setPartBatch(partBatch);
								insertPRPo.setVenderId(Long.parseLong(partVenId));
								insertPRPo.setPartNum(returnQty);//出入库数量
								insertPRPo.setConfigId(Long.parseLong(configId));
								insertPRPo.setOrderId(changeId);//变更单ID
								insertPRPo.setLineId(Long.parseLong(dtlId));//变更单详情ID
								insertPRPo.setOrgId(Long.parseLong(parentOrgId));
								insertPRPo.setOrgCode(parentOrgCode);
								insertPRPo.setOrgName(chgorgCname);
								insertPRPo.setWhId(Long.parseLong(whId));
								insertPRPo.setWhCode(whCode);
								insertPRPo.setWhName(whName);
								insertPRPo.setLocId(locId);
								insertPRPo.setLocCode(locCode);
								insertPRPo.setLocName(locName);
								insertPRPo.setOptDate(date);
								insertPRPo.setCreateDate(date);
								insertPRPo.setPersonId(userId);
								insertPRPo.setPersonName(name);
								insertPRPo.setPartState(partRecState);//配件状态
								
								dao.insert(insertPRPo);
								
								//调用出入库逻辑
								ins = new LinkedList<Object>();
				                ins.add(0, changeId);
				                ins.add(1, configId);
				                
				                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);
							}
							//盘亏 + 封存(出入库数量为 正数)
							else if(bzType3 == bzType && cgType1 == cgType)
							{
								//1.正常 出库
								//配件出入库记录表
								insertPRPo = new TtPartRecordPO();
								inOrOutFlag = 2;
								partRecState = 1;
								
								Long recId = Long.parseLong(SequenceManager.getSequence("")); 
								insertPRPo.setRecordId(recId);
								insertPRPo.setAddFlag(inOrOutFlag);//出入库
								insertPRPo.setPartId(partId);
								insertPRPo.setPartCode(partCode);
								insertPRPo.setPartOldcode(partOldcode);
								insertPRPo.setPartName(partCname);
								insertPRPo.setPartBatch(partBatch);
								insertPRPo.setVenderId(Long.parseLong(partVenId));
								insertPRPo.setPartNum(returnQty);//出库数量
								insertPRPo.setConfigId(Long.parseLong(configId));
								insertPRPo.setOrderId(changeId);//变更单ID
								insertPRPo.setLineId(Long.parseLong(dtlId));//变更单详情ID
								insertPRPo.setOrgId(Long.parseLong(parentOrgId));
								insertPRPo.setOrgCode(parentOrgCode);
								insertPRPo.setOrgName(chgorgCname);
								insertPRPo.setWhId(Long.parseLong(whId));
								insertPRPo.setWhCode(whCode);
								insertPRPo.setWhName(whName);
								insertPRPo.setLocId(locId);
								insertPRPo.setLocCode(locCode);
								insertPRPo.setLocName(locName);
								insertPRPo.setOptDate(date);
								insertPRPo.setCreateDate(date);
								insertPRPo.setPersonId(userId);
								insertPRPo.setPersonName(name);
								insertPRPo.setPartState(partRecState);//配件状态
								
								dao.insert(insertPRPo);
								
								//调用出入库逻辑
								ins = new LinkedList<Object>();
								ins.add(0, changeId);
				                ins.add(1, configId);
				                
				                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);
								
								//2.盘亏入库   -> 入库数量为 正数
								//配件出入库记录表
				                insertPRPo = new TtPartRecordPO();
								inOrOutFlag = 1;
								partRecState = 4; //盘亏封存标识
								
								Long recId1 = Long.parseLong(SequenceManager.getSequence("")); 
								insertPRPo.setRecordId(recId1);
								insertPRPo.setAddFlag(inOrOutFlag);//出入库
								insertPRPo.setPartId(partId);
								insertPRPo.setPartCode(partCode);
								insertPRPo.setPartOldcode(partOldcode);
								insertPRPo.setPartName(partCname);
								insertPRPo.setPartBatch(partBatch);
								insertPRPo.setVenderId(Long.parseLong(partVenId));
								insertPRPo.setPartNum(returnQty);//入库数量(正数)
								insertPRPo.setConfigId(Long.parseLong(configId));
								insertPRPo.setOrderId(changeId);//变更单ID
								insertPRPo.setLineId(Long.parseLong(dtlId));//变更单详情ID
								insertPRPo.setOrgId(Long.parseLong(parentOrgId));
								insertPRPo.setOrgCode(parentOrgCode);
								insertPRPo.setOrgName(chgorgCname);
								insertPRPo.setWhId(Long.parseLong(whId));
								insertPRPo.setWhCode(whCode);
								insertPRPo.setWhName(whName);
								insertPRPo.setLocId(locId);
								insertPRPo.setLocCode(locCode);
								insertPRPo.setLocName(locName);
								insertPRPo.setOptDate(date);
								insertPRPo.setCreateDate(date);
								insertPRPo.setPersonId(userId);
								insertPRPo.setPersonName(name);
								insertPRPo.setPartState(partRecState);//配件状态
								
								dao.insert(insertPRPo);
								
								//调用出入库逻辑
								ins = new LinkedList<Object>();
				                ins.add(0, changeId);
				                ins.add(1, configId);
				                
				                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);
							}
							//盘亏 + 解封(出入库数量为 正数)
							else if(bzType3 == bzType && cgType2 == cgType)
							{
								inOrOutFlag = 2; //出库标识
								partRecState = 4; //盘亏封存
								
								//1.配件盘盈封存出库
								insertPRPo = new TtPartRecordPO();
								
								Long recId = Long.parseLong(SequenceManager.getSequence("")); 
								insertPRPo.setRecordId(recId);
								insertPRPo.setAddFlag(inOrOutFlag);//出入库标识
								insertPRPo.setPartId(partId);
								insertPRPo.setPartCode(partCode);
								insertPRPo.setPartOldcode(partOldcode);
								insertPRPo.setPartName(partCname);
								insertPRPo.setPartBatch(partBatch);
								insertPRPo.setVenderId(Long.parseLong(partVenId));
								insertPRPo.setPartNum(returnQty);//出入库数量
								insertPRPo.setConfigId(Long.parseLong(configId));
								insertPRPo.setOrderId(changeId);//变更单ID
								insertPRPo.setLineId(Long.parseLong(dtlId));//变更单详情ID
								insertPRPo.setOrgId(Long.parseLong(parentOrgId));
								insertPRPo.setOrgCode(parentOrgCode);
								insertPRPo.setOrgName(chgorgCname);
								insertPRPo.setWhId(Long.parseLong(whId));
								insertPRPo.setWhCode(whCode);
								insertPRPo.setWhName(whName);
								insertPRPo.setLocId(locId);
								insertPRPo.setLocCode(locCode);
								insertPRPo.setLocName(locName);
								insertPRPo.setOptDate(date);
								insertPRPo.setCreateDate(date);
								insertPRPo.setPersonId(userId);
								insertPRPo.setPersonName(name);
								insertPRPo.setPartState(partRecState);//配件状态
								
								dao.insert(insertPRPo);
								
								//调用出入库逻辑
								ins = new LinkedList<Object>();
				                ins.add(0, changeId);
				                ins.add(1, configId);
				                
				                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);
				                
				                //盘亏解封入库
				                inOrOutFlag = 1; //入库标识
				                partRecState = 1; //正常
								
								//2.配件正常入库
								insertPRPo = new TtPartRecordPO();
								
								Long recId1 = Long.parseLong(SequenceManager.getSequence("")); 
								insertPRPo.setRecordId(recId1);
								insertPRPo.setAddFlag(inOrOutFlag);//出入库标识
								insertPRPo.setPartId(partId);
								insertPRPo.setPartCode(partCode);
								insertPRPo.setPartOldcode(partOldcode);
								insertPRPo.setPartName(partCname);
								insertPRPo.setPartBatch(partBatch);
								insertPRPo.setVenderId(Long.parseLong(partVenId));
								insertPRPo.setPartNum(returnQty);//出入库数量
								insertPRPo.setConfigId(Long.parseLong(configId));
								insertPRPo.setOrderId(changeId);//变更单ID
								insertPRPo.setLineId(Long.parseLong(dtlId));//变更单详情ID
								insertPRPo.setOrgId(Long.parseLong(parentOrgId));
								insertPRPo.setOrgCode(parentOrgCode);
								insertPRPo.setOrgName(chgorgCname);
								insertPRPo.setWhId(Long.parseLong(whId));
								insertPRPo.setWhCode(whCode);
								insertPRPo.setWhName(whName);
								insertPRPo.setLocId(locId);
								insertPRPo.setLocCode(locCode);
								insertPRPo.setLocName(locName);
								insertPRPo.setOptDate(date);
								insertPRPo.setCreateDate(date);
								insertPRPo.setPersonId(userId);
								insertPRPo.setPersonName(name);
								insertPRPo.setPartState(partRecState);//配件状态
								
								dao.insert(insertPRPo);
								
								//调用出入库逻辑
								ins = new LinkedList<Object>();
				                ins.add(0, changeId);
				                ins.add(1, configId);
				                
				                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);
							}
						}
					}
					else
					{
						errorExist = "处理数量不能大于可处理数量 "+canClsNum+" !";
					}
					
				}
				else
				{
					errorExist = "操作失败，请联系管理员!";
				}
				
				//判断明细是否已全部处理
				sbStr = " AND TD.CHANGE_ID = '" + changeId + "' AND TD.STATUS = '1' ";
				chgDtlList = dao.queryPartStockDeatil(sbStr);
				
				if(null != chgDtlList && chgDtlList.size() == 0)
				{
					//现场BO单主表
					selBMPo = new TtPartChgStateMainPO();
					updBMPo = new TtPartChgStateMainPO();
					
					selBMPo.setChangeId(changeId);
					
					updBMPo.setUpdateBy(userId);
					updBMPo.setUpdateDate(date);
					updBMPo.setState(0);//已完成
					
					dao.update(selBMPo, updBMPo);
				}
				success = "操作成功!";
			}
		}
		
		act.setOutData("errorExist", errorExist);//返回错误信息
		act.setOutData("success", success);
		act.setOutData("curPage", curPage); 
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"处理状态变更单异常");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 
	 * @Title :导出库存状态变更信息
	 * @param : @return
	 * @return :
	 * @throws : LastDate : 2013-4-24
	 */
	public void exportPartStockStatusExcel() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
			String changeCode = CommonUtils.checkNull(request
					.getParamValue("changeCode")); // 变更单号
			String checkSDate = CommonUtils.checkNull(request
					.getParamValue("checkSDate")); // 开始时间
			String checkEDate = CommonUtils.checkNull(request
					.getParamValue("checkEDate")); // 截止时间
			String whId = CommonUtils.checkNull(request
					.getParamValue("whId")); // 截止时间
			String businessType = CommonUtils.checkNull(request
					.getParamValue("businessType1")); // 业务类型
			String remark = CommonUtils.checkNull(request
					.getParamValue("remark1")); // 备注
			String isFinish = CommonUtils.checkNull(request
					.getParamValue("isFinish1")); // 完成状态
			
			StringBuffer sbString = new StringBuffer();
			if(null != changeCode && !"".equals(changeCode))
			{
				sbString.append(" AND UPPER(TM.CHANGE_CODE) LIKE '%" + changeCode.toUpperCase() + "%' ");
			}
			if(null != checkSDate && !"".equals(checkSDate))
			{
				sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
			}
			if(null != checkEDate && !"".equals(checkEDate))
			{
				sbString.append(" AND TO_CHAR(TM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
			}
			if(null != whId && !"".equals(whId))
			{
				sbString.append(" AND TM.WH_ID = '" + whId + "' ");
			}
			if(null != parentOrgId && !"".equals(parentOrgId))
			{
				sbString.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
			}
			if(null != businessType && !"".equals(businessType))
			{
				sbString.append(" AND TM.CHG_TYPE = '" + businessType + "' ");
			}
			if(null != remark && !"".equals(remark))
			{
				sbString.append(" AND TM.REMARK LIKE '%" + remark.trim() + "%' ");
			}
			if(null != isFinish && !"".equals(isFinish))
			{
				sbString.append(" AND TM.STATE = '" + isFinish + "' ");
			}
			String[] head = new String[10];
			head[0] = "序号";
			head[1] = "变更单号";
			head[2] = "业务类型";
			head[3] = "制单单位";
			head[4] = "制单人";
			head[5] = "制单日期";
			head[6] = "备注";
			head[7] = "完成状态";
			head[8] = "仓库";
			List<Map<String, Object>> list = dao.queryAllPartStockStatus(sbString.toString());
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[10];
						detail[0] = CommonUtils.checkNull(i+1);
						detail[1] = CommonUtils.checkNull(map
								.get("CHANGE_CODE"));
						if(null != map.get("CHG_TYPE"))
						{
							int bzType = Integer.parseInt(map.get("CHG_TYPE").toString());
							
							if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01 == bzType)
							{
								detail[2] = "正常";
							}
							else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02 == bzType)
							{
								detail[2] = "盘盈";
							}
							else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03 == bzType)
							{
								detail[2] = "盘亏";
							}
							else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_04 == bzType)
							{
								detail[2] = "来货错误";
							}
							else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_05 == bzType)
							{
								detail[2] = "质量问题";
							}
							else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_06 == bzType)
							{
								detail[2] = "借条";
							}
							else
							{
								detail[2] = "现场BO";
							}
						}
						detail[3] = CommonUtils
								.checkNull(map.get("CHGORG_CNAME"));
						detail[8] = CommonUtils
								.checkNull(map.get("WH_CNAME"));
						detail[4] = CommonUtils.checkNull(map
								.get("NAME"));
						detail[5] = CommonUtils.checkNull(map
								.get("CREATE_DATE"));
						detail[6] = CommonUtils
							.checkNull(map.get("REMARK"));
						if(null != map.get("STATE") && "1".equals(map.get("STATE").toString()))
						{
							detail[7] = "未完成";
						}
						else
						{
							detail[7] = "已完成";
						}
						list1.add(detail);
					}
				}
			}
			
			String fileName = "库存状态变更信息";
			this.exportEx(fileName, ActionContext.getContext().getResponse(),
					request, head, list1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "导出库存状态变更信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 汇总查询导出
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-7-1
	 */
	public void exportStaSetDetExcel() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
			String partCode = CommonUtils.checkNull(request
					.getParamValue("partCode")); // 件号
			String partOldcode = CommonUtils.checkNull(request
					.getParamValue("partOldcode")); // 配件编码
			String partCname = CommonUtils.checkNull(request
					.getParamValue("partCname")); // 配件名称
			String handleSDate = CommonUtils.checkNull(request
					.getParamValue("handleSDate")); // 开始时间
			String handleEDate = CommonUtils.checkNull(request
					.getParamValue("handleEDate")); // 截止时间
			String changeType = CommonUtils.checkNull(request
					.getParamValue("changeType")); // 调整类型
			String businessType = CommonUtils.checkNull(request
					.getParamValue("businessType2")); // 业务类型
			String remark = CommonUtils.checkNull(request
					.getParamValue("remark2")); // 备注
			String isFinish = CommonUtils.checkNull(request
					.getParamValue("isFinish2")); // 完成状态
			String whId = CommonUtils.checkNull(request
					.getParamValue("whId"));//仓库ID
			
			StringBuffer sbString = new StringBuffer();
			if(null != partCode && !"".equals(partCode))
			{
				sbString.append(" AND UPPER(TD.PART_CODE) LIKE '%" + partCode.toUpperCase() + "%' ");
			}
			if(null != partOldcode && !"".equals(partOldcode))
			{
				sbString.append(" AND UPPER(TD.PART_OLDCODE) LIKE '%" + partOldcode.toUpperCase() + "%' ");
			}
			if(null != partCname && !"".equals(partCname))
			{
				sbString.append(" AND UPPER(TD.PART_CNAME) LIKE '%" + partCname.toUpperCase() + "%' ");
			}
			if(null != remark && !"".equals(remark))
			{
				sbString.append(" AND UPPER(TD.REMARK) LIKE '%" + remark.toUpperCase() + "%' ");
			}
			if(null != handleSDate && !"".equals(handleSDate))
			{
				sbString.append(" AND TO_CHAR(TD.UPDATE_DATE,'yyyy-MM-dd') >= '" + handleSDate + "' ");
			}
			if(null != handleEDate && !"".equals(handleEDate))
			{
				sbString.append(" AND TO_CHAR(TD.UPDATE_DATE,'yyyy-MM-dd') <= '" + handleEDate + "' ");
			}
			if(null != changeType && !"".equals(changeType))
			{
				sbString.append(" AND TD.CHANGE_TYPE = '" + changeType + "' ");
			}
			if(null != businessType && !"".equals(businessType))
			{
				sbString.append(" AND TD.CHANGE_REASON = '" + businessType + "' ");
			}
			if(null != parentOrgId && !"".equals(parentOrgId))
			{
				sbString.append(" AND TM.CHGORG_ID = '" + parentOrgId + "' ");
			}
			if(null != isFinish && !"".equals(isFinish))
			{
				sbString.append(" AND TD.STATUS = '" + isFinish + "' ");
			}
			if(null != whId && !"".equals(whId))
			{
				sbString.append(" AND TM.WH_ID = '" + whId + "' ");
			}
			
			String[] head = new String[20];
			head[0] = "序号";
			head[1] = "件号";
			head[2] = "配件编码";
			head[3] = "配件名称";
			head[4] = "可用库存";
			head[5] = "业务类型";
			head[6] = "调整方式";
			head[7] = "调整数量";
			head[8] = "创建日期";
			head[9] = "备注(原因)";
			head[10] = "已处理数量";
			head[11] = "可处理数量";
			head[12] = "处理日期";
			head[13] = "处理原因";
			head[14] = "完成状态";
			head[15] = "仓库";
			List<Map<String, Object>> list = dao.staSetCntQuyList(sbString.toString());
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					if (map != null && map.size() != 0) {
						String[] detail = new String[20];
						detail[0] = CommonUtils.checkNull(i+1);
						detail[1] = CommonUtils.checkNull(map.get("PART_CODE"));
						detail[2] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
						detail[3] = CommonUtils.checkNull(map.get("PART_CNAME"));
						detail[4] = CommonUtils.checkNull(map.get("NORMAL_QTY"));
						int bzType = Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01;
						int cgType = Constant.PART_STOCK_STATUS_CHANGE_TYPE_01;
						Object bzTypeStr = map.get("CHANGE_REASON");
						Object bgTypeStr = map.get("CHANGE_TYPE");
						if(null != bzTypeStr && !"".equals(bzTypeStr))
						{
							bzType = Integer.parseInt(bzTypeStr.toString());
						}
						if(null != bgTypeStr && !"".equals(bgTypeStr))
						{
							cgType = Integer.parseInt(bgTypeStr.toString());
						}
						
						if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_01 == bzType)
						{
							detail[5] = "正常";
						}
						else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02 == bzType)
						{
							detail[5] = "盘盈";
						}
						else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03 == bzType)
						{
							detail[5] = "盘亏";
						}
						else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_04 == bzType)
						{
							detail[5] = "来货错误";
						}
						else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_05 == bzType)
						{
							detail[5] = "质量问题";
						}
						else if(Constant.PART_STOCK_STATUS_BUSINESS_TYPE_06 == bzType)
						{
							detail[5] = "借条";
						}
						else
						{
							detail[5] = "现场BO";
						}
						
						if(Constant.PART_STOCK_STATUS_CHANGE_TYPE_01 == cgType)
						{
							detail[6] = "封存";
						}
						else
						{
							detail[6] = "解封";
						}
						detail[7] = CommonUtils.checkNull(map.get("RETURN_QTY"));
						detail[8] = CommonUtils.checkNull(map.get("CREATE_DATE_FM"));
						detail[9] = CommonUtils.checkNull(map.get("REMARK"));
						detail[10] = CommonUtils.checkNull(map.get("COLSE_QTY"));
						detail[11] = CommonUtils.checkNull(map.get("UNCLS_QTY"));
						detail[12] = CommonUtils.checkNull(map.get("UPDATE_DATE_FM"));
						detail[13] = CommonUtils.checkNull(map.get("REMARK2"));
						if(null != map.get("STATUS") && "1".equals(map.get("STATUS").toString()))
						{
							detail[14] = "未完成";
						}
						else
						{
							detail[14] = "已完成";
						}
						detail[15] = CommonUtils.checkNull(map.get("WH_CNAME"));
						
						list1.add(detail);
					}
				}
			}
			
			String fileName = "库存状态变更汇总信息";
			this.exportEx(fileName, ActionContext.getContext().getResponse(),
					request, head, list1);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "导出库存状态变更汇总信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title      : 文件导出为xls文件
	 * @param      : @param response
	 * @param      : @param request
	 * @param      : @param head
	 * @param      : @param list
	 * @param      : @return
	 * @param      : @throws Exception      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-24
	 */
	public static Object exportEx(String fileName, ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = fileName + ".xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
			response.addHeader("Content-disposition", "attachment;filename="
					+ URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize = list.size() / 30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
						/*ws.addCell(new Label(i, z, str[i]));*/ //modify by yuan
                     if(CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])){
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
                    }else{
                        ws.addCell(new Label(i, z, str[i]));
                    }
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
}

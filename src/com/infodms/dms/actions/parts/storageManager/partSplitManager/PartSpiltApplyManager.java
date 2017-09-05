package com.infodms.dms.actions.parts.storageManager.partSplitManager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.storageManager.partSplitManager.PartSpiltApplyDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtPartLoactionDefinePO;
import com.infodms.dms.po.TtPartOemReturnDtlPO;
import com.infodms.dms.po.TtPartOemReturnMainPO;
import com.infodms.dms.po.TtPartSpcpMainPO;
import com.infodms.dms.po.TtPartSplitCompoundDtlPO;
import com.infodms.dms.po.TtPartSplitDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : PartSpiltApplyManager 
 * @Description   : 配件拆合件申请 
 * @author        : chenjunjiang
 * CreateDate     : 2013-5-3
 */
public class PartSpiltApplyManager implements PTConstants {

	public Logger logger = Logger.getLogger(PartSpiltApplyManager.class);
	private PartSpiltApplyDao dao = PartSpiltApplyDao.getInstance();
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 配件拆合件申请查询初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
	 */
	public void queryPartSplitApplyInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(PART_SPLIT_APPLY_QUERY_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件拆合件申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询配件拆合申请 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
	 */
	public void queryPartSpiltApplyInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
      try {
			String spcpdCode = CommonUtils.checkNull(request.getParamValue("SPCPD_CODE"));//拆合单号
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartSpiltApplyList(spcpdCode,startDate,endDate,logonUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件拆合申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 配件拆合申请新增初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
	 */
	public void addPartSpiltApplyInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String spcpdCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_23);//拆合申请单号
			
			long orgId=0;//制单单位id
			String createOrgName="";//制单单位名称
			String createOrgCode="";//制单单位编码
			TmOrgPO tmOrgPO = new TmOrgPO();
			orgId = logonUser.getOrgId();
			tmOrgPO.setOrgId(orgId);
			tmOrgPO = (TmOrgPO) dao.select(tmOrgPO).get(0);
			createOrgName = tmOrgPO.getOrgName();
			createOrgCode = tmOrgPO.getOrgCode();
			String createName = logonUser.getName();//制单人
			List list = dao.getPartWareHouseList(logonUser);//获取当前机构的库房信息
			
			act.setOutData("wareHouses", list);
			act.setOutData("createName", createName);
			act.setOutData("spcpdCode", spcpdCode);
			act.setOutData("orgId", orgId);
			act.setOutData("createOrgCode", createOrgCode);
			act.setOutData("createOrgName", createOrgName);
			act.setForword(PART_SPLIT_APPLY_ADD_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"配件拆合申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 通过仓库id查询配件信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
	 */
	public void queryPartInfoByWhId(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
      try {
			String whId = CommonUtils.checkNull(request.getParamValue("whId"));//仓库id
			String spcpdType = CommonUtils.checkNull(request.getParamValue("spcpdType"));//拆合类型
			String partOldCode = CommonUtils.checkNull(request.getParamValue("partOldCode"));//配件编码
			String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));//配件名称
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartByWhIdList(whId,spcpdType,logonUser,partOldCode,partCname,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description:查询分总成件信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-4
	 */
	public void querySubPartInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
      try {
			String partId = CommonUtils.checkNull(request.getParamValue("PART_ID"));//总成件id
			String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//总成件名称
		    String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//仓库id
		    
		    PageResult<Map<String, Object>> ps = new PageResult<Map<String,Object>>();
		    
		    if(!"".equals(partId)){
		    	//查询当前总成件有没有设置拆分比列
			    TtPartSplitDefinePO splitDefinePO = new TtPartSplitDefinePO();
			    splitDefinePO.setPartId(CommonUtils.parseLong(partId));
			    splitDefinePO.setState(Constant.STATUS_ENABLE);
			    splitDefinePO.setStatus(1);
			    
			    List<TtPartSplitDefinePO> list = dao.select(splitDefinePO);
			    if(list.size()==0){//如果没有设置拆分比例
			    	act.setOutData("errorMsg", "请为当前所选总成件设置拆分比例!");
			    }else{
			    	for(int i=0;i<list.size();i++){
			    		TtPartSplitDefinePO po = list.get(i);
			    		 //判断当前分总成件是否有对应货位信息 add by yuan 20130515
	                    TtPartLoactionDefinePO locPo = new TtPartLoactionDefinePO();
	                    locPo.setWhId(Long.valueOf(whId));
	                    locPo.setPartId(po.getSubpartId());
	                    locPo.setStatus(1);
	                    locPo.setState(Constant.STATUS_ENABLE);

	                    List<TtPartLoactionDefinePO> ll =  dao.select(locPo);
	                    if(ll.size()==0){
	                        //无货位则为该配件插入一条货位信息
	                        locPo.setLocId(CommonUtils.parseLong(SequenceManager.getSequence("")));
	                        locPo.setLocCode("暂无");
	                        locPo.setLocName("暂无");
	                        locPo.setOrgId(logonUser.getOrgId());
	                        locPo.setCreateDate(new Date());
	                        locPo.setCreateBy(-1l);
	                        dao.insert(locPo);

	                    }
			    	}
			    	
			    	//分页方法 begin
					Integer curPage = request.getParamValue("curPage") != null ? Integer
							.parseInt(request.getParamValue("curPage"))
							: 1; // 处理当前页
					ps = dao.querySubpartList(partId,whId,curPage,Constant.PAGE_SIZE);
					//分页方法 end
			    }
		    	
		    }else{ //如果传过来的总成件id为空,那就证明已经重新选择了仓库,那就要清空页面的分总成件信息
		    	act.setOutData("errorMsg", "请选择总成件编码!");
		    }
		    
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"分总成件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 保存拆合件申请信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-5
	 */
	public void saveApply(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			TtPartSpcpMainPO mainPO = new TtPartSpcpMainPO();
			mainPO.setSpcpdId(CommonUtils.parseLong(SequenceManager.getSequence("")));
			
			String spcpdCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_23);//拆合申请单号
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));//制单单位id
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));//制单单位编码
			String orgName = CommonUtils.checkNull(request.getParamValue("orgName"));//制单单位名称
			String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房id
			String whName = CommonUtils.checkNull(request.getParamValue("whName"));//库房名称
			String partId = CommonUtils.checkNull(request.getParamValue("PART_ID"));//总成件id
			String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//总成件件号
			String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//总成件编码
			String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//总成件名称
			String spcpdType = CommonUtils.checkNull(request.getParamValue("SPCPD_TYPE"));//拆合类型
			String qty = CommonUtils.checkNull(request.getParamValue("QTY"));//拆合数量
			String normalQty = CommonUtils.checkNull(request.getParamValue("NORMAL_QTY"));//库存数量
			String unit = CommonUtils.checkNull(request.getParamValue("UNIT"));//规格
			String locid = CommonUtils.checkNull(request.getParamValue("LOC_ID"));//货位id
			String loccode = CommonUtils.checkNull(request.getParamValue("LOC_CODE"));//货位编码
			String remark = CommonUtils.checkNull(request.getParamValue("REMARK"));//备注
			
			mainPO.setSpcpdCode(spcpdCode);
			mainPO.setSpcpdType(CommonUtils.parseInteger(spcpdType));
			mainPO.setOrgId(CommonUtils.parseLong(orgId));
			mainPO.setOrgCode(orgCode);
			mainPO.setOrgCname(orgName);
			mainPO.setWhId(CommonUtils.parseLong(whId));
			mainPO.setWhCname(whName);
			mainPO.setPartId(CommonUtils.parseLong(partId));
			mainPO.setPartCode(partCode);
			mainPO.setPartOldcode(partOldCode);
			mainPO.setPartName(partCname);
			mainPO.setNormalQty(CommonUtils.parseLong(normalQty));
			mainPO.setQty(CommonUtils.parseLong(qty));
			mainPO.setUnit(unit);
			mainPO.setLocId(CommonUtils.parseLong(locid));
			mainPO.setLocCode(loccode);
			mainPO.setRemark(remark);
			mainPO.setCreateDate(new Date());
			mainPO.setCreateBy(logonUser.getUserId());
			mainPO.setState(Constant.PART_SPCPD_STATUS_01);//审核中
			
			dao.insert(mainPO);
			String[] subPartIds = request.getParamValues("SUBPART_ID");//分总成件id
			if(subPartIds!=null&&subPartIds.length>1){
				for(int i=0;i<subPartIds.length;i++){
					TtPartSplitCompoundDtlPO dtlPO = new TtPartSplitCompoundDtlPO();
					dtlPO.setDtlId(CommonUtils.parseLong(SequenceManager.getSequence("")));
					dtlPO.setSpcpdId(mainPO.getSpcpdId());
					dtlPO.setLineNo((long)(i+1));
					dtlPO.setPartId(CommonUtils.parseLong(subPartIds[i]));
					
					String subPartCode = CommonUtils.checkNull(request.getParamValue("SUBPART_CODE"+subPartIds[i]));//分总成件件号
					String subPartOldCode = CommonUtils.checkNull(request.getParamValue("SUBPART_OLDCODE"+subPartIds[i]));//分总成件编码
					String subPartCname = CommonUtils.checkNull(request.getParamValue("SUBPART_CNAME"+subPartIds[i]));//分总成件名称
					String subUnit = CommonUtils.checkNull(request.getParamValue("SUBUNIT"+subPartIds[i]));//分总成件单位
					String splitNum = CommonUtils.checkNull(request.getParamValue("SPLIT_NUM"+subPartIds[i]));//拆分数量
					//String costRate = CommonUtils.checkNull(request.getParamValue("COST_RATE"+subPartIds[i]));//成本比例
					String subNormalQty = CommonUtils.checkNull(request.getParamValue("SUBNORMAL_QTY"+subPartIds[i]));//分总成件库存数量
					String subQty = CommonUtils.checkNull(request.getParamValue("SUBQTY"+subPartIds[i]));//分总成件数量
					String subLocid = CommonUtils.checkNull(request.getParamValue("SUBLOC_ID"+subPartIds[i]));//分总成件货位id
					String subLoccode = CommonUtils.checkNull(request.getParamValue("SUBLOC_CODE"+subPartIds[i]));//分总成件货位编码
					String subRemark = CommonUtils.checkNull(request.getParamValue("SUBREMARK"+subPartIds[i]));//备注
					
					dtlPO.setPartCode(subPartCode);
					dtlPO.setPartOldcode(subPartOldCode);
					dtlPO.setPartCname(subPartCname);
					dtlPO.setUnit(subUnit);
					dtlPO.setSplitQty(CommonUtils.parseInteger(splitNum));
					//dtlPO.setSplitRate(CommonUtils.parseDouble(costRate));
					dtlPO.setSplitRate(0.0);
					dtlPO.setNormalQty(CommonUtils.parseLong(subNormalQty));
					dtlPO.setQty(CommonUtils.parseLong(subQty));
					dtlPO.setLocId(CommonUtils.parseLong(subLocid));
					dtlPO.setLocCode(subLoccode);
					dtlPO.setRemark(subRemark);
					dtlPO.setCreateDate(new Date());
					dtlPO.setCreateBy(logonUser.getUserId());
					
					dao.insert(dtlPO);
				}
				
				//如果拆合类型是拆件,那么就需要先占用当前总成件的库存数量
				if(CommonUtils.parseInteger(spcpdType).equals(Constant.PART_SPCPD_TYPE_01)){
					//调用库存占用逻辑
		            List ins = new LinkedList<Object>();
		            ins.add(0, mainPO.getSpcpdId());
		            ins.add(1, Constant.PART_CODE_RELATION_23);
		            ins.add(2,1);// 1:占用 0：释放占用
		            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
				}
				
				//如果拆合类型是合件,那么就需要先占用当前分总成件的库存数量
				if(CommonUtils.parseInteger(spcpdType).equals(Constant.PART_SPCPD_TYPE_02)){
					//调用库存占用逻辑
		            List ins = new LinkedList<Object>();
		            ins.add(0, mainPO.getSpcpdId());
		            ins.add(1, Constant.PART_CODE_RELATION_27);
		            ins.add(2,1);// 1:占用 0：释放占用
		            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
				}
			}
			
			act.setOutData("success", "提交成功!");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"配件拆合申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 拆合申请明细查询初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-5
	 */
	public void querySpiltApplyDetailInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String spcpdId = CommonUtils.checkNull(request.getParamValue("spcpdId"));
			String flag = CommonUtils.checkNull(request.getParamValue("flag"));//是否被驳回
			String flag1 = CommonUtils.checkNull(request.getParamValue("flag1"));
			Map map = dao.getPartSpcpMainInfo(spcpdId);
			request.setAttribute("po", map);
			request.setAttribute("flag", flag);
			request.setAttribute("flag1", flag1);
			act.setForword(PART_RETURN_APPLY_DETAIL_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件拆合申请明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 通过拆合单id获取分总成件信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-5
	 */
	public void querySubPartInfoBySpcpdId(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
      try {
			String spcpdId = CommonUtils.checkNull(request.getParamValue("spcpdId"));//拆合单id
		    
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.querySubpartBySpcpdIdList(spcpdId,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"分总成件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
}

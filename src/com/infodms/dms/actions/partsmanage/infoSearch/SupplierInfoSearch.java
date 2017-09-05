package com.infodms.dms.actions.partsmanage.infoSearch;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PartinfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.partinfo.PartinfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmPtPartSupRelationPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: SupplierInfoSearch.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-5
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class SupplierInfoSearch implements PTConstants {
	public Logger logger = Logger.getLogger(SupplierInfoSearch.class);
	private PartinfoDao dao = new PartinfoDao();
	/**
	 * 初始化
	 */
	public void supplierInfoSearchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(ssinitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"供应商信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 分页查询供应商
	 */
	public void querySupplierInfoSearch(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String supplierCode = CommonUtils.checkNull(request.getParamValue("SUPPLIER_CODE"));//供应商代码
			String supplierName = CommonUtils.checkNull(request.getParamValue("SUPPLIER_NAME"));//供应商名称
			if(!supplierCode.equals("")){//截串加单引号
				String[] supp = supplierCode.split(",");
				supplierCode = "";
				for(int i=0;i<supp.length;i++){
					supp[i] = "'"+supp[i]+"'";
					if(!supplierCode.equals("")){
						supplierCode += "," + supp[i];
					}else{
						supplierCode = supp[i];
					}
				}
			}
			PartinfoBean bean = new PartinfoBean();
			bean.setSupplierCode(supplierCode);
			bean.setSupplierName(supplierName);
			
			PartinfoDao dao = new PartinfoDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.querySupplierInfoList(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"供应商信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: addSupplier 
	* @Description: TODO(增加配件和供应商的关系) 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void addSupplier(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String[] supplierIds = request.getParamValues("sp");//供应商Id
			String partId = CommonUtils.checkNull(request.getParamValue("partId"));//配件Id
			if (null != supplierIds && supplierIds.length > 0) {
				for (String supplierId : supplierIds) {
					TmPtPartSupRelationPO tpo = new TmPtPartSupRelationPO();
					tpo.setSupplierId(Long.parseLong(supplierId));
					tpo.setOrderId(Long.parseLong(partId));
					List<TmPtPartSupRelationPO> tpos = dao.select(tpo);
					if (null != tpos && tpos.size() > 0) {
						continue;
					}
					TmPtPartSupRelationPO po = new TmPtPartSupRelationPO();
					po.setSupplierId(Long.parseLong(supplierId));
					po.setOrderId(Long.parseLong(partId));
					po.setCreateBy(logonUser.getUserId());
					po.setCreateDate(new Date());
					po.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
					dao.insert(po);
				}
				Map<String, Object> dealerInfo = dao.getPartDetail(partId);//配件详细
				act.setOutData("dealerInfo", dealerInfo);
				act.setForword(infoUrlMod);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"供应商信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: delRelation 
	* @Description: TODO(删除配件和供应商关系) 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void delRelation() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String relationId = CommonUtils.checkNull(request.getParamValue("relationId")); //配件Id
			if (Utility.testString(relationId)) {
				TmPtPartSupRelationPO po = new TmPtPartSupRelationPO();
				po.setRelationId(Long.parseLong(relationId));
				PartinfoDao dao = new PartinfoDao();
				dao.delete(po);
				logger.info("删除配件和供应商关系...");
			}
		} catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件基本信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}

package com.infodms.dms.actions.sysbusinesparams.businesparamsmanage;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysproduct.productmanage.MaterialGroupManage;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sysbusinesparams.businesparamsmanage.BusinessAreaManageDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmAreaGroupPO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TtAsWrProductPackagePO;
import com.infodms.dms.po.TtProductDistributionPO;
import com.infodms.dms.po.TtProductMaterialPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**   
 * @Title  : BusinessAreaManage.java
 * @Package: com.infodms.dms.actions.sysbusinesparams.businesparamsmanage
 * @Description: 业务范围维护
 * @date   : 2010-7-5 
 * @version: V1.0   
 */
public class BusinessAreaManage extends BaseDao{
	public Logger logger = Logger.getLogger(BusinessAreaManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private static final BusinessAreaManage dao = new BusinessAreaManage ();
	public static final BusinessAreaManage getInstance() {
		return dao;
	}
	
	
	private final String  businessAreaManageInit = "/jsp/sysbusinesparams/businesparamsmanage/businessAreaManageInit.jsp";
	private final String  toEditBusinessAreaInfoURL = "/jsp/sysbusinesparams/businesparamsmanage/toEditBusinessAreaInfo.jsp";
	private final String  toAddBusinessInit = "/jsp/sysbusinesparams/businesparamsmanage/toAddBusiness.jsp";
	private final String  toAddBusAreaURL = "/jsp/sysbusinesparams/businesparamsmanage/toAddBusArea.jsp";
	
	/** 
	* @Title	  : businessAreaManageInit 
	* @Description: 业务范围维护页面初始化
	* @throws 
	* @LastUpdate :2010-7-5
	*/
	public void businessAreaManageInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.setForword(businessAreaManageInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "业务范围维护页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	/** 
	* @Title	  : businessAreaList 
	* @Description: 业务范围列表
	* @throws 
	* @LastUpdate :2010-7-5
	*/
	public void businessAreaList(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String area_code =  CommonUtils.checkNull(request.getParamValue("area_code"));
			String area_name =  CommonUtils.checkNull(request.getParamValue("area_name"));
			String produce_base=CommonUtils.checkNull(request.getParamValue("produce_base"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			Long companyId=logonUser.getCompanyId();
			PageResult<Map<String, Object>> ps = BusinessAreaManageDAO.getbusinessAreaList(produce_base,area_code, area_name, Constant.PAGE_SIZE, curPage,companyId);
			act.setOutData("ps", ps);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "业务范围列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : toEditBusinessAreaInfo 
	* @Description:修改业务范围PRE 
	* @param      : 设定文件 
	* @return     : void返回类型 
	* @throws 
	* @LastUpdate :2010-7-5
	*/
	public void toEditBusinessAreaInfo(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String area_id = CommonUtils.checkNull(request.getParamValue("area_id"));
			String area_short = CommonUtils.checkNull(request.getParamValue("up_area_short"));
			String area_code = CommonUtils.checkNull(request.getParamValue("up_area_code"));
			String area_name = CommonUtils.checkNull(request.getParamValue("up_area_name"));
			String status = CommonUtils.checkNull(request.getParamValue("up_status"));
			String produce_base = CommonUtils.checkNull(request.getParamValue("up_produce_base"));
			
			if (null != area_id && !"".equals(area_id) && null != area_code && !"".equals(area_code) && null != area_name && !"".equals(area_name) && null != area_short && !"".equals(area_short)) {
				act.setOutData("area_id", area_id);
				act.setOutData("area_code", area_code);
				act.setOutData("area_name", area_name);
				act.setOutData("area_short", area_short);
				act.setOutData("status", status);
				act.setOutData("produce_base", produce_base);
				act.setForword(toEditBusinessAreaInfoURL);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "修改业务范围PRE ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : getBusinessAreaDetailInfo 
	* @Description: 业务范围详细信息
	* @throws 
	* @LastUpdate :2010-7-5
	*/
	public void getBusinessAreaDetailInfo(){
		AclUserBean logonUser = null;
		try {
			String area_id = CommonUtils.checkNull(request.getParamValue("area_id"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = BusinessAreaManageDAO.getbusinessAreaDetailList(area_id, Constant.PAGE_SIZE_MAX, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "业务范围详细信息 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : toAddBusiness 
	* @Description: 新增业务范围PRE
	* @param      : 设定文件 
	* @return     : void返回类型 
	* @throws 
	* @LastUpdate :2010-7-5
	*/
	public void toAddBusiness(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.setForword(toAddBusinessInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "新增业务范围PRE ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : getMaterialGroupList 
	* @Description: 得到物料组列表 
	* @throws 
	* @LastUpdate :2010-7-6
	*/
	public void getMaterialGroupList(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String area_id = CommonUtils.checkNull(request.getParamValue("area_id"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = BusinessAreaManageDAO.getMaterialGroupList(area_id,groupCode ,groupName,Constant.PAGE_SIZE, curPage);
			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "得到物料组列表 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : addMaterialGroup 
	* @Description: 对某业务范围添加物料组
	* @throws 
	* @LastUpdate :2010-7-6
	*/
	public void addMaterialGroup(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String area_id = CommonUtils.checkNull(request.getParamValue("area_id"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			
			String[] groupIds = groupId.split(",");
			for (int i = 0; i < groupIds.length; i++) {
				TmAreaGroupPO areaGroupPO = new TmAreaGroupPO();
				areaGroupPO.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
				areaGroupPO.setMaterialGroupId(Long.parseLong(groupIds[i]));
				areaGroupPO.setAreaId(Long.parseLong(area_id));
				areaGroupPO.setCreateBy(logonUser.getUserId());
				areaGroupPO.setCreateDate(new Date());
				dao.insert(areaGroupPO);
			}
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "对某业务范围添加物料组 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-24
	public void addMaterialGroup111(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String area_id = CommonUtils.checkNull(request.getParamValue("area_id"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			
			String[] groupIds = groupId.split(",");
			for (int i = 0; i < groupIds.length; i++) {
				TtProductMaterialPO tpmPO = new TtProductMaterialPO();
				tpmPO.setId(Long.parseLong(SequenceManager.getSequence("")));
				tpmPO.setMaterialId(Long.parseLong(groupIds[i]));
				tpmPO.setProductId(Long.parseLong(area_id));
				tpmPO.setCreateBy(logonUser.getUserId());
				tpmPO.setCreateDate(new Date());
				dao.insert(tpmPO);
			}
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "对某业务范围添加物料组 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : deleteMaterialGroup 
	* @Description: 删除某业务范围对应的物料信息
	* @throws 
	* @LastUpdate :2010-7-6
	*/
	public void deleteMaterialGroup(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String relationId = CommonUtils.checkNull(request.getParamValue("relationId"));
			String[] relationIds = relationId.split(",");
			for (int i = 0; i < relationIds.length; i++) {
				TmAreaGroupPO areaGroupPO = new TmAreaGroupPO();
				areaGroupPO.setRelationId(Long.parseLong(relationIds[i]));
				dao.delete(areaGroupPO);
			}
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "删除某业务范围对应的物料信息 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-24
	public void deleteProductMaterial(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String relationId = CommonUtils.checkNull(request.getParamValue("relationId"));
			String[] relationIds = relationId.split(",");
			for (int i = 0; i < relationIds.length; i++) {
				TtProductMaterialPO productMaterial = new TtProductMaterialPO();
				productMaterial.setId(Long.parseLong(relationIds[i]));
				dao.delete(productMaterial);
			}
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "删除某业务范围对应的物料信息 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : toAddBusArea 
	* @Description: 新增业务范围PRE
	* @throws 
	* @LastUpdate :2010-7-6
	*/
	public void toAddBusArea(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.setForword(toAddBusAreaURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "新增业务范围PRE ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : showAllGroupList 
	* @Description: 显示物料组列表
	* @throws 
	* @LastUpdate :2010-7-6
	*/
	public void showAllGroupList(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String groupIds = CommonUtils.checkNull(request.getParamValue("groupIds_"));
			String group_code = CommonUtils.checkNull(request.getParamValue("group_code"));
			String group_name = CommonUtils.checkNull(request.getParamValue("group_name"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = BusinessAreaManageDAO.getAllGroupList(group_code ,group_name,groupIds, Constant.PAGE_SIZE_MAX, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "显示物料组列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	/** 
	* @Title	  : addAreaAction 
	* @Description: 新增业务范围:提交
	* @param      : 设定文件 
	* @return     : void返回类型 
	* @throws 
	* @LastUpdate :2010-7-6
	*/
	public void addAreaAction(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String area_code = CommonUtils.checkNull(request.getParamValue("area_code"));
			String area_name = CommonUtils.checkNull(request.getParamValue("area_name"));
			String area_short = CommonUtils.checkNull(request.getParamValue("area_short"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			String produce_base=CommonUtils.checkNull(request.getParamValue("produce_base"));
			String[] groupIds = groupId.split(",");
			
			TmBusinessAreaPO businessAreaPO = new TmBusinessAreaPO();
			Long areaId = Long.parseLong(SequenceManager.getSequence(""));
			businessAreaPO.setAreaId(areaId);
			businessAreaPO.setCompanyId(logonUser.getCompanyId());
			businessAreaPO.setAreaCode(area_code.trim());
			businessAreaPO.setAreaName(area_name.trim());
			businessAreaPO.setAreaShortcode(area_short.trim()) ;
			businessAreaPO.setStatus(Constant.STATUS_ENABLE);
			businessAreaPO.setErpCode(Long.parseLong(getErpCode(produce_base))) ;
			businessAreaPO.setCreateBy(logonUser.getUserId());
			businessAreaPO.setCreateDate(new Date());
			if(produce_base!=null&&produce_base!=""){
			businessAreaPO.setProduceBase(Long.parseLong(produce_base));
			}
			dao.insert(businessAreaPO);
			
			for (int i = 0; i < groupIds.length; i++) {
				TmAreaGroupPO areaGroupPO = new TmAreaGroupPO();
				areaGroupPO.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
				areaGroupPO.setMaterialGroupId(Long.parseLong(groupIds[i]));
				areaGroupPO.setAreaId(areaId);
				areaGroupPO.setCreateBy(logonUser.getUserId());
				areaGroupPO.setCreateDate(new Date());
				dao.insert(areaGroupPO);
			}
			act.setForword(businessAreaManageInit);
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "新增业务范围:提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : deleteArea 
	* @Description: 删除业务范围
	* @throws 
	* @LastUpdate :2010-7-6
	*/
	public void deleteArea(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String area_id = CommonUtils.checkNull(request.getParamValue("area_id"));
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			areaPO.setAreaId(Long.parseLong(area_id));
			dao.delete(areaPO);
			
			TmAreaGroupPO areaGroupPO = new TmAreaGroupPO();
			areaGroupPO.setAreaId(Long.parseLong(area_id));
			dao.delete(areaGroupPO);
			
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "删除业务范围");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : statucChangeAction 
	* @Description: 修改业务范围状态
	* @throws 
	*/
	public void statucChangeAction(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String area_id = CommonUtils.checkNull(request.getParamValue("area_id"));
			String new_status = CommonUtils.checkNull(request.getParamValue("new_status"));
			String produce_base=CommonUtils.checkNull(request.getParamValue("produce_base"));
			TmBusinessAreaPO tempAreaPO = new TmBusinessAreaPO();
			tempAreaPO.setAreaId(Long.parseLong(area_id));
			TmBusinessAreaPO valueAreaPO = new TmBusinessAreaPO();
			valueAreaPO.setStatus(Integer.parseInt(new_status));
			if(produce_base!=null&&produce_base!=""){
				valueAreaPO.setProduceBase(Long.parseLong(produce_base));
				valueAreaPO.setErpCode(Long.parseLong(getErpCode(produce_base))) ;
			}
			dao.update(tempAreaPO, valueAreaPO);
			act.setForword(businessAreaManageInit);
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "删除业务范围");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-24
	public void productChangeAction(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String area_id = CommonUtils.checkNull(request.getParamValue("area_id"));
			String packageCode = CommonUtils.checkNull(request.getParamValue("packageCode"));
			String packageName=CommonUtils.checkNull(request.getParamValue("packageName"));
			String status=CommonUtils.checkNull(request.getParamValue("status"));
			String province=CommonUtils.checkNull(request.getParamValue("province"));
			TtAsWrProductPackagePO tppPO = new TtAsWrProductPackagePO();
			tppPO.setProductId(Long.parseLong(area_id));
			TtAsWrProductPackagePO tppPOValue = new TtAsWrProductPackagePO();
			tppPOValue.setPackageCode(packageCode);
			tppPOValue.setPackageName(packageName);
			tppPOValue.setStatus(Integer.parseInt(status));
			if(province!=null&&province!=""){
				tppPOValue.setProvinceId(Long.parseLong(province));
			}
			dao.update(tppPO, tppPOValue);
			MaterialGroupManage mgm = new MaterialGroupManage();
			mgm.dealerProductPackage();
		} catch(RuntimeException e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.PUTIN_FAILURE_CODE,e.getMessage());
			logger.error(logonUser,e1);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "删除业务范围");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	private String getErpCode(String entity) {
		String erpCode = "" ;
		String para = CommonDAO.getPara(Constant.CHANA_SYS.toString()) ;
		
		if(Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
			erpCode = Constant.ENTITY_JC.toString() ;
		} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
			if(Constant.SERVICEACTIVITY_CAR_YIELDLY_01.toString().equals(entity)) {
				erpCode = Constant.ENTITY_WC_CQ.toString() ;
			} else if(Constant.SERVICEACTIVITY_CAR_YIELDLY_02.toString().equals(entity) || Constant.SERVICEACTIVITY_CAR_YIELDLY_03.toString().equals(entity)) {// ENTITY_WC_HE, ENTITY_WC_CQ,ENTITY_WC_NJ
				erpCode = Constant.ENTITY_WC_HE.toString() ;
			} else if(Constant.SERVICEACTIVITY_CAR_YIELDLY_03.toString().equals(entity)) {
				erpCode = Constant.ENTITY_WC_NJ.toString() ;
			} else {
				throw new RuntimeException("对应基地实体的Constant不存在！") ;
			}
		} else {
			throw new RuntimeException("系统标识参数设置错误或系统辨别Constant不存在！") ;
		}
		
		return erpCode ;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}

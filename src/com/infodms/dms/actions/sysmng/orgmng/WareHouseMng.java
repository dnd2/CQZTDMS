/**********************************************************************
* <pre>
* FILE : DlrInfoMng.java
* CLASS : DlrInfoMng
*
* AUTHOR : LAX
*
* FUNCTION : 经销商信息维护.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-08-18| LAX  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: DlrInfoMng.java,v 1.6 2010/12/01 03:51:49 yangz Exp $
 */
package com.infodms.dms.actions.sysmng.orgmng;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.storageManage.WareHouseMngDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmWarehousePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  经销商信息维护
 * @author        :  LAX
 * CreateDate     :  2009-08-18
 * @version       :  0.1
 */
public class WareHouseMng {
	private Logger logger = Logger.getLogger(WareHouseMng.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private static final WareHouseMngDAO dao = WareHouseMngDAO.getInstance();
	
	private  final String  WareHouseMnginit =  "/jsp/systemMng/orgMng/wareHouseMngInit.jsp";
	private  final String  WareHouseMngAdd =  "/jsp/systemMng/orgMng/wareHouseAdd.jsp";
	private  final String  WareHouseMngEdit =  "/jsp/systemMng/orgMng/wareHouseEdit.jsp";
	
	public void wareHouseMngInit() throws Exception {
		AclUserBean logonUser = null;
		try{
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			logonUser.getDutyType();//判断是否是经销商端还是车厂端
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
			act.setOutData("logonUser", logonUser);
//			String dealerIds = logonUser.getDealerId();
//			List<Map<String,Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds);
//			act.setOutData("dealerList", dealerList);
			act.setForword(WareHouseMnginit);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void wareHouseAdd() throws Exception {
		AclUserBean logonUser = null;
		try{
			RequestWrapper request = act.getRequest();
			//String wareHouseId = request.getParamValue("WAREHOUSE_ID");
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			logonUser.getDutyType();//判断是否是经销商端还是车厂端
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
			act.setOutData("logonUser", logonUser);
			List<Map<String, Object>> houseTypeList= dao.getWareHouseType();
			List<Map<String, Object>> area= dao.getArea();
			//Map<String, Object> wareHouseIdList =  dao.getWareHouseById(wareHouseId);
			//act.setOutData("wareHouseIdList", wareHouseIdList);
			act.setOutData("houseTypeList", houseTypeList);
			act.setOutData("areaList", area);
			act.setForword(WareHouseMngAdd);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void wareHouseEdit() throws Exception {
		AclUserBean logonUser = null;
		try{
			RequestWrapper request = act.getRequest();
			String wareHouseId = request.getParamValue("WAREHOUSE_ID");
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			logonUser.getDutyType();//判断是否是经销商端还是车厂端
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("orgId", logonUser.getOrgId());
			act.setOutData("logonUser", logonUser);
			List<Map<String, Object>> houseTypeList= dao.getWareHouseType();
			List<Map<String, Object>> area= dao.getArea();
			Map<String, Object> wareHouseIdList =  dao.getWareHouseById(wareHouseId);
			act.setOutData("wareHouseId", wareHouseId); 
			act.setOutData("wareHouseIdList", wareHouseIdList); 
			act.setOutData("houseTypeList", houseTypeList);
			act.setOutData("areaList", area);
			act.setForword(WareHouseMngEdit);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void wareHouseDoSave() throws Exception {
		AclUserBean logonUser = null;
		try{
			RequestWrapper request = act.getRequest();
			String company_man =  CommonUtils.checkNull(request.getParamValue("company_man"));
			String company_tel =  CommonUtils.checkNull(request.getParamValue("company_tel"));
			String areaID =  CommonUtils.checkNull(request.getParamValue("area"));
			String houseType =  CommonUtils.checkNull(request.getParamValue("houseType"));
			String province1 =  CommonUtils.checkNull(request.getParamValue("province1"));
			String city1 =  CommonUtils.checkNull(request.getParamValue("city1"));
			String district1 =  CommonUtils.checkNull(request.getParamValue("district1"));
			String address =  CommonUtils.checkNull(request.getParamValue("address"));
			String house_code =  CommonUtils.checkNull(request.getParamValue("house_code"));
			String house_name =  CommonUtils.checkNull(request.getParamValue("house_name"));
			
		//	Long.parseLong(accountsType)
			Long mseq=new Long(SequenceManager.getSequence(""));
			
			TmWarehousePO wareHouse = new TmWarehousePO();
			wareHouse.setWarehouseId(mseq);
			wareHouse.setCreateDate(new Date());
			wareHouse.setAreaId(Long.parseLong(areaID));
			wareHouse.setProvCode(province1);
			wareHouse.setCityCode(city1);
			wareHouse.setCountyCode(district1);
			wareHouse.setAddress(address);
			wareHouse.setTel(company_tel);
			wareHouse.setLinkMan(company_man);
			wareHouse.setWarehouseType(Integer.parseInt(houseType));
			wareHouse.setStatus(Constant.STATUS_ENABLE);
			wareHouse.setWarehouseCode(house_code);
			wareHouse.setWarehouseName(house_name);
			dao.insert(wareHouse);
			act.setOutData("success", "成功");	

		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void wareHouseDoEditSave() throws Exception {
		
		try{
			RequestWrapper request = act.getRequest();
			String wareHouseId =  CommonUtils.checkNull(request.getParamValue("warehouse_id"));
			String company_man =  CommonUtils.checkNull(request.getParamValue("company_man"));
			String company_tel =  CommonUtils.checkNull(request.getParamValue("company_tel"));
			String areaID =  CommonUtils.checkNull(request.getParamValue("area"));
			String houseType =  CommonUtils.checkNull(request.getParamValue("houseType"));
			String province1 =  CommonUtils.checkNull(request.getParamValue("province1"));
			String city1 =  CommonUtils.checkNull(request.getParamValue("city1"));
			String district1 =  CommonUtils.checkNull(request.getParamValue("district1"));
			String address =  CommonUtils.checkNull(request.getParamValue("address"));
			String house_code =  CommonUtils.checkNull(request.getParamValue("house_code"));
			String house_name =  CommonUtils.checkNull(request.getParamValue("house_name"));
			String status =  CommonUtils.checkNull(request.getParamValue("status"));
		//	Long.parseLong(accountsType)
			//Long mseq=new Long(SequenceManager.getSequence(""));
			TmWarehousePO wareHouse1 = new TmWarehousePO();
			wareHouse1.setWarehouseId(Long.parseLong(wareHouseId));
			TmWarehousePO wareHouse = new TmWarehousePO();
			//wareHouse.setWarehouseId(Long.parseLong(wareHouseId));
			wareHouse.setCreateDate(new Date());
			wareHouse.setAreaId(Long.parseLong(areaID));
			wareHouse.setProvCode(province1);
			wareHouse.setCityCode(city1);
			wareHouse.setCountyCode(district1);
			wareHouse.setAddress(address);
			wareHouse.setTel(company_tel);
			wareHouse.setLinkMan(company_man);
			wareHouse.setWarehouseType(Integer.parseInt(houseType));
			wareHouse.setStatus(Constant.STATUS_ENABLE);
			wareHouse.setWarehouseCode(house_code);
			wareHouse.setWarehouseName(house_name);
			wareHouse.setStatus(Integer.parseInt(status));
			dao.update(wareHouse1, wareHouse);
			act.setOutData("success", "成功");	

		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void wareHouseQuery() throws Exception {
		AclUserBean logonUser = null;
		try{
			RequestWrapper request = act.getRequest();
			String curPage =  CommonUtils.checkNull(request.getParamValue("curPage"));
			String pageSize =  CommonUtils.checkNull(request.getParamValue("pageSize"));
			if(curPage==""){
				curPage = 1+"";
			}
			if(pageSize==""){
				pageSize = 10000+"";
			}
			
			String houseCode =  CommonUtils.checkNull(request.getParamValue("houseCode"));
			String houseName =  CommonUtils.checkNull(request.getParamValue("houseName"));
			String hourseDate =  CommonUtils.checkNull(request.getParamValue("hourseDate"));
			String linkMain =  CommonUtils.checkNull(request.getParamValue("linkMain"));
			String tel =  CommonUtils.checkNull(request.getParamValue("tel"));
			String address =  CommonUtils.checkNull(request.getParamValue("address"));
			Map<String, String> dataPara = new HashMap<String, String>();
			dataPara.put("houseCode",houseCode);
			dataPara.put("houseName",houseName);
			dataPara.put("hourseDate",hourseDate);
			dataPara.put("linkMain",linkMain);
			dataPara.put("tel",tel);
			dataPara.put("address",address);
			PageResult<Map<String, Object>> ps = dao.getHouseDataDetail(dataPara,Integer.parseInt(curPage),Integer.parseInt(pageSize));
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, " ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}

/**********************************************************************
* <pre>
* FILE : ServiceActivityManageItem.java
* CLASS : ServiceActivityManageItem
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理--活动项目
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-03| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageItem.java,v 1.1 2010/08/16 01:44:11 yuch Exp $
 */
package com.infodms.dms.actions.claim.serviceActivity;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageItemDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsActivityNetitemPO;
import com.infodms.dms.po.TtAsActivityPartsPO;
import com.infodms.dms.po.TtAsActivityRepairitemPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  服务活动管理--活动项目
 * @author        :  PGM
 * CreateDate     :  2010-06-02
 * @version       :  0.1
 */
public class ServiceActivityManageItem {
	private Logger logger = Logger.getLogger(ServiceActivityManageItem.class);
	private ServiceActivityManageItemDao dao = ServiceActivityManageItemDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ServiceActivityItemInitUrl = "/jsp/claim/serviceActivity/serviceActivityItem.jsp";//查询页面
	private final String ServiceActivityItemWorkHourUrl = "/jsp/claim/serviceActivity/serviceActivityItemWorkHours.jsp";//活动工时查询页面
	private final String ServiceActivityItemPartsUrl = "/jsp/claim/serviceActivity/serviceActivityItemParts.jsp";//配件查询页面
	private final String ServiceActivityItemOthersUrl = "/jsp/claim/serviceActivity/serviceActivityItemOthers.jsp";//活动其他项目查询页面
	
	/**
	 * Function       :  根据条件查询服务活动管理中符合条件的信息，其中包括：活动项目
	 * @param         :  request-活动ID
	 * @return        :  服务活动管理--活动项目
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-02
	 */
	public void serviceActivityManageItemQuery(){ 
		RequestWrapper request = act.getRequest();
		String activityId=request.getParamValue("activityId");//活动ID
		try {
			List<TtAsActivityBean> ActivityBeanList=dao.getWorkingHoursInfoList(activityId);//活动工时
			List<TtAsActivityBean> ActivityPartsList=dao.getPartsList(activityId);//活动配件
			List<TtAsActivityBean> ActivityNetItemList=dao.getNetItemList(activityId);//活动其它项目
			act.setOutData("ActivityBeanList", ActivityBeanList);
			//request.setAttribute("ActivityBeanList", ActivityBeanList);
			request.setAttribute("ActivityPartsList", ActivityPartsList);
			request.setAttribute("ActivityNetItemList", ActivityNetItemList);
			request.setAttribute("activityId", activityId);
			act.setForword(ServiceActivityItemInitUrl);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理--活动项目");
			logger.error(logonUser,e1);
			act.setException(e1);
		 }
		}
	/**
	 * Function       :  根据条件查询符合条件的信息，其中包括：工时信息（其中不包含在活动维修项目中）
	 * @param         :  request-活动ID、工时代码、名称
	 * @return        :  活动工时
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public void serviceActivityManageItemWorkHoursQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId=request.getParamValue("activityId");//活动ID
			String labourCode=request.getParamValue("labourCodes");//工时代码
			String cnDes=request.getParamValue("cnDess");//名称
			TtAsActivityBean MantainBean = new TtAsActivityBean();
			MantainBean.setActivityId(activityId);
			MantainBean.setLabourCode(labourCode);
			MantainBean.setCnDes(cnDes);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getServiceActivityManageItemWorkHoursQuery(MantainBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			request.setAttribute("activityId", activityId);
			act.setForword(ServiceActivityItemWorkHourUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔工时信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  工时新增
	 * @param         :  request-活动ID、labourId、维修项目代码、维修项目名称、工时数
	 * @return        :  服务活动管理---工时新增
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	@SuppressWarnings("static-access")
	public void serviceActivityManageItemWorkHoursOption(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId =request.getParamValue("activityId");         //活动ID
			String labourId =request.getParamValue("labourId");            //labourId
			String labourCode =request.getParamValue("labourCode");       //维修项目代码
			String cnDes =request.getParamValue("cnDes");                 //维修项目名称
			String labourHour =request.getParamValue("labourHour");       //工时数
			if (labourId!=null&&!"".equals(labourId)) {
				String [] labourIdArray = labourId.split(",");             //取得所有groupIds放在数组中
				String [] labourCodeArray = labourCode.split(",");         //取得所有labourCode放在数组中
				String [] cnDesArray = cnDes.split(",");                   //取得所有cnDes放在数组中
				String [] labourHourArray = labourHour.split(",");         //取得所有labourHour放在数组中
				TtAsActivityRepairitemPO RepairitemPO =new TtAsActivityRepairitemPO();
				RepairitemPO.setActivityId(Long.parseLong(activityId));
				RepairitemPO.setCreateBy(logonUser.getUserId());
				RepairitemPO.setCreateDate(new Date());
				RepairitemPO.setUpdateBy(logonUser.getUserId());
				RepairitemPO.setUpdateDate(new Date());
				dao.serviceActivityManageItemWorkHoursOption(labourIdArray,labourCodeArray,cnDesArray,labourHourArray,RepairitemPO);
				act.setOutData("success", "true");
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理活动工时信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  工时删除
	 * @param         :  request-活动ID,ItemId
	 * @return        :  服务活动管理---工时删除
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	@SuppressWarnings("static-access")
	public void deleteItemWorkHoursOption(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId =request.getParamValue("activityId");         //活动ID
			String itemId =request.getParamValue("itemId");                 //itemId
			TtAsActivityRepairitemPO RepairitemPO=new TtAsActivityRepairitemPO();
			RepairitemPO.setActivityId(Long.parseLong(activityId));
			RepairitemPO.setItemId(Long.parseLong(itemId));
			dao.deleteItemWorkHoursOption(RepairitemPO);
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理活动工时信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
  }
	/**
	 * Function       :  根据条件查询符合条件的信息，其中包括：配件信息（其中不包含在活动维修项目中）
	 * @param         :  request-活动ID、配件代码、配件名称
	 * @return        :  配件信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public void serviceActivityManageItemPartsQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId=request.getParamValue("activityId");//活动ID
			String partNo=request.getParamValue("partNos");       //配件代码
			String partName=request.getParamValue("partNames");   //配件名称
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);      //公司ID
			TtAsActivityBean MantainBean = new TtAsActivityBean();
			MantainBean.setActivityId(activityId);
			MantainBean.setPartNo(partNo);
			MantainBean.setPartName(partName);
			MantainBean.setCompanyId(String.valueOf(companyId));
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getServiceActivityManagePartsQuery(MantainBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			request.setAttribute("activityId", activityId);
			act.setForword(ServiceActivityItemPartsUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  配件信息新增
	 * @param         :  request-活动ID、partsId、配件项目代码、配件项目名称、配件项目数量
	 * @return        :  服务活动管理---配件信息新增
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	@SuppressWarnings("static-access")
	public void serviceActivityManageItemPartsOption(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId =request.getParamValue("activityId");         //活动ID
			String partsId =request.getParamValue("partsId");              //partsId
			String partNo =request.getParamValue("partNo");                //配件项目代码
			String partName =request.getParamValue("partName");            //配件项目名称
			String partsQuantityArray =request.getParamValue("PART_QUANTITY");//配件项目数量
			String claimPrice =request.getParamValue("claimPrice");//单价
			String supplierCode =request.getParamValue("supplierCode");//供应商代码
			String supplierName =request.getParamValue("supplierName");//供应商名称
			if (partsId!=null&&!"".equals(partsId)) {
				String [] partsIdArray = partsId.split(",");                //取得所有partsId放在数组中
				String [] partNoArray =  partNo.split(",");                 //取得所有partNo放在数组中
				String [] partNameArray = partName.split(",");              //取得所有partName放在数组中
				String [] partsQuantityArrays =null;
				String [] claimPriceArray =null;
				String [] supplierCodeArray =null;
				String [] supplierNameArray =null;
				if (null!=partsQuantityArray&&!"".equals(partsQuantityArray)&&!",".equals(partsQuantityArray)) {
				   partsQuantityArrays = partsQuantityArray.split(",");  //取得所有partsQuantity放在数组中
				}
				if(null!=claimPrice&&!"".equals(claimPrice)){
				   claimPriceArray = claimPrice.split(",");                  //取得所有单价放在数组中
				}
				if(null!=supplierCode&&!"".equals(supplierCode)){
				    supplierCodeArray = supplierCode.split(",");              //取得所有供应商代码放在数组中
				}
				if(null!=supplierName&&!"".equals(supplierName)){
				    supplierNameArray = supplierName.split(",");              //取得所有供应商名称放在数组中
				}
				TtAsActivityPartsPO PartsPO =new TtAsActivityPartsPO();
				PartsPO.setActivityId(Long.parseLong(activityId));
				PartsPO.setCreateBy(logonUser.getUserId());
				PartsPO.setCreateDate(new Date());
				PartsPO.setUpdateBy(logonUser.getUserId());
				PartsPO.setUpdateDate(new Date());
				dao.serviceActivityManageItemPartsOption(partsIdArray,partNoArray,partNameArray,partsQuantityArrays,claimPriceArray,supplierCodeArray,supplierNameArray,PartsPO);
				act.setOutData("success", "true");
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理活动配件信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  配件信息删除
	 * @param         :  request-活动ID,ItemId
	 * @return        :  服务活动管理---配件信息删除
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	@SuppressWarnings("static-access")
	public void deleteItemPartsOption(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId =request.getParamValue("activityId");         //活动ID
			String partsId =request.getParamValue("partsId");                 //itemId
			TtAsActivityPartsPO PartsPO=new TtAsActivityPartsPO();
			PartsPO.setActivityId(Long.parseLong(activityId));
			PartsPO.setPartsId(Long.parseLong(partsId));
			dao.deleteItemPartsOption(PartsPO);
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理活动配件信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
  }
	/**
	 * Function       :  根据条件查询符合条件的信息，其中包括：活动其它项目信息（其中不包含在活动维修项目中）
	 * @param         :  request-活动ID
	 * @return        :  活动其它项目信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public void serviceActivityManageItemOthersQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId=request.getParamValue("activityId"); //活动ID
		//	String partNo=request.getParamValue("partNo");        //配件代码
		//	String partName=request.getParamValue("partName");    //配件名称
			TtAsActivityBean MantainBean = new TtAsActivityBean();
			MantainBean.setActivityId(activityId);
			//MantainBean.setPartNo(partNo);
			//MantainBean.setPartName(partName);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getServiceActivityManageOthersQuery(MantainBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			request.setAttribute("activityId", activityId);
			act.setForword(ServiceActivityItemOthersUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  活动其它项目信息新增
	 * @param         :  request-活动ID、项目代码、项目名称
	 * @return        :  服务活动管理---活动其它项目信息新增
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	@SuppressWarnings("static-access")
	public void serviceActivityManageItemOthersOption(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId =request.getParamValue("activityId");         //活动ID
			String id =request.getParamValue("id");                        //ID
			String itemCode =request.getParamValue("itemCode");            //项目代码
			String itemDesc =request.getParamValue("itemDesc");            //项目名称
			if (id!=null&&!"".equals(id)) {
				String [] idArray = id.split(",");                        //取得所有id放在数组中
				String [] itemCodeArray = itemCode.split(",");            //取得所有itemCode放在数组中
				String [] itemDescArray = itemDesc.split(",");            //取得所有itemDesc放在数组中
				TtAsActivityNetitemPO NetitemPO =new TtAsActivityNetitemPO();
				NetitemPO.setActivityId(Long.parseLong(activityId));
				NetitemPO.setCreateBy(logonUser.getUserId());
				NetitemPO.setCreateDate(new Date());
				NetitemPO.setUpdateBy(logonUser.getUserId());
				NetitemPO.setUpdateDate(new Date());
				dao.serviceActivityManageItemOthersOption(idArray,itemCodeArray,itemDescArray,NetitemPO);
				act.setOutData("success", "true");
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理活动配件信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  活动其它项目信息删除
	 * @param         :  request-活动ID,ItemId
	 * @return        :  服务活动管理---活动其它项目信息删除
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	@SuppressWarnings("static-access")
	public void deleteItemOthersOption(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId =request.getParamValue("activityId");         //活动ID
			String id =request.getParamValue("id");                         //itemId
			TtAsActivityNetitemPO PartsPO=new TtAsActivityNetitemPO();
			PartsPO.setActivityId(Long.parseLong(activityId));
			PartsPO.setId(Long.parseLong(id));
			dao.deleteItemOthersOption(PartsPO);
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理活动配件信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
  }
}
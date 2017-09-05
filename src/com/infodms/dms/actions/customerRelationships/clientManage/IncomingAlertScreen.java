package com.infodms.dms.actions.customerRelationships.clientManage;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.customerRelationships.ComplaintConsultDao;
import com.infodms.dms.dao.customerRelationships.IncomingAlertScreenDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmCustomerPO;
import com.infodms.dms.po.TtCustomerPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : IncomingAlertScreen 
 * @Description   : 来电弹屏
 * @author        : wangming
 * CreateDate     : 2013-4-12
 */
public class IncomingAlertScreen{
	private static Logger logger = Logger.getLogger(IncomingAlertScreen.class);
	// 来电弹屏初始化页面
	private final String incomingAlertScreenUrl = "/jsp/customerRelationships/clientManage/incomingAlertScreen.jsp";
	//投诉咨询历史页面
	private final String complaintInfoUrl = "/jsp/customerRelationships/complaintConsult/complaintConsultHistory.jsp";
	

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	
	/**
	 * 来电弹屏初始化
	 */
	public void incomingAlertScreenInit(){		
		try{
			RequestWrapper request = act.getRequest();
			String strCaller = request.getParamValue("strCaller");
			String strInfo = request.getParamValue("strInfo");
			if(strInfo == null )
			{
				strInfo = "0";
			}
			if(java.nio.charset.Charset.forName("ISO-8859-1").newEncoder().canEncode(strInfo))
			{
				strInfo = new String(strInfo.getBytes("ISO-8859-1"),"UTF-8");
			}
			
			if(!strInfo.equals("0"))
			{
				strInfo = strInfo.replaceAll("\"", "");
				strInfo = strInfo.split(",")[0]+","+strInfo.split(",")[1];
			}
			if(strCaller != null)
			{
				act.setOutData("strCallid", strCaller);
			}
			
			if(!strInfo.equals("0"))
			{
				act.setOutData("strInfo", strInfo);
			}
			
			act.setForword(incomingAlertScreenUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"来电弹屏初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	//查询弹屏信息内容
	public void queryIncomingAlertScreen(){
		act.getResponse().setContentType("application/json");
		try{
			String incomeTep = CommonUtils.checkNull(request.getParamValue("incomeTep")); //来电号码
			String vinNo = CommonUtils.checkNull(request.getParamValue("vinNo")); 			  //底盘号
			String name = CommonUtils.checkNull(request.getParamValue("name")); 		  //客户姓名
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone")); //联系电话
			String engineNo = CommonUtils.checkNull(request.getParamValue("engineNo"));//发动机号
				
			IncomingAlertScreenDao dao = IncomingAlertScreenDao.getInstance();
			
			//艾春 9.17 添加 坐席状态更新
			int len = incomeTep.length();
			// 如果电话号码是12位, 则表示坐席处于来电状态
			if(12 == len){
				this.updateSeatsStatus(logonUser.getUserId(), Constant.SEAT_INCOMING_TYPE);
			}
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
//			PageResult<Map<String,Object>> queryIncomingAlertScreeData = dao.queryIncomingAlertScreen(incomeTep,
//					vinNo,name,telephone,Constant.PAGE_SIZE,curPage);
			
			// 艾春 9.25 添加 来电弹屏查询每日定时获取的客户信息
			PageResult<Map<String,Object>> queryIncomingAlertScreeData = null;
			
			if(Utility.testString(incomeTep) )
			{
				List<DynaBean> list= dao.queryIncomingAlertScreenNew01(incomeTep,
						vinNo,name,telephone);
				queryIncomingAlertScreeData = new PageResult<Map<String,Object>>();
				
				List<DynaBean> tmp = list;
				LinkedList<Map<String,Object>> t1 = new LinkedList<Map<String,Object>>();	
				if( list!=null ){
					for( DynaBean bean : list ){
						t1.addLast((Map<String,Object>)bean);
						queryIncomingAlertScreeData.setRecords(t1);	
					}	
				}
				
				queryIncomingAlertScreeData.setCurPage(1);
				queryIncomingAlertScreeData.setPageSize(15);
				queryIncomingAlertScreeData.setTotalPages(1);
				act.setOutData("ps", queryIncomingAlertScreeData);
			}else
			{
				queryIncomingAlertScreeData= dao.queryIncomingAlertScreenNew(incomeTep,
				vinNo,name,telephone,engineNo,Constant.PAGE_SIZE,curPage);
				act.setOutData("ps", queryIncomingAlertScreeData);
			}
			
			
			
			
			//特殊处理当只有一条数据时 前台处理
//			if(queryIncomingAlertScreeData.getTotalRecords() == 1){
//				Map<String,Object> map = queryIncomingAlertScreeData.getRecords().get(0);
//				map.put("ONLYONE", true);
//			}else if(queryIncomingAlertScreeData.getTotalRecords() != 0){
//				for(Map<String,Object> map : queryIncomingAlertScreeData.getRecords()){
//					map.put("ONLYONE", false);
//				}
//			}				
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"来电弹屏查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//查询客户详细信息
	public void queryIncomingAlertScreenInfor(){
		act.getResponse().setContentType("application/json");
		try{
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid")); //客户ID
			String orderid = CommonUtils.checkNull(request.getParamValue("orderid"));     

				
			IncomingAlertScreenDao dao = IncomingAlertScreenDao.getInstance();
			CommonUtilActions  commonUtilActions = new CommonUtilActions();
	
			Map<String,Object> queryIncomingAlertScreeData = dao.queryIncomingAlertScreenInfor(ctmid,orderid);
			//客户关系
			act.setOutData("customerTypeList", commonUtilActions.getTcCode(Constant.CUSTOMER_TYPE.toString()));
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//城市
			act.setOutData("cityList", commonUtilActions.getAllCity());
			//用途
			act.setOutData("salesAddressList", commonUtilActions.getTcCode(Constant.SALES_ADDRESS));
			//客户级别
			act.setOutData("guestStarsList",commonUtilActions.getTcCode(Constant.GUEST_STARS.toString()));
			act.setOutData("queryIncomingAlertScreeData", queryIncomingAlertScreeData);
			
			ClaimBillMaintainDAO claimBillMaintainDAO = ClaimBillMaintainDAO.getInstance();
			String vin = (String)queryIncomingAlertScreeData.get("VIN");
		    String claimTypes = "" + Constant.CLA_TYPE_01 + "," + Constant.CLA_TYPE_07 
		                + "," + Constant.CLA_TYPE_09;
		    List<Map<String,Object>> maintaimHisList = claimBillMaintainDAO.maintaimHistoryByVIN(vin);
			act.setOutData("maintaimHisList", maintaimHisList);
			
			ComplaintConsultDao complaintConsultDao = ComplaintConsultDao.getInstance();
			List<Map<String,Object>> complaintInfoList = complaintConsultDao.QueryComplaintInfoList(ctmid);
	      	act.setOutData("complaintInfoList", complaintInfoList);
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"来电弹屏查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//查询客户详细信息 投诉受理接口
	public void queryCustomerInfor(){
		act.getResponse().setContentType("application/json");
		try{
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid")); //客户ID
			String orderid = CommonUtils.checkNull(request.getParamValue("orderid"));     //所属地区

				
			IncomingAlertScreenDao dao = IncomingAlertScreenDao.getInstance();
	
			Map<String,Object> queryCustomerInforData = dao.queryCustomerInfor(ctmid,orderid);
			
			act.setOutData("queryCustomerInforData", queryCustomerInforData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//更新客户电话信息
	public void updateCustomer(){
		act.getResponse().setContentType("application/json");
		try{
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid")); //客户ID
			String ctmname = CommonUtils.checkNull(request.getParamValue("ctmname")); 			 
			String ctmtype = CommonUtils.checkNull(request.getParamValue("ctmtype")); 		 
			//String purpose = CommonUtils.checkNull(request.getParamValue("purpose")); 
			String cardNum = CommonUtils.checkNull(request.getParamValue("cardNum")); 			 
			String province = CommonUtils.checkNull(request.getParamValue("province")); 		 
			String city = CommonUtils.checkNull(request.getParamValue("city")); 
			String postCode = CommonUtils.checkNull(request.getParamValue("postCode")); 			 
			String address = CommonUtils.checkNull(request.getParamValue("address")); 		 
			String stars = CommonUtils.checkNull(request.getParamValue("stars")); 
			String ctel = CommonUtils.checkNull(request.getParamValue("ctel")); 			 
			String otel = CommonUtils.checkNull(request.getParamValue("otel")); 		 
			String tele = CommonUtils.checkNull(request.getParamValue("tele")); 
				
			IncomingAlertScreenDao dao = IncomingAlertScreenDao.getInstance();
			TtCustomerPO ttCustomerPO1 = new TtCustomerPO();
			TtCustomerPO ttCustomerPO2 = new TtCustomerPO();
			
			ttCustomerPO1.setCtmId(Long.parseLong(ctmid));
			ttCustomerPO2.setCtmName(ctmname);
			if(!"".equals(ctmtype))ttCustomerPO2.setCtmType(Integer.parseInt(ctmtype));
			ttCustomerPO2.setCardNum(cardNum);
			if(!"".equals(province))ttCustomerPO2.setProvince(Long.parseLong(province));
			if(!"".equals(city))ttCustomerPO2.setCity(Long.parseLong(city));
			ttCustomerPO2.setPostCode(postCode);
			ttCustomerPO2.setAddress(address);
			ttCustomerPO2.setGuestStars(stars);
			ttCustomerPO2.setCompanyPhone(ctel);
			ttCustomerPO2.setOtherPhone(otel);
			ttCustomerPO2.setMainPhone(tele);
			dao.update(ttCustomerPO1, ttCustomerPO2);
			
			TtCrmCustomerPO ttCrmCustomerPO1 = new TtCrmCustomerPO();
			TtCrmCustomerPO ttCrmCustomerPO2 = new TtCrmCustomerPO();
			
			ttCrmCustomerPO1.setCtmId(Long.parseLong(ctmid));
			List<TtCrmCustomerPO> TtCrmCustomerPOList = dao.select(ttCrmCustomerPO1);
			TtCrmCustomerPO ttCrmCustomerPO3 = TtCrmCustomerPOList!=null ? TtCrmCustomerPOList.get(0):null;
			if(ttCrmCustomerPO3!=null){
				
				// 艾春 2013.12.5 修改更新字段
//				String tempTel = ttCrmCustomerPO3.getPhone();
				String tempTel = ""; /*三个字段重新累加*/
				String newCompanyPhone = "";
				String newOtherPhone = "";
				String newMainPhone = "";
				if(!ctel.equals(ttCrmCustomerPO3.getCompanyPhone()) && !("".equals(ctel))){
					tempTel = tempTel != "" ? tempTel+","+ctel:ctel;
					newCompanyPhone = ctel;
				}
				if(!otel.equals(ttCrmCustomerPO3.getOtherPhone()) && !("".equals(otel))){
					tempTel = tempTel != "" ? tempTel+","+otel:otel;
					newOtherPhone = otel;
				}
				if(!tele.equals(ttCrmCustomerPO3.getMainPhone()) && !("".equals(tele))){
					tempTel = tempTel != "" ? tempTel+","+tele:tele;
					newMainPhone = tele;
				}
				ttCrmCustomerPO2.setPhone(tempTel);
				if(!"".equals(newCompanyPhone)) ttCrmCustomerPO2.setCompanyPhone(newCompanyPhone);
				if(!"".equals(newOtherPhone)) ttCrmCustomerPO2.setOtherPhone(newOtherPhone);
				if(!"".equals(newMainPhone)) ttCrmCustomerPO2.setMainPhone(newMainPhone);
				// 艾春 2013.12.5 修改更新字段
				dao.update(ttCrmCustomerPO1, ttCrmCustomerPO2);
			}
			act.setOutData("success", "true");
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"来电弹屏查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//投拆咨询记录
    public void complaintInfo(){
	    
	    try {
	    	String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid")); //客户ID
	    	ComplaintConsultDao dao = ComplaintConsultDao.getInstance();
	    	
	    	List<Map<String,Object>> complaintInfoList = dao.QueryComplaintInfoList(ctmid);
	      	act.setOutData("complaintInfoList", complaintInfoList);
	      	act.setForword(complaintInfoUrl);
	    }catch (Exception e) {
	    	BizException e1 = new BizException(act, e,
	          ErrorCodeConstant.QUERY_FAILURE_CODE, "投拆咨询记录");
	    	act.setOutData("success", false);
	    	logger.error(logonUser, e1);
	    	act.setException(e1);
	    }  
	 }
    
    // 艾春9.17更新坐席状态
    public void updateSeatsStatus(Long userId, Integer seatsStatus){
    	// 艾春9.17判断用户ID和坐席状态不为空
    	if(null != userId && null != seatsStatus){
    		IncomingAlertScreenDao dao = IncomingAlertScreenDao.getInstance();
    		// 坐席状态
    		dao.updateSeatStatus(userId, seatsStatus);
    	}
    }

}
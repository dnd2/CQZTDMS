package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.po.TtPartUserprovinceDefinePO;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartSalesScopeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartSalesscopeDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : PartSalesScopeManager 
 * @Description   : 配件销售人员区域范围管理
 * @author        : chenjunjiang
 * CreateDate     : 2013-4-12
 */
public class PartSalesScopeManager implements PTConstants{
	public Logger logger = Logger.getLogger(PartSalesScopeManager.class);
	private PartSalesScopeDao dao = PartSalesScopeDao.getInstance();
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 配件销售人员区域范围查询初始化,转到查询页面 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public void partSalesScopeQueryInit(){


		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(PART_SALES_QUERY_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售人员区域范围信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 配件销售人员查询 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public void queryPartSales(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String acnt = request.getParamValue("acnt");//用户账号
			String name = request.getParamValue("name");//用户名称
			String companyId = String.valueOf(logonUser.getCompanyId());//公司id
			String user_status = request.getParamValue("USER_STATUS");//用户状态
			String userType = request.getParamValue("userType");//用户类型
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryPartSales(acnt, name, companyId,user_status,userType,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件销售人员信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 通过用户id来查询该用户信息,并把用户信息带到修改页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public void queryPartSalesByUserId(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String userId = request.getParamValue("userId");
			String userType = request.getParamValue("userType");//用户类型
			Map<String, Object> map = dao.getUserMap(userId);
			act.setOutData("map", map);
			act.setOutData("userId", userId);
			act.setOutData("userType", userType);
			act.setForword(PART_SALESSCOPE_QUERY_URL1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件销售人员区域范围");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询当前选择用户对应的区域范围(经销商) 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public void queryScopeByUserList(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空
			String userId = request.getParamValue("userId");
			String userType = request.getParamValue("userType");//用户类型
			String regionCode = CommonUtils.checkNull(request.getParamValue("REGION_CODE"));
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryScopeByUserList(userId,userType,regionCode,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户对应的区域范围(经销商)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	
	public void queryScopeByUserList1(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String json = new String(request.getParamValue("json").getBytes("ISO8859-1"), "UTF-8");	
			JSONObject paraObject = JSONObject.fromObject(json);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ?Integer.parseInt(request.getParamValue("curPage")) : 1;
			
			PageResult<Map<String, Object>> ps = dao.queryScopeByUserList1(paraObject,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户对应的区域范围(经销商)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}	
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 为配件销售人员添加区域范围(经销商) 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-13
	 */
	public void addPartSalesScopeInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String userId = request.getParamValue("USERID");//销售人员id
			String userType = request.getParamValue("userType");//用户类型
			String[] ids = request.getParamValue("DEALER_IDS").split(",");//经销商id
			TtPartSalesscopeDefinePO po = null;
			if(ids!=null){
				for(int i=0;i<ids.length;i++){
					
					Map map = dao.checkDealerId(userId,ids[i]);//检测该销售人员是否已经对应了该经销商
					if(map!=null&&map.size()>0){
						act.setOutData("error",map.get("DEALER_SHORTNAME")+"已经存在,请重新选择!");
						return;
					}
					po = new TtPartSalesscopeDefinePO();
					po.setUserId(CommonUtils.parseLong(userId));
					po.setDealerId(CommonUtils.parseLong(ids[i]));
					po.setDefineId(Long.parseLong(SequenceManager.getSequence("")));
					po.setUserType(Long.parseLong(userType));
					po.setCreateDate(new Date());
					po.setCreateBy(logonUser.getUserId());
					
					dao.insert(po);
				}
			}
			act.setOutData("success","添加成功!");
			act.setOutData("userId", userId);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"用户对应的区域范围(经销商)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 删除销售人员对应的区域范围 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-13
	 */
	public void deletePartSalesScope(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String[] ids = request.getParamValues("ids");
			if(ids!=null){
				for(int i=0;i<ids.length;i++){
					TtPartSalesscopeDefinePO spo = new TtPartSalesscopeDefinePO();
					spo.setDefineId(CommonUtils.parseLong(ids[i]));
					
					TtPartSalesscopeDefinePO po = new TtPartSalesscopeDefinePO();
					po.setStatus(0);
					dao.update(spo, po);
				}
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"用户对应的区域范围(经销商)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
    //根据省份新增
	public void addPartSalesScopeByRegion(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String userId = request.getParamValue("userId");//销售人员id
			String userType = request.getParamValue("userType");//用户类型
			String codes=request.getParamValue("codes");//省份IDs
            String [] codeArr = codes.split(",");
			List<Map<String, Object>> list = dao.checkRegion(userId,codes);//检测该销售人员是否已经对应了该省份
			String errormsg="";
			for(int i=0;i<list.size();i++){
				Map map=list.get(i);
			    if(map!=null&&map.size()>0){
			    	errormsg+=map.get("REGION_NAME")+"已经存在,请重新选择!";
				    //act.setOutData("error",map.get("REGION_NAME")+"已经存在,请重新选择!");
				    //return;
			    }			    
			}
			if(!errormsg.equals("")){
				act.setOutData("error",errormsg);
				return;
			}
			List<Map<String,Object>> ids=dao.getDealerId(codes);
			addPartSalesScopeInfo1(ids,userId,userType,logonUser);
            addPartUserPROVINCEInfo(codeArr,userId,userType,logonUser);//人员和省份关系表
			act.setOutData("success","添加成功!");
			act.setOutData("userId", userId);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"用户对应的区域范围(经销商)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}	
	//新增实际操作1
	public void addPartSalesScopeInfo1(List<Map<String,Object>> ids,String userId,String userType,AclUserBean logonUser){
			TtPartSalesscopeDefinePO po = null;
			TtPartSalesscopeDefinePO delpo = null;
			if(ids!=null){
				for(int i=0;i<ids.size();i++){
					//logger.info("----------id="+ids[i]);
					po = new TtPartSalesscopeDefinePO();
					po.setUserId(CommonUtils.parseLong(userId));
					po.setDealerId(CommonUtils.parseLong(ids.get(i).get("DEALER_ID").toString()));
					po.setDefineId(Long.parseLong(SequenceManager.getSequence("")));
					po.setUserType(Long.parseLong(userType));
					po.setCreateDate(new Date());
					po.setCreateBy(logonUser.getUserId());
                    delpo = new TtPartSalesscopeDefinePO();
                    delpo.setDealerId(CommonUtils.parseLong(ids.get(i).get("DEALER_ID").toString()));
                    delpo.setUserId(CommonUtils.parseLong(userId));
                    dao.delete(delpo);
					dao.insert(po);
				}
			}
	}
    //新增实际操作1
    public void addPartUserPROVINCEInfo(String []  provIds,String userId,String userType,AclUserBean logonUser){
        TtPartUserprovinceDefinePO po = null;
        if(provIds!=null){
            for(int i=0;i<provIds.length;i++){
                //logger.info("----------id="+ids[i]);
                po = new TtPartUserprovinceDefinePO();
                po.setUserId(CommonUtils.parseLong(userId));
                po.setProvinceId(CommonUtils.parseLong(provIds[i]));
                po.setDefineId(Long.parseLong(SequenceManager.getSequence("")));
                po.setUserType(Long.parseLong(userType));
                po.setCreateDate(new Date());
                po.setCreateBy(logonUser.getUserId());
                dao.insert(po);
            }
        }
    }

	//根据省份删除
	public void deletePartSalesScopeByRegion(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String userId = request.getParamValue("userId");//销售人员id
			String userType = request.getParamValue("userType");//用户类型			
			String ids[]=request.getParamValues("ids");//省份ID
			String codes="";
			for(int j=0;j<ids.length;j++){
				codes+=ids[j]+",";
			}
			codes=codes.substring(0,codes.length()-1);
			//logger.info("------codes="+codes.toString());
			List<Map<String,Object>> dealerids=dao.getDealerId(codes);
			deletePartSalesScope1(dealerids,userId,userType,logonUser);
            deletePartUSERPROVINCE1(ids,userId,userType,logonUser);
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"用户对应的区域范围(经销商)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}	
	//删除实际操作
	public void deletePartSalesScope1(List<Map<String,Object>> ids,String userId,String userType,AclUserBean logonUserss){
			if(ids!=null){
				for(int i=0;i<ids.size();i++){
					logger.info("------id="+ids.get(i).get("DEALER_ID").toString());
					TtPartSalesscopeDefinePO spo = new TtPartSalesscopeDefinePO();
					//spo.setDefineId(CommonUtils.parseLong(ids.get(i).get("DEALER_ID").toString()));	
					spo.setDealerId(CommonUtils.parseLong(ids.get(i).get("DEALER_ID").toString()));
					spo.setUserId(Long.parseLong(userId));
					spo.setUserType(Long.parseLong(userType));
					TtPartSalesscopeDefinePO po = new TtPartSalesscopeDefinePO();
					po.setStatus(0);
                    dao.delete(spo);
					//dao.update(spo, po);
				}
			}
	}
    //删除实际操作
    public void deletePartUSERPROVINCE1(String [] ids,String userId,String userType,AclUserBean logonUserss){
        if(ids!=null){
            for(int i=0;i<ids.length;i++){
                TtPartUserprovinceDefinePO spo = new TtPartUserprovinceDefinePO();
                spo.setProvinceId(CommonUtils.parseLong(ids[i]));
                spo.setUserId(Long.parseLong(userId));
                spo.setUserType(Long.parseLong(userType));
                dao.delete(spo);
            }
        }
    }
    //根据省份获取经销商明细
	public void getDealerByRegion(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();		
		try {
			String userId = request.getParamValue("userId");//销售人员id
			String userType = request.getParamValue("userType");//用户类型
			String regionCode = request.getParamValue("regionCode");//省份
			List<Map<String,Object>> dealerids=dao.getSaleScope(regionCode,userId,userType);
			JSONObject dealerObject=new JSONObject();
			for(int i=0;i<dealerids.size();i++){
		        dealerObject.put(dealerids.get(i).get("DEALER_ID").toString(),dealerids.get(i).get("DEALER_NAME").toString());				
			}
			logger.info("-----dealerObject="+dealerObject.toString());
			act.setOutData("returnValue", 1);
			act.setOutData("regionCode", regionCode);
			act.setOutData("dealerObject", dealerObject.toString());
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"获取经销商明细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    }
	
	//保存经销商明细
	public void saveDealer(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String userId = request.getParamValue("userId");//销售人员id
			String userType = request.getParamValue("userType");//用户类型
			String regionCode = request.getParamValue("regionCode");//省份
			String ids = request.getParamValue("ids");//经销商ID
			List<Map<String,Object>> dealerids=dao.getDealerId(regionCode);
			deletePartSalesScope1(dealerids,userId,userType,logonUser);
			addPartSalesScopeInfo2(ids,userId,userType,logonUser);
			act.setOutData("success","明细更新成功!");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"用户对应的区域范围(经销商)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//新增实际操作2
	public void addPartSalesScopeInfo2(String strids,String userId,String userType,AclUserBean logonUser){
			TtPartSalesscopeDefinePO po = null;
			if(strids!=null){
				String[] ids=strids.split(","); 
				for(int i=0;i<ids.length;i++){
					//logger.info("----------id="+ids[i]);
					po = new TtPartSalesscopeDefinePO();
					po.setUserId(CommonUtils.parseLong(userId));
					po.setDealerId(CommonUtils.parseLong(ids[i]));
					po.setDefineId(Long.parseLong(SequenceManager.getSequence("")));
					po.setUserType(Long.parseLong(userType));
					po.setCreateDate(new Date());
					po.setCreateBy(logonUser.getUserId());					
					dao.insert(po);
				}
			}
	}	
	
}

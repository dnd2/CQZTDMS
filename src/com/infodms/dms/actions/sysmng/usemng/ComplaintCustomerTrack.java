
/**********************************************************************
* <pre>
* FILE : ComplaintCustomerTrack.java
* CLASS : ComplaintCustomerTrack
* 
* AUTHOR : YH
*
* FUNCTION : 维修投诉抱怨客户跟踪sAction.
*-----------------------------------------------------------
*|2011-04-22| 
* DESCRIPTION:
* </pre>
***********************************************************************/

package com.infodms.dms.actions.sysmng.usemng;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.SeriesShowDao;
import com.infodms.dms.dao.potentialCustomer.ComplaintCustomerTrackDao;
import com.infodms.dms.dao.potentialCustomer.RepairCustomerVisitDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmPotentialCustomerPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtComplaintCustomerTrackPO;
import com.infodms.dms.po.TtRepairCusVisitPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class ComplaintCustomerTrack {
    
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	public Logger logger = Logger.getLogger(ComplaintCustomerTrack.class);
	private final String queryInitUrl = "/jsp/systemMng/userMng/ComplaintCustomerTrackSearch.jsp";
	private final String addInitUrl = "/jsp/systemMng/userMng/ComplaintCustomerTrackAdd.jsp";
	private final String modfiInitUrl = "/jsp/systemMng/userMng/ComplaintCustomerTrackUpdate.jsp";
    private ComplaintCustomerTrackDao dao = ComplaintCustomerTrackDao.getInstance();
    private SeriesShowDao sdao = SeriesShowDao.getInstance();
	/**
	 * 维修投诉抱怨客户跟踪查询初始化(经销商端)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryInit() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(queryInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DISABLED_FAILURE_CODE,"维修投诉抱怨客户查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 维修投诉抱怨客户跟踪添加初始化(经销商端)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addInit() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<TmVhclMaterialGroupPO> list = sdao.getMaterialGroup();
			act.setOutData("list", list);
			act.setForword(addInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DISABLED_FAILURE_CODE,"维修投诉抱怨客户添加初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 维修投诉抱怨客户跟踪初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void modfiInit() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String cus_id = request.getParamValue("CUS_ID");
			
			TtComplaintCustomerTrackPO cct = new TtComplaintCustomerTrackPO();
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				StringBuffer sql= new StringBuffer();
			    sql.append("select cct.cus_id,\n" );
			    sql.append("       cct.customer_name,\n" );
			    sql.append("       cct.group_code,\n" );
			    sql.append("       cct.phone,\n" );
			    sql.append("       cct.dealer_code,\n" );
				sql.append("       cct.license_no,\n" );
				sql.append("       cct.times,\n" );
				sql.append("       to_char(cct.track_date,'yyyy-MM-dd') track_date,\n" );
				sql.append("       cct.complaint_record,\n" );
				sql.append("       cct.deal_with,\n" );
				sql.append("       cct.cus_request,\n" );
				sql.append("       cct.create_by,\n" );
				sql.append("       cct.create_date,\n" );
				sql.append("       cct.update_by,\n" );
				sql.append("       cct.update_date,\n" );
				sql.append("       cct.RESULT \n" );
				sql.append("  from tt_complaint_customer_track cct  \n" );
			    sql.append(" WHERE 1=1  AND cct.CUS_ID='"+cus_id+"'");
			list = dao.pageQuery(sql.toString(), null,null);
			if(list.size()>0){
				act.setOutData("map",list.get(0));
			}
			
			Map<String,Object> map1 = new HashMap();
			map1.put("TIME","1");
			map1.put("NAME","1次");
			Map<String,Object> map2 = new HashMap();
			map2.put("TIME","2");
			map2.put("NAME","2次");
			Map<String,Object> map3 = new HashMap();
			map3.put("TIME","3");
			map3.put("NAME","3次");
			List timelist = new ArrayList();
			timelist.add(map1);
			timelist.add(map2);
			timelist.add(map3);
			act.setOutData("timelist", timelist);
			
			List<TmVhclMaterialGroupPO> list1 = sdao.getMaterialGroup();
			act.setOutData("list", list1);	
			act.setForword(modfiInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"跳转修改维修投诉抱怨客户页面失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 维修投诉抱怨客户跟踪修改提交
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void modfi() {
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerCode = logonUser.getDealerCode();
		try{
			String CUS_ID =	CommonUtils.checkNull(request.getParamValue("CUS_ID"));//客户姓名
			String CUSTOMER_NAME =	CommonUtils.checkNull(request.getParamValue("CUSTOMER_NAME"));//客户姓名
			String TRACK_DATE  =  CommonUtils.checkNull(request.getParamValue("track_date"));//跟踪日期
			String PHONE	= CommonUtils.checkNull(request.getParamValue("PHONE"));//电话
			String	GROUP_CODE = CommonUtils.checkNull(request.getParamValue("GROUP_CODE"));//车型
			String	LICENSE_NO = CommonUtils.checkNull(request.getParamValue("LICENSE_NO"));//车牌
			String	TIMES= CommonUtils.checkNull(request.getParamValue("TIMES"));//频次 
			String	COMPLAINT_RECORD = CommonUtils.checkNull(request.getParamValue("COMPLAINT_RECORD"));//投诉纪录 
			String	DEAL_WITH = CommonUtils.checkNull(request.getParamValue("DEAL_WITH"));// 处理人
			String	CUS_REQUEST = CommonUtils.checkNull(request.getParamValue("CUS_REQUEST"));// 客户要求
			String	RESULT = CommonUtils.checkNull(request.getParamValue("RESULT"));// 处理结果
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			
			//更新的时候
			TtComplaintCustomerTrackPO cct = new TtComplaintCustomerTrackPO();
			   cct.setCustomerName(CUSTOMER_NAME);
			   cct.setTrackDate(dateFormat.parse(TRACK_DATE));
			   cct.setPhone(PHONE);
			   cct.setGroupCode(GROUP_CODE);
			   cct.setLicenseNo(LICENSE_NO);
	           cct.setTimes(TIMES);
	           cct.setComplaintRecord(COMPLAINT_RECORD);
	           cct.setDealWith(DEAL_WITH);
	           cct.setResult(RESULT);
	           cct.setUpdateBy(logonUser.getUserId());
			   cct.setUpdateDate(new Date());
			   TtComplaintCustomerTrackPO oldcct = new TtComplaintCustomerTrackPO();
			   oldcct.setCusId(Long.parseLong(CUS_ID));      
            int i = dao.update(oldcct,cct);
            if(i==1){
    			act.setOutData("flag", true);
            }else {
            	act.setOutData("flag", false);
            }
			act.setForword(queryInitUrl);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"修改投诉抱怨客户失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 维修投诉抱怨客户跟踪添加
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void add() throws Exception{
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerCode = logonUser.getDealerCode();
		try{
			String CUSTOMER_NAME =	CommonUtils.checkNull(request.getParamValue("CUSTOMER_NAME"));//客户姓名
			String TRACK_DATE  =  CommonUtils.checkNull(request.getParamValue("track_date"));//跟踪日期
			String PHONE	= CommonUtils.checkNull(request.getParamValue("PHONE"));//电话
			String	GROUP_CODE = CommonUtils.checkNull(request.getParamValue("GROUP_CODE"));//车型
			String	LICENSE_NO = CommonUtils.checkNull(request.getParamValue("LICENSE_NO"));//车牌
			String	TIMES= CommonUtils.checkNull(request.getParamValue("TIMES"));//频次 
			String	COMPLAINT_RECORD = CommonUtils.checkNull(request.getParamValue("COMPLAINT_RECORD"));//投诉纪录 
			String	DEAL_WITH = CommonUtils.checkNull(request.getParamValue("DEAL_WITH"));// 处理人
			String	CUS_REQUEST = CommonUtils.checkNull(request.getParamValue("CUS_REQUEST"));// 客户要求
			String	RESULT = CommonUtils.checkNull(request.getParamValue("RESULT"));// 处理结果
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			//新增的时候	
		   Long id=0L;  
		   TtComplaintCustomerTrackPO cct = new TtComplaintCustomerTrackPO();
           id = Utility.getLong(SequenceManager.getSequence(""));
           cct.setCusId(id);
           cct.setCustomerName(CUSTOMER_NAME);
           cct.setTrackDate(dateFormat.parse(TRACK_DATE));
           cct.setPhone(PHONE);
           cct.setGroupCode(GROUP_CODE);
           cct.setLicenseNo(LICENSE_NO);
           cct.setDealerCode(dealerCode);
           cct.setTimes(TIMES);
           cct.setComplaintRecord(COMPLAINT_RECORD);
           cct.setDealWith(DEAL_WITH);
           cct.setCusRequest(CUS_REQUEST);
           cct.setResult(RESULT);
           cct.setCreateBy(logonUser.getUserId());
           cct.setCreateDate(new Date());         
		   dao.insert(cct);
		   act.setOutData("flag", true);
			
		} catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"维修投诉抱怨客户添加保存失败");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	
	}
	
	/**
	 * 维修投诉抱怨客户跟踪查询(经销商端)
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void Query() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerCode = logonUser.getDealerCode();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");	
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
			    StringBuffer con = new StringBuffer();	
			    String customer_Name = request.getParamValue("customer_Name"); 
				String strDate = request.getParamValue("CON_APPLY_DATE_START");
				String endDate = request.getParamValue("CON_APPLY_DATE_END");
				
				// 客户名称
				if (customer_Name != null && !"".equals(customer_Name)) {
					con.append(" and cct.CUSTOMER_NAME like '%" + customer_Name + "%'");
				}					
				// 创建时间
				if (strDate != null && !"".equals(strDate)) {
					con.append(" and cct.CREATE_DATE >= to_date('" + strDate +" 00:00:00"
							+ "', 'yyyy-mm-dd hh24:mi:ss') ");
				}
				// 结束时间
				if (endDate != null && !"".equals(endDate)) {
					con.append(" and cct.CREATE_DATE <= to_date('" + endDate + " 23:59:59"
							+ "', 'yyyy-mm-dd hh24:mi:ss') ");
				}	
				// 特定经销商
				if (dealerCode != null && !"".equals(dealerCode)) {
					con.append(" and cct.dealer_code='" + dealerCode + "' ");
				}
				PageResult<Map<String, Object>> ps = dao.applyQuery(con.toString(), curPage, 10) ; // 按条件查询维修客户回访
				act.setOutData("ps", ps);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉抱怨客户查询(经销商端)失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
    public void delete(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String CUS_ID = CommonUtils.checkNull(request.getParamValue("CUS_ID"));//客户编号
			int i = dao.del(CUS_ID);
			if(i==1){
			act.setOutData("flag", true);
			}else {
			act.setOutData("flag", false);	
			}				
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"投诉抱怨客户删除(经销商端)失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    	
    }
}
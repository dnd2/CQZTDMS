package com.infodms.dms.actions.customerRelationships.customerInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.customerRelationships.complaint.ComplaintDisposalOEM;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.SeriesBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.SeriesShowDao;
import com.infodms.dms.dao.customerRelationships.CustomerManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtCustomerPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class customerManage {
	private final String AMA_URL = "/jsp/customerRelationships/customerInfo/customerManage.jsp";
	private static Logger logger = Logger.getLogger(ComplaintDisposalOEM.class);
	
	// 客户信息查询初始化页面
	private final String customerManageUrl = "/jsp/customerRelationships/customerInfo/customerManage.jsp";
	//客户信息修改页面
	private final String manageAmendUrl = "/jsp/customerRelationships/customerInfo/manageAmend.jsp";

	/**
	 * 客户信息查询初始化
	 */
	public void customerManageFor(){
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			SeriesShowDao dao = SeriesShowDao.getInstance();
			//获得车系列表
			List<SeriesBean> seriesList = dao.querySeriesFromMG() ;
			
			act.setOutData("seriesList", seriesList);
			act.setForword(customerManageUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户信息查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 根据车系查询车型
	 */
	public void queryModel(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper req = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String id = req.getParamValue("id");
			SeriesShowDao dao = SeriesShowDao.getInstance();
			//获得车型列表
			List<TmVhclMaterialGroupPO> modelList = dao.getModel(id);
			StringBuffer modelStr = new StringBuffer("<select id='model' name='model' class='short_sel'><option value=''>-请选择-</option>");
			for(int i=0;i<modelList.size();i++){
				modelStr.append("<option value=").append(modelList.get(i).getGroupId()).append(">").append(modelList.get(i).getGroupName()).append("</option>");
			}
			modelStr.append("</select>");
			
			act.setOutData("modelStr", modelStr.toString());
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户信息查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 客户信息查询
	 */
	public void customerManageGet(){
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");

			String ctmName = CommonUtils.checkNull(request.getParamValue("ctmName"));//客户姓名
			String mainPhone = CommonUtils.checkNull(request.getParamValue("mainPhone"));//客户电话
			String ctmType = CommonUtils.checkNull(request.getParamValue("ctmType"));//客户类型
			String sex = CommonUtils.checkNull(request.getParamValue("sex"));//性别
			String guestStars = CommonUtils.checkNull(request.getParamValue("guestStars"));//客户星级
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));//购车开始日期
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));//购车结束日期
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//VIN
			String materialName = CommonUtils.checkNull(request.getParamValue("materialName"));//选择物料
			String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));//生产基地
			String seriesId = request.getParamValue("series");//车系
			String modelId = request.getParamValue("model");//车型
			StringBuilder sql= new StringBuilder("\n");
			sql.append("select a.ctm_id ctmId,\n" );
			sql.append("       a.card_num cardNum,\n" );
			sql.append("       a.GUEST_STARS,\n" );
			sql.append("       a.ctm_name ctmName,\n" );
			sql.append("       a.main_phone mainPhone,\n" );
			sql.append("       a.ctm_type ctmType,\n" );
			sql.append("       a.level_id levelId,\n" );
			sql.append("       a.sex,\n" );
			sql.append("       c.Purchased_Date purchasedDate,\n" );
			sql.append("       c.vin,\n" );
			sql.append("       c.Yieldly,\n" );
			sql.append("       c.material_id materialId,\n" );
			sql.append("       g.group_name as model_name,\n" );
			sql.append("       (select group_name\n" );
			sql.append("          from tm_vhcl_material_group\n" );
			sql.append("         where group_id = g.parent_group_id) as series_name\n" );
			sql.append("  from tt_customer              a,\n" );
			sql.append("       TT_DEALER_ACTUAL_SALES   b,\n" );
			sql.append("       tm_vehicle               c,\n" );
			sql.append("       tm_vhcl_material_group   g,\n" );
			sql.append("       tm_vhcl_material_group_r r\n" );
			sql.append(" where a.ctm_id = b.ctm_id\n" );
			sql.append("   and b.Vehicle_Id = c.vehicle_id\n" );
			sql.append("   and c.material_id = r.material_id\n" );
			sql.append("   and g.group_id = (select parent_group_id\n" );
			sql.append("                       from tm_vhcl_material_group\n" );
			sql.append("                      where group_id = r.group_id)\n" );


			if(StringUtil.notNull(ctmName))
				sql.append(" and A.CTM_NAME LIKE '%"+ctmName+"%'\n");
			if(StringUtil.notNull(mainPhone))
				sql.append("and A.MAIN_PHONE LIKE '%"+mainPhone+"%'\n");
			if(StringUtil.notNull(ctmType))
				sql.append("and A.CTM_TYPE = '"+ctmType+"'\n");
			if(StringUtil.notNull(sex))
				sql.append("and A.SEX = '"+sex+"'\n");
			if(StringUtil.notNull(vin))
				sql.append("and C.VIN LIKE '%"+vin+"%'\n");
			if(StringUtil.notNull(guestStars))
				sql.append("and A.GUEST_STARS = '"+guestStars+"'\n");
			if(StringUtil.notNull(yieldly))
				sql.append("and C.YIELDLY = '"+yieldly+"'\n");
			if(StringUtil.notNull(dateStart))
				sql.append("and c.Purchased_Date>=to_date('"+dateStart+" 00:00:00','yyyy-MM-dd hh24:mi:ss') \n");
			if(StringUtil.notNull(dateEnd))
				sql.append("and c.Purchased_Date<=to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \n");
			if(StringUtil.notNull(seriesId))
				sql.append("and g.parent_group_id=").append(seriesId).append("\n");
			if(StringUtil.notNull(modelId))
				sql.append("and g.group_id=").append(modelId).append("\n");
			
			CustomerManageDao cmdao = CustomerManageDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> ps = cmdao.queryComplaintDisposal(sql.toString(),Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户信息查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改客户信息
	 */
	public void manageAmend(){
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			String anaId = request.getParamValue("id"); 

			CustomerManageDao dao = CustomerManageDao.getInstance();
			List<Map<String,Object>> resultList = dao.queryCustomerDetail(Long.parseLong(anaId));
			
			Map<String,Object> resultMap = new HashMap<String, Object>();
			if(resultList!=null && resultList.size()>0)
				resultMap = resultList.get(0);
			
			act.setOutData("customMap", resultMap);
			act.setForword(manageAmendUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户信息修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改客户信息
	 */
	@SuppressWarnings("unchecked")
	public void manageUpdate (){
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			RequestWrapper request = act.getRequest();
			Long ctmId = Long.parseLong(request.getParamValue("CTMID"));
			String guestStars = request.getParamValue("guestStars");
			CustomerManageDao dao = CustomerManageDao.getInstance();
			
			TtCustomerPO m1 = new TtCustomerPO();
			m1.setCtmId(ctmId);
			TtCustomerPO m2 = dao.queryAnaById(m1);
			m2.setGuestStars(guestStars);
			dao.update(m1,m2);
			
			act.setForword(AMA_URL);
		}
		catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户信息修改确认");
			logger.error(logonUser,e1);
			act.setException(e1);
	}
	}
}
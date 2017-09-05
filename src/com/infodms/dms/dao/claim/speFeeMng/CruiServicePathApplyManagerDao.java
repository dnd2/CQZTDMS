package com.infodms.dms.dao.claim.speFeeMng;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.CruiServiceDealerInfoBean;
import com.infodms.dms.bean.CruiServiceDetailInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrCruisePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * 类说明：特殊费用管理--巡航服务线路申请Dao层
 * 作者：  赵伦达
 */
@SuppressWarnings("unchecked")
public class CruiServicePathApplyManagerDao extends BaseDao{


	public static Logger logger = Logger.getLogger(CruiServicePathApplyManagerDao.class);
	
	private static final CruiServicePathApplyManagerDao dao = null;
	
	public static final CruiServicePathApplyManagerDao getInstance() {
	   if(dao==null) return new CruiServicePathApplyManagerDao();
	   return dao;
	}
	/**
	 * Function：根据经销商id返回经销商信息
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-13 Zhaolunda
	 */
	public CruiServiceDealerInfoBean getDealerInfo(Map params){
		String dealer_id=CommonUtils.checkNull(params.get("dealer_id"));
		if("".equals(dealer_id)) return null;
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select a.dealer_id,a.dealer_code,a.dealer_shortname dealer_name,\n" );
		sqlStr.append("nvl(b.region_name,'')  privince_name\n" );
		sqlStr.append("from tm_dealer a,tm_region b\n" );
		sqlStr.append("where a.province_id=b.region_code(+)\n" );
		sqlStr.append("and dealer_id = "+dealer_id);
		List<CruiServiceDealerInfoBean> list = select(CruiServiceDealerInfoBean.class,sqlStr.toString(),null);

		if(list!=null&&list.size()>0){
			return (CruiServiceDealerInfoBean)list.get(0);
		}
		
        return null;
	}
	/**
	 * Function：保存巡航服务信息
	 * @param  ：	
	 * @return:		@param params 
	 * @throw：	
	 * LastUpdate：	2010-7-13 Zhaolunda
	 */
	public String saveCruiServiceInfo(Map params){
		String user_id=CommonUtils.checkNull(params.get("user_id"));
		String dealer_id=CommonUtils.checkNull(params.get("dealer_id"));
		String company_id=CommonUtils.checkNull(params.get("company_id"));
		String crui_aim=CommonUtils.checkNull(params.get("crui_aim"));
		String crui_km=CommonUtils.checkNull(params.get("crui_km"));
		String crui_days=CommonUtils.checkNull(params.get("crui_days"));
		String crui_man=CommonUtils.checkNull(params.get("crui_man"));
		String crui_phone=CommonUtils.checkNull(params.get("crui_phone"));
		String crui_reason=CommonUtils.checkNull(params.get("crui_reason"));
		
		if("".equals(user_id)||"".equals(dealer_id)||"".equals(company_id)){
			return "save_failure_001";//无法获得登陆经销商user_id、dealer_id或company_id
		}
		TtAsWrCruisePO insertObj=new TtAsWrCruisePO();
		insertObj.setId(Long.parseLong(SequenceManager.getSequence("")));
		insertObj.setCrNo(SequenceManager.getSequence("XH"));
		insertObj.setDealerId(Long.parseLong(dealer_id));
		insertObj.setCrMileage(Double.parseDouble(crui_km));
		insertObj.setCrDay(Double.parseDouble(crui_days));
		insertObj.setCrWhither(crui_aim);
		insertObj.setCrPrincipal(crui_man);
		insertObj.setCrPhone(crui_phone);
		insertObj.setCrCause(crui_reason);
		insertObj.setStatus(Constant.CURI_SERVICE_STATUS_01);//未上报
		insertObj.setCreateBy(Long.parseLong(user_id));
		insertObj.setCreateDate(new Date());
		insertObj.setCompanyId(Long.parseLong(company_id));
		insertObj.setIsSpefee(0);
		insert(insertObj);
		return "save_success";
	}
	/**
	 * Function：获得未上报巡航服务路线数据
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@param curPage
	 * @return:		@param pageSize
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-13 赵伦达
	 */
	public PageResult<TtAsWrCruisePO> getUnReportCruiInfo(Map params,int curPage, int pageSize){
		String dealer_id=CommonUtils.checkNull(params.get("dealer_id"));
		String company_id=CommonUtils.checkNull(params.get("company_id"));
		String xh_order_no=CommonUtils.checkNull(params.get("xh_order_no"));
		String create_start_date=CommonUtils.checkNull(params.get("create_start_date"));
		String create_end_date=CommonUtils.checkNull(params.get("create_end_date"));
		String xh_aim_area=CommonUtils.checkNull(params.get("xh_aim_area"));

		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select * from TT_AS_WR_CRUISE tawc\n" );
		sqlStr.append("where (tawc.status="+Constant.CURI_SERVICE_STATUS_01+"\n" );
		sqlStr.append("or tawc.status="+Constant.CURI_SERVICE_STATUS_03+"\n" );
		sqlStr.append("or tawc.status="+Constant.CURI_SERVICE_STATUS_08+")\n" );
		if(!"".equals(xh_order_no)){
			sqlStr.append("and tawc.cr_no like'%"+xh_order_no.toUpperCase()+"%'\n" );
		}
		if(!"".equals(create_start_date)){
			sqlStr.append(" and tawc.create_date>=to_date('" + create_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(!"".equals(create_end_date)){
			sqlStr.append(" and tawc.create_date<=to_date('" + create_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(!"".equals(xh_aim_area)){
			sqlStr.append("and tawc.cr_whither like'%"+xh_aim_area+"%'\n" );
		}
		sqlStr.append("and tawc.dealer_id="+dealer_id+"\n" );
		sqlStr.append("and tawc.company_id="+company_id+"\n");
		sqlStr.append("order by tawc.id desc\n");
		
		PageResult<TtAsWrCruisePO> pr=pageQuery(TtAsWrCruisePO.class,
				sqlStr.toString(),null, pageSize, curPage);
		return pr;
	}
	/**
	 * Function：获得巡航服务路线修改数据
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@param curPage
	 * @return:		@param pageSize
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-13 赵伦达
	 */
	public CruiServiceDetailInfoBean getCruiModifyInfo(Map params){
		String ord_id=CommonUtils.checkNull(params.get("ord_id"));//工单id
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select tawc.id,td.dealer_code,td.dealer_shortname dealer_name,\n" );
		sqlStr.append("to_char(tawc.create_date,'YYYY-MM-DD') make_date,tawc.cr_whither,\n" );
		sqlStr.append("tawc.cr_mileage,tawc.cr_day,tawc.cr_principal,tawc.cr_phone,\n" );
		sqlStr.append("tawc.cr_cause\n" );
		sqlStr.append("from TT_AS_WR_CRUISE tawc,tm_dealer td\n" );
		sqlStr.append("where tawc.dealer_id=td.dealer_id\n" );
		sqlStr.append("and tawc.id="+ord_id);

		List<CruiServiceDetailInfoBean> list=select(CruiServiceDetailInfoBean.class, sqlStr.toString(),null);
		if(list!=null&&list.size()>0){
			return (CruiServiceDetailInfoBean)list.get(0);
		}else{
			return null;
		}
	}
	/**
	 * Function：修改保存巡航路线信息
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-14 赵伦达
	 */
	public String modifyAndSaveCruiDetailInfo(Map params){
		int retNum=0;
		
		String ord_id=CommonUtils.checkNull(params.get("ord_id"));
		String user_id=CommonUtils.checkNull(params.get("user_id"));
		String crui_aim=CommonUtils.checkNull(params.get("crui_aim"));
		String crui_km=CommonUtils.checkNull(params.get("crui_km"));
		String crui_days=CommonUtils.checkNull(params.get("crui_days"));
		String crui_man=CommonUtils.checkNull(params.get("crui_man"));
		String crui_phone=CommonUtils.checkNull(params.get("crui_phone"));
		String crui_reason=CommonUtils.checkNull(params.get("crui_reason"));
		
		TtAsWrCruisePO updateObj=new TtAsWrCruisePO();
		updateObj.setId(Long.parseLong(ord_id));
		
		TtAsWrCruisePO vo=new TtAsWrCruisePO();
		vo.setCrMileage(Double.parseDouble(crui_km));
		vo.setCrDay(Double.parseDouble(crui_days));
		vo.setCrWhither(crui_aim);
		vo.setCrPrincipal(crui_man);
		vo.setCrPhone(crui_phone);
		vo.setCrCause(crui_reason);
		vo.setUpdateBy(Long.parseLong(user_id));
		vo.setUpdateDate(new Date());
		
		retNum=update(updateObj, vo);
		if(retNum==1){
			return "modify_success";
		}else{
			return "modify_failure";
		}
		
	}
	/**
	 * Function：删除巡航路线信息
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-14 赵伦达
	 */
	public String delCruiInfo(Map params){
        int retNum=0;
		
		String orderIds=CommonUtils.checkNull(params.get("orderIds"));
		String user_id=CommonUtils.checkNull(params.get("user_id"));
		String[] idArr=orderIds.split(",");
		for(int count=0;count<idArr.length;count++){
			TtAsWrCruisePO updateObj=new TtAsWrCruisePO();
			updateObj.setId(Long.parseLong(idArr[count]));
			
			TtAsWrCruisePO vo=new TtAsWrCruisePO();
			vo.setStatus(Constant.CURI_SERVICE_STATUS_06);//设置已删除标志
			vo.setUpdateBy(Long.parseLong(user_id));
			vo.setUpdateDate(new Date());
			retNum+=update(updateObj, vo);
		}
		
		if(retNum==idArr.length){
			return "del_success";
		}else{
			return "del_failure";
		}
	}
	/**
	 * Function：上报巡航路线信息
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-14 赵伦达
	 */
	public String reportCruiInfo(Map params){
        int retNum=0;
		
		String orderIds=CommonUtils.checkNull(params.get("orderIds"));
		String user_id=CommonUtils.checkNull(params.get("user_id"));
		String[] idArr=orderIds.split(",");
		for(int count=0;count<idArr.length;count++){
			TtAsWrCruisePO updateObj=new TtAsWrCruisePO();
			updateObj.setId(Long.parseLong(idArr[count]));
			
			TtAsWrCruisePO vo=new TtAsWrCruisePO();
			vo.setMakeDate(new Date());
			vo.setStatus(Constant.CURI_SERVICE_STATUS_02);//设置已删除标志
			vo.setUpdateBy(Long.parseLong(user_id));
			vo.setUpdateDate(new Date());
			retNum+=update(updateObj, vo);
		}
		
		if(retNum==idArr.length){
			return "report_success";
		}else{
			return "report_failure";
		}
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
}

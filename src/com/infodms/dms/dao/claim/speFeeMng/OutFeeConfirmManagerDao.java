package com.infodms.dms.dao.claim.speFeeMng;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.SpeFeeApproveDetailInfoBean;
import com.infodms.dms.bean.SpeFeeApproveLogListBean;
import com.infodms.dms.bean.SpeFeeConfirmInfoListBean;
import com.infodms.dms.bean.SpeFeeVehicleListInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsWrSpeoutfeeAuditingPO;
import com.infodms.dms.po.TtAsWrSpeoutfeePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class OutFeeConfirmManagerDao extends BaseDao{

    public static Logger logger = Logger.getLogger(OutFeeConfirmManagerDao.class);
	
	private static final OutFeeConfirmManagerDao dao = null;
	
	public static final OutFeeConfirmManagerDao getInstance() {
	   if(dao==null) return new OutFeeConfirmManagerDao();
	   return dao;
	}
	
	public PageResult<SpeFeeConfirmInfoListBean> querySpeFeeConfirmInfo(Map params,int curPage, int pageSize){
		String company_id=CommonUtils.checkNull(params.get("company_id"));
		String ts_order_no=CommonUtils.checkNull(params.get("ts_order_no"));
		String xh_order_no=CommonUtils.checkNull(params.get("xh_order_no"));
		String dealer_code=CommonUtils.checkNull(params.get("dealerCode"));
		String dealer_name=CommonUtils.checkNull(params.get("dealerName"));
		String report_start_date=CommonUtils.checkNull(params.get("report_start_date"));
		String report_end_date=CommonUtils.checkNull(params.get("report_end_date"));

		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select taws.id,taws.cr_id,taws.fee_no,tawc.cr_no,td.dealer_code,td.dealer_shortname dealer_name,\n" );
		sqlStr.append("to_char(taws.make_date,'YYYY-MM-DD') make_date,tawc.cr_whither,\n" );
		sqlStr.append("taws.single_mileage+taws.pass_fee+taws.traffic_fee+taws.quarter_fee+taws.eat_fee+taws.person_subside total_fee\n" );
		sqlStr.append("from tt_as_wr_speoutfee taws,tt_as_wr_cruise tawc,tm_dealer td\n" );
		sqlStr.append("where taws.cr_id=tawc.id\n" );
		sqlStr.append("and taws.dealer_id=td.dealer_id\n");

		if(!"".equals(ts_order_no)){
			sqlStr.append("and taws.fee_no like'%"+ts_order_no+"%'\n" );
		}
		if(!"".equals(xh_order_no)){
			sqlStr.append("and tawc.cr_no like'%"+xh_order_no+"%'\n" );
		}
		if(dealer_code!=null&&!"".equals(dealer_code)){
			String[] temp=dealer_code.split(",");
			String str="";
			if(temp.length>0){
				sqlStr.append(" and (");
				for(int count=0;count<temp.length;count++){
					str+=" td.dealer_code='"+temp[count]+"' or\n";
				}
				sqlStr.append(str.substring(0, str.length()-3));
				sqlStr.append(")\n");
			}
		}
		if(dealer_name!=null&&!"".equals(dealer_name))
			sqlStr.append(" and td.dealer_shortname like '%"+dealer_name+"%'\n");
		if(!"".equals(report_start_date)){
			sqlStr.append(" and taws.make_date>=to_date('" + report_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(!"".equals(report_end_date)){
			sqlStr.append(" and taws.make_date<=to_date('" + report_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		sqlStr.append(" and taws.status="+Constant.SPE_OUTFEE_STATUS_02+"\n" );
		sqlStr.append(" and tawc.company_id="+company_id+"\n");
		sqlStr.append(" order by taws.id desc\n");
		
		PageResult<SpeFeeConfirmInfoListBean> pr=pageQuery(SpeFeeConfirmInfoListBean.class,
				sqlStr.toString(),null, pageSize, curPage);
		return pr;
	}
	public SpeFeeApproveDetailInfoBean getSpeFeeApproveDetailInfo(Map params){
		String order_id=CommonUtils.checkNull(params.get("order_id"));
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select taws.id,taws.cr_id,taws.fee_no,tawc.cr_no,td.dealer_code,td.dealer_shortname dealer_name,\n" );
		sqlStr.append("to_char(taws.make_date,'YYYY-MM-DD') make_date,taws.person_num,taws.person_name,tawc.cr_whither,taws.single_mileage,\n" );
		sqlStr.append("decode(taws.start_date-taws.end_date,0,'1',abs(taws.start_date-taws.end_date)+1) out_days,\n" );
		sqlStr.append("to_char(taws.start_date,'YYYY-MM-DD') start_date,to_char(taws.end_date,'YYYY-MM-DD') end_date,\n" );
		sqlStr.append("taws.pass_fee,taws.traffic_fee,taws.quarter_fee,taws.eat_fee,taws.person_subside,\n" );
		sqlStr.append("taws.pass_fee+taws.traffic_fee+taws.quarter_fee+taws.eat_fee+taws.person_subside total_fee,\n" );
		sqlStr.append("taws.apply_content\n" );
		sqlStr.append("from tt_as_wr_speoutfee taws,tt_as_wr_cruise tawc,tm_dealer td\n" );
		sqlStr.append("where taws.cr_id=tawc.id\n" );
		sqlStr.append("and taws.dealer_id=td.dealer_id\n");
		sqlStr.append("and taws.id="+order_id);
		
		List list=select(SpeFeeApproveDetailInfoBean.class, sqlStr.toString(), null);
		if(list!=null&&list.size()>0){
			return (SpeFeeApproveDetailInfoBean)list.get(0);
		}else{
			return null;
		}
	}
	/**
     * Function：获得附件信息列表
     * @param  ：	
     * @return:		@param id
     * @return:		@return 
     * @throw：	
     * LastUpdate：	2010-7-16
     */
    public List<FsFileuploadPO> queryAttachFileInfo(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A ");
		sql.append(" WHERE A.YWZJ=(select id from Tt_As_Wr_Speoutfee where id='"+id+"')");
		List<FsFileuploadPO> ls= select (FsFileuploadPO.class,sql.toString(),null);
		return ls;
	}
    /**
     * Function：获得特殊费用车辆信息列表
     * @param  ：	
     * @return:		@param params
     * @return:		@return 
     * @throw：	
     * LastUpdate：	2010-7-16
     */
    public List<SpeFeeVehicleListInfoBean> querySpeVehicleListInfo(Map params){
    	String order_id=CommonUtils.checkNull(params.get("order_id"));
    	StringBuffer sqlStr= new StringBuffer();
    	sqlStr.append("select tt.id,tt.fee_id,tt.vin,tt.engine_no,\n" );
    	sqlStr.append("tt.model,to_char(tt.product_date,'YYYY-MM-DD') product_date,\n" );
    	sqlStr.append("to_char(tt.sale_date,'YYYY-MM-DD') sale_date,tt.mileage,\n" );
    	sqlStr.append("tt.customer_name,tt.customer_phone,tt.remark\n" );
    	sqlStr.append("from Tt_As_Wr_Speoutfee_Vehicle tt\n" );
    	sqlStr.append("where tt.fee_id=(select id from Tt_As_Wr_Speoutfee where id="+order_id+")\n");
    	sqlStr.append("order by tt.id");
    	List<SpeFeeVehicleListInfoBean> list= select (SpeFeeVehicleListInfoBean.class,sqlStr.toString(),null);
    	return list;
    }
    /**
     * Function：审核特殊费用工单数据操作
     * @param  ：	
     * @return:		@param params
     * @return:		@return 
     * @throw：	
     * LastUpdate：	2010-7-20 赵伦达
     */
    public String approveSpeFeeDataOper(Map params){
    	String user_id=CommonUtils.checkNull(params.get("user_id"));
    	String org_id=CommonUtils.checkNull(params.get("org_id"));
    	String order_id=CommonUtils.checkNull(params.get("order_id"));
    	String approve_status=CommonUtils.checkNull(params.get("approve_status"));
    	String approve_content=CommonUtils.checkNull(params.get("approve_content"));
    	int updateNum=0;
    	
    	if("".equals(order_id)) return "failure_001";//无法获得审批订单号
    	if("".equals(user_id)||"".equals(org_id)) return "failure_002";//无法获得审批人信息
    	
    	TtAsWrSpeoutfeePO updateObj=new TtAsWrSpeoutfeePO();
    	updateObj.setId(Long.parseLong(order_id));
    	
    	TtAsWrSpeoutfeePO vo=new TtAsWrSpeoutfeePO();
    	vo.setStatus(Integer.parseInt(approve_status));
    	vo.setAuditingOpinion(approve_content);
    	vo.setUpdateBy(Long.parseLong(user_id));
    	vo.setUpdateDate(new Date());
    	updateNum=update(updateObj,vo);
    	if(updateNum==1){
    		TtAsWrSpeoutfeeAuditingPO insertObj=new TtAsWrSpeoutfeeAuditingPO();
    		insertObj.setId(Long.parseLong(SequenceManager.getSequence("")));
    		insertObj.setFeeId(Long.parseLong(order_id));
    		insertObj.setAuditingDate(new Date());
    		insertObj.setAuditingPerson(Long.parseLong(user_id));
    		insertObj.setPresonDept(Long.parseLong(org_id));
    		insertObj.setAuditingOpinion(approve_content);
    		insertObj.setStatus(Integer.parseInt(approve_status));
    		insertObj.setCreateBy(Long.parseLong(user_id));
    		insertObj.setCreateDate(new Date());
    		insert(insertObj);
    	}
    	return "success";
    }
    /**
     * Function：获得特殊费用审核意见日志
     * @param  ：	
     * @return:		@param order_id
     * @return:		@return 
     * @throw：	
     * LastUpdate：	2010-7-20 赵伦达
     */
    public List<SpeFeeApproveLogListBean> getApproveLogList(String order_id){
    	if(order_id==null||"".equals(order_id)) return null;
    	StringBuffer sqlStr= new StringBuffer();
    	sqlStr.append("select to_char(tawsa.auditing_date,'YYYY-MM-DD') auditing_date,\n" );
    	sqlStr.append("tu.name user_name,tmo.org_name dept_name,tc.code_desc audit_status,tawsa.auditing_opinion\n" );
    	sqlStr.append("from tt_as_wr_speoutfee_auditing tawsa,tc_user tu,tm_org tmo,tc_code tc\n" );
    	sqlStr.append("where tawsa.auditing_person=tu.user_id and tawsa.preson_dept=tmo.org_id\n" );
    	sqlStr.append("and tawsa.status=tc.code_id\n" );
    	sqlStr.append("and tawsa.fee_id="+order_id+"\n" );
    	sqlStr.append("order by tawsa.id desc");
    	
    	List<SpeFeeApproveLogListBean> list=select(SpeFeeApproveLogListBean.class, sqlStr.toString(), null);
    	return list;
    }
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

}

package com.infodms.dms.dao.claim.oldPart;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.TtAsWrBackListQryBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrOldReturnedPO;
import com.infodms.dms.po.TtAsWrReturnedOrderPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * 类说明：索赔旧件管理--索赔件回运清单上报
 * 作者：  赵伦达
 * 日期：2010-06-10
 */
@SuppressWarnings("unchecked")
public class ClaimReturnReportDao extends BaseDao{
	
	public static Logger logger = Logger.getLogger(ClaimReturnReportDao.class);
    private static final ClaimReturnReportDao dao = null;
	
	public static final ClaimReturnReportDao getInstance() {
	   if(dao==null) return new ClaimReturnReportDao();
	   return dao;
	}
	/**
	 * Function：查询所有回运单状态
	 * @param  ：	
	 * @return:	
	 * @throw：	
	 * LastUpdate：	2010-6-8
	 */
	public PageResult<TtAsWrBackListQryBean> queryClaimBackList(String sqlStr,int curPage, int pageSize){
		PageResult<TtAsWrBackListQryBean> pr = pageQuery(TtAsWrBackListQryBean.class,sqlStr,null, pageSize, curPage);
		return pr;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function：修改索赔回运清单表信息
	 * @param  ：	
	 * @return:		@param id
	 * @return:		@param vo
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-10
	 */
	public int updateClaimBackOrdMainInfo(String id,TtAsWrReturnedOrderPO vo){
		int updateNum=0;
		TtAsWrReturnedOrderPO data=new TtAsWrReturnedOrderPO();
		data.setId(Long.parseLong(id));
		updateNum=super.update(data, vo);
		return updateNum;
	}
	/**
	 * Function：检查回运单是否填写货运单号
	 * @param  ：	
	 * @return:		@param id
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-8-6
	 */
	public String checkReturnOrdIsFillTransNo(String id){
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select * from tt_as_wr_old_returned where id="+id);
		List<TtAsWrOldReturnedPO> list=select(TtAsWrOldReturnedPO.class, sqlStr.toString(), null);
		if(list!=null&&list.size()>0){
			TtAsWrOldReturnedPO data=(TtAsWrOldReturnedPO)list.get(0);
			if(data.getTranNo()!=null&&!"".equals(data.getTranNo())){
				return "1";//填写货运单号
			}else{
				return "0";
			}
		}else{
			return "0";
		}
	}
	/********
	 * 查询回运通知单-----打印功能
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getReturnNo(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("select o.*,d.dealer_code,d.dealer_shortname ddealer_shortname,ba.area_name,d.phone\n");
		sql.append(" from tt_as_wr_returned_order o,tm_dealer d,tm_business_area ba  where o.id="+id+"\n" );
		sql.append(" and o.yieldly = ba.area_id(+) and o.dealer_id = d.dealer_id");
		List<Map<String, Object>> list =pageQuery(sql.toString(),null,getFunName());
		
		return list;
	}
	
	/***************Iverson by 2010-11-02 查找旧件起始时间*************************/
	public List<Map<String, Object>> getReturnNo1(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("select ro.wr_start_date from tt_as_wr_returned_order ro " );
		sql.append(" where ro.id in (select distinct od.dealer_return_id ");
		sql.append(" from tt_as_wr_old_returned_detail od ");
		sql.append(" where od.return_id = "+id+") ");
		System.out.println("sql+"+sql);
		List<Map<String, Object>> list =pageQuery(sql.toString(),null,getFunName());
		
		return list;
	}
	
/**
	 * Iverson add By 2010-11-23 添加打印条件,根据装箱单号打印(sql.append("   and d.box_no='"+boxNo+"'");)
	 * 查询回运通知单明细-----打印功能
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getReturnNoDetail(String id,String boxNo){
		 
		  StringBuffer sql= new StringBuffer();
		        //b.main_part_code,b.remark bmark,WA.MODEL_CODE,
				sql.append("select d.*,wa.dealer_id,del.dealer_code,del.dealer_shortname,del.phone,vmgm.model_name,b.old_part_cname part_name, b.old_part_code part_code,wa.apply_remark remark,to_char(wa.auth_audit_date,'yyyy-mm-dd') as AUDITING_DATE,to_char(wa.repair_date_begin,'yyyy-mm-dd') AS CLAIM_DATE\n");
				sql.append("  from TT_AS_WR_RETURNED_ORDER_DETAIL d,tt_as_wr_app_part b,tt_as_wr_application_claim wa,TM_DEALER del,tm_vehicle tv,\n" );
				sql.append("       (select distinct package_id, package_name, model_id, model_name, series_name ,brand_name from vw_material_group_mat) vmgm\n");
				sql.append(" where d.claim_part_id = b.claim_part_id\n" );
				sql.append("   and wa.vin = tv.vin\n" );
				sql.append("   and tv.package_id = vmgm.package_id\n" );
				sql.append("   and d.return_id ="+id+"\n" );
				sql.append("   AND del.DEALER_ID=wa.dealer_id" );
				sql.append("   and wa.id=d.claim_id");
				sql.append("   and d.IS_SIGN=0");
				if(!"".equals(boxNo) && boxNo!=null){
					sql.append("   and d.box_no='"+boxNo+"'");
				}
				sql.append("   ORDER BY d.box_no,wa.app_claim_no,b.old_part_cname,d.barcode_no");
		
		System.out.println("sql:"+sql);
		List<Map<String, Object>> listDetail = pageQuery(sql.toString(),null,getFunName());
		return listDetail;
	}
	/**
	 * 根据装箱号,确定装箱总数
	 * @param id
	 * @return
	 */
	public List<TtAsWrOldReturnedDetailPO> getPackageAmount(Long id){
		
		StringBuffer  sql = new StringBuffer();
		sql.append(" select box_no from tt_as_wr_old_returned_detail d where d.return_id="+id+ "  group by d.box_no\n");
		List<TtAsWrOldReturnedDetailPO> list = this.pageQuery(sql.toString(), null, this.getFunName());
		
		return list;
	}
}

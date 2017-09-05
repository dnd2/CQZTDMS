package com.infodms.dms.dao.claim.oldPart;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.TtAsWrBackListQryBean;
import com.infodms.dms.bean.TtAsWrOldPartBackListDetailBean;
import com.infodms.dms.bean.TtAsWrOldPartDetailListBean;
import com.infodms.dms.bean.TtAsWrPartsitemApplyBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.po.TtDeliveryOrderDetailPO;
import com.infodms.dms.po.TtDeliveryOrderPO;
import com.infodms.dms.po.TmAsWrBarcodePartStockPO;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrOldReturnedPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.po.TtAsWrReturnedOrderDetailPO;
import com.infodms.dms.po.TtAsWrReturnedOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 类说明：
 * 作者：  赵伦达
 */
@SuppressWarnings("unchecked")
public class ClaimoldPartReturnApplyDao extends BaseDao{
	
	public static Logger logger = Logger.getLogger(ClaimBackListDao.class);
	private static final ClaimoldPartReturnApplyDao dao = null;
	
	public static final ClaimoldPartReturnApplyDao getInstance() {
	   if(dao==null) return new ClaimoldPartReturnApplyDao();
	   return dao;
	}
	//查询旧件回运申请单
	 public PageResult<Map<String, Object>> queryOldPartReturnApply(RequestWrapper req,String dealerCode,
			 String returnApplyNo,String status,
			 int curPage, int pageSize){

			StringBuffer sb = new StringBuffer();
			sb.append("SELECT TORA.RETURN_APPLY_ID,\n");
			sb.append("       TORA.RETURN_APPLY_NO,TORA.DEALER_ID,\n");
			sb.append("       TORA.CREATE_DATE,\n");
			sb.append("       TORA.CREATE_BY,\n");
			sb.append("       TORA.UPDATE_DATE,\n");
			sb.append("       TORA.UPDATE_BY,\n");
			sb.append("       TORA.CHECK_USER,\n");
			sb.append("       TORA.CHECK_DATE,\n");
			sb.append("       TORA.REPORT_DATE,\n");
			sb.append("       TUR.NAME AS REPORT_USER,\n");
			sb.append("       TORA.STATUS,\n");
			sb.append("       TORA.SEND_NO,\n");
			sb.append("       TORA.SEND_DATE,\n");
			sb.append("       TUS.NAME AS SEND_USER,\n");
			sb.append("       TORA.SEND_LINK_USER,\n");
			sb.append("       TORA.SEND_LINK_PHONE,\n");
			sb.append("       TD.DEALER_CODE,\n");
			sb.append("       TD.DEALER_SHORTNAME\n");
			sb.append("  FROM TM_OLDPART_RETURN_APPLY TORA, TM_DEALER TD,TC_USER TUR,TC_USER TUS\n");
			sb.append(" WHERE TORA.DEALER_ID = TD.DEALER_ID(+)\n");
			sb.append("AND TUR.USER_ID(+)=TORA.REPORT_USER AND TUS.USER_ID(+)=TORA.SEND_USER\n");

			if(dealerCode!=null&&!"".equals(dealerCode)){
				
				sb.append(" and TD.dealer_code='"+dealerCode+"'");
			}
			if(returnApplyNo!=null&&!"".equals(returnApplyNo)){
				
				sb.append(" and TORA.RETURN_APPLY_NO like '%"+returnApplyNo+"%'");
			}
			if(status!=null&&!"".equals(status)){
				
				sb.append(" and TORA.status="+status);
			}else{
				if(dealerCode==null||"".equals(dealerCode)){  //车厂端运输状态选全部，只能查审核已提交和已通过的
					
					sb.append(" and TORA.status in ("+Constant.SP_JJ_RETURN_APPLY_STATUS_02+","+Constant.SP_JJ_RETURN_APPLY_STATUS_03+","+Constant.SP_JJ_RETURN_APPLY_STATUS_04+")");
				}
			}
			sb.append(" order by TORA.create_date desc\n");

			
			PageResult<Map<String, Object>> ps = pageQuery(sb.toString(), null, getFunName(), 15, curPage);
	        return ps;
	    }
	 //通过Id查回运申请单
	 public HashMap<String, Object> queryOldPartReturnApplyById(RequestWrapper req,String returnApplyId){

	        StringBuffer sb = new StringBuffer();
			sb.append("SELECT TORA.RETURN_APPLY_ID,\n");
			sb.append("       TORA.RETURN_APPLY_NO,TORA.DEALER_ID,\n");
			sb.append("       TORA.CREATE_DATE,\n");
			sb.append("       TORA.CREATE_BY,\n");
			sb.append("       TORA.UPDATE_DATE,\n");
			sb.append("       TORA.UPDATE_BY,\n");
			sb.append("       TORA.CHECK_USER,\n");
			sb.append("       TORA.CHECK_DATE,\n");
			sb.append("       TORA.REPORT_DATE,\n");
			sb.append("       TUR.NAME AS REPORT_USER,\n");
			sb.append("       TORA.STATUS,\n");
			sb.append("       TORA.SEND_NO,\n");
			sb.append("       TORA.SEND_DATE,\n");
			sb.append("       TUS.NAME AS SEND_USER,\n");
			sb.append("       TORA.SEND_LINK_USER,\n");
			sb.append("       TORA.SEND_LINK_PHONE,\n");
			sb.append("       TD.DEALER_CODE,\n");
			sb.append("       TD.DEALER_SHORTNAME\n");
			sb.append("  FROM TM_OLDPART_RETURN_APPLY TORA, TM_DEALER TD,TC_USER TUR,TC_USER TUS\n");
			sb.append(" WHERE TORA.DEALER_ID = TD.DEALER_ID(+)\n");
			sb.append("AND TUR.USER_ID(+)=TORA.REPORT_USER AND TUS.USER_ID(+)=TORA.SEND_USER\n");
			if(returnApplyId!=null&&!"".equals(returnApplyId)){
				
				sb.append(" and TORA.RETURN_APPLY_ID="+returnApplyId.trim()+"");
			}

			HashMap<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> relist = pageQuery(sb.toString(), null,getFunName());
			if(relist != null && relist.size() > 0){
				map = (HashMap)relist.get(0);
			}
			return map;
	    }
	     //通过Id查回运申请单明细
		 public List<Map<String, Object>> queryOldPartReturnApplyDetailById(RequestWrapper req,String returnApplyId){

		        StringBuffer sb = new StringBuffer();

				sb.append("SELECT TOR.DETAIL_ID,\n");
				sb.append("       TOR.RETURN_NO,\n");
				sb.append("       TOR.RETURN_DETAIL_ID,\n");
				sb.append("       TOR.PART_CODE,\n");
				sb.append("       TOR.PART_NAME,\n");
				sb.append("       TOR.CLAIM_NO,\n");
				sb.append("       TOR.BARCODE_NO,\n");
				sb.append("       TOR.DEDUCT_REMARK AS DEDUCT_REMARK_CODE,\n");
				sb.append("       TC.CODE_DESC AS DEDUCT_REMARK,\n");
				sb.append("       TOR.IS_AGREE,\n");
				sb.append("       TOR.RETURN_APPLY_ID,\n");
				sb.append("       TOR.REMARK\n");
				sb.append("  FROM tm_oldpart_return_apply_detail TOR, TC_CODE TC\n");
				sb.append(" WHERE TOR.DEDUCT_REMARK=TC.CODE_ID(+)");
				if(returnApplyId!=null&&!"".equals(returnApplyId)){
					
					sb.append(" and TOR.RETURN_APPLY_ID="+returnApplyId.trim()+"");
				}

				List<Map<String, Object>> result = super.pageQuery(sb.toString(),null, this.getFunName());
				
				return result;
				
				/*HashMap<String, Object> map = new HashMap<String, Object>();
				List<Map<String, Object>> relist = pageQuery(sb.toString(), null,getFunName());
				if(relist != null && relist.size() > 0){
					map = (HashMap)relist.get(0);
				}
				return map;*/
		    }
		//通过returnDetailId查回运申请单和明细
		 public int queryOldPartReturnApplyDetailByDetailId(RequestWrapper req,String returnDetailId,String dealerId){

				StringBuffer sb = new StringBuffer();
				sb.append("SELECT TOR.DETAIL_ID\n");
				sb.append("  FROM tm_oldpart_return_apply_detail TOR, Tm_Oldpart_Return_Apply ORA\n");
				sb.append(" WHERE TOR.RETURN_APPLY_ID = ORA.RETURN_APPLY_ID\n");
				sb.append("   AND ORA.DEALER_ID="+dealerId+"\n");
				sb.append("   AND ORA.STATUS in ("+Constant.SP_JJ_RETURN_APPLY_STATUS_02+","+Constant.SP_JJ_RETURN_APPLY_STATUS_03+","+Constant.SP_JJ_RETURN_APPLY_STATUS_04+")\n");
				sb.append("   AND TOR.RETURN_DETAIL_ID ="+returnDetailId+"\n");

				List rsList=pageQuery(sb.toString(), null, getFunName());
				return rsList.size();
				
		    }
	 //查询旧件回运列表
	 public PageResult<Map<String, Object>> queryOldPartReturnList(RequestWrapper req,String dealerCode,
			 String returnNo,String partCode,String[] rdi,
			 int curPage, int pageSize){

			StringBuffer sb = new StringBuffer();
			sb.append("SELECT r.id,\n");
			sb.append("       r.dealer_id,\n");
			sb.append("       r.return_no,\n");
			sb.append("       d.part_code,\n");
			sb.append("       d.id as retoldpart_id,\n");
			sb.append("       d.part_name,\n");
			sb.append("       d.claim_no,\n");
			sb.append("       d.barcode_no,\n");
			sb.append("       d.deduct_remark,\n");
			sb.append("       C.CODE_DESC\n");
			sb.append("  FROM Tt_As_Wr_Old_Returned r, tt_as_wr_old_returned_detail d, TC_CODE C,TM_DEALER TD\n");
			sb.append(" WHERE r.ID = d.return_id\n");
			//sb.append("   AND C.CODE_ID <> "+Constant.OLDPART_DEDUCT_TYPE_01+"\n");
			sb.append("   AND D.DEDUCT_REMARK = C.CODE_ID(+)\n");
			sb.append("   AND d.sign_amount = 0\n");
			sb.append("   AND (d.deduct_remark IS NOT NULL OR d.deduct_remark = 0)\n");
			sb.append("   AND r.dealer_id=TD.dealer_id(+)");
			sb.append("   AND D.ID NOT IN\n");
			sb.append(" (SELECT ORAD.RETURN_DETAIL_ID\n");
			sb.append("          FROM tm_oldpart_return_apply_detail ORAD,tm_oldpart_return_apply ORA WHERE\n");
			sb.append(" ORAD.RETURN_APPLY_ID=ORA.RETURN_APPLY_ID(+) AND ORA.STATUS IN ("+Constant.SP_JJ_RETURN_APPLY_STATUS_02+","+Constant.SP_JJ_RETURN_APPLY_STATUS_03+","+Constant.SP_JJ_RETURN_APPLY_STATUS_04+"))");


			if(dealerCode!=null&&!"".equals(dealerCode)){
				
				sb.append(" and TD.dealer_code='"+dealerCode+"'");
			}
			if(returnNo!=null&&!"".equals(returnNo)){
				
				sb.append(" and r.return_no like '%"+returnNo+"%'");
			}
			if(partCode!=null&&!"".equals(partCode)){
				
				sb.append(" and d.part_code="+partCode+"");
			}
			if(rdi!=null&&!"".equals(rdi)&&!"".equals(setArrToStrByReg(rdi, ",", false))){
				
				sb.append(" and d.id not in ("+setArrToStrByReg(rdi, ",", false)+")");
			}
			sb.append(" ORDER BY r.create_date DESC\n");


			
			PageResult<Map<String, Object>> ps = pageQuery(sb.toString(), null, getFunName(), 15, curPage);
	        return ps;
	    }
	 
	//修改车厂保存回运申请单状态为不同意xxxx
	public void updateReturnApplyStatus(String returnApplyId,String[] detailId){

		StringBuffer sb = new StringBuffer();
		sb.append("update tm_oldpart_return_apply_detail tord set tord.is_agree="+Constant.SP_JJ_RETURN_APPLY_ISAGREE_02+"\n");
		sb.append(" where tord.return_apply_id = "+returnApplyId+"\n");
		if(!"".equals(setArrToStrByReg(detailId, ",", true))){
			sb.append("   and tord.detail_id not in ("+setArrToStrByReg(detailId, ",", true)+")\n");
		}
		
		dao.update(sb.toString(),new ArrayList());
	}
	 
	 public void delOldPartReturnApplysport(RequestWrapper req,String dealerCode,String returnApplyId){

			StringBuffer sb = new StringBuffer();
			sb.append("delete from tm_oldpart_return_apply_detail tord\n");
			sb.append(" where 1=1 \n");
			sb.append(" and tord.return_apply_id = "+returnApplyId);
			
			factory.delete(sb.toString(), null);
	    }
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	 * 数组转换REG分隔字符串
	 * str:要分隔的字符串
	 * reg:分隔符
	 * flag:true>字符串;false>数值
	 */
	
	public static String setArrToStrByReg(Object[] str,String reg,boolean flag){
		
		String newStr = "";
		if(str==null||str.length<=0){
			return newStr;
		}
		for(int i = 0; i < str.length; i++){
			
			if(flag){
				if(str[i]!=null){
					if("".equals(newStr)){
						newStr = newStr + "'";
					}
					newStr = newStr + str[i] + "'" + reg + "'";	
				}
			}else{
				if(str[i]!=null){
					newStr = newStr + str[i] + reg;	
				}
			}
			
		}
		if(flag){
			if(!"".equals(newStr)){
				newStr = newStr.substring(0, newStr.length()-2);
			}
		}else{
			if(!"".equals(newStr)){
				newStr = newStr.substring(0, newStr.length()-1);
			}
		}
		return newStr;
	}
	 
}

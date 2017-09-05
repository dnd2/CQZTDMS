package com.infodms.dms.dao.claim.oldPart;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.yxdms.utils.BaseUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 类说明：
 * 作者：  赵伦达
 */
@SuppressWarnings("unchecked")
public class ClaimoldPartTransportDao extends BaseDao{
	
	public static Logger logger = Logger.getLogger(ClaimBackListDao.class);
	private static final ClaimoldPartTransportDao dao = null;
	
	public static final ClaimoldPartTransportDao getInstance() {
	   if(dao==null) return new ClaimoldPartTransportDao();
	   return dao;
	}
	
	 public PageResult<Map<String, Object>> queryOldPartTransport(RequestWrapper req,String dealerCode,String transportNo,String transportStatus,int curPage, int pageSize){
	        StringBuffer sb = new StringBuffer();
			sb.append("select totd.audit_remark,totd.status, tot.transport_id,td.dealer_code,\n");
			sb.append("tot.transport_no,tot.transport_status,tuc.name as check_name,\n");
			sb.append("tot.check_date,tot.report_date,tcr.name as report_name,\n");
			sb.append("tcc.name as create_name,tot.create_date,tot.update_date,\n");
			sb.append("tot.update_by,tot.dealer_name from tm_oldpart_transport tot,tc_user tuc,tc_user tcr,tm_oldpart_transport_detail totd,\n");
			sb.append("tc_user tcc,tm_dealer td\n");
			sb.append(" where tot.check_user=tuc.user_id(+) and tot.report_user=tcr.user_id(+) and tot.transport_id=totd.transport_id(+)\n");
			sb.append(" and tot.create_by=tcc.user_id(+) and tot.dealer_id=td.dealer_id(+)\n");
			String DEAR_CODE = CommonUtils.checkNull(req.getParamValue("DEAR_CODE"));
			if(!"".equals(DEAR_CODE)){
				sb.append(" and  td.dealer_code like '%"+DEAR_CODE+"%'");
			}
			if(null!=dealerCode&&!"".equals(dealerCode)){
				
				sb.append(" and td.dealer_code='"+dealerCode+"'");
			}
			if(transportNo!=null&&!"".equals(transportNo)){
				
				sb.append(" and tot.transport_no like '%"+transportNo+"%'");
			}
			if(transportStatus!=null&&!"".equals(transportStatus)){
				
				sb.append(" and tot.transport_status='"+transportStatus+"'");
			}else{
				if(dealerCode==null||"".equals(dealerCode)){  //车厂端运输状态选全部，只能查审核已提交和已通过的
					
					sb.append(" and tot.transport_status not in ("+Constant.SP_JJ_TRANSPORT_STATUS_01+")");
				}
			}
			DaoFactory.getsql(sb, "td.dealer_name", DaoFactory.getParam(req, "DEALER_NAME"), 2);
			DaoFactory.getsql(sb, "td.dealer_code", DaoFactory.getParam(req, "DEALER_CODE"), 2);
			DaoFactory.getsql(sb, "totd.status", DaoFactory.getParam(req, "STATUS"), 1);
			DaoFactory.getsql(sb, "totd.transport_name", DaoFactory.getParam(req, "transport_name"), 2);
			sb.append("order by tot.create_date desc\n");
			PageResult<Map<String, Object>> ps = pageQuery(sb.toString(), null, getFunName(), pageSize, curPage);
	        return ps;
	    }
	 
	 public PageResult<Map<String, Object>> queryOldPartTransportDetail(String dealerName,String dealerCode,String transportNo,String transportStatus,String status, int curPage, int pageSize){

	        StringBuffer sb = new StringBuffer();
			sb.append("select tot.transport_id,td.dealer_code,\n");
			sb.append("tot.transport_no,tot.transport_status,totd.transport_name,\n");
			sb.append("decode(totd.status,0,'不同意',1,'同意',3,'审核中') as status,tuc.name as check_name,\n");
			sb.append("tot.check_date,tot.report_date,tcr.name as report_name,\n");
			sb.append("tcc.name as create_name,tot.create_date,tot.update_date,\n");
			sb.append("tot.update_by,tot.dealer_name from tm_oldpart_transport tot,tm_oldpart_transport_detail totd,\n");
			sb.append("tc_user tuc,tc_user tcr,tc_user tcc,tm_dealer td\n");
			sb.append(" where tot.transport_id=totd.transport_id(+) and tot.check_user=tuc.user_id(+) and tot.report_user=tcr.user_id(+)\n");
			sb.append(" and tot.create_by=tcc.user_id(+) and tot.dealer_id=td.dealer_id(+)\n");
			sb.append(" and tot.transport_status not in ("+Constant.SP_JJ_TRANSPORT_STATUS_01+")");
			DaoFactory.getsql(sb, "td.dealer_name", dealerName, 2);
			DaoFactory.getsql(sb, "td.dealer_code", dealerCode, 2);
			DaoFactory.getsql(sb, "tot.transport_no", transportNo, 2);
			DaoFactory.getsql(sb, "tot.transport_status", transportStatus, 1);
			DaoFactory.getsql(sb, "totd.status", status, 1);
			sb.append(" order by tot.create_date desc\n");
			PageResult<Map<String, Object>> ps = pageQuery(sb.toString(), null, getFunName(), 15, curPage);
	        return ps;
	    }
	 
	 public HashMap<String, Object> queryOldPartTransportById(RequestWrapper req,String transportId){
	        StringBuffer sb = new StringBuffer();
			sb.append("select tot.transport_id,td.dealer_code,\n");
			sb.append("tot.transport_no,tot.transport_status,tuc.name as check_name,\n");
			sb.append("tot.check_date,tot.report_date,tcr.name as report_name,\n");
			sb.append("tcc.name as create_name,tot.create_date,tot.update_date,\n");
			sb.append("tot.update_by,tot.dealer_name from tm_oldpart_transport tot,tc_user tuc,tc_user tcr,\n");
			sb.append("tc_user tcc,tm_dealer td\n");
			sb.append(" where tot.check_user=tuc.user_id(+) and tot.report_user=tcr.user_id(+)\n");
			sb.append(" and tot.create_by=tcc.user_id(+) and tot.dealer_id=td.dealer_id(+)\n");
			if(transportId!=null&&!"".equals(transportId)){
				
				sb.append(" and tot.transport_id="+transportId.trim()+"");
			}

			HashMap<String, Object> map = new HashMap<String, Object>();
			List<Map<String, Object>> relist = pageQuery(sb.toString(), null,getFunName());
			if(relist != null && relist.size() > 0){
				map = (HashMap)relist.get(0);
			}
			return map;
	    }
	 public void delOldPartTransport(RequestWrapper req,String dealerCode,String transportId,String[] detailIds,Object [] newDetailIds){
	        StringBuffer sql = new StringBuffer();


			StringBuffer sb = new StringBuffer();
			sb.append("delete from tm_oldpart_transport_detail totd\n");
			sb.append(" where 1=1 \n");
				sb.append(" and totd.transport_id = "+transportId);
			if(detailIds!=null&&detailIds.length>0){
				sb.append(" and totd.detail_id not in ("+setArrToStrByReg(detailIds,","));
			}
			if(newDetailIds!=null&&newDetailIds.length>0){
				sb.append(","+setArrToStrByReg(newDetailIds,","));
			}
			sb.append(")");
			factory.delete(sb.toString(), null);
	    }
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static String setArrToStrByReg(Object[] str,String reg){
		
		String newStr = "";
		
		for(int i = 0; i < str.length; i++){
			
			if(str[i]!=null){
				if("".equals(newStr)){
					newStr = newStr + "'";
				}
				newStr = newStr + str[i] + "'" + reg + "'";	
			}
			
		}
		if(!"".equals(newStr)){
			newStr = newStr.substring(0, newStr.length()-2);
		}
		return newStr;
	}
	
	public PageResult<Map<String, Object>> queryCreateOldPartType(
			String typename, String oldcode, String nameold, Integer curPage, int i) {
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT t.ID,t.NAME_TYPE,t.CODE_OLD,t.NAME_OLD,tc.NAME,t.CREATE_DATE FROM TT_AS_CREATE_OLD t left join tc_user tc on t.create_user = tc.user_id where 1=1 ");
		if(StringUtil.notNull(typename)){
			sb.append(" and t.NAME_TYPE="+typename);
		}
		if(StringUtil.notNull(oldcode)){
			sb.append(" and t.CODE_OLD like '%"+oldcode+"%'");
		}
		if(StringUtil.notNull(nameold)){
			sb.append(" and t.NAME_OLD like '%"+nameold+"%'");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sb.toString(), null, getFunName(), 15, curPage);
        return ps;
	}
	
	public int checkCodeunique(String codeOld) {
		String  sql="select * from TT_AS_CREATE_OLD t where t.code_old='"+codeOld+"'";
		List<Map<String, Object>> list = pageQuery(sql, null, this.getFunName());
		return list.size();
	}
	 
	/**
	 * 修改索赔旧件运输方式审查备注
	 * @param detailId
	 * @param remark
	 */
	public void updateAudtiRemark(String detailId,String remark){
		String sql = "update tm_oldpart_transport_detail set audit_remark = ? where detail_id = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(remark);
		params.add(detailId);
		update(sql, params);
	}
  /**
   * 导出数据索赔旧件运输管理
   * @param request
   * @param pageSizeMax
   * @param currPage
   */
	public void ExportOldpartTransport(ActionContext act,RequestWrapper request,Integer pageSizeMax, Integer currPage) {
		String transportNo = request.getParamValue("TRANSPORT_NO");//运输公司名称
	    String transportStatus = request.getParamValue("TRANSPORT_STATUS");//运输公司状态
	    PageResult<Map<String, Object>> ps  =this.queryOldPartTransport(request, null, transportNo, transportStatus, currPage, pageSizeMax);
	    try {
		    String[] head={"服务站代码","服务站名称","申请单号","申请单状态","上报人","上报时间","审核人","审核时间","审核结果","审核备注"};
			List<Map<String, Object>> records = ps.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[10];
					detail[0]=BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[1]=BaseUtils.checkNull(map.get("DEALER_NAME"));
					detail[2]=BaseUtils.checkNull(map.get("TRANSPORT_NO"));
					detail[3]=this.getTypeDesc(BaseUtils.checkNull(map.get("TRANSPORT_STATUS")));
					detail[4]=BaseUtils.checkNull(map.get("REPORT_NAME"));
					detail[5]=BaseUtils.checkNull(map.get("REPORT_DATE"));
					detail[6]=BaseUtils.checkNull(map.get("CHECK_NAME"));
					detail[7]=BaseUtils.checkNull(map.get("CHECK_DATE"));
					if ("1".equals(map.get("STATUS").toString())) {
						detail[8]="同意";
					}else if ("2".equals(map.get("STATUS").toString())) {
						detail[8]="不同意";
					}else if ("3".equals(map.get("STATUS").toString())) {
						detail[8]="审核中";
					}
					detail[9]=BaseUtils.checkNull(map.get("AUDIT_REMARK"));
					params.add(detail);
				}
			}
			    String systemDateStr = BaseUtils.getSystemDateStr();
				BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "索赔旧件运输管理数据导出"+systemDateStr+".xls", "导出数据",null);
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

     public String getTypeDesc(String codeId) {
    	 TcCodePO t=new TcCodePO();
 		t.setCodeId(codeId);
 		List<TcCodePO> list= this.select(t);
 		if(list!=null && list.size()>0){
 			t=list.get(0);
 			return t.getCodeDesc();
 		}else{
 			return "无";
 		}
     }


}

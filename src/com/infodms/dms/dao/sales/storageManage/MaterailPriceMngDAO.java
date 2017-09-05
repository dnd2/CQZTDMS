package com.infodms.dms.dao.sales.storageManage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class MaterailPriceMngDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(MaterailPriceMngDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final MaterailPriceMngDAO dao = new MaterailPriceMngDAO();
	
	public static final MaterailPriceMngDAO getInstance() {
		return dao;
	}

	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		// TODO Auto-generated method stub
		return null;
	}
	


	



	public PageResult<Map<String, Object>> getMaterailPriceDetail(Map<String, String> dataPara,int curPage, int pageSize) throws Exception {
		String priceCode = dataPara.get("priceCode");
		String priceName = dataPara.get("priceName");
		String startDate = dataPara.get("startDate");
		String endDate = dataPara.get("endDate");
		String createDate = dataPara.get("createDate");
				
		StringBuffer sql = new StringBuffer();
		sql.append("select T.PRICE_ID,T.PRICE_CODE,T.PRICE_DESC, "
				+ "TO_CHAR(T.START_DATE,'yyyy-MM-dd') START_DATE, "
				+ "TO_CHAR(T.END_DATE,'yyyy-MM-dd') END_DATE, "
				+ "TO_CHAR(T.CREATE_DATE,'yyyy-MM-dd') CREATE_DATE "
				+ "from TT_VS_PRICE T");
		sql.append(" where 1=1 \n");
		if(priceCode!=""){
			sql.append("and T.PRICE_CODE like \'%"+priceCode+"%\' \n");
		}
		if(priceName!=""){
			sql.append("and T.PRICE_DESC like \'%"+priceName+"%\' \n");
		}
		if(startDate!=""){
			sql.append("and TO_CHAR(T.START_DATE,'yyyy-MM-dd') =\'"+startDate+"\'\n");
		}
		if(endDate!=""){
			sql.append("and TO_CHAR(T.END_DATE,'yyyy-MM-dd') =\'"+endDate+"\'\n");
		}
		if(createDate!=""){
			sql.append("and TO_CHAR(T.CREATE_DATE,'yyyy-MM-dd') =\'"+createDate+"\'\n");
		}
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public List<Map<String, Object>> getMaterailPriceById(String priceId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select T.PRICE_CODE, T.PRICE_DESC,"+
					"TO_CHAR(T.START_DATE,'yyyy-MM-dd') START_DATE,"+
					"TO_CHAR(T.END_DATE,'yyyy-MM-dd') END_DATE,"+
					"TO_CHAR(T.CREATE_DATE,'yyyy-MM-dd') CREATE_DATE"+
					" from  TT_VS_PRICE T where t.price_id = "+priceId);
		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName());
			
		return ps;
	}
	
	/*
	 * 价格详细表与物料信息 
	 * */
	public PageResult<Map<String, Object>> getMaterailPriceDeatailById(String priceId,int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select dtl.sales_price,gp.group_code,gp.group_name "
				+ "from TT_VS_PRICE_DTL dtl,tm_vhcl_material_group gp where  "
				+ "dtl.price_id = "+priceId  
				+ "and dtl.group_id = gp.group_id");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);			
		return ps;
	}
	
	public List<Map<String, Object>> getMaterailPriceDeatailById(String priceId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select dtl.sales_price,gp.group_code,gp.group_name "
				+ "from TT_VS_PRICE_DTL dtl,tm_vhcl_material_group gp where  "
				+ "dtl.price_id = "+priceId  
				+ "and dtl.group_id = gp.group_id");
		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	public  List<Map<String, Object>> getTtVsPriceDtlTempDate(){
		StringBuffer sql = new StringBuffer("");
		sql.append("select * from TT_VS_PRICE_DTL_TEMP t order by t.line\n");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	public  PageResult<Map<String, Object>> getTtVsPriceDtlTempPageDate(int curPage,int pageSize){
		StringBuffer sql = new StringBuffer("");
		sql.append("select * from TT_VS_PRICE_DTL_TEMP t order by t.line\n");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);			
		return ps;
	}
	
	public  List<Map<String, Object>> getTtVsPriceDtlTempRepeatDate(){
		StringBuffer sql = new StringBuffer("");
		sql.append(
				"select * from  TT_VS_PRICE_DTL_TEMP te where te.group_code in\n" +
						"(\n" + 
						"(select t.group_code from TT_VS_PRICE_DTL_TEMP t group by t.group_code having count(1) >= 2)\n" + 
						") order by te.line"
				);
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	/*
	 * 检查物料代码是否成在
	 * */
	public  boolean checkGroupCodeIsHave(String groupCode){
		StringBuffer sql = new StringBuffer("");
		sql.append("select * from tm_vhcl_material_group t where t.group_code = '"+groupCode+"'");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		if(list.size()>0){
			return false;
		}
		return true;
	}
	
	/*
	 * 得到物料ID
	 * */
	public  String getGroupID(String groupCode){
		StringBuffer sql = new StringBuffer("");
		sql.append("select t.group_id from tm_vhcl_material_group t where t.group_code = '"+groupCode+"'");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		if(list.size()>0){
			return list.get(0).get("GROUP_ID").toString();
		}
		return "";
	}
}

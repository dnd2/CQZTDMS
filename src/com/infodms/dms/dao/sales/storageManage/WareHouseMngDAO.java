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

public class WareHouseMngDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(WareHouseMngDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final WareHouseMngDAO dao = new WareHouseMngDAO();
	
	public static final WareHouseMngDAO getInstance() {
		return dao;
	}

	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		// TODO Auto-generated method stub
		return null;
	}
	


	



	public PageResult<Map<String, Object>> getHouseDataDetail(Map<String, String> dataPara,int curPage, int pageSize) throws Exception {
		String houseCode = dataPara.get("houseCode");
		String houseName = dataPara.get("houseName");
		String hourseDate = dataPara.get("hourseDate");
		String tel = dataPara.get("tel");
		String linkMain = dataPara.get("linkMain");
		String address = dataPara.get("address");
		StringBuffer sql = new StringBuffer();
		sql.append("select t.code_desc, F.WAREHOUSE_ID,F.WAREHOUSE_NAME,F.WAREHOUSE_CODE,F.ADDRESS ,f.link_man,f.tel, "
				+ " TO_CHAR(F.CREATE_DATE,'yyyy-MM-dd') CREATE_DATE "
				+ " from tm_warehouse  F ,tc_code t where 1=1 \n");
		sql.append("and t.type = 1001 \n");
		sql.append(" and t.code_id = F.Status \n");
		if(houseCode!=""){
			sql.append("and F.WAREHOUSE_CODE like '%"+houseCode+"%' \n");
		}
		if(houseName!=""){
			sql.append("and F.WAREHOUSE_NAME like '%"+houseName+"%' \n");
		}
		if(hourseDate!=""){
			sql.append("and TO_CHAR(F.CREATE_DATE,'yyyy-MM-dd') =\'"+hourseDate+"\'\n");
		}
		if(tel!=""){
			sql.append("and F.tel like '%"+tel+"%' \n");
		}
		if(linkMain!=""){
			sql.append("and F.link_man like '%"+linkMain+"%' \n");
		}
		if(address!=""){
			sql.append("and F.ADDRESS like '%"+address+"%' \n");
		}
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public List<Map<String, Object>> getWareHouseType() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.code_id,t.code_desc from tc_code t where t.type = 1401");
		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName());
		
		Map<String, Object> firstNull = new HashMap<String, Object>();//首选为空
		firstNull.put("", "");
		ps.add(0, firstNull);		
		return ps;
	}
	
	public List<Map<String, Object>> getArea() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.area_id,t.area_name from tm_business_area t where t.status = 10011001");
		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName());
		
		Map<String, Object> firstNull = new HashMap<String, Object>();//首选为空
		firstNull.put("", "");
		ps.add(0, firstNull);		
		return ps;
	}
	
	public Map<String, Object> getWareHouseById(String wareHouseId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tm_warehouse t where t.warehouse_id ="+wareHouseId);
		Map<String, Object> ps = dao.pageQueryMap(sql.toString(), null, getFunName());	
		return ps;
	}

}

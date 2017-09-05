package com.infodms.dms.dao.sales.dealer;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmpShDealerPO;
import com.infodms.dms.po.TmpXsDealerPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: CHANADMS
 *
 * @Description:
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company:  www.infoservice.com.cn
 * @Date: 2014-2-21
 *
 * @author ranj 
 * @remark 
 */
public class ShDealerDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ShDealerDao.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static ShDealerDao dao = new ShDealerDao ();
	public static final ShDealerDao getInstance() {
		if (dao == null) {
			dao = new ShDealerDao();
		}
		return dao;
	}
	private ShDealerDao() {}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	 * 单表查询Tmp_Xs_Dealer
	 */
	public List<TmpShDealerPO> selectTmpShDealer(TmpShDealerPO po){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");
		sql.append("SELECT *\n");
		sql.append("  FROM Tmp_Sh_Dealer\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append(" order by ROW_NUMBER");
		return dao.select(TmpXsDealerPO.class, sql.toString(), params);
	}
	/*
	 * ，查询临时表数据
	 */
	public List<Map<String, Object>> selectTmpShDealerList(){	
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT *\n");
		sbSql.append("  FROM Tmp_Sh_Dealer\n");  
		sbSql.append(" WHERE 1 = 1 order by ROW_NUM\n");
		logger.info(sbSql.toString());
		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}
	/**
	 * 查询临时表数据(分页)
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> selectTmpShDealerQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT *\n");
		sbSql.append("  FROM Tmp_Sh_Dealer\n");  
		sbSql.append(" WHERE 1 = 1 order by ROW_NUM\n");
		logger.info(sbSql.toString());
		PageResult<Map<String, Object>> ps= dao.pageQuery(sbSql.toString(), null, getFunName(),pageSize,curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> selectTmpShDealerQuery2nd(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT *\n");
		sbSql.append("  FROM Tmp_Sh_Dealer_secend\n");  
		sbSql.append(" WHERE 1 = 1 order by ROW_NUM\n");
		logger.info(sbSql.toString());
		PageResult<Map<String, Object>> ps= dao.pageQuery(sbSql.toString(), null, getFunName(),pageSize,curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> selectTmpShDealerAddressQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT *\n");
		sbSql.append("  FROM TMP_SH_DEALER_ADDRESS\n");  
		sbSql.append(" WHERE 1 = 1 order by id\n");
		logger.info(sbSql.toString());
		PageResult<Map<String, Object>> ps= dao.pageQuery(sbSql.toString(), null, getFunName(),pageSize,curPage);
		return ps;
	}
	public PageResult<Map<String, Object>> selectTmpXSDealerAddressQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT *\n");
		sbSql.append("  FROM TMP_XS_DEALER_ADDRESS\n");  
		sbSql.append(" WHERE 1 = 1 order by id\n");
		logger.info(sbSql.toString());
		PageResult<Map<String, Object>> ps= dao.pageQuery(sbSql.toString(), null, getFunName(),pageSize,curPage);
		return ps;
	}
	/*
	 * 
	 * 查询临时表中是否有相同的
	 * 返回所有重复数据集合
	 */
	public List<Map<String, Object>> sbuTalbeCheckDump(){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");
		sql.append("SELECT P1.ROW_NUM ROW_NUMBER1, P2.ROW_NUM ROW_NUMBER2\n");
		sql.append("  FROM Tmp_Sh_Dealer P1, Tmp_Sh_Dealer P2\n");
		sql.append(" WHERE P1.DEALER_CODE = P2.DEALER_CODE\n");
		sql.append("   AND P1.ROW_NUM <> P2.ROW_NUM");  
		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	public List<Map<String, Object>> getProvinceId(String proName){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql=new StringBuffer("");
		sbSql.append("SELECT T.REGION_ID,T.REGION_CODE\n");
		sbSql.append("  FROM TM_REGION T\n");
		sbSql.append(" WHERE T.REGION_NAME = ?\n");
		sbSql.append("   AND T.REGION_TYPE = 10541002"); 
		params.add(proName);
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	public List<Map<String, Object>> getCityId(String parentId,String cityName){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql=new StringBuffer("");
		sbSql.append("SELECT T.REGION_ID,T.REGION_CODE\n");
		sbSql.append("  FROM TM_REGION T\n");
		sbSql.append(" WHERE T.REGION_NAME = ?\n");
		sbSql.append("   AND T.PARENT_ID = ?");
		sbSql.append("   AND T.REGION_TYPE = 10541003"); 
		params.add(cityName);
		params.add(parentId);
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	public List<Map<String, Object>> getContinusId(String parentId,String conName){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql=new StringBuffer("");
		sbSql.append("SELECT T.REGION_ID,T.REGION_CODE\n");
		sbSql.append("  FROM TM_REGION T\n");
		sbSql.append(" WHERE T.REGION_NAME = ?\n");
		sbSql.append("   AND T.PARENT_ID = ?");
		sbSql.append("   AND T.REGION_TYPE = 10541004"); 
		params.add(conName);
		params.add(parentId);
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	public List<Map<String, Object>> getCompany(String comCode){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");
		sql.append("SELECT * FROM TM_COMPANY T WHERE T.COMPANY_CODE=?");
		params.add(comCode);
		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	 /***
	  * 根据上级经销商名称获得上级经销商代码
	  */
	public List<Map<String, Object>> getPDealer(String parentDealerName){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql=new StringBuffer("");
		sbSql.append("SELECT T.DEALER_ID\n");
		sbSql.append("  FROM TM_DEALER T\n");
		sbSql.append(" WHERE T.DEALER_NAME =? AND T.DEALER_LEVEL=10851001\n");
		params.add(parentDealerName);
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	 /***
	  * 根据大区名称获取大区ID
	  */
	public List<Map<String, Object>> getBigId(String bigName){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql=new StringBuffer("");
		sbSql.append("SELECT T.ORG_ID\n");
		sbSql.append("  FROM TM_ORG T\n");
		sbSql.append(" WHERE T.ORG_LEVEL=3 AND T.ORG_NAME = ? \n");
		params.add(bigName);
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	 /***
	  * 查询中间表是否有数据（经销商表和组织表）
	  */
	public List<Map<String, Object>> getDO(String dealerC,String orgName,String busType){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql=new StringBuffer("");
		sbSql.append("select 1 from TM_DEALER_ORG_RELATION t where t.dealer_id =(SELECT t.dealer_id FROM TM_Dealer T WHERE T.Dealer_CODE=?) \n");
		sbSql.append("and t.org_id =(SELECT T.ORG_ID FROM TM_ORG T WHERE T.ORG_LEVEL=3 AND T.ORG_NAME LIKE ?)\n");
		params.add(dealerC);
		params.add("%"+orgName+"%");
		//params.add(busType);
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	 /***
	  * 删除经销商表和组织表中间表数据
	  */
	public void delRation(String dealerId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql=new StringBuffer("");
		sbSql.append("delete from TM_DEALER_ORG_RELATION t where t.dealer_id = ?");
		params.add(dealerId);
		dao.delete(sbSql.toString(), params);
	}
}

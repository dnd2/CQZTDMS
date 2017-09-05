package com.infodms.dms.dao.sales.storage.storagebase;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesAccarPerPO;
import com.infodms.dms.po.TtSalesLogiAreaPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : AccarPerDao 
 * @Description   : 接车员管理DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-9
 */
public class AccarPerDao extends BaseDao<PO>{
	private static final AccarPerDao dao = new AccarPerDao ();
	public static final AccarPerDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 接车员信息查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getAccarPerQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		
		
		String perCode = (String)map.get("PER_CODE"); // 人员代码
		String perName = (String)map.get("PER_NAME"); //人员名称
		String yieldly = (String)map.get("YIELDLY");// 产地
		String status = (String)map.get("STATUS"); // 状态
		String poseId = (String)map.get("poseId"); // 状态
		
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT  TSAP.PER_ID,TSAP.PER_CODE,TSAP.PER_NAME,TSAP.CON_TEL,\n");
		sql.append("TBA.AREA_NAME YIELDLY,TSAP.STATUS FROM TT_SALES_ACCAR_PER TSAP,TM_BUSINESS_AREA TBA WHERE TSAP.YIELDLY=TBA.AREA_ID\n");
		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TSAP.YIELDLY",poseId));//车厂端查询列表产地数据权限
		if(yieldly!=null&&!"".equals(yieldly)){
			sql.append("   AND TSAP.YIELDLY = "+yieldly+"\n" );
		}
		if(perCode!=null&&!"".equals(perCode)){
			sql.append("   AND TSAP.PER_CODE  LIKE ? \n" );
			params.add("%"+perCode+"%");
		}
		if(perName!=null&&!"".equals(perName)){
			sql.append("   AND TSAP.PER_NAME  LIKE ? \n" );
			params.add("%"+perName+"%");
		}
		if(yieldly!=null&&!"".equals(yieldly)){
			sql.append("   AND TSAP.YIELDLY = ? \n" );
			params.add(yieldly);
		}
		if(status!=null&&!"".equals(status)){
			sql.append("   AND TSAP.STATUS = "+status+"\n" );
		}
		sql.append(" ORDER BY TSAP.PER_CODE");
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 接车员信息添加 
	 * @param      : @param ttSalesAccarPerPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public  void accarPerAdd(TtSalesAccarPerPO ttSalesAccarPerPO) {
		dao.insert(ttSalesAccarPerPO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 接车员信息修改 
	 * @param      : @param seachPO
	 * @param      : @param ttSalesLogiPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public  void accarPerUpdate(TtSalesAccarPerPO seachPO,TtSalesAccarPerPO ttSalesAccarPerPO) {
		dao.update(seachPO, ttSalesAccarPerPO);
    }
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据接车员ID得到接车员信息
	 * @param      : @param id
	 * @param      : @return     接车员信息
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public Map<String, Object> getAccarPerMsg(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT  TSAP.PER_ID,TSAP.PER_CODE,TSAP.PER_NAME,TSAP.CON_TEL,\n");
		sql.append("TBA.AREA_NAME YIELDLY,TSAP.YIELDLY ARREA_ID,TSAP.STATUS,TSAP.REMARK FROM TT_SALES_ACCAR_PER TSAP,");
		sql.append("TM_BUSINESS_AREA TBA WHERE TSAP.YIELDLY=TBA.AREA_ID AND TSAP.PER_ID= \n");
		sql.append(id);
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据物流商ID得到物流商与区域关联信息
	 * @param      : @param id
	 * @param      : @return     物流商与区域关联信息 
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public  List<TtSalesLogiAreaPO>  getLogisticsMata(TtSalesLogiAreaPO po){
		return dao.factory.select(po);
	}
}

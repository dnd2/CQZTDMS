package com.infodms.dms.dao.sales.storage.storagebase;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesMilsetPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : FareMileageDao 
 * @Description   : 运费里程管理DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-1
 */
public class FareMileageDao extends BaseDao<PO>{
	private static final FareMileageDao dao = new FareMileageDao ();
	public static final FareMileageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 运费里程信息查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getFareMileageQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		
		String milStart = (String)map.get("MIL_START");
		String milEnd = (String)map.get("MIL_END");
		String yieldly = (String)map.get("YIELDLY");	
		String poseId = (String)map.get("poseId");
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSM.MIL_ID,TSM.MIL_START,TSM.MIL_END,TSM.YIELDLY YIELDLY_ID,TBA.AREA_NAME YIELDLY ");
		sql.append("FROM TT_SALES_MILSET  TSM,TM_BUSINESS_AREA TBA  WHERE TSM.YIELDLY=TBA.AREA_ID ");
		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TSM.yieldly",poseId));//车厂端查询列表产地数据权限
		if(milStart!=null&&!"".equals(milStart)){
			sql.append("   AND TSM.MIL_START >="+milStart+"\n" );
		}
		if(milEnd!=null&&!"".equals(milEnd)){
			sql.append("   AND TSM.MIL_END <= "+milEnd+"\n" );
		}
		if(yieldly!=null&&!"".equals(yieldly)){
			sql.append("   AND TSM.yieldly = "+yieldly+"\n" );
		}
		sql.append(" ORDER BY TSM.YIELDLY,TSM.MIL_START ASC");
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运费里程信息添加 
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void fareMileageAdd(TtSalesMilsetPO ttSalesMilsetPO) {
		dao.insert(ttSalesMilsetPO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 运费里程信息修改 
	 * @param      : @param seachSalesAreaPO
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void fareMileageUpdate(TtSalesMilsetPO seachPO,TtSalesMilsetPO ttSalesMilsetPO) {
		dao.update(seachPO, ttSalesMilsetPO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据运费里程ID得到运费里程信息
	 * @param      : @param id
	 * @param      : @return     运费里程信息
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public Map<String, Object> getFareMileageMsg(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSM.MIL_ID,TSM.MIL_START,TSM.MIL_END,TSM.YIELDLY  FROM TT_SALES_MILSET TSM WHERE TSM.MIL_ID=\n");
		sql.append(Long.parseLong(id));
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
}

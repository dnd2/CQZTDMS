package com.infodms.dms.dao.parts.baseManager.logisticsManage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtTransportValuationPO;
import com.infodms.dms.po.TtTransportinfoPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

/**
 * 运输方式维护dao
 * @author hyy 
 * @version 2017-7-6
 * @see 
 * @since 
 */
public class TransportInfoDao extends BaseDao<PO> {
	private static final TransportInfoDao dao = new TransportInfoDao();

	public static final TransportInfoDao getInstance() {
		return dao;
	}
	private TransportInfoDao() {}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	
	public PageResult<Map<String, Object>> selTtransportInfo(TtTransportinfoPO po, int curPage, int pageSize)
			throws Exception {

		List<Object> params = new LinkedList<Object>();

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TI.PK_ID,TI.PLACE_PROVINCE_ID,TI.PLACE_CITY_ID,TI.PLACE_COUNTIES,TI.DEST_PROVINCE_ID,TI.DEST_CITY_ID,TI.DEST_COUNTIES,");
		sql.append(" TS.LOGI_FULL_NAME,TV.TRANSPORT_CODE,TV.VALUATION_CODE,TI.PRICE,TI.STATUS FROM TT_TRANSPORTINFO TI ");
		sql.append(" LEFT JOIN TT_TRANSPORT_VALUATION TV ON TI.TV_ID=TV.TV_ID");
		sql.append(" LEFT JOIN TT_SALES_LOGI  TS ON TI.CARRIER=TS.LOGI_CODE WHERE 1=1 ");

		
		if (po.getTvId() != null && !"".equals(po.getTvId())) {
			sql.append("   AND TI.TV_ID  = '" + po.getTvId()+ "' ");
		}
		if (po.getCarrier() != null && !"".equals(po.getCarrier())) {
			sql.append("   AND TI.CARRIER = '" + po.getCarrier() + "' ");
		}
		if (po.getPlaceProvinceId() != null && !"".equals(po.getPlaceProvinceId())) {
			sql.append("   AND PLACE_PROVINCE_ID  = '" + po.getPlaceProvinceId() + "' ");
		}
		if (po.getPlaceCityId() != null && !"".equals(po.getPlaceCityId())) {
			sql.append("   AND PLACE_CITY_ID  = '" + po.getPlaceCityId() + "' ");
		}
		if (po.getPlaceCityId() != null && !"".equals(po.getPlaceCityId())) {
			sql.append("   AND PLACE_COUNTIES  = '" + po.getPlaceCityId() + "' ");
		}
		if (po.getDestProvinceId() != null && !"".equals(po.getDestProvinceId())) {
			sql.append("   AND DEST_PROVINCE_ID  = '" + po.getDestProvinceId() + "' ");
		}
		if (po.getDestCityId() != null && !"".equals(po.getDestCityId())) {
			sql.append("   AND DEST_CITY_ID  = '" + po.getDestCityId() + "' ");
		}
		if (po.getDestCounties() != null && !"".equals(po.getDestCounties())) {
			sql.append("   AND DEST_COUNTIES  = '" + po.getDestCounties() + "' ");
		}
		sql.append(" ORDER BY ti.UPDATE_DATE DESC");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(),
				params, getFunName(), pageSize, curPage);
		return ps;
	}

	
	
	public  Map<String, Object> selTransportById(String id) throws Exception {
		Map<String, Object> map;
        try {
        	StringBuffer sql=new StringBuffer("");
    		sql.append("SELECT PK_ID,CARRIER,TV_ID,PRICE,PLACE_PROVINCE_ID,PLACE_CITY_ID,PLACE_COUNTIES,");
    		sql.append(" DEST_PROVINCE_ID,DEST_CITY_ID,DEST_COUNTIES,STATUS FROM TT_TRANSPORTINFO WHERE PK_ID= '"+id+"'");
            map = pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return map;
	}
	
	
	/**
	 * 检查出发地、目的地、承运商、运输方式、计价方式是否唯一
	 * @param po
	 * @param type  1新增查询   2修改查询
	 * @return
	 * @throws Exception
	 */
	 public List selTransportFlag(TtTransportinfoPO po,int type) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT * FROM TT_TRANSPORTINFO WHERE 1=1 ");
            if(type==2){
            	sql.append(" AND PK_ID !='"+po.getPkId()+"'  ");
            }
//          sql.append(" AND PLACE_PROVINCE_ID='"+po.getPlaceProvinceId()+"' AND PLACE_CITY_ID='"+po.getPlaceCityId()+"' ");
//          sql.append(" AND PLACE_COUNTIES='"+po.getPlaceCounties()+"' AND DEST_PROVINCE_ID='"+po.getDestProvinceId()+"' ");
//          sql.append(" AND DEST_CITY_ID='"+po.getDestCityId()+"' AND DEST_COUNTIES='"+po.getDestCounties()+"' ");
            sql.append(" AND PLACE_COUNTIES='"+po.getPlaceCounties()+"' ");
            sql.append(" AND DEST_COUNTIES='"+po.getDestCounties()+"' ");
            sql.append(" AND CARRIER='"+po.getCarrier()+"' AND TV_ID='"+po.getTvId()+"' ");
            return pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }
	 
	/**
	 * 添加运输信息
	 * @param ttTransportValuationPO
	 */
	public void transportInfoAdd(TtTransportinfoPO po) {
		dao.insert(po);
	}
	
	/**
	 * 修改运输信息
	 * @param po
	 * @return
	 */
	public int transportInfoUpdate(TtTransportinfoPO po) {
		String sql="UPDATE tt_transportinfo SET CARRIER='"+po.getCarrier()+"',TV_ID='"+po.getTvId()+"',PRICE="+po.getPrice()+",PLACE_PROVINCE_ID='"+po.getPlaceProvinceId()+"',PLACE_CITY_ID='"+po.getPlaceCityId()+"',PLACE_COUNTIES='"+po.getPlaceCounties()+"', DEST_PROVINCE_ID='"+po.getDestProvinceId()+"',DEST_CITY_ID='"+po.getDestCityId()+"',DEST_COUNTIES='"+po.getDestCounties()+"',STATUS='"+po.getStatus()+"',UPDATE_DATE=sysdate,UPDATE_BY="+po.getUpdateBy()+" WHERE PK_ID='"+po.getPkId()+"' ";
		return dao.update(sql, null);
	}
		
}

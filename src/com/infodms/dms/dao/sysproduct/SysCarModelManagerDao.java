/**********************************************************************
 * <pre>
 * FILE : SysCarModelManagerDao.java
 * CLASS : SysCarModelManagerDao
 *
 * AUTHOR : yangyong
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.|   DATE   |   NAME  | REASON  | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		    |2009-12-22|yangyong| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
package com.infodms.dms.dao.sysproduct;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.sysposition.SysPositionDAO;
import com.infodms.dms.po.TmCarNamePO;
import com.infodms.dms.po.TmCarShortNamePO;
import com.infodms.dms.po.TmSeriesPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;

public class SysCarModelManagerDao {
	public static Logger logger = Logger.getLogger(SysPositionDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	/**
	 * 通过carTypeId查询出下属系列车型
	 * @param carTypeId
	 * @return
	 */
	public static void updateCarModelFilterByCarTypeId(int oldModelSift,Long carTypeId){
		String sql = "UPDATE TM_MODEL T SET T.OLD_MODEL_SIFT = ? WHERE T.CAR_TYPE_ID = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(oldModelSift);
		params.add(carTypeId);
		factory.update(sql,params);
	}
	
	/**
	 * 通过carNameId查询出下属车型简称
	 * @param carNameId
	 * @return
	 */
	public static void updateCarShortNameFilterByCarNameId(int oldModelSift,Long carNameId,Long userId){
				
		String sql = "UPDATE Tm_Car_Short_Name T SET T.Old_Model_Sift = ? WHERE T.Car_Name_Id = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(oldModelSift);
		params.add(carNameId);
		factory.update(sql,params);
		
		//查询出所有的车型简称
		TmCarShortNamePO po = new TmCarShortNamePO();
		po.setCarNameId(carNameId);		
		List<TmCarShortNamePO> pos = factory.select(po);
		
		if(pos.size() > 0){
			for (TmCarShortNamePO c : pos) {				
				updateCarModelFilterByCarTypeId(oldModelSift, c.getCarTypeId());
			}
		}		
	}
	
	/**
	 * 通过seriesId查询出下属车名系列
	 * @param seriesId
	 * @return
	 */
	public static void updateSeriesFilterBySeriesId(int oldModelSift,Long seriesId,Long userId){
		
		String sql = "UPDATE Tm_Car_Name T SET T.Old_Model_Sift = ? WHERE T.Series_Id = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(oldModelSift);
		params.add(seriesId);
		factory.update(sql,params);		
		
		
		TmCarNamePO po = new TmCarNamePO();
		po.setSeriesId(seriesId);		
		List<TmCarNamePO> pos = factory.select(po);
		
		if(pos.size() > 0){
			for (TmCarNamePO c : pos) {				
				updateCarShortNameFilterByCarNameId(oldModelSift,c.getCarNameId(),userId);
			}
		}		
	}
	
	
	/**
	 * 通过brandId查询出下属系列
	 * @param brandId
	 * @return
	 */
	public static void updateBrandIdFilterBySeriesId(int oldModelSift,Long brandId,Long userId){
		
		String sql = "UPDATE Tm_Series T SET T.Old_Model_Sift = ? WHERE T.Brand_Id = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(oldModelSift);
		params.add(brandId);
		factory.update(sql,params);			
		
		TmSeriesPO po = new TmSeriesPO();
		po.setBrandId(brandId);		
		List<TmSeriesPO> pos = factory.select(po);
		
		if(pos.size() > 0){
			for (TmSeriesPO c : pos) {				
				updateSeriesFilterBySeriesId(oldModelSift,c.getSeriesId(),userId);
			}
		}		
	}
}

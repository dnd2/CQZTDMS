/**********************************************************************
* <pre>
* FILE : ServiceActivityManageItemDao.java
* CLASS : ServiceActivityManageItemDao
*
* AUTHOR : PGM
*
* FUNCTION : 服务活动管理--活动项目
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-03| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageItemDao.java,v 1.1 2010/08/16 01:42:19 yuch Exp $
 */
package com.infodms.dms.dao.claim.serviceActivity;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsActivityNetitemPO;
import com.infodms.dms.po.TtAsActivityPartsPO;
import com.infodms.dms.po.TtAsActivityRepairitemPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  服务活动管理--活动项目
 * @author        :  PGM
 * CreateDate     :  2010-06-03
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityManageItemDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityManageItemDao.class);
	private static final ServiceActivityManageItemDao dao = new ServiceActivityManageItemDao ();
	public  static final ServiceActivityManageItemDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function       :  服务管理活动信息---工时查询WorkingHours
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public  List<TtAsActivityBean>  getWorkingHoursInfoList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT UPDATE_DATE,  CREATE_DATE, CREATE_BY, UPDATE_BY, ITEM_CODE, ITEM_ID, ACTIVITY_ID, ITEM_NAME, LABOR_FEE,  NORMAL_LABOR,  STAT_TITLE   \n");
		sql.append("  FROM TT_AS_ACTIVITY_REPAIRITEM \n");
		if (activityId!=null&&!("").equals(activityId)){
		sql.append("  WHERE  ACTIVITY_ID = ? \n");
		params.add(activityId);
		sql.append("");
		sql.append("order by ACTIVITY_ID desc ");
		}
        List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		return list;
	}
	/**
	 * Function       :  服务管理活动信息---活动配件
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public  List<TtAsActivityBean>  getPartsList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT UPDATE_DATE,  CREATE_DATE, CREATE_BY, UPDATE_BY, PART_NO, PART_NAME, IS_MAIN_PART, PART_AMOUNT, ACTIVITY_ID, PART_PRICE, PART_QUANTITY, PART_UNIT, PARTS_ID   \n");
		sql.append("  FROM TT_AS_ACTIVITY_PARTS \n");
		if (activityId!=null&&!("").equals(activityId)){
		sql.append("  WHERE  ACTIVITY_ID = ? \n");
		params.add(activityId);
		sql.append("");
		sql.append("order by ACTIVITY_ID desc ");
		}
        List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function       :  服务管理活动信息---活动其它项目
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public  List<TtAsActivityBean>  getNetItemList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  select ID,  UPDATE_DATE, REMARK,  CREATE_DATE, CREATE_BY, UPDATE_BY, ITEM_CODE AS ITEM_CODES, ITEM_DESC,  AMOUNT, ACTIVITY_ID   \n");
		sql.append("  FROM TT_AS_ACTIVITY_NETITEM \n");
		if (activityId!=null&&!("").equals(activityId)){
		sql.append("  WHERE  ACTIVITY_ID = ? \n");
		params.add(activityId);
		sql.append(" \n");
		sql.append("  ORDER BY ACTIVITY_ID desc \n");
		}
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		return list;
	}
	/**
	 * Function         : 服务活动管理---工时维护查询
	 * @param           : 活动ID
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的工时信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-03
	 */
	public  PageResult<Map<String, Object>>  getServiceActivityManageItemWorkHoursQuery(TtAsActivityBean MantainBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	select ww.ID as labourId,  ww.LABOUR_CODE, ww.CN_DES, ww.LABOUR_HOUR  \n");
		sql.append("    from TT_AS_WR_WRLABINFO ww \n");
		if(!"".equals(MantainBean.getActivityId())&&!(null==MantainBean.getActivityId())){//活动ID
		sql.append("    where TREE_CODE=3 and IS_DEL=0 and  not exists (  select ITEM_CODE from TT_AS_ACTIVITY_REPAIRITEM T  where ww.LABOUR_CODE = T.item_code AND ACTIVITY_ID ="+MantainBean.getActivityId()+") \n");
		}
		if(!"".equals(MantainBean.getLabourCode())&&!(null==MantainBean.getLabourCode())){//工时代码
			sql.append("    and  UPPER(ww.LABOUR_CODE) like  UPPER('%"+MantainBean.getLabourCode()+"%') \n");
			}
		if(!"".equals(MantainBean.getCnDes())&&!(null==MantainBean.getCnDes())){//工时名称
			sql.append("    and  ww.LABOUR_CODE like '%"+MantainBean.getCnDes()+"%' \n");
			}
		sql.append("order by ww.LABOUR_CODE desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * Function       :   新增
	 * @param         :  request-工单号
	 * @return        :  服务活动管理---工时新增
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public static void serviceActivityManageItemWorkHoursOption(String []groupIdsArray,String []labourCodeArray,String []cnDesArray,String []labourHourArray,TtAsActivityRepairitemPO RepairitemPO) {
		//TtAsActivityRepairitemPO itemPO=new TtAsActivityRepairitemPO();
		//itemPO.setActivityId(RepairitemPO.getActivityId());
		//dao.delete(itemPO);
		for (int i = 0;i<groupIdsArray.length;i++) {
			RepairitemPO.setItemId(Long.parseLong(SequenceManager.getSequence("")));
			RepairitemPO.setItemCode(labourCodeArray[i]);
			RepairitemPO.setItemName(cnDesArray[i]);
			RepairitemPO.setNormalLabor(Float.parseFloat(labourHourArray[i]));
			dao.insert(RepairitemPO);
		}
    }
	/**
	 * Function       :  工时删除
	 * @param         :  request-活动ID,ItemId
	 * @return        :  服务活动管理---工时删除
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public static void deleteItemWorkHoursOption(TtAsActivityRepairitemPO RepairitemPO) {
		dao.delete(RepairitemPO);
    }
	/**
	 * Function         : 服务活动管理---配件维护查询
	 * @param           : 活动ID
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的工时信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-03
	 */
	public  PageResult<Map<String, Object>>  getServiceActivityManagePartsQuery(TtAsActivityBean MantainBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	select PART_ID, PART_CODE, PART_NAME ,a.CLAIM_PRICE,c.SUPPLIER_CODE ,c.SUPPLIER_NAME  \n");
		sql.append("    from TM_PT_PART_BASE a \n");
		sql.append("    left outer join TM_PT_PART_SUP_RELATION b on a.part_id=b.order_id  \n");
		sql.append("    left outer join TM_PT_SUPPLIER c on b.supplier_id=c.supplier_id  \n");
		if(!"".equals(MantainBean.getCompanyId())&&!(null==MantainBean.getCompanyId())){//公司ID不为空
			sql.append("  where 1=1  \n");
		}
		if(!"".equals(MantainBean.getActivityId())&&!(null==MantainBean.getActivityId())){//活动ID
		sql.append("    and  not exists (  select * from TT_AS_ACTIVITY_PARTS where a.PART_CODE = part_no AND ACTIVITY_ID ="+MantainBean.getActivityId()+" \n");
		}
		sql.append(" ) \n");
		if(!"".equals(MantainBean.getPartNo())&&!(null==MantainBean.getPartNo())){//配件代码
			sql.append("    and  UPPER(a.PART_CODE) like  UPPER('%"+MantainBean.getPartNo()+"%') \n");
			}
		if(!"".equals(MantainBean.getPartName())&&!(null==MantainBean.getPartName())){//配件名称
			sql.append("    and  a.PART_NAME like '%"+MantainBean.getPartName()+"%' \n");
			}
		sql.append("order by a.PART_CODE desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * Function       :  配件新增
	 * @param         :  request-工单号
	 * @return        :  服务活动管理---配件新增
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */                                                       
	public static void serviceActivityManageItemPartsOption(String []partsIdArray,String []partNoArray,String []partNameArray,String []partsQuantityArray,String []claimPriceArray,String []supplierCodeArray,String []supplierNameArray,TtAsActivityPartsPO PartsPO) {
		//TtAsActivityPartsPO partPO=new TtAsActivityPartsPO();
		//partPO.setActivityId(PartsPO.getActivityId());
		//dao.delete(partPO);
		for (int i = 0;i<partsIdArray.length;i++) {
			//PartsPO.setPartsId(Long.parseLong(partsIdArray[i]));
			PartsPO.setPartsId(Long.parseLong(SequenceManager.getSequence("")));
			PartsPO.setPartNo(partNoArray[i]);
			PartsPO.setPartName(partNameArray[i]);
			PartsPO.setPartQuantity(Float.parseFloat(partsQuantityArray[i]));
			if(null!=claimPriceArray&&!"".equals(claimPriceArray)){
				PartsPO.setPartPrice(Float.parseFloat(claimPriceArray[i]));
				PartsPO.setPartAmount(PartsPO.getPartQuantity()*PartsPO.getPartPrice());
			}
			if(null!=supplierCodeArray&&!"".equals(supplierCodeArray)){
				PartsPO.setSupplierCode(supplierCodeArray[i]);
			}
			if(null!=supplierNameArray&&!"".equals(supplierNameArray)){
				PartsPO.setSupplierName(supplierNameArray[i]);
			}
			dao.insert(PartsPO);
		}
    }
	/**
	 * Function       :  配件删除
	 * @param         :  request-活动ID,ItemId
	 * @return        :  服务活动管理---配件删除
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public static void deleteItemPartsOption(TtAsActivityPartsPO PartsPO) {
		dao.delete(PartsPO);
    }
	/**
	 * Function         : 服务活动管理---活动其它项目查询
	 * @param           : 活动ID
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的工时信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-03
	 */
	public  PageResult<Map<String, Object>>  getServiceActivityManageOthersQuery(TtAsActivityBean MantainBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	select FEE_ID, FEE_CODE, FEE_NAME  \n");
		sql.append("    from TT_AS_WR_OTHERFEE  \n");
		if(!"".equals(MantainBean.getActivityId())&&!(null==MantainBean.getActivityId())){//活动ID
		sql.append("   where IS_DEL=0 and FEE_CODE not in (select ITEM_CODE from TT_AS_ACTIVITY_NETITEM  where ACTIVITY_ID  ="+MantainBean.getActivityId()+") \n");
		}
		sql.append("order by FEE_CODE desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * Function       :  活动其它项目新增
	 * @param         :  request-工单号
	 * @return        :  服务活动管理---活动其它项目新增
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */                                                              
	public static void serviceActivityManageItemOthersOption(String []idArray,String []itemCodeArray,String []itemDescArray,TtAsActivityNetitemPO NetitemPO) {
		//TtAsActivityNetitemPO otherPO=new TtAsActivityNetitemPO();
		//otherPO.setActivityId(NetitemPO.getActivityId());
		//dao.delete(otherPO);
		for (int i = 0;i<itemCodeArray.length;i++) {
			//NetitemPO.setId((Long.parseLong(idArray[i])));
			NetitemPO.setId(Long.parseLong(SequenceManager.getSequence("")));
			NetitemPO.setItemCode(itemCodeArray[i]);
			NetitemPO.setItemDesc(itemDescArray[i]);
			dao.insert(NetitemPO);
		}
    }
	/**
	 * Function       :  活动其它项目删除
	 * @param         :  request-活动ID,ItemId
	 * @return        :  服务活动管理---活动其它项目删除
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public static void deleteItemOthersOption(TtAsActivityNetitemPO NetitemPO) {
		dao.delete(NetitemPO);
    }
}
package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesChaHisPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : SendAssignmentDao 
 * @Description   : 配车调整DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-26
 */
public class AllocaAdjustDao  extends BaseDao<PO>{
	private static final AllocaAdjustDao dao = new AllocaAdjustDao ();
	public static final AllocaAdjustDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * 组板信息查询(配置页面)
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getAllocaSeachQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		/******************************页面查询字段start**************************/
		String raiseStartDate =CommonUtils.checkNull((String)map.get("raiseStartDate")); // 提报日期开始
		String raiseEndDate = CommonUtils.checkNull((String)map.get("raiseEndDate")); // 提报日期结束
		String boNo = CommonUtils.checkNull((String)map.get("boNo")); //组板编号
		String vin = CommonUtils.checkNull((String)map.get("VIN")); //VIN
		String poseId = CommonUtils.checkNull((String)map.get("poseId"));
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT   A.BO_ID,A.BO_NO,\n");
		sbSql.append("         B.NAME,\n");
		sbSql.append("         TO_CHAR (A.BO_DATE, 'YYYY-MM-DD HH24:MI:SS') AS BO_DATE,\n");
		sbSql.append("         TO_CHAR (A.ALLOCA_DATE, 'YYYY-MM-DD HH24:MI:SS') AS ALLOCA_DATE,\n");
		sbSql.append("         DECODE(A.HAVE_RETAIL,'1','是','0','否',null,'否') HAVE_RETAIL,\n");
		sbSql.append("         NVL (A.BO_NUM, 0) BO_NUM,\n");
		sbSql.append("         NVL (A.ALLOCA_NUM, 0) ALLOCA_NUM,\n");
		sbSql.append("         NVL (A.OUT_NUM, 0) OUT_NUM,\n");
		sbSql.append("         NVL (A.SEND_NUM, 0) SEND_NUM,\n");
		sbSql.append("         NVL (A.ACC_NUM, 0) ACC_NUM,\n");
		sbSql.append("(select count(1) FUK_NUM\n");
		sbSql.append("  from TT_SALES_BO_DETAIL A1, TT_SALES_ALLOCA_DE B1, TM_VEHICLE C1\n");
		sbSql.append(" WHERE A1.BO_DE_ID = B1.BO_DE_ID\n");
		sbSql.append("   AND B1.VEHICLE_ID = C1.VEHICLE_ID\n");
		sbSql.append("   AND A.BO_ID=A1.BO_ID\n");
		sbSql.append("   AND A1.ORDER_TYPE IN ( "+Constant.ACC_STATUS_01+","+Constant.ACC_STATUS_03+")\n");
		sbSql.append("   AND A1.IS_ENABLE="+Constant.IF_TYPE_YES+"\n"); 
		sbSql.append("   AND C1.OUT_STATUS IN ("+Constant.PAYMENT_STATUS_02+", "+Constant.PAYMENT_STATUS_03+")) FUK_NUM,"); //中转库是否已有付款的或正在付款的车辆（有的话无法取消组板或配车）
		sbSql.append("(SELECT T.SEND_TYPE FROM TT_SALES_BO_DETAIL T WHERE  T.BO_ID=A.BO_ID AND ROWNUM=1) SEND_TYPE"); 
		sbSql.append("  FROM   TT_SALES_BOARD A, TC_USER B\n");
		sbSql.append(" WHERE   A.BO_PER = B.USER_ID\n");
		sbSql.append(" AND A.IS_ENABLE=").append(Constant.IF_TYPE_YES).append("\n"); //有效的
		sbSql.append(" AND A.HANDLE_STATUS IN("+Constant.HANDLE_STATUS03+","+
				Constant.HANDLE_STATUS04+","+Constant.HANDLE_STATUS05+")\n"); //未生成运单前都可换车
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("A.AREA_ID",poseId));//车厂端查询列表产地数据权限
		if(!raiseStartDate.equals("")){
			sbSql.append("AND A.BO_DATE>=TO_DATE('"+raiseStartDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!raiseEndDate.equals("")){
			sbSql.append("AND A.BO_DATE<=TO_DATE('"+raiseEndDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(boNo)){
			sbSql.append("AND A.BO_NO LIKE '%"+boNo+"%'");
		}
		if(!"".equals(vin)){
			sbSql.append(MaterialGroupManagerDao.geVinSql("A.BO_ID",vin)); 
		}
		PageResult<Map<String, Object>> ps= dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 添加换车历史记录
	 * @param po 参数TtSalesChaHisPO实体
	 * @return
	 * @throws Exception
	 */
	public void addCarHis(TtSalesChaHisPO po) throws Exception{
		dao.insert(po);
	}
	/**
	 * 根据组板ID添加取消配车详细信息道删除配车表里面
	 * @param 组板ID
	 * @return
	 * @throws Exception
	 */
	public void addAllocaDeDel(Long boId,Long userId) {
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("INSERT INTO TT_SALES_ALLOCA_DE_DEL\n");
		sbSql.append("  (DETAIL_ID,\n");
		sbSql.append("   BO_DE_ID,\n");
		sbSql.append("   VEHICLE_ID,\n");
		sbSql.append("   ALLOCA_DATE,\n");
		sbSql.append("   ALLOCA_PER,\n");
		sbSql.append("   CREATE_BY,\n");
		sbSql.append("   CREATE_DATE,\n");
		sbSql.append("   UPDATE_BY,\n");
		sbSql.append("   UPDATE_DATE,\n");
		sbSql.append("   IS_OUT,\n");
		sbSql.append("   OUT_DATE,\n");
		sbSql.append("   OUT_BY,\n");
		sbSql.append("   IS_SEND,\n");
		sbSql.append("   SEND_DATE,\n");
		sbSql.append("   SEND_BY,\n");
		sbSql.append("   IS_ACC,\n");
		sbSql.append("   ACC_DATE,\n");
		sbSql.append("   ACC_BY)\n");
		sbSql.append("  SELECT TSAD.DETAIL_ID,\n");
		sbSql.append("         TSAD.BO_DE_ID,\n");
		sbSql.append("         TSAD.VEHICLE_ID,\n");
		sbSql.append("         TSAD.ALLOCA_DATE,\n");
		sbSql.append("         TSAD.ALLOCA_PER,\n");
		sbSql.append("         ?,\n");
		sbSql.append("         SYSDATE,\n");
		sbSql.append("         TSAD.UPDATE_BY,\n");
		sbSql.append("         TSAD.UPDATE_DATE,\n");
		sbSql.append("         TSAD.IS_OUT,\n");
		sbSql.append("         TSAD.OUT_DATE,\n");
		sbSql.append("         TSAD.OUT_BY,\n");
		sbSql.append("         TSAD.IS_SEND,\n");
		sbSql.append("         TSAD.SEND_DATE,\n");
		sbSql.append("         TSAD.SEND_BY,\n");
		sbSql.append("         TSAD.IS_ACC,\n");
		sbSql.append("         TSAD.ACC_DATE,\n");
		sbSql.append("         TSAD.ACC_BY\n");
		sbSql.append("    FROM TT_SALES_BOARD     TSB,\n");
		sbSql.append("         TT_SALES_BO_DETAIL TSBD,\n");
		sbSql.append("         TT_SALES_ALLOCA_DE TSAD\n");
		sbSql.append("   WHERE TSB.BO_ID = TSBD.BO_ID\n");
		sbSql.append("     AND TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
		sbSql.append("     AND TSB.BO_ID = ?"); 
		List<Object> parm=new ArrayList<Object>();
		parm.add(userId);
		parm.add(boId);
		factory.insert(sbSql.toString(), parm);		
	}
	/**
	 * 根据组板ID删除配车详细表信息
	 * @param vehicleIds 车辆IDS
	 * @return
	 * @throws Exception
	 */
	public void delAllocaDe(Long boId) {
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("DELETE FROM TT_SALES_ALLOCA_DE TSAD\n");
		sbSql.append(" WHERE EXISTS (SELECT TSAD.BO_DE_ID\n");
		sbSql.append("          FROM TT_SALES_BOARD TSB, TT_SALES_BO_DETAIL TSBD\n");
		sbSql.append("         WHERE TSB.BO_ID = TSBD.BO_ID\n");
		sbSql.append("           AND TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
		sbSql.append("           AND TSB.BO_ID = ?)"); 
		List<Object> parm=new ArrayList<Object>();
		parm.add(boId);
		factory.delete(sbSql.toString(), parm);
	}
	/**
	 * 修改车辆锁定状态
	 * @param 组板ID
	 * @return
	 * @throws Exception
	 */
	public void updateVehicleStatus(Long boId,Long userId) {
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("UPDATE TM_VEHICLE TV\n");
		sbSql.append("   SET TV.LOCK_STATUS = ?,TV.UPDATE_BY = ?,TV.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE EXISTS (SELECT TSAD.VEHICLE_ID\n");
		sbSql.append("          FROM TT_SALES_BOARD     TSB,\n");
		sbSql.append("               TT_SALES_BO_DETAIL TSBD,\n");
		sbSql.append("               TT_SALES_ALLOCA_DE TSAD\n");
		sbSql.append("         WHERE TSB.BO_ID = TSBD.BO_ID\n");
		sbSql.append("           AND TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
		sbSql.append("           AND TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append("           AND TSB.BO_ID = ?)"); 
		List<Object> parm=new ArrayList<Object>();
		parm.add(Constant.LOCK_STATUS_01);//正常状态
		parm.add(userId);
		parm.add(boId);
		factory.update(sbSql.toString(), parm);		
	}
	/**
	 * 获取某组板下出库的数量
	 * @param 组板ID
	 * @return
	 * @throws Exception
	 */
	public int getAllocaCount(Long boId) {
		int count=0;
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("select nvl(t.out_num,0) out_num from tt_sales_board t where t.bo_id=?"); 
		List<Object> parm=new ArrayList<Object>();
		parm.add(boId);
		 count=Integer.parseInt(dao.pageQuery(sbSql.toString(), parm, getFunName()).get(0).get("OUT_NUM").toString());
		 return count;
	}
	/**
	 * 获取与中转库关联的数量
	 * @param 组板ID
	 * @return
	 * @throws Exception
	 */
	public int getOutOrderCount(Long vehicleId) {
		int count=0;
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("select count(1) c_count\n");
		sbSql.append("  from tm_vehicle s, TT_OUT_ORDER_DETAIL s1\n");
		sbSql.append(" where s.out_detail_id = s1.detail_id\n");
		sbSql.append("   and s.out_status in (?, ?)\n");
		sbSql.append("   and s.vehicle_id = ?"); 
		List<Object> parm=new ArrayList<Object>();
		parm.add(Constant.PAYMENT_STATUS_03);
		parm.add(Constant.PAYMENT_STATUS_02);
		parm.add(vehicleId);
		 count=Integer.parseInt(dao.pageQuery(sbSql.toString(), parm, getFunName()).get(0).get("C_COUNT").toString());
		 return count;
	}
	/**
	 * 修改车辆表out_status状态
	 * @param params 参数list
	 * @return
	 * @throws Exception
	 */
	public void updateVehicleOutStatus(List<Object> params) throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("UPDATE TM_VEHICLE TV\r\n");
		sbSql.append("   SET TV.OUT_STATUS =\r\n");
		sbSql.append("       (SELECT DECODE(A.ORDER_TYPE, ?, ?, ?) O_STATUS\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL A\r\n");
		sbSql.append("         WHERE A.BO_DE_ID = ?),\r\n");
		sbSql.append("       TV.LOCK_STATUS = ?,\r\n");
		sbSql.append("       TV.DEALER_ID  =\r\n");
		sbSql.append("       (SELECT CASE\r\n");
		sbSql.append("                 WHEN a.order_type = 10201005 then\r\n");
		sbSql.append("                  a.DEALER_ID\r\n");
		sbSql.append("                 ELSE\r\n");
		sbSql.append("                  a.rec_DEALER_ID\r\n");
		sbSql.append("               END O_STATUS\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL A\r\n");
		sbSql.append("         WHERE A.BO_DE_ID = ?),\r\n");
		sbSql.append("       TV.UPDATE_BY   = ?,\r\n");
		sbSql.append("       TV.UPDATE_DATE = SYSDATE\r\n");
		sbSql.append(" WHERE TV.VEHICLE_ID = ?"); 
		dao.update(sbSql.toString(), params);
	}
}

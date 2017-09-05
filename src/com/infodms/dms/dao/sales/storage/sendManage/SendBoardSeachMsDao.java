package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : SendAssignmentDao 
 * @Description   : 发运组板明细查询DAO 
 * @author        : ranjian
 * CreateDate     : 2013-11-12
 */
public class SendBoardSeachMsDao extends BaseDao<PO>{
	private static final SendBoardSeachMsDao dao = new SendBoardSeachMsDao ();
	public static final SendBoardSeachMsDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * 组板信息明细查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getSendBoardSeachQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		Object[] obj=getSQL(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 导出EXCEL
	 * @param params 查询参数LIST
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSendBoardSeachQueryExport(Map<String, Object> map){
		Object[] obj=getSQL(map);
		List<Map<String, Object>> list= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName());
		return list;
	}
	/**
	 * 查询统计
	 *param map 查询参数
	 *@return 
	 */
	public Map<String,Object> tgSum(Map<String, Object> map){
		Object[] obj=getSQL(map);
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT SUM(NVL(BO_NUM, 0)) BO_NUM,\n");
		sbSql.append("       SUM(NVL(ALLOCA_NUM, 0)) ALLOCA_NUM,\n");
		sbSql.append("       SUM(NVL(SEND_NUM, 0)) SEND_NUM,\n");
		sbSql.append("       SUM(NVL(ACC_NUM, 0)) ACC_NUM,\n");
		sbSql.append("       SUM(NVL(OUT_NUM, 0)) OUT_NUM FROM (\n");
		sbSql.append(obj[0].toString());
		sbSql.append(")\n");
		Map<String, Object> mapR= dao.pageQueryMap(sbSql.toString(), (List<Object>)obj[1], getFunName());
		return mapR;
	}
	//get sql
	public Object[] getSQL(Map<String, Object> map){
		/******************************页面查询字段start**************************/
		String raiseStartDate =CommonUtils.checkNull((String)map.get("raiseStartDate")); // 提报日期开始
		String raiseEndDate = CommonUtils.checkNull((String)map.get("raiseEndDate")); // 提报日期结束
		String boNo = CommonUtils.checkNull((String)map.get("boNo")); //组板编号
		String poseId = CommonUtils.checkNull((String)map.get("poseId"));
		String yieldly = CommonUtils.checkNull((String)map.get("yieldly")); //产地
		String logiName = CommonUtils.checkNull((String)map.get("logiName")); //物流商
		String transportType = CommonUtils.checkNull((String)map.get("transportType"));//发运方式
		String dealerCode = CommonUtils.checkNull((String)map.get("dealerCode"));//经销商
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		params.add(Constant.IF_TYPE_YES);
		params.add(Constant.IF_TYPE_YES);
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("\n");
		sbSql.append("SELECT A.BO_ID,\n");
		sbSql.append("       A.BO_NO,\n");
		sbSql.append("       B.NAME,\n");
		sbSql.append("       A.HANDLE_STATUS,\n");
		sbSql.append("       DECODE(A.HAVE_RETAIL, '1', '是', '0', '否', null, '否') HAVE_RETAIL,\n");
		sbSql.append("       TO_CHAR(A.BO_DATE, 'YYYY-MM-DD HH24:MI:SS') AS BO_DATE,\n");
		sbSql.append("       H.DEALER_NAME, --发运经销商名称\n");
		sbSql.append("       E.REGION_NAME P_NAME, --省份\n");
		sbSql.append("       F.REGION_NAME C_NAME, --城市\n");
		sbSql.append("       G.REGION_NAME D_NAME, --地区\n");
		sbSql.append("       NVL(C.BOARD_NUM, 0) BO_NUM,\n");
		sbSql.append("       NVL(C.ALLOCA_NUM, 0) ALLOCA_NUM,\n");
		sbSql.append("       NVL(C.OUT_NUM, 0) OUT_NUM,\n");
		sbSql.append("       NVL(C.SEND_NUM, 0) SEND_NUM,\n");
		sbSql.append("       NVL(C.ACC_NUM, 0) ACC_NUM\n");
		sbSql.append("  FROM TT_SALES_BOARD     A,\n");
		sbSql.append("       TC_USER            B,\n");
		sbSql.append("       TT_SALES_BO_DETAIL C,\n");
		sbSql.append("       TM_VS_ADDRESS      D,\n");
		sbSql.append("       TM_REGION          E,\n");
		sbSql.append("       TM_REGION          F,\n");
		sbSql.append("       TM_REGION          G,\n");
		sbSql.append("       TM_DEALER          H\n");
		sbSql.append(" WHERE A.BO_PER = B.USER_ID\n");
		sbSql.append("   AND A.BO_ID = C.BO_ID\n");
		sbSql.append("   AND C.ADDRESS_ID = D.ID\n");
		sbSql.append("   AND D.PROVINCE_ID = E.REGION_CODE\n");
		sbSql.append("   AND D.CITY_ID = F.REGION_CODE\n");
		sbSql.append("   AND D.AREA_ID = G.REGION_CODE\n");
		sbSql.append("   AND C.REC_DEALER_ID = H.DEALER_ID\n");
		sbSql.append("   AND A.IS_ENABLE = ?\n");
		sbSql.append("   AND C.IS_ENABLE = ?"); 
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSqlByPar("A.AREA_ID",poseId,params));//车厂端查询列表产地数据权限
		sbSql.append(MaterialGroupManagerDao.getPoseIdLogiSqlByPar("C.LOGI_ID",poseId,params));//物流判断（是否是物流公司）
		if(!raiseStartDate.equals("")){
			sbSql.append("AND A.BO_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(raiseStartDate+" 00:00:00");
		}
		if(!raiseEndDate.equals("")){
			sbSql.append("AND A.BO_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(raiseEndDate+" 23:59:59");
		}
		if(!"".equals(boNo)){
			sbSql.append("AND A.BO_NO LIKE ?");
			params.add("%"+boNo+"%");
		}
		if(!"".equals(yieldly)){
			sbSql.append("AND A.AREA_ID=?\n");
			params.add(Long.valueOf(yieldly));
		}
		if(!"".equals(logiName)){
			sbSql.append("AND C.LOGI_ID=?"); 
			params.add(Long.valueOf(logiName));
		}
		if(!"".equals(transportType)){
			sbSql.append("AND C.SEND_TYPE=?\n");
			params.add(transportType);
		}
		if(!"".equals(dealerCode)){
			sbSql.append(Utility.getConSqlByParamForEqual(dealerCode, params,"H", "DEALER_CODE"));
		}
		sbSql.append("  ORDER BY A.BO_ID, A.BO_DATE ASC\n");
		Object[] arr=new Object[2];
		arr[0]=sbSql;
		arr[1]=params;
		return arr;
	}
}

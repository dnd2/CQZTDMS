package com.infodms.dms.dao.parts.purchaseOrderManager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 配件移位dao
 * @author 
 * @version 2017-8-22
 * @see 
 * @since 
 * @deprecated
 */
@SuppressWarnings("rawtypes")
public class PartDisplacementDao  extends BaseDao  {

	public static Logger logger = Logger.getLogger(PartDisplacementDao.class);

    private static final PartDisplacementDao dao = new PartDisplacementDao();

    private PartDisplacementDao() {

    }

    public static final PartDisplacementDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }
    
    /**
	 * 查询入库信息
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPoInInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String IN_CODE = request.getParamValue("IN_CODE");//入库单号
		String sCreateDate = request.getParamValue("sCreateDate");//入库日期开始时间
		String eCreateDate = request.getParamValue("eCreateDate");//入库日期结束时间
		
//		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
//		String PART_CNAME = request.getParamValue("PART_CNAME");
//		String PART_CODE = request.getParamValue("PART_CODE");

		sql.append(" SELECT IN_ID,IN_CODE,PART_ID,PART_CODE,PART_OLDCODE,PART_CNAME,UNIT,VENDER_NAME,WH_ID,WH_NAME,");
		sql.append(" BATCH_NO,LOC_ID,LOC_CODE,IN_QTY,PRODUCE_STATE,IN_DATE,STATE FROM TT_PART_PO_IN ");
		sql.append(" WHERE TO_CHAR(IN_DATE,'YYYY-MM-DD')>=? ");
		sql.append(" AND TO_CHAR(IN_DATE,'YYYY-MM-DD')<=? ");
		sql.append(" AND ORG_ID=2010010100070674 AND STATUS=1 ");
		sql.append(" AND IN_TYPE=92311001 ");
		params.add(sCreateDate);
		params.add(eCreateDate);
		if(StringUtil.notNull(IN_CODE)){
			sql.append("  AND IN_CODE LIKE ? ");
			params.add("%"+IN_CODE.trim()+"%");
		}
		sql.append("  ORDER BY IN_DATE DESC");


		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
    
	
//	public  Map<String, Object> queryPartInIdInfo(String inId) {
//        StringBuffer sql = new StringBuffer();
//		List<Object> params = new ArrayList<Object>();
//		sql.append(" SELECT IN_ID,IN_CODE,PART_ID,PART_CODE,PART_OLDCODE,PART_CNAME,UNIT,VENDER_NAME,WH_ID,WH_NAME,");
//		sql.append(" BATCH_NO,LOC_ID,LOC_CODE,IN_QTY,PRODUCE_STATE,IN_DATE,STATE FROM TT_PART_PO_IN ");
//		sql.append(" WHERE IN_ID = ? ");
//		params.add(inId);
//		List<Map<String, Object>> list = this.pageQuery(sql.toString(), params, this.getFunName());
//        if (list.size() <= 0) {
//            return null;
//        }
//        return list.get(0);
//	}
//	public List<Map<String, Object>> queryPartInIdInfo(String inId) {
//        StringBuffer sql = new StringBuffer();
//		List<Object> params = new ArrayList<Object>();
//		sql.append(" SELECT IN_ID,IN_CODE,PART_ID,PART_CODE,PART_OLDCODE,PART_CNAME,UNIT,VENDER_NAME,WH_ID,WH_NAME,");
//		sql.append(" BATCH_NO,LOC_ID,LOC_CODE,IN_QTY,PRODUCE_STATE,IN_DATE,STATE FROM TT_PART_PO_IN ");
//		sql.append(" WHERE IN_ID = ? ");
//		params.add(inId);
//		List<Map<String, Object>> list = pageQuery(sql.toString(), params, this.getFunName());
//		return list;
//	}
    
	
	
	public PageResult<Map<String, Object>> queryInIdInfo(String inId, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append(" SELECT IN_ID,IN_CODE,PART_ID,PART_CODE,PART_OLDCODE,PART_CNAME,UNIT,VENDER_NAME,WH_ID,WH_NAME,");
		sql.append(" BATCH_NO,LOC_ID,LOC_CODE,IN_QTY,PRODUCE_STATE,IN_DATE,STATE,ORG_ID, ");
		sql.append(" NVL((select NORMAL_QTY from tt_part_book tb  ");
		sql.append(" 	  where tb.PART_ID=ti.part_id and tb.LOC_ID=ti.LOC_ID and tb.WH_ID=ti.wh_id and tb.ORG_ID=ti.org_id and tb.BATCH_NO=ti.BATCH_NO ) ");
		sql.append(" ,0) STOCK_QTY ");
		sql.append(" FROM TT_PART_PO_IN ti ");
		sql.append(" WHERE IN_ID = ? ");
		
		params.add(inId);
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
}

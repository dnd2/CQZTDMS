package com.infodms.dms.dao.vehicleInfoManage;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsQuelityFollowPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class QualityInfoReportDao extends BaseDao {
	public static final Logger logger = Logger.getLogger(QualityInfoReportDao.class);
	private static final QualityInfoReportDao dao = new QualityInfoReportDao();
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public static final QualityInfoReportDao getInstance() {
		return dao;
	}
	public Map<String,Object> queryDataByVin(String vin){
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("select b.dealer_code DEALER_CODE,\r\n");
		sbSql.append("       b.dealer_name DEALER_NAME,\r\n");
		sbSql.append("       c.group_name MODEL_NAME,\r\n");
		sbSql.append("       a.model_id MODEL_ID,\r\n");
		sbSql.append("       a.engine_no ENGINE_NO,\r\n");
		sbSql.append("		 a.mileage MILEAGE,"); 
		sbSql.append("       to_char(a.product_date, 'yyyy-MM-dd') PRODUCT_DATE,\r\n");
		sbSql.append("       to_char(a.purchased_date, 'yyyy-MM-dd') BUY_DATE,\r\n");
		sbSql.append("       e.ctm_name CTM_NAME,\r\n");
		sbSql.append("       e.main_phone PHONE\r\n");
		sbSql.append("  from tm_vehicle a\r\n");
		sbSql.append("  left join tm_dealer b\r\n");
		sbSql.append("    on a.dealer_id = b.dealer_id\r\n");
		sbSql.append("  left join tm_vhcl_material_group c\r\n");
		sbSql.append("    on a.model_id = c.group_id\r\n");
		sbSql.append("  left join tt_dealer_actual_sales d\r\n");
		sbSql.append("    on a.vehicle_id = d.vehicle_id\r\n");
		sbSql.append("  left join tt_customer e\r\n");
		sbSql.append("    on e.ctm_id = d.ctm_id\r\n");
		sbSql.append(" where a.vin = '"+vin+"'"); 
		Map<String, Object> ps = pageQueryMap(sbSql.toString(), null, getFunName());
		return ps;
	}
	
	public Map<String,Object> queryDataById(String dealerId){
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("select b.dealer_code DEALER_CODE,\r\n");
		sbSql.append("       b.dealer_name DEALER_NAME \r\n");
		sbSql.append("  from tm_dealer b\r\n");
		sbSql.append(" where b.dealer_id = "+dealerId); 
		Map<String, Object> ps = pageQueryMap(sbSql.toString(), null, getFunName());
		return ps;
	}
	
	public List<FsFileuploadPO> queryAttById(String id) {
		StringBuffer sql = new StringBuffer();
		List<FsFileuploadPO> ls = new ArrayList<FsFileuploadPO>();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A ");
		// sql.append(" LEFT OUTER JOIN FS_FILEUPLOAD B ON B.YWZJ = A.ID ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND A.YWZJ='" + id + "'");
		ls = select(FsFileuploadPO.class, sql.toString(), null);
		return ls;
	}

	public PageResult<Map<String, Object>> queryQualityInfoReport(
			String reportName, String ctmName, String dealerName,
			String dealerId,Integer pageSize, Integer curPage) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select *\r\n");
		sbSql.append("  from TT_SALES_QUALITY_INFO_REPORT t\r\n");
		sbSql.append(" where DEALER_ID ='"+dealerId+"' \r\n");
		if(StringUtil.notNull(reportName)){
			sbSql.append("   and t.report_name like '%"+reportName+"%'\r\n");
		}
		if(StringUtil.notNull(ctmName)){
			sbSql.append("   and t.ctm_name like '%"+ctmName+"%'"); 
		}
		if(StringUtil.notNull(dealerName)){
			sbSql.append("   and t.dealer_name like '%"+dealerName+"%'"); 
		}
		
		sbSql.append("order by t.create_date desc");
		return (PageResult<Map<String, Object>>)this.pageQuery(sbSql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}

	public void deleteQualityInfoReportDao(String ids) {
		String sql = "delete from TT_SALES_QUALITY_INFO_REPORT t where t.QUALITY_REPORT_ID in('"+ids+"') and t.VERIFY_STATUS in ('"+Constant.Quality_Verify_01+"','"+Constant.Quality_Verify_04+"')";
		this.delete(sql, null);
	}

	public PageResult<Map<String, Object>> queryQualityInfoReportVerify(
			String reportName, String ctmName, String dealerName, String verifyStatus,
			Integer pageSize, Integer curPage) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select *\r\n");
		sbSql.append("  from TT_SALES_QUALITY_INFO_REPORT t\r\n");
		sbSql.append(" where 1 = 1 \r\n");
		if(StringUtil.notNull(verifyStatus)){
			sbSql.append("   and t.verify_status = "+verifyStatus+"\r\n");
		}
		if(StringUtil.notNull(reportName)){
			sbSql.append("   and t.report_name like '%"+reportName+"%'\r\n");
		}
		if(StringUtil.notNull(ctmName)){
			sbSql.append("   and t.ctm_name like '%"+ctmName+"%'"); 
		}
		if(StringUtil.notNull(dealerName)){
			sbSql.append("   and t.dealer_name like '%"+dealerName+"%'"); 
		}
		
		
		sbSql.append("order by t.create_date desc");
		return (PageResult<Map<String, Object>>)this.pageQuery(sbSql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	/**
	 * 质量查询列表SQL
	 * @param request 
	 * @param dealerId
	 * @param i 
	 * @param curPage 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queFollowFind(RequestWrapper request, String dealerId, Integer curPage, int i) {
		PageResult<Map<String, Object>> ps=null;
		String vin = request.getParamValue("VIN");
		String carTieId = request.getParamValue("carTieId");// 车系
		String carTypeId = request.getParamValue("carTypeId");// 车型
		String makerCode = request.getParamValue("makerCode");// 部件厂代码
		String PART_CODE = request.getParamValue("PART_CODE");// 零件号
		String PART_NAME = request.getParamValue("PART_NAME");// 零件名称
		
		StringBuffer sb = getInfoData();

		if(StringUtil.notNull(vin)){
			sb.append(" and t.vin like '%"+vin+"%'");
		}
		if(StringUtil.notNull(carTieId) &&  !carTieId.equals("-1"))
		{
			sb.append(" and t.car_tie_id ="+carTieId);
		}
		if(StringUtil.notNull(carTypeId) && !carTypeId.equals("-1"))
		{
			sb.append(" and t.car_type_id="+carTypeId);
		}
		if(StringUtil.notNull(makerCode))
		{
			sb.append(" and t.maker_code like '%"+makerCode+"%' ");
		}
		if(StringUtil.notNull(PART_CODE))
		{
			sb.append(" and t.PART_CODE like '%"+PART_CODE+"%' ");
		}
		if(StringUtil.notNull(PART_NAME))
		{
			sb.append(" and t.PART_NAME like '%"+PART_NAME+"%' ");
		}
		sb.append(" order by t.create_date desc");
		ps=this.pageQuery(sb.toString(), null,getFunName(), i, curPage);
        return ps;
	}

	private StringBuffer getInfoData() {
		StringBuffer sb= new StringBuffer();
		sb.append("select t.id,t.car_tie_id as car_tie, \n" );
		sb.append("       (select a.group_name　from tm_vhcl_material_group a where a.group_id = t.car_tie_id) as car_tie_id,\n" );
		sb.append("       (select a.group_name　from tm_vhcl_material_group a where a.group_id = t.CAR_TYPE_ID) as CAR_TYPE_ID,\n" );
		sb.append("       t.vin,\n" );
		sb.append("       t.part_num,\n  to_char(t.ro_create_date, 'yyyy-mm-dd hh24:mi') ro_create_date,\n" );
		sb.append("       t.part_code,\n" );
		sb.append("       to_char(t.ro_repair_date_one, 'yyyy-mm-dd hh24:mi ') ro_repair_date_one,\n" );
		sb.append("       to_char(t.ro_repair_date_two, 'yyyy-mm-dd hh24:mi') ro_repair_date_two,\n" );
		sb.append("       t.maker_code,\n" );
		sb.append("       t.maker_name,\n" );
		sb.append("       t.mal_code,\n" );
		sb.append("       t.MAL_NAME,\n" );
		sb.append("       t.remark,\n" );
		sb.append("       t.PART_NAME,\n" );
		sb.append("       (select tc.name from tc_user tc where tc.user_id = t.create_user) as NAME,\n" );
		sb.append("       t.create_user,\n" );
		sb.append("       to_char(t.create_date, 'yyyy-mm-dd hh24:mi:ss') as create_date\n" );
		sb.append("  from tt_as_quelity_follow t\n" );
		sb.append(" where 1 = 1");

		return sb;
	}
	
	/**
	 * 根据id查询信息
	 * @param request 
	 * @param pageSize 
	 * @param currPage 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryById(RequestWrapper request, Integer currPage, Integer pageSize){
		String id = DaoFactory.getParam(request, "id");
		StringBuffer sb = this.getInfoData();
		DaoFactory.getsql(sb, "t.id", id, 1);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> selectmalfunction2(RequestWrapper request, String currDealerId, Integer currPage, int pageSize) {
		String malcode = CommonUtils.checkNull(request.getParamValue("malcode"));
        String malname = CommonUtils.checkNull(request.getParamValue("malname"));
        PageResult<Map<String, Object>> ps;
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT T.* FROM TT_PART_DEFINE T WHERE 1=1 ");
		/*if(!"".equals(malcode)){
			sb.append(" AND (T.PART_OLDCODE  LIKE '%"+malcode+"%'");
			sb.append(" or T.PART_CODE  LIKE '%"+malcode+"%')");
		}*/
		DaoFactory.getsql(sb, "T.PART_OLDCODE", malcode, 2);
		DaoFactory.getsql(sb, "T.PART_CNAME", malname, 2);
		ps = pageQuery(sb.toString(), null, getFunName(), pageSize,currPage);
		return ps;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> selectmalfunction1(RequestWrapper request, String currDealerId, Integer currPage, int pageSize) {
		String malcode = CommonUtils.checkNull(request.getParamValue("malcode"));
        String malname = CommonUtils.checkNull(request.getParamValue("malname"));
        PageResult<Map<String, Object>> ps;
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT T.* FROM TT_AS_WR_MALFUNCTION T WHERE 1=1 ");
		if(!"".equals(malcode)){
			sb.append(" AND T.MAL_CODE LIKE '%"+malcode+"%'");
		}
		if(!"".equals(malname)){
			sb.append(" AND T.MAL_NAME LIKE '%"+malname+"%'");
		}
		ps = pageQuery(sb.toString(), null, getFunName(), pageSize,currPage);
		return ps;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> selectmalfunction3(RequestWrapper request, String currDealerId, Integer currPage, int pageSize) {
		String makercode = CommonUtils.checkNull(request.getParamValue("makercode"));
		String makername = CommonUtils.checkNull(request.getParamValue("makername"));
        PageResult<Map<String, Object>> ps;
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT T.* FROM TT_PART_MAKER_DEFINE T WHERE 1=1 ");
		if(!"".equals(makercode)){
			sb.append(" AND T.MAKER_CODE LIKE '%"+makercode+"%'");
		}
		if(!"".equals(makername)){
			sb.append(" AND T.MAKER_NAME LIKE '%"+makername+"%'");
		}
		ps = pageQuery(sb.toString(), null, getFunName(), pageSize,currPage);
		return ps;
	}
	 /**
	  * 查询零件数量
	  * @param partcode
	  * @param time1
	  * @param time2
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	public int queryQuantity(String partcode ,String time1,String time2)
	 {
			StringBuffer sb= new StringBuffer();
			sb.append("select count(*) as PART_NUM\n" );
			sb.append("  from tt_as_repair_order o, Tt_As_Wr_Application a, Tt_As_Wr_Partsitem p\n" );
			sb.append(" where 1 = 1 and a.ro_no = o.ro_no\n" );
			sb.append("   and a.id = p.id and a.claim_type<>10661006  and p.pay_type=11801002 \n");
			DaoFactory.getsql(sb, "a.ro_startdate", time1, 3);
			DaoFactory.getsql(sb, "a.ro_startdate", time2, 4);
			DaoFactory.getsql(sb, "p.part_code", partcode, 1);
			List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, getFunName());
			if(list.size() > 0)
	        {
	        	return Integer.parseInt(list.get(0).get("PART_NUM").toString());
	        }
	        return 0;
	 }
	 /**
	  * 明细
	  * @param act
	 * @param currPage 
	 * @param pageSize 
	  */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> partNumDetail(ActionContext act, Integer pageSize, Integer currPage) {
		RequestWrapper request = act.getRequest();
		String id = DaoFactory.getParam(request, "id");
		TtAsQuelityFollowPO t=new TtAsQuelityFollowPO();
		t.setId(BaseUtils.ConvertLong(id));
		t=(TtAsQuelityFollowPO) dao.select(t).get(0);
		StringBuffer sb= new StringBuffer();
		sb.append("select a.*,p.part_code,p.part_name\n" );
		sb.append("  from  Tt_As_Wr_Application a, Tt_As_Wr_Partsitem p ，tm_vehicle t \n" );
		sb.append(" where 1 = 1 and  p.id=a.id and t.vin=a.vin \n" );
		sb.append("    and a.claim_type<>10661006 and p.pay_type=11801002 \n");
		DaoFactory.getsql(sb, "p.down_part_code",BaseUtils.checkNull(t.getPartCode()), 1);
		if(t.getRoRepairDateOne()!=null){
			DaoFactory.getsql(sb, "a.ro_startdate",BaseUtils.checkNull(BaseUtils.printDateTime(t.getRoRepairDateOne())), 3);
		}
		if(t.getRoRepairDateTwo()!=null){
			DaoFactory.getsql(sb, "a.ro_startdate",BaseUtils.checkNull(BaseUtils.printDateTime(t.getRoRepairDateTwo())), 4);
		}
		DaoFactory.getsql(sb, "t.series_id",BaseUtils.checkNull(t.getCarTieId()==-1L?"":t.getCarTieId()), 1);
		DaoFactory.getsql(sb, "t.model_id",BaseUtils.checkNull(t.getCarTypeId()==-1L?"":t.getCarTypeId()), 1);
		if(t.getRoCreateDate()!=null){
			DaoFactory.getsql(sb, "t.manufacturedate",BaseUtils.checkNull(BaseUtils.printDateTime(t.getRoCreateDate())), 31);
			DaoFactory.getsql(sb, "t.manufacturedate",BaseUtils.checkNull(BaseUtils.printDateTime(new Date())), 41);
		}
		DaoFactory.getsql(sb, "p.producer_code",BaseUtils.checkNull(t.getMakerCode()), 1);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	/**
	 * 质改跟踪全动态的实现
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public int showActive(RequestWrapper request) {
		int res=0;
		try {
			String ids = DaoFactory.getParam(request, "ids");
			String[] split = ids.split(",");
			for (int i = 0; i <split.length; i++) {
				String id = split[i];
				TtAsQuelityFollowPO t=new TtAsQuelityFollowPO();
				t.setId(BaseUtils.ConvertLong(id));
				t=(TtAsQuelityFollowPO) dao.select(t).get(0);
				StringBuffer sb= new StringBuffer();
				sb.append("select nvl(count(1),0) as num \n" );
				sb.append("  from Tt_As_Wr_Partsitem p, Tt_As_Wr_Application a，tm_vehicle t \n" );
				sb.append(" where a.id = p.id(+)  and a.vin=t.vin  and a.claim_type<>10661006  and p.pay_type=11801002  ");
				DaoFactory.getsql(sb, "p.down_part_code",BaseUtils.checkNull(t.getPartCode()), 1);
				if(t.getRoRepairDateOne()!=null){
					DaoFactory.getsql(sb, "a.ro_startdate",BaseUtils.checkNull(BaseUtils.printDateTime(t.getRoRepairDateOne())), 3);
				}
				if(t.getRoRepairDateTwo()!=null){
					DaoFactory.getsql(sb, "a.ro_startdate",BaseUtils.checkNull(BaseUtils.printDateTime(t.getRoRepairDateTwo())), 4);
				}
				DaoFactory.getsql(sb, "t.series_id",BaseUtils.checkNull(t.getCarTieId()==-1L?"":t.getCarTieId()), 1);
				DaoFactory.getsql(sb, "t.model_id",BaseUtils.checkNull(t.getCarTypeId()==-1L?"":t.getCarTypeId()), 1);
				if(t.getRoCreateDate()!=null){
					DaoFactory.getsql(sb, "t.manufacturedate",BaseUtils.checkNull(BaseUtils.printDateTime(t.getRoCreateDate())), 31);
					DaoFactory.getsql(sb, "t.manufacturedate",BaseUtils.checkNull(BaseUtils.printDateTime(new Date())), 41);
				}
				DaoFactory.getsql(sb, "p.producer_code",BaseUtils.checkNull(t.getMakerCode()), 1);
				List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, getFunName());
				int num =0;
				System.out.println(sb.toString());
				if(list.size() ==1){
					 num = Integer.parseInt(list.get(0).get("NUM").toString());
		        }
				String sql="update tt_as_quelity_follow t set t.part_num="+num+" where t.id="+id;
				dao.update(sql, null);
			}
		} catch (NumberFormatException e) {
			res=-1;
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * 查询详细数据
	 * @param request
	 * @param integer 
	 * @param pageSize 
	 * @return
	 */
	public PageResult<Map<String, Object>> selectTtAsQuelityFollow(RequestWrapper request, Integer pageSize, Integer currPage) {
		String id = DaoFactory.getParam(request, "id");
		StringBuffer sb = this.getInfoData();
		DaoFactory.getsql(sb, "t.id", id, 1);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	public void exportToexcelTarking(RequestWrapper request,AclUserBean loginUser, ActionContext act, Integer pageSizeMax, Integer currPage) {
		PageResult<Map<String, Object>> list=this.queFollowFind(request,loginUser.getDealerId(),currPage, pageSizeMax);
		 try {
			    String[] head={"车系","车型","底盘号","生产日期","跟踪天数","零件号","零件名称","故障零件数","故障零件数排序","起始修理日期","终止修理日期","部件厂代码","故障类别代码","故障现象","备注","创建人","创建时间"};
				List<Map<String, Object>> records = list.getRecords();
				List params=new ArrayList();
				if(records!=null &&records.size()>0){
					for (Map<String, Object> map : records) {
						String[] detail=new String[17];
						detail[0]=BaseUtils.checkNull(map.get("CAR_TIE_ID"));
						detail[1]=BaseUtils.checkNull(map.get("CAR_TYPE_ID"));
						detail[2]=BaseUtils.checkNull(map.get("VIN"));
						detail[3]=BaseUtils.checkNull(map.get("RO_CREATE_DATE"));
						int i=0;
						if (BaseUtils.notNull(detail[3])) {//跟踪天数
							String str = map.get("RO_CREATE_DATE").toString();
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
							Date RO_CREATE_DATE =   format.parse(str);
							Date date2 = new Date();
							Long timeOne = RO_CREATE_DATE.getTime();
							Long timeTwo = date2.getTime();
							i = (int) ((timeTwo-timeOne)/86400000);
						}else if (!BaseUtils.notNull(detail[3])) {
							i=0;
						}
						detail[4]=i+"";
						detail[5]=BaseUtils.checkNull(map.get("PART_CODE"));
						detail[6]=BaseUtils.checkNull(map.get("PART_NAME"));
						detail[7]=BaseUtils.checkNull(map.get("PART_NUM"));
						detail[8]=BaseUtils.checkNull(map.get("PART_NUM"));
						detail[9]=BaseUtils.checkNull(map.get("RO_REPAIR_DATE_ONE"));
						detail[10]=BaseUtils.checkNull(map.get("RO_REPAIR_DATE_TWO"));
						detail[11]=BaseUtils.checkNull(map.get("MAKER_CODE"));
						detail[12]=BaseUtils.checkNull(map.get("MAL_CODE"));
						detail[13]=BaseUtils.checkNull(map.get("MAL_NAME"));
						detail[14]=BaseUtils.checkNull(map.get("REMARK"));
						detail[15]=BaseUtils.checkNull(map.get("NAME"));
						detail[16]=BaseUtils.checkNull(map.get("CREATE_DATE"));
						params.add(detail);
					}
				}
				    String systemDateStr = BaseUtils.getSystemDateStr();
					BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "质改跟踪维护数据导出"+systemDateStr+".xls", "导出数据",null);
				} catch (Exception e) {
					e.printStackTrace();
				}
	}

	@SuppressWarnings("unchecked")
	public int checkpartcode(RequestWrapper request, AclUserBean loginUser) {
		int res = 0;
		StringBuffer str = new StringBuffer();
		str.append("select * from tt_as_quelity_follow f where 1=1 ");
		DaoFactory.getsql(str, "f.part_code", DaoFactory.getParam(request,"partcode"), 1) ;
		List list= this.pageQuery(str.toString(), null, getFunName());
		if (null!=list && list.size()>0) {
			res=1;
		}
		return res;
	}


}

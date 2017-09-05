package com.infodms.dms.dao.claim.oldPart;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsPartBorrowPO;
import com.infodms.dms.po.TtAsPartBorrowSubclassPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


@SuppressWarnings({ "rawtypes", "unchecked" })
public class EmergencyDeviceDao extends BaseDao{

	
private static final EmergencyDeviceDao dao = new EmergencyDeviceDao();
	
	public static EmergencyDeviceDao getInstance(){
		if (dao == null) {
			return new EmergencyDeviceDao();
		}
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public PageResult<Map<String, Object>> showAppListData(RequestWrapper request, Integer pageSize,Integer currPage) {
		String claim_no = DaoFactory.getParam(request, "claim_no");
		String part_code = DaoFactory.getParam(request, "part_code");
		String part_name = DaoFactory.getParam(request, "part_name");
		String beginTime = DaoFactory.getParam(request, "beginTime");
		String endTime = DaoFactory.getParam(request, "endTime");
		String dealer_id = DaoFactory.getParam(request, "dealer_id");
		StringBuffer sb= new StringBuffer();//只选择索赔单上只有一个主因件的
		sb.append("select * from (");
		sb.append("select p.part_name, p.part_code,p.part_id, p.OLD_PART_CODE, a.vin,p.trouble_reason as TROUBLE_DESC,a.CLAIM_NO,a.id, " );
		sb.append("(select count(1) from tt_as_wr_partsitem p\n" );
		sb.append("         where p.id = a.id and p.responsibility_type = 94001001) as res_type_count");
		sb.append("  from tt_as_wr_partsitem p, Tt_As_Wr_Application a " );
		sb.append("  where p.id = a.id(+) and a.urgent =0   and p.is_return = 95361001 and p.part_use_type=1  and a.IS_IMPORT=10041002 and a.STATUS=10791008 and a.is_invoice<>1 ");
		sb.append("  and p.part_code  in ( select B.PART_CODE from TM_PT_PART_BASE B\n" );
		sb.append("  where B.IS_DEL=0\n" );
		sb.append("    and B.part_is_changhe =95411001\n" );
		sb.append("    and b.part_code not in ('CV6000-00000','CV8000-00000','00-0000','00-000','CV11000-00000')\n" );
		sb.append("    and B.IS_RETURN=95361001)\n" );
		sb.append(" and a.claim_no not in (SELECT d.claim_no FROM Tt_As_Wr_Old_Returned r, Tt_As_Wr_Old_Returned_Detail d WHERE r.id = d.return_id ");
		if (!"".equals(dealer_id)){
			sb.append("	and r.dealer_id="+dealer_id);
		}
		sb.append("	) ");
		DaoFactory.getsql(sb, "a.dealer_id", dealer_id, 1);
		DaoFactory.getsql(sb, "a.claim_no", claim_no, 2);
		DaoFactory.getsql(sb, "p.part_code", part_code, 2);
		DaoFactory.getsql(sb, "p.part_name", part_name, 2);
		DaoFactory.getsql(sb, "a.report_date", beginTime, 3);
		DaoFactory.getsql(sb, "a.report_date", endTime, 4);
		sb.append("	) c where c.res_type_count=1 ");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	
	public int saveOrUpdare(RequestWrapper request, AclUserBean loginUser) {
		int res=1;
		try {
			String type = DaoFactory.getParam(request, "type");
			String id = DaoFactory.getParam(request, "id");
			String productAddr = DaoFactory.getParam(request, "productAddr");
			String applyDept = DaoFactory.getParam(request, "applyDept");
			String borrowPerson = DaoFactory.getParam(request, "borrowPerson");
			String borrowNo = DaoFactory.getParam(request, "borrowNo");
			String borrowDept = DaoFactory.getParam(request, "borrowDept");
			String consigneePerson = DaoFactory.getParam(request, "consigneePerson");
			String consigneePhone = DaoFactory.getParam(request, "consigneePhone");
			String consigneeAddr = DaoFactory.getParam(request, "consigneeAddr");
			String consigneeEmail = DaoFactory.getParam(request, "consigneeEmail");
			String dealerCode = DaoFactory.getParam(request, "dealerCode");
			String dealerId = DaoFactory.getParam(request, "dealerId");
			String borrowReason = DaoFactory.getParam(request, "borrowReason");
			String borrowPhone = DaoFactory.getParam(request, "borrowPhone");
			String requireDate = DaoFactory.getParam(request, "requireDate");
			
			String [] vin = DaoFactory.getParams(request, "vin");
			String [] partCode = DaoFactory.getParams(request, "part_code");
			String [] partName = DaoFactory.getParams(request, "part_name");
			String [] problemReason = DaoFactory.getParams(request, "problem_reason");
			String [] part_id = DaoFactory.getParams(request, "part_id");
			String [] claim_no = DaoFactory.getParams(request, "claim_no");
			//String[] fjids=request.getParamValues("fjid");
			String [] claim_id = DaoFactory.getParams(request, "claim_id");
			String chooseType = DaoFactory.getParam(request, "chooseType");
			Date createDate = new Date();
			if(chooseType.equals("2")){ //需要分单
				List<Map<String,Object>> listDealerids = getSingleBydealerId(claim_id);
				for (Map<String, Object> map : listDealerids) {
					String  dealer_id = map.get("DEALER_ID").toString();
					String  dealer_code = map.get("DEALER_CODE").toString();
					Long pkId = DaoFactory.getPkId();
					TtAsPartBorrowPO t=new TtAsPartBorrowPO();
					t.setId(pkId);
					t.setProductAddr(productAddr);
					t.setApplyDept(applyDept);
					t.setBorrowPerson(borrowPerson);
					t.setBorrowNo(borrowNo);
					t.setBorrowDept(borrowDept);
					t.setBorrowReason(borrowReason);
					t.setConsigneeAddr(consigneeAddr);
					t.setConsigneeEmail(consigneeEmail);
					t.setConsigneePerson(consigneePerson);
					t.setConsigneePhone(consigneePhone);
					t.setConsigneePerson(consigneePerson);
					t.setCreateBy(loginUser.getUserId());
					t.setCreateDate(createDate);
					t.setDealerId(BaseUtils.ConvertLong(dealer_id));
					t.setDealerCode(dealer_code);
					t.setIsDelete(0);//是分单的标识
					t.setStatus(BaseUtils.ConvertLong(type));
					t.setIsReturn(0);
					t.setBorrowPhone(borrowPhone);
					t.setRequireDate(new SimpleDateFormat("yyyy-MM-dd").parse(requireDate));
					if(type.equals(String.valueOf(Constant.OLD_PART_BORROW_02))){
						t.setNextTime(new Date());
					}
					dao.insert(t);					
					List<Map<String,Object>> listClaimids=getClaimBydealerId(dealer_id,claim_id);//根据经销商id去查找出数据
					for (Map<String, Object> mapData : listClaimids) {
						TtAsPartBorrowSubclassPO p=new TtAsPartBorrowSubclassPO();
						p.setId(DaoFactory.getPkId());
						p.setClaimId(BaseUtils.ConvertLong(mapData.get("ID").toString()));
						p.setClaimNo(mapData.get("CLAIM_NO").toString());
						p.setCreateDate(createDate);
						p.setParentId(pkId);
						p.setPartCode(mapData.get("PART_CODE").toString());
						p.setPartName(mapData.get("PART_NAME").toString());
						p.setProblemReason(BaseUtils.checkNull(mapData.get("TROUBLE_DESC")));
						p.setVin(mapData.get("VIN").toString());
						p.setPartId(BaseUtils.ConvertLong(mapData.get("PART_ID").toString()));
						dao.insert(p);
						TtAsWrApplicationPO app1=new TtAsWrApplicationPO();
						app1.setClaimNo(mapData.get("CLAIM_NO").toString());
						TtAsWrApplicationPO app2=new TtAsWrApplicationPO();
						app2.setUrgent(1);//加紧急调件标识
						dao.update(app1, app2);
					}
				}
			}
			
			
			if(chooseType.equals("1")) {
				//新增
			if("".equals(id)){
				Long pkId = DaoFactory.getPkId();
				TtAsPartBorrowPO t=new TtAsPartBorrowPO();
				t.setId(pkId);
				t.setProductAddr(productAddr);
				t.setApplyDept(applyDept);
				t.setBorrowPerson(borrowPerson);
				t.setBorrowNo(borrowNo);
				t.setBorrowDept(borrowDept);
				t.setBorrowReason(borrowReason);
				t.setConsigneeAddr(consigneeAddr);
				t.setConsigneeEmail(consigneeEmail);
				t.setConsigneePerson(consigneePerson);
				t.setConsigneePhone(consigneePhone);
				t.setConsigneePerson(consigneePerson);
				t.setCreateBy(loginUser.getUserId());
				t.setCreateDate(createDate);
				t.setDealerId(BaseUtils.ConvertLong(dealerId));
				t.setDealerCode(dealerCode);
				t.setIsDelete(1);
				t.setIsReturn(0);
				t.setStatus(BaseUtils.ConvertLong(type));
				t.setBorrowPhone(borrowPhone);
				t.setRequireDate(new SimpleDateFormat("yyyy-MM-dd").parse(requireDate) );
				if(type.equals(String.valueOf(Constant.OLD_PART_BORROW_02))){
					t.setNextTime(new Date());
				}
				dao.insert(t);
				int temp=0;
				for (String claimNo : claim_no) {
					TtAsPartBorrowSubclassPO p=new TtAsPartBorrowSubclassPO();
					p.setId(DaoFactory.getPkId());
					p.setClaimId(BaseUtils.ConvertLong(claim_id[temp]));
					p.setClaimNo(claimNo);
					p.setCreateDate(createDate);
					p.setParentId(t.getId());
					p.setPartCode(partCode[temp]);
					p.setPartName(partName[temp]);
					p.setProblemReason(problemReason[temp]);
					p.setVin(vin[temp]);
					p.setPartId(BaseUtils.ConvertLong(part_id[temp]));
					dao.insert(p);
					TtAsWrApplicationPO app1=new TtAsWrApplicationPO();
					app1.setClaimNo(claimNo);
					TtAsWrApplicationPO app2=new TtAsWrApplicationPO();
					app2.setUrgent(1);//加紧急调件标识
					dao.update(app1, app2);
					temp++;
				}
			}else{//修改
				TtAsPartBorrowPO t1=new TtAsPartBorrowPO();
				Long idUpdate = BaseUtils.ConvertLong(id);
				t1.setId(idUpdate);
				List<TtAsPartBorrowPO> list = dao.select(t1);
				TtAsPartBorrowPO t=new TtAsPartBorrowPO();
				t = list.get(0);
				t.setProductAddr(productAddr);
				t.setApplyDept(applyDept);
				t.setBorrowPerson(borrowPerson);
				t.setBorrowNo(borrowNo);
				t.setBorrowDept(borrowDept);
				t.setBorrowReason(borrowReason);
				t.setConsigneeAddr(consigneeAddr);
				t.setConsigneeEmail(consigneeEmail);
				t.setConsigneePerson(consigneePerson);
				t.setConsigneePhone(consigneePhone);
				t.setConsigneePerson(consigneePerson);
				t.setCreateBy(loginUser.getUserId());
				t.setCreateDate(createDate);
				t.setDealerId(BaseUtils.ConvertLong(dealerId));
				t.setDealerCode(dealerCode);
				t.setIsDelete(0);
				t.setStatus(BaseUtils.ConvertLong(type));
				t.setBorrowPhone(borrowPhone);
				if(type.equals(String.valueOf(Constant.OLD_PART_BORROW_02))){
					t.setNextTime(new Date());
				}
				dao.update(t1, t);
				//先查询将索赔单的标识还原再 删除
				TtAsPartBorrowSubclassPO p1=new TtAsPartBorrowSubclassPO();
				p1.setParentId(t.getId());
				List<TtAsPartBorrowSubclassPO> select = dao.select(p1);
				if(select!=null && select.size()>0){
					for (TtAsPartBorrowSubclassPO tt : select) {
						TtAsWrApplicationPO app1=new TtAsWrApplicationPO();
						app1.setClaimNo(tt.getClaimNo());
						TtAsWrApplicationPO app2=new TtAsWrApplicationPO();
						app2.setUrgent(0);//还原紧急调件标识
						dao.update(app1, app2);
					}
				}
				dao.delete(p1);
				//再添加
				int temp=0;
				for (String claimNo : claim_no) {
					TtAsPartBorrowSubclassPO p=new TtAsPartBorrowSubclassPO();
					p.setId(DaoFactory.getPkId());
					p.setClaimId(BaseUtils.ConvertLong(claim_id[temp]));
					p.setClaimNo(claimNo);
					p.setCreateDate(createDate);
					p.setParentId(t.getId());
					p.setPartCode(partCode[temp]);
					p.setPartName(partName[temp]);
					p.setProblemReason(problemReason[temp]);
					p.setVin(vin[temp]);
					p.setPartId(BaseUtils.ConvertLong(part_id[temp]));
					dao.insert(p);
					TtAsWrApplicationPO app1=new TtAsWrApplicationPO();
					app1.setClaimNo(claimNo);
					TtAsWrApplicationPO app2=new TtAsWrApplicationPO();
					app2.setUrgent(1);//加紧急调件标识
					dao.update(app1, app2);
					temp++;
				}
				
			}
				//FileUploadManager.fileUploadByBusiness(String.valueOf(ywzj), fjids, loginUser);
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * 查找出数据
	 * @param dealer_id
	 * @param claim_id
	 * @return
	 */
	private List<Map<String, Object>> getClaimBydealerId(String dealer_id,String[] claim_id) {
		StringBuffer sb= new StringBuffer();
		sb.append("select a.CLAIM_NO ,a.id ,a.VIN,p.PART_CODE,p.PART_NAME,p.trouble_reason as TROUBLE_DESC,p.PART_ID\n" );
		sb.append("  from Tt_As_Wr_Application a, Tt_As_Wr_Partsitem p\n" );
		sb.append(" where p.id = a.id(+)\n" );
		sb.append("   and a.urgent = 0 and p.part_use_type = 1\n" );//zyw2015-3-16 配件为更换的
		sb.append("   and a.IS_IMPORT = 10041002\n" );
		sb.append("   and a.STATUS = 10791008\n" );
		sb.append("   and a.is_invoice <> 1\n" );
		sb.append("  and p.part_code  in ( select B.PART_CODE from TM_PT_PART_BASE B\n" );
		sb.append("  where B.IS_DEL=0\n" );
		sb.append("    and B.part_is_changhe =95411001\n" );
		sb.append("    and b.part_code not in ('CV6000-00000','CV8000-00000','00-0000','00-000','CV11000-00000')\n" );
		sb.append("    and B.IS_RETURN=95361001)\n ");
		sb.append("   and a.claim_no not in\n" );
		sb.append("       (SELECT d.claim_no\n" );
		sb.append("          FROM Tt_As_Wr_Old_Returned r, Tt_As_Wr_Old_Returned_Detail d\n" );
		sb.append("         WHERE r.id = d.return_id)");
		String idarrs = DaoFactory.getSqlByarrIn(claim_id);
		DaoFactory.getsql(sb, "a.id", idarrs, 6);
		DaoFactory.getsql(sb, "a.dealer_id", dealer_id, 1);
		List<Map<String,Object>> list = dao.pageQuery(sb.toString(), null, getFunName());
		return list;
	}

	/***
	 * 分单操作
	 * @param claim_id
	 * @return
	 */
	private List<Map<String,Object>> getSingleBydealerId(String[] claim_id) {
		StringBuffer sb= new StringBuffer();
		sb.append("select a.dealer_id,(select tm.dealer_code from tm_dealer tm where tm.dealer_id=a.dealer_id) as dealer_code\n" );
		sb.append("  from Tt_As_Wr_Application a, Tt_As_Wr_Partsitem p\n" );
		sb.append(" where p.id = a.id(+)\n" );
		sb.append("   and a.urgent = 0 and p.part_use_type = 1\n" );//zyw2015-3-16 配件为更换的
		sb.append("   and a.IS_IMPORT = 10041002\n" );
		sb.append("   and a.STATUS = 10791008\n" );
		sb.append("   and a.is_invoice <> 1\n" );
		sb.append("  and p.part_code  in ( select B.PART_CODE from TM_PT_PART_BASE B\n" );
		sb.append("  where B.IS_DEL=0\n" );
		sb.append("    and B.part_is_changhe =95411001\n" );
		sb.append("    and b.part_code not in ('CV6000-00000','CV8000-00000','00-0000','00-000','CV11000-00000')\n" );
		sb.append("    and B.IS_RETURN=95361001) \n ");
		String idarrs = DaoFactory.getSqlByarrIn(claim_id);
		sb.append("   and a.id in ("+idarrs+")\n" );
		sb.append("   and a.claim_no not in\n" );
		sb.append("       (SELECT d.claim_no\n" );
		sb.append("          FROM Tt_As_Wr_Old_Returned r, Tt_As_Wr_Old_Returned_Detail d\n" );
		sb.append("         WHERE r.id = d.return_id)\n" );
		sb.append(" group by a.dealer_id");
		List<Map<String,Object>> list = dao.pageQuery(sb.toString(), null, getFunName());
		return list;
	}

	public PageResult<Map<String, Object>> listShowData(RequestWrapper request,Integer pageSize, Integer currPage) {
		String choose = DaoFactory.getParam(request, "action");
		String dealer_id = DaoFactory.getParam(request, "dealer_id");
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from TT_AS_PART_BORROW t where 1=1 and t.is_return =0 ");
		if(!"".equals(choose)){
			DaoFactory.getsql(sb, "t.status", "18041002", 1);
		}
		DaoFactory.getsql(sb, "t.dealer_id", dealer_id, 1);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	public List<FsFileuploadPO> queryAttById(String id) {
			StringBuffer sql = new StringBuffer();
			List<FsFileuploadPO> ls = new ArrayList<FsFileuploadPO>();
			sql.append(" SELECT A.* FROM FS_FILEUPLOAD A WHERE 1=1");
			sql.append(" AND A.YWZJ='" + id + "'");
			ls = select(FsFileuploadPO.class, sql.toString(), null);
			return ls;
	}

	public PageResult<Map<String, Object>> listShowDataView(RequestWrapper request, Integer pageSize, Integer currPage) {
		String dealer_id = DaoFactory.getParam(request, "dealerId");
		String borrowPerson = DaoFactory.getParam(request, "borrowPerson");
		String borrowDept = DaoFactory.getParam(request, "borrowDept");
		String consigneePerson = DaoFactory.getParam(request, "consigneePerson");
		String beginTime = DaoFactory.getParam(request, "beginTime");
		String endTime = DaoFactory.getParam(request, "endTime");
		String status = DaoFactory.getParam(request, "status");
		String is_return = DaoFactory.getParam(request, "is_return");
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT T.*,(SELECT TM.DEALER_SHORTNAME FROM TM_DEALER TM WHERE TM.DEALER_ID=T.DEALER_ID) AS DEALER_SHORTNAME FROM TT_AS_PART_BORROW T WHERE 1=1 ");
		DaoFactory.getsql(sb, "T.DEALER_ID", dealer_id, 6);
		DaoFactory.getsql(sb, "T.BORROW_PERSON", borrowPerson, 2);
		DaoFactory.getsql(sb, "T.BORROW_DEPT", borrowDept, 2);
		DaoFactory.getsql(sb, "T.STATUS", status, 1);
		DaoFactory.getsql(sb, "T.CONSIGNEE_PERSON", consigneePerson, 2);
		DaoFactory.getsql(sb, "T.NEXT_TIME", beginTime, 3);
		DaoFactory.getsql(sb, "T.NEXT_TIME", endTime, 4);
		DaoFactory.getsql(sb, "T.IS_RETURN", is_return,1);
		String claim_no = DaoFactory.getParam(request, "claim_no");
		if(BaseUtils.testString(claim_no)){
			sb.append(" and t.id in (select d.parent_id from \n" );
			sb.append("               TT_AS_PART_BORROW_SUBCLASS d where 1=1 \n" );
			DaoFactory.getsql(sb, "d.claim_no", claim_no, 2);
			sb.append("   ) \n");
		}
		String borrow_man = DaoFactory.getParam(request, "borrow_man");
		/**
		 * 增加借出人查询条件 2015-2-3
		 */
		if(BaseUtils.testString(borrow_man)){
			sb.append(" and t.id in (select d.parent_id from Tt_As_Part_Borrow_Store s,\n" );
			sb.append("               TT_AS_PART_BORROW_SUBCLASS d where\n" );
			sb.append("               s.id = d.claim_id \n");
			DaoFactory.getsql(sb, "s.borrow_man", borrow_man, 2);
			sb.append("   ) \n");
		}
		sb.append(" ORDER BY T.CREATE_DATE DESC ");
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	/**
	 * 查询所有调出调入记录
	 * @param request
	 * @param currPage 
	 * @param pageSize 
	 * @return
	 */
	public PageResult<Map<String, Object>> listShowAllData(RequestWrapper request, Integer pageSize, Integer currPage) {
		String partCode = request.getParamValue("partCode");
		String partName =request.getParamValue("partName");
		String dealerCode = request.getParamValue("dealerCode");
		String dealerName =request.getParamValue("dealerName");
		String supplyCode = request.getParamValue("supplyCode");
		String supplyName =request.getParamValue("supplyName");
		String VIN = request.getParamValue("VIN");
		String claimNo =request.getParamValue("claimNo");
		
		String start_date = request.getParamValue("start_date");
		String end_date =request.getParamValue("end_date");
		String back_start_date = request.getParamValue("back_start_date");
		String back_end_date =request.getParamValue("back_end_date");
		
		
		
		
		StringBuffer sb= new StringBuffer();
		sb.append("select t.* from Tt_As_Part_Borrow_Store t where t.status=2");
		
		
		
		if(partCode!=null){
			sb.append(" and t.part_code like '%"+partCode+"%' ");
		}
		if(partName!=null){
			sb.append(" and t.part_name like '%"+partName+"%' ");
		}
		if(dealerCode!=null){
			sb.append(" and t.dealer_code like '%"+dealerCode+"%' ");
		}
		if(dealerName!=null){
			sb.append(" and t.dealer_name like '%"+dealerName+"%' ");
		}
		if(supplyCode!=null){
			sb.append(" and t.supply_code like '%"+supplyCode+"%' ");
		}
		if(supplyName!=null){
			sb.append(" and t.supply_name like '%"+supplyName+"%' ");
		}
		if(VIN!=null){
			sb.append(" and t.vin like '%"+VIN+"%' ");
		}
		if(claimNo!=null){
			sb.append(" and t.claim_no like '%"+claimNo+"%' ");
		}
		
		if(start_date!=null){
			sb.append(" and t.create_date>= to_date('"+start_date+"','yyyy-MM-dd')");
		}
		if(end_date!=null){
			sb.append("  and to_date(to_char(t.create_date,'yyyy-MM-dd'),'yyyy-MM-dd')<= to_date('"+end_date+"','yyyy-MM-dd')");
		}
		
		if(back_start_date!=null){
			sb.append(" and t.next_date>= to_date('"+back_start_date+"','yyyy-MM-dd')");
		}
		if(back_end_date!=null){
			sb.append(" and to_date(to_char(t.next_date,'yyyy-MM-dd'),'yyyy-MM-dd') <= to_date('"+back_end_date+"','yyyy-MM-dd')");
		}
		
		
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	public PageResult<Map<String, Object>> listMeterialGroup(
			RequestWrapper request, Integer pageSize, Integer currPage) {
		String sql="select * from tm_vhcl_material_group where group_level=3";
		PageResult<Map<String, Object>> list=pageQuery(sql, null,getFunName(), pageSize, currPage);
		return list;
	}
}

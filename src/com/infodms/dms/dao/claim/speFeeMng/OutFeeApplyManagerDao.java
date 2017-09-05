package com.infodms.dms.dao.claim.speFeeMng;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import com.infodms.dms.bean.CruiServiceBasicHeaderInfoBean;
import com.infodms.dms.bean.QueryCruiInfoBean;
import com.infodms.dms.bean.SpeFeeDetailInfoBean;
import com.infodms.dms.bean.SpeFeeVehicleListInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmVehicleExtPO;
import com.infodms.dms.po.TtAsWrSpeoutfeePO;
import com.infodms.dms.po.TtAsWrSpeoutfeeVehiclePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class OutFeeApplyManagerDao extends BaseDao{
	
    public static Logger logger = Logger.getLogger(OutFeeApplyManagerDao.class);
	
	private static final OutFeeApplyManagerDao dao = null;
	
	public static final OutFeeApplyManagerDao getInstance() {
	   if(dao==null) return new OutFeeApplyManagerDao();
	   return dao;
	}
	
	public PageResult<QueryCruiInfoBean> queryApprovedCruiInfo(Map params,int curPage, int pageSize){
		String dealer_id=CommonUtils.checkNull(params.get("dealer_id"));
		String company_id=CommonUtils.checkNull(params.get("company_id"));
		String xh_order_no=CommonUtils.checkNull(params.get("xh_order_no"));
		String report_start_date=CommonUtils.checkNull(params.get("report_start_date"));
		String report_end_date=CommonUtils.checkNull(params.get("report_end_date"));
		String approve_start_date=CommonUtils.checkNull(params.get("approve_start_date"));
		String approve_end_date=CommonUtils.checkNull(params.get("approve_end_date"));
		String xh_aim_area=CommonUtils.checkNull(params.get("xh_aim_area"));

		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select tawc.id,tawc.cr_no,taws.status,tc.code_desc status_desc,\n" );
		sqlStr.append("to_char(tawc.make_date,'YYYY-MM-DD') make_date,tawc.cr_whither,\n" );
		sqlStr.append("tawc.cr_mileage,tawc.cr_day,tawc.cr_principal,to_char(tawc.audit_date,'YYYY-MM-DD') audit_date,taws.status outfee_status\n" );
		sqlStr.append("from TT_AS_WR_CRUISE tawc,tc_code tc,Tt_As_Wr_Speoutfee taws\n" );
		sqlStr.append("where tawc.status=tc.code_id\n" );
		if(!"".equals(xh_order_no)){
			sqlStr.append("and tawc.cr_no like'%"+xh_order_no+"%'\n" );
		}
		if(!"".equals(report_start_date)){
			sqlStr.append(" and tawc.make_date>=to_date('" + report_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(!"".equals(report_end_date)){
			sqlStr.append(" and tawc.make_date<=to_date('" + report_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(!"".equals(approve_start_date)){
			sqlStr.append(" and tawc.audit_date>=to_date('" + approve_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(!"".equals(approve_end_date)){
			sqlStr.append(" and tawc.audit_date<=to_date('" + approve_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(!"".equals(xh_aim_area)){
			sqlStr.append("and tawc.cr_whither like'%"+xh_aim_area+"%'\n" );
		}
		sqlStr.append("and tawc.id=taws.cr_id(+)\n" );
		sqlStr.append("and (taws.status is null or taws.status="+Constant.SPE_OUTFEE_STATUS_01+"\n" );
		sqlStr.append("or taws.status="+Constant.SPE_OUTFEE_STATUS_03+"\n" );
		sqlStr.append("or taws.status="+Constant.SPE_OUTFEE_STATUS_06+")\n" );
		sqlStr.append("and tawc.status="+Constant.CURI_SERVICE_STATUS_07+"\n" );
		sqlStr.append("and tawc.is_spefee=0\n" );
		sqlStr.append("and tawc.dealer_id="+dealer_id+"\n" );
		sqlStr.append("and tawc.company_id="+company_id+"\n");
		sqlStr.append("order by tawc.id desc\n");
		
		PageResult<QueryCruiInfoBean> pr=pageQuery(QueryCruiInfoBean.class,
				sqlStr.toString(),null, pageSize, curPage);
		return pr;
	}
	/**
	 * Function：获得车厂名称
	 * @param  ：	
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-14
	 */
	public String getProducerName(String name){
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select group_name from tm_vhcl_material_group where group_level=1 and group_code='"+name+"'");
		
		List rsList=pageQuery(sqlStr.toString(), null, getFunName());
		String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
		if(temp!=null&&!"".equals(temp)){
			return temp;
		}else{
			return "";
		}
	}
	/**
	 * Function：从代码表中获取相应代码解释
	 * @param  ：	
	 * @return:		@param codeId
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-15
	 */
	public String getCodeName(Integer codeId){
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select code_desc from tc_code where status=10011001 and code_id="+codeId+"\n");
		
		List rsList=pageQuery(sqlStr.toString(), null, getFunName());
		String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
		if(temp!=null&&!"".equals(temp)){
			return temp;
		}else{
			return "";
		}

	}
	/**
	 * Function：申请特殊费用获得巡航信息
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-14
	 */
	public CruiServiceBasicHeaderInfoBean getCruiBasicInfo(Map params){
		String ord_id=CommonUtils.checkNull(params.get("ord_id"));
		String dealer_id=CommonUtils.checkNull(params.get("dealer_id"));
		String company_id=CommonUtils.checkNull(params.get("company_id"));
		
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select tawc.id,tawc.cr_no,tu.name create_by,to_char(tawc.make_date,'YYYY-MM-DD') make_date,\n" );
		sqlStr.append("td.dealer_shortname dealer_name,tawc.cr_day,tawc.cr_whither\n" );
		sqlStr.append("from TT_AS_WR_CRUISE tawc,tc_user tu,tm_dealer td\n" );
		sqlStr.append("where tawc.create_by=tu.user_id(+)\n" );
		sqlStr.append("and tawc.dealer_id=td.dealer_id\n" );
		sqlStr.append("and tawc.id="+ord_id+"\n");
		sqlStr.append("and tawc.dealer_id="+dealer_id+"\n" );
		sqlStr.append("and tawc.company_id="+company_id+"\n");
		
		List<CruiServiceBasicHeaderInfoBean> list=select(CruiServiceBasicHeaderInfoBean.class, sqlStr.toString(),null);
		if(list!=null&&list.size()>0){
			return (CruiServiceBasicHeaderInfoBean)list.get(0);
		}else{
			return null;
		}
	}
	/**
	 * 
	* @Title: getVin 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public PageResult<TmVehicleExtPO> getVin(Map<String,String> map,int pageSize,int curPage) {
		
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT V.*,(select brand_name from VW_MATERIAL_GROUP vw where vw.package_id=v.package_id) as brand_name,a.CUSTOMER_NAME AS customer_name,a.CERT_NO AS cert_no,a.MOBILE AS mobile,a.ADDRESS_DESC AS address_desc,G1.GROUP_NAME AS series_name,G2.GROUP_NAME AS model_name FROM TM_VEHICLE V \n");
		//sql.append(" LEFT OUTER JOIN TM_VHCL_MATERIAL_GROUP G0 on V.");
		sql.append(" LEFT OUTER JOIN TM_VHCL_MATERIAL_GROUP G1 on G1.GROUP_ID=V.SERIES_ID \n");
		sql.append(" LEFT OUTER JOIN TM_VHCL_MATERIAL_GROUP G2 on G2.GROUP_ID=V.MODEL_ID \n");
		sql.append(" LEFT OUTER JOIN TT_ACTUAL_SALES A on A.VEHILCE_ID=V.VEHICLE_ID \n");
		sql.append(" WHERE 1=1 ");
		if (Utility.testString(map.get("vin"))) {
			sql.append(" and v.VIN LIKE '%" + map.get("vin") + "%' ");
		}
		if (Utility.testString(map.get("customer"))) {
			sql.append(" and a.CUSTOMER_NAME LIKE '%" + map.get("customer") + "%' ");
		}
		PageResult<TmVehicleExtPO> ps = pageQuery(TmVehicleExtPO.class,sql.toString(),null,pageSize,curPage);
		return ps;
	}
	/**
	 * Function：修改或保存特殊费用工单信息
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@param request
	 * @return:		@param reportStr 
	 * @throw：	
	 * LastUpdate：	2010-7-15 赵伦达
	 * @throws ParseException 
	 */
	public Map modifyAndSaveOutFeeOrdInfo(Map params,RequestWrapper request,Integer status) throws ParseException{
		String xh_ord_id=CommonUtils.checkNull(params.get("xh_ord_id"));
		String dealer_id=CommonUtils.checkNull(params.get("dealer_id"));
		String company_id=CommonUtils.checkNull(params.get("company_id"));
		String user_id=CommonUtils.checkNull(params.get("user_id"));
		String vin_str=CommonUtils.checkNull(params.get("vin_str"));
		Map retMap=new HashMap();
		String attachFileId="";//为添加附件功能提供返回特殊费用表主键
		//取出页面特殊费用工单信息
		String out_start_date=CommonUtils.checkNull(request.getParamValue("out_start_date"));
		String out_end_date=CommonUtils.checkNull(request.getParamValue("out_end_date"));
		String out_person_num=CommonUtils.checkNull(request.getParamValue("out_person_num"));
		String out_name=CommonUtils.checkNull(request.getParamValue("out_name"));
		String single_mileage=CommonUtils.checkNull(request.getParamValue("single_mileage"));
		String road_bridge_fee=CommonUtils.checkNull(request.getParamValue("road_bridge_fee"));
		String vehicle_extra_fee=CommonUtils.checkNull(request.getParamValue("vehicle_extra_fee"));
		String lodging_fee=CommonUtils.checkNull(request.getParamValue("lodging_fee"));
		String eat_extra_fee=CommonUtils.checkNull(request.getParamValue("eat_extra_fee"));
		String person_extra_fee=CommonUtils.checkNull(request.getParamValue("person_extra_fee"));
		String apply_content=CommonUtils.checkNull(request.getParamValue("apply_content"));
		
		String[] vinArr=vin_str.split(",");
		
		if("".equals(dealer_id)||"".equals(user_id)||"".equals(company_id)){
			retMap.put("retCode", "failure_001");
			retMap.put("fileId", attachFileId);
			return retMap;//无法获得登陆人的信息
		}
		//判断特殊费用数据是否已经保存
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select * from TT_AS_WR_SPEOUTFEE where cr_id="+xh_ord_id);
		List rsList=select(TtAsWrSpeoutfeePO.class, sqlStr.toString(), null);
		TtAsWrSpeoutfeePO sfeeInfo=new TtAsWrSpeoutfeePO();
		@SuppressWarnings("unused")
		int updateNum=0;
		String newSpeFeeId=SequenceManager.getSequence("");
		String newSpeFeeNo=SequenceManager.getSequence("TF");
		if(rsList!=null&&rsList.size()>0){//已存在，修改特殊费用
			sfeeInfo=(TtAsWrSpeoutfeePO)rsList.get(0);
			attachFileId=sfeeInfo.getId().toString();
				
			TtAsWrSpeoutfeePO updateObj=new TtAsWrSpeoutfeePO();
			updateObj.setId(sfeeInfo.getId());
			TtAsWrSpeoutfeePO vo =new TtAsWrSpeoutfeePO();
			vo.setStartDate(parseStringToDate(out_start_date));
			vo.setEndDate(parseStringToDate(out_end_date));
			vo.setPersonNum(Long.parseLong(out_person_num));
			vo.setPersonName(out_name);
			vo.setSingleMileage(Double.parseDouble(single_mileage));
			vo.setPassFee(Double.parseDouble(road_bridge_fee));
			vo.setTrafficFee(Double.parseDouble(vehicle_extra_fee));
			vo.setQuarterFee(Double.parseDouble(lodging_fee));
			vo.setEatFee(Double.parseDouble(eat_extra_fee));
			vo.setPersonSubside(Double.parseDouble(person_extra_fee));
			vo.setApplyContent(apply_content);
			vo.setStatus(status);
			vo.setUpdateBy(Long.parseLong(user_id));
			vo.setUpdateDate(new Date());
			updateNum=update(updateObj, vo);
			//插入或更新特殊费用车辆信息
			for(int count=0;count<vinArr.length;count++){
				if(isExistSpFeeVin(sfeeInfo.getId().toString(),vinArr[count])){//存在，更新所在vin特殊费用车辆信息
					TtAsWrSpeoutfeeVehiclePO upObj=new TtAsWrSpeoutfeeVehiclePO();
					upObj.setFeeId(sfeeInfo.getId());
					upObj.setVin(vinArr[count]);
					
					TtAsWrSpeoutfeeVehiclePO updateVo=new TtAsWrSpeoutfeeVehiclePO();
					String mileage=CommonUtils.checkNull(request.getParamValue("vin_mileage"+vinArr[count]));
					updateVo.setMileage("".equals(mileage)?0:Double.parseDouble(mileage));
					String customerName=CommonUtils.checkNull(request.getParamValue("customer_name"+vinArr[count]));
					updateVo.setCustomerName("".equals(customerName)?"":customerName);
					String customerPhone=CommonUtils.checkNull(request.getParamValue("customer_phone"+vinArr[count]));
					updateVo.setCustomerPhone("".equals(customerPhone)?"":customerPhone);
					String remark=CommonUtils.checkNull(request.getParamValue("remark"+vinArr[count]));
					updateVo.setRemark("".equals(remark)?"":remark);
					updateVo.setUpdateBy(Long.parseLong(user_id));
					updateVo.setUpdateDate(new Date());
					
					updateNum=update(upObj, updateVo);
				}else{//不存在，插入新的vin特殊费用车辆信息
					TtAsWrSpeoutfeeVehiclePO insertObj=new TtAsWrSpeoutfeeVehiclePO();
					insertObj.setId(Long.parseLong(SequenceManager.getSequence("")));
					insertObj.setFeeId(sfeeInfo.getId());
					insertObj.setVin(vinArr[count]);
					insertObj.setEngineNo(CommonUtils.checkNull(request.getParamValue("engine_no"+vinArr[count])));
					insertObj.setModel(CommonUtils.checkNull(request.getParamValue("vehicle_type"+vinArr[count])));
					String productDate=CommonUtils.checkNull(request.getParamValue("factory_date"+vinArr[count]));
					String saleDate=CommonUtils.checkNull(request.getParamValue("sale_date"+vinArr[count]));
					if(!"".equals(productDate)){
						insertObj.setProductDate(parseStringToDate(productDate));
					}
					if(!"".equals(saleDate)){
						insertObj.setSaleDate(parseStringToDate(saleDate));
					}
					String mileage=CommonUtils.checkNull(request.getParamValue("vin_mileage"+vinArr[count]));
					insertObj.setMileage("".equals(mileage)?0:Double.parseDouble(mileage));
					String customerName=CommonUtils.checkNull(request.getParamValue("customer_name"+vinArr[count]));
					insertObj.setCustomerName("".equals(customerName)?"":customerName);
					String customerPhone=CommonUtils.checkNull(request.getParamValue("customer_phone"+vinArr[count]));
					insertObj.setCustomerPhone("".equals(customerPhone)?"":customerPhone);
					String remark=CommonUtils.checkNull(request.getParamValue("remark"+vinArr[count]));
					insertObj.setRemark("".equals(remark)?"":remark);
					insertObj.setCreateBy(Long.parseLong(user_id));
					insertObj.setCreateDate(new Date());
					insert(insertObj);
				}
			}
		}else{
			TtAsWrSpeoutfeePO insertSeFee=new TtAsWrSpeoutfeePO();
			attachFileId=newSpeFeeId;//为附件设置业务主键
			insertSeFee.setId(Long.parseLong(newSpeFeeId));
			insertSeFee.setCrId(Long.parseLong(xh_ord_id));
			insertSeFee.setFeeNo(newSpeFeeNo);
			insertSeFee.setStartDate(parseStringToDate(out_start_date));
			insertSeFee.setEndDate(parseStringToDate(out_end_date));
			insertSeFee.setPersonNum(Long.parseLong(out_person_num));
			insertSeFee.setPersonName(out_name);
			insertSeFee.setSingleMileage(Double.parseDouble(single_mileage));
			insertSeFee.setPassFee(Double.parseDouble(road_bridge_fee));
			insertSeFee.setTrafficFee(Double.parseDouble(vehicle_extra_fee));
			insertSeFee.setQuarterFee(Double.parseDouble(lodging_fee));
			insertSeFee.setEatFee(Double.parseDouble(eat_extra_fee));
			insertSeFee.setPersonSubside(Double.parseDouble(person_extra_fee));
			insertSeFee.setApplyContent(apply_content);
			insertSeFee.setStatus(status);
			insertSeFee.setCreateBy(Long.parseLong(user_id));
			insertSeFee.setCreateDate(new Date());
			insertSeFee.setCompanyId(Long.parseLong(company_id));
			insertSeFee.setFeeChannel(Constant.SPE_OUTFEE_CHANNEL_01);
			insertSeFee.setDealerId(Long.parseLong(dealer_id));
			insert(insertSeFee);
			//插入特殊费用车辆信息
			for(int count=0;count<vinArr.length;count++){
				TtAsWrSpeoutfeeVehiclePO insertObj=new TtAsWrSpeoutfeeVehiclePO();
				insertObj.setId(Long.parseLong(SequenceManager.getSequence("")));
				insertObj.setFeeId(Long.parseLong(newSpeFeeId));
				insertObj.setVin(vinArr[count]);
				insertObj.setEngineNo(CommonUtils.checkNull(request.getParamValue("engine_no"+vinArr[count])));
				insertObj.setModel(CommonUtils.checkNull(request.getParamValue("vehicle_type"+vinArr[count])));
				String productDate=CommonUtils.checkNull(request.getParamValue("factory_date"+vinArr[count]));
				String saleDate=CommonUtils.checkNull(request.getParamValue("sale_date"+vinArr[count]));
				if(!"".equals(productDate)){
					insertObj.setProductDate(parseStringToDate(productDate));
				}
				if(!"".equals(saleDate)){
					insertObj.setSaleDate(parseStringToDate(saleDate));
				}
				String mileage=CommonUtils.checkNull(request.getParamValue("vin_mileage"+vinArr[count]));
				insertObj.setMileage("".equals(mileage)?0:Double.parseDouble(mileage));
				String customerName=CommonUtils.checkNull(request.getParamValue("customer_name"+vinArr[count]));
				insertObj.setCustomerName("".equals(customerName)?"":customerName);
				String customerPhone=CommonUtils.checkNull(request.getParamValue("customer_phone"+vinArr[count]));
				insertObj.setCustomerPhone("".equals(customerPhone)?"":customerPhone);
				String remark=CommonUtils.checkNull(request.getParamValue("remark"+vinArr[count]));
				insertObj.setRemark("".equals(remark)?"":remark);
				insertObj.setCreateBy(Long.parseLong(user_id));
				insertObj.setCreateDate(new Date());
				insert(insertObj);
			}
		}
		retMap.put("retCode", "success");
		retMap.put("fileId", attachFileId);
		return retMap;
	}
	/**
	 * Function：判断在特殊费用车辆表是否存在VIN数据信息
	 * @param  ：	
	 * @return:		@param fee_id
	 * @return:		@param vin
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-16
	 */
	public boolean isExistSpFeeVin(String fee_id,String vin){
		int retNum=0;
		if(fee_id==null||"".equals(fee_id)||vin==null||"".equals(vin)) return false;
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select count(1) from Tt_As_Wr_Speoutfee_Vehicle where fee_id="+fee_id+" and vin='"+vin+"'");
		List rsList=pageQuery(sqlStr.toString(), null, getFunName());
		String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
		retNum=Integer.parseInt(temp);
		if(retNum>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * Function：将时间字符串格式化Date对象
	 * @param  ：	
	 * @return:		@param date
	 * @return:		@return
	 * @return:		@throws ParseException 
	 * @throw：	
	 * LastUpdate：	2010-7-16
	 */
	public Date parseStringToDate(String date)  throws  ParseException{ 
		if("".equals(date)) return null;
        Date result= null ; 
        String parse=date; 
        parse=parse.replaceFirst( "^[0-9]{4}([^0-9]?)" ,  "yyyy$1" ); 
        parse=parse.replaceFirst( "^[0-9]{2}([^0-9]?)" ,  "yy$1" ); 
        parse=parse.replaceFirst( "([^0-9]?)[0-9]{1,2}([^0-9]?)" ,  "$1MM$2" ); 
        parse=parse.replaceFirst( "([^0-9]?)[0-9]{1,2}( ?)" ,  "$1dd$2" ); 
        parse=parse.replaceFirst( "( )[0-9]{1,2}([^0-9]?)" ,  "$1HH$2" ); 
        parse=parse.replaceFirst( "([^0-9]?)[0-9]{1,2}([^0-9]?)" ,  "$1mm$2" ); 
        parse=parse.replaceFirst( "([^0-9]?)[0-9]{1,2}([^0-9]?)" ,  "$1ss$2" ); 
         
        DateFormat format= new  SimpleDateFormat(parse); 

        result=format.parse(date); 
         
         return  result; 
    } 
	/**
	 * Function：删除车辆信息
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-16
	 */
    public String delVinInfo(Map params){
    	String xh_ord_id=CommonUtils.checkNull(params.get("xh_ord_id"));
		String del_vin=CommonUtils.checkNull(params.get("del_vin"));
		@SuppressWarnings("unused")
		int delNum=0;
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("delete from Tt_As_Wr_Speoutfee_Vehicle where\n" );
		sqlStr.append("fee_id=(select id from Tt_As_Wr_Speoutfee where cr_id="+xh_ord_id+")\n" );
		sqlStr.append("and vin='"+del_vin+"'");
        
		delNum=delete(sqlStr.toString(), null);
		
    	return "success";
    }
    /**
     * Function：获得特殊费用详细信息
     * @param  ：	
     * @return:		@param params
     * @return:		@return 
     * @throw：	
     * LastUpdate：	2010-7-16
     */
    public SpeFeeDetailInfoBean getSpeFeeInfoDetailBean(Map params){
    	String ord_id=CommonUtils.checkNull(params.get("ord_id"));
    	StringBuffer sqlStr= new StringBuffer();
    	sqlStr.append("select tt.id,td.dealer_code,td.dealer_shortname dealer_name,tt.fee_no,tt.cr_id,to_char(tt.start_date,'YYYY-MM-DD') start_date,\n" );
    	sqlStr.append("to_char(tt.end_date,'YYYY-MM-DD') end_date,tt.person_num,tt.person_name,\n" );
    	sqlStr.append("decode(tt.start_date-tt.end_date,0,'1',abs(tt.start_date-tt.end_date)+1) out_days,\n" );
    	sqlStr.append("tt.person_num,tt.person_name,\n" );
    	sqlStr.append("tt.single_mileage,tt.pass_fee,tt.traffic_fee,tt.quarter_fee,\n" );
    	sqlStr.append("tt.eat_fee,tt.person_subside,tt.apply_content,\n" );
    	sqlStr.append("tt.pass_fee+tt.traffic_fee+tt.quarter_fee+tt.eat_fee+tt.person_subside total_fee\n" );
    	sqlStr.append("from Tt_As_Wr_Speoutfee tt,tm_dealer td where tt.dealer_id=td.dealer_id and tt.cr_id="+ord_id);
    	
    	List list=select(SpeFeeDetailInfoBean.class, sqlStr.toString(), null);
    	if(list!=null&&list.size()>0){
    		return (SpeFeeDetailInfoBean)list.get(0);
    	}else{
    		return null;
    	}
    }
    /**
     * Function：上报功能信息
     * @param  ：	
     * @return:		@param params
     * @return:		@return 
     * @throw：	
     * LastUpdate：	2010-7-16
     */
    public String reportOrdOper(Map params){
    	String xh_ord_id=CommonUtils.checkNull(params.get("xh_ord_id"));
    	String user_id=CommonUtils.checkNull(params.get("user_id"));
    	int updateNum=0;
    	if("".equals(xh_ord_id)||"".equals(user_id)){
    		return "failure_001";//无法获得登陆信息
    	}
    	StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select * from TT_AS_WR_SPEOUTFEE where cr_id="+xh_ord_id);
		List rsList=select(TtAsWrSpeoutfeePO.class, sqlStr.toString(), null);
		TtAsWrSpeoutfeePO queryObj=new TtAsWrSpeoutfeePO();
		if(rsList!=null&&rsList.size()>0){
			queryObj=(TtAsWrSpeoutfeePO)rsList.get(0);
		}else{
			return "failure_002";//该特殊费用申请单不存在，请先保存再上报
		}
		//判断
		Integer status=queryObj.getStatus();
		if("".equals(status)){
			return "failure_003";//该特殊费用申请单还没有保存，请先保存再上报
		}
    	TtAsWrSpeoutfeePO updateObj=new TtAsWrSpeoutfeePO();
    	updateObj.setCrId(Long.parseLong(xh_ord_id));
    	
    	TtAsWrSpeoutfeePO vo=new TtAsWrSpeoutfeePO();
    	vo.setStatus(Constant.SPE_OUTFEE_STATUS_02);
    	vo.setMakeDate(new Date());
    	vo.setUpdateBy(Long.parseLong(user_id));
    	vo.setUpdateDate(new Date());
    	updateNum=update(updateObj,vo);
    	if(updateNum==1){
    		return "success";
    	}else{
    		return "failure";
    	}
    }
    /**
     * Function：获得附件信息列表ss
     * @param  ：	
     * @return:		@param id
     * @return:		@return 
     * @throw：	
     * LastUpdate：	2010-7-16
     */
    public List<FsFileuploadPO> queryAttachFileInfo(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A ");
		sql.append(" WHERE A.YWZJ=(select id from Tt_As_Wr_Speoutfee where cr_id='"+id+"')");
		List<FsFileuploadPO> ls= select (FsFileuploadPO.class,sql.toString(),null);
		return ls;
	}
    /**
     * Function：获得特殊费用车辆信息列表
     * @param  ：	
     * @return:		@param params
     * @return:		@return 
     * @throw：	
     * LastUpdate：	2010-7-16
     */
    public List<SpeFeeVehicleListInfoBean> querySpeVehicleListInfo(Map params){
    	String ord_id=CommonUtils.checkNull(params.get("ord_id"));
    	StringBuffer sqlStr= new StringBuffer();
    	sqlStr.append("select tt.id,tt.fee_id,tt.vin,tt.engine_no,\n" );
    	sqlStr.append("tt.model,to_char(tt.product_date,'YYYY-MM-DD') product_date,\n" );
    	sqlStr.append("to_char(tt.sale_date,'YYYY-MM-DD') sale_date,tt.mileage,\n" );
    	sqlStr.append("tt.customer_name,tt.customer_phone,tt.remark\n" );
    	sqlStr.append("from Tt_As_Wr_Speoutfee_Vehicle tt\n" );
    	sqlStr.append("where tt.fee_id=(select id from Tt_As_Wr_Speoutfee where cr_id="+ord_id+")\n");
    	sqlStr.append("order by tt.id");
    	List<SpeFeeVehicleListInfoBean> list= select (SpeFeeVehicleListInfoBean.class,sqlStr.toString(),null);
    	return list;
    }
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
}

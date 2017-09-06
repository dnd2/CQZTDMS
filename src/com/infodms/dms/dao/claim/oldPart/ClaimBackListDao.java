package com.infodms.dms.dao.claim.oldPart;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.ClaimOldPartApproveStoreListBean;
import com.infodms.dms.bean.TtAsWrBackListQryBean;
import com.infodms.dms.bean.TtAsWrOldPartBackListDetailBean;
import com.infodms.dms.bean.TtAsWrOldPartDetailListBean;
import com.infodms.dms.bean.TtAsWrPartsitemApplyBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmAsWrBarcodePartStockPO;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOldpartTransportPO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrOldReturnedPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.po.TtAsWrReturnedOrderDetailPO;
import com.infodms.dms.po.TtAsWrReturnedOrderPO;
import com.infodms.dms.po.TtDeliveryOrderDetailPO;
import com.infodms.dms.po.TtDeliveryOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.dao.SpecialDAO;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 类说明：
 * 作者：  赵伦达
 */
@SuppressWarnings("unchecked")
public class ClaimBackListDao extends BaseDao{
	
	public static Logger logger = Logger.getLogger(ClaimBackListDao.class);
	private static final ClaimBackListDao dao = new ClaimBackListDao();
	public static final ClaimBackListDao getInstance(){
		return dao;
	}
	/**
	 * Function：查询所有回运单状态
	 * @param  ：	
	 * @return:	
	 * @throw：	
	 * LastUpdate：	2010-6-8
	 */
	public PageResult<TtAsWrBackListQryBean> queryClaimBackList(String sqlStr,int curPage, int pageSize){
		PageResult<TtAsWrBackListQryBean> pr = pageQuery(TtAsWrBackListQryBean.class,sqlStr,null, pageSize, curPage);
		return pr;
	}
	/**
	 * Function：新增页面查询未回运清单列表
	 * @param  ：	
	 * @return:		@param sqlStr
	 * @return:		@param curPage
	 * @return:		@param pageSize
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-8
	 */
	public PageResult<TtAsWrPartsitemApplyBean> queryUnBackClaimList(String sqlStr,int curPage, int pageSize) {
		PageResult<TtAsWrPartsitemApplyBean> pr = pageQuery(TtAsWrPartsitemApplyBean.class,sqlStr,null, pageSize, curPage);
		
		return pr;
	}
	public boolean isExistClaimBackId(String id){
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select * from TT_AS_WR_OLD_RETURNED where return_no='"+id+"'");
		List rsList=select(TtAsWrOldReturnedPO.class, sqlStr.toString(), null);
		if(rsList.size()>0){
			return true;
		}else{
			return false;
		}
	}
	//判断当前时间是否是当月的第四天
	public String isFourDay(){
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select trunc(sysdate) - to_date(to_char(sysdate, 'yyyy-mm') || '-01', 'yyyy-mm-dd') + 1 as days  from dual");
		Map<String, Object> map = pageQueryMap(sqlStr.toString(), null, getFunName());
		 String days = String.valueOf(map.get("DAYS"));
		 System.out.println(days);
		 if(Integer.valueOf(days)>4){
			 return "canOpen";
		 }
		return "notOpen";
	}
	public void insertClaimBackMainInfo(TtAsWrOldReturnedPO insertObj){
		insert(insertObj);
	}
	/**
	 * Function：得到索赔单总数
	 * @param  ：	
	 * @return:		@param idStr
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-5
	 */
	public int getClaimTotalNum(String type,String idStr){
		int retNum=0;
		StringBuffer sqlStr=new StringBuffer();
		if("part_id".equals(type)){
			sqlStr.append("select count(distinct(id)) num from tt_as_wr_partsitem where part_id in('"+idStr+"')");
		}else{
			sqlStr.append("select count(distinct(id)) num from tt_as_wr_partsitem where id in("+idStr+")");
		}
		
		List rsList=pageQuery(sqlStr.toString(), null, getFunName());
		String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
		retNum=Integer.parseInt(temp);
		return retNum;
	}
	/**
	 * Function：通过索赔回运单号获得配件项数
	 * @param  ：	
	 * @return:		@param id
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-7
	 */
	public int getClaimPartItemNumByClaimId(String backAppId){
		int retNum=0;
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select count(distinct(PART_ID)) NUM from TT_AS_WR_OLD_RETURNED_DETAIL where RETURN_ID=(");
		sqlStr.append("select id from TT_AS_WR_OLD_RETURNED where RETURN_NO='"+backAppId+"')");
		List rsList=pageQuery(sqlStr.toString(), null, getFunName());
		String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
		retNum=Integer.parseInt(temp);
		
		return retNum;
	}
	/**
	 * Function：通过索赔回运单号获得索赔申请单数
	 * @param  ：	
	 * @return:		@param backAppId
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-7
	 */
	public int getClaimOrdToTalNum(String backAppId){
		int retNum=0;
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select count(*) num from TT_AS_WR_OLD_RETURNED_DETAIL where RETURN_ID=(");
		sqlStr.append("select id from TT_AS_WR_OLD_RETURNED where RETURN_NO='"+backAppId+"')");
		List rsList=pageQuery(sqlStr.toString(), null, getFunName());
		String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
		retNum=Integer.parseInt(temp);
		
		return retNum;
	}
	/**
	 * Function：通过索赔回运单号获得配件数量
	 * @param  ：	
	 * @return:		@param backAppId
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-7
	 */
	public int getClaimPartNum(String backAppId){
		int retNum=0;
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select  sum(nvl(RETURN_AMOUNT,0)) num from TT_AS_WR_OLD_RETURNED_DETAIL where RETURN_ID=(");
		sqlStr.append("select id from TT_AS_WR_OLD_RETURNED where RETURN_NO='"+backAppId+"')");
		List rsList=pageQuery(sqlStr.toString(), null, getFunName());
		String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
	    if("".equals(temp)) return 0;
		retNum=Integer.parseInt(temp);
		
		return retNum;
	}
	/**
	 * Function：得到回运配件项数
	 * @param  ：	
	 * @return:		@param idStr
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-5
	 */
	public int getClaimItemTotalNum(String type,String idStr){
		int retNum=0;
		StringBuffer sqlStr=new StringBuffer();
		if("part_id".equals(type)){
			sqlStr.append("select count(distinct(part_code)) num from tt_as_wr_partsitem where part_id in('"+idStr+"')");
		}else{
			sqlStr.append("select count(distinct(part_code)) num from tt_as_wr_partsitem where id in("+idStr+")");
		}
		List rsList=pageQuery(sqlStr.toString(), null, getFunName());
		String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
		retNum=Integer.parseInt(temp);
		return retNum;
	}
	
	/**
	 * Function：根据配件编号和回运单生成编号获得已回运数量
	 * @param  ：	
	 * @return:		@param idStr
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-5
	 */
	public int getBackedPartNumByReturnPartId(String claimOrdId,String part_id){
		int retNum=0;
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select nvl(sum(RETURN_AMOUNT),0) num from TT_AS_WR_OLD_RETURNED_DETAIL ");
		sqlStr.append("where part_id="+part_id+" and return_id=(select id from TT_AS_WR_OLD_RETURNED where return_no='"+claimOrdId+"')");
		List rsList=super.pageQuery(sqlStr.toString(), null, getFunName());
	    String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
	    if("".equals(temp)) return 0;
		retNum=Integer.parseInt(temp);
		
		return retNum;
	}
	/**
	 * Function：根据配件编号获得已回运数量
	 * @param  ：	
	 * @return:		@param idStr
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-5
	 */
	public long getDetailMainId(String return_no,String part_id){
		long retNum=0;
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select id from TT_AS_WR_OLD_RETURNED_DETAIL where part_id="+part_id);
		sqlStr.append(" and claim_no='"+return_no.trim()+"'");
		List rsList=pageQuery(sqlStr.toString(), null, getFunName());
	    String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
		retNum=Long.parseLong(temp);
		
		return retNum;
	}
	/**
	 * Function：根据回运配件id获得索赔申请表信息
	 * @param  ：	
	 * @return:		@param part_id
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-6 赵伦达
	 */
	public TtAsWrApplicationPO getClaimApplyOrdPo(String part_id){
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select * from TT_AS_WR_APPLICATION");
		sqlStr.append(" where id=(select id from tt_as_wr_partsitem where part_id='"+part_id+"')");
		List temp=super.select(TtAsWrApplicationPO.class, sqlStr.toString(), null);
		
		return (TtAsWrApplicationPO)temp.get(0);
	}
	public TmVehiclePO getVehicleInfo(String vin){
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select * from tm_vehicle");
		sqlStr.append(" where vin='"+vin+"'");
		List temp=super.select(TmVehiclePO.class, sqlStr.toString(), null);
		if(temp==null||temp.size()<=0){
			return null;
		}
		return (TmVehiclePO)temp.get(0);
	}
	/**
	 * Function：通过配件编号获得索赔配件信息表
	 * @param  ：	
	 * @return:		@param part_id
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-8
	 */
	public TtAsWrPartsitemPO getClaimPartItemPo(String part_id){
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select * from tt_as_wr_partsitem where part_id='"+part_id+"'");
		List temp=super.select(TtAsWrPartsitemPO.class, sqlStr.toString(), null);
		
		return (TtAsWrPartsitemPO)temp.get(0);
	}
	/**
	 * Function 在索赔回运明细表是否存在该配件编号的记录
	 * @param  ：	
	 * @return:		@param part_id
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-8
	 */
	public boolean isExistPartitemAtBackDetail(String part_id){
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select * from tt_as_wr_old_returned_detail where part_id="+part_id);
		List rsList=select(TtAsWrOldReturnedPO.class, sqlStr.toString(), null);
		if(rsList.size()>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * Function：修改索赔回运清单
	 * @param  ：	
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-7
	 */
	public int updateClaimBackOrdMainInfo(String id,TtAsWrOldReturnedPO vo){
		int updateNum=0;
		TtAsWrOldReturnedPO date=new TtAsWrOldReturnedPO();
		date.setReturnNo(id);
		updateNum=super.update(date, vo);
		return updateNum;
	}
	/**
	 * Function：修改索赔回运清单明细
	 * @param  ：	
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-7
	 */
	public int updateClaimBackOrdDetailInfo(String id,TtAsWrOldReturnedDetailPO vo){
		int updateNum=0;
		TtAsWrOldReturnedDetailPO date=new TtAsWrOldReturnedDetailPO();
		date.setId(Long.parseLong(id));
		updateNum=super.update(date, vo);
		return updateNum;
	}
	/**
	 * Function：修改索赔配件信息表
	 * @param  ：	
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-7
	 */
	public int updateClaimApplyPartInfo(String partId,TtAsWrPartsitemPO vo){
		int updateNum=0;
		TtAsWrPartsitemPO date=new TtAsWrPartsitemPO();
		date.setPartId(Long.parseLong(partId));
		updateNum=super.update(date, vo);
		return updateNum;
	}
	/**
	 * Function：获得索赔配件信息表的详细信息
	 * @param  ：	
	 * @return:		@param sqlStr
	 * @return:		@param curPage
	 * @return:		@param pageSize
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-9
	 */
	public TtAsWrOldPartBackListDetailBean getClaimBackInfo(String sqlStr,int curPage, int pageSize){
		
		TtAsWrOldPartBackListDetailBean resultPO = null;
		PageResult<TtAsWrOldPartBackListDetailBean> pr = null;
    	pr = pageQuery(TtAsWrOldPartBackListDetailBean.class,sqlStr, null, pageSize, curPage);
    	if(pr!=null && pr.getRecords()!=null && pr.getRecords().size()>0)
    		resultPO = (TtAsWrOldPartBackListDetailBean)pr.getRecords().get(0);
    	
    	return resultPO;
    }
	/**
	 * Function：获得索赔回运清单明细信息
	 * @param  ：	
	 * @return:		@param sqlStr
	 * @return:		@param curPage
	 * @return:		@param pageSize
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-9
	 */
	public List<TtAsWrOldPartDetailListBean> queryClaimBackDetailList(String sqlStr,int curPage, int pageSize){
		PageResult<TtAsWrOldPartDetailListBean> pr = pageQuery(TtAsWrOldPartDetailListBean.class,sqlStr,null, pageSize, curPage);
		return pr.getRecords();
	}
	
	/**
	 * 获得索赔回运清单明细信息（不带分页查询）
	 * @param sqlStr
	 * @return
	 */
	public List<TtAsWrOldPartDetailListBean> queryClaimBackDetailList(String sqlStr){
		return this.select(TtAsWrOldPartDetailListBean.class, sqlStr, null);
	}
	
	public List<TtAsWrOldReturnedDetailPO> getOldReturnedDetailPOByReturnId(String sqlStr,int curPage, int pageSize){
		PageResult<TtAsWrOldReturnedDetailPO> pr = pageQuery(TtAsWrOldReturnedDetailPO.class,sqlStr,null, pageSize, curPage);
		return pr.getRecords();
	}
	public int getExistReturnedDetailPartNum(String part_id){
		int retNum=0;
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select nvl(sum(RETURN_AMOUNT),0) num from TT_AS_WR_OLD_RETURNED_DETAIL ");
		sqlStr.append("where part_id="+part_id);
		List rsList=super.pageQuery(sqlStr.toString(), null, getFunName());
	    String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
	    if("".equals(temp)) return 0;
		retNum=Integer.parseInt(temp);
		return retNum;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * Function：通过回运清单主键和索赔单号判断索赔单是否已回运
	 * @param  ：	
	 * @return:		@param return_no
	 * @return:		@param claim_no
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-11
	 */
	public boolean isExistSameClaimNo(String return_no,String claim_no){
		int retNum=0;
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select * from TT_AS_WR_OLD_RETURNED_DETAIL ");
		sqlStr.append("where return_id="+return_no+" and claim_no='"+claim_no+"'");
		List rsList=super.pageQuery(sqlStr.toString(), null, getFunName());
		if(rsList!=null) retNum=rsList.size();
		if(retNum>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * Function：根据索赔回运清单生成号获得回运清单主键
	 * @param  ：	
	 * @return:		@param return_no
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-11
	 */
	public String getReturnIdByReturnNo(String return_no){
		String retStr="";
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select id from TT_AS_WR_OLD_RETURNED ");
		sqlStr.append("where return_no='"+return_no+"'");
		List rsList=super.pageQuery(sqlStr.toString(), null, getFunName());
	    String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
	    retStr=temp;
	    return retStr;
	}
	/**
	 * Function：通过配件编号查找索赔配件信息
	 * @param  ：	
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-6-12
	 */
	public List getPartItemPoByPartId(String part_id){
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select * from tt_as_wr_partsitem tt ");
		sqlStr.append("where tt.part_id="+part_id);
		PageResult<TtAsWrPartsitemPO> pr = pageQuery(TtAsWrPartsitemPO.class,sqlStr.toString(),null, 10, 1);
		return pr.getRecords();
	}
	/**
	 * Function：常规回运方式生成回运清单
	 * @param  ：	
	 * @return:		@param params
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-8
	 */
	public Map addNormalBackProcess(RequestWrapper request,Map params,Long logonId,Long dealer_id){
		String company_id=ClaimTools.dealParamStr(params.get("company_id"));//公司id
		String idStr=ClaimTools.dealParamStr(params.get("idStr"));//选中的索赔表中的id
		String claimOrdId=ClaimTools.dealParamStr(params.get("claimOrdId"));//回运清单号
		String claimQryDate=ClaimTools.dealParamStr(params.get("claimQryDate"));//提报日期
		String freight_type=ClaimTools.dealParamStr(params.get("freight_type"));//货运方式
		String back_type=ClaimTools.dealParamStr(params.get("back_type"));//回运类型
		String boxTotalNum=ClaimTools.dealParamStr(params.get("boxTotalNum"));//装箱总数
		String remark=ClaimTools.dealParamStr(params.get("remark"));//装箱总数
		Map retMap=new HashMap();
		StringBuffer sqlStr=new StringBuffer();
		int updateSuccessNum=0;
		
		if(!"".equals(idStr)){
			String[] array=idStr.split(",");
			if(isExistClaimBackId(claimOrdId)){//已经存在回运清单主表，只需要向回运明细表中插入新数据
				float addPratItem=0;
				//取出回运主表的信息
				sqlStr.delete(0, sqlStr.length());
				sqlStr.append("select * from TT_AS_WR_OLD_RETURNED where return_no='"+claimOrdId+"'");
				List list=select(TtAsWrOldReturnedPO.class, sqlStr.toString(), null);
				TtAsWrOldReturnedPO returnData=(TtAsWrOldReturnedPO)list.get(0);
				//根据索赔id插入到回运明细表中
				for(int count=0;count<array.length;count++){
					TtAsWrApplicationPO applicationInfo=new TtAsWrApplicationPO();
					applicationInfo=getClaimInfo(Long.parseLong(array[count]));
					List partItemList=getClaimPartInfo(Long.parseLong(array[count]));
					if(partItemList!=null){
						for(int i=0;i<partItemList.size();i++){
							TtAsWrPartsitemPO partitemInfo=new TtAsWrPartsitemPO();
							partitemInfo=(TtAsWrPartsitemPO)partItemList.get(i);
							
							addPratItem+=partitemInfo.getBalanceQuantity();//累计配件数
							
							TtAsWrOldReturnedDetailPO insertObj=new TtAsWrOldReturnedDetailPO();
							TmVehiclePO tmVinInfo=getVehicleInfo(applicationInfo.getVin());
							insertObj.setId(Long.parseLong(SequenceManager.getSequence("")));
							insertObj.setReturnId(returnData.getId());
							insertObj.setClaimNo(applicationInfo==null?"":applicationInfo.getClaimNo());
							insertObj.setVin(applicationInfo==null?"":applicationInfo.getVin());
							insertObj.setPartId(partitemInfo.getPartId());
							insertObj.setNReturnAmount(Float.valueOf(partitemInfo.getQuantity()).intValue());
							insertObj.setReturnAmount(Float.valueOf(partitemInfo.getQuantity()).intValue());
							//insertObj.setBoxNo(request.getParamValue("boxOrd"+array[count])==null?"":request.getParamValue("boxOrd"+array[count]));
							insertObj.setSignAmount(0);
							insertObj.setProducerCode(partitemInfo.getProducerCode());//配件生产商code
							insertObj.setProducerName(partitemInfo.getProducerName());//配件生产商name
							insertObj.setCreateDate(new Date());
							insertObj.setCreateBy(logonId);
							insertObj.setProcFactory(tmVinInfo==null?"":String.valueOf(tmVinInfo.getYieldly()));//产地
							dao.insert(insertObj);
							
							//同时修改索赔配件表中的回运数量
							TtAsWrPartsitemPO updateObj=new TtAsWrPartsitemPO();
							updateObj.setPartId(partitemInfo.getPartId());
							TtAsWrPartsitemPO vo=new TtAsWrPartsitemPO();
							vo.setReturnNum(partitemInfo.getQuantity());
							vo.setUpdateBy(logonId);
							vo.setUpdateDate(new Date());
							updateSuccessNum+=update(updateObj, vo);
						}
						//插入完回运明细表后修改回运主表中的索赔单数、回运配件项数、配件数
						TtAsWrOldReturnedPO updateReturnObj=new TtAsWrOldReturnedPO();
						updateReturnObj.setId(returnData.getId());
						
						TtAsWrOldReturnedPO voReturnObj=new TtAsWrOldReturnedPO();
						if(boxTotalNum!=null&&!"".equals(boxTotalNum)){//装箱总数量
							voReturnObj.setParkageAmount(Integer.parseInt(boxTotalNum));
						}
						voReturnObj.setWrAmount(returnData.getWrAmount()+array.length);
						voReturnObj.setPartItemAmount(getClaimItemTotalNum(String.valueOf(returnData.getId()),idStr));
						voReturnObj.setPartAmount(returnData.getPartAmount()+Float.valueOf(addPratItem).intValue());
						voReturnObj.setStatus(Constant.BACK_LIST_STATUS_01);//将回运清单置为未上报状态
						voReturnObj.setUpdateBy(logonId);
						voReturnObj.setUpdateDate(new Date());
						voReturnObj.setRemark(remark);
						update(updateReturnObj, voReturnObj);
					}
				}
			}else{//不存在，需要向回运主表和明细表插入新数据
				//保存索赔回运清单表
				long mainTableId=Long.parseLong(SequenceManager.getSequence(""));
				TtAsWrOldReturnedPO mainPo=new TtAsWrOldReturnedPO();
				mainPo.setId(mainTableId);
				mainPo.setDealerId(dealer_id);
				mainPo.setReturnNo(claimOrdId);
				mainPo.setWrAmount(getClaimTotalNum("",idStr));
				mainPo.setPartItemAmount(getClaimItemTotalNum(null,idStr));
				mainPo.setPartAmount(getPartItemAmountInBackInfoTable(idStr));
				if(boxTotalNum!=null&&!"".equals(boxTotalNum)){//装箱总数量
					mainPo.setParkageAmount(Integer.parseInt(boxTotalNum));
				}
				mainPo.setTransportType(Integer.parseInt(freight_type));
				mainPo.setStatus(Constant.BACK_LIST_STATUS_01);
				mainPo.setReturnType(Integer.parseInt(back_type));
				mainPo.setWrStartDate(claimQryDate);
				mainPo.setCreateDate(new Date());
				mainPo.setCreateBy(logonId);
				mainPo.setOemCompanyId(Long.parseLong(company_id));
				mainPo.setRemark(remark);
				insert(mainPo);
				//保存回运明细信息
				float addPratItem=0;
				//根据索赔id插入到回运明细表中
				for(int count=0;count<array.length;count++){
					TtAsWrApplicationPO applicationInfo=new TtAsWrApplicationPO();
					applicationInfo=getClaimInfo(Long.parseLong(array[count]));
					List partItemList=getClaimPartInfo(Long.parseLong(array[count]));
					if(partItemList!=null){
						for(int i=0;i<partItemList.size();i++){
							TtAsWrPartsitemPO partitemInfo=new TtAsWrPartsitemPO();
							partitemInfo=(TtAsWrPartsitemPO)partItemList.get(i);
							//20100930 将需要回运数量和回运数量都修改为结算数量
							addPratItem+=partitemInfo.getBalanceQuantity();//累计配件数
							
							TtAsWrOldReturnedDetailPO insertObj=new TtAsWrOldReturnedDetailPO();
							TmVehiclePO tmVinInfo=getVehicleInfo(applicationInfo.getVin());
							insertObj.setId(Long.parseLong(SequenceManager.getSequence("")));
							insertObj.setReturnId(mainTableId);
							insertObj.setClaimNo(applicationInfo==null?"":applicationInfo.getClaimNo());
							insertObj.setVin(applicationInfo==null?"":applicationInfo.getVin());
							insertObj.setPartId(partitemInfo.getPartId());
							insertObj.setNReturnAmount(Float.valueOf(partitemInfo.getBalanceQuantity()).intValue());
							insertObj.setReturnAmount(Float.valueOf(partitemInfo.getBalanceQuantity()).intValue());
							//insertObj.setBoxNo(request.getParamValue("boxOrd"+array[count])==null?"":request.getParamValue("boxOrd"+array[count]));
							insertObj.setSignAmount(0);
							insertObj.setProducerCode(partitemInfo.getProducerCode());//配件生产商code
							insertObj.setProducerName(partitemInfo.getProducerName());//配件生产商name
							insertObj.setCreateDate(new Date());
							insertObj.setCreateBy(logonId);
							insertObj.setProcFactory(tmVinInfo==null?"":String.valueOf(tmVinInfo.getYieldly()));//产地
							insert(insertObj);
							
							//同时修改索赔配件表中的回运数量
							TtAsWrPartsitemPO updateObj=new TtAsWrPartsitemPO();
							updateObj.setPartId(partitemInfo.getPartId());
							TtAsWrPartsitemPO vo=new TtAsWrPartsitemPO();
							vo.setReturnNum(partitemInfo.getBalanceQuantity());
							vo.setUpdateBy(logonId);
							vo.setUpdateDate(new Date());
							updateSuccessNum+=update(updateObj, vo);
						}
					}
				}
			}
			retMap.put("flag",1);//成功
		}else{//无法获得选中字符串
			retMap.put("flag",0);
		}
		
		return retMap;
	}
	/**
	 * Function：根据索赔单主键获取索赔信息
	 * @param  ：	
	 * @return:		@param id
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-8
	 */
	public TtAsWrApplicationPO getClaimInfo(Long id){
		if(id==null||id<=0) return null;
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select * from Tt_As_Wr_Application where id="+id);
		List list=select(TtAsWrApplicationPO.class, sqlStr.toString(), null);
		if(list!=null&&list.size()>0){
			return (TtAsWrApplicationPO)list.get(0);
		}else{
			return null;
		}
	}
	/**
	 * Function：根据索赔单主键获取索赔配件信息
	 * @param  ：	
	 * @return:		@param id
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-8
	 */
	public List getClaimPartInfo(Long id){
		if(id==null||id<=0) return null;
		//StringBuffer sqlStr=new StringBuffer();
		//sqlStr.append("select * from Tt_As_Wr_Partsitem where NVL(BALANCE_QUANTITY,0)>NVL(RETURN_NUM,0) AND id="+id);
		//加入配件是否需要回运条件的判断
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT *\n" );
		sql.append("  FROM TT_AS_WR_PARTSITEM A\n" );
		sql.append(" WHERE NVL(A.BALANCE_QUANTITY, 0) > NVL(A.RETURN_NUM, 0)\n" );
		sql.append("   AND EXISTS (SELECT *\n" );
		sql.append("          FROM TM_PT_PART_BASE B\n" );
		sql.append("         WHERE B.PART_CODE = A.PART_CODE\n" );
		sql.append("           AND B.IS_RETURN = "+Constant.IS_NEED_RETURN+")\n" );
		sql.append("   AND A.ID = "+id);

		List list=select(TtAsWrPartsitemPO.class, sql.toString(), null);
		if(list!=null&&list.size()>0){
			return list;
		}else{
			return null;
		}
	}
	/**
	 * Function：根据索赔单id
	 * @param  ：	
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-7-8
	 */
	public int getPartItemAmountInBackInfoTable(String idStr){
		//StringBuffer sqlStr= new StringBuffer();
		int retNum=0;
		//sqlStr.append("select sum(BALANCE_QUANTITY) num from tt_as_wr_partsitem\n" );
		//sqlStr.append("where id in("+idStr+")\n" );
		//加入是否需要回运现在
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT SUM(BALANCE_QUANTITY) NUM\n" );
		sql.append("  FROM TT_AS_WR_PARTSITEM A\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND NVL(A.BALANCE_QUANTITY, 0) > NVL(A.RETURN_NUM, 0)\n" );
		sql.append("   AND EXISTS (SELECT B.PART_ID\n" );
		sql.append("          FROM TM_PT_PART_BASE B\n" );
		sql.append("         WHERE B.PART_CODE = A.PART_CODE\n" );
		sql.append("           AND B.IS_RETURN = "+Constant.IS_NEED_RETURN+")");
		sql.append("   AND ID IN ("+idStr+")\n" );
		
		List rsList=pageQuery(sql.toString(), null, getFunName());
		String temp=rsList.get(0).toString();
	    temp=temp.substring(temp.indexOf("="),temp.indexOf("}")).replace("=","");
		retNum=Integer.parseInt(temp);
		return retNum;
	}
	/**
	 * 
	* @Title: getExportDetail 
	* @Description: TODO(查询将要导出的旧件回运列表) 
	* @param @returnId  回运单ID
	* @return List<Map<String, Object>>    返回类型 
	* @throws
	 */
	public List<Map<String, Object>> getExportDetail(Long returnId, Long dealerId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.CLAIM_NO, A.VIN, B.YIELDLY, B.MODEL_CODE, B.ENGINE_NO, B.IN_MILEAGE, B.TROUBLE_DESC, \n");
		sql.append("       C.PART_CODE, C.PART_NAME, C.REMARK, TO_CHAR(D.PURCHASED_DATE, 'YYYY-MM-DD')PURCHASED_DATE, \n");
		sql.append("       E.DEALER_CODE, E.DEALER_NAME, F.DELIVERER, F.DELIVERER_PHONE, H.DC_NAME \n");
		sql.append("FROM TT_AS_WR_OLD_RETURNED_DETAIL A, TT_AS_WR_APPLICATION B, TT_AS_WR_PARTSITEM C, \n");
		sql.append(     "TM_VEHICLE D, TM_DEALER E, TT_AS_REPAIR_ORDER F, TT_PT_DC_STOCK G, TM_PT_DC H\n");
		sql.append("WHERE A.RETURN_ID = ").append(returnId).append("\n");
		sql.append("  AND A.CLAIM_NO = B.CLAIM_NO \n");
		sql.append("  AND A.PART_ID = C.PART_ID \n");
		sql.append("  AND A.VIN = D.VIN \n");
		sql.append("  AND A.VIN = F.VIN \n");
		sql.append("  AND A.PART_ID = G.PART_ID(+) \n");
		sql.append("  AND B.ID = C.ID \n");
		sql.append("  AND B.DEALER_ID = ").append(dealerId).append("\n");
		sql.append("  AND B.DEALER_ID = E.DEALER_ID");
		sql.append("  AND B.RO_NO = F.RO_NO \n");
		sql.append("  AND G.DC_ID = H.DC_ID(+) \n");
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * 根据经销商ID查询该经销商级下级经销商
	 * @param dealerId 经销商ID
	 * @return
	 */
	public List<TmDealerPO> queryDealersByParentDealerId(String dealerId){
		
		if(dealerId==null)
			return new ArrayList<TmDealerPO>();
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT D.*\n" );
		sql.append("  FROM TM_DEALER D\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append(" START WITH D.DEALER_ID = ?\n" );
		sql.append("CONNECT BY PRIOR D.DEALER_ID = D.PARENT_DEALER_D");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(dealerId);
		
		List<TmDealerPO> dealerList = this.select(TmDealerPO.class, sql.toString(), paramList);
		
		if(dealerList==null)
			dealerList = new ArrayList<TmDealerPO>();

		return dealerList;
	}
	
	/**
	 * 生成回运清单明细
	 * 注意：回运索赔单状态为 "结算审核中"
	 * @param startDate 索赔单开始时间(上报日期)
	 * @param endDate 索赔单结束日期(上报日期)
	 * @param yieldly 产地
	 * @param userId 操作人ID
	 * @param returnId 回运清单ID
	 * @param claim_nos 
	 * @param backType 
	 */
	public void createReturnOrderDetail(String startDate,String endDate,String yieldly,
			Long userId,Long returnId,Long dealerId, String[] claim_nos, String backType){
		
		StringBuffer sql= new StringBuffer();
		/**
		 * 插入数据逻辑变化： 1，索赔单状态由以前的 结算支付 变为 结算室审核。 因为索赔单一上报 状态就变为了结算室审核
		 * 2，时间范围： 由以前的 索赔单结算时间 变为 索赔单上报时间，因为现在的逻辑 在进行旧件回运时,还没有进行结算
		 */
		sql.append("INSERT INTO TT_AS_WR_RETURNED_ORDER_DETAIL(ID,RETURN_ID,CLAIM_NO,VIN,PART_ID,N_RETURN_AMOUNT,\n" );
		sql.append("RETURN_AMOUNT,SIGN_AMOUNT,DEDUCT_REMARK,PRODUCER_CODE,PRODUCER_NAME,CREATE_DATE,CREATE_BY,PROC_FACTORY,CLAIM_ID,PART_CODE,PART_NAME,BARCODE_NO,PRICE,IS_CLIAM,is_main_code,main_part_code,is_import,local_war_house,LOCAL_WAR_SHEL,LOCAL_WAR_layer)\n" );
		
		sql.append("SELECT F_GETID() ID,"+returnId+" RETURN_ID,A.CLAIM_NO,A.VIN,P.PART_ID PARTID,1,\n" );
		sql.append("1,1,0,P.DOWN_PRODUCT_CODE PRODUCER_CODE,P.DOWN_PRODUCT_NAME PRODUCER_NAME,\n" );
		sql.append("SYSDATE CREATE_DATE,"+userId+" CREATE_BY,A.YIELDLY PROC_FACTORY,P.ID CLAIM_ID,P.DOWN_PART_CODE,P.DOWN_PART_NAME, pb1.barcode_no,decode(P.balance_quantity,0,0,P.balance_amount/P.balance_quantity) as price, 0,p.responsibility_type is_main_code,p.main_part_code,a.is_import,b.local_war_house,b.local_war_shel,b.local_war_layer\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TT_AS_WR_PARTSITEM P,Tt_As_Wr_Partsitem_Barcode pb1,tm_pt_part_base b\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = P.ID\n" );
		sql.append("AND p.part_code=b.part_code(+)\n"); 
		sql.append("and p.part_id=pb1.part_id and p.part_use_type=1 and a.claim_type not in (10661002,10661011)\n");
		if(backType.equals("10731002")){
			sql.append("  and A.urgent=0  \n" );
		}
		if(backType.equals("10731001")){
			sql.append(" and  A.urgent=1 \n" );
		}
		sql.append("AND   A.Claim_No not in ( select A.Claim_No from TT_AS_WR_RETURNED_ORDER_DETAIL A    )  and  a.balance_yieldly= "+yieldly+"\n");
		sql.append("AND NVL(P.Quantity,0) > NVL(P.RETURN_NUM,0)\n");
		sql.append("AND ((A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_08+" AND a.is_import="+Constant.IF_TYPE_NO+") OR (A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_13+" AND a.is_import="+Constant.IF_TYPE_YES+"))\n"); 
		sql.append("AND A.DEALER_ID = "+dealerId+"\n");
		//sql.append("AND A.sub_date >= TO_DATE('"+startDate+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n" );
		//sql.append("AND A.sub_date <= TO_DATE('"+endDate+" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
		DaoFactory.getsql(sql, "A.report_date", startDate, 3);
		DaoFactory.getsql(sql, "A.report_date", endDate, 4);
		String claims="";
		if(claim_nos!=null){
			int length = claim_nos.length;
			if(length>0){
				for (int i = 0; i < length; i++) {
					claims+=claim_nos[i]+",";
				}
			}
		}
		DaoFactory.getsql(sql, "A.id", claims, 6);
		sql.append(" AND A.CREATE_DATE >=TO_DATE('2013-08-26 00:00:00','YYYY-MM-DD HH24:MI:SS')");
		this.insert(sql.toString());
	}
	
	//zhumingwei 2011-4-14
	public void createReturnOrderDetail11(String startDate,String endDate,String yieldly,
			Long userId,Long returnId,Long dealerId){
		
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO TT_AS_WR_RETURNED_ORDER_DETAIL(ID,RETURN_ID,CLAIM_NO,VIN,PART_ID,N_RETURN_AMOUNT,\n" );
		sql.append("RETURN_AMOUNT,SIGN_AMOUNT,DEDUCT_REMARK,PRODUCER_CODE,PRODUCER_NAME,CREATE_DATE,CREATE_BY,PROC_FACTORY,CLAIM_ID,PART_CODE,PART_NAME,BARCODE_NO,PRICE,IS_CLIAM)\n" );
		sql.append("SELECT /*+ordered*/F_GETID() ID,"+returnId+" RETURN_ID,A.CLAIM_NO,A.VIN,P.PART_ID PARTID,1,\n" );
		sql.append("1,0,10501004,P.DOWN_PRODUCT_CODE PRODUCER_CODE,P.DOWN_PRODUCT_NAME PRODUCER_NAME,\n" );
		sql.append("SYSDATE CREATE_DATE,"+userId+" CREATE_BY,A.YIELDLY PROC_FACTORY,P.ID CLAIM_ID,P.DOWN_PART_CODE,P.DOWN_PART_NAME, pb1.barcode_no,decode(P.balance_quantity,0,0,P.balance_amount/P.balance_quantity) as price,PB.IS_CLIAM\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A,TT_AS_WR_PARTSITEM P,TM_PT_PART_BASE PB,tm_pt_part_type pt,Tt_As_Wr_Partsitem_Barcode pb1\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = P.ID\n" );
		sql.append("AND P.DOWN_PART_CODE = PB.PART_CODE and p.part_id=pb1.part_id\n" );
		sql.append("AND pb.part_type_id=pt.id and Pt.IS_RETURN = "+Constant.IS_NEED_RETURN+"\n" );
		sql.append("AND PB.PART_IS_CHANGHE = ?\n");
		sql.append("AND NVL(P.BALANCE_QUANTITY,0) > NVL(P.RETURN_NUM,0)\n");
		sql.append("AND A.STATUS='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"'");
		sql.append("AND A.DEALER_ID = ?\n");
		sql.append("AND trunc(A.account_date) >= TO_DATE(?,'YYYY-MM')\n" );
		sql.append("AND trunc(A.account_date) < ADD_MONTHS(TO_DATE(?,'YYYY-MM'),1)");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(yieldly);
		paramList.add(dealerId);
		paramList.add(startDate);
		paramList.add(endDate);
		
		this.update(sql.toString(), paramList);
	}
	
	/**
	 * 根据回运清单明细生成回运清单信息
	 * @param roPO
	 */
	public void createReturnOrder(TtAsWrReturnedOrderPO roPO){
		
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO TT_AS_WR_RETURNED_ORDER\n" );
		sql.append("  (ID,DEALER_ID,TOP_DEALER_ID,DEALER_LEVEL,\n" );
		sql.append("   RETURN_NO,WR_AMOUNT,PART_ITEM_AMOUNT,PART_AMOUNT,\n" );
		sql.append("   STATUS,WR_START_DATE,RETURN_TYPE,OEM_COMPANY_ID,\n" );
		sql.append("   YIELDLY,CREATE_DATE,CREATE_BY)\n" );
		sql.append("  SELECT '"+roPO.getId()+"' ID,\n" );
		sql.append("         '"+roPO.getDealerId()+"' DEALER_ID,\n" );
		sql.append("         '"+roPO.getTopDealerId()+"' TOP_DEALER_ID,\n" );
		sql.append("         '"+roPO.getDealerLevel()+"' DEALER_LEVEL,\n" );
		sql.append("         '"+roPO.getReturnNo()+"' RETURN_NO,\n" );
		sql.append("         CLAIMCOUNT WR_AMOUNT,\n" );
		sql.append("         PART_ITEM_COUNT PART_ITEM_AMOUNT,\n" );
		sql.append("         RETURNCOUNT PART_AMOUNT,\n" );
		sql.append("         '"+roPO.getStatus()+"' STATUS,\n" );
		sql.append("         '"+roPO.getWrStartDate()+"' WR_START_DATE,\n" );
		sql.append("         '"+roPO.getReturnType()+"' RETURN_TYPE,\n" );
		sql.append("         '"+roPO.getOemCompanyId()+"' OEM_COMPANY_ID,\n" );
		sql.append("         '"+roPO.getYieldly()+"' YIELDLY,\n" );
		sql.append("         SYSDATE,\n" );
		sql.append("         '"+roPO.getCreateBy()+"' CREATE_BY\n" );
		sql.append("    FROM (SELECT COUNT(DISTINCT A.CLAIM_NO) CLAIMCOUNT,\n" );
		sql.append("                 COUNT(DISTINCT A.PART_CODE) PART_ITEM_COUNT,\n" );
		sql.append("                 SUM(A.RETURN_AMOUNT) RETURNCOUNT\n" );
		sql.append("            FROM TT_AS_WR_RETURNED_ORDER_DETAIL A\n" );
		sql.append("           WHERE 1 = 1\n" );
		sql.append("             AND A.RETURN_ID = ?)");

		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(roPO.getId());
		
		this.update(sql.toString(), paramList);

	}
	
	public void createReturnOrder11(TtAsWrReturnedOrderPO roPO){
		StringBuilder sql= new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sql.append("INSERT INTO TT_AS_WR_RETURNED_ORDER\n" );
		sql.append("  (ID,BORROW_NO,DEALER_ID,TOP_DEALER_ID,DEALER_LEVEL,\n" );
		sql.append("   RETURN_NO,WR_AMOUNT,PART_ITEM_AMOUNT,PART_AMOUNT,\n" );
		sql.append("   STATUS,WR_START_DATE,return_end_date,RETURN_TYPE,OEM_COMPANY_ID,\n" );
		sql.append("   YIELDLY,RETURN_DATE,CREATE_DATE,CREATE_BY,DEALER_NAME,DEALER_SHORTNAME)\n" );
		sql.append("  SELECT '"+roPO.getId()+"' ID,\n" );
		sql.append("         '"+roPO.getBorrowNo()+"' BORROW_NO,\n");
		sql.append("         '"+roPO.getDealerId()+"' DEALER_ID,\n" );
		sql.append("         '"+roPO.getTopDealerId()+"' TOP_DEALER_ID,\n" );
		sql.append("         '"+roPO.getDealerLevel()+"' DEALER_LEVEL,\n" );
		sql.append("         '"+roPO.getReturnNo()+"' RETURN_NO,\n" );
		sql.append("         CLAIMCOUNT WR_AMOUNT,\n" );
		sql.append("         PART_ITEM_COUNT PART_ITEM_AMOUNT,\n" );
		sql.append("         RETURNCOUNT PART_AMOUNT,\n" );
		sql.append("         '"+roPO.getStatus()+"' STATUS,\n" );
		sql.append("         '"+sdf.format(roPO.getWrStartDate())+"' WR_START_DATE,\n" );
		sql.append("         to_date('"+sdf.format(roPO.getReturnEndDate())+"','yyyy-mm-dd') return_end_date,\n" );
		sql.append("         '"+roPO.getReturnType()+"' RETURN_TYPE,\n" );
		sql.append("         '"+roPO.getOemCompanyId()+"' OEM_COMPANY_ID,\n" );
		sql.append("         '"+roPO.getYieldly()+"' YIELDLY,SYSDATE,\n" );
		sql.append("         SYSDATE,\n" );
		sql.append("         '"+roPO.getCreateBy()+"' CREATE_BY,'"+roPO.getDealerName()+"' DEALER_NAME,'"+roPO.getDealerShortname()+"' DEALER_SHORTNAME\n" );
		sql.append("    FROM (SELECT /*+RULE*/ COUNT(DISTINCT A.CLAIM_NO) CLAIMCOUNT,\n" );
		sql.append("                 COUNT(DISTINCT A.PART_CODE) PART_ITEM_COUNT,\n" );
		sql.append("                 SUM(A.RETURN_AMOUNT) RETURNCOUNT\n" );
		sql.append("            FROM TT_AS_WR_RETURNED_ORDER_DETAIL A\n" );
		sql.append("           WHERE 1 = 1\n" );
		sql.append("             AND A.RETURN_ID = ?)");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(roPO.getId());
		this.update(sql.toString(), paramList);
	}
	
	/**
	 * 将配件回运数量回写到索赔单之配件中
	 * @param detailPO
	 */
	public void writeBackReturnCount(TtAsWrReturnedOrderDetailPO detailPO){
		
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_PARTSITEM P\n" );
		sql.append("   SET P.RETURN_NUM = P.BALANCE_QUANTITY\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND P.PART_ID IN (SELECT /*+index(ROD   IDX_RO_DETAIL_RETURNID)*/ ROD.PART_ID\n" );
		sql.append("          FROM TT_AS_WR_RETURNED_ORDER_DETAIL ROD\n" );
		sql.append("         WHERE 1 = 1\n" );
		sql.append("           AND ROD.RETURN_ID = ?)");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(detailPO.getReturnId());

		this.update(sql.toString(), paramList);
	}
	
	/**
	 * 将配件回运数量回写到索赔单之配件中
	 * @param detailPO
	 */
	public void writeBackReturnCountZero(TtAsWrReturnedOrderDetailPO detailPO){
		
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_PARTSITEM P\n" );
		sql.append("   SET P.RETURN_NUM = 0\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND P.PART_ID IN (SELECT /*+index(ROD   IDX_RO_DETAIL_RETURNID)*/ ROD.PART_ID\n" );
		sql.append("          FROM TT_AS_WR_RETURNED_ORDER_DETAIL ROD\n" );
		sql.append("         WHERE 1 = 1\n" );
		sql.append("           AND ROD.RETURN_ID = ?)");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(detailPO.getReturnId());

		this.update(sql.toString(), paramList);
	}
	
	/**
	 * 将回运清单明细写入到物流单明细中
	 * @param logisticsId
	 */
	public void writeDetailToLogisticsDetail(Long logisticsId,Long userId){
		
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO TT_AS_WR_OLD_RETURNED_DETAIL(ID,RETURN_ID,CLAIM_NO,VIN,PART_ID,N_RETURN_AMOUNT,\n" );
		sql.append("RETURN_AMOUNT,PRODUCER_CODE,PRODUCER_NAME,PROC_FACTORY,DEALER_RETURN_ID,CREATE_BY,CREATE_DATE,CLAIM_ID,PART_CODE,PART_NAME)\n" );
		sql.append("SELECT ID,'"+logisticsId+"' RETURN_ID,CLAIM_NO,VIN,PART_ID,N_RETURN_AMOUNT,RETURN_AMOUNT,PRODUCER_CODE,\n" );
		sql.append("PRODUCER_NAME,PROC_FACTORY,RETURN_ID DEALER_RETURN_ID,'"+userId+"' CREATE_BY,SYSDATE CREATE_DATE,CLAIM_ID,PART_CODE,PART_NAME\n" );
		sql.append("FROM TT_AS_WR_RETURNED_ORDER_DETAIL A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND EXISTS (SELECT B.RETURN_ID FROM TR_RETURN_LOGISTICS B\n" );
		sql.append("WHERE B.RETURN_ID = A.RETURN_ID\n" );
		sql.append("AND B.LOGICTIS_ID = "+logisticsId+")");

		this.update(sql.toString(), null);
	}
	
	//zhumingwei 2011-05-13
	public void writeDetailToLogisticsDetail11(Long logisticsId,Long userId){
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO TT_AS_WR_OLD_RETURNED_DETAIL(ID,RETURN_ID,CLAIM_NO,VIN,PART_ID,N_RETURN_AMOUNT,\n" );
		sql.append("RETURN_AMOUNT,SIGN_AMOUNT,PRODUCER_CODE,PRODUCER_NAME,PROC_FACTORY,DEALER_RETURN_ID,CREATE_BY,CREATE_DATE,CLAIM_ID,PART_CODE,PART_NAME,BARCODE_NO,DEDUCT_REMARK,PRICE,IS_CLIAM,is_main_code,main_part_code,is_import");
		sql.append(",local_war_house,LOCAL_WAR_SHEL,LOCAL_WAR_layer,stock_amount)\n" );
		sql.append("SELECT /*+INDEX(A IDX_RO_DETAIL_RETURNID)*/ID,'"+logisticsId+"' RETURN_ID,CLAIM_NO,VIN,PART_ID,N_RETURN_AMOUNT,RETURN_AMOUNT,SIGN_AMOUNT,PRODUCER_CODE,\n" );
		sql.append("PRODUCER_NAME,PROC_FACTORY,RETURN_ID DEALER_RETURN_ID,'"+userId+"' CREATE_BY,SYSDATE CREATE_DATE,CLAIM_ID,PART_CODE,PART_NAME,BARCODE_NO,DEDUCT_REMARK,PRICE,IS_CLIAM,is_main_code,main_part_code,is_import\n");
		sql.append(",local_war_house,LOCAL_WAR_SHEL,LOCAL_WAR_layer,2\n" );
		sql.append("FROM TT_AS_WR_RETURNED_ORDER_DETAIL A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND EXISTS (SELECT /*+INDEX(B IDX_LOGISTICS_ID)*/B.RETURN_ID FROM TR_RETURN_LOGISTICS B\n" );
		sql.append("WHERE B.RETURN_ID = A.RETURN_ID\n" );
		sql.append("AND B.LOGICTIS_ID = "+logisticsId+")");

		this.update(sql.toString(), null);
	}
	
	public void createLogisticsOrder(TtAsWrOldReturnedPO roPO,String sendTime){
		
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO TT_AS_WR_OLD_RETURNED(ID,DEALER_ID,RETURN_NO,WR_AMOUNT,PART_ITEM_AMOUNT,PART_AMOUNT,\n" );
		sql.append("TRANSPORT_TYPE,PARKAGE_AMOUNT,STATUS,TRAN_NO,RETURN_TYPE,CREATE_DATE,CREATE_BY,OEM_COMPANY_ID,YIELDLY,\n" );
		sql.append("SEND_TIME)\n");
		sql.append("  SELECT '"+roPO.getId()+"' ID,\n" );
		sql.append("         '"+roPO.getDealerId()+"' DEALER_ID,\n" );
		sql.append("         '"+roPO.getReturnNo()+"' RETURN_NO,\n" );
		sql.append("         CLAIMCOUNT WR_AMOUNT,\n" );
		sql.append("         PART_ITEM_COUNT PART_ITEM_AMOUNT,\n" );
		sql.append("         RETURNCOUNT PART_AMOUNT,\n" );
		sql.append("         '"+CommonUtils.checkNull(roPO.getTransportType())+"' TRANSPORT_TYPE,\n" );
		sql.append("         '"+CommonUtils.checkNull(roPO.getParkageAmount())+"' PARKAGE_AMOUNT,\n" );
		sql.append("         '"+roPO.getStatus()+"' STATUS,\n" );
		sql.append("         '"+CommonUtils.checkNull(roPO.getTranNo())+"' TRAN_NO,\n" );
		sql.append("         '"+CommonUtils.checkNull(roPO.getReturnType())+"' RETURN_TYPE,\n" );
		sql.append("         SYSDATE CREATE_DATE,\n" );
		sql.append("         '"+roPO.getCreateBy()+"' CREATE_BY,\n" );
		sql.append("         '"+roPO.getOemCompanyId()+"' OEM_COMPANY_ID,\n" );
		sql.append("         '"+roPO.getYieldly()+"' YIELDLY,\n" );
		sql.append("         TO_DATE('"+CommonUtils.checkNull(sendTime)+"','YYYY-MM-DD') SEND_TIME\n" );
		sql.append("    FROM (SELECT COUNT(DISTINCT A.CLAIM_NO) CLAIMCOUNT,\n" );
		sql.append("                 COUNT(DISTINCT A.PART_CODE) PART_ITEM_COUNT,\n" );
		sql.append("                 SUM(A.RETURN_AMOUNT) RETURNCOUNT\n" );
		sql.append("            FROM TT_AS_WR_OLD_RETURNED_DETAIL A\n" );
		sql.append("           WHERE 1 = 1\n" );
		sql.append("             AND A.RETURN_ID = "+roPO.getId()+")");
		
		this.update(sql.toString(), null);
	}
	
	//zhumingwei 2011-04-12
	public void createLogisticsOrder1(TtAsWrOldReturnedPO roPO){
		StringBuilder sql= new StringBuilder();
		sql.append("INSERT INTO TT_AS_WR_OLD_RETURNED(ID,DEALER_ID,RETURN_NO,WR_AMOUNT,PART_ITEM_AMOUNT,PART_AMOUNT,\n" );
		sql.append("TRANSPORT_TYPE,PARKAGE_AMOUNT,STATUS,TRAN_NO,RETURN_TYPE,CREATE_DATE,CREATE_BY,OEM_COMPANY_ID,YIELDLY,IS_STATUS,dealer_name,dealer_shortname\n" );
		sql.append(")\n");
		sql.append("  SELECT '"+roPO.getId()+"' ID,\n" );
		sql.append("         '"+roPO.getDealerId()+"' DEALER_ID,\n" );
		sql.append("         '"+roPO.getReturnNo()+"' RETURN_NO,\n" );
		sql.append("         CLAIMCOUNT WR_AMOUNT,\n" );
		sql.append("         PART_ITEM_COUNT PART_ITEM_AMOUNT,\n" );
		sql.append("         RETURNCOUNT PART_AMOUNT,\n" );
		sql.append("         '"+CommonUtils.checkNull(roPO.getTransportType())+"' TRANSPORT_TYPE,\n" );
		sql.append("         '"+CommonUtils.checkNull(roPO.getParkageAmount())+"' PARKAGE_AMOUNT,\n" );
		sql.append("         '"+roPO.getStatus()+"' STATUS,\n" );
		sql.append("         '"+CommonUtils.checkNull(roPO.getTranNo())+"' TRAN_NO,\n" );
		sql.append("         '"+CommonUtils.checkNull(roPO.getReturnType())+"' RETURN_TYPE,\n" );
		sql.append("         SYSDATE CREATE_DATE,\n" );
		sql.append("         '"+roPO.getCreateBy()+"' CREATE_BY,\n" );
		sql.append("         '"+roPO.getOemCompanyId()+"' OEM_COMPANY_ID,\n" );
		sql.append("         '"+roPO.getYieldly()+"' YIELDLY,\n" );
		sql.append("         '"+roPO.getIsStatus()+"' IS_STATUS,\n" );
		sql.append("         '"+roPO.getDealerName()+"' dealer_name,\n" );
		sql.append("         '"+roPO.getDealerShortname()+"' dealer_shortname\n" );
		
		sql.append("    FROM (SELECT COUNT(DISTINCT A.CLAIM_NO) CLAIMCOUNT,\n" );
		sql.append("                 COUNT(DISTINCT A.PART_CODE) PART_ITEM_COUNT,\n" );
		sql.append("                 SUM(A.RETURN_AMOUNT) RETURNCOUNT\n" );
		sql.append("            FROM TT_AS_WR_OLD_RETURNED_DETAIL A\n" );
		sql.append("           WHERE 1 = 1\n" );
		sql.append("             AND A.RETURN_ID = "+roPO.getId()+")");
		
		this.update(sql.toString(), null);
	}
	
	/**
	 * 根据旧件物流单号和经销商级别删除物流单
	 * 注:经销商级别为该级别
	 * @param status 状态
	 * @param logisticsId 物流单ID
	 * @param dealerLevel 经销商级别=dealerLevel
	 */
	public void writeBackReturnOrderStatus(Integer status,Long logisticsId,Integer dealerLevel){
		
		if(status==null || logisticsId==null || dealerLevel==null)
			return;
		
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_RETURNED_ORDER A\n" );
		sql.append("SET A.STATUS = ?\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND EXISTS (SELECT * FROM TR_RETURN_LOGISTICS B\n" );
		sql.append("WHERE B.RETURN_ID = A.ID\n" );
		sql.append("AND B.LOGICTIS_ID = ?)");
		sql.append("AND A.DEALER_LEVEL = ?");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(status);
		paramList.add(logisticsId);
		paramList.add(dealerLevel);
		
		this.update(sql.toString(), paramList);
	}
	
	/**
	 * 根据旧件物流单号和经销商级别删除物流单
	 * 注：经销商级别不为该级别
	 * @param status 状态
	 * @param logisticsId 物流单ID
	 * @param dealerLevel 经销商级别<>dealerLevel
	 */
	public void writeBackReturnOrderStatus2(Integer status,Long logisticsId,Integer dealerLevel){
		
		if(status==null || logisticsId==null || dealerLevel==null)
			return;
		
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_RETURNED_ORDER A\n" );
		sql.append("SET A.STATUS = ?\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND EXISTS (SELECT * FROM TR_RETURN_LOGISTICS B\n" );
		sql.append("WHERE B.RETURN_ID = A.ID\n" );
		sql.append("AND B.LOGICTIS_ID = ?)");
		sql.append("AND (A.DEALER_LEVEL > ?\n");
		sql.append("OR A.DEALER_LEVEL < ?)");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(status);
		paramList.add(logisticsId);
		paramList.add(dealerLevel);
		paramList.add(dealerLevel);
		
		this.update(sql.toString(), paramList);
	}
	
	/**
	 * Iverson add By 2010-11-22 
	 * 取出最大的时间
	 * @param dealerId
	 * @return
	 */
	public String getMaxDate(String dealerId,Long yieldly) throws Exception{
		String time="";
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select max(subStr(a.wr_start_date,0,10)) as wr_start_date from Tt_As_Wr_Returned_Order a where a.return_type=10731002 and a.dealer_id =? and a.yieldly=? ");
		List parList=new ArrayList();
		parList.add(dealerId);
		parList.add(yieldly);
		List<Map<String,Object>> rsList= pageQuery(sqlStr.toString(), parList, getFunName());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(rsList.size()<=0){
			TmBusinessParaPO po = new TmBusinessParaPO();
			po.setTypeCode(Constant.TYPE_CODE);
			TmBusinessParaPO poValue = (TmBusinessParaPO)this.select(po).get(0);
			time=poValue.getParaValue();
			time=time.substring(11, time.length());
		}else{
			Date date= new Date();
			int count=0;
			for(Map map:rsList){
				String timeStr=(String)map.get("WR_START_DATE");
				if(count>0){
					Date date2=sdf.parse(timeStr);
					if(date.before(date2)){
						date=date2;
					}
				}
				date=sdf.parse(timeStr);
				count++;
			}
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.set(Calendar.DATE,c.get(Calendar.DATE)-0);  
			time=sdf.format(c.getTime());
		}
		return time;
	}
	
	public List<TtAsWrReturnedOrderPO> getDetailS(String time,String dealerId,Long yieldly){
		
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" SELECT * FROM Tt_As_Wr_Returned_Order o WHERE 1=1 and o.return_Type=10731002 and o.yieldly="+yieldly+" AND  substr(o.wr_start_date,0,10)>'"+time+"' and o.dealer_id="+dealerId+"\n");
		System.out.println(sqlStr);
		return this.select(TtAsWrReturnedOrderPO.class, sqlStr.toString(), null);
	}
	/**
	 * Iverson add By 2010-11-22 
	 * 取出最大的时间
	 * @param dealerId
	 * @return
	 */
	public String getMaxDate2(String dealerId,Integer yieldly) throws Exception{
		String time="";
		StringBuffer sqlStr=new StringBuffer();
		sqlStr.append("select subStr(a.wr_start_date,12,length(a.wr_start_date)) as wr_start_date from Tt_As_Wr_Returned_Order a where a.return_type=10731002 and a.dealer_id =? and a.yieldly=? ");
		List parList=new ArrayList();
		parList.add(dealerId);
		parList.add(yieldly);
		List<Map<String,Object>> rsList= pageQuery(sqlStr.toString(), parList, getFunName());

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=null;
		int count=0;
		for(Map map:rsList){
			String timeStr=(String)map.get("WR_START_DATE");
			if(count>0){
				Date date2=sdf.parse(timeStr);
				if(date.before(date2)){
					date=date2;
				}
			}
			date=sdf.parse(timeStr);
			count++;
		}
		time=sdf.format(date);
		
		return time;
	}
	
	//Iverson add By 2010-11-23 根据Id查询装箱单号(去掉重复和空)
	public List<Map<String, Object>> getBoxNo(long id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct a.box_no,a.return_id as id from tt_as_wr_returned_order_detail a where a.return_id='"+id+"' and a.is_sign=0 and a.box_no is not null order by length(box_no) asc,box_no \n");
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	public List<Map<String, Object>> getCode() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT c.code_id,c.code_desc FROM Tc_Code  c  WHERE 1=1  AND Type='"+Constant.OLDPART_DEDUCT_TYPE+"' \n");
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	public List<Map<String, Object>> getClaimList(String starDate,String enDate,String dealerId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.sub_date FROM Tt_As_Wr_Application a\n");
		sql.append(" WHERE a.status IN ( "+Constant.CLAIM_APPLY_ORD_TYPE_01+","+Constant.CLAIM_APPLY_ORD_TYPE_03+")\n");
		sql.append(" and  1=1 \n");
		sql.append("AND a.dealer_id = "+dealerId+"\n");
		sql.append("AND a.report_date>=to_date('"+starDate+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		sql.append("AND a.report_date<=to_date('"+enDate+" 23:59:59','yyyy-mm-dd hh24:mi:ss')"); 

		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	//yyh 2012-4-17 根据id和箱号统计
	public List<Map<String, Object>> getBoxNo1(long id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(a.is_upload) as is_upload,count(a.deductible_reason_code) as reason_num,count(a.return_amount) as return_amount,a.box_no, a.return_id,a.is_storage   from Tt_As_Wr_Old_Returned_Detail a where a.return_id = '"+id+"'   and a.is_sign = 0  and a.box_no is not null  GROUP BY a.box_no, a.return_id,a.is_storage\n");
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	public List<TtAsWrReturnedOrderDetailPO> queryOrderDetail(String sqlStr,int pageSize,int curPage ){
		return this.select(TtAsWrReturnedOrderDetailPO.class, sqlStr.toString(), null);
	}
	
	//Iverson add By 2010-11-23 根据Id查询装箱单明细
	public List<Map<String, Object>> getBoxNoDetail(String id,String boxNo) {
		StringBuffer sql = new StringBuffer();
		sql.append("select taword.remark as remarks,taword.id,taword.claim_no,taword.vin,c.id as claim_id,taword.PRODUCER_NAME,to_char(c.RO_STARTDATE,'yyyy-mm-dd') as RO_STARTDATE,c.REMARK,del.dealer_code,");
				sql.append("tawp.part_code,tawp.part_name,taword.is_upload,tc1.CODE_DESC as deductible_reason_code,taword.is_storage,taword.barcode_no,");
				sql.append("taword.box_no,taword.warehouse_region,taword.deduct_remark ");
				sql.append("from tt_as_wr_old_returned tawor,");
				sql.append("tt_as_wr_old_returned_detail taword,");
				sql.append("tt_as_wr_partsitem tawp,tc_code tc,TT_AS_WR_APPLICATION c,tm_dealer del,tc_code tc1");
				sql.append(" where tawor.id=taword.return_id and taword.part_id=tawp.part_id and taword.deduct_remark=tc.code_id(+)");
				sql.append(" and taword.is_sign=0 and tawor.id="+id+" and c.id=taword.claim_id and del.dealer_id=c.dealer_id");
				sql.append(" and taword.box_no='"+boxNo+"' and taword.deductible_reason_code=tc1.code_id(+)");
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	//查询不索赔是否有需要出库的数据
	public Long isStock(String out_start_date,String out_end_date){
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select count(a.id) as count  From tt_as_wr_barcode_part_stock a where a.is_library =0   and a.is_cliam = 10011001   and a.status = 10011001 " );
		if(Utility.testString(out_start_date)){
			sqlStr.append(" and a.CREATE_DATE>=to_date('" + out_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(Utility.testString(out_end_date)){
			sqlStr.append(" and a.CREATE_DATE<=to_date('" + out_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		
		PageResult<TmAsWrBarcodePartStockPO> pr=pageQuery(TmAsWrBarcodePartStockPO.class, sqlStr.toString(), null, 10, 1);
		List<TmAsWrBarcodePartStockPO> ls=pr.getRecords();
		return ls.get(0).getCount();
	}
	
	//查询二次抵扣是否有需要出库的数据
	public Long isOffsetStock(String out_start_date,String out_end_date){
		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select   count(c.id) as count      From tt_as_wr_deduct_detail       a, tt_as_wr_old_returned_detail b,       tt_as_wr_barcode_part_stock  c,       tt_as_wr_deduct d where a.part_id = b.id   and b.barcode_no = c.barcode_no   and c.is_library =0  and c.is_cliam =10011002   and d.id=a.deduct_id   and c.status = 10011001   and d.is_count=2   " );
		if(Utility.testString(out_start_date)){
			sqlStr.append(" and c.CREATE_DATE>=to_date('" + out_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(Utility.testString(out_end_date)){
			sqlStr.append(" and c.CREATE_DATE<=to_date('" + out_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		
		PageResult<TmAsWrBarcodePartStockPO> pr=pageQuery(TmAsWrBarcodePartStockPO.class, sqlStr.toString(), null, 10, 1);
		List<TmAsWrBarcodePartStockPO> ls=pr.getRecords();
		return ls.get(0).getCount();
	}
	
	//不索赔的库存出库
	public void notCliamStockInfo(String stockNo,String out_start_date,String out_end_date,Long userId) throws ParseException{
		String Id = SequenceManager.getSequence("");
    	
    		
    		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select a.part_id,a.part_name,a.part_code,a.barcode_No   From tt_as_wr_barcode_part_stock a where a.is_library =0   and a.is_cliam = 10011001   and a.status = 10011001 " );
		if(Utility.testString(out_start_date)){
			sqlStr.append(" and a.CREATE_DATE>=to_date('" + out_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(Utility.testString(out_end_date)){
			sqlStr.append(" and a.CREATE_DATE<=to_date('" + out_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		
		
		PageResult<TmAsWrBarcodePartStockPO> pr=pageQuery(TmAsWrBarcodePartStockPO.class, sqlStr.toString(), null, 10, 1);
		List<TmAsWrBarcodePartStockPO> ls=pr.getRecords();
		
		//1。新增出库单主表
		TtDeliveryOrderPO tdop=new TtDeliveryOrderPO();
		
		tdop.setStockId(Long.valueOf(Id));
		tdop.setStockNo(stockNo);
		tdop.setStockDate(new Date());
		tdop.setCreateBy(userId);
		tdop.setCreateDate(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		Date starDate=sdf.parse(out_start_date); 
		tdop.setStarTime(starDate);
		Date endDate=sdf.parse(out_end_date); 
		tdop.setEndTime(endDate);
		tdop.setStockNumber(Long.valueOf(ls.size()));
		tdop.setStockType(Long.valueOf(Constant.Stock_type_1));
		tdop.setStockState(Long.valueOf(Constant.IF_TYPE_YES));
		
		this.insert(tdop);
		//2.新增出库单明细表
		

		for(int i=0;i<ls.size();i++){
			TtDeliveryOrderDetailPO tdodp=new TtDeliveryOrderDetailPO();
			String Id1 = SequenceManager.getSequence("");
			tdodp.setId(Long.valueOf(Id1));
			tdodp.setStockId(Long.valueOf(Id));
			tdodp.setPartCode(ls.get(i).getPartCode().toString());
			tdodp.setPartName(ls.get(i).getPartName().toString());
			tdodp.setPartId(ls.get(i).getPartId());
			tdodp.setState(Long.valueOf(Constant.Entry_type_1));
			tdodp.setBarcodeNo(ls.get(i).getBarcodeNo());
			tdodp.setCreateBy(userId);
			tdodp.setCreateDate(new Date());
			this.insert(tdodp);
		}
		
		//3.更新库存表
		
		String sql1="update  tt_as_wr_barcode_part_stock a set a.is_library="+Constant.STATUS_ENABLE+",stock_type="+Constant.Stock_type_3+",a.update_by="+userId+",a.update_date=sysdate,a.stock_date=sysdate where a.is_library is null   and a.is_cliam = 10011001   and a.status = 10011001 and a.CREATE_DATE>=to_date('" + out_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss') and a.CREATE_DATE<=to_date('" + out_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')"; 
		
		this.update(sql1, null);
		
	}
	


	//不索赔的库存出库
	public void offsetStockInfo(String stockNo,String out_start_date,String out_end_date,Long userId) throws ParseException{
		String Id = SequenceManager.getSequence("");
    	
    		
    		StringBuffer sqlStr= new StringBuffer();
		sqlStr.append("select c.part_id,       c.barcode_no,       c.part_name,       c.part_code,       c.create_date      From tt_as_wr_deduct_detail       a, tt_as_wr_old_returned_detail b,       tt_as_wr_barcode_part_stock  c,       tt_as_wr_deduct d where a.part_id = b.id   and b.barcode_no = c.barcode_no   and c.is_library =0   and c.is_cliam =10011002   and d.id=a.deduct_id   and c.status = 10011001   and d.is_count=2    " );
		if(Utility.testString(out_start_date)){
			sqlStr.append(" and c.CREATE_DATE>=to_date('" + out_start_date+ " 00:00:00','YYYY-MM-DD HH24:mi:ss')\n");
		}
		if(Utility.testString(out_end_date)){
			sqlStr.append(" and c.CREATE_DATE<=to_date('" + out_end_date+ " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");
		}
		
		
		PageResult<TmAsWrBarcodePartStockPO> pr=pageQuery(TmAsWrBarcodePartStockPO.class, sqlStr.toString(), null, 10, 1);
		List<TmAsWrBarcodePartStockPO> ls=pr.getRecords();
		
		//1。新增出库单主表
		TtDeliveryOrderPO tdop=new TtDeliveryOrderPO();
		
		tdop.setStockId(Long.valueOf(Id));
		tdop.setStockNo(stockNo);
		tdop.setStockDate(new Date());
		tdop.setCreateBy(userId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		Date starDate=sdf.parse(out_start_date); 
		tdop.setStarTime(starDate);
		Date endDate=sdf.parse(out_end_date); 
		tdop.setEndTime(endDate);
		tdop.setCreateDate(new Date());
		tdop.setStockNumber(Long.valueOf(ls.size()));
		tdop.setStockType(Long.valueOf(Constant.Stock_type_2));
		tdop.setStockState(Long.valueOf(Constant.IF_TYPE_YES));
		
		this.insert(tdop);
		//2.新增出库单明细表
		

		for(int i=0;i<ls.size();i++){
			TtDeliveryOrderDetailPO tdodp=new TtDeliveryOrderDetailPO();
			String Id1 = SequenceManager.getSequence("");
			tdodp.setId(Long.valueOf(Id1));
			tdodp.setStockId(Long.valueOf(Id));
			tdodp.setPartCode(ls.get(i).getPartCode().toString());
			tdodp.setPartName(ls.get(i).getPartName().toString());
			tdodp.setPartId(ls.get(i).getPartId());
			tdodp.setState(Long.valueOf(Constant.Entry_type_1));
			tdodp.setBarcodeNo(ls.get(i).getBarcodeNo());
			tdodp.setCreateBy(userId);
			tdodp.setCreateDate(new Date());
			this.insert(tdodp);
			
			
			//3.更新库存表
			
			String sql1="update  tt_as_wr_barcode_part_stock a set a.is_library="+Constant.STATUS_ENABLE+",stock_type="+Constant.Stock_type_3+",a.update_by="+userId+",a.update_date=sysdate,a.stock_date=sysdate where a.barcode_no="+ls.get(i).getBarcodeNo();
			this.update(sql1, null);
		}

	}
	
	//查询出库单明细
	 public List<Map<String,Object>> queryClaimDetail(String stockId){
	    	StringBuffer sql= new StringBuffer();
	    	sql.append("select d.erpd_code,a.stock_id,(select GROUP_NAME from TM_VHCL_MATERIAL_GROUP where group_id=(select  series_id from Tt_As_Wr_Application where id=(select id From tt_as_wr_partsitem g where g.part_id=(select d.part_id From Tt_As_Wr_Barcode_Part_Stock d where d.part_code=b.part_code and rownum<=1) ))) as series_name,a.stock_no,c.code_desc as stock_type ,TO_CHAR(a.stock_date,'YYYY-MM-DD') AS STOCK_DATE,b.part_name,b.part_code,count(a.stock_no) AS COUNT From Tt_Delivery_Order a,Tt_Delivery_Order_Detail b,tc_code c,TM_PT_PART_BASE d where a.stock_id=b.stock_id and b.part_code=d.part_code and c.code_id=a.stock_type and a.stock_id="+stockId+" group by  d.erpd_code,a.stock_id,a.stock_no,c.code_desc ,a.stock_date,b.part_name,b.part_code" );
	    	
	    
	    	return this.pageQuery(sql.toString(), null, this.getFunName());
	    } 
	 
	
	 public List<Map<String,Object>> queryJD(String yieldly){
	    	StringBuffer sql= new StringBuffer();
	    	sql.append("select CODE_DESC from tc_code where code_id="+yieldly );
	    	
	    
	    	return this.pageQuery(sql.toString(), null, this.getFunName());
	    } 
	 
	 public String  queryJW(){
		 //查询属于轿车或者微车
    	 TcCodePO tcp=new TcCodePO();
    	 tcp.setType(String.valueOf(Constant.chana));
    	List<TcCodePO> ls4= this.select(tcp);
    	TcCodePO tcp1=new TcCodePO();
    	tcp1= ls4.get(0);
    	String codeId= tcp1.getCodeId();
    	String jw="";
    	if(codeId.equals(String.valueOf(Constant.chana_jc))){
    		jw="轿车";
    		
    	}
    	else{
    		
    		jw= "微车";
    	}
    	return jw;
	    } 
	//查询出库单明细
	 public List<Map<String,Object>> queryClaimDetail111(String stockId,String erpdCode,String partCode){
	    	StringBuffer sql= new StringBuffer();
	    	sql.append("select d.erpd_code,a.stock_id,a.stock_no,c.code_desc as stock_type ,TO_CHAR(a.stock_date,'YYYY-MM-DD') AS STOCK_DATE,b.part_name,b.part_code,count(a.stock_no) AS COUNT From Tt_Delivery_Order a,Tt_Delivery_Order_Detail b,tc_code c,TM_PT_PART_BASE d where a.stock_id=b.stock_id and b.part_code=d.part_code and c.code_id=a.stock_type and a.stock_id="+stockId+" " );
	    	if(Utility.testString(erpdCode)){
	    		
	    		sql.append(" and d.erpd_code like '%"+erpdCode+"%'");
	    	}
if(Utility.testString(partCode)){
	    		
	    		sql.append(" and b.part_code like '%"+partCode+"%'");
	    	}
sql.append(" group by  d.erpd_code,a.stock_id,a.stock_no,c.code_desc ,a.stock_date,b.part_name,b.part_code");
	    	return this.pageQuery(sql.toString(), null, this.getFunName());
	    } 
	 

		//查询出库单明细
		 public List<Map<String,Object>> notScanningDetail(String stockId){
		    	StringBuffer sql= new StringBuffer();
		    	sql.append("select distinct a.barcode_no From TT_BARCODE_INTERFACE_STOCK_D a where a.stock_id= "+stockId+" and a.is_handle is null");
		    	
		    
		    	return this.pageQuery(sql.toString(), null, this.getFunName());
		    } 
	//查询出库单明细
	 public List<Map<String,Object>> queryClaimDetail11(String stockId){
	    	StringBuffer sql= new StringBuffer();
	    	sql.append("select D.ERPD_CODE,a.stock_id,a.stock_no, (select GROUP_NAME from TM_VHCL_MATERIAL_GROUP where group_id=(select  series_id from Tt_As_Wr_Application where id=(select id From tt_as_wr_partsitem g where g.part_id=(select d.part_id From Tt_As_Wr_Barcode_Part_Stock d where d.part_code=b.part_code and rownum<=1) ))) as series_name,c.code_desc as stock_type ,TO_CHAR(a.stock_date,'YYYY-MM-DD') AS STOCK_DATE,b.part_name,b.part_code,count(a.stock_no) AS COUNT From Tt_Delivery_Order a,Tt_Delivery_Order_Detail b,tc_code c,TM_PT_PART_BASE D where B.PART_CODE=D.PART_CODE AND  a.stock_id=b.stock_id and c.code_id=a.stock_type and a.stock_id="+stockId+" group by  D.ERPD_CODE,a.stock_id,a.stock_no,c.code_desc ,a.stock_date,b.part_name,b.part_code" );
	    //	sql.append(" select D.ERPD_CODE,       a.stock_id,       a.stock_no,       wa.series_name        ,         c.code_desc as stock_type,       TO_CHAR(a.stock_date, 'YYYY-MM-DD') AS STOCK_DATE,       b.part_name,       b.part_code,l.id,");
		    
	    	//sql.append("  count(a.stock_no) AS COUNT,sum(l.labour_hours) as labour_hours From Tt_Delivery_Order        a,       Tt_Delivery_Order_Detail b,       tc_code                  c,      TM_PT_PART_BASE          D,Tt_As_Wr_Partsitem p,Tt_As_Wr_Labouritem l,Tt_As_Wr_Application wa where B.PART_CODE = D.PART_CODE  AND a.stock_id = b.stock_id   and c.code_id = a.stock_type and wa.id=l.id    and a.stock_id = "+stockId+" and b.part_id=p.part_id and p.id=l.id and p.wr_labourcode=l.wr_labourcode group by D.ERPD_CODE,          a.stock_id,          a.stock_no,wa.series_name        ,           c.code_desc,          a.stock_date,          b.part_name,          b.part_code,l.id          ");
		    
	    
	    	return this.pageQuery(sql.toString(), null, this.getFunName());
	    } 
	 
	 
	//查询出库单明细
	 public List<Map<String,Object>> queryClaimDetail1(String stockId){
	    	StringBuffer sql= new StringBuffer();
	    	sql.append("select to_char(STOCK_DATE,'yyyy-mm-dd') as STOCK_DATE,to_char(end_time,'yyyy-mm-dd') as end_time,remark,to_char(star_time,'yyyy-mm-dd') as star_time,STOCK_NO,SUPPLIER_NAME,SUPPLIER_CODE FROM Tt_Delivery_Order WHERE STOCK_ID="+stockId );
	    	
	    
	    	return this.pageQuery(sql.toString(), null, this.getFunName());
	    } 
	 
		//查询补录明细
	 public List<Map<String,Object>> querybulumDetail1(String stockId,String partCode,String partName){
			StringBuffer sql= new StringBuffer();
	    	sql.append("select d.erpd_code,a.stock_id,a.stock_no,c.code_desc as stock_type ,TO_CHAR(a.stock_date,'YYYY-MM-DD') AS STOCK_DATE,b.part_name,b.part_code,b.id,to_char(b.barcode_no) as BAR_NO From Tt_Delivery_Order a,Tt_Delivery_Order_Detail b,tc_code c,TM_PT_PART_BASE D where b.part_code=d.part_code and  a.stock_id=b.stock_id and c.code_id=a.stock_type and a.stock_id="+stockId+" and b.part_code='"+partCode+"' and b.part_name='"+partName+"'" );
	    	
	    
	    	return this.pageQuery(sql.toString(), null, this.getFunName());
	    } 
		//查询补录明细
	 public List<Map<String,Object>> querybulumDetail11(String stockId,String partCode,String barcodeNo){
			StringBuffer sql= new StringBuffer();
	    	sql.append("select d.erpd_code,a.stock_id,a.stock_no,c.code_desc as stock_type ,TO_CHAR(a.stock_date,'YYYY-MM-DD') AS STOCK_DATE,b.part_name,b.part_code,b.id,to_char(b.barcode_no) as BAR_NO From Tt_Delivery_Order a,Tt_Delivery_Order_Detail b,tc_code c,TM_PT_PART_BASE D where b.part_code=d.part_code and  a.stock_id=b.stock_id and c.code_id=a.stock_type and b.barcode_no='"+barcodeNo+"' and a.stock_id="+stockId+" and b.part_code='"+partCode+"'" );
	    	
	    
	    	return this.pageQuery(sql.toString(), null, this.getFunName());
	    } 
	 //删除补录
	 public void deleteBulu(String id){
		 StringBuffer sql= new StringBuffer();
		 
		 sql.append("delete from TT_Delivery_order_Detail where id="+id);
		 
		 this.delete(sql.toString(),null);
		 
	 }
	 
	 //补录
	 public void addBuluInfo(String stockId,String partCode,String partName,String num,Long userId,String yieldly){
		
	
		 
		 for(int i=0;i<Integer.valueOf(num);i++){
			 String Id1 = SequenceManager.getSequence("");
		 TtDeliveryOrderDetailPO tdod=new TtDeliveryOrderDetailPO();
		 
		 
		 tdod.setId(Long.valueOf(Id1));
		 tdod.setStockId(Long.valueOf(stockId));
		 tdod.setPartCode(partCode);
		 tdod.setPartName(partName);
		 tdod.setPlaceNo(Long.valueOf(yieldly));
		 tdod.setCreateBy(userId);
		 tdod.setState(Long.valueOf(Constant.Entry_type_1));
		 tdod.setCreateDate(new Date());
		 insert(tdod);
		 }
		 
		 
	 }
	 
	 //生成出库单
	 public void addBuluQueDing(String stockId,String SUPPLIER_ID,String SUPPLIER_CODE,Long userId,String remark){
		 
		 
		 	TmPtSupplierPO tps=new TmPtSupplierPO();
			tps.setSupplierId(Long.valueOf(SUPPLIER_ID));
			
			List<TmPtSupplierPO> ls=this.select(tps);
			
			String SUPPLIER_Name=ls.get(0).getSupplierName();
			
			TtDeliveryOrderDetailPO tdod=new TtDeliveryOrderDetailPO();
			
			tdod.setStockId(Long.valueOf(stockId));
			List<TtDeliveryOrderDetailPO> ls1=this.select(tdod);
			String sql="";
			
			if(remark==null||remark==""||remark.equals("")||remark.equals(null)){
			sql="update tt_delivery_order  set STOCK_STATE="+Constant.IF_TYPE_YES+",Stock_Date=sysdate,Stock_Number="+ls1.size()+",Supplier_Id="+SUPPLIER_ID+",Supplier_Code='"+SUPPLIER_CODE+"',Supplier_Name='"+SUPPLIER_Name+"',Update_By="+userId+",Update_Date=sysdate where stock_id="+stockId;
				
			}
			
			else{
			sql="update tt_delivery_order  set STOCK_STATE="+Constant.IF_TYPE_YES+",Stock_Date=sysdate,remark='"+remark+"',Stock_Number="+ls1.size()+",Supplier_Id="+SUPPLIER_ID+",Supplier_Code='"+SUPPLIER_CODE+"',Supplier_Name='"+SUPPLIER_Name+"',Update_By="+userId+",Update_Date=sysdate where stock_id="+stockId;
				
			}
		
			
			this.update(sql, null);
			
			String sql1="update tt_as_wr_barcode_part_stock a   set a.is_library  = 10011001,stock_type="+Constant.Stock_type_3+",       a.stock_date  = sysdate,       a.update_date = sysdate,       a.update_by   = "+userId+"  where a.barcode_no in        (select d.barcode_no          From tt_delivery_order_detail c, tt_as_wr_barcode_part_stock d         where c.stock_id = "+stockId+"           and c.barcode_no is not null           and d.barcode_no = c.barcode_no           and d.is_library is null)";
			
			
			
			this.update(sql1, null);
			
			
	 }
	 
	 public void ScanningDelete(String stockId){
		 String sql="delete tt_delivery_order_detail where stock_id="+stockId;
		 
		 String sql1="delete tt_delivery_order where stock_id="+stockId;
		 
		 this.delete(sql, null);
		 this.delete(sql1, null);
		 
	 }
	 
	 public PageResult<TmPtPartBasePO> queryPartBaseList(Map params,int curPage, int pageSize){
			String part_name=ClaimTools.dealParamStr(params.get("part_name"));
			String part_code=ClaimTools.dealParamStr(params.get("part_code"));
			String erpd_code=ClaimTools.dealParamStr(params.get("erpd_code"));
			String isCliam=ClaimTools.dealParamStr(params.get("isCliam"));
		
			
			StringBuffer sqlStr= new StringBuffer();
			sqlStr.append("select a.is_cliam,part_id,a.part_name,part_code,erpd_code,(CASE WHEN a.is_cliam = 10011001 THEN '不索赔' ELSE '索赔' END) as is_cliams From tm_pt_part_base a where 1=1  " );
		
			
			
			if(Utility.testString(part_name)){
				sqlStr.append(" and a.part_name like'%"+part_name.replaceAll("'", "\''")+"%'\n" );
			}
			
			if(Utility.testString(isCliam)){
				if(isCliam.equals("yes")){
					sqlStr.append(" and a.is_cliam ="+Constant.STATUS_ENABLE+" " );
					
				}
				else{
					sqlStr.append(" and a.is_cliam  ="+Constant.STATUS_DISABLE+" " );
				}
				
			}
			
			
			if(Utility.testString(part_code)){
				sqlStr.append(" and a.part_code like'%"+part_code.replaceAll("'", "\''")+"%'\n" );
			}
			
			if(Utility.testString(erpd_code)){
				sqlStr.append(" and a.erpd_code like'%"+erpd_code.replaceAll("'", "\''")+"%'\n" );
			}
			
			PageResult<TmPtPartBasePO> pr=pageQuery(TmPtPartBasePO.class, sqlStr.toString(), null, pageSize, curPage);
			return pr;
		}
	 
	 public void queryPartBaseUpdate(String partId,String isCliam){
		 String sql="";
		 if(isCliam.equals(Constant.STATUS_ENABLE.toString())||isCliam==Constant.STATUS_ENABLE.toString()){
			 sql="update tm_pt_part_base set is_cliam="+Constant.STATUS_DISABLE+" where part_id="+partId;
			 
		 }
		 else{
			 sql="update tm_pt_part_base set is_cliam="+Constant.STATUS_ENABLE+" where part_id="+partId;
			 
		 }
		 update(sql, null);
	 }
	 public List<TmOldpartTransportPO> queryGetTransPList(Long dealerId){
		 StringBuffer sql  = new StringBuffer();
		 sql.append("select t.* from tm_oldpart_transport t\n");
		 sql.append("WHERE t.check_date IS NOT NULL\n");
		 sql.append("AND t.transport_status="+Constant.SP_JJ_TRANSPORT_STATUS_03+"\n");
		 sql.append("AND t.dealer_id="+dealerId+"\n");
		 sql.append("order by t.check_date DESC \n");
		return this.select(TmOldpartTransportPO.class, sql.toString(), null);
	}
	 public  List<Map<String, Object>> getStr(Long id ){
		 List<Map<String, Object>> list = null;
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT d.detail_id,d.transport_name\n");
			sql.append(" FROM tm_oldpart_transport_detail d\n");
			sql.append("WHERE   d.status=1 and d.transport_id="+id); 
			
			list=super.pageQuery(sql.toString(), null, getFunName());
			return list;
		}
	public List<Map<String, Object>> getStr(List<TmOldpartTransportPO> sList, String return_type) {
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT d.detail_id,d.transport_name\n");
		sql.append(" FROM tm_oldpart_transport_detail d\n");
		sql.append("WHERE   d.status=1 "); 
		DaoFactory.getsql(sql, "d.return_Type", return_type, 1);
		if(sList.size()>0){
			sql.append(" and d.transport_id in (");
		}
		int size = sList.size();
		for (int i = 0; i < size; i++) {
			if(size-1==i){
				sql.append(sList.get(i).getTransportId()+"");
			}else{
				sql.append(sList.get(i).getTransportId()+",");
			}
		}
		sql.append(")");
		list=super.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public Map<String, Object> finddatabyreturnno(String return_no) {
		Map<String, Object> map  = null;
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select  bor.borrow_person,bb.DEALER_ID,tawor.borrow_no as borrow_no,bor.borrow_phone,bor.require_date,bb.price,bb.auth_price auth_price,\n");
		sqlStr.append(" bb.id as return_id,bb.return_no,bb.PARKAGE_AMOUNT,bb.CREATE_DATE,bb.arrive_date,bb.status as old_status,bb.return_date,tawor.wr_amount,tawor.part_item_amount,\n");
		sqlStr.append("tawor.part_amount,tawor.PART_ITEM_AMOUNT,tawor.dealer_shortname,tc.code_desc status_desc,tawor.status,tawor.top_dealer_id dealer_id,tcc.code_desc yieldly_name,tawor.yieldly,tawor.dealer_level,u1.name auth_person_name,tawor.return_type,u.name sign_name,tawor.WR_START_DATE,tawor.return_end_date,bb.DEALER_NAME,bb.WR_AMOUNT\n");
		sqlStr.append("from  TT_AS_WR_RETURNED_ORDER tawor\n");
		sqlStr.append("  left join TT_AS_PART_BORROW bor on tawor.return_no = bor.return_no\n");
		sqlStr.append("  left join tc_code tcc on tcc.code_id = tawor.yieldly, tr_return_logistics aa,\n");
		sqlStr.append(" tt_as_wr_old_returned bb\n" 	);
		sqlStr.append("  left join tc_user u on u.user_id = bb.sign_person\n");
		sqlStr.append("  left join tc_user u1 on u1.user_id = bb.in_warhouse_by, tm_dealer d,\n");
		sqlStr.append(" tc_code tc\n");
		sqlStr.append(" where tawor.status = tc.code_id\n" );
		sqlStr.append("   and aa.return_id = tawor.id\n" 	);
		sqlStr.append("   and bb.id = aa.logictis_id\n");
		sqlStr.append("   and tawor.dealer_id = d.dealer_id");
		
		sqlStr.append(" and  bb.return_no = '"+return_no+"'");
		map = this.pageQueryMap(sqlStr.toString(), null, getFunName());
		return map;
	}
	
	//旧件抵扣通知单查询
	public PageResult<Map<String, Object>> oldPartDeductionQuery(Map params,int curPage, int pageSize){
		//获取前台条件
		String dealerId=ClaimTools.dealParamStr(params.get("dealerId"));//经销商ID
		String deductionStatus=ClaimTools.dealParamStr(params.get("deductionStatus"));//抵扣状态
		String deductionNo=ClaimTools.dealParamStr(params.get("deductionNo"));//抵扣单号
		String updateDateStart=ClaimTools.dealParamStr(params.get("updateDateStart"));//通知开始日期
		String updateDateEnd=ClaimTools.dealParamStr(params.get("updateDateEnd"));//通知结束日期
		
		StringBuffer sql=new StringBuffer();
		sql.append(" SELECT A.DEDUCTION_ID,\n");
		sql.append("        A.CLAIM_ID,\n");
		sql.append("        A.DEDUCTION_NO,\n");
		sql.append("        A.STATUS,\n");
		sql.append("        NVL(A.PART_DEDUCTION_AMOUNT,0) PART_DEDUCTION_AMOUNT,\n");
		sql.append("        NVL(A.HOURS_DEDUCTION_AMOUNT,0) HOURS_DEDUCTION_AMOUNT,\n");
		sql.append("        NVL(A.OUTWARD_DEDUCTION_AMOUNT,0) OUTWARD_DEDUCTION_AMOUNT,\n");
		sql.append("        NVL(A.PART_DEDUCTION_AMOUNT,0)+NVL(A.HOURS_DEDUCTION_AMOUNT,0)+NVL(A.OUTWARD_DEDUCTION_AMOUNT,0) TOTAL_DEDUCTION_AMOUNT,\n");
		sql.append("        A.BALANCE_NO,\n");
		sql.append("        TO_CHAR(NVL(A.UPDATE_DATE,A.CREATE_DATE), 'YYYY-MM-DD') UPDATE_DATE\n");
		sql.append("   FROM TT_AS_WR_OLDPART_DEDUCTION A\n");
		sql.append("  WHERE 1 = 1\n");
		if(StringUtil.notNull(deductionNo)){
			sql.append("    AND A.DEDUCTION_NO LIKE '%"+deductionNo+"%'\n");
		}
		if(StringUtil.notNull(deductionStatus)){
			sql.append("    AND A.STATUS = "+deductionStatus+"");
		}
		if(StringUtil.notNull(updateDateStart)){
			sql.append("     AND NVL(A.UPDATE_DATE,A.CREATE_DATE)>= TO_DATE('"+updateDateStart+"', 'YYYY-MM-DD')\n");
		}
		if(StringUtil.notNull(updateDateEnd)){
			sql.append("     AND NVL(A.UPDATE_DATE,A.CREATE_DATE)<= TO_DATE('"+updateDateEnd+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("	AND A.DEALER_ID = "+dealerId+"\n");
		sql.append("	ORDER BY A.CREATE_DATE DESC");
		PageResult<Map<String, Object>> pr = pageQuery(sql.toString(), null,getFunName(), pageSize, curPage);
		return pr;
	}
	
	public PageResult<Map<String, Object>> oldPartDeductionInfor(Map<String,Object> params,int curPage, int pageSize) {
		String claimId = CommonUtils.checkNull(params.get("claimId"));
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ID,\n") ;
		sql.append("       APP_CLAIM_ID,\n") ;
		sql.append("       APP_CLAIM_NO,\n") ;
		sql.append("       VIN,\n") ;
		sql.append("       OBJ_TYPE,\n") ;
		sql.append("       OBJ_CODE,\n") ;
		sql.append("       OBJ_NAME,\n") ;
		sql.append("       TO_CHAR(DEDUCTION_AMOUNT,'FM99999990.00') DEDUCTION_AMOUNT,\n") ;
		sql.append("       DEDUCT_REMARK,\n") ;
		sql.append("       OTHER_REMARK\n") ;
		sql.append("  FROM (SELECT C.CLAIM_PART_ID ID,\n") ;
		sql.append("               A.ID APP_CLAIM_ID,\n") ;
		sql.append("               A.APP_CLAIM_NO,\n") ;
		sql.append("               A.VIN,\n") ;
		sql.append("               "+Constant.DEDUCTION_TYPE_01+" OBJ_TYPE,\n") ;
		sql.append("               C.PART_CODE OBJ_CODE,\n") ;
		sql.append("               C.PART_CNAME OBJ_NAME,\n") ;
		sql.append("               NVL(C.SALE_PRICE, 0) DEDUCTION_AMOUNT,\n") ;
		sql.append("               B.DEDUCT_REMARK, --抵扣原因\n") ;
		sql.append("               B.OTHER_REMARK --抵扣备注\n") ;
		sql.append("          FROM TT_AS_WR_APPLICATION_CLAIM A\n") ;
		sql.append("          JOIN TT_AS_WR_RETURNED_ORDER_DETAIL B\n") ;
		sql.append("            ON A.ID = B.CLAIM_ID\n") ;
		sql.append("          JOIN TT_AS_WR_APP_PART C\n") ;
		sql.append("            ON B.CLAIM_PART_ID = C.CLAIM_PART_ID\n") ;
		sql.append("         WHERE 1 = 1\n") ;
		sql.append("           AND B.SIGN_AMOUNT = 0\n") ;
		sql.append("        UNION ALL\n") ;
		sql.append("        SELECT B.CLAIM_PROJECT_ID,\n") ;
		sql.append("               A.ID,\n") ;
		sql.append("               A.APP_CLAIM_NO,\n") ;
		sql.append("               A.VIN,\n") ;
		sql.append("               "+Constant.DEDUCTION_TYPE_02+" OBJ_TYPE,\n") ;
		sql.append("               B.LABOUR_CODE,\n") ;
		sql.append("               B.CN_DES,\n") ;
		sql.append("               NVL(B.HOURS_APPLY_AMOUNT, 0) -\n") ;
		sql.append("               NVL(B.HOURS_SETTLEMENT_AMOUNT, 0),\n") ;
		sql.append("               0 DEDUCT_REMARK,\n") ;
		sql.append("               '' OTHER_REMARK\n") ;
		sql.append("          FROM TT_AS_WR_APPLICATION_CLAIM A\n") ;
		sql.append("          JOIN TT_AS_WR_APP_PROJECT B\n") ;
		sql.append("            ON A.ID = B.APP_CLAIM_ID\n") ;
		sql.append("         WHERE 1 = 1\n") ;
		sql.append("           AND NVL(B.HOURS_APPLY_AMOUNT, 0) -\n") ;
		sql.append("               NVL(B.HOURS_SETTLEMENT_AMOUNT, 0) > 0\n") ;
		sql.append("        UNION ALL\n") ;
		sql.append("        SELECT B.OUT_ID,\n") ;
		sql.append("               A.ID,\n") ;
		sql.append("               A.APP_CLAIM_NO,\n") ;
		sql.append("               A.VIN,\n") ;
		sql.append("               "+Constant.DEDUCTION_TYPE_03+" OBJ_TYPE,\n") ;
		sql.append("               B.FEE_CODE,\n") ;
		sql.append("               B.FEE_NAME,\n") ;
		sql.append("               NVL(B.FEE_PRICE，0) - NVL(B.FEE_SETTLEMENT_PRICE, 0),\n") ;
		sql.append("               0 DEDUCT_REMARK,\n") ;
		sql.append("               '' OTHER_REMARK\n") ;
		sql.append("          FROM TT_AS_WR_APPLICATION_CLAIM A\n") ;
		sql.append("          JOIN TT_AS_WR_APP_OUT B\n") ;
		sql.append("            ON A.ID = B.APPCLAIM_ID\n") ;
		sql.append("         WHERE NVL(B.FEE_PRICE，0) - NVL(B.FEE_SETTLEMENT_PRICE, 0) > 0)\n") ;
		sql.append(" WHERE 1=1\n") ;//APP_CLAIM_ID = 1

        if(!claimId.equals("")){
        	sql.append(" AND APP_CLAIM_ID = "+claimId+"\n");
        }else{
        	sql.append(" AND 1=2\n");
        }
		
        PageResult<Map<String, Object>> pr = pageQuery(sql.toString(), null,getFunName(), pageSize, curPage);
        return pr;
	}

}

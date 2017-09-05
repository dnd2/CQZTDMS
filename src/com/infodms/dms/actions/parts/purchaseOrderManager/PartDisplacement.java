package com.infodms.dms.actions.parts.purchaseOrderManager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.purchaseOrderManager.PartDisplacementDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartBookPO;
import com.infodms.dms.po.TtPartDisplacementRecordPO;
import com.infodms.dms.po.TtPartItemStockPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 配件移位
 * @author 
 * @version 2017-8-22
 * @see 
 * @since 
 * @deprecated
 */
public class PartDisplacement extends BaseImport {
	 public Logger logger = Logger.getLogger(PurchaseArrConfir.class);
	 private static final PartDisplacementDao dao = PartDisplacementDao.getInstance();
		
	 /**
	 * 跳转到配件货位移位初始化页面
	 */
	 private static final String PART_DISPLACEMENT_INIT = "/jsp/parts/purchaseOrderManager/partDisplacementInit.jsp";
	 /**
	 * 跳转到配件货位移位页面
	 */
	 private static final String PART_DISPLACEMENT_MAIN = "/jsp/parts/purchaseOrderManager/partDisplacementMain.jsp";
				
	 /**
	  * 配件货位移位初始化
	  */
	 public void partDisplacementInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("old", CommonUtils.getBefore(new Date()));
			act.setForword(PART_DISPLACEMENT_INIT);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "初始化配件货位移位页面异常!");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	 }

	 /**
	 * 查询可进行货位移位的入库信息
	 */
	public void getPoInInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.getPoInInfo(request,loginUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询入库信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据入库单id查询配件信息
	 */
	public void getInIdInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String inId = CommonUtils.checkNull(request.getParamValue("inId"));
			String whId = CommonUtils.checkNull(request.getParamValue("whId"));
//			Map<String, Object> detailMap = dao.queryPartInIdInfo(inId);
//            request.setAttribute("detailMap", detailMap);
//			List<Map<String, Object>> detailList = dao.queryPartInIdInfo(inId);
//            request.setAttribute("detailList", detailList);
            request.setAttribute("inId", inId);
            request.setAttribute("whId", whId);
			act.setForword(PART_DISPLACEMENT_MAIN);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "根据入库单id查询配件信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void queryInIdInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try{
			String inId = CommonUtils.checkNull(request.getParamValue("inId"));
			//分页方法 begin
			Integer curPage=request.getParamValue("curPage")!=null?Integer.parseInt(request.getParamValue("curPage")):1;//处理当前页
			PageResult<Map<String, Object>> ps = dao.queryInIdInfo(inId,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "根据入库单id查询配件信息异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 保存货位移位
	 */
	@SuppressWarnings("unchecked")
	public void saveDisplacement(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String err = "";
		try{

			String inId = CommonUtils.checkNull(request.getParamValue("inId"));
			String[] cks = request.getParamValues("ck");
			if(cks==null || cks.length==0){
				err = "没有数据，无法保存！";
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
			}
			for (int i = 0; i < cks.length; i++) {
				String partId = cks[i];
				String oldLocId = request.getParamValue("OLD_LOC_ID_"+partId);//原货位id
				String oldLocCode = request.getParamValue("OLD_LOC_CODE_"+partId);//原货位编码
				Long locId = Long.parseLong(request.getParamValue("LOC_ID_"+partId));//移位后货位id
				String locCode = request.getParamValue("LOC_CODE_"+partId);//移位后货位编码
				Long ableDisQty = Long.parseLong(request.getParamValue("ABLE_DIS_QTY_"+partId));//可移位数量
				Long disQty = Long.parseLong(request.getParamValue("DIS_QTY_"+partId));//移位数量
				String remark = request.getParamValue("REMARK_"+partId);//备注
				String whId = request.getParamValue("WH_ID_"+partId);//仓库id
				String orgId = request.getParamValue("ORG_ID_"+partId);//机构id
				String batchNo = request.getParamValue("BATCH_NO_"+partId);//批次
				
				System.out.println("===oldLocId:"+oldLocId+"=====locId:"+locId);
				System.out.println("===ableDisQty:"+ableDisQty+"=====disQty:"+disQty);
				System.out.println("===whId:"+whId+"=====orgId:"+orgId);
				System.out.println("===batchNo:"+batchNo+"=====batchNo:"+batchNo);
				//新增tt_part_book数据，更新tt_part_book表
				TtPartBookPO bookPo=new TtPartBookPO();
				bookPo.setPartId(Long.parseLong(partId));
				bookPo.setWhId(Long.parseLong(whId));
				bookPo.setOrgId(Long.parseLong(orgId));
				bookPo.setLocId(Long.parseLong(oldLocId));
				bookPo.setBatchNo(batchNo);
				TtPartBookPO selBookPo=(TtPartBookPO)dao.select(bookPo).get(0);
				
				TtPartBookPO insBookPo=new TtPartBookPO();
				insBookPo.setBookId(Long.parseLong(SequenceManager.getSequence("")));
				insBookPo.setPartId(selBookPo.getPartId());
				insBookPo.setWhId(selBookPo.getWhId());
				insBookPo.setOrgId(selBookPo.getOrgId());
				insBookPo.setNormalQty(disQty);
				insBookPo.setBookedQty(Long.parseLong("0"));
				insBookPo.setCreateDate(new Date());
				insBookPo.setCreateBy(loginUser.getUserId());
				insBookPo.setLocId(locId);
				insBookPo.setBatchNo(batchNo);
				insBookPo.setState(1);
				insBookPo.setStatus(1);
				dao.insert(insBookPo);
				
				
				TtPartBookPO updBookPo=new TtPartBookPO();
				updBookPo.setNormalQty(selBookPo.getNormalQty()-disQty);
				dao.update(bookPo, updBookPo);
				
				//新增tt_part_item_stock数据，更新tt_part_item_stock
				TtPartItemStockPO itemPo=new TtPartItemStockPO();
				itemPo.setPartId(Long.parseLong(partId));
				itemPo.setWhId(Long.parseLong(whId));
				itemPo.setOrgId(Long.parseLong(orgId));
				itemPo.setLocId(Long.parseLong(oldLocId));
				itemPo.setBatchCode(batchNo);
				TtPartItemStockPO selItemPo=(TtPartItemStockPO)dao.select(itemPo).get(0);
				
				TtPartItemStockPO insItemPo=new TtPartItemStockPO();
				insItemPo.setStockId(Long.parseLong(SequenceManager.getSequence("")));
				insItemPo.setPartId(selItemPo.getPartId());
				insItemPo.setWhId(selItemPo.getWhId());
				insItemPo.setLocId(locId);
				insItemPo.setOrgId(selItemPo.getOrgId());
				insItemPo.setVenderId(selItemPo.getVenderId());
				insItemPo.setBatchCode(selItemPo.getBatchCode());
				insItemPo.setItemQty(disQty);
				insItemPo.setCreateDate(new Date());
				insItemPo.setCreateBy(loginUser.getUserId());
				insItemPo.setState(1);
				insItemPo.setStatus(1);
				insItemPo.setIsLocked(0);
				dao.insert(insItemPo);
				
				TtPartItemStockPO updItemPo=new TtPartItemStockPO();
				updItemPo.setItemQty(selItemPo.getItemQty()-disQty);
				dao.update(itemPo, updItemPo);
				
				//添加tt_part_displacement_record表移位记录
				TtPartDisplacementRecordPO disPo=new TtPartDisplacementRecordPO();
				disPo.setRecordId(Long.parseLong(SequenceManager.getSequence("")));
				disPo.setPartId(Long.parseLong(partId));
				disPo.setWhId(Long.parseLong(whId));
				disPo.setOrgId(loginUser.getOrgId());
				disPo.setLocId(Long.parseLong(oldLocId));
				disPo.setLocCode(oldLocCode);
				disPo.setNewLocId(locId);
				disPo.setNewLocCode(locCode);
				disPo.setAbleDisQty(ableDisQty);
				disPo.setDisQty(disQty);
				disPo.setCreateDate(new Date());
				disPo.setCreateBy(loginUser.getUserId());
				disPo.setState(10011001);
				disPo.setRemark(remark);
				disPo.setBatchNo(batchNo);
				disPo.setInId(Long.parseLong(inId));
				dao.insert(disPo);
			}
			if(StringUtil.notNull(err)){
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
			}
			
			act.setOutData("success", "移位成功！");
		}catch (Exception e) {//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "货位移位保存异常");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
}

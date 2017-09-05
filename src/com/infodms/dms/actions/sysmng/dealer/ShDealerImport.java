/**
 * 
 */
package com.infodms.dms.actions.sysmng.dealer;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.ValidateUtil;
import com.infodms.dms.common.component.dict.CodeDict;
import com.infodms.dms.dao.sales.dealer.ShDealerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerDetailPO;
import com.infodms.dms.po.TmDealerOrgRelationPO;
import com.infodms.dms.po.TmDealerOrgRelationSecendPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmDealerSecendPO;
import com.infodms.dms.po.TmpShDealerAddressPO;
import com.infodms.dms.po.TmpShDealerPO;
import com.infodms.dms.po.TmpShDealerSecendPO;
import com.infodms.dms.po.TtPartAddrDefinePO;
import com.infodms.dms.po.TtPartAddressMorePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;

import flex.messaging.io.ArrayList;

/**
 * @author ranj
 *
 */
public class ShDealerImport extends BaseImport {

	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String shDealerImportInitUrl = "/jsp/systemMng/dealer/shDealerImportInit.jsp";//销售经销商导入初始化页面
	private final String sh2ndDealerImportInitUrl = "/jsp/systemMng/dealer/sh2ndDealerImportInit.jsp";
	private final String shDealerAddressImportInitUrl = "/jsp/systemMng/dealer/shDealerAddressImportInit.jsp";//销售经销商导入初始化页面
	private final String shDealerImportSuccessUrl = "/jsp/systemMng/dealer/shDealerImportSuccess.jsp";
	private final String shDealerImportSuccessUrl2nd = "/jsp/systemMng/dealer/shDealerImportSuccess2nd.jsp";
	private final String shDealerAddressImportSuccessUrl = "/jsp/systemMng/dealer/shDealerAddressImportSuccess.jsp";
	private final String shDealerImportFailureUrl = "/jsp/systemMng/dealer/shDealerImportFailure.jsp";
	private final String shDealerImportCompleteUrl = "/jsp/systemMng/dealer/shDealerImportComplete.jsp";
	private final String shDealerImportCompleteUrl2nd = "/jsp/systemMng/dealer/shDealerImportComplete2nd.jsp";
	private String errInfoStr;
	private Boolean errBo;
	/**
	 * 初始使化导放页面
	 */
	public void shImportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(shDealerImportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void sh2ndImportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(sh2ndDealerImportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 初始化地址导入页面
	 */
	public void shImportAddressInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(shDealerAddressImportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商（销售）导入临时表
	 */
	public void shImportOperate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ShDealerDao dao=ShDealerDao.getInstance();
        try {
			RequestWrapper request = act.getRequest();
			
			TmpShDealerPO po=new TmpShDealerPO();
			//清空临时表中目标年度的数据
			dao.delete(po);
			long maxSize=1024*1024*5;
			//
			insertIntoTmp(request, "uploadFile",82,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(shDealerImportFailureUrl);
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTmpShDealer(list, logonUser.getUserId());
				//校验临时表数据
				List<ExcelErrors> errorList=null;
				errorList=checkData();
				if(null!=errorList){
					act.setOutData("errorList", errorList);
					act.setForword(shDealerImportFailureUrl);
				}else{
					/*String planVer=CommonUtils.checkNull(request.getParamValue("verNo")); //版本号
					// 取得临时表数据
					List<Map<String, Object>> countList = dao.selectTmpShDealerList();
					act.setOutData("countList", countList);*/
					act.setForword(shDealerImportSuccessUrl);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void shImportOperate2nd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ShDealerDao dao=ShDealerDao.getInstance();
        try {
			RequestWrapper request = act.getRequest();
			
			TmpShDealerSecendPO po=new TmpShDealerSecendPO();
			//清空临时表中目标年度的数据
			dao.delete(po);
			long maxSize=1024*1024*5;
			//
			insertIntoTmp(request, "uploadFile",81,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(shDealerImportFailureUrl);
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTmpShDealer2nd(list, logonUser.getUserId());
				//校验临时表数据
				List<ExcelErrors> errorList=null;
				errorList=checkData();
				if(null!=errorList){
					act.setOutData("errorList", errorList);
					act.setForword(shDealerImportFailureUrl);
				}else{
					act.setForword(shDealerImportSuccessUrl2nd);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商（售后）地址导入临时表
	 */
	public void shImportAddressOperate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ShDealerDao dao=ShDealerDao.getInstance();
        try {
			RequestWrapper request = act.getRequest();
			
			TmpShDealerAddressPO po=new TmpShDealerAddressPO();
			//清空临时表中目标年度的数据
			dao.delete(po);
			long maxSize=1024*1024*5;
			//
			insertIntoTmp(request, "uploadFile",8,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(shDealerImportFailureUrl);
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTmpShDealerAddress(list, logonUser.getUserId());
				//校验临时表数据
				List<ExcelErrors> errorList=null;
				errorList=checkData();
				if(null!=errorList){
					act.setOutData("errorList", errorList);
					act.setForword(shDealerImportFailureUrl);
				}else{
					/*String planVer=CommonUtils.checkNull(request.getParamValue("verNo")); //版本号
					// 取得临时表数据
					List<Map<String, Object>> countList = dao.selectTmpShDealerList();
					act.setOutData("countList", countList);*/
					act.setForword(shDealerAddressImportSuccessUrl);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void shImportOperateQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ShDealerDao dao=ShDealerDao.getInstance();
		try {
				Map<String, Object> map = new HashMap<String, Object>();
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.selectTmpShDealerQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后临时表信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void shImportOperateQuery2nd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ShDealerDao dao=ShDealerDao.getInstance();
		try {
				Map<String, Object> map = new HashMap<String, Object>();
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.selectTmpShDealerQuery2nd(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后临时表信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void shImportAddressOperateQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ShDealerDao dao=ShDealerDao.getInstance();
		try {
				Map<String, Object> map = new HashMap<String, Object>();
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.selectTmpShDealerAddressQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后临时表信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/*
	 * 把所有导入记录插入TMP_XS_DEALER
	 */
	private void insertTmpShDealer(List<Map> list,Long userId) throws Exception{
		if(null==list){
			list=new ArrayList();
		}
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			while(it.hasNext()){
				key=(String)it.next();
				Cell[] cells=(Cell[])map.get(key);
				parseCells(key, cells, userId);
			}
		}
		
	}
	
	private void insertTmpShDealer2nd(List<Map> list,Long userId) throws Exception{
		if(null==list){
			list=new ArrayList();
		}
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			while(it.hasNext()){
				key=(String)it.next();
				Cell[] cells=(Cell[])map.get(key);
				parseCells2nd(key, cells, userId);
			}
		}
		
	}
	
	/*
	 * 把所有导入记录插入Tmp_Sh_Dealer_Address
	 */
	private void insertTmpShDealerAddress(List<Map> list,Long userId) throws Exception{
		if(null==list){
			list=new ArrayList();
		}
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			while(it.hasNext()){
				key=(String)it.next();
				Cell[] cells=(Cell[])map.get(key);
				parseCellsForAddress(key, cells, userId);
			}
		}
		
	}
	/*
	 * 每一行插入TMP_XS_DEALER
	 * 数字只截取30位
	 */
	private void parseCells(String rowNum,Cell[] cells,Long userId) throws Exception{
			ShDealerDao dao=ShDealerDao.getInstance();
			TmpShDealerPO po=new TmpShDealerPO();
			po.setDxName(lastNull(1,cells,"").toString());//大区
			po.setDealerOrgId(lastNull(2,cells,"").toString());//所属组织
			po.setProvinceId(lastNull(3,cells,"").toString());//省份
			po.setCityId(lastNull(4,cells,"").toString());//城市
			po.setCounties(lastNull(5,cells,"").toString());//县城
			po.setZipCode(lastNull(6,cells,"").toString());//邮编
			po.setDealerCode(lastNull(7,cells,"").toString());//经销商编码
			po.setDealerType(lastNull(8,cells,"").toString());//经销商级别
			po.setServiceStatus(lastNull(9,cells,"").toString());//信息状态
			
			po.setDealerName(lastNull(10,cells,"").toString());//经销商名称
			po.setDealerShortname(lastNull(11,cells,"").toString());//经销商简称
			po.setParentDealerD(lastNull(12,cells,"").toString());//上级服务商
			po.setDealerRelation(lastNull(13,cells,"").toString());//与一级经销商关系  
			po.setAddress(lastNull(14,cells,"").toString());//企业注册地址
			po.setZccode(lastNull(15,cells,"").toString());//注册证号
			po.setZyScope(lastNull(16,cells,"").toString());//主营范围
			po.setJyScope(lastNull(17,cells,"").toString());//兼营范围
			po.setCompanyZcCode(lastNull(18,cells,"").toString());//组织机构代码
			po.setMainResources(lastNull(19,cells,"").toString());//维修资质
			
			po.setSiteDate(lastNull(20,cells,"").toString());//建厂时间(建站时间)
			po.setLegal(lastNull(21,cells,"").toString());//法人
			po.setUnionType(lastNull(22,cells,"").toString());//单位性质
			po.setFixedCapital(lastNull(23,cells,"").toString());//固定资产（万元）
			po.setRegisteredCapital(lastNull(24,cells,"").toString());//注册资金（万元）/企业注册资金
			po.setPeopleNumber(lastNull(25,cells,"").toString());//人数
			po.setMainArea(lastNull(26,cells,"").toString());//维修车间面积（平方米）
			po.setReceiveArea(lastNull(27,cells,"").toString());//接待室面积（平方米）
			po.setPartsArea(lastNull(28,cells,"").toString());//配件库面积（平方米）
			po.setDepotArea(lastNull(29,cells,"").toString());//停车场面积（平方米）
			po.setOpeningTime(lastNull(30,cells,"").toString());//营业时间
			
			po.setOnlyMonthCount(lastNull(31,cells,"").toString());//月平均维修能力(台次)
			po.setWorkType(lastNull(32,cells,"").toString());//经营类型
			po.setIsFourS(lastNull(33,cells,"").toString());//是否4S店
			po.setImageLevel(lastNull(34,cells,"").toString());//建店类别
			po.setCompanyAddress(lastNull(35,cells,"").toString());//公司地址/销售展厅地址
			po.setIsLowSer(lastNull(36,cells,"").toString());//有无二级服务站
			po.setAuthorizationType(lastNull(37,cells,"").toString());//企业授权类型
			po.setAuthorizationDate(lastNull(38,cells,"").toString());//授权时间
			po.setIsActingBrand(lastNull(39,cells,"").toString());//是否经营其他品牌
			po.setActingBrandName(lastNull(40,cells,"").toString());//代理其它品牌名称
			po.setHotline(lastNull(41,cells,"").toString());//24小时热线
			po.setSerManagerName(lastNull(42,cells,"").toString());//服务经理
			po.setSerManagerTelphone(lastNull(43,cells,"").toString());//服务经理手机
			po.setSerManagerEmail(lastNull(44,cells,"").toString());//服务经理邮箱
			po.setClaimDirectorName(lastNull(45,cells,"").toString());//索赔主管
			po.setClaimDirectorPhone(lastNull(46,cells,"").toString());//索赔主管办公电话
			po.setClaimDirectorTelphone(lastNull(47,cells,"").toString());//索赔主管手机
			po.setClaimDirectorEmail(lastNull(48,cells,"").toString());//索赔主管邮箱
			po.setClaimDirectorFax(lastNull(49,cells,"").toString());//索赔传真
			po.setSerDirectorName(lastNull(50,cells,"").toString());//服务主管
			po.setSerDirectorPhone(lastNull(51,cells,"").toString());//服务主管办公电话
			po.setSerDirectorTelhone(lastNull(52,cells,"").toString());//服务主管手机
			po.setTechnologyDirectorName(lastNull(53,cells,"").toString());//技术主管
			po.setTechnologyDirectorTelphone(lastNull(54,cells,"").toString());//技术主管手机
			po.setFittingsDecName(lastNull(55,cells,"").toString());//配件主管
			po.setFittingsDecTelphone(lastNull(56,cells,"").toString());//配件主管办公电话
			po.setFittingsDecPhone(lastNull(57,cells,"").toString());//配件主管手机
			po.setFittingsDecEmail(lastNull(58,cells,"").toString());//配件主管邮箱
			po.setFittingsDecFax(lastNull(59,cells,"").toString());//配件主管传真
			po.setPartsStoreAmount(lastNull(60,cells,"").toString());//配件储备金额（万元）
			
			po.setBeginBank(lastNull(61,cells,"").toString());//开户行
			po.setErpCode(lastNull(62,cells,"").toString());//开票名称/开票公司名称
			po.setInvoiceAccount(lastNull(63,cells,"").toString());//银行帐号
			po.setInvoicePhone(lastNull(64,cells,"").toString());//开票电话/开票联系人电话
			po.setInvoiceAdd(lastNull(65,cells,"").toString());//开票地址
			po.setTaxpayerNo(lastNull(66,cells,"").toString());//纳税人识别号
			po.setTaxpayerNature(lastNull(67,cells,"").toString());//纳税人性质
			po.setTaxInvoice(lastNull(68,cells,"").toString());//增值税发票
			po.setTaxDisrate(lastNull(69,cells,"").toString());//开票税率
			po.setFinanceManagerName(lastNull(70,cells,"").toString());//财务经理
			po.setFinanceManagerTelphone(lastNull(71,cells,"").toString());//财务经理办公电话
			po.setFinanceManagerPhone(lastNull(72,cells,"").toString());//财务经理手机
			po.setFinanceManagerEmail(lastNull(73,cells,"").toString());//财务经理邮箱
			po.setRemark(lastNull(74,cells,"").toString());//备注
			po.setBalanceLevel(lastNull(75,cells,"").toString());//结算等级
			po.setInvoiceLevel(lastNull(76,cells,"").toString());//开票等级
			po.setSpyMan(lastNull(77,cells,"").toString());//索赔员
			po.setPhone(lastNull(78,cells,"").toString());//服务商电话
			po.setTheAgents(lastNull(79,cells,"").toString());//辐射区域
			
			po.setProxyVehicleType(lastNull(80,cells,"").toString());//代理车型
			po.setImageCLevel(lastNull(81,cells,"").toString());//验收形象等级
			po.setRowNum(Integer.parseInt(rowNum));//行号
	        dao.insert(po);	
	}
	
	private void parseCells2nd(String rowNum,Cell[] cells,Long userId) throws Exception{
		ShDealerDao dao=ShDealerDao.getInstance();
		TmpShDealerSecendPO po=new TmpShDealerSecendPO();
		po.setDxName(lastNull(1,cells,"").toString());//大区
		po.setDealerOrgId(lastNull(2,cells,"").toString());//所属组织
		po.setProvinceId(lastNull(3,cells,"").toString());//省份
		po.setCityId(lastNull(4,cells,"").toString());//城市
		po.setCounties(lastNull(5,cells,"").toString());//县城
		po.setZipCode(lastNull(6,cells,"").toString());//邮编
		po.setDealerCode(lastNull(7,cells,"").toString());//经销商编码
		po.setDealerType(lastNull(8,cells,"").toString());//经销商级别
		po.setServiceStatus(lastNull(9,cells,"").toString());//信息状态
		
		po.setDealerName(lastNull(10,cells,"").toString());//经销商名称
		po.setDealerShortname(lastNull(11,cells,"").toString());//经销商简称
		po.setParentDealerD(lastNull(12,cells,"").toString());//上级服务商
		po.setDealerRelation(lastNull(13,cells,"").toString());//与一级经销商关系  
		po.setAddress(lastNull(14,cells,"").toString());//企业注册地址
		po.setZccode(lastNull(15,cells,"").toString());//注册证号
		po.setZyScope(lastNull(16,cells,"").toString());//主营范围
		po.setJyScope(lastNull(17,cells,"").toString());//兼营范围
		po.setCompanyZcCode(lastNull(18,cells,"").toString());//组织机构代码
		po.setMainResources(lastNull(19,cells,"").toString());//维修资质
		
		po.setSiteDate(lastNull(20,cells,"").toString());//建厂时间(建站时间)
		po.setLegal(lastNull(21,cells,"").toString());//法人
		po.setUnionType(lastNull(22,cells,"").toString());//单位性质
		po.setFixedCapital(lastNull(23,cells,"").toString());//固定资产（万元）
		po.setRegisteredCapital(lastNull(24,cells,"").toString());//注册资金（万元）/企业注册资金
		po.setPeopleNumber(lastNull(25,cells,"").toString());//人数
		po.setMainArea(lastNull(26,cells,"").toString());//维修车间面积（平方米）
		po.setReceiveArea(lastNull(27,cells,"").toString());//接待室面积（平方米）
		po.setPartsArea(lastNull(28,cells,"").toString());//配件库面积（平方米）
		po.setDepotArea(lastNull(29,cells,"").toString());//停车场面积（平方米）
		po.setOpeningTime(lastNull(30,cells,"").toString());//营业时间
		
		po.setOnlyMonthCount(lastNull(31,cells,"").toString());//月平均维修能力(台次)
		po.setWorkType(lastNull(32,cells,"").toString());//经营类型
		po.setIsFourS(lastNull(33,cells,"").toString());//是否4S店
		po.setImageLevel(lastNull(34,cells,"").toString());//建店类别
		po.setCompanyAddress(lastNull(35,cells,"").toString());//公司地址/销售展厅地址
		po.setIsLowSer(lastNull(36,cells,"").toString());//有无二级服务站
		po.setAuthorizationType(lastNull(37,cells,"").toString());//企业授权类型
		po.setAuthorizationDate(lastNull(38,cells,"").toString());//授权时间
		po.setIsActingBrand(lastNull(39,cells,"").toString());//是否经营其他品牌
		po.setActingBrandName(lastNull(40,cells,"").toString());//代理其它品牌名称
		po.setHotline(lastNull(41,cells,"").toString());//24小时热线
		po.setSerManagerName(lastNull(42,cells,"").toString());//服务经理
		po.setSerManagerTelphone(lastNull(43,cells,"").toString());//服务经理手机
		po.setSerManagerEmail(lastNull(44,cells,"").toString());//服务经理邮箱
		po.setClaimDirectorName(lastNull(45,cells,"").toString());//索赔主管
		po.setClaimDirectorPhone(lastNull(46,cells,"").toString());//索赔主管办公电话
		po.setClaimDirectorTelphone(lastNull(47,cells,"").toString());//索赔主管手机
		po.setClaimDirectorEmail(lastNull(48,cells,"").toString());//索赔主管邮箱
		po.setClaimDirectorFax(lastNull(49,cells,"").toString());//索赔传真
		po.setSerDirectorName(lastNull(50,cells,"").toString());//服务主管
		po.setSerDirectorPhone(lastNull(51,cells,"").toString());//服务主管办公电话
		po.setSerDirectorTelhone(lastNull(52,cells,"").toString());//服务主管手机
		po.setTechnologyDirectorName(lastNull(53,cells,"").toString());//技术主管
		po.setTechnologyDirectorTelphone(lastNull(54,cells,"").toString());//技术主管手机
		po.setFittingsDecName(lastNull(55,cells,"").toString());//配件主管
		po.setFittingsDecTelphone(lastNull(56,cells,"").toString());//配件主管办公电话
		po.setFittingsDecPhone(lastNull(57,cells,"").toString());//配件主管手机
		po.setFittingsDecEmail(lastNull(58,cells,"").toString());//配件主管邮箱
		po.setFittingsDecFax(lastNull(59,cells,"").toString());//配件主管传真
		po.setPartsStoreAmount(lastNull(60,cells,"").toString());//配件储备金额（万元）
		
		po.setBeginBank(lastNull(61,cells,"").toString());//开户行
		po.setErpCode(lastNull(62,cells,"").toString());//开票名称/开票公司名称
		po.setInvoiceAccount(lastNull(63,cells,"").toString());//银行帐号
		po.setInvoicePhone(lastNull(64,cells,"").toString());//开票电话/开票联系人电话
		po.setInvoiceAdd(lastNull(65,cells,"").toString());//开票地址
		po.setTaxpayerNo(lastNull(66,cells,"").toString());//纳税人识别号
		po.setTaxpayerNature(lastNull(67,cells,"").toString());//纳税人性质
		po.setTaxInvoice(lastNull(68,cells,"").toString());//增值税发票
		po.setTaxDisrate(lastNull(69,cells,"").toString());//开票税率
		po.setFinanceManagerName(lastNull(70,cells,"").toString());//财务经理
		po.setFinanceManagerTelphone(lastNull(71,cells,"").toString());//财务经理办公电话
		po.setFinanceManagerPhone(lastNull(72,cells,"").toString());//财务经理手机
		po.setFinanceManagerEmail(lastNull(73,cells,"").toString());//财务经理邮箱
		po.setRemark(lastNull(74,cells,"").toString());//备注
		po.setBalanceLevel(lastNull(75,cells,"").toString());//结算等级
		po.setInvoiceLevel(lastNull(76,cells,"").toString());//开票等级
		po.setSpyMan(lastNull(77,cells,"").toString());//索赔员
		po.setPhone(lastNull(78,cells,"").toString());//服务商电话
		po.setTheAgents(lastNull(79,cells,"").toString());//辐射区域
		
		po.setProxyVehicleType(lastNull(80,cells,"").toString());//代理车型
//		po.setProxyArea(lastNull(81,cells,"").toString());//代理区域
		po.setRowNum(Integer.parseInt(rowNum));//行号
        dao.insert(po);	
}
	
	/*
	 * 每一行插入TMP_SH_DEALER_ADDRESS
	 * 数字只截取30位
	 */
	private void parseCellsForAddress(String rowNum,Cell[] cells,Long userId) throws Exception{
			ShDealerDao dao=ShDealerDao.getInstance();
			TmpShDealerAddressPO po=new TmpShDealerAddressPO();
			
			po.setDealerCode(lastNull(0,cells,"").toString());
			po.setDealerName(lastNull(1,cells,"").toString());
			po.setLinkman(lastNull(2,cells,"").toString());
//			po.setGender(lastNull(3,cells,"").toString());
			po.setMobilePhone(lastNull(3,cells,"").toString());
			po.setTel(lastNull(4,cells,"").toString());
			po.setAddressType(lastNull(5,cells,"").toString());
			po.setState(lastNull(6,cells,"").toString());
			po.setAddr(lastNull(7,cells,"").toString());
			po.setId(Long.valueOf(rowNum)-1);
			
	        dao.insert(po);	
	}
	/**
	 * 判断最后有值的列后面时候还有值
	 */
	private Object lastNull(int i,Cell[] cells,Object obj){
	       return cells.length>i?cells[i].getContents().trim():obj;
	}
	/*
	 * 校验TMP_XS_DEALER表中数据是否符合导入标准
	 * DEALER_CODE 是否存在
	 */
	private List<ExcelErrors> checkData(){
		ShDealerDao dao=ShDealerDao.getInstance();
		TmpShDealerPO ppo=new TmpShDealerPO();
		List<TmpShDealerPO> list=dao.select(ppo);
		if(null==list){
			list=new ArrayList();
		}
		ExcelErrors errors=null;
		TmpShDealerPO po=null;
		StringBuffer errorInfo=new StringBuffer("");
		boolean isError=false;
		
		List<ExcelErrors> errorList = new LinkedList<ExcelErrors>();
		for(int i=0;i<list.size();i++){
			errors=new ExcelErrors();
			//取得TmpYearlyPlanPO
			po=list.get(i);
			//取得行号
			String rowNum=po.getRowNum().toString();
			try {
				if(null==po.getDealerCode()){
					isError=true;errorInfo.append("经销商编码不能为空!");	
				}else{
					String[] strCode=po.getDealerCode().trim().split("-");
					if(strCode.length==2){
						if(!strCode[1].equals("F")){
							isError=true;errorInfo.append("经销商编码格式错误，格式为：【100001-F】!");
						}
					}else{
						isError=true;errorInfo.append("经销商编码格式错误，格式为：【100001-F】!");	
					}
				}
				if(null==po.getDealerName()){isError=true;errorInfo.append("经销商名称不能为空!");}
				if(null==po.getDealerShortname()){isError=true;errorInfo.append("经销商简称不能为空!");}
				if(null==po.getServiceStatus()){isError=true;errorInfo.append("信息状态不能为空!");}
				if(null==po.getDxName()){isError=true;errorInfo.append("大区不能为空!");}
				if(null==po.getDealerOrgId()){isError=true;errorInfo.append("所属组织不能为空!");}
				if(null==po.getParentDealerD()){isError=true;errorInfo.append("所属上级单位不能为空，如是一级服务商请默认为-1!");}
				if(null==po.getDealerType()){isError=true;errorInfo.append("服务商等级不能为空!");}else{
					if(!po.getDealerType().trim().equals("一级") && !po.getDealerType().trim().equals("二级")){
						isError=true;errorInfo.append("服务商等级只能为{一级,二级}!");
					}
					if(po.getDealerType().trim().equals("一级") && !po.getParentDealerD().trim().equals("-1")){
						isError=true;errorInfo.append("服务商等级为一级所属上级单位默认-1!");
					}
					if(po.getDealerType().trim().equals("二级") && po.getParentDealerD().trim().equals("-1")){
						isError=true;errorInfo.append("服务商等级为二级所属上级单位不能为-1!");
					}
				}
				if(po.getImageLevel()!=null){
					if(!po.getImageLevel().trim().equals("A类") && !po.getImageLevel().trim().equals("B类") && !po.getImageLevel().trim().equals("C类") && !po.getImageLevel().trim().equals("D类") && !po.getImageLevel().trim().equals("F类")){
						isError=true;errorInfo.append("建店类别别只能为{A类;B类;C类;D类;F类}！");
					}
				}
				//状态正常,注销,待退,整改
				if(null==po.getServiceStatus()){	
					 isError=true;errorInfo.append("状态不能为空!");
					}else{
						//状态
						if(!po.getServiceStatus().trim().equals("正常") && !po.getServiceStatus().trim().equals("意向")&& !po.getServiceStatus().trim().equals("拟撤")&& !po.getServiceStatus().trim().equals("已撤")){
							 isError=true;errorInfo.append("状态只能为{意向,正常,拟撤,已撤}!");
						}
				}
				//经营类型
				if(null==po.getWorkType()){	
					 isError=true;errorInfo.append("经营类型不能为空!");
					}else{
						//状态
						if(!po.getWorkType().trim().equals("直营") && !po.getWorkType().trim().equals("代理")&& !po.getWorkType().trim().equals("托管")){
							 isError=true;errorInfo.append("状态只能为{直营,代理,托管}!");
						}
				}
				if(null==po.getProvinceId()){isError=true;errorInfo.append("省份不能为空!");}
				if(null==po.getCityId()){isError=true;errorInfo.append("城市不能为空!");}
				if(null==po.getCounties()){isError=true;errorInfo.append("县城不能为空!");}
				if(po.getDealerCode().split("-").length!=2){isError=true;errorInfo.append("经销商代码格式错误,售后经销商代码格式为【公司代码-销售SH1】！");}
				if(null==po.getMainResources()){isError=true;errorInfo.append("维修资质不能为空!");}
				if(po.getFixedCapital()!=null && !ValidateUtil.isNumeric(po.getFixedCapital().trim())){isError=true;errorInfo.append("固定资产（万元）只能是数字!");}	
				if(po.getRegisteredCapital()!=null && !ValidateUtil.isNumeric(po.getRegisteredCapital().trim())){isError=true;errorInfo.append("企业注册资金只能是数字!");}	
				if(po.getPeopleNumber()!=null && !ValidateUtil.isNumericOne(po.getPeopleNumber().trim())){isError=true;errorInfo.append("人数只能是数字!");}	
				if(po.getReceiveArea()!=null && !ValidateUtil.isNumeric(po.getReceiveArea().trim())){isError=true;errorInfo.append("接待室面积（平方米）只能是数字!");}	
				if(po.getPartsArea()!=null && !ValidateUtil.isNumeric(po.getPartsArea().trim())){isError=true;errorInfo.append("配件库面积（平方米）只能是数字!");}	
				if(po.getDepotArea()!=null && !ValidateUtil.isNumeric(po.getDepotArea().trim())){isError=true;errorInfo.append("停车场面积（平方米）只能是数字!");}	
				if(po.getMainArea()!=null && !ValidateUtil.isNumeric(po.getMainArea().trim())){isError=true;errorInfo.append("维修车间面积（平方米）只能是数字!");}	
				if(po.getFixedCapital()!=null && !ValidateUtil.isNumericOne(po.getFixedCapital().trim())){isError=true;errorInfo.append("月平均维修能力(台次)只能是数字!");}	
				//if(po.getOpeningTime()!=null && !ValidateUtil.isValidDate(po.getOpeningTime().trim())){isError=true;errorInfo.append("营业时间格式错误，格式为09:00-17:30!");}
				if(po.getAuthorizationDate()!=null && !ValidateUtil.isValidDate(po.getAuthorizationDate().trim())){isError=true;errorInfo.append("授权时间格式错误，格式为2014-01-14!");}
				if(po.getIsActingBrand()!=null && !po.getIsActingBrand().trim().equals("是") && !po.getIsActingBrand().trim().equals("否")){isError=true;errorInfo.append("是否经营其他品牌只能为{是,否}!");}	
				if(po.getPartsStoreAmount()!=null && !ValidateUtil.isNumeric(po.getPartsStoreAmount().trim())){isError=true;errorInfo.append("配件储备金额（万元）只能是数字!");}	
				if(po.getIsLowSer()!=null && !po.getIsLowSer().trim().equals("是") && !po.getIsLowSer().trim().equals("否")){isError=true;errorInfo.append("是否有二级服务站只能为{是,否}!");}
				if(po.getIsFourS()!=null && !po.getIsFourS().trim().equals("是") && !po.getIsFourS().trim().equals("否")){isError=true;errorInfo.append("是否4S店只能为{是,否}!");}	
				if(null==po.getBalanceLevel()){	
					 isError=true;errorInfo.append("结算等级不能为空!");
					}else{
						//结算等级
						if(!po.getBalanceLevel().trim().equals("独立结算") && !po.getBalanceLevel().trim().equals("上级结算")){
							 isError=true;errorInfo.append("结算等级只能为{独立结算,上级结算}!");
						}
				}
				if(null==po.getInvoiceLevel()){	
					 isError=true;errorInfo.append("开票等级不能为空!");
					}else{
						//开票等级
						if(!po.getInvoiceLevel().trim().equals("独立开票") && !po.getInvoiceLevel().trim().equals("上级开票")){
							 isError=true;errorInfo.append("开票等级只能为{独立开票,上级开票}!");
						}
				}
				if(po.getSiteDate()!=null && !ValidateUtil.isValidDate(po.getSiteDate().trim())){isError=true;errorInfo.append("建厂时间格式错误，格式为2014-01-14!");}
				if(null==po.getMainResources()){	
					 isError=true;errorInfo.append("维修资质不能为空!");
					}else{
						//维修资质
						if(!po.getMainResources().trim().equals("一类") && !po.getMainResources().trim().equals("二类")&& !po.getMainResources().trim().equals("三类")&& !po.getMainResources().trim().equals("无")){
							 isError=true;errorInfo.append("维修资质只能为{一类,二类,三类,无}!");
						}
				}
				//企业授权类型
				if(null==po.getAuthorizationType()){	
					 isError=true;errorInfo.append("企业授权类型不能为空!");
					}else{
						//企业授权类型
						if(!po.getAuthorizationType().trim().equals("形象店") && !po.getAuthorizationType().trim().equals("特约站")&& !po.getAuthorizationType().trim().equals("代理库")&& !po.getAuthorizationType().trim().equals("专卖店")){
							 isError=true;errorInfo.append("企业授权类型只能为{形象店,特约站,代理库,专卖店}!");
						}else{
							if(po.getAuthorizationType().trim().equals("代理库")){
								if(null==po.getTheAgents()){isError=true;errorInfo.append("辐射区域不能为空!");}
							}
						}
				}
				
				if(null==po.getHotline()){isError=true;errorInfo.append("24小时热线不能为空!");}
				if(null==po.getSerManagerName()){isError=true;errorInfo.append("服务经理不能为空!");}
				if(null==po.getSerManagerTelphone()){isError=true;errorInfo.append("服务经理手机不能为空!");}
				if(null==po.getFittingsDecName()){isError=true;errorInfo.append("配件主管不能为空!");}
				if(null==po.getFittingsDecPhone()){isError=true;errorInfo.append("配件主管手机不能为空!");}
				if(null==po.getErpCode()){isError=true;errorInfo.append("开票名称不能为空!");}
				if(null==po.getTaxpayerNature()){isError=true;errorInfo.append("纳税人性质不能为空!");}
				if(null==po.getPhone()){isError=true;errorInfo.append("服务商电话不能为空!");}
				if(null==po.getFinanceManagerName()){isError=true;errorInfo.append("财务经理不能为空!");}
				if(null==po.getSpyMan()){isError=true;errorInfo.append("索赔员不能为空!");}
			} catch (Exception e) {
				isError=true;
			}
			
			if(errorInfo.length()>0){
				String info=errorInfo.substring(0,errorInfo.length()-1);
				errors.setRowNum(new Integer(rowNum));
				errors.setErrorDesc(info);
				errorList.add(errors);
				errorInfo.delete(0, errorInfo.length());
			}
		}
		//临时表校验重复数据
		List<Map<String, Object>> dumpList=dao.sbuTalbeCheckDump();
		if(null!=dumpList&&dumpList.size()>0){
			
			String r1="";
			String r2="";
			List<String> tmp=new ArrayList();
			String s1="";
			String s2="";
			for(int i=0;i<dumpList.size();i++){
				Map<String, Object> map=dumpList.get(i);
				r1=map.get("ROW_NUMBER1").toString();
				r2=map.get("ROW_NUMBER2").toString();
				s1=r1+","+r2;
				s2=r2+","+r1;
				if(tmp.contains(s1)||tmp.contains(s2)){
					continue;
				}else{
					isError=true;
					ExcelErrors err=new ExcelErrors();
					err.setRowNum(Integer.parseInt(r1));
					err.setErrorDesc("与"+r2+"行数据重复");
					errorList.add(err);
					tmp.add(s1);
				}
				
			}
		}
		if(isError){
			return errorList;
		}else{
			return null;
		}
		
	}
	
	/**
	 * 判断输入的字符必须是正整数, 而且第一位不能为0；
	 * @param source
	 * @return 如果符合规则则返回true,否则返回false
	 */
	public static boolean checkFormatNumber(String source){
		final String SEQUECNE_FORMAT_STR5 = "^-?[0-9]\\d*$";
		Pattern pattern =Pattern.compile(SEQUECNE_FORMAT_STR5);
		Matcher matcher =pattern.matcher(source);
		return matcher.matches();
	}
	
	
	/*
	 * 导入业务表（导入正式数据表）
	 */
	public void importExcel(){
		ActionContext act = ActionContext.getContext();
		ShDealerDao dao=ShDealerDao.getInstance();
		Connection conn = DBService.getInstance().getConnection();
		TmpShDealerPO ppo=new TmpShDealerPO();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String,Object>> errorJsp=new ArrayList();
			List<PO> l=dao.select(ppo);
			if(l!=null && l.size()>0){
				for(int i=0;i<l.size();i++){
					Map<String,Object> errorMap=new HashMap<String,Object>();
					TmpShDealerPO p=(TmpShDealerPO)l.get(i);
					String hiDealerId="";//经销商ID 中间表保存需要
					String hiOrgId="";//组织ID 中间表保存需要
					//Long rowNumMsg=p.getRowNumber();//行号信息
					 errInfoStr="";//记录错误信息
					 errBo=false;//判断是否有错误信息
					TmDealerPO po=new TmDealerPO();
					po.setDealerCode(CommonUtils.checkNull(p.getDealerCode()).trim());
					List<PO> ps=dao.select(po);
					if(ps.size()>0){//首先判断时候经销商是否有，有就更新公司表，经销商表[有]
						hiDealerId=((TmDealerPO)ps.get(0)).getDealerId().toString();
						//公司表数据
						TmCompanyPO tcpo=new TmCompanyPO();
//						tcpo.setCompanyCode(CommonUtils.checkNull(p.getDealerCode()).trim().split("-")[0]);//截取经销商编码“-”前一部分
//						tcpo.setCompanyName(CommonUtils.checkNull(p.getDealerName()).trim());//公司全称
//						tcpo.setCompanyShortname(CommonUtils.checkNull(p.getDealerShortname()).trim());//公司简称
						tcpo.setStatus(Constant.STATUS_ENABLE);//有效的
						tcpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));//默认
						tcpo.setCompanyType(Integer.parseInt(Constant.COMPANY_TYPE_DEALER));//公司类型
						
						//经销商表
						TmDealerPO tdpo=new TmDealerPO();
						String PId="";//省份ID下面调用
						String CId="";//城市ID下面调用
						String CCId="";//地区ID下面调用
						List<Map<String, Object>> proId=dao.getProvinceId(CommonUtils.checkNull(p.getProvinceId()).trim());
						if(proId!=null && proId.size()==1){
							PId=proId.get(0).get("REGION_CODE").toString();
							tdpo.setProvinceId(Long.parseLong(PId));//省份
							List<Map<String, Object>> cId=dao.getCityId(proId.get(0).get("REGION_ID").toString(),CommonUtils.checkNull(p.getCityId()).trim());
							if(cId!=null && cId.size()==1){
								CId=cId.get(0).get("REGION_CODE").toString();
								tdpo.setCityId(Long.parseLong(CId));//城市
								List<Map<String, Object>> coId=dao.getContinusId(cId.get(0).get("REGION_ID").toString(),CommonUtils.checkNull(p.getCounties()).trim());
								if(coId!=null && coId.size()==1){
									CCId=coId.get(0).get("REGION_CODE").toString();
									tdpo.setCounties(Integer.parseInt(CCId));//地区
								}else{
									errInfoStr+="区县输入有误！";
									errBo=true;
								}
							}else{
								errInfoStr+="城市输入有误！";
								errBo=true;
							}
						}else{
							errInfoStr+="省份输入有误！";
							errBo=true;
						}
						tdpo.setDealerCode(checkVarchar(CommonUtils.checkNull(p.getDealerCode()).trim(),errInfoStr,errBo,"经销商代码",20));//经销商CODE
						tdpo.setDealerName(checkVarchar(CommonUtils.checkNull(p.getDealerName()).trim(),errInfoStr,errBo,"经销商名称",300));//经销商名称
						
						//增加经销商拼音 20150521 冉可
						if(tdpo.getDealerName() != null && !"".equals(tdpo.getDealerName())){
							String pinyin = Utility.hanZiToPinyin(tdpo.getDealerName());
							tdpo.setPinyin(pinyin);
						}
						
						tdpo.setDealerShortname(checkVarchar(CommonUtils.checkNull(p.getDealerShortname()).trim(),errInfoStr,errBo,"经销商简称",150));//经销商简称
						if(CommonUtils.checkNull(p.getDealerType()).trim().equals("一级")){
							tdpo.setDealerLevel(Constant.DEALER_LEVEL_01);//经销商等级
						}else if(CommonUtils.checkNull(p.getDealerType()).trim().equals("二级")){
							tdpo.setDealerLevel(Constant.DEALER_LEVEL_02);//经销商等级
						}else{
							errInfoStr+="服务商等级输入有误【只能输入一级或二级】！";
							errBo=true;
						}
						tdpo.setStatus(Constant.STATUS_ENABLE);//经销商状态
							tdpo.setZipCode(checkVarchar(CommonUtils.checkNull(p.getZipCode()).trim(),errInfoStr,errBo,"邮编",10));//邮编
							tdpo.setAddress(checkVarchar(CommonUtils.checkNull(p.getAddress()).trim(),errInfoStr,errBo,"企业注册地址",300));//企业注册地址
							tdpo.setZccode(checkVarchar(CommonUtils.checkNull(p.getZccode()).trim(),errInfoStr,errBo,"注册证号",50));//注册证号
							tdpo.setZyScope(checkVarchar(CommonUtils.checkNull(p.getZyScope()).trim(),errInfoStr,errBo,"主营范围",300));//主营范围
							tdpo.setJyScope(checkVarchar(CommonUtils.checkNull(p.getJyScope()).trim(),errInfoStr,errBo,"兼营范围",300));//兼营范围
							tdpo.setLegal(checkVarchar(CommonUtils.checkNull(p.getLegal()).trim(),errInfoStr,errBo,"法人",20));//法人
							tdpo.setBeginBank(checkVarchar(CommonUtils.checkNull(p.getBeginBank()).trim(),errInfoStr,errBo,"开户行",300));//开户行
							tdpo.setErpCode(checkVarchar(CommonUtils.checkNull(p.getErpCode()).trim(),errInfoStr,errBo,"/开票名称/开票公司名称",100));//开票名称/开票公司名称
							tdpo.setInvoiceAccount(checkVarchar(CommonUtils.checkNull(p.getInvoiceAccount()).trim(),errInfoStr,errBo,"银行帐号",100));//银行帐号
							tdpo.setInvoicePhone(checkVarchar(CommonUtils.checkNull(p.getInvoicePhone()).trim(),errInfoStr,errBo,"开票联系人电话",50));//开票电话/开票联系人电话
							tdpo.setInvoiceAdd(checkVarchar(CommonUtils.checkNull(p.getInvoiceAdd()).trim(),errInfoStr,errBo,"开票地址",200));//开票地址
							tdpo.setTaxpayerNo(checkVarchar(CommonUtils.checkNull(p.getTaxpayerNo()).trim(),errInfoStr,errBo,"纳税人识别号",20));//纳税人识别号
							tdpo.setTaxpayerNature(checkVarchar(CommonUtils.checkNull(p.getTaxpayerNature()).trim(),errInfoStr,errBo,"纳税人性质",50));//纳税人性质
							tdpo.setTaxInvoice(checkVarchar(CommonUtils.checkNull(p.getTaxInvoice()).trim(),errInfoStr,errBo,"增值税发票",30));//增值税发票
							tdpo.setTaxDisrate(checkVarchar(CommonUtils.checkNull(p.getTaxDisrate()).trim(),errInfoStr,errBo,"开票税率",30));//开票税率
							
							if(CommonUtils.checkNull(p.getSiteDate()).trim().contains("-")){
								tdpo.setSitedate(Utility.getDate(CommonUtils.checkNull(p.getSiteDate()).trim(),1));//建厂时间(建站时间)
							}else if(CommonUtils.checkNull(p.getSiteDate()).trim().contains("/")){
								tdpo.setSitedate(Utility.getDate(CommonUtils.checkNull(p.getSiteDate()).trim(),2));//建厂时间(建站时间)
							}else{
								errInfoStr+="建厂时间格式必须是2014-01-14或者2014/01/14!";
								errBo=true;
							}
							
							tdpo.setDealerType(Constant.DEALER_TYPE_DWR);//经销商类型
							tdpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));//默认
							tdpo.setSpyMan(checkVarchar(CommonUtils.checkNull(p.getSpyMan()).trim(),errInfoStr,errBo,"索赔员",20));//索赔员
							tdpo.setPhone(checkVarchar(CommonUtils.checkNull(p.getPhone()).trim(),errInfoStr,errBo,"服务商电话",100));//服务商电话
							//tdpo.setTaxesNo(checkVarchar(CommonUtils.checkNull(p.getTaxesNo()).trim(),errInfoStr,errBo,"税号",50));//税号
							if(CommonUtils.checkNull(p.getImageLevel()).trim().equals("")){
								
							}else{
								String deaLev=CodeDict.getDictCodeByName(Constant.IMAGE_LEVEL.toString(), p.getImageLevel().trim());
								if(deaLev!=null){
									tdpo.setImageLevel(Integer.parseInt(deaLev));
								}else{
									errInfoStr+="拟建店类别输入有误【A类;B类;C类;D类;F类】！";
									errBo=true;
								}
							}

							//服务状态
							String serName=CodeDict.getDictCodeByName(Constant.DLR_SERVICE_STATUS, p.getServiceStatus());
							if(serName!=null){
								tdpo.setServiceStatus(Long.parseLong(serName));
							}else{
								errInfoStr+="服务状态输入有误【只能输入{意向,正常,拟撤,已撤}】！";
								errBo=true;
							}
							//维修资质
							String mrces=CodeDict.getDictCodeByName(Constant.MAIN_RESOURCES.toString(), p.getMainResources());
							if(mrces!=null){
								tdpo.setMainResources(Integer.parseInt(mrces));
							}else{
								errInfoStr+="维修资质输入有误【只能输入{一类,二类,三类,无}】！";
								errBo=true;
							}
							
						tdpo.setBalanceLevel(CommonUtils.checkNull(p.getBalanceLevel()).trim().equals("独立结算")?Constant.BALANCE_LEVEL_SELF:Constant.BALANCE_LEVEL_HIGH);//结算等级
						tdpo.setInvoiceLevel(CommonUtils.checkNull(p.getInvoiceLevel()).trim().equals("独立开票")?Constant.INVOICE_LEVEL_SELF:Constant.INVOICE_LEVEL_HIGH);//开票等级
						tdpo.setUpdateDate(new Date());

						//根据大区名称获取大区代码
						List<Map<String, Object>> bdd=dao.getBigId(CommonUtils.checkNull(p.getDealerOrgId()).trim());
						if(bdd!=null && bdd.size()==1){//有就设置
							hiOrgId=bdd.get(0).get("ORG_ID").toString();
							tdpo.setDealerOrgId(Long.parseLong(hiOrgId));//大区
						}else{
							errInfoStr+="所属组织输入有误！";
							errBo=true;
						}
						List<Map<String, Object>> pdd=dao.getPDealer(CommonUtils.checkNull(p.getParentDealerD()).trim());
						if(pdd!=null && pdd.size()==1){//有就设置
							tdpo.setParentDealerD(Long.parseLong(pdd.get(0).get("DEALER_ID").toString()));//上级经销商
						}else{
							if(CommonUtils.checkNull(p.getDealerType()).trim().equals("二级")){
								errInfoStr+="上级单位输入有误,请确认是否输错(必须全称)!";
								errBo=true;
							}
							//tdpo.setParentDealerD(-1L);//上级经销商
						}
						//经销商明显表数据插入
						TmDealerDetailPO tdd=new TmDealerDetailPO();
							tdd.setTheAgents(checkVarchar(CommonUtils.checkNull(p.getTheAgents()).trim(),errInfoStr,errBo,"辐射区域",300));//辐射区域
							tdd.setSerManagerName(checkVarchar(CommonUtils.checkNull(p.getSerManagerName()).trim(),errInfoStr,errBo,"服务经理",30));//服务经理
							tdd.setSerManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getSerManagerTelphone()).trim(),errInfoStr,errBo,"服务经理手机",30));//服务经理手机
							tdd.setSerManagerEmail(checkVarchar(CommonUtils.checkNull(p.getSerManagerEmail()).trim(),errInfoStr,errBo,"服务经理邮箱",50));//服务经理邮箱
							tdd.setClaimDirectorName(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorName()).trim(),errInfoStr,errBo,"索赔主管",30));//索赔主管
							tdd.setClaimDirectorPhone(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorPhone()).trim(),errInfoStr,errBo,"索赔主管办公电话",30));//索赔主管办公电话
							tdd.setClaimDirectorTelphone(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorTelphone()).trim(),errInfoStr,errBo,"索赔主管手机",30));//索赔主管手机
							tdd.setClaimDirectorEmail(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorEmail()).trim(),errInfoStr,errBo,"索赔主管邮箱",50));//索赔主管邮箱
							tdd.setClaimDirectorFax(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorFax()).trim(),errInfoStr,errBo,"索赔传真",30));//索赔传真
							tdd.setTechnologyDirectorName(checkVarchar(CommonUtils.checkNull(p.getTechnologyDirectorName()).trim(),errInfoStr,errBo,"技术主管",30));//技术主管
							tdd.setTechnologyDirectorTelphone(checkVarchar(CommonUtils.checkNull(p.getTechnologyDirectorTelphone()).trim(),errInfoStr,errBo,"技术主管手机",30));//技术主管手机
							tdd.setUnionType(checkVarchar(CommonUtils.checkNull(p.getUnionType()).trim(),errInfoStr,errBo,"单位性质",50));//单位性质
							tdd.setFixedCapital(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getFixedCapital()).trim(),errInfoStr,errBo,"固定资产（万元）",15)));//固定资产（万元）
							tdd.setRegisteredCapital(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getRegisteredCapital()).trim(),errInfoStr,errBo,"注册资金（万元）/企业注册资金",15)));//注册资金（万元）/企业注册资金
							tdd.setPeopleNumber(Integer.parseInt(checkVarchar(CommonUtils.checkNullNum(p.getPeopleNumber()).trim(),errInfoStr,errBo,"人数",8)));//人数
							tdd.setPartsArea(Long.parseLong(checkVarchar(CommonUtils.checkNullNum(p.getPartsArea()).trim(),errInfoStr,errBo,"配件库面积（平方米）",10)));//配件库面积（平方米）
							tdd.setDepotArea(Long.parseLong(checkVarchar(CommonUtils.checkNullNum(p.getDepotArea()).trim(),errInfoStr,errBo,"停车场面积（平方米）",10)));//停车场面积（平方米）
							tdd.setMainArea(Long.parseLong(checkVarchar(CommonUtils.checkNullNum(p.getMainArea()).trim(),errInfoStr,errBo,"维修车间面积（平方米）",10)));//维修车间面积（平方米）
							tdd.setOnlyMonthCount(Integer.parseInt(checkVarchar(CommonUtils.checkNullNum(p.getOnlyMonthCount()).trim(),errInfoStr,errBo,"月平均维修能力(台次)",8)));//月平均维修能力(台次)
							tdd.setOpeningTime(checkVarchar(CommonUtils.checkNull(p.getOpeningTime()).trim(),errInfoStr,errBo,"营业时间",50));//营业时间
							tdd.setWorkType(checkVarchar(CommonUtils.checkNull(p.getWorkType()).trim(),errInfoStr,errBo,"经营类型",50));//经营类型
							tdd.setIsFourS(checkVarchar(CommonUtils.checkNull(p.getIsFourS()).trim(),errInfoStr,errBo,"是否4S店",8));//是否4S店
							tdd.setAuthorizationType(checkVarchar(CommonUtils.checkNull(p.getAuthorizationType()).trim(),errInfoStr,errBo,"企业授权类型",30));//企业授权类型
							if(CommonUtils.checkNull(p.getImageCLevel()).trim().equals("")){
								
							}else{
								String deaLev=CodeDict.getDictCodeByName(Constant.IMAGE_LEVEL.toString(), p.getImageCLevel().trim());
								if(deaLev!=null){
									tdd.setImageComfirmLevel(Integer.parseInt(deaLev));
								}else{
									errInfoStr+="验收形象等级输入有误【A类;B类;C类;D类;F类】！";
									errBo=true;
								}
							}
							if(CommonUtils.checkNull(p.getAuthorizationDate()).trim().contains("-")){
								tdd.setAuthorizationDate(Utility.getDate(CommonUtils.checkNull(p.getAuthorizationDate()).trim(),1));//授权时间
							}else if(CommonUtils.checkNull(p.getAuthorizationDate()).trim().contains("/")){
								tdpo.setSitedate(Utility.getDate(CommonUtils.checkNull(p.getAuthorizationDate()).trim(),2));//授权时间
							}else{
								errInfoStr+="授权时间格式必须是2014-01-14或者2014/01/14!";
								errBo=true;
							}
							
							tdd.setIsActingBrand(CommonUtils.checkNull(p.getIsActingBrand()).trim().equals("是")?Constant.IF_TYPE_YES:Constant.IF_TYPE_YES);//是否经营其他品牌
							tdd.setActingBrandName(checkVarchar(CommonUtils.checkNull(p.getActingBrandName()).trim(),errInfoStr,errBo,"代理其它品牌名称",200));//代理其它品牌名称
							tdd.setPartsStoreAmount(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getPartsStoreAmount()).trim(),errInfoStr,errBo,"配件储备金额（万元）",15)));//配件储备金额（万元）
							tdd.setCompanyZcCode(checkVarchar(CommonUtils.checkNull(p.getCompanyZcCode()).trim(),errInfoStr,errBo,"组织机构代码",30));//组织机构代码
							tdd.setDealerRelation(checkVarchar(CommonUtils.checkNull(p.getDealerRelation()).trim(),errInfoStr,errBo,"与一级经销商关系",30));//与一级经销商关系
							tdd.setMeetingRoomArea(Integer.parseInt(checkVarchar(CommonUtils.checkNullNum(p.getReceiveArea()).trim(),errInfoStr,errBo,"接待室面积（平方米）",8)));//接待室面积（平方米）
							//tdd.seti
							
							tdd.setFittingsDecName(checkVarchar(CommonUtils.checkNull(p.getFittingsDecName()).trim(),errInfoStr,errBo,"配件主管",50));//配件主管
							tdd.setFittingsDecPhone(checkVarchar(CommonUtils.checkNull(p.getFittingsDecPhone()).trim(),errInfoStr,errBo,"配件主管办公电话",30));//配件主管办公电话
							tdd.setFittingsDecTelphone(checkVarchar(CommonUtils.checkNull(p.getFittingsDecTelphone()).trim(),errInfoStr,errBo,"配件主管手机",30));//配件主管手机
							tdd.setFittingsDecEmail(checkVarchar(CommonUtils.checkNull(p.getFittingsDecEmail()).trim(),errInfoStr,errBo,"配件主管邮箱",30));//配件主管邮箱
							tdd.setFittingsDecFax(checkVarchar(CommonUtils.checkNull(p.getFittingsDecFax()).trim(),errInfoStr,errBo,"配件传真",30));//配件传真
							tdd.setFinanceManagerName(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerName()).trim(),errInfoStr,errBo,"财务经理",30));//财务经理
							tdd.setFinanceManagerPhone(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerPhone()).trim(),errInfoStr,errBo,"财务经理办公电话",30));//财务经理办公电话
							tdd.setFinanceManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerTelphone()).trim(),errInfoStr,errBo,"财务经理手机",30));//财务经理手机
							tdd.setFinanceManagerEmail(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerEmail()).trim(),errInfoStr,errBo,"财务经理邮箱",30));//财务经理邮箱
							
							
							tdd.setRemark(checkVarchar(CommonUtils.checkNull(p.getRemark()).trim(),errInfoStr,errBo,"备注",200));//备注
						tdd.setSerDirectorName(checkVarchar(CommonUtils.checkNull(p.getSerDirectorName()).trim(),errInfoStr,errBo,"服务主管",30));//服务主管
						tdd.setSerDirectorPhone(checkVarchar(CommonUtils.checkNull(p.getSerDirectorPhone()).trim(),errInfoStr,errBo,"服务主管办公电话",30));//服务主管办公电话
						tdd.setSerDirectorTelhone(checkVarchar(CommonUtils.checkNull(p.getSerDirectorTelhone()).trim(),errInfoStr,errBo,"服务主管手机",30));//服务主管手机
						tdd.setCompanyAddress(checkVarchar(CommonUtils.checkNull(p.getCompanyAddress()).trim(),errInfoStr,errBo,"公司地址/销售展厅地址",300));//公司地址/销售展厅地址
						tdd.setHotline(checkVarchar(CommonUtils.checkNull(p.getHotline()).trim(),errInfoStr,errBo,"24小时热线",30));//24小时热线
						
						
						//判断是否有错误信息,有就不执行插入或更新操作
						if(errBo){
							errorMap.put("rowNum", p.getRowNum());//错误行号
							errorMap.put("errorInfo", errInfoStr);//错误信息
							errorJsp.add(errorMap);//保存错误信息
							continue;
						}
						List<Map<String, Object>> comList=dao.getCompany(CommonUtils.checkNull(p.getDealerCode().split("-")[0]).trim());
						//如果公司有 更新
						if(comList!=null && comList.size()>0){
							//companyId=((TmCompanyPO)comList.get(0)).getCompanyId().toString();
							tcpo.setUpdateBy(logonUser.getUserId());//创建人
							tcpo.setUpdateDate(new Date());
							//更新公司表数据
							TmCompanyPO tcpo1=new TmCompanyPO();
							tcpo1.setCompanyCode(CommonUtils.checkNull(p.getDealerCode().split("-")[0]).trim());
							dao.update(tcpo1, tcpo);
						}else{//无 添加公司表数据
							tcpo.setCreateBy(logonUser.getUserId());//创建人
							tcpo.setCreateDate(new Date());
							//companyId=SequenceManager.getSequence("");
							tcpo.setCompanyId(Long.parseLong(SequenceManager.getSequence("")));
							dao.insert(tcpo);
						}
						//获取公司ID
						TmCompanyPO c1=new TmCompanyPO();
						c1.setCompanyCode(CommonUtils.checkNull(p.getDealerCode().split("-")[0]).trim());
						TmCompanyPO tmc=(TmCompanyPO)dao.select(c1).get(0);
						//公司
						tdpo.setCompanyId(tmc.getCompanyId());//公司ID
						//更新经销商表
						TmDealerPO tdpo1=new TmDealerPO();
						tdpo1.setDealerCode(CommonUtils.checkNull(p.getDealerCode()).trim());
						dao.update(tdpo1, tdpo);
						//首先清除经销商与组织中间表的数据
						dao.delRation(hiDealerId);
						//添加经销商与组织中间表的数据
						TmDealerOrgRelationPO cc=new TmDealerOrgRelationPO();
						cc.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
						cc.setDealerId(Long.parseLong(hiDealerId));
						cc.setOrgId(Long.parseLong(hiOrgId));
						cc.setBusinessType(Constant.ORG_TYPE_OEM);
						dao.insert(cc);
						//查询经销商与组织中间表是否有数据，无的话就添加
						/*List<Map<String, Object>> toi=dao.getDO(CommonUtils.checkNull(p.getDealerCode()).trim(),CommonUtils.checkNull(p.getProvinceId()).trim(),"-1");
						if(toi!=null && toi.size()>0){
							
						}else{
							TmDealerOrgRelationPO cc=new TmDealerOrgRelationPO();
							cc.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
							cc.setDealerId(Long.parseLong(hiDealerId));
							cc.setOrgId(Long.parseLong(hiOrgId));
							cc.setBusinessType(Constant.ORG_TYPE_OEM);
							dao.insert(cc);
						}*/
						//更新经销商明显表
						TmDealerDetailPO tdd2=new TmDealerDetailPO();
						tdd2.setFkDealerId(Long.parseLong(hiDealerId));
						List<PO> plist=dao.select(tdd2);
						if(plist!=null && plist.size()>0){
							//更新经销商明显表
							TmDealerDetailPO tdd1=new TmDealerDetailPO();
							tdd1.setFkDealerId(Long.parseLong(hiDealerId));
							dao.update(tdd1, tdd);
						}else{
							//新增
							TmDealerDetailPO tdd1=new TmDealerDetailPO();
							tdd.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
							tdd.setFkDealerId(Long.parseLong(hiDealerId));
							tdd.setUpdateDate(new Date());
							dao.insert(tdd);
						}
						
					}else{//无该经销商
						//公司表数据
						TmCompanyPO tcpo=new TmCompanyPO();
						tcpo.setCompanyCode(CommonUtils.checkNull(p.getDealerCode()).trim().split("-")[0]);//截取经销商编码“-”前一部分
						tcpo.setCompanyName(CommonUtils.checkNull(p.getDealerName()).trim());//公司全称
						tcpo.setCompanyShortname(CommonUtils.checkNull(p.getDealerShortname()).trim());//公司简称
						tcpo.setStatus(Constant.STATUS_ENABLE);//有效的
						tcpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));//默认
						tcpo.setCompanyType(Integer.parseInt(Constant.COMPANY_TYPE_DEALER));//公司类型
						
						//经销商表
						TmDealerPO tdpo=new TmDealerPO();
						String PId="";//省份ID下面调用
						String CId="";//城市ID下面调用
						String CCId="";//地区ID下面调用
						List<Map<String, Object>> proId=dao.getProvinceId(CommonUtils.checkNull(p.getProvinceId()).trim());
						if(proId!=null && proId.size()==1){
							PId=proId.get(0).get("REGION_CODE").toString();
							tdpo.setProvinceId(Long.parseLong(PId));//省份
							List<Map<String, Object>> cId=dao.getCityId(proId.get(0).get("REGION_ID").toString(),CommonUtils.checkNull(p.getCityId()).trim());
							if(cId!=null && cId.size()==1){
								CId=cId.get(0).get("REGION_CODE").toString();
								tdpo.setCityId(Long.parseLong(CId));//城市
								List<Map<String, Object>> coId=dao.getContinusId(cId.get(0).get("REGION_ID").toString(),CommonUtils.checkNull(p.getCounties()).trim());
								if(coId!=null && coId.size()==1){
									CCId=coId.get(0).get("REGION_CODE").toString();
									tdpo.setCounties(Integer.parseInt(CCId));//地区
								}else{
									errInfoStr+="区县输入有误！";
									errBo=true;
								}
							}else{
								errInfoStr+="城市输入有误！";
								errBo=true;
							}
						}else{
							errInfoStr+="省份输入有误！";
							errBo=true;
						}
						tdpo.setDealerCode(checkVarchar(CommonUtils.checkNull(p.getDealerCode()).trim(),errInfoStr,errBo,"经销商代码",20));//经销商CODE
						tdpo.setDealerName(checkVarchar(CommonUtils.checkNull(p.getDealerName()).trim(),errInfoStr,errBo,"经销商名称",300));//经销商名称
						tdpo.setDealerShortname(checkVarchar(CommonUtils.checkNull(p.getDealerShortname()).trim(),errInfoStr,errBo,"经销商简称",150));//经销商简称
						if(CommonUtils.checkNull(p.getDealerType()).trim().equals("一级")){
							tdpo.setDealerLevel(Constant.DEALER_LEVEL_01);//经销商等级
						}else if(CommonUtils.checkNull(p.getDealerType()).trim().equals("二级")){
							tdpo.setDealerLevel(Constant.DEALER_LEVEL_02);//经销商等级
						}else{
							errInfoStr+="服务商等级输入有误【只能输入一级或二级】！";
							errBo=true;
						}
							tdpo.setStatus(Constant.STATUS_ENABLE);//经销商状态
							tdpo.setZipCode(checkVarchar(CommonUtils.checkNull(p.getZipCode()).trim(),errInfoStr,errBo,"邮编",10));//邮编
							tdpo.setAddress(checkVarchar(CommonUtils.checkNull(p.getAddress()).trim(),errInfoStr,errBo,"企业注册地址",300));//企业注册地址
							tdpo.setZccode(checkVarchar(CommonUtils.checkNull(p.getZccode()).trim(),errInfoStr,errBo,"注册证号",50));//注册证号
							tdpo.setZyScope(checkVarchar(CommonUtils.checkNull(p.getZyScope()).trim(),errInfoStr,errBo,"主营范围",300));//主营范围
							tdpo.setJyScope(checkVarchar(CommonUtils.checkNull(p.getJyScope()).trim(),errInfoStr,errBo,"兼营范围",300));//兼营范围
							tdpo.setLegal(checkVarchar(CommonUtils.checkNull(p.getLegal()).trim(),errInfoStr,errBo,"法人",20));//法人
							tdpo.setBeginBank(checkVarchar(CommonUtils.checkNull(p.getBeginBank()).trim(),errInfoStr,errBo,"开户行",300));//开户行
							tdpo.setErpCode(checkVarchar(CommonUtils.checkNull(p.getErpCode()).trim(),errInfoStr,errBo,"/开票名称/开票公司名称",100));//开票名称/开票公司名称
							tdpo.setInvoiceAccount(checkVarchar(CommonUtils.checkNull(p.getInvoiceAccount()).trim(),errInfoStr,errBo,"银行帐号",100));//银行帐号
							tdpo.setInvoicePhone(checkVarchar(CommonUtils.checkNull(p.getInvoicePhone()).trim(),errInfoStr,errBo,"开票联系人电话",50));//开票电话/开票联系人电话
							tdpo.setInvoiceAdd(checkVarchar(CommonUtils.checkNull(p.getInvoiceAdd()).trim(),errInfoStr,errBo,"开票地址",200));//开票地址
							tdpo.setTaxpayerNo(checkVarchar(CommonUtils.checkNull(p.getTaxpayerNo()).trim(),errInfoStr,errBo,"纳税人识别号",20));//纳税人识别号
							tdpo.setTaxpayerNature(checkVarchar(CommonUtils.checkNull(p.getTaxpayerNature()).trim(),errInfoStr,errBo,"纳税人性质",50));//纳税人性质
							tdpo.setTaxInvoice(checkVarchar(CommonUtils.checkNull(p.getTaxInvoice()).trim(),errInfoStr,errBo,"增值税发票",30));//增值税发票
							tdpo.setTaxDisrate(checkVarchar(CommonUtils.checkNull(p.getTaxDisrate()).trim(),errInfoStr,errBo,"开票税率",30));//开票税率
							tdpo.setSitedate(Utility.getDate(CommonUtils.checkNull(p.getSiteDate()).trim(),1));//建厂时间(建站时间)
							tdpo.setDealerType(Constant.DEALER_TYPE_DWR);//经销商类型
							tdpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));//默认
							tdpo.setSpyMan(checkVarchar(CommonUtils.checkNull(p.getSpyMan()).trim(),errInfoStr,errBo,"索赔员",20));//索赔员
							tdpo.setPhone(checkVarchar(CommonUtils.checkNull(p.getPhone()).trim(),errInfoStr,errBo,"服务商电话",100));//服务商电话
							//tdpo.setTaxesNo(checkVarchar(CommonUtils.checkNull(p.getTaxesNo()).trim(),errInfoStr,errBo,"税号",50));//税号
							if(CommonUtils.checkNull(p.getImageLevel()).trim().equals("")){
							
							}else{
								String deaLev=CodeDict.getDictCodeByName(Constant.IMAGE_LEVEL.toString(), p.getImageLevel().trim());
								if(deaLev!=null){
									tdpo.setImageLevel(Integer.parseInt(deaLev));
								}else{
									errInfoStr+="拟建店类别输入有误【A类;B类;C类;D类;F类】！";
									errBo=true;
								}
							}
							//服务状态
							String serName=CodeDict.getDictCodeByName(Constant.DLR_SERVICE_STATUS, p.getServiceStatus());
							if(serName!=null){
								tdpo.setServiceStatus(Long.parseLong(serName));
							}else{
								errInfoStr+="服务状态输入有误【只能输入{意向,正常,拟撤,已撤}】！";
								errBo=true;
							}
							//维修资质
							String mrces=CodeDict.getDictCodeByName(Constant.MAIN_RESOURCES.toString(), p.getMainResources());
							if(mrces!=null){
								tdpo.setMainResources(Integer.parseInt(mrces));
							}else{
								errInfoStr+="维修资质输入有误【只能输入{一类,二类,三类,无}】！";
								errBo=true;
							}
						//根据大区名称获取大区代码
						List<Map<String, Object>> bdd=dao.getBigId(CommonUtils.checkNull(p.getDealerOrgId()).trim());
						if(bdd!=null && bdd.size()==1){//有就设置
							hiOrgId=bdd.get(0).get("ORG_ID").toString();
							tdpo.setDealerOrgId(Long.parseLong(hiOrgId));//大区
						}else{
							errInfoStr+="所属组织输入有误！";
							errBo=true;
						}
						List<Map<String, Object>> pdd=dao.getPDealer(CommonUtils.checkNull(p.getParentDealerD()).trim());
						if(pdd!=null && pdd.size()==1){//有就设置
							tdpo.setParentDealerD(Long.parseLong(pdd.get(0).get("DEALER_ID").toString()));//上级经销商
						}else{
							if(CommonUtils.checkNull(p.getDealerType()).trim().equals("二级")){
								errInfoStr+="上级单位输入有误,请确认是否输错(必须全称)!";
								errBo=true;
							}
							//tdpo.setParentDealerD(-1L);//上级经销商
						}
						tdpo.setBalanceLevel(CommonUtils.checkNull(p.getBalanceLevel()).trim().equals("独立结算")?Constant.BALANCE_LEVEL_SELF:Constant.BALANCE_LEVEL_HIGH);//结算等级
						tdpo.setInvoiceLevel(CommonUtils.checkNull(p.getInvoiceLevel()).trim().equals("独立开票")?Constant.INVOICE_LEVEL_SELF:Constant.INVOICE_LEVEL_HIGH);//开票等级
						tdpo.setCreateDate(new Date());
						//经销商明显表数据插入
						TmDealerDetailPO tdd=new TmDealerDetailPO();
							tdd.setTheAgents(checkVarchar(CommonUtils.checkNull(p.getTheAgents()).trim(),errInfoStr,errBo,"辐射区域",300));//辐射区域
							tdd.setSerManagerName(checkVarchar(CommonUtils.checkNull(p.getSerManagerName()).trim(),errInfoStr,errBo,"服务经理",30));//服务经理
							tdd.setSerManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getSerManagerTelphone()).trim(),errInfoStr,errBo,"服务经理手机",30));//服务经理手机
							tdd.setSerManagerEmail(checkVarchar(CommonUtils.checkNull(p.getSerManagerEmail()).trim(),errInfoStr,errBo,"服务经理邮箱",50));//服务经理邮箱
							tdd.setClaimDirectorName(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorName()).trim(),errInfoStr,errBo,"索赔主管",30));//索赔主管
							tdd.setClaimDirectorPhone(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorPhone()).trim(),errInfoStr,errBo,"索赔主管办公电话",30));//索赔主管办公电话
							tdd.setClaimDirectorTelphone(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorTelphone()).trim(),errInfoStr,errBo,"索赔主管手机",30));//索赔主管手机
							tdd.setClaimDirectorEmail(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorEmail()).trim(),errInfoStr,errBo,"索赔主管邮箱",50));//索赔主管邮箱
							tdd.setClaimDirectorFax(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorFax()).trim(),errInfoStr,errBo,"索赔传真",30));//索赔传真
							tdd.setTechnologyDirectorName(checkVarchar(CommonUtils.checkNull(p.getTechnologyDirectorName()).trim(),errInfoStr,errBo,"技术主管",30));//技术主管
							tdd.setTechnologyDirectorTelphone(checkVarchar(CommonUtils.checkNull(p.getTechnologyDirectorTelphone()).trim(),errInfoStr,errBo,"技术主管手机",30));//技术主管手机
							tdd.setUnionType(checkVarchar(CommonUtils.checkNull(p.getUnionType()).trim(),errInfoStr,errBo,"单位性质",50));//单位性质
							tdd.setFixedCapital(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getFixedCapital()).trim(),errInfoStr,errBo,"固定资产（万元）",15)));//固定资产（万元）
							tdd.setRegisteredCapital(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getRegisteredCapital()).trim(),errInfoStr,errBo,"注册资金（万元）/企业注册资金",15)));//注册资金（万元）/企业注册资金
							tdd.setPeopleNumber(Integer.parseInt(checkVarchar(CommonUtils.checkNullNum(p.getPeopleNumber()).trim(),errInfoStr,errBo,"人数",8)));//人数
							tdd.setPartsArea(Long.parseLong(checkVarchar(CommonUtils.checkNullNum(p.getPartsArea()).trim(),errInfoStr,errBo,"配件库面积（平方米）",10)));//配件库面积（平方米）
							tdd.setDepotArea(Long.parseLong(checkVarchar(CommonUtils.checkNullNum(p.getDepotArea()).trim(),errInfoStr,errBo,"停车场面积（平方米）",10)));//停车场面积（平方米）
							tdd.setMainArea(Long.parseLong(checkVarchar(CommonUtils.checkNullNum(p.getMainArea()).trim(),errInfoStr,errBo,"维修车间面积（平方米）",10)));//维修车间面积（平方米）
							tdd.setOnlyMonthCount(Integer.parseInt(checkVarchar(CommonUtils.checkNullNum(p.getOnlyMonthCount()).trim(),errInfoStr,errBo,"月平均维修能力(台次)",8)));//月平均维修能力(台次)
							tdd.setOpeningTime(checkVarchar(CommonUtils.checkNull(p.getOpeningTime()).trim(),errInfoStr,errBo,"营业时间",50));//营业时间
							tdd.setWorkType(checkVarchar(CommonUtils.checkNull(p.getWorkType()).trim(),errInfoStr,errBo,"经营类型",50));//经营类型
							tdd.setIsFourS(checkVarchar(CommonUtils.checkNull(p.getIsFourS()).trim(),errInfoStr,errBo,"是否4S店",8));//是否4S店
							tdd.setIsLowSer(checkVarchar(CommonUtils.checkNull(p.getIsLowSer()).trim(),errInfoStr,errBo,"是否有二级服务商",8));//是否有二级服务商
							tdd.setAuthorizationType(checkVarchar(CommonUtils.checkNull(p.getAuthorizationType()).trim(),errInfoStr,errBo,"企业授权类型",30));//企业授权类型
							tdd.setAuthorizationDate(Utility.getDate(CommonUtils.checkNull(p.getAuthorizationDate()).trim(),1));//授权时间
							tdd.setIsActingBrand(CommonUtils.checkNull(p.getIsActingBrand()).trim().equals("是")?Constant.IF_TYPE_YES:Constant.IF_TYPE_YES);//是否经营其他品牌
							tdd.setActingBrandName(checkVarchar(CommonUtils.checkNull(p.getActingBrandName()).trim(),errInfoStr,errBo,"代理其它品牌名称",30));//代理其它品牌名称
							tdd.setPartsStoreAmount(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getPartsStoreAmount()).trim(),errInfoStr,errBo,"配件储备金额（万元）",15)));//配件储备金额（万元）
							tdd.setCompanyZcCode(checkVarchar(CommonUtils.checkNull(p.getCompanyZcCode()).trim(),errInfoStr,errBo,"组织机构代码",30));//组织机构代码
							tdd.setDealerRelation(checkVarchar(CommonUtils.checkNull(p.getDealerRelation()).trim(),errInfoStr,errBo,"与一级经销商关系",30));//与一级经销商关系
							tdd.setMeetingRoomArea(Integer.parseInt(checkVarchar(CommonUtils.checkNullNum(p.getPartsArea()).trim(),errInfoStr,errBo,"接待室面积（平方米）",8)));//接待室面积（平方米）
							//tdd.seti
							
							tdd.setFittingsDecName(checkVarchar(CommonUtils.checkNull(p.getFittingsDecName()).trim(),errInfoStr,errBo,"配件主管",50));//配件主管
							tdd.setFittingsDecPhone(checkVarchar(CommonUtils.checkNull(p.getFittingsDecPhone()).trim(),errInfoStr,errBo,"配件主管办公电话",30));//配件主管办公电话
							tdd.setFittingsDecTelphone(checkVarchar(CommonUtils.checkNull(p.getFittingsDecTelphone()).trim(),errInfoStr,errBo,"配件主管手机",30));//配件主管手机
							tdd.setFittingsDecEmail(checkVarchar(CommonUtils.checkNull(p.getFittingsDecEmail()).trim(),errInfoStr,errBo,"配件主管邮箱",30));//配件主管邮箱
							tdd.setFittingsDecFax(checkVarchar(CommonUtils.checkNull(p.getFittingsDecFax()).trim(),errInfoStr,errBo,"配件传真",30));//配件传真
							tdd.setFinanceManagerName(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerName()).trim(),errInfoStr,errBo,"财务经理",30));//财务经理
							tdd.setFinanceManagerPhone(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerPhone()).trim(),errInfoStr,errBo,"财务经理办公电话",30));//财务经理办公电话
							tdd.setFinanceManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerTelphone()).trim(),errInfoStr,errBo,"财务经理手机",30));//财务经理手机
							tdd.setFinanceManagerEmail(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerEmail()).trim(),errInfoStr,errBo,"财务经理邮箱",30));//财务经理邮箱
							
							
							tdd.setRemark(checkVarchar(CommonUtils.checkNull(p.getRemark()).trim(),errInfoStr,errBo,"备注",200));//备注
						tdd.setSerDirectorName(checkVarchar(CommonUtils.checkNull(p.getSerDirectorName()).trim(),errInfoStr,errBo,"服务主管",30));//服务主管
						tdd.setSerDirectorPhone(checkVarchar(CommonUtils.checkNull(p.getSerDirectorPhone()).trim(),errInfoStr,errBo,"服务主管办公电话",30));//服务主管办公电话
						tdd.setSerDirectorTelhone(checkVarchar(CommonUtils.checkNull(p.getSerDirectorTelhone()).trim(),errInfoStr,errBo,"服务主管手机",30));//服务主管手机
						tdd.setCompanyAddress(checkVarchar(CommonUtils.checkNull(p.getCompanyAddress()).trim(),errInfoStr,errBo,"公司地址/销售展厅地址",300));//公司地址/销售展厅地址
						tdd.setHotline(checkVarchar(CommonUtils.checkNull(p.getHotline()).trim(),errInfoStr,errBo,"24小时热线",30));//24小时热线
						
						tdd.setProxyVehicleType(CommonUtils.checkNull(p.getProxyVehicleType()).trim());//代理车型
						tdd.setProxyArea(CommonUtils.checkNull(p.getProxyArea()).trim());//代理区域
						
						//判断是否有错误信息,有就不执行插入或更新操作
						if(errBo){
							errorMap.put("rowNum", p.getRowNum());//错误行号
							errorMap.put("errorInfo", errInfoStr);//错误信息
							errorJsp.add(errorMap);//保存错误信息
							continue;
						}
						//首先判断是否有公司，有公司就修改公司信息，再添加经销商
						List<Map<String, Object>> comList=dao.getCompany(CommonUtils.checkNull(p.getDealerCode().split("-")[0]).trim());
						//如果公司有 更新
						if(comList!=null && comList.size()>0){
							tcpo.setUpdateBy(logonUser.getUserId());//创建人
							tcpo.setUpdateDate(new Date());
							//更新公司表数据
							TmCompanyPO tcpo1=new TmCompanyPO();
							tcpo1.setCompanyCode(CommonUtils.checkNull(p.getDealerCode().split("-")[0]).trim());
							dao.update(tcpo1, tcpo);
						}else{//无 添加公司表数据
							tcpo.setCompanyId(Long.parseLong(SequenceManager.getSequence("")));
							tcpo.setCreateBy(logonUser.getUserId());//创建人
							tcpo.setCreateDate(new Date());
							dao.insert(tcpo);
						}
						//获取公司ID
						TmCompanyPO c1=new TmCompanyPO();
						c1.setCompanyCode(CommonUtils.checkNull(p.getDealerCode().split("-")[0]).trim());
						TmCompanyPO tmc=(TmCompanyPO)dao.select(c1).get(0);
						//公司
						tdpo.setCompanyId(tmc.getCompanyId());//公司ID
						//无公司添加公司，在添加经销商
						hiDealerId=SequenceManager.getSequence("");
						tdpo.setDealerId(Long.parseLong(hiDealerId));
						dao.insert(tdpo);
						//首先清除经销商与组织中间表的数据
						dao.delRation(hiDealerId);
						//添加经销商与组织中间表的数据
						TmDealerOrgRelationPO cc=new TmDealerOrgRelationPO();
						cc.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
						cc.setDealerId(Long.parseLong(hiDealerId));
						cc.setOrgId(Long.parseLong(hiOrgId));
						cc.setBusinessType(Constant.ORG_TYPE_OEM);
						dao.insert(cc);
						//查询经销商与组织中间表是否有数据，无的话就添加
						/*List<Map<String, Object>> toi=dao.getDO(CommonUtils.checkNull(p.getDealerCode()).trim(),CommonUtils.checkNull(p.getProvinceId()).trim(),"-1");
						if(toi!=null && toi.size()>0){
							
						}else{
							TmDealerOrgRelationPO cc=new TmDealerOrgRelationPO();
							cc.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
							cc.setDealerId(Long.parseLong(hiDealerId));
							cc.setOrgId(Long.parseLong(hiOrgId));
							cc.setBusinessType(Constant.ORG_TYPE_OEM);
							dao.insert(cc);
						}*/
						//新增经销商明显表
						tdd.setDetailId(Long.parseLong(SequenceManager.getSequence("")));//明显ID
						tdd.setFkDealerId(Long.parseLong(hiDealerId));//经销商ID
						dao.insert(tdd);
			//			调用存储过程 保存经销商的旧件回运时间
						Utility.setDealerType(hiDealerId,"P_DEALER_DATE", conn);
			//			将该经销商的配件加价率默认为0,否则开工单时查不出配件
						Utility.setDealerType(hiDealerId,"P_TM_DOWN_PARAMETER", conn);
						
					}
				}
			}
			POContext.endTxn(true);//added by lijj 2014-05-21
			//经销商（入查询到就更新 否则添加）
			//根据经销商代码判断规则[公司代码-SO1] 销售是SO1 售后是SH1 判断是否有数据
			act.setOutData("errorJsp", errorJsp);
			act.setForword(shDealerImportCompleteUrl);
		} catch (Exception e) {
			POContext.endTxn(false);//added by lijj 2014-05-21
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}finally{
			//关闭连接
			if(conn!=null){
				try {
					conn.close();
					POContext.cleanTxn();//added by lijj 2014-05-21
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void importExcel2nd(){
		ActionContext act = ActionContext.getContext();
		ShDealerDao dao=ShDealerDao.getInstance();
		Connection conn = DBService.getInstance().getConnection();
		TmpShDealerSecendPO ppo=new TmpShDealerSecendPO();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String,Object>> errorJsp=new ArrayList();
			List<PO> l=dao.select(ppo);
			if(l!=null && l.size()>0){
				for(int i=0;i<l.size();i++){
					Map<String,Object> errorMap=new HashMap<String,Object>();
					TmpShDealerSecendPO p=(TmpShDealerSecendPO)l.get(i);
					String hiDealerId="";//经销商ID 中间表保存需要
					String hiOrgId="";//组织ID 中间表保存需要
					//Long rowNumMsg=p.getRowNumber();//行号信息
					 errInfoStr="";//记录错误信息
					 errBo=false;//判断是否有错误信息
					TmDealerSecendPO po=new TmDealerSecendPO();
					po.setDealerCode(CommonUtils.checkNull(p.getDealerCode()).trim());
					List<PO> ps=dao.select(po);
					if(ps.size()>0){//首先判断时候经销商是否有，有就更新公司表，经销商表[有]
						hiDealerId=((TmDealerSecendPO)ps.get(0)).getDealerId().toString();
						//公司表数据
						TmCompanyPO tcpo=new TmCompanyPO();
						tcpo.setCompanyCode(CommonUtils.checkNull(p.getDealerCode()).trim().split("-")[0]);//截取经销商编码“-”前一部分
						tcpo.setCompanyName(CommonUtils.checkNull(p.getDealerName()).trim());//公司全称
						tcpo.setCompanyShortname(CommonUtils.checkNull(p.getDealerShortname()).trim());//公司简称
						tcpo.setStatus(Constant.STATUS_ENABLE);//有效的
						tcpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));//默认
						tcpo.setCompanyType(Integer.parseInt(Constant.COMPANY_TYPE_DEALER));//公司类型
						
						//经销商表
						TmDealerSecendPO tdpo=new TmDealerSecendPO();
						String PId="";//省份ID下面调用
						String CId="";//城市ID下面调用
						String CCId="";//地区ID下面调用
						List<Map<String, Object>> proId=dao.getProvinceId(CommonUtils.checkNull(p.getProvinceId()).trim());
						if(proId!=null && proId.size()==1){
							PId=proId.get(0).get("REGION_CODE").toString();
							tdpo.setProvinceId(Long.parseLong(PId));//省份
							List<Map<String, Object>> cId=dao.getCityId(proId.get(0).get("REGION_ID").toString(),CommonUtils.checkNull(p.getCityId()).trim());
							if(cId!=null && cId.size()==1){
								CId=cId.get(0).get("REGION_CODE").toString();
								tdpo.setCityId(Long.parseLong(CId));//城市
								List<Map<String, Object>> coId=dao.getContinusId(cId.get(0).get("REGION_ID").toString(),CommonUtils.checkNull(p.getCounties()).trim());
								if(coId!=null && coId.size()==1){
									CCId=coId.get(0).get("REGION_CODE").toString();
									tdpo.setCounties(Integer.parseInt(CCId));//地区
								}else{
									errInfoStr+="区县输入有误！";
									errBo=true;
								}
							}else{
								errInfoStr+="城市输入有误！";
								errBo=true;
							}
						}else{
							errInfoStr+="省份输入有误！";
							errBo=true;
						}
						tdpo.setDealerCode(checkVarchar(CommonUtils.checkNull(p.getDealerCode()).trim(),errInfoStr,errBo,"经销商代码",20));//经销商CODE
						tdpo.setDealerName(checkVarchar(CommonUtils.checkNull(p.getDealerName()).trim(),errInfoStr,errBo,"经销商名称",300));//经销商名称
						tdpo.setDealerShortname(checkVarchar(CommonUtils.checkNull(p.getDealerShortname()).trim(),errInfoStr,errBo,"经销商简称",150));//经销商简称
						if(CommonUtils.checkNull(p.getDealerType()).trim().equals("一级")){
							tdpo.setDealerLevel(Constant.DEALER_LEVEL_01);//经销商等级
						}else if(CommonUtils.checkNull(p.getDealerType()).trim().equals("二级")){
							tdpo.setDealerLevel(Constant.DEALER_LEVEL_02);//经销商等级
						}else{
							errInfoStr+="服务商等级输入有误【只能输入一级或二级】！";
							errBo=true;
						}
						tdpo.setStatus(Constant.STATUS_ENABLE);//经销商状态
							tdpo.setZipCode(checkVarchar(CommonUtils.checkNull(p.getZipCode()).trim(),errInfoStr,errBo,"邮编",10));//邮编
							tdpo.setAddress(checkVarchar(CommonUtils.checkNull(p.getAddress()).trim(),errInfoStr,errBo,"企业注册地址",300));//企业注册地址
							tdpo.setZccode(checkVarchar(CommonUtils.checkNull(p.getZccode()).trim(),errInfoStr,errBo,"注册证号",50));//注册证号
							tdpo.setZyScope(checkVarchar(CommonUtils.checkNull(p.getZyScope()).trim(),errInfoStr,errBo,"主营范围",300));//主营范围
							tdpo.setJyScope(checkVarchar(CommonUtils.checkNull(p.getJyScope()).trim(),errInfoStr,errBo,"兼营范围",300));//兼营范围
							tdpo.setLegal(checkVarchar(CommonUtils.checkNull(p.getLegal()).trim(),errInfoStr,errBo,"法人",20));//法人
							tdpo.setBeginBank(checkVarchar(CommonUtils.checkNull(p.getBeginBank()).trim(),errInfoStr,errBo,"开户行",300));//开户行
							tdpo.setErpCode(checkVarchar(CommonUtils.checkNull(p.getErpCode()).trim(),errInfoStr,errBo,"/开票名称/开票公司名称",100));//开票名称/开票公司名称
							tdpo.setInvoiceAccount(checkVarchar(CommonUtils.checkNull(p.getInvoiceAccount()).trim(),errInfoStr,errBo,"银行帐号",100));//银行帐号
							tdpo.setInvoicePhone(checkVarchar(CommonUtils.checkNull(p.getInvoicePhone()).trim(),errInfoStr,errBo,"开票联系人电话",50));//开票电话/开票联系人电话
							tdpo.setInvoiceAdd(checkVarchar(CommonUtils.checkNull(p.getInvoiceAdd()).trim(),errInfoStr,errBo,"开票地址",200));//开票地址
							tdpo.setTaxpayerNo(checkVarchar(CommonUtils.checkNull(p.getTaxpayerNo()).trim(),errInfoStr,errBo,"纳税人识别号",20));//纳税人识别号
							tdpo.setTaxpayerNature(checkVarchar(CommonUtils.checkNull(p.getTaxpayerNature()).trim(),errInfoStr,errBo,"纳税人性质",50));//纳税人性质
							tdpo.setTaxInvoice(checkVarchar(CommonUtils.checkNull(p.getTaxInvoice()).trim(),errInfoStr,errBo,"增值税发票",30));//增值税发票
							tdpo.setTaxDisrate(checkVarchar(CommonUtils.checkNull(p.getTaxDisrate()).trim(),errInfoStr,errBo,"开票税率",30));//开票税率
							
							if(CommonUtils.checkNull(p.getSiteDate()).trim().contains("-")){
								tdpo.setSitedate(Utility.getDate(CommonUtils.checkNull(p.getSiteDate()).trim(),1));//建厂时间(建站时间)
							}else if(CommonUtils.checkNull(p.getSiteDate()).trim().contains("/")){
								tdpo.setSitedate(Utility.getDate(CommonUtils.checkNull(p.getSiteDate()).trim(),2));//建厂时间(建站时间)
							}else{
								errInfoStr+="建厂时间格式必须是2014-01-14或者2014/01/14!";
								errBo=true;
							}
							
							tdpo.setDealerType(Constant.DEALER_TYPE_DWR);//经销商类型
							tdpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));//默认
							tdpo.setSpyMan(checkVarchar(CommonUtils.checkNull(p.getSpyMan()).trim(),errInfoStr,errBo,"索赔员",20));//索赔员
							tdpo.setPhone(checkVarchar(CommonUtils.checkNull(p.getPhone()).trim(),errInfoStr,errBo,"服务商电话",100));//服务商电话
							//tdpo.setTaxesNo(checkVarchar(CommonUtils.checkNull(p.getTaxesNo()).trim(),errInfoStr,errBo,"税号",50));//税号
							if(CommonUtils.checkNull(p.getImageLevel()).trim().equals("")){
								
							}else{
								String deaLev=CodeDict.getDictCodeByName(Constant.IMAGE_LEVEL.toString(), p.getImageLevel().trim());
								if(deaLev!=null){
									tdpo.setImageLevel(Integer.parseInt(deaLev));
								}else{
									errInfoStr+="拟建店类别输入有误【A类;B类;C类;D类;F类】！";
									errBo=true;
								}
							}
							//服务状态
							String serName=CodeDict.getDictCodeByName(Constant.DLR_SERVICE_STATUS, p.getServiceStatus());
							if(serName!=null){
								tdpo.setServiceStatus(Long.parseLong(serName));
							}else{
								errInfoStr+="服务状态输入有误【只能输入{意向,正常,拟撤,已撤}】！";
								errBo=true;
							}
							//维修资质
							String mrces=CodeDict.getDictCodeByName(Constant.MAIN_RESOURCES.toString(), p.getMainResources());
							if(mrces!=null){
								tdpo.setMainResources(Integer.parseInt(mrces));
							}else{
								errInfoStr+="维修资质输入有误【只能输入{一类,二类,三类,无}】！";
								errBo=true;
							}
							
						tdpo.setBalanceLevel(CommonUtils.checkNull(p.getBalanceLevel()).trim().equals("独立结算")?Constant.BALANCE_LEVEL_SELF:Constant.BALANCE_LEVEL_HIGH);//结算等级
						tdpo.setInvoiceLevel(CommonUtils.checkNull(p.getInvoiceLevel()).trim().equals("独立开票")?Constant.INVOICE_LEVEL_SELF:Constant.INVOICE_LEVEL_HIGH);//开票等级
						tdpo.setUpdateDate(new Date());

						//根据大区名称获取大区代码
						List<Map<String, Object>> bdd=dao.getBigId(CommonUtils.checkNull(p.getDealerOrgId()).trim());
						if(bdd!=null && bdd.size()==1){//有就设置
							hiOrgId=bdd.get(0).get("ORG_ID").toString();
							tdpo.setDealerOrgId(Long.parseLong(hiOrgId));//大区
						}else{
							errInfoStr+="所属组织输入有误！";
							errBo=true;
						}
						List<Map<String, Object>> pdd=dao.getPDealer(CommonUtils.checkNull(p.getParentDealerD()).trim());
						if(pdd!=null && pdd.size()==1){//有就设置
							tdpo.setParentDealerD(Long.parseLong(pdd.get(0).get("DEALER_ID").toString()));//上级经销商
						}else{
							if(CommonUtils.checkNull(p.getDealerType()).trim().equals("二级")){
								errInfoStr+="上级单位输入有误,请确认是否输错(必须全称)!";
								errBo=true;
							}
							//tdpo.setParentDealerD(-1L);//上级经销商
						}
						//经销商明显表数据插入
						TmDealerDetailPO tdd=new TmDealerDetailPO();
							tdd.setTheAgents(checkVarchar(CommonUtils.checkNull(p.getTheAgents()).trim(),errInfoStr,errBo,"辐射区域",300));//辐射区域
							tdd.setSerManagerName(checkVarchar(CommonUtils.checkNull(p.getSerManagerName()).trim(),errInfoStr,errBo,"服务经理",30));//服务经理
							tdd.setSerManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getSerManagerTelphone()).trim(),errInfoStr,errBo,"服务经理手机",30));//服务经理手机
							tdd.setSerManagerEmail(checkVarchar(CommonUtils.checkNull(p.getSerManagerEmail()).trim(),errInfoStr,errBo,"服务经理邮箱",50));//服务经理邮箱
							tdd.setClaimDirectorName(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorName()).trim(),errInfoStr,errBo,"索赔主管",30));//索赔主管
							tdd.setClaimDirectorPhone(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorPhone()).trim(),errInfoStr,errBo,"索赔主管办公电话",30));//索赔主管办公电话
							tdd.setClaimDirectorTelphone(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorTelphone()).trim(),errInfoStr,errBo,"索赔主管手机",30));//索赔主管手机
							tdd.setClaimDirectorEmail(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorEmail()).trim(),errInfoStr,errBo,"索赔主管邮箱",50));//索赔主管邮箱
							tdd.setClaimDirectorFax(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorFax()).trim(),errInfoStr,errBo,"索赔传真",30));//索赔传真
							tdd.setTechnologyDirectorName(checkVarchar(CommonUtils.checkNull(p.getTechnologyDirectorName()).trim(),errInfoStr,errBo,"技术主管",30));//技术主管
							tdd.setTechnologyDirectorTelphone(checkVarchar(CommonUtils.checkNull(p.getTechnologyDirectorTelphone()).trim(),errInfoStr,errBo,"技术主管手机",30));//技术主管手机
							tdd.setUnionType(checkVarchar(CommonUtils.checkNull(p.getUnionType()).trim(),errInfoStr,errBo,"单位性质",50));//单位性质
							tdd.setFixedCapital(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getFixedCapital()).trim(),errInfoStr,errBo,"固定资产（万元）",15)));//固定资产（万元）
							tdd.setRegisteredCapital(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getRegisteredCapital()).trim(),errInfoStr,errBo,"注册资金（万元）/企业注册资金",15)));//注册资金（万元）/企业注册资金
							tdd.setPeopleNumber(Integer.parseInt(checkVarchar(CommonUtils.checkNullNum(p.getPeopleNumber()).trim(),errInfoStr,errBo,"人数",8)));//人数
							tdd.setPartsArea(Long.parseLong(checkVarchar(CommonUtils.checkNullNum(p.getPartsArea()).trim(),errInfoStr,errBo,"配件库面积（平方米）",10)));//配件库面积（平方米）
							tdd.setDepotArea(Long.parseLong(checkVarchar(CommonUtils.checkNullNum(p.getDepotArea()).trim(),errInfoStr,errBo,"停车场面积（平方米）",10)));//停车场面积（平方米）
							tdd.setMainArea(Long.parseLong(checkVarchar(CommonUtils.checkNullNum(p.getMainArea()).trim(),errInfoStr,errBo,"维修车间面积（平方米）",10)));//维修车间面积（平方米）
							tdd.setOnlyMonthCount(Integer.parseInt(checkVarchar(CommonUtils.checkNullNum(p.getOnlyMonthCount()).trim(),errInfoStr,errBo,"月平均维修能力(台次)",8)));//月平均维修能力(台次)
							tdd.setOpeningTime(checkVarchar(CommonUtils.checkNull(p.getOpeningTime()).trim(),errInfoStr,errBo,"营业时间",50));//营业时间
							tdd.setWorkType(checkVarchar(CommonUtils.checkNull(p.getWorkType()).trim(),errInfoStr,errBo,"经营类型",50));//经营类型
							tdd.setIsFourS(checkVarchar(CommonUtils.checkNull(p.getIsFourS()).trim(),errInfoStr,errBo,"是否4S店",8));//是否4S店
							tdd.setAuthorizationType(checkVarchar(CommonUtils.checkNull(p.getAuthorizationType()).trim(),errInfoStr,errBo,"企业授权类型",30));//企业授权类型
							
							if(CommonUtils.checkNull(p.getAuthorizationDate()).trim().contains("-")){
								tdd.setAuthorizationDate(Utility.getDate(CommonUtils.checkNull(p.getAuthorizationDate()).trim(),1));//授权时间
							}else if(CommonUtils.checkNull(p.getAuthorizationDate()).trim().contains("/")){
								tdpo.setSitedate(Utility.getDate(CommonUtils.checkNull(p.getAuthorizationDate()).trim(),2));//授权时间
							}else{
								errInfoStr+="授权时间格式必须是2014-01-14或者2014/01/14!";
								errBo=true;
							}
							
							tdd.setIsActingBrand(CommonUtils.checkNull(p.getIsActingBrand()).trim().equals("是")?Constant.IF_TYPE_YES:Constant.IF_TYPE_YES);//是否经营其他品牌
							tdd.setActingBrandName(checkVarchar(CommonUtils.checkNull(p.getActingBrandName()).trim(),errInfoStr,errBo,"代理其它品牌名称",200));//代理其它品牌名称
							tdd.setPartsStoreAmount(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getPartsStoreAmount()).trim(),errInfoStr,errBo,"配件储备金额（万元）",15)));//配件储备金额（万元）
							tdd.setCompanyZcCode(checkVarchar(CommonUtils.checkNull(p.getCompanyZcCode()).trim(),errInfoStr,errBo,"组织机构代码",30));//组织机构代码
							tdd.setDealerRelation(checkVarchar(CommonUtils.checkNull(p.getDealerRelation()).trim(),errInfoStr,errBo,"与一级经销商关系",30));//与一级经销商关系
							tdd.setMeetingRoomArea(Integer.parseInt(checkVarchar(CommonUtils.checkNullNum(p.getReceiveArea()).trim(),errInfoStr,errBo,"接待室面积（平方米）",8)));//接待室面积（平方米）
							//tdd.seti
							
							tdd.setFittingsDecName(checkVarchar(CommonUtils.checkNull(p.getFittingsDecName()).trim(),errInfoStr,errBo,"配件主管",50));//配件主管
							tdd.setFittingsDecPhone(checkVarchar(CommonUtils.checkNull(p.getFittingsDecPhone()).trim(),errInfoStr,errBo,"配件主管办公电话",30));//配件主管办公电话
							tdd.setFittingsDecTelphone(checkVarchar(CommonUtils.checkNull(p.getFittingsDecTelphone()).trim(),errInfoStr,errBo,"配件主管手机",30));//配件主管手机
							tdd.setFittingsDecEmail(checkVarchar(CommonUtils.checkNull(p.getFittingsDecEmail()).trim(),errInfoStr,errBo,"配件主管邮箱",30));//配件主管邮箱
							tdd.setFittingsDecFax(checkVarchar(CommonUtils.checkNull(p.getFittingsDecFax()).trim(),errInfoStr,errBo,"配件传真",30));//配件传真
							tdd.setFinanceManagerName(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerName()).trim(),errInfoStr,errBo,"财务经理",30));//财务经理
							tdd.setFinanceManagerPhone(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerPhone()).trim(),errInfoStr,errBo,"财务经理办公电话",30));//财务经理办公电话
							tdd.setFinanceManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerTelphone()).trim(),errInfoStr,errBo,"财务经理手机",30));//财务经理手机
							tdd.setFinanceManagerEmail(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerEmail()).trim(),errInfoStr,errBo,"财务经理邮箱",30));//财务经理邮箱
							
							
							tdd.setRemark(checkVarchar(CommonUtils.checkNull(p.getRemark()).trim(),errInfoStr,errBo,"备注",200));//备注
						tdd.setSerDirectorName(checkVarchar(CommonUtils.checkNull(p.getSerDirectorName()).trim(),errInfoStr,errBo,"服务主管",30));//服务主管
						tdd.setSerDirectorPhone(checkVarchar(CommonUtils.checkNull(p.getSerDirectorPhone()).trim(),errInfoStr,errBo,"服务主管办公电话",30));//服务主管办公电话
						tdd.setSerDirectorTelhone(checkVarchar(CommonUtils.checkNull(p.getSerDirectorTelhone()).trim(),errInfoStr,errBo,"服务主管手机",30));//服务主管手机
						tdd.setCompanyAddress(checkVarchar(CommonUtils.checkNull(p.getCompanyAddress()).trim(),errInfoStr,errBo,"公司地址/销售展厅地址",300));//公司地址/销售展厅地址
						tdd.setHotline(checkVarchar(CommonUtils.checkNull(p.getHotline()).trim(),errInfoStr,errBo,"24小时热线",30));//24小时热线
						
						
						//判断是否有错误信息,有就不执行插入或更新操作
						if(errBo){
							errorMap.put("rowNum", p.getRowNum());//错误行号
							errorMap.put("errorInfo", errInfoStr);//错误信息
							errorJsp.add(errorMap);//保存错误信息
							continue;
						}
						List<Map<String, Object>> comList=dao.getCompany(CommonUtils.checkNull(p.getDealerCode().split("-")[0]).trim());
						//如果公司有 更新
						if(comList!=null && comList.size()>0){
							//companyId=((TmCompanyPO)comList.get(0)).getCompanyId().toString();
							tcpo.setUpdateBy(logonUser.getUserId());//创建人
							tcpo.setUpdateDate(new Date());
							//更新公司表数据
							TmCompanyPO tcpo1=new TmCompanyPO();
							tcpo1.setCompanyCode(CommonUtils.checkNull(p.getDealerCode().split("-")[0]).trim());
							dao.update(tcpo1, tcpo);
						}else{//无 添加公司表数据
							tcpo.setCreateBy(logonUser.getUserId());//创建人
							tcpo.setCreateDate(new Date());
							//companyId=SequenceManager.getSequence("");
							tcpo.setCompanyId(Long.parseLong(SequenceManager.getSequence("")));
							dao.insert(tcpo);
						}
						//获取公司ID
						TmCompanyPO c1=new TmCompanyPO();
						c1.setCompanyCode(CommonUtils.checkNull(p.getDealerCode().split("-")[0]).trim());
						TmCompanyPO tmc=(TmCompanyPO)dao.select(c1).get(0);
						//公司
						tdpo.setCompanyId(tmc.getCompanyId());//公司ID
						//更新经销商表
						TmDealerSecendPO tdpo1=new TmDealerSecendPO();
						tdpo1.setDealerCode(CommonUtils.checkNull(p.getDealerCode()).trim());
						dao.update(tdpo1, tdpo);
						//首先清除经销商与组织中间表的数据
						dao.delRation(hiDealerId);
						//添加经销商与组织中间表的数据
						TmDealerOrgRelationSecendPO cc=new TmDealerOrgRelationSecendPO();
						cc.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
						cc.setDealerId(Long.parseLong(hiDealerId));
						cc.setOrgId(Long.parseLong(hiOrgId));
						cc.setBusinessType(Constant.ORG_TYPE_OEM);
						dao.insert(cc);
						//更新经销商明显表
						TmDealerDetailPO tdd2=new TmDealerDetailPO();
						tdd2.setFkDealerId(Long.parseLong(hiDealerId));
						List<PO> plist=dao.select(tdd2);
						if(plist!=null && plist.size()>0){
							//更新经销商明显表
							TmDealerDetailPO tdd1=new TmDealerDetailPO();
							tdd1.setFkDealerId(Long.parseLong(hiDealerId));
							dao.update(tdd1, tdd);
						}else{
							//新增
							TmDealerDetailPO tdd1=new TmDealerDetailPO();
							tdd.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
							tdd.setFkDealerId(Long.parseLong(hiDealerId));
							tdd.setUpdateDate(new Date());
							dao.insert(tdd);
						}
						
					}else{//无该经销商
						//公司表数据
						TmCompanyPO tcpo=new TmCompanyPO();
						tcpo.setCompanyCode(CommonUtils.checkNull(p.getDealerCode()).trim().split("-")[0]);//截取经销商编码“-”前一部分
						tcpo.setCompanyName(CommonUtils.checkNull(p.getDealerName()).trim());//公司全称
						tcpo.setCompanyShortname(CommonUtils.checkNull(p.getDealerShortname()).trim());//公司简称
						tcpo.setStatus(Constant.STATUS_ENABLE);//有效的
						tcpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));//默认
						tcpo.setCompanyType(Integer.parseInt(Constant.COMPANY_TYPE_DEALER));//公司类型
						
						//经销商表
						TmDealerSecendPO tdpo=new TmDealerSecendPO();
						String PId="";//省份ID下面调用
						String CId="";//城市ID下面调用
						String CCId="";//地区ID下面调用
						List<Map<String, Object>> proId=dao.getProvinceId(CommonUtils.checkNull(p.getProvinceId()).trim());
						if(proId!=null && proId.size()==1){
							PId=proId.get(0).get("REGION_CODE").toString();
							tdpo.setProvinceId(Long.parseLong(PId));//省份
							List<Map<String, Object>> cId=dao.getCityId(proId.get(0).get("REGION_ID").toString(),CommonUtils.checkNull(p.getCityId()).trim());
							if(cId!=null && cId.size()==1){
								CId=cId.get(0).get("REGION_CODE").toString();
								tdpo.setCityId(Long.parseLong(CId));//城市
								List<Map<String, Object>> coId=dao.getContinusId(cId.get(0).get("REGION_ID").toString(),CommonUtils.checkNull(p.getCounties()).trim());
								if(coId!=null && coId.size()==1){
									CCId=coId.get(0).get("REGION_CODE").toString();
									tdpo.setCounties(Integer.parseInt(CCId));//地区
								}else{
									errInfoStr+="区县输入有误！";
									errBo=true;
								}
							}else{
								errInfoStr+="城市输入有误！";
								errBo=true;
							}
						}else{
							errInfoStr+="省份输入有误！";
							errBo=true;
						}
						tdpo.setDealerCode(checkVarchar(CommonUtils.checkNull(p.getDealerCode()).trim(),errInfoStr,errBo,"经销商代码",20));//经销商CODE
						tdpo.setDealerName(checkVarchar(CommonUtils.checkNull(p.getDealerName()).trim(),errInfoStr,errBo,"经销商名称",300));//经销商名称
						tdpo.setDealerShortname(checkVarchar(CommonUtils.checkNull(p.getDealerShortname()).trim(),errInfoStr,errBo,"经销商简称",150));//经销商简称
						if(CommonUtils.checkNull(p.getDealerType()).trim().equals("一级")){
							tdpo.setDealerLevel(Constant.DEALER_LEVEL_01);//经销商等级
						}else if(CommonUtils.checkNull(p.getDealerType()).trim().equals("二级")){
							tdpo.setDealerLevel(Constant.DEALER_LEVEL_02);//经销商等级
						}else{
							errInfoStr+="服务商等级输入有误【只能输入一级或二级】！";
							errBo=true;
						}
							tdpo.setStatus(Constant.STATUS_ENABLE);//经销商状态
							tdpo.setZipCode(checkVarchar(CommonUtils.checkNull(p.getZipCode()).trim(),errInfoStr,errBo,"邮编",10));//邮编
							tdpo.setAddress(checkVarchar(CommonUtils.checkNull(p.getAddress()).trim(),errInfoStr,errBo,"企业注册地址",300));//企业注册地址
							tdpo.setZccode(checkVarchar(CommonUtils.checkNull(p.getZccode()).trim(),errInfoStr,errBo,"注册证号",50));//注册证号
							tdpo.setZyScope(checkVarchar(CommonUtils.checkNull(p.getZyScope()).trim(),errInfoStr,errBo,"主营范围",300));//主营范围
							tdpo.setJyScope(checkVarchar(CommonUtils.checkNull(p.getJyScope()).trim(),errInfoStr,errBo,"兼营范围",300));//兼营范围
							tdpo.setLegal(checkVarchar(CommonUtils.checkNull(p.getLegal()).trim(),errInfoStr,errBo,"法人",20));//法人
							tdpo.setBeginBank(checkVarchar(CommonUtils.checkNull(p.getBeginBank()).trim(),errInfoStr,errBo,"开户行",300));//开户行
							tdpo.setErpCode(checkVarchar(CommonUtils.checkNull(p.getErpCode()).trim(),errInfoStr,errBo,"/开票名称/开票公司名称",100));//开票名称/开票公司名称
							tdpo.setInvoiceAccount(checkVarchar(CommonUtils.checkNull(p.getInvoiceAccount()).trim(),errInfoStr,errBo,"银行帐号",100));//银行帐号
							tdpo.setInvoicePhone(checkVarchar(CommonUtils.checkNull(p.getInvoicePhone()).trim(),errInfoStr,errBo,"开票联系人电话",50));//开票电话/开票联系人电话
							tdpo.setInvoiceAdd(checkVarchar(CommonUtils.checkNull(p.getInvoiceAdd()).trim(),errInfoStr,errBo,"开票地址",200));//开票地址
							tdpo.setTaxpayerNo(checkVarchar(CommonUtils.checkNull(p.getTaxpayerNo()).trim(),errInfoStr,errBo,"纳税人识别号",20));//纳税人识别号
							tdpo.setTaxpayerNature(checkVarchar(CommonUtils.checkNull(p.getTaxpayerNature()).trim(),errInfoStr,errBo,"纳税人性质",50));//纳税人性质
							tdpo.setTaxInvoice(checkVarchar(CommonUtils.checkNull(p.getTaxInvoice()).trim(),errInfoStr,errBo,"增值税发票",30));//增值税发票
							tdpo.setTaxDisrate(checkVarchar(CommonUtils.checkNull(p.getTaxDisrate()).trim(),errInfoStr,errBo,"开票税率",30));//开票税率
							tdpo.setSitedate(Utility.getDate(CommonUtils.checkNull(p.getSiteDate()).trim(),1));//建厂时间(建站时间)
							tdpo.setDealerType(Constant.DEALER_TYPE_DWR);//经销商类型
							tdpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));//默认
							tdpo.setSpyMan(checkVarchar(CommonUtils.checkNull(p.getSpyMan()).trim(),errInfoStr,errBo,"索赔员",20));//索赔员
							tdpo.setPhone(checkVarchar(CommonUtils.checkNull(p.getPhone()).trim(),errInfoStr,errBo,"服务商电话",100));//服务商电话
							//tdpo.setTaxesNo(checkVarchar(CommonUtils.checkNull(p.getTaxesNo()).trim(),errInfoStr,errBo,"税号",50));//税号
							if(CommonUtils.checkNull(p.getImageLevel()).trim().equals("")){
							
							}else{
								String deaLev=CodeDict.getDictCodeByName(Constant.IMAGE_LEVEL.toString(), p.getImageLevel().trim());
								if(deaLev!=null){
									tdpo.setImageLevel(Integer.parseInt(deaLev));
								}else{
									errInfoStr+="拟建店类别输入有误【A类;B类;C类;D类;F类】！";
									errBo=true;
								}
							}
							//服务状态
							String serName=CodeDict.getDictCodeByName(Constant.DLR_SERVICE_STATUS, p.getServiceStatus());
							if(serName!=null){
								tdpo.setServiceStatus(Long.parseLong(serName));
							}else{
								errInfoStr+="服务状态输入有误【只能输入{意向,正常,拟撤,已撤}】！";
								errBo=true;
							}
							//维修资质
							String mrces=CodeDict.getDictCodeByName(Constant.MAIN_RESOURCES.toString(), p.getMainResources());
							if(mrces!=null){
								tdpo.setMainResources(Integer.parseInt(mrces));
							}else{
								errInfoStr+="维修资质输入有误【只能输入{一类,二类,三类,无}】！";
								errBo=true;
							}
						//根据大区名称获取大区代码
						List<Map<String, Object>> bdd=dao.getBigId(CommonUtils.checkNull(p.getDealerOrgId()).trim());
						if(bdd!=null && bdd.size()==1){//有就设置
							hiOrgId=bdd.get(0).get("ORG_ID").toString();
							tdpo.setDealerOrgId(Long.parseLong(hiOrgId));//大区
						}else{
							errInfoStr+="所属组织输入有误！";
							errBo=true;
						}
						List<Map<String, Object>> pdd=dao.getPDealer(CommonUtils.checkNull(p.getParentDealerD()).trim());
						if(pdd!=null && pdd.size()==1){//有就设置
							tdpo.setParentDealerD(Long.parseLong(pdd.get(0).get("DEALER_ID").toString()));//上级经销商
						}else{
							if(CommonUtils.checkNull(p.getDealerType()).trim().equals("二级")){
								errInfoStr+="上级单位输入有误,请确认是否输错(必须全称)!";
								errBo=true;
							}
							//tdpo.setParentDealerD(-1L);//上级经销商
						}
						tdpo.setBalanceLevel(CommonUtils.checkNull(p.getBalanceLevel()).trim().equals("独立结算")?Constant.BALANCE_LEVEL_SELF:Constant.BALANCE_LEVEL_HIGH);//结算等级
						tdpo.setInvoiceLevel(CommonUtils.checkNull(p.getInvoiceLevel()).trim().equals("独立开票")?Constant.INVOICE_LEVEL_SELF:Constant.INVOICE_LEVEL_HIGH);//开票等级
						tdpo.setCreateDate(new Date());
						//经销商明显表数据插入
						TmDealerDetailPO tdd=new TmDealerDetailPO();
							tdd.setTheAgents(checkVarchar(CommonUtils.checkNull(p.getTheAgents()).trim(),errInfoStr,errBo,"辐射区域",300));//辐射区域
							tdd.setSerManagerName(checkVarchar(CommonUtils.checkNull(p.getSerManagerName()).trim(),errInfoStr,errBo,"服务经理",30));//服务经理
							tdd.setSerManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getSerManagerTelphone()).trim(),errInfoStr,errBo,"服务经理手机",30));//服务经理手机
							tdd.setSerManagerEmail(checkVarchar(CommonUtils.checkNull(p.getSerManagerEmail()).trim(),errInfoStr,errBo,"服务经理邮箱",50));//服务经理邮箱
							tdd.setClaimDirectorName(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorName()).trim(),errInfoStr,errBo,"索赔主管",30));//索赔主管
							tdd.setClaimDirectorPhone(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorPhone()).trim(),errInfoStr,errBo,"索赔主管办公电话",30));//索赔主管办公电话
							tdd.setClaimDirectorTelphone(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorTelphone()).trim(),errInfoStr,errBo,"索赔主管手机",30));//索赔主管手机
							tdd.setClaimDirectorEmail(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorEmail()).trim(),errInfoStr,errBo,"索赔主管邮箱",50));//索赔主管邮箱
							tdd.setClaimDirectorFax(checkVarchar(CommonUtils.checkNull(p.getClaimDirectorFax()).trim(),errInfoStr,errBo,"索赔传真",30));//索赔传真
							tdd.setTechnologyDirectorName(checkVarchar(CommonUtils.checkNull(p.getTechnologyDirectorName()).trim(),errInfoStr,errBo,"技术主管",30));//技术主管
							tdd.setTechnologyDirectorTelphone(checkVarchar(CommonUtils.checkNull(p.getTechnologyDirectorTelphone()).trim(),errInfoStr,errBo,"技术主管手机",30));//技术主管手机
							tdd.setUnionType(checkVarchar(CommonUtils.checkNull(p.getUnionType()).trim(),errInfoStr,errBo,"单位性质",50));//单位性质
							tdd.setFixedCapital(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getFixedCapital()).trim(),errInfoStr,errBo,"固定资产（万元）",15)));//固定资产（万元）
							tdd.setRegisteredCapital(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getRegisteredCapital()).trim(),errInfoStr,errBo,"注册资金（万元）/企业注册资金",15)));//注册资金（万元）/企业注册资金
							tdd.setPeopleNumber(Integer.parseInt(checkVarchar(CommonUtils.checkNullNum(p.getPeopleNumber()).trim(),errInfoStr,errBo,"人数",8)));//人数
							tdd.setPartsArea(Long.parseLong(checkVarchar(CommonUtils.checkNullNum(p.getPartsArea()).trim(),errInfoStr,errBo,"配件库面积（平方米）",10)));//配件库面积（平方米）
							tdd.setDepotArea(Long.parseLong(checkVarchar(CommonUtils.checkNullNum(p.getDepotArea()).trim(),errInfoStr,errBo,"停车场面积（平方米）",10)));//停车场面积（平方米）
							tdd.setMainArea(Long.parseLong(checkVarchar(CommonUtils.checkNullNum(p.getMainArea()).trim(),errInfoStr,errBo,"维修车间面积（平方米）",10)));//维修车间面积（平方米）
							tdd.setOnlyMonthCount(Integer.parseInt(checkVarchar(CommonUtils.checkNullNum(p.getOnlyMonthCount()).trim(),errInfoStr,errBo,"月平均维修能力(台次)",8)));//月平均维修能力(台次)
							tdd.setOpeningTime(checkVarchar(CommonUtils.checkNull(p.getOpeningTime()).trim(),errInfoStr,errBo,"营业时间",50));//营业时间
							tdd.setWorkType(checkVarchar(CommonUtils.checkNull(p.getWorkType()).trim(),errInfoStr,errBo,"经营类型",50));//经营类型
							tdd.setIsFourS(checkVarchar(CommonUtils.checkNull(p.getIsFourS()).trim(),errInfoStr,errBo,"是否4S店",8));//是否4S店
							tdd.setIsLowSer(checkVarchar(CommonUtils.checkNull(p.getIsLowSer()).trim(),errInfoStr,errBo,"是否有二级服务商",8));//是否有二级服务商
							tdd.setAuthorizationType(checkVarchar(CommonUtils.checkNull(p.getAuthorizationType()).trim(),errInfoStr,errBo,"企业授权类型",30));//企业授权类型
							tdd.setAuthorizationDate(Utility.getDate(CommonUtils.checkNull(p.getAuthorizationDate()).trim(),1));//授权时间
							tdd.setIsActingBrand(CommonUtils.checkNull(p.getIsActingBrand()).trim().equals("是")?Constant.IF_TYPE_YES:Constant.IF_TYPE_YES);//是否经营其他品牌
							tdd.setActingBrandName(checkVarchar(CommonUtils.checkNull(p.getActingBrandName()).trim(),errInfoStr,errBo,"代理其它品牌名称",30));//代理其它品牌名称
							tdd.setPartsStoreAmount(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getPartsStoreAmount()).trim(),errInfoStr,errBo,"配件储备金额（万元）",15)));//配件储备金额（万元）
							tdd.setCompanyZcCode(checkVarchar(CommonUtils.checkNull(p.getCompanyZcCode()).trim(),errInfoStr,errBo,"组织机构代码",30));//组织机构代码
							tdd.setDealerRelation(checkVarchar(CommonUtils.checkNull(p.getDealerRelation()).trim(),errInfoStr,errBo,"与一级经销商关系",30));//与一级经销商关系
							tdd.setMeetingRoomArea(Integer.parseInt(checkVarchar(CommonUtils.checkNullNum(p.getPartsArea()).trim(),errInfoStr,errBo,"接待室面积（平方米）",8)));//接待室面积（平方米）
							//tdd.seti
							
							tdd.setFittingsDecName(checkVarchar(CommonUtils.checkNull(p.getFittingsDecName()).trim(),errInfoStr,errBo,"配件主管",50));//配件主管
							tdd.setFittingsDecPhone(checkVarchar(CommonUtils.checkNull(p.getFittingsDecPhone()).trim(),errInfoStr,errBo,"配件主管办公电话",30));//配件主管办公电话
							tdd.setFittingsDecTelphone(checkVarchar(CommonUtils.checkNull(p.getFittingsDecTelphone()).trim(),errInfoStr,errBo,"配件主管手机",30));//配件主管手机
							tdd.setFittingsDecEmail(checkVarchar(CommonUtils.checkNull(p.getFittingsDecEmail()).trim(),errInfoStr,errBo,"配件主管邮箱",30));//配件主管邮箱
							tdd.setFittingsDecFax(checkVarchar(CommonUtils.checkNull(p.getFittingsDecFax()).trim(),errInfoStr,errBo,"配件传真",30));//配件传真
							tdd.setFinanceManagerName(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerName()).trim(),errInfoStr,errBo,"财务经理",30));//财务经理
							tdd.setFinanceManagerPhone(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerPhone()).trim(),errInfoStr,errBo,"财务经理办公电话",30));//财务经理办公电话
							tdd.setFinanceManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerTelphone()).trim(),errInfoStr,errBo,"财务经理手机",30));//财务经理手机
							tdd.setFinanceManagerEmail(checkVarchar(CommonUtils.checkNull(p.getFinanceManagerEmail()).trim(),errInfoStr,errBo,"财务经理邮箱",30));//财务经理邮箱
							
							
							tdd.setRemark(checkVarchar(CommonUtils.checkNull(p.getRemark()).trim(),errInfoStr,errBo,"备注",200));//备注
						tdd.setSerDirectorName(checkVarchar(CommonUtils.checkNull(p.getSerDirectorName()).trim(),errInfoStr,errBo,"服务主管",30));//服务主管
						tdd.setSerDirectorPhone(checkVarchar(CommonUtils.checkNull(p.getSerDirectorPhone()).trim(),errInfoStr,errBo,"服务主管办公电话",30));//服务主管办公电话
						tdd.setSerDirectorTelhone(checkVarchar(CommonUtils.checkNull(p.getSerDirectorTelhone()).trim(),errInfoStr,errBo,"服务主管手机",30));//服务主管手机
						tdd.setCompanyAddress(checkVarchar(CommonUtils.checkNull(p.getCompanyAddress()).trim(),errInfoStr,errBo,"公司地址/销售展厅地址",300));//公司地址/销售展厅地址
						tdd.setHotline(checkVarchar(CommonUtils.checkNull(p.getHotline()).trim(),errInfoStr,errBo,"24小时热线",30));//24小时热线
						
						tdd.setProxyVehicleType(CommonUtils.checkNull(p.getProxyVehicleType()).trim());//代理车型
						tdd.setProxyArea(CommonUtils.checkNull(p.getProxyArea()).trim());//代理区域
						
						//判断是否有错误信息,有就不执行插入或更新操作
						if(errBo){
							errorMap.put("rowNum", p.getRowNum());//错误行号
							errorMap.put("errorInfo", errInfoStr);//错误信息
							errorJsp.add(errorMap);//保存错误信息
							continue;
						}
						//首先判断是否有公司，有公司就修改公司信息，再添加经销商
						List<Map<String, Object>> comList=dao.getCompany(CommonUtils.checkNull(p.getDealerCode().split("-")[0]).trim());
						//如果公司有 更新
						if(comList!=null && comList.size()>0){
							tcpo.setUpdateBy(logonUser.getUserId());//创建人
							tcpo.setUpdateDate(new Date());
							//更新公司表数据
							TmCompanyPO tcpo1=new TmCompanyPO();
							tcpo1.setCompanyCode(CommonUtils.checkNull(p.getDealerCode().split("-")[0]).trim());
							dao.update(tcpo1, tcpo);
						}else{//无 添加公司表数据
							tcpo.setCompanyId(Long.parseLong(SequenceManager.getSequence("")));
							tcpo.setCreateBy(logonUser.getUserId());//创建人
							tcpo.setCreateDate(new Date());
							dao.insert(tcpo);
						}
						//获取公司ID
						TmCompanyPO c1=new TmCompanyPO();
						c1.setCompanyCode(CommonUtils.checkNull(p.getDealerCode().split("-")[0]).trim());
						TmCompanyPO tmc=(TmCompanyPO)dao.select(c1).get(0);
						//公司
						tdpo.setCompanyId(tmc.getCompanyId());//公司ID
						//无公司添加公司，在添加经销商
						hiDealerId=SequenceManager.getSequence("");
						tdpo.setDealerId(Long.parseLong(hiDealerId));
						dao.insert(tdpo);
						//首先清除经销商与组织中间表的数据
						dao.delRation(hiDealerId);
						//添加经销商与组织中间表的数据
						TmDealerOrgRelationSecendPO cc = new TmDealerOrgRelationSecendPO();
						cc.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
						cc.setDealerId(Long.parseLong(hiDealerId));
						cc.setOrgId(Long.parseLong(hiOrgId));
						cc.setBusinessType(Constant.ORG_TYPE_OEM);
						dao.insert(cc);
						//新增经销商明显表
						tdd.setDetailId(Long.parseLong(SequenceManager.getSequence("")));//明显ID
						tdd.setFkDealerId(Long.parseLong(hiDealerId));//经销商ID
						dao.insert(tdd);
						//调用存储过程 保存经销商的旧件回运时间
						Utility.setDealerType(hiDealerId,"P_DEALER_DATE", conn);
						//将该经销商的配件加价率默认为0,否则开工单时查不出配件
						Utility.setDealerType(hiDealerId,"P_TM_DOWN_PARAMETER", conn);
						
					}
				}
			}
			POContext.endTxn(true);//added by lijj 2014-05-21
			//经销商（入查询到就更新 否则添加）
			//根据经销商代码判断规则[公司代码-SO1] 销售是SO1 售后是SH1 判断是否有数据
			act.setOutData("errorJsp", errorJsp);
			act.setForword(shDealerImportCompleteUrl2nd);
		} catch (Exception e) {
			POContext.endTxn(false);//added by lijj 2014-05-21
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}finally{
			//关闭连接
			if(conn!=null){
				try {
					conn.close();
					POContext.cleanTxn();//added by lijj 2014-05-21
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * 导入业务表（导入正式数据表地址导入）
	 */
	public void importExcelForAddress(){
		ActionContext act = ActionContext.getContext();
		ShDealerDao dao=ShDealerDao.getInstance();
		TmpShDealerAddressPO ppo=new TmpShDealerAddressPO();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String,Object>> errorJsp=new ArrayList();
			Map<String,Object> errorMap=new HashMap<String,Object>();
			List<PO> l = dao.select(ppo);
			TtPartAddrDefinePO addrDefinePO = new TtPartAddrDefinePO();
			TtPartAddressMorePO ttPartAddressMorePO = new TtPartAddressMorePO();
			errInfoStr = "";
			errBo = false;
			if(l!=null && l.size()>0){
				for(int i=0;i<l.size();i++){
					TmpShDealerAddressPO PO = (TmpShDealerAddressPO)l.get(i);
					TmDealerPO tmd = new TmDealerPO();
					tmd.setDealerCode(PO.getDealerCode());
					tmd.setDealerType(10771002);
					if(dao.select(tmd).size()==0){
						errInfoStr+="输入的经销商代码不存在!  ";
						errBo=true;
					}
					else{
						TmDealerPO tmDealerPO = (TmDealerPO)dao.select(tmd).get(0);
						addrDefinePO.setDealerId(tmDealerPO.getDealerId());
						ttPartAddressMorePO.setDealerId(tmDealerPO.getDealerId());
					}
					if(StringUtil.isNull(PO.getAddressType())){
						errInfoStr+="地址类型不能为空!";
						errBo=true;
					}else if((!PO.getAddressType().equals("配件接收地址"))
							&&(!PO.getAddressType().equals("发票邮寄地址"))
							&&(!PO.getAddressType().equals("信件接收地址"))){
						
							errInfoStr+="地址类型必须是 : 配件接收地址,发票邮寄地址,信件接收地址!";
							errBo=true;
						
					}else{
						TcCodePO codePO = new TcCodePO();
						codePO.setCodeDesc(PO.getAddressType());
						TcCodePO TcCodePO = (TcCodePO)dao.select(codePO).get(0);
						ttPartAddressMorePO.setAddressType(Integer.parseInt(TcCodePO.getCodeId()));
						addrDefinePO.setAddressType(Integer.parseInt(TcCodePO.getCodeId()));
					}

					
					if((StringUtil.isNull(PO.getState()))
							&&(!PO.getState().equals("无效"))){
						errInfoStr+="地址状态不能为空!";
						errBo=true;
					}else if((!PO.getState().equals("有效"))
							&&(!PO.getState().equals("无效"))){
						errInfoStr+="地址状态 必须是: 有效,无效!";
						errBo=true;
					}
					else{
						TcCodePO codePO = new TcCodePO();
						codePO.setCodeDesc(PO.getState());
						TcCodePO TcCodePO = (TcCodePO)dao.select(codePO).get(0);
						ttPartAddressMorePO.setState(Integer.parseInt(TcCodePO.getCodeId()));
						addrDefinePO.setState(Integer.parseInt(TcCodePO.getCodeId()));
					}
//					if((!PO.getGender().equals("男"))
//							&&(!PO.getGender().equals("女"))){
//						errInfoStr+="性别 必须是: 男,女!";
//						errBo=true;
//					}
//					else{
//						TcCodePO codePO = new TcCodePO();
//						codePO.setCodeDesc(PO.getGender());
//						TcCodePO TcCodePO = (TcCodePO)dao.select(codePO).get(0);
//						ttPartAddressMorePO.setGender(Integer.parseInt(TcCodePO.getCodeId()));
//						addrDefinePO.setGender(Integer.parseInt(TcCodePO.getCodeId()));
//					}
					String match = "^0?1\\d{10}$";
					if(!com.infoservice.infox.util.StringUtil.isEmpty(PO.getMobilePhone())) {
						if(!PO.getMobilePhone().matches(match)) {
							errInfoStr+="手机号码格式错误";
							errBo=true;
						}
					}
					if(errBo){
						errorMap.put("rowNum", PO.getId());//错误行号
						errorMap.put("errorInfo", errInfoStr);//错误信息
						errorJsp.add(errorMap);//保存错误信息
						break;
					}else{
						if(ttPartAddressMorePO.getAddressType().equals(Constant.SHOU_ADDRESS_TYPE_01)){
							
							addrDefinePO.setDealerCode(PO.getDealerCode());
							addrDefinePO.setDealerName(PO.getDealerName());
							addrDefinePO.setAddr(checkVarchar(CommonUtils.checkNull(PO.getAddr()).trim(),errInfoStr,errBo,"详细地址",300));
							addrDefinePO.setAddrId(Long.parseLong(SequenceManager.getSequence("")));
							addrDefinePO.setTel(PO.getTel());
							addrDefinePO.setMobilePhone(PO.getMobilePhone());
							addrDefinePO.setLinkman(PO.getLinkman());
							addrDefinePO.setCreateBy(logonUser.getUserId());
							addrDefinePO.setCreateDate(new Date()); 
							
							dao.insert(addrDefinePO);
						}else{
							
							ttPartAddressMorePO.setAddr(checkVarchar(CommonUtils.checkNull(PO.getAddr()).trim(),errInfoStr,errBo,"详细地址",300));
							ttPartAddressMorePO.setAddrId(Long.parseLong(SequenceManager.getSequence("")));
							ttPartAddressMorePO.setTel(PO.getTel());
							ttPartAddressMorePO.setMobilePhone(PO.getMobilePhone());
							ttPartAddressMorePO.setLinkman(PO.getLinkman());
							ttPartAddressMorePO.setCreateBy(logonUser.getUserId());
							ttPartAddressMorePO.setCreateDate(new Date()); 
							ttPartAddressMorePO.setDealerCode(PO.getDealerCode());
							ttPartAddressMorePO.setDealerName(PO.getDealerName());
							dao.insert(ttPartAddressMorePO);
						}
					}
				}
			}

			act.setOutData("errorJsp", errorJsp);
			act.setForword(shDealerImportCompleteUrl);
		} catch (Exception e) {
			POContext.endTxn(false);//added by lijj 2014-05-21
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @param pa  字段
	 * @param errInfoStr 错误信息
	 * @param errBo  错误返回
	 * @param errPa  错误字段名
	 * @param len  字段最大长度
	 * @return
	 */
	public String checkVarchar(String pa,String errInfoS,Boolean errB,String errPa,int len){
		String errInfo="";
		if(pa.getBytes().length>len){
			errInfo=errPa+"列长度不能大于"+len+"个字节[汉字和中文标点占2个字节,英文和英文标点占1个字节]！";
			errInfoStr=errInfoS+errInfo;
			errBo=true;
			return pa;
		}
		return pa;
	}
	/**
	 * 经销商导入模板（售后）下载
	 * @author RANJ
	 */
	public void downloadTempleSh(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			// 用于下载传参的
			List<List<Object>> list = new LinkedList<List<Object>>();
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("序号");
			listHead.add("大区");
			listHead.add("所属组织");
			listHead.add("省份");
			listHead.add("所属行政区域城市");
			listHead.add("所属行政区域区县");
			listHead.add("邮编");
			listHead.add("服务商编码{100001-F}");
			listHead.add("服务商等级{一级，二级}");
			listHead.add("状态{意向,整改,正常,拟撤,已撤}");
			listHead.add("服务商全称");
			listHead.add("服务商简称");
			listHead.add("上级服务商");
			listHead.add("与一级经销商关系");
			listHead.add("企业注册地址");
			listHead.add("注册证号");
			listHead.add("主营范围");
			listHead.add("兼营范围");
			listHead.add("组织机构代码");
			listHead.add("维修资质为{一类,二类,三类,无}");
			listHead.add("建厂时间{格式为2014-01-14}");
			listHead.add("法人代表");
			listHead.add("单位性质");
			listHead.add("固定资产（万元）");
			listHead.add("注册资金（万元）");
			listHead.add("服务站人数");
			listHead.add("维修车间面积（平方米）");
			listHead.add("接待室面积（平方米）");
			listHead.add("配件库面积（平方米）");
			listHead.add("停车场面积（平方米）");
			listHead.add("营业时间{格式为09:00-17:30}");
			listHead.add("月平均维修能力(台次)");
			listHead.add("经营类型{直营,代理,托管}");
			listHead.add("是否4S店{是,否}");
			listHead.add("建店类别{A类;B类;C类;D类;E类;F类}");
			listHead.add("地址（省、市、县、区、路、号）");
			listHead.add("是否有二级服务站{是,否}");
			listHead.add("企业授权类型{形象店,特约站,代理库,专卖店}");
			listHead.add("授权时间{格式为2014-01-14}");
			listHead.add("是否经营其他品牌{是,否}");
			listHead.add("代理其它品牌名称");
			listHead.add("24小时服务热线");
			listHead.add("服务经理");
			listHead.add("服务经理手机");
			listHead.add("服务经理邮箱");
			listHead.add("索赔主管");
			listHead.add("索赔主管办公电话");
			listHead.add("索赔主管手机");
			listHead.add("索赔主管邮箱");
			listHead.add("索赔传真");
			listHead.add("服务主管");
			listHead.add("服务主管办公电话");
			listHead.add("服务主管手机");
			listHead.add("技术主管");
			listHead.add("技术主管手机");
			listHead.add("配件主管");
			listHead.add("配件主管办公电话");
			listHead.add("配件主管手机");
			listHead.add("配件主管邮箱");
			listHead.add("配件传真");
			listHead.add("配件储备金额（万元）");
			listHead.add("开户行");
			listHead.add("开票名称");
			listHead.add("银行帐号");
			listHead.add("开票电话");
			listHead.add("开票地址");
			listHead.add("纳税人识别号");
			listHead.add("纳税人性质");
			listHead.add("增值税发票");
			listHead.add("开票税率");
			listHead.add("财务经理");
			listHead.add("财务经理办公电话");
			listHead.add("财务手机");
			listHead.add("财务邮箱");
			listHead.add("备注");
			listHead.add("结算等级{独立结算,上级结算}");
			listHead.add("开票等级{独立开票,上级开票}");
			listHead.add("索赔员");
			listHead.add("服务商电话");
			listHead.add("辐射区域");
			//20140626 add start
			listHead.add("代理车型");
			listHead.add("验收形象等级{A类;B类;C类;D类;E类;F类}");
			//20140626 add end
			list.add(listHead);
			// 导出的文件名
			String fileName = "经销商导入模板（售后）.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void downloadTempleSh2nd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			// 用于下载传参的
			List<List<Object>> list = new LinkedList<List<Object>>();
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("序号");
			listHead.add("大区");
			listHead.add("所属组织");
			listHead.add("省份");
			listHead.add("所属行政区域城市");
			listHead.add("所属行政区域区县");
			listHead.add("邮编");
			listHead.add("服务商编码{100001-F}");
			listHead.add("服务商等级{一级，二级}");
			listHead.add("状态{意向,整改,正常,拟撤,已撤}");
			listHead.add("服务商全称");
			listHead.add("服务商简称");
			listHead.add("上级服务商");
			listHead.add("与一级经销商关系");
			listHead.add("企业注册地址");
			listHead.add("注册证号");
			listHead.add("主营范围");
			listHead.add("兼营范围");
			listHead.add("组织机构代码");
			listHead.add("维修资质为{一类,二类,三类,无}");
			listHead.add("建厂时间{格式为2014-01-14}");
			listHead.add("法人代表");
			listHead.add("单位性质");
			listHead.add("固定资产（万元）");
			listHead.add("注册资金（万元）");
			listHead.add("服务站人数");
			listHead.add("维修车间面积（平方米）");
			listHead.add("接待室面积（平方米）");
			listHead.add("配件库面积（平方米）");
			listHead.add("停车场面积（平方米）");
			listHead.add("营业时间{格式为09:00-17:30}");
			listHead.add("月平均维修能力(台次)");
			listHead.add("经营类型{直营,代理,托管}");
			listHead.add("是否4S店{是,否}");
			listHead.add("建店类别{A类;B类;C类;D类;E类;F类}");
			listHead.add("地址（省、市、县、区、路、号）");
			listHead.add("是否有二级服务站{是,否}");
			listHead.add("企业授权类型{形象店,特约站,代理库,专卖店}");
			listHead.add("授权时间{格式为2014-01-14}");
			listHead.add("是否经营其他品牌{是,否}");
			listHead.add("代理其它品牌名称");
			listHead.add("24小时服务热线");
			listHead.add("服务经理");
			listHead.add("服务经理手机");
			listHead.add("服务经理邮箱");
			listHead.add("索赔主管");
			listHead.add("索赔主管办公电话");
			listHead.add("索赔主管手机");
			listHead.add("索赔主管邮箱");
			listHead.add("索赔传真");
			listHead.add("服务主管");
			listHead.add("服务主管办公电话");
			listHead.add("服务主管手机");
			listHead.add("技术主管");
			listHead.add("技术主管手机");
			listHead.add("配件主管");
			listHead.add("配件主管办公电话");
			listHead.add("配件主管手机");
			listHead.add("配件主管邮箱");
			listHead.add("配件传真");
			listHead.add("配件储备金额（万元）");
			listHead.add("开户行");
			listHead.add("开票名称");
			listHead.add("银行帐号");
			listHead.add("开票电话");
			listHead.add("开票地址");
			listHead.add("纳税人识别号");
			listHead.add("纳税人性质");
			listHead.add("增值税发票");
			listHead.add("开票税率");
			listHead.add("财务经理");
			listHead.add("财务经理办公电话");
			listHead.add("财务手机");
			listHead.add("财务邮箱");
			listHead.add("备注");
			listHead.add("结算等级{独立结算,上级结算}");
			listHead.add("开票等级{独立开票,上级开票}");
			listHead.add("索赔员");
			listHead.add("服务商电话");
			listHead.add("辐射区域");
			//20140626 add start
			listHead.add("代理车型");
//			listHead.add("代理区域");
			//20140626 add end
			list.add(listHead);
			// 导出的文件名
			String fileName = "经销商导入模板（售后二网）.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 经销商导入模板（售后）下载
	 * @author RANJ
	 */
	public void downloadTempleShAddress(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			// 用于下载传参的
			List<List<Object>> list = new LinkedList<List<Object>>();
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("经销商代码");
			listHead.add("经销商名称");
			listHead.add("联系人姓名");
//			listHead.add("联系人性别(必须为：男,女)");
			listHead.add("联系人手机");
			listHead.add("联系人电话");
			listHead.add("地址类型(必须为：配件接收地址,发票邮寄地址,信件接收地址)");
			listHead.add("地址状态(必须为有效,无效)");
			listHead.add("详细地址");
			
			list.add(listHead);
			// 导出的文件名
			String fileName = "经销商地址导入模板（售后）.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}

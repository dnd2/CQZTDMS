/**
 * 
 */
package com.infodms.dms.actions.sysmng.dealer;

import java.io.IOException;
import java.io.OutputStream;
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

import org.apache.commons.lang.StringUtils;
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
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.dao.sales.dealer.ShDealerDao;
import com.infodms.dms.dao.sales.dealer.XsDealerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerBusinessAreaPO;
import com.infodms.dms.po.TmDealerDetailPO;
import com.infodms.dms.po.TmDealerOrgRelationPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmDealerSecondLevelPO;
import com.infodms.dms.po.TmVsAddressMorePO;
import com.infodms.dms.po.TmVsAddressPO;
import com.infodms.dms.po.TmpXsDealerAddressPO;
import com.infodms.dms.po.TmpXsDealerPO;
import com.infodms.dms.po.TtDealerSecendAuditPO;
import com.infodms.dms.po.TtProxyAreaPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.POContext;

import flex.messaging.io.ArrayList;

/**
 * @author ranj
 *
 */
public class XsDealerImport extends BaseImport {

	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private final String xsDealerImportInitUrl = "/jsp/systemMng/dealer/xsDealerImportInit.jsp";//销售经销商导入初始化页面
	private final String xsDealerImportSuccessUrl = "/jsp/systemMng/dealer/xsDealerImportSuccess.jsp";
	private final String xsDealerImportSuccessUrl2 = "/jsp/systemMng/dealer/xsDealerImportSuccess2.jsp";
	private final String xsDealerImportFailureUrl = "/jsp/systemMng/dealer/xsDealerImportFailure.jsp";
	private final String xsDealerImportFailureUrl2nd = "/jsp/systemMng/dealer/xsDealerImportFailure2nd.jsp";
	private final String xsDealerImportCompleteUrl = "/jsp/systemMng/dealer/xsDealerImportComplete.jsp";
	private final String xsDealerImportCompleteUrl2nd = "/jsp/systemMng/dealer/xsDealerImportComplete2nd.jsp";
	private final String xsDealerAddressImportInitUrl = "/jsp/systemMng/dealer/xsDealerAddressImportInit.jsp";//地址导入初始化页面
	private final String xsDealerAddressImportSuccessUrl = "/jsp/systemMng/dealer/xsDealerAddressImportSuccess.jsp";
	private final String xsDealerAddressImportSuccessUrl2nd = "/jsp/systemMng/dealer/xsDealerAddressImportSuccess2nd.jsp";
	private String errInfoStr;
	private Boolean errBo;
	/**
	 * 初始使化导放页面
	 */
	public void xsImportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(xsDealerImportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 初始化地址导入页面
	 */
	public void xsImportAddressInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(xsDealerAddressImportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商（销售）导入临时表
	 */
	public void xsImportOperate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		XsDealerDao dao=XsDealerDao.getInstance();
        try {
			RequestWrapper request = act.getRequest();
			
			TmpXsDealerPO po=new TmpXsDealerPO();
			//清空临时表中目标年度的数据
			dao.delete(po);
			long maxSize=1024*1024*5;
			//
			insertIntoTmp(request, "uploadFile",92,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(xsDealerImportFailureUrl);
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTmpXsDealer(list, logonUser.getUserId());
				//校验临时表数据
				List<ExcelErrors> errorList=null;
				errorList=checkData();
				if(null!=errorList){
					act.setOutData("errorList", errorList);
					act.setForword(xsDealerImportFailureUrl);
				}else{
					/*String planVer=CommonUtils.checkNull(request.getParamValue("verNo")); //版本号
					// 取得临时表数据
					List<Map<String, Object>> countList = dao.selectTmpXsDealerList();
					act.setOutData("countList", countList);*/
					act.setForword(xsDealerImportSuccessUrl);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void xsImportOperate2nd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		XsDealerDao dao=XsDealerDao.getInstance();
        try {
			RequestWrapper request = act.getRequest();
			
			TmpXsDealerPO po=new TmpXsDealerPO();
			//清空临时表中目标年度的数据
			dao.delete(po);
			long maxSize=1024*1024*5;
			//
			insertIntoTmp(request, "uploadFile",57,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(xsDealerImportFailureUrl2nd);
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTmpXsDealer(list, logonUser.getUserId());
				//校验临时表数据
				List<ExcelErrors> errorList=null;
				errorList=checkDataForSecend();
				if(null!=errorList){
					act.setOutData("errorList", errorList);
					act.setForword(xsDealerImportFailureUrl2nd);
				}else{
					/*String planVer=CommonUtils.checkNull(request.getParamValue("verNo")); //版本号
					// 取得临时表数据
					List<Map<String, Object>> countList = dao.selectTmpXsDealerList();
					act.setOutData("countList", countList);*/
					act.setForword(xsDealerImportSuccessUrl2);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void xsImportOperateQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		XsDealerDao dao=XsDealerDao.getInstance();
		try {
				Map<String, Object> map = new HashMap<String, Object>();
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.selectTmpXsDealerQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售临时表信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/*
	 * 把所有导入记录插入TMP_XS_DEALER
	 */
	private void insertTmpXsDealer(List<Map> list,Long userId) throws Exception{
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
	/*
	 * 每一行插入TMP_XS_DEALER
	 * 数字只截取30位
	 */
	private void parseCells(String rowNum,Cell[] cells,Long userId) throws Exception{
			XsDealerDao dao=XsDealerDao.getInstance();
			TmpXsDealerPO po=new TmpXsDealerPO();
			po.setRowNumber(Long.parseLong(rowNum));//行号
			po.setA1(lastNull(0,cells,"").toString()); //大区 
			po.setA2(lastNull(1,cells,"").toString()); //省份
			po.setA3(lastNull(2,cells,"").toString()); //所属行政区域城市
			po.setA4(lastNull(3,cells,"").toString()); //所属行政区域区县
			po.setA5(lastNull(4,cells,"").toString()); //所属一级经销商名称
			po.setA6(lastNull(5,cells,"").toString()); //所属一级经销商代码
			po.setA7(lastNull(6,cells,"").toString()); //邮编
			po.setA8(lastNull(7,cells,"").toString()); //二级网络编码{系统自动生成无需输入}
			po.setA9(lastNull(8,cells,"").toString()); //二级网络状态
			po.setA10(lastNull(9,cells,"").toString()); //二级网络全称
			
			po.setA11(lastNull(10,cells,"").toString()); //二级网络简称
			po.setA12(lastNull(11,cells,"").toString()); //对应服务商编码
			po.setA13(lastNull(12,cells,"").toString()); //对应服务商状态
			po.setA14(lastNull(13,cells,"").toString()); //销售展厅地址
			po.setA15(lastNull(14,cells,"").toString()); //代理其它品牌名称
			po.setA16(lastNull(15,cells,"").toString()); //二级网络销售热线
			po.setA17(lastNull(16,cells,"").toString()); //二级网络邮箱
			po.setA18(lastNull(17,cells,"").toString()); //代理车型
			
			po.setA19(lastNull(18,cells,"").toString()); //最低库存
			po.setA20(lastNull(19,cells,"").toString()); //北汽幻速专营面积
			po.setA21(lastNull(20,cells,"").toString()); //北汽幻速专营销售人员数
			po.setA22(lastNull(21,cells,"").toString()); //全年任务量
			po.setA23(lastNull(22,cells,"").toString()); //代理区域
			
			po.setA24(lastNull(23,cells,"").toString()); //负责人	
			po.setA25(lastNull(24,cells,"").toString()); //负责人办公电话
			po.setA26(lastNull(25,cells,"").toString()); //负责人手机	
			po.setA27(lastNull(26,cells,"").toString()); //负责人邮箱
			
			po.setA28(lastNull(27,cells,"").toString()); //服务经理
			po.setA29(lastNull(28,cells,"").toString()); //服务经理办公电话
			po.setA30(lastNull(29,cells,"").toString()); //服务经理手机
			po.setA31(lastNull(30,cells,"").toString()); //服务经理邮箱
			po.setA32(lastNull(31,cells,"").toString()); //信息员
			po.setA33(lastNull(32,cells,"").toString()); //信息员办公电话
			po.setA34(lastNull(33,cells,"").toString()); //信息员手机
			po.setA35(lastNull(34,cells,"").toString()); //信息员QQ
			po.setA36(lastNull(35,cells,"").toString()); //信息员邮箱
			po.setA37(lastNull(36,cells,"").toString()); //整车收货地址
			
			po.setA38(lastNull(37,cells,"").toString()); //整车收货联系人
			po.setA39(lastNull(38,cells,"").toString()); //整车收货联系人性别
			po.setA40(lastNull(39,cells,"").toString()); //整车收货联系人办公电话
			po.setA41(lastNull(40,cells,"").toString()); //整车收货联系人手机
			po.setA42(lastNull(41,cells,"").toString()); //服务站地址
			po.setA43(lastNull(42,cells,"").toString()); //24小时服务热线
			po.setA44(lastNull(43,cells,"").toString()); //二级网络性质
			po.setA45(lastNull(44,cells,"").toString()); //竞品品牌
			po.setA46(lastNull(45,cells,"").toString()); //与竞品行驶距离（米）
			po.setA47(lastNull(46,cells,"").toString()); //月均销量
			
			po.setA48(lastNull(47,cells,"").toString()); //门头长度
			po.setA49(lastNull(48,cells,"").toString()); //是否具有销售门头
			po.setA50(lastNull(49,cells,"").toString()); //是否具有销售形象墙
			po.setA51(lastNull(50,cells,"").toString()); //服务网点性质
			po.setA52(lastNull(51,cells,"").toString()); //维修资质
			po.setA53(lastNull(52,cells,"").toString()); //服务车间面积
			po.setA54(lastNull(53,cells,"").toString()); //是否具有服务门头
			po.setA55(lastNull(54,cells,"").toString()); //是否具有服务形象墙
			po.setA56(lastNull(55,cells,"").toString()); //服务离销售网点距离
			po.setA57(lastNull(56,cells,"").toString()); //维修技术师最低配备 （人员数量）
	        dao.insert(po);	
	}
	/**
	 * 判断最后有值的列后面时候还有值
	 */
	private Object lastNull(int i,Cell[] cells,Object obj){
	       return cells.length>i?cells[i].getContents().trim().replace("\n", ""):obj;
	}
	/*
	 * 校验TMP_XS_DEALER表中数据是否符合导入标准
	 * DEALER_CODE 是否存在
	 */
	private List<ExcelErrors> checkData(){
		XsDealerDao dao=XsDealerDao.getInstance();
		TmpXsDealerPO ppo=new TmpXsDealerPO();
		List<TmpXsDealerPO> list=dao.select(ppo);
		if(null==list){
			list=new ArrayList();
		}
		ExcelErrors errors=null;
		TmpXsDealerPO po=null;
		StringBuffer errorInfo=new StringBuffer("");
		boolean isError=false;
		
		List<ExcelErrors> errorList = new LinkedList<ExcelErrors>();
		for(int i=0;i<list.size();i++){
			errors=new ExcelErrors();
			//取得TmpYearlyPlanPO
			po=list.get(i);
			//取得行号
			String rowNum=po.getRowNumber().toString();
			try {
				if(null==po.getA1()){isError=true;errorInfo.append("大区不能为空!");}
				if(null==po.getA2()){isError=true;errorInfo.append("省份不能为空!");}
				if(null==po.getA3()){isError=true;errorInfo.append("所属行政区域城市不能为空!");}
				if(null==po.getA4()){isError=true;errorInfo.append("所属行政区域区县不能为空!");}
				if(null==po.getA5()){isError=true;errorInfo.append("邮编不能为空!");}
				if(null==po.getA6()){
					isError=true;errorInfo.append("经销商编码不能为空!");	
				}else{
					String[] strCode=po.getA6().trim().split("-");
					if(strCode.length==2){
						if(!strCode[1].equals("S")){
							isError=true;errorInfo.append("经销商编码格式错误，格式为：【100001-S】!");
						}
					}else{
						isError=true;errorInfo.append("经销商编码格式错误，格式为：【100001-S】!");	
					}
				}
				if(null==po.getA7()){	
				 isError=true;errorInfo.append("经销商状态不能为空!");
				}else{
					//经销商状态判断
					if(!po.getA7().trim().equals("正常") && !po.getA7().trim().equals("拟撤") && !po.getA7().trim().equals("已撤") && !po.getA7().trim().equals("意向")){
						 isError=true;errorInfo.append("经销商状态只能为{意向,正常,拟撤,已撤}!");
					}
				}
				if(null==po.getA8()){isError=true;errorInfo.append("所属上级单位不能为空!");}
				//if(null==po.getA9()){isError=true;errorInfo.append("对应服务商编码不能为空!");}
				/*if(null==po.getA10()){	
					 isError=true;errorInfo.append("对应服务商状态不能为空!");
					}else{
						//对应服务商状态判断
						if(!po.getA10().trim().equals("正常") || !po.getA10().trim().equals("注销") || !po.getA10().trim().equals("待退") || !po.getA10().trim().equals("试运营") || !po.getA10().trim().equals("升级") || !po.getA10().trim().equals("降级")){
							 isError=true;errorInfo.append("对应服务商状态只能为{正常,注销,待退,试运营,升级,降级}!");
						}
					}*/
				if(null==po.getA11()){isError=true;errorInfo.append("经销商全称不能为空!");}
				if(null==po.getA14()){isError=true;errorInfo.append("经销商简称不能为空!");}
				if(null!=po.getA17()){
					if(!po.getA17().trim().equals("国营") && !po.getA17().trim().equals("民营")&& !po.getA17().trim().equals("合资") && !po.getA17().trim().equals("其他")){
						isError=true;errorInfo.append("单位性质只能为{国营,民营,合资,其他}!");
					}
				}
				if(null==po.getA8()){isError=true;errorInfo.append("所属上级单位不能为空，如是一级代理请默认为-1!");}
				if(null==po.getA18()){isError=true;errorInfo.append("代理等级不能为空!");}else{
					if(!po.getA18().trim().equals("一级") && !po.getA18().trim().equals("二级")){
						isError=true;errorInfo.append("代理等级只能为{一级,二级}!");
					}
					if(po.getA18().trim().equals("一级") && !po.getA8().trim().equals("-1")){
						isError=true;errorInfo.append("代理等级为一级所属上级单位默认-1!");
					}
					if(po.getA18().trim().equals("二级") && po.getA8().trim().equals("-1")){
						isError=true;errorInfo.append("代理等级为二级所属上级单位不能为-1!");
					}
				}
				if(po.getA83()!=null){
					if(!po.getA83().trim().equals("A类") && !po.getA83().trim().equals("B类") && !po.getA83().trim().equals("C类") && !po.getA83().trim().equals("D类") && !po.getA83().trim().equals("E类") && !po.getA83().trim().equals("F类")){
						isError=true;errorInfo.append("VI形象验收确定级别只能为{A类;B类;C类;D类;E类;F类}");
					}
				}
				if(po.getA82()!=null){
					if(!po.getA82().trim().equals("A类") && !po.getA82().trim().equals("B类") && !po.getA82().trim().equals("C类") && !po.getA82().trim().equals("D类") && !po.getA82().trim().equals("E类") && !po.getA82().trim().equals("F类")){
						isError=true;errorInfo.append("拟建店类别只能为{A类;B类;C类;D类;E类;F类}");
					}
				}
				if(po.getA84()!=null && !ValidateUtil.isNumeric(po.getA84().trim())){isError=true;errorInfo.append("VI支持总金额只能是数字!");}	
				if(po.getA85()!=null && !ValidateUtil.isNumeric(po.getA85().trim())){isError=true;errorInfo.append("VI支持首批比例只能是数字!");}
				if(po.getA26()!=null && !po.getA26().trim().equals("是") && !po.getA26().trim().equals("否")){isError=true;errorInfo.append("是否是国家品牌授权城市只能为{是,否}!");}	
				if(po.getA27()!=null && !po.getA27().trim().equals("是") && !po.getA27().trim().equals("否")){isError=true;errorInfo.append("是否是国家品牌授权区县只能为{是,否}!");}
				if(po.getA20()!=null && !po.getA20().trim().equals("是") && !po.getA20().trim().equals("否")){isError=true;errorInfo.append("是否经营其他品牌只能为{是,否}");}
				if(po.getA29()!=null && !ValidateUtil.isValidDate(po.getA29().trim())){isError=true;errorInfo.append("国家品牌授权信息收集时间日期格式错误，格式为2014-01-14 !");}
				if(po.getA30()!=null && !ValidateUtil.isValidDate(po.getA30().trim())){isError=true;errorInfo.append("国家品牌授权提交时间日期格式错误，格式为2014-01-14 !");}
				if(po.getA31()!=null && !ValidateUtil.isValidDate(po.getA31().trim())){isError=true;errorInfo.append("国家品牌授权起始时间日期格式错误，格式为2014-01-14 !");}
				if(po.getA33()!=null && !ValidateUtil.isValidDate(po.getA33().trim())){isError=true;errorInfo.append("工商总局公布日期日期格式错误，格式为2014-01-14 !");}
				if(po.getA34()!=null && !ValidateUtil.isValidDate(po.getA34().trim())){isError=true;errorInfo.append("国家品牌授权截止时间日期格式错误，格式为2014-01-14 !");}
				if(po.getA78()!=null && !ValidateUtil.isValidDate(po.getA78().trim())){isError=true;errorInfo.append("VI建设申请日期日期格式错误，格式为2014-01-14 !");}
				if(po.getA79()!=null && !ValidateUtil.isValidDate(po.getA79().trim())){isError=true;errorInfo.append("VI建设开工日期日期格式错误，格式为2014-01-14 !");}
				if(po.getA80()!=null && !ValidateUtil.isValidDate(po.getA80().trim())){isError=true;errorInfo.append("VI建设竣工日期日期格式错误，格式为2014-01-14 !");}
				if(po.getA81()!=null && !ValidateUtil.isValidDate(po.getA81().trim())){isError=true;errorInfo.append("VI形象验收日期日期格式错误，格式为2014-01-14 !");}
				if(po.getA87()!=null && !ValidateUtil.isValidDate(po.getA87().trim())){isError=true;errorInfo.append("VI支持起始时间日期格式错误，格式为2014-01-14 !");}
				if(po.getA88()!=null && !ValidateUtil.isValidDate(po.getA88().trim())){isError=true;errorInfo.append("VI支持截止时间日期格式错误，格式为2014-01-14 !");}
				if(po.getA89()!=null && !ValidateUtil.isValidDate(po.getA89().trim())){isError=true;errorInfo.append("首次提车时间日期格式错误，格式为2014-01-14 !");}
				if(po.getA90()!=null && !ValidateUtil.isValidDate(po.getA90().trim())){isError=true;errorInfo.append("首次到车日期日期格式错误，格式为2014-01-14 !");}
				if(po.getA91()!=null && !ValidateUtil.isValidDate(po.getA91().trim())){isError=true;errorInfo.append("首次销售时间日期格式错误，格式为2014-01-14 !");}
				if(po.getA6().split("-").length!=2){isError=true;errorInfo.append("经销商代码格式错误,销售经销商代码格式为【公司代码-销售SO1】！");}
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
	
	
	private List<ExcelErrors> checkDataForSecend(){
		XsDealerDao dao=XsDealerDao.getInstance();
		TmpXsDealerPO ppo=new TmpXsDealerPO();
		List<TmpXsDealerPO> list=dao.select(ppo);
		if(null==list){
			list=new ArrayList();
		}
		ExcelErrors errors=null;
		TmpXsDealerPO po=null;
		StringBuffer errorInfo=new StringBuffer("");
		boolean isError=false;
		
		List<ExcelErrors> errorList = new LinkedList<ExcelErrors>();
		for(int i=0;i<list.size();i++){
			errors=new ExcelErrors();
			//取得TmpYearlyPlanPO
			po=list.get(i);
			//取得行号
			String rowNum=po.getRowNumber().toString();
			try {
				if(null==po.getA1()){isError=true;errorInfo.append("大区不能为空!");}
				if(null==po.getA2()){isError=true;errorInfo.append("省份不能为空!");}
				if(null==po.getA3()){isError=true;errorInfo.append("所属行政区域城市不能为空!");}
				if(null==po.getA4()){isError=true;errorInfo.append("所属行政区域区县不能为空!");}
				if(null==po.getA5()){isError=true;errorInfo.append("所属一级经销商名称不能为空!");}
//				if(null==po.getA7()){isError=true;errorInfo.append("邮编不能为空!");}
//				if(null==po.getA8()){
//					isError=true;errorInfo.append("经销商编码不能为空!");	
//				}else{
//					String[] strCode=po.getA8().trim().split("-");
//					if(strCode.length==2){
//						if(!strCode[1].equals("S")){
//							isError=true;errorInfo.append("经销商编码格式错误，格式为：【10000001-S】!");
//						}
//					}else{
//						isError=true;errorInfo.append("经销商编码格式错误，格式为：【10000001-S】!");	
//					}
//				}
				if(null==po.getA9()){	
					isError=true;errorInfo.append("经销商状态不能为空!");
				}else{
					//经销商状态判断
					if(!po.getA9().trim().equals("正常") && !po.getA9().trim().equals("注销") && !po.getA9().trim().equals("待退") 
							&& !po.getA9().trim().equals("试运营")&& !po.getA9().trim().equals("升级")&& !po.getA9().trim().equals("降级")){
						 isError=true;errorInfo.append("经销商状态只能为{正常,注销,待退,试运营,升级,降级}!");
					}
				}
				if(null==po.getA10()){isError=true;errorInfo.append("经销商全称不能为空!");}
				if(null==po.getA11()){isError=true;errorInfo.append("经销商简称不能为空!");}
//				if(null==po.getA14()){isError=true;errorInfo.append("代理级别不能为空!");}else{
//					if(!po.getA17().trim().equals("一级") && !po.getA17().trim().equals("二级")){
//						isError=true;errorInfo.append("代理等级只能为{一级,二级}!");
//					}
//				}
				
//				if(null!=po.getA16()){
//					if(!po.getA16().trim().equals("国营") && !po.getA16().trim().equals("民营")&& !po.getA16().trim().equals("合资") && !po.getA16().trim().equals("其他")){
//						isError=true;errorInfo.append("单位性质只能为{国营,民营,合资,其他}!");
//					}
//				}
				if (StringUtils.isNotEmpty(po.getA17()) && !ValidateUtil.isEmail(po.getA17(), 1, 20)) {
					isError=true;
					errorInfo.append("二级网络邮箱格式不正确!");
				}
				if (StringUtils.isEmpty(po.getA19())) {
					isError=true;
					errorInfo.append("最低库存不能为空!");
				} else {
					if (!ValidateUtil.isDigit(po.getA19(), 0, 10)) {
						isError=true;
						errorInfo.append("最低库存只能是10位以下的正整数!");
					}
				}
				if (StringUtils.isNotEmpty(po.getA20()) && !ValidateUtil.isDigit(po.getA20(), 0, 10)) {
					isError=true;
					errorInfo.append("北汽幻速专营面积只能是10位以下的正整数!");
				}
				if (StringUtils.isNotEmpty(po.getA21()) && !ValidateUtil.isDigit(po.getA21(), 0, 10)) {
					isError=true;
					errorInfo.append("北汽幻速专营销售人员数只能是10位以下的正整数!");
				}
				if (StringUtils.isEmpty(po.getA22())) {
					isError=true;
					errorInfo.append("全年任务量不能为空!");
				} else {
					if (!ValidateUtil.isDigit(po.getA22(), 0, 10)) {
						isError=true;
						errorInfo.append("全年任务量只能是10位以下的正整数!");
					}
				}
				if (StringUtils.isEmpty(po.getA24())) {
					isError=true;
					errorInfo.append("负责人不能为空!");
				}
				if (StringUtils.isEmpty(po.getA26())) {
					isError=true;
					errorInfo.append("负责人手机不能为空!");
				} else {
					if (!ValidateUtil.isPhone(po.getA26(), 0, 11)) {
						isError=true;
						errorInfo.append("负责人手机格式不正确!");
					}
				}
				if (StringUtils.isNotEmpty(po.getA27()) && !ValidateUtil.isEmail(po.getA27(), 1, 30)) {
					isError=true;
					errorInfo.append("负责人邮箱格式不正确!");
				}
				if (StringUtils.isNotEmpty(po.getA30()) && !ValidateUtil.isPhone(po.getA30(), 0, 11)) {
					isError=true;
					errorInfo.append("服务经理手机格式不正确!");
				}
				if (StringUtils.isNotEmpty(po.getA31()) && !ValidateUtil.isEmail(po.getA31(), 1, 30)) {
					isError=true;
					errorInfo.append("服务经理邮箱格式不正确!");
				}
				if (StringUtils.isNotEmpty(po.getA34()) && !ValidateUtil.isPhone(po.getA34(), 0, 11)) {
					isError=true;
					errorInfo.append("信息员手机格式不正确!");
				}
				if (StringUtils.isNotEmpty(po.getA35()) && !ValidateUtil.isPhone(po.getA35(), 0, 10)) {
					isError=true;
					errorInfo.append("信息员QQ式不正确!");
				}
				if (StringUtils.isNotEmpty(po.getA36()) && !ValidateUtil.isEmail(po.getA36(), 1, 30)) {
					isError=true;
					errorInfo.append("信息员邮箱格式不正确!");
				}
				if (StringUtils.isEmpty(po.getA37())) {
					isError=true;
					errorInfo.append("整车收货地址不能为空!");
				}
				if (StringUtils.isEmpty(po.getA38())) {
					isError=true;
					errorInfo.append("整车收货联系人不能为空!");
				}
				if(null==po.getA39()){	
					isError=true;errorInfo.append("整车收货联系人性别不能为空!");
				}else{
					if(!po.getA39().trim().equals("男") && !po.getA39().trim().equals("女")){
						 isError=true;
						 errorInfo.append("整车收货联系人性别只能为{男,女}!");
					}
				}
				if (StringUtils.isEmpty(po.getA41())) {
					isError=true;
					errorInfo.append("整车收货联系人手机不能为空!");
				} else if (!ValidateUtil.isPhone(po.getA41(), 0, 11)) {
					isError=true;
					errorInfo.append("整车收货联系人手机格式不正确!");
				}
				
				// 其它数据信息
				// 二级网络性质
				if (StringUtils.isEmpty(po.getA44())) {
					isError = true;
					errorInfo.append("二级网络性质不能为空!");
				} else {
					if (!(po.getA44().trim().equals("直营") || po.getA44().trim().equals("联营") || po.getA44().trim().equals("其它"))) {
						isError = true;
						errorInfo.append("二级网络性质只能为{直营,联营,其它}!");
					}
				}
				/*// 竞品品牌
				if (StringUtils.isEmpty(po.getA45())) {
					isError = true;
					errorInfo.append("竞品品牌不能为空!");
				}
				// 与竞品行驶距离（米）
				if (StringUtils.isEmpty(po.getA46())) {
					isError = true;
					errorInfo.append("与竞品行驶距离（米）不能为空!");
				} else if (!ValidateUtil.isDouble(po.getA46().trim(), 0, 10)) {
					isError = true;
					errorInfo.append("与竞品行驶距离（米）只能是10位以下的数字类型!");
				}
				// 月均销量
				if (StringUtils.isEmpty(po.getA47())) {
					isError = true;
					errorInfo.append("月均销量不能为空!");
				} else if (!ValidateUtil.isDigit(po.getA47().trim(), 0, 20)) {
					isError = true;
					errorInfo.append("均销量只能是20位以下的数字类型!");
				}
				// 门头长度
				if (StringUtils.isEmpty(po.getA48())) {
					isError = true;
					errorInfo.append("门头长度不能为空!");
				} else if (!ValidateUtil.isDigit(po.getA48().trim(), 0, 20)) {
					isError = true;
					errorInfo.append("门头长度只能是20位以下的数字类型!");
				}
				// 是否具有销售门头
				if (StringUtils.isEmpty(po.getA49())) {
					isError = true;
					errorInfo.append("是否具有销售门头不能为空!");
				} else if (!(po.getA49().trim().equals("是") || po.getA49().trim().equals("否"))) {
					isError = true;
					errorInfo.append("是否具有销售门头只能为{是,否}");
				}
				// 是否具有销售形象墙
				if (StringUtils.isEmpty(po.getA50())) {
					isError = true;
					errorInfo.append("是否具有销售形象墙不能为空!");
				} else if (!(po.getA50().trim().equals("是") || po.getA50().trim().equals("否"))) {
					isError = true;
					errorInfo.append("是否具有销售形象墙只能为{是,否}");
				}
				// 代理区域人口数量
				if (StringUtils.isEmpty(po.getA66())) {
					isError = true;
					errorInfo.append("代理区域人口数量不能为空!");
				} else if (!ValidateUtil.isDigit(po.getA66().trim(), 0, 20)) {
					isError = true;
					errorInfo.append("代理区域人口数量只能是20位以下的数字类型!");
				}
				// 销售顾问（人员数量）
				if (StringUtils.isEmpty(po.getA67())) {
					isError = true;
					errorInfo.append("销售顾问（人员数量）不能为空!");
				} else if (!ValidateUtil.isDigit(po.getA67().trim(), 0, 20)) {
					isError = true;
					errorInfo.append("销售顾问（人员数量）只能是20位以下的数字类型!");
				}
				// 服务网点性质
				if (StringUtils.isEmpty(po.getA51())) {
					isError = true;
					errorInfo.append("服务网点性质不能为空!");
				} else if (!(po.getA51().trim().equals("自有") || po.getA51().trim().equals("合作"))) {
					isError = true;
					errorInfo.append("服务网点性质只能为{自有,合作}");
				}
				// 维修资质
				if (StringUtils.isEmpty(po.getA52())) {
					isError = true;
					errorInfo.append("维修资质不能为空!");
				} else if (!(po.getA52().trim().equals("一类") || po.getA52().trim().equals("二类") || po.getA52().trim().equals("三类"))) {
					isError = true;
					errorInfo.append("维修资质只能为{一类,二类,三类}");
				}
				// 服务车间面积
				if (StringUtils.isEmpty(po.getA53())) {
					isError = true;
					errorInfo.append("服务车间面积不能为空!");
				} else if (!ValidateUtil.isDouble(po.getA53().trim(), 0, 20)) {
					isError = true;
					errorInfo.append("服务车间面积只能是20位以下的数字类型!");
				}
				// 是否具有服务门头
				if (StringUtils.isEmpty(po.getA54())) {
					isError = true;
					errorInfo.append("是否具有服务门头不能为空!");
				} else if (!(po.getA54().trim().equals("是") || po.getA54().trim().equals("否"))) {
					isError = true;
					errorInfo.append("是否具有服务门头只能为{是,否}");
				}
				// 是否具有服务形象墙
				if (StringUtils.isEmpty(po.getA55())) {
					isError = true;
					errorInfo.append("是否具有服务形象墙不能为空!");
				} else if (!(po.getA55().trim().equals("是") || po.getA55().trim().equals("否"))) {
					isError = true;
					errorInfo.append("是否具有服务形象墙只能为{是,否}");
				}
				// 服务离销售网点距离
				if (StringUtils.isEmpty(po.getA56())) {
					isError = true;
					errorInfo.append("服务离销售网点距离不能为空!");
				} else if (!ValidateUtil.isDouble(po.getA56().trim(), 0, 10)) {
					isError = true;
					errorInfo.append("服务离销售网点距离只能是10位以下的数字类型!");
				}
				// 维修技术师最低配备
				if (StringUtils.isEmpty(po.getA57())) {
					isError = true;
					errorInfo.append("维修技术师最低配备不能为空!");
				} else if (!ValidateUtil.isDigit(po.getA57().trim(), 0, 20)) {
					isError = true;
					errorInfo.append("维修技术师最低配备只能是20位以下的数字类型!");
				}*/
				/*// 星级申报
				if (StringUtils.isEmpty(po.getA75())) {
					isError = true;
					errorInfo.append("星级申报不能为空!");
				} else if (!(po.getA75().trim().equals("三星级") || po.getA75().trim().equals("四星级") || po.getA75().trim().equals("五星级")
						|| po.getA75().trim().equals("六星级") || po.getA75().trim().equals("七星级"))) {
					isError = true;
					errorInfo.append("星级申报只能为{三星级,四星级,五星级,六星级,七星级}");
				}*/
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
		XsDealerDao dao=XsDealerDao.getInstance();
		TmpXsDealerPO ppo=new TmpXsDealerPO();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String,Object>> errorJsp=new ArrayList();
			
			List<PO> l=dao.select(ppo);
			if(l!=null && l.size()>0){
				for(int i=0;i<l.size();i++){
					TmpXsDealerPO p=(TmpXsDealerPO)l.get(i);
					String hiDealerId="";//经销商ID 中间表保存需要
					String hiOrgId="";//组织ID 中间表保存需要
					//Long rowNumMsg=p.getRowNumber();//行号信息
					 errInfoStr="";//记录错误信息
					 errBo=false;//判断是否有错误信息
					 Map<String,Object> errorMap=new HashMap<String,Object>();
					TmDealerPO po=new TmDealerPO();
					po.setDealerCode(CommonUtils.checkNull(p.getA6()).trim());
					List<PO> ps=dao.select(po);
					if(ps.size()>0){//首先判断时候经销商是否有，有就更新公司表，经销商表[有]
						hiDealerId=((TmDealerPO)ps.get(0)).getDealerId().toString();
						//公司表数据
						TmCompanyPO tcpo=new TmCompanyPO();
						tcpo.setCompanyCode(CommonUtils.checkNull(p.getA6()).trim().split("-")[0]);//截取经销商编码“-”前一部分
						tcpo.setCompanyName(CommonUtils.checkNull(p.getA11()).trim());//公司全称
						tcpo.setCompanyShortname(CommonUtils.checkNull(p.getA14()).trim());//公司简称
						tcpo.setStatus(Constant.STATUS_ENABLE);//有效的
						tcpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));//默认
						tcpo.setCompanyType(Integer.parseInt(Constant.COMPANY_TYPE_DEALER));//公司类型
						
						//经销商表
						TmDealerPO tdpo=new TmDealerPO();
						String PId="";//省份ID下面调用
						String CId="";//城市ID下面调用
						String CCId="";//地区ID下面调用
						List<Map<String, Object>> proId=dao.getProvinceId(CommonUtils.checkNull(p.getA2()).trim());
						if(proId!=null && proId.size()==1){
							PId=proId.get(0).get("REGION_CODE").toString();
							tdpo.setProvinceId(Long.parseLong(PId));//省份
							List<Map<String, Object>> cId=dao.getCityId(proId.get(0).get("REGION_ID").toString(),CommonUtils.checkNull(p.getA3()).trim());
							if(cId!=null && cId.size()==1){
								CId=cId.get(0).get("REGION_CODE").toString();
								tdpo.setCityId(Long.parseLong(CId));//城市
								List<Map<String, Object>> coId=dao.getContinusId(cId.get(0).get("REGION_ID").toString(),CommonUtils.checkNull(p.getA4()).trim());
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
						tdpo.setZipCode(checkVarchar(CommonUtils.checkNull(p.getA5()).trim(),errInfoStr,errBo,"邮编",10));//邮编
						tdpo.setAddress(checkVarchar(CommonUtils.checkNull(p.getA15()).trim(),errInfoStr,errBo,"企业注册地址",300));//企业注册地址
						tdpo.setLegal(checkVarchar(CommonUtils.checkNull(p.getA36()).trim(),errInfoStr,errBo,"法人",20));//法人
						tdpo.setLegalPhone(checkVarchar(CommonUtils.checkNull(p.getA37()).trim(),errInfoStr,errBo,"法人办公电话",30));// 法人办公电话
						tdpo.setLegalTelphone(checkVarchar(CommonUtils.checkNull(p.getA38()).trim(),errInfoStr,errBo,"法人电话",30));//法人电话
						tdpo.setLegalEmail(checkVarchar(CommonUtils.checkNull(p.getA39()).trim(),errInfoStr,errBo,"法人邮箱",30));//法人邮箱
						tdpo.setDealerCode(checkVarchar(CommonUtils.checkNull(p.getA6()).trim(),errInfoStr,errBo,"经销商CODE",20));//经销商CODE
						tdpo.setDealerName(checkVarchar(CommonUtils.checkNull(p.getA11()).trim(),errInfoStr,errBo,"经销商名称",300));//经销商名称
						tdpo.setDealerShortname(checkVarchar(CommonUtils.checkNull(p.getA14()).trim(),errInfoStr,errBo,"经销商简称",150));//经销商简称
						tdpo.setDealerType(Constant.DEALER_TYPE_DVS);//经销商级别
						tdpo.setStatus(Constant.STATUS_ENABLE);//经销商状态
						tdpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));//默认
						tdpo.setBrand("BQHS");//默认北汽
						tdpo.setIsSpecial(Constant.IF_TYPE_NO);//是否特商
						
						//增加经销商拼音 20150521 冉可
						if(tdpo.getDealerName() != null && !"".equals(tdpo.getDealerName())){
							String pinyin = Utility.hanZiToPinyin(tdpo.getDealerName());
							tdpo.setPinyin(pinyin);
						}
						
//						tdpo.set
						//根据大区名称获取大区代码
						List<Map<String, Object>> bdd=dao.getBigId(CommonUtils.checkNull(p.getA1()).trim());
						if(bdd!=null && bdd.size()==1){//有就设置
							hiOrgId=bdd.get(0).get("ORG_ID").toString();
							tdpo.setDealerOrgId(Long.parseLong(hiOrgId));//大区
						}else{
							errInfoStr+="所属区域输入有误！";
							errBo=true;
						}
						List<Map<String, Object>> pdd=dao.getPDealer(CommonUtils.checkNull(p.getA8()).trim());
						if(pdd!=null && pdd.size()==1){//有就设置
							tdpo.setParentDealerD(Long.parseLong(pdd.get(0).get("DEALER_ID").toString()));//上级经销商
						}else{
							if(CommonUtils.checkNull(p.getA18()).trim().equals("二级")){
								errInfoStr+="上级单位输入有误,请确认是否输错(必须全称)!";
								errBo=true;
							}
							//tdpo.setParentDealerD(-1L);//上级经销商
						}
						tdpo.setProvinceId(PId==""?-1L:Long.parseLong(PId));//省份
						tdpo.setCityId(CId==""?-1L:Long.parseLong(CId));//城市
						tdpo.setCounties(CCId==""?-1:Integer.parseInt(CCId));//地区
						//tdpo.setZipCode(CommonUtils.checkNull(p.getA5()).trim());//邮编
						
						tdpo.setInvoicePersion(checkVarchar(CommonUtils.checkNull(p.getA66()).trim(),errInfoStr,errBo,"开票联系人",30));//开票联系人
						tdpo.setInvoiceTelphone(checkVarchar(CommonUtils.checkNull(p.getA68()).trim(),errInfoStr,errBo,"开票联系人手机",30));//开票联系人手机
						tdpo.setBeginBank(checkVarchar(CommonUtils.checkNull(p.getA70()).trim(),errInfoStr,errBo,"开户行",300));//开户行
						tdpo.setErpCode(checkVarchar(CommonUtils.checkNull(p.getA65()).trim(),errInfoStr,errBo,"开票名称/开票公司名称",200));//开票名称/开票公司名称
						tdpo.setInvoiceAccount(checkVarchar(CommonUtils.checkNull(p.getA71()).trim(),errInfoStr,errBo,"银行帐号",100));//银行帐号
						tdpo.setInvoicePhone(checkVarchar(CommonUtils.checkNull(p.getA67()).trim(),errInfoStr,errBo,"开票联系人电话",30));//开票电话/开票联系人电话
						tdpo.setInvoiceAdd(checkVarchar(CommonUtils.checkNull(p.getA69()).trim(),errInfoStr,errBo,"开票地址",200));//开票地址
						tdpo.setTaxesNo(checkVarchar(CommonUtils.checkNull(p.getA72()).trim(),errInfoStr,errBo,"纳税人识别号",50));//纳税人识别号
						if(p.getA7().toString().equals("意向")){
							tdpo.setServiceStatus(Long.parseLong(Constant.DLR_SERVICE_STATUS_01));
						}else if(p.getA7().toString().equals("正常")){
							tdpo.setServiceStatus(Long.parseLong(Constant.DLR_SERVICE_STATUS_02));
						}else if(p.getA7().toString().equals("拟撤")){
							tdpo.setServiceStatus(Long.parseLong(Constant.DLR_SERVICE_STATUS_03));
						}else if(p.getA7().toString().equals("已撤")){
							tdpo.setServiceStatus(Long.parseLong(Constant.DLR_SERVICE_STATUS_04));
						}
						tdpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));//默认
						//tdpo.setTaxpayerNature(taxpayerNature);//纳税人性质
						tdpo.setUpdateBy(logonUser.getUserId());//创建人
						if(CommonUtils.checkNull(p.getA18()).trim().equals("一级")){
							tdpo.setDealerLevel(Constant.DEALER_LEVEL_01);//代理等级对应经销商等级
						}else if(CommonUtils.checkNull(p.getA18()).trim().equals("二级")){
							tdpo.setDealerLevel(Constant.DEALER_LEVEL_02);//代理等级对应经销商等级
						}else{
							errInfoStr+="代理级别输入有误【只能输入一级或二级】！";
							errBo=true;
						}
						if(CommonUtils.checkNull(p.getA83()).trim().equals("")){
							
						}else{
							String deaLev=CodeDict.getDictCodeByName(Constant.IMAGE_LEVEL.toString(), p.getA83().trim());
							if(deaLev!=null){
								tdpo.setImageLevel(Integer.parseInt(deaLev));
							}else{
								errInfoStr+="拟建店类别输入有误【A类;B类;C类;D类;E类;F类】！";
								errBo=true;
							}
						}
						tdpo.setUpdateDate(new Date());
						
						//经销商明显表数据插入
						TmDealerDetailPO tdd=new TmDealerDetailPO();
						tdd.setHotline(checkVarchar(CommonUtils.checkNull(p.getA22()).trim(),errInfoStr,errBo,"热线",30));//热线
						tdd.setCompanyAddress(checkVarchar(CommonUtils.checkNull(p.getA16()).trim(),errInfoStr,errBo,"销售展厅地址",300));//销售展厅地址
						tdd.setCompanyZcCode(checkVarchar(CommonUtils.checkNull(p.getA13()).trim(),errInfoStr,errBo,"经销企业组织机构代码",30));//经销商组织代码
						tdd.setWebmasterName(checkVarchar(CommonUtils.checkNull(p.getA40()).trim(),errInfoStr,errBo,"总经理",30));//总经理
						tdd.setWebmasterPhone(checkVarchar(CommonUtils.checkNull(p.getA41()).trim(),errInfoStr,errBo,"总经理办公电话",30));//总经理办公电话
						tdd.setWebmasterTelphone(checkVarchar(CommonUtils.checkNull(p.getA42()).trim(),errInfoStr,errBo,"总经理手机",30));//总经理手机
						tdd.setWebmasterEmail(checkVarchar(CommonUtils.checkNull(p.getA43()).trim(),errInfoStr,errBo,"总经理邮箱",50));//总经理邮箱
						tdd.setMarketName(checkVarchar(CommonUtils.checkNull(p.getA44()).trim(),errInfoStr,errBo,"销售经理",30));//销售经理
						tdd.setMarketPhone(checkVarchar(CommonUtils.checkNull(p.getA45()).trim(),errInfoStr,errBo,"销售经理办公电话",30));//销售经理办公电话
						tdd.setMarketTelphone(checkVarchar(CommonUtils.checkNull(p.getA46()).trim(),errInfoStr,errBo,"销售经理手机",30));//销售经理手机
						tdd.setMarketEmail(checkVarchar(CommonUtils.checkNull(p.getA47()).trim(),errInfoStr,errBo,"销售经理邮箱",50));//销售经理邮箱
						tdd.setManagerName(checkVarchar(CommonUtils.checkNull(p.getA48()).trim(),errInfoStr,errBo,"市场经理",30));//市场经理
						tdd.setManagerPhone(checkVarchar(CommonUtils.checkNull(p.getA49()).trim(),errInfoStr,errBo,"市场经理办公电话",30));//市场经理办公电话
						tdd.setManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getA50()).trim(),errInfoStr,errBo,"市场经理手机",30));//市场经理手机
						tdd.setManagerEmail(checkVarchar(CommonUtils.checkNull(p.getA51()).trim(),errInfoStr,errBo,"市场经理邮箱",50));//市场经理邮箱
						tdd.setSerManagerName(checkVarchar(CommonUtils.checkNull(p.getA52()).trim(),errInfoStr,errBo,"服务经理",30));//服务经理
						tdd.setSerManagerPhone(checkVarchar(CommonUtils.checkNull(p.getA53()).trim(),errInfoStr,errBo,"服务经理办公电话",30));//服务经理办公电话
						tdd.setSerManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getA54()).trim(),errInfoStr,errBo,"服务经理手机",30));//服务经理手机
						tdd.setSerManagerEmail(checkVarchar(CommonUtils.checkNull(p.getA55()).trim(),errInfoStr,errBo,"服务经理邮箱",50));//服务经理邮箱
						tdd.setFinanceManagerName(checkVarchar(CommonUtils.checkNull(p.getA56()).trim(),errInfoStr,errBo,"财务经理",30));//财务经理
						tdd.setFinanceManagerPhone(checkVarchar(CommonUtils.checkNull(p.getA57()).trim(),errInfoStr,errBo,"财务经理办公电话",30));//财务经理办公电话
						tdd.setFinanceManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getA58()).trim(),errInfoStr,errBo,"财务经理手机",30));//财务经理手机
						tdd.setFinanceManagerEmail(checkVarchar(CommonUtils.checkNull(p.getA59()).trim(),errInfoStr,errBo,"财务经理邮箱",50));//财务经理邮箱
						tdd.setMessagerName(checkVarchar(CommonUtils.checkNull(p.getA60()).trim(),errInfoStr,errBo,"信息员",30));//信息员
						tdd.setMessagerPhone(checkVarchar(CommonUtils.checkNull(p.getA61()).trim(),errInfoStr,errBo,"信息员办公电话",30));//信息员办公电话
						tdd.setMessagerTelphone(checkVarchar(CommonUtils.checkNull(p.getA62()).trim(),errInfoStr,errBo,"信息员手机",30));//信息员手机
						tdd.setMessagerQq(checkVarchar(CommonUtils.checkNull(p.getA63()).trim(),errInfoStr,errBo,"信息员QQ",30));//信息员QQ
						tdd.setMessagerEmail(checkVarchar(CommonUtils.checkNull(p.getA64()).trim(),errInfoStr,errBo,"信息员邮箱",50));//信息员邮箱
						tdd.setUnionType(checkVarchar(CommonUtils.checkNull(p.getA17()).trim(),errInfoStr,errBo,"单位性质",30));//单位性质
						tdd.setRegisteredCapital(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getA12()).trim(),errInfoStr,errBo,"企业注册资金",15)));//企业注册资金
						tdd.setViApplayDate(Utility.getDate(CommonUtils.checkNull(p.getA78()).trim(),1));//VI建设申请日期
						tdd.setViBeginDate(Utility.getDate(CommonUtils.checkNull(p.getA79()).trim(),1));//VI建设开工日期
						tdd.setViCompletedDate(Utility.getDate(CommonUtils.checkNull(p.getA80()).trim(),1));//VI建设竣工日期
						tdd.setViConfrimDate(Utility.getDate(CommonUtils.checkNull(p.getA81()).trim(),1));//VI形象验收日期
						//tdd.setImageLevel(-1);//形象等级
						//tdd.setImageComfirmLevel(Integer.parseInt(CommonUtils.checkNullNum(p.getA84()).trim()));//验收形象等级[int]
						tdd.setViSupportAmount(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getA84()).trim(),errInfoStr,errBo,"VI支持总金额",15)));//VI支持总金额[double]
						tdd.setViSupportRatio(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getA85()).trim(),errInfoStr,errBo,"VI支持首批比例",15)));//VI支持首批比例
						tdd.setViSupportType(checkVarchar(CommonUtils.checkNull(p.getA86()).trim(),errInfoStr,errBo,"VI支持后续支持方式",50));//VI支持后续支持方式
						tdd.setViSupportDate(Utility.getDate(CommonUtils.checkNull(p.getA87()).trim(),1));//VI支持起始时间
						tdd.setViSupportEndDate(Utility.getDate(CommonUtils.checkNull(p.getA88()).trim(),1));//VI支持截止时间
						tdd.setFirstSubDate(Utility.getDate(CommonUtils.checkNull(p.getA89()).trim(),1));//首次提车时间
						tdd.setFirstGetcarDate(Utility.getDate(CommonUtils.checkNull(p.getA90()).trim(),1));//首次到车日期
						tdd.setFirstSaelsDate(Utility.getDate(CommonUtils.checkNull(p.getA91()).trim(),1));//首次销售时间
						tdd.setIsActingBrand(CommonUtils.checkNull(p.getA20()).trim().equals("是")?Constant.IF_TYPE_YES:Constant.IF_TYPE_YES);//是否经营其他品牌
						tdd.setActingBrandName(checkVarchar(CommonUtils.checkNull(p.getA21()).trim(),errInfoStr,errBo,"代理其它品牌名称",200));//代理其它品牌名称
						tdd.setRemark(checkVarchar(CommonUtils.checkNull(p.getA92()).trim(),errInfoStr,errBo,"备注",200));//备注
						tdd.setShopType(checkVarchar(CommonUtils.checkNull(p.getA19()).trim(),errInfoStr,errBo,"经营类型",30));//经营类型
						tdd.setFaxNo(checkVarchar(CommonUtils.checkNull(p.getA23()).trim(),errInfoStr,errBo,"经销商传真",30));//经销商传真
						tdd.setEmail(checkVarchar(CommonUtils.checkNull(p.getA24()).trim(),errInfoStr,errBo,"经销商邮箱",50));//经销商邮箱
						tdd.setIsAuthorizeCity(CommonUtils.checkNull(p.getA26()).trim().equals("是")?Constant.IF_TYPE_YES:Constant.IF_TYPE_YES);//是否是国家品牌授权城市
						tdd.setIsAuthorizeCounty(CommonUtils.checkNull(p.getA27()).trim().equals("是")?Constant.IF_TYPE_YES:Constant.IF_TYPE_YES);//是否是国家品牌授权区县
						tdd.setAuthorizeBrand(checkVarchar(CommonUtils.checkNull(p.getA28()).trim(),errInfoStr,errBo,"国家品牌授权",30));//国家品牌授权
						tdd.setAuthorizeGetDate(Utility.getDate(CommonUtils.checkNull(p.getA29()).trim(),1));//国家品牌授权信息收集时间
						tdd.setAuthorizeSubDate(Utility.getDate(CommonUtils.checkNull(p.getA30()).trim(),1));//国家品牌授权提交时间
						tdd.setAuthorizeEffectDate(Utility.getDate(CommonUtils.checkNull(p.getA31()).trim(),1));//国家品牌授权起始时间
						tdd.setAnnouncementNo(checkVarchar(CommonUtils.checkNull(p.getA32()).trim(),errInfoStr,errBo,"工商总局公告号",50));//工商总局公告号
						tdd.setAnnouncementDate(Utility.getDate(CommonUtils.checkNull(p.getA33()).trim(),1));//工商总局公布日期
						tdd.setAnnouncementEndDate(Utility.getDate(CommonUtils.checkNull(p.getA34()).trim(),1));//国家品牌授权截止时间
						
						tdd.setProxyVehicleType(CommonUtils.checkNull(p.getA25()).trim());//代理车型
//						tdd.setProxyArea(CommonUtils.checkNull(p.getA26()).trim());//代理区域
						
						
						if(CommonUtils.checkNull(p.getA83()).trim().equals("")){
							
						}else{
							String imgc=CodeDict.getDictCodeByName(Constant.IMAGE_LEVEL.toString(), p.getA83().trim());
							if(imgc!=null){
								tdd.setImageComfirmLevel(Integer.parseInt(imgc));
							}else{
								errInfoStr+="VI形象验收确定级别输入有误【A类;B类;C类;D类;E类;F类】！";
								errBo=true;
							}
						}
						List<Map<String, Object>> comList=dao.getCompany(CommonUtils.checkNull(p.getA6().split("-")[0]).trim());
						
						//判断是否有错误信息,有就不执行插入或更新操作
						if(errBo){
							errorMap.put("rowNum", p.getRowNumber());//错误行号
							errorMap.put("errorInfo", errInfoStr);//错误信息
							errorJsp.add(errorMap);//保存错误信息
							continue;
						}
						//如果公司有 更新
						if(comList!=null && comList.size()>0){
							//companyId=((TmCompanyPO)comList.get(0)).getCompanyId().toString();
							tcpo.setUpdateBy(logonUser.getUserId());//创建人
							tcpo.setUpdateDate(new Date());
							//更新公司表数据
							TmCompanyPO tcpo1=new TmCompanyPO();
							tcpo1.setCompanyCode(CommonUtils.checkNull(p.getA6().split("-")[0]).trim());
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
						c1.setCompanyCode(CommonUtils.checkNull(p.getA6().split("-")[0]).trim());
						TmCompanyPO tmc=(TmCompanyPO)dao.select(c1).get(0);
						//公司
						tdpo.setCompanyId(tmc.getCompanyId());//公司ID
						//更新经销商表
						TmDealerPO tdpo1=new TmDealerPO();
						tdpo1.setDealerCode(CommonUtils.checkNull(p.getA6()).trim());
						tdpo.setIsStatus(1);
						tdpo.setIsStatus1(3);
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
						/*List<Map<String, Object>> toi=dao.getDO(CommonUtils.checkNull(p.getA6()).trim(),CommonUtils.checkNull(p.getA2()).trim(),"-1");
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
						
					}else{
						
						//无该经销商
						//公司表数据
						TmCompanyPO tcpo=new TmCompanyPO();
						tcpo.setCompanyCode(CommonUtils.checkNull(p.getA6()).split("-")[0].trim());//截取经销商编码“-”前一部分
						tcpo.setCompanyName(CommonUtils.checkNull(p.getA11()).trim());//公司全称
						tcpo.setCompanyShortname(CommonUtils.checkNull(p.getA14()).trim());//公司简称
						tcpo.setStatus(Constant.STATUS_ENABLE);//有效的
						tcpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));//默认
						tcpo.setCompanyType(Integer.parseInt(Constant.COMPANY_TYPE_DEALER));//公司类型
						//经销商表
						TmDealerPO tdpo=new TmDealerPO();
						String PId="";//省份ID下面调用
						String CId="";//城市ID下面调用
						String CCId="";//地区ID下面调用
						List<Map<String, Object>> proId=dao.getProvinceId(CommonUtils.checkNull(p.getA2()).trim());
						if(proId!=null && proId.size()==1){
							PId=proId.get(0).get("REGION_CODE").toString();
							tdpo.setProvinceId(Long.parseLong(PId));//省份
							List<Map<String, Object>> cId=dao.getCityId(proId.get(0).get("REGION_ID").toString(),CommonUtils.checkNull(p.getA3()).trim());
							if(cId!=null && cId.size()==1){
								CId=cId.get(0).get("REGION_CODE").toString();
								tdpo.setCityId(Long.parseLong(CId));//城市
								List<Map<String, Object>> coId=dao.getContinusId(cId.get(0).get("REGION_ID").toString(),CommonUtils.checkNull(p.getA4()).trim());
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

						tdpo.setZipCode(checkVarchar(CommonUtils.checkNull(p.getA5()).trim(),errInfoStr,errBo,"邮编",10));//邮编
						tdpo.setAddress(checkVarchar(CommonUtils.checkNull(p.getA15()).trim(),errInfoStr,errBo,"企业注册地址",300));//企业注册地址
						tdpo.setLegal(checkVarchar(CommonUtils.checkNull(p.getA36()).trim(),errInfoStr,errBo,"法人",20));//法人
						tdpo.setLegalPhone(checkVarchar(CommonUtils.checkNull(p.getA37()).trim(),errInfoStr,errBo,"法人办公电话",30));// 法人办公电话
						tdpo.setLegalTelphone(checkVarchar(CommonUtils.checkNull(p.getA38()).trim(),errInfoStr,errBo,"法人电话",30));//法人电话
						tdpo.setLegalEmail(checkVarchar(CommonUtils.checkNull(p.getA39()).trim(),errInfoStr,errBo,"法人邮箱",30));//法人邮箱
						tdpo.setDealerCode(checkVarchar(CommonUtils.checkNull(p.getA6()).trim(),errInfoStr,errBo,"经销商CODE",20));//经销商CODE
						tdpo.setDealerName(checkVarchar(CommonUtils.checkNull(p.getA11()).trim(),errInfoStr,errBo,"经销商名称",300));//经销商名称
						tdpo.setDealerShortname(checkVarchar(CommonUtils.checkNull(p.getA14()).trim(),errInfoStr,errBo,"经销商简称",150));//经销商简称
						tdpo.setDealerType(Constant.DEALER_TYPE_DVS);//经销商级别
						tdpo.setStatus(Constant.STATUS_ENABLE);//经销商状态
						if(p.getA7().toString().equals("意向")){
							tdpo.setServiceStatus(Long.parseLong(Constant.DLR_SERVICE_STATUS_01));
						}else if(p.getA7().toString().equals("正常")){
							tdpo.setServiceStatus(Long.parseLong(Constant.DLR_SERVICE_STATUS_02));
						}else if(p.getA7().toString().equals("拟撤")){
							tdpo.setServiceStatus(Long.parseLong(Constant.DLR_SERVICE_STATUS_03));
						}else if(p.getA7().toString().equals("已撤")){
							tdpo.setServiceStatus(Long.parseLong(Constant.DLR_SERVICE_STATUS_04));
						}
						tdpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));//默认
						//tdpo.setDealerLevel(dealerLevel);//经销商等级
						tdpo.setBrand("BQHS");//默认北汽
						tdpo.setIsSpecial(Constant.IF_TYPE_NO);//是否特商
						
						//根据大区名称获取大区代码
						List<Map<String, Object>> bdd=dao.getBigId(CommonUtils.checkNull(p.getA1()).trim());
						if(bdd!=null && bdd.size()==1){//有就设置
							hiOrgId=bdd.get(0).get("ORG_ID").toString();
							tdpo.setDealerOrgId(Long.parseLong(hiOrgId));//大区
						}else{
							errInfoStr+="所属区域输入有误！";
							errBo=true;
						}
						List<Map<String, Object>> pdd=dao.getPDealer(CommonUtils.checkNull(p.getA8()).trim());
						if(pdd!=null && pdd.size()==1){//有就设置
							tdpo.setParentDealerD(Long.parseLong(pdd.get(0).get("DEALER_ID").toString()));//上级经销商
						}else{
							if(CommonUtils.checkNull(p.getA18()).trim().equals("二级")){
								errInfoStr+="上级单位输入有误,请确认是否输错(必须全称)!";
								errBo=true;
							}
							//tdpo.setParentDealerD(-1L);//上级经销商
						}
						tdpo.setProvinceId(PId==""?-1L:Long.parseLong(PId));//省份
						tdpo.setCityId(CId==""?-1L:Long.parseLong(CId));//城市
						tdpo.setCounties(CCId==""?-1:Integer.parseInt(CCId));//地区
						//tdpo.setZipCode(CommonUtils.checkNull(p.getA5()).trim());//邮编
						tdpo.setInvoicePersion(checkVarchar(CommonUtils.checkNull(p.getA66()).trim(),errInfoStr,errBo,"开票联系人",30));//开票联系人
						tdpo.setInvoiceTelphone(checkVarchar(CommonUtils.checkNull(p.getA68()).trim(),errInfoStr,errBo,"开票联系人手机",30));//开票联系人手机
						tdpo.setBeginBank(checkVarchar(CommonUtils.checkNull(p.getA70()).trim(),errInfoStr,errBo,"开户行",300));//开户行
						tdpo.setErpCode(checkVarchar(CommonUtils.checkNull(p.getA65()).trim(),errInfoStr,errBo,"开票名称/开票公司名称",200));//开票名称/开票公司名称
						tdpo.setInvoiceAccount(checkVarchar(CommonUtils.checkNull(p.getA71()).trim(),errInfoStr,errBo,"银行帐号",100));//银行帐号
						tdpo.setInvoicePhone(checkVarchar(CommonUtils.checkNull(p.getA67()).trim(),errInfoStr,errBo,"开票联系人电话",30));//开票电话/开票联系人电话
						tdpo.setInvoiceAdd(checkVarchar(CommonUtils.checkNull(p.getA69()).trim(),errInfoStr,errBo,"开票地址",200));//开票地址
						tdpo.setTaxesNo(checkVarchar(CommonUtils.checkNull(p.getA72()).trim(),errInfoStr,errBo,"纳税人识别号",50));//纳税人识别号
						tdpo.setCreateBy(logonUser.getUserId());//创建人
						tdpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));//默认
						if(CommonUtils.checkNull(p.getA18()).trim().equals("一级")){
							tdpo.setDealerLevel(Constant.DEALER_LEVEL_01);//代理等级对应经销商等级
						}else if(CommonUtils.checkNull(p.getA18()).trim().equals("二级")){
							tdpo.setDealerLevel(Constant.DEALER_LEVEL_02);//代理等级对应经销商等级
						}else{
							errInfoStr+="代理级别输入有误【只能输入一级或二级】！";
							errBo=true;
						}
						if(CommonUtils.checkNull(p.getA82()).trim().equals("")){
							
						}else{
							String Delev=CodeDict.getDictCodeByName(Constant.IMAGE_LEVEL.toString(), p.getA82().trim());
							if(Delev!=null){
								tdpo.setImageLevel(Integer.parseInt(Delev));
							}else{
								errInfoStr+="拟建店类别输入有误【A类;B类;C类;D类;E类;F类】！";
								errBo=true;
							}
						}
						tdpo.setCreateDate(new Date());
						
						//经销商明显表数据插入
						TmDealerDetailPO tdd=new TmDealerDetailPO();
						tdd.setHotline(checkVarchar(CommonUtils.checkNull(p.getA22()).trim(),errInfoStr,errBo,"热线",30));//热线
						tdd.setCompanyAddress(checkVarchar(CommonUtils.checkNull(p.getA16()).trim(),errInfoStr,errBo,"销售展厅地址",300));//销售展厅地址
						tdd.setCompanyZcCode(checkVarchar(CommonUtils.checkNull(p.getA13()).trim(),errInfoStr,errBo,"经销企业组织机构代码",30));//经销商组织代码
						tdd.setWebmasterName(checkVarchar(CommonUtils.checkNull(p.getA40()).trim(),errInfoStr,errBo,"总经理",30));//总经理
						tdd.setWebmasterPhone(checkVarchar(CommonUtils.checkNull(p.getA41()).trim(),errInfoStr,errBo,"总经理办公电话",30));//总经理办公电话
						tdd.setWebmasterTelphone(checkVarchar(CommonUtils.checkNull(p.getA42()).trim(),errInfoStr,errBo,"总经理手机",30));//总经理手机
						tdd.setWebmasterEmail(checkVarchar(CommonUtils.checkNull(p.getA43()).trim(),errInfoStr,errBo,"总经理邮箱",50));//总经理邮箱
						tdd.setMarketName(checkVarchar(CommonUtils.checkNull(p.getA44()).trim(),errInfoStr,errBo,"销售经理",30));//销售经理
						tdd.setMarketPhone(checkVarchar(CommonUtils.checkNull(p.getA45()).trim(),errInfoStr,errBo,"销售经理办公电话",30));//销售经理办公电话
						tdd.setMarketTelphone(checkVarchar(CommonUtils.checkNull(p.getA46()).trim(),errInfoStr,errBo,"销售经理手机",30));//销售经理手机
						tdd.setMarketEmail(checkVarchar(CommonUtils.checkNull(p.getA47()).trim(),errInfoStr,errBo,"销售经理邮箱",50));//销售经理邮箱
//						tdd.setManagerName(checkVarchar(CommonUtils.checkNull(p.getA49()).trim(),errInfoStr,errBo,"市场经理",30));//市场经理
//						tdd.setManagerPhone(checkVarchar(CommonUtils.checkNull(p.getA50()).trim(),errInfoStr,errBo,"市场经理办公电话",30));//市场经理办公电话
//						tdd.setManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getA51()).trim(),errInfoStr,errBo,"市场经理手机",30));//市场经理手机
//						tdd.setManagerEmail(checkVarchar(CommonUtils.checkNull(p.getA52()).trim(),errInfoStr,errBo,"市场经理邮箱",50));//市场经理邮箱
						tdd.setSerManagerName(checkVarchar(CommonUtils.checkNull(p.getA52()).trim(),errInfoStr,errBo,"服务经理",30));//服务经理
						tdd.setSerManagerPhone(checkVarchar(CommonUtils.checkNull(p.getA53()).trim(),errInfoStr,errBo,"服务经理办公电话",30));//服务经理办公电话
						tdd.setSerManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getA54()).trim(),errInfoStr,errBo,"服务经理手机",30));//服务经理手机
						tdd.setSerManagerEmail(checkVarchar(CommonUtils.checkNull(p.getA55()).trim(),errInfoStr,errBo,"服务经理邮箱",50));//服务经理邮箱
						tdd.setFinanceManagerName(checkVarchar(CommonUtils.checkNull(p.getA56()).trim(),errInfoStr,errBo,"财务经理",30));//财务经理
						tdd.setFinanceManagerPhone(checkVarchar(CommonUtils.checkNull(p.getA57()).trim(),errInfoStr,errBo,"财务经理办公电话",30));//财务经理办公电话
						tdd.setFinanceManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getA58()).trim(),errInfoStr,errBo,"财务经理手机",30));//财务经理手机
						tdd.setFinanceManagerEmail(checkVarchar(CommonUtils.checkNull(p.getA59()).trim(),errInfoStr,errBo,"财务经理邮箱",50));//财务经理邮箱
						tdd.setMessagerName(checkVarchar(CommonUtils.checkNull(p.getA60()).trim(),errInfoStr,errBo,"信息员",30));//信息员
						tdd.setMessagerPhone(checkVarchar(CommonUtils.checkNull(p.getA61()).trim(),errInfoStr,errBo,"信息员办公电话",30));//信息员办公电话
						tdd.setMessagerTelphone(checkVarchar(CommonUtils.checkNull(p.getA62()).trim(),errInfoStr,errBo,"信息员手机",30));//信息员手机
						tdd.setMessagerQq(checkVarchar(CommonUtils.checkNull(p.getA63()).trim(),errInfoStr,errBo,"信息员QQ",30));//信息员QQ
						tdd.setMessagerEmail(checkVarchar(CommonUtils.checkNull(p.getA64()).trim(),errInfoStr,errBo,"信息员邮箱",50));//信息员邮箱
						tdd.setUnionType(checkVarchar(CommonUtils.checkNull(p.getA17()).trim(),errInfoStr,errBo,"单位性质",30));//单位性质
						tdd.setRegisteredCapital(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getA12()).trim(),errInfoStr,errBo,"企业注册资金",15)));//企业注册资金
						tdd.setViApplayDate(Utility.getDate(CommonUtils.checkNull(p.getA78()).trim(),1));//VI建设申请日期
						tdd.setViBeginDate(Utility.getDate(CommonUtils.checkNull(p.getA79()).trim(),1));//VI建设开工日期
						tdd.setViCompletedDate(Utility.getDate(CommonUtils.checkNull(p.getA80()).trim(),1));//VI建设竣工日期
						tdd.setViConfrimDate(Utility.getDate(CommonUtils.checkNull(p.getA81()).trim(),1));//VI形象验收日期
						//tdd.setImageLevel(-1);//形象等级
						//tdd.setImageComfirmLevel(Integer.parseInt(CommonUtils.checkNullNum(p.getA84()).trim()));//验收形象等级[int]
						tdd.setViSupportAmount(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getA84()).trim(),errInfoStr,errBo,"VI支持总金额",15)));//VI支持总金额[double]
						tdd.setViSupportRatio(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getA85()).trim(),errInfoStr,errBo,"VI支持首批比例",15)));//VI支持首批比例
						tdd.setViSupportType(checkVarchar(CommonUtils.checkNull(p.getA86()).trim(),errInfoStr,errBo,"VI支持后续支持方式",50));//VI支持后续支持方式
						tdd.setViSupportDate(Utility.getDate(CommonUtils.checkNull(p.getA87()).trim(),1));//VI支持起始时间
						tdd.setViSupportEndDate(Utility.getDate(CommonUtils.checkNull(p.getA88()).trim(),1));//VI支持截止时间
						tdd.setFirstSubDate(Utility.getDate(CommonUtils.checkNull(p.getA89()).trim(),1));//首次提车时间
						tdd.setFirstGetcarDate(Utility.getDate(CommonUtils.checkNull(p.getA90()).trim(),1));//首次到车日期
						tdd.setFirstSaelsDate(Utility.getDate(CommonUtils.checkNull(p.getA91()).trim(),1));//首次销售时间
						tdd.setIsActingBrand(CommonUtils.checkNull(p.getA20()).trim().equals("是")?Constant.IF_TYPE_YES:Constant.IF_TYPE_YES);//是否经营其他品牌
						tdd.setActingBrandName(checkVarchar(CommonUtils.checkNull(p.getA21()).trim(),errInfoStr,errBo,"代理其它品牌名称",30));//代理其它品牌名称
						tdd.setRemark(checkVarchar(CommonUtils.checkNull(p.getA92()).trim(),errInfoStr,errBo,"备注",200));//备注
						tdd.setShopType(checkVarchar(CommonUtils.checkNull(p.getA19()).trim(),errInfoStr,errBo,"经营类型",30));//经营类型
						tdd.setFaxNo(checkVarchar(CommonUtils.checkNull(p.getA23()).trim(),errInfoStr,errBo,"经销商传真",30));//经销商传真
						tdd.setEmail(checkVarchar(CommonUtils.checkNull(p.getA24()).trim(),errInfoStr,errBo,"经销商邮箱",50));//经销商邮箱
						tdd.setIsAuthorizeCity(CommonUtils.checkNull(p.getA26()).trim().equals("是")?Constant.IF_TYPE_YES:Constant.IF_TYPE_YES);//是否是国家品牌授权城市
						tdd.setIsAuthorizeCounty(CommonUtils.checkNull(p.getA27()).trim().equals("是")?Constant.IF_TYPE_YES:Constant.IF_TYPE_YES);//是否是国家品牌授权区县
						tdd.setAuthorizeBrand(checkVarchar(CommonUtils.checkNull(p.getA28()).trim(),errInfoStr,errBo,"国家品牌授权",30));//国家品牌授权
						tdd.setAuthorizeGetDate(Utility.getDate(CommonUtils.checkNull(p.getA29()).trim(),1));//国家品牌授权信息收集时间
						tdd.setAuthorizeSubDate(Utility.getDate(CommonUtils.checkNull(p.getA30()).trim(),1));//国家品牌授权提交时间
						tdd.setAuthorizeEffectDate(Utility.getDate(CommonUtils.checkNull(p.getA31()).trim(),1));//国家品牌授权起始时间
						tdd.setAnnouncementNo(checkVarchar(CommonUtils.checkNull(p.getA32()).trim(),errInfoStr,errBo,"工商总局公告号",50));//工商总局公告号
						tdd.setAnnouncementDate(Utility.getDate(CommonUtils.checkNull(p.getA33()).trim(),1));//工商总局公布日期
						tdd.setAnnouncementEndDate(Utility.getDate(CommonUtils.checkNull(p.getA34()).trim(),1));//国家品牌授权截止时间
						if(CommonUtils.checkNull(p.getA83()).trim().equals("")){
							
						}else{
							String imgc=CodeDict.getDictCodeByName(Constant.IMAGE_LEVEL.toString(), p.getA83().trim());
							if(imgc!=null){
								tdd.setImageComfirmLevel(Integer.parseInt(imgc));
							}else{
								errInfoStr+="VI形象验收确定级别输入有误【A类;B类;C类;D类;E类;F类】！";
								errBo=true;
							}
						}
						//判断是否有错误信息,有就不执行插入或更新操作
						if(errBo){
							errorMap.put("rowNum", p.getRowNumber());//错误行号
							errorMap.put("errorInfo", errInfoStr);//错误信息
							errorJsp.add(errorMap);//保存错误信息
							continue;
						}
						//首先判断是否有公司，有公司就修改公司信息，再添加经销商
						List<Map<String, Object>> comList=dao.getCompany(CommonUtils.checkNull(p.getA6().split("-")[0]).trim());
						//如果公司有 更新
						if(comList!=null && comList.size()>0){
							tcpo.setUpdateBy(logonUser.getUserId());//创建人
							tcpo.setUpdateDate(new Date());
							//更新公司表数据
							TmCompanyPO tcpo1=new TmCompanyPO();
							tcpo1.setCompanyCode(CommonUtils.checkNull(p.getA6().split("-")[0]).trim());
							dao.update(tcpo1, tcpo);
						}else{//无 添加公司表数据
							tcpo.setCompanyId(Long.parseLong(SequenceManager.getSequence("")));
							tcpo.setCreateBy(logonUser.getUserId());//创建人
							tcpo.setCreateDate(new Date());
							dao.insert(tcpo);
						}
						//获取公司ID
						TmCompanyPO c1=new TmCompanyPO();
						c1.setCompanyCode(CommonUtils.checkNull(p.getA6().split("-")[0]).trim());
						TmCompanyPO tmc=(TmCompanyPO)dao.select(c1).get(0);
						//公司
						tdpo.setCompanyId(tmc.getCompanyId());//公司ID
						//无公司添加公司，在添加经销商
						hiDealerId=SequenceManager.getSequence("");
						tdpo.setDealerId(Long.parseLong(hiDealerId));
						tdpo.setIsStatus(1);
						tdpo.setIsStatus1(3);
						dao.insert(tdpo);
						
						//の誌鴻  2014.10.29下午 9:59:02
						//新增经销商的时候，插入这个start
						TmDealerBusinessAreaPO businessAreaPO = new TmDealerBusinessAreaPO();
						businessAreaPO.setAreaId(new Long(Constant.areaId));
						businessAreaPO.setDealerId(tdpo.getDealerId());
						businessAreaPO.setCreateBy(logonUser.getUserId());
						businessAreaPO.setCreateDate(new Date());
						businessAreaPO.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
						dao.insert(businessAreaPO);
						//新增经销商的时候，插入这个end

						
						//插入默认代理区域
						TtProxyAreaPO areaPO = new TtProxyAreaPO();
						areaPO.setCreateBy(logonUser.getUserId());
						areaPO.setCreateDate(new Date());
						areaPO.setDealerId(Long.parseLong(hiDealerId));
						areaPO.setId(Long.parseLong(SequenceManager.getSequence("")));
						
						if (PId.equals("500000") || PId.equals("120001")||PId.equals("310000")||PId.equals("110000")) {
							areaPO.setProxyArea(CCId);
							areaPO.setProxyAreaName(p.getA4());
						}else{
							areaPO.setProxyArea(CId);
							areaPO.setProxyAreaName(p.getA3());
						}
						
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
						/*List<Map<String, Object>> toi=dao.getDO(CommonUtils.checkNull(p.getA6()).trim(),CommonUtils.checkNull(p.getA2()).trim(),"-1");
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
					}
				}
			}
			//经销商（入查询到就更新 否则添加）
			//根据经销商代码判断规则[公司代码-SO1] 销售是SO1 售后是SH1 判断是否有数据
			act.setOutData("errorJsp", errorJsp);
			act.setForword(xsDealerImportCompleteUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e);
			act.setException(e1);
		}
	}
	
	public void importExcel2nd() {
		ActionContext act = ActionContext.getContext();
		XsDealerDao dao = XsDealerDao.getInstance();
		TmpXsDealerPO ppo = new TmpXsDealerPO();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> errorJsp = new ArrayList();

			List<PO> l = dao.select(ppo);
			if (l != null && l.size() > 0) {
				for (int i = 0; i < l.size(); i++) {
					TmpXsDealerPO p = (TmpXsDealerPO) l.get(i);
					String hiDealerId = "";// 经销商ID 中间表保存需要
					String hiOrgId = "";// 组织ID 中间表保存需要
					errInfoStr = "";// 记录错误信息
					errBo = false;// 判断是否有错误信息
					Map<String, Object> errorMap = new HashMap<String, Object>();
					TmDealerPO po = new TmDealerPO();
					TmDealerPO tdpo = new TmDealerPO();
					
					// 	经销商CODE取得
					String dealer_code_secend = "";
					if (StringUtils.isEmpty(p.getA8())) {
						List<Map<String, Object>> pdd = dao.getPDealer(CommonUtils.checkNull(p.getA5()).trim());
						if (pdd != null && pdd.size() == 1) {// 有就设置
							tdpo.setParentDealerD(Long.parseLong(pdd.get(0).get("DEALER_ID").toString()));// 上级经销商
							List<Object> inParameter = new ArrayList();// 输入参数
							inParameter.add(tdpo.getParentDealerD());
							inParameter.add(logonUser.getUserId());
							List outParameter = new ArrayList();// 输出参数
							outParameter.add(java.sql.Types.VARCHAR);
							outParameter = POFactoryBuilder.getInstance().callProcedure("P_GET_SNDDEALERCODE",inParameter,outParameter);
							dealer_code_secend = outParameter.get(0)==null?"-1":outParameter.get(0).toString();
							tdpo.setDealerCode(dealer_code_secend);// 经销商CODE
						} else {
							if (CommonUtils.checkNull(p.getA17()).trim().equals("二级")) {
								errInfoStr += "上级单位输入有误,请确认是否输错(必须全称)!";
								errBo = true;
							}
						}
					} else {
						dealer_code_secend = p.getA8().trim();
					}
					po.setDealerCode(dealer_code_secend);
					List<PO> ps = dao.select(po);
					if(ps.size()==0){
						hiDealerId = "";
					}else{
						hiDealerId = ((TmDealerPO) ps.get(0)).getDealerId().toString();
					}

					// 经销商表
					hiDealerId = SequenceManager.getSequence("");
					tdpo.setDealerId(Long.parseLong(hiDealerId));
					String PId = "";// 省份ID下面调用
					String CId = "";// 城市ID下面调用
					String CCId = "";// 地区ID下面调用
					List<Map<String, Object>> proId = dao.getProvinceId(CommonUtils.checkNull(p.getA2()).trim());
					if (proId != null && proId.size() == 1) {
						PId = proId.get(0).get("REGION_CODE").toString();
						tdpo.setProvinceId(Long.parseLong(PId));// 省份
						List<Map<String, Object>> cId = dao.getCityId(proId.get(0).get("REGION_ID").toString(), CommonUtils.checkNull(p.getA3()).trim());
						if (cId != null && cId.size() == 1) {
							CId = cId.get(0).get("REGION_CODE").toString();
							tdpo.setCityId(Long.parseLong(CId));// 城市
							List<Map<String, Object>> coId = dao.getContinusId(cId.get(0).get("REGION_ID").toString(), CommonUtils.checkNull(p.getA4()).trim());
							if (coId != null && coId.size() == 1) {
								CCId = coId.get(0).get("REGION_CODE").toString();
								tdpo.setCounties(Integer.parseInt(CCId));// 地区
							} else {
								errInfoStr += "区县输入有误！";
								errBo = true;
							}
						} else {
							errInfoStr += "城市输入有误！";
							errBo = true;
						}
					} else {
						errInfoStr += "省份输入有误！";
						errBo = true;
					}
					if(StringUtils.isNotEmpty(p.getA7()) && ValidateUtil.isDigit(p.getA7().trim(),0,9)){
						tdpo.setZipCode(checkVarchar(CommonUtils.checkNull(p.getA7()).trim(), errInfoStr, errBo, "邮编", 10));// 邮编
					}
					
					// 根据大区名称获取大区代码
					List<Map<String, Object>> bdd = dao.getBigId(CommonUtils.checkNull(p.getA1()).trim());
					if (bdd != null && bdd.size() == 1) {// 有就设置
						hiOrgId = bdd.get(0).get("ORG_ID").toString();
						tdpo.setDealerOrgId(Long.parseLong(hiOrgId));// 大区
					} else {
						errInfoStr += "所属区域输入有误！";
						errBo = true;
					}

					tdpo.setProvinceId(PId == "" ? -1L : Long.parseLong(PId));// 省份
					tdpo.setCityId(CId == "" ? -1L : Long.parseLong(CId));// 城市
					tdpo.setCounties(CCId == "" ? -1 : Integer.parseInt(CCId));// 地区

					tdpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));// 默认
					
					if (p.getA9().toString().equals("正常")) {
						tdpo.setServiceStatus(Long.parseLong(Constant.DLR_SERVICE_STATUS_SECEND_01));
					} else if (p.getA9().toString().equals("注销")) {
						tdpo.setServiceStatus(Long.parseLong(Constant.DLR_SERVICE_STATUS_SECEND_02));
					} else if (p.getA9().toString().equals("待退")) {
						tdpo.setServiceStatus(Long.parseLong(Constant.DLR_SERVICE_STATUS_SECEND_03));
					} else if (p.getA9().toString().equals("试运营")) {
						tdpo.setServiceStatus(Long.parseLong(Constant.DLR_SERVICE_STATUS_SECEND_04));
					}else if (p.getA9().toString().equals("升级")) {
						tdpo.setServiceStatus(Long.parseLong(Constant.DLR_SERVICE_STATUS_SECEND_05));
					}else if (p.getA9().toString().equals("降级")) {
						tdpo.setServiceStatus(Long.parseLong(Constant.DLR_SERVICE_STATUS_SECEND_06));
					}
					tdpo.setDealerName(checkVarchar(CommonUtils.checkNull(p.getA10()).trim(), errInfoStr, errBo, "经销商名称", 300));// 经销商名称
					tdpo.setDealerShortname(checkVarchar(CommonUtils.checkNull(p.getA11()).trim(), errInfoStr, errBo, "经销商简称", 150));// 经销商简称
					tdpo.setDealerType(Constant.DEALER_TYPE_DVS);// 经销商级别
					tdpo.setSecondDealerMail(p.getA17()); //二级网络邮箱
					tdpo.setLegal(checkVarchar(CommonUtils.checkNull(p.getA24()), errInfoStr, errBo, "负责人", 20));// 负责人
					tdpo.setLegalPhone(checkVarchar(CommonUtils.checkNull(p.getA25()), errInfoStr, errBo, "负责人办公电话", 30));// 负责人办公电话
					tdpo.setLegalTelphone(checkVarchar(CommonUtils.checkNull(p.getA26()), errInfoStr, errBo, "负责人电话", 30));// 负责人电话
					tdpo.setLegalEmail(checkVarchar(CommonUtils.checkNull(p.getA27()), errInfoStr, errBo, "负责人邮箱", 30));// 负责人邮箱
					tdpo.setStatus(Constant.STATUS_ENABLE);// 经销商状态
					tdpo.setOemCompanyId(Long.parseLong(Constant.OEM_ACTIVITIES));// 默认
					tdpo.setBrand("BQHS");// 默认北汽
					tdpo.setIsSpecial(Constant.IF_TYPE_NO);// 是否特商
					tdpo.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_01);
					tdpo.setDealerLevel(Constant.DEALER_LEVEL_02);
					
					
					/*tdpo.setAddress(checkVarchar(CommonUtils.checkNull(p.getA14()).trim(), errInfoStr, errBo, "企业注册地址", 300));// 企业注册地址
					if (CommonUtils.checkNull(p.getA17()).trim().equals("一级")) {
						tdpo.setDealerLevel(Constant.DEALER_LEVEL_01);// 代理等级对应经销商等级
					} else if (CommonUtils.checkNull(p.getA17()).trim().equals("二级")) {
						tdpo.setDealerLevel(Constant.DEALER_LEVEL_02);// 代理等级对应经销商等级
					} else {
						errInfoStr += "代理级别输入有误【只能输入一级或二级】！";
						errBo = true;
					}*/

					// 经销商明显表数据插入
					TmDealerDetailPO tdd = new TmDealerDetailPO();
					tdd.setCompanyAddress(checkVarchar(CommonUtils.checkNull(p.getA14()), errInfoStr, errBo, "销售展厅地址", 300));// 销售展厅地址
					tdd.setActingBrandName(checkVarchar(CommonUtils.checkNull(p.getA15()), errInfoStr, errBo, "代理其它品牌名称", 200));// 代理其它品牌名称
					tdd.setHotline(checkVarchar(CommonUtils.checkNull(p.getA16()), errInfoStr, errBo, "二级网络销售热线", 30));// 销售热线
					tdd.setProxyVehicleType(CommonUtils.checkNull(p.getA18()));// 代理车型
					if(StringUtils.isNotEmpty(p.getA19()) && ValidateUtil.isDigit(p.getA19(),0,9)){
						tdd.setMinStock(Long.parseLong(CommonUtils.checkNull(p.getA19()).trim()));// 最低库存
					}else{
						errInfoStr += "最低库存必须是正整数！";
						errBo = true;
					}
					if(StringUtils.isNotEmpty(p.getA20()) && ValidateUtil.isDigit(p.getA20(),0,9)){
						tdd.setOmeArea(Long.parseLong(CommonUtils.checkNull(p.getA20()).trim()));// 北汽面积
					}
					if(StringUtils.isNotEmpty(p.getA21()) && ValidateUtil.isDigit(p.getA21().trim(),0,9)){
						tdd.setOmePeopleTotal(Long.parseLong(CommonUtils.checkNull(p.getA21()).trim()));// 北汽人数
					}
					if(ValidateUtil.isDigit(p.getA22().trim(),0,9)){
						tdd.setYearPlan(Long.parseLong(CommonUtils.checkNull(p.getA22()).trim()));// 全年任务
					}else{
						errInfoStr += "全年任务必须是正整数！";
						errBo = true;
					}
					tdd.setProxyArea(CommonUtils.checkNull(p.getA23()));// 代理区域
					tdd.setWebmasterName(checkVarchar(CommonUtils.checkNull(p.getA28()), errInfoStr, errBo, "总经理", 30));// 服务经理
					tdd.setWebmasterPhone(checkVarchar(CommonUtils.checkNull(p.getA29()), errInfoStr, errBo, "总经理办公电话", 30));// 服务经理办公电话
					tdd.setWebmasterTelphone(checkVarchar(CommonUtils.checkNull(p.getA30()), errInfoStr, errBo, "总经理手机", 30));// 服务经理手机
					tdd.setWebmasterEmail(checkVarchar(CommonUtils.checkNull(p.getA31()), errInfoStr, errBo, "总经理邮箱", 50));// 服务经理邮箱
					tdd.setMessagerName(checkVarchar(CommonUtils.checkNull(p.getA32()), errInfoStr, errBo, "信息员", 30));// 信息员
					tdd.setMessagerPhone(checkVarchar(CommonUtils.checkNull(p.getA33()), errInfoStr, errBo, "信息员办公电话", 30));// 信息员办公电话
					tdd.setMessagerTelphone(checkVarchar(CommonUtils.checkNull(p.getA34()), errInfoStr, errBo, "信息员手机", 30));// 信息员手机
					tdd.setMessagerQq(checkVarchar(CommonUtils.checkNull(p.getA35()), errInfoStr, errBo, "信息员QQ", 30));// 信息员QQ
					tdd.setMessagerEmail(checkVarchar(CommonUtils.checkNull(p.getA36()), errInfoStr, errBo, "信息员邮箱", 50));// 信息员邮箱
					tdd.setVhclShAddress(checkVarchar(CommonUtils.checkNull(p.getA37()), errInfoStr, errBo, "整车收货地址", 300)); //整车收货地址
					tdd.setVhclShName(checkVarchar(CommonUtils.checkNull(p.getA38()), errInfoStr, errBo, "整车收货联系人", 100)); //整车收货联系人
					if (CommonUtils.checkNull(p.getA39()).trim().equals("男")) {
						tdd.setVhclShSex(Constant.MAN);
					} else if (CommonUtils.checkNull(p.getA39()).trim().equals("女")) {
						tdd.setVhclShSex(Constant.WOMEN);
					} else {
						errInfoStr += "整车收货联系人性别有误【只能输入男或女】！";
						errBo = true;
					}
					tdd.setVhclShTelphone(checkVarchar(CommonUtils.checkNull(p.getA40()), errInfoStr, errBo, "整车收货联系人办公电话", 30)); //整车收货联系人办公电话
					tdd.setVhclShPhone(checkVarchar(CommonUtils.checkNull(p.getA41()), errInfoStr, errBo, "整车收货联系人手机", 30)); //整车收货联系人手机
					tdd.setServiceAddress(CommonUtils.checkNull(p.getA42()));// 服务站地址
					tdd.setServiceHotline(CommonUtils.checkNull(p.getA43()));// 24小时服务热线
					
					/*tdd.setEmail(checkVarchar(CommonUtils.checkNull(p.getA22()).trim(), errInfoStr, errBo, "邮箱", 50));// 邮箱
					tdd.setCompanyZcCode(checkVarchar(CommonUtils.checkNull(p.getA13()).trim(), errInfoStr, errBo, "经销企业组织机构代码", 30));// 经销商组织代码
					
					tdd.setMarketName(checkVarchar(CommonUtils.checkNull(p.getA37()).trim(), errInfoStr, errBo, "销售经理", 30));// 销售经理
					tdd.setMarketPhone(checkVarchar(CommonUtils.checkNull(p.getA38()).trim(), errInfoStr, errBo, "销售经理办公电话", 30));// 销售经理办公电话
					tdd.setMarketTelphone(checkVarchar(CommonUtils.checkNull(p.getA39()).trim(), errInfoStr, errBo, "销售经理手机", 30));// 销售经理手机
					tdd.setMarketEmail(checkVarchar(CommonUtils.checkNull(p.getA40()).trim(), errInfoStr, errBo, "销售经理邮箱", 50));// 销售经理邮箱
					tdd.setManagerName(checkVarchar(CommonUtils.checkNull(p.getA41()).trim(), errInfoStr, errBo, "市场经理", 30));// 市场经理
					tdd.setManagerPhone(checkVarchar(CommonUtils.checkNull(p.getA42()).trim(), errInfoStr, errBo, "市场经理办公电话", 30));// 市场经理办公电话
					tdd.setManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getA43()).trim(), errInfoStr, errBo, "市场经理手机", 30));// 市场经理手机
					tdd.setManagerEmail(checkVarchar(CommonUtils.checkNull(p.getA44()).trim(), errInfoStr, errBo, "市场经理邮箱", 50));// 市场经理邮箱
					tdd.setFinanceManagerName(checkVarchar(CommonUtils.checkNull(p.getA45()).trim(), errInfoStr, errBo, "财务经理", 30));// 财务经理
					tdd.setFinanceManagerPhone(checkVarchar(CommonUtils.checkNull(p.getA46()).trim(), errInfoStr, errBo, "财务经理办公电话", 30));// 财务经理办公电话
					tdd.setFinanceManagerTelphone(checkVarchar(CommonUtils.checkNull(p.getA47()).trim(), errInfoStr, errBo, "财务经理手机", 30));// 财务经理手机
					tdd.setFinanceManagerEmail(checkVarchar(CommonUtils.checkNull(p.getA48()).trim(), errInfoStr, errBo, "财务经理邮箱", 50));// 财务经理邮箱
					
					tdd.setUnionType(checkVarchar(CommonUtils.checkNull(p.getA16()).trim(), errInfoStr, errBo, "单位性质", 30));// 单位性质
					if(ValidateUtil.isDigit(p.getA12().trim(),0,9)){
						tdd.setRegisteredCapital(Double.parseDouble(checkVarchar(CommonUtils.checkNullNum(p.getA12()).trim(), errInfoStr, errBo, "企业注册资金", 15)));// 企业注册资金
					}else{
						errInfoStr += "企业注册资金必须是正整数！";
						errBo = true;
					}
					
					tdd.setIsActingBrand(CommonUtils.checkNull(p.getA19()).trim().equals("是") ? Constant.IF_TYPE_YES : Constant.IF_TYPE_YES);// 是否经营其他品牌
					
					tdd.setShopType(checkVarchar(CommonUtils.checkNull(p.getA18()).trim(), errInfoStr, errBo, "经营类型", 30));// 经营类型
					tdd.setHaveService(CommonUtils.checkNull(p.getA54()).trim());// 是否有自备服务站
					
					if(p.getA54().trim().equals("是")){
						if(p.getA55()==null){
							errInfoStr += "请输入服务站面积！";
							errBo = true;
						}else{
							if(ValidateUtil.isDigit(p.getA55().trim(),0,9)){
								tdd.setServiceArea(Long.parseLong(CommonUtils.checkNull(p.getA55()).trim()));// 服务站面积
							}else{
								errInfoStr += "服务站面积【只能输入正整数】！";
								errBo = true;
							}
						}
					}else if(p.getA54().trim().equals("否")){
						if(StringUtil.isNull(p.getA55())){
						}else{
							errInfoStr += "没有自备服务站请勿输入服务站面积！";
							errBo = true;
						}
					}else{
						errInfoStr += "是否有自备服务站【只能输入是或否】！";
						errBo = true;
					}*/
					
					// 二级经销商其它信息
					TmDealerSecondLevelPO tdslp = new TmDealerSecondLevelPO();
					// 二级网络性质
					if (p.getA44().toString().equals("直营")) {
						tdslp.setSecondLevelNetworkNature(Long.valueOf(Constant.SECOND_LEVEL_NETWORK_NATURE_01));
					} else if (p.getA44().toString().equals("联营")) {
						tdslp.setSecondLevelNetworkNature(Long.valueOf(Constant.SECOND_LEVEL_NETWORK_NATURE_02));
					} else if (p.getA44().toString().equals("其它")) {
						tdslp.setSecondLevelNetworkNature(Long.valueOf(Constant.SECOND_LEVEL_NETWORK_NATURE_03));
					}
					// 竞品品牌
					tdslp.setCompetingBrand(CommonUtils.checkNull(p.getA45()));
					// 与竞品行驶距离（米）
					if (StringUtils.isNotEmpty(p.getA46()) && ValidateUtil.isDouble(p.getA46().trim(),0,10)) {
						tdslp.setAndCompetingRunDistance(Double.parseDouble(p.getA46().trim()));
					}
					// 月均销量
					if (StringUtils.isNotEmpty(p.getA47()) && ValidateUtil.isDigit(p.getA47().trim(),0,20)) {
						tdslp.setMonthAverageSales(Long.parseLong(p.getA47().trim()));
					}
					// 门头长度
					if (StringUtils.isNotEmpty(p.getA48()) && ValidateUtil.isDigit(p.getA48().trim(),0,20)) {
						tdslp.setDoorheadLength(Long.parseLong(p.getA48().trim()));
					}
					// 是否具有销售门头
					if (StringUtils.isNotEmpty(p.getA49())) {

						if (p.getA49().toString().equals("是")) {
							tdslp.setIsHaveSalesDoorhead(Long.valueOf(Constant.IF_TYPE_YES));
						} else if (p.getA49().toString().equals("否")) {
							tdslp.setIsHaveSalesDoorhead(Long.valueOf(Constant.IF_TYPE_NO));
						}
					}
					// 是否具有销售形象墙
					if (StringUtils.isNotEmpty(p.getA50())) {

						if (p.getA50().toString().equals("是")) {
							tdslp.setIsHaveSalesImageWall(Long.valueOf(Constant.IF_TYPE_YES));
						} else if (p.getA50().toString().equals("否")) {
							tdslp.setIsHaveSalesImageWall(Long.valueOf(Constant.IF_TYPE_NO));
						}
					}
					// 服务网点性质
					if (StringUtils.isNotEmpty(p.getA51())) {

						if (p.getA51().toString().equals("自有")) {
							tdslp.setServiceNetworkNature(Long.valueOf(Constant.SERVICE_NETWORK_NATURE_01));
						} else if (p.getA51().toString().equals("合作")) {
							tdslp.setServiceNetworkNature(Long.valueOf(Constant.SERVICE_NETWORK_NATURE_02));
						}
					}
					// 维修资质
					if (StringUtils.isNotEmpty(p.getA52())) {

						if (p.getA52().toString().equals("一类")) {
							tdslp.setRepairAptitude(Long.valueOf(Constant.REPAIR_APTITUDE_01));
						} else if (p.getA52().toString().equals("二类")) {
							tdslp.setRepairAptitude(Long.valueOf(Constant.REPAIR_APTITUDE_02));
						} else if (p.getA52().toString().equals("三类")) {
							tdslp.setRepairAptitude(Long.valueOf(Constant.REPAIR_APTITUDE_03));
						}
					}
					// 服务车间面积
					if (StringUtils.isNotEmpty(p.getA53()) && ValidateUtil.isDouble(p.getA53().trim(),0,10)) {
						tdslp.setServiceWorkshopArea(Double.parseDouble(p.getA53().trim()));
					}
					// 是否具有服务门头
					if (StringUtils.isNotEmpty(p.getA54())) {

						if (p.getA54().toString().equals("是")) {
							tdslp.setIsHaveServiceDoorhead(Long.valueOf(Constant.IF_TYPE_YES));
						} else if (p.getA54().toString().equals("否")) {
							tdslp.setIsHaveServiceDoorhead(Long.valueOf(Constant.IF_TYPE_NO));
						}
					}
					// 是否具有服务形象墙
					if (StringUtils.isNotEmpty(p.getA55())) {

						if (p.getA55().toString().equals("是")) {
							tdslp.setIsHaveServiceImageWall(Long.valueOf(Constant.IF_TYPE_YES));
						} else if (p.getA55().toString().equals("否")) {
							tdslp.setIsHaveServiceImageWall(Long.valueOf(Constant.IF_TYPE_NO));
						}
					}
					// 服务离销售网点距离
					if (StringUtils.isNotEmpty(p.getA56()) && ValidateUtil.isDouble(p.getA56().trim(),0,10)) {
						tdslp.setServiceSalesNetworkDistance(Double.parseDouble(p.getA56().trim()));
					}
					// 维修技术师最低配备 （人员数量）
					if (StringUtils.isNotEmpty(p.getA57()) && ValidateUtil.isDigit(p.getA57().trim(),0,20)) {
						tdslp.setRepairEngineerLowestDeploy(Long.parseLong(p.getA57().trim()));
					}
					List<Map<String, Object>> comList = dao.getCompany(CommonUtils.checkNull(p.getA6().split("-")[0]).trim());

					// 判断是否有错误信息,有就不执行插入或更新操作
					if (errBo) {
						errorMap.put("rowNum", p.getRowNumber());// 错误行号
						errorMap.put("errorInfo", errInfoStr);// 错误信息
						errorJsp.add(errorMap);// 保存错误信息
						continue;
					}
					// 获取公司ID
					TmCompanyPO c1 = new TmCompanyPO();
					c1.setCompanyCode(CommonUtils.checkNull(p.getA6().split("-")[0]).trim());
					TmCompanyPO tmc = (TmCompanyPO) dao.select(c1).get(0);
					// 公司
					tdpo.setCompanyId(tmc.getCompanyId());// 公司ID
					// 更新经销商表
					TmDealerPO tdpo1 = new TmDealerPO();
					tdpo1.setDealerCode(CommonUtils.checkNull(p.getA8()).trim());
					
					if(ps.size()==0){
						tdpo.setCreateBy(logonUser.getUserId());
						tdpo.setCreateDate(new Date());
						dao.insert(tdpo);
						tdslp.setSecondLevelId(Long.parseLong(SequenceManager.getSequence("")));
						tdslp.setFkDealerId(tdpo.getDealerId());
						dao.insert(tdslp);
					}else{
						tdpo.setUpdateBy(logonUser.getUserId());// 创建人
						tdpo.setUpdateDate(new Date());
						dao.update(tdpo1, tdpo);
						TmDealerSecondLevelPO tdslp1 = new TmDealerSecondLevelPO();
						tdslp1.setFkDealerId(tdpo.getDealerId());
						dao.update(tdslp1, tdslp);
					}
					
					// 添加经销商与组织中间表的数据
					if(ps.size()==0){
						TmDealerOrgRelationPO cc = new TmDealerOrgRelationPO();
						cc.setRelationId(Long.parseLong(SequenceManager.getSequence("")));
						cc.setDealerId(Long.parseLong(hiDealerId));
						cc.setOrgId(Long.parseLong(hiOrgId));
						cc.setBusinessType(Constant.ORG_TYPE_OEM);
						dao.insert(cc);
					}

					
					// 更新经销商明显表
					TmDealerDetailPO tdd2 = new TmDealerDetailPO();
					tdd2.setFkDealerId(Long.parseLong(hiDealerId));
					List<PO> plist = dao.select(tdd2);
					if (plist != null && plist.size() > 0) {
						// 更新经销商明显表
						TmDealerDetailPO tdd1 = new TmDealerDetailPO();
						tdd1.setFkDealerId(Long.parseLong(hiDealerId));
						dao.update(tdd1, tdd);
					} else {
						// 新增
						TmDealerDetailPO tdd1 = new TmDealerDetailPO();
						tdd.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
						tdd.setFkDealerId(Long.parseLong(hiDealerId));
						tdd.setUpdateDate(new Date());
						dao.insert(tdd);
					}
					if(logonUser.getDealerId()==null){
						if(ps.size()==0){
							TtDealerSecendAuditPO auditPO = new TtDealerSecendAuditPO();
				 			auditPO.setCreateBy(logonUser.getUserId());
				 			auditPO.setCreateDate(new Date());
				 			auditPO.setDealerId(Long.parseLong(hiDealerId));
				 			auditPO.setId(Long.parseLong(SequenceManager.getSequence("")));
				 			auditPO.setStatus(Constant.DEALER_SECEND_STATUS_06);
				 			auditPO.setUserId(Constant.DEALER_SECEND_AUDIT_04);
							
							DealerInfoDao.getInstance().insert(auditPO);
							
							TmDealerPO dealerPO = new TmDealerPO();
							TmDealerPO dealerPO1 = new TmDealerPO();
							
							dealerPO.setDealerId(auditPO.getDealerId());
							
							dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_06);
							
							dao.update(dealerPO, dealerPO1);
						}
					}else{
						if(ps.size()==0){
							TtDealerSecendAuditPO auditPO = new TtDealerSecendAuditPO();
				 			auditPO.setCreateBy(logonUser.getUserId());
				 			auditPO.setCreateDate(new Date());
				 			auditPO.setDealerId(Long.parseLong(hiDealerId));
				 			auditPO.setId(Long.parseLong(SequenceManager.getSequence("")));
				 			auditPO.setStatus(Constant.DEALER_SECEND_STATUS_01);
				 			auditPO.setUserId(Constant.DEALER_SECEND_AUDIT_01);
							
							DealerInfoDao.getInstance().insert(auditPO);
							
							TmDealerPO dealerPO = new TmDealerPO();
							TmDealerPO dealerPO1 = new TmDealerPO();
							
							dealerPO.setDealerId(auditPO.getDealerId());
							
							dealerPO1.setSecendAutidStatus(Constant.DEALER_SECEND_STATUS_01);
							
							dao.update(dealerPO, dealerPO1);
						}
					}
				}
			}
			act.setOutData("errorJsp", errorJsp);
			List<Object> inParameter = new ArrayList();
			List outParameter = new ArrayList();
			dao.callProcedure("p_vw_org_dealer_service", inParameter, outParameter);
			act.setForword(xsDealerImportCompleteUrl2nd);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "");
			logger.error(logonUser, e);
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
	 * 经销商导入模板（销售）下载
	 * @author RANJ
	 */
	public void downloadTempleXs(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			// 用于下载传参的
			List<List<Object>> list = new LinkedList<List<Object>>();
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("所属区域"); 
			listHead.add("省份");
			listHead.add("所属行政区域城市"); 
			listHead.add("所属行政区域区县");
			listHead.add("邮编");
			listHead.add("经销商编码{100001-S}");
			listHead.add("经销商状态{意向,正常,拟撤,已撤}");
			listHead.add("所属上级单位{无上级默认-1}");
			listHead.add("对应服务商编码");
			listHead.add("对应服务商状态");
			listHead.add("经销商全称");
			listHead.add("经销企业注册资金");
			listHead.add("经销企业组织机构代码");
			listHead.add("经销商简称");
			listHead.add("企业注册地址");
			listHead.add("销售展厅地址");
			listHead.add("单位性质{国营,民营,合资,其它}");
			listHead.add("代理级别{一级,二级}");
			listHead.add("经营类型{代理,直营}");
			listHead.add("是否经营其他品牌{是,否}");
			listHead.add("代理其它品牌名称");
			listHead.add("经销商销售热线");
			listHead.add("经销商传真");
			listHead.add("经销商邮箱");
			listHead.add("代理车型");
//			listHead.add("代理区域");
			listHead.add("是否国家品牌授权城市{是,否}");
			listHead.add("是否国家品牌授权区县{是,否}");
			listHead.add("国家品牌授权");
			listHead.add("国家品牌授权信息收集时间{格式为2014-01-14}");
			listHead.add("国家品牌授权提交时间{格式为2014-01-14}");
			listHead.add("国家品牌授权起始时间{格式为2014-01-14}");
			listHead.add("工商总局公告号");
			listHead.add("工商总局公布日期{格式为2014-01-14}");
			listHead.add("国家品牌授权截止时间{格式为2014-01-14}");
			listHead.add("品牌授权书打印");
			listHead.add("经销企业法人");
			listHead.add("法人办公电话");
			listHead.add("法人手机");
			listHead.add("法人邮箱");
			listHead.add("总经理");
			listHead.add("总经理办公电话");
			listHead.add("总经理手机");
			listHead.add("总经理邮箱");
			listHead.add("销售经理");
			listHead.add("销售经理办公电话");
			listHead.add("销售经理手机");
			listHead.add("销售经理邮箱");
			listHead.add("市场经理");
			listHead.add("市场经理办公电话");
			listHead.add("市场经理手机");
			listHead.add("市场经理邮箱");
			listHead.add("服务经理");
			listHead.add("服务经理办公电话");
			listHead.add("服务经理手机");
			listHead.add("服务经理邮箱");
			listHead.add("财务经理");
			listHead.add("财务经理办公电话");
			listHead.add("财务手机");
			listHead.add("财务邮箱");
			listHead.add("信息员");
			listHead.add("信息员办公电话");
			listHead.add("信息员手机");
			listHead.add("信息员QQ");
			listHead.add("信息员邮箱");
			listHead.add("开票公司名称");
			listHead.add("开票联系人");
			listHead.add("开票联系人办公电话");
			listHead.add("开票联系人手机");
			listHead.add("开票信息地址");
			listHead.add("开户行全称");
			listHead.add("开户行账号");
			listHead.add("纳税识别号");
			listHead.add("信函收件地址");
			listHead.add("信函收件联系人");
			listHead.add("信函收件人性别");
			listHead.add("信函收件联系人办公电话");
			listHead.add("信函收件联系人手机");
			listHead.add("VI建设申请日期{格式为2014-01-14}");
			listHead.add("VI建设开工日期{格式为2014-01-14}");
			listHead.add("VI建设竣工日期{格式为2014-01-14}");
			listHead.add("VI形象验收日期{格式为2014-01-14}");
			listHead.add("拟建店类别{A类;B类;C类;D类;E类;F类}");
			listHead.add("VI形象验收确定级别{A类;B类;C类;D类;E类;F类}");
			listHead.add("VI支持总金额");
			listHead.add("VI支持首批比例");
			listHead.add("VI支持后续支持方式");
			listHead.add("VI支持起始时间{格式为2014-01-14}");
			listHead.add("VI支持截止时间{格式为2014-01-14}");
			listHead.add("首次提车时间{格式为2014-01-14}");
			listHead.add("首次到车日期{格式为2014-01-14}");
			listHead.add("首次销售时间{格式为2014-01-14}");
			listHead.add("备注");
			//listHead.add("自建新增字段项");
			list.add(listHead);
			// 导出的文件名
			String fileName = "经销商导入模板（销售）.xls";
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
	
	public void downloadTempleXs2nd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			// 用于下载传参的
			List<List<Object>> list = new LinkedList<List<Object>>();
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("所属省份"); 
			listHead.add("省份");
			listHead.add("所属行政区域城市"); 
			listHead.add("所属行政区域区县");
			listHead.add("所属一级经销商名称");
			listHead.add("所属一级经销商代码");
			listHead.add("邮编");
			listHead.add("二级网络编码{系统自动生成无需输入}");
			listHead.add("二级网络状态{正常,注销,待退,试运营,升级,降级}");
			listHead.add("二级网络全称");
			listHead.add("二级网络简称");
			listHead.add("对应服务商编码");
			listHead.add("对应服务商状态");
			/*listHead.add("经销企业注册资金");
			listHead.add("经销企业组织机构代码");
			listHead.add("企业注册地址");*/
			listHead.add("销售展厅地址");
			/*listHead.add("单位性质{国营,民营,合资,其它}");
			listHead.add("代理级别{一级,二级}");
			listHead.add("经营类型{代理,直营}");
			listHead.add("是否经营其他品牌{是,否}");*/
			listHead.add("代理其它品牌名称");
			listHead.add("二级网络销售热线");
			listHead.add("二级网络邮箱");
			listHead.add("代理车型");
			listHead.add("最低库存");
			listHead.add("北汽幻速专营面积（平方米，请输入正整数)");
			listHead.add("北汽幻速专营销售人员数（请输入正整数)");
			listHead.add("全年任务量（请输入正整数)");
			listHead.add("代理区域");
			listHead.add("负责人");
			listHead.add("负责人办公电话");
			listHead.add("负责人手机");
			listHead.add("负责人邮箱");
			listHead.add("服务经理");
			listHead.add("服务经理办公电话");
			listHead.add("服务经理手机");
			listHead.add("服务经理邮箱");
			/*listHead.add("销售经理");
			listHead.add("销售经理办公电话");
			listHead.add("销售经理手机");
			listHead.add("销售经理邮箱");
			listHead.add("市场经理");
			listHead.add("市场经理办公电话");
			listHead.add("市场经理手机");
			listHead.add("市场经理邮箱");
			listHead.add("财务经理");
			listHead.add("财务经理办公电话");
			listHead.add("财务手机");
			listHead.add("财务邮箱");*/
			
			listHead.add("信息员");
			listHead.add("信息员办公电话");
			listHead.add("信息员手机");
			listHead.add("信息员QQ");
			listHead.add("信息员邮箱");
			listHead.add("整车收货地址");
			listHead.add("整车收货联系人");
			listHead.add("整车收货联系人性别");
			listHead.add("整车收货联系人办公电话");
			listHead.add("整车收货联系人手机");
			listHead.add("服务站地址");
			listHead.add("24小时服务热线");
			listHead.add("二级网络性质");
			listHead.add("竞品品牌");
			listHead.add("与竞品行驶距离（米）");
			listHead.add("月均销量");
			listHead.add("门头长度");
			listHead.add("是否具有销售门头");
			listHead.add("是否具有销售形象墙");
			listHead.add("服务网点性质");
			listHead.add("维修资质");
			listHead.add("服务车间面积");
			listHead.add("是否具有服务门头");
			listHead.add("是否具有服务形象墙");
			listHead.add("服务离销售网点距离");
			listHead.add("维修技术师最低配备 （人员数量）");
			list.add(listHead);
			// 导出的文件名
			String fileName = "二级经销商导入模板（销售）.xls";
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
	public void downloadTempleXsAddress(){
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
			listHead.add("地址类型(必须为：广宣收货地址,信函收件地址,整车收货地址,发票收件地址)");
			listHead.add("地址状态(必须为有效,无效)");
			listHead.add("详细地址");
			
			list.add(listHead);
			// 导出的文件名
			String fileName = "经销商地址导入模板（销售）.xls";
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
	 * 经销商（销售）地址导入临时表
	 */
	public void xsImportAddressOperate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ShDealerDao dao=ShDealerDao.getInstance();
        try {
			RequestWrapper request = act.getRequest();
			
			TmpXsDealerAddressPO po=new TmpXsDealerAddressPO();
			//清空临时表中目标年度的数据
			dao.delete(po);
			long maxSize=1024*1024*5;
			//
			insertIntoTmp(request, "uploadFile",8,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(xsDealerImportFailureUrl);
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTmpShDealerAddress(list, logonUser.getUserId());
				//校验临时表数据
				List<ExcelErrors> errorList=null;
				errorList=checkData();
				if(null!=errorList){
					act.setOutData("errorList", errorList);
					act.setForword(xsDealerImportFailureUrl);
				}else{
					/*String planVer=CommonUtils.checkNull(request.getParamValue("verNo")); //版本号
					// 取得临时表数据
					List<Map<String, Object>> countList = dao.selectTmpShDealerList();
					act.setOutData("countList", countList);*/
					act.setForword(xsDealerAddressImportSuccessUrl);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void xsImportAddressOperate2nd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ShDealerDao dao=ShDealerDao.getInstance();
        try {
			RequestWrapper request = act.getRequest();
			
			TmpXsDealerAddressPO po=new TmpXsDealerAddressPO();
			//清空临时表中目标年度的数据
			dao.delete(po);
			long maxSize=1024*1024*5;
			//
			insertIntoTmp(request, "uploadFile",8,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(xsDealerImportFailureUrl2nd);
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTmpShDealerAddress(list, logonUser.getUserId());
				//校验临时表数据
				List<ExcelErrors> errorList=null;
				errorList=checkData();
				if(null!=errorList){
					act.setOutData("errorList", errorList);
					act.setForword(xsDealerImportFailureUrl2nd);
				}else{
					/*String planVer=CommonUtils.checkNull(request.getParamValue("verNo")); //版本号
					// 取得临时表数据
					List<Map<String, Object>> countList = dao.selectTmpShDealerList();
					act.setOutData("countList", countList);*/
					act.setForword(xsDealerAddressImportSuccessUrl2nd);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
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
	private void parseCellsForAddress(String rowNum,Cell[] cells,Long userId) throws Exception{
		ShDealerDao dao=ShDealerDao.getInstance();
		TmpXsDealerAddressPO po=new TmpXsDealerAddressPO();
		
		po.setDealerCode(lastNull(0,cells,"").toString());
		po.setDealerName(lastNull(1,cells,"").toString());
		po.setLinkman(lastNull(2,cells,"").toString());
//		po.setGender(lastNull(3,cells,"").toString());
		po.setMobilePhone(lastNull(3,cells,"").toString());
		po.setTel(lastNull(4,cells,"").toString());
		po.setAddressType(lastNull(5,cells,"").toString());
		po.setState(lastNull(6,cells,"").toString());
		po.setAddr(lastNull(7,cells,"").toString());
		po.setId(Long.valueOf(rowNum)-1);
		
        dao.insert(po);	
	}
	
	public void xsImportAddressOperateQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ShDealerDao dao=ShDealerDao.getInstance();
		try {
				Map<String, Object> map = new HashMap<String, Object>();
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.selectTmpXSDealerAddressQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后临时表信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 导入业务表（导入正式数据表地址导入）
	 */
	public void importExcelForAddress(){
		ActionContext act = ActionContext.getContext();
		ShDealerDao dao=ShDealerDao.getInstance();
		TmpXsDealerAddressPO ppo=new TmpXsDealerAddressPO();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String,Object>> errorJsp=new ArrayList();
			Map<String,Object> errorMap=new HashMap<String,Object>();
			List<PO> l = dao.select(ppo);
			TmVsAddressPO addrDefinePO = new TmVsAddressPO();
			TmVsAddressMorePO ttPartAddressMorePO = new TmVsAddressMorePO();
			errInfoStr = "";
			errBo = false;
			if(l!=null && l.size()>0){
				for(int i=0;i<l.size();i++){
					TmpXsDealerAddressPO PO = (TmpXsDealerAddressPO)l.get(i);
					TmDealerPO tmd = new TmDealerPO();
					tmd.setDealerCode(PO.getDealerCode());
					tmd.setDealerType(10771001);
					if(dao.select(tmd).size()==0){
						errInfoStr+="输入的经销商代码不存在!  ";
						errBo=true;
					}
					else{
						TmDealerPO tmDealerPO = (TmDealerPO)dao.select(tmd).get(0);
						addrDefinePO.setDealerId(tmDealerPO.getDealerId());
						ttPartAddressMorePO.setDealerId(tmDealerPO.getDealerId());
					}
					
					if((!PO.getAddressType().equals("广宣收货地址"))
							&&(!PO.getAddressType().equals("发票收件地址"))
							&&(!PO.getAddressType().equals("信函收件地址"))
							&&(!PO.getAddressType().equals("整车收货地址"))){
						errInfoStr+="地址类型必须是 : 广宣收货地址,发票收件地址,信函收件地址,整车收货地址!";
						errBo=true;
					}
					else{
						TcCodePO codePO = new TcCodePO();
						codePO.setCodeDesc(PO.getAddressType());
						TcCodePO TcCodePO = (TcCodePO)dao.select(codePO).get(0);
						ttPartAddressMorePO.setAddressType(Integer.parseInt(TcCodePO.getCodeId()));
						addrDefinePO.setAddressType(Integer.valueOf(TcCodePO.getCodeId()));
					}
					
					if((!PO.getState().equals("有效"))
							&&(!PO.getState().equals("无效"))){
						errInfoStr+="地址状态 必须是: 有效,无效!";
						errBo=true;
					}
					else{
						TcCodePO codePO = new TcCodePO();
						codePO.setCodeDesc(PO.getState());
						TcCodePO TcCodePO = (TcCodePO)dao.select(codePO).get(0);
						ttPartAddressMorePO.setStatus(Integer.parseInt(TcCodePO.getCodeId()));
						addrDefinePO.setStatus(Integer.parseInt(TcCodePO.getCodeId()));
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
//						ttPartAddressMorePO.setSex(Integer.parseInt(TcCodePO.getCodeId()));
//						addrDefinePO.setSex(Integer.parseInt(TcCodePO.getCodeId()));
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
						if(ttPartAddressMorePO.getAddressType().equals(Constant.SH_ADDRESS_TYPE_04)){
							
							addrDefinePO.setAddress(checkVarchar(CommonUtils.checkNull(PO.getAddr()).trim(),errInfoStr,errBo,"详细地址",300));
							addrDefinePO.setId(Long.parseLong(SequenceManager.getSequence("")));
							addrDefinePO.setTel(PO.getTel());
							addrDefinePO.setMobilePhone(PO.getMobilePhone());
							addrDefinePO.setLinkMan(PO.getLinkman());
							addrDefinePO.setCreateBy(logonUser.getUserId());
							addrDefinePO.setCreateDate(new Date()); 
							
							dao.insert(addrDefinePO);
						}else{
							
							ttPartAddressMorePO.setAddress(checkVarchar(CommonUtils.checkNull(PO.getAddr()).trim(),errInfoStr,errBo,"详细地址",300));
							ttPartAddressMorePO.setId(Long.parseLong(SequenceManager.getSequence("")));
							ttPartAddressMorePO.setTel(PO.getTel());
							ttPartAddressMorePO.setMobilePhone(PO.getMobilePhone());
							ttPartAddressMorePO.setLinkMan(PO.getLinkman());
							ttPartAddressMorePO.setCreateBy(logonUser.getUserId());
							ttPartAddressMorePO.setCreateDate(new Date()); 
							dao.insert(ttPartAddressMorePO);
						}
					}
				}
			}

			act.setOutData("errorJsp", errorJsp);
			act.setForword(xsDealerImportCompleteUrl);
		} catch (Exception e) {
			POContext.endTxn(false);//added by lijj 2014-05-21
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}

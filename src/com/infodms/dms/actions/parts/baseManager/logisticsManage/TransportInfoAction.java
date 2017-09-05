package com.infodms.dms.actions.parts.baseManager.logisticsManage;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.parts.baseManager.logisticsManage.LogisticsDao;
import com.infodms.dms.dao.parts.baseManager.logisticsManage.TransportAndValueDao;
import com.infodms.dms.dao.parts.baseManager.logisticsManage.TransportInfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtTransportinfoPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 运输方式维护
 * @author hyy 
 * @version 2017-7-6
 * @see 
 * @since 
 */
public class TransportInfoAction {

	private TransportInfoDao transportDao = TransportInfoDao.getInstance();
	private TransportAndValueDao transValuationDao = TransportAndValueDao.getInstance();
	private final LogisticsDao logiDao = LogisticsDao.getInstance();
	
	public Logger logger = Logger.getLogger(TransportInfoAction.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String transporInfoInitUrl = "/jsp/parts/baseManager/logisticsManage/transportInfo.jsp";
	private final String transporInfoAddUrl = "/jsp/parts/baseManager/logisticsManage/transportInfoAdd.jsp";
	private final String transporInfoUpdUrl = "/jsp/parts/baseManager/logisticsManage/transportInfoUpd.jsp";

	public void transportInfoInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//承运商
			List logisticsList=logiDao.selLogistics();
			//运输方式与计价
			List modeList=transValuationDao.selTvRelation();
            act.setOutData("logisticsList", logisticsList);
            act.setOutData("modeList", modeList);
            
			act.setForword(transporInfoInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运输方式维护初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 查询运输信息
	 */
	public void transportInfoQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String logi=act.getRequest().getParamValue("logiCode");//承运商
			String tvCode=act.getRequest().getParamValue("tvCode");//运输与计价方式
			String placeProvinc=act.getRequest().getParamValue("PROVINCE_ID");//出发地 省
			String placeCity=act.getRequest().getParamValue("CITY_ID");//出发地 市
			String placeCounties=act.getRequest().getParamValue("COUNTIES");//出发地 区
			String destProvinc=act.getRequest().getParamValue("PROVINCE_ID1");//目的地 省
			String destCity=act.getRequest().getParamValue("CITY_ID1");//目的地 市
			String destCounties=act.getRequest().getParamValue("COUNTIES1");//目的地 区
			
			TtTransportinfoPO po=new TtTransportinfoPO();
			po.setCarrier(logi);
			po.setTvId(tvCode);
			po.setPlaceProvinceId(placeProvinc);
			po.setPlaceCityId(placeCity);
			po.setPlaceCounties(placeCounties);
			po.setDestProvinceId(destProvinc);
			po.setPlaceCityId(destCity);
			po.setDestCounties(destCounties);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = transportDao.selTtransportInfo(po,curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运输方式维护查询失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增运输信息初始化
	 */
	public void transportInfoAddInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//承运商
			List logisticsList=logiDao.selLogistics();
			//运输方式与计价
			List modeList=transValuationDao.selTvRelation();
            act.setOutData("logisticsList", logisticsList);
            act.setOutData("modeList", modeList);
            
			act.setForword(transporInfoAddUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运输方式维护新增初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 添加运输信息
	 */
	public void transportInfoAdd(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String logi=act.getRequest().getParamValue("logiCode");//承运商
			String tvCode=act.getRequest().getParamValue("tvCode");//运输与计价方式
			String price=act.getRequest().getParamValue("price");//价格
			String placeProvinc=act.getRequest().getParamValue("PROVINCE_ID");//出发地 省
			String placeCity=act.getRequest().getParamValue("CITY_ID");//出发地 市
			String placeCounties=act.getRequest().getParamValue("COUNTIES");//出发地 区
			String destProvinc=act.getRequest().getParamValue("PROVINCE_ID1");//目的地 省
			String destCity=act.getRequest().getParamValue("CITY_ID1");//目的地 市
			String destCounties=act.getRequest().getParamValue("COUNTIES1");//目的地 区
			String status=act.getRequest().getParamValue("isStatus");//状态
			//检查出发地、目的地、承运商、运输方式、计价方式是否唯一
			TtTransportinfoPO po=new TtTransportinfoPO();
			po.setCarrier(logi);
			po.setTvId(tvCode);
			po.setPrice(Double.parseDouble(price));
			po.setPlaceProvinceId(placeProvinc);
			po.setPlaceCityId(placeCity);
			po.setPlaceCounties(placeCounties);
			po.setDestProvinceId(destProvinc);
			po.setDestCityId(destCity);
			po.setDestCounties(destCounties);
			po.setStatus(status);
			List list=transportDao.selTransportFlag(po,1);
			if(list.size()==0){
				po.setPkId(DaoFactory.getPkId()+"");
				po.setCreateDate(new Date());
				po.setCreateBy(logonUser.getUserId());
				//添加
				transportDao.transportInfoAdd(po);
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			act.setForword(transporInfoUpdUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运输方式维护新增失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 修改运输信息初始化
	 */
	public void transportInfoUpdInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String id=act.getRequest().getParamValue("Id");//id
			Map<String, Object>  transInfoMap=transportDao.selTransportById(id);
			//承运商
			List logisticsList=logiDao.selLogistics();
			//运输方式与计价
			List modeList=transValuationDao.selTvRelation();
			
            act.setOutData("logisticsList", logisticsList);
            act.setOutData("modeList", modeList);
            act.setOutData("transInfoMap", transInfoMap);
            
			act.setForword(transporInfoUpdUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运输方式维护修改初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改运输信息
	 */
	public void transportInfoUpd(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String id=act.getRequest().getParamValue("Id");//id 主键
			String logi=act.getRequest().getParamValue("logiCode");//承运商
			String tvCode=act.getRequest().getParamValue("tvCode");//运输与计价方式
			String price=act.getRequest().getParamValue("price");//价格
			String placeProvinc=act.getRequest().getParamValue("PROVINCE_ID");//出发地 省
			String placeCity=act.getRequest().getParamValue("CITY_ID");//出发地 市
			String placeCounties=act.getRequest().getParamValue("COUNTIES");//出发地 区
			String destProvinc=act.getRequest().getParamValue("PROVINCE_ID1");//目的地 省
			String destCity=act.getRequest().getParamValue("CITY_ID1");//目的地 市
			String destCounties=act.getRequest().getParamValue("COUNTIES1");//目的地 区
			String status=act.getRequest().getParamValue("isStatus");//状态
			//检查出发地、目的地、承运商、运输方式、计价方式是否唯一
			TtTransportinfoPO po=new TtTransportinfoPO();
			po.setPkId(id);
			po.setCarrier(logi);
			po.setTvId(tvCode);
			po.setPrice(Double.parseDouble(price));
			po.setPlaceProvinceId(placeProvinc);
			po.setPlaceCityId(placeCity);
			po.setPlaceCounties(placeCounties);
			po.setDestProvinceId(destProvinc);
			po.setDestCityId(destCity);
			po.setDestCounties(destCounties);
			po.setStatus(status);
			List list=transportDao.selTransportFlag(po,2);
			if(list.size()==0){
				po.setUpdateDate(new Date());
				po.setUpdateBy(logonUser.getUserId());
				//修改
				int result=transportDao.transportInfoUpdate(po);
				act.setOutData("returnValue", 1);
			
			}else{
				act.setOutData("returnValue", 2);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运输方式维护修改失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}




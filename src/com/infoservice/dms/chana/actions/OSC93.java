package com.infoservice.dms.chana.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;

import com.infodms.dms.actions.repairOrder.WrRuleUtil;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsActivityAgePO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infoservice.de.DEException;
import com.infoservice.de.DEMessage;
import com.infoservice.de.convertor.f2.XmlConvertor4YiQiP01;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.dao.DeWarrantyQueryDao;
import com.infoservice.dms.chana.service.DeUtility;
import com.infoservice.dms.chana.vo.WarrantyPartVO;
import com.infoservice.dms.chana.vo.WarrantyQueryResultVO;
import com.infoservice.dms.chana.vo.WarrantyQueryVO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

/**
 * @Title: DeWarrantyQuery.java
 *
 * @Description:同步查询配件三包期
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-29
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class OSC93 extends AbstractReceiveAction {
	private static Logger logger = Logger.getLogger(OSC93.class);
	private DeWarrantyQueryDao dao = DeWarrantyQueryDao.getInstance();
	//进厂里程数
	private Double inMileage;
	
	/*	public static void main(String[] args) {
	 ContextUtil.loadConf();
	 OSC93 o = new OSC93();
	 try {
		 File file = new File("D:/20110930112825937000001601.dat");
		 InputStream is = new FileInputStream(file);
		 byte[] b = new byte[is.available()];
		 is.read(b, 0, b.length);
		 XmlConvertor4YiQiP01 xml = new XmlConvertor4YiQiP01();
		 DEMessage msg = xml.convert(b);
		 System.out.println(msg.getAppName());
		 Map<String, Serializable> bodys = msg.getBody();
		 is.close();
		 o.handleExecutor(msg);
	 	 }catch (Exception e) {
	        e.printStackTrace();
	     }
	}*/
	
	@Override
	protected DEMessage handleExecutor(DEMessage msg) {
		logger.info("====同步查询配件三包期开始====");
		long begin = System.currentTimeMillis();
		WrRuleUtil util = new WrRuleUtil();
		try{
			Map<String, Serializable> bodys = msg.getBody();
			for (Entry<String, Serializable> entry : bodys.entrySet()) {
				logger.info("====事务开启====");
				POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
				WarrantyQueryVO vo = new WarrantyQueryVO();
				WarrantyQueryVO tempVo = new WarrantyQueryVO();
				vo = (WarrantyQueryVO) entry.getValue();
				tempVo = vo;
				tempVo.setEntityCode(msg.getSource());
				String tempstr = vo.getVin();
				vo.setVin(getDmsValue(tempVo, "vin"));//取出vin码 重置
				//vo.setVin("LS5A2ADE2AA503032");
				//vo.setInMileage(0.0);
				if (null == vo.getInMileage()) {
					POContext.endTxn(false);
					return wrapperMsg(new WarrantyQueryResultVO(), "同步查询配件三包期失败," + vo.getVin() + "没有进厂里程数");
				}
				inMileage = vo.getInMileage();
				LinkedList<WarrantyPartVO> partList = vo.getPartVoList();
				//LinkedList<WarrantyPartVO> partList = new LinkedList<WarrantyPartVO>();
				//WarrantyPartVO vo1 = new WarrantyPartVO();
				//vo1.setPartNo("QXJ011");
				//vo1.setPartName("门锁销总成");
				//partList.add(vo1);
				//vo1 = new WarrantyPartVO();
				//vo1.setPartNo("B301097-0800");
				//vo1.setPartName("侧出风口总成");
				//partList.add(vo1);
				//modify by tanv 2013-01-17 对无配件的单子，也要进行保养查询
/*				if (null == partList || partList.size() == 0) {
					POContext.endTxn(false);
					return wrapperMsg(new WarrantyQueryResultVO(), "同步查询配件三包期失败," + vo.getVin() + "不包含任何配件");
				}*/
				//modify by tanv 2013-01-17 对无配件的单子，也要进行保养查询
				//查询 PURCHASED_DATE,MILEAGE,MODEL_ID,PROVINCE_ID,OEM_COMPANY_ID
				Map<String, Object> map = dao.getVinInfo(vo);//获取由VIN查询的相关条件
				if (null == map ) {
					POContext.endTxn(false);
					return wrapperMsg(new WarrantyQueryResultVO(), "没有在车辆表里找到" + vo.getVin());
				}
				if (null == map.get("PURCHASED_DATE")) {
					POContext.endTxn(false);
					return wrapperMsg(new WarrantyQueryResultVO(), vo.getVin() + "还没有卖出");
				}
				int freeTimes = this.getFreeTimes(vo.getVin());//车辆保养次数
				int isValid = util.isProtected(freeTimes, String.valueOf(inMileage), map.get("PURCHASED_DATE").toString());//整车是否在三包期内
				//如果整车未脱保，进行配件三包信息验证
				//modify by tanv 2013-01-17 对无配件的单子，也要进行保养查询
				if(isValid==Constant.STATUS_ENABLE && null != partList && partList.size()>0){
					LinkedList<WarrantyPartVO> newList = new LinkedList<WarrantyPartVO>();
					for (int i = 0; i < partList.size(); i++) {
						WarrantyPartVO old = new WarrantyPartVO();
						old = (WarrantyPartVO) partList.get(i);
						//查询配件,是否在三包期内,三包期开始时间结束时间
						//wp = assembleWarrantyPartVO(map, wp);
						WarrantyPartVO wp = null;
						wp = util.wrRuleCompute(String.valueOf(inMileage), map.get("PURCHASED_DATE").toString(),
									vo.getVin(), old.getPartNo());
						if(wp.getIsInWarranty()==null){
							wp.setErrorMsg("该配件没有加入三包期");
						}
						//modify by tanv 取三包规则信息 start
						List<TtAsWrRuleListPO> ls = util.getWrRule(vo.getVin(), old.getPartNo());
						TtAsWrRuleListPO rule = new TtAsWrRuleListPO();
						if(ls!=null && ls.size()>0){
							rule = ls.get(0);
						}
						wp.setPartName(old.getPartName());
						String melieage = rule.getClaimMelieage()==null?"0":String.valueOf(rule.getClaimMelieage());
						wp.setPartNo("("+old.getPartNo()+")("+rule.getClaimMonth()+","+melieage.split("\\.")[0]+")");
						//modify by tanv 取三包规则信息 end
						newList.add(wp);
					}
					vo.setPartVoList(newList);//把list放回WarrantyQueryVO	
				}else if(null == partList || partList.size()==0){
					WarrantyPartVO wp = new WarrantyPartVO();
					LinkedList<WarrantyPartVO> newList = new LinkedList<WarrantyPartVO>();
					newList.add(wp);
					vo.setPartVoList(newList);//把list放回WarrantyQueryVO
				}
				WarrantyQueryResultVO resultVO = new WarrantyQueryResultVO();
				resultVO.setInMileage(vo.getInMileage());
				resultVO.setFreeTimes(freeTimes);
				//modify by tanv 加入三包规则代码显示
				resultVO.setGameName(this.getGameName(vo.getVin()));
				resultVO.setIsValid(isValid);
				resultVO.setPartVoList(DEUtil.transType(vo.getPartVoList()));
				//modify by tanv 2012-12-24 添加服务活动信息 start
				tempVo.setVin(tempstr);//还原下端上报的vin字符串
				boolean updateFlag = checkUpdateFlag(tempVo);//判断下端是否升级
				//updateFlag=true;
				String activityIds = "";
				if(updateFlag==true){
					activityIds = getActivityIds(tempVo);
					String content = fabricateMsg(resultVO,vo.getPartVoList(),activityIds);//拼装下发信息
					resultVO.setVin(getDmsValue(vo, "vin")+content);
				}else{
					resultVO.setVin(vo.getVin());
				}
				//modify by tanv 2012-12-24 添加服务活动信息 end
				DEMessage rmsg = wrapperMsg(resultVO, null);
				logger.info("====事务结束====");
				POContext.endTxn(true);
				long end = System.currentTimeMillis();
				logger.info("====同步查询配件三包期结束====");
				logger.info("任务耗时:"+(end-begin));
				return rmsg;
			}
		} catch(Exception e) {
			POContext.endTxn(false);
			logger.error("同步查询配件三包期错误", e);
			throw new RpcException(e);
		}finally {
			POContext.cleanTxn();
		}
		return null;
	}
	
	private String fabricateMsg(WarrantyQueryResultVO r, LinkedList<WarrantyPartVO> vo,String activtyIds) {
		int tempInt = r.getIsValid();
		String str1="&该车保养次数为"+r.getFreeTimes()+"次，";
        if (tempInt == Constant.STATUS_ENABLE)
        	str1=str1+"已按规定进行保养\\n";
          else
        	str1=str1+"未按规定进行保养\\n";
		String str2="三包策略名称："+r.getGameName()+"\\n";
		String str3="三包内配件：";
		String str4="三包外配件：";
		String str5="出错配件信息：";
		String str6="可进行的服务活动："+activtyIds;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for(int i=0;i<vo.size();i++){
			WarrantyPartVO p = vo.get(i);
			if(p.getIsInWarranty()!=null && (!"".equals(p.getIsInWarranty()))){
				if(10041001==p.getIsInWarranty()){
					str3=str3+p.getPartName()+p.getPartNo()+",";
				}else if(10041002==p.getIsInWarranty()){
					StringBuilder tempStr = new StringBuilder();
					tempStr.append(p.getWarrantyBeginDate()==null?"":df.format(p.getWarrantyBeginDate())+"至");
					tempStr.append(p.getWarrantyBeginDate()==null?"":df.format(p.getWarrantyEndDate()));
					str4=str4+p.getPartName()+p.getPartNo()+"("+tempStr.toString()+")"+",";
				}
			}
			if(null!=p.getErrorMsg()&&(!"".equals(p.getErrorMsg()))){
				str5=str5+p.getPartName()+p.getPartNo()+" "+p.getErrorMsg()+",";
			}
		}
		if(str3.endsWith(",")){
			str3 = str3.substring(0,str3.length()-1);
		}
		if(str4.endsWith(",")){
			str4 = str4.substring(0,str4.length()-1);
		}
		if(str5.endsWith(",")){
			str5 = str5.substring(0,str5.length()-1);
		}
		str3 = str3+"\\n";
		str4 = str4+"\\n";
		str5 = str5+"\\n";
		return str1+str2+str3+str4+str5+str6;
	}

	private String formatStr(String str) {
		int ta = 0;
		int tb = 0;
		String sa = "";
		for(int i=0;i<str.length();i++){
			if(i%50==0 && i>0){
				tb=i;
				sa=sa+str.substring(ta,tb)+"\\n";
				ta=tb;
			}
		}
		sa=sa+str.substring(tb);
		return sa;
	}

	private int getMonthCount(WarrantyPartVO wp) {
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String d1 = df.format(wp.getWarrantyBeginDate());
		String d2 = df.format(wp.getWarrantyEndDate());
		int month  = Utility.compareDate(d1,d2,1); //获取相差月数
		int day  = Utility.compareDate(d1,d2,2);//获取相差天数
		if (day >= 1) {
			month = month+1;
		}
		return month;
	}

	/**
	 * @param vo 
	 * 
	* @Description: TODO(厂端服务活动id) 
	* @throws
	 */	
	private String getActivityIds(WarrantyQueryVO vo) {
		List params = new LinkedList();
		params.add(getDmsValue(vo,"vin"));
		String dmsCode = vo.getEntityCode();
		String dealerCodes = getDealerIds(dmsCode).get(0);
		params.add(dealerCodes);
		params.add(String.valueOf(vo.getInMileage()));		
		Object ss = dao.callFunction("f_get_activity_id", java.sql.Types.VARCHAR,params );
		String aids=ss==null?"":ss.toString();
		String str="";
		Date currentDate = new Date();
		if(!"".equals(aids)){
			String[] ids = aids.split(",");
			for(int i=0;i<ids.length;i++){
				TtAsActivityPO apo = new TtAsActivityPO();
				TtAsActivityPO apoTemp = new TtAsActivityPO();
				apo.setActivityId(Long.valueOf(ids[i]));
				List<PO> list=dao.select(apo);
				if(list!=null && list.size()>0){
					apoTemp=(TtAsActivityPO) list.get(0);
					if(apoTemp.getEnddate() != null && apoTemp.getEnddate().getTime()>currentDate.getTime()){
						str=str+apoTemp.getActivityCode()+"("+apoTemp.getActivityName()+"),";
					}
				}
			}
		}
		if(str.endsWith(",")){
			str = str.substring(0,str.length()-1);
		}
		return str;
	}

	private List<String> getDealerIds(String dmsCode) {
		List<Object> inParameter = new ArrayList<Object>();// 输入参数
		List outParameter = new ArrayList();// 输出参数
		List dealers = new ArrayList();// 返回值
		inParameter.add(dmsCode);
		outParameter.add(Types.VARCHAR);
		outParameter = dao.callProcedure("P_C_DE_GETDEALERIDS", inParameter, outParameter);
		String rs = outParameter.get(0)==null?"-1":outParameter.get(0).toString();
		String[] ds = rs.split(",");
		for(String s:ds){
			dealers.add(s);
		}
		return dealers;
	}

	/**
	 * 
	* @Description: TODO(获取下端是否升级的标识)  tanv
	* @throws
	 */	
	private boolean checkUpdateFlag(WarrantyQueryVO vo) {
		String updateFlag = getDmsValue(vo,"version");
		if(null!=updateFlag && (!"".equals(updateFlag))){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 
	* @Description: TODO( 拆分字符串存入map中 )  tanv
	* @throws
	 */
	private String getDmsValue(WarrantyQueryVO vo, String key) {
		String[] temp = vo.getVin().split("&");
		Map dmsMap = new HashMap<String,Object>();
		if(temp.length>1){
			for(int i = 0;i<temp.length;i++){
				if(i==0){
					dmsMap.put("vin", temp[0]);
				}else{
					String[] str = temp[i].split("=");
					dmsMap.put(str[0].toString(), str[1].toString());
				}
			}
		}else{
			dmsMap.put("vin",vo.getVin());
		}
		String value = dmsMap.get(key)==null?"":dmsMap.get(key).toString();
		return value;
	}

	/**
	 * 
	* @Title: getFreeTimes 
	* @Description: TODO(获得车辆保修次数) 
	* @param @param vin 车辆VIN
	* @param @return
	* @return Integer  返回类型 (车辆保养次数)
	* @throws
	 */
	public Integer getFreeTimes(String vin)throws Exception{
		TmVehiclePO tvp = new TmVehiclePO();
		int freeTimes = 0;
		if (Utility.testString(vin)){
			tvp.setVin(vin);
			CommonUtils.jugeVinNull(vin);
			List<PO> ls = dao.select(tvp);
			if (ls!=null&&ls.size()>0){
				tvp = (TmVehiclePO)ls.get(0);
				freeTimes = tvp.getFreeTimes();
			}
		}
		return freeTimes;
	}
	
	/**
	 * 
	* @Title: getGameName 
	* @Description: TODO(获得车辆三包策略名称) 
	* @param @param vin 车辆VIN
	* @param @return
	* @return String  返回类型 (车辆三包策略名称)
	* @throws
	 */
	public String getGameName(String vin)throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select GAME_NAME,GAME_CODE from tm_vehicle v left join TT_AS_WR_GAME a  on v.claim_tactics_id=a.id\n");
		sql.append("where 1=1\n" );
		sql.append("and v.VIN = '"+vin+"'\n" );
		Map<String,Object> map = dao.pageQueryMap(sql.toString(), null, dao.getFunName());
		return (String)map.get("GAME_NAME")+"("+(String)map.get("GAME_CODE")+")";
	}
	
	//计算月份
	public static Date MonthAdd(String date1, int month) throws ParseException{
		Date date = DateTimeUtil.stringToDate(date1);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, month);
		return c.getTime();
	}
	
	/**
	 * 
	* @Title: milesAdd 
	* @Description: TODO(根据进厂行驶公里数减去出厂公里数) 
	* @param @param aMile 实际行驶公里数
	* @param @param cMile  出厂公里数
	* @param @return startMile + endMile
	* @return Date    返回类型 
	* @throws
	 */
	public static Double milesCut(Double aMile, Double cMile) {
		return aMile - cMile;
	}
	
	/**
	 * 
	* @Title: getPartGurn 
	* @Description: TODO(获取配件三包期信息) 
	* @param @param map vin信息,包含购车时间,行驶公里数
	* @param @param wp   上端传的配件信息
	* @return void    返回类型 
	 * @throws Exception 
	* @throws
	 */
	private WarrantyPartVO assembleWarrantyPartVO(Map<String, Object> map, WarrantyPartVO wp) throws Exception {
		Map<String, Object> partMap = dao.getPartGurn(map, wp);
		if (null != partMap) {
			//在业务规则里查询
			return getPartGurn(partMap, map, wp);
		} else {
			//在系统规则里查询
			partMap = dao.getPartSysGurn(map, wp);
			if (null == partMap) {
				return DeUtility.wrapperMsg(wp, wp.getPartNo() + "没有维护三包期规则");
			}
			return getPartGurn(partMap, map, wp);
		}
	}
	
	private WarrantyPartVO getPartGurn(Map<String, Object> partMap, Map<String, Object> map, WarrantyPartVO wp) throws Exception {//业务规则里有
		
		int month = Integer.parseInt(String.valueOf(partMap.get("GURN_MONTH")));//三包期内最大月份
		Date maxMonth = MonthAdd(String.valueOf(map.get("PURCHASED_DATE")), month);//车购买时间加上三包期最大月份,算出三包期结束时间
		
		Double mile = Double.parseDouble(String.valueOf(partMap.get("GURN_MILE")));//三包期内最大公里数
		Double startMile = Double.parseDouble(String.valueOf(map.get("MILEAGE")));//初始里程
		Double aMile = milesCut(inMileage, startMile);//实际行驶公里数
		if (aMile > mile) {
			//如果行驶里程数大于三包期最大里程数,过三包期
			wp.setIsInWarranty(Constant.IF_TYPE_NO);
		} else if (maxMonth.compareTo(new Date()) < 0) {//当前日期小于三包期结束时间
			wp.setIsInWarranty(Constant.IF_TYPE_NO);//不在三包期
		} else {
			wp.setIsInWarranty(Constant.IF_TYPE_YES);//在三包期内
		}
		Date startDate = DateTimeUtil.stringToDate(String.valueOf(map.get("PURCHASED_DATE")));
		wp.setWarrantyBeginDate(startDate);//三包开始日期
		wp.setWarrantyEndDate(maxMonth);//三包结束日期
		// 超出天数  三包期结束日期-开始日期
		wp.setOverDay(overDays(startDate.getTime(), maxMonth.getTime()));
		
		wp.setWarrantyBeginMileage(startMile);
		wp.setWarrantyEndMileage(mile);
		//超出里程  三包期最大公里数-出厂公里数
		wp.setOverMileage(mile - aMile);
		return wp;
	}
		
	private DEMessage wrapperMsg(WarrantyQueryResultVO vo, String msg) {
		if (null != msg) {
			//出错的时候
			vo = DeUtility.wrapperMsg(vo, msg);
		}
		HashMap<String, Serializable> body = new HashMap<String, Serializable>();
		body.put("body", vo);
		DeUtility de = new DeUtility();
		DEMessage rmsg = null;
		try {
			rmsg = de.assembleDEMessage("DRC93", body);
		} catch (DEException e) {
			logger.error(e, e);
		}
		return rmsg;
	}
	
	private Double overDays(long startTime, long endTime) {
		long time = (endTime - startTime) / (1000*60*60*24);
		return Double.parseDouble(String.valueOf(time));
	}
}

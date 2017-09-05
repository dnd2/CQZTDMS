package com.infoservice.dms.chana.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.dao.partinfo.PartinfoDao;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.po.TSellpartDefinePO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

/**
 * 
* @ClassName: PartInfoTask 
* @Description: TODO(接收配件主信息) 
* @author liuqiang 
* @date Sep 29, 2010 3:06:48 PM 
*
 */
public class PartInfoTask extends AbstractSendTask {
	private PartinfoDao dao = new PartinfoDao();
	private Long oemCompanyId;
	private final Logger LOG = Logger.getLogger(PartInfoTask.class);
	public static void main(String[] args) throws Exception {
		ContextUtil.loadConf();
		POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);		
		PartInfoTask pt = new PartInfoTask();
		pt.handleExecute();
	}
		
	
	@Override
	protected String handleExecute() throws Exception {
		LOG.info("配件主数据接收开始");
		List<Long> ids = new ArrayList<Long>();
		List<TSellpartDefinePO> parts = queryPartInfo(); //查询配件接口表
		List<TmPtPartBasePO> pos = assemblePO(parts);      //转化PO
		insertPart(pos, ids);                                   //将配件基础信息插入到表中
		if (ids.size() > 0) {
			delPart(ids);                                    //配件插入完毕,将接口表的数据删除
			//upPart(ids);//将处理成功的标示写入接口表
		}
		LOG.info("配件主数据接收结束");
		return null;
	}
	/**
	 * 
	* @Title: queryPartInfo 
	* @Description: TODO(查询临时表的数据) 
	* @return List<TSellpartDefinePO>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private List<TSellpartDefinePO> queryPartInfo() {
		//每次发500条数据
		String sql = "SELECT * FROM T_SELLPART_DEFINE where rownum <= 500 ";
		List<TSellpartDefinePO> parts = dao.select(TSellpartDefinePO.class, sql, null);
//		for (TSellpartDefinePO part : parts) {
//			ids.add(part.getSellpartId());
//		}
		return parts;
	}
	
	/**
	 * 2013-05-02 艾春添加修改注释
	 * 该功能仅用于长安配件系统传递到售后系统的接口，不适用于其他项目
	 * @param parts
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<TmPtPartBasePO> assemblePO(List<TSellpartDefinePO> parts) {
		List<TmPtPartBasePO> pos = new ArrayList<TmPtPartBasePO>();
		for (TSellpartDefinePO part : parts) {
			TmPtPartBasePO po = new TmPtPartBasePO();
			po.setPartId(part.getSellpartId());
			po.setErpdCode(part.getSellpartCode());
			po.setPartCode(part.getSellpartOldcode());
			po.setPartName(part.getSellpartName());
			po.setUnit(part.getUnit());
//			po.setPartType(part.getSellpartType());
			po.setMiniPack(part.getMinPack().intValue());
			po.setReplacePartId(part.getRepalcedSellpartId());
//			po.setChangeCode(part.getIsReplaced());
			po.setStockPrice(part.getBuyPrice().floatValue());
			po.setSalePrice(part.getSalePrice().floatValue());
			po.setCustomerPrice(part.getSalePrice().floatValue());
			po.setClaimPrice(part.getSellpartPrice().floatValue());
			po.setStopFlag(part.getIsForbidden().intValue());
			po.setRemark(part.getNote());
			po.setIsDel(0);
			po.setIsArc(0);
			po.setIfStatus(DEConstant.IF_STATUS_0);
			po.setIsReturn(Constant.IS_NEED_RETURN);
//			po.setCarType(part.getCarType());
			TmCompanyPO company = new TmCompanyPO();
			company.setCompanyType(Integer.parseInt(Constant.COMPANY_TYPE_SGM));
			if (null == oemCompanyId) {
				List<TmCompanyPO> poss = dao.select(company);//取车厂公司id
				if (null == poss || poss.size() == 0) {
					throw new RpcException("没有找到车厂公司");
				}
				oemCompanyId = poss.get(0).getCompanyId();
			}
			 //取轿车公司ID
			po.setOemCompanyId(oemCompanyId);
			pos.add(po);
		}
		return pos;
	}
	
	@SuppressWarnings("unchecked")
	public void insertPart(List<TmPtPartBasePO> pos, List<Long> ids) {
		for (TmPtPartBasePO po : pos) {
			try {
				TmPtPartBasePO tpo = new TmPtPartBasePO();
				tpo.setPartCode(po.getPartCode()); //改为ID对应 YH 2011.9.27
				List<TmPtPartBasePO> tmps = dao.select(tpo);
				if (null != tmps && tmps.size() > 0) {
					po.setUpdateDate(new Date());
					po.setUpdateBy(123L);
					po.setIsNewPart(tpo.getIsNewPart()); //更新时，不更新是否新件和是否回运 YH2011.10.9
					po.setIsReturn(tpo.getIsReturn());
					dao.update(tpo, po);
					ids.add(po.getPartId());
				} else {
					try {
						po.setCreateDate(new Date());
						po.setCreateBy(123L);
						po.setIsNewPart(1); //新件状态 YH 2011.5.31
						dao.insert(po);
						List<Map<String, Object>> maps = dao.queryWrSysRule();//查询业务规则ID
						if (null == maps || maps.size() == 0) {
							throw new IllegalArgumentException("没有查到三包系统规则");
						}
						for (Map<String, Object> map : maps) {//插入三包规则表
							TtAsWrRuleListPO rule = new TtAsWrRuleListPO();
							rule.setId(Long.parseLong(SequenceManager.getSequence("")));
							rule.setRuleId(Long.parseLong(String.valueOf(map.get("ID"))));
							if("20061020".equals(String.valueOf(map.get("RULE_CODE")))){ // 零三包规则 YH.20110120
								rule.setClaimMonth(0);
								rule.setClaimMelieage(0D);
							}
							if("20060101".equals(String.valueOf(map.get("RULE_CODE")))){ // 通用三包规则 YH.20110120
								rule.setClaimMonth(0);
								rule.setClaimMelieage(-1D);
							}
							rule.setPartCode(po.getPartCode());
							rule.setPartName(po.getPartName());
							rule.setCreateDate(new Date());
							rule.setCreateBy(123L);
							dao.insert(rule);
						}
						ids.add(po.getPartId());
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
				}
			} catch (Exception e) {
				LOG.error("配件接收失败  " + po.getPartCode(), e);
			
				e.printStackTrace();
			}finally{
				try{
		            POContext.endTxn(true);		
				}catch(Exception e){
				}finally{
					POContext.cleanTxn();
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void delPart(List<Long> ids) {
		String id = StringUtil.compileStr(ids);
		dao.delPartInfoByIds(id);
//		for (Long id : ids) {
//			try {
//				TSellpartDefinePO po = new TSellpartDefinePO();
//				po.setSellpartId(id);
//				dao.delete(po);
//			} catch (Exception e) {
//				LOG.error("删除配件接口表失败  id === " + id, e);
//				e.printStackTrace();
//			}
//		}
	}

	public void upPart(List<Long> ids) {
		String id = StringUtil.compileStr(ids);
		dao.upPartInfoByIds(id);

	}
}

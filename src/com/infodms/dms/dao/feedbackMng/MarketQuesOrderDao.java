package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.TtIfMarketAuditListBean;
import com.infodms.dms.bean.TtIfMarketBean;
import com.infodms.dms.bean.TtIfMarketDetailBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TtIfMarketAuditPO;
import com.infodms.dms.po.TtIfMarketPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class MarketQuesOrderDao extends BaseDao {
	public static Logger logger = Logger.getLogger(MarketQuesOrderDao.class);
	private static final MarketQuesOrderDao dao = new MarketQuesOrderDao();
    
	public static final MarketQuesOrderDao getInstance() {
		return dao;
	}
	/**
	 * Function：
	 * @param  ：	
	 * @return:	
	 * @throw：	
	 * LastUpdate：	2010-5-18
	 */
	public PageResult<TtIfMarketBean> pageQueryData(String sqlStr,List<Object> params,int curPage, int pageSize) {
		PageResult<TtIfMarketBean> pr = pageQuery(TtIfMarketBean.class,sqlStr,params, pageSize, curPage);
		return pr;
	}
	@Override
	protected TtIfMarketPO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	public void createMarketOrder(TtIfMarketPO po){
    	insert(po);
    }
	/**
	 * Function：获取市场问题处理工单详细信息
	 * @param  ：	
	 * @return:		@param sqlStr
	 * @return:		@param curPage
	 * @return:		@param pageSize
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-5-25
	 */
	public TtIfMarketDetailBean getMarketDetailInfoData(String sqlStr,int curPage, int pageSize){
    	PageResult<TtIfMarketDetailBean> pr = pageQuery(TtIfMarketDetailBean.class,sqlStr, null, pageSize, curPage);
    	if(null!=pr.getRecords()&&pr.getRecords().size()!=0)
    	return (TtIfMarketDetailBean)pr.getRecords().get(0);
    	else 
    	return null;
    }
	/**
	 * Function：获得工单的审核记录
	 * @param  ：	
	 * @return:		@return 
	 * @throw：	
	 * LastUpdate：	2010-5-25
	 */
	public List<TtIfMarketAuditListBean> getMarketOrderAuditList(String sqlStr,int curPage, int pageSize){
		PageResult<TtIfMarketAuditListBean> pr = pageQuery(TtIfMarketAuditListBean.class,sqlStr, null, pageSize, curPage);
		return pr.getRecords();
	}
	public int delMarketOrder(String[] orderIds,TtIfMarketPO delPre){
    	int retCode=0;
    	for (int i = 0;i<orderIds.length;i++) {
    		TtIfMarketPO deledObj=new TtIfMarketPO();
    		deledObj.setOrderId(orderIds[i]);
    		TtIfMarketPO delObj=new TtIfMarketPO();
    		delObj.setIsDel(delPre.getIsDel());
    		delObj.setUpdateBy(delPre.getUpdateBy());
    		delObj.setUpdateDate(delPre.getUpdateDate());
    		retCode+=update(deledObj, delObj);
    	}
    	return retCode;
    }
	public int updateSingleOrder(String orderId,TtIfMarketPO timp){
		int retCode=0;
		TtIfMarketPO  pkObj =new TtIfMarketPO();//orderId条件
		pkObj.setOrderId(orderId);
		retCode=update(pkObj, timp);
		return retCode;
	}
	public int updateMarketOrderStatus(String[] orderIds,TtIfMarketPO timp,TtIfMarketAuditPO timap){
    	int retCode=0;
    	for (int i = 0;i<orderIds.length;i++) {
    		TtIfMarketPO  pkObj =new TtIfMarketPO();//orderId条件
    		pkObj.setOrderId(orderIds[i]);
    		
    		TtIfMarketPO  vo  =new TtIfMarketPO();//内容
    		vo.setStatus(timp.getStatus());
    		vo.setUpdateBy(timp.getUpdateBy());
    		vo.setUpdateDate(timp.getUpdateDate());
    		retCode+=dao.update(pkObj, vo);
			
    		TtIfMarketAuditPO  insertObj =new TtIfMarketAuditPO();//内容
			insertObj.setId(Long.parseLong(SequenceManager.getSequence("")));//ID
			insertObj.setOrderId(orderIds[i]);//orderId
			insertObj.setAuditDate(new Date());
			insertObj.setAuditBy(timap.getAuditBy());//审核人
			insertObj.setAuditStatus(timap.getAuditStatus());
			insertObj.setAuditContent(timap.getAuditContent());
			insertObj.setOrgId(timap.getOrgId());
			dao.insert(insertObj);
    	}
    	return retCode;
    }
    /**
     * Function：通过职位编号获得职位类型
     * @param  ：	
     * @return:		@param pose_id
     * @return:		@return 
     * @throw：	
     * LastUpdate：	2010-5-24
     */
	public boolean getPoseType(long pose_id){
    	boolean retCode=false;
    	TcPosePO queryObj=new TcPosePO();
    	queryObj.setPoseId(pose_id);
    	TcPosePO data=(TcPosePO)super.select(queryObj).get(0);
    	if(data.getPoseType()==Constant.POSE_TYPE_MANAGER) 
    		retCode=true;
    	return retCode;
    }
	
	/*
	 * @param poseId
	 * @return TcPosePO
	 */
	public String getPoseCode(Long poseId){
		TcPosePO po = new TcPosePO();
		po.setPoseId(poseId);
		List<TcPosePO> lists = super.select(po);
		if(lists.size()>0) 
			return lists.get(0).getPoseCode();
		return null ;
	}
}

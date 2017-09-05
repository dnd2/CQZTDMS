package com.infoservice.dms.chana.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.infodms.dms.dao.partinfo.PartShippingDao;
import com.infodms.dms.po.TtPtOrderPO;
import com.infodms.dms.po.TtPtOrditemPO;
import com.infodms.dms.po.TtPtSalesPO;
import com.infodms.dms.po.TtPtShippingsheetPO;
import com.infodms.dms.po.TtPtShippingsheetitemPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.common.TransConEnum;
import com.infoservice.dms.chana.dao.CommonDao;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.dms.chana.po.TtPtOrderRpcPO;
import com.infoservice.dms.chana.po.TtPtOrditemRpcPO;
import com.infoservice.dms.chana.po.TtPtSalesRpcPO;
import com.infoservice.dms.chana.po.TtPtShippingsheetRpcPO;
import com.infoservice.dms.chana.po.TtPtShippingsheetitemRpcPO;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

/**
 * 
* @ClassName: PartTask 
* @Description: TODO(从配件货运单中间表取数据) 
* @author liuqiang 
* @date Sep 4, 2010 2:45:22 PM 
*
 */
public class PartTask extends AbstractSendTask {
	private PartShippingDao dao = PartShippingDao.getInstance();
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	private CommonDao commonDao = CommonDao.getInstance();
	
	
	/**
	 * 
	* @Title: insertOrder 
	* @Description: TODO(插入采购订单接口表) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private void insertOrder() {
		//接口订单主表
		TtPtOrderRpcPO order = new TtPtOrderRpcPO();
		order.setOrderId(Long.parseLong(SequenceManager.getSequence("")));
		order.setOrderNo(SequenceManager.getSequence("or"));
		order.setTopupCode("S00492");
		order.setRaiseDate(new Date());
		order.setRequireDate(new Date());
		dao.insert(order);
		//接口订单明细表
		TtPtOrditemRpcPO item = new TtPtOrditemRpcPO();
		item.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
		item.setOrderId(order.getOrderId());
		item.setPartCode("87804-4A212");
		item.setOrderCount(5);
		dao.insert(item);
		//接口销售表
		TtPtSalesRpcPO sale = new TtPtSalesRpcPO();
		sale.setOrderNo(order.getOrderNo());
		sale.setSoNo(SequenceManager.getSequence("so"));
		dao.insert(sale);
		//接口货运表
		TtPtShippingsheetRpcPO ship = new TtPtShippingsheetRpcPO();
		ship.setDoNo(SequenceManager.getSequence("do"));
		ship.setSoNo(sale.getSoNo());
		ship.setShippingCondition("自提");
		ship.setDeliveryCompany("company");
		ship.setDeliveryPdc("chana");
		ship.setIfStatus(DEConstant.IF_STATUS_0);
		ship.setRemark("主表备注");
		dao.insert(ship);
		//接口货运表明细
		TtPtShippingsheetitemRpcPO shipItem = new TtPtShippingsheetitemRpcPO();
		shipItem.setItemId(Long.parseLong(SequenceManager.getSequence("")));
		shipItem.setDoNo(ship.getDoNo());
		shipItem.setPartCode("87804-4A212");
		shipItem.setPartName("右后侧玻璃");
		shipItem.setCount(3);
		shipItem.setCartonNo("001");
		shipItem.setRemark("子表备注");
		shipItem.setTotalPrice(4d);
		shipItem.setInstructPrice(2d);
		shipItem.setPrice(3d);
		dao.insert(shipItem);
	}
	/**
	 * 
	* @Title: queryShipping 
	* @Description: TODO(查询接口货运中间表并插入业务表) 
	* @param     设定文件 
	* @return List<TtPtShippingsheetPO> 接口状态为未下发的货运中间表记录(此处未下发代表还没有从中间表插入到业务表)
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private List<TtPtShippingsheetPO> queryShipping() {
		List<TtPtShippingsheetPO> doNos = new ArrayList<TtPtShippingsheetPO>();
		String sql = "SELECT * FROM TT_PT_SHIPPINGSHEET_RPC WHERE NVL(IF_STATUS,0) = " + DEConstant.IF_STATUS_0;
		List<TtPtShippingsheetRpcPO> items = dao.select(TtPtShippingsheetRpcPO.class, sql, null);
		for (TtPtShippingsheetRpcPO po : items) {
			TtPtShippingsheetPO newpo = new TtPtShippingsheetPO();
			newpo.setDoNo(po.getDoNo());
			newpo.setSoNo(po.getSoNo());
			newpo.setShippingCondition(TransConEnum.getUTransType(po.getShippingCondition()));
			newpo.setCreateDate(new Date());
			newpo.setDeliveryCompany(po.getDeliveryCompany());
			newpo.setDeliveryPdc(po.getDeliveryPdc());
			newpo.setRemark(po.getRemark());
			dao.insert(newpo);
			doNos.add(newpo);
			//commonDao.updateComplete("TT_PT_SHIPPINGSHEET_RPC", "DO_NO", ship.getDoNo());
		}
		return doNos;
	}
	/**
	 * 
	* @Title: queryShippingItem 
	* @Description: TODO(根据货运单号查询货运明细表并插入业务表) 
	* @param @param pos
	* @param @return    设定文件 
	* @return List<TtPtShippingsheetPO>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private void queryShippingItem(List<TtPtShippingsheetPO> pos) {
		for (TtPtShippingsheetPO po : pos) {
			String sql = "SELECT * FROM TT_PT_SHIPPINGSHEETITEM_RPC WHERE DO_NO = '" + po.getDoNo() + "'";
			//查询货运单子表
			List<TtPtShippingsheetitemRpcPO> items = dao.select(TtPtShippingsheetitemRpcPO.class, sql, null);
			for (TtPtShippingsheetitemRpcPO item : items) {
				TtPtShippingsheetitemPO newpo = new TtPtShippingsheetitemPO();
				newpo.setItemId(Long.parseLong(SequenceManager.getSequence("")));
				newpo.setDoNo(item.getDoNo());
				newpo.setPartCode(item.getPartCode());
				newpo.setPartName(item.getPartName());
				newpo.setCount(item.getCount());
				newpo.setCartonNo(item.getCartonNo());
				newpo.setCreateDate(new Date());
				newpo.setRemark(item.getRemark());
				newpo.setPrice(item.getPrice());
				newpo.setInstructPrice(item.getInstructPrice());
				newpo.setTotalPrice(item.getTotalPrice());
				dao.insert(newpo);
			}
		}
	}
	/**
	 * 
	* @Title: querySales 
	* @Description: TODO(查询销售接口表并插入业务表) 
	* @param @param pos    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private List<TtPtSalesPO> querySales(List<TtPtShippingsheetPO> pos) {
		List<TtPtSalesPO> sales = new ArrayList<TtPtSalesPO>();
		for (TtPtShippingsheetPO po : pos) {
			String sql = "SELECT * FROM TT_PT_SALES_RPC WHERE SO_NO = '" + po.getSoNo() + "'";
			//查询销售单表
			List<TtPtSalesRpcPO> items = dao.select(TtPtSalesRpcPO.class, sql, null);
			for (TtPtSalesRpcPO item : items) {
				TtPtSalesPO newpo = new TtPtSalesPO();
				newpo.setSoNo(item.getSoNo());
				newpo.setOrderNo(item.getOrderNo());
				newpo.setCreateDate(new Date());
				dao.insert(newpo);
				sales.add(newpo);
			}
		}
		return sales;
	}
	
	/**
	 * 
	* @Title: queryOrder 
	* @Description: TODO(查询配件订单表并插入业务表) 
	* @param @param pos    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private List<TtPtOrderPO> queryOrder(List<TtPtSalesPO> pos) {
		List<TtPtOrderPO> orders = new ArrayList<TtPtOrderPO>();
		for (TtPtSalesPO po : pos) {
			String sql = "SELECT * FROM TT_PT_ORDER_RPC WHERE ORDER_NO = '" + po.getOrderNo() + "'";
			//查询销售单表
			List<TtPtOrderRpcPO> items = dao.select(TtPtOrderRpcPO.class, sql, null);
			for (TtPtOrderRpcPO item : items) {
				TtPtOrderPO newpo = new TtPtOrderPO();
				newpo.setOrderId(item.getOrderId());
				newpo.setOrderNo(item.getOrderNo());
				Map<String, Object> map = deCommonDao.getDealerByComCode(item.getTopupCode());
				newpo.setDealerId(Long.parseLong(map.get("DEALER_ID").toString()));
				newpo.setRaiseDate(item.getRaiseDate());
				newpo.setRequireDate(item.getRequireDate());
				newpo.setCreateDate(new Date());
				dao.insert(newpo);
				orders.add(newpo);
			}
		}
		return orders;
	}
	/**
	 * 
	* @Title: queryOrderItem 
	* @Description: TODO(查询配件采购订单明细表并插入业务表) 
	* @param @param pos    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private void queryOrderItem(List<TtPtOrderPO> pos) {
		for (TtPtOrderPO po : pos) {
			String sql = "SELECT * FROM TT_PT_ORDITEM_RPC WHERE ORDER_ID = " + po.getOrderId();
			//查询销售单表
			List<TtPtOrditemRpcPO> items = dao.select(TtPtOrditemRpcPO.class, sql, null);
			for (TtPtOrditemRpcPO item : items) {
				TtPtOrditemPO newpo = new TtPtOrditemPO();
				newpo.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
				newpo.setOrderId(item.getOrderId());
				Map<String, Object> map = deCommonDao.getPartByCode(item.getPartCode());
				newpo.setPartId(Long.parseLong(map.get("PART_ID").toString()));
				newpo.setOrderCount(item.getOrderCount());
				newpo.setCreateDate(new Date());
				dao.insert(newpo);
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		ContextUtil.loadConf();
		POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);		
		PartTask pt = new PartTask();
		pt.handleExecute();
		//pt.insertOrder();
		POContext.endTxn(true);
	}
	@Override
	protected String handleExecute() throws Exception {
		//先查询未插入业务表的货运单,插入业务表
		List<TtPtShippingsheetPO> shippings = queryShipping();
		if (shippings.size() > 0) {
			//根据货运单查询并插入业务货运单明细表
			queryShippingItem(shippings);
			//根据货运单查询并插入销售单
			List<TtPtSalesPO> sales = querySales(shippings);
			//根据销售单查询采购订单,并插入业务表
			List<TtPtOrderPO> orders = queryOrder(sales);
			//根据采购订单编号查询采购订单明细,并插入业务表
			queryOrderItem(orders);
			//将接口货运订单表的接口状态置为已完成
			for (TtPtShippingsheetPO ship : shippings) {
				commonDao.updateComplete("TT_PT_SHIPPINGSHEET_RPC", "DO_NO", ship.getDoNo());
			}
		}
		return null;
	}
	

}

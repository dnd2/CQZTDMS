package com.infoservice.dms.chana.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.po.TmPtPartSupRelationPO;
import com.infodms.dms.util.StringUtil;

import com.infoservice.dms.chana.dao.DePtDcDao;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

/**
 * 
* @ClassName: PartVenderRelationTask 
* @Description: TODO(配件供应商关系接收) 
* @author liuqiang 
* @date Nov 6, 2010 4:32:13 PM 
*
 */
public class PartVenderRelationTask extends AbstractSendTask {

	private final Logger LOG = Logger.getLogger(PartVenderTask.class);
	private DePtDcDao dao = DePtDcDao.getInstance();
	
	public static void main(String[] args) {
		ContextUtil.loadConf();
		POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);		
		PartVenderRelationTask pt = new PartVenderRelationTask();
		try {
			pt.handleExecute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		POContext.endTxn(true);
	}
	
	@Override
	protected String handleExecute() throws Exception {
		LOG.info("PartVenderRelationTask receive starting...");
		List<Map<String, Object>> maps = queryPartVenderRe();      //查询配件供应商关系接口表
		List<Long> ids = new ArrayList<Long>();
		List<TmPtPartSupRelationPO> pos = assemblePO(maps);
		if (null != pos && pos.size() > 0) {
			insertPtDc(pos, ids);                      //供应商信息插入到表中
			if (ids.size() > 0) {
				delVender(ids);                            //供应商插入完毕,将接口表的数据删除
			}
		}
		LOG.info("PartVenderRelationTask receive ending...");
		return null;
	}

	private List<Map<String, Object>> queryPartVenderRe() {
		List<Map<String, Object>> maps = dao.queryPartVenderRelation();
		return maps;
	}
	
	private List<TmPtPartSupRelationPO> assemblePO(List<Map<String, Object>> maps) {
		List<TmPtPartSupRelationPO> pos = new ArrayList<TmPtPartSupRelationPO>();
		for (Map<String, Object> map : maps) {
			TmPtPartSupRelationPO po = new TmPtPartSupRelationPO();
			po.setRelationId(Long.parseLong(String.valueOf(map.get("SV_ID"))));
			po.setOrderId(Long.parseLong(String.valueOf(map.get("SELLPART_ID"))));
			po.setSupplierId(Long.parseLong(String.valueOf(map.get("VENDER_ID"))));
			pos.add(po);
		}
		return pos;
	}
	
	@SuppressWarnings("unchecked")
	private void insertPtDc(List<TmPtPartSupRelationPO> pos, List<Long> ids) {
		for (TmPtPartSupRelationPO po : pos) {
			try {
				TmPtPartSupRelationPO tpo = new TmPtPartSupRelationPO();
				tpo.setOrderId(po.getOrderId());
				tpo.setSupplierId(po.getSupplierId());
				List<TmPtPartSupRelationPO> tmps = dao.select(tpo);
				if (null != tmps && tmps.size() > 0) {
					po.setUpdateDate(new Date());
					dao.update(tpo, po);
					ids.add(po.getRelationId());
				} else {
					po.setCreateDate(new Date());
					dao.insert(po);
					ids.add(po.getRelationId());
				}
			} catch (Exception e) {
				LOG.error("PartVenderRelationTask receive fail...  " + po.getRelationId(), e);
				e.printStackTrace();
			}
		}
	}
	
	private void delVender(List<Long> ids) {
		String id = StringUtil.compileStr(ids);
		dao.delPartVenderReByIds(id);
	}
}

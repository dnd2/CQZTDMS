package com.infoservice.dms.chana.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.dms.chana.dao.DePtDcDao;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;

/**
 * 
* @ClassName: PartVenderTask 
* @Description: TODO(配件供应商接收计划任务) 
* @author liuqiang 
* @date Nov 6, 2010 3:18:47 PM 
*
 */
public class PartVenderTask extends AbstractSendTask {
	private final Logger LOG = Logger.getLogger(PartVenderTask.class);
	private DePtDcDao dao = DePtDcDao.getInstance();
	
	public static void main(String[] args) {
		ContextUtil.loadConf();
		POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);		
		PartVenderTask pt = new PartVenderTask();
		try {
			pt.handleExecute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		POContext.endTxn(true);
	}
	
	@Override
	protected synchronized String handleExecute() throws Exception {
		System.out.println("======================================="+"配件供应商接口开始");
		LOG.info("PartVenderTask receive starting...");
		List<String> dcCodes = new ArrayList<String>();//存储更新或插入成功的供应商code
		List<TmPtSupplierPO> pos = queryVender();            //查询供应商接口表
		if (null != pos && pos.size() > 0) {
			insertPtDc(pos, dcCodes);                      //供应商信息插入到表中
			if (dcCodes.size() > 0) {
				delVender(dcCodes);                            //供应商插入完毕,将接口表的数据删除
			}
		}
		LOG.info("PartVenderTask receive ending...");
		System.out.println("======================================="+"配件供应商接口结束");
		return null;
	}
	
	private List<TmPtSupplierPO> queryVender() {
		List<TmPtSupplierPO> pos = dao.queryVender();
		return pos;
	}
	
	@SuppressWarnings("unchecked")
	private void insertPtDc(List<TmPtSupplierPO> pos, List<String> dcCodes) {
		for (TmPtSupplierPO po : pos) {
			try {
				TmPtSupplierPO tpo = new TmPtSupplierPO();
				tpo.setSupplierCode(po.getSupplierCode());
				List<TmPtSupplierPO> tmps = dao.select(tpo);
				if (null != tmps && tmps.size() > 0) {
					po.setUpdateDate(new Date());
					dao.update(tpo, po);
					dcCodes.add(po.getSupplierCode());
				} else {
					po.setCreateDate(new Date());
					dao.insert(po);
					dcCodes.add(po.getSupplierCode());
				}
			} catch (Exception e) {
				LOG.error("PartVenderTask receive fail...  " + po.getSupplierCode(), e);
				e.printStackTrace();
			}
		}
	}
	
	private void delVender(List<String> dcCodes) {
		if (dcCodes.size() > 0) {
			StringBuilder str = new StringBuilder();
			for (String dcCode : dcCodes) {
				str.append(dcCode);
				str.append(",");
			}
			str.deleteCharAt(str.length() - 1);
			String codes = StringUtil.compileStr(str.toString());
			dao.delVenderByCodes(codes);
		}	
	}

}

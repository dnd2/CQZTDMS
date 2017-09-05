package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.dms.chana.vo.BaseVO;
public abstract class AbstractIFDao extends BaseDao<PO> {
	private static final Logger logger = Logger.getLogger(AbstractIFDao.class);
	protected Date downTimestamp = new Date();
	@SuppressWarnings("hiding")
	public <BaseVO> List<BaseVO> select(String sql, List<Object> params, final int flag) {
		logger.info(sql);
		return (List<BaseVO>) factory.select(sql, params, new DAOCallback<BaseVO>() {
			@SuppressWarnings("unchecked")
			public BaseVO wrapper(ResultSet rs, int idx) {
				return (BaseVO) wrapperVO(rs, idx, flag);
			}
		});
	}
	
	protected BaseVO wrapperVO(ResultSet rs, int idx, int flag) {
		return null;
	}
	
	@SuppressWarnings("hiding")
	public <BaseVO> List<BaseVO> select(String sql, List<Object> params) {
		logger.info(sql);
		return (List<BaseVO>) factory.select(sql, params, new DAOCallback<BaseVO>() {
			@SuppressWarnings("unchecked")
			public BaseVO wrapper(ResultSet rs, int idx) {
				return (BaseVO) wrapperVO(rs, idx);
			}
		});
	}
	
	protected BaseVO wrapperVO(ResultSet rs, int idx) {
		return null;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.infodms.dms.dao.parts.salesManager.carFactorySalesManager.boOrderMananger;

import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class PartBoQueryDao extends BaseDao {
    public Logger logger = Logger.getLogger(PartBoQueryDao.class);
    private static final PartBoQueryDao dao = new PartBoQueryDao();

    private PartBoQueryDao() {
    }

    public static final PartBoQueryDao getInstance() {
        return dao;
    }

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }


}

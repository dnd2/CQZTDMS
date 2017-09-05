package com.infodms.dms.dao.scsj;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.infox.util.StringUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ScsjManagerDao extends BaseDao<PO> {
	public static final Logger logger = Logger.getLogger(ScsjManagerDao.class);
	private static final ScsjManagerDao dao = new ScsjManagerDao();
	
	public static final ScsjManagerDao getInstance() {
		return dao;
	}

    /**
     * 
     * @param dealerId 
     * @param endTime 
     * @param beginTime 
     * @param request 
     * @Title      : scsj预约信息
     * @param      : @param sqlStr
     * @param      : @param pageSize
     * @param      : @param curPage
     * @param      : @return      
     * @return     :    
     * @throws     :
     * LastDate    : 2014-5-22
     */
    public PageResult<Map<String, Object>> queryScsjInfo(String fromInfo,String dealerId, String beginTime, String endTime, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		String isChanged = CommonUtils.checkNull(request.getParamValue("isChanged"));
        if(!StringUtil.isEmpty(isChanged)){
        	sql.append("SELECT KSID,'北汽幻速' as PPNAME,'SUV' as CXMC,MOTONAME, FROMINFO,"
        			+ "CASE WHEN DEALER_ID='' THEN '抢单' "
        			+ " WHEN STATUS=1 THEN '上报' "
        			+ "     WHEN STATUS=2 THEN '已跟进' END as CAOZUO,"
            		+ "decode(WANADATE,'0','一个月内','1','三个月内','2','半年内','3','一年内','4','一年以上') WANADATE,"
            		+ "CUSNAME,SEX,AGE,CONTACT,"
            		+ "(select REGION_NAME from tm_region where REGION_CODE=PROVINCE) PROVINCE,"
            		+ "(select REGION_NAME from tm_region where REGION_CODE=CITY) CITY,"
            		+ "(select REGION_NAME from tm_region where REGION_CODE=TOWN) TOWN,"
            		+ "decode(STATUS,1,'系统已处理',2,'已跟进',3,'滞后') STATUS,"
            		+ "TIME1,TIME2,TIME3,IS_ALLOCATION,IS_DELAY,org_code,DEALER_ID FROM tt_scsjyydj ");
            sql.append(" WHERE 1 = 1 ");
            if(!StringUtil.isEmpty(fromInfo)){
            	sql.append(" and fromInfo like'%").append(fromInfo).append("%'");
            }
            if(isChanged.equals("0")){
            	sql.append(" and dealer_Id='").append(dealerId).append("'");
            	sql.append(" and STATUS !='2'");
        		if(!"".equals(beginTime)){
        			sql.append("   AND TIME1 >= TO_DATE('" );
        			sql.append(beginTime);
        			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		
        		if(!"".equals(endTime)){
        			sql.append("   AND TIME1 <= TO_DATE('");
        			sql.append(endTime);
        			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		sql.append("  order by time1 desc ");
            }
            if(isChanged.equals("1")){
            	sql.append(" and dealer_Id='").append(dealerId).append("'");
            	sql.append(" and STATUS ='2'");
        		if(!"".equals(beginTime)){
        			sql.append("   AND TIME1 >= TO_DATE('" );
        			sql.append(beginTime);
        			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		
        		if(!"".equals(endTime)){
        			sql.append("   AND TIME1 <= TO_DATE('");
        			sql.append(endTime);
        			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		sql.append("  order by time1 desc ");
            }
            if(isChanged.equals("2")){
            	sql.append(" and org_code ='").append(this.getOrgCode(Long.valueOf(dealerId))).append("'");
        		if(!"".equals(beginTime)){
        			sql.append("   AND TIME1 >= TO_DATE('" );
        			sql.append(beginTime);
        			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		if(!"".equals(endTime)){
        			sql.append("   AND TIME1 <= TO_DATE('");
        			sql.append(endTime);
        			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		sql.append(" union SELECT KSID,'北汽幻速' as PPNAME,'SUV' as CXMC,MOTONAME,FROMINFO,"
        				+ "CASE WHEN DEALER_ID='' THEN '抢单' "
        				+ " WHEN STATUS=1 THEN '上报' "
        				+ "     WHEN STATUS=2 THEN '已跟进' END as CAOZUO,"
                		+ "decode(WANADATE,'0','一个月内','1','三个月内','2','半年内','3','一年内','4','一年以上') WANADATE,"
                		+ "CUSNAME,SEX,AGE,CONTACT,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=PROVINCE) PROVINCE,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=CITY) CITY,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=TOWN) TOWN,"
                		+ "decode(STATUS,1,'系统已处理',2,'已跟进',3,'滞后') STATUS,"
                		+ "TIME1,TIME2,TIME3,IS_ALLOCATION,IS_DELAY,org_code,DEALER_ID FROM tt_scsjyydj ");
                sql.append(" WHERE 1 = 1 ");
                if(!StringUtil.isEmpty(fromInfo)){
                	sql.append(" and fromInfo like'%").append(fromInfo).append("%'");
                }
                sql.append(" and root_org_id ='").append(this.getRootOrgId(Long.valueOf(dealerId))).append("'");
        		if(!"".equals(beginTime)){
        			sql.append("   AND TIME1 >= TO_DATE('" );
        			sql.append(beginTime);
        			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		if(!"".equals(endTime)){
        			sql.append("   AND TIME1 <= TO_DATE('");
        			sql.append(endTime);
        			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		sql.append("  order by time1,ksid desc ");
            }

        }else{	
        	sql.append("SELECT KSID,'北汽幻速' as PPNAME,'SUV' as CXMC,MOTONAME,FROMINFO,"
        			+ "CASE WHEN DEALER_ID='' THEN '抢单' "
        			+ " WHEN STATUS=1 THEN '上报' "
        			+ "     WHEN STATUS=2 THEN '已跟进' END as CAOZUO,"
            		+ "decode(WANADATE,'0','一个月内','1','三个月内','2','半年内','3','一年内','4','一年以上') WANADATE,"
            		+ "CUSNAME,SEX,AGE,CONTACT,"
            		+ "(select REGION_NAME from tm_region where REGION_CODE=PROVINCE) PROVINCE,"
            		+ "(select REGION_NAME from tm_region where REGION_CODE=CITY) CITY,"
            		+ "(select REGION_NAME from tm_region where REGION_CODE=TOWN) TOWN,"
            		+ "decode(STATUS,1,'系统已处理',2,'已跟进',3,'滞后') STATUS,"
            		+ "TIME1,TIME2,TIME3,IS_ALLOCATION,IS_DELAY,org_code,DEALER_ID FROM tt_scsjyydj ");
            sql.append(" WHERE 1 = 1 ");
            if(!StringUtil.isEmpty(fromInfo)){
            	sql.append(" and fromInfo like'%").append(fromInfo).append("%'");
            }
            	sql.append(" and dealer_Id='").append(dealerId).append("'");
        		if(!"".equals(beginTime)){
        			sql.append("   AND TIME1 >= TO_DATE('" );
        			sql.append(beginTime);
        			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		
        		if(!"".equals(endTime)){
        			sql.append("   AND TIME1 <= TO_DATE('");
        			sql.append(endTime);
        			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
        		}

            	sql.append("UNION SELECT KSID,'北汽幻速' as PPNAME,'SUV' as CXMC,MOTONAME,FROMINFO,"
            			+ "CASE WHEN DEALER_ID='' THEN '抢单' "
            			+ " WHEN STATUS=1 THEN '上报' "
            			+ "     WHEN STATUS=2 THEN '已跟进' END as CAOZUO,"
                		+ "decode(WANADATE,'0','一个月内','1','三个月内','2','半年内','3','一年内','4','一年以上') WANADATE,"
                		+ "CUSNAME,SEX,AGE,CONTACT,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=PROVINCE) PROVINCE,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=CITY) CITY,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=TOWN) TOWN,"
                		+ "decode(STATUS,1,'系统已处理',2,'已跟进',3,'滞后') STATUS,"
                		+ "TIME1,TIME2,TIME3,IS_ALLOCATION,IS_DELAY,org_code,DEALER_ID FROM tt_scsjyydj ");
            	sql.append(" WHERE 1 = 1 ");
                if(!StringUtil.isEmpty(fromInfo)){
                	sql.append(" and fromInfo like'%").append(fromInfo).append("%'");
                }
            	sql.append(" and org_code ='").append(this.getOrgCode(Long.valueOf(dealerId))).append("'");
        		if(!"".equals(beginTime)){
        			sql.append("   AND TIME1 >= TO_DATE('" );
        			sql.append(beginTime);
        			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		
        		if(!"".equals(endTime)){
        			sql.append("   AND TIME1 <= TO_DATE('");
        			sql.append(endTime);
        			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		sql.append("UNION SELECT KSID,'北汽幻速' as PPNAME,'SUV' as CXMC,MOTONAME,FROMINFO,"
        				+ "CASE WHEN DEALER_ID='' THEN '抢单' "
        				+ " WHEN STATUS=1 THEN '上报' "
        				+ "     WHEN STATUS=2 THEN '已跟进' END as CAOZUO,"
                		+ "decode(WANADATE,'0','一个月内','1','三个月内','2','半年内','3','一年内','4','一年以上') WANADATE,"
                		+ "CUSNAME,SEX,AGE,CONTACT,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=PROVINCE) PROVINCE,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=CITY) CITY,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=TOWN) TOWN,"
                		+ "decode(STATUS,1,'系统已处理',2,'已跟进',3,'滞后') STATUS,"
                		+ "TIME1,TIME2,TIME3,IS_ALLOCATION,IS_DELAY,org_code,DEALER_ID FROM tt_scsjyydj ");
            	sql.append(" WHERE 1 = 1 ");
                if(!StringUtil.isEmpty(fromInfo)){
                	sql.append(" and fromInfo like'%").append(fromInfo).append("%'");
                }
            	sql.append(" and root_org_id ='").append(this.getRootOrgId(Long.valueOf(dealerId))).append("'");
        		if(!"".equals(beginTime)){
        			sql.append("   AND TIME1 >= TO_DATE('" );
        			sql.append(beginTime);
        			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		
        		if(!"".equals(endTime)){
        			sql.append("   AND TIME1 <= TO_DATE('");
        			sql.append(endTime);
        			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		sql.append("  order by time1 desc ,ksid  ");
        }
        
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }
    public PageResult<Map<String, Object>> queryScsjInfoByDownLoad(String fromInfo,String dealerId, String beginTime, String endTime, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		String isChanged = CommonUtils.checkNull(request.getParamValue("isChanged"));
        if(!StringUtil.isEmpty(isChanged)){
        	sql.append("SELECT KSID,'北汽幻速' as PPNAME,'SUV' as CXMC,MOTONAME, FROMINFO,"
        			+ "CASE WHEN DEALER_ID='' THEN '抢单' "
        			+ " WHEN STATUS=1 THEN '上报' "
        			+ "     WHEN STATUS=2 THEN '已跟进' END as CAOZUO,"
            		+ "decode(WANADATE,'0','一个月内','1','三个月内','2','半年内','3','一年内','4','一年以上') WANADATE,"
            		+ "CUSNAME,SEX,AGE,CONTACT,"
            		+ "(select REGION_NAME from tm_region where REGION_CODE=PROVINCE) PROVINCE,"
            		+ "(select REGION_NAME from tm_region where REGION_CODE=CITY) CITY,"
            		+ "(select REGION_NAME from tm_region where REGION_CODE=TOWN) TOWN,"
            		+ "decode(STATUS,1,'系统已处理',2,'已跟进',3,'滞后') STATUS,"
            		+ "TIME1,TIME2,TIME3,IS_ALLOCATION,IS_DELAY,org_code,DEALER_ID FROM tt_scsjyydj ");
            sql.append(" WHERE 1 = 1 ");
            sql.append(" and DEALER_ID is not null ");
            sql.append(" and DEALER_ID !=0");
            if(!StringUtil.isEmpty(fromInfo)){
            	sql.append(" and fromInfo like'%").append(fromInfo).append("%'");
            }
            if(isChanged.equals("0")){
            	sql.append(" and dealer_Id='").append(dealerId).append("'");
            	sql.append(" and STATUS !='2'");
        		if(!"".equals(beginTime)){
        			sql.append("   AND TIME1 >= TO_DATE('" );
        			sql.append(beginTime);
        			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		
        		if(!"".equals(endTime)){
        			sql.append("   AND TIME1 <= TO_DATE('");
        			sql.append(endTime);
        			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		sql.append("  order by time1 desc ");
            }
            if(isChanged.equals("1")){
            	sql.append(" and dealer_Id='").append(dealerId).append("'");
            	sql.append(" and STATUS ='2'");
        		if(!"".equals(beginTime)){
        			sql.append("   AND TIME1 >= TO_DATE('" );
        			sql.append(beginTime);
        			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		
        		if(!"".equals(endTime)){
        			sql.append("   AND TIME1 <= TO_DATE('");
        			sql.append(endTime);
        			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		sql.append("  order by time1 desc ");
            }
            if(isChanged.equals("2")){
            	sql.append(" and org_code ='").append(this.getOrgCode(Long.valueOf(dealerId))).append("'");
        		if(!"".equals(beginTime)){
        			sql.append("   AND TIME1 >= TO_DATE('" );
        			sql.append(beginTime);
        			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		if(!"".equals(endTime)){
        			sql.append("   AND TIME1 <= TO_DATE('");
        			sql.append(endTime);
        			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		sql.append(" union SELECT KSID,'北汽幻速' as PPNAME,'SUV' as CXMC,MOTONAME,FROMINFO,"
        				+ "CASE WHEN DEALER_ID='' THEN '抢单' "
        				+ " WHEN STATUS=1 THEN '上报' "
        				+ "     WHEN STATUS=2 THEN '已跟进' END as CAOZUO,"
                		+ "decode(WANADATE,'0','一个月内','1','三个月内','2','半年内','3','一年内','4','一年以上') WANADATE,"
                		+ "CUSNAME,SEX,AGE,CONTACT,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=PROVINCE) PROVINCE,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=CITY) CITY,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=TOWN) TOWN,"
                		+ "decode(STATUS,1,'系统已处理',2,'已跟进',3,'滞后') STATUS,"
                		+ "TIME1,TIME2,TIME3,IS_ALLOCATION,IS_DELAY,org_code,DEALER_ID FROM tt_scsjyydj ");
                sql.append(" WHERE 1 = 1 ");
                sql.append(" and DEALER_ID is not null ");
                sql.append(" and DEALER_ID !=0");
                if(!StringUtil.isEmpty(fromInfo)){
                	sql.append(" and fromInfo like'%").append(fromInfo).append("%'");
                }
                sql.append(" and root_org_id ='").append(this.getRootOrgId(Long.valueOf(dealerId))).append("'");
        		if(!"".equals(beginTime)){
        			sql.append("   AND TIME1 >= TO_DATE('" );
        			sql.append(beginTime);
        			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		if(!"".equals(endTime)){
        			sql.append("   AND TIME1 <= TO_DATE('");
        			sql.append(endTime);
        			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		sql.append("  order by time1,ksid desc ");
            }

        }else{	
        	sql.append("SELECT KSID,'北汽幻速' as PPNAME,'SUV' as CXMC,MOTONAME,FROMINFO,"
        			+ "CASE WHEN DEALER_ID='' THEN '抢单' "
        			+ " WHEN STATUS=1 THEN '上报' "
        			+ "     WHEN STATUS=2 THEN '已跟进' END as CAOZUO,"
            		+ "decode(WANADATE,'0','一个月内','1','三个月内','2','半年内','3','一年内','4','一年以上') WANADATE,"
            		+ "CUSNAME,SEX,AGE,CONTACT,"
            		+ "(select REGION_NAME from tm_region where REGION_CODE=PROVINCE) PROVINCE,"
            		+ "(select REGION_NAME from tm_region where REGION_CODE=CITY) CITY,"
            		+ "(select REGION_NAME from tm_region where REGION_CODE=TOWN) TOWN,"
            		+ "decode(STATUS,1,'系统已处理',2,'已跟进',3,'滞后') STATUS,"
            		+ "TIME1,TIME2,TIME3,IS_ALLOCATION,IS_DELAY,org_code,DEALER_ID FROM tt_scsjyydj ");
            sql.append(" WHERE 1 = 1 ");
            if(!StringUtil.isEmpty(fromInfo)){
            	sql.append(" and fromInfo like'%").append(fromInfo).append("%'");
            }
            	sql.append(" and dealer_Id='").append(dealerId).append("'");
        		if(!"".equals(beginTime)){
        			sql.append("   AND TIME1 >= TO_DATE('" );
        			sql.append(beginTime);
        			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		
        		if(!"".equals(endTime)){
        			sql.append("   AND TIME1 <= TO_DATE('");
        			sql.append(endTime);
        			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
        		}

            	sql.append("UNION SELECT KSID,'北汽幻速' as PPNAME,'SUV' as CXMC,MOTONAME,FROMINFO,"
            			+ "CASE WHEN DEALER_ID='' THEN '抢单' "
            			+ " WHEN STATUS=1 THEN '上报' "
            			+ "     WHEN STATUS=2 THEN '已跟进' END as CAOZUO,"
                		+ "decode(WANADATE,'0','一个月内','1','三个月内','2','半年内','3','一年内','4','一年以上') WANADATE,"
                		+ "CUSNAME,SEX,AGE,CONTACT,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=PROVINCE) PROVINCE,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=CITY) CITY,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=TOWN) TOWN,"
                		+ "decode(STATUS,1,'系统已处理',2,'已跟进',3,'滞后') STATUS,"
                		+ "TIME1,TIME2,TIME3,IS_ALLOCATION,IS_DELAY,org_code,DEALER_ID FROM tt_scsjyydj ");
            	sql.append(" WHERE 1 = 1 ");
                sql.append(" and DEALER_ID is not null ");
                sql.append(" and DEALER_ID !=0");
                if(!StringUtil.isEmpty(fromInfo)){
                	sql.append(" and fromInfo like'%").append(fromInfo).append("%'");
                }
            	sql.append(" and org_code ='").append(this.getOrgCode(Long.valueOf(dealerId))).append("'");
        		if(!"".equals(beginTime)){
        			sql.append("   AND TIME1 >= TO_DATE('" );
        			sql.append(beginTime);
        			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		
        		if(!"".equals(endTime)){
        			sql.append("   AND TIME1 <= TO_DATE('");
        			sql.append(endTime);
        			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		sql.append("UNION SELECT KSID,'北汽幻速' as PPNAME,'SUV' as CXMC,MOTONAME,FROMINFO,"
        				+ "CASE WHEN DEALER_ID='' THEN '抢单' "
        				+ " WHEN STATUS=1 THEN '上报' "
        				+ "     WHEN STATUS=2 THEN '已跟进' END as CAOZUO,"
                		+ "decode(WANADATE,'0','一个月内','1','三个月内','2','半年内','3','一年内','4','一年以上') WANADATE,"
                		+ "CUSNAME,SEX,AGE,CONTACT,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=PROVINCE) PROVINCE,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=CITY) CITY,"
                		+ "(select REGION_NAME from tm_region where REGION_CODE=TOWN) TOWN,"
                		+ "decode(STATUS,1,'系统已处理',2,'已跟进',3,'滞后') STATUS,"
                		+ "TIME1,TIME2,TIME3,IS_ALLOCATION,IS_DELAY,org_code,DEALER_ID FROM tt_scsjyydj ");
            	sql.append(" WHERE 1 = 1 ");
                if(!StringUtil.isEmpty(fromInfo)){
                	sql.append(" and fromInfo like'%").append(fromInfo).append("%'");
                }
            	sql.append(" and root_org_id ='").append(this.getRootOrgId(Long.valueOf(dealerId))).append("'");
        		if(!"".equals(beginTime)){
        			sql.append("   AND TIME1 >= TO_DATE('" );
        			sql.append(beginTime);
        			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		
        		if(!"".equals(endTime)){
        			sql.append("   AND TIME1 <= TO_DATE('");
        			sql.append(endTime);
        			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
        		}
        		sql.append("  order by time1 desc ,ksid  ");
        }
        
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }
    /**
     * 
     * @param endTime 
     * @param beginTime 
     * @param logonUser 
     * @param dealerId 
     * @param request 
     * @Title      : scsj预约信息
     * @param      : @param sqlStr
     * @param      : @param pageSize
     * @param      : @param curPage
     * @param      : @return      
     * @return     :    
     * @throws     :
     * LastDate    : 2014-5-22
     */
    public PageResult<Map<String, Object>> queryScsjInfoForFac(String fromInfo, String beginTime, String endTime, int pageSize, int curPage, AclUserBean logonUser) {
        StringBuffer sql = new StringBuffer("");
        ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		String status = request.getParamValue("state");
		String isChanged = request.getParamValue("isChanged");
        sql.append("SELECT "
        		+ "decode(DEALER_ID,0,'转经销商','已转经销商') CAOZUO,"
        		+ "(select tc.Name from tc_user tc where tc.user_id = t.update_by) as update_by_name,t.FROMINFO,"
        		+ "(select tmd.dealer_Name from tm_dealer tmd where tmd.dealer_id = t.dealer_id) as dealer_name,"
        		+ "(select tmd.dealer_code from tm_dealer tmd where tmd.dealer_id = t.dealer_id) as dealer_code,"
        		+ "t.KSID,'北汽幻速' as PPNAME,'SUV' as CXMC,MOTONAME,"
        		+ "decode(WANADATE,'0','一个月内','1','三个月内','2','半年内','3','一年内','4','一年以上') WANADATE,"
        		+ "t.CUSNAME,t.SEX,t.AGE,t.CONTACT,"
        		+ "(select tmr.REGION_NAME from tm_region tmr where REGION_CODE=t.PROVINCE) PROVINCE,"
        		+ "(select tmr.REGION_NAME from tm_region tmr where REGION_CODE=t.CITY) CITY,"
        		+ "(select tmr.REGION_NAME from tm_region tmr where REGION_CODE=t.TOWN) TOWN,"
        		+ "decode(t.STATUS,1,'系统已处理',2,'已跟进',3,'滞后') STATUS,"
        		+ "t.TIME1,t.TIME2,t.TIME3,t.IS_ALLOCATION,t.IS_DELAY,t.org_code,t.DEALER_ID FROM tt_scsjyydj t");
        sql.append(" WHERE 1 = 1 ");
        if(!StringUtil.isEmpty(status)){
        	sql.append(" and t.STATUS ='").append(status).append("'");
        }
        if(!StringUtil.isEmpty(fromInfo)){
        	sql.append(" and t.fromInfo like'%").append(fromInfo).append("%'");
        }
        if(!StringUtil.isEmpty(isChanged)){
        	if(isChanged.equals("0")){
        		sql.append(" and t.DEALER_ID is not null ");
        		sql.append(" and t.DEALER_ID !=0 ");
        	}else{
        		sql.append(" and (t.DEALER_ID is null or t.DEALER_ID =0)");
        	}
        }
		if(!"".equals(beginTime)){
			sql.append("   AND t.TIME1 >= TO_DATE('" );
			sql.append(beginTime);
			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(!"".equals(endTime)){
			sql.append("   AND t.TIME1 <= TO_DATE('");
			sql.append(endTime);
			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("t.DEALER_ID", logonUser));
		
		sql.append(" union SELECT "
				+ "decode(DEALER_ID,0,'转经销商','已转经销商') CAOZUO,"
        		+ "(select tc.Name from tc_user tc where tc.user_id = t.update_by) as update_by_name,t.FROMINFO,"
        		+ "(select tmd.dealer_Name from tm_dealer tmd where tmd.dealer_id = t.dealer_id) as dealer_name,"
        		+ "(select tmd.dealer_code from tm_dealer tmd where tmd.dealer_id = t.dealer_id) as dealer_code,"
        		+ "t.KSID,'北汽幻速' as PPNAME,'SUV' as CXMC,MOTONAME,"
        		+ "decode(WANADATE,'0','一个月内','1','三个月内','2','半年内','3','一年内','4','一年以上') WANADATE,"
        		+ "t.CUSNAME,t.SEX,t.AGE,t.CONTACT,"
        		+ "(select tmr.REGION_NAME from tm_region tmr where REGION_CODE=t.PROVINCE) PROVINCE,"
        		+ "(select tmr.REGION_NAME from tm_region tmr where REGION_CODE=t.CITY) CITY,"
        		+ "(select tmr.REGION_NAME from tm_region tmr where REGION_CODE=t.TOWN) TOWN,"
        		+ "decode(t.STATUS,1,'系统已处理',2,'已跟进',3,'滞后') STATUS,"
        		+ "t.TIME1,t.TIME2,t.TIME3,t.IS_ALLOCATION,t.IS_DELAY,t.org_code,t.DEALER_ID FROM tt_scsjyydj t");

        sql.append(" WHERE 1 = 1 ");
        if(!StringUtil.isEmpty(status)){
        	sql.append(" and t.STATUS ='").append(status).append("'");
        }
        if(!StringUtil.isEmpty(fromInfo)){
        	sql.append(" and t.fromInfo like'%").append(fromInfo).append("%'");
        }
        if(!StringUtil.isEmpty(isChanged)){
        	if(isChanged.equals("0")){
        		sql.append(" and t.DEALER_ID is not null ");
        		sql.append(" and t.DEALER_ID !=0 ");
        	}else{
        		sql.append(" and (t.DEALER_ID is null or t.DEALER_ID =0)");
        	}
        }
		if(!"".equals(beginTime)){
			sql.append("   AND t.TIME1 >= TO_DATE('" );
			sql.append(beginTime);
			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(!"".equals(endTime)){
			sql.append("   AND t.TIME1 <= TO_DATE('");
			sql.append(endTime);
			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("and (t.root_org_id=").append(logonUser.getOrgId()).append(" or org_code in( select org_code from vw_org_dealer_service where root_org_id = "+ logonUser.getOrgId() +"))");//省份限制
		sql.append(" ORDER BY TIME1 desc ,ksid ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }
    public PageResult<Map<String, Object>> queryScsjInfoForFacByDownload(String fromInfo, String beginTime, String endTime, int pageSize, int curPage, AclUserBean logonUser) {
        StringBuffer sql = new StringBuffer("");
        ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		String status = request.getParamValue("state");
		String isChanged = request.getParamValue("isChanged");
        sql.append("SELECT "
        		+ "decode(DEALER_ID,0,'转经销商','已转经销商') CAOZUO,"
        		+ "(select tc.Name from tc_user tc where tc.user_id = t.update_by) as update_by_name,t.FROMINFO,"
        		+ "(select tmd.dealer_Name from tm_dealer tmd where tmd.dealer_id = t.dealer_id) as dealer_name,"
        		+ "(select tmd.dealer_code from tm_dealer tmd where tmd.dealer_id = t.dealer_id) as dealer_code,"
        		+ "t.KSID,'北汽幻速' as PPNAME,'SUV' as CXMC,MOTONAME,"
        		+ "decode(WANADATE,'0','一个月内','1','三个月内','2','半年内','3','一年内','4','一年以上') WANADATE,"
        		+ "t.CUSNAME,t.SEX,t.AGE,t.CONTACT,"
        		+ "(select tmr.REGION_NAME from tm_region tmr where REGION_CODE=t.PROVINCE) PROVINCE,"
        		+ "(select tmr.REGION_NAME from tm_region tmr where REGION_CODE=t.CITY) CITY,"
        		+ "(select tmr.REGION_NAME from tm_region tmr where REGION_CODE=t.TOWN) TOWN,"
        		+ "decode(t.STATUS,1,'系统已处理',2,'已跟进',3,'滞后') STATUS,"
        		+ "t.TIME1,t.TIME2,t.TIME3,t.IS_ALLOCATION,t.IS_DELAY,t.org_code,t.DEALER_ID FROM tt_scsjyydj t");
        sql.append(" WHERE 1 = 1 ");
        if(!StringUtil.isEmpty(status)){
        	sql.append(" and t.STATUS ='").append(status).append("'");
        }
        if(!StringUtil.isEmpty(fromInfo)){
        	sql.append(" and t.fromInfo like'%").append(fromInfo).append("%'");
        }
//        if(!StringUtil.isEmpty(isChanged)){
//        	if(isChanged.equals("0")){
        		sql.append(" and t.DEALER_ID is not null ");
        		sql.append(" and t.DEALER_ID !=0 ");
//        	}else{
//        		sql.append(" and (t.DEALER_ID is null or t.DEALER_ID =0)");
//        	}
//        }
		if(!"".equals(beginTime)){
			sql.append("   AND t.TIME1 >= TO_DATE('" );
			sql.append(beginTime);
			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(!"".equals(endTime)){
			sql.append("   AND t.TIME1 <= TO_DATE('");
			sql.append(endTime);
			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("t.DEALER_ID", logonUser));
		
		sql.append(" union SELECT "
				+ "decode(DEALER_ID,0,'转经销商','已转经销商') CAOZUO,"
        		+ "(select tc.Name from tc_user tc where tc.user_id = t.update_by) as update_by_name,t.FROMINFO,"
        		+ "(select tmd.dealer_Name from tm_dealer tmd where tmd.dealer_id = t.dealer_id) as dealer_name,"
        		+ "(select tmd.dealer_code from tm_dealer tmd where tmd.dealer_id = t.dealer_id) as dealer_code,"
        		+ "t.KSID,'北汽幻速' as PPNAME,'SUV' as CXMC,MOTONAME,"
        		+ "decode(WANADATE,'0','一个月内','1','三个月内','2','半年内','3','一年内','4','一年以上') WANADATE,"
        		+ "t.CUSNAME,t.SEX,t.AGE,t.CONTACT,"
        		+ "(select tmr.REGION_NAME from tm_region tmr where REGION_CODE=t.PROVINCE) PROVINCE,"
        		+ "(select tmr.REGION_NAME from tm_region tmr where REGION_CODE=t.CITY) CITY,"
        		+ "(select tmr.REGION_NAME from tm_region tmr where REGION_CODE=t.TOWN) TOWN,"
        		+ "decode(t.STATUS,1,'系统已处理',2,'已跟进',3,'滞后') STATUS,"
        		+ "t.TIME1,t.TIME2,t.TIME3,t.IS_ALLOCATION,t.IS_DELAY,t.org_code,t.DEALER_ID FROM tt_scsjyydj t");

        sql.append(" WHERE 1 = 1 ");
        if(!StringUtil.isEmpty(status)){
        	sql.append(" and t.STATUS ='").append(status).append("'");
        }
        if(!StringUtil.isEmpty(fromInfo)){
        	sql.append(" and t.fromInfo like'%").append(fromInfo).append("%'");
        }
//        if(!StringUtil.isEmpty(isChanged)){
//        	if(isChanged.equals("0")){
        		sql.append(" and t.DEALER_ID is not null ");
        		sql.append(" and t.DEALER_ID !=0 ");
//        	}else{
//        		sql.append(" and (t.DEALER_ID is null or t.DEALER_ID =0)");
//        	}
//        }
		if(!"".equals(beginTime)){
			sql.append("   AND t.TIME1 >= TO_DATE('" );
			sql.append(beginTime);
			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(!"".equals(endTime)){
			sql.append("   AND t.TIME1 <= TO_DATE('");
			sql.append(endTime);
			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("and (t.root_org_id=").append(logonUser.getOrgId()).append(" or org_code in( select org_code from vw_org_dealer_service where root_org_id = "+ logonUser.getOrgId() +"))");//省份限制
		sql.append(" ORDER BY TIME1 desc ,ksid ");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }
    /**
     * 
     * @param request 
     * @Title      : scsj预约信息
     * @param      : @param sqlStr
     * @param      : @param pageSize
     * @param      : @param curPage
     * @param      : @return      
     * @return     :    
     * @throws     :
     * LastDate    : 2014-5-22
     */
	public List<Map<String, Object>>queryScsjInfoByTemp(){
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT KSID,PPNAME,CXMC,MOTONAME,"
        		+ "WANADATE,"
        		+ "CUSNAME,SEX,AGE,CONTACT,"
        		+ "PROVINCE,"
        		+ "CITY,"
        		+ "TOWN,"
        		+ "STATUS,"
        		+ "TIME1,TIME2 FROM tt_scsjyydj ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" and status='1'");
        sql.append(" and dealer_id=0");
        sql.append(" and ROOT_ORG_ID=0");
		return this.pageQuery(sql.toString(), null, getFunName());
	}
	
    /**
     * 
     * @param request 
     * @Title      : scsj预约信息
     * @param      : @param sqlStr
     * @param      : @param pageSize
     * @param      : @param curPage
     * @param      : @return      
     * @return     :    
     * @throws     :
     * LastDate    : 2014-5-22
     */
	public String getRootOrgId(Long dealerId){
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT ROOT_ORG_ID FROM VW_ORG_DEALER_service ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" and DEALER_ID =").append(dealerId);
//        sql.append(" and status =10011001");
        Map<String, Object> map = this.pageQueryMap(sql.toString(), null, getFunName());
        if(map!=null){
        	return map.get("ROOT_ORG_ID").toString();
        }else{
        	return "";
        }
	}
	public String getOrgCode(Long dealerId){
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT Org_Code FROM VW_ORG_DEALER ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" and DEALER_ID =").append(dealerId);
//        sql.append(" and status =10011001");
        Map<String, Object> map = this.pageQueryMap(sql.toString(), null, getFunName());
        if(map!=null){
        	return map.get("ORG_CODE").toString();
        }else{
        	return "";
        }
	}
	public String getRootOrgId(String ksId){
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT ROOT_ORG_ID FROM tt_scsjyydj ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" and KSID =").append(ksId);
		
		return CommonUtils.checkNull(this.pageQuery(sql.toString(), null, getFunName()).get(0).get("ROOT_ORG_ID"));
	}
	
	public String getOrgCode(String ksId){
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT org_code FROM tt_scsjyydj ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" and KSID =").append(ksId);
		
		return CommonUtils.checkNull(this.pageQuery(sql.toString(), null, getFunName()).get(0).get("ORG_CODE"));
	}
	
	public List<Map<String, Object>> getTime2(Long dealerId){
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM tt_scsjyydj ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" and DEALER_ID =").append(dealerId);
        sql.append(" and status != '2'");
		
		return this.pageQuery(sql.toString(), null, getFunName());
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
    /**
     * 
     * @param request 
     * @Title      : scsj预约信息
     * @param      : @param sqlStr
     * @param      : @param pageSize
     * @param      : @param curPage
     * @param      : @return      
     * @return     :    
     * @throws     :
     * LastDate    : 2014-5-22
     */
	public int checkOnly(String town,String cityId){
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM tm_dealer ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" and city_id =").append(cityId);
        sql.append(" and address like '%").append(town).append("%'");
		
		return this.pageQuery(sql.toString(), null, getFunName()).size();
	}
    /**
     * 
     * @param request 
     * @Title      : scsj预约信息
     * @param      : @param sqlStr
     * @param      : @param pageSize
     * @param      : @param curPage
     * @param      : @return      
     * @return     :    
     * @throws     :
     * LastDate    : 2014-5-22
     */
	public String getTownName(String townId){
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT REGION_NAME FROM tm_region ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" and REGION_CODE ='").append(townId).append("'");
		
		return CommonUtils.checkNull(this.pageQuery(sql.toString(), null, getFunName()).get(0).get("REGION_NAME"));
	}
	
    public PageResult<Map<String,Object>> queryDlr(int pageSize, int curPage){
    	ActionContext act = ActionContext.getContext();
    	RequestWrapper request = act.getRequest();
    	String ksId = request.getParamValue("ksId");
    	String dlrName = request.getParamValue("dlrName");
    	String OrgCode = dao.getOrgCode(ksId);
    	StringBuffer sql= new StringBuffer();
    	if(OrgCode.equals("")||OrgCode.equals("0")){
    		String RootOrgId = dao.getRootOrgId(ksId);
            sql.append(" SELECT tmd.ROOT_DEALER_ID,tmd.dealer_code,tmd.dealer_name,tmd.ROOT_ORG_NAME,tmd.REGION_NAME FROM vw_org_dealer_service tmd ,tm_dealer tm\n");
            sql.append(" WHERE 1=1"); 
            sql.append(" and tmd.dealer_id = tm.dealer_id AND tmd.Root_Org_Id =").append(RootOrgId);
            
    	}else{
            sql.append(" SELECT tmd.ROOT_DEALER_ID,tmd.dealer_code,tmd.dealer_name,tmd.ROOT_ORG_NAME,tmd.REGION_NAME FROM vw_org_dealer_service tmd ,tm_dealer tm\n");
            sql.append(" WHERE 1=1"); 
            sql.append(" and tmd.dealer_id = tm.dealer_id AND tmd.Org_Code ='").append(OrgCode).append("'");
            
    	}
    	sql.append(" AND tm.status =").append(Constant.STATUS_ENABLE);
    	sql.append(" AND tm.dealer_type =10771001");
        if(!StringUtil.isEmpty(dlrName)){
        	sql.append(" and tmd.DEALER_NAME like'%").append(dlrName).append("%'");
        }
    	return  pageQuery(sql.toString(), null, this.getFunName(),pageSize , curPage);
    }

	public List<Map<String, Object>> getRootName(){
		StringBuffer sql = new StringBuffer();
        sql.append("select t.root_org_name,t.root_org_id from VW_ORG_DEALER t group by t.root_org_name,t.root_org_id ");
		
		return this.pageQuery(sql.toString(), null, getFunName());
	}	
	public List<Map<String, Object>> getFormInfo(){
		StringBuffer sql = new StringBuffer();
        sql.append("select t.frominfo from tt_scsjyydj t where t.frominfo is not null group by t.frominfo ");
		
		return this.pageQuery(sql.toString(), null, getFunName());
	}
}

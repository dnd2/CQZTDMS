package com.infodms.dms.dao.report.serviceReport;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.parts.baseManager.mainData.mainDataMaintenance;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class BaseReportViemDao extends BaseDao {
	public static final Logger logger = Logger.getLogger(BaseReportViemDao.class);
	public static final BaseReportViemDao dao = new BaseReportViemDao();
	public static final BaseReportViemDao getInstance(){
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成的方法存根
		return null;
	}
	//运费结算明细
	public PageResult<Map<String, Object>> FreightSettlementSummaryQuery(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		// TODO 自动生成的方法存根
		List par=new ArrayList();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DS.ROOT_DEALER_CODE, /*一级站代码*/\n" );
		sql.append("       DS.DEALER_CODE, /*服务站代码*/\n" );
		sql.append("       DS.DEALER_NAME, /*服务站名称*/\n" );
		sql.append("       SUBSTR(WT.GOODSNUM, 8) BALANCE_MONTH,\n" );
		sql.append("       decode(wt.balance_yieldly,"+Constant.PART_IS_CHANGHE_01+",NVL(wt.sum_carriage,0),"+Constant.PART_IS_CHANGHE_02+",nvl(wt.da_carriage,0),0) tran_sum, /*运费*/\n" );
		sql.append("       decode(wt.balance_yieldly,"+Constant.PART_IS_CHANGHE_01+",'昌河',"+Constant.PART_IS_CHANGHE_02+",'东安','') balance_yieldly, /*结算基地*/\n" );
		sql.append("       to_char(wt.fi_date,'yyyy-mm') fi_month /*系统确认年月*/\n" );
		sql.append("  FROM TT_AS_WR_TICKETS WT, VW_ORG_DEALER_SERVICE DS\n" );
		sql.append(" WHERE WT.DEALERID = DS.DEALER_ID\n" );
		if(!CommonUtils.isNullString(map.get("yieldlyType"))) {
				sql.append(" AND wt.balance_yieldly = ?\n"); 
				par.add(""+map.get("yieldlyType").trim()+"");
			}
		if(!CommonUtils.isNullString(map.get("supplyCode"))) {
			sql.append(" AND ds.dealer_code LIKE ?\n"); 
			par.add("%"+map.get("supplyCode").trim()+"%");
		}
		if(!CommonUtils.isNullString(map.get("supplyName"))) {
			sql.append(" AND ds.dealer_name LIKE ?\n"); 
			par.add("%"+map.get("supplyName").trim()+"%");
		}
		if(Utility.testString(map.get("bgDate"))){
			sql.append("and to_date(SUBSTR(WT.GOODSNUM, 8),'yyyy-mm')>=to_date(?,'yyyy-mm-dd')\n"); 
			par.add(map.get("bgDate"));
			
		}
		if(Utility.testString(map.get("egDate"))){
			sql.append("and to_date(SUBSTR(WT.GOODSNUM, 8),'yyyy-mm')<=to_date(?,'yyyy-mm-dd')\n"); 
			par.add(map.get("egDate"));
		}
		if(Utility.testString(map.get("bDate"))){
			sql.append("and wt.fi_date>=to_date(?,'yyyy-mm-dd')\n"); 
			par.add(map.get("bDate"));

		}
		if(Utility.testString(map.get("eDate"))){
			sql.append("and wt.fi_date<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
			par.add(map.get("eDate")+" 23:59:59");
		}
		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), par, getFunName(),pageSize, curPage);
		return ps;
	}

	public PageResult<Map<String, Object>> AfterSalesServiceQuery(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		List par=new ArrayList();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT '主因件结算件数' TYPE, /*类型*/\n" );
		sql.append("       SUM(DECODE(TO_CHAR(A.FI_DATE,'YYYYMM'),?/*当年单月*/,NVL(P.BALANCE_QUANTITY, 0),0)) MNTOTAL, /*当年单月*/\n" );
		par.add(onLYearScope(map.get("bDate")));
		sql.append("       SUM(DECODE(TO_CHAR(A.FI_DATE,'YYYYMM'),?/*前年单月*/,NVL(P.BALANCE_QUANTITY, 0),0)) MYTOTAL, /*前年单月*/\n" );
		par.add(onYearScope(map.get("bDate")));
		sql.append("       ROUND(SUM(DECODE(TO_CHAR(A.FI_DATE,'YYYYMM'),?/*当年单月*/,NVL(P.BALANCE_QUANTITY, 0),0)) * 100/\n" );
		par.add(onLYearScope(map.get("bDate")));
		sql.append("       SUM(DECODE(TO_CHAR(A.FI_DATE,'YYYYMM'),?/*前年单月*/,NVL(P.BALANCE_QUANTITY, 0),0)),2) MPER, /*前年单月比*/\n" );
		par.add(onYearScope(map.get("bDate")));
		sql.append("\n" );
		sql.append("       SUM(DECODE(TO_CHAR(A.FI_DATE,'YYYY'),?/*当年月累计*/,NVL(P.BALANCE_QUANTITY, 0),0)) YNTOTAL, /*当年月累计*/\n" );
		par.add(onLYearScope1(map.get("bDate")));
		sql.append("       SUM(DECODE(TO_CHAR(A.FI_DATE,'YYYY'),?/*前年月累计*/,NVL(P.BALANCE_QUANTITY, 0),0)) YYTOTAL, /*前年月累计*/\n" );
		par.add(onYearScope1(map.get("bDate")));
		
		sql.append("       ROUND(SUM(DECODE(TO_CHAR(A.FI_DATE,'YYYY'),?/*当年月累计*/,NVL(P.BALANCE_QUANTITY, 0),0)) * 100/\n" );
		par.add(onLYearScope1(map.get("bDate")));
		sql.append("       SUM(DECODE(TO_CHAR(A.FI_DATE,'YYYY'),?/*前年月累计*/,NVL(P.BALANCE_QUANTITY, 0),0)),2) YPER /*前年累计比*/\n" );
		par.add(onYearScope1(map.get("bDate")));
		sql.append("  FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("  JOIN TT_AS_WR_PARTSITEM P ON A.ID = P.ID\n" );
		sql.append(" WHERE A.SERIES_CODE IN ('B', 'D', 'E', 'F', 'H', 'X')\n" );
		sql.append("   AND A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_13+"\n" );
		sql.append("   AND A.CLAIM_TYPE IN ("+Constant.CLA_TYPE_01+", "+Constant.CLA_TYPE_07+", "+Constant.CLA_TYPE_09+", "+Constant.CLA_TYPE_10+")\n" );
		sql.append("   AND P.RESPONSIBILITY_TYPE = "+Constant.RESPONS_NATURE_STATUS_01+"\n" );
		// 艾春 13.11.14 修改 添加配件代码和更换类型控制 开始
		sql.append("   AND P.PART_CODE <> 'ZB-001' AND P.PART_USE_TYPE = 1\n" );
		// 艾春 13.11.14 修改 添加配件代码和更换类型控制结束
		sql.append("   AND A.BALANCE_YIELDLY = "+Constant.PART_IS_CHANGHE_01+"\n" );
		sql.append("   /*得到页面月份后,生成当年第一天,生成去年第一天,生成去年同月*/\n" );
		
		if(Utility.testString(map.get("bDate"))){
			sql.append("   AND ((A.FI_DATE >= TO_DATE(?, 'YYYY-MM-DD') AND\n" );
			sql.append("       A.FI_DATE <= TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')) OR\n" );
			sql.append("       (A.FI_DATE >= TO_DATE(?, 'YYYY-MM-DD') AND\n" );
			sql.append("       A.FI_DATE <= TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')))\n" );
			String  date=DateUtil.getYearByDate(DateUtil.str2Date(map.get("bDate"),"-"))+"-01-01";
			par.add(date);
			par.add(onLastYearScope(map.get("bDate")));
			par.add(appointYearScope(date));
			par.add(onNestYearScope(map.get("bDate")));

		}
		sql.append("UNION ALL\n" );
		sql.append("select type,\n" );
		sql.append("       sum(decode(r, 1, MNTOTAL + l_amount, MNTOTAL)) MNTOTAL,\n" );
		sql.append("       sum(decode(r, 1, MYTOTAL + y_amount, MYTOTAL)) MYTOTAL,\n" );
		sql.append("       round(sum(decode(r, 1, MNTOTAL + l_amount, MNTOTAL)) * 100/sum(decode(r, 1, MYTOTAL + y_amount, MYTOTAL)),2) MPER,\n" );
		sql.append("       sum(decode(r, 1, YNTOTAL + l_YNTOTAL, YNTOTAL)) YNTOTAL,\n" );
		sql.append("       sum(decode(r, 1, YYTOTAL + l_YYTOTAL, YYTOTAL)) YYTOTAL,\n" );
		sql.append("       round(sum(decode(r, 1, YNTOTAL + l_YNTOTAL, YNTOTAL)) * 100 / sum(decode(r, 1, YYTOTAL + l_YYTOTAL, YYTOTAL)),2) YPER\n" );
		sql.append(" from (\n" );
		sql.append("SELECT '主因件结算金额' type,\n" );
		sql.append("       DECODE(TO_CHAR(A.FI_DATE,'YYYYMM'),?/*当年单月*/,NVL(P.BALANCE_AMOUNT, 0),0) MNTOTAL,\n" );
		sql.append("       DECODE(TO_CHAR(A.FI_DATE,'YYYYMM'),?/*前年单月*/,NVL(P.BALANCE_AMOUNT, 0),0) MYTOTAL,\n" );
		sql.append("       DECODE(TO_CHAR(A.FI_DATE,'YYYY'),?/*当年月累计*/,NVL(P.BALANCE_AMOUNT, 0),0) YNTOTAL,\n" );
		sql.append("       DECODE(TO_CHAR(A.FI_DATE,'YYYY'),?/*前年月累计*/,NVL(P.BALANCE_AMOUNT, 0),0) YYTOTAL,\n" );
		sql.append("       DECODE(TO_CHAR(A.FI_DATE,'YYYYMM'),?/*当年单月*/,NVL(l.BALANCE_AMOUNT,0),0) l_amount,\n" );
		sql.append("        DECODE(TO_CHAR(A.FI_DATE,'YYYYMM'),?/*当年单月*/,NVL(l.BALANCE_AMOUNT,0),0) y_amount,\n" );
		sql.append("         DECODE(TO_CHAR(A.FI_DATE,'YYYY'),?/*当年月累计*/,NVL(l.BALANCE_AMOUNT, 0),0) l_YNTOTAL,\n" );
		sql.append("        DECODE(TO_CHAR(A.FI_DATE,'YYYY'),?/*前年月累计*/,NVL(l.BALANCE_AMOUNT, 0),0) l_YYTOTAL,\n" );
		sql.append("       DENSE_RANK() OVER(PARTITION BY A.ID, L.WR_LABOURCODE ORDER BY P.PART_ID) r\n" );
		sql.append("  FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("  JOIN TT_AS_WR_PARTSITEM P ON A.ID = P.ID\n" );
		sql.append("  LEFT JOIN Tt_As_Wr_Labouritem L ON A.ID = L.ID AND P.WR_LABOURCODE = L.WR_LABOURCODE\n" );
		sql.append(" WHERE A.SERIES_CODE IN ('B', 'D', 'E', 'F', 'H', 'X')\n" );
		sql.append("   AND A.STATUS = 10791013\n" );
		sql.append("   AND A.CLAIM_TYPE IN ( 10661001, 10661007, 10661009, 10661010)\n" );
		sql.append("   AND P.RESPONSIBILITY_TYPE = 94001001\n" );
		sql.append("   AND P.PART_CODE <> 'ZB-001' AND P.PART_USE_TYPE = 1\n" );
		sql.append("   AND A.BALANCE_YIELDLY = 95411001\n" );
		sql.append("   /*得到页面月份后,生成当年第一天,生成去年第一天,生成去年同月*/\n" );
		sql.append("   AND ((A.FI_DATE >= TO_DATE(?, 'YYYY-MM-DD') AND\n" );
		sql.append("       A.FI_DATE <= TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')) OR\n" );
		sql.append("       (A.FI_DATE >= TO_DATE(?, 'YYYY-MM-DD') AND\n" );
		sql.append("       A.FI_DATE <= TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')))) group by type");
		par.add(onLYearScope(map.get("bDate")));
		par.add(onYearScope(map.get("bDate")));
		par.add(onLYearScope1(map.get("bDate")));
		par.add(onYearScope1(map.get("bDate")));
		par.add(onLYearScope(map.get("bDate")));
		par.add(onYearScope(map.get("bDate")));
		par.add(onLYearScope1(map.get("bDate")));
		par.add(onYearScope1(map.get("bDate")));
		if(Utility.testString(map.get("bDate"))){
			String  date=DateUtil.getYearByDate(DateUtil.str2Date(map.get("bDate"),"-"))+"-01-01";
			par.add(date);
			par.add(onLastYearScope(map.get("bDate")));
			par.add(appointYearScope(date));
			par.add(onNestYearScope(map.get("bDate")));

		}
		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), par, getFunName(),pageSize, curPage);
		return ps;

	}
	
	   /**
     * 获取上一年的当月当天
     * 
     * @param appointValue 传入类型
     * @param endDate
     * @return Map
     */
    public static String appointYearScope(String endDate) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = null;
        Date d = null;
        try {
            if (endDate != null) {
                d = sf.parse(endDate);
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.add(c.YEAR, -1);
                startDate = sf.format(c.getTime());
            }
        } catch (Exception e) {
        }
        return startDate;
    }
    /**  
    * TODO 获取当年的上一年 年月
    * @param name  
    * @param @return 设定文件  
    * @return String DOM对象  
    * @Exception 异常对象  
    * @since  CodingExample　Ver(编码范例查看) 1.1  
    */  
    public static String onYearScope(String endDate) {
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMM");
    	String startDate = null;
    	Date d = null;
    	try {
    		if (endDate != null) {
    			d = sf.parse(endDate);
    			Calendar c = Calendar.getInstance();
    			c.setTime(d);
    			c.add(c.YEAR, -1);
    			startDate = sf1.format(c.getTime());
    		}
    	} catch (Exception e) {
    	}
    	return startDate;
    }
    /**  
     * TODO 获取当年
     * @param name  
     * @param @return 设定文件  
     * @return String DOM对象  
     * @Exception 异常对象  
     * @since  CodingExample　Ver(编码范例查看) 1.1  
     */  
    public static String onLYearScope1(String endDate) {
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat sf1 = new SimpleDateFormat("yyyy");
    	String startDate = null;
    	Date d = null;
    	try {
    		if (endDate != null) {
    			d = sf.parse(endDate);
    			Calendar c = Calendar.getInstance();
    			c.setTime(d);
    		
    			startDate = sf1.format(c.getTime());
    		}
    	} catch (Exception e) {
    	}
    	return startDate;
    }
    /**  
     * TODO 获取当年的上一年
     * @param name  
     * @param @return 设定文件  
     * @return String DOM对象  
     * @Exception 异常对象  
     * @since  CodingExample　Ver(编码范例查看) 1.1  
     */  
    public static String onYearScope1(String endDate) {
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat sf1 = new SimpleDateFormat("yyyy");
    	String startDate = null;
    	Date d = null;
    	try {
    		if (endDate != null) {
    			d = sf.parse(endDate);
    			Calendar c = Calendar.getInstance();
    			c.setTime(d);
    			c.add(c.YEAR, -1);
    			startDate = sf1.format(c.getTime());
    		}
    	} catch (Exception e) {
    	}
    	return startDate;
    }
    /**  
     * TODO 获取当年 年月
     * @param name  
     * @param @return 设定文件  
     * @return String DOM对象  
     * @Exception 异常对象  
     * @since  CodingExample　Ver(编码范例查看) 1.1  
     */  
    public static String onLYearScope(String endDate) {
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMM");
    	String startDate = null;
    	Date d = null;
    	try {
    		if (endDate != null) {
    			d = sf.parse(endDate);
    			Calendar c = Calendar.getInstance();
    			c.setTime(d);
    			
    			startDate = sf1.format(c.getTime());
    		}
    	} catch (Exception e) {
    	}
    	return startDate;
    }
    //获取当年当月最后一天
    public static String onLastYearScope(String endDate) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = null;
        Date d = null;
        try {
            if (endDate != null) {
                d = sf.parse(endDate);
                Calendar c = Calendar.getInstance();
                c.setTime(d);
        		c.set( Calendar.DATE, 1 );
                c.roll(Calendar.DATE,  -1 );  

                startDate = sf.format(c.getTime());
            }
        } catch (Exception e) {
        }
    	return startDate+" 23:59:59";
    }
    //获取上年当月最后一天
    public static String onNestYearScope(String endDate) {
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    	String startDate = null;
    	Date d = null;
    	try {
    		if (endDate != null) {
    			d = sf.parse(endDate);
    			Calendar c = Calendar.getInstance();
    			c.setTime(d);
    			c.add(c.YEAR, -1);
    			c.set( Calendar.DATE, 1 );
    			c.roll(Calendar.DATE, -1 );  
    			startDate = sf.format(c.getTime());
    		}
    	} catch (Exception e) {
    	}
    	return startDate+" 23:59:59";
    }
    //获取当年当月第一天
    public static String onLastYearScope1(String endDate) {
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    	String startDate = null;
    	Date d = null;
    	try {
    		if (endDate != null) {
    			d = sf.parse(endDate);
    			Calendar c = Calendar.getInstance();
    			c.setTime(d);
    			c.set(GregorianCalendar.DAY_OF_MONTH, 1);   

    			startDate = sf.format(c.getTime());
    		}
    	} catch (Exception e) {
    	}
    	return startDate;
    }
    //获取上年当月第一天
    public static String onNestYearScope1(String endDate) {
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    	String startDate = null;
    	Date d = null;
    	try {
    		if (endDate != null) {
    			d = sf.parse(endDate);
    			Calendar c = Calendar.getInstance();
    			c.setTime(d);
    			c.add(c.YEAR, -1);
    			c.set(GregorianCalendar.DAY_OF_MONTH, 1);    
    			startDate = sf.format(c.getTime());
    		}
    	} catch (Exception e) {
    	}
    	return startDate;
    }
    //
//外部质量损失汇总
	public PageResult<Map<String, Object>> ExternalQualityQuery(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		StringBuffer sql= new StringBuffer();
		List par=new ArrayList();
		sql.append("SELECT decode(grouping_id(a.balance_yieldly),1,'昌河东安总计',decode(grouping_id(a.group_name),1,\n" );
		sql.append("         decode(a.balance_yieldly,"+Constant.PART_IS_CHANGHE_01+",'昌河汽车合计','东安发动机合计'),a.group_name)) series, /*车系*/\n" );
		sql.append("       a.model_code, /*车型*/\n" );
		sql.append("       sum(decode(a.claim_type,"+Constant.CLA_TYPE_02+",NVL(a.labour_price,0),0)) fl_amount, /*强保工时费*/\n" );
		sql.append("       SUM(decode(a.claim_type,"+Constant.CLA_TYPE_02+",nvl(a.part_price,0),0)) fp_amount, /*强保材料费*/\n" );
		sql.append("       SUM(decode(a.claim_type,"+Constant.CLA_TYPE_02+",NVL(a.free,0),0)) f_amount, /*强保小计*/\n" );
		sql.append("\n" );
		sql.append("       SUM(decode(a.claim_type,"+Constant.CLA_TYPE_07+",NVL(a.apply_labour_amount,0),0)) ql_amount, /*售前工时费*/\n" );
		sql.append("       SUM(decode(a.claim_type,"+Constant.CLA_TYPE_07+",NVL(a.apply_part_amount,0),0)) qp_amount, /*售前材料费*/\n" );
		sql.append("       SUM(decode(a.claim_type,"+Constant.CLA_TYPE_07+",NVL(a.apply_repair_total,0),0)) q_amount, /*售前小计*/\n" );
		sql.append("\n" );
		sql.append("       SUM(CASE WHEN a.claim_type IN ("+Constant.CLA_TYPE_01+","+Constant.CLA_TYPE_09+") THEN NVL(a.apply_labour_amount,0) ELSE 0 END) bl_amount, /*保修工时费*/\n" );
		sql.append("       SUM(CASE WHEN a.claim_type IN ("+Constant.CLA_TYPE_01+","+Constant.CLA_TYPE_09+") THEN NVL(a.apply_part_amount,0) ELSE 0 END) bp_amount, /*保修材料费*/\n" );
		sql.append("       SUM(CASE WHEN a.claim_type = "+Constant.CLA_TYPE_09+" THEN NVL(a.apply_netitem_amount,0) ELSE 0 END) bw_amount, /*保修派出费*/\n" );
		sql.append("       SUM(CASE WHEN a.claim_type IN ("+Constant.CLA_TYPE_01+","+Constant.CLA_TYPE_09+")\n" );
		sql.append("                THEN CASE WHEN a.claim_type = "+Constant.CLA_TYPE_01+" THEN NVL(a.apply_labour_amount,0) + NVL(a.apply_part_amount,0)\n" );
		sql.append("                          ELSE NVL(a.apply_labour_amount,0) + NVL(a.apply_part_amount,0) + NVL(a.apply_netitem_amount,0) END\n" );
		sql.append("                ELSE 0 END) b_amount, /*保修小计*/\n" );
		sql.append("       SUM(decode(a.claim_type,"+Constant.CLA_TYPE_10+",NVL(a.apply_labour_amount,0),0)) tl_amount, /*特殊工时费*/\n" );
		sql.append("       SUM(decode(a.claim_type,"+Constant.CLA_TYPE_10+",NVL(a.apply_part_amount,0),0)) tp_amount, /*特殊材料费*/\n" );
		sql.append("       SUM(decode(a.claim_type,"+Constant.CLA_TYPE_10+",NVL(a.apply_repair_total,0),0)) t_amount, /*特殊小计*/\n" );
		sql.append("\n" );
		sql.append("       SUM(decode(a.claim_type,10661020,NVL(a.apply_labour_amount,0),0)) hl_amount, /*退换车工时费*/\n" );
		sql.append("       SUM(decode(a.claim_type,10661020,NVL(a.apply_part_amount,0),0)) hp_amount, /*退换车材料费*/\n" );
		sql.append("       SUM(decode(a.claim_type,10661020,NVL(a.apply_repair_total,0),0)) h_amount, /*退换车小计*/\n" );
		sql.append("\n" );
		sql.append("       0 pl_amount, /*批量工时费*/\n" );
		sql.append("       0 pp_amount, /*批量材料费*/\n" );
		sql.append("       0 pg_amount, /*批量管理费*/\n" );
		sql.append("       0 p_amount /*批量小计*/\n" );
		sql.append("  FROM (SELECT t.balance_yieldly, vs.group_name, t.model_code, t.claim_type,\n" );
		sql.append("               wg.labour_price, wg.part_price, wg.free, t.apply_labour_amount,\n" );
		sql.append("               t.apply_part_amount, t.apply_netitem_amount, t.apply_repair_total\n" );
		sql.append("        FROM TT_AS_WR_APPLICATION t\n" );
		sql.append("        LEFT JOIN TM_VHCL_MATERIAL_GROUP VS ON t.SERIES_CODE = VS.GROUP_CODE AND VS.GROUP_LEVEL = 2\n" );
		sql.append("        LEFT JOIN tm_vhcl_material_group vm ON vm.group_code = t.model_code AND vm.group_level = 4\n" );
		sql.append("        LEFT JOIN tt_as_wr_model_item wi ON wi.model_id = vm.group_id\n" );
		sql.append("        LEFT JOIN tt_as_wr_model_group wg ON wi.wrgroup_id = wg.wrgroup_id\n" );
		sql.append("       WHERE t.status >= "+Constant.CLAIM_APPLY_ORD_TYPE_02+"\n" );
		sql.append("        AND t.create_date >= to_date('2013-08-26','yyyy-mm-dd')\n" );
		sql.append("        /*AND vs.group_name LIKE '%%' \\*车系查询条件*\\\n" );
		sql.append("        AND t.model_code LIKE '%%' \\*车型查询条件*\\\n" );
		sql.append("        AND t.REPORT_DATE >= TO_DATE('' \\*结算上报日期开始*\\, 'YYYY-MM-DD')\n" );
		sql.append("        AND t.REPORT_DATE <= TO_DATE('' \\*结算上报日期结束*\\, 'YYYY-MM-DD HH24:MI:SS')*/\n" );
		   if(!CommonUtils.isNullString(map.get("serisid"))) {
				sql.append(" AND vs.group_name like ?\n"); 
				par.add("%"+map.get("serisid").trim()+"%");
			}
	   
		   if(!CommonUtils.isNullString(map.get("groupCode"))) {
			   sql.append(" AND t.model_code like ?\n"); 
			   par.add("%"+map.get("groupCode").trim()+"%");
		   }
			
			if(Utility.testString(map.get("bDate"))){
				sql.append("and t.REPORT_DATE>=to_date(?,'yyyy-mm-dd')\n"); 
				par.add(map.get("bDate"));

			}
		
		if(Utility.testString(map.get("eDate"))){
			sql.append("and t.REPORT_DATE<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
			par.add(map.get("eDate")+" 23:59:59");
		}
		sql.append("        UNION ALL\n" );
		sql.append("        SELECT "+Constant.PART_IS_CHANGHE_01+", vs.group_name, ws.v_model, decode(ws.fee_type,118310043, 10661020, 10661010), 0, 0, 0,\n" );
		sql.append("               decode(ws.balance_fee_type,"+Constant.PART_IS_CHANGHE_01+",NVL(ws.declare_sum1,0),0) apply_labour_amount,\n" );
		sql.append("               decode(ws.balance_fee_type,"+Constant.PART_IS_CHANGHE_02+",NVL(ws.declare_sum1,0),0) apply_part_amount, 0, NVL(ws.declare_sum1,0)\n" );
		sql.append("         FROM tt_as_wr_spefee ws\n" );
		sql.append("          LEFT JOIN tm_vhcl_material_group vm ON ws.v_model = vm.group_code AND vm.group_level = 3\n" );
		sql.append("          LEFT JOIN tm_vhcl_material_group vs ON vs.group_id = vm.parent_group_id AND vs.group_level = 2\n" );
		sql.append("         WHERE ws.fee_type IN ("+Constant.FEE_TYPE_01+", 118310043, "+Constant.FEE_TYPE_04+", 11831005) AND ws.status >= 11841002\n" );
		sql.append("         /*AND vs.group_name LIKE '%%' \\*车系查询条件*\\\n" );
		sql.append("         AND ws.v_model LIKE '%%' \\*车型查询条件*\\\n" );
		sql.append("         AND ws.MAKE_DATE >= TO_DATE('' \\*结算上报日期开始*\\, 'YYYY-MM-DD')\n" );
		sql.append("         AND ws.MAKE_DATE <= TO_DATE('' \\*结算上报日期结束*\\, 'YYYY-MM-DD HH24:MI:SS')*/\n" );
		   if(!CommonUtils.isNullString(map.get("serisid"))) {
				sql.append(" AND vs.group_name like ?\n"); 
				par.add("%"+map.get("serisid").trim()+"%");
			}
	   
		   if(!CommonUtils.isNullString(map.get("groupCode"))) {
			   sql.append(" AND ws.v_model like ?\n"); 
			   par.add("%"+map.get("groupCode").trim()+"%");
		   }
			
			if(Utility.testString(map.get("bDate"))){
				sql.append("and ws.MAKE_DATE>=to_date(?,'yyyy-mm-dd')\n"); 
				par.add(map.get("bDate"));

			}
		
		if(Utility.testString(map.get("eDate"))){
			sql.append("and ws.MAKE_DATE<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
			par.add(map.get("eDate")+" 23:59:59");
		}
		sql.append("        ) a\n" );
		sql.append(" GROUP BY rollup(a.balance_yieldly, (a.group_name, a.model_code))\n" );
		sql.append("UNION ALL\n" );
		sql.append("SELECT decode(grouping_id(a.balance_yieldly),1,'昌河发动机总计',decode(grouping_id(vs.group_name),1,'昌河发动机合计',vs.group_name)) series, /*车系*/\n" );
		sql.append("       a.model_code, /*车型*/\n" );
		sql.append("       0 fl_amount, /*强保工时费*/\n" );
		sql.append("       0 fp_amount, /*强保材料费*/\n" );
		sql.append("       0 f_amount, /*强保小计*/\n" );
		sql.append("\n" );
		sql.append("       SUM(decode(a.claim_type,"+Constant.CLA_TYPE_07+",NVL(a.apply_labour_amount,0),0)) ql_amount, /*售前工时费*/\n" );
		sql.append("       SUM(decode(a.claim_type,"+Constant.CLA_TYPE_07+",NVL(a.apply_part_amount,0),0)) qp_amount, /*售前材料费*/\n" );
		sql.append("       SUM(decode(a.claim_type,"+Constant.CLA_TYPE_07+",NVL(a.apply_repair_total,0),0)) q_amount, /*售前小计*/\n" );
		sql.append("\n" );
		sql.append("       SUM(CASE WHEN a.claim_type IN ("+Constant.CLA_TYPE_01+","+Constant.CLA_TYPE_09+") THEN NVL(a.apply_labour_amount,0) ELSE 0 END) bl_amount, /*保修工时费*/\n" );
		sql.append("       SUM(CASE WHEN a.claim_type IN ("+Constant.CLA_TYPE_01+","+Constant.CLA_TYPE_09+") THEN NVL(a.apply_part_amount,0) ELSE 0 END) bp_amount, /*保修材料费*/\n" );
		sql.append("       SUM(CASE WHEN a.claim_type = "+Constant.CLA_TYPE_09+" THEN NVL(a.apply_netitem_amount,0) ELSE 0 END) bw_amount, /*保修派出费*/\n" );
		sql.append("       SUM(CASE WHEN a.claim_type IN ("+Constant.CLA_TYPE_01+","+Constant.CLA_TYPE_09+")\n" );
		sql.append("                THEN CASE WHEN a.claim_type = "+Constant.CLA_TYPE_01+" THEN NVL(a.apply_labour_amount,0) + NVL(a.apply_part_amount,0)\n" );
		sql.append("                          ELSE NVL(a.apply_labour_amount,0) + NVL(a.apply_part_amount,0) + NVL(a.apply_netitem_amount,0) END\n" );
		sql.append("                ELSE 0 END) b_amount, /*保修小计*/\n" );
		sql.append("       SUM(decode(a.claim_type,"+Constant.CLA_TYPE_10+",NVL(a.apply_labour_amount,0),0)) tl_amount, /*特殊工时费*/\n" );
		sql.append("       SUM(decode(a.claim_type,"+Constant.CLA_TYPE_10+",NVL(a.apply_part_amount,0),0)) tp_amount, /*特殊材料费*/\n" );
		sql.append("       SUM(decode(a.claim_type,"+Constant.CLA_TYPE_10+",NVL(a.apply_repair_total,0),0)) t_amount, /*特殊小计*/\n" );
		sql.append("\n" );
		sql.append("       0 hl_amount, /*工时费*/\n" );
		sql.append("       0 hp_amount, /*特殊材料费*/\n" );
		sql.append("       0 h_amount, /*特殊小计*/\n" );
		sql.append("\n" );
		sql.append("       0 pl_amount, /*批量工时费*/\n" );
		sql.append("       0 pp_amount, /*批量材料费*/\n" );
		sql.append("       0 pg_amount, /*批量管理费*/\n" );
		sql.append("       0 p_amount /*批量小计*/\n" );
		sql.append("\n" );
		sql.append("        FROM TT_AS_WR_APPLICATION a\n" );
		sql.append("        LEFT JOIN TM_VHCL_MATERIAL_GROUP VS ON a.SERIES_CODE = VS.GROUP_CODE AND VS.GROUP_LEVEL = 2\n" );
		sql.append("        LEFT JOIN tm_vhcl_material_group vm ON vm.group_code = a.model_code AND vm.group_level = 4\n" );
		sql.append("        LEFT JOIN tt_as_wr_model_item wi ON wi.model_id = vm.group_id\n" );
		sql.append("        LEFT JOIN tt_as_wr_model_group wg ON wi.wrgroup_id = wg.wrgroup_id\n" );
		sql.append("       WHERE a.status >= 10791002\n" );
		sql.append("        AND a.create_date >= to_date('2013-08-26','yyyy-mm-dd')\n" );
		sql.append("        AND a.balance_yieldly = 95411001\n" );
		sql.append("        AND EXISTS (SELECT 1 FROM Tt_As_Wr_Partsitem p, tt_part_define pd\n" );
		sql.append("                     WHERE p.part_code = pd.part_oldcode\n" );
		sql.append("                      AND p.ID = a.ID AND pd.part_is_changhe = 95411001\n" );
		sql.append("                      AND pd.IS_ENGINE = 1)\n" );
		sql.append("        /*AND vs.group_name LIKE '%%' \\*车系查询条件*\\\n" );
		sql.append("        AND a.model_code LIKE '%%' \\*车型查询条件*\\\n" );
		sql.append("        AND a.REPORT_DATE >= TO_DATE('' \\*结算上报日期开始*\\, 'YYYY-MM-DD')\n" );
		sql.append("        AND a.REPORT_DATE <= TO_DATE('' \\*结算上报日期结束*\\, 'YYYY-MM-DD HH24:MI:SS')*/\n" );
		   if(!CommonUtils.isNullString(map.get("serisid"))) {
				sql.append(" AND vs.group_name like ?\n"); 
				par.add("%"+map.get("serisid").trim()+"%");
			}
	   
		   if(!CommonUtils.isNullString(map.get("groupCode"))) {
			   sql.append(" AND a.model_code like ?\n"); 
			   par.add("%"+map.get("groupCode").trim()+"%");
		   }
			
			if(Utility.testString(map.get("bDate"))){
				sql.append("and a.REPORT_DATE>=to_date(?,'yyyy-mm-dd')\n"); 
				par.add(map.get("bDate"));

			}
		
		if(Utility.testString(map.get("eDate"))){
			sql.append("and a.REPORT_DATE<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
			par.add(map.get("eDate")+" 23:59:59");
		}
		sql.append("  GROUP BY rollup(a.balance_yieldly, (vs.group_name, a.model_code))");

		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), par, getFunName(),pageSize, curPage);
		return ps;

	}

}

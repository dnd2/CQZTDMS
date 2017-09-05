package com.infodms.dms.dao.sales.storage;

import java.util.ArrayList;
import java.util.List;

import com.infodms.dms.common.Constant;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;

/**
 * @author liuxianghui
 *         20130517
 *         储运相关数量更新
 */
public class StorageUtil {
	private StorageUtil()
	{
	}
	
	public static StorageUtil getInstance()
	{
		return new StorageUtil();
	}
	
	private POFactory factory = POFactoryBuilder.getInstance();
	
	/**
	 * 组板,配车操作数量更新
	 * 组板表ID
	 * 
	 * @param boDeId
	 */
	public void updateBoardOrAllocaNum(Long boId, Long userId)
	{//
	
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE   TT_SALES_BO_DETAIL A\n");
		sbSql.append("   SET   A.ALLOCA_NUM =\n");
		sbSql.append("            (SELECT   COUNT (1)\n");
		sbSql.append("               FROM   TT_SALES_ALLOCA_DE B\n");
		sbSql.append("              WHERE   B.BO_DE_ID = A.BO_DE_ID AND B.STATUS=?),\n");
		sbSql.append("         A.UPDATE_BY = ?,\n");
		sbSql.append("         A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE   A.BO_ID = ?\n");
		List<Object> list1 = new ArrayList<Object>();
		list1.add(Constant.STATUS_ENABLE);
		list1.add(userId);
		list1.add(boId);
		factory.update(sbSql.toString(), list1);//更新组板明细配车数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_VS_ORDER_DETAIL A\n");
		sbSql.append("   SET (A.BOARD_NUMBER, A.MATCH_AMOUNT) =\n");
		sbSql.append("       (SELECT NVL(SUM(NVL(B.BOARD_NUM, 0)), 0),\n");
		sbSql.append("               NVL(SUM(NVL(B.ALLOCA_NUM, 0)), 0)\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL B\n");
		sbSql.append("         WHERE A.DETAIL_ID = B.OR_DE_ID\n");
		sbSql.append("           AND B.IS_ENABLE = ?),\n");
		sbSql.append("       A.UPDATE_BY = ?,\n");
		sbSql.append("       A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE EXISTS (SELECT C.BO_ID\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL C\n");
		sbSql.append("         WHERE C.OR_DE_ID = A.DETAIL_ID\n");
		sbSql.append("           AND C.BO_ID = ?)");
		List<Object> list2 = new ArrayList<Object>();
		list2.add(Constant.IF_TYPE_YES);//有效  因涉及到组板删除 增加 有效无效状态
		list2.add(userId);
		list2.add(boId);
		factory.update(sbSql.toString(), list2);//更新订单明细表组板数量,配车数量
		
		sbSql.delete(0, sbSql.length());
		
		sbSql.append("UPDATE TT_SALES_BOARD A\n");
		sbSql.append("   SET (A.BO_NUM, A.ALLOCA_NUM) =\n");
		sbSql.append("       (SELECT NVL(SUM(NVL(B.BOARD_NUM, 0)), 0),\n");
		sbSql.append("               NVL(SUM(NVL(B.ALLOCA_NUM, 0)), 0)\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL B\n");
		sbSql.append("         WHERE B.BO_ID = A.BO_ID\n");
		sbSql.append("           AND B.IS_ENABLE = ?),\n");
		sbSql.append("       A.UPDATE_BY = ?,\n");
		sbSql.append("       A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE A.BO_ID = ?");
		List<Object> list3 = new ArrayList<Object>();
		list3.add(Constant.IF_TYPE_YES);
		list3.add(userId);
		list3.add(boId);
		factory.update(sbSql.toString(), list3);//更新组板表组板数量，配车数量
		
//		sbSql.delete(0, sbSql.length());
//		sbSql.append("UPDATE TT_SALES_ASSIGN A\n");
//		sbSql.append("   SET (A.BOARD_NUM, A.ALLOCA_NUM) =\n");
//		sbSql.append("       (SELECT NVL(SUM(NVL(B.BO_NUM, 0)), 0),\n");
//		sbSql.append("               NVL(SUM(NVL(B.ALLOCA_NUM, 0)), 0)\n");
//		sbSql.append("          FROM TT_SALES_BOARD B\n");
//		sbSql.append("         WHERE A.ASS_ID = B.ASS_ID\n");
//		sbSql.append("           AND B.IS_ENABLE = ?),\n");
//		sbSql.append("       A.UPDATE_BY = ?,\n");
//		sbSql.append("       A.UPDATE_DATE = SYSDATE\n");
//		sbSql.append(" WHERE A.ASS_ID = (SELECT C.ASS_ID FROM TT_SALES_BOARD C WHERE C.BO_ID = ?)");
//		List<Object> list4 = new ArrayList<Object>();
//		list4.add(Constant.IF_TYPE_YES);
//		list4.add(userId);
//		list4.add(boId);
//		factory.update(sbSql.toString(), list4);//更新发运分派表	组板数量，配车数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE   TT_SALES_ASSIGN A\n");
		sbSql.append("   SET\n");
		sbSql.append("         (A.BOARD_NUM,\n");
		sbSql.append("         A.ALLOCA_NUM\n");
		sbSql.append("         ) =\n");
		sbSql.append("            (SELECT   NVL (SUM (NVL (B.BOARD_NUM, 0)), 0),\n");
		sbSql.append("                      NVL (SUM (NVL (B.ALLOCA_NUM, 0)), 0)\n");
		sbSql.append("               FROM   TT_SALES_BO_DETAIL B\n");
		sbSql.append("              WHERE   B.ORDER_ID = A.ORDER_ID AND B.IS_ENABLE=?),\n");
		sbSql.append("         A.UPDATE_BY = ?,\n");
		sbSql.append("         A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE   EXISTS\n");
		sbSql.append("            (SELECT   1\n");
		sbSql.append("               FROM   TT_SALES_BOARD C, TT_SALES_BO_DETAIL E\n");
		sbSql.append("              WHERE       C.BO_ID = E.BO_ID\n");
		sbSql.append("                      AND E.ORDER_ID = A.ORDER_ID\n");
		sbSql.append("                      AND C.BO_ID = ?)\n");
		List<Object> list4 = new ArrayList<Object>();
		list4.add(Constant.IF_TYPE_YES);
		list4.add(userId);
		list4.add(boId);
		factory.update(sbSql.toString(), list4);//更新发运分派表	组板数量，配车数量

		
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_SALES_ASSIGN TSA\n");
		sbSql.append("   SET (TSA.BO_STATUS) =\n");
		sbSql.append("       (SELECT CASE\n");
		sbSql.append("                 WHEN TSB.BOARD_NUM = 0 THEN\n");
		sbSql.append("                  ?\n");
		sbSql.append("                 WHEN NVL(TSB.BOARD_NUM, 0) = NVL(TSB.ORDER_NUM, 0) THEN\n");
		sbSql.append("                  ?\n");
		sbSql.append("                 ELSE\n");
		sbSql.append("                  ?\n");
		sbSql.append("               END BO_STATUS\n");
		sbSql.append("          FROM TT_SALES_ASSIGN TSB\n");
		sbSql.append("         WHERE TSA.ASS_ID = TSB.ASS_ID\n");
		sbSql.append("           ),\n");
		sbSql.append("       TSA.UPDATE_BY = ?,\n");
		sbSql.append("       TSA.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE   EXISTS\n");
		sbSql.append("            (SELECT   1\n");
		sbSql.append("               FROM   TT_SALES_BOARD C, TT_SALES_BO_DETAIL E\n");
		sbSql.append("              WHERE       C.BO_ID = E.BO_ID\n");
		sbSql.append("                      AND E.ORDER_ID = TSA.ORDER_ID\n");
		sbSql.append("                      AND C.BO_ID = ?)\n");
		List<Object> list5 = new ArrayList<Object>();
		list5.add(Constant.BO_STATUS01);
		list5.add(Constant.BO_STATUS03);
		list5.add(Constant.BO_STATUS02);
		list5.add(userId);
		list5.add(boId);
		factory.update(sbSql.toString(), list5);//更改分派表组板状态
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_SALES_BOARD TSB\n");
		sbSql.append("   SET (TSB.HANDLE_STATUS) =\n");
		sbSql.append("       (SELECT CASE\n");
		sbSql.append("                 WHEN NVL(TSB1.ALLOCA_NUM, 0) = 0 THEN\n");
		sbSql.append("                  ?\n");
		sbSql.append("                 WHEN NVL(TSB1.ALLOCA_NUM, 0) = NVL(TSB1.BO_NUM, 0) THEN\n");
		sbSql.append("                  ?\n");
		sbSql.append("                 ELSE\n");
		sbSql.append("                  ?\n");
		sbSql.append("               END HANDLE_STATUS\n");
		sbSql.append("          FROM TT_SALES_BOARD TSB1\n");
		sbSql.append("         WHERE TSB1.BO_ID = ?),\n");
		sbSql.append("       TSB.UPDATE_BY = ?,TSB.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE TSB.BO_ID = ?");
		List<Object> list6 = new ArrayList<Object>();
		list6.add(Constant.HANDLE_STATUS01);
		list6.add(Constant.HANDLE_STATUS03);
		list6.add(Constant.HANDLE_STATUS02);
		list6.add(boId);
		list6.add(userId);
		list6.add(boId);
		factory.update(sbSql.toString(), list6);//更改组板表处理状态
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_VS_ORDER_RESOURCE_RESERVE A\n");
		sbSql.append("   SET A.ALLOCA_NUM =\n");
		sbSql.append("       (SELECT COUNT(E.VEHICLE_ID) ALLOCA_NUM\n");
		sbSql.append("          FROM TT_VS_ORDER_DETAIL B,\n");
		sbSql.append("               TT_SALES_BO_DETAIL C,\n");
		sbSql.append("               TT_SALES_ALLOCA_DE D,\n");
		sbSql.append("               TM_VEHICLE         E,\n");
		sbSql.append("               TM_PLAN_DETAIL     F\n");
		sbSql.append("         WHERE B.DETAIL_ID = C.OR_DE_ID\n");
		sbSql.append("           AND C.BO_DE_ID = D.BO_DE_ID\n");
		sbSql.append("           AND D.VEHICLE_ID = E.VEHICLE_ID\n");
		sbSql.append("           AND E.plan_detail_id = F.plan_detail_id\n");
		sbSql.append("           AND B.DETAIL_ID = A.ORDER_DETAIL_ID\n");
		sbSql.append("           AND F.ORG_ID = A.ORG_ID\n");
		sbSql.append("           AND C.IS_ENABLE = ?\n");
		sbSql.append("           AND D.STATUS = ?\n");
		sbSql.append("         GROUP BY F.ORG_ID, B.DETAIL_ID),\n");
		sbSql.append("       A.UPDATE_BY   = ?,\n");
		sbSql.append("       A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE EXISTS (SELECT E.BO_ID\n");
		sbSql.append("          FROM TT_VS_ORDER_DETAIL D, TT_SALES_BO_DETAIL E\n");
		sbSql.append("         WHERE D.DETAIL_ID = E.OR_DE_ID\n");
		sbSql.append("           AND D.DETAIL_ID = A.ORDER_DETAIL_ID\n");
		sbSql.append("           AND E.BO_ID = ?)");
		List<Object> list7 = new ArrayList<Object>();
		list7.add(Constant.IF_TYPE_YES);
		list7.add(Constant.STATUS_ENABLE);
		list7.add(userId);
		list7.add(boId);
		factory.update(sbSql.toString(), list7);//更改预留表配车数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_SA_ORDER_DETAIL A\r\n");
		sbSql.append("   SET (A.BOARD_NUMBER, A.MATCH_AMOUNT) =\r\n");
		sbSql.append("       (SELECT NVL(SUM(NVL(B.BOARD_NUM, 0)), 0),\r\n");
		sbSql.append("               NVL(SUM(NVL(B.ALLOCA_NUM, 0)), 0)\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL B, TT_VS_ORDER_DETAIL C\r\n");
		sbSql.append("         WHERE A.DETAIL_ID = C.WR_DETAIL_ID\r\n");
		sbSql.append("           AND B.OR_DE_ID = C.DETAIL_ID\r\n");
		sbSql.append("           AND B.IS_ENABLE = ?),\r\n");
		sbSql.append("       A.UPDATE_BY = ?,\r\n");
		sbSql.append("       A.UPDATE_DATE = SYSDATE\r\n");
		sbSql.append(" WHERE EXISTS (SELECT D.BO_ID\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL D, TT_VS_ORDER_DETAIL E\r\n");
		sbSql.append("         WHERE A.DETAIL_ID = E.WR_DETAIL_ID\r\n");
		sbSql.append("           AND D.OR_DE_ID = E.DETAIL_ID\r\n");
		sbSql.append("           AND D.BO_ID = ?)"); 

		List<Object> list8 = new ArrayList<Object>();
		list8.add(Constant.IF_TYPE_YES);
		list8.add(userId);
		list8.add(boId);
		factory.update(sbSql.toString(), list8);//更改需求计划明细表组板,配车表数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_SA_ORDER A\r\n");
		sbSql.append("   SET (A.BO_NUM, A.ASS_NUM) =\r\n");
		sbSql.append("       (SELECT NVL(SUM(NVL(B.BOARD_NUM, 0)), 0),\r\n");
		sbSql.append("               NVL(SUM(NVL(B.ALLOCA_NUM, 0)), 0)\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL B,\r\n");
		sbSql.append("               TT_VS_ORDER_DETAIL C,\r\n");
		sbSql.append("               TT_SA_ORDER_DETAIL X\r\n");
		sbSql.append("         WHERE X.DETAIL_ID = C.WR_DETAIL_ID\r\n");
		sbSql.append("           AND B.OR_DE_ID = C.DETAIL_ID\r\n");
		sbSql.append("           AND A.ORDER_ID = X.ORDER_ID\r\n");
		sbSql.append("           AND B.IS_ENABLE = ?),\r\n");
		sbSql.append("       A.UPDATE_BY = ?,\r\n");
		sbSql.append("       A.UPDATE_DATE = SYSDATE\r\n");
		sbSql.append(" WHERE EXISTS (SELECT D.BO_ID\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL D,\r\n");
		sbSql.append("               TT_VS_ORDER_DETAIL E,\r\n");
		sbSql.append("               TT_SA_ORDER_DETAIL Y\r\n");
		sbSql.append("         WHERE Y.DETAIL_ID = E.WR_DETAIL_ID\r\n");
		sbSql.append("           AND D.OR_DE_ID = E.DETAIL_ID\r\n");
		sbSql.append("           AND A.ORDER_ID = Y.ORDER_ID\r\n");
		sbSql.append("           AND D.BO_ID = ?)"); 
		List<Object> list9 = new ArrayList<Object>();
		list9.add(Constant.IF_TYPE_YES);
		list9.add(userId);
		list9.add(boId);
		factory.update(sbSql.toString(), list9);//更改需求计划表组板,配车数量
	}
	
	/**
	 * 出库数量更新
	 * 
	 * @param boId
	 * @param userId
	 */
	public void updateOutStorgeNum(Long boId, Long userId)
	{//
	
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE   TT_SALES_BO_DETAIL A\n");
		sbSql.append("   SET   A.OUT_NUM =\n");
		sbSql.append("            (SELECT   COUNT (1)\n");
		sbSql.append("               FROM   TT_SALES_ALLOCA_DE B\n");
		sbSql.append("              WHERE   B.BO_DE_ID = A.BO_DE_ID AND B.STATUS=? AND B.IS_OUT=?),\n");
		sbSql.append("         A.UPDATE_BY = ?,\n");
		sbSql.append("         A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE   A.BO_ID = ?\n");
		List<Object> list = new ArrayList<Object>();
		list.add(Constant.STATUS_ENABLE);
		list.add(Constant.IF_TYPE_YES);
		list.add(userId);
		list.add(boId);
		factory.update(sbSql.toString(), list);//更新组板明细 出库数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_VS_ORDER_DETAIL A\n");
		sbSql.append("   SET A.OUT_AMOUNT =\n");
		sbSql.append("       (SELECT NVL(SUM(NVL(B.OUT_NUM, 0)), 0)\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL B\n");
		sbSql.append("         WHERE A.DETAIL_ID = B.OR_DE_ID\n");
		sbSql.append("           AND B.IS_ENABLE = ?),\n");
		sbSql.append("       A.UPDATE_BY = ?,\n");
		sbSql.append("       A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE EXISTS (SELECT C.BO_ID\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL C\n");
		sbSql.append("         WHERE C.OR_DE_ID = A.DETAIL_ID\n");
		sbSql.append("           AND C.BO_ID = ?)");
		List<Object> list2 = new ArrayList<Object>();
		list2.add(Constant.IF_TYPE_YES);//有效  因涉及到组板删除 增加 有效无效状态
		list2.add(userId);
		list2.add(boId);
		factory.update(sbSql.toString(), list2);//更新订单明细表出库数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE   TT_SALES_BOARD A\n");
		sbSql.append("   SET   A.OUT_NUM =\n");
		sbSql.append("            (SELECT   NVL (SUM (NVL (B.OUT_NUM, 0)), 0)\n");
		sbSql.append("               FROM   TT_SALES_BO_DETAIL B\n");
		sbSql.append("              WHERE   B.BO_ID = A.BO_ID AND B.IS_ENABLE=?),\n");
		sbSql.append("         A.UPDATE_BY = ?,\n");
		sbSql.append("         A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE   A.BO_ID = ?\n");
		List<Object> list3 = new ArrayList<Object>();
		list3.add(Constant.IF_TYPE_YES);
		list3.add(userId);
		list3.add(boId);
		factory.update(sbSql.toString(), list3);//更新组板表 出库数量
	
//		sbSql.delete(0, sbSql.length());
//		sbSql.append("UPDATE   TT_SALES_ASSIGN A\n");
//		sbSql.append("   SET   A.OUT_NUM =\n");
//		sbSql.append("            (SELECT   NVL (SUM (NVL (B.OUT_NUM, 0)), 0)\n");
//		sbSql.append("               FROM   TT_SALES_BOARD B\n");
//		sbSql.append("              WHERE   B.ASS_ID = A.ASS_ID AND B.IS_ENABLE=?),\n");
//		sbSql.append("         A.UPDATE_BY = ?,\n");
//		sbSql.append("         A.UPDATE_DATE = SYSDATE\n");
//		sbSql.append(" WHERE   A.ASS_ID = (SELECT   C.ASS_ID\n");
//		sbSql.append("                       FROM   TT_SALES_BOARD C\n");
//		sbSql.append("                      WHERE   C.BO_ID = ?)\n");
//		sbSql.append(" \n");
//		List<Object> list4 = new ArrayList<Object>();
//		list4.add(Constant.IF_TYPE_YES);
//		list4.add(userId);
//		list4.add(boId);
//		factory.update(sbSql.toString(), list4);//更新发运分派表	出库数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE   TT_SALES_ASSIGN A\n");
		sbSql.append("   SET\n");
		sbSql.append("         A.OUT_NUM=\n");
		sbSql.append("            (SELECT   \n");
		sbSql.append("                      NVL (SUM (NVL (B.OUT_NUM, 0)), 0)\n");
		sbSql.append("               FROM   TT_SALES_BO_DETAIL B\n");
		sbSql.append("              WHERE   B.ORDER_ID = A.ORDER_ID AND B.IS_ENABLE=?),\n");
		sbSql.append("         A.UPDATE_BY = ?,\n");
		sbSql.append("         A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE   EXISTS\n");
		sbSql.append("            (SELECT   1\n");
		sbSql.append("               FROM   TT_SALES_BOARD C, TT_SALES_BO_DETAIL E\n");
		sbSql.append("              WHERE       C.BO_ID = E.BO_ID\n");
		sbSql.append("                      AND E.ORDER_ID = A.ORDER_ID\n");
		sbSql.append("                      AND C.BO_ID = ?)\n");
		List<Object> list4 = new ArrayList<Object>();
		list4.add(Constant.IF_TYPE_YES);
		list4.add(userId);
		list4.add(boId);
		factory.update(sbSql.toString(), list4);//更新发运分派表	出库数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_SA_ORDER_DETAIL A\r\n");
		sbSql.append("   SET A.OUT_AMOUNT=\r\n");
		sbSql.append("       (SELECT NVL(SUM(NVL(B.OUT_NUM, 0)), 0)\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL B, TT_VS_ORDER_DETAIL C\r\n");
		sbSql.append("         WHERE A.DETAIL_ID = C.WR_DETAIL_ID\r\n");
		sbSql.append("           AND B.OR_DE_ID = C.DETAIL_ID\r\n");
		sbSql.append("           AND B.IS_ENABLE = ?),\r\n");
		sbSql.append("       A.UPDATE_BY = ?,\r\n");
		sbSql.append("       A.UPDATE_DATE = SYSDATE\r\n");
		sbSql.append(" WHERE EXISTS (SELECT D.BO_ID\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL D, TT_VS_ORDER_DETAIL E\r\n");
		sbSql.append("         WHERE A.DETAIL_ID = E.WR_DETAIL_ID\r\n");
		sbSql.append("           AND D.OR_DE_ID = E.DETAIL_ID\r\n");
		sbSql.append("           AND D.BO_ID = ?)"); 

		List<Object> list8 = new ArrayList<Object>();
		list8.add(Constant.IF_TYPE_YES);
		list8.add(userId);
		list8.add(boId);
		factory.update(sbSql.toString(), list8);//更改需求计划明细表出库表数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_SA_ORDER A\r\n");
		sbSql.append("   SET A.OUT_NUM =\r\n");
		sbSql.append("       (SELECT NVL(SUM(NVL(B.OUT_NUM, 0)), 0)\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL B,\r\n");
		sbSql.append("               TT_VS_ORDER_DETAIL C,\r\n");
		sbSql.append("               TT_SA_ORDER_DETAIL X\r\n");
		sbSql.append("         WHERE X.DETAIL_ID = C.WR_DETAIL_ID\r\n");
		sbSql.append("           AND B.OR_DE_ID = C.DETAIL_ID\r\n");
		sbSql.append("           AND A.ORDER_ID = X.ORDER_ID\r\n");
		sbSql.append("           AND B.IS_ENABLE = ?),\r\n");
		sbSql.append("       A.UPDATE_BY = ?,\r\n");
		sbSql.append("       A.UPDATE_DATE = SYSDATE\r\n");
		sbSql.append(" WHERE EXISTS (SELECT D.BO_ID\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL D,\r\n");
		sbSql.append("               TT_VS_ORDER_DETAIL E,\r\n");
		sbSql.append("               TT_SA_ORDER_DETAIL Y\r\n");
		sbSql.append("         WHERE Y.DETAIL_ID = E.WR_DETAIL_ID\r\n");
		sbSql.append("           AND D.OR_DE_ID = E.DETAIL_ID\r\n");
		sbSql.append("           AND A.ORDER_ID = Y.ORDER_ID\r\n");
		sbSql.append("           AND D.BO_ID = ?)"); 
		List<Object> list9 = new ArrayList<Object>();
		list9.add(Constant.IF_TYPE_YES);
		list9.add(userId);
		list9.add(boId);
		factory.update(sbSql.toString(), list9);//更改需求计划表出库数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_SALES_BOARD TSB\n");
		sbSql.append("   SET (TSB.HANDLE_STATUS) =\n");
		sbSql.append("       (SELECT CASE\n");
		sbSql.append("                 WHEN NVL(TSB1.OUT_NUM, 0) = 0 THEN\n");
		sbSql.append("                  ?\n");
		sbSql.append("                 WHEN NVL(TSB1.ALLOCA_NUM, 0) = NVL(TSB1.OUT_NUM, 0) THEN\n");
		sbSql.append("                  ?\n");
		sbSql.append("                 ELSE\n");
		sbSql.append("                  ?\n");
		sbSql.append("               END HANDLE_STATUS\n");
		sbSql.append("          FROM TT_SALES_BOARD TSB1\n");
		sbSql.append("         WHERE TSB1.BO_ID = ?),\n");
		sbSql.append("       TSB.UPDATE_BY = ?,TSB.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE TSB.BO_ID = ?");
		List<Object> list6 = new ArrayList<Object>();
		list6.add(Constant.HANDLE_STATUS03);//完全配车=未出库
		list6.add(Constant.HANDLE_STATUS05);
		list6.add(Constant.HANDLE_STATUS04);
		list6.add(boId);
		list6.add(userId);
		list6.add(boId);
		factory.update(sbSql.toString(), list6);//更改组板表处理状态
		
		
	}
	
	/**
	 * 发运数量更新
	 * 
	 * @param boId
	 * @param userId
	 */
	public void updateSendNum(Long boId, Long userId)
	{//
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE   TT_SALES_BO_DETAIL A\n");
		sbSql.append("   SET   A.SEND_NUM =\n");
		sbSql.append("            (SELECT   COUNT (1)\n");
		sbSql.append("               FROM   TT_SALES_ALLOCA_DE B\n");
		sbSql.append("              WHERE   B.BO_DE_ID = A.BO_DE_ID AND B.STATUS=? AND B.IS_SEND=?),\n");
		sbSql.append("         A.UPDATE_BY = ?,\n");
		sbSql.append("         A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE   A.BO_ID = ?\n");
		List<Object> list = new ArrayList<Object>();
		list.add(Constant.STATUS_ENABLE);
		list.add(Constant.IF_TYPE_YES);
		list.add(userId);
		list.add(boId);
		factory.update(sbSql.toString(), list);//更新组板明细配车发运数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_VS_ORDER_DETAIL A\n");
		sbSql.append("   SET A.DELIVERY_AMOUNT =\n");
		sbSql.append("       (SELECT NVL(SUM(NVL(B.SEND_NUM, 0)), 0)\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL B\n");
		sbSql.append("         WHERE A.DETAIL_ID = B.OR_DE_ID\n");
		sbSql.append("           AND B.IS_ENABLE = ?),\n");
		sbSql.append("       A.UPDATE_BY       = ?,\n");
		sbSql.append("       A.UPDATE_DATE     = SYSDATE\n");
		sbSql.append(" WHERE EXISTS (SELECT C.BO_ID\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL C\n");
		sbSql.append("         WHERE C.OR_DE_ID = A.DETAIL_ID\n");
		sbSql.append("           AND C.BO_ID = ?)");
		List<Object> list2 = new ArrayList<Object>();
		list2.add(Constant.IF_TYPE_YES);
		list2.add(userId);
		list2.add(boId);
		factory.update(sbSql.toString(), list2);//更新订单明细表组板发运数量
		
		sbSql.delete(0, sbSql.length());
		
		sbSql.append("UPDATE   TT_SALES_BOARD A\n");
		sbSql.append("   SET   A.SEND_NUM =\n");
		sbSql.append("            (SELECT   NVL (SUM (NVL (B.SEND_NUM, 0)), 0)\n");
		sbSql.append("               FROM   TT_SALES_BO_DETAIL B\n");
		sbSql.append("              WHERE   B.BO_ID = A.BO_ID AND B.IS_ENABLE=?),\n");
		sbSql.append("         A.UPDATE_BY = ?,\n");
		sbSql.append("         A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE   A.BO_ID = ?\n");
		List<Object> list3 = new ArrayList<Object>();
		list3.add(Constant.IF_TYPE_YES);
		list3.add(userId);
		list3.add(boId);
		factory.update(sbSql.toString(), list3);//更新组板表 发运数量
		
//		sbSql.delete(0, sbSql.length());
//		sbSql.append("UPDATE   TT_SALES_ASSIGN A\n");
//		sbSql.append("   SET   A.SEND_NUM =\n");
//		sbSql.append("            (SELECT   NVL (SUM (NVL (B.SEND_NUM, 0)), 0)\n");
//		sbSql.append("               FROM   TT_SALES_BOARD B\n");
//		sbSql.append("              WHERE   B.ASS_ID = A.ASS_ID AND B.IS_ENABLE=?),\n");
//		sbSql.append("         A.UPDATE_BY = ?,\n");
//		sbSql.append("         A.UPDATE_DATE = SYSDATE\n");
//		sbSql.append(" WHERE   A.ASS_ID = (SELECT   C.ASS_ID\n");
//		sbSql.append("                       FROM   TT_SALES_BOARD C\n");
//		sbSql.append("                      WHERE   C.BO_ID = ?)\n");
//		sbSql.append(" \n");
//		List<Object> list4 = new ArrayList<Object>();
//		list4.add(Constant.IF_TYPE_YES);
//		list4.add(userId);
//		list4.add(boId);
//		factory.update(sbSql.toString(), list4);//更新发运分派表	发运数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE   TT_SALES_ASSIGN A\n");
		sbSql.append("   SET\n");
		sbSql.append("         A.SEND_NUM =\n");
		sbSql.append("            (SELECT   \n");
		sbSql.append("                      NVL (SUM (NVL (B.SEND_NUM, 0)), 0)\n");
		sbSql.append("               FROM   TT_SALES_BO_DETAIL B\n");
		sbSql.append("              WHERE   B.ORDER_ID = A.ORDER_ID AND B.IS_ENABLE=?),\n");
		sbSql.append("         A.UPDATE_BY = ?,\n");
		sbSql.append("         A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE   EXISTS\n");
		sbSql.append("            (SELECT   1\n");
		sbSql.append("               FROM   TT_SALES_BOARD C, TT_SALES_BO_DETAIL E\n");
		sbSql.append("              WHERE       C.BO_ID = E.BO_ID\n");
		sbSql.append("                      AND E.ORDER_ID = A.ORDER_ID\n");
		sbSql.append("                      AND C.BO_ID = ?)\n");
		List<Object> list4 = new ArrayList<Object>();
		list4.add(Constant.IF_TYPE_YES);
		list4.add(userId);
		list4.add(boId);
		factory.update(sbSql.toString(), list4);//更新发运分派表	发运数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_SA_ORDER_DETAIL A\r\n");
		sbSql.append("   SET A.DELIVERY_AMOUNT=\r\n");
		sbSql.append("       (SELECT NVL(SUM(NVL(B.SEND_NUM, 0)), 0)\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL B, TT_VS_ORDER_DETAIL C\r\n");
		sbSql.append("         WHERE A.DETAIL_ID = C.WR_DETAIL_ID\r\n");
		sbSql.append("           AND B.OR_DE_ID = C.DETAIL_ID\r\n");
		sbSql.append("           AND B.IS_ENABLE = ?),\r\n");
		sbSql.append("       A.UPDATE_BY = ?,\r\n");
		sbSql.append("       A.UPDATE_DATE = SYSDATE\r\n");
		sbSql.append(" WHERE EXISTS (SELECT D.BO_ID\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL D, TT_VS_ORDER_DETAIL E\r\n");
		sbSql.append("         WHERE A.DETAIL_ID = E.WR_DETAIL_ID\r\n");
		sbSql.append("           AND D.OR_DE_ID = E.DETAIL_ID\r\n");
		sbSql.append("           AND D.BO_ID = ?)"); 

		List<Object> list8 = new ArrayList<Object>();
		list8.add(Constant.IF_TYPE_YES);
		list8.add(userId);
		list8.add(boId);
		factory.update(sbSql.toString(), list8);//更改需求计划明细表发运数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_SA_ORDER A\r\n");
		sbSql.append("   SET A.SEND_NUM =\r\n");
		sbSql.append("       (SELECT NVL(SUM(NVL(B.SEND_NUM, 0)), 0)\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL B,\r\n");
		sbSql.append("               TT_VS_ORDER_DETAIL C,\r\n");
		sbSql.append("               TT_SA_ORDER_DETAIL X\r\n");
		sbSql.append("         WHERE X.DETAIL_ID = C.WR_DETAIL_ID\r\n");
		sbSql.append("           AND B.OR_DE_ID = C.DETAIL_ID\r\n");
		sbSql.append("           AND A.ORDER_ID = X.ORDER_ID\r\n");
		sbSql.append("           AND B.IS_ENABLE = ?),\r\n");
		sbSql.append("       A.UPDATE_BY = ?,\r\n");
		sbSql.append("       A.UPDATE_DATE = SYSDATE\r\n");
		sbSql.append(" WHERE EXISTS (SELECT D.BO_ID\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL D,\r\n");
		sbSql.append("               TT_VS_ORDER_DETAIL E,\r\n");
		sbSql.append("               TT_SA_ORDER_DETAIL Y\r\n");
		sbSql.append("         WHERE Y.DETAIL_ID = E.WR_DETAIL_ID\r\n");
		sbSql.append("           AND D.OR_DE_ID = E.DETAIL_ID\r\n");
		sbSql.append("           AND A.ORDER_ID = Y.ORDER_ID\r\n");
		sbSql.append("           AND D.BO_ID = ?)"); 
		List<Object> list9 = new ArrayList<Object>();
		list9.add(Constant.IF_TYPE_YES);
		list9.add(userId);
		list9.add(boId);
		factory.update(sbSql.toString(), list9);//更改需求计划表发运数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_SALES_BOARD TSB\n");
		sbSql.append("   SET (TSB.HANDLE_STATUS) =\n");
		sbSql.append("       (SELECT CASE\n");
		sbSql.append("                 WHEN NVL(TSB1.SEND_NUM, 0) = 0 THEN\n");
		sbSql.append("                  ?\n");
//		sbSql.append("                 WHEN NVL(TSB1.SEND_NUM, 0) > 0 AND\n");
//		sbSql.append("                      NVL(TSB1.SEND_NUM, 0) < NVL(TSB1.BO_NUM, 0) THEN\n");
//		sbSql.append("                  ?\n");
		sbSql.append("                 ELSE\n");
		sbSql.append("                  ?\n");
		sbSql.append("               END HANDLE_STATUS\n");
		sbSql.append("          FROM TT_SALES_BOARD TSB1\n");
		sbSql.append("         WHERE TSB1.BO_ID = ?),\n");
		sbSql.append("       TSB.UPDATE_BY = ?,\n");
		sbSql.append("       TSB.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE TSB.BO_ID = ?"); 
		List<Object> list5 = new ArrayList<Object>();
		list5.add(Constant.HANDLE_STATUS05);
		//list5.add(Constant.HANDLE_STATUS09);
		list5.add(Constant.HANDLE_STATUS06);
		list5.add(boId);
		list5.add(userId);
		list5.add(boId);
		factory.update(sbSql.toString(), list5);//更改组板表处理状态
		
		
	}
	
	/**
	 * 验收数量更新
	 * 
	 * @param boId
	 * @param userId
	 */
	public void updateAcceptNum(Long boId, Long userId)
	{//
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE   TT_SALES_BO_DETAIL A\n");
		sbSql.append("   SET   A.ACC_NUM =\n");
		sbSql.append("            (SELECT   COUNT (1)\n");
		sbSql.append("               FROM   TT_SALES_ALLOCA_DE B\n");
		sbSql.append("              WHERE   B.BO_DE_ID = A.BO_DE_ID AND B.STATUS=? AND B.IS_ACC=?),\n");
		sbSql.append("         A.UPDATE_BY = ?,\n");
		sbSql.append("         A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE   A.BO_ID = ?\n");
		List<Object> list = new ArrayList<Object>();
		list.add(Constant.STATUS_ENABLE);
		list.add(Constant.IF_TYPE_YES);
		list.add(userId);
		list.add(boId);
		factory.update(sbSql.toString(), list);//更新组板明细验收数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_VS_ORDER_DETAIL A\n");
		sbSql.append("   SET A.ACC_AMOUNT =\n");
		sbSql.append("       (SELECT NVL(SUM(NVL(B.ACC_NUM, 0)), 0)\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL B\n");
		sbSql.append("         WHERE A.DETAIL_ID = B.OR_DE_ID\n");
		sbSql.append("           AND B.IS_ENABLE = ?),\n");
		sbSql.append("       A.UPDATE_BY   = ?,\n");
		sbSql.append("       A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE EXISTS (SELECT C.BO_ID\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL C\n");
		sbSql.append("         WHERE C.OR_DE_ID = A.DETAIL_ID\n");
		sbSql.append("           AND C.BO_ID = ?)");
		List<Object> list2 = new ArrayList<Object>();
		list2.add(Constant.IF_TYPE_YES);
		list2.add(userId);
		list2.add(boId);
		factory.update(sbSql.toString(), list2);//更新订单明细表验收数量
		
		sbSql.delete(0, sbSql.length());
		
		sbSql.append("UPDATE   TT_SALES_BOARD A\n");
		sbSql.append("   SET   A.ACC_NUM =\n");
		sbSql.append("            (SELECT   NVL (SUM (NVL (B.ACC_NUM, 0)), 0)\n");
		sbSql.append("               FROM   TT_SALES_BO_DETAIL B\n");
		sbSql.append("              WHERE   B.BO_ID = A.BO_ID AND B.IS_ENABLE=?),\n");
		sbSql.append("         A.UPDATE_BY = ?,\n");
		sbSql.append("         A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE   A.BO_ID = ?\n");
		List<Object> list3 = new ArrayList<Object>();
		list3.add(Constant.IF_TYPE_YES);
		list3.add(userId);
		list3.add(boId);
		factory.update(sbSql.toString(), list3);//更新组板表验收数量
		
//		sbSql.delete(0, sbSql.length());
//		sbSql.append("UPDATE   TT_SALES_ASSIGN A\n");
//		sbSql.append("   SET   A.ACC_NUM =\n");
//		sbSql.append("            (SELECT   NVL (SUM (NVL (B.ACC_NUM, 0)), 0)\n");
//		sbSql.append("               FROM   TT_SALES_BOARD B\n");
//		sbSql.append("              WHERE   B.ASS_ID = A.ASS_ID AND B.IS_ENABLE=?),\n");
//		sbSql.append("         A.UPDATE_BY = ?,\n");
//		sbSql.append("         A.UPDATE_DATE = SYSDATE\n");
//		sbSql.append(" WHERE   A.ASS_ID = (SELECT   C.ASS_ID\n");
//		sbSql.append("                       FROM   TT_SALES_BOARD C\n");
//		sbSql.append("                      WHERE   C.BO_ID = ?)\n");
//		sbSql.append(" \n");
//		List<Object> list4 = new ArrayList<Object>();
//		list4.add(Constant.IF_TYPE_YES);
//		list4.add(userId);
//		list4.add(boId);
//		factory.update(sbSql.toString(), list4);//更新发运分派表	验收数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE   TT_SALES_ASSIGN A\n");
		sbSql.append("   SET\n");
		sbSql.append("         A.ACC_NUM =\n");
		sbSql.append("            (SELECT   \n");
		sbSql.append("                      NVL (SUM (NVL (B.ACC_NUM, 0)), 0)\n");
		sbSql.append("               FROM   TT_SALES_BO_DETAIL B\n");
		sbSql.append("              WHERE   B.ORDER_ID = A.ORDER_ID AND B.IS_ENABLE=?),\n");
		sbSql.append("         A.UPDATE_BY = ?,\n");
		sbSql.append("         A.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE   EXISTS\n");
		sbSql.append("            (SELECT   1\n");
		sbSql.append("               FROM   TT_SALES_BOARD C, TT_SALES_BO_DETAIL E\n");
		sbSql.append("              WHERE       C.BO_ID = E.BO_ID\n");
		sbSql.append("                      AND E.ORDER_ID = A.ORDER_ID\n");
		sbSql.append("                      AND C.BO_ID = ?)\n");
		List<Object> list4 = new ArrayList<Object>();
		list4.add(Constant.IF_TYPE_YES);
		list4.add(userId);
		list4.add(boId);
		factory.update(sbSql.toString(), list4);//更新发运分派表	验收数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_SA_ORDER_DETAIL A\r\n");
		sbSql.append("   SET A.ACC_AMOUNT=\r\n");
		sbSql.append("       (SELECT NVL(SUM(NVL(B.ACC_NUM, 0)), 0)\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL B, TT_VS_ORDER_DETAIL C\r\n");
		sbSql.append("         WHERE A.DETAIL_ID = C.WR_DETAIL_ID\r\n");
		sbSql.append("           AND B.OR_DE_ID = C.DETAIL_ID\r\n");
		sbSql.append("           AND B.IS_ENABLE = ?),\r\n");
		sbSql.append("       A.UPDATE_BY = ?,\r\n");
		sbSql.append("       A.UPDATE_DATE = SYSDATE\r\n");
		sbSql.append(" WHERE EXISTS (SELECT D.BO_ID\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL D, TT_VS_ORDER_DETAIL E\r\n");
		sbSql.append("         WHERE A.DETAIL_ID = E.WR_DETAIL_ID\r\n");
		sbSql.append("           AND D.OR_DE_ID = E.DETAIL_ID\r\n");
		sbSql.append("           AND D.BO_ID = ?)"); 

		List<Object> list8 = new ArrayList<Object>();
		list8.add(Constant.IF_TYPE_YES);
		list8.add(userId);
		list8.add(boId);
		factory.update(sbSql.toString(), list8);//更改需求计划明细表验收数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_SA_ORDER A\r\n");
		sbSql.append("   SET A.ACC_NUM =\r\n");
		sbSql.append("       (SELECT NVL(SUM(NVL(B.ACC_NUM, 0)), 0)\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL B,\r\n");
		sbSql.append("               TT_VS_ORDER_DETAIL C,\r\n");
		sbSql.append("               TT_SA_ORDER_DETAIL X\r\n");
		sbSql.append("         WHERE X.DETAIL_ID = C.WR_DETAIL_ID\r\n");
		sbSql.append("           AND B.OR_DE_ID = C.DETAIL_ID\r\n");
		sbSql.append("           AND A.ORDER_ID = X.ORDER_ID\r\n");
		sbSql.append("           AND B.IS_ENABLE = ?),\r\n");
		sbSql.append("       A.UPDATE_BY = ?,\r\n");
		sbSql.append("       A.UPDATE_DATE = SYSDATE\r\n");
		sbSql.append(" WHERE EXISTS (SELECT D.BO_ID\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL D,\r\n");
		sbSql.append("               TT_VS_ORDER_DETAIL E,\r\n");
		sbSql.append("               TT_SA_ORDER_DETAIL Y\r\n");
		sbSql.append("         WHERE Y.DETAIL_ID = E.WR_DETAIL_ID\r\n");
		sbSql.append("           AND D.OR_DE_ID = E.DETAIL_ID\r\n");
		sbSql.append("           AND A.ORDER_ID = Y.ORDER_ID\r\n");
		sbSql.append("           AND D.BO_ID = ?)"); 
		List<Object> list9 = new ArrayList<Object>();
		list9.add(Constant.IF_TYPE_YES);
		list9.add(userId);
		list9.add(boId);
		factory.update(sbSql.toString(), list9);//更改需求计划表验收数量
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_SALES_BOARD TSB\n");
		sbSql.append("   SET (TSB.HANDLE_STATUS) =\n");
		sbSql.append("       (SELECT CASE\n");
		sbSql.append("                 WHEN NVL(TSB1.ACC_NUM, 0) = 0 THEN\n");
		sbSql.append("                  ?\n");
		sbSql.append("                 WHEN NVL(TSB1.ACC_NUM, 0) = NVL(TSB1.SEND_NUM, 0) THEN\n");
		sbSql.append("                  ?\n");
		sbSql.append("                 ELSE\n");
		sbSql.append("                  ?\n");
		sbSql.append("               END HANDLE_STATUS\n");
		sbSql.append("          FROM TT_SALES_BOARD TSB1\n");
		sbSql.append("         WHERE TSB1.BO_ID = ?),\n");
		sbSql.append("       TSB.UPDATE_BY = ?,TSB.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE TSB.BO_ID = ?");
		List<Object> list5 = new ArrayList<Object>();
		list5.add(Constant.HANDLE_STATUS06);//发运完成=未验收
		list5.add(Constant.HANDLE_STATUS08);
		list5.add(Constant.HANDLE_STATUS07);
		list5.add(boId);
		list5.add(userId);
		list5.add(boId);
		factory.update(sbSql.toString(), list5);//更改组板表处理状态
		
		
	}
	
	/**
	 * 取消组板号
	 * 
	 * @param boId
	 */
	public void updateIsEnable(Long boId, Long userId)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE TT_SALES_BO_DETAIL TSBD\n");
		sbSql.append("   SET TSBD.IS_ENABLE = ?, TSBD.UPDATE_BY = ?, TSBD.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE TSBD.BO_ID = ?");
		List<Object> list = new ArrayList<Object>();
		list.add(Constant.IF_TYPE_NO);
		list.add(userId);
		list.add(boId);
		factory.update(sbSql.toString(), list);//修改组板明细表状态
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_SALES_BOARD TSB\n");
		sbSql.append("   SET TSB.IS_ENABLE = ?,TSB.UPDATE_BY = ?,TSB.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE TSB.BO_ID = ?");
		List<Object> list2 = new ArrayList<Object>();
		list2.add(Constant.IF_TYPE_NO);
		list2.add(userId);
		list2.add(boId);
		factory.update(sbSql.toString(), list2);//修改组板表状态
		
		
		
	}
	
	/**
	 * 修改配车时间（由于只记录第一次配车时间，所以单独处理）
	 * 
	 * @param boId
	 */
	public void updateAllocaTime(Long boId, Long userId)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE TT_SALES_BOARD TSB\n");
		sbSql.append("   SET TSB.ALLOCA_PER = ?,TSB.ALLOCA_DATE = SYSDATE\n");
		sbSql.append(" WHERE TSB.BO_ID = ?");
		List<Object> list1 = new ArrayList<Object>();
		list1.add(userId);
		list1.add(boId);
		factory.update(sbSql.toString(), list1);//更改 配车状态（组板表里面）
		
	}
}
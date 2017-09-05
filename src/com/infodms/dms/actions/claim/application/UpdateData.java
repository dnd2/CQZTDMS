package com.infodms.dms.actions.claim.application;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateData {
	public static void main(String[] args) throws Exception{
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			/********1111*******/
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//			con=DriverManager.getConnection("jdbc:oracle:thin:@(description=(address_list=(address=(host=10.0.2.49)(protocol=tcp)(port=1525))(address=(host=10.0.2.45)(protocol=tcp) (port=1525))(load_balance=false)(failover=yes))(connect_data=(service_name= INFODMS)))","CVSDEVDMS","QUERY4WC");
//			StringBuffer sql  = new StringBuffer();
//			StringBuffer sbSql=new StringBuffer();
//			sbSql.append("SELECT MIN(rowid) AS DATESTR ,A.BALANCE_ID \n" );
//			sbSql.append("        FROM TT_AS_WR_BALANCE_AUTHITEM A \n");
//			sbSql.append("       WHERE A.AUTH_STATUS = 11861006 \n");
//			sbSql.append("       GROUP BY A.AUTH_STATUS, A.BALANCE_ID \n");
//			sbSql.append("      HAVING COUNT(*) > 1 \n");
//			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
//			ps=con.prepareStatement(sbSql.toString());
//			rs=ps.executeQuery();
//			while(rs.next()){
//				String date=rs.getString("DATESTR");
//				
//				long balanceId=rs.getLong("BALANCE_ID");
//				sql.append("DELETE FROM tt_as_wr_balance_authitem WHERE BALANCE_ID="+balanceId+" AND AUTH_STATUS = 11861006 AND rowid='"+date+"';\n");
//				System.out.println("DELETE FROM tt_as_wr_balance_authitem WHERE BALANCE_ID="+balanceId+" AND AUTH_STATUS = 11861006 AND rowid='"+date+"';\n");
//			}
			
//			String sql="select cb.id from Tt_As_Wr_Claim_Balance cb where cb.start_date = to_date('2010-12-11', 'YYYY-MM-DD')";
//			ps=con.prepareStatement(sql);
//			rs=ps.executeQuery(sql);
//			StringBuffer sbSql=new StringBuffer();
//			int count=0;
//			while(rs.next()){
//				count++;
//				long balanceId=rs.getLong("ID");
//				sbSql.append("  UPDATE TT_AS_WR_CLAIM_BALANCE_DETAIL D SET D.AFTER_OTHER_AMOUNT= \n" );
//				sbSql.append(" (SELECT SUM(NVL(WA.BALANCE_NETITEM_AMOUNT,0)) \n");
//				sbSql.append("          FROM TT_AS_WR_APPLICATION WA,TR_BALANCE_CLAIM B \n");
//				sbSql.append("         WHERE WA.SERIES_CODE = D.SERIES_CODE \n");
//				sbSql.append("           AND WA.CLAIM_TYPE IN (10661001, 10661009) \n");
//				sbSql.append("           AND WA.ID=B.CLAIM_ID \n");
//				sbSql.append("           AND D.BALANCE_ID=B.BALANCE_ID) \n");
//				sbSql.append("WHERE D.BALANCE_ID="+balanceId+";");
//				sbSql.append("          \n");
//				
//				System.out.println("第:"+count+"条");
//			}
//			   String path = "E:/bbb.sql";
//			   File file = new File(path);
//	
//			   FileWriter writer = new FileWriter(file);
//			   PrintWriter pw = new PrintWriter(writer);
//	
//			   pw.println(sql.toString());
//			   pw.flush();
//			   writer.close();
//			
//			StringBuffer sbSql=new StringBuffer();
//			sbSql.append("  SELECT A.DEALER_ID, A.YIELDLY, A.ID,A.START_DATE, A.ADMIN_DEDUCT, B.AAA \n" );
//			sbSql.append("  FROM (SELECT T.DEALER_ID, T.YIELDLY, T.ID, T.START_DATE,T.ADMIN_DEDUCT \n");
//			sbSql.append("          FROM TT_AS_WR_CLAIM_BALANCE T \n");
//			sbSql.append("         WHERE T.ADMIN_DEDUCT > 0) A, \n");
//			sbSql.append("       (SELECT T.DEALER_ID, T.YIELDLY, SUM(T.DEDUCT_AMOUNT) AAA \n");
//			sbSql.append("          FROM TT_AS_WR_ADMIN_DEDUCT T \n");
//			sbSql.append("         GROUP BY T.DEALER_ID, T.YIELDLY) B \n");
//			sbSql.append(" WHERE A.DEALER_ID = B.DEALER_ID(+) \n");
//			sbSql.append("   AND A.YIELDLY = B.YIELDLY(+) \n");
//			sbSql.append("  AND A.ADMIN_DEDUCT = B.AAA \n");
//			sbSql.append("  ORDER BY A.DEALER_ID, A.YIELDLY,A.START_DATE \n");
//			ps=con.prepareStatement(sbSql.toString());
//			rs=ps.executeQuery();
//			StringBuffer sbRes=new StringBuffer(); 
//			while(rs.next()){
//				long balanceId=rs.getLong("ID");
//				long dealer=rs.getLong("DEALER_ID");
//				long yieldly=rs.getLong("YIELDLY");
//				String sql="SELECT COUNT(*) AS COUNT FROM TT_AS_WR_CLAIM_BALANCE WHERE DEALER_ID="+dealer+" AND YIELDLY="+yieldly;
//				
//				PreparedStatement psLx=con.prepareStatement(sql);
//				ResultSet rsLx=psLx.executeQuery();
//				rsLx.next();
//				long count=rsLx.getLong("COUNT");
//				if(count>=2){ //11月份和12月份都有
//					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//					String timeStr=sdf.format(rs.getDate("START_DATE"));
//					
//					String sql2="SELECT ID,NOTE_AMOUNT FROM TT_AS_WR_CLAIM_BALANCE WHERE DEALER_ID="+dealer+" AND YIELDLY="+yieldly+" ORDER BY START_DATE ASC ";
//					PreparedStatement ps2=con.prepareStatement(sql2);
//					ResultSet rs2=ps2.executeQuery();
//					rs2.next();
//					long id=rs2.getLong("ID");
//					//String timeStr=sdf.format(rs2.getDate("START_DATE"));
//					if(timeStr.equals("2010-11-01"))
//					{
//					
//						System.out.println("UPDATE TT_AS_WR_ADMIN_DEDUCT SET DEDUCT_STATUS=11521002,CLAIMBALANCE_ID="+id+" WHERE DEALER_ID="+dealer+" AND YIELDLY="+yieldly+";");
//						sbRes.append("UPDATE TT_AS_WR_ADMIN_DEDUCT SET DEDUCT_STATUS=11521002,CLAIMBALANCE_ID="+id+" WHERE DEALER_ID="+dealer+" AND YIELDLY="+yieldly+";\r\n");
//					}
//					if(timeStr.equals("2010-12-01"))
//					{
//						rs2.next();
//						long balanceId222=rs2.getLong("ID");
//						
//						System.out.println("UPDATE TT_AS_WR_CLAIM_BALANCE SET ADMIN_DEDUCT=0 WHERE ID="+balanceId222+";");
//						sbRes.append("UPDATE TT_AS_WR_CLAIM_BALANCE SET ADMIN_DEDUCT=0 WHERE ID="+balanceId222+";\r\n");
//						
//						StringBuffer sb222=new StringBuffer();
//						sb222.append("UPDATE TT_AS_WR_CLAIM_BALANCE SET BALANCE_AMOUNT=((NVL(LABOUR_AMOUNT,0)+NVL(PART_AMOUNT,0)+NVL(OTHER_AMOUNT,0)+NVL(FREE_AMOUNT,0)");
//						sb222.append("+NVL(RETURN_AMOUNT,0)+NVL(MARKET_AMOUNT,0)+NVL(SPEOUTFEE_AMOUNT,0)+NVL(APPEND_AMOUNT,0)+NVL(SERVICE_TOTAL_AMOUNT,0))");
//						sb222.append("-(NVL(FREE_DEDUCT,0)+NVL(SERVICE_DEDUCT,0)+NVL(CHECK_DEDUCT,0)+NVL(OLD_DEDUCT,0)+NVL(ADMIN_DEDUCT,0))) ");
//						sb222.append("WHERE ID="+balanceId222+";\r\n");
//						
//						System.out.println(sb222.toString());
//						sbRes.append(sb222.toString());
//						
//						Double notAmout=rs2.getDouble("NOTE_AMOUNT");
//						notAmout=notAmout==null?0:notAmout;
//						if(notAmout>0){
//							sbRes.append("UPDATE TT_AS_WR_CLAIM_BALANCE SET NOTE_AMOUNT=BALANCE_AMOUNT-NVL(FINANCIAL_DEDUCT,0) WHERE ID="+balanceId222+";\r\n");
//						}
//					}
//					rs2.close();
//					ps2.close();
//				}
//				if(count==1){//只有一张结算单
//					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//					Date date=rs.getDate("START_DATE");
//					String dateStr=sdf.format(date);
//					if(dateStr.equals("2010-11-01")){//11月
//						System.out.println("UPDATE TT_AS_WR_ADMIN_DEDUCT SET DEDUCT_STATUS=11521002,CLAIMBALANCE_ID="+balanceId+" WHERE DEALER_ID="+dealer+" AND YIELDLY="+yieldly+";");
//						sbRes.append("UPDATE TT_AS_WR_ADMIN_DEDUCT SET DEDUCT_STATUS=11521002,CLAIMBALANCE_ID="+balanceId+" WHERE DEALER_ID="+dealer+" AND YIELDLY="+yieldly+";\r\n");
//					}
//					if(dateStr.equals("2010-12-01")){//12月
//						System.out.println("UPDATE TT_AS_WR_ADMIN_DEDUCT SET DEDUCT_STATUS=11521002,CLAIMBALANCE_ID="+balanceId+" WHERE DEALER_ID="+dealer+" AND YIELDLY="+yieldly+";");
//						sbRes.append("UPDATE TT_AS_WR_ADMIN_DEDUCT SET DEDUCT_STATUS=11521002,CLAIMBALANCE_ID="+balanceId+" WHERE DEALER_ID="+dealer+" AND YIELDLY="+yieldly+";\r\n");
//					}
//				}
//			}
//			String path = "D:/aaaa.sql";
//			   File file = new File(path);
//	
//			   FileWriter writer = new FileWriter(file);
//			   PrintWriter pw = new PrintWriter(writer);
//	
//			   pw.println(sbRes.toString());
//			   pw.flush();
//			   writer.close();
			
//			int count=0;
//			StringBuffer sbSql3=new StringBuffer();
//			sbSql3.append("select id from Tt_As_Wr_Claim_Balance" );
//			ps=con.prepareStatement(sbSql3.toString());
//			rs=ps.executeQuery();
//			while(rs.next()){
//				long balId=rs.getLong("ID");
//				StringBuffer sbSql=new StringBuffer();
//				sbSql.append("UPDATE tt_as_wr_claim_balance \n" );
//				sbSql.append("   SET (labour_amount_bak, part_amount_bak, other_amount_bak, free_amount_bak, \n");
//				sbSql.append("         append_amount_bak,service_total_amount_bak,append_labour_amount_bak) = \n");
//				sbSql.append("          (SELECT labour_amount, part_amount, netitem_amount other_amount, \n");
//				sbSql.append("                  free_m_price free_amount, \n");
//				sbSql.append("                  append_amount, service_total_amount, \n");
//				sbSql.append("                  appendlabour_amount \n");
//				sbSql.append("             FROM (SELECT   a1.yieldly, SUM (a1.claimcount) claimcount, \n");
//				sbSql.append("                            SUM (a1.repair_total) repair_total, \n");
//				sbSql.append("                            SUM (a1.free_m_price) free_m_price, \n");
//				sbSql.append("                            SUM \n");
//				sbSql.append("                               (a1.service_labour_amount \n");
//				sbSql.append("                               ) service_labour_amount, \n");
//				sbSql.append("                            SUM (a1.service_part_amount) service_part_amount, \n");
//				sbSql.append("                            SUM \n");
//				sbSql.append("                               (a1.service_netitem_amount \n");
//				sbSql.append("                               ) service_netitem_amount, \n");
//				sbSql.append("                            SUM (a1.campaign_fee) campaign_fee, \n");
//				sbSql.append("                            SUM (a1.labour_amount) labour_amount, \n");
//				sbSql.append("                            SUM (a1.part_amount) part_amount, \n");
//				sbSql.append("                            SUM (a1.netitem_amount) netitem_amount, \n");
//				sbSql.append("                            SUM (a1.balance_amount) balance_amount, \n");
//				sbSql.append("                            SUM (appendlabour_amount) appendlabour_amount, \n");
//				sbSql.append("                            SUM (append_amount) append_amount, \n");
//				sbSql.append("                            SUM (free_labour_amount) free_labour_amount, \n");
//				sbSql.append("                            (SUM (free_m_price) - SUM (free_labour_amount) \n");
//				sbSql.append("                            ) free_part_amount, \n");
//				sbSql.append("                            (  SUM (service_labour_amount) \n");
//				sbSql.append("                             + SUM (service_part_amount) \n");
//				sbSql.append("                             + SUM (service_netitem_amount) \n");
//				sbSql.append("                             + SUM (campaign_fee) \n");
//				sbSql.append("                            ) service_total_amount \n");
//				sbSql.append("                       FROM (SELECT   a.yieldly, COUNT (*) claimcount, \n");
//				sbSql.append("                                      0 labour_amount, 0 part_amount, \n");
//				sbSql.append("                                      0 netitem_amount, \n");
//				sbSql.append("                                      0 service_labour_amount, \n");
//				sbSql.append("                                      0 service_part_amount, \n");
//				sbSql.append("                                      0 service_netitem_amount, \n");
//				sbSql.append("                                      0 campaign_fee, \n");
//				sbSql.append("                                      (  NVL (SUM (a.free_m_price), 0) \n");
//				sbSql.append("                                       + NVL (SUM (a.appendlabour_amount), 0) \n");
//				sbSql.append("                                      ) free_m_price, \n");
//				sbSql.append("                                      NVL (SUM (a.repair_total), \n");
//				sbSql.append("                                           0 \n");
//				sbSql.append("                                          ) repair_total, \n");
//				sbSql.append("                                      NVL \n");
//				sbSql.append("                                         (SUM (a.balance_amount), \n");
//				sbSql.append("                                          0 \n");
//				sbSql.append("                                         ) balance_amount, \n");
//				sbSql.append("                                      NVL \n");
//				sbSql.append("                                         (SUM (a.appendlabour_amount), \n");
//				sbSql.append("                                          0 \n");
//				sbSql.append("                                         ) appendlabour_amount, \n");
//				sbSql.append("                                      NVL \n");
//				sbSql.append("                                         (SUM (a.append_amount), \n");
//				sbSql.append("                                          0 \n");
//				sbSql.append("                                         ) append_amount, \n");
//				sbSql.append("                                      50.0 * (COUNT (*)) free_labour_amount, \n");
//				sbSql.append("                                      0 free_part_amount \n");
//				sbSql.append("                                 FROM tt_as_wr_application a, \n");
//				sbSql.append("                                      tr_balance_claim b \n");
//				sbSql.append("                                WHERE 1 = 1 \n");
//				sbSql.append("                                  AND a.ID = b.claim_id \n");
//				sbSql.append("                                  AND a.claim_type IN (10661002) \n");
//				sbSql.append("                                  AND b.balance_id = "+balId+" \n");
//				sbSql.append("                             GROUP BY a.yieldly \n");
//				sbSql.append("                             UNION ALL \n");
//				sbSql.append("                             SELECT   a.yieldly, COUNT (*) claimcount, \n");
//				sbSql.append("                                      0 labour_amount, 0 part_amount, \n");
//				sbSql.append("                                      0 netitem_amount, \n");
//				sbSql.append("                                      NVL \n");
//				sbSql.append("                                         (SUM (a.labour_amount), \n");
//				sbSql.append("                                          0 \n");
//				sbSql.append("                                         ) service_labour_amount, \n");
//				sbSql.append("                                      NVL \n");
//				sbSql.append("                                         (SUM (a.part_amount), \n");
//				sbSql.append("                                          0 \n");
//				sbSql.append("                                         ) service_part_amount, \n");
//				sbSql.append("                                      NVL \n");
//				sbSql.append("                                         (SUM (a.netitem_amount), \n");
//				sbSql.append("                                          0 \n");
//				sbSql.append("                                         ) service_netitem_amount, \n");
//				sbSql.append("                                      NVL (SUM (a.campaign_fee), \n");
//				sbSql.append("                                           0 \n");
//				sbSql.append("                                          ) campaign_fee, \n");
//				sbSql.append("                                      0 free_m_price, \n");
//				sbSql.append("                                      NVL (SUM (a.repair_total), \n");
//				sbSql.append("                                           0 \n");
//				sbSql.append("                                          ) repair_total, \n");
//				sbSql.append("                                      NVL \n");
//				sbSql.append("                                         (SUM (a.balance_amount), \n");
//				sbSql.append("                                          0 \n");
//				sbSql.append("                                         ) balance_amount, \n");
//				sbSql.append("                                      NVL \n");
//				sbSql.append("                                         (SUM (a.appendlabour_amount), \n");
//				sbSql.append("                                          0 \n");
//				sbSql.append("                                         ) appendlabour_amount, \n");
//				sbSql.append("                                      NVL \n");
//				sbSql.append("                                         (SUM (a.append_amount), \n");
//				sbSql.append("                                          0 \n");
//				sbSql.append("                                         ) append_amount, \n");
//				sbSql.append("                                      0 free_labour_amount, \n");
//				sbSql.append("                                      0 free_part_amount \n");
//				sbSql.append("                                 FROM tt_as_wr_application a, \n");
//				sbSql.append("                                      tr_balance_claim b \n");
//				sbSql.append("                                WHERE 1 = 1 \n");
//				sbSql.append("                                  AND a.ID = b.claim_id \n");
//				sbSql.append("                                  AND a.claim_type IN (10661006) \n");
//				sbSql.append("                                  AND b.balance_id = "+balId+" \n");
//				sbSql.append("                             GROUP BY a.yieldly \n");
//				sbSql.append("                             UNION ALL \n");
//				sbSql.append("                             SELECT   a.yieldly, COUNT (*) claimcount, \n");
//				sbSql.append("                                      (  NVL (SUM (a.labour_amount), \n");
//				sbSql.append("                                              0) \n");
//				sbSql.append("                                       + NVL (SUM (a.appendlabour_amount), 0) \n");
//				sbSql.append("                                      ) labour_amount, \n");
//				sbSql.append("                                      NVL \n");
//				sbSql.append("                                         (SUM (a.part_amount), \n");
//				sbSql.append("                                          0 \n");
//				sbSql.append("                                         ) part_amount, \n");
//				sbSql.append("                                      NVL \n");
//				sbSql.append("                                         (SUM (a.netitem_amount), \n");
//				sbSql.append("                                          0 \n");
//				sbSql.append("                                         ) netitem_amount, \n");
//				sbSql.append("                                      0 free_m_price, 0 service_labour_amount, \n");
//				sbSql.append("                                      0 service_part_amount, \n");
//				sbSql.append("                                      0 service_netitem_amount, \n");
//				sbSql.append("                                      0 campaign_fee, \n");
//				sbSql.append("                                      NVL (SUM (a.repair_total), \n");
//				sbSql.append("                                           0 \n");
//				sbSql.append("                                          ) repair_total, \n");
//				sbSql.append("                                      NVL \n");
//				sbSql.append("                                         (SUM (a.balance_amount), \n");
//				sbSql.append("                                          0 \n");
//				sbSql.append("                                         ) balance_amount, \n");
//				sbSql.append("                                      NVL \n");
//				sbSql.append("                                         (SUM (a.appendlabour_amount), \n");
//				sbSql.append("                                          0 \n");
//				sbSql.append("                                         ) appendlabour_amount, \n");
//				sbSql.append("                                      NVL \n");
//				sbSql.append("                                         (SUM (a.append_amount), \n");
//				sbSql.append("                                          0 \n");
//				sbSql.append("                                         ) append_amount, \n");
//				sbSql.append("                                      0 free_labour_amount, \n");
//				sbSql.append("                                      0 free_part_amount \n");
//				sbSql.append("                                 FROM tt_as_wr_application a, \n");
//				sbSql.append("                                      tr_balance_claim b \n");
//				sbSql.append("                                WHERE 1 = 1 \n");
//				sbSql.append("                                  AND a.ID = b.claim_id \n");
//				sbSql.append("                                  AND a.claim_type NOT IN \n");
//				sbSql.append("                                                         (10661002, 10661006) \n");
//				sbSql.append("                                  AND b.balance_id = "+balId+" \n");
//				sbSql.append("                             GROUP BY a.yieldly) a1 \n");
//				sbSql.append("                   GROUP BY yieldly) a2 \n");
//				sbSql.append("            WHERE ROWNUM = 1) \n");
//				sbSql.append(" WHERE ID = "+balId);
//				
//				PreparedStatement ps2=con.prepareStatement(sbSql.toString());
//				ps2.executeUpdate();
//				
//				StringBuffer sbSql2=new StringBuffer();
//				sbSql2.append("update Tt_As_Wr_Claim_Balance c set c.apply_amount = nvl(c.LABOUR_AMOUNT_BAK,0) + nvl(PART_AMOUNT_BAK,0) + nvl(OTHER_AMOUNT_BAK,0) + \n" );
//				sbSql2.append("nvl(FREE_AMOUNT_BAK,0) + nvl(RETURN_AMOUNT_BAK,0) + nvl(MARKET_AMOUNT_BAK,0) + \n");
//				sbSql2.append("nvl(SPEOUTFEE_AMOUNT_BAK,0) + \n");
//				sbSql2.append("nvl(APPEND_AMOUNT_BAK,0) + nvl(SERVICE_TOTAL_AMOUNT_BAK,0) WHERE ID = "+balId);
//				PreparedStatement ps3=con.prepareStatement(sbSql2.toString());
//				ps3.executeUpdate();
//				ps2.close();
//				ps3.close();
//				System.out.println("正在更新第:"+count+++"条数据");
//			}

			
//			StringBuffer sb=new StringBuffer();
//			sb.append("SELECT * FROM tt_as_wr_claim_balance A WHERE A.DEALER_ID IN ");
//			sb.append("(");
//			//sb.append("SELECT a.dealer_id FROM tm_dealer a WHERE a.parent_dealer_d IN (SELECT b.dealer_id  FROM tm_dealer b WHERE b.dealer_level = 10851002)");
//			sb.append("SELECT a.dealer_id FROM tm_dealer a WHERE a.dealer_level = 10851002");
//			sb.append(") order by a.id asc");
//			System.out.println(sb.toString());
//			StringBuffer sbRes=new StringBuffer();
//			ps=con.prepareStatement(sb.toString());
//			rs=ps.executeQuery();
//			while(rs.next()){
//				Long kdDealerId=null;
//				String kpName=null;
//				long balanceId=rs.getLong("ID");
//				long dealerId=rs.getLong("DEALER_ID");
//				
//				StringBuilder sql= new StringBuilder();
//				sql.append("SELECT A.* FROM TM_DEALER A\n" );
//				sql.append("START WITH A.DEALER_ID = "+dealerId+"\n" );
//				sql.append("CONNECT BY A.DEALER_ID = PRIOR A.PARENT_DEALER_D"); //向上层遍历
//				PreparedStatement ps2=con.prepareStatement(sql.toString());
//				ResultSet rs2=ps2.executeQuery(sql.toString());
//				while(rs2.next()){
//					String INVOICE_LEVEL=rs2.getString("INVOICE_LEVEL");
//					if(INVOICE_LEVEL.equals("13611001")){//独立开票
//						kdDealerId=rs2.getLong("DEALER_ID");
//						kpName=rs2.getString("DEALER_NAME");
//						break;
//					}else{
//						continue;
//					}
//				}
//				if(rs2!=null) rs2.close();
//				if(ps2!=null) ps2.close();
//				long vvv=rs.getLong("KP_DEALER_ID");
//				if(vvv==kdDealerId.longValue()){
//					System.out.println("正确的开票单位:"+kdDealerId);
//					continue;
//				}
//				System.out.println("UPDATE tt_as_wr_claim_balance SET KP_DEALER_ID="+kdDealerId+",INVOICE_MAKER='"+kpName+"' WHERE ID="+balanceId+";");
//				sbRes.append("UPDATE tt_as_wr_claim_balance SET KP_DEALER_ID="+kdDealerId+",INVOICE_MAKER='"+kpName+"' WHERE ID="+balanceId+";\r\n");
//				
//				//sbRes.append(balanceId+" "+rs.getLong("KP_DEALER_ID")+" "+rs.getString("INVOICE_MAKER")+"\r\n");
//				//sbRes.append(balanceId+" "+kdDealerId+" "+kpName+"\r\n");
//			}
//			String path = "D:/aaaa.sql";
//			   File file = new File(path);
//	
//			   FileWriter writer = new FileWriter(file);
//			   PrintWriter pw = new PrintWriter(writer);
//	
//			   pw.println(sbRes.toString());
//			   pw.flush();
//			   writer.close();
			
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//			con=DriverManager.getConnection("jdbc:oracle:thin:@(description=(address_list=(address=(host=10.0.2.49)(protocol=tcp)(port=1525))(address=(host=10.0.2.45)(protocol=tcp) (port=1525))(load_balance=false)(failover=yes))(connect_data=(service_name= INFODMS)))","23","233");
//			StringBuffer sb=new StringBuffer();
//			StringBuffer sb2=new StringBuffer();
//			sb2.append("SELECT * FROM tt_as_wr_claim_balance");
//			ps=con.prepareStatement(sb2.toString());
//			rs=ps.executeQuery();
//			int count=0;
//			
//			while(rs.next()){
//				count++;
//				long balanceId=rs.getLong("ID");
//				long dealerId=rs.getLong("DEALER_ID");
//				long yeild=rs.getLong("YIELDLY");//产地
//				String a=("UPDATE TT_AS_WR_SPEFEE A SET A.CLAIMBALANCE_ID="+balanceId+" WHERE A.CLAIMBALANCE_ID IS NULL " +
//						"AND A.FEE_TYPE=11831001 AND A.STATUS=11841002 AND A.DEALER_ID="+dealerId+" AND A.YIELD="+yeild+" "+
//								"AND A.MAKE_DATE>=TO_DATE('2010-11-01 00:00:00','YYYY-MM-DD HH24:MI:SS') AND A.MAKE_DATE<=TO_DATE('2010-11-30 23:59:59','YYYY-MM-DD HH24:MI:SS');");
//				sb.append(a);
//				sb.append("\r\n");
//				String  b=("UPDATE TT_AS_WR_SPEFEE A SET A.CLAIMBALANCE_ID="+balanceId+" WHERE A.CLAIMBALANCE_ID IS NULL " +
//						"AND A.FEE_TYPE=11831002 AND A.STATUS=11841004 AND A.DEALER_ID="+dealerId+" AND A.YIELD="+yeild+" "+
//								"AND EXISTS (SELECT B.STATUS FROM TT_AS_WR_SPEFEE_AUDITING B " +
//								"WHERE B.FEE_ID=A.ID AND B.STATUS=11841004 AND B.AUDITING_DATE>=TO_DATE('2010-11-01 00:00:00','YYYY-MM-DD HH24:MI:SS') " +
//								"AND B.AUDITING_DATE<=TO_DATE('2010-11-30 23:59:59','YYYY-MM-DD HH24:MI:SS'));");
//				
//				sb.append(b);
//				sb.append("\r\n");
//			}
//			System.out.println("总数:"+count);
//			 String path = "D:/aaaa.sql";
//			   File file = new File(path);
//
//			   FileWriter writer = new FileWriter(file);
//			   PrintWriter pw = new PrintWriter(writer);
//
//			   pw.println(sb.toString());
//			   pw.flush();
//			   writer.close();
				

			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(ps!=null) ps.close();
			if(con!=null) con.close();
		}
		
	}
}







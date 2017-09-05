<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@ page import="com.infodms.dms.common.Constant"%>
	<%@ page import="java.util.Map"%>
	<%@ page import="java.util.List"%>
	<%@taglib uri="/jstl/cout" prefix="c"%>
	<head>
		<%
			String contextPath = request.getContextPath();
			long tnba = 0;
			long tda = 0;
			long tsa = 0;
			long tst = 0;
		%>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>市场活动报表（经销商）</title>
		<link href="<%=request.getContextPath()%>/style/content.css"
			rel="stylesheet" type="text/css" />
		<link href="<%=request.getContextPath()%>/style/calendar.css"
			type="text/css" rel="stylesheet" />
		<link href="<%=request.getContextPath()%>/style/page-info.css"
			rel="stylesheet" type="text/css" />

		<style type="text/css">
body,table,td,a {
	font: 9pt;
}

/*固定行头样式*/
.scrollRowThead {
	position: relative;
	left: expression(this . parentElement . parentElement . parentElement .
		scrollLeft);
	z-index: 0;
}

/*固定表头样式*/
.scrollColThead {
	position: relative;
	top: expression(this . parentElement . parentElement . parentElement .
		scrollTop);
	z-index: 2;
}

/*行列交叉的地方*/
.scrollCR {
	z-index: 3;
}

/*div外框*/
.scrollDiv {
	height: 480px;
	clear: both;
	border: 1px solid #EEEEEE;
	OVERFLOW: scroll;
	width: 100%;
}

/*行头居中*/
.scrollColThead td,.scrollColThead th {
	text-align: center;
}

/*行头列头背景*/
.scrollRowThead,.scrollColThead td,.scrollColThead th {
	background-color: EEEEEE;
}

/*表格的线*/
.scrolltable {
	border-bottom: 1px solid #CCCCCC;
	border-right: 1px solid #CCCCCC;
}

/*单元格的线等*/
.scrolltable td,.scrollTable th {
	border-left: 1px solid #CCCCCC;
	border-top: 1px solid #CCCCCC;
	padding: 5px;
	text-align: center;
	/*   white-space: nowrap; */
}
</style>
	</head>
	<body>
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />
			&nbsp;
			<strong>市场活动报表（经销商）&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong>
		</div>
		<div id="scrollDiv" class="scrollDiv">
			<table border="0" cellpadding="3" cellspacing="0" width="100%"
				class="scrollTable">

				<tr class="scrollColThead">
					<th colspan="50" class="scrollRowThead scrollCR">
						<strong>
							市场活动报表
						</strong>
					</th>
				</tr>
				<tr class="scrollColThead" align="left">
					<th colspan="5" class="scrollRowThead scrollCR" align="left">
						<div align="left">
							
						</div>
					</th>
				</tr>

				<tr class="scrollColThead">
					<th nowrap class="scrollRowThead scrollCR">
						大区
					</th>
					<th nowrap class="scrollRowThead scrollCR">
						省系
					</th>
					<th nowrap class="scrollRowThead scrollCR">
						责任人
					</th>
					 <th nowrap class="scrollRowThead scrollCR">
						地点
					</th>
                    <th nowrap class="scrollRowThead scrollCR">
						月份
					</th>
					<th nowrap class="scrollRowThead scrollCR">
						经销商代名称
					</th>
                    <th nowrap class="scrollRowThead scrollCR">
						活动名称
					</th>
                    <th nowrap class="scrollRowThead scrollCR">
						主题
					</th>
					 <th nowrap class="scrollRowThead scrollCR">
						类型
					</th>
					<th nowrap class="scrollRowThead scrollCR">
						开始时间
					</th>
					<th nowrap>
						结束时间
					</th>
					<th nowrap class="scrollRowThead scrollCR">
						首客目标
					</th>
                    <th nowrap class="scrollRowThead scrollCR">
						实际首客
					</th>
					<th nowrap>
						混客目标
					</th>
					<th nowrap class="scrollRowThead scrollCR">
						实际混客
					</th>
                    <th nowrap class="scrollRowThead scrollCR">
						建卡目标
					</th>
                    <th nowrap class="scrollRowThead scrollCR">
						实际建卡
					</th>
					<th nowrap class="scrollRowThead scrollCR">
						订单目标
					</th>
					<th nowrap class="scrollRowThead scrollCR">
						实际订单
					</th>
                   
				</tr>

<% List<Map<String,Object>> ps = (List<Map<String,Object>>)request.getAttribute("ps");  %>
             <%for(int i=0;i<ps.size();i++){%>
						<tr>
							<td nowrap>
								<%=ps.get(i).get("ROOT_ORG_NAME")%>&nbsp;
							</td>
							<td nowrap>
                                <%=ps.get(i).get("PQ_ORG_NAME")%>&nbsp;
							</td>
							<td nowrap>
                                <%=ps.get(i).get("CHARGE_MAN")%>&nbsp;
							</td>
							 <td nowrap>
                                <%=ps.get(i).get("ADDRESS")%>&nbsp;
							</td>
							<td nowrap>
                                <%=ps.get(i).get("ACTIVITY_MONTH")%>&nbsp;
							</td>
							<td nowrap>
                                <%=ps.get(i).get("DEALER_SHORTNAME")%>&nbsp;
							</td>
							<td nowrap>
                                <%=ps.get(i).get("ACTIVITY_NAME")%>&nbsp;
							</td>
							<td nowrap>
                                <%=ps.get(i).get("ACTIVITY_THEME")%>&nbsp;
							</td> 
							<td nowrap>
                                <%=ps.get(i).get("ACTIVITY_TYPE")%>&nbsp;
							</td>                     
							<td nowrap>
                                <%=ps.get(i).get("START_DATE")%>&nbsp;
							</td>
                            <td nowrap>
                                <%=ps.get(i).get("END_DATE")%>&nbsp;
							</td>
							<td nowrap>
                                <%=ps.get(i).get("TOTAL_FCLIENT")%>&nbsp;
							</td>
							<td nowrap>
                                <%=ps.get(i).get("ACT_TOTAL_FCLIENT")%>&nbsp;
							</td>
                            <td nowrap>
                                <%=ps.get(i).get("TOTAL_MCLIENT")%>&nbsp;
							</td>
							<td nowrap>
								<%=ps.get(i).get("ACT_TOTAL_MCLIENT")%>&nbsp;
							</td>
							<td nowrap>
                                <%=ps.get(i).get("TOTAL_AIMCARD")%>&nbsp;
							</td>
                            <td nowrap>
                                <%=ps.get(i).get("ACT_TOTAL_AIMCARD")%>&nbsp;
							</td>
							<td nowrap>
								<%=ps.get(i).get("TOTAL_AIMORDER")%>&nbsp;
							</td>
							<td nowrap>
                                <%=ps.get(i).get("ACT_TOTAL_AIMORDER")%>&nbsp;
							</td>
						</tr>
             <%}%>
			</table>
		</div>
		<br>
		<table border="0" align="center" class="table_list">
			<tr>
				<td>
					<input name="button2" type=button class="cssbutton"
						onClick="window.close();" value="关闭">
				</td>
			</tr>
		</table>
	</body>
</html>

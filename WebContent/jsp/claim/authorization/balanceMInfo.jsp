<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.Map"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>结算单明细</title>
		<script type="text/javascript">
			function divControl()
			{
				document.getElementById('detailDiv').style.width=document.body.clientWidth;
			}
		</script>
	</head>
<body >
<form method="post" name="fm" id="fm">
<input type="hidden" name="var" value="<c:out value="${map.VAR}"/>"/>
<input type="hidden" name="jiujianCount" value="<c:out value="${map.TOTAL_AMOUNT}"/>"/>
<input type="hidden" name="kaoheCount" value="<c:out value="${map.FINE_SUM}"/>"/>
<input type="hidden" name="xingzhengCount" value="<c:out value="${map.DAMOUNT}"/>"/>
<input type="hidden" id="zongjiCount" name="zongjiCount" value="<c:out value="${map.AAMOUNT}"/>"/>
<input type="hidden" id="feiyongCount" name="feiyongCount" value="<c:out value="${map.AMOUNT_SUM}"/>"/>
<input type="hidden" id="id" name="id" value="<c:out value="${map.ID}"/>"/>
<input type="hidden" name="dealerId" value="<c:out value="${map.DEALER_ID}"/>"/>
<input type="hidden" name="dealerCode" value="<c:out value="${map.DEALER_CODE}"/>"/>
	<table width="100%">
	    <tr>
		    <td>
				<div class="navigation">
			    	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;结算单复核申请管理
			    </div>
		    </td>
	    </tr>
	</table>
	<table class="table_edit">
			<tr>
				<td align="right" nowrap="nowrap">结算单号：</td>
				<td align="left" nowrap="nowrap">
				   <c:out value="${map.BALANCE_NO}"/>
				</td>
				<td align="right" nowrap="nowrap">制单人姓名：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.APPLY_PERSON_NAME}"/>
				</td>
				<td align="right" nowrap="nowrap">制单日期：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.CREATEDATE}"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">经销商代码：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.DEALER_CODE}"/>
				</td>
				<td align="right" nowrap="nowrap">经销商名称：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.DEALER_NAME}"/>
				</td>
				<td align="right" nowrap="nowrap">开票单位：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.INVOICE_MAKER}"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">维修时间起：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.STARTDATE}"/>
				</td>
				<td align="right" nowrap="nowrap">维修时间止：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.ENDDATE}"/>
				</td>
				<td align="right" nowrap="nowrap">生产商：</td>
				<td align="left" nowrap="nowrap">
					<script type="text/javascript">
						writeItemValue(<c:out value="${map.YIELDLY}"/>)
					</script>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">工时费用(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.LABOUR_AMOUNT}"/>
				</td>
				<td align="right" nowrap="nowrap">材料费(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.PART_AMOUNT}"/>
				</td>
				<td align="right" nowrap="nowrap">救急费用(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.OTHER_AMOUNT}"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">保养费用(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.FREE_AMOUNT}"/>
				</td>
				<td align="right" nowrap="nowrap">服务活动费用(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.SERVICE_AMOUNT}"/>
				</td>
				<td align="right" nowrap="nowrap">运费(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.RETURN_AMOUNT}"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">特殊费用(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.DECLARE_SUM1}"/>
				</td>
				<td align="right" nowrap="nowrap">特殊外出费用(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.SPEOUTFEE_AMOUNT}"/>
				</td>
				<td align="right" nowrap="nowrap">&nbsp;</td>
				<td align="left" nowrap="nowrap">&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">旧件扣款(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.OLD_DEDUCT}" default="0"/>
				</td>
				<td align="right" nowrap="nowrap">考核扣款(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.CHECK_DEDUCT}" default="0"/>
				</td>
				<td align="right" nowrap="nowrap">行政扣款(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.ADMIN_DEDUCT}" default="0"/>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">保养费扣款(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.FREE_DEDUCT}" default="0"/>
				</td>
				<td align="right" nowrap="nowrap">服务活动单扣款(元)：</td>
				<td align="left" nowrap="nowrap">
					<c:out value="${map.SERVICE_DEDUCT}"/>
				</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td align="right" nowrap="nowrap">费用合计(元)：</td>
				<td align="left" nowrap="nowrap">
					<div id="amountsum">
						<c:out value="${map.AMOUNT_SUM}"/>
					</div>
				</td>
				<td align="right" nowrap="nowrap">扣款合计(元)：</td>
				<td align="left" nowrap="nowrap">
					<div id="tamount">
						<c:out value="${map.DEDUCT}"/>
					</div>
				</td>
				<td align="right" nowrap="nowrap">总计(元)：</td>
				<td>
					<div id="aamount">
						<c:out value="${map.BALANCE_AMOUNT}"/>
					</div>
				</td>
			</tr>
			<tr>
				<td align="right">备注：</td>
				<td align="left" colspan="5">
					<c:out value="${map.REMARK}"/>
				</td>
			</tr>
			<tr>
				<td align="right">索赔单数：</td>
				<td align="left" colspan="5">
					<c:out value="${map.CLAIM_COUNT}"/>
				</td>
			</tr>
			<tr>
				<td align="right">站长电话：</td>
				<td align="left">
					<c:out value="${map.STATIONER_TEL}"/>
				</td>
				<td align="right">索赔员电话：</td>
				<td align="left" colspan="3">
					<c:out value="${map.CLAIMER_TEL}"/>
				</td>
			</tr>
		</table>
	<table>
		<tr>
			<td>
				<div id="detailDiv" style="overflow:scroll">
					<table class="table_list">
						<tr class="table_list_th">
							<th style="position:relative">行号</th><%--0--%>
							<th style="position:relative">车系</th><%--1--%>
							<th style="position:relative">备注</th><%--17--%>
							<th>售前工时费用(元)</th><%--2--%>
							<th>售前配件费用(元)</th><%--3--%>
							<th>售后工时费用(元)</th><%--5--%>
							<th>售后配件费用(元)</th><%--6--%>
							<th>保养次数</th><%--9--%>
							<th>保养费用(元)</th><%--8--%>
							<th>保养工时费用(元)</th><%--81--%>
							<th>保养材料费用(元)</th><%--82--%>
							<th>服务活动次数</th><%--14--%>
							<th>服务活动费用(元)</th><%--13--%>
							<th>售前三包单数</th><%--15--%>
							<th>售后三包单数</th><%--16--%>
						</tr>
					<%
						List<Map<String, Object>> list = (List<Map<String, Object>>)request.getAttribute("list");
						if(list!=null&&list.size()>0){
							for(int i=0;i<list.size();i++){
								Map<String, Object> map = list.get(i);
					%>
								<tr class="<%=(i%2)==0?"table_list_row1":"table_list_row2"%>">
									<td style="position:relative"><%=map.get("NUM") %></td>
									<td style="position:relative"><%=map.get("SERIES_NAME") %></td>
									<td style="position:relative" title="<%=map.get("REMARK")==null?"":map.get("REMARK")%>">
										<%
							          			if(map.get("REMARK")!=null){
							          				if(String.valueOf(map.get("REMARK")).length()<=6){
							          		%>
							          			<%=map.get("REMARK")%>
							          		<%
						          				}
						          		%>
						          		<%
						          				if(String.valueOf(map.get("REMARK")).length()>10){
						          					String s = String.valueOf(map.get("REMARK"));
						          					s = s.substring(0,5);
						          		%>
						          			<%=s%>...
						          		<%
						          				}
						          			}
						          		%>
									</td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("BEFORE_LABOUR_AMOUNT") %>))
										</script>
									</td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("BEFORE_PART_AMOUNT") %>))
										</script>
									</td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("AFTER_LABOUR_AMOUNT") %>))
										</script>
									</td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("AFTER_PART_AMOUNT") %>))
										</script>
									</td>
									<td><%=map.get("FREE_CLAIM_COUNT") %></td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("FREE_CLAIM_AMOUNT") %>))
										</script>
									</td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("FREE_LABOUR_AMOUNT") %>))
										</script>
									</td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("FREE_PART_AMOUNT") %>))
										</script>
									</td>
									<td><%=map.get("SERVICE_CLAIM_COUNT") %></td>
									<td>
										<script type="text/javascript">
											document.write(amountFormat(<%=map.get("SERVICE_AMOUNT") %>))
										</script>
									</td>
									<td><%=map.get("BEFORE_CLAIM_COUNT") %></td>
									<td><%=map.get("AFTER_CLAIM_COUNT") %></td>
								</tr>
					<%
							}
						}
					%>
					</table>
					<script type="text/javascript">
						divControl();
					</script>
				</div>
			</td>
		</tr>
	</table>
	<table class="table_edit">
			<tr>
				<td colspan="6" align="center">
					<input type="button" class="normal_btn" name="backBtn" onclick="javascript:history.go(-1)" value="返回"/>
				</td>
			</tr>
	</table>
</form>
<script type="text/javascript">
	function divControl()
	{
		document.getElementById('detailDiv').style.width=document.body.clientWidth;
	}
</script>
</body>
</html>
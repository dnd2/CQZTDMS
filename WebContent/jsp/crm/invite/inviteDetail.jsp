<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>邀约查询</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/basedata/pcgroup.js"></script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 邀约管理 &gt; 邀约查询</div>
<div style="background-color: #F3F4F8;height:800px;" >
<!--<table class="table_query" >-->
<!--	<tr  >-->
<!--		<td style="width:20px;"><a href="#">&nbsp;</a></td>-->
<!--		<td style="width:20px;"><a href="#">&nbsp;</a></td>-->
<!--		<td style="width:20px;"><a href="#">&nbsp;</a></td>-->
<!--		<td style="width:80px;"><a href="#">查看邀约计划</a></td>-->
<!--		<td style="width:80px;"><a href="#">查看邀约记录</a></td>-->
<!--		<td style="width:80px;"><a href="#">查看邀约话术</a></td>-->
<!--		<td  colspan="3"><a href="#">&nbsp;</a></td>-->
<!--	</tr>-->
<!--</table>-->
<br/>
<form id="fm" name="fm" method="post">
<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
<input type="hidden" name="dealerId" id="dealerId" value="${dealerId}" />
<input type="hidden" name="curPage" id="curPage" value="1" />
		<div>
				<b style="display: inline-block; float: left">邀约计划</b>
				<hr style="display: inline-block;"/>
			</div>
<table>
				<tr>
					<td align="right" width="6%">客户编码：</td>
					<td><input id="customer_code" name="customer_code" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${tpc.customerCode}"
						maxlength="60" /></td>
					<td align="right" width="14%">客户姓名：</td>
					<td><input id="customer_name" name="customer_name" type="text" readonly="readonly" style="background-color: #EEEEEE;"
						class="middle_txt" datatype="1,is_textarea,30" size="20" value="${tpc.customerName }"
						maxlength="60" /></td>
					<td align="right" width="14%">联系电话：</td>
					<td width="12%"><input id="telephone" name="telephone" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${tpc.telephone }"
						size="20" maxlength="60" /></td>
					<td align="right" width="6%">邀约方式：</td>
					<td>
					<input id="invite_type" name="invite_type" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${inviteWay }"
						size="20" maxlength="60" />
					</td>
				</tr>
				
				<tr>
					<td align="right" width="12%">邀约类型：</td>
					<td>
					<input id="invite_way" name="invite_way" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${inviteType }"
						size="20" maxlength="60" />
					</td>
					<td align="right" width="10%">是否邀约成功：</td>
					<td>
					<input id="oif_invite" name="oif_invite" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="是"
						size="20" maxlength="60" />
					</td>
					<td width="3%" align="right">预约到店时间：</td>
					<td width="22%">
						<div align="left">
							<input type="text" value="${inviteShopDate }" name="pre_invite_shop_date" id="pre_invite_shop_date" group="pre_invite_shop_date" style="background-color: #EEEEEE;"
								class="middle_txt" datatype="1,is_textarea,30" size="20" readonly="readonly"
								maxlength="60"  />
						</div>
					</td>
					<td align="right" width="15%">原意向等级：</td>
					<td width="25%">
					<div align="left">
							<input type="text" value="${oldLevel} " name="ointent_type" id="ointent_type" group="pre_invite_shop_date" style="background-color: #EEEEEE;"
								class="middle_txt" datatype="1,is_textarea,30" size="20" readonly="readonly"
								maxlength="60"  />
						</div>
					</td>
				</tr>
				<tr>
					<td align="right" width="12%">新意向等级：</td>
					<td>
					<input id="invite_way" name="invite_way" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${newLevel }"
						size="20" maxlength="60" />
					</td>
					<td align="right" width="10%">意向车型：</td>
					<td>
					<input id="invite_way" name="invite_way" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${intentVehicle }"
						size="20" maxlength="60" />
					</td>
					<td width="3%" align="right">&nbsp;</td>
					<td width="22%">
						&nbsp;
					</td>
					<td align="right" width="15%">&nbsp;</td>
					<td width="25%">
						&nbsp;
						
					</td>
				</tr>
				
				<tr>
					<td align="right" colspan="1">邀约成效：</td>
					<td align="left" colspan="5">
						<textarea rows="5" cols="70" id="invite_info" name="invite_info" readonly="readonly" style="background-color: #EEEEEE;">${tp.remark }</textarea>
					</td>
				</tr>
				<tr>
					<td  align="center" colspan="2" >需求分析</td>
					<td  align="center" colspan="2">邀约目标</td>
					<td  align="center" colspan="2">赢得客户信任设计</td>
					<td  align="center" colspan="2">感动客户情景设计</td>
				</tr>
				<tr>
					<td align="center" colspan="2">
							<textarea style="background-color: #EEEEEE;" rows="6" cols="30" readonly="readonly" >${tp.requirement}</textarea>
					</td>
					<td  align="center" colspan="2" >
							<textarea style="background-color: #EEEEEE;" rows="6" cols="30" readonly="readonly"> ${tp.inviteTarget}</textarea>
					</td>
					<td align="center" colspan="2">
							<textarea style="background-color: #EEEEEE;" rows="6" cols="30" readonly="readonly"> ${tp.trustDesign}</textarea>
					</td>
					<td align="center" colspan="2">
							<textarea style="background-color: #EEEEEE;"  rows="6" cols="30" readonly="readonly"> ${tp.sceneDesign}</textarea>
					</td>
				</tr>
<!--				<tr>-->
<!--					 <td align="center" colspan="7">-->
<!--						<a href="#" id="FOLLOW_ID" name="FOLLOW_ID" onclick='followOpenInfo("${customerId}")'>客户资料维护</a>-->
<!--					</td>-->
<!--				</tr>-->

			</table>
			<br/>
			<div>
				<b style="display: inline-block; float: left">到店结果</b>
				<hr style="display: inline-block;"/>
			</div>
			<br/>
			<table class="table_query" width="95%" align="center">
				<tr>
					<td align="right" width="12%">到店时间：</td>
					<td width="15%">
						<div align="left">
							<input id="invite_way" name="invite_way" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${actShopDate }"
						size="20" maxlength="60" />
						</div>
					</td>
					<td align="right" width="12%">是否到店：</td>
					<td width="12%">
					<input id="invite_way" name="invite_way" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${ifShop }"
						size="20" maxlength="60" />
					</td>
					<td align="right" width="10%">原意向等级：</td>
					<td width="15%">
					<input id="invite_way" name="invite_way" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${oldShopLevel }"
						size="20" maxlength="60" />
		      		
					</td>
					<td align="right" width="10%">新意向等级：</td>
					<td>
					<input id="invite_way" name="invite_way" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${newShopLevel }"
						size="20" maxlength="60" />
					</td>
		      		
				</tr>
				<tr>
					<td align="right" width="12%">原销售流程进度：</td>
					<td width="15%">
						<div align="left">
							<input id="invite_way" name="invite_way" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${oldProgress }"
						size="20" maxlength="60" />
						</div>
					</td>
					<td align="right" width="12%">新销售流程进度：</td>
					<td width="12%">
					<input id="invite_way" name="invite_way" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${newProgress }"
						size="20" maxlength="60" />
					</td>
					<td align="right" width="10%">&nbsp;</td>
					<td width="15%">
					&nbsp;
					</td>
					<td align="right" width="10%">&nbsp;</td>
					<td>
						&nbsp;
					</td>
		      		
				</tr>
				<tr>
					<td align="right" colspan="1">到店结果记录：</td>
					<td align="left" colspan="5">
						<textarea rows="5" cols="70" id="invite_shop_info" name="invite_shop_info" style="background-color: #EEEEEE;" readonly>${tpp.remark}</textarea>
					</td>
				</tr>
				<tr>
					<td colspan="6">
						<div align="center">
							<input type="button" class="normal_btn" onclick="history.back();" value="返回" id="saveCompets" />
						</div>
					</td>
				</tr>
			</table>
</form>
</div>
</div>
</body>
</html>

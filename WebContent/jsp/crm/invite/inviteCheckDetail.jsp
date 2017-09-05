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
<title>邀约计划审核</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/invite/inviteCheck.js"></script>

<script type="text/javascript">
/**  弹出框设置   **/
function followOpenInfo(customerId){
	var openUrl = "<%=contextPath%>/crm/customer/CustomerManage/ctmUpdateInit.do?ctmId="+customerId;
	if(customerId != null && customerId != ""){
		OpenAddWindow(openUrl,800,600);
	}else
	MyAlert('数据发生错误，请联系管理员!');
}
</script>
</head>
<body  onload="loadcalendar();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 潜客管理 &gt; 邀约管理 &gt; 邀约计划审核</div>
<div style="background-color: #F3F4F8;height:800px;" >
<form id="fm" name="fm" method="post">
<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
<input type="hidden" name="dealerId" id="dealerId" value="${dealerId}" />
<input type="hidden" name="invite_id" id="invite_id" value="${tp.inviteId}" />

<input type="hidden" name="curPage" id="curPage" value="1" />
	<table class="table_query" border="0">
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
					<td width="3%" align="right">销售顾问信息:</td>
					<td width="22%">
						<input id="name" name="name" readonly="readonly" style="background-color: #EEEEEE;"
						type="text" class="middle_txt" datatype="1,is_textarea,30" value="${userName}"
						size="20" maxlength="60" />
					</td>
					<td align="right" width="15%">&nbsp;</td>
					<td width="25%">
						
					</td>
				</tr>
				<tr>
					<td align="right" colspan="1">邀约成效：</td>
					<td align="left" colspan="5">
						<textarea rows="5" cols="70" id="invite_info" name="invite_info" readonly="readonly" style="background-color: #EEEEEE;">${tp.remark }</textarea>
					</td>
					<td align="right"  ><a href="#" id="FOLLOW_ID" name="FOLLOW_ID" onclick='followOpenInfo("${tpc.customerId }")'> 客户资料维护</a></td>
				</tr>
				<tr>
					<td  align="center" colspan="2" >需求分析</td>
					<td  align="center" colspan="2">邀约目标</td>
					<td  align="center" colspan="2">赢得客户信任设计</td>
					<td  align="center" colspan="2" >感动客户情景设计</td>
				</tr>
				<tr>
					<td align="center" colspan="2">
							<textarea id="requirement" name="requirement" rows="6" cols="30"  >${tp.requirement}</textarea>
					</td>
					<td align="center" colspan="2" >
							<textarea id="inviteTarget" name="inviteTarget" rows="6" cols="30" > ${tp.inviteTarget}</textarea>
					</td>
					<td align="center" colspan="2">
							<textarea id="trustDesign" name="trustDesign" rows="6" cols="30" > ${tp.trustDesign}</textarea>
					</td>
					<td align="center" colspan="2">
							<textarea id="sceneDesign" name="sceneDesign" rows="6" cols="30" > ${tp.sceneDesign}</textarea>
					</td>
				</tr>
	</table>
	<br/><br/><br/>
	<table>
		<tr>
			<td align="right">计划邀约时间:</td>
			<td align="left">
				<input  type="text" id="planInviteDate"  name="planInviteDate" class="short_txt" readonly  value="${planInviteDate}"   />
                <input class="time_ico" type="button" onClick="showcalendar(event, 'planInviteDate', false);" />
				</td>
			<td align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;计划见面时间:</td>
			<td align="left">
			<input type="text" class="short_txt" name="planMeetDate" id="planMeetDate" readonly  value="${planMeetDate}"   />
			<input class="time_ico" type="button" onClick="showcalendar(event, 'planMeetDate', false);" />
			</td>
			<td colspan="4">&nbsp;</td>
		</tr>
		<tr>
			<td class="table_query_2Col_label_6Letter" align="right">主管审核:</td>
			<td align="left">
				<script type="text/javascript">
	              	  genSelBoxExp("checkStatus",6030,"",false,"mini_sel","","false",'');
	            </script>
			</td>
			<td class="table_query_2Col_label_6Letter" align="right">审核意见:</td>
			<td align="left">
				<textarea rows="6" cols="20" name="auditRemark"></textarea>
			</td>
			<td colspan="4">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="8">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="8">
				<center>
					<input type="button" class="normal_btn" onclick="auditSubmit();" value="保  存" id="addSub" />
					<input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />
				</center>
			</td>
		</tr>
	</table>
	
</form>
</div>
</div>
</body>
</html>

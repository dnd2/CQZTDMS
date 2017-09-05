<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>客户抱怨登记查询</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
   		__extQuery__(1);
	}
	function extQ(){
		var curPage = document.getElementById('curPage').value;
		__extQuery__(curPage);
	}
	function add(){
		var url="<%=contextPath%>/customerRelationships/complaintConsult/CustomerComplain/customerComplainAdd.do";
		fm.action = url;
		fm.submit();
	}	
	function detailDownload(){
		var url="<%=contextPath%>/customerRelationships/complaintConsult/CustomerComplain/queryCustomerComplain.json?cmd=1";
		fm.action = url;
		fm.submit();
	}
	function txtClr(){
		 document.getElementById('dealerCode').value="";
		 document.getElementById('dealerName').value="";
	}
	function myLink(value,metaDate,record){
		var status = record.data.STATUS;
		var ISDEALER = record.data.ISDEALER;
		
		if(ISDEALER=='true'){
			if(status!="3"){
				return String.format("<a href=\"<%=contextPath%>/customerRelationships/complaintConsult/CustomerComplain/customerComplainFollow.do?id="+value+"\">[跟进]</a>");
			}else{
				return String.format("已闭环 <a href=\"<%=contextPath%>/customerRelationships/complaintConsult/CustomerComplain/customerComplainCheck.do?id="+value+"\">[查看]</a>");
			}
		}else{
			if(status!="3"){
				return String.format("未闭环 <a href=\"<%=contextPath%>/customerRelationships/complaintConsult/CustomerComplain/customerComplainCheck.do?id="+value+"\">[查看]</a>");
			}else{
				return String.format("已闭环   <a href=\"<%=contextPath%>/customerRelationships/complaintConsult/CustomerComplain/customerComplainCheck.do?id="+value+"\">[查看]</a>");
			}
		}
    }
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 投诉咨询管理 &gt; 客户抱怨登记查询 </div>
	<form method="post" name = "fm" id="fm">
		<input type="hidden" id="curPage" name="curPage" value="${curP}">
		<input type="hidden" id="isDealer" name="isDealer" value="${isDealer}">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />查询条件</th>

			<tr>
			<c:if test="${isDealer == false }"><td align="right">选择服务商：</td>
				<td align="left"><input name="dealerCode" type="hidden" id="dealerCode" class="middle_txt" value="" /> <input name="dealerName" type="text" id="dealerName"
					class="middle_txt" value="" readonly="readonly" /> <input name="dlbtn" type="button" class="mini_btn"
					onclick="showOrgDealer('dealerCode','','true','','true','','10771002','dealerName');" value="..." /> <input type="button" class="normal_btn"
					onclick="txtClr('dealerCode','dealerName');" value="清 空" id="clrBtn" /></td></c:if>
			</tr>
			<tr>
				<td class="table_query_label" align="right">状态：</td>
				<td class="table_query_label" align="left">
						<select id="status" name="status">
						<option value="">-请选择-</option>
						<option value="1">未闭环</option>
						<option value="2">已闭环</option>
						</select>
						</td>
				<td align="right" >VIN：</td>
				<td align="left">
					<input class="middle_txt" type="text" id="vin" name="vin"/>
				</td>
			</tr>
				<tr>
				<td class="table_query_label" align="right">抱怨类型:</td>
				<td class="table_query_label" align="left"><script type="text/javascript">
					genSelBoxExp("COMPLAINT_TYPE",<%=Constant.COMPLAINT_TYPE%>, "", true,"short_sel", "", "false", '');
				</script></td>
				<td class="table_query_label" align="right">抱怨等级:</td>
				<td class="table_query_label" align="left"><script type="text/javascript">
					genSelBoxExp("COMPLAINT_LEVEL",<%=Constant.COMPLAINT_LEVEL%>, "", true,"short_sel", "", "false", '');
				</script></td>
			</tr>
			<tr>
				
				<td width="10%" align="right">登记日期：</td>
                <td width="22%" align="left"><input class="time_txt" id="SDENGJI_DATE" name="SDENGJI_DATE" style="width:65px"
                                                    datatype="1,is_date,10" maxlength="10"
                                                    group="SDENGJI_DATE,EDENGJI_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'SDENGJI_DATE', false);" type="button"/>
                    至
                    <input class="time_txt" id="EDENGJI_DATE" name="EDENGJI_DATE" datatype="1,is_date,10" style="width:65px"
                           maxlength="10" group="SDENGJI_DATE,EDENGJI_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'EDENGJI_DATE', false);" type="button"/></td>
				
				<td width="10%" align="right">跟进日期：</td>
                <td width="22%" align="left"><input class="time_txt" id="SGENJIN_DATE" name="SGENJIN_DATE" style="width:65px"
                                                    datatype="1,is_date,10" maxlength="10"
                                                    group="SGENJIN_DATE,EGENJIN_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'SGENJIN_DATE', false);" type="button"/>
                    至
                    <input class="time_txt" id="EGENJIN_DATE" name="EGENJIN_DATE" datatype="1,is_date,10" style="width:65px"
                           maxlength="10" group="SGENJIN_DATE,EGENJIN_DATE"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'EGENJIN_DATE', false);" type="button"/></td>
			</tr>
				<tr>
		<td nowrap="true" width="10%" align="right">故障描述:</td>
		<td colspan="8" nowrap="true" width="30%" align="left"><textarea name="guzhang_miaosu" id="guzhang_miaosu" rows="3" style="width: 55.25%;"></textarea></td>
	</tr>
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick=" __extQuery__(1);" />
					<c:if test="${isDealer == true }"><input class="normal_btn" type="button" value="新增" name="recommit" id="queryBtn" onclick=" add();" /></c:if>
					<c:if test="${isDealer != true }"><input class="normal_btn" type="button" value="明细导出" name="recommit" id="queryBtn" onclick=" detailDownload();" /></c:if>
        		</td>
			</tr>
		</table>
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>
<script type="text/javascript">
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/complaintConsult/CustomerComplain/queryCustomerComplain.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'center', renderer:getIndex},
				{header: "操作",dataIndex: 'ID',align:'center',renderer:myLink},
				{header: "登记日期",dataIndex: 'CREATE_DATE',align:'center'},
				{header: "跟进日期",dataIndex: 'GENJIN_SHIJIAN',align:'center'},
				{header: "服务商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "服务商名称",dataIndex: 'DEALER_NAME',align:'center'},
				
				{header: "抱怨时间",dataIndex: 'BAOYUAN_SHIJIAN',align:'center'},
				{header: "地点",dataIndex: 'ADDRESS',align:'center'},
				
				{header: "抱怨类型",dataIndex: 'COMPLAINT_TYPE',align:'center',renderer:getItemValue},
				{header: "抱怨等级",dataIndex: 'COMPLAINT_LEVEL',align:'center',renderer:getItemValue},
				{header: "车主姓名",dataIndex: 'CHEZHU_XINGMING',align:'center'},
				{header: "车主电话",dataIndex: 'CHEZHU_DIANHUA',align:'center'},
				{header: "车主地址",dataIndex: 'CHEZHU_DIZHI',align:'center'},
				{header: "VIN",dataIndex: 'VIN',align:'center'},
				{header: "品牌",dataIndex: 'PINPAI',align:'center'},
				{header: "车系",dataIndex: 'CHEXI',align:'center'},
				{header: "车型",dataIndex: 'CHEXING',align:'center'},
				{header: "配置",dataIndex: 'PEIZHI',align:'center'},
				{header: "颜色",dataIndex: 'COLOR',align:'center'},
				{header: "车辆用途",dataIndex: 'CHELIANG_YONGTU',align:'center'}
		      ];
</script>
</body>
</html>
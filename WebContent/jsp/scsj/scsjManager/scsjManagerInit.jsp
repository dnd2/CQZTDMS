<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>试乘试驾管理</title>
<script language="javascript" type="text/javascript">
	function doInit(){
			loadcalendar();  //初始化时间控件
			__extQuery__(1);
	}
</script>
</head>
<body>
  <form method="post" name ="fm" id="fm">
  <input id="flag" name="flag" type="hidden" value="${flag}">
    <input type="hidden" name="desId" id="desId" value="" />
    <input type="hidden" name="describe" id="describe" value="" />
    <input type="hidden" name="startDate" id="startDate" value="" />
    <input type="hidden" name=endDate id="endDate" value="" />
    <input type="hidden" name=actCode id="actCode" value="" />
    <input type="hidden" name=actType id="actType" value="" />
    <input type="hidden" name=partType id="partType" value="" />
	<div class="wbox">
		<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
			试乘试驾管理 &gt; 试乘试驾管理
		</div>
		<table class="table_query">
			<th colspan="10"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 查询条件</th>
				<tr>
					<td width="10%" align="right">操作：</td>
					<td width="20%" align="left"><select name="isChanged" id="isChanged" class="short_sel">
							<option selected value=''>-请选择-</option>
							<c:forEach items="${stateMapByDlr}" var="stateMapByDlr">
								<option value="${stateMapByDlr.key}">${stateMapByDlr.value}</option>
							</c:forEach>
					</select></td>
					<td width="10%" align="right">来源：</td>
					<td width="20%" align="left"><select name="fromInfo" id="fromInfo" class="short_sel">
							<option selected value=''>-请选择-</option>
							<c:forEach items="${fromInfo}" var="fromInfo">
								<option value="${fromInfo.FROMINFO}">${fromInfo.FROMINFO}</option>
							</c:forEach>
					</select></td>
										<td class="table_query_2Col_label_4Letter">预约时间：</td>
					<td align="left"><input class="short_txt" type="text" id="t1" name="beginTime" datatype="1,is_date,10" group="t1,t2" value="" /> <input class="time_ico" type="button"
						onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp; <input class="short_txt" type="text" id="t2" name="endTime" datatype="1,is_date,10" group="t1,t2"
						value="" /> <input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" /></td>
				</tr>
				<tr>
					<td align="center" colspan="10">
						<input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
						<input class="normal_btn" type="button" value="导 出" name="BtnQuery" id="queryBtn" onclick="download_()"/>
					</td>
				</tr>
			</table>
	</div>
	
	<!-- 查询条件 end -->
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
  </form>
  <script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/scsj/scsjManager/scsjManagerAction/scsjManagerQuery.json";
	
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'KSID', renderer:getIndex, style: 'text-align:center'},
                {id:'action',header: "操作",sortable: false,dataIndex: 'ROOT_ORG_ID',renderer:myLink},
				{header: "品牌", dataIndex: 'PPNAME',  style: 'text-align:left'},
				//{header: "车系名称", dataIndex: 'CXMC',  style: 'text-align:left'},
				{header: "车型", dataIndex: 'MOTONAME',  style: 'text-align:center'},
				{header: "意向购车时间", dataIndex: 'WANADATE',  style: 'text-align:center'},
				{header: "客户名称", dataIndex: 'CUSNAME',  style: 'text-align:left'},
				{header: "客户性别", dataIndex: 'SEX',  style: 'text-align:center',renderer: getItemValue},
				{header: "客户年龄", dataIndex: 'AGE',  style: 'text-align:center'},
				{header: "联系方式", dataIndex: 'CONTACT',  style: 'text-align:left'},
				{header: "信息来源", dataIndex: 'FROMINFO',  style: 'text-align:left'},
				{header: "省份", dataIndex: 'PROVINCE',  style: 'text-align:left'},
				{header: "市", dataIndex: 'CITY',  style: 'text-align:left'},
				{header: "区县", dataIndex: 'TOWN',  style: 'text-align:left'},
				{header: "预约时间", dataIndex: 'TIME1',  style: 'text-align:center'},
				{header: "是否分配", dataIndex: 'IS_ALLOCATION',  style: 'text-align:center',renderer: getItemValue},
				{header: "分配时间", dataIndex: 'TIME3',  style: 'text-align:center'},
				{header: "处理时间", dataIndex: 'TIME2',  style: 'text-align:center'},
				{header: "状态", dataIndex: 'STATUS',  style: 'text-align:center'}

		      ];
	     
	
	//设置超链接
	function myLink(value,meta,record)
	{
		var dealerId = record.data.DEALER_ID;
		var status = record.data.STATUS;
		var ksId = record.data.KSID;
		if(dealerId == ''||dealerId == null||dealerId == 'undefined'){
			if(status=='滞后'){
				return String.format("超过规定时效");
			}else{
				return String.format("<a href=\"#\" onclick='formod1(\""+ksId+"\")'>[抢单]</a>");
			}
		}else{
			if(status=='滞后'){
				return String.format("超过规定时效");
			}
			else if(status=='系统已处理'){
				return String.format("<a href=\"#\" onclick='formod2(\""+ksId+"\")'>[上报]</a>");
			}else{
				return String.format("已跟进");
			}
		}
	}
    function download_()
    {
		document.fm.action = "<%=contextPath%>/scsj/scsjManager/scsjManagerAction/scsjManagerQuery.do?cmd=1";
		document.fm.target="_self";
		document.fm.submit();
    }
    //抢单
    function formod1(ksId)
    {
    	btnDisable();
    	document.fm.action = "<%=contextPath%>/scsj/scsjManager/scsjManagerAction/mod1.do?ksId="+ksId;
		document.fm.target="_self";
		document.fm.submit();
    }
    
    //上报
    function formod2(ksId)
    {
//     	btnDisable();
    	document.fm.action = "<%=contextPath%>/scsj/scsjManager/scsjManagerAction/mod2.do?ksId="+ksId;
		document.fm.target="_self";
		document.fm.submit();
    }

	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
  </script>
</body>
</html>
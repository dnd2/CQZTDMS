<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
    String error = request.getParameter("error"); 
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>采购退货申请</title>
<script language="JavaScript">

	//初始化方法
	function doInit()
	{
		loadcalendar();  //初始化时间控件
	}

</script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：
		配件管理&gt; 配件仓储管理&gt; 配件退货管理&gt; 采购退货申请
</div>
<form method="post" name ="fm" id="fm" >
    <input type="hidden" name="curPage" id="curPage"/>
	<table class="table_query" bordercolor="#DAE0EE">
		<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 查询条件</th>
	     <tr>
	      <td width="20%"   align="right">采购订单号：</td>
	      <td width="30%"><input class="middle_txt" type="text"  name="ORDER_CODE"/></td>
	      <td width="20%"   align="right" >采购员：</td>
	      <td width="30%" ><input class="middle_txt" type="text"  name="BUYER"/></td>
        </tr>
	    <tr>
	       <td  width="20%"   align="right">制单时间：</td>
           <td width="30%">
           		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't1', false);"/>
           		&nbsp;至&nbsp;
           		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't2', false);"/>
          </td>
	      <td width="20%"   align="right" >库房：</td>
	      <td width="30%">
	      <select id="WH_ID" name="WH_ID">
	      <option value="">-请选择-</option>
	      <c:forEach items="${wareHouses}" var="wareHouse">
            <option value="${wareHouse.whId }">${wareHouse.whName }</option>
          </c:forEach>
	      </select>
          </td>
      </tr>
    <tr>
      <td width="20%"   align="right" >配件种类：</td>
      <td width="30%" >
        <script type="text/javascript">
		       genSelBoxExp("PART_TYPE",<%=Constant.PART_BASE_PART_TYPES %>,"",true,"short_sel","","false",'');
		</script>
    </td>
      <td width="20%"   align="right" >供应商：</td>
	  <td width="30%" >
	    <input class="middle_txt" type="text" readonly="readonly" id="VENDER_CODE" name="VENDER_CODE" />
	    <input class="mark_btn" type="button" value="&hellip;" onclick="showPartVender('VENDER_CODE','VENDER_ID','false')"/>
	    <INPUT class=normal_btn onclick="clearInput();" value=清除 type=button name=clrBtn>
	    <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
	  </td>
	</tr>
	
	<tr>
      <td width="20%"   align="right" >配件编码：</td>
      <td width="30%" ><input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE"/></td>
	  <td width="20%" class=table_query_2Col_label_6Letter>配件名称：</td>
	  <td width="30%"><input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME"/></td>
    </tr>
    
	<tr>
       <td width="20%"   align="right">入库时间：</td>
           <td width="30%">
           		<input name="inBeginTime" id="t3" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't3', false);"/>
           		&nbsp;至&nbsp;
           		<input name="inEndTime" id="t4" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t3,t4">
           		<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" onclick="showcalendar(event, 't4', false);"/>
          </td>
	  <td width="20%"  class=table_query_2Col_label_6Letter>入库人员：</td>
	  <td width="30%" ><input name="IN_NAME" type="text" class="middle_txt" id="IN_NAME"/></td>
    </tr>
	
    <tr>
     <td align="center" colspan="4"><input name="BtnQuery" id="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询"/>
	&nbsp;<input name="button" type="button" class="long_btn" onclick="setCheckModel();" value="生成退货申请"/>
</td>
   </tr>
	</table>
	
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>

<script type="text/javascript" >
    //autoAlertException();//输出错误信息
	var myPage;
	var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/queryOrderInInfo.json";
				
	var title = null;

    var inIdArr = new Array();
    var inIds = "";
	var columns = [
{header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"ids\")' />", width:'8%',sortable: false,dataIndex: 'IN_ID',renderer:myCheckBox},
				{header: "采购订单号", dataIndex: 'ORDER_CODE', align:'center'},
				{header: "入库单号", dataIndex: 'IN_CODE', align:'center'},
				{header: "制单日期", dataIndex: 'CREATE_DATE', align:'center',renderer:formatDate},
				{header: "采购员", dataIndex: 'BUYER', align:'center'},
				{header: "配件类型", dataIndex: 'PART_TYPE', align:'center',renderer:getItemValue},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
				{header: "入库库房", dataIndex: 'WH_NAME', align:'center'},
				{header: "入库数量", dataIndex: 'IN_QTY', align:'center'},
                {header: "已申请退货数量", dataIndex: 'APPLY_QTY', align:'center'},
				{header: "已退货数量", dataIndex: 'RETURN_QTY', align:'center'},
				{header: "供应商名称", dataIndex: 'VENDER_NAME', align:'center'},
				{header: "单价", dataIndex: 'BUY_PRICE', align:'center'},
				{header: "金额", dataIndex: 'IN_AMOUNT', align:'center'},
				{header: "入库人员", dataIndex: 'IN_NAME', align:'center'},
				{header: "入库时间", dataIndex: 'IN_DATE', align:'center',renderer:formatDate},
				{header: "是否已结算", dataIndex: 'IS_BALANCES', align:'center',renderer:getItemValue}
		      ];

//设置超链接  begin      
	
	//全选checkbox
	function myCheckBox(value,metaDate,record)
	{   
			return String.format("<input type='checkbox' name='ids' value='" + value + "'/>");
    }

	//格式化日期
	function formatDate(value,meta,record){
		var output = value.substr(0,10);
		return output;
	}

	function setCheckModel()
	{
		
		var chk = document.getElementsByName("ids");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{            
				cnt++;
				inIdArr.push(chk[i].value);
			}
		}
      if(cnt==0)
      {
    	  MyAlert("请选择要退货的入库单！");
           return;
      }
      inIds = inIdArr.join(',');
      MyConfirm("确认生成采购退货申请？",generateApply);
	}
	
	//生成申请
	function generateApply(){
        btnDisable();
		window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/generateApplyInit.do?ids='+inIds;
	}
	
	function clearInput() {
		//清空选定供应商
		document.getElementById("VENDER_ID").value = '';
		document.getElementById("VENDER_CODE").value = '';
	}
</script>
</div>
</body>
</html>
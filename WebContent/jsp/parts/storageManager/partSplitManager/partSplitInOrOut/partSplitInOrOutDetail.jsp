<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件拆合出入库明细</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
	<div class="navigation"> <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	  配件管理&gt;配件拆合件管理&gt;配件拆合件出入库&gt;查看
	</div>
<form name="fm" id="fm" method="post"  enctype="multipart/form-data">
<input type="hidden" name="ioStockId" id="ioStockId" value="${po['IOSTOCK_ID'] }"/>
	<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" >
		<th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />信息</th>
	     <tr>
	      <td width="10%"   align="right">拆合单号：</td>
	      <td width="20%">${po['SPCPD_CODE'] }
	      </td>
	      <td width="10%"   align="right">制单单位：</td>
	      <td width="20%">${po['ORG_CNAME'] }
	      </td>
	      <td  width="10%"  align="right">制单人：</td>
	      <td  width="20%">${po['NAME'] }
	      </td>
	    </tr>
	    <tr>
	        <td width="10%"   align="right" >仓库：</td>
      		<td width="20%">
            ${po['WH_CNAME'] }
        	</td>
        	 <td width="10%"   align="right">拆合类型：</td>
      <td width="20%">
      ${po['SPCPD_TYPE1'] }
      <input type="hidden" id="SPCPD_TYPE" name="SPCPD_TYPE" value="${po['SPCPD_TYPE'] }"/>
      </td>
      </tr>
	</table>
	 <table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr>
      <th colspan="7" align="left"><img class="nav" src="<%=contextPath%>/img/nav.gif" />配件出入库信息
	  </th>
    </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
    <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<table class="table_edit">
	<tr>
    	<td align="center">
            <input type="button" name="saveBtn" id="saveBtn" value="返 回" 

onclick="javascript:goback();"  class="normal_btn"/>
        </td>
    </tr>
  </table>
</form>
<script type="text/javascript" >

autoAlertException();
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partSplitManager/PartSplitInOrOutManager/querySpiltInOrOutDetail.json";
				
var title = null;

var columns = [
				{header: "序号", align:'center', renderer:getIndex,width:'7%'},
				{header: "配件件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
				{header: "规格", dataIndex: 'UNIT', align:'center'},
				{header: "是否总成件", dataIndex: 'IS_MB', align:'center',renderer:getItemValue},
				{header: "拆分数量", dataIndex: 'SPLIT_QTY', align:'center'},
				//{header: "成本比例", dataIndex: 'SPLIT_RATE', align:'center'},
				{header: "库存数量", dataIndex: 'NORMAL_QTY', align:'center'},
				{header: "出入库数量", dataIndex: 'QTY', align:'center', renderer:insertQtyInput},
				{header: "金额", dataIndex: 'AMOUNT', style: 'text-align:right'},
				{header: "货位", dataIndex: 'LOC_NAME', align:'center'},
				{header: "出入库日期", dataIndex: 'CREATE_DATE', align:'center',renderer:formatDate}
			  ];

//格式化日期
function formatDate(value,meta,record){
	var output = value.substr(0,10);
	return output;
}

function insertQtyInput(value, meta, record) {
    var isMb = record.data.IS_MB;
    var stype = ${po['SPCPD_TYPE']};
    if(stype==<%=Constant.PART_SPCPD_TYPE_01%>){//如果是拆件
    	if(isMb==<%=Constant.IF_TYPE_YES%>){//如果是总成件
        	return "-"+value+"(出)";
        }else{
            return value+"(入)";
        }
    }else{//如果是合件
    	if(isMb==<%=Constant.IF_TYPE_YES%>){//如果是总成件
        	return value+"(入)";
        }else{
            return "-"+value+"(出)";
        }
    }
}

//返回查询页面
function goback(){
	window.location.href = '<%=contextPath%>/parts/storageManager/partSplitManager/PartSplitInOrOutManager/queryPartSplitApplyInit.do';
}
</script>
</div>
</body>
</html>

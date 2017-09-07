<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>运输方式维护</title>
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/parts/baseManager/logisticsManage/TransportInfoAction/transportInfoQuery.json";
	var title = null;
	var columns = [
 				{header: "序号",align:'center',renderer:getIndex},
 				{header: "出发地-省",dataIndex: 'PLACE_PROVINCE_ID',align:'center',renderer:getRegionName},
 				{header: "出发地-市",dataIndex: 'PLACE_CITY_ID',align:'center',renderer:getRegionName},
 				{header: "出发地-区",dataIndex: 'PLACE_COUNTIES',align:'center',renderer:getRegionName},
 				{header: "目的地-省",dataIndex: 'DEST_PROVINCE_ID',align:'center',renderer:getRegionName},
 				{header: "目的地-市",dataIndex: 'DEST_CITY_ID',align:'center',renderer:getRegionName},
 				{header: "目的地-区",dataIndex: 'DEST_COUNTIES',align:'center',renderer:getRegionName},
				{header: "承运商",dataIndex: 'LOGI_FULL_NAME',align:'center' },
				{header: "运输方式",dataIndex: 'TRANSPORT_CODE',align:'center',renderer:getItemValue},
				{header: "计价方式",dataIndex: 'VALUATION_CODE',align:'center',renderer:getItemValue},
				{header: "价格",dataIndex: 'PRICE',align:'center'},
				{header: "状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
				{header: "操作",sortable: false, dataIndex: 'PK_ID', align:'center',renderer:myLink}
		      ];

	//设置超链接
	function myLink(value,meta,record){
  		return String.format("<a href=\"#\" onclick='updTransportInfo(\""+value+"\")'>[修改]</a>");
	}
	
	//修改页面
	function updTransportInfo(value){
	 	window.location.href='<%=contextPath%>/parts/baseManager/logisticsManage/TransportInfoAction/transportInfoUpdInit.do?Id='+value;  
	}
	
	//跳转新增页面
	function addTransportInfo(){
		fm.action = "<%=contextPath%>/parts/baseManager/logisticsManage/TransportInfoAction/transportInfoAddInit.do";
		fm.submit();
	}
    function setItemValue(selectName, objItemValue) {
        var objSelect = document.getElementById(selectName);
        if(!objSelect) {return;}
        if(!objItemValue || objItemValue == '-1' || objItemValue == '') {return;}

        for (var i = 0; i < objSelect.options.length; i++) {
            if (objSelect.options[i].value == objItemValue) {
                objSelect.options[i].selected = true;
                break;
            }
        }
    }
    
    $(document).ready(function(){
    	//出发地
        genLocSel('PROVINCE_ID','CITY_ID','COUNTIES');
        
        var p = document.getElementById("PROVINCE_ID");
        setItemValue('PROVINCE_ID', '${dMap.PROVINCE_ID}');
        _genCity(p,'CITY_ID');
        
        var c = document.getElementById("CITY_ID");
        setItemValue('CITY_ID', '${dMap.CITY_ID}');
        _genCity(c,'COUNTIES');
        
        var t = document.getElementById("COUNTIES");
        setItemValue('COUNTIES', '${dMap.COUNTIES}');
    	//目的地
        genLocSel('PROVINCE_ID1','CITY_ID1','COUNTIES1');
    	
        var p1 = document.getElementById("PROVINCE_ID1");
        setItemValue('PROVINCE_ID1', '${dMap.PROVINCE_ID}');
        _genCity(p,'CITY_ID1');
        var c1 = document.getElementById("CITY_ID1");
        setItemValue('CITY_ID1', '${dMap.CITY_ID}');
        _genCity(c,'COUNTIES1');
        var t1 = document.getElementById("COUNTIES1");
        setItemValue('COUNTIES1', '${dMap.COUNTIES}');
        
        __extQuery__(1);
    });

    

</script>
</head>
<body>
<form name="fm" method="post" id="fm">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理 &gt;基础信息管理 &gt; 配件基础信息维护 &gt; 运输方式维护</div>
<div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	<table class="table_query" id="subtab" >
	<tr>
		  <td class="right" >承运商：</td>
	   	  <td class="left">
			  <select  name="logiCode" id = "logiCode" class="u-select" >
	       		<option  value="">-请选择-</option>
			   	<c:forEach items="${logisticsList}" var="logi">
					<option  value="${logi.LOGI_CODE}">${logi.LOGI_FULL_NAME}</option>
				</c:forEach>
	      	</select>
		  </td> 
		  <td class="right" >运输计划类型：</td> 
		  <td class="left">
			   <select  name="tvCode" id = ""tvCode"" class="u-select" >
	       		<option  value="">-请选择-</option>
			   	<c:forEach items="${modeList}" var="mode">
					<option  value="${mode.TV_ID}">${mode.TV_NAME}</option>
				</c:forEach>
	      	</select>
		  </td>  
		  <td></td>
		  <td></td>
	 </tr>
	  <tr>
	    <td class="right">出发地 省份：</td>
	    <td class="left"><select class="u-select" id="PROVINCE_ID" name="PROVINCE_ID" onchange="_genCity(this,'CITY_ID')"></select></td>
	    <td class="right">城市：</td>
	    <td class="left"><select class="u-select" id="CITY_ID" name="CITY_ID" onchange="_genCity(this,'COUNTIES')"></select></td>
	    <td class="right">区县：</td>
	    <td class="left"><select class="u-select" id="COUNTIES" name="COUNTIES" ></select></td>
	</tr>
	<tr>
	    <td class="right">目的地 省份：</td>
	    <td class="left"><select class="u-select" id="PROVINCE_ID1" name="PROVINCE_ID1" onchange="_genCity(this,'CITY_ID1')"></select></td>
	    <td class="right">城市：</td>
	    <td class="left"><select class="u-select" id="CITY_ID1" name="CITY_ID1" onchange="_genCity(this,'COUNTIES1')"></select></td>
	    <td class="right">区县：</td>
	    <td class="left"><select class="u-select" id="COUNTIES1" name="COUNTIES1" ></select></td>
	</tr>
	  <tr>
	  <td colspan="6" class="center">
	    	  <input type="button" id="queryBtn" class="u-button"  value="查询" onclick="__extQuery__(1);" />   	
	    	  <input type="button" id="queryBtn" class="u-button"  value="新增" onclick="addTransportInfo();" />  	
	  		 <input type="reset" class="u-button" id="resetButton"  value="重置"/>
	    </td>
	  </tr>
	</table>
</div>
</div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</div>
</form>
 
</body>
</html>

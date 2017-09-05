<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
	String reFlag = (String)request.getAttribute("REFLAG");
	
%>
<script type="text/javascript">


var count=0;


</script>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>辅料费维护</title>
</head>
<body onload="doinit()">
<form name='fm' id='fm'>
<input type="hidden" name="wrmodelgrouplist" id="wrmodelgrouplist" value="<%=request.getAttribute("wrmodelgrouplist")%>"/>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;辅料费维护</div>
   <table  class="table_query">
			<tr>
				<td align="right">工时代码：</td>
				<td><input name="workhour_code" id="workhour_code" value="" type="text"
					class="middle_txt" /></td>
				<td align="right">工时名称：</td>
				<td><input name="workhour_name" id="workhour_name" value="" type="text"
					class="middle_txt" /></td>
			</tr>
			<tr>    
		   <td colspan="4" align="center">
            <input class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1)"/>
            <input class="normal_btn" type="button" value="新增" name="add" onclick="subFun();"/>
            <input class="normal_btn" type="button" onclick="goImport();" value="批量导入"/>
           </td>
           </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
</form>  
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/AccPriceMaintainMain/dateQuery.json";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: '工时代码',align:'left',dataIndex:'WORKHOUR_CODE'},
				{header: '工时名称',align:'left',dataIndex:'WORKHOUR_NAME'},
				{header: "金额",sortable: false,dataIndex: 'PRICE',align:'right',renderer:getPriceText},
				{header: "创建人",sortable: false,dataIndex: 'ADD_BY',align:'left'},
				{header: "创建时间",sortable: false,dataIndex: 'ADD_TIME',align:'center'},
				{header: "修改人",sortable: false,dataIndex: 'UPDATE_BY',align:'left'},
				{header: "修改时间",sortable: false,dataIndex: 'UPDATE_TIME',align:'center'},
				{header: "状态",sortable: false,dataIndex: 'STATUS',align:'center',renderer:mySelect},
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];  
    
	function mySelect(value,meta,record){
		var id = record.data.ID;
		var status=record.data.STATUS;
		if(status==10011001){
			return String.format("<select name='status_"+id+"'><option value='10011001'>有效</option><option value='10011002'>无效</option></select>");
		}
		if(status==10011002){
			return String.format("<select name='status_"+id+"'><option value='10011002'>无效</option><option value='10011001'>有效</option></select>");
		}
	}
	function subDate(value,meta,record){
		var POLICY_START_DATE = record.data.POLICY_START_DATE;
		var aa = POLICY_START_DATE.substring(0,11);
	 	return String.format(aa);
	}
	function subDate1(value,meta,record){
		var POLICY_END_DATE = record.data.POLICY_END_DATE;
		var aa = POLICY_END_DATE.substring(0,11);
	 	return String.format(aa);
	}
	
	function goImport(){
		location = '<%=request.getContextPath()%>/claim/basicData/AccPriceMaintainMain/inportPer.do' ;
	}
	
//修改的超链接设置
function myLink(value,meta,record){
	var price = record.data.PRICE;
	var status = record.data.STATUS;
<%-- 	return String.format("<a href='<%=contextPath%>/claim/basicData/AccPriceMaintainMain/modInit.do?ID="+value+"'>[修改]</a>"); --%>
		return String.format("<a href=\"#\" onclick='modPrice(\""+value+"\")'>[修改]</a>");
	
}
function modPrice(value) {
	var url="<%=contextPath%>/claim/basicData/AccPriceMaintainMain/modInit.json?ID="+value;
	makeNomalFormCall(url,returnBack,'fm','queryBtn');
}
function returnBack(json){
	var ok = json.ok;
	var priceError = json.priceError;
	if(ok =='ok'){
		MyAlert("修改成功!");
		__extQuery__(1);
	}else if(priceError==true){
		MyAlert("请输入正确的金额!");
		return;
	}else{
		MyAlert("修改金额失败!");
		return;
	}
}
function doinit(){
	__extQuery__(1);
}
function getPriceText(value, meta, record) {
	var id = record.data.ID
// 	onchange='onchangeVlidateSaleQty(this," + value + ")'
// 	return "<input name='price_" + id + "' id='price_" + id + "' datatype='0,is_double' decimal='2' value='" + value + "'  style='background-color:#FFFFCC;text-align:right' class='middle_txt'>";

    if (value != null) {
        return "<input name='price_" + id + "' id='price_" + id + "' type='text' value='" + value + "' onchange='onchangeVlidateSaleQty(this," + value + ")' style='background-color:#FFFFCC;text-align:right' class='middle_txt'   >";
    } else {
        return "<input name='price_" + id + "' id='price_" + id + "' type='text' value='" + value + "' readonly='readonly' onchange='onchangeVlidateSaleQty(this)' style='background-color:#FFFFCC;text-align:right' class='middle_txt'  >";
    }
}

function onchangeVlidateSaleQty(obj,oldPrice) {
	
    if (obj.value == "") {
        return;
    }
    var re = /^\d+(?:\.\d{0,2})?$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入数字保留2位小数点!");
        obj.value = oldPrice;
        return;
    }
//     var idx = obj.parentElement.parentElement.rowIndex;
//     var tbl = document.getElementById('myTable');

//     if (obj.value != '0') {
//         tbl.rows[idx].cells[1].firstChild.checked = true;
//     } else {
//         tbl.rows[idx].cells[1].firstChild.checked = false;
//     }
}
//新增
function subFun(){
	location="<%=contextPath%>/claim/basicData/AccPriceMaintainMain/addInit.do";   
}
</script>
</body>
</html>
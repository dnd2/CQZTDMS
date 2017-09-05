<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔其它费用维护</title>
</head>
<body>
<form name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置1： 售后服务管理&gt;索赔基础数据&gt;开票概率维护</div>
   <table  class="table_query">
   			<tr>
	          	<td class="table_query_2Col_label_4Letter">税率名称：</td>
	          	<td><input name="INVOICE_NAME" type="text" id="INVOICE_NAME"  datatype="1,is_null,20"  class="middle_txt" /></td>
	          	<td class="table_query_2Col_label_4Letter">状态：</td>
	          	<td><input name="STATUS" type="text" id="STATUS"  datatype="1,is_null,30"  class="middle_txt"/></td> 				
   			</tr>
		   <tr>    
		   <td  colspan="4" align="center">
            <input class="normal_btn" type="button" name="button1" value="查询"  onclick="__extQuery__(1)"/>
            <input class="normal_btn" type="button" value="新增" name="add" onclick="subFun();"/>
           </td></tr>
       
  </table>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<%-- <form name="form1" style="display:none"> --%>
<%-- <table align="center"  class=csstable >--%>
<%--	  <tr>--%>
<%--	  		<td align="center"><input type="button" name="return1"  class="normal_btn" value="全部下发" onclick=""/></td>--%>
<%--	  </tr>--%>
<%--  </table>--%>
<%-- </form>  --%>
<script type="text/javascript" >
<%--document.form1.style.display = "none";--%>
<%--var HIDDEN_ARRAY_IDS=['form1'];--%>
var myPage;  
	var url = "<%=request.getContextPath()%>/claim/basicData/TreeGuaranteesStrategy/invoiceQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "税率类型",sortable: false,dataIndex: 'INVOICE_NAME',align:'center'},
				{header: "税率",sortable: false,dataIndex: 'TAX_RATE',align:'center'},
				{header: "状态",sortable: false,dataIndex: 'STATUS',renderer:getItemValue,align:'center'},
				{header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
		      ];

//设置超链接  begin
	
	//跳转到修改页面
	function myLink(value,meta,record){
		return String.format("<a href=\"<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/invoiceAddInit.do?ID="
				+ value + "\" >[修改]</a>");
		//return String.format("<a href=\"#\" onclick=\"toUpdatePage("+value+")\">[修改]</a>");
	}
	
//设置超链接 end
  //新增
  function subFun(){
    location="<%=contextPath%>/claim/basicData/TreeGuaranteesStrategy/invoiceAddInit.do";   
  }
//删除方法：
function sel(str){
	MyConfirm("是否确认删除？",del,[str]);
}  
//删除
function del(str){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimOtherFeeMain/claimOtherFeeDel.json?FEE_ID='+str,delBack,'fm','');
}
//删除回调方法：
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyAlert("删除成功！");
		__extQuery__(1);
	} else {
		MyAlert("删除失败！请联系管理员！");
	}
}  
//下发方法：
function sendAll(){
	makeNomalFormCall('<%=contextPath%>/claim/basicData/ClaimOtherFeeMain/sendAll.json',sendAllBack,'fm','');
}
//下发后的回调方法：
function sendAllBack(json) {
	if(json.success != null && json.success == "true") {
	    if(json.returnValue != null && json.returnValue.length > 0){
			MyAlert("新增成功！但经销商："+json.returnValue+"原先已经维护了！");
	    }else {
	    	MyAlert("下发成功 !");
	    }
	} else {
		MyAlert("下发失败！请联系管理员！");
	}
}
</script>
</body>
</html>
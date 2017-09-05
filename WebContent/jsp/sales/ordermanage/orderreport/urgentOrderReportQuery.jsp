<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/orderNumberFormat.js"></script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>补充订单提报</title>
<script type="text/javascript">
function doInit(){
   __extQuery__(1);
	var choice_code = document.getElementById("orderYearWeek").value;
	showData(choice_code);
}   
</script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单提报 > 批售订单提报</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
  <%-- <c:if test="${isFlag == 0 }">
		<tr>
				<td colspan="3">
					<strong>
						<font color="red">
<pre>新代收代发审核流程:
     经销商启票(选择代收代发)---各基地业务人员审核具体地址,选择代收单位----事业部销售经理确认审核---各基地进行启票</pre>
						</font>
					</strong>
				</td>
		</tr>
	</c:if> --%>
	<%--  <tr>
	    <td width="37%" align="right" nowrap>订单周度：   </td>
	    <td width="43%" class="table_query_2Col_input" nowrap>
		    <select name="orderWeek" id="orderYearWeek" onchange="showData(this.value);">
		      <c:forEach items="${dateList}" var="po"> 
		      <c:choose>   
    				<c:when test="${po.code==myWeek}" >   
							<option value="${po.code}" selected="selected">${po.name}</option>
					</c:when>
					<c:otherwise>  
							<option value="${po.code}">${po.name}</option> 
					</c:otherwise>
					</c:choose>
			  </c:forEach>
	        </select>
	        <span id="data_start" class="innerHTMLStrong"></span>
			<span id="data_end" class="innerHTMLStrong"></span>
        </td>
	    <td width="20%" align=left nowrap>&nbsp;</td>
    </tr> 
    <tr>
      <td align="right" nowrap></td>
      <td align="right" nowrap></td>
      <td align="left" nowrap></td>
    </tr>
	<tr>
	  <TD align="right" nowrap>业务范围：</TD>
		<TD class="table_query_2Col_input" nowrap>
			<select name="areaId">
				<option value="">-请选择-</option>
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}|${po.DEALER_ID}">${po.AREA_NAME}</option>
				</c:forEach>
            </select>
        </TD>
		<td align=left nowrap>&nbsp;</td>
	</tr>  --%>
	<tr>
	   <td  width="100%"  class="table_query_3Col_input"><div  align="center">
	   	<input id="queryBtn" name="button22" type=button class="normal_btn" onClick="__extQuery__(1);" value="查询">
	   	<input id="addBtn" name="button22" type=button class="normal_btn" onClick="loginAdd();" value="新增">
	   	</div>
	   </td>
	</tr>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
</div>
<script type="text/javascript">
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/urgentOrderReoprtQuery.json";
				
	var title = null;
	var arr1=null;
	var arr2=null;
	var arr3=null;
	var columns = [
				{id:'action',header: "操作",sortable: false,dataIndex: 'ORDER_ID',renderer:myLink ,align:'center'},
				{header: "订货方", dataIndex: 'ORDER_ORG_NAME', align:'center'},
				{header: "开票方", dataIndex: 'BILLING_ORG_NAME', align:'center'},
				{header: "批售订单号", dataIndex: 'ORDER_NO', align:'center'},
				{header: "制单日期", dataIndex: 'QUOTA_DATE', align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center' ,renderer:getItemValue},
				{header: "订单状态", dataIndex: 'ORDER_STATUS', align:'center' ,renderer:getItemValue},
				{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'}
				
		      ];		         
	
	//修改的超链接
	function myLink(value,meta,record){
		//<a href='#' onclick='confirmSubmit(\""+ value +"\","+data.ORDER_AMOUNT+",\""+data.ORDER_NO+"\")'>[提报]</a>
		var data = record.data;
  		return String.format("<a href='#'  class='u-anchor' onclick='loginMod(\""+ value +"\",\""+ data.ORDER_NO +"\")'>[修改]</a><a href='#'  class='u-anchor' onclick='confirmDel(\""+ value +"\")'>[删除]</a><a href='#'  class='u-anchor' onclick='confirmSubmit(\""+ value +"\","+data.ORDER_AMOUNT+",\""+data.ORDER_NO+"\")'>[提报]</a>");
	}
	
	function loginAdd(){
	 	document.fm.action='<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/urgentOrderReoprtAddPre.do';
		document.fm.submit();
	}
	
	function loginMod(arg,orderNO){
		arr1=new Array();
		arr1[0]=arg;
		arr1[1]=orderNO;
		var lockUrl='<%=request.getContextPath()%>/util/ResourceLock/checkIsOper.json?&billId='+arg;
		makeNomalFormCall(lockUrl,lockReturn1,'fm');
		
	}
	function lockReturn1(json){
		if(json.flag=="1"){
				MyAlert("有其他人操作");
				return;
			}else{
				document.fm.action = '<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/urgentOrderReoprtModPre.do?orderId='+arr1[0]+'&orderNO='+arr1[1];
		 		document.fm.submit();
		}
	}
	
	function confirmSubmit(arg,amount,orderNO){
		arr2=new Array();
		arr2[0]=arg;
		arr2[1]=amount;
		arr2[2]=orderNO;
		var lockUrl='<%=request.getContextPath()%>/util/ResourceLock/checkIsOper.json?billId='+arg;
		makeNomalFormCall(lockUrl,lockReturn2,'fm');
	}
	
	function lockReturn2(json){
		if(json.flag=="1"){
			MyAlert("有其他人操作");
			return;
		}else{
			MyConfirm("是否确认提交?",orderSubmit,[arr2[0],arr2[1],arr2[2]]);
		}
		
	}
	function orderSubmit(arg){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/urgentOrderReoprtSubmit.json?orderId='+arg[0]+'&amount='+arg[1]+'&orderNO='+arg[2],showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
			__extQuery__(1);
		}else if(json.returnValue == '2'){
			MyAlert("单据状态不对，无法提报！！！");
			__extQuery__(1);
		}else{
			MyAlert("提交失败！可用余额不足！");
		}
	}
	
	//删除方法：
	function confirmDel(arg){
		arr3=new Array();
		arr3[0]=arg;
		var lockUrl='<%=request.getContextPath()%>/util/ResourceLock/checkIsOper.json?billId='+arg;
		makeNomalFormCall(lockUrl,lockReturn3,'fm');
		
	}  
	function lockReturn3(json){
		if(json.flag=="1"){
			MyAlert("有其他人操作");
			return;
		}else{
			MyConfirm("确认删除？",del,[arr3[0]]);
		}
		
	}
	//删除
	function del(arg){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/urgentOrderReoprtDel.json?orderId='+arg,delBack,'fm');
	}
	//删除回调方法：
	function delBack(json) {
		if(json.returnValue == '1') {
			MyAlert("删除成功！");
			__extQuery__(1);
		}else if(json.returnValue == '2'){
			MyAlert("状态不对，无法删除！");
			__extQuery__(1);
		}else {
			MyAlert("删除失败！请联系管理员！");
		}
	} 
	function showData(choice_code){
		var data_start = "";
		var data_end = "";
		<c:forEach items="${dateList}" var="list">
			var code = "${list.code}";
			if(choice_code+"" == code+""){
				data_start = "${list.date_start}";
				data_end = "${list.date_end}";
			}
		</c:forEach>
		if(data_start){
			document.getElementById("data_start").innerHTML = data_start+"  至  ";
			document.getElementById("data_end").innerHTML = data_end;
		}
	}
</script>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/jmstyle/jslib/dealer.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title> 订单账号审核查询</title>
<script type="text/javascript">
function doInit(){

}   
</script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单审核账号管理 > 订单审核账号查询</div>
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/> 订单审核账号新增</h2>
<div class="form-body">
<form method="POST" name="fm" id="fm">
<input type="hidden" id="orderAcntId" name="orderAcntId" />
<input type="hidden" id="largeIds" name="largeIds" value="${largeIds }" />
  <table class="table_query">
  	<tr>
  		<td  align="right" style="width:10%"><div align="right">大区代码:</div></td>
  		<td  align="right" style="width:15%"><input type="text" class="middle_txt" id="largeCode"  name="largeCode"  value=""/></td>
  		<td   align="right"style="width:10%"><div align="right">大区名称:</div></td>
  		<td   align="right" style="width:15%"><input type="text" class="middle_txt"  id="largeName" name="largeName"  value=""/></td>
  	</tr>
	<tr>
	   <td  width="100%" colspan="4" class="table_query_3Col_input"><div  align="center">
	   	<input id="queryBtn" name="button22" type=button class="normal_btn" onClick="__extQuery__(1);" value="查询">
	   	<input id="queryBtn" name="button22" type=button class="normal_btn" onClick="setCheckModel();" value="确认">
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
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderacnt/OrderAcntQuery/orderLargeAddQuery.json";
				
	var title = null;
	var columns = [
				{header: "选择",   dataIndex: 'ORG_ID', width:"10px",renderer:myCheck},
				{header: "大区代码", dataIndex: 'ORG_CODE', align:'center'},
				{header: "大区", dataIndex: 'ORG_NAME', align:'center'}
		      ];		         
	
	//修改的超链接
		function myCheck(value,metadate,record){
		var data = record.data;
		return "<input type='checkbox' name='cb' id=\""+data.ORG_ID+"\"  value=\""+data.ORG_ID+"\" />"

		//return "<input type='checkbox' name='cb' onclick='checkAcnt(\""+data.ORG_ID+"\",\""+data.ACNT+"\");' id=\""+data.USER_ID+"\"  value=\""+data.USER_ID+"\" /><input type='hidden'  value=\""+data.USER_ID+"\" />"
	}
	
		function setCheckModel(){
			var reCode="";
			var materialCheckBoxs=document.getElementsByName("cb");
			if(!materialCheckBoxs)return;
			for(var i=0;i<materialCheckBoxs.length;i++)
			{
				if(materialCheckBoxs[i].checked)
				{
					if(reCode && reCode.length > 0)
						reCode += "," + materialCheckBoxs[i].value;
					else
						reCode = reCode+materialCheckBoxs[i].value;
			    }
			}
		    var arr=reCode.split(",");
		    var largeIds=parent.document.getElementById('inIframe').contentWindow.document.getElementById('largeIds').value
		   for(var i=0;i<arr.length;i++){
		       	parent.document.getElementById('inIframe').contentWindow.document.getElementById('largeId').value=arr[i];
		        parent.document.getElementById('inIframe').contentWindow.addLarge();
		    }
			//parent.document.getElementById('inIframe').contentWindow.document.getElementById('largeIds').value=reCode;
			_hide();
		}
	
	function checkAcnt(userId,name){
		var id = document.getElementsByName("cb");
		document.getElementById("orderAcntId").value=userId;
		parentContainer.document.getElementById("orderAcnt").value = name;
		 _hide();
	}
	
	function orderAcntAdd(){
	 	document.fm.action='<%=request.getContextPath()%>/sales/ordermanage/orderacnt/OrderAcntQuery/orderAcntAddInit.do';
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
	function orderSubmit(arg,amount,orderNO){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/urgentOrderReoprtSubmit.json?orderId='+arg+'&amount='+amount+'&orderNO='+orderNO,showResult,'fm');
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

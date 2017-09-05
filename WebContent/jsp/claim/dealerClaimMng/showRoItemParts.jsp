<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动- 活动配件选择</title>
<% String contextPath = request.getContextPath();%>
</head>

<body>
	<div class="navigation">
			<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;经销商索赔管理&gt;索赔单编辑&gt;工单配件选择
	</div> 
  <form name="fm" id="fm">
	    <input type="hidden" id="partNo" name="partNo"></input>
	    <input type="hidden" id="partName" name="partName"></input>
	    <input type="hidden" id="PART_QUANTITY" name="PART_QUANTITY"></input>
	    <input type="hidden" id="roId" name="roId" value="<%=request.getAttribute("roId") %>" />
	    <input type="hidden" id="claimPrice" name="claimPrice"></input>
	    <input type="hidden" id="supplierCode" name="supplierCode"></input>
	    <input type="hidden" id="supplierName" name="supplierName"></input>
	 <table class="table_query" >
		    <tr>
		      <td  class="table_query_2Col_label_5Letter">配件代码：</td>
	          <td align="left" >
	        	    <input name="partNos" type="text" class="normal_txt" id="partNos" value="" datatype="1,is_null,18"/>
	          </td>
	          <td class="table_query_2Col_label_5Letter">配件名称: </td>
	          <td align="left" >
	          		<input name="partNames" type="text"  class="normal_txt" id="partNames" value="" datatype="1,is_null,18"/>
	          </td>
	        </tr>
		    <tr align="left">
		      <td  colspan="4" align="center">
			      <input type="button" name="list" value="查询" class="normal_btn" onclick="__extQuery__(1);"/>
		          <input type="button" name="list2" value="返回" class="normal_btn"  onclick="goBack();" />
	          </td>
		    </tr>
	 </table>
     <table align=center  class="table_list" style="border-bottom:1px solid #DAE0EE" >
	      <tr>
		      <th colspan="4" align="left">
			       <img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 配件列表               
			       <input type="button" name="list3" value="添加" class="normal_btn" onclick="subChecked();"/>
		      </th>
	     </tr>
     </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/roItemPartsQuery.json";
	var title = null;
	var columns = [
     			{id:'action',header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"orderIds\")' />", width:'8%',sortable: false,dataIndex: 'PART_ID',renderer:myCheckBox},
				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称",dataIndex: 'PART_NAME' ,align:'center'},
				{id:'action',header: "数量",sortable: false,dataIndex: 'PART_QUANTITY' ,align:'center'},
				{header: "单价",dataIndex: 'CLAIM_PRICE' ,align:'center'}
				//{header: "供应商代码",dataIndex: 'SUPPLIER_CODE' ,align:'center'},
				//{header: "供应商名称",dataIndex: 'SUPPLIER_NAME' ,align:'center'}
		      ];
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' name='orderIds' value='" + value + "' />");
	}
	//数量
	function partsQuantity(value,metaDate,record){
		return String.format("<input type='text' name='PART_QUANTITY' value='" + value + "' datatype='1,is_digit,18'/>");
	}
	/*
  	功能：增加方法
  	参数：action : "add":增加
  	描述: 取得已经选择的checkbox，拼接成字符串，各项目以,隔开
   */
function subChecked() {
	var str="";
	var st="";
	var chk = document.getElementsByName("orderIds");
	var ch = document.getElementsByName("PART_QUANTITY");
	//if(ch.value.length>6){
	//                MyDivAlert("数量不能大于6位！");
	//	    		return false;
	//}
	var l = chk.length;
	var ll = ch.length;
	var cnt = 0;
	
	for(var i=0;i<l;i++){        
		if(chk[i].checked)
		{          
			if(str)
			{  
				str += ","+chk[i].value;
				/*
				st += ","+ch[i+1].value;
				if(""==ch[i+1].value||","==ch[i+1].value)
				{  
					MyDivAlert("请填写数量！");
		    		return false;
				}else{
					var pattern = /^-?\d+$/;
		             if(ch[i+1].value.search(pattern)!=0)
		             {
		            	 MyDivAlert("请填写数字！")
		              return false;
		             }else if(ch[i+1].value.length>6){
		              	MyDivAlert("数量不能大于6位！");
		    			return false;
		             }
				}
				*/
			}else{
				str = chk[i].value;
				/*
				st += ch[i+1].value;
				if(""==ch[i+1].value||","==ch[i+1].value)
				{
					MyDivAlert("请填写数量！");
		    		return false;
				}else{
					var pattern = /^-?\d+$/;
		             if(ch[i+1].value.search(pattern)!=0)
		             {
		            	 MyDivAlert("请填写数字！")
		              return false;
		             }else if(ch[i+1].value.length>6){
		              	MyDivAlert("数量不能大于6位！");
		    			return false;
		             }
				}
				*/
			} 
			cnt++;
		}
	}
	
	if(cnt==0){
		MyDivAlert("请选择！");
        return;
    }else{
    	
    	var tab = document.getElementById("myTable");
    	var codes = "";
    	var names = "";
    	var couts = "";
    	var co = "";
    	var claimPrices = "";
    	var supplierCodes = "";
    	var supplierNames = "";
    	if(tab.rows.length >1)
    	{
    		for(var i=1; i < tab.rows.length; i++)
    		{
    			var checkObj = tab.rows[i].cells[0].firstChild;

    			if(checkObj.checked ==  true)
    			{
    				var code = tab.rows[i].cells[1].innerText;
    				if(codes){
    					codes += "," + code;
    				}else{
    					codes = code;
    				}
    				var name = tab.rows[i].cells[2].innerText;
    				if(names){
    					names += "," + name;
    				}else{
    					names = name;
    				}
    				var claimPrice = tab.rows[i].cells[4].innerText;
    				if(claimPrices){
    					claimPrices += "," + claimPrice;
    				}else{
    					claimPrices = claimPrice;
    				}
    				/*
    				var supplierCode = tab.rows[i].cells[5].innerText;
    				if(supplierCodes){
    					supplierCodes += "," + supplierCode;
    				}else{
    					supplierCodes = supplierCode;
    				}
    				var supplierName = tab.rows[i].cells[6].innerText;
    				if(supplierNames){
    					supplierNames += "," + supplierName;
    				}else{
    					supplierNames = supplierName;
    				}
    				*/
    			}
    		}
    	}
    	//if(""==st||","==st){
    	//	MyDivAlert("请填写数量！");
    	//	return false;
       // }else{
	       if(!submitForm('fm')) {//表单校验
					return false;
				}
	    	document.getElementById("partNo").value = codes;
	    	document.getElementById("partName").value = names;
	    	document.getElementById("claimPrice").value = claimPrices;
	    	//document.getElementById("supplierCode").value = supplierCodes;
	    	//document.getElementById("supplierName").value = supplierNames;
	    	MyDivConfirm("是否确认增加？",sures,[str,st]);
	    	
   // }
   }
}

function sures(str,st){
	makeNomalFormCall('<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/roItemPartsOption.json?partsId='+str+'&PART_QUANTITY='+st,delBack,'fm','queryBtn');
}

//新增回调方法
function delBack(json) {
	if(json.success != null && json.success == "true") {
		MyDivAlert("新增成功！");
		//__extQuery__(1);
		goBack();
	} else {
		MyDivAlert("新增失败！请联系管理员！");
	}
}
//返回配件列表页面
function goBack(){
	fm.action = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/roItemQuery.do?roId='+<%=request.getAttribute("roId") %>";
	fm.submit();
	}
</script>
<!--页面列表 end -->
</body>
</html>
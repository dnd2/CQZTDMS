<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); 
%>
<script language="JavaScript" >
	function init(){
	var price = $('price').value;
	if(price=="noPrice"){
	MyAlert("系统未维护贵站该车型的工时单价,请联系管理员!");
	}else if(price=="isPrice"){
		__extQuery__(1)
	}
	}

</script>
<body onload="init();">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：工时选择 </div>
</div>
  <form  name="fm" id="fm" method="post">
  <input type="hidden" name="timeCode" id="timeCode" value="<%=request.getAttribute("timeCode")%>"/>
  <input type="hidden" name="timeId" id="timeId" value="<%=request.getAttribute("timeId")%>"/>
  <input type="hidden" name="TREE_CODE" id="TREE_CODE" value="<%=request.getAttribute("TREE_CODE")%>"/>
  <input type="hidden" name="WRGROUP_ID" id="WRGROUP_ID" value="<%=request.getAttribute("modelId")%>"/>
  <input type="hidden" name="list01" id="list01" value="<%=request.getAttribute("GROUP")%>"/>
  <input type="hidden" name="roNo" id="roNo" value="<%=request.getAttribute("roNo")%>"/>
  <input type="hidden" name="vin" id="vin" value="<%=request.getAttribute("vin")%>"/>
  <input type="hidden" name="yiedly" id="yiedly" value="<%=request.getAttribute("yiedly")%>"/>
   <input type="hidden" name="price" id="price" value="<%=request.getAttribute("price")%>"/>
  <input type="hidden" name="aCode" id="aCode" value="<%=request.getAttribute("aCode")%>"/>
   <!--查询条件begin-->
    <table class="table_query" border="0" >
      <tr>
        <td class="table_query_2Col_label_5Letter">工时代码：</td>
        <td align="left">
            <input name="LABOUR_CODE" id="LABOUR_CODE" type="text" class="middle_txt" size="5" />
        </td>
      <td class="table_query_2Col_label_5Letter">工时名称：</td>
        <td align="left">
            <input name="CN_DES" id="CN_DES" type="text" value="${partName}"class="middle_txt" size="5" />
       </td>
       
      </tr>
      <tr>
       <td colspan="4" align="center"><input name="button" id="queryBtn" type="button" onclick="init();" class="normal_btn"  value="查询" />
        <input name="button" id="queryBtn" type="button" onclick="_hide()" class="normal_btn"  value="关闭" />
        </td>
      </tr>
      <tr>
      <td colspan="4" align="left">
      	<input type="hidden" value="${printNum}" id='printNumHidden${idex}' name='printNumHidden'/>
      	<input type="hidden" value="${idex}" id='parentIdex' name='parentIdex'/>
      	旧件打印数量:<input type="text" value="${printNum}" id='printNum${idex}' onblur="checkInsure('0')" name='printNum'/>
      	 <input name="button" id="queryBtn" type="button" onclick="checkInsure('1');" class="normal_btn"  value="确认" />
      	 <span id='labourValue'>【算法:新增工时费=(输入数量-原始数量)*工时价格*1.5】更改旧件打印数量后,工时费增加:<B>0.00元</B></span>
      </td></tr>
    </table>
    <!--查询条件end-->
      <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >
	var myPage;
	
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/queryTime2.json";
				
	var title = null;

	var columns = [
				{id:'action',header: "选择",sortable: false,dataIndex: 'LABOUR_CODE',renderer:mySelect,align:'center'},
				{header: "车型组名称", dataIndex: 'WRGROUP_NAME', align:'center'},
				{header: "工时代码", dataIndex: 'LABOUR_CODE', align:'center'},
				{header: "工时名称", dataIndex: 'CN_DES', align:'center'},
				{header: "工时价格", dataIndex: 'LABOUR_AMOUNT', align:'center'},
				{header: "工时定额", dataIndex: 'LABOUR_FIX', align:'center'},
				//{header: "附加工时数", dataIndex: 'engineNo', align:'center'},
				{header: "工时单价", dataIndex: 'PARAMETER_VALUE', align:'center'}
				//{header: "预授权", dataIndex: 'fore', align:'center',renderer:getItemValue}
				//{header: "特殊工时", dataIndex: 'approvalLevel', align:'center'}
		      ];
		      
	function mySelect(value,metaDate,record){
		// return String.format("<input type='radio' name='rd' onclick='setMainTime(\""+record.data.id+"\",\""+record.data.labourCode+"\",\""+record.data.wrgroupName+"\",\""+record.data.cnDes+"\",\""+record.data.labourFix+"\",\""+record.data.parameterValue+"\",\""+record.data.fore+"\",\""+record.data.isSpec+"\",\""+record.data.approvalLevel+"\")' />");
		 return String.format("<input type='radio' name='rd' value='"+record.data.LABOUR_CODE+"-"+record.data.LABOUR_FIX+"-"+record.data.PARAMETER_VALUE+"' onclick='doCaculate(\""+record.data.LABOUR_CODE+"\",\""+record.data.LABOUR_FIX+"\",\""+record.data.PARAMETER_VALUE+"\",0)' />");
		 
	}

	function doCaculate(labourCode,unit,money,flag){
		var idex = document.getElementById("parentIdex").value;
		var newPrintNum = document.getElementById("printNum"+idex).value;
		var oldPrintNum = document.getElementById("printNumHidden"+idex).value;
		newPrintNum = parseInt(newPrintNum,10);
		oldPrintNum = parseInt(oldPrintNum,10);
		var addMoney = parseFloat(unit*money*(newPrintNum-oldPrintNum)*1.5);
		document.getElementById("labourValue").innerHTML = "【算法:新增工时费=(输入数量-原始数量)*工时价格*1.5】旧件打印数量由"+oldPrintNum+"变为"+newPrintNum+"后,工时费增加:<B>"+addMoney+"元</B>";
		if(flag=="1"){
			if("undefined"==typeof labourCode|| labourCode==""){
				MyAlert("请选择工时参数");
				return;
			}
			setMainTime(idex,newPrintNum,addMoney,labourCode);	
			_hide();
		}
	}

	function checkInsure(t){
		var idex = document.getElementById("parentIdex").value;
		var newPrintNum = document.getElementById("printNum"+idex).value;
		var oldPrintNum = document.getElementById("printNumHidden"+idex).value;
		
		newPrintNum = parseInt(newPrintNum,10);
		oldPrintNum = parseInt(oldPrintNum,10);
		if(isNaN(newPrintNum)){
			MyAlert("请输入正确的数字");
			document.getElementById("printNum"+idex).value = oldPrintNum;
			return;
		}
		if(newPrintNum<=oldPrintNum){
			MyAlert("修改后的数量需要大于原始数量");
			document.getElementById("printNum"+idex).value = oldPrintNum;
			return;
		}
		var rds = document.getElementsByName("rd");
		var rdValue = "";
		var unit = "";
		var money = "";
		var arrs = new Array();
		var flag = false;
		for(var i=0;i<rds.length;i++){
			if(rds[i].checked){
				rdValue = rds[i].value;
				arrs = rdValue.split("-");
				flag = true;
				doCaculate(arrs[0],arrs[1],arrs[2],t);
			}
		}	
		if(!flag&&t=="1"){
			MyAlert("请选择工时参数");
			return;
		}	
	}
	function setMainTime(idex,newPrintNum,addMoney){
		 //调用父页面方法
		 parentContainer.setMainTime(idex,newPrintNum,addMoney);
	}
	
</script>
</body>
</html>

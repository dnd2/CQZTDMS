<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- created by lishuai103@yahoo.com.cn 20100603 配件采购订单明细 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购订单维护</title>

</head>

<script language="JavaScript">

	//初始化方法
	function doInit()
	{
		loadcalendar();  //初始化时间控件
	}

</script>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;经销商配件采购&gt;配件采购订单编辑</div>
<form id="fm" name="fm" method="post"> 
<input type="hidden" name="dc" id="dc" value=""/>
<input type="hidden" name="cot" id="cot" value="0"/>
<input type="hidden" name="cou" id="cou" value="0"/>
<input type="hidden" name="dis" id="dis" value="0"/>
<input type="hidden" name="ordStatus" id="ordStatus" value=""/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    <tr>
    	<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 基本信息</th>
    </tr>
     <tr>
       <td class="table_query_3Col_label_7Letter">要求到货时间：</td>
       <td><input name="requireDate" id="t2" value="" type="text" datatype="1,is_date,10" class="short_txt" hasbtn="true" callFunction="showcalendar(event, 't2', false);"></td>
       <td class="table_query_3Col_label_5Letter">运输类型：</td>
	   <td><script type="text/javascript">     
 				  genSelBoxExp("freightType",<%=Constant.FREIGHT_TYPE%>,"",false,"short_sel","","false",'');
			</script>
       </td>
       <td>&nbsp;</td>
       <td>&nbsp;</td>
     </tr>
     <tr>
        <td class="table_query_3Col_label_7Letter">供货方：</td>
        <td id="dc_Id"><select name="dcId" id="dcId" onchange="ShowAmount(this.value)" class="short_sel">
		        <option value="">-请选择-</option>
				 <c:forEach items="${dcList}" var="dc">
				 	<option value="<c:out value="${dc.DC_ID}"/>"><c:out value="${dc.DC_NAME}"/></option>
				 </c:forEach>
            </select><font color="red">*</font>
        </td>
        <td class="table_query_3Col_label_5Letter">资金明细：</td>
        <td id="amount">无</td>
        <td colspan="2">
        	<input type="button" name="BtnYes3" value="备件供货查询" class="long_btn" onclick="CheckDCName();" />
        </td>
      </tr>
      <tr>
        <td class="table_query_3Col_label_7Letter">备注：</td>
        <td colspan="5">
          <textarea name="remark" id="remark" rows="5" cols="70" ></textarea>
        </td>
      </tr>
    </table>
   <div id="partInfo">
  	<table class="table_list" id="pi">
	 <tr>
		<th colspan="38" align="left"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 基本信息
	 		<input type="button" name="BtnYes2" value="新增明细" class="normal_btn" onclick="addPart()" />
		</th>
	</tr>
	<tr>
		<th>序号</th>
		<th>配件号</th>
		<th>配件名称</th>
		<th>单位</th>
		<th>最小包装数</th>
		<th>帐面库存</th>
		<th>安全库存</th>
		<th>单价</th>
		<th>折扣</th>
		<th>折扣后价格</th>
		<th>订购数量</th>
		<th>汇总</th>
		<th>备注</th>
		<th>操作</th>
	</tr>
	
  </table>
  </div>
<br>
	<table class="table_edit">
		<tr>
		  <td align="center">		    
		  	<input type="button" name="BtnNo" value="保存" class="normal_btn" onclick="savePtOrder(0)">
			<input type="button" name="BtnNo" value="完成" class="normal_btn" onclick="savePtOrder(1)">
			<input type="hidden" id="sel_dcId" name="sel_dcId" />
          </td>
        </tr>
	</table>
</form> 
<script type="text/javascript">

	//动态查询经销商在供货方的资金明细
	function ShowAmount(value)
	{
		$("dc").value = value;
		document.getElementById("sel_dcId").value=value;
		var url = "<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/getAmount.json"
		makeNomalFormCall(url,showAmountValue,'fm','queryBtn'); 
	}
	
	//回显资金明细
	function showAmountValue(json){
		var Amount = json.amount;
		if(Amount){
			document.getElementById("amount").innerText = Amount + "元";
		}else{
			document.getElementById("amount").innerText = "无";
		}
	}
	
	//查看供货方的库存
	function CheckDCName(){
		var dcId = document.getElementById("dcId").value;
		OpenHtmlWindow('<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/getDCPart.do?dcId='+dcId,800,500);
	}
	
	
	function savePtOrder(val){
		var did = document.getElementById("sel_dcId").value;
		if(did==""){
			MyAlert("请选择供货方信息！");
			return;
		}
		var pi = document.getElementById("pi");
		if(val == 1 && pi.rows.length<=2)
		{
			MyAlert("点击完成请添加配件信息！");
			reutrn;
		}
		var ordStatus;
		if(val==0)
		{
			ordStatus = '<%=Constant.PART_ORDER_STATUS_01 %>';
		}
		else
		{
			ordStatus = '<%=Constant.PART_ORDER_STATUS_02 %>';
		}
		$("ordStatus").value = ordStatus;
		var cot = 0;
		
		if(pi.rows.length>2){
			for(var i=2; i < pi.rows.length; i++)
			{
				if(pi.rows[i].cells[10].firstChild.value == 0)
				{
					 MyAlert("请添加配件数量！");
					 return;
				}
				//取最小包装数
	    		var miniPack = pi.rows[i].cells[4].innerText;
	    		var count = pi.rows[i].cells[10].firstChild.value;
	    		if (count % miniPack != 0) {
	    			MyAlert('第' + (i-1) + '行数量必须是最小包装数的整数倍');
	    			return;
	    		}
	    		var a = pi.rows[i].cells[11].innerText;
	    		var t = a.replace(/[,]/g,"");
	    		cot = cot+parseFloat(t,10);
			}
			$("cot").value = cot;
			$("cou").value = pi.rows.length-2;
			$("dis").value = pi.rows[2].cells[8].innerText;
		}
		
		fm.action = "<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/savePtOrder.do";
		fm.submit();
	}
	
	//新增配件明细
	function addPart(){
	
		var ord = document.getElementById("dcId");
		if(ord.value == ""){
			MyAlert("请选择供货方信息！");
			return;
		}
		var id = ord.value;
		OpenHtmlWindow('<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/setPartInfo.do?dcId='+id,800,500);
	}
	
	function addPartInfo(json) {
		if(json.partInfoSet.length > 0){
			document.getElementById("dc_Id").disabled = true ;
		}else{
			document.getElementById("dc_Id").disabled = false ;
		}
		var partInfo = document.getElementById("partInfo");
		var str = '';
		str += '<table class="table_list" id="pi">';
		str += '<tr><th colspan="38" align="left"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 基本信息<input type="button" name="BtnYes2" value="新增明细" class="normal_btn" onclick="addPart()" /></th></tr>' + 
		'<tr class="table_list_th">' + 
		' <th>序号</th> ' + 
		' <th>配件号</th> ' + 
		' <th>配件名称</th>' +
		' <th>单位</th>' +
		' <th>最小包装数</th>' +
		' <th>账面库存</th>' +
		' <th>安全库存</th>' +
		' <th>单价(元)</th>' +
		' <th>折扣</th>' +
		' <th>折扣后价格(元)</th>' +
		' <th>订购数量</th>' +
		' <th>汇总</th>' +
		' <th>备注</th>' +
		' <th>操作</th>' +
		'</tr>';
		for (var i = 0; i < json.partInfoSet.length; i++) {
			str += '<tr class="table_list_row1">'
			str += '<td>' + (i+1) + '</td>'
			str += '<td>' + json.partInfoSet[i].partCode + '</td>'
			str += '<td>' + json.partInfoSet[i].partName + '</td>'
			str += '<td>' + json.partInfoSet[i].unit + '</td>'
			str += '<td>'+ json.partInfoSet[i].miniPack + '</td>'
			str += '<td>'+ json.partInfoSet[i].paperQuantity + '</td>'
			str += '<td>'+ json.partInfoSet[i].safeQuantity + '</td>'
			str += '<td>'+ json.partInfoSet[i].salePrice + '</td>'
			str += '<td>'+ json.partInfoSet[i].discountRate + '</td>'
			str += '<td>'+ json.partInfoSet[i].disPrice + '</td>'
			str += '<td><input name="pcount" class="short_txt" type="text" onblur="getCount(this.value,'+json.partInfoSet[i].disPrice+','+i+','+json.partInfoSet[i].partId+ ')" value= "' + json.partInfoSet[i].count + '" /></td>'
			str += '<td>'+ json.partInfoSet[i].orderPrice + '</td>'
			str += '<td><input name="premark" type="text" value="' + json.partInfoSet[i].remark + '" onblur="getRemark(this.value, '+json.partInfoSet[i].partId+ ')"/></td>'
			str += '<td><input type="button" class="normal_btn" onclick="del(\''+json.partInfoSet[i].partId+'\')" value="删除" /></td>'
			str += '</tr>';
		}
		str += '</table>';
		partInfo.innerHTML = str;
		
	}
	//删除方法
	function del(partId)
	{
		makeNomalFormCall("<%=contextPath%>/partsmanage/common/PartMemory/delPartInfoSet.json?partId=" + partId,addPartInfo,'fm','queryBtn');
	}
	
	//获得汇总钱数，在内存中存个数
	function getCount(val,val2,val3,val4)
	{
		var tab = document.getElementById("pi");
		//总金额
		var orderPrice = amountFormat(val*val2);
		tab.rows[val3+2].cells[11].innerHTML = orderPrice;
		makeNomalFormCall("<%=contextPath%>/partsmanage/common/PartMemory/modifyCount.json?partId=" + val4+'&count='+val+'&orderPrice='+orderPrice,'','fm','queryBtn');
	}
	
	//在内存中存备注
	function getRemark(remark, partId) {
		if (remark.trim()) {
			//去后台改变备注
			makeNomalFormCall("<%=contextPath%>/partsmanage/common/PartMemory/modifyRemark.json?partId=" + partId+'&remark='+remark,'','fm','queryBtn');
		} 
	}
	
</script>
</body>
</html>
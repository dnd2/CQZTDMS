<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@page import="com.infodms.dms.common.Constant" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>旧件运输方式新增</title>
<% 
   String contextPath = request.getContextPath();
%>
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;旧件运输方式新增</div>
 <form method="post" name ="fm" id="fm">
       <table id="itemTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="16" >
					<h3>索赔旧件运输方式（变更）申请表</h3>
				</th>
				<tr  align="center" class="table_list_row1">
				    <td colspan="7" align="left">
				   		服务站名称：${DEALER_NAME }
				    </td>
				    <td colspan="5" align="left">
				   		服务站编码：${DEALER_CODE }
				    </td>
				</tr>
				<tr  align="center" class="table_list_row2">
				    <td colspan="2">
				                          所发运输公司
				    </td>
				    <td colspan="4">
				                         发运信息
				    </td>
				    <td colspan="4">
				                          计费方式
				    </td>
				    <td >
						运输类型
					</td>
				    <td rowspan="2">
						备注
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr align="center" class="table_list_row1">
					<td>
						名称
					</td>
					<td>
						联系电话
					</td>
					<td>
						到货地点
					</td>
					<td>
						发运人姓名
					</td>
					<td>
						联系电话
					</td>
					
					<td>
						到货方式
					</td>
					<td>
						元/公斤
					</td>
					<td>
						元/立方
					</td>
					<td>
						其他
					</td>
					<td>
						送货上门费
					</td>
					<td>
						回运类型
					</td>
					<td>
						<input id="itemBtn" type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:addRow('transportTable');" />
					</td>
				</tr>

				<tbody id="transportTable">
				</tbody>
			</table>
     <table class="table_list">
      <tr > 
       <td height="12" align="center">
     
       <input type="button" id="save_btn" onclick="save(1);" class="normal_btn" style="width=8%" value="保存"/>
       <input type="button" id="commit_btn" onclick="save(2);" class="normal_btn" style="width=8%" value="提交"/>
          <input type="button"  onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/>
      	 <input type="hidden" name="totalPrice3" id="totalPrice3" value=""/>
       </td>
      </tr>
    </table>  
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<br />
<script type="text/javascript">
//动态生成表格
	function addRow(tableId){
    var addTable = document.getElementById(tableId);
	var rows = addTable.rows;
	var length = rows.length;
	var insertRow = addTable.insertRow(length);
	insertRow.className = "table_list_row1";
	insertRow.insertCell(0);
	insertRow.insertCell(1);
	insertRow.insertCell(2);
	insertRow.insertCell(3);
	insertRow.insertCell(4);
	insertRow.insertCell(5);
	insertRow.insertCell(6);
	insertRow.insertCell(7);
	insertRow.insertCell(8);
	insertRow.insertCell(9);
	insertRow.insertCell(10);
	insertRow.insertCell(11);
	insertRow.insertCell(12);
	if (tableId=='transportTable') {
		
		addTable.rows[length].cells[0].innerHTML = '<td><input type="text" name="TRANSPORT_NAME" datatype="0,is_null"  value="" size="5" id="TRANSPORT_NAME"/></td>';
		addTable.rows[length].cells[1].innerHTML =  '<td><input type="text" class="phone_txt" name="LINK_PHONE" datatype="0,is_phone,9"  value="" size="10" id="LINK_PHONE"/></td>';
		addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" class="little_txt" name="ARRIVE_PLACE" datatype="0,is_null" id="ARRIVE_PLACE"  size="3" style="length:5"/></td>';
		addTable.rows[length].cells[3].innerHTML =  '<td><input type="text" class="little_txt" name="SEND_PERSON" datatype="0,is_null" id="SEND_PERSON"  size="3"/></td>';
	
		addTable.rows[length].cells[4].innerHTML =  '<td><input type="text" class="phone_txt" name="SEND_PHONE" datatype="0,is_phone,11"  value="" size="10" id="SEND_PHONE"/></td>';
		addTable.rows[length].cells[5].innerHTML =  genSelBoxExpPay("ARRIVE_WAY",9556,"",false,"min_sel","","false",'');
		addTable.rows[length].cells[6].innerHTML ='<td><input type="text" class="little_txt" name="PRICE_WEIGHT" datatype="0,is_double"  value="" size="10" id="PRICE_WEIGHT"/></td>';
		addTable.rows[length].cells[7].innerHTML = '<td><input type="text" class="little_txt" name="PRICE_CUBIC" datatype="0,is_double"  value="" size="10" id="PRICE_CUBIC"/></td>';
		addTable.rows[length].cells[8].innerHTML = '<td><input type="text" class="little_txt" name="PRICE_OTHER" datatype="1,is_double"  value="" size="10" id="PRICE_OTHER"/></td>';
		addTable.rows[length].cells[9].innerHTML = '<td><input type="text" class="little_txt" name="SEND_COSTS" datatype="1,is_double"  value="" size="10" id="SEND_COSTS"/></td>';
		addTable.rows[length].cells[10].innerHTML = genSelBoxExpPay("RETURN_TYPE",1073,"10731002",false,"min_sel","","false",'');
		addTable.rows[length].cells[11].innerHTML = '<td><input type="text" name="REMARK"  datatype="1,is_textarea,1000" value="" size="10" id="REMARK"/></td>';
		addTable.rows[length].cells[12].innerHTML = '<td><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delTransportItem(this);"/></td>';
	}
		return addTable.rows[length];
	}
	//删除行 配件
	function delTransportItem(obj){
	     var tr = this.getRowObj(obj);
	    if(tr.childNodes[5].childNodes.length==3) {
	    	MyConfirm("是否删除？",delItems,[tr]);
	    }else{
	  		 if(tr != null){
	    		tr.parentNode.removeChild(tr);
		   	}else{
		    	throw new Error("the given object is not contained by the table");
		   	}
	   }
	}
	//删除配件
	function delItems(tr){
	    var roNo = getRowNo(tr);
		var trlen = tr.childNodes.length;
		var length=tr.parentNode.childNodes.length;
		var endRo=roNo;
		if (length>roNo){
		for (var i=roNo+1;i<length;i++) {
			if (tr.parentNode.childNodes[i].childNodes[5].childNodes.length==3) {
				endRo=i;
				break;
			}
		}
		}
		//如果没有找到结束行，说明为最后一个主工时，一直删除到表格长度即可
		if (endRo==roNo) {
			endRo=length;
		}
		var trObj = tr.parentNode;
		for (var i=roNo;i<endRo;i++) {
			var oldNode = trObj.removeChild(trObj.childNodes[roNo]);
			oldNode = null;
		}
		countFee();
		//refreshAppTimeCombo();
	}
	//得到行对象
	function getRowObj(obj)
	{
	   var i = 0;
	   while(obj.tagName.toLowerCase() != "tr"){
	    obj = obj.parentNode;
	    if(obj.tagName.toLowerCase() == "table")
	  return null;
	   }
	   return obj;
	}
	//根据得到的行对象得到所在的行数
	function getRowNo(obj){
	   var trObj = getRowObj(obj); 
	   var trArr = trObj.parentNode.children;
	   var ret;
	 for(var trNo= 0; trNo < trArr.length; trNo++){
	  if(trObj == trObj.parentNode.children[trNo]){
	  		ret = trNo;
	  		break;
	  }
	 }
	 return ret;
	}
	function save(val)
	{   
		
		var detailSel = document.getElementById('TRANSPORT_NAME');
		if(detailSel==null||detailSel.length<=0){
			MyAlert('没有数据，请新增！');
			return;
		}
		if(submitForm(fm)==false) return ;
		var arriveWay = document.getElementById('ARRIVE_WAY').value;
			
		var sendCosts = document.getElementsByName('SEND_COSTS');
		var arriveWay = document.getElementsByName('ARRIVE_WAY');
		for(var i = 0 ; i < sendCosts.length; i++){
			
			var sendCostsVal = sendCosts[i].value;
			
			if(arriveWay[i].value==<%=Constant.SP_JJ_TRANSPORT_SENDWAY_02%>){//送货上门必须有送货费用
				if(sendCostsVal==null||sendCostsVal==''){
					MyAlert('送货上门费用必须填写！');
					return;
				}
			}
		}
	var   way= document.getElementsByName('ARRIVE_WAY');
	for (var j=0;j<way.length;j++){
		if(way[j].value==null || way[j].value==""){
				MyAlert("到货方式必选!");
				return;
			}

		}
		
		/* if(submitForm(fm)==false) return ;
		var yieldly = $('yieldly').value ;
		if(!yieldly){
			MyAlert('结算厂家为必填项！');
			return ; 
		} */
		//附件添加必填限制。
		//if(val==2){
		//	var arr = document.getElementsByName('uploadFileId') ;
		//	if(arr.length==0){
		//		MyAlert('附件为必填项！');
		//		return ;
		//	}
		//}
		//fm.action = "/BQYXDMS/claim/specialExpenses/SpecialExpensesManage/addSpeciaExpensesDO.do";
		MyConfirm("确认新增！",addSpecia, [val]);
	}
	function addSpecia(val)
	{
	  if(val == 1){
		$('commit_btn').disabled = true ;
		$('save_btn').disabled = true ;
		} 
		makeNomalFormCall("<%=contextPath%>/claim/oldPart/ClaimOldPartTransportManager/oldpartTransportAdd.json?flag="+val,addSpeciaBack,'fm','queryBtn');
	}
	function addSpeciaBack(json)
	{
		if(json.returnValue == 1)
		{
			MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/claim/oldPart/ClaimOldPartTransportManager/queryOldpartTransport.do";
			fm.submit();
		}
		else
		{
			MyAlert("操作失败！请联系系统管理员！");
		}
	}
	//产生收费方式下拉框
	function genSelBoxExpPay(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
		var str = "";
		var arr;
		if(expStr.indexOf(",")>0)
			arr = expStr.split(",");
		else {
			expStr = expStr+",";
			arr = expStr.split(",");
		}
		str += "<select id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
		if(nullFlag && nullFlag == "true"){
			str += " datatype='0,0,0' ";
		}
		// end
		str += " onChange=doCusChange(this.value);> ";
		if(setAll){
			str += genDefaultOpt();
		}
		for(var i=0;i<codeData.length;i++){
			var flag = true;
			for(var j=0;j<arr.length;j++){
				if(codeData[i].codeId == arr[j]){
					flag = false;
				}
			}
			if(codeData[i].type == type && flag){
				str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '"+codeData[i].codeDesc+"' >" + codeData[i].codeDesc + "</option>";
			}
		}
		str += "</select>";
		return str;
	}

</script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@page import="com.infodms.dms.common.Constant" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>旧件回运单申请新增</title>
<% 
   String contextPath = request.getContextPath();
%>
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;旧件回运单申请新增</div>
 <form method="post" name ="fm" id="fm">
       <table id="itemTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="7" >
					<h3>旧件回运申请表</h3>
				</th>
				<tr  align="center" class="table_list_row1">
				    <td colspan="4" align="left">
				   		服务站名称：${DEALER_NAME }
				    </td>
				    <td colspan="3" align="left">
				   		服务站编码：${DEALER_CODE }
				    </td>
				</tr>
				<tr align="center" class="table_list_row1">
					<td>
						回运清单号
					</td>
					<td>
						配件代码
					</td>
					<td>
						配件名称
					</td>
					<td>
						索赔单号
					</td>
					<td>
						旧件编号
					</td>
					<td>
						抵扣原因
					</td>
					<td>
						<input id="itemBtn" type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:addRow('returnApplyTable');" />
					</td>
				</tr>

				<tbody id="returnApplyTable">
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
var myobj;
//选择主上件
function selectMainTime(obj){
	myobj = getRowObj(obj);
	OpenHtmlWindow('<%=contextPath%>/claim/oldPart/ClaimOldPartReturnApplyManager/selectOldPartReturnListForward.do',800,500);
}


function setMainPartCode(id,returnNo,partCode,partName,
		claimNo,barcodeNo,deductRemark) {
			var table = myobj.parentNode;
				var length= table.childNodes.length;
				var flag=0;
				/*if($('PART_CODE')!=null){
					var partCodes = $$('.PART_CODE').pluck('value');
					if(partCodes.indexOf(partCode)!=-1){
							MyAlert("该配件已经存在，不可添加！");
							return false;
					}
				}
				*/
				//判断是否添加了重复的主工时
				/* for (var i = 0;i<length;i++) {
						if(returnNo==table.childNodes[i].childNodes[0].childNodes[0].value){
							cloMainPart=0;
							MyAlert("该旧件清单已经存在，不可添加！");
							flag=1;
							break;
						}
				} */
				//if (flag==0) {
				//cloMainPart=1;
					
		    //myobj.cells.item(3).childNodes[0].value='';
		    myobj.cells.item(0).innerHTML='<input type="text" class="short_txt" name="RETURN_NO" value="'+returnNo+'" size="10" maxlength="11" id="RETURN_NO" readonly/><span class="tbwhite">&nbsp;&nbsp;&nbsp;<input type="button"  class="normal_btn"  value="选择"   onClick="javascript:selectMainTime(this);"/></span>'+
		    '<input type=hidden name="RETURN_DETAIL_ID" value="'+id+'"/>';
			myobj.cells.item(1).innerHTML='<input type="text" class="short_txt PART_CODE" name="PART_CODE"   value="'+partCode+'" size="10" id="PART_CODE" readonly="true"/>';
			myobj.cells.item(2).innerHTML='<span class="tbwhite"><input type="text" class="short_txt" name="PART_NAME" readonly value="'+partName+'" id="PART_NAME"  size="10"/></span>';
			myobj.cells.item(3).innerHTML='<input type="text" class="short_txt" name="CLAIM_NO" value="'+claimNo+'" size="10" maxlength="11" id="CLAIM_NO" readonly/>';
			myobj.cells.item(4).innerHTML = '<input type="text" class="short_txt" name="BARCODE_NO" value="'+barcodeNo+'" size="10" maxlength="11" id="BARCODE_NO" readonly/>';
			myobj.cells.item(5).innerHTML = '<input type="text" class="short_txt" name="DEDUCT_REMARK" value="'+getItemValue(deductRemark)+'" size="10" maxlength="11" id="DEDUCT_REMARK" readonly/>'+
			'<input type=hidden name="DEDUCT_REMARK_CODE" value="'+deductRemark+'"/>';
			//myobj.cells.item(8).innerHTML='<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\''+partCode+'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			//myobj.cells.item(7).innerHTML='<td><input type="button" style="display:none" class="normal_btn"  value="新增附加"  name="button42" onClick="javascript:addPlusRow(\'partTable\',this,\''+partCode+'\');"/><input type="button" class="normal_btn"  value="删除"  name="button42" onClick="javascript:delPartItem(this,\'part\');"/></td>';
			//getGuaFlag();
			parent._hide();
			//}
		}

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
	if (tableId=='returnApplyTable') {
		
		addTable.rows[length].cells[0].innerHTML = '<td><input type="text"  readonly="readonly"  class="short_txt" name="RETURN_NO" datatype="0,is_null"  value=""  id="RETURN_NO	"/></td><span style="color:red">*</span><input type="button"  class="normal_btn"  value="选择"   onClick="javascript:selectMainTime(this);"/>';
		addTable.rows[length].cells[1].innerHTML =  '<td><input type="text"  readonly="readonly" class="short_txt" name="PART_CODE" datatype="0,is_null"  value="" size="3" id="PART_CODE"/></td>';
		addTable.rows[length].cells[2].innerHTML =  '<td><input type="text"   readonly="readonly" class="short_txt" name="PART_NAME" datatype="0,is_null" id="PART_NAME"  size="3" style="length:5"/></td>';
		addTable.rows[length].cells[3].innerHTML =  '<td><input type="text"  readonly="readonly" class="short_txt" name="CLAIM_NO" datatype="0,is_null" id="CLAIM_NO"  size="3"/></td>';
		addTable.rows[length].cells[5].innerHTML =  '<td><input type="text"  readonly="readonly" class="short_txt" name="CODE_DESC" datatype="0,is_null"  value="" size="10" id="CODE_DESC"/></td>';
		addTable.rows[length].cells[4].innerHTML =  '<td><input type="text"  readonly="readonly" class="short_txt" name="BARCODE_NO" datatype="0,is_null"  value="" size="10" id="BARCODE_NO"/></td>';
		addTable.rows[length].cells[6].innerHTML =  '<td><input type="button"  class="normal_btn"  value="删除"  name="button42" onClick="javascript:delTransportItem(this);"/></td>';
	}
		return addTable.rows[length];
	}
	/* function addValues(){
		//刷新维修配件上的维修项目值
	var Labour0s = document.getElementsByName("Labour0");
	if(Labour0s.length > 0){
		//清空所有值
		
		
		for(var j=0; j<Labour0s.length; j++){
			Labour0s[j].options.length = 0;
		}
		//重新赋值
		for(var k=0; k<Labour0s.length; k++){
			for(var a=0; a<itemcodes.length; a++){
				var varItem = new Option(itemnames[a],itemcodes[a]);
				Labour0s[k].options.add(varItem);
			}
		}
	
	}
	} */
	//删除行 配件
	function delTransportItem(obj){
	     var tr = this.getRowObj(obj);
	    //MyAlert("length="+tr.childNodes[10].childNodes.length);
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
		
		var detailSel = document.getElementById('RETURN_NO');
		if(detailSel==null||detailSel.length<=0){
			MyAlert('没有数据，请新增！');
			return;
		}
		if(submitForm(fm)==false) return ;
		
		MyConfirm("确认新增！",addSpecia, [val]);
	}
	function addSpecia(val)
	{
	  if(val == 1){
		$('commit_btn').disabled = true ;
		$('save_btn').disabled = true ;
		} 
		makeNomalFormCall("<%=contextPath%>/claim/oldPart/ClaimOldPartReturnApplyManager/oldparReturnApplyAdd.json?flag="+val,addSpeciaBack,'fm','queryBtn');
	}
	function addSpeciaBack(json)
	{
		if(json.returnValue == 1)
		{
			parent.MyAlert("操作成功！");
			fm.action = "<%=contextPath%>/claim/oldPart/ClaimOldPartReturnApplyManager/queryOldpartReturnApply.do";
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

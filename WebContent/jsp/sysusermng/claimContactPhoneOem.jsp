<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>个人信息维护</title>
</head>
<body onunload="__extQuery__(1);loadcalendar();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 个人信息管理  &gt; 个人信息管理   &gt; 服务站人员及联系方式 </div>
	<form id="fm" name="fm" method="post">
	<input type="hidden" name="curPage" id="curPage" value="1" />
	<input id="contactId" name="contactId" type="hidden" value="${po.id}" />
	<table class="table_query" border="0" >
      <tr>
      	<td class="table_query_2Col_label_8Letter"><div align="right"><span class="tabletitle">职位：</span></div></td>
        <td align="left">
            <script type="text/javascript">
            	var perPose = <%=request.getAttribute("perPose")%>;
       			genSelBoxExp("CON_PER_POSE",<%=Constant.STATION_HEAD_AND_AEGIS%>,perPose,true,"short_sel","","false",'');
           </script>
        </td>
        <td class="table_query_2Col_label_8Letter"><div align="right"><span class="tabletitle">姓名：</span></div></td>
        <td align="left">
            <input name="CON_PER_NAME" id="CON_PER_NAME" value="${perName }" type="text" class="middle_txt" size="17" />
        </td>
        </tr>
        <tr>
      	<td class="table_query_2Col_label_8Letter"><div align="right"><span class="tabletitle">电话：</span></div></td>
        <td align="left">
            <input name="CON_PER_PHONE" id="CON_PER_PHONE" value="${perPhone }" type="text" class="middle_txt" size="17" />
        </td>
        <td class="table_query_2Col_label_8Letter"><div align="right"><span class="tabletitle">状态：</span></div></td>
        <td align="left">
            <script type="text/javascript">
            	var status = <%=request.getAttribute("status")%>
       			genSelBoxExp("CON_STATUS",<%=Constant.STATUS%>,status,true,"short_sel","","false",'');
           </script>
        </td>
        </tr>
        <tr>
        <td width="10%" align="right">所选经销商：</td>
		<td width="30%">
			<input type="text" name="con_dealer_code" id="dealer_code" class="long_txt" readonly="readonly"/>
			<input type="hidden" name="con_dealer_id" id="dealer_id"/>
			<input type="button" class="mini_btn" value="..." onclick="showOrgDealer('dealer_code','dealer_id',true,'',true,'','10771002');"/>
           	<input type="button" class="normal_btn" value="清除" onclick="wrapOut2('dealer_code','dealer_id');"/>
		</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
        </tr>
        <tr>
        <td colspan="4" align="center"><input name="button" id="queryBtn" type="button" onclick="javascript:__extQuery__(1);" class="normal_btn"  value="查询" />
        <input class="normal_btn" type="button" value="保存" id="save_but"  onclick="save();"/>
        <!-- <input id="itemBtn" type="button" class="normal_btn" value="新增"
							name="button422" onClick="javascript:addRow('myTable');" /> -->
        </td>
      </tr>
    </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	<script type="text/javascript" >
	   var myPage;
	   //查询路径
	   var url = "<%=contextPath%>/sysusermng/sysuserinfo/SysPasswordManager/maintainClaimPhone.json";
					
	   var title = null;

	   var columns = [
	  				{header: "序号",align:'center',renderer:getIndex},
	  				{header: "服务站代码",dataIndex: 'DEALER_CODE',align:'center',renderer:roRender},
	  				{header: "服务站名称",dataIndex: 'DEALER_SHORTNAME',align:'center'},//return_no
	  				{header: "职位",dataIndex: 'PER_POSE',align:'center',renderer:getItemValue},
	  				{header: "姓名",dataIndex: 'PER_NAME',align:'center'},
	  				{header: "电话", dataIndex: 'PER_PHONE', align:'center'},
	  				{header: "热线电话", dataIndex: 'HOT_LINE_PHONE', align:'center'},
	  				{header: "备注", dataIndex: 'PER_REMARK', align:'center'},
	  				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
	  				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:operateLink,align:'center'}
	  		      ];
	  		     
	  
	 //超链接设置
	   function operateLink(value,meta,record){
		   var width=800;
		   var height=500;
		   var screenW = window.screen.width-30;	
		   var screenH = document.viewport.getHeight();
		   if(screenW!=null && screenW!='undefined')
			   width = screenW;
		   if(screenH!=null && screenH!='undefined')
			   height = screenH;
	   	   var id=record.data.ID;
	   	   var perPose = record.data.PER_POSE;
	   	   var perName = record.data.PER_NAME;
	   	   var perPhone =record.data.PER_PHONE;
	   	   var perRemark = record.data.PER_REMARK;
	   	   var status = record.data.STATUS;
	   	   var dealerId = record.data.DEALER_ID;
	   	   var hotLine = record.data.HOT_LINE_PHONE;
	   	  
		   var link = "<a href='#' onclick=\"historyData("+id+","+dealerId+")\">[修改历史]</a>"+
		   "<a href='#' onclick=\"deleteData("+id+","+dealerId+")\">[删 除]</a>"+
		   "<a href='#' onclick=\"modifyRow(this,"+id+","+perPose+",'"+perName+"','"+perPhone+"','"+perRemark+"','"+status+"','"+hotLine+"','"+dealerId+"')\">[修 改]</a>";
			return String.format(
					link
				   );
	   }

	//查看修改历史记录
	function historyData(contactId,dealerId){
		var url ="<%=contextPath%>/sysusermng/sysuserinfo/SysPasswordManager/queryContactChangeHistory.do?CONTACT_ID="+contactId+"&dealerId="+dealerId;
		OpenHtmlWindow(url,800,500);
	}
	//经销商内容清除操作
	function wrapOut2(dealerCode,dealerId){
		document.getElementById(dealerCode).value = '' ;
		document.getElementById(dealerId).value = '' ;
	}
	//从数据库里删除数据
	function deleteData(id,dealerId){
		 MyConfirm("您确定要删除?",deleteDataDo,[id,dealerId]);
	}
	function deleteDataDo(id,dealerId){
		fm.action="<%=contextPath %>/sysusermng/sysuserinfo/SysPasswordManager/delClaimPhone.do?contactId="+id+"&dealer_id="+dealerId;
		fm.submit();		
	}
	 // 动态生成表格
	 var num="0";
 	function addRow(tableId){
 		var sel = "genSelBoxExpforThisPage('PER_POSE',"+<%=Constant.STATION_HEAD_AND_AEGIS%>+",'',false,'','','true','')";
 		var sel2 = "genSelBoxExpforThisPage('STATUS',"+<%=Constant.STATUS%>+",'<%=Constant.STATUS_ENABLE%>',false,'','','true','')";
 		var dealerCodeInputName = "dealer_code"+num;
 		var dealerIdInputName = "dealer_id"+num;
		num++;
 		var sel3 = "showOrgDealer('"+dealerCodeInputName+"','"+dealerIdInputName+"',true,'',true,'','10771002')";
 		var sel4 = "wrapOut2('"+dealerCodeInputName+"','"+dealerIdInputName+"');";
	    var addTable = document.getElementById(tableId);
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(1);
		
		
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
		addTable.rows[1].cells[0].innerHTML =  "";
		addTable.rows[1].cells[1].innerHTML =  "";
		addTable.rows[1].cells[2].innerHTML =  "<td>"+
		'<input type="text" name="dealer_code" id="'+dealerCodeInputName+'" class="middle_txt" readonly="readonly"/>'+
		'<input type="hidden" name="dealer_id" id="'+dealerIdInputName+'"/>'+
		'<input type="button" class="mini_btn" value="..." onclick="'+sel3+'"/>'+
       	'<input type="button" class="normal_btn" value="清除" onclick="'+sel4+'"/>'
		+"</td>";
			addTable.rows[1].cells[3].innerHTML =  eval(sel);
			addTable.rows[1].cells[4].innerHTML =  '<td><span class="tbwhite"><input type="text" name="PER_NAME" datatype="1,is_null" class="short_txt" value="" size="5" /></span>'+
			'<input type=hidden name="CONNECT_ID" value=""/>'
			+'<span style="color:red">*</span></td>';
			addTable.rows[1].cells[5].innerHTML =  '<td><input type="text" name="PER_PHONE" datatype="1,is_null" class="middle_txt"   value="" size="15" maxlength="11"  /><span style="color:red">*</span></td>';
			addTable.rows[1].cells[6].innerHTML =  '<td>&nbsp;</td>';
			addTable.rows[1].cells[7].innerHTML =  '<td><input type="text" name="PER_REMARK" class="long_txt"   value="" size="20" maxlength="21" /></td>';
			addTable.rows[1].cells[8].innerHTML =  eval(sel2);
			addTable.rows[1].cells[9].innerHTML =  "<a href=\"#\" onclick=\"deleteRowPre(this,'itemTableId')\">[删 除]<a>";
	}
	// 动态生成表格
 	function modifyRow(obj,connectId,pose,name,phone,remark,status,hotLine,dealerId){
 		var sel = "genSelBoxExpforThisPage('PER_POSE',"+<%=Constant.STATION_HEAD_AND_AEGIS%>+",'"+pose+"',false,'','','true','')";
 		var sel2 = "genSelBoxExpforThisPage('STATUS',"+<%=Constant.STATUS%>+",'"+status+"',false,'','','true','')";
 		var tr = obj.parentNode.parentNode;
 		tr.childNodes[3].innerHTML =  eval(sel);
 		tr.childNodes[4].innerHTML =  '<td><span class="tbwhite"><input type="text" name="PER_NAME" datatype="1,is_null" class="short_txt" value="'+name+'" size="5" /></span><span style="color:red">*</span>'+
 		'<input type=hidden name="CONNECT_ID" value="'+connectId+'"/><input type=hidden name="dealer_id" value="'+dealerId+'"/>'
 		+'</td>';
 		tr.childNodes[5].innerHTML =  '<td><input type="text" name="PER_PHONE" datatype="1,is_null" class="middle_txt"   value="'+phone+'" size="15" maxlength="11"  /><span style="color:red">*</span></td>';
 		tr.childNodes[6].innerHTML =  '<td>'+hotLine+'</td>';
 		tr.childNodes[7].innerHTML =  '<td><input type="text" name="PER_REMARK" class="long_txt"   value="'+remark+'" size="20" maxlength="21" /></td>';
 		tr.childNodes[8].innerHTML =  eval(sel2);
 		tr.childNodes[9].innerHTML =  "<a href=\"#\" onclick=\"deleteRowPre(this,'itemTableId')\">[删 除]<a>";
	}
 	//根据FIXCODE形成下拉框，除了expStr字符串中包含的状态，expStr可为多个中间用逗号隔开
 	function genSelBoxExpforThisPage(id,type,selectedKey,setAll,_class_,_script_,nullFlag,expStr){
 		var str = "";
 		var arr;
 		if(expStr.indexOf(",")>0)
 			arr = expStr.split(",");
 		else {
 			expStr = expStr+",";
 			arr = expStr.split(",");
 		}
 		str += "<select id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
 		// modified by lishuai@infoservice.com.cn 2010-05-18 解决select下拉框中增加属性datatype判断bug begin
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
 	function save(){
		if(submitForm(fm)==false)return;
		var perPhone = document.getElementsByName('PER_PHONE');
		var perName = document.getElementsByName('PER_NAME');
		for(var i=0;i<perPhone.length;i++){
			if(perPhone[i].value==''||perPhone[i].value==null){
				MyAlert('请输入站长电话！');
				return;
			}
			if(perName[i].value==''||perName[i].value==null){
				MyAlert('请输入姓名！');
				return;
			}
		}
		
		MyConfirm('确认修改?',saveDo);
		
	}
	function saveDo(){
		fm.action="<%=contextPath%>/sysusermng/sysuserinfo/SysPasswordManager/saveClaimPhone.do"
		fm.submit();
	}
	//双击变成input
	function bianInput(obj,flag,val){
		if(flag){
			obj.innerHTML = "<input type=text name='HOT_LINE_PHONE' id='HOT_LINE_PHONE' value='"+val+"' onblur='bianInput(this,false,"+val+");'/>";
			document.getElementById('HOT_LINE_PHONE').focus();
		}else{
			//obj.parentNode.innerText = val;
			submitForm(fm);
		    fm.action = "<%=contextPath%>/sysusermng/sysuserinfo/SysPasswordManager/saveDealerHotLinePhone.do";
		    fm.submit();
		}
	}
	function roRender(value,metadata,record) {
		return '<a href="#" onclick="roLink(\''+record.data.DEALER_ID+'\')" >'+value+'</a>';
	}
	function roLink (dealerId){
		var fm = document.getElementById('fm');
		fm.action="<%=contextPath%>/sysusermng/sysuserinfo/SysPasswordManager/saveDealerHotLinePhone.do?dealer_id="+dealerId;
		fm.submit();
	}
	//删除行
	function deleteRowPre(obj,tableId){
	
	 MyConfirm("您确定要删除?",deleteRowConfirm,[obj,tableId]);
		//deleteRowConfirm(obj,tableId);
 	} 
	</script>
</body>
</html>
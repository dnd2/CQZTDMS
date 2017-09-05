<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ taglib uri="/jstl/cout" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
    List<Map<String, Object>>  detailList = (List<Map<String, Object>>)request.getAttribute("detailList");
    
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户服务资料审批表维护</title>
</head>
<body>
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 信息反馈管理&gt;信息反馈提&gt;服务资料审批表修改</div>
  <form method="post" name = "fm" id="fm">
  <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
	 <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 基本信息</th>
          <tr>
            <td align="right">工单号：</td>
            <td><c:out value="${map.ORDER_ID}"/></td>
            <td align="right"><input type="hidden" name="orderId" id="orderId" value="<c:out value="${map.ORDER_ID}"/>"></td>
            <td>&nbsp;</td>
            <td align="right">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td align="right">经销商联系人：</td>
            <td><input type='text'  name="linkman"  id='linkmanId' value="<c:out value="${map.LINK_MAN}"/>"   class="middle_txt"  datatype="0,is_null,10"/></td>
            <td align="right">经销商电话：</td>
            <td><input type='text'  name="tel"  id='telId' value="<c:out value="${map.TEL}"/>"   class="middle_txt"  datatype="0,is_phone,15"/></td>
            <td height="27"  align="right">经销商传真：</td>
            <td ><input type='text'  name="fax"  id='faxId' value="<c:out value="${map.FAX}"/>"   class="middle_txt"  datatype="0,is_phone,15"/></td>
          </tr>
          <tr>
            <td height="27"  align="right">邮政编码：</td>
            <td ><input type='text'  name="zipCode"  id='zipCode'  value="<c:out value="${map.ZIP_CODE}"/>"  class="middle_txt"  datatype="0,is_phone,15"/></td>     
          	<td height="27"  align="right">邮寄方式：</td>
            <td>
            	<input type="hidden" name="mailType" value="<%=Constant.ORDER_SI_MAIL_TYPE_02%>"/>
            	<script type="text/javascript">
                	//genSelBoxExp("mailType",<%=Constant.ORDER_SI_MAIL_TYPE%>,"",false,"short_sel","","false",'');
                	document.write(getItemValue("<%=Constant.ORDER_SI_MAIL_TYPE_02%>"));
                	document.write(" <span style='color:red'>(经销商自己付费)</font>");
                </script>   
            </td>
            </tr>
          <tr>
           <td height="27"  align="right">邮寄地址：</td>
            <td><input type='text'  name='mailAddress'  id='mailAddressId' value="<c:out value="${map.MAIL_ADDRESS}"/>"  class="maxlong_txt"  datatype="0,is_null,30"/></td>
          </tr>
          <tr>
            <td height="27" align="right">申请内容：</td>
            <td height="27" colspan="5" align="left" ><span class="tbwhite">
              <textarea  name='selContent'  id='selContentId'   rows='5' cols='80' datatype="0,is_null,10"><c:out value="${map.SE_CONTENT}"/></textarea>
            </span></td>
          </tr>    
  </table>
  <br>
  <table width=100% border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="91908E"  class="table_edit">
    <tr>
	     <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />资料明细
         <input type="Button" name="addButton" onclick="addRecord()" class="normal_btn" style="width=5%" value="新增"/>
         <input type="Button" name="delButton" onclick="delRow()" class="normal_btn" style="width=5%" value="删除"/></th>
    </tr> 
    <table class="table_list"  style="border-bottom:1px solid #DAE0EE">
	<tr>
       <th>
	     <input name="checkAll" id="checkAll" type="checkbox" onClick="selectAll();"/>
	   </th>   
       <th align="center">名称</th>
       <th align="center">数量</th>
       <th align="center">单价</th>
       <th align="center">总价</th>
       <th align="center">说明</th>
    </tr>
    <tbody id="activeTable">
     <% if(detailList!=null&&detailList.size()!=0){ %>
    	<c:forEach items="${detailList}" var="dl">
       		<tr class="table_list_row1" id="<c:out value="${dl.DATA_ID}"/>">
       		    <td><input type='checkbox' name='dataIds' value="<c:out value="${dl.DATA_ID}"/>" onclick='checkselectAllBox(this)'/></td>
            	<td><c:out value="${dl.DATA_NAME}"/></td>
            	<td><input type='text' name='amounts'  value="<c:out value="${dl.AMOUNT}"/>" onblur='calculate(this,<c:out value="${dl.PRICE}"/>,<c:out value="${dl.DATA_ID}"/>)' class='middle_txt' maxlength='60'/></td>
            	<td><c:out value="${dl.PRICE}"/></td>
	            <td><c:out value="${dl.SUMPRICE}"/></td>	
           	 	<td><input type='text' name='remarks' value="<c:out value="${dl.REMARK}"/>" class='middle_txt' maxlength='200'/></td>
           	 	<td><input type="hidden" name='flag' value="1"></td>
        	</tr>
    	</c:forEach>
      <%} %>
    </tbody>
    </table>
  </table>
  <table width=100% border="0" cellspacing="0" cellpadding="0">
	<tr><td height="12" align=center>&nbsp;</td></tr>
    <tr><td height="12" align=center>      
		<input type="button" name="saveBtn" onClick="updateServiceInfo()" class="normal_btn" style="width=8%" value="确定"/>
			&nbsp;&nbsp;
		<input type="button" onClick="javascript:history.go(-1);" class="normal_btn" style="width=8%" value="返回"/>
        </td>
    </tr>
  </table>
 <input type="hidden" name="str" id="strId" value=""/>
 <input type="hidden" name="delstr" id="delstrId" value=""/>
</form>
<script type="text/javascript">
 // 保存维护好的服务资料
 function updateServiceInfo(){
       var cnt = 0;
	   var l=document.getElementsByName("dataIds").length;
	   var chk = document.getElementsByName("dataIds");
	   var count = document.getElementsByName("amounts");
	   var remarks =  document.getElementsByName("remarks");
	   var flags = document.getElementsByName("flag");
	   var str="";
	   var content = document.getElementById('selContentId').value;
	   var le = document.getElementById('selContentId').value.length;
	   if(content == null || content == ''){
	     MyAlert("申请内容是必填项！");
	   }else if(le <= 0 || le > 200){
	     MyAlert("申请内容不能超过200个字符！");
	   	}else{
		   if(chk.length==0){
			    MyAlert("请添加资料明细！");
			}else{
			for(var i=0;i<l;i++){
				//if(chk[i].checked){
				   //MODIFY BY XIAYANPENG BEGIN 当用户不输入数量时，提示用户输入数量
				  if(count[i].value.trim()==""||parseFloat(count[i].value.trim())==parseFloat(0))
				   {
				   		MyAlert("请输入数量！");
				   		return;
				   }
				  	var strTemp= "0123456789"; 
				    for(var j=0;j<count[i].value.length;j++){
				    if(strTemp.indexOf(count[i].value.charAt(j))==-1){
				    	MyAlert("数量字符无效！");
				   		return;
				    }
				   }
				   //MODIFY BY XIAYANPENG END
				    str = str+","+chk[i].value+"@"+count[i].value+"@"+flags[i].value+"@"+remarks[i].value;
			       cnt++;
				//}
			 }
			 document.getElementById("strId").value = str;
		     saveConfirm(str);
		     }
		   }
    }
    
  function  saveConfirm(str){
  	submitForm(fm);
    fm.action = "<%=contextPath%>/feedbackmng/apply/ServiceInfoApplyManager/modifyServiceInfo.do?";
    MyConfirm("确认修改?",fm.submit);
    //fm.submit();
    
  }
   
  
    
 // 新增服务资料
 function addRecord(){
        OpenHtmlWindow('<%=contextPath%>/feedbackmng/apply/ServiceInfoApplyManager/addServiceDataInit.do',800,500);
    }
  
 // 接收子窗口传回的值同时在生成表格之前进行判断选择的服务资料是否存在  
 function getSelectValue(dataId,dataName,price){
        var trNode = document.getElementById(dataId);
        if(trNode!=null){
          var trID = trNode.id;
          if(trID.indexOf(dataId)==0){
            MyAlert("该资料已存在，请选择不同的资料！");
		    return ;
          }else{
            addRow(dataId,dataName,price);
          }
          
        }else{
        
         addRow(dataId,dataName,price);
        }
 }
 
 // 动态生成表格
 function addRow(dataId,dataName,price){
	    var addTable = document.getElementById("activeTable");
		var rows = addTable.rows;
		var length = rows.length;
		var insertRow = addTable.insertRow(length);
		insertRow.className = "table_list_row1";
		insertRow.id = dataId;
		insertRow.insertCell(0);
		insertRow.insertCell(1);
		insertRow.insertCell(2);
		insertRow.insertCell(3);
		insertRow.insertCell(4);
		insertRow.insertCell(5);
		insertRow.insertCell(6);
		addTable.rows[length].cells[0].innerHTML =  "<td nowrap='nowrap'><input type='checkbox' name='dataIds' value='" +dataId+"' onclick='checkselectAllBox(this)'/></td>";
		addTable.rows[length].cells[1].innerHTML =  "<td nowrap='nowrap'>"+dataName+"</td>";
		addTable.rows[length].cells[2].innerHTML =  "<td nowrap='nowrap'><input type='text' name='amounts'  onblur='calculate(this,"+price+","+dataId+")' class='middle_txt' maxlength='60'/></td>";
		addTable.rows[length].cells[3].innerHTML =  "<td>"+price+"</td>";
		addTable.rows[length].cells[4].innerHTML =  "<td></td>";
		addTable.rows[length].cells[5].innerHTML =  "<td nowrap='nowrap'><input type='text' name='remarks' class='middle_txt' value='' maxlength='200'/></td>";
		addTable.rows[length].cells[6].innerHTML =  "<td><input type='hidden' name='flag' value='0'></td>";
		
	}
	
 // 删除表格之前判断checkbox是否被选择
 function delRow(){
		var str="";
		var chk = document.getElementsByName("dataIds");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++){        
		if(chk[i].checked)
		{            
			str = chk[i].value+","+str; 
			cnt++;
		}
		}
		if(cnt==0){
		        MyAlert("请选择！");
		        return;
		    }else{
		     	if(confirm("确认提交？")){
		       	deleteConfirm();
		       }else{
		       return;
		       }
		    }
 }
 
 // 删除表格
 function deleteConfirm(){
 
    var tbodyNode=document.getElementById("activeTable");
    var orderId = document.getElementById("orderId");
    
	// 取所有name为dataIds的节点
	var chkes=document.getElementsByName("dataIds");
	var flags = document.getElementsByName("flag");
    var delStr = "";
   
	for(var i=chkes.length-1;i>-1;i--){
		if(chkes[i].checked){
		    if(flags[i].value==1){
		      delStr = delStr+","+chkes[i].value;
		      document.getElementById("delstrId").value=delStr;
		    }
		    delDetail();
		    var trNode=document.getElementById(chkes[i].getAttribute("value"));
			tbodyNode.removeChild(trNode);
		}
	}
 	$('checkAll').checked = false ;
 }
 
 function delDetail(){
    makeNomalFormCall('<%=contextPath%>/feedbackmng/apply/ServiceInfoApplyManager/serviceInfoDetailDel.json',showResult,'fm');
    
 }
 
 
	// 删除回调函数
	function showResult(json){
		var rtnValue = json.returnValue;
		if(rtnValue==1){
			__extQuery__(1);
			MyAlert("删除成功！");
		}else{
			MyAlert("删除失败！请联系管理员！");
		}
	}
 
 // checkbox全选
 function selectAll(){
 	   var chkAll=document.getElementById("checkAll");
		for(var i=0;i<document.forms[0].elements.length;i++){
			if(document.forms[0].elements[i].type=="checkbox"){
				if(document.forms[0].elements[i].name.indexOf("dataIds")==0){
					document.forms[0].elements[i].checked=chkAll.checked;		
				}	
			}
		}
 
 }
 
 // 计算总价显示在页面中
 function calculate(obj,price,dataId){
      var count = obj.value;
      var sumPrice = price*count;
      var trNode=document.getElementById(dataId);
      var newtext=document.createTextNode(sumPrice);
      var tdNode = trNode.cells[4];
      tdNode.innerHTML="";
      tdNode.appendChild(newtext);   
 }
</script>
</body>
</html>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>通用选择框</title>

<script language="JavaScript">

</script>
</head>
<body onload="__extQuery__(1);" onbeforeunload="doSupp();">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 选择配件</div>
<form method="post" name ="fm" id="fm">
	<input id="PART_CNAME" name="PART_CNAME" type="hidden" value="" />
	<input id="PART_ID" name="PART_ID" type="hidden" value=""/>	
	<input id="PART_CODE" name="PART_CODE" type="hidden" value=""/>	
	<input id="PART_OLDCODE" name="PART_OLDCODE" type="hidden" value=""/>
	<input id="giftType" name="giftType" type="hidden" value="${giftType }"/>	
	<input id="condition" name="condition" type="hidden" value="${condition }"/>
	<input id="giftWay" name="giftWay" type="hidden" value="${giftWay }"/>
	<input id="isOemStart" name="isOemStart" type="hidden" value="${isOemStart }"/>
	<input id="checkEDate" name="checkEDate" type="hidden" value="${endDate }"/>
	<input id="checkSDate" name="checkSDate" type="hidden" value="${startDate }"/>		
	<table class="table_edit">
    <tr>
      <td   align="right" nowrap="nowrap">配件编码：</td>
      <td class="table_query_3Col_input" nowrap="nowrap">
      	<input class="middle_txt" id="partOldCode" name="partOldCode" value="" type="text"/>
      </td>
      <td   align="right" nowrap="nowrap">配件名称：</td>
      <td>
      	<input name="partCname" type="text" id="partCname"  value="" class="middle_txt"/>
      </td>
      <td>&nbsp;</td>
      <td>
      	<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
      </td>
  	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<div style="margin-top:25px;float: left" id="sel">
           <input name="queren1" type="button" class="cssbutton" onclick="checkAll();" value="全选" />
           <input class="cssbutton" type="button" name ="queren2" value="全不选" onclick="doDisAllClick()"/>
           <input name="queren3" type="button" class="cssbutton" onclick="doConfirm();" value="确认" />               
		</div>
		<script language="JavaScript">
		var isMulti="true";
	    	if(isMulti == "true"){
		    	document.getElementById("sel").style.display = "";
	    	}
	    	else
	    	{  
	    		document.getElementById("sel").style.display = "none";
	    	}	
	    </script>
<script type="text/javascript" >


    
	var myPage;

	var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartBuyPriceManager/queryPartInfo.json";
				
	var title = null;

	var columns = [
				{header: "选择", dataIndex: 'PART_ID', align:'center',renderer:seled},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
				{header: "配件件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'}
		      ];
		      
	function seled(value,meta,record){
		var data = record.data;
		var isMulti = "true";
 		if(isMulti == "false"){
 			return "<input type='radio' onclick='singleSelect(\""+value+"\",\""+data.PART_CODE+"\",\""+data.PART_OLDCODE+"\",\""+data.PART_CNAME+"\")'/>";
 		}else{
        	return "<input type='checkbox' name='checkID' id='checkID' value='"+ value+"' />";
        }
    }
    
    function singleSelect(val1,val2,val3,val4){
    	 $('PART_ID').value = val1;
    	 $('PART_CODE').value = val2;
    	 $('PART_OLDCODE').value = val3;
    	 $('PART_CNAME').value = val4;
    	_hide();
    }
  //关闭弹出窗口的时候执行该方法
    function doSupp()
	{   
    	parentContainer.__extQuery__(1);
	}
	
	
	function doDisAllClick()
	{
		var chk = document.getElementsByName("checkID");
		var l = chk.length;
		for(var i=0;i<l;i++)
		{        
			chk[i].checked = false;
		}
	}

	function checkAll(){
		var groupCheckBoxs=document.getElementsByName("checkID");
		if(!groupCheckBoxs) return;
		for(var i=0;i<groupCheckBoxs.length;i++)
		{
			groupCheckBoxs[i].checked=true;
		}
	}
	
	function doConfirm()
	{
		var chk = document.getElementsByName("checkID");
		var l = chk.length;
		var cnt = 0;
		for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{            
				cnt++;			
			}
		}
        if(cnt==0)
        {
             MyDivAlert("请选择配件!");
        }
        else
        {
        	if(confirm("确认是否新增?"))
    		{
    			commitForm();
    		}
		}
	}

	function commitForm()
	{
		var chk = document.getElementsByName("checkID");
		var l = chk.length;
		var ids = "";
        for(var i=0;i<l;i++)
		{        
			if(chk[i].checked)
			{
				ids += chk[i].value + ",";
			}				
		}

		btnDisable();
		var url = '<%=contextPath%>/parts/baseManager/mainData/mainDataMaintenance/insrtPartMaintenance.json?ids='+ids;
  		makeFormCall(url,showResult,'fm');
	}

	function showResult(json) {
		btnEnable();
	    if (json.errorExist != null && json.errorExist.length > 0) {
	    	MyAlert("配件：【" + json.errorExist + "】已添加，不能重复添加!");
	    } else if (json.success != null && json.success == "true") {
	    	MyAlert("新增成功!");
	 //   	parentContainer.__extQuery__(1);
			_hide();        
	    } else {
	        MyAlert("新增失败，请联系管理员!");
	    }
	}

	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
</script>
</body>
</html>
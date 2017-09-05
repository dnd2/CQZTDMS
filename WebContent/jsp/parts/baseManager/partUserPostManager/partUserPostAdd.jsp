<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>配件人员类型设置</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<style>#add_tab td {padding:3px 0}</style>
    <script language="javascript" type="text/javascript">
        //返回
        function reBack() {
        	btnDisable();
            window.location = "<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/partUserPostInit.do";
        }

        //表单提交方法：
        function checkForm() {
        	var obj = document.getElementsByName("userIds");
        	var users = [];
        	var k = 0;
        	for(var i=0;i<obj.length;i++){
        		users[k] = obj[i].value;
   				k++;
    		}
        	btnDisable();
        	var url = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/insertPartUserPost.json?users='+users;
        	makeNomalFormCall(url,showResult,'fm');
        }
        function showResult(json) {
        	btnEnable();
            if (json.errorExist != null && json.errorExist.length > 0) {
            	MyAlert("人员：【" + json.errorExist + "】已添加，不能重复添加!");
            } else if (json.success != null && json.success == "true") {
            	MyAlert("新增成功!");
                window.location.href = "<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/partUserPostInit.do";
            } else {
                disableBtn($("#saveBtn")[0]);
                MyAlert("新增失败，请联系管理员!");
            }
        }
        //表单提交前的验证：
        function checkFormUpdate() {
        	var fixValue = document.getElementById("fixValue").value;
        	if("" == fixValue || null == fixValue)
        	{
        		MyAlert('请选择一种人员类型!');
    			return false;
        	}
    		var userIds = document.getElementsByName("userIds");
    		if( null == userIds || userIds.length <= 0)
    		{
    			MyAlert('请至少选择一个新增的人员!');
    			return false;
    		}
    		var cks = document.getElementsByName('isLeader');
    		var cks1 = document.getElementsByName('isDirect1');
    		var cks2 = document.getElementsByName('isChkZy1');
    		var leaderIds = "";
    		var directIds = "";
    		var chkZyIds = "";
    		for(var i =0 ;i<cks.length;i++){
    			if(cks[i].checked)
    			{
    				leaderIds += cks[i].value + ",";
    			}
    		}
    		for(var i =0 ;i<cks1.length;i++){
    			if(cks1[i].checked)
    			{
    				directIds += cks1[i].value + ",";
    			}
    		}
    		for(var i =0 ;i<cks2.length;i++){
    			if(cks2[i].checked)
    			{
    				chkZyIds += cks2[i].value + ",";
    			}
    		}
    		document.getElementById("leaderIds").value = leaderIds;
    		document.getElementById("directIds").value = directIds;
    		document.getElementById("chkZyIds").value = chkZyIds;
            MyConfirm("确认是否新增?", checkForm);
        }

        function showPart(){
            var fixValue = $("#fixValue").val();
            if(!fixValue){
                MyAlert("请先选择人员类型!");
                return;
            }
    		var url = '<%=contextPath%>/parts/baseManager/partUserPostManager/partUserPostAction/queryUsersForAddInit.do?fixValue='+fixValue ;
    		OpenHtmlWindow(url,700,500);
    	}
        
        // 添加人员
    	function setPartCode(userIdsNames,fixValue){
    		var tab = $('#add_tab')[0];
    		var preUserIds = document.getElementsByName("userIds");
    		var rpUserNames = "";
    		var userIdArr = new Array(); 
    		if(null != preUserIds && preUserIds.length > 0)
    		{
        		var idsSize = preUserIds.length;
    			for(var i = 0; i < idsSize; i++)
        		{
    				userIdArr.push([preUserIds[i].value]);
        		}
    		}
    		var strTemp;
    		for(var i = 0 ;i<userIdsNames.length;i++){
    			strTemp = userIdsNames[i].toString();
   			 	//定义一数组
	   			strsTemp = strTemp.split("@@"); //字符分割     
	   			var userId = strsTemp[0];
	   			var userName = strsTemp[1];

	   			if(userIdArr.length > 0 && userIdArr.toString().indexOf(userId) > -1)
	   			{
	   				rpUserNames = rpUserNames + userName + " ";
	   			}
	   			else
	   			{
	   				var idx = tab.rows.length ;
	    			var insert_row = tab.insertRow(idx);
	    			if(idx%2==0){
	    				insert_row.className = 'table_list_row2';
	    			}
	    			else {
	    				insert_row.className = 'table_list_row1' ;
	    			}

    				if(fixValue==1){//如果人员类型是计划员
    					insert_row.insertCell(0);
    	    			insert_row.insertCell(1);
    	    			insert_row.insertCell(2);
    	    			insert_row.insertCell(3);
    	    			insert_row.insertCell(4);
    	    			insert_row.insertCell(5);
    	    			
    	    			var cur_row = tab.rows[idx];
    	    			cur_row.cells[0].innerHTML = idx;
    	    			cur_row.cells[1].innerHTML = userName +'<input type="hidden" name="userIds" value='+userId+'><input type="hidden" name="userNames" value='+ userName +'>' ;
    	    			cur_row.cells[2].innerHTML = '<input type="checkbox" name="isLeader" value='+userId+' />';
    	     			cur_row.cells[3].innerHTML = '<input type="checkbox" name="isDirect1" value='+userId+' />';
    	     			cur_row.cells[4].innerHTML = '<input type="checkbox" name="isChkZy1" value='+userId+' />';
    	     			cur_row.cells[5].innerHTML = '<input class="u-button u-cancel u-small-button" type="button" value="删 除" onclick="deleteTblRow('+ idx +')"/>' ;
    				}else{
    					insert_row.insertCell(0);
    	    			insert_row.insertCell(1);
    	    			insert_row.insertCell(2);
    	    			insert_row.insertCell(3);
    	    			
    	    			var cur_row = tab.rows[idx];
    	    			cur_row.cells[0].innerHTML = idx;
    	    			cur_row.cells[1].innerHTML = userName +'<input type="hidden" name="userIds" value='+userId+'><input type="hidden" name="userNames" value='+ userName +'>' ;
    	    			cur_row.cells[2].innerHTML = '<input type="checkbox" name="isLeader" value='+userId+' />';
    	     			cur_row.cells[3].innerHTML = '<input class="u-button u-cancel u-small-button" type="button" value="删 除" onclick="deleteTblRow('+ idx +')"/>' ;
    				}
	    			
	   			}
    		}
    		if("" != rpUserNames)
    		{
        		MyAlert("人员：【" + rpUserNames + "】不能被重复添加!");
    		}
    	}

    	function deleteTblRow(rowNum) {
    		var fixValue = $("#fixValue").val();
    		var tbl = document.getElementById('add_tab');
    		tbl.deleteRow(rowNum);
    		var count = tbl.rows.length;
    		for (var i=rowNum;i<=count;i++)
    	       {
    	         tbl.rows[i].cells[0].innerText=i;
    	         if(fixValue==1){
    	        	 tbl.rows[i].cells[5].innerHTML='<input type="button" value="删 除" onclick="deleteTblRow('+i+');"/>'
    	         }else{
    	        	 tbl.rows[i].cells[3].innerHTML='<input type="button" value="删 除" onclick="deleteTblRow('+i+');"/>'
    	         }
    	         if((i-1)%2==0){
    		    	tbl.rows[i].className   = "table_list_row1";
    			  }else{
    				  tbl.rows[i].className  = "table_list_row2";
    			  }
    	      }
    	}

    	//全选按钮
    	function selAll(obj){
    		var cks = document.getElementsByName('isLeader');
    		for(var i =0 ;i<cks.length;i++){
    			if(obj.checked){
    				cks[i].checked = true;
    			}else{
    				cks[i].checked = false;
    			}
    		}
    	}

    	//全选按钮
    	function selAll1(obj){
    		var cks = document.getElementsByName('isDirect1');
    		for(var i =0 ;i<cks.length;i++){
    			if(obj.checked){
    				cks[i].checked = true;
    			}else{
    				cks[i].checked = false;
    			}
    		}
    	}

    	//全选按钮
    	function selAll2(obj){
    		var cks = document.getElementsByName('isChkZy1');
    		for(var i =0 ;i<cks.length;i++){
    			if(obj.checked){
    				cks[i].checked = true;
    			}else{
    				cks[i].checked = false;
    			}
    		}
    	}

    	function changeTd(obj){
        	var fixValue = obj.value;
        	if(fixValue==1){
            	$("#isDirect")[0].style.display="";
            	$("#isChkZY")[0].style.display="";
        	}else{
        		$("#isDirect")[0].style.display="none";
        		$("#isChkZY")[0].style.display="none";
        	}
        	var tbl = document.getElementById('add_tab');
       	    var len = tbl.rows.length;
       	    if (len > 1) {
       	        for (var i = tbl.rows.length - 1; i >= 1; i--) {
       	            tbl.deleteRow(i);
       	        }
       	        $("#headCK")[0].checked = false;
       	        $("#headCK1")[0].checked = false;
       	        $("#headCK2")[0].checked = false;
       	    }
    	}
    </script>
</head>
<body>
<form name='fm' id='fm'>
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 配件人员类型设置 &gt; 新增
        </div>
        <table class="table_edit">
          <input type="hidden" id="leaderIds" name="leaderIds" value=""/>
          <input type="hidden" id="directIds" name="directIds" value=""/>
          <input type="hidden" id="chkZyIds" name="chkZyIds" value=""/>
            <tr>
                <td width="10%" align="right" >人员类型：</td>
		        <td  width="25%">
		        <select name="fixValue" id="fixValue" class="u-select" onchange="changeTd(this);">
					<option value="">-请选择-</option>
					<c:if test="${postList!=null}">
						<c:forEach items="${postList}" var="list">
							<option value="${list.FIX_VALUE }">${list.FIX_NAME }</option>
						</c:forEach>
					</c:if>
				</select>
				<font color="red">*</font>
		        </td>
                <td width="10%"  align="right"></td>
                <td width="25%"></td>
                <td width="10%"    align="right"></td>
                <td width="25%"></td>
            </tr>
        </table>
        <table class="table_list" id="add_tab">
		<tr class="table_list_row1" style="background-color: #DAE0EE;">
	   		<td>序号</td>
			<td>人员名称</td>
			<td><input type="checkbox" id="headCK" name="headCK" onclick="selAll(this)"/>是否主管</td>
			<td id="isDirect" style="display:none"><input type="checkbox" id="headCK1" name="headCK1" onclick="selAll1(this)"/>是否直发计划员</td>
			<td id="isChkZY" style="display:none"><input type="checkbox" id="headCK2" name="headCK2" onclick="selAll2(this)"/>是否审核专员</td>
			<td><input type="button" value="选择人员" class="long_btn u-button u-query u-small-button" onclick="showPart();"/></td>
		</tr>
		</table>
		<br />
		
		<table class="table_edit tb-button-set">
            <tr>
                <td align="center">
                    <input type="button" name="saveBtn" id="saveBtn" value="确  认" onclick="checkFormUpdate();" class="u-button u-submit"/>
                    <input type="button" name="backBtn" id="backBtn" value="返 回" onclick="reBack()" class="u-button"/>
                </td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>

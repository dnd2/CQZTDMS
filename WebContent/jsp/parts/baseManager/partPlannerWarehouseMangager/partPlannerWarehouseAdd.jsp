<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>计划员与仓库维护</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<style>#add_tab td {padding:3px 0}</style>
    <script language="javascript" type="text/javascript">
        //返回
        function reBack() {
        	btnDisable();
            window.location = "<%=contextPath%>/parts/baseManager/partPlannerWarehouseManager/partPlannerWarehouseAction/partPlannerWarehouseInit.do";
        }

        //选择计划员
        function sel() {
            OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partPlannerWarehouseManager/partPlannerWarehouseAction/partPlannerSelect.do', 700, 400);
        }

        //表单提交方法：
        function checkForm() {
        	var plannerID = document.getElementById("plannerID").value;
        	var obj = document.getElementsByName("whIds");
        	var whIds = [];
        	var k = 0;
        	for(var i=0;i<obj.length;i++){
   				whIds[k] = obj[i].value;
   				k++;
    		}
        	btnDisable();
        	var url = '<%=contextPath%>/parts/baseManager/partPlannerWarehouseManager/partPlannerWarehouseAction/insertPartPlannerWarehouse.json?whIds='+whIds+'&plannerID='+plannerID;
        	makeNomalFormCall(url,showResult,'fm');
        }
        function showResult(json) {
        	btnEnable();
            if (json.errorExist != null && json.errorExist.length > 0) {
            	 MyAlert("仓库：【" + json.errorExist + "】已与计划员创建关系，不能重复创建!");
            } else if (json.success != null && json.success == "true") {
            	MyAlert("新增成功!", function(){
	                window.location.href = "<%=contextPath%>/parts/baseManager/partPlannerWarehouseManager/partPlannerWarehouseAction/partPlannerWarehouseInit.do";
            	});
            } else {
                disableBtn($("saveBtn"));
                MyAlert("新增失败，请联系管理员!");
            }
        }
        //表单提交前的验证：
        function checkFormUpdate() {
        	var plannerID = document.getElementById("plannerID").value;
        	if("" == plannerID || null == plannerID)
        	{
        		MyAlert('请选择一个计划员!');
    			return false;
        	}
    		var whIds = document.getElementsByName("whIds");
    		if( null == whIds || whIds.length <= 0)
    		{
    			MyAlert('请至少选择一个新增的仓库!');
    			return false;
    		}
            MyConfirm("确认是否新增?", checkForm);
        }

        function showPart(){
    		var url = '<%=contextPath%>/parts/baseManager/partPlannerWarehouseManager/partPlannerWarehouseAction/queryWarehousefoAddInit.do' ;
    		OpenHtmlWindow(url,700,400);
    	}
    	
    	function setPartCode(whIdsNames){
    		var tab = $('#add_tab')[0];
    		var preWHIds = document.getElementsByName("whIds");
    		var rpWHNames = "";
    		var whIdArr = new Array(); 
    		if(null != preWHIds && preWHIds.length > 0)
    		{
        		var idsSize = preWHIds.length;
    			for(var i = 0; i < idsSize; i++)
        		{
            		whIdArr.push([preWHIds[i].value]);
        		}
    		}
    		var strTemp;
    		for(var i = 0 ;i<whIdsNames.length;i++){
    			strTemp = whIdsNames[i].toString();
   			 	//定义一数组
	   			strsTemp = strTemp.split("@@"); //字符分割     
	   			var whId = strsTemp[0];
	   			var whName = strsTemp[1];

	   			if(whIdArr.length > 0 && whIdArr.toString().indexOf(whId) > -1)
	   			{
	   				rpWHNames = rpWHNames + whName + " ";
	   			}
	   			else
	   			{
	   				var idx = tab.rows.length ;
	    			var insert_row = tab.insertRow(idx);
	    			if(idx%2==0)
	    				insert_row.className = 'table_list_row2';
	    			else 
	    				insert_row.className = 'table_list_row1' ;
	    			insert_row.insertCell(0);
	    			insert_row.insertCell(1);
		   			insert_row.insertCell(2);
	    			var cur_row = tab.rows[idx];
	    			cur_row.cells[0].innerHTML = idx;
	    			cur_row.cells[1].innerHTML = whName +'<input type="hidden" name="whIds" value='+whId+'><input type="hidden" name="whNames" value='+ whName +'>' ;
	    			cur_row.cells[2].innerHTML = '<input class="u-button u-cancel u-small-button" type="button" value="删 除" onclick="deleteTblRow('+idx+');"/>' ;
	   			}
    		}
    		if("" != rpWHNames)
    		{
        		MyAlert("仓库：【" + rpWHNames + "】不能被重复添加!");
    		}
    	}

    	function deleteTblRow(rowNum) {
    		var tbl = document.getElementById('add_tab');
    		tbl.deleteRow(rowNum);
    		var count = tbl.rows.length;
    		for (var i=rowNum;i<=count;i++)
    	       {
    	         tbl.rows[i].cells[0].innerText=i;
    	         tbl.rows[i].cells[2].innerHTML='<input class="u-button u-cancel u-small-button" type="button" value="删 除" onclick="deleteTblRow('+i+');"/>'
    	         if((i-1)%2==0){
    		    	tbl.rows[i].className   = "table_list_row1";
    			  }else{
    				  tbl.rows[i].className  = "table_list_row2";
    			  }
    	      }
    	}
    	
    </script>
</head>
<body>
<div class="wbox">
	<form name='fm' id='fm'>
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 计划员与仓库维护 &gt; 新增
        </div>
        <table class="table_edit">
            <tr>
                <td class="right">计划员：</td>
                <td >
                    <input class="middle_txt" id="selectedName" name="selectedName" type="text" readonly="readonly" />
                    <input name="BUTTON" type="button" class="mini_btn" onclick="sel()" value="..."/>
                    <input type="hidden" name="plannerID" id="plannerID"/>
                    <font color="red">*</font>
                </td>
                <td class="right"></td>
                <td ><input type="hidden" class="normal_txt" id="remark" name="remark"/></td>
            </tr>
        </table>
        <table class="table_list" id="add_tab">
		<tr class="table_list_row1" style="background-color: #DAE0EE;">
	   		<td >序号</td>
			<td >仓库名称</td>
			<td><input class="u-button u-query u-small-button" type="button" value="选择仓库" class="long_btn" onclick="showPart();"/></td>
		</tr>
		</table>
		<br />
        <table class="table_edit tb-button-set">
            <tr>
                <td align="center">
                    <input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="checkFormUpdate();" class="u-button"/>
                    <input type="button" name="backBtn" id="backBtn" value="返 回" onclick="reBack()" class="u-button"/>
                </td>
            </tr>
        </table>
</form>
</div>
</body>
</html>

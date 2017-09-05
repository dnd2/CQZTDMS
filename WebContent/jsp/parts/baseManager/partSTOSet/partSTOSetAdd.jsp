<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>配件人员类型设置</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script language="javascript" type="text/javascript">
        //返回
        function reBack() {
        	btnDisable();
    		location = '<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/partSTOSetInit.do';
        }

        //表单提交方法：
        function checkForm() {
        	var obj = document.getElementsByName("partIds");
        	var parts = [];
        	var k = 0;
        	for(var i=0;i<obj.length;i++){
        		parts[k] = obj[i].value;
   				k++;
    		}
        	btnDisable();
        	var url = '<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/insertPartSTOSet.json?parts='+parts;
      		makeFormCall(url,showResult,'fm');
        }
        function showResult(json) {
        	btnEnable();
            if (json.errorExist != null && json.errorExist.length > 0) {
            	MyAlert("配件：【" + json.errorExist + "】已添加，不能重复添加!");
            } else if (json.success != null && json.success == "true") {
            	MyAlert("新增成功!");
                window.location.href = "<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/partSTOSetInit.do";
            } else {
                disableBtn($("saveBtn"));
                MyAlert("新增失败，请联系管理员!");
            }
        }
        //表单提交前的验证：
        function checkFormUpdate() {
        	var venderId = document.getElementById("venderId").value;
        	var brand = document.getElementById("brand").value;
        	var criterion = document.getElementById("criterion").value;
        	if("" == venderId || null == venderId)
        	{
        		MyAlert('请选择一个供应商!');
    			return false;
        	}

        	if("" == brand || null == brand)
        	{
        		MyAlert('请设置品牌分类!');
    			return false;
        	}

        	if("" == criterion || null == criterion)
        	{
        		MyAlert('请设置最小订货箱数!');
    			return false;
        	}
        	
    		var partIds = document.getElementsByName("partIds");
    		if( null == partIds || partIds.length <= 0)
    		{
    			MyAlert('请至少选择一个新增的配件!');
    			return false;
    		}
            MyConfirm("确认是否新增?", checkForm);
        }

        function showPart(){
    		var url = '<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/queryPartsForAddInit.do' ;
    		OpenHtmlWindow(url,700,500);
    	}
    	function showVender()
    	{
    		var url = '<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/queryVendersForAddInit.do' ;
    		OpenHtmlWindow(url,700,500);
    	}
    	
    	function setPartCode(partIdsNames){
    		var tab = $('add_tab');
    		var prePartIds = document.getElementsByName("partIds");
    		var rpPartNames = "";
    		var partIdArr = new Array(); 
    		if(null != prePartIds && prePartIds.length > 0)
    		{
        		var idsSize = prePartIds.length;
    			for(var i = 0; i < idsSize; i++)
        		{
    				partIdArr.push([prePartIds[i].value]);
        		}
    		}
    		var strTemp;
    		for(var i = 0 ;i<partIdsNames.length;i++){
    			strTemp = partIdsNames[i].toString();
   			 	//定义一数组
	   			strsTemp = strTemp.split("@@"); //字符分割     
	   			var partId = strsTemp[0];
	   			var partCode = strsTemp[1];
	   			var partOldcode = strsTemp[2];
	   			var partName = strsTemp[3];

	   			if(partIdArr.length > 0 && partIdArr.toString().indexOf(partId) > -1)
	   			{
	   				rpPartNames = rpPartNames + partName + " ";
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
	    			insert_row.insertCell(3);
	    			insert_row.insertCell(4);
	    			insert_row.insertCell(5);
	    			
	    			var cur_row = tab.rows[idx];
	    			cur_row.cells[0].innerHTML = idx;
	    			cur_row.cells[1].innerHTML = partCode +'<input type="hidden" name="partIds" value='+partId+'><input type="hidden" name="partCode" value='+partCode+'>' ;
	    			cur_row.cells[2].innerHTML = partOldcode+'<input type="hidden" name="partOldcode" value='+ partOldcode +'>';
	    			cur_row.cells[3].innerHTML = partName+'<input type="hidden" name="partName" value='+ partName +'>';
	    			cur_row.cells[4].innerHTML = '<input type="text" id=\"minPkg_'+partId+'\" name=\"minPkg_'+partId+'\" onchange="dataTypeCheck(this)" value="1" />';
	     			cur_row.cells[5].innerHTML = '<input type="button" class="normal_btn" value="删 除" onclick="deleteTblRow('+ idx +')"/>' ;
	   			}
    		}
    		if("" != rpPartNames)
    		{
        		MyAlert("配件：【" + rpPartNames + "】不能被重复添加!");
    		}
    	}

    	//数据验证
    	function dataTypeCheck(obj)
    	{
    		var value = obj.value;
    	    if (isNaN(value)) {
    	        MyAlert("请输入数字!");
    	        obj.value = "";
    	        return;
    	    }
    	    var re = /^[1-9]+[0-9]*]*$/;
    	    if (!re.test(obj.value)) {
    	        MyAlert("请输入正整数!");
    	        obj.value = "";
    	        return;
    	    }
    	}

    	function deleteTblRow(rowNum) {
    		var tbl = document.getElementById('add_tab');
    		tbl.deleteRow(rowNum);
    		var count = tbl.rows.length;
    		for (var i=rowNum;i<=count;i++)
    	       {
    	         tbl.rows[i].cells[0].innerText=i;
    	         tbl.rows[i].cells[5].innerHTML='<input type="button" class="normal_btn" value="删 除" onclick="deleteTblRow('+i+');"/>'
    	         if((i+1)%2==0){
    		    	tbl.rows[i].className   = "table_list_row1";
    			  }else{
    				  tbl.rows[i].className  = "table_list_row2";
    			  }
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
</head>
<body>
<form name='fm' id='fm'>
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 配件直发条件设置 &gt; 新增
        </div>
        <table class="table_edit">
          <tr>
	      <td width="10%" align="right"  >供应商：</td>
	      <td width="30%">
			<input class="long_txt" type="text" readonly="readonly" id="venderName"  name="venderName" />
            <input name="BUTTON" type="button" class="mini_btn" onclick="showVender()" value="..." />
            <input id="venderId" name="venderId" type="hidden" value=""/>
            <font color="red">*</font>
          </td>
          <td width="10%" align="right" >品牌分类：</td>
	      <td width="20%" ><input class="middle_txt" type="text" name="brand" id="brand" value="" /><font color="red"> *</font></td>
	      <td width="10%" align="right" >最小订货箱数：</td>
	      <td width="20%" >
	      <input class="middle_txt" type="text" name="criterion" id="criterion" value="" onchange="dataTypeCheck(this)"/>
	      <font color="red">*</font>
	      </td>
     	 </tr>
        </table>
        <table class="table_edit">
            <tr>
                <td align="center">
                    <input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="checkFormUpdate();" class="normal_btn"/>
                    <input type="button" name="backBtn" id="backBtn" value="返 回" onclick="reBack()" class="normal_btn"/>
                </td>
            </tr>
        </table>
        <table class="table_list" id="add_tab">
		<tr class="table_list_row1" style="background-color: #DAE0EE;">
	   		<td>序号</td>
	   		<td>件号</td>
	   		<td>配件编码</td>
			<td>配件名称</td>
			<td>最小包装数</td>
			<td><input type="button" value="选择配件" class="long_btn" onclick="showPart();"/></td>
		</tr>
		</table>
    </div>
</form>
</body>
</html>

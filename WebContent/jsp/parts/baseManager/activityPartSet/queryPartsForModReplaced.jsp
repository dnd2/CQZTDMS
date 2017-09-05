<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人员信息</title>
</head>
<script type="text/javascript" >

</script>
<body onload="__extQuery__(1);">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：基础信息管理 &gt; 配件基础信息维护 &gt; 活动配件明细设置 &gt; 配件选择</div>
  <form method="post" name = "fm" id="fm">
    <input type="hidden" id="desId" name="desId" value="${desId }"/>
    <input type="hidden" id="describe" name="describe" value="${describe }"/>
    <input type="hidden" id="checkSDate" name="checkSDate" value="${startDate }"/>
    <input type="hidden" id="checkEDate" name="checkEDate" value="${endDate }"/>
      <input type="hidden" id="actCode" name="actCode" value="${actCode }"/>
        <input type="hidden" id="partType" name="partType" value="${partType }"/>
<%--           <input type="hidden" id="checkEDate" name="checkEDate" value="${endDate }"/> --%>
    <table class="table_query" border="0" >
      <tr>            
        <td width="20%" class="table_query_2Col_label_4Letter">配件编码：</td>            
        <td width="30%">
			<input  class="middle_txt" id="partolcode"  name="partolcode" type="text" value="" datatype="1,is_null,20"/>
        </td>
        <td width="20%" class="table_query_2Col_label_4Letter">配件名称：</td>
        <td width="30%"><input type="text" name="partcname" id="partcname" datatype="1,is_null,30" class="middle_txt" value=""/></td>   
       </tr>
      <tr>
        <td colspan="4" align="center">
        	<input type="button" onclick="__extQuery__(1);" class="normal_btn"  value="查询" name="BtnQuery" id="queryBtn" />
        	<input type="button" value="确认" class="normal_btn" onclick="winClose();"/>
        	<input type="button" value="关闭" class="normal_btn" onclick="_hide();"/>
        </td>
      </tr>
    </table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />  
</form>

<script type="text/javascript">
	var flag = true ;

	var url = "<%=contextPath%>/parts/baseManager/partSTOSet/partSTOSetAction/queryReplacedPartsDialog.json";
				
	var title = null;

	var columns = [
	           {header:'序号',align:'center',renderer:getIndex},
	           {header:'<input type=\"checkbox\" id = \"selectedAll\" onclick=\"selectAll()\" />&nbsp;全选',dataIndex:'PART_ID',renderer:myCheckBox},
	           {header: "配件编码",sortable: false,dataIndex: 'PART_OLDCODE',align:'center'},
	           {header: "配件名称",sortable: false,dataIndex: 'PART_CNAME',align:'center'},
	           {header: "切换件编码",sortable: false,dataIndex: 'REPART_OLDCODE',align:'center'},
	           {header: "切换件名称",sortable: false,dataIndex: 'REPART_CNAME',align:'center'},
	           {header: "是否需要回运",sortable: false,align:'center',renderer:myRadio}
		   		]; 
		      
	function myCheckBox(value,meta,record){
		return String.format("<input type='checkbox' name='parts' value='"+record.data.PART_ID+"' />");
	}
	
	function myRadio(value,meta,record)
	{
		var partId = record.data.PART_ID;
		partId= "isneedFlag_"+partId;
// '<input name="isneedFlag_'+ partId +'" type="radio" value="10041001" />是'+'<input name="isneedFlag_'+ partId +'" type="radio" value="10041002" />否 '
		return String.format("<input name='"+partId+"' type='radio' value='10041001' />是"+"<input name='"+partId+"' type='radio' value='10041002' />否");
	}

	//全选
	function selectAll()
	{
		var selectAll = document.getElementById("selectedAll");
		var objArr = document.getElementsByName('parts');
		if(true == selectAll.checked)
		{
			for(var i=0;i<objArr.length;i++)
			{
				objArr[i].checked = true;
			}
		}
		else
		{
			for(var i=0;i<objArr.length;i++)
			{
				objArr[i].checked = false;
			}
		}
	}

	function setMainPartCode(v1){
		 //调用父页面方法
		var v1=v1;
		if(!v1) return;
		if(flag){
			for(var i=0;i<v1.length;i++){
				MyAlert("checked: " + v1);
				v[i].checked=true;
			}
			flag = false ;
		} else if(!flag){
			for(var i=0;i<v1.length;i++){
				MyAlert("unchecked: " + v1);
				v1[i].checked=false;
			}
			flag = true ;
		}
	}
	
	function winClose(){
		var codes = document.getElementsByName("parts");
		
		var parts = [];
		var k = 0;
		for(var i=0;i<codes.length;i++){
			if(codes[i].checked==true){
				parts[k] = codes[i].value;
				k++;
			} 
		}
// 		MyAlert(parts[0]);
// MyAlert(parts.length); 
		if(parts.length <= 0)
		{
			MyAlert('请至少选择一个新增的配件!');
			return false;
		}
// 		var PartIds = document.getElementsByName("parts");
//         if (null != PartIds && PartIds.length > 0) {
//             var idsSize = parts.length;
            for (var i = 0; i < parts.length; i++) {
            	var isneedFlag = document.getElementsByName("isneedFlag_"+parts[i]);
//             	MyAlert(isneedFlag[0].checked);
            	if(isneedFlag[0].checked==false&&isneedFlag[1].checked==false){
            	MyAlert('请选择是否需要回运!');
                 return false;
            	}
            }
//         }
		if(confirm("确认是否新增?"))
		{
// 			MyAlert(parts.length); 
			btnDisable();
			var url = '<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/insertActivityReplacedPartSetForMod.json?parts='+parts;
	  		makeFormCall(url,showResult,'fm');
		}
	}

// 	function checkForm()
// 	{
// 		var codes = document.getElementsByName("parts");
// 		var parts = [];
// 		var k = 0;
// 		for(var i=0;i<codes.length;i++){
// 			if(codes[i].checked==true){
// 				parts[k] = codes[i].value;
// 				k++;
// 			} 
// 		}
// 		btnDisable();
<%-- 		var url = '<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/insertActivityReplacedPartSetForMod.json?parts='+parts; --%>
//   		makeFormCall(url,showResult,'fm');
// 	}

	function showResult(json) {
		btnEnable();
	    if (json.errorExist != null && json.errorExist.length > 0) {
	    	MyAlert("配件：【" + json.errorExist + "】已添加，不能重复添加!");
	    } else if (json.success != null && json.success == "true") {
	    	MyAlert("新增成功!");
	    	parentContainer.__extQuery__(1);
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
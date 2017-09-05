<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件制造商维护维护</title>
<%
	String contextPath = request.getContextPath();
%>
<script language="javascript" type="text/javascript">
	function doInit(){
		__extQuery__(1);
	}
	function goBack(){
		window.location.href = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/queryPartMakerInit.do' ;
	}
</script>
</head>
<body>
	<div id="loader"
		style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
	<div class="navigation">
		<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 配件基础信息维护 &gt; 配件与制造商关系维护
	</div>
	<form name="fm" id="fm" method="post">
		<input type="hidden" name="makerId" id="makerId" value="${makerId}" />
		<table class="table_query">
			<tr>
				<%-- <td width="15%"   align="right">制造商编码：</td>
				<td width="15%"><input class="short_txt" type="text"
					value="${makerInfo.MAKER_CODE}" disabled="disabled" /></td> --%>
				<td width="15%"   align="right">制造商名称：</td>
				<td width="15%">${makerInfo.MAKER_NAME}</td>
				<td class="table_info_3col_label_4Letter"></td>
				<td width="15%"   align="right">配件编码：</td>
				<td width="15%"><input class="middle_txt" type="text"
					id="part_oldCode" name="part_oldCode" /></td>
				<td><input class="normal_btn" type="button" value="查 询"
					onClick="__extQuery__(1)" /><input type="button" value="选择配件"
					class="long_btn"
					onclick=" showTable();showPartInfo2('PART_CODE','PART_OLDCODE','PART_CNAME','PART_ID','true','true');" />
					<input type="button" name="saveBtn" id="saveBtn" value="确  认"
					onclick="checkFormUpdate();" class="normal_btn" /> <input
					type="button" class="normal_btn" value="返回" onclick="goBack();"></td>
			</tr>
		</table>
		<table class="table_list" id="add_tab" style="display:none ">
			<tr class="table_list_row1">
				<%--<td>序列</td>--%>
				<td>配件件号</td>
				<td>配件编码</td>
				<td>配件名称</td>
				<td>操作</td>
			</tr>
		</table>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!--分页 end -->
	</form>
	<script type="text/javascript">
   
		var myPage;

    	var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/queryPartMakerRelation.json';
    	
	
	    var title = null;

	    var columns = [
	            {header: "序号", width: '5%', renderer: getIndex},
				{header: "件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
				{header: "是否有效", dataIndex: 'STATE', align:'center',renderer:getItemValue},
				{id:'action',header: "操作",sortable: false,dataIndex: 'RELAION_ID', align:'center',renderer:myLink}
		      ];

	    //设置超链接
	     	//设置超链接
	function myLink(value,meta,record)
	{
		if(<%=Constant.STATUS_ENABLE%> == record.data.STATE){
			return String.format("<a href=\"#\" onclick='del(\""+value+"\")'>[删除]</a>");
	    } else {
	    	return String.format("<a href=\"#\" onclick='del(\""+value+"\")'>[删除]</a>");
		}
			
	}
	function del(value){
	     MyConfirm("确定要删除?",delAction,[value]);
    	}
		
	function delAction(value){
	     var url = '<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/upDatePartMakerRelation.json?REALATION_ID='+value+'&curPage='+myPage.page;
	     makeNomalFormCall(url,handleCel,'fm');
		}
	 
	function setPartCode(whIdsNames){
    var tab = $('add_tab');
    var strTemp;
    for (var i = 0; i < whIdsNames.length; i++) {
        strTemp = whIdsNames[i].toString();
        //定义一数组
        strsTemp = strTemp.split("||"); //字符分割     
        var Part_Id = strsTemp[0];
        var Part_code = strsTemp[1];
        var Part_oldcode = strsTemp[2];
        var Part_name = strsTemp[3];
        
        var idx = tab.rows.length;
        var insert_row = tab.insertRow(idx);
        if (idx % 2 == 0) 
            insert_row.className = 'table_list_row2';
        else 
            insert_row.className = 'table_list_row1';
        insert_row.insertCell(0);
        insert_row.insertCell(1);
        insert_row.insertCell(2);
        insert_row.insertCell(3);
        //insert_row.insertCell(4);
        var cur_row = tab.rows[idx];
        // cur_row.cells[0].innerHTML = idx;
        cur_row.cells[0].innerHTML = Part_code + '<input type="hidden" name="PartIds" value=' + Part_Id + '>';
        cur_row.cells[1].innerHTML = Part_oldcode + '<input type="hidden" name="Part_oldcodes" value=' + Part_oldcode + '>';
        cur_row.cells[2].innerHTML = Part_name + '<input type="hidden" name="Part_names" value=' + Part_name + '>';
        cur_row.cells[3].innerHTML = '<input type="button" class="normal_btn" value="删除" onclick="delRow(this);"/>';
         }
}

		function delRow(obj) {
			var tab = $('add_tab');
			var idx = obj.parentElement.parentElement.rowIndex;
			tab.deleteRow(idx);
		}
		     //表单提交前的验证：
        function checkFormUpdate() {
		    	 $("add_tab").clean;
    		var whIds = document.getElementsByName("PartIds");
    		if( null == whIds || whIds.length <= 0)
    		{
    			MyAlert('请至少选择一个配件!');
    			return false;
    		}
            MyConfirm("确认保存?", checkForm);
        }
           //表单提交方法：
        function checkForm() {
        	var PartIds = document.getElementsByName("PartIds");
      		var url ='<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/insertPartMakerRelation.json?PartIds='+PartIds;
            makeFormCall(url,showResult,'fm');
        }
        
		 function showResult() {
			 window.location.replace('<%=contextPath%>/parts/baseManager/partsBaseManager/PartMakerManager/addPartMakderRelationInit.do?makerId='+${makerId});	 
        }
	   function showTable(){
		   $("add_tab").style.display="";
	   }
		function handleCel(jsonObj) {
			  if(jsonObj!=null){
			     var success = jsonObj.success;
			     MyAlert(success);
			      __extQuery__(jsonObj.curPage);
			  }
		   }
        function showPartInfo2(inputCode,inputOldCode,inputName ,inputId ,isMulti,isFeedBack){
            if(!inputName){ inputName = null;}
            if(!inputId){ inputId = null;}
            OpenHtmlWindow(g_webAppName+"/dialog/PartSelect.jsp?INPUTCODE="+inputCode+"&INPUTOLDCODE="+inputOldCode+"&INPUTNAME="+inputName+"&INPUTID="+inputId+"&ISMULTI="+isMulti+"&ISFEEDBACK="+isFeedBack,730,390);
        }
	</script>
</body>
</html>
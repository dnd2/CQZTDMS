<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head.jsp" />
</head>
<body onunload='javascript:destoryPrototype()' >
	<form id='fm' name='fm'>
		<input type="hidden" id="orgId" name="orgId" value="" />
		<table class="table_query" >
			<tr>
				<td class="table_query_label" nowrap="nowrap">经销商代码：</td>
				<td class="table_query_input"   nowrap="nowrap">
					<input id="dlrCode" name="dlrCode" class="short_txt" type="text" />
				</td>
				<td class="table_query_label" nowrap="nowrap" >经销商名称：</td>
				<td class="table_query_input" nowrap="nowrap">
					<input id="dlrName" name="dlrName" class="middle_txt" type="text" />
				</td>
			</tr>
			<tr>
				<td class="table_query_label" nowrap="nowrap">
					<input class="normal_btn" id="queryBtn" type="button" value="查 询" onclick="__extQuery__(1);"/>
				</td>
				<td class="table_query_label" nowrap="nowrap">
					<input class="normal_btn" type="reset" value="重 置" />
				</td>
				<td colspan="2">&nbsp;</td>
			</tr>
		</table>

	<br/>
	<div id="_page"></div>
	<div id="myGrid"></div>
	<div id="myPage" class="pages"></div>
		
<script>
	var myPage; //定义公共的showPages对象
	var gridId = "myGrid";
	var url = "<%=contextPath%>/common/OrgMng/queryDlr.json";
	//设置列字段
	var fields =  [
					{name: 'dlrId', type: 'string'},
					{name: 'dlrCode', type: 'string'},
					{name: 'dlrName', type: 'string'}
				  ];
	var store = new Ext.data.JsonStore({ //设置显示箭头图标
		fields: fields,
		sortInfo: {
		 	field: 'dlrCode',
	 		direction: 'ASC' // or 'DESC' (case sensitive for local sorting)
		}
	});
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
					{header: "选择", width:50, sortable: true, dataIndex: 'dlrId',renderer:function(value){return "<input type='checkbox' name='dlrId' value=" + value +" />"}},
					{header: "经销商代码", width:80, sortable: true, dataIndex: 'dlrCode'},
					{header: "经销商名称", width:80, sortable: true, dataIndex: 'dlrName',renderer:function(value){return "<input type='text' style='border:0' name='dlr_Name' value=" + value +" />"}}
			      ];
			      
	function __extQuery__(page){
		disableBtn($("queryBtn"));
		makeFormCall(url+"?curPage="+page,callBack,'fm');
	}
	//Ajax返回调用函数 设置字段、列名属性参数
	function callBack(json){
		//设置对应数据
		var ps = json.dlr;
		//生成数据集
		if(ps.records != null){
			$("_page").innerHTML = "";
			removeGird(gridId);
			var grid = new Ext.grid.GridPanel({							  
					title: title,
					store: store,
					columns: columns,
					autoHeight:true,
					forceFit: true,
					enableColumnHide:false,
					enableHdMenu:false
				});
			grid.render(gridId);
			store.loadData(ps.records);
			Ext.fly(gridId).clip();
			
			$(gridId).show();
			//分页
			myPage = new showPages("myPage",ps,url);
			myPage.printHtml();
			$("multiTb").style.display="inline";
		}else{
			$(gridId).hide();
			document.getElementById("multiTb").style.display="none";
			$("_page").innerHTML = "<div class='pageTips'>没有满足条件的数据</div>";
			$("myPage").innerHTML = "";
		}
		useableBtn($("queryBtn"));
	}
	    
	function setDlr(obj){
		flag = false;
		var dlrIdList = "";
		var dlrNameList = "";
		var count = document.getElementsByName("dlrId").length;
		if(count == 1){
			if(obj.dlrId.checked){
				flag = true;
			}
		}
		if(count > 1){
			for(i = 0;i < count;i ++){
				if(obj.dlrId[i].checked){
					flag = true;
				}
			}
		}
		if(!flag){
			MyAlert("请选择经销商!");
			return false;
		}
		if(count == 1){
			parent.parentDocument.getElementById('dlrId').value = obj.dlrId.value;
			parent.parentDocument.getElementById('dlrName').value = obj.dlr_Name.value;
			parent.parent.removeDiv();
		}else{
			for(j = 0;j < count;j ++){
				if(obj.dlrId[j].checked){
					dlrIdList += obj.dlrId[j].value; 
					dlrNameList += obj.dlr_Name[j].value;
					dlrIdList += ","; 
					dlrNameList += ",";
				}
			}
			parent.parentDocument.getElementById('dlrId').value = dlrIdList.substring(0,dlrIdList.length-1);
			parent.parentDocument.getElementById('dlrName').value = dlrNameList.substring(0,dlrNameList.length-1);
			parent.parent.removeDiv();
		}
	}
	
</script>
		<table id="multiTb" class="table_list" style="border-bottom:0px solid #DAE0EE;display: none">
			<tr bgcolor="#FFFFFF">			
				<td colspan='3'><input class='normal_btn' type='button' value='确定' onclick='setDlr(this.form)' /></td>
			</tr>
		</table>
	</form>
</body>
</html>

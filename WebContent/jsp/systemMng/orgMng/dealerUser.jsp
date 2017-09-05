<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head.jsp" />
<title>经销商用户查询</title>
<style>
	.img{ border:none}
</style>
</head>

<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
	<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 组织管理 &gt; 经销商仓库维护
	</div>
	<form id="fm">
		<table class="table_query" border="0">
			<tr><input type="hidden" name="fun" id="fun" value="" />
				<td nowrap="nowrap" align="center">用户名称：
					<input class="middle_txt"  type="text" name="USER_NAME"/></td>
				<td align="left" width="350">
					<input id="queryBtn" class="normal_btn" type="button" value="查 询" onclick="__extQuery__(1)"/>&nbsp;&nbsp;
				</td>	
			</tr>
		</table>

	<div id="_page"></div>
	<div id="myGrid"></div>
	<div id="myPage" class="pages"></div>	
	<div style="clear:both"></div>
	<table class="table_info_button">
			<tr >
				<td>
					 <input class="normal_btn" type="button" value="确  定"  onclick="javascript:goParent();"/>
					<input class="normal_btn" type="button" value="取 消"  onclick="javascript:cel();"/>
				</td>
			</tr>
	</table>
	</form>	
</div>
<script type="text/javascript" >
	var __init__ = true;
	var myPage; //定义公共的showPages对象
	var gridId = "myGrid";
	var url = "<%=contextPath%>/common/UserManager/UserSearch.json?command=1";
	//设置列字段
	var fields =  [
					{name: 'userId', type: 'string'},
					{name: 'name', type: 'string'},
					{name: 'handPhone', type: 'string'},
				  ];
	var store = new Ext.data.JsonStore({ //设置显示箭头图标
		fields: fields,
		sortInfo: {
		 	field: 'userId',
	 		direction: 'ASC' // or 'DESC' (case sensitive for local sorting)
		}
	});
	//设置表格标题
	var title= null;

	var columns = [
					{header: "选择", sortable: true, dataIndex: 'userId',width:35,renderer:generateUserCheckBox},
					new Ext.grid.RowNumberer({header: "序号", width:35}),
					{header: "用户名称", sortable: true, dataIndex: 'name'},
					{header: "联系电话", sortable: true, dataIndex: 'handPhone'}
				  ];
			      
	function __extQuery__(page){
		disableBtn($("queryBtn"));
		makeFormCall(url+"&curPage="+page,callBack,'fm');
	}
	//Ajax返回调用函数 设置字段、列名属性参数
	function callBack(json){
		//设置对应数据
		var ps = json.ps;
		//生成数据集
		if(ps.records != null){
			$("_page").innerHTML = "";
			if(!__init__) removeGird(gridId);

			var grid = new Ext.grid.GridPanel({							  
					title: title,
					store: store,
					columns: columns,
					autoHeight:true,
					forceFit: true,
					enableColumnHide:false,
					enableHdMenu:false,
					enableColumnResize:true
				});
			grid.render(gridId);
			store.loadData(ps.records);
			
			Ext.fly(gridId).clip();
			
			
			$(gridId).show();
			//分页
			
			myPage = new showPages("myPage",ps,url);
			myPage.printHtml();
		}else{
			$(gridId).hide();
			$("_page").innerHTML = "<div class='pageTips'>没有符合条件的记录</div>";
			$("myPage").innerHTML = "";
		}
		useableBtn($("queryBtn"));
		__init__ = false;
	}
	function goParent(){
		var selectValue = "";
		var userId = document.getElementsByName("selectCheck");
		var userName = document.getElementsByName("selectNameCheck");
		var selectName = "";
		for(var i=0;i<userId.length;i++){
			if(userId[i].checked){
				selectValue += userId[i].value;
				selectValue +=",";				
				selectName += userName[i].value; 
				selectName +=",";
			}
		}
		if(selectValue==null||selectValue==""||selectValue=="null"){
			MyAlert("请选择用户");
			return;
		}
		var fun = $F("fun");
		if(fun!=""){
			parentContainer.eval(fun+"('"+selectValue+"','"+selectName+"')");
		}else{
			parent.$('user_id').value = selectValue.substring(0,selectValue.length-1);
			parent.$('wh_user_name').value = selectName.substring(0,selectName.length-1);
		}
		removeDiv();
	}
	function cel(){
		removeDiv();
	}
</script>
</body>
</html>

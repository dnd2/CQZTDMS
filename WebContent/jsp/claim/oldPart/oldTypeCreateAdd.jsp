<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<head>
<% 
   String contextPath = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<title>旧件新增</title>
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;旧件新增</div>
 <form method="post" name ="fm" id="fm">
       <table id="itemTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="17" >
					<h3>添加页面</h3>
				</th>
				</tr>
				<tr>
					<td width="25%">
						选择类型
					</td>
					<td width="25%">
					<select name="selectType" id="selectType" class="short_sel">
							<option value="1">库区</option>
							<option value="2">货架</option>
							<option value="3">层数</option>
						</select>
					</td>
					<td width="25%">
					</td>
					<td width="25%">
					</td>
				</tr>
				 

				<tbody id="transportTable">
				</tbody>
			</table>
     <table class="table_list">
      <tr > 
       <td height="12" align="center">
       	  <input type="button"  id="commit_btn" class="normal_btn" style="width=8%"  value="提交"/>
          <input type="button"  onclick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
       </td>
      </tr>
    </table>  
</form>
<br />
<script type="text/javascript">
	var temp=0;
	var type=100;
//动态生成表格
	function addRow(tableId){
    var addTable = document.getElementById(tableId);
	var rows = addTable.rows;
	var length = rows.length;
	var insertRow = addTable.insertRow(length);
	insertRow.className = "table_list_row1";
	insertRow.insertCell(0);
	insertRow.insertCell(1);
	insertRow.insertCell(2);
	insertRow.insertCell(3);
	if (tableId=='transportTable') {
		
		addTable.rows[length].cells[0].innerHTML = '<td><input id='+temp+' type="text" readonly="readonly" class="middle_txt" name="" /><input id='+type+' type="hidden" name="nameType" /></td>';
		addTable.rows[length].cells[1].innerHTML =  '<td><input type="text" class="middle_txt" name="codeOld" onkeyup="keyup(this);" maxlength="20"/></td>';
		addTable.rows[length].cells[2].innerHTML =  '<td><input type="text" class="middle_txt" name="nameOld" maxlength="20" datatype="0,is_digit_letter_cn,10" /></td>';
		addTable.rows[length].cells[3].innerHTML =  '<td><input type="button" class="normal_btn" value="删除" onclick="deleteRow(this);" /></td>';
	}
		var text=$("#selectType").find("option:selected").text();
		$("#"+temp).val(text);
		temp++;
		var val=$("#selectType").find("option:selected").val();
		$("#"+type).val(val);
		type++;
		return addTable.rows[length];
	}
	function deleteRow(obj){
		$(obj).parent().parent().remove(); 
	}
	
	$("#commit_btn").live("click",function(){
		if(temp==0){
			MyAlert("提示：请先新增！");
		}else if(temp>0){
			var codeOld= $("input[name='codeOld']").val();
			var blank=0;
			if(true){
				$("input[name='codeOld']").each(function(){
		        if($.trim($(this).val())==""){
		        	MyAlert("提示：请填写代码！");
		        	blank++;
		        	return;
		        }
				});
				$("input[name='nameOld']").each(function(){
				        if($.trim($(this).val())==""){
				        	MyAlert("提示：请填写名称！");
				        	blank++;
				        	return;
				        }
				});
				 
			}
			if(blank==0){
				var url='<%=contextPath%>/claim/oldPart/ClaimOldPartTransportManager/checkCode.json?codeOld='+codeOld;
				makeCall(url ,afterCallThen,null) ;
			}
		}
	});
	function afterCallThen(json){
		if(""!=json.kpnum){
			MyAlert(json.kpnum);
		}else{
			fm.action='<%=contextPath%>/claim/oldPart/ClaimOldPartTransportManager/oldTypeaddCommit.do';
			fm.method="post";
			fm.submit();
		}
	}
	function keyup(obj){
		var code= obj.value;
		var tr = this.getRowObj(obj.parentNode);
		var type=tr.childNodes[0].childNodes[0].value;
		var reg1=/^[A-Z]$/;
		var reg2=/^[0-9]\d{0,2}$/;
		var reg3=/^\d{1}$/;
		var temp=0;
		$("input[name='codeOld']").each(function(){
		        if(code==$.trim($(this).val())){
			        temp++;
		        }
		});
		
		if(temp>1){
			MyAlert("提示：代码填写不能重复！");
			$(obj).val("");
			return;
		}
		if(""!=code){
			if(type=="库区" && !reg1.test(code)&&code.length!=1){
				MyAlert("提示：填写不匹配库区类型，请重新输入一位大写字母！");
				$(obj).attr("value","");
			}		
			if(type=="货架" && !reg2.test(code)&&code.length!=3){
				MyAlert("提示：填写不匹配货架类型，请重新输入3位数字！");
				$(obj).attr("value","");
			}		
			if(type=="层数" && !reg3.test(code)&&code.length!=1){
				MyAlert("提示：填写不匹配层数类型，请重新输入一位正整数！");
				$(obj).attr("value","");
			}		
		}else{
			MyAlert("提示：代码不能为空!");
			return false;
		}
	};
	//得到行对象
		function getRowObj(obj)
		{
		   var i = 0;
		   while(obj.tagName.toLowerCase() != "tr"){
		    obj = obj.parentNode;
		    if(obj.tagName.toLowerCase() == "table")
		  return null;
		   }
		   return obj;
		};
</script>
</body>
</html>

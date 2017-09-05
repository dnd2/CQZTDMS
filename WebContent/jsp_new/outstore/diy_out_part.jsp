<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@page import="java.util.*"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔旧件退赔单</title>
<% String contextPath = request.getContextPath();%>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript">
	function add(){
		var addOutPart=$("#addOutPart");
		var str="";
		str+="<tr>";
        str+="<td nowrap='true' ><input name='part_code' type='text' size='10' readonly='readonly' onclick='showPart(this);' value=''/></td>";
        str+="<td nowrap='true' ><input name='part_name' type='text' size='10' readonly='readonly'  value=''/></td>";
        str+="<td nowrap='true' ><input name='unit' type='text' size='3'   value=''/></td>";
        str+="<td nowrap='true' ><input name='fill_num' type='text' size='3'  onblur='insertNum(this);' value=''/></td>";
        str+="<td nowrap='true' ><input name='labour_amount' type='text' size='10'  onblur='changePrice(this);' value=''/></td>";
        str+="<td nowrap='true' ><input name='relation_amount' type='text' size='10' onblur='changePrice(this);' value=''/></td>";
        str+="<td nowrap='true' ><input name='out_amount' type='text' size='10'  onblur='changePrice(this);'  value=''/></td>";
        str+="<td nowrap='true' ><input name='part_amount' type='text' size='10'  value='' onblur='changePrice(this);' /></td>";
        str+="<td nowrap='true' ><input name='amount' type='text' size='10' readonly='readonly'  value=''/></td>";
        str+="<td nowrap='true' ><input name='print_mun' type='text' size='3' onblur='changePrice(this);'  value=''/></td>";
        str+="<td nowrap='true' ><input name='remark' type='text' size='15'  value=''/></td>";
        str+="<td nowrap='true' ><input name='part_price' readonly='readonly' type='text' size='3'  value=''/></td>";
        str+="<td nowrap='true' ><input type='button' value='删除' onclick='deleteTr(this);' class='normal_btn' /></td>";
        str+="</tr>";
		addOutPart.append(str);
	}
	function insertNum(obj){
		var tr=$(obj).parent().parent();
		var partcode=tr.children("td:eq(0)").find('input').val();
		if(""==partcode){
			MyAlert("提示：先点击选择零部件代码！");
			tr.children("td:eq(0)").find('input').val("");
			tr.children("td:eq(2)").find('input').val("");
			tr.children("td:eq(3)").find('input').val("");
			tr.children("td:eq(4)").find('input').val("");
			tr.children("td:eq(5)").find('input').val("");
			tr.children("td:eq(7)").find('input').val("");
			tr.children("td:eq(8)").find('input').val("");
			tr.children("td:eq(9)").find('input').val("");
			tr.children("td:eq(6)").find('input').val("");
			tr.children("td:eq(1)").find('input').val("");
			return;
		}
		var reg = /^\d+$/;
		var val=$(obj).val();
		if(""!=val && !reg.test(val)){
			MyAlert("提示：请输入正整数填入新件数量！");
			$(obj).val("1"); 
		}else{
			var tr=$(obj).parent().parent();
			tr.children("td:eq(9)").find('input').val(val);
			var price=tr.children("td:eq(11)").find('input').val();
			var price1=tr.children("td:eq(4)").find('input').val();
			var price2=tr.children("td:eq(5)").find('input').val();
			var price3=tr.children("td:eq(6)").find('input').val();
			var smallPrice=((price*val*1.5*1000/1000)+parseFloat(price1)+parseFloat(price2)+parseFloat(price3)).toFixed(2);
			tr.children("td:eq(8)").find('input').val(smallPrice);
		}
	}
	function changePrice(obj){
		var val=$(obj).val();
		var tr=$(obj).parent().parent();
		var partcode=tr.children("td:eq(0)").find('input').val();
		if(""==partcode){
			MyAlert("提示：先点击选择零部件代码！");
			tr.children("td:eq(0)").find('input').val("");
			tr.children("td:eq(2)").find('input').val("");
			tr.children("td:eq(3)").find('input').val("");
			tr.children("td:eq(4)").find('input').val("");
			tr.children("td:eq(5)").find('input').val("");
			tr.children("td:eq(7)").find('input').val("");
			tr.children("td:eq(8)").find('input').val("");
			tr.children("td:eq(9)").find('input').val("");
			tr.children("td:eq(6)").find('input').val("");
			tr.children("td:eq(1)").find('input').val("");
			return;
		}
		var reg = /^[0-9]+([.]{1}[0-9]{1,2})?$/;
		if(""!=val && !reg.test(val)){
			MyAlert("提示：请输入正确的数据！");
			$(obj).val("0");
			return;
		}else{
			var tr=$(obj).parent().parent();
			var price=tr.children("td:eq(11)").find('input').val();
			var price1=tr.children("td:eq(4)").find('input').val();
			var price2=tr.children("td:eq(5)").find('input').val();
			var price3=tr.children("td:eq(6)").find('input').val();
			var price4=tr.children("td:eq(7)").find('input').val();
			var smallPrice=(parseFloat(price1)+parseFloat(price2)+parseFloat(price3)+parseFloat(price4)).toFixed(2);
			tr.children("td:eq(8)").find('input').val(smallPrice);
		}
	}
	var object;
	function showPart(obj){
		object=obj;
		OpenHtmlWindow('<%=contextPath%>/MainTainAction/addPart.do',800,500);
	}
	function setMainPartCode(part_id,part_code,part_name,claim_price_param){
		var tr=$(object).parent().parent();
		var price=(claim_price_param*1.5*1000/1000).toFixed(2);
		tr.children("td:eq(0)").find('input').val(part_code);
		tr.children("td:eq(2)").find('input').val("件");
		tr.children("td:eq(3)").find('input').val("1");
		tr.children("td:eq(4)").find('input').val("0");
		tr.children("td:eq(5)").find('input').val("0");
		tr.children("td:eq(7)").find('input').val(price);
		tr.children("td:eq(8)").find('input').val(price);
		tr.children("td:eq(9)").find('input').val("1");
		tr.children("td:eq(11)").find('input').val(claim_price_param);
		tr.children("td:eq(6)").find('input').val("0");
		tr.children("td:eq(1)").find('input').val(part_name);
	}
	function deleteTr(obj){
		$(obj).parent().parent().remove(); 
	}
	function showSupply(){
		OpenHtmlWindow('<%=contextPath%>/jsp_new/outstore/showSupply.jsp',800,500);
	}
	function setSupplier(supplierCode,supplierName){
		$("#supplierCode").val(supplierCode);
		$("#supplierName").val(supplierName);
	}
	function sureInsert(){
		var temp=0;
		var supplierCode=$("#supplierCode").val();
		if(""==$.trim(supplierCode)){
			MyAlert("提示：请先输入供应商代码再提交！");
			temp++;
			return;
		}
		var part_code=$("input[name='part_code']");
		if(part_code.length==0 ){
			MyAlert("提示：请至少选择一个配件再添加！");
			temp++;
			return;
		}
		$("input[name='part_code']").each(function(){
			var val= $(this).val();
			if(""==val){
				MyAlert("提示：请选择配件！");
				temp++;
				return;
			}
		});
		
		if(temp==0){
				MyConfirm("是否确认保存？",sureInsertCommit,"");
			}
		}
		function sureInsertCommit(identify){
			var url="<%=contextPath%>/OutStoreAction/diyOutPartSure.json";
			makeNomalFormCall1(url,sureInsertCommitBack,"fm");
		}
		function sureInsertCommitBack(json){
			var str="保存";
			if(json.succ=="1"){
				MyAlert("提示："+str+"成功！");
				var url='<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/queryListPage.do';
				window.location.href=url;
			}else{
				MyAlert("提示："+str+"失败！");
			}
		}
</script>
</head>
<body >
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;索赔旧件管理&gt;手工退赔单新增</div>
 <form method="post" name ="fm" id="fm">
 <table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" ><span style="color: red">*</span>退赔单号：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="ro_no"  value="添加后自动新增" readonly="readonly" name="ro_no" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" ><span style="color: red">*</span>供应商代码：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="supplierCode" value="" readonly="readonly" name="supplierCode" onclick="showSupply();" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" ><span style="color: red">*</span>供应商名称：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="supplierName" value="" readonly="readonly" name="supplierName" type="text" maxlength="30" />
    	</td>
    	<td width="12.5%"><input type="button"  name="bntAdd"  id="bntAdd"  value="添加" onclick="add();" class="normal_btn" /></td>
	</tr>
</table>
<br />
 <table id="addOutPart" border="1" cellpadding="1" cellspacing="1" class="table_query" width="120%" style="text-align: center;">
 	<th colspan="13">
		<img class="nav" src="../jsp_new/img/subNav.gif"/>录入信息
	</th>
 	<tr>
            <td nowrap='true' width="10%">零部件代码</td>
            <td nowrap='true' width="10%">零部件名称</td>
            <td nowrap='true' width="7%">单位</td>
            <td nowrap='true' width="7%">数量</td>
            <td nowrap='true' width="10%">工时金额</td>
            <td nowrap='true' width="10%">关联损失</td>
            <td nowrap='true' width="10%">外出费用</td>
            <td nowrap='true' width="10%">材料合计</td>
            <td nowrap='true' width="10%">小计</td>
            <td nowrap='true' width="7%">打印数量</td>
            <td nowrap='true' width="19%">备注</td>
            <td nowrap='true' width="7%">单价</td>
            <td nowrap='true' width="7%">操作</td>
       </tr>
</table>
<br />
     <table class="table_list">
      <tr > 
       <td height="12" align="center">
        <input type="button" id="save_btn" onclick="sureInsert();" class="normal_btn" style="width=8%" value="保存"/>
          <input type="button"  onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/>
       </td>
      </tr>
    </table>  
  <div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<br />
</body>
</html>

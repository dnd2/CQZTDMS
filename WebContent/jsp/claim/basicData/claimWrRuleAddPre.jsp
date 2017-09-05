<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TmPtPartBasePO"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>三包预警规则新增</title>
<script type="text/javascript">
//日历控件初始化
	function doInit()
		{
		   loadcalendar();
		}
//新增三包规则 开始
	function checkedAdd(){
		//表单校验
		var reg = /^[0-9]*[1-9][0-9]*$/;
		var vrType = document.getElementById("vrType").value;
		var level = document.getElementById("vrLevel").value;
		var lawStandard = document.getElementById("lawStandard").value;
		var law1 = document.getElementById("vrLaw1").value;
		var warranty1 = document.getElementById("vrWarranty1").value;
		var law2 = document.getElementById("vrLaw2").value;
		var warranty2 = document.getElementById("vrWarranty2").value;
		if(vrType == null || vrType == ""){
			MyAlert("请选择预警类型！");
			return false;
		}
		if(level ==null || level==""){
			MyAlert("请选择预警等级！");
			return false;
		}
		if(lawStandard==null || lawStandard==""){
			MyAlert("请填写法规！");
			return false;
		}
		if(vrType == '<%=Constant.VR_TYPE_1%>'){
			if(law1==null || law1==""){
				MyAlert("请填写法定天数！");
				return false;
			}else{
				if(!reg.test(law1)){
					MyAlert("法定天数必须为正整数！");
					return false;
				}
			}
			if(warranty1==null || warranty1==""){
				MyAlert("请填写预警天数！");
				return false;
			}else{
				if(!reg.test(warranty1)){
					MyAlert("预警天数必须为正整数！");
					return false;
				}
			}
			if(law1 < warranty1){
				MyAlert("法定天数不能小于预警次数！");
				return false;
			}
		}else if(vrType == '<%=Constant.VR_TYPE_2%>'){
			if(law2==null || law2==""){
				MyAlert("请填写法定次数！");
				return false;
			}else{
				if(!reg.test(law2)){
					MyAlert("法定次数必须为正整数！");
					return false;
				}
			}
			if(warranty2==null || warranty2==""){
				MyAlert("请填写预警次数！");
				return false;
			}else{
				if(!reg.test(warranty2)){
					MyAlert("预警次数必须为正整数！");
					return false;
				}
			}
			if(law2 < warranty2){
				MyAlert("法定次数不能小于预警次数！");
				return false;
			}
		}
		
		
		if(vrType == '<%=Constant.VR_TYPE_2%>'){
			var wrType = document.getElementById("partWrType").value;
			if(wrType == null || wrType==""){
				MyAlert("请选择配件类型！");
				return false;
			}else if(wrType == '<%=Constant.PART_WR_TYPE_2%>'){
				var part = document.getElementById("part").value;
				if(part==null || part==""){
					MyAlert("请选择配件！");
					return false;
				}
			}else if(wrType == '<%=Constant.PART_WR_TYPE_3%>'){
				var pos = document.getElementById("pos").value;
				if(!(law2 == '1' || law2 == '2' ))
				{
					MyAlert("关注件法定次数只能为 1 或者 2！");
					return false;
				}
				
				if(pos==null || pos==""){
					MyAlert("请选择关注部位！");
					return false;
				}
			}
		}
		//检验是否存在
		makeFormCall('<%=contextPath%>/claim/basicData/ClaimWrRule/checkUnique.json?sta=1',checkUnique,'fm');	
	}
	
	function checkUnique(json){
		if(json.success != null && json.success == "false"){
			MyAlert("三包预警规则已存在，请重新填写！");
			return false;
		}else if(json.success != null && json.success == "true"){
			MyConfirm("是否确认添加？",claimWrRuleAdd);
		}else{
			MyAlert("预警时间（次数）不符合规则，请重新填写！");
		}
	}

	function claimWrRuleAdd(){
		disableBtn($("commitBtn"));
		makeFormCall( '<%=contextPath%>/claim/basicData/ClaimWrRule/claimWrRuleAdd.json',showResult,'fm');
	}

	function showResult(json){
		if(json.success != null && json.success == "true"){
			MyAlert("添加成功！");
			window.location.href = "<%=contextPath%>/claim/basicData/ClaimWrRule/claimWrRuleInit.do";
		}else{
			MyAlert("添加失败，请联系管理员！");
		}
	}
//新增三包规则 结束
//返回主查询页面 开始
function goBack(){
		fm.action = "<%=contextPath%>/claim/basicData/ClaimWrRule/claimWrRuleInit.do";
		fm.submit();
}

function getpartName(obj)
{
  if(obj == '')
  {
     document.getElementById('partName').value = '';
  }else
  {
    document.getElementById('partName').value = document.getElementById(obj).title;
  }
  
}

//返回主查询页面 结束

 function doCusChange(){
	 var vrtype = document.getElementById("vrType").value;
	  	if(vrtype == '<%=Constant.VR_TYPE_1 %>'){
	  		document.getElementById('trid').disabled = true;
	  		document.getElementById('day1').style.display = '';
	  		document.getElementById('day2').style.display = 'none';
	  	}else if(vrtype == '<%=Constant.VR_TYPE_2 %>'){
		  	var partWrType = document.getElementById("partWrType").value;
		  	document.getElementById('trid').disabled = false;
		  	document.getElementById('day2').style.display = '';
	  		document.getElementById('day1').style.display = 'none';
	  	  	if(partWrType == '<%=Constant.PART_WR_TYPE_1%>'){
	  	  	  	document.getElementById("part").disabled = true;
	  	  		document.getElementById("pos").disabled = true;
	  	  	}else if(partWrType == '<%=Constant.PART_WR_TYPE_2%>'){
	  	  		document.getElementById("pos").disabled = true;
	  	  		document.getElementById("part").disabled = false;
	  	  	}else if(partWrType == '<%=Constant.PART_WR_TYPE_3%>'){
	  	  		document.getElementById("part").disabled = true;
	  	  		document.getElementById("pos").disabled = false;
	 	  	}
		}
  }
</script>
</head>

<body >
  <div class="navigation">
        <img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;三包预警规则维护
  </div>
  <form method="post" name="fm" id="fm">
     <table class="table_edit" >
          <tr>
            <td class="table_edit_2Col_label_7Letter">预警类型：</td>
            <td align="left">
                <script type="text/javascript" onchange="doCusChange();">
	   					    genSelBoxExp("vrType",<%=Constant.VR_TYPE%>,"",true,"short_sel","","true",'');
	  		      </script>
            </td>
            <td class="table_edit_2Col_label_7Letter">预警等级：</td>
            <td align="left">
                <script type="text/javascript">
	   					    genSelBoxExp("vrLevel",<%=Constant.VR_LEVEL%>,"",true,"short_sel","","true",'');
	  		      </script>
            </td>
            <td>
            </td>
             <td>
            </td>
           </tr>
           <tr id="trid" style="display:;">
            <td class="table_edit_2Col_label_7Letter">配件三包类型：</td>
            <td align="left">
            	<select id="partWrType" name="partWrType" class="short_sel" onchange="doCusChange();">
						<option value=''>-请选择-</option>
						<c:forEach var="typeList" items="${partWrTypeList}">
								<option value="${typeList.codeId}" title="${typeList.codeId}" >${typeList.codeDesc}</option> 
						</c:forEach>
					</select>
            	<font color="red">*</font>
            </td>
            <td class="table_edit_2Col_label_7Letter">配件名称：</td>
            <td align="left">
	             <select id="part" name="part" onchange="getpartName(this.value);" class="short_sel" >
						<option value=''>-请选择-</option>
						<c:forEach var="tmPtPartBasePO" items="${partList}">
								<option value="${tmPtPartBasePO.partCode}" id="${tmPtPartBasePO.partCode}" title="${tmPtPartBasePO.partName}" >${tmPtPartBasePO.partCode}</option> 
						</c:forEach>
					</select>
					<input type="text" disabled="disabled" id="partName" > 
					<font color="red">*</font>
            </td>
            <td class="table_edit_2Col_label_7Letter">关注部位：</td>
            <td align="left">
	             <select id="pos" name="position" class="short_sel" >
						<option value=''>-请选择-</option>
						<c:forEach var="ttAsWrMalfunctionPosition" items="${posList}">
								<option value="${ttAsWrMalfunctionPosition.posCode}" title="${ttAsWrMalfunctionPosition.posName}" >${ttAsWrMalfunctionPosition.posName}</option> 
						</c:forEach>
					</select><font color="red">*</font>
            </td>
          </tr>  
         
          <tr id="day1" style="display:;">
            <td class="table_edit_2Col_label_7Letter">法定天数：</td>
            <td align="left">
                <input type="text"  name="vrLaw1"  id="vrLaw1" class="little_txt" />天<font color="red">*</font>
            </td>
           <td class="table_edit_2Col_label_7Letter">预警天数：</td>
            <td align="left">
                <input type="text"  name="vrWarranty1"  id="vrWarranty1" class="little_txt" />天<font color="red">*</font>
            </td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
           </tr>
           <tr id="day2" style="display:none;">
            <td class="table_edit_2Col_label_7Letter">法定：</td>
            <td align="left">
                <input type="text"  name="vrLaw2"  id="vrLaw2" class="little_txt" />次<font color="red">*</font>
            </td>
           <td class="table_edit_2Col_label_7Letter">预警：</td>
            <td align="left">
                <input type="text"  name="vrWarranty2"  id="vrWarranty2" class="little_txt" />次<font color="red">*</font>
            </td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
           </tr>
           
           <tr>
            <td class="table_edit_2Col_label_7Letter">法规：</td>
            <td align="left" colspan="5" >
                <textarea  name="lawStandard"  id="lawStandard" style="width:60%;height: 100%;"></textarea><font color="red">*</font>
            </td>
            </tr>
          <tr>
            <td colspan="7" align="center">
		      <input id="commitBtn" type="button" name="bt_add" class="normal_btn" onclick="checkedAdd();" value="确定"/>
		      <input type="button" name="bt_back" class="normal_btn" onclick="history.back();" value="返回"/>
		      </td>
		  </tr>
  </table>
  </form>
</body>
</html>
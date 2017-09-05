<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
	
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件工时关系维护</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<form name='fm' id='fm' method="post">
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;二次索赔工时维护</div>
  <table class="table_query">
   <tr>            
        <td >配件代码：${bean.partCode }
			<input  class="middle_txt" id="PART_CODE" value="${bean.partCode }" name="PART_CODE" type="hidden" />
        </td>
    </tr>
     <tr>
        <td >配件名称：${bean.partName }
        	<input type="hidden" name="PART_NAME" id="PART_NAME"  class="middle_txt" value="${bean.partName }"/>
        </td>
     </tr>
     <tr>            
        <td >工时代码：
			<input readonly="readonly" class="middle_txt" id="LABOUR_CODE" value="${bean.labourCode }" name="LABOUR_CODE" type="text" /><span style="color: RED">*</span>
         	<input  class=normal_btn onclick=showLbour(); align="right" value=请选择 type=button name=button/> 
        </td>
     </tr>
     <tr>
        <td >工时名称：
        	<input readonly="readonly" type="text" name="LABOUR_NAME" id="LABOUR_NAME"  class="middle_txt" value="${bean.labourName }"/><span style="color: RED">*</span>
        </td>
     </tr>
     <tr>            
        <td >工时系数：
			<input readonly="readonly"  class="middle_txt" id="LABOUR_HOURS" value="${bean.labourHours }" name="LABOUR_HOURS" type="text" /><span style="color: RED">*</span>
        </td>
     </tr>
     <tr style="display: none">
        <td >工时单价：
        	<input type="text" name="LABOUR_PRICE" id="LABOUR_PRICE"  class="middle_txt" value="${bean.labourPrice }"/><span style="color: RED">*</span>
        </td>
     </tr>
     <tr>  
        <td align="center">
        <input  class=normal_btn id=btn1 onclick=save(); align="right" value=保存 type=button name=button/> 
        <input  class=normal_btn id=btn2 onclick=backTo(); align="right" value=返回 type=button name=button/> 
  		</td>
    </tr>   
    <br/> 
    <tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<script type="text/javascript" >
	var cloMainTime = 1;
	
	function showLbour(){
		parent.OpenHtmlWindow('<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/showLabour.do',900,700);
	}
	function setLabour(code,name,hours){
		$('LABOUR_CODE').value=code;
		$('LABOUR_NAME').value=name;
		$('LABOUR_HOURS').value = hours;
	}
	function save(){
	var code = $('LABOUR_CODE').value;
	var name = $('LABOUR_NAME').value;
	var hour = $('LABOUR_HOURS').value;
	var price = $('LABOUR_PRICE').value;
	var reg = /^(\d+\.\d{1,2}|\d+)$/;
	if(code==""||name==""||hour==""||price==""){
		MyAlert("带 * 号必填!");
		return false;
	}
	if(!reg.test(price)){
		MyAlert("工时单价只能是整数或者2位小数!");
		return false;
	}
		 MyConfirm("此次操作将会更新已存在的数据,是否继续？",saveInfo,[]);
	}
	function saveInfo(){
	$('btn1').disabled=true;
	$('btn2').disabled=true;
		var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/save.json";
		 makeNomalFormCall(url,saveResult,'fm','');
	}
	function saveResult(json){
		var str = json.modResult;
		if(str=="success"){
			MyAlert("保存成功!");
		 	fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/partLabourPer.do";
       	 	fm.submit();
		}else{
			MyAlert("保存失败,请联系管理员!");
			$('btn1').disabled=false;
			$('btn2').disabled=false;
			return false;
		}
	}
	function backTo(){
		 fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/partLabourPer.do";
       	 fm.submit();
	}
</script>
</form>
</body>
</html>
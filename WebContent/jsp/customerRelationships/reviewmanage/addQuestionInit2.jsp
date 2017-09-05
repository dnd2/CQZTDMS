<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import=" com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>  
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
 <script type="text/javascript">
   function gcheck()
	     {     CheckBox.Checked = true; }
</script>
<title>我的客户回访</title>
<% String contextPath = request.getContextPath(); %>
 <script language="javascript">
			String.prototype.Trim = function() 
			{
				return this.replace(/(^\s*)|(\s*$)/g, ""); 
			}
			function HaveSep(str1)
			{
				var i;
				var str;
				str = str1.Trim();				
				i = str.indexOf("|");
				if (i>0 && i<str.length-1 && str.charAt(str.length-1)!="|") 
					return true;
				return false;
			}
			function CheckSave()
			{
				var obj= document.getElementById('ddlQuestionType');
				var index=obj.selectedIndex;
				var text=obj.options[index].text;
				var value=obj.options[index].value;
				//MyAlert(text);
				//MyAlert(value);
				if(index==0)
				{
					 MyAlert("请输选择问题类型！");
      				 return false;
				}
				
                var obj1= document.getElementById('QD_NO'); 
				
				if(obj1.value=="")
				{
					 MyAlert("题号不能为空，请输入题号！");
      				 return false;	
				}
				else if(isnumber(obj1.value)==false){
					
					
				    MyAlert("题号必须为整数！");
				    document.getElementById('QD_NO').value="";
                    return false;	
				}
				
				if(window.Form1.txtQuestion.value.Trim() == "")
				{
					 MyAlert("请输入问题！");
					return false; 
				}
				//95151001 单选    95151002多选
				if (text== "单选" ||text == "多选")
				{	
					if(window.Form1.txtSelection.value.Trim() == "")
					{
						MyAlert("选择题选项不能为空！");
						return false; 
					}				
					if(!HaveSep(window.Form1.txtSelection.value))
					{
						MyAlert("选择题选项：必须以|分隔！");
						return false; 
					}
				}else
				{
				    var radio=document.getElementById("rblTextMode_1");
				    if(radio)
				    {
				        if(radio.checked)
				        {
				            var v=document.getElementById("txtWidth");
				            if(v)
				            {
				                if(v.value.Trim()=="")
				                {
				                    //MyAlert("");
				                    MyAlert("请输入多行文本框的宽度。");
				                    return false;
				                }
				                else if(isnumber(v.value)==false)
				                {
				                    //MyAlert("");
				                    MyAlert("多行文本框的宽度必须为整数。");
				                    return false;				            
				                }else if(parseInt(v.value)<20)
				                {
				                	  MyAlert("多行文本框的宽度不能小于20！");
				                	  return false;
				                }else if(parseInt(v.value)>1000)
				                {
				                	  MyAlert("多行文本框的宽度不能超过1000！");
				                	  return false;
				                }
				            }
				             var v2=document.getElementById("txtHeight");
				            if(v2)
				            {
				                if(v2.value.Trim()=="")
				                {
				                    //MyAlert("");
				                    MyAlert("请输入多行文本框的高度。");
				                    return false;
				                }
				                else if(isnumber(v2.value)==false)
				                {
				                    //MyAlert("");
				                    MyAlert("多行文本框的高度必须为整数。");
				                    return false;				            
				                }else if(parseInt(v2.value)<14)
				                {
				                	  MyAlert("多行文本框的高度不能小于14！");
				                	  return false;
				                }else if(parseInt(v2.value)>200)
				                {
				                	  MyAlert("多行文本框的高度不能超过200！");
				                	  return false;
				                }
				            }
				        }
				    }
				    var radio=document.getElementById("rblTextMode_0");
				    if(radio)
				    {
				        if(radio.checked)
				        {
				            var v=document.getElementById("txtWidth");
				            if(v)
				            {
				                if(v.value.Trim()=="")
				                {
				                    MyAlert("请输入多行文本框的宽度。");
				                    return false;
				                }
				                else if(isnumber(v.value)==false)
				                {
				                    MyAlert("多行文本框的宽度必须为整数。");
				                    return false;				            
				                }else if(parseInt(v.value)<20)
				                {
				                	  MyAlert("多行文本框的宽度不能小于20！");
				                	  return false;
				                }else if(parseInt(v.value)>1000)
				                {
				                	  MyAlert("多行文本框的宽度不能超过1000！");
				                	  return false;
				                }
				            }
				        }
				    }
				   
				}
				
				//ShowCoverLayer();
				return true; 
			}
        function isnumber(str)
        {
	        var number_chars = "1234567890";
	        var i;
	        for(i=0;i<str.length;i++)
	        {	
		        if (number_chars.indexOf(str.charAt(i))==-1) 
			        return false;
	        }
	        return true;
        }				
        //选择问题类型
        function ChangeQuestionType(questionType)
        {
            var bar=document.getElementById('SectionBar');
            var textmode=document.getElementById('TextMode');
            if((questionType==1)||(questionType==2))
            {
                if(bar)
                {
                    bar.style.display="";
                    textmode.style.display='none';
                }
            }
            else
            {
                if(bar)
                {
                    bar.style.display="none";
                    textmode.style.display='';
                    var radio=document.getElementById('rblTextMode_0');
                    TextModeChange(radio.checked);
                }
            }
        }
		function selectConfirm()
		{
			var v=document.form1.PaperName;
			returnValue=v.value;
			self.close();
		}
		function selectCancel()
		{
			returnValue = "";
			self.close();
		}	
		function TextModeChange(isSingle)
        {
            if(isSingle)
            {
                var control=document.getElementById("lblWidth");
                if(control)
                {
                    control.style.display="";
                }
                control=document.getElementById("txtWidth");
                if(control)
                {
                    control.style.display="";
                }
                control=document.getElementById("lblHeight");
                if(control)
                {
                    control.style.display="none";
                }
                control=document.getElementById("txtHeight");
                if(control)
                {
                    control.style.display="none";
                }                     
            }
            else
            {   //多行文本框
                var control=document.getElementById("lblWidth");
                if(control)
                {
                    control.style.display="";
                }
                control=document.getElementById("txtWidth");
                if(control)
                {
                    control.style.display="";
                }
                control=document.getElementById("lblHeight");
                if(control)
                {
                    control.style.display="";
                }
                control=document.getElementById("txtHeight");
                if(control)
                {
                    control.style.display="";
                }                
            }
        }	
    </script>

</head>
<body onload="gcheck();">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户化关系管理 &gt; 回访管理 &gt; 问卷管理 &gt; 问题新增 
</div>
  <form method="post" name="Form1" id="Form1">
  <input type="hidden" name="QR_ID" id="QR_ID" value="${QR_ID}"/>
  <table width="100%" align="center" cellpadding="2" cellspacing="1" class=table_query>
    <tbody>
      <tr class="">
        <td align="right">问题类型： </td>
         <td width="218"><select onchange="ChangeQuestionType(this.selectedIndex)" id="ddlQuestionType" name="ddlQuestionType">
           <option selected="selected" value="">--请选择--</option>
		   <c:forEach var="ql" items="${choiceList}">
				<option value="${ql.CODE_ID}" >${ql.CODE_DESC}</option>
		   </c:forEach>
        </select>
          <div id="TextMode" style="DISPLAY: none"><span id="rblTextMode" style="dislplay: none">
            <input onclick="TextModeChange(true);" id="rblTextMode_0" checked="checked" type="radio" value="1" name="rblTextModes" />
            <label for="rblTextMode_0">单行文本框</label>
            <input onclick="TextModeChange(false);" id="rblTextMode_1" type="radio" value="2" name="rblTextModes" />
            <label for="rblTextMode_1">多行文本框</label>
            </span>  <span id="lblWidth" style="DISPLAY: inline">宽度：</span>
            <input id="txtWidth" style="DISPLAY: inline; WIDTH: 42px" value="600" name="txtWidth" />
            <span id="lblHeight" style="DISPLAY: none">高度：</span>
            <input id="txtHeight" style="DISPLAY: none; WIDTH: 42px" value="110" name="txtHeight" />
          </div></td>
         <td width="66"><div align="right">题号：</div></td>
	     <td width="336"><input type="text" class="normal_txt" id="QD_NO"  name="QD_NO" value="${QD_NO}"/>
	       <font color="#ff0000" face="宋体">*</font>
         <td width="207">&nbsp;</td>
      </tr>
      <tr class="">
        <td width="168" align="right" style="WIDTH: 17%">问题： </td>
        <td colspan="3"><textarea id="txtQuestion" style="WIDTH: 600px" rows="5" name="txtQuestion"></textarea>
          <font color="#ff0000" face="宋体">*</font><br />
          <span style="FONT-FAMILY: 宋体; COLOR: red">问题内容最多允许输入500个字。</span></td>
      </tr>
      <tr class="" id="SectionBar" style="DISPLAY: ">
        <td style="WIDTH: 17%" align="right">选择题选项： </td>
        <td colspan="3"><textarea id="txtSelection" style="WIDTH: 600px; dislplay: " rows="5" name="txtSelection"></textarea>
          <span id="Star" style="FONT-FAMILY: 宋体; COLOR: red">*</span> <br />
          <span style="FONT-FAMILY: 宋体; COLOR: red">选择题选项最多允许输入500个字。填写多个选项时以&quot;|&quot;分隔，如：小学|初中|高中|大学</span></td>
      </tr>
      <tr>
        <td colspan="4" align="center">
 
   
 
  <input id="queryBtn1" class="cssbutton"  value="保存" onclick="saveAction();"  type="button" name="button1" />
  <input id="queryBtn2" class="cssbutton" onclick="javascript:history.go(-1)" value="返回" type="button" name="button2" />
      <input id="chk_Clear" type="checkbox" name="chk_Clear" Checked="checked"/>
      <label for="chk_Clear">保存完成后自动清空</label></td>
      </tr>
    </tbody>
  </table>
  
<!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
 <!--分页 end --> 
</form> 
<br/>
<!--页面列表 begin -->
<script type="text/javascript" >
	function saveAction()
	{
		var flag = false;
			flag=CheckSave();
			//MyAlert(flag);
		if(flag)
		{
			 var url= "<%=contextPath%>/customerRelationships/reviewmanage/ReviewManage/questionAddFact2.json";
			 makeNomalFormCall(url,showResult,'Form1');
		}
	}
	function showResult(json){
		var msg=json.msg;
		if(msg=='01'){
			MyAlertForFun("保存成功",clearContext);
		}else if(msg=='02'){
			MyAlert('问题题号重复，该问卷中已有该题号，请修改题号！');
		}else if(msg='03'){
			MyAlert('问题名称重复，该问卷已有该问题，请修改名称！');
		}
		else{
			MyAlert('修改失败,请联系管理员');
		}
	}
	function clearContext(){
		var ischeck = document.Form1.chk_Clear;
		if(ischeck.checked)
		{
			document.getElementById('ddlQuestionType').selectedIndex=0;
			document.getElementById('QD_NO').value="";
			window.Form1.txtSelection.value="";
			window.Form1.txtSelection.valu="";
			document.getElementById('txtQuestion').value="";
		}else
		{
			
		}
		
	}
</script>  
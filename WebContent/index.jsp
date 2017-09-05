<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" class="login-html">
<head>
<link rel="shortcut icon" href="<%=request.getContextPath()%>/img/favicon.ico" type="image/vnd.microsoft.icon">
<link rel="icon" href="<%=request.getContextPath()%>/img/favicon.ico"  type="image/vnd.microsoft.icon">
<%
String path = request.getContextPath();
String errorMessage = (String)request.getAttribute("ERROR_MESSAGE");
%>
<title>君马新能源DCS系统</title>
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<script>
    $(function() {
        BrowserVersion.showNoSupportedTips();
    });

	function bgInit() {
        var b = document.getElementById('bgdiv');
        if ( b != null ) {
            var clientHeight = (document.documentElement.clientHeight - 576)/2;
		    b.style.marginTop = clientHeight + 'px';
        }
	}
	window.onresize = function() {
		bgInit();
	}

	var code;
    function createCode() {
        code = "";
        var codeLength = 5; //验证码的长度
        var checkCode = document.getElementById("checkCode");
        var codeChars = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9/*, 
        'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'*/); //所有候选组成验证码的字符，当然也可以用中文的
        var colors = new Array("Red","Green","Gray","Blue","Maroon","Aqua","Fuchsia","Lime","Olive","Silver");

        //checkCode.style.color = colors[Math.floor(Math.random()*10)]; 
        
        for (var i = 0; i < codeLength; i++) 
        {
            var charNum = Math.floor(Math.random() * 10);
            code += codeChars[charNum];
        }
        if (checkCode) 
        {
            checkCode.className = "code";
            checkCode.innerHTML = code;
        }
    }
    function validateCode() 
    {
        var inputCode = document.getElementById("validateCode").value;
        if (inputCode.length <= 0) 
        {
            MyAlert("请输入验证码！");
            return false;
        }
        else if (inputCode.toUpperCase() != code.toUpperCase()) 
        {
        	MyAlert("验证码输入错误！");
            createCode();
            return false;
        }
        return true; 
    }  
</script>
   <style type="text/css">
    .code
    {
            font-family:Arial;
            font-style:italic;
            color:blue;
            font-size:20px;
            border:0;
            font-weight:bolder; 
            cursor:pointer;
    }
    a
    {
        text-decoration:none;
        font-size:12px;
        color:#288bc4;
        }
    a:hover
    {
       text-decoration:underline;
        }
    </style>
    
</head>
<body class="xny-index-page u-main" onload="createCode();bgInit();">
	<div class="login-page">
        <form action="<%=path%>/common/UserManager/login.do" method="post" name="form1">
            <fieldset>
                <h2>DMS系统</h2>
                <div class="form-elem">
                    <label for=""><i class="icon-user" title="用户名"></i></label>
                    <input type="text" name="userName" id="tbUploadEndDate5" tabindex="1" autocomplete="off"/>    
                </div>
                <div class="form-elem">
                    <label for=""><i class="icon-key" title="密码"></i></label>
                    <input id="tbUploadEndDate22" type="password" name="password" tabindex="2" autocomplete="off"/>   
                </div>    
                <div class="form-elem">
                    <label for=""><i class="icon-lock" title="验证码"></i></label>
                    <input id="validateCode" class="form-input-code" type="text" name="validateCode" tabindex="3" />   
                    <a class="form-code-refresh" href="javascript:createCode()">
                        <i class="icon-repeat"></i>
                    </a>
                    <span class="code" id="checkCode" onclick="createCode()"></span>
                </div>   
            </fieldset>
            <div class="button-set">
                <button class="u-button u-submit" type="button" onclick="submitform();">登 录</button>
                <button class="u-button u-cancel" type="reset">重 置</button>
            </div>
        </form>
    </div>

<%--
<form action="<%=path%>/common/UserManager/login.do" method="post" name="form1">
<div id="bgdiv" style="width:1024px; height:576px; margin: 0px auto auto auto; background: url('<%=request.getContextPath()%>/img/chana/BJ-BJ.jpg') no-repeat center top;">
	<table width="100%" style="" cellpadding="0" cellspacing="0">
		<tr>
			<td height="329">&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td height="219" >&nbsp;</td>
			<td width="300"  height="219" >
				<div id="bgdiv" style="width:300px; height:219px; margin: 0px auto auto auto; background: url('<%=request.getContextPath()%>/img/chana/logonDiv.png') no-repeat center top;">
				<table cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td colspan="2" height="69">&nbsp;</td>
					</tr>
					<tr>
						<td width="100" align="right" height="40">&nbsp;</td>
						<td><input name='userName' id='tbUploadEndDate5' style="width: 130px; height: 15px;" class='login_for' value=""/></td>
					</tr>
					<tr>
						<td align="right" height="23">&nbsp;</td>
						<td><input name='password' type='password' id='tbUploadEndDate22' style="width: 130px; height: 15px;" class="login_for" /></td>
					</tr>
					<tr >
						<td align="right" height="30">&nbsp;</td>
						<td style='white-space: nowrap;'>
							
							<input name='validateCode' id='validateCode' style="width: 50px" class="login_for" />
						    <!-- <img height="18" align="middle" id="validateCodeImage" src="<%=request.getContextPath()%>/image.jsp" />
						    <img height="18" src="<%=request.getContextPath() %>/img/chana/refresh.png" onclick="refreshCode()"/>
						     -->
						     <span class="code" id="checkCode" onclick="createCode()" ></span>
						    <img height="18" src="<%=request.getContextPath() %>/img/chana/refresh.png" onclick="createCode()"/>
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<img src="<%=request.getContextPath() %>/img/chana/logonbtn.png" style="cursor: pointer;" onclick="submitform();"/>
							&nbsp;
							<img src="<%=request.getContextPath() %>/img/chana/resetbtn.png" style="cursor: pointer;" onclick="document.form1.reset();"/>
						</td>
					</tr>
				</table>
				</div>
			</td>
		</tr>
	</table>
	<br/>
	<br/>
</div>
</form> --%>
</body>
<script language="javascript">
	<% 
	if(errorMessage != null && !errorMessage.equals(""))
	{ 
	%>
	MyAlert('<%=errorMessage %>');
	<% 
	} 
	%>
	function submitform(){
		 //屏蔽猎豹
        if (window.navigator.userAgent.indexOf('LBBROWSER') != -1) {
            MyAlert("本系统不支持猎豹浏览器,请用IE浏览器登陆!");
            return;
        }
        //屏蔽搜狗
        if(navigator.userAgent.toLowerCase().indexOf('se 2.x')>-1 ? true : false){
            MyAlert("本系统不支持搜狗浏览器,请用IE浏览器登陆!");
            return;
        }
		/*if(!Prototype.Browser.IE){
			MyAlert("本系统不支持非IE浏览器,请用IE浏览器登陆!");
			return;
		}*/else{
			  //新增判断是否同一台机器已有用户登陆 2013-05-15 HXY
			var flag = validateCode();
			if(!flag)
				return;
            var url = "<%=request.getContextPath()%>/common/UserManager/validateLogin.json" ;
            return makeCall(url ,callBackShowInfo,{});
		   // return document.form1.submit();
		}
	}

	function callBackShowInfo(json) {
        if(json.logined != null && json.logined == true) {
               MyAlert("<center>请注销以前登录窗口!</center>");
               document.getElementById('tbUploadEndDate5').disabled = 'disabled';
               document.getElementById('tbUploadEndDate22').disabled = 'disabled';
               document.getElementById('validateCode').disabled = 'disabled';
               return false;
        }
        return document.form1.submit();
 }

	
	function reset_text(){
		document.form1.reset();
		document.getElementById('userName').focus();
		return false;		
	}
	
		window.document.onkeydown = function (){
		if(event.keyCode==13){
			submitform();
		};
	}
		
	function refreshCode() {
		var img = document.getElementById("validateCodeImage"); 
		now = new Date(); 
		img.src = "<%=request.getContextPath()%>/image.jsp?code="+now.getTime(); 
	}
</script>
</html>
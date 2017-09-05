//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
//2009-05-27 升级到V400，使用ATClient控件
//2011-09-20 整理
//           记录通信数据日志：手工创建目录 C:\ATOCX\LOG_ULTRA_ECMA
//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
var isConnect_CTI = 0;
var nTimer_id;
var login_uid,login_pwd,login_ext;
var IsCallDisconnect = 0;

window.onunload = function() {
  $("Prompt").value ="";
};


// 控件处理函数
function Connect_CTI() {

   var strIP = $("txtIP").value;
   var nRet_conn = document.ut_atocx.ATConnect(strIP, 18005);
   if (nRet_conn == false) {
       alert("与UltraCTI连接失败，请检查，原因：\n  1.网络连接是否正常.\n  2.UltraCTI工作是否正常.");
    }
    else {
        rem("连接CTI服务器成功！");
    }
    return false;
}

function DisConnect_CTI() {
    document.ut_atocx.ATDisconnect();
    return false;
}


//座席登录
function client_login() {
    var validity = false; // assume valid

    login_uid = $("txtUid").value;
    login_pwd = $("txtPwd").value;
    login_ext = $("txtExt").value;
    if (login_pwd == "") login_pwd = "";

    if (!check_empty(login_uid)) {
        alert('对不起，您输入的工号不正确！');
        return validity;
    }
    if (!check_empty(login_ext)) {
        alert('对不起，您输入的座席号码不正确！');
        return validity;
    }

    document.ut_atocx.ATLogin(login_uid, login_pwd, login_ext, "0");
    var Result = document.ut_atocx.ATCommandResult();
    if (Result == "OK") {
        validity = true;
        isConnect_CTI = 1;
        rem("登录CTI成功！");
        nTimer_id = setTimeout('delay_1ms_timer()', 1000);
    }
    else if (Result == "ERROR_UID")
        alert("Local:登录失败，请检查工号是否正确！");
    else if (Result == "ERROR_PWD")
        alert("Local:登录失败，请检查密码是否正确！");
    else if (Result == "ERROR_REP")
        alert("Local:对不起，该帐号正在使用！");
    else if (Result == "FAIL")
        alert("Local:登录失败，请检查工号或密码是否正确！");
    else
        alert("Local:登录失败(" + Result + ")，请检查 CTI Server 是否运行正常！");
    return validity;
}

//座席登出
 function client_logout() {
    var validity = true; // assume valid 
     if (isConnect_CTI > 0) {
         document.ut_atocx.ATDisconnect();
         isConnect_CTI = 0;
         rem("座席成功退出");
     }
     return validity;
 }

 function check_empty(text) {
     return (text.length > 0); // 如果为空则返回错误
 }

   function delay_1ms_timer() {
       if (isConnect_CTI == -1) {
           Connect_CTI();
           client_login();
           return 1;  //只提示一次
       }
       if (IsCallDisconnect == 1) {
           if (document.ut_atocx.myCall_Status == "06") document.ut_atocx.ATBeep(1000, 500);
           else IsCallDisconnect = 0;
       }
       nTimer_id = setTimeout('delay_1ms_timer()', 1000);
       return 1;
   }

//接收ATOCX的消息
function ut_atocx_ATMsgEvent(sender, EventCode, EventData) {
    var strDisp, strPrompt;
    var caller_code, ext_code, nDirection;
    var strStatus, lbl_name;
    var rc;
    switch (EventCode) {
        case 0: //显示信息
            strDisp = "EventCode=" + EventCode + ",EventData=" + EventData;
            rem(strDisp);
            break;

        case 1: //1：表示座席员状态发生变化，EventData 表示座席员工号
            document.ut_atocx.UidSelected = EventData;
            strStatus = document.ut_atocx.UidCall_Status;
            show_agent_status(document, EventData, strStatus);
            break;
        case 2: //表示座席员呼叫状态发生变化，EventData 表示座席员工号
            break;
        case 3: //3：表示外线状态发生变化，EventData 表示外线号码
            document.ut_atocx.TrunkSelected = EventData;
            strStatus = document.ut_atocx.TrunkStatus;
            show_trunk_status(document, EventData, strStatus);
            break;
        case 4: //4：表示分机状态发生变化，EventData 表示分机号
            document.ut_atocx.ExtSelected = EventData;
            strStatus = document.ut_atocx.ExtStatus;
            show_ext_status(document, EventData, strStatus);
            break;

        case 5: //5：表示其它座席传来消息，EventData 表示消息内容
            Popup_Callin(EventCode, EventData);
            document.ut_atocx.ATBeep(2000, 500);   //add by gaoww 20101014 收到座席间的消息，发出声音提醒座席
            break;

        case 6: //6：表示有用户呼入信息：应用程序依靠此消息弹出用户资料，EventData 表示分机号
            if ((document.ut_atocx.ExtDirection == 2) || (document.ut_atocx.ExtDirection == 4)) break;
            Popup_Callin(EventCode, EventData);
            break;

        case 7: //7：表示有OEM呼入信息，可能是FAX、VMS、EMAIL、SMS、CHAT等            
            Popup_Callin(EventCode, EventData);
            document.ut_atocx.ATBeep(2000, 500);   //add by gaoww 20101014 收到多媒体消息，发出声音提醒座席
            break;

        case 10: //10：表示呼叫进入排队，EventData 表示呼叫标识
            rem("呼叫进入排队" + EventData)
            break;
        case 11: //11：表示呼叫结束排队，EventData 表示呼叫标识 
            rem("呼叫结束排队" + EventData)
            break;

        case 100: //100：表示与服务器的连接中断
            strDisp = "表示与服务器的连接中断";
            if (isConnect_CTI > 0) {
                isConnect_CTI = -1;
                alert("本座席与 CTI Server 的连接出现故障，请重新登录！");
            }
            //Connect_CTI();
            //client_login();
            break;
    }
}

//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
//表示座席员状态发生变化，显示相应状态
//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
function show_agent_status(frmDetails, strUid, strStatus) {
    document.ut_atocx.UidSelected = strUid;
    var user_ext = document.ut_atocx.UidExt; //ATSeatstrUID();
    var user_name = document.ut_atocx.UidName; //ATSeatstrUID();
    if (user_name == "") user_name = document.ut_atocx.UidCode;
    if (user_name == "") user_name = document.ut_atocx.UidExt;
    var lbl_name = get_call_status(strStatus);
    if (strUid == login_uid) IsCallDisconnect = 1;

    //01：可工作 02：置忙 03：事后处理 04：离席
    var UidStatus = document.ut_atocx.UidStatus;
    if (UidStatus == "01") UidStatus = "可工作";
    else if (UidStatus == "02") UidStatus = "置忙";
    else if (UidStatus == "03") UidStatus = "事后处理";
    else if (UidStatus == "04") UidStatus = "离席";
    else if (UidStatus == "06") UidStatus = "未准备好";
    else if (UidStatus == "00") UidStatus = "注销";
    rem("座席状态：" + UidStatus + "，姓名：" + user_name + "，分机：" + user_ext + "，呼叫状态：" + lbl_name);
}

//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
//表示分机状态发生变化，显示相应状态
//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
function show_ext_status(frmDetails, strExt, strStatus) {

    document.ut_atocx.ExtSelected = strExt;
    var strUid = document.ut_atocx.Ext_Uid;
    //不处理登录座席
    if (strUid != "") return;

    var lbl_name = get_call_status(strStatus);
    rem("分机状态：" + lbl_name + "，分机：" + strExt);
}

//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
//表示外线状态发生变化，显示相应状态
//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
function show_trunk_status(frmDetails, strTrunk, strStatus) {
    var strDisp, strPrompt;
    var caller_code, ext_code, nDirection;

    var lbl_name = get_call_status(strStatus);
    caller_code = document.ut_atocx.TrunkCaller;
    ext_code = document.ut_atocx.TrunkCalled;
    nDirection = document.ut_atocx.TrunkDirection;
    rem("外线状态：" + lbl_name + "，主叫：" + caller_code + "，被叫：" + ext_code + "，方向：" + nDirection + "，时长：" + document.ut_atocx.TrunkTimer);
}

function get_call_status(strStatus) {
    var lbl_name;
    switch (strStatus) {
        case "01": //空闲
            lbl_name = "空闲";
            break;
        case "02": //应答
            lbl_name = "摘机";
            break;
        case "03": //振铃
            lbl_name = "振铃";
            break;
        case "04": //回铃
            lbl_name = "回铃";
            break;
        case "05": // 连接
            lbl_name = "通话";
            break;
        case "06": //断开
            lbl_name = "断开";
            break;
        case "07": //摘机
            lbl_name = "占用";
            break;
        case "08": //转移
            lbl_name = "转移";
            break;
        default: //未知
            lbl_name = "未知";
            break;
    }
    return lbl_name;
}


//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
//表示弹屏态发生变化，显示相应状态
//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
function Popup_Callin(EventCode, EventData) {
    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    //表示其它坐席传来消息
    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    if (EventCode == 5) //表示其它坐席传来消息
    {
        var strShortMsg = EventData;
        var strItem = strShortMsg;
        var nPos = strShortMsg.indexOf(";");
        if (nPos > 0) {
            strItem = strShortMsg.substring(0, nPos);
            strItem = strItem.toUpperCase();
        }

        if (strItem == "SHORTMSG")//接收短消息
        {
            rem("接收公文：" + strShortMsg);
        }
        else if (strItem == "TRANSMSG")//转发工单
        {
            rem("转发工单：" + strShortMsg);
        }
        else if (strItem == "CALLBACK") //用户远程通过网络预设呼叫 
        {
        }
        else {
            alert(strShortMsg);
        }
    }
    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    //表示有用户呼入信息
    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
    else if (EventCode == 6) //表示有用户呼入信息：应用程序依靠此消息弹出用户资料
    {	
	var strExt=EventData;
        document.ut_atocx.ExtSelected=strExt;   
        var strCaller=document.ut_atocx.ExtCaller;    
        var strInfo=document.ut_atocx.Ext_UserInfo;
        var strTrunkCalled=document.ut_atocx.TrunkCalled; 
        var strCallid=document.ut_atocx.ExtCallId;
        //rem("呼叫编号:"+strCallid+"分机号:"+strExt+"主叫号码:"+strCaller+"中继号码:"+strTrunkCalled+"随路数据:"+strInfo); 
        strCallid=document.ut_atocx.ATGetCallId_list(EventData);
        var v=strCallid.split("|");
        for(var j=0; j<3; j++)
        {		
           var callkey=v[j]+"-"+EventData;           
           document.ut_atocx.CallKey_Selected=callkey;
           var calldirection=document.ut_atocx.Call_Direction;
           if(calldirection=="1")
           {
              strCaller=document.ut_atocx.Call_Caller;
              var strTrunk=document.ut_atocx.Call_Trunk;
              strExt=document.ut_atocx.Call_Ext;              
              strCallid=v[j];
              document.ut_atocx.TrunkSelected=strTrunk;
              strInfo=document.ut_atocx.Trunk_UserInfo;
    		  getIncomingAlertScreen(strCaller,strCallid,strInfo);
              rem("呼叫编号:"+strCallid+"分机号:"+strExt+"主叫号码:"+strCaller+"随路数据:"+strInfo); 
	          break;                           
           }
        } 
        //document.ut_atocx.ExtSelected = EventData;
        //var strCaller = document.ut_atocx.ExtCaller;
        //var chnum = document.ut_atocx.ExtCode;
        //var strInfo = document.ut_atocx.Ext_UserInfo; //"PINPOPUP;FILTER=xxx;"
        //var strPopupUrl = "callid=" + document.ut_atocx.ExtCallId + "&caller=" + strCaller + "&info=" + strInfo;
        //rem("来电信息：" + strPopupUrl);
    }
    else if (EventCode == 7) //表示OEM传来消息
    {
        var strSubKey = EventData;
        var strMsgInfo = document.ut_atocx.ATGetOEMMessage(EventData); // EventData OEM消息应为 “IVRTOAGENT”
        var strInfo = "";
        var strItem = strMsgInfo;
        var nPos = strMsgInfo.indexOf(";");
        if (nPos > 0) {
            strItem = strMsgInfo.substring(0, nPos);
            strItem = strItem.toUpperCase();
        }

        //alert(strSubKey+strMsgInfo);
        if (strSubKey == "IVRTOAGENT") {
            if (strItem == "VMS_RECV")   //接收传真消息
            {
                strInfo = "您有一个新留言，单击此处可以查看";
            }
            if (strItem == "FAX_RECV")   //接收传真消息
            {
                strInfo = "您有一个新传真，单击此处可以查看";
            }
        }
        else if (strSubKey == "SMSTOAGENT") //即时消息信息
        {
            strInfo = "您有一个新短信，单击此处可以查看";
        }
        else if (strSubKey == "EMAILTOAGENT") //接收EMAIL消息
        {
            //var strCallid = GetItemValue(1, "CALLID", strMsgInfo);
            strInfo = "您有一个新邮件，单击此处可以查看," + strCallid;
        }
        else if (strSubKey == "CHATTOAGENT") //接收Webcall-chat消息
        {
            strInfo = "在线客服," + strMsgInfo;
        }
        if (strInfo.length > 0)
            rem("OEM传来消息：" + strInfo);
    }
    return false;
}


function AT_Command(strCmd) {
      if (isConnect_CTI < 1) {
        alert("请先连接CTI服务器!");
        return false;
    }
    switch (strCmd) {
        case "Pickup":
            document.ut_atocx.ATAnswer(login_uid, "");
            break;
        case "Hangup":
            document.ut_atocx.ATHangup(login_uid, "");
            break;
        case "Pickup_dj":
            document.ut_atocx.ATPickCall(login_uid,$("txtTel").value,"");
            break;
        case "PlaceCall": //呼叫电话
            document.ut_atocx.ATPlaceCall(login_uid,$("txtTel").value);
            break;
        case "HoldCall":
            document.ut_atocx.ATHoldCall(login_uid);
            break;
        case "RetriveCall":
            document.ut_atocx.ATRetriveCall(login_uid, "");
            break;
        case "TransCall": //单步转接
            document.ut_atocx.ATTranCall(login_ext, $("txtTel_tran").value);
            break;
        case "ConsTrans": //协商转接
            document.ut_atocx.ATConsTrans(login_uid, $("txtTel_tran").value);
            break;
        case "TranOver": //协商转接完成
            document.ut_atocx.ATTranOver(login_uid, "");
            break;
        case "TransIVR": //转接IVR
            var strIvr = "AC_SWITCHIVR;CALLID=" + document.ut_atocx.myCall_CallId + ";EXT=" + login_ext + ";IVRFILE=" + $("txtIvrFile").value + ";NODE=" + $("txtIvrNode").value + ";IVRMSG="+login_uid+"|"+document.ut_atocx.myCall_CallId + ";";
            document.ut_atocx.ATTranCall_toIVR(login_uid,0,"",strIvr);
            break;
        case "Conf_est": //单步建立会议
            document.ut_atocx.ATConf_est(login_ext, $("txtTel_conf").value);
            break;
        case "ConsConf": //协商会议
            document.ut_atocx.ATConsConf(login_uid, $("txtTel_conf").value);
            break;
        case "ConfOver": //协商会议完成
            document.ut_atocx.ATConfOver(login_uid, "");
            break;

        case "Insert": //强插
            document.ut_atocx.ATInsert(login_uid, $("txtTel_insert").value, 1);
            break;
        case "MoniExt":  //监听
            document.ut_atocx.ATInsert(login_uid, $("txtTel_insert").value, 2);
            break;
        case "DisConnect":
            document.ut_atocx.ATDiscCall(login_uid, $("txtTel_insert").value,"");
            break;

        case "SendNote":
            var strSend = "SHORTMSG;FROM=" + login_uid + ";TO=" + $("txtDest").value + ";CALLID=12345678;SUB=" + $("txtMsg").value;
            document.ut_atocx.ATSendMsg(login_uid, $("txtDest").value, strSend);
            break;
        case "SendEmail":
            var strSend = "EMAIL_SEND;GHID=" + login_uid + ";TO=" + $("txtEmail").value + ";SUBJ=测试邮件;MSG=" + $("txtMsg").value;
            document.ut_atocx.ATSendOEMCommand(login_uid,  "", "AGENTTOMCI", strSend);
            break;
        case "SendSms":
            var strSend = "SMS_SEND;UID=" + login_uid + ";TEL=" + $("txtMobile").value + ";SUBJ=测试短信;BODY=" + $("txtMsg").value;
            document.ut_atocx.ATSendOEMCommand(login_uid, "", "AGENTTOMCI", strSend);
            break;
    
        case "SetBusy":
            document.ut_atocx.ATSetBusy(login_uid, 1, $("txtCause").value);
            break;
        case "SetNoBusy":
            document.ut_atocx.ATSetBusy(login_uid, 0, $("txtCause").value);
            break;
        case "SetLeave": //离席
            document.ut_atocx.ATSetLeaveSeat(login_uid, 1, $("txtCause").value);
            break;
        case "SetNoLeave": //取消离席
            document.ut_atocx.ATSetLeaveSeat(login_uid, 0, $("txtCause").value);
            break;
        case "AfterWorking":  //进入事后处理
            document.ut_atocx.ATSetAfterWorking(login_uid, 1, $("txtCause").value);
            break;
        case "AfterNoWorking": //取消事后处理
            document.ut_atocx.ATSetAfterWorking(login_uid, 0, $("txtCause").value);
            break;
            
        default:
            alert("未处理命令：" + strCmd);
            break;
    }
    return true;
}

function btn_Command(strCmd) {
    switch (strCmd) {
        case "ClearDisp": //清除显示
            $("Prompt").value = "";
            break;
        default:
            alert("未处理命令：" + strCmd);
            break;
    }
    return true;
}

function btnTest_command() {
    var strRet = document.ut_atocx.ATGetUid_Online("");
    alert(strRet);
    
}

function rem(strMsg) {
    //alert(strMsg);
    var myPrompt = document.getElementById("Prompt");
    if (myPrompt != null)
        myPrompt.innerText = myPrompt.innerText + "\n" + strMsg;
}


function $(itemID) {
    if (document.getElementById) {
        return document.getElementById(itemID);
    } else {
        return document.all(itemID);
    }
}

/*
 * CreateDate : 2009-09-24
 * CreateBy   : ChenLiang
 * Comment    : 菜单展示
 */

window.addEvent('domready', function(){
  initPage();
});

var sysFuns; // 系统功能
var userFuns; // 用户可用功能
var thisfunid; // 用户当前点击的功能
var shortCutFuns;

var thisPose;
var thisDept;
var thisifurl;

var arrlink = new Array();
arrlink.push("10020104");
arrlink.push("10060201");
arrlink.push("10080204");
arrlink.push("10020106");
arrlink.push("10090101");

var menu1 = 4; // 一级菜单
var menu2 = 6; // 二级菜单
var menu3 = 8; // 三级菜单

function loadMenuPage() {
	getSysFuns();
}

function showMenu() {
	var sf = sysFuns.sysfun;
	for(var i=0; i<sf.length; i++) {
		var fid = sf[i].funcId;
		var fname = sf[i].funcName;
		if(fid.length == menu1) {
			$('lmenu').innerHTML += "<a herf='javascript:void(0)' id='"+fid+"' target='menuIframe' onclick='showm("+fid+")' style=' cursor:pointer;'>"+fname+"</a>";
		}
	}
	
/*	var uf = userFuns.userfuns;
	for(var index=0; index<uf.length; index++) {
		var fid = uf[index].funcId;
		for(var k=0; k<arrlink.length; k++) {
			if(arrlink[k] == fid) {
				$('mylink').innerHTML += "<a href='"+syspath+uf[index].funcCode+".do' target='inIframe'>"+uf[index].funcName+"</a>";
			}
		}
				var fid = fid + '';
		fid = fid.substring(0,4);
		if(fid.length == menu1) {
			if($(fid) != null) {
				$(fid).style.display = "";
			}
		}
		}
*/		
   if(shortCutFuns &&  shortCutFuns.ShortFuns){
		var uf = shortCutFuns.ShortFuns;
		for(var index=0; index<uf.length; index++) {
		var fid = uf[index].funcId;
		
		$('mylink').innerHTML += "<a href='"+syspath+uf[index].funcCode+".do' target='inIframe'>"+uf[index].funcName+"</a>";
		}
	}


	
	//getTaskLink();
	$('menuIframe').src = syspath+"/menu/leftMenu.jsp";
	$('inIframe').src = syspath+"/welcome.jsp";
	getTaskLink.periodical(300000);
}

function showm(fid) {
	var purl = document.location;
	window.location = location.pathname+"?deptId="+thisDept+"&poseId="+thisPose+"&fid="+fid+"&furl="+thisifurl;
}

function initPage() {
	thisfunid = getparastr("fid");
	if(thisfunid == null) {
		thisfunid = "1022";
	}
	thisPose = getparastr("poseId");
	thisDept = getparastr("deptId");
	/*
	if(getparastr("furl") == null) {
		thisifurl = syspath+"/individualInfo/individualTask/IndividualTask/getTaskInfo.do";
	} else {
		thisifurl = getparastr("furl");
	}
	$('inIframe').src = thisifurl;
	*/
}

function getparastr(strname) {
var hrefstr,pos,parastr,para,tempstr;
hrefstr = window.location.href;
pos = hrefstr.indexOf("?")
parastr = hrefstr.substring(pos+1);

para = parastr.split("&");
tempstr="";
for(i=0;i<para.length;i++)
{
 tempstr = para[i];
 pos = tempstr.indexOf("=");
 if(tempstr.substring(0,pos) == strname) {
  return tempstr.substring(pos+1);
 }
}
return null;
}

function getSysFuns() {
	sendAjax(sysfun_url,getSysFunsBack,'fm');
}

function getUserFuns() {
	sendAjax(userfun_url,getUserFunsBack,'fm');
}

function getTaskLink() {
	//sendAjax(tasklink_url,getTaskLinkBack,'fm');
}

function getShortCutFuns(){
	sendAjax(shortcut_url,getShortCutFunsBack,'fm');
}

function getShortCutFunsBack(obj){
   shortCutFuns = obj;
   showMenu();
}

function getUserFunsBack(obj) {
	userFuns = obj;
	getShortCutFuns();
	
}

function getSysFunsBack(obj) {
	sysFuns = obj;
	getUserFuns();

}

var sp,hf,db1,db2,db3,db4;

function getTaskLinkBack(obj) {
/*
	sp = obj.splist;
	db1 = obj.dblist1;
	db2 = obj.dblist2;
	db3 = obj.dblist3;
	db4 = obj.dblist4;
	db5 = obj.dblist5;
	db6 = obj.dblist6;
	hf = obj.hflist;
	
	var ti = 0;
	if(db1 != null && db1.length > 0) {
		ti += db1.length;
	}
	if(db2 != null && db2.length > 0) {
		ti += db2.length;
	}
	if(db3 != null && db3.length > 0) {
		ti += db3.length;
	}
	if(db4 != null && db4.length > 0) {
		ti += db4.length;
	}
	if(db5 != null && db5.length > 0) {
		ti += db5.length;
	}
	if(db6 != null && db6.length > 0) {
		ti += db6.length;
	}
	
	if(sp != null && sp.length > 0) {
		$('spc').innerHTML = "("+sp.length+")";
	} else {
		$('spc').innerHTML = "(0)";
	}
	
	$('dbc').innerHTML = "("+ti+")";
	
	if(hf != null && hf.length > 0) {
		$('hfc').innerHTML = "("+hf.length+")";
	} else {
		$('hfc').innerHTML = "(0)";
	}
	*/
}

function showTask(id) {
	
}

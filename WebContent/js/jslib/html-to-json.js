
    /*获取元素,返回string对象*/
	function getElementsByJSet(jset){
		var json=getElementsBySetTag(jset);
		return JSON.stringify(json);
	}

	/*获取document的所有节点，返回匹配的json对象*/
	function getElementsBySetTag(jset){
		var item=$$("*");
		var json={};
		for(var i=0;i<item.length;i++){
			try{
			    if(item[i].getAttribute("jset")==jset){
			    	json[item[i].id]=encodeURI(item[i].value.trim(),"UTF-8");
			    }			    
			} catch(e){
				//alert(e.name + ": " + e.message);
			}
		}
		return json;	
	}
	
	/*
	 *动态导入JS文件
	**/
	function jsImport(path) {
	 var i;
	 var ss = document.getElementsByTagName("script");
	 for (i = 0; i < ss.length; i++) {
	  if (ss[i].src && ss[i].src.indexOf(path) != -1) {
	   return;
	  }
	 }
	 var s = document.createElement("script");
	 s.type = "text/javascript";
	 s.src = path;
	 var head = document.getElementsByTagName("head")[0];
	 head.appendChild(s);
	}	
	
	/*根据tagName获取元素,返回json对象
	function getElementByJSetTag(jset,tagName){				
		var item=document.getElementsByTagName(tagName);
		var json={};	
		for(var i=0;i<item.length;i++){
			try{
			    if(item[i].getAttribute("jset")==jset){
			    	json[item[i].id]=item[i].value;
			    }
			} catch(e){
				//alert(e.name + ": " + e.message);
			}
		}
		return json;	
	}
    */
    /*合并json对象
    function jCombine(source, target){
        var result={};
        for(var attr in source){  
        	result[attr]=source[attr];  
        }  
        for(var attr in target){  
        	result[attr]=target[attr];  
        }
        return result;
    }
	*/
	/*
	
	    function relatedChange(obj){       
        var id=obj.id;      
        var value=obj.value;
        var orginJson={};
        orginJson["ID"]=encodeURI(id,"UTF-8");
        orginJson["VALUE"]=encodeURI(value,"UTF-8");
        findValue(JSON.stringify(orginJson));
    }

    function findValue(jsonstr){
        alert("----jsonstr="+jsonstr);
        var url = "<%=contextPath%>/config/ConfigManager/getRelatedList.json?orginJson="+jsonstr;
        sendAjax(url,showValue,'fm');
    }

    function showValue(json){
		var returnJson;
		//设置对应数据
		if(Object.keys(json).length>0){
			keys = Object.keys(json);
			for(var i=0;i<keys.length;i++){
			   if(keys[i] =="returnJson"){
				   returnJson = json[keys[i]];
				   break;
			   }
			}
		    var jsonData = returnJson.records;
		    for(var i=0; i< jsonData.length;i++){
			    eval("fm."+jsonData[i]["ID"]).value=jsonData[i]["VALUE"];
			    alert(JSON.stringify(jsonData[i]));
			    findValue(encodeURI(JSON.stringify(jsonData[i]),"UTF-8"));	    
		    }
		}       
    }
    */
	
    //单选弹出框
	function popSingleSelect(sqlName,callFunc,paraJson){
		var url=g_webAppName+"/jsp/widget/popSingleSelect.jsp?sqlName="+sqlName+"&callFunc="+callFunc+"&paraJson="+JSON.stringify(paraJson);
		OpenHtmlWindow(url,600,400);
	}
	
    //复选弹出框
	function popMultiSelect(sqlName,callFunc,paraJson,checkJson){
		if($("checkJson")){
			document.getElementById("fm").removeChild($("checkJson"));
		}
		var ckEle=document.createElement("input");
		ckEle.id="checkJson";
		ckEle.type="hidden";
		ckEle.value=JSON.stringify(checkJson);
		document.getElementById("fm").appendChild(ckEle);	
		var url=g_webAppName+"/jsp/widget/popMultiSelect.jsp?sqlName="+sqlName+"&callFunc="+callFunc+"&paraJson="+JSON.stringify(paraJson);
		OpenHtmlWindow(url,600,400);	
	}	
	
	
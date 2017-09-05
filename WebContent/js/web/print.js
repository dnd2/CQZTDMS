var hkey_root,hkey_path,hkey_key  

02 hkey_root="HKEY_CURRENT_USER" 

03 hkey_path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\" 

04    

05 // 设置页眉页脚为空  

06 function PageSetup_Null()  

07 {  

08  try{  

09   var RegWsh = new ActiveXObject("WScript.Shell") ;  

10   hkey_key="header" ;  

11   RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"") ;  

12   hkey_key="footer" ;  

13   RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"") ;  

14   }  

15  catch(e){}  

16 }  

17    

18 // 设置页眉页脚为默认值  

19 function PageSetup_Default()  

20 {  

21  try{  

22   var RegWsh = new ActiveXObject("WScript.Shell") ;  

23   hkey_key="header" ;  

24   RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"&w&b页码，&p/&P") ;  

25   hkey_key="footer" ;  

26   RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"&u&b&d") ;  

27   }  

28  catch(e){}  

29 }  

30    

31 // 打印  

32 function PrintPage()  

33 {  

34  PageSetup_Null() ;  

35  window.print() ;  

36 // PageSetup_Default() ;  

37     

38 }  

39    

40 document.write('<style media=print>' );   

41 document.write('.Noprint{display:none;}');//用本样式在打印时隐藏非打印项目  

42 document.write('.PageNext{page-break-after: always;}');//控制分页  

43 document.write('</style>');    

44 document.write('<object  id=WebBrowser  width=0  height=0  classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2>');       

45 document.write('</object>');       

46      

47 document.write('<center class=Noprint>');   

48    

49 document.write('<input type=button value=打印 onclick=PrintPage()>');  

50 document.write('<input type=button value=页面设置 onclick=document.all.WebBrowser.ExecWB(8,1)>');    

51 document.write('<input type=button value=打印预览 onclick=document.all.WebBrowser.ExecWB(7,1)>');   

52 document.write('</center>'); 

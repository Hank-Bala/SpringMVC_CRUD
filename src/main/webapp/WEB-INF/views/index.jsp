<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<!DOCTYPE html>
<html>
<head> 
<link rel='stylesheet' href="<c:url value='/css/style.css' />" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Spring MVC</title>
</head>   
<body>
	<h2 style="text-align:center">Spring MVC 範例</h2>
	<h3 style="text-align:center">進行新增、刪除、修改、查巡</h3>
		<p style="text-align:center"><font  color='red'>${DataReset}</font>&nbsp;</p>
	<hr/>
	<div style="text-align:center">
    <a href='_01_customer/index'>處理客戶資料(無圖)</a><br><br>
	<a href='_02_member/showAllMembers'>處理會員資料(有圖)</a><br><br>
	</div>
	<hr/>
	<h6 style="text-align:center; color:red">${dbType}</h6>
</body>
</html>
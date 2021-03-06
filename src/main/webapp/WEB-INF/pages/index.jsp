<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>RATE THIS GUY</title>
    <link rel="stylesheet" type="text/css" href="../../resources/css/styles.css">
    <link href='http://fonts.googleapis.com/css?family=Roboto+Condensed:400,300&subset=latin,cyrillic' rel='stylesheet' type='text/css'>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<div class="wrapper">
<div class="logo">
    <!-- <img src="src/logo.png"> -->
    <h1><a href="/groups">RATE THIS GUY</a></h1>
</div>
<div class="group_name" style="margin-bottom: 30px;">
    <h2>Enter group for rating</h2>
</div>
<div class="center">
    <input class="group_input" placeholder="URL of group">
    <input class="group_submit" type="button" value="Let's go!">
</div>
<div class="group_name">
    <h2>or select already added group</h2>
</div>
<ul>
    <c:if test="${not empty groupId}">
        <script type="text/javascript">
            window.location = "/vote?group_id=${groupId}"
        </script>
    </c:if>
    <script>
        if (document.URL.indexOf("error") > 0) {
            alert("Unable to add group due to incorrect url!");
        }
        if (document.URL.indexOf("warning") > 0) {
            alert("Sorry, you can't add group with more than 500 members");
        }
    </script>

    <c:forEach items="${groups}" var="group">
    <li class="group_li">
        <div class="group_block">
            <img class="cross" src="../../resources/images/cross.png" id="${group.id}">
            <div class="user_background" style="border-radius: 10px; z-index: -1;"></div>
            <div class="group_info"><div class="abs_center"><a href = "/vote?group_id=${group.id}" style="color: white;">${group.name}</a></div></div>
            <div class="group_down">
                <div class ="sub_lans"><div class="abs_center"> <a class="group_button" href="http://vk.com/${group.screenName}"> vk.com </a> </div></div>
                <div class ="sub_lans"><div class="abs_center"> <a class="group_button" href = "/rating/${group.id}"> Rating </a> </div> </div>
            </div>
        </div>
    </li>
    </c:forEach>
    </ul>
    <div class="push"></div>
    </div> 
    <div class="footer">
    <div class="bottom">
        <div class="bottom_unit">About</div>
    </div> 
    </div>
</body>
<script type="text/javascript" src="/resources/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="/resources/js/index.js"></script>
<script type="text/javascript" src="/resources/js/main.js"></script>
</html>

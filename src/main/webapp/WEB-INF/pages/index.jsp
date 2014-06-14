<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>RATETHISMAN</title>
    <link rel="stylesheet" type="text/css" href="../../resources/css/styles.css">
    <link href='http://fonts.googleapis.com/css?family=Roboto+Condensed:400,300&subset=latin,cyrillic' rel='stylesheet' type='text/css'>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<div class="logo">
    <!-- <img src="src/logo.png"> -->
    <h1><a href="/">RATETHISMAN</a></h1>
</div>
<div class="group_name">
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
    <c:if test="${not empty error}">
        <div>${error}</div>
    </c:if>

    <c:forEach items="${groups}" var="group">
    <li class="group_li">
        <div class="group_block">
            <div class="user_background" style="border-radius: 10px; z-index: -1;"></div>
            <div class="group_info"><div class="abs_center"><a href="http://vk.com/${group.screenName}" style="color: white;">${group.name}</a></div></div>
            <div class="group_down">
                <div class ="sub_lans"><div class="abs_center"> <a class="group_button" href = "/vote?group_id=${group.id}"> Vote </a> </div></div>
                <div class ="sub_lans"><div class="abs_center"> <a class="group_button" href = "/rating/${group.id}"> Rating </a> </div> </div>
            </div>
        </div>
    </li>
    </c:forEach>
</ul>
</body>
<script type="text/javascript" src="/resources/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="/resources/js/index.js"></script>
</html>

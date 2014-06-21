<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Rating | RATE THIS GUY</title>
  <link rel="stylesheet" type="text/css" href="../../resources/css/styles.css">
  <link href='http://fonts.googleapis.com/css?family=Roboto+Condensed:400,300&subset=latin,cyrillic' rel='stylesheet' type='text/css'>
</head>
<body>
<div class="wrapper">
  <div class="logo">
    <!-- <img src="src/logo.png"> -->
    <h1><a href="/groups">RATE THIS GUY</a></h1>
  </div>  
  <div class="group_name">
    <h2>Ratings for group "${group.name}"</h2>
  </div> 
  <ul>
    <c:forEach items="${users}" var="user">
    <li class="user_li">
      <a href="http://vk.com/id${user.id}" target="_blank">
      <div class="user_block" style="background-image: url(${user.photoUrl});">
        <div class="user_info">
          <div class="user_background"></div>
          <span class="user_text">${user.firstName} ${user.lastName}, ${user.rating}</span>
        </div>
      </div>
      </a>
    </li>
    </c:forEach>
  </ul>
  <div class="push"></div>
</div> 
<div class="footer">
    <div class="bottom">
        <a class="bottom_unit" href="mailto:support@thatman.ru">Send a feedback</a>
        <div class="bottom_unit" onclick='alert("Oh, dear lord! Our card number is 0000 0000 0000 0000. Thanks for your support!")'>Donate</div>
    </div> 
</div>
</body>
</html>
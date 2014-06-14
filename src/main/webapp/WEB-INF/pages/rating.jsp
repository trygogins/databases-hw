<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Rating</title>
  <link rel="stylesheet" type="text/css" href="../../resources/css/styles.css">
  <link href='http://fonts.googleapis.com/css?family=Roboto+Condensed:400,300&subset=latin,cyrillic' rel='stylesheet' type='text/css'>
</head>
<body>
  <div class="logo">
    <!-- <img src="src/logo.png"> -->
    <h1><a href="/">#RATETHISMAN</a></h1>    
  </div>  
  <div class="group_name">
    <h2>Ratings for group ${group}</h2> 
  </div> 
  <ul>
    <c:forEach items="${users}" var="user">
    <li>
      <div class="user_block" style="background-image: url(${user.photoUrl});">
        <div class="user_info">
          <div class="user_background"></div>
          <span class="user_text">${user.firstName} ${user.lastName}, ${user.rating}</span>
        </div>
      </div>
    </li>
    </c:forEach>
  </ul>
</body>
</html>
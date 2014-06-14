<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>Rating</title>
</head>
<body>
  <h1>#RATETHISMAN</h1>
  <h2>Ratings for group ${group}</h2>
  <table>
    <c:forEach items="${users}" var="user">
      <tr>
        <td><img src="${user.image}"></td>
        <td>${user.name} ${user.surname}</td>
        <td>${user.rating}</td>
      </tr>
     </c:forEach>
   </table>
</body>
</html>
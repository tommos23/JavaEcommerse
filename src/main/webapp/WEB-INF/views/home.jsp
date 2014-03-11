<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<!--<P>  The time on the server is ${serverTime}. </P>-->

<c:forEach items="${articles}" var="article">
	<p>${article.id}</p>
</c:forEach>
</body>
</html>

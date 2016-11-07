<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Projects</title>
</head>
<body>
<h1><a href="<c:url value="/projects" />">Projects</a></h1>

<table>
<tr>
	<c:forEach var="p" items="${projects}">
	<td><c:out value="${p.name}" /></td>
	<td><c:out value="${p.type}" /></td>
	</c:forEach>
</tr>
</table>

</body>
</html>
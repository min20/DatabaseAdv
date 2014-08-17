<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>

<%@page import="java.util.*" %>
<%@page import="com.mongodb.*" %>

<html>
<head>
	<meta charset="UTF-8"></meta>
	<title>Hello, mongo Blog!</title>
	<style>
		input[name=title] {
			width: 200px;
		}

		input[type="submit"] {
			display: block;
		}

		textarea {
			width: 330px;
			height: 150px;
		}
	</style>
</head>

<body>
	<h1><a href="/">메인 화면으로</a></h1> 

<%
	// DB Connection
	MongoClient mongoClient = new MongoClient("localhost");

	// 사용할 DB와 collection 선택
	DB db = mongoClient.getDB("popidb");
	DBCollection collection = db.getCollection("article");

	request.setCharacterEncoding("UTF-8");
	String title = new String(request.getParameter("title"));
	String writer = new String(request.getParameter("writer"));

	if (title == "") {
		title = ".";
	}
	if (writer == "") {
		writer = ".";
	}

	List<BasicDBObject> conditions = new ArrayList<BasicDBObject>();

	conditions.add(new BasicDBObject("title", 
			new BasicDBObject("$regex", ".*" + title + ".*")
					.append("$options", "i")));

	conditions.add(new BasicDBObject("writer",
			new BasicDBObject("$regex", ".*" + writer + ".*")
					.append("$options", "i")));

	DBCursor cursor = collection.find(
			new BasicDBObject("$and", conditions));
	cursor.sort(new BasicDBObject("_id", -1));

	while (cursor.hasNext()) {
		DBObject currentArticle = cursor.next();
		out.print("<div>");
		out.print("<h2>" + currentArticle.get("title") + "</h2>");
		out.print("<p>" + currentArticle.get("writer") + "</p>");
		out.print("<p>" + currentArticle.get("content") + "</p>");
		out.print("</div>");
	}
%>
</body>
</html>


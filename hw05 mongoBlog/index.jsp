<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>

<%@page import="java.util.Set" %>
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
	<form action="articleController.jsp" method="post">
		<input type="text" name="title" placeholder="제목" />
		<input type="text" name="writer" placeholder="작성자" /><br />
		<textarea name="content" placeholder="내용"></textarea>

		<input type="submit" value="글쓰기">
	</form>

	<form action="search.jsp" method="post">
		<input type="text" name="title" placeholder="제목" />
		<input type="text" name="writer" placeholder="작성자" /><br />

		<input type="submit" value="검색하기">
	</form>
<%
	// DB Connection
	MongoClient mongoClient = new MongoClient("localhost");

	// 사용할 DB와 collection 선택
	DB db = mongoClient.getDB("popidb");
	DBCollection collection = db.getCollection("article");

	DBCursor cursor = collection.find();
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


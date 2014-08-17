<%@ page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8" %>

<%@page import="java.util.Set" %>
<%@page import="com.mongodb.*" %>

<%
	// DB Connection
	MongoClient mongoClient = new MongoClient("localhost");

	// 사용할 DB와 collection 선택
	DB db = mongoClient.getDB("popidb");
	DBCollection collection = db.getCollection("article");

	request.setCharacterEncoding("UTF-8");
	String title = new String(request.getParameter("title"));
	String writer = new String(request.getParameter("writer"));
	String content = new String(request.getParameter("content"));

	BasicDBObject document = new BasicDBObject("title", title);
	document.append("writer", writer);
	document.append("content", content);

	collection.insert(document);

	response.sendRedirect("/");
%>


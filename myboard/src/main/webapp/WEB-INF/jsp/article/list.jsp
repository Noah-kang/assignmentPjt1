<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>게시판 목록</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

.container {
	width: 80%;
	margin: auto;
	padding: 20px;
}

h1 {
	text-align: left;
	font-size: 28px;
	color: #333;
	border-bottom: 2px solid #ddd;
	padding-bottom: 10px;
}

.search-section {
	margin-bottom: 20px;
	display: flex;
	gap: 10px;
	align-items: center;
}

.search-section input, .search-section select, .search-section button {
	padding: 8px 12px;
	border: 1px solid #ccc;
	border-radius: 4px;
}

table {
	width: 100%;
	border-collapse: collapse;
	text-align: center;
}

table th, table td {
	padding: 10px;
	border: 1px solid #ddd;
}

table th {
	background-color: #f8f9fa;
	color: #333;
	font-weight: bold;
}

table tr:nth-child(even) {
	background-color: #f9f9f9;
}

table tr:hover {
	background-color: #f1f1f1;
}

a {
	text-decoration: none;
	color: #007bff;
}

a:hover {
	text-decoration: underline;
}

.title {
	text-align: left;
	padding-left: 20px;
}

.pagination {
	display: flex;
	justify-content: center;
	margin: 20px 0;
	gap: 5px;
}

.pagination a, .pagination span {
	padding: 8px 12px;
	text-decoration: none;
	color: #007bff;
	border: 1px solid #ddd;
	border-radius: 4px;
}

.pagination a.active {
	background-color: #007bff;
	color: #fff;
}

.write-btn {
	display: inline-block;
	padding: 8px 12px;
	margin-bottom: 10px;
	background-color: #007bff;
	color: #fff;
	text-decoration: none;
	border-radius: 4px;
}

.write-btn:hover {
	background-color: #0056b3;
}
</style>
</head>
<body>
	<div class="container">
		<h1>게시판 목록</h1>

		<!-- 검색 영역 -->
		<form action="/myboard/article/list" method="get"
			class="search-section">
			<select name="searchType">
				<option value="title" ${searchType == 'title' ? 'selected' : ''}>제목</option>
				<option value="writer" ${searchType == 'writer' ? 'selected' : ''}>작성자</option>
			</select> <input type="text" name="keyword" placeholder="검색어를 입력하세요"
				value="${keyword}">
			<button type="submit">검색</button>
		</form>

		<!-- 전체 게시글 수 출력 -->
		<div style="margin-bottom: 10px; font-size: 14px;">
			<strong>전체: ${totalArticles}건</strong>
		</div>

		<!-- 게시판 테이블 -->
		<table>
			<thead>
				<tr>
					<th>번호</th>
					<th>제목</th>
					<th>작성자</th>
					<th>작성일</th>
					<th>조회수</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="article" items="${articles}">
					<tr>
						<td>${article.rowNum}</td>
						<td class="title"
							style="padding-left: ${(article.depth != null ? article.depth : 0) * 20}px;">
							<a href="/myboard/article/${article.articleId}">${article.title}</a>
						</td>
						<td>${article.writer}</td>
						<td><fmt:formatDate value="${article.createdAt}"
								pattern="yyyy-MM-dd" /></td>
						<td>${article.viewCount}</td>
					</tr>
				</c:forEach>
				<c:if test="${empty articles}">
					<tr>
						<td colspan="5">등록된 게시글이 없습니다.</td>
					</tr>
				</c:if>
			</tbody>
		</table>


		<!-- 페이징 영역 -->
		<div class="pagination">
			<!-- 처음 페이지 -->
			<a href="?page=1&searchType=${searchType}&keyword=${keyword}">&laquo;</a>

			<!-- 이전 페이지 -->
			<c:if test="${currentPage > 1}">
				<a
					href="?page=${currentPage - 1}&searchType=${searchType}&keyword=${keyword}">&lt;</a>
			</c:if>

			<!-- 페이지 번호 -->
			<c:forEach begin="${startPage}" end="${endPage}" var="page">
				<a href="?page=${page}&searchType=${searchType}&keyword=${keyword}"
					class="${currentPage == page ? 'active' : ''}"> ${page} </a>
			</c:forEach>

			<!-- 다음 페이지 -->
			<c:if test="${currentPage < totalPages}">
				<a
					href="?page=${currentPage + 1}&searchType=${searchType}&keyword=${keyword}">&gt;</a>
			</c:if>

			<!-- 마지막 페이지 -->
			<a
				href="?page=${totalPages}&searchType=${searchType}&keyword=${keyword}">&raquo;</a>
		</div>


		<!-- 새 글 쓰기 버튼 -->
		<a href="/myboard/article/write" class="write-btn">새 글 쓰기</a>

	</div>
</body>
</html>

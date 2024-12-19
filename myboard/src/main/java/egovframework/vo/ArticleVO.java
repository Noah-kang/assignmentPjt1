package egovframework.vo;

import java.sql.Timestamp;

public class ArticleVO {
	// 게시글 ID
    private int articleId;
    // 제목
    private String title;
    // 내용
    private String content;
    // 작성자
    private String writer;
    // 비밀번호
    private String password;
    // 생성일
    private Timestamp createdAt;
    // 수정일
    private Timestamp updatedAt;
    // 조회수
    private int viewCount;
    // 상위글 ID
    // 원글은 null 이라서 int가 아닌 Integer 
    private Integer parentId;  
    // 소프트 삭제를 위한 삭제여부 
    private boolean isDeleted;
    // 계층 확인을 위한 depth
    private int depth;
    
    // 추가된 필드: 순번 가상컬럼
    private int rowNum;

    
    // Getters and Setters
    public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public int getDepth() {
	    return depth;
	}
	public void setDepth(int depth) {
	    this.depth = depth;
	}

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }
}

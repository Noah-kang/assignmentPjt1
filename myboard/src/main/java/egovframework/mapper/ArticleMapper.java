package egovframework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.vo.ArticleVO;
import egovframework.vo.AttachFileVO;

public interface ArticleMapper {

    // 총 게시글 수 조회 (검색 조건 포함)
    int countArticlesWithSearch(@Param("searchType") String searchType, @Param("keyword") String keyword);
    // 페이징된 게시글 조회 (검색 조건 포함)
    List<ArticleVO> getPagedArticleHierarchy(@Param("offset") int offset, 
                                             @Param("size") int size, 
                                             @Param("searchType") String searchType, 
                                             @Param("keyword") String keyword);
    // 게시글 추가
    void insertArticle(ArticleVO article);
    // 게시글 답글 추가
    void insertReply(ArticleVO article);
    // 게시글 상세 조회
    ArticleVO selectArticleById(int articleId);
    // 게시글 수정
    void updateArticle(ArticleVO article);
    // 게시글 삭제 (논리 삭제)
    void deleteArticle(int articleId);
    // 조회수 증가
    void incrementViewCount(int articleId);
    // 비밀번호 검증 메서드
    String checkPassword(int articleId);
    // 첨부파일 저장
    void insertAttachFile(AttachFileVO attachFile);
    // 다중파일 가져오기
    List<AttachFileVO> getAttachFilesByArticleId(int articleId);
    // 파일 다운로드
    AttachFileVO selectAttachFileById(int fileId);
    // 파일 삭제
    void deleteAttachFile(int fileId);
    // 원글과 답글의 id 가져오기
    List<Integer> selectArticleAndRepliesIds(int articleId);
}

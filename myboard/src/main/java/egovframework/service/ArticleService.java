package egovframework.service;

import java.util.List;
import java.util.Map;

import egovframework.vo.ArticleVO;
import egovframework.vo.AttachFileVO;

public interface ArticleService {

    //게시글 목록 조회(계층형 + 검색 + 페이징)
    Map<String, Object> getPagedArticleHierarchy(int page, int size, String searchType, String keyword);
    //게시글 상세 조회
    ArticleVO getArticleById(int articleId);
    //조회수 증가
    void incrementViewCount(int articleId);
    //게시글 작성(답글포함)
    void addArticle(ArticleVO article);
    //비밀번호 확인
    boolean validatePassword(int articleId, String inputPassword);
    //게시글 삭제 처리
    void removeArticle(int articleId);
    //게시글 수정 처리
    void updateArticle(ArticleVO article);
    //첨부파일 저장
    void saveAttachFile(AttachFileVO attachFile);
    //다중파일 가져오기
    public List<AttachFileVO> getAttachFilesByArticleId(int articleId);
    //파일 다운로드
    AttachFileVO getAttachFileById(int fileId);
    //파일 삭제하기
    void deleteAttachFile(int fileId);
}

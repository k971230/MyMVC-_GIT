package myshop.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import myshop.domain.CategoryVO;
import myshop.domain.ImageVO;
import myshop.domain.ProductVO;
import myshop.domain.SpecVO;

public interface ProductDAO {

	// 메인페이지에 보여지는 상품이미지파일명을 모두 조회(select)하는 메소드
	List<ImageVO> imageSelectAll() throws SQLException;

	// 제품의 스펙별(HIT, NEW, BEST) 상품의 전체개수를 알아오기.
	int totalPspecCount(String fk_snum) throws SQLException;

	// 더보기 방식(페이징처리)으로 상품정보를 8개씩 잘라(start ~ end) 조회해오기
	List<ProductVO> selectBySpecName(Map<String, String> paraMap) throws SQLException;
	
	// tbl_category 테이블에서 카테고리 대분류 번호(cnum), 카테고리코드(code), 카테고리명(cname)을 조회해오기 
	List<CategoryVO> getCategoryList() throws SQLException;

	//cnum(카테고리번호)이 tbl_category 테이블에 존재하는지 존재하지 않는지 알아오기
	boolean isExist_cnum(String cnum) throws SQLException;
	
	// *** 페이징 처리를 위한 특정 카테고리에 대한 총페이지수 알아오기 //
	int getTotalPage(String cnum) throws SQLException;

	// **** ==== 특정한 카테고리에서 사용자가 보고자 하는 특정 페이지번호에 해당하는 제품들 조회해오기 *** //
	List<ProductVO> selectProductByCategory(Map<String, String> paraMap) throws SQLException;
	
	// 카테고리 목록을 조회해오기
	List<CategoryVO> selectCategoryList() throws SQLException;
	
	// SPEC 목록을 조회해오기
	List<SpecVO> selectSpecList() throws SQLException;

	// 제품번호 채번 해오기
	int getPnumOfProduct() throws SQLException;

	// tbl_product 테이블에 제품정보 insert 하기
	int productInsert(ProductVO pvo) throws SQLException;

	// tbl_product_imagefile 테이블에 제품의 추가이미지 파일명 insert 하기
	int product_imagefile_insert(Map<String, String> paraMap) throws SQLException;

	// 제품번호를 가지고서 해당 제품의 정보를 조회해오기
	ProductVO selectOneProductByPnum(String pnum) throws SQLException;
	
	// 제품번호를 가지고서 해당 제품의 추가된 이미지 정보 조회해오기
	List<String> getImagesByPnum(String pnum) throws SQLException;
	
}

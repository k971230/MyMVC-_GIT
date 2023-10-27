package myshop.model;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.sql.DataSource;

import myshop.domain.*;

public class ProductDAO_imple implements ProductDAO {

	private DataSource ds; // DataSource ds 는 아파치톰캣이 제공하는 DBCP(DB Connection Pool)이다.  
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	// 생성자
	public ProductDAO_imple() {
		
		try {
			Context initContext = new InitialContext();
		    Context envContext  = (Context)initContext.lookup("java:/comp/env");
		    ds = (DataSource)envContext.lookup("jdbc/myoracle");
		    
		} catch(NamingException e) {
			e.printStackTrace();
		}
		
	}
	
	
	// 사용한 자원을 반납하는 close() 메소드 생성하기 
	private void close() {
		try {
			if(rs != null)    {rs.close();    rs=null;}
			if(pstmt != null) {pstmt.close(); pstmt=null;}
			if(conn != null)  {conn.close();  conn=null;}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// 메인페이지에 보여지는 상품이미지파일명을 모두 조회(select)하는 메소드 
	@Override
	public List<ImageVO> imageSelectAll() throws SQLException {
		
		List<ImageVO> imgList = new ArrayList<>();
		 
		try {
			conn = ds.getConnection();
			
			String sql = " select imgno, imgfilename "
					   + " from tbl_main_image "
					   + " order by imgno asc ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				ImageVO imgvo = new ImageVO();
				imgvo.setImgno(rs.getInt(1));
				imgvo.setImgfilename(rs.getString(2));
				
				imgList.add(imgvo);
			}// end of while(rs.next())-----------------
			
		} finally {
			close();
		}
		
		return imgList;
	}// end of public List<ImageVO> imageSelectAll() throws SQLException------

	
	
	// 제품의 스펙별(HIT, NEW, BEST) 상품의 전체개수를 알아오기.
	@Override
	public int totalPspecCount(String fk_snum) throws SQLException {
		
		int totalCount = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select count(*) "
					   + " from tbl_product "
					   + " where fk_snum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fk_snum);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			
			totalCount = rs.getInt(1);
			
		} finally {
			close();
		}
		
		return totalCount;
		
	} // end of public int totalPspecCount(String string) throws SQLException ------


	
	// 더보기 방식(페이징처리)으로 상품정보를 8개씩 잘라(start ~ end) 조회해오기
	@Override
	public List<ProductVO> selectBySpecName(Map<String, String> paraMap) throws SQLException {
		
		List<ProductVO> productList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT pnum, pname, cname, pcompany, pimage1, pimage2, pqty, price, saleprice, sname, pcontent, point, pinputdate "
					   + " FROM "
					   + " ( "
					   + "    select row_number() over(order by pnum desc) AS RNO "
					   + "         , pnum, pname, C.cname, pcompany, pimage1, pimage2, pqty, price, saleprice, S.sname, pcontent, point "
					   + "         , to_char(pinputdate, 'yyyy-mm-dd') AS pinputdate "
					   + "    from tbl_product P "
					   + "    JOIN tbl_category C "
					   + "    ON P.fk_cnum = C.cnum "
					   + "    JOIN tbl_spec S "
					   + "    ON P.fk_snum = S.snum "
					   + "    where S.sname = ? "
					   + " ) V "
					   + " WHERE RNO between ? and ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("sname"));
			pstmt.setString(2, paraMap.get("start"));
			pstmt.setString(3, paraMap.get("end"));
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				ProductVO pvo = new ProductVO();
				
				pvo.setPnum(rs.getInt(1));           // 제품번호 
				pvo.setPname(rs.getString(2));	     // 제품명
				
				CategoryVO categvo = new CategoryVO();
				categvo.setCname(rs.getString(3));   // 카테고리명
				pvo.setCategvo(categvo);
				
				pvo.setPcompany(rs.getString(4));    // 제조회사명
	            pvo.setPimage1(rs.getString(5));     // 제품이미지1   이미지파일명
	            pvo.setPimage2(rs.getString(6));     // 제품이미지2   이미지파일명
	            pvo.setPqty(rs.getInt(7));           // 제품 재고량
	            pvo.setPrice(rs.getInt(8));          // 제품 정가
	            pvo.setSaleprice(rs.getInt(9));      // 제품 판매가(할인해서 팔 것이므로)
	            
	            SpecVO spvo = new SpecVO();
	            spvo.setSname(rs.getString(10));     // 스펙명
	            pvo.setSpvo(spvo);
	            
	            pvo.setPcontent(rs.getString(11));   // 제품설명 
	            pvo.setPoint(rs.getInt(12));         // 포인트 점수        
	            pvo.setPinputdate(rs.getString(13)); // 제품입고일자  
	            
	            productList.add(pvo);
			} // end of while() --------------------------------------------
			
		} finally {
			close();
		}
		
		return productList;
	} // end of public List<ProductVO> selectBySpecName(Map<String, String> paraMap)

	// tbl_category 테이블에서 카테고리 대분류 번호(cnum), 카테고리코드(code), 카테고리명(cname)을 조회해오기 
	@Override
	public List<CategoryVO> getCategoryList() throws SQLException {


		      List<CategoryVO> categoryList = new ArrayList<>(); 
		      
		      try {
		          conn = ds.getConnection();
		          
		          String sql = " select cnum, code, cname "  
		                    + " from tbl_category "
		                    + " order by cnum asc ";
		                    
		         pstmt = conn.prepareStatement(sql);
		               
		         rs = pstmt.executeQuery();
		                  
		         while(rs.next()) {
		            CategoryVO cvo = new CategoryVO();
		            cvo.setCnum(rs.getInt(1));
		            cvo.setCode(rs.getString(2));
		            cvo.setCname(rs.getString(3));
		            
		            categoryList.add(cvo);
		         }// end of while(rs.next())----------------------------------
		         
		      } finally {
		         close();
		      }   
		      
		      return categoryList;
		      
		   }// end of public List<CategoryVO> getCategoryList() throws SQLException-------------


	@Override
	public boolean isExist_cnum(String cnum) throws SQLException {
		
		boolean isExist = false;
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql = " select * "  
	                  + " from tbl_category "
	                  + " where cnum = ? "; 
	         
	         pstmt = conn.prepareStatement(sql);
	         
	         pstmt.setString(1, cnum);
	               
	         rs = pstmt.executeQuery();
	         
	         isExist = rs.next();
	         
	      } finally {
	         close();
	      }      
	      
	      return isExist;
	}

	// *** 페이징 처리를 위한 특정 카테고리에 대한 총페이지수 알아오기 //
	@Override
	public int getTotalPage(String cnum) throws SQLException {
		
		int totalPage = 0;
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql =  " select ceil(count(*) / 10) " // 10 이 sizePerPage 이다.
	                  + " from tbl_product "
	                  + " where fk_cnum = ? ";
	         
	         
	         
	         
	         // / ? 페이지 사이즈 한페이지당 몇개인지
	         
	         
	         pstmt = conn.prepareStatement(sql); 
	         
	         pstmt.setString(1, cnum); 
	         
	         rs = pstmt.executeQuery();
	         
	         rs.next();
	         
	         totalPage = rs.getInt(1);
	        
	      }  finally {
	         
	    	  	close();
	      }
	      
	      return totalPage;      

		
		
		
	}

	// **** ==== 특정한 카테고리에서 사용자가 보고자 하는 특정 페이지번호에 해당하는 제품들 조회해오기 *** //
	@Override
	public List<ProductVO> selectProductByCategory(Map<String, String> paraMap) throws SQLException {
		
		List<ProductVO> productList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " SELECT cname, sname, pnum, pname, pcompany, pimage1, pimage2, pqty, price, saleprice, pcontent, point, pinputdate "
					+ " FROM "
					+ " ( "
					+ "    SELECT row_number() over(order by P.pnum desc) AS RNO\r\n"
					+ "         , C.cname, S.sname, P.pnum, P.pname, P.pcompany, P.pimage1, P.pimage2, P.pqty, P.price, P.saleprice, P.pcontent, P.point, P.pinputdate "
					+ "    FROM "
					+ "    ( "
					+ "    select pnum, pname, pcompany, pimage1, pimage2, pqty, price, saleprice, pcontent, point "
					+ "         , to_char(pinputdate, 'yyyy-mm-dd') AS pinputdate, fk_cnum, fk_snum "
					+ "    from tbl_product "
					+ "    where fk_cnum = ? "
					+ "    ) P "
					+ "    JOIN tbl_category C "
					+ "    ON P.fk_cnum = C.cnum "
					+ "    JOIN tbl_spec S "
					+ "    ON P.fk_snum = S.snum "
					+ " ) V "
					+ " WHERE V.RNO between ? and ? ";
			
			pstmt = conn.prepareStatement(sql);
			
		    // === 페이징처리의 공식 ===
    	    // where RNO between (조회하고자하는페이지번호 * 한페이지당보여줄행의개수) - (한페이지당보여줄행의개수 - 1) and (조회하고자하는페이지번호 * 한페이지당보여줄행의개수);

			int currentShowPageNo = Integer.parseInt(paraMap.get("currentShowPageNo"));
			int sizePerPage = 10; //  한 페이지당 화면상에 보여줄 제품의 개수는 10으로 한다.
			
			
			pstmt.setString(1, paraMap.get("cnum"));
			pstmt.setInt(2, (currentShowPageNo * sizePerPage) - (sizePerPage - 1)); // 공식이다.
			pstmt.setInt(3, (currentShowPageNo * sizePerPage)); // 공식이다.
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				ProductVO pvo = new ProductVO();
				
				pvo.setPnum(rs.getInt("pnum"));           // 제품번호 
				pvo.setPname(rs.getString("pname"));	     // 제품명
				
				CategoryVO categvo = new CategoryVO();
				categvo.setCname(rs.getString("cname"));   // 카테고리명
				pvo.setCategvo(categvo);
				
				pvo.setPcompany(rs.getString("pcompany"));    // 제조회사명
	            pvo.setPimage1(rs.getString("pimage1"));     // 제품이미지1   이미지파일명
	            pvo.setPimage2(rs.getString("pimage2"));     // 제품이미지2   이미지파일명
	            pvo.setPqty(rs.getInt("pqty"));           // 제품 재고량
	            pvo.setPrice(rs.getInt("price"));          // 제품 정가
	            pvo.setSaleprice(rs.getInt("saleprice"));      // 제품 판매가(할인해서 팔 것이므로)
	            
	            SpecVO spvo = new SpecVO();
	            spvo.setSname(rs.getString("sname"));     // 스펙명
	            pvo.setSpvo(spvo);
	            
	            pvo.setPcontent(rs.getString("pcontent"));   // 제품설명 
	            pvo.setPoint(rs.getInt("point"));         // 포인트 점수        
	            pvo.setPinputdate(rs.getString("pinputdate")); // 제품입고일자  
	            
	            productList.add(pvo);
			} // end of while() --------------------------------------------
			
		} finally {
			close();
		}
		
		return productList;
	} // end of public List<ProductVO> selectProductByCategory(Map<String, String> paraMap) throws SQLException 

	// 카테고리 목록을 조회해오기
	@Override
	public List<CategoryVO> selectCategoryList() throws SQLException {
		
		List<CategoryVO> categoryList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " select cnum, code, cname "
					   + " from tbl_category "
					   + " order by cnum asc ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				CategoryVO cvo = new CategoryVO();
				cvo.setCnum(rs.getInt(1));
				cvo.setCode(rs.getString(2));
				cvo.setCname(rs.getString(3));
			
				categoryList.add(cvo);
				
			} // end of while

			
		} finally {
			close();
		}
		
		return categoryList;
	} // public List<CategoryVO> selectCategoryList() throws SQLException 

	
	// SPEC 목록을 조회해오기
	@Override
	public List<SpecVO> selectSpecList() throws SQLException {
		
		List<SpecVO> specList = new ArrayList<>();
		
		try {
			conn = ds.getConnection();
			
			String sql = " select snum, sname "
					   + " from tbl_spec "
					   + " order by snum asc ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				SpecVO spvo = new SpecVO();
				spvo.setSnum(rs.getInt(1));
				spvo.setSname(rs.getString(2));

				specList.add(spvo);
				
			} // end of while -----
			
		} finally {
			close();
		}
		
		
		
		return specList;
	} // public List<SpecVO> selectSpecList() throws SQLException 


	
	// 제품번호 채번 해오기
	@Override
	public int getPnumOfProduct() throws SQLException {
		
		int pnum = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select seq_tbl_product_pnum.nextval AS PNUM "
					   + " from dual ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			rs.next();
			pnum = rs.getInt(1);
			
		} finally {
			close();
		}

		return pnum;
	}// end of public int getPnumOfProduct() throws SQLException------------------------------


	
	// tbl_product 테이블에 제품정보 insert 하기
	@Override
	public int productInsert(ProductVO pvo) throws SQLException {
		
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " insert into tbl_product(pnum, pname, fk_cnum, pcompany, pimage1, pimage2, prdmanual_systemFileName, prdmanual_orginFileName, pqty, price, saleprice, fk_snum, pcontent, point) "
					   + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?) "; 
					   
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, pvo.getPnum());
	        pstmt.setString(2, pvo.getPname());
	        pstmt.setInt(3, pvo.getFk_cnum());    
	        pstmt.setString(4, pvo.getPcompany()); 
	        pstmt.setString(5, pvo.getPimage1());    
	        pstmt.setString(6, pvo.getPimage2()); 
	        pstmt.setString(7, pvo.getPrdmanual_systemFileName());
	        pstmt.setString(8, pvo.getPrdmanual_orginFileName());
	        pstmt.setInt(9, pvo.getPqty()); 
	        pstmt.setInt(10, pvo.getPrice());
	        pstmt.setInt(11, pvo.getSaleprice());
	        pstmt.setInt(12, pvo.getFk_snum());
	        pstmt.setString(13, pvo.getPcontent());
	        pstmt.setInt(14, pvo.getPoint());
			
	        result = pstmt.executeUpdate();
			
		} finally {
			close();
		}

		return result;
	}// end of public int productInsert(ProductVO pvo) throws SQLException---------------


	
	// tbl_product_imagefile 테이블에 제품의 추가이미지 파일명 insert 하기
	@Override
	public int product_imagefile_insert(Map<String, String> paraMap) throws SQLException {
		
		int result = 0;
		
		try {
			conn = ds.getConnection();
			
			String sql = " insert into tbl_product_imagefile(imgfileno, fk_pnum, imgfilename) "
					   + " values(seqImgfileno.nextval, ?, ?) "; 
					   
			
			pstmt = conn.prepareStatement(sql);
			
	        pstmt.setInt(1, Integer.parseInt(paraMap.get("pnum")));
	        pstmt.setString(2, paraMap.get("attachFileName"));    
	        
			
	        result = pstmt.executeUpdate();
			
		} finally {
			close();
		}

		return result;
	}// end of public int product_imagefile_insert(Map<String, String> paraMap) throws SQLException-----------


	
	// 제품번호를 가지고 해당 제품의 정보를 조회해오기
	   @Override
	   public ProductVO selectOneProductByPnum(String pnum) throws SQLException {
	      
	      ProductVO pvo = null;
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql = " SELECT S.sname, pnum, pname, pcompany, price, saleprice, point, pqty, pcontent, pimage1, pimage2, prdmanual_systemFileName, NVL(prdmanual_orginFileName, '업슴') AS prdmanual_orginFileName "
	                  + " FROM "
	                  + " ( "
	                  + " select fk_snum, pnum, pname, pcompany, price, saleprice, point, pqty, pcontent, pimage1, pimage2, prdmanual_systemFileName, prdmanual_orginFileName "
	                  + " from tbl_product "
	                  + " where pnum = ? "
	                  + " ) P "
	                  + " JOIN tbl_spec S "
	                  + " ON P.fk_snum = S.snum ";
	         
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, pnum);
	         
	         rs = pstmt.executeQuery();
	         
	         if(rs.next()) {
	            
	            pvo = new ProductVO();
	            
	            pvo.setPnum(rs.getInt("PNUM"));              // 제품번호 
	            pvo.setPname(rs.getString("PNAME"));       // 제품명
	            pvo.setPcompany(rs.getString("PCOMPANY"));  // 제조회사명
	               pvo.setPimage1(rs.getString("PIMAGE1"));    // 제품이미지1   이미지파일명
	               pvo.setPimage2(rs.getString("PIMAGE2"));    // 제품이미지2   이미지파일명
	               pvo.setPqty(rs.getInt("PQTY"));              // 제품 재고량
	               pvo.setPrice(rs.getInt("PRICE"));             // 제품 정가
	               pvo.setSaleprice(rs.getInt("SALEPRICE"));   // 제품 판매가(할인해서 팔 것이므로)
	               
	               SpecVO spvo = new SpecVO();
	               spvo.setSname(rs.getString("SNAME"));        // 스펙명
	               pvo.setSpvo(spvo);
	               
	               pvo.setPcontent(rs.getString("PCONTENT"));  // 제품설명 
	               pvo.setPoint(rs.getInt("POINT"));            // 포인트 점수        
	               
	               pvo.setPrdmanual_systemFileName(rs.getString("PRDMANUAL_SYSTEMFILENAME"));   // 파일서버에 업로드되어지는 실제 제품설명서 파일명
	               pvo.setPrdmanual_orginFileName(rs.getString("PRDMANUAL_ORGINFILENAME"));   // 웹클라이언트의 웹브라우저에서 파일을 업로드 할때 올리는 제품설명서 파일명
	               
	         } // end of if() --------------------------------------------
	         
	      } finally {
	         close();
	      }
	      
	      return pvo;
	      
	   } // end of public ProductVO selectOneProductByPnum(String pnum) throws SQLException ---

	// 제품번호를 가지고서 해당 제품의 추가된 이미지 정보 조회해오기
	@Override
	public List<String> getImagesByPnum(String pnum) throws SQLException {
		
		List<String> imgList = new ArrayList<>();
	      
	      try {
	         conn = ds.getConnection();
	         
	         String sql = " select imgfilename "+
	                     " from tbl_product_imagefile "+
	                     " where fk_pnum = ? ";
	         
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, pnum);
	         
	         rs = pstmt.executeQuery();
	         
	         while(rs.next()) {
	            String imgfilename = rs.getString(1); // 이미지파일명 
	            imgList.add(imgfilename); 
	         }
	         
	      } finally {
	         close();
	      }
	      
	      return imgList;
	      
	} // end of public List<String> getImagesByPnum(String pnum) throws SQLException 
	
}



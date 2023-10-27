package myshop.controller;

import java.util.*;

import myshop.domain.CategoryVO;
import myshop.model.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import common.controller.AbstractController;

public class CategoryListJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ProductDAO pdao = new ProductDAO_imple();
	      
	    List<CategoryVO> categoryList = pdao.getCategoryList();
	      
	    JSONArray jsonArr = new JSONArray(); // []
	      
	    if( categoryList.size() > 0 ) {
	       // DB에서 조회해온 결과물이 있을 경우
	      
	       for(CategoryVO cvo : categoryList) {
	            
	          JSONObject jsonObj = new JSONObject(); 
	          
	          jsonObj.put("cnum",  cvo.getCnum()); 
	          jsonObj.put("code", cvo.getCode());  
	          jsonObj.put("cname", cvo.getCname());   
	            
	          jsonArr.put(jsonObj); 
	       }// end of for------------------
	         
	    }// end of if----------------------------
	      
	    String json = jsonArr.toString(); // 문자열로 변환 
	      
	 // System.out.println("~~~ 확인용 json => " + json);
	    /* ~~~ 확인용 json => [{"code":"100000","cname":"전자제품","cnum":1}
	                         ,{"code":"200000","cname":"의류","cnum":2}
	                         ,{"code":"300000","cname":"도서","cnum":3}]
	    */
	      
	    request.setAttribute("json", json);
	      
	 // super.setRedirect(false);
	    super.setViewPage("/WEB-INF/jsonview.jsp");   

	}

}

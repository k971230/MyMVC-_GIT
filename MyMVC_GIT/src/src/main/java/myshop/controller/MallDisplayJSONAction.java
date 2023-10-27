package myshop.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import common.controller.AbstractController;
import myshop.domain.ProductVO;
import myshop.model.*;

public class MallDisplayJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// js 안의 json 데이터들을 파라미터로 받아온 것
		String sname = request.getParameter("sname"); // "HIT"   "MEW"   "BEST" 
		String start = request.getParameter("start");
		String len = request.getParameter("len");
		/*
        맨 처음에는 sname("HIT")상품을  start("1") 부터 len("8")개를 보여준다.
        더보기... 버튼을 클릭하면  sname("HIT")상품을  start("9") 부터 len("8")개를 보여준다.
        또  더보기... 버튼을 클릭하면  sname("HIT")상품을  start("17") 부터 len("8")개를 보여준다.      
		*/
		
		ProductDAO pdao = new ProductDAO_imple();
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("sname", sname); // "HIT"   "MEW"   "BEST" 
		paraMap.put("start", start); // start    "1"   "9"   "17"    "24"
		
		String end = String.valueOf(Integer.parseInt(start) + Integer.parseInt(len) - 1);
		paraMap.put("end", end); // end => start + len - 1    
								 // end     "8"   "16"   "24"   "32"   "40"
		
		List<ProductVO> productList = pdao.selectBySpecName(paraMap);
		
		JSONArray jsonArr = new JSONArray(); // [] 이모양임.
		
		if( productList.size() > 0) {
			// DB에서 조회해온 결과물이 있을 경우
			
			for(ProductVO pvo : productList) { // productList 사이즈 만큼 반복
				
				JSONObject jsonObj = new JSONObject(); // {}
				// pnum, pname, cname, pcompany, pimage1, pimage2, pqty, price, saleprice, sname, pcontent, point, pinputdate
				jsonObj.put("pnum", pvo.getPnum());   // {"pnum":1}
				jsonObj.put("pname", pvo.getPname()); // {"pnum":1, "pname":"스마트TV"}
				jsonObj.put("cname", pvo.getCategvo().getCname()); // {"pnum":1, "pname":"스마트TV", "cname":"전자제품"}
				jsonObj.put("pcompany", pvo.getPcompany());
				jsonObj.put("pimage1", pvo.getPimage1());
				jsonObj.put("pimage2", pvo.getPimage2());
				jsonObj.put("pqty", pvo.getPqty());
				jsonObj.put("price", pvo.getPrice());
				jsonObj.put("saleprice", pvo.getSaleprice());
				jsonObj.put("sname", pvo.getSpvo().getSname());
				jsonObj.put("pcontent", pvo.getPcontent());
				jsonObj.put("point", pvo.getPoint());
				jsonObj.put("pinputdate", pvo.getPinputdate());
				
				jsonObj.put("discountPercent", pvo.getDiscountPercent()); // productVO 에 이미 함수를 만들어둠.
				
				// jsonObj ==> {"pnum":1, "pname":"스마트TV", "cname":"전자제품", .... ,"pinputdate":"2023-10-19","discountPercent":15}
				
				jsonArr.put(jsonObj); // [ {"pnum":1, "pname":"스마트TV", "cname":"전자제품", .... , "discountPercent":15} ] 
				
			} // end of for ------------------------
			
		} // end of if ---------------------------------------------------------
		
		String json = jsonArr.toString(); // 문자열로 변환
		
	//	System.out.println("확인용 json => " + json);
		/*
		 확인용 json => [{"pnum":4,"pname":"남방","pcompany":"버카루","cname":"의류","saleprice":13000,"point":10,"pinputdate":"2023-10-19","pimage1":"cloth_buckaroo_1.png","pqty":50,"pimage2":"cloth_buckaroo_2.png","pcontent":"멋져요!!","price":15000,"sname":"HIT","discountPercent":15}
		              ,{"pnum":3,"pname":"바지","pcompany":"S사","cname":"의류","saleprice":10000,"point":5,"pinputdate":"2023-10-19","pimage1":"cloth_canmart_1.png","pqty":20,"pimage2":"cloth_canmart_2.png","pcontent":"예뻐요!!","price":12000,"sname":"HIT","discountPercent":15}
		              ,{"pnum":2,"pname":"노트북","pcompany":"엘지","cname":"전자제품","saleprice":750000,"point":30,"pinputdate":"2023-10-19","pimage1":"notebook_lg_gt50k_1.png","pqty":150,"pimage2":"notebook_lg_gt50k_2.png","pcontent":"노트북. 기능 짱!!","price":900000,"sname":"HIT","discountPercent":15}
		              ,{"pnum":1,"pname":"스마트TV","pcompany":"삼성","cname":"전자제품","saleprice":800000,"point":50,"pinputdate":"2023-10-19","pimage1":"tv_samsung_h450_1.png","pqty":100,"pimage2":"tv_samsung_h450_2.png","pcontent":"42인치 스마트 TV. 기능 짱!!","price":1200000,"sname":"HIT","discountPercent":15}]
		              
		 확인용 json => [{"pnum":12,"pname":"노트북6","pcompany":"HP","cname":"전자제품","saleprice":1000000,"point":60,"pinputdate":"2023-10-19","pimage1":"11.jpg","pqty":100,"pimage2":"12.jpg","pcontent":"6번 노트북","price":1200000,"sname":"HIT","discountPercent":15}
		 			  ,{"pnum":11,"pname":"노트북5","pcompany":"삼성전자","cname":"전자제품","saleprice":1000000,"point":60,"pinputdate":"2023-10-19","pimage1":"9.jpg","pqty":100,"pimage2":"10.jpg","pcontent":"5번 노트북","price":1200000,"sname":"HIT","discountPercent":15}
		 			  ,{"pnum":10,"pname":"노트북4","pcompany":"레노버","cname":"전자제품","saleprice":1000000,"point":60,"pinputdate":"2023-10-19","pimage1":"7.jpg","pqty":100,"pimage2":"8.jpg","pcontent":"4번 노트북","price":1200000,"sname":"HIT","discountPercent":15}
		 			  ,{"pnum":9,"pname":"노트북3","pcompany":"LG전자","cname":"전자제품","saleprice":1000000,"point":60,"pinputdate":"2023-10-19","pimage1":"5.jpg","pqty":100,"pimage2":"6.jpg","pcontent":"3번 노트북","price":1200000,"sname":"HIT","discountPercent":15}
		 			  ,{"pnum":8,"pname":"노트북2","pcompany":"에이서","cname":"전자제품","saleprice":1000000,"point":60,"pinputdate":"2023-10-19","pimage1":"3.jpg","pqty":100,"pimage2":"4.jpg","pcontent":"2번 노트북","price":1200000,"sname":"HIT","discountPercent":15}
		 			  ,{"pnum":7,"pname":"노트북1","pcompany":"DELL","cname":"전자제품","saleprice":1000000,"point":60,"pinputdate":"2023-10-19","pimage1":"1.jpg","pqty":100,"pimage2":"2.jpg","pcontent":"1번 노트북","price":1200000,"sname":"HIT","discountPercent":15}
		 			  ,{"pnum":6,"pname":"만화한국사","pcompany":"녹색지팡이","cname":"도서","saleprice":120000,"point":60,"pinputdate":"2023-10-19","pimage1":"book_koreahistory_1.png","pqty":80,"pimage2":"book_koreahistory_2.png","pcontent":"만화로 보는 이야기 한국사 전집","price":130000,"sname":"HIT","discountPercent":15}
		 			  ,{"pnum":5,"pname":"보물찾기시리즈","pcompany":"아이세움","cname":"도서","saleprice":33000,"point":20,"pinputdate":"2023-10-19","pimage1":"book_bomul_1.png","pqty":100,"pimage2":"book_bomul_2.png","pcontent":"만화로 보는 세계여행","price":35000,"sname":"HIT","discountPercent":15}]
		 			  
		 확인용 json => []
		*/
		
		request.setAttribute("json", json);
		
	//	super.setRedirect(false);
		super.setViewPage("/WEB-INF/jsonview.jsp");
		
	} // end of public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception

}

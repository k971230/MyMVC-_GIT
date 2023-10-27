package my.util;

import javax.servlet.http.HttpServletRequest;

public class MyUtil {
	
	// *** ? 다음의 데이터까지 포함한 현재 URL 주소를 알려주는 메소드를 생성 *** //
	public static String getCurrentURL(HttpServletRequest request) {
		
		String currentURL = request.getRequestURL().toString();
	 // 확인용  currentURL => http://localhost:9090/MyMVC/member/memberList.up
		
		String queryString = request.getQueryString();
	 //	System.out.println("확인용 queryString => " + queryString);
	 // 확인용 queryString => searchType=name&searchWord=%EB%AF%BC%EC%A4%80&sizePerPage=5	
	 // queryString => null (POST 방식일 경우)
		
		if(queryString != null) { // "GET" 방식일 경우
			currentURL += "?" + queryString;
		 // currentURL => http://localhost:9090/MyMVC/member/memberList.up?searchType=name&searchWord=%EB%AF%BC%EC%A4%80&sizePerPage=5
			
		}
		
		String ctxPath = request.getContextPath();
		//     /MyMVC
		
		int beginIndex = currentURL.indexOf(ctxPath) + ctxPath.length();
		//     27      =          21                 +        6
		
		currentURL = currentURL.substring(beginIndex);
		// /member/memberList.up?searchType=name&searchWord=%EB%AF%BC%EC%A4%80&sizePerPage=5
		
		return currentURL;
	} // end of public static String getCurrentURL(HttpServletRequest request) 
	
}

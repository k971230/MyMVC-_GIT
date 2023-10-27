package member.controller;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.domain.MemberVO;
import member.model.*;

public class CoinUpdateLoginUserAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String method = request.getMethod(); // "GET" 또는 "POST"
		
		if("POST".equalsIgnoreCase(method)) {
			// POST 방식이라면
			
			String userid = request.getParameter("userid");
			String coinmoney = request.getParameter("coinmoney");
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", userid);
			paraMap.put("coinmoney", coinmoney);
			
			MemberDAO mdao = new MemberDAO_imple();
			
			String message = "";
			String loc = "";
			
			try {
				int n = mdao.coinUpdateLoginUser(paraMap); // DB에 코인 및 포인트 증가하기 
				
				if(n==1) {
					
					HttpSession session = request.getSession();
					MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
					
					// !!!!! 세션값을 변경하기 !!!!! //
					loginuser.setCoin( loginuser.getCoin() + Integer.parseInt(coinmoney) );
					loginuser.setPoint( loginuser.getPoint() + (int) (Integer.parseInt(coinmoney)*0.01) );
					
					
					DecimalFormat df = new DecimalFormat("#,###");
				//  예를 들면
				//	System.out.println(df.format(2005100));
					//  "2,005,100" 
					
					message = loginuser.getName() + "님의 "+ df.format(Long.parseLong(coinmoney)) +"원 결제가 완료되었습니다.";
					loc = request.getContextPath()+"/index.up";
				}
				
			} catch(SQLException e) {
				message = "코인액 결제가 DB오류로 인해 실패되었습니다.";
				loc = "javascript:history.back()";
			}
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
		//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
		}
		else {
			// POST 방식이 아니라면
			
			String message = "비정상적인 경로로 들어왔습니다.";
			String loc = "javascript:history.back()";
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
		//	super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
		
		
	}

}

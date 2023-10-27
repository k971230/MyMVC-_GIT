package member.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.domain.MemberVO;
import member.model.MemberDAO;
import member.model.MemberDAO_imple;

public class MemberEditEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod(); // "GET" 또는 "POST" 
		
		if("POST".equalsIgnoreCase(method)) {
           // **** POST 방식으로 넘어온 것이라면 **** //
		
		   String userid = request.getParameter("userid");	
		   String name = request.getParameter("name");
		   String pwd = request.getParameter("pwd");
		   String email = request.getParameter("email");
    	   String hp1 = request.getParameter("hp1");
		   String hp2 = request.getParameter("hp2");
		   String hp3 = request.getParameter("hp3");
 		   String postcode = request.getParameter("postcode");
		   String address = request.getParameter("address");
		   String detailaddress = request.getParameter("detailaddress");
		   String extraaddress = request.getParameter("extraaddress");
			
		   String mobile = hp1+hp2+hp3;
			
		   MemberVO member = new MemberVO(userid, pwd, name, email, mobile, postcode, address, detailaddress, extraaddress);  
			
		   MemberDAO mdao = new MemberDAO_imple();
		
		   // === 회원수정이 성공되어지면 "회원정보 수정 성공!!" 이라는 alert 를 띄우고 시작페이지로 이동한다. === //
		
			String message = "";
			String loc = "";
			
			try {
				int n = mdao.updateMember(member);
				
				if(n==1) {
					
					// !!!! session 에 저장된 loginuser 를 변경된 사용자의 정보값으로 변경해주어야 한다. !!!!
					HttpSession session = request.getSession();
					MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
					
					loginuser.setName(name);
					loginuser.setPwd(pwd);
					loginuser.setEmail(email);
					loginuser.setMobile(mobile);
					loginuser.setPostcode(postcode);
					loginuser.setAddress(address);
					loginuser.setDetailaddress(detailaddress);
					loginuser.setExtraaddress(extraaddress);
					
					message = "회원정보 수정 성공!!";
					loc = request.getContextPath()+"/index.up"; // 시작페이지로 이동한다.
				}
			} catch(SQLException e) {
				message = "SQL구문 에러발생";
				loc = "javascript:history.back()"; // 자바스크립트를 이용한 이전페이지로 이동하는 것. 
				e.printStackTrace();
			}
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
		//	super.setRedirect(false); 
			super.setViewPage("/WEB-INF/msg.jsp");
		}		
		
	}

}

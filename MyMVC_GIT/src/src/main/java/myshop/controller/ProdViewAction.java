package myshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import myshop.domain.*;
import myshop.model.*;

public class ProdViewAction extends AbstractController {

   @Override
   public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
      
      String pnum = request.getParameter("pnum");

      ProductDAO pdao = new ProductDAO_imple();
      
      // 제품번호를 가지고 해당 제품의 정보를 조회해오기
      ProductVO pvo = pdao.selectOneProductByPnum(pnum);
      
      // 제품번호를 가지고 해당 제품의 추가된 이미지 정보를 조회해오기
      List<String> imgList = pdao.getImagesByPnum(pnum);
      
      if(pvo == null) {
         // GET 방식이므로 사용자가 웹브라우저 주소창에서 장난쳐서 존재하지 않는 제품번호를 입력한 경우
           String message = "검색하신 제품은 존재하지 않습니다.";
           String loc = "javascript:history.back()";
            
           request.setAttribute("message", message);
           request.setAttribute("loc", loc);
            
           // super.setRedirect(false);
           super.setViewPage("/WEB-INF/msg.jsp");
            
           return;
      } 
      else {
         // 제품이 존재하는 경우
         request.setAttribute("pvo", pvo); // 제품의 정보
         request.setAttribute("imgList", imgList); // 해당 제품의 추가된 이미지 정보
         
         // super.setRedirect(false);
           super.setViewPage("/WEB-INF/myshop/prodView.jsp");
      }
      
   } // end of public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception 

}
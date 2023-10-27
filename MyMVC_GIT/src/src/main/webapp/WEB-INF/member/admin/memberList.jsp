<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<%
   String ctxPath = request.getContextPath();
   //      /MyMVC
%>

<jsp:include page="../../header2.jsp" />


<style type="text/css">

   table#memberTbl {
      width:80%;
      margin: 0 auto;
   } 
   
   table#memberTbl th {
      text-align:center;
      font-size:14pt; 
   }
   
   table#memberTbl tr.memberInfo:hover {
      background-color:#e6ffe6;
      cursor:pointer;
   }
   
   form[name="member_search_frm"] {
      border:solid 0px red;
      width:80%;
      margin: 0 auto 3% auto;
   }
   
   form[name="member_search_frm"] button.btn-secondary {
      margin-left: 2%;
      margin-right: 30%;
   }
   
   div#pageBar {
      border: solid 0px red;
      width: 80%;
      margin: 3% auto 0 auto;
      display: flex;
   }
   
   div#pageBar > nav {
      margin: auto;
   }

</style>

<script type="text/javascript">
   $(document).ready(function(){
      
      if("${requestScope.searchType}" != "" && 
         "${requestScope.searchWord}" != ""){
         $("select[name='searchType']").val("${requestScope.searchType}");
         $("input[name='searchWord']").val("${requestScope.searchWord}")
      }
      
      if("${requestScope.sizePerPage}" != "") {
         $("select[name='sizePerPage']").val("${requestScope.sizePerPage}")
      }
      
      
      $("input[name='searchWord']").bind("keydown",function(e){
         
         if(e.keyCode == 13) {
            goSearch();   
         }
         
      });
   
      // **** select 태그에 대한 이벤트는 click이 아니라 change이다. **** //
      $("select[name='sizePerPage']").bind("change", function(){
         
         const frm = document.member_search_frm;
         //   frm.action = "memberList.up"; // form 태그에 action이 명기되지 않았으면 현재보이는 URL 경로로 submit되어진다.
         //   frm.method = "get"; // form태그에 method를 명기하지 않으면 'get'방식이다.
         frm.submit();
         
      }) // end of $("select[name='sizePerPage']").bind("change", function(){
      
      
	  // *** 특정 회원을 클릭하면 그 회원의 상세정보를 보여주도록 한다. *** //
	  $("table#memberTbl tr.memberInfo").click( e =>{
	  	  // alert($(e.target).parent().html());
	  	  // $(e.target).parent().find(".userid").text();
	  	  // 또는
	  	  const userid = $(e.target).parent().children(".userid").text();
	  	  // alert(userid);
	  	  
	  	  const frm = document.memberOneDetail_frm;
	  	  frm.userid.value = userid;
	  	  
	  	  frm.action = "<%= ctxPath%>/member/memberOneDetail.up";
	  	  frm.method = "post";
	  	  frm.submit();
	  	
	  }) // end of $("table#memberTbl tr.memberInfo").click( e =>{
	  
   }); // end of $(document).ready(function(){ ------
      
   function goSearch() {
      
      const searchType = $("select[name='searchType']").val();
      
      if(searchType == ""){
         alert("검색대상을 선택하세요!!");
         return; // goSearch() 함수를 종료한다.
      };
      
      const frm = document.member_search_frm;
   //   frm.action = "memberList.up"; // form 태그에 action이 명기되지 않았으면 현재보이는 URL 경로로 submit되어진다.
   //   frm.method = "get"; // form태그에 method를 명기하지 않으면 'get'방식이다.
      frm.submit();
   
   }// end of function goSearch() {
      
   
</script>



<div class="container" style="padding: 3% 0;">
   <h2 class="text-center mb-5">::: 회원전체 목록:::</h2>
   
   <form name="member_search_frm">
      <select name="searchType">
         <option value="" >검색대상</option>
         <option value="name" >회원명</option>
         <option value="userid" >아이디</option>
         <option value="email" >이메일</option>
      </select>
      &nbsp;&nbsp;
      <input type="text" name="searchWord" />
      <%--
             form 태그내에서 데이터를 전송해야 할 input 태그가 만약에 1개 밖에 없을 경우에는
             input 태그내에 값을 넣고나서 그냥 엔터를 해버리면 submit 되어져 버린다.
             그래서 유효성 검사를 거친후 submit 을 하고 싶어도 input 태그가 만약에 1개 밖에 없을 경우라면 
             유효성검사가 있더라도 유효성검사를 거치지 않고 바로 submit 되어진다. 
             이것을 막으려면 input 태그가 2개 이상 존재하면 된다.  
             그런데 실제 화면에 보여질 input 태그는 1개 이어야 한다.
             이럴 경우 아래와 같이 해주면 된다.
             
             ------------------------ 조심 -------------------
             !!!! ---- hidden은 안된다. ---- !!!!
             
             또한 form 태그에 action이 명기되지 않았으면 현재보이는 URL 경로로 submit되어진다.
        --%>
        <input type="text" style="display:none;"/>
        
      <button type="button" class="btn btn-secondary" onclick="goSearch()">검색</button>
      
      <span style="font-size:12pt; font-weight:bold;">페이지당 회원명수&nbsp;-&nbsp;</span>
      <select name="sizePerPage">
         <option value="10">10명</option>
         <option value="5">5명</option>
         <option value="3">3명</option>
      </select>
   </form>

   <table class="table table-bordered" id="memberTbl">
      <thead>
         <tr>
            <th>아이디</th>
            <th>회원명</th>
            <th>이메일</th>
            <th>성별</th>
         </tr>
      </thead>

      <tbody>
         <%-- 검색어에 해당하는 사용자가 있어야 표시한다. empty는 사이즈가 0 null 둘다 포함한다--%>
                                    <%-- 객체 != null 과는 다르다.  --%>
         <c:if test="${not empty requestScope.memberList}">
            <c:forEach var="membervo" items="${requestScope.memberList}">
               <tr class="memberInfo">
                  <td class="userid">${membervo.userid}</td>
                  <td>${membervo.name}</td>
                  <td>${membervo.email}</td>
                  <td>
                     <c:choose>
                        <c:when test="${membervo.gender == '1'}">남</c:when>
                        <c:otherwise>여</c:otherwise>
                     </c:choose>
                  </td>
               </tr>
            </c:forEach>
         </c:if>

         <%-- 검색어에 해당하는 사용자가 없다. --%>
         <c:if test="${empty requestScope.memberList}">
            <tr>
               <td colspan="4">데이터가 존재하지 않습니다.</td>
            </tr>
         </c:if>

      </tbody>
   </table>
   
   <div id="pageBar">
       <nav>
          <ul class="pagination">${requestScope.pageBar}</ul>
       </nav>
    </div>
</div>

<form name="memberOneDetail_frm">
	<input type="hidden" name="userid" />
	<input type="hidden"   name="goBackURL" value="${requestScope.currentURL}" />
</form>
<jsp:include page="../../footer2.jsp" />












<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<%
   String ctxPath = request.getContextPath();
   //      /MyMVC
%>

<jsp:include page="../../header2.jsp" />

<style type="text/css">

	table#tblProdInput {border: solid gray 1px; 
                       border-collapse: collapse; }
                       
    table#tblProdInput td {border: solid gray 1px; 
                          padding-left: 10px;
                          height: 50px; }
                          
    .prodInputName {background-color: #e6fff2; 
                    font-weight: bold; }                                                 
   
   .error {color: red; font-weight: bold; font-size: 9pt;


</style>


<script type="text/javascript">
	
	$(document).ready(function(){
		
		$("span.error").hide();
		
		// "제품수령" 에 스피너 달아주기
		$("input#spinnerPqty").spinner({
			
			spin : function(event, ui) {

				if (ui.value > 100) {
					$(this).spinner("value", 100);
					return false;
				} else if (ui.value < 1) {
					$(this).spinner("value", 1);
					return false;
				}
			}
		
		});// end of $("input#spinnerPqty").spinner()-------------------

		// "추가이미지파일" 에 스피너 달아주기
		$("input#spinnerImgQty").spinner({

			spin : function(event, ui) {
				if (ui.value > 10) {
					$(this).spinner("value", 10);
					return false;
				} else if (ui.value < 0) {
					$(this).spinner("value", 0);
					return false;
				}
			}

		});// end of $("input#spinnerImgQty").spinner()-------------------
		
		
		// #### 스피너의 이벤트는 click 도 아니고 change 도 아니고 "spinstop" 이다. #### //
		$("input#spinnerImgQty").bind("spinstop", function(){
			
			$("input:text[name='changeCount']").val(""); // 초기화
			let n_changeCount = 0;
			
			let v_html = ``;
			let cnt = $(this).val();
			
			
		/*
			console.log("~~ 확인용 cnt => ", cnt);
			console.log("~~ 확인용 typeof cnt => ", typeof cnt);
			// ~~ 확인용 typeof cnt =>  string
		*/
			
			for(let i=0; i<Number(cnt); i++){
				v_html += `<br><input type="file" name="attach\${i}" class="btn btn-default img_file" accept="image/*" />`;
			}
			
			$("div#divfileattach").html(v_html);
			$("input:hidden[name='attachCount']").val(cnt); // 추가이미지파일 개수 
			
		});// end of $("input#spinnerImgQty").bind("spinstop", function(){})--------------------
		
		
		// == 제품이미지 또는 추가이미지 파일을 선택하면 화면에 이미지를 미리 보여주기 구현하기 시작 == //
		$(document).on("change", "input.img_file", function(e){
			
			$("img#previewImg").show();
			
			const input_file = $(e.target).get(0);
			// jQuery선택자.get(0) 은 jQuery 선택자인 jQuery Object 를 DOM(Document Object Model) element 로 바꿔주는 것이다. 
	        // DOM element 로 바꿔주어야 순수한 javascript 문법과 명령어를 사용할 수 있게 된다. 
			
	    //  console.log(input_file);
			// <input type="file" name="pimage1" class="infoData img_file" accept="image/*">
			
		//	console.log(input_file.files);
		/*
			FileList {0: File, length: 1}
			0: File {name: 'berkelekle심플라운드01.jpg', lastModified: 1605506138000, lastModifiedDate: Mon Nov 16 2020 14:55:38 GMT+0900 (한국 표준시), webkitRelativePath: '', size: 71317, …}
			length: 1
			[[Prototype]]: FileList
		*/
			
		//	console.log(input_file.files[0]);
		/*	
			File {name: 'berkelekle심플라운드01.jpg', lastModified: 1605506138000, lastModifiedDate: Mon Nov 16 2020 14:55:38 GMT+0900 (한국 표준시), webkitRelativePath: '', size: 71317, …}
			
			>>설명<<
             name : 단순 파일의 이름 string타입으로 반환 (경로는 포함하지 않는다.)
             lastModified : 마지막 수정 날짜 number타입으로 반환 (없을 경우, 현재 시간)
             lastModifiedDate: 마지막 수정 날짜 Date객체타입으로 반환
             size : 64비트 정수의 바이트 단위 파일의 크기 number타입으로 반환
             type : 문자열인 파일의 MIME 타입 string타입으로 반환 
                    MIME 타입의 형태는 type/subtype 의 구조를 가지며, 다음과 같은 형태로 쓰인다. 
                    text/plain
                    text/html
                    image/jpeg
                    image/png
                    audio/mpeg
                    video/mp4
                    ...
		*/	
		 //	 console.log(input_file.files[0].name);
             // berkelekle심플라운드01.jpg
             
             // 자바스크립트에서 file 객체의 실제 데이터(내용물)에 접근하기 위해 FileReader 객체를 생성하여 사용한다.
              
			 const fileReader = new FileReader();

 			 fileReader.readAsDataURL(input_file.files[0]) // --> 파일을 읽고, result속성에 파일을 나타내는 URL을 저장 시켜준다.

			 fileReader.onload = function() { // // FileReader.onload --> 파일 읽기 완료 성공시에만 작동하도록 하는 것임.
			 	console.log(fileReader.result);
				/*
				data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAeAB4AAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAg 
				이러한 형태로 출력되며, img.src 의 값으로 넣어서 사용한다.
				*/
			 	document.getElementById("previewImg").src = fileReader.result;
				
			};

		});
		// == 제품이미지 또는 추가이미지 파일을 선택하면 화면에 이미지를 미리 보여주기 구현하기 끝 == //

		// 제품등록하기
		$("input:button[id='btnRegister']").click(function(){
			
			$("span.error").hide();
			
			let b_flag = false;
			
			$(".infoData").each(function(index, elmt){
				const val = $(elmt).val().trim();
				if(val == ""){
					$(elmt).next().show();
					b_flag = true;
					return false; // 일반적인 for문의 break; 와 같은 기능이다
				}
				
			});
			
			if(!b_flag){
				const frm = document.prodInputFrm;
				frm.submit();
			}
			
			
		});// end of $("input:button[id='btnRegister']").click(function(){})--------------
		
		// 취소하기
		$("input[type='reset']").click(function(){
			$("span.error").hide();
			$("div#divfileattach").empty();
			$("img#previewImg").hide();
		})
		
		
		
	});// end of $(document).ready(function(){})---------------------
</script>






<div align="center" style="margin-bottom: 20px;">

	<div style="border: solid green 2px; width: 250px; margin-top: 20px; padding-top: 10px; padding-bottom: 10px; border-left: hidden; border-right: hidden;">       
	   <span style="font-size: 15pt; font-weight: bold;">제품등록&nbsp;[관리자전용]</span>   
	</div>
	<br/>
	
	
	<%-- !!!!! ==== 중요 ==== !!!!! --%>
<%-- 폼에서 파일을 업로드 하려면 반드시 method 는 POST 이어야 하고 
     enctype="multipart/form-data" 으로 지정해주어야 한다.!! --%>
<form name="prodInputFrm"
      action="<%= request.getContextPath()%>/shop/admin/productRegister.up"
      method="POST"                         
      enctype="multipart/form-data"> 
	      
	<table id="tblProdInput" style="width: 80%;">
	<tbody>
	   <tr>
	      <td width="25%" class="prodInputName" style="padding-top: 10px;">카테고리</td>
	      <td width="75%" align="left" style="padding-top: 10px;" >
	         <select name="fk_cnum" class="infoData">
	            <option value="">:::선택하세요:::</option>
	            <c:forEach var="cvo" items="${requestScope.categoryList}">
	            	<option value="${cvo.cnum}">${cvo.cname}</option>
	            </c:forEach>
	            
	            
	            <%-- 
	               <option value="1">전자제품</option>
	               <option value="2">의  류</option>
	               <option value="3">도  서</option>
	            --%> 
	            
	         </select>
	         <span class="error">필수입력</span>
	      </td>   
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품명</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;" >
	         <input type="text" style="width: 300px;" name="pname" class="box infoData" />
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제조사</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="text" style="width: 300px;" name="pcompany" class="box infoData" />
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품이미지</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="file" name="pimage1" class="infoData img_file" accept='image/*' /><span class="error">필수입력</span>
	         <input type="file" name="pimage2" class="infoData img_file" accept='image/*' /><span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품설명서 파일첨부(선택)</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="file" name="prdmanualFile" />
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품수량</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	              <input id="spinnerPqty" name="pqty" value="1" style="width: 30px; height: 20px;"> 개
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품정가</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="text" style="width: 100px;" name="price" class="box infoData" /> 원
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품판매가</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <input type="text" style="width: 100px;" name="saleprice" class="box infoData" /> 원
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품스펙</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <select name="fk_snum" class="infoData">
	            <option value="">:::선택하세요:::</option>
				
				 <c:forEach var="spvo" items="${requestScope.specList}">
	            	<option value="${spvo.snum}">${spvo.sname}</option>
	            </c:forEach>
					           
	           
	            <%-- 
	               <option value="1">HIT</option>
	               <option value="2">NEW</option>
	               <option value="3">BEST</option> 
	            --%>
	            
	         </select>
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName">제품설명</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden;">
	         <textarea name="pcontent" rows="5" cols="60"></textarea>
	      </td>
	   </tr>
	   <tr>
	      <td width="25%" class="prodInputName" style="padding-bottom: 10px;">제품포인트</td>
	      <td width="75%" align="left" style="border-top: hidden; border-bottom: hidden; padding-bottom: 10px;">
	         <input type="text" style="width: 100px;" name="point" class="box infoData" /> POINT
	         <span class="error">필수입력</span>
	      </td>
	   </tr>
	   
	   <%-- ==== 첨부파일 타입 추가하기 ==== --%>
	    <tr>
	          <td width="25%" class="prodInputName" style="padding-bottom: 10px;">추가이미지파일(선택)</td>
	          <td>
	             <label for="spinnerImgQty">파일갯수 : </label>
	          <input id="spinnerImgQty" value="0" style="width: 30px; height: 20px;">
	             <div id="divfileattach"></div>
	              
	             <input type="hidden" name="attachCount" />
	              
	          </td>
	    </tr>
	    
	    <%-- ==== 이미지파일 미리보여주기 ==== --%>
	    <tr>
	          <td width="25%" class="prodInputName" style="padding-bottom: 10px;">이미지파일 미리보기</td>
	          <td>
	             <img id="previewImg" width="300" />
	          </td>
	    </tr>
	   
	   <tr style="height: 70px;">
            <td colspan="2" align="center" style="border-left: hidden; border-bottom: hidden; border-right: hidden; padding: 50px 0;">
                <input type="button" value="제품등록" id="btnRegister" style="width: 120px;" class="btn btn-info btn-lg mr-5" /> 
                <input type="reset" value="취소"  style="width: 120px;" class="btn btn-danger btn-lg" />   
            </td>
         </tr>
	</tbody>
	</table>
</form>
	
	

</div>




<jsp:include page="../../footer2.jsp" />


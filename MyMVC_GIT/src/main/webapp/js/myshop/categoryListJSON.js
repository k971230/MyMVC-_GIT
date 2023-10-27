
$(document).ready(function(){
   
   $.ajax({
      url:"categoryListJSON.up",
      dataType:"json",
      success:function(json){
          let v_html = ``;
          
          if(json.length == 0) {
            v_html = `현재 카테고리 준비중 입니다...`;
            $("div#categoryList").html(v_html);
         }
         
         else if(json.length > 0) {
            // 데이터가 존재하는 경우
            
         /*   
            // 자바스크립트 사용하는 경우
            json.forEach(function(item, index, array){
               
            });
            
            // jQuery 를 사용하는 경우
            $.each(json, function(index, item){
               
            });
         */    
		 // console.log("~~~ 확인용 json => ", JSON.stringify(json));
         /*
            ~~~ 확인용 json => [{"code":"100000","cname":"전자제품","cnum":1}
                              ,{"code":"200000","cname":"의류","cnum":2}
                              ,{"code":"300000","cname":"도서","cnum":3}]    
         */   
			
			v_html = `<div style="width: 95%; margin: 0 auto;">
                        <div style="border: solid 1px gray;
                                  padding-top: 5px;
                                  padding-bottom: 5px;
                                 text-align: center;
                                 color: navy;
                                 font-size: 14pt;
                                 font-weight: bold;">
                          CATEGORY LIST
                      </div>
                       <div style="border: solid 1px gray;
                                border-top: hidden;
                                padding-top: 5px;
                                padding-bottom: 5px;
                                text-align: center;">
                            <a href="mallHomeMore.up">전체</a>&nbsp;&nbsp;`;
                          
            $.each(json, function(index, item){
               
                 v_html += `<a href="/MyMVC/shop/mallByCategory.up?cnum=${item.cnum}">${item.cname}</a>&nbsp;&nbsp;`;  
                          
            });// end of $.each(json, function(index, item)-----
            
            v_html += ` </div>
                      </div>`;
			
			// 카테고리 출력하기
            $("div#categoryList").html(v_html);
             
         }// end of else if(json.length > 0)----------------
                  
      },
      error: function(request, status, error){
         alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
      }      
   });
   
});// end of $(document).ready(function(){})---------------- 
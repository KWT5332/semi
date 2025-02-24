package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.BoardDAO;
import dao.CommentsDAO;
import dto.CommentsDTO;
import dto.MemberDTO;



@WebServlet("*.cmt")
public class CommentsController extends HttpServlet {
	   
	   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   
	      request.setCharacterEncoding("utf-8");

	      String requestURI = request.getRequestURI();
	      String ctxPath = request.getContextPath();
	      String cmd = requestURI.substring(ctxPath.length());
	      System.out.println("요청된 url :" + cmd);
	      
	      CommentsDAO cdao = CommentsDAO.getInstance();
	      BoardDAO dao = BoardDAO.getInstance();
	      Gson g = new Gson();
	      
	      try {
	         if(cmd.contentEquals("/write.cmt")) {
	        	 
	        	response.setCharacterEncoding("utf8");
		        response.setContentType("text/html; charset=utf8;");
	            MemberDTO dto = ((MemberDTO)request.getSession().getAttribute("login"));
	            String id =dto.getId();
	            
	            String cmt_content = request.getParameter("cmt_content");
	            cmt_content = dao.XSSFilter(cmt_content);
	            int board_seq = Integer.parseInt(request.getParameter("board_seq"));
	            
	            int cmt_seq = cdao.getcmt_seq();
	            cdao.insert(cmt_seq, id, cmt_content, board_seq);
	            CommentsDTO cdto = cdao.getComments(cmt_seq);
	           
	            String result = g.toJson(cdto);
	            response.getWriter().append(result);   
	            
	            
	            
//	            request.setAttribute("cmt",board_seq);
//	            response.sendRedirect("detail.bor?board_seq="+board_seq);
	            
	         }else if(cmd.contentEquals("/delete.cmt")) {
	            int board_seq = Integer.parseInt(request.getParameter("board_seq"));
	            int cmt_seq = Integer.parseInt(request.getParameter("cmt_seq"));
	            cdao.delete(cmt_seq);
	            
	            response.sendRedirect("/detail.bor?board_seq="+board_seq);
	            
	         }else if(cmd.contentEquals("/modify.cmt")){
	            
	            String cmt_content = request.getParameter("cmt_content");
	            cmt_content = dao.XSSFilter(cmt_content);
	            int cmt_seq = Integer.parseInt(request.getParameter("cmt_seq"));
	         
	            
	            cdao.modify(cmt_content,cmt_seq);
	            CommentsDTO cdto = cdao.getComments(cmt_seq);
	            
	            String result = g.toJson(cdto);
	            response.getWriter().append(result);
	            
	         }
	      
	      }catch(Exception e) {
	         e.printStackTrace();
	         
	      }
	   }


	   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	      doGet(request, response);
	   }

	}


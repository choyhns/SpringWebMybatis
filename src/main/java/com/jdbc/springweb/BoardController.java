package com.jdbc.springweb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jdbc.dao.BoardDAO;
import com.jdbc.dto.BoardDTO;
import com.jdbc.util.MyUtil;

@Controller
public class BoardController {
	
	@Autowired
	BoardDAO dao;
	
	@Autowired
	MyUtil myUtil;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "index";
	}
	
	/*
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST},
			value = "/created.action")
	public String created() {
		return "bbs/created";
	}
	*/
	
	@RequestMapping(value = "/created.action")
	public ModelAndView created() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("bbs/created");
		
		return mav;
	}
	
	/*
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST},
			value = "/created_ok.action")
	public String created_ok(BoardDTO dto,HttpServletRequest req) {
		
		dto.setNum(dao.getMaxNum()+1);
		dto.setIpAddr(req.getRemoteAddr());
		
		dao.insertData(dto);
		
		return "redirect:/list.action";
	}
	*/
	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST},
			value = "/created_ok.action")
	public ModelAndView created_ok(BoardDTO dto,HttpServletRequest req) {
		
		ModelAndView mav = new ModelAndView();
		
		dto.setNum(dao.getMaxNum()+1);
		dto.setIpAddr(req.getRemoteAddr());
		
		dao.insertData(dto);
		
		mav.setViewName("redirect:/list.action");
		
		return mav;
	}
	
	@RequestMapping(value = "/list.action",
			method = {RequestMethod.GET,RequestMethod.POST})
	public String list(HttpServletRequest req,String pageNum,
			String searchKey,String searchValue) throws Exception {
		
		String cp = req.getContextPath();
		
		int currentPage = 1;
		
		if(pageNum!=null && !pageNum.equals("")) {
			currentPage = Integer.parseInt(pageNum);
		}
		
		if(searchValue!=null && !searchValue.equals("")) {
			if(req.getMethod().equalsIgnoreCase("get")) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
		}else {
			searchKey = "subject";
			searchValue = "";
		}
		
		int totalDataCount = dao.getDataCount(searchKey, searchValue);
		int numPerPage = 5;
		int totalPage = myUtil.getPageCount(numPerPage, totalDataCount);
		
		if(currentPage>totalPage) {
			currentPage = totalPage;
		}
		
		int start = (currentPage-1)*numPerPage+1;
		int end = currentPage*numPerPage;
		
		List<BoardDTO> lists = dao.getLists(start, end, searchKey, searchValue);
		
		String params = "";
		if(searchValue!=null && !searchValue.equals("")) {
			params = "searchKey=" + searchKey;
			params+= "&searchValue=" + URLEncoder.encode(searchValue,"UTF-8");
		}
		
		String listUrl = cp + "/list.action";
		String articleUrl = cp + "/article.action?pageNum=" + currentPage;
		
		if(!params.equals("")) {
			listUrl+= "?" + params;
			articleUrl+= "&" + params;
		}
		
		String pageIndexList = myUtil.pageIndexList(currentPage, totalPage, listUrl);
		
		req.setAttribute("lists", lists);
		req.setAttribute("pageIndexList", pageIndexList);
		req.setAttribute("totalDataCount", totalDataCount);
		req.setAttribute("articleUrl", articleUrl);
		
		return "bbs/list";
	}
	
	@RequestMapping(value = "/article.action",
			method = {RequestMethod.GET,RequestMethod.POST})
	public String article(String num,String pageNum,String searchKey,
			String searchValue,HttpServletRequest req) throws Exception{
		
		if(searchValue!=null && !searchValue.equals("")) {
			if(req.getMethod().equalsIgnoreCase("get")) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
		}else {
			searchKey = "subject";
			searchValue = "";
		}
		
		String params = "pageNum=" + pageNum;
		if(searchValue!=null && !searchValue.equals("")) {
			params+= "&searchKey=" + searchKey;
			params+= "&searchValue=" + URLEncoder.encode(searchValue,"UTF-8");
		}
		
		BoardDTO dto = dao.getReadData(Integer.parseInt(num));
		
		if(dto==null) {
			return "redirect:/list.action";
		}
		
		int lineSu = dto.getContent().split("\n").length;
		dto.setContent(dto.getContent().replaceAll("\n", "<br/>"));
		dao.updateHitCount(Integer.parseInt(num));
		
		req.setAttribute("dto", dto);
		req.setAttribute("params", params);
		req.setAttribute("lineSu", lineSu);
		
		return "bbs/article";
	}
	
	@RequestMapping(value = "delete.action",
			method = {RequestMethod.GET,RequestMethod.POST})
	public String delete(String num,String pageNum,String searchKey,String searchValue,
			HttpServletRequest req) throws Exception {
		
		if(searchValue!=null && !searchValue.equals("")) {
			if(req.getMethod().equalsIgnoreCase("get")) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
		}else {
			searchKey = "subject";
			searchValue = "";
		}
		
		String params = "pageNum=" + pageNum;
		if(searchValue!=null && !searchValue.equals("")) {
			params+= "&searchKey=" + searchKey;
			params+= "&searchValue=" + URLEncoder.encode(searchValue,"UTF-8");
		}
		
		dao.deleteData(Integer.parseInt(num));
		
		return "redirect:/list.action?" + params;
	}
	
	@RequestMapping(value = "/update.action",
			method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView update(String num,String pageNum,
			String searchKey,String searchValue,
				HttpServletRequest req) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		if(searchValue!=null && !searchValue.equals("")) {
			if(req.getMethod().equalsIgnoreCase("get")) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
		}else {
			searchKey = "subject";
			searchValue = "";
		}
		
		String params = "pageNum=" + pageNum;
		if(searchValue!=null && !searchValue.equals("")) {
			params+= "&searchKey=" + searchKey;
			params+= "&searchValue=" + URLEncoder.encode(searchValue,"UTF-8");
		}
		
		BoardDTO dto = dao.getReadData(Integer.parseInt(num));
		
		mav.addObject("dto", dto);
		mav.addObject("params", params);
		
		mav.setViewName("/bbs/update");
		
		return mav;
	}
	
	@RequestMapping(value = "/update_ok.action",
			method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView update_ok(BoardDTO dto,String pageNum,
			String searchKey,String searchValue,
			HttpServletRequest req) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		
		if(searchValue!=null && !searchValue.equals("")) {
			if(req.getMethod().equalsIgnoreCase("get")) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
		}else {
			searchKey = "subject";
			searchValue = "";
		}
		
		String params = "pageNum=" + pageNum;
		
		if(searchValue!=null && !searchValue.equals("")) {
			params+= "&searchKey=" + searchKey;
			params+= "&searchValue=" + URLEncoder.encode(searchValue,"UTF-8");
		}
		
		dao.updateData(dto);
		
		mav.addObject("dto", dto);
		mav.addObject("params", params);
		
		mav.setViewName("redirect:/article.action?" + params + "&num=" + dto.getNum());
		
		return mav;
	}
}

package com.jdbc.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartResolver;

import com.jdbc.dto.BoardDTO;

public class BoardDAO {
	
	MultipartResolver multipartResolver;
	
	private SqlSessionTemplate sessionTemplate;
	
	public void setSessionTemplate(SqlSessionTemplate sessionTemplate) {
		this.sessionTemplate = sessionTemplate;
	}
	
	public int getMaxNum() {
	
		int maxNum = 0;
		
		maxNum = sessionTemplate.selectOne("com.board.maxNum");
		
		return maxNum;
	}
	
	public void insertData(BoardDTO dto) {
		sessionTemplate.insert("com.board.insert", dto);
	}
	
	public int getDataCount(String searchKey,String searchValue) {
		
		int totalDataCount = 0;
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("searchKey", searchKey);
		params.put("searchValue", searchValue);
		
		totalDataCount = sessionTemplate.selectOne("com.board.dataCount", params);
		
		return totalDataCount;
	}
	
	public List<BoardDTO> getLists(int start,int end,String searchKey,String searchValue) {
			
		Map<String, Object> hMap = new HashMap<String, Object>();
		
		hMap.put("searchKey", searchKey);
		hMap.put("searchValue", searchValue);
		hMap.put("start", start);
		hMap.put("end", end);
		
		List<BoardDTO> lists = sessionTemplate.selectList("com.board.list", hMap);
			
		return lists;
	}
	
	public BoardDTO getReadData(int num) {
		BoardDTO dto = sessionTemplate.selectOne("com.board.readData", num);
		return dto;
	}
	
	public void updateData(BoardDTO dto) {
		sessionTemplate.update("com.board.update", dto);
	}
	
	public void updateHitCount(int num) {
		
		sessionTemplate.update("com.board.hitCount",num);
	}
	
	public void deleteData(int num) {
		
		sessionTemplate.delete("com.board.delete",num);
	}
	
}

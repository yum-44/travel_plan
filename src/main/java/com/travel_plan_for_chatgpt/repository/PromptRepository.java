package com.travel_plan_for_chatgpt.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.travel_plan_for_chatgpt.model.*;

@Repository
public class PromptRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	// 処理結果をデータベースに保存するメソッド
	public void saveResult(Result result) {
		jdbcTemplate.update("insert into chat_gpt_request_info (request_prompt, response, kind) VALUES (?, ?, 10)",
			result.getPrompt(),
			result.getResponse()
	    );
	}
}
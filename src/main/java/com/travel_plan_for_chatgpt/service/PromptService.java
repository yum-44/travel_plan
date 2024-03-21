package com.travel_plan_for_chatgpt.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import org.json.JSONArray;
import java.net.URL;

import com.travel_plan_for_chatgpt.model.*;
import com.travel_plan_for_chatgpt.repository.*;

@Service
public class PromptService {

	@Autowired
	private PromptRepository promptRepository;
	
	// フォームのデータから処理結果を取得し、データベースに保存するメソッド
	public Result getResult(String prefecture, String day) {
		
	    String prompt = generatePrompt(prefecture, day);
	    
	    // GPT API を呼び出して応答を取得
	    String response = callGPTAPI(prompt);
	    Result result = new Result(prompt, response);
	    
	    // 処理結果をデータベースに保存
	    promptRepository.saveResult(result);
	    return result;
	}
	
	// Prompt を生成するメソッド
	private String generatePrompt(String prefecture, String day) {
	
		// 都道府県と日付から Prompt を生成する
		return prefecture + " へ " + day + "日間旅行するプランを提案してください。";
	}
	
	/*
	* ChatGPTの設定
	*/
	// APIのURL
	@Value("${api.url}")
	private String API_URL;
	// APIのKey
	@Value("${api.key}")
	private String API_KEY;
	//ChatGPTのバージョン
	@Value("${api.model}")
	private String MODEL;
	 
	// GPT API を呼び出して結果を取得するメソッド
	private String callGPTAPI(String prompt) {
	    // GPT API を呼び出して結果を取得
		String returnMessage = null;

		try {
			// リクエスト設定
			URL url = new URL(API_URL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Authorization", "Bearer " + API_KEY);
			con.setDoOutput(true);

			// リクエストメッセージ生成
			String requestPrompt = "[{\"role\": \"system\", \"content\": \"日本語で返答してください。\"},{\"role\": \"user\", \"content\": \"" + prompt + "\"}]";
			String requestBody = "{\"model\": \"" + MODEL + "\", \"messages\": " + requestPrompt + "}";

			OutputStream os = con.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
			osw.write(requestBody);
			osw.flush();

			// リクエスト処理
            int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				// レスポンスからcontentのみを取得して返す
				JSONObject jsonResponse = new JSONObject(response.toString());
				JSONArray choices = jsonResponse.getJSONArray("choices");
				StringBuilder result = new StringBuilder();
				for (int i = 0; i < choices.length(); i++) {
					JSONObject choice = choices.getJSONObject(i);
					JSONObject message = choice.getJSONObject("message");
					result.append(message.getString("content")).append("\n");
				}

				returnMessage = result.toString();

			} else {
				// 通信に失敗した場合
				returnMessage = "false";
				System.out.println("POST request failed with response code: " + responseCode);
			}

		} catch (IOException | JSONException e) {
			// 一連の処理で例外が発生した場合
			returnMessage = "false";
			System.out.println(e);
		}
        return returnMessage;
    }
}

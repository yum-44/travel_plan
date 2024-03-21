package com.travel_plan_for_chatgpt.model;


public class Result {
    private String prompt;
    private String response;

    public Result(String prompt, String response) {
        this.prompt = prompt;
        this.response = response;
    }

    // プロンプトを取得する
    public String getPrompt() {
        return prompt;
    }

    // プロンプトを設定する
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    // 応答を取得する
    public String getResponse() {
        return response;
    }

    // 応答を設定する
    public void setResponse(String response) {
        this.response = response;
    }
}
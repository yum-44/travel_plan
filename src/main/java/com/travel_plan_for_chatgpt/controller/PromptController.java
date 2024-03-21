package com.travel_plan_for_chatgpt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.travel_plan_for_chatgpt.model.*;
import com.travel_plan_for_chatgpt.service.*;


@Controller
public class PromptController {

    @Autowired
    private PromptService promptService;

    // フォームを表示するエンドポイント
    @GetMapping("/travel-plan-by-chatgpt")
    public String showForm() {
        return "form";
    }

    // フォームのデータを受け取り、処理結果を表示するエンドポイント
    @PostMapping("/travel-plan-by-chatgpt/response")
    public String handleSubmit(@RequestParam("prefecture") String prefecture, @RequestParam("day") String day, Model model) {

        if ( prefecture.equals("none") ) {
            // エラーがある場合はフォームページに戻る
            model.addAttribute("errorMessage", "★都道府県を選択してください！");
            return "form";
        } else if ( day.equals("none") ) {
            // エラーがある場合はフォームページに戻る
            model.addAttribute("errorMessage", "★予定日数を選択してください！");
            return "form";
        }

        Result result = promptService.getResult(prefecture, day);

        // 表示用に回答の改行コードをタグ変換
       String response = result.getResponse().replace("\n","<br />");

        model.addAttribute("request", result.getPrompt());
        model.addAttribute("result", response);
        return "result";
    }
}
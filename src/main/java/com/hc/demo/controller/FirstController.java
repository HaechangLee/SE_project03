package com.hc.demo.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hc.demo.container.User;
import com.hc.demo.model.UserModel;
import com.hc.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class FirstController {
    @Autowired
    UserModel userModel;
    @GetMapping("/rest/getUserInfo")
    public String firstcont(HttpServletRequest req) {
        HttpSession hs = req.getSession();
        JsonObject jo = new JsonObject();

        if(hs!=null && hs.getAttribute("Logged") != null && (Boolean)hs.getAttribute("Logged") == true) {
            JsonObject jo2 = new JsonObject();
            User user = (User)hs.getAttribute("User");
            jo2.addProperty("id",user.getID());
            jo2.addProperty("name",user.getName());
            jo2.addProperty("nickname",user.getNickname());
            jo.addProperty("logged",true);
            jo.add("data",jo2);
        }
        else {
            jo.addProperty("logged", false);
        }

        return jo.toString();
    }

    @PostMapping("/rest/login")
    public String login(HttpServletRequest req, @RequestBody HashMap<String,Object> body) {
        HttpSession hs = req.getSession(true); // 사용자 세션정보 조회 (없으면 새로운 세션 생성)
        JsonObject jo = new JsonObject();
        if(userModel.loginUser((String)body.get("id"),(String)body.get("pw"),hs) != null) // id 유효성 체크
            jo.addProperty("res","success");
        else
            jo.addProperty("res","failed");

        return jo.toString(); // 로그인 성공여부 반환
    }

    @GetMapping("/rest/logout")
    public String logout(HttpServletRequest req) {
        HttpSession hs = req.getSession(); // 세션 정보 조회 (기존 세션이 없으면 새로 생성)
        JsonObject jo = new JsonObject();

        try{
            hs.invalidate(); // 세션 만료처리 (세션에 저장된 정보 삭제)
            jo.addProperty("res","success");  // 로그아웃 처리 결과 리턴
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            jo.addProperty("res","failed");  // 로그아웃 처리 결과 리턴
        }

//        jo.addProperty("res","success");  // 로그아웃 처리 결과 리턴
        return jo.toString();
    }

//    @PostMapping("/rest/registerMember")
//    public String registerMember(HttpServletRequest req) {
//        JsonObject jo = new JsonObject();
//    }

}

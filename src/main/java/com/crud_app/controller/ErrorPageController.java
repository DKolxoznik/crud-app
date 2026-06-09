package com.crud_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController {

    @RequestMapping("/error/403")
    public String accessDenied(Model model) {
        model.addAttribute("status", 403);
        model.addAttribute("title", "Доступ запрещён");
        model.addAttribute("message", "У вас нет прав для выполнения этого действия. "
                + "Обычный пользователь может создавать и редактировать записи, но не удалять их.");
        return "error/error";
    }
}

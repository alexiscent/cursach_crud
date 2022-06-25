package courseWork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String showMain(Model model) {
        String[][] content = { { "Поети", "poets" },
                { "Публікації", "published" },
                { "Архів змагань", "contests" },
                { "Поточне змагання", "current" } };
        model.addAttribute("mainContent", content);
        return "mainPage";
    }
}

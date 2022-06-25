package courseWork.controller;

import courseWork.entity.*;
import courseWork.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/published")
public class PublishedController {

    @Autowired
    private PublishedRepository publishedRepository;

    @GetMapping
    public String showPublished(Model model) {
        model.addAttribute("mainContent", publishedRepository.findByOrderByName());
        model.addAttribute("display", "name");
        model.addAttribute("pageName", "Список опублікованих робіт");
        return "page";
    }

    @GetMapping("/{id}")
    public String showWork(Model model, @PathVariable Long id) {
        try {
            Published content = publishedRepository.findById(id).get();
            String[][] pFields = { { "Назва", "name" },
                    { "Автор", "poet.name", "poets", "poet.id" },
                    { "Дата видання", "date" } };
            model.addAttribute("mainContent", content);
            model.addAttribute("primaryFields", pFields);
            model.addAttribute("pageName", "Публікація");
            return "specificPage";
        } catch (Exception e) {
            System.out.println(e);
            return "redirect:/contests";
        }
    }

    @PutMapping("/{id}")
    public String updateWork(Published newItem, BindingResult bindingResult, @PathVariable Long id) {
        try {
            Published published = publishedRepository.findById(id).get();
            published.setName(newItem.getName());
            publishedRepository.save(published);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/published/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteWork(@PathVariable Long id) {
        try {
            publishedRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/published";
    }

    @GetMapping("/add")
    public String addWork(Model model) {
        model.addAttribute("pageName", "Додати публікацію");
        model.addAttribute("newItem", new Published());
        String[][] fields = { { "Назва", "name" },
                { "Дата видання", "date" },
                { "ID автора", "poet" } };
        model.addAttribute("fields", fields);
        return "addItem";
    }

    @PostMapping("/add")
    public String addWork(Published newItem, BindingResult bindingResult) {
        try {
            return "redirect:/published/" + publishedRepository.save(newItem).getId();
        } catch (Exception e) {
            System.out.println(e);
            return "redirect:/published/add";
        }
    }
}

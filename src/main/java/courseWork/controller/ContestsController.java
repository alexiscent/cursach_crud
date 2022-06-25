package courseWork.controller;

import java.util.stream.Collectors;

import courseWork.*;
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
@RequestMapping("/contests")
public class ContestsController {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ContestService service;

    @GetMapping
    public String showContests(Model model) {
        model.addAttribute("mainContent", contestRepository.findByOrderByDate());
        model.addAttribute("display", "date");
        model.addAttribute("pageName", "Архів змагань");
        return "page";
    }

    @GetMapping("/{id}")
    public String showContest(Model model, @PathVariable Long id) {
        try {
            Contest content = contestRepository.findById(id).get();
            Object[] aContent = {
                    content.getPoets().stream()
                            .sorted((o1, o2) -> o1.getPositionNumber().compareTo(o2.getPositionNumber()))
                            .map(Attendance::getPoet)
                            .collect(Collectors.toList())
            };
            String[][] pFields = { { "Дата проведення", "date" },
                    { "Тема", "theme" },
                    { "Аналіз", "analysis" } };
            String[][] aFields = { { "Приймали участь", "name", "poets" } };
            model.addAttribute("pageName", "Змагання");
            model.addAttribute("mainContent", content);
            model.addAttribute("primaryFields", pFields);
            model.addAttribute("additionalContent", aContent);
            model.addAttribute("additionalFields", aFields);
            return "specificPage";
        } catch (Exception e) {
            System.out.println(e);
            return "redirect:/contests";
        }
    }

    @PutMapping("/{id}")
    public String updateContest(Contest newItem, BindingResult bindingResult, @PathVariable Long id) {
        try {
            newItem.setId(id);
            contestRepository.save(newItem);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/contests/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteContest(@PathVariable Long id) {
        try {
            contestRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/contests";
    }

    @GetMapping("/add")
    public String addContests(Model model) {
        model.addAttribute("pageName", "Додати змагання");
        model.addAttribute("newItem", new ContestNPoets());
        String[][] fields = { { "Дата проведення", "contest.date" },
                { "Тема", "contest.theme" },
                { "Аналіз", "contest.analysis" },
                { "ID учасників", "poets" } };
        model.addAttribute("fields", fields);
        return "addItem";
    }

    @PostMapping("/add")
    public String addContest(ContestNPoets newItem, BindingResult bindingResult) {
        return service.add(newItem);
    }

}

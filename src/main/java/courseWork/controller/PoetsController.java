package courseWork.controller;

import java.util.stream.Collectors;

import courseWork.*;
import courseWork.entity.*;
import courseWork.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/poets")
public class PoetsController {
    @Autowired
    private PoetRepository poetRepository;

    @Autowired
    private PublishedRepository publishedRepository;

    @Autowired
    private LuggageRepository luggageRepository;

    @GetMapping
    public String showPoets(Model model) {
        model.addAttribute("mainContent", poetRepository.findByOrderByName());
        model.addAttribute("display", "name");
        model.addAttribute("pageName", "Список поетів");
        return "page";
    }

    @GetMapping("/{id}")
    public String showPoet(Model model, @PathVariable Long id) {
        try {
            Poet content = poetRepository.findById(id).get();
            Object[] aContent = { luggageRepository.findByPoetId(id),
                    publishedRepository.findByPoetIdOrderByDate(id),
                    content.getContests().stream()
                            .sorted((o1, o2) -> o1.getContest().getDate().compareTo(o2.getContest().getDate()))
                            .map(Attendance::getContest)
                            .collect(Collectors.toList())
            };
            String[][] pFields = { { "Ім'я", "name" } };
            String[][] aFields = { { "Творчий багаж", "luggage" },
                    { "Опубліковані роботи", "name", "published" },
                    { "Приймав участь", "date", "contests" } };
            model.addAttribute("pageName", "Поет");
            model.addAttribute("mainContent", content);
            model.addAttribute("primaryFields", pFields);
            model.addAttribute("additionalContent", aContent);
            model.addAttribute("additionalFields", aFields);
            return "specificPage";
        } catch (Exception e) {
            System.out.println(e);
            return "redirect:/poets";
        }
    }

    @PutMapping("/{id}")
    public String updatePoet(Poet newItem, @PathVariable Long id) {
        try {
            newItem.setId(id);
            poetRepository.save(newItem);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/poets/" + id;
    }

    @DeleteMapping("/{id}")
    public String deletePoet(@PathVariable Long id) {
        try {
            poetRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/poets";
    }

    @GetMapping("/add")
    public String addPoet(Model model) {
        model.addAttribute("pageName", "Додати поета");
        model.addAttribute("newItem", new PoetNLuggage());
        String[][] fields = { { "Ім'я", "name" },
                { "Творчий багаж", "luggage" } };
        model.addAttribute("fields", fields);
        return "addItem";
    }

    @PostMapping("/add")
    public String addPoet(PoetNLuggage newItem) {
        Poet poet = new Poet();
        try {
            poet.setName(newItem.getName());
            poet = poetRepository.save(poet);
        } catch (Exception e) {
            System.out.println(e);
            return "redirect:/poets/add";
        }
        try {
            Luggage luggage = new Luggage();
            luggage.setLuggage(newItem.getLuggage());
            luggage.setPoet(poet);
            luggageRepository.save(luggage);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/poets/" + poet.getId();
    }
}

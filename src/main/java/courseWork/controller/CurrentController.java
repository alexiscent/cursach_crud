package courseWork.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
@RequestMapping("/current")
public class CurrentController {
    @Autowired
    private PoetRepository poetRepository;

    @Autowired
    private CurrentContestRepository currentRepository;

    @Autowired
    private ContestService service;

    @GetMapping
    public String showCurrentContest(Model model) {
        model.addAttribute("mainContent", currentRepository.findByOrderByPositionNumber());
        model.addAttribute("newItem", new CurrentContest());
        model.addAttribute("pageName", "Поточне змагання");
        return "currentContest";
    }

    @DeleteMapping
    public String deleteCurrentContest() {
        currentRepository.deleteAll();
        return "redirect:/current";
    }

    @PutMapping
    public String changeOrder(String newOrder) {
        List<Long> order;
        try {
            order = Stream.of(newOrder.split("\\D"))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e);
            order = new ArrayList<Long>();
        }
        List<CurrentContest> contestants = currentRepository.findByOrderByPositionNumber();
        try {
            order = order.subList(0, contestants.size());
        } catch (Exception e) {
            System.out.println(e);
        }
        for (int i = 0; i < order.size(); ++i) {
            int min = order.indexOf(Collections.min(order));
            contestants.get(min).setPositionNumber(Long.valueOf(i + 1));
            currentRepository.save(contestants.get(min));
            order.set(min, Long.MAX_VALUE);
        }
        return "redirect:/current";
    }

    @PostMapping
    public String saveCurrentContest() {
        Contest contest = new Contest();
        contest.setDate(LocalDate.now());
        String poets = currentRepository.findByOrderByPositionNumber().stream()
                .map(CurrentContest::getId)
                .map(Object::toString)
                .collect(Collectors.joining(" "));
        ContestNPoets item = new ContestNPoets(contest, poets);
        String newContest = service.add(item);
        System.out.println(newContest);
        if (newContest.matches(".+\\d$")) {
            currentRepository.deleteAll();
        }
        return newContest;
    }

    @DeleteMapping("/{id}")
    public String deleteContestant(@PathVariable Long id) {
        try {
            Long position = currentRepository.findById(id).get().getPositionNumber();
            currentRepository.deleteById(id);
            List<CurrentContest> list = currentRepository.findByOrderByPositionNumber();
            for (CurrentContest item : list) {
                if (item.getPositionNumber() > position) {
                    item.setPositionNumber(position++);
                    currentRepository.save(item);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/current";
    }

    @PutMapping("/add")
    public String addContestant(CurrentContest newItem) {
        try {
            Optional<CurrentContest> test = currentRepository.findById(newItem.getPoet().getId());
            if (test.isPresent()) {
                newItem.setId(newItem.getPoet().getId());
                newItem.setPoet(test.get().getPoet());
                newItem.setPositionNumber(test.get().getPositionNumber());
            } else {
                newItem.setPoet(poetRepository.findById(newItem.getPoet().getId()).get());
                List<CurrentContest> list = currentRepository.findByOrderByPositionNumber();
                try {
                    newItem.setPositionNumber(list.get(list.size() - 1).getPositionNumber() + 1);
                } catch (Exception e) {
                    System.out.println(e);
                    newItem.setPositionNumber(Long.valueOf(1));
                }
            }
            currentRepository.save(newItem);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/current";
    }
}

package courseWork;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import courseWork.entity.*;
import courseWork.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContestService {
    @Autowired
    private PoetRepository poetRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    public String add(ContestNPoets newItem) {
        List<Long> poets_id;
        try {
            poets_id = Stream.of(newItem.getPoets().split("\\D"))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e);
            poets_id = new ArrayList<Long>();
        }
        Contest contest;
        try {
            contest = contestRepository.save(newItem.getContest());
        } catch (Exception e) {
            System.out.println(e);
            return "redirect:/contests/add";
        }
        for (int i = 0; i < poets_id.size(); ++i) {
            try {
                AttendanceId id = new AttendanceId(poets_id.get(i), contest.getId());
                Attendance object = new Attendance(id, poetRepository.findById(poets_id.get(i)).get(),
                        contest, Long.valueOf(i + 1));
                attendanceRepository.save(object);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return "redirect:/contests/" + contest.getId();
    }
}

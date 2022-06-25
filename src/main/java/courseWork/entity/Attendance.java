package courseWork.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import courseWork.AttendanceId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "attendance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

    @EmbeddedId
    private AttendanceId id;

    @ManyToOne
    @MapsId("poetId")
    @JoinColumn(name = "poet_id")
    private Poet poet;

    @ManyToOne
    @MapsId("contestId")
    @JoinColumn(name = "contest_id")
    private Contest contest;

    private Long positionNumber;
}

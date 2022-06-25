package courseWork.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "current_contest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentContest {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "poet_id")
    private Poet poet;

    private String script;
    private Long positionNumber;
}

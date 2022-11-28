package peaksoft.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.CascadeType.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    @SequenceGenerator(name = "task_seq", sequenceName = "task_seq", allocationSize = 1)
    private Long id;

    @Column(length = 100000, name = "task_name")
    private String taskName;

    @Column(length = 100000, name = "task_text")
    private String taskText;

    @Column(name = "deadline")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deadLine;

    @ManyToOne(cascade = {MERGE,DETACH,REFRESH,PERSIST}, fetch = FetchType.EAGER)
    private Lesson lesson;
}

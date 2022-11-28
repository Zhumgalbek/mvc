package peaksoft.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lesson_seq")
    @SequenceGenerator(name = "lesson_seq", sequenceName = "lesson_seq", allocationSize = 1)
    private Long id;

    @Column(length = 100000, name = "lesson_name")
    private String lessonName;

    @ManyToOne(cascade = {MERGE,DETACH,REFRESH,PERSIST}, fetch = FetchType.EAGER)
    private Course course;

    @OneToMany(cascade = {ALL}, fetch = FetchType.LAZY, mappedBy = "lesson")
    private List<Task> tasks;

    public void addTask(Task task){
        if (tasks==null){
            tasks=new ArrayList<>();
        }
        tasks.add(task);
    }

}

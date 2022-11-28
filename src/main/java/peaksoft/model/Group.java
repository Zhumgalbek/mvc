package peaksoft.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_seq")
    @SequenceGenerator(name = "group_seq", sequenceName = "group_seq", allocationSize = 1)
    private Long id;

    @Column(length = 100000, name = "group_name")
    private String groupName;

    @Column(name = "date_of_start")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfStart;

    @Column(length = 100000, name = "image")
    private String image;

    private int count;

    public void plusCount(){
        count++;
    }

    public void minusCount(){
        count--;
    }


    @ManyToMany(cascade = {MERGE, REFRESH, DETACH,PERSIST}, mappedBy = "groups")
    private List<Course> courses;

    public void addCourse(Course course){
        if (courses==null){
            courses=new ArrayList<>();
        }
        courses.add(course);
        plusCount();
    }

    @OneToMany(cascade = {MERGE, PERSIST, DETACH, REFRESH, REMOVE}, fetch = FetchType.LAZY, mappedBy = "groups")

    private List<Student> students;

    public void addStudent(Student student){
        if (students==null){
            students=new ArrayList<>();
        }
        students.add(student);
        this.getCompany().plusStudent();
    }

    public void assignStudent(Student student){
        if (students==null){
            students=new ArrayList<>();
        }
        students.add(student);
    }

    @ManyToOne(cascade = {MERGE, DETACH, PERSIST, REFRESH}, fetch = FetchType.EAGER)
    private Company company;
}

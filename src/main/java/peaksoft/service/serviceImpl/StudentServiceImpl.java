package peaksoft.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import peaksoft.model.Instructor;
import peaksoft.model.Student;
import peaksoft.repository.StudentRepository;
import peaksoft.service.StudentService;

import java.io.IOException;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository service;

    @Autowired
    public StudentServiceImpl(StudentRepository service) {
        this.service = service;
    }

    @Override
    public List<Student> getAllListStudent() {
        return service.getAllListStudent();
    }

    @Override
    public List<Student> getAllStudents(Long id) {
        return service.getAllStudents(id);
    }

    @Override
    public void addStudent(Long id, Student student) throws IOException {
        List<Student> students = service.getAllStudents(id);
        for (Student i : students) {
            if (i.getEmail().equals(student.getEmail())){
                throw new IOException("Student with email already exists!");
            }
        }
        validator(student.getPhoneNumber().replace(" ", ""), student.getFirstName().replace(" ", ""), student.getLastName().replace(" ", ""));
        service.addStudent(id,student);
    }

    @Override
    public Student getStudentById(Long id) {
        return service.getStudentById(id);
    }

    @Override
    public void updateStudent(Student student, Long id) throws IOException {
        validator(student.getPhoneNumber().replace(" ", ""), student.getFirstName().replace(" ", ""), student.getLastName().replace(" ", ""));
        service.updateStudent(student,id);
    }

    @Override
    public void deleteStudent(Long id) {
        service.deleteStudent(id);
    }

    @Override
    public void assignStudent(Long groupId, Long studentId) throws IOException {
        service.assignStudent(groupId,studentId);
    }

    private void validator(String phone, String firstName, String lastName) throws IOException {
        if (firstName.length()>2 && lastName.length()>2) {
            for (Character i : firstName.toCharArray()) {
                if (!Character.isAlphabetic(i)) {
                    throw new IOException("В имени инструктора нельзя вставлять цифры");
                }
            }

            for (Character i : lastName.toCharArray()) {
                if (!Character.isAlphabetic(i)) {
                    throw new IOException("В фамилию инструктора нельзя вставлять цифры");
                }
            }
        } else {
            throw new IOException("В имени или фамилии инструктора должно быть как минимум 3 буквы");
        }

        if (phone.length()==13
                && phone.charAt(0) == '+'
                && phone.charAt(1) == '9'
                && phone.charAt(2) == '9'
                && phone.charAt(3) == '6'){
            int counter = 0;

            for (Character i : phone.toCharArray()) {
                if (counter!=0){
                    if (!Character.isDigit(i)) {
                        throw new IOException("Формат номера не правильный");
                    }
                }
                counter++;
            }
        }else {
            throw new IOException("Формат номера не правильный");
        }
    }
}

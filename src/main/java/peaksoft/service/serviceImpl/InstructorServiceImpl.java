package peaksoft.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import peaksoft.model.Instructor;
import peaksoft.repository.InstructorRepository;
import peaksoft.service.InstructorService;

import java.io.IOException;
import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;

    @Autowired
    public InstructorServiceImpl(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public List<Instructor> getAllList() {
        return instructorRepository.getAllList();
    }

    @Override
    public List<Instructor> getAllInstructor(Long courseId) {
        return instructorRepository.getAllInstructor(courseId);
    }

    @Override
    public void addInstructor(Long id, Instructor instructor) throws IOException {
        List<Instructor> instructors = instructorRepository.getAllInstructor(id);
        for (Instructor i : instructors) {
            if (i.getEmail().equals(instructor.getEmail())){
                throw new IOException("Instructor with email already exists!");
            }
        }

        validator(instructor.getPhoneNumber().replace(" ", ""), instructor.getLastName().replace(" ", ""), instructor.getFirstName().replace(" ", ""));
        instructorRepository.addInstructor(id,instructor);
    }

    @Override
    public Instructor getInstructorById(Long id) {
        return instructorRepository.getInstructorById(id);
    }

    @Override
    public void updateInstructor(Instructor instructor, Long id) throws IOException {
        validator(instructor.getPhoneNumber().replace(" ", ""), instructor.getLastName().replace(" ", ""), instructor.getFirstName().replace(" ", ""));
        instructorRepository.updateInstructor(instructor,id);
    }

    @Override
    public void deleteInstructor(Long id) {
        instructorRepository.deleteInstructor(id);
    }

    @Override
    public void assignInstructor(Long courseId, Long instructorId) throws IOException {
        instructorRepository.assignInstructor(courseId, instructorId);
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

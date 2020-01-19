package com.infoshareacademy.web;

import com.infoshareacademy.dao.*;
import com.infoshareacademy.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@WebServlet(urlPatterns = "/student")
@Transactional
public class StudentServlet extends HttpServlet {

    private Logger LOG = LoggerFactory.getLogger(StudentServlet.class);

    @Inject
    private StudentDao studentDao;

    @Inject
    private ComputerDao computerDao;

    @Inject
    private AddressDao addressDao;

    @Inject
    private CourseDao courseDao;

    @Inject
    private TeacherDao teacherDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // Test data

        // Courses
        Course course1 = new Course("JJDD6");
        courseDao.save(course1);

        Course course2 = new Course("JJDZ6");
        courseDao.save(course2);

        Course course3 = new Course("JJFD9");
        courseDao.save(course3);

        Teacher t1 = new Teacher("Adam", "Przyby", "98798765678", Arrays.asList(course2));
        teacherDao.save(t1);

        // Addresses
        Address a1 = new Address("Grunwaldzka 472B", "Gdansk");
        addressDao.save(a1);

        Address a2 = new Address("Luzycka 12d", "Gdynia");
        addressDao.save(a2);

        // Computers
        Computer c1 = new Computer("Xiaomi Lepsze", "Windows XP SP2");
        computerDao.save(c1);

        Computer c2 = new Computer("Zenbook", "Fedora");
        computerDao.save(c2);

        // Students
        Student s1 = new Student("Michal",
                "Graczyk",
                LocalDate.of(1980, 11, 12),
                c1,
                a1,
                Arrays.asList(course1, course2, course3));
        studentDao.save(s1);

        Student s2 = new Student("Marek",
                "Malinovsky",
                LocalDate.of(1960, 5, 13),
                c2,
                a1,
                Arrays.asList(course2, course3));
        studentDao.save(s2);

        LOG.info("System time zone is: {}", ZoneId.systemDefault());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final String action = req.getParameter("action");
        LOG.info("Requested action: {}", action);
        if (action == null || action.isEmpty()) {
            resp.getWriter().write("Empty action parameter.");
            return;
        }

        if (action.equals("findAll")) {
            findAll(req, resp);
        } else if (action.equals("add")) {
            addStudent(req, resp);
        } else if (action.equals("delete")) {
            deleteStudent(req, resp);
        } else if (action.equals("update")) {
            updateStudent(req, resp);
        } else if (action.equals("bornAfter")) {
            findAllBornAfter(req, resp);
        } else if (action.equals("summary")) {
            summary(req, resp);
        } else {
            resp.getWriter().write("Unknown action.");
        }
    }

    private void updateStudent(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        final Long id = Long.parseLong(req.getParameter("id"));
        LOG.info("Updating Student with id = {}", id);

        final Student existingStudent = studentDao.findById(id);
        if (existingStudent == null) {
            LOG.info("No Student found for id = {}, nothing to be updated", id);
        } else {
            existingStudent.setName(req.getParameter("name"));
            existingStudent.setSurname(req.getParameter("surname"));

            String dateStr = req.getParameter("date");
            LocalDate date = LocalDate.parse(dateStr);
            existingStudent.setDateOfBirth(date);

            String cidStr = req.getParameter("cid"); // computer id
            Long cid = Long.valueOf(cidStr);
            Computer computer = computerDao.findById(cid); // != null
            existingStudent.setComputer(computer);

            String aidStr = req.getParameter("aid"); // address od
            Long aid = Long.valueOf(aidStr);
            Address address = addressDao.findById(aid);
            existingStudent.setAddress(address);

            String courseName = req.getParameter("courseName");
            Course course = courseDao.findByName(courseName);
            existingStudent.getCourses().add(course);

            studentDao.update(existingStudent);
            LOG.info("Student object updated: {}", existingStudent);
        }

        // Return all persisted objects
        findAll(req, resp);
    }

    private void addStudent(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final Student p = new Student();
        p.setName(req.getParameter("name"));
        p.setSurname(req.getParameter("surname"));
        p.setDateOfBirth(LocalDate.parse(req.getParameter("date")));

        String cidStr = req.getParameter("cid"); // computer id
        Long cid = Long.valueOf(cidStr);
        Computer computer = computerDao.findById(cid); // != null
        p.setComputer(computer);

        String aidStr = req.getParameter("aid"); // address od
        Long aid = Long.valueOf(aidStr);
        Address address = addressDao.findById(aid);
        p.setAddress(address);

        String courseName = req.getParameter("courseName");
        Course course = courseDao.findByName(courseName);
        p.setCourses(Arrays.asList(course));

        studentDao.save(p);
        LOG.info("Saved a new Student object: {}", p);

        // Return all persisted objects
        findAll(req, resp);
    }

    private void deleteStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final Long id = Long.parseLong(req.getParameter("id"));
        LOG.info("Removing Student with id = {}", id);

        studentDao.delete(id);

        // Return all persisted objects
        findAll(req, resp);
    }

    private void findAllBornAfter(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String date = req.getParameter("date");
        LocalDate localDate = LocalDate.parse(date);

        final List<Student> result = studentDao.findBornAfter(localDate);

        LOG.info("Found {} objects", result.size());
        for (Student p : result) {
            resp.getWriter().write(p.toString() + "\n");
        }
    }

    private void findAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final List<Student> result = studentDao.findAll();
        LOG.info("Found {} objects", result.size());
        for (Student p : result) {
            resp.getWriter().write(p.toString() + "\n");
        }
    }

    private void summary(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final List<CourseSummary> result = courseDao.getCoursesSummary();
        LOG.info("Found {} objects", result.size());
        for (CourseSummary p : result) {
            resp.getWriter().write(p.toString() + "\n");
        }
    }
}




package com.infoshareacademy.web;


import com.infoshareacademy.dao.CourseDao;
import com.infoshareacademy.dao.TeacherDao;
import com.infoshareacademy.model.Course;
import com.infoshareacademy.model.CourseSummary;
import com.infoshareacademy.model.Teacher;
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
import java.util.Arrays;
import java.util.List;


@WebServlet(urlPatterns = "/teacher")
@Transactional
public class TeacherServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(TeacherServlet.class);

    @Inject
    private TeacherDao teacherDao;


    @Inject
    private CourseDao courseDao;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Course course4 = new Course("JJDD4");
        courseDao.save(course4);

        Course course5 = new Course("JJDD5");
        courseDao.save(course5);

        Teacher t1 = new Teacher("Adam", "Przybylowski", "99999999999", Arrays.asList(course4, course5));
        teacherDao.save(t1);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final String action = req.getParameter("action");
        logger.info("Requested action: {}", action);
        if (action == null || action.isEmpty()) {
            resp.getWriter().write("Empty action parameter.");
            return;
        }

        if (action.equals("findAll")) {
            findAll(req, resp);
        } else if (action.equals("add")) {
            addTeacher(req, resp);
        } else if (action.equals("delete")) {
            deleteTeacher(req, resp);
        } else if (action.equals("summary")) {
            summary(req, resp);
        } else {
            resp.getWriter().write("Unknown action.");
        }
    }

    private void addTeacher(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final Teacher p = new Teacher();
        p.setName(req.getParameter("name"));
        p.setSurname(req.getParameter("surname"));
        p.setPesel(req.getParameter("pesel"));


        String courseName = req.getParameter("courseName");
        Course course = courseDao.findByName(courseName);
        p.setCourses(Arrays.asList(course));

        teacherDao.save(p);
        logger.info("Saved a new Teacher object: {}", p);

        // Return all persisted objects
        findAll(req, resp);
    }


    private void deleteTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String pesel = (req.getParameter("pesel"));
        logger.info("Removing Student with id = {}", pesel);

        teacherDao.delete(pesel);

        // Return all persisted objects
        findAll(req, resp);
    }

    private void findAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final List<Teacher> result = teacherDao.findAll();
        logger.info("Found {} objects", result.size());
        for (Teacher p : result) {
            resp.getWriter().write(p.toString() + "\n");
        }
    }

    private void summary(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final List<CourseSummary> result = courseDao.getCoursesSummary();
        logger.info("Found {} objects", result.size());
        for (CourseSummary p : result) {
            resp.getWriter().write(p.toString() + "\n");
        }
    }

}



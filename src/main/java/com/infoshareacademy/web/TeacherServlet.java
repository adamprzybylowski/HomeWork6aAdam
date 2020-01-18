package com.infoshareacademy.web;


import com.infoshareacademy.dao.CourseDao;
import com.infoshareacademy.dao.TeacherDao;
import com.infoshareacademy.model.Course;
import com.infoshareacademy.model.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@WebServlet(urlPatterns = "/teacher")
@Transactional
public class TeacherServlet {

    private Logger logger = LoggerFactory.getLogger(TeacherServlet.class);

    @Inject
    private TeacherDao teacherDao;


    @Inject
    private CourseDao courseDao;


    private void addTeacher(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final Teacher p = new Teacher();
        p.setName(req.getParameter("name"));
        p.setSurname(req.getParameter("surname"));


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

}

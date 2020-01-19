package com.infoshareacademy.web;

import com.infoshareacademy.dao.CourseDao;
import com.infoshareacademy.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/course")
@Transactional
public class CourseServlet extends HttpServlet {

    private Logger LOG = LoggerFactory.getLogger(CourseServlet.class);

    @Inject
    private CourseDao courseDao;

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
            addCourse(req, resp);
        } else if (action.equals("delete")) {
            deleteCourse(req, resp);
        } else {
            resp.getWriter().write("Unknown action.");
        }
    }

    private void addCourse(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        final Course p = new Course();
        p.setName(req.getParameter("name"));

        courseDao.save(p);
        LOG.info("Saved a new Course object: {}", p);

        // Return all persisted objects
        findAll(req, resp);
    }

    private void deleteCourse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String name = req.getParameter("name");
        LOG.info("Removing Course with name = {}", name);

        courseDao.delete(name);

        // Return all persisted objects
        findAll(req, resp);
    }

    private void findAll(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final List<Course> result = courseDao.findAll();
        LOG.info("Found {} objects", result.size());
        for (Course p : result) {
            resp.getWriter().write(p.toString() + "\n");
        }
    }
}


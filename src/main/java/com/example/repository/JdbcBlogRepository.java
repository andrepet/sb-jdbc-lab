package com.example.repository;

import com.example.domain.Author;
import com.example.domain.Blog;
import com.example.domain.Comment;
import com.example.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcBlogRepository implements BlogRepository {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private DataSource dataSource;

    @Override
    public List<Blog> listBlogs() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, title FROM blogs")) {
            List<Blog> blogs = new ArrayList<>();
            while (rs.next()) blogs.add(rsBlog(rs));
            return blogs;
        } catch (SQLException e) {
            throw new BlogRepositoryException(e);
        }
    }

    @Override
    public Blog getBlog(long blogId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, title FROM blogs WHERE id = ?")) {
            ps.setLong(1, blogId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new BlogRepositoryException("No repository with ID " + blogId);
                else return rsBlog(rs);
            }
        } catch (SQLException e) {
            throw new BlogRepositoryException(e);
        }
    }

    @Override
    public List<Post> getEntriesIn(Blog blog) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, title, body, entryDate, blog_id " +
                     "FROM posts WHERE blog_id = ? ORDER BY entryDate DESC")) {
            ps.setLong(1, blog.id);
            try (ResultSet rs = ps.executeQuery()) {
                List<Post> posts = new ArrayList<>();
                while (rs.next()) posts.add(rsPost(rs));
                return posts;
            }
        } catch (SQLException e) {
            throw new BlogRepositoryException(e);
        }
    }

    @Override
    public Post getPost(long postId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, title, body, entryDate, blog_id FROM posts WHERE id = ?")) {
            ps.setLong(1, postId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new BlogRepositoryException("No repository with ID " + postId);
                else return rsPost(rs);
            }
        } catch (SQLException e) {
            throw new BlogRepositoryException(e);
        }
    }

    @Override
    public List<Comment> getCommentsFor(Post post) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, name, text, entryDate, post_id " +
                     "FROM comments WHERE post_id = ? ORDER BY entryDate DESC")) {
            ps.setLong(1, post.id);
            try (ResultSet rs = ps.executeQuery()) {
                List<Comment> comments = new ArrayList<>();
                while (rs.next()) comments.add(rsComment(rs));
                return comments;
            }
        } catch (SQLException e) {
            throw new BlogRepositoryException(e);
        }
    }

    @Override
    public Author getAuthorOf(Blog blog) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, name, username, password, blog_id " +
                     "FROM authors WHERE blog_id = ?")) {
            ps.setLong(1, blog.id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return new Author(0,"","","");
                else return rsAuthor(rs);
            }
        } catch (SQLException e) {
            throw new BlogRepositoryException(e);
        }
    }

    @Override
    public void addComment(long post_id, String name, String text, java.sql.Date date) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO comments(post_id, name, text, entryDate) VALUES (?,?,?,?) ", new String[] {"id"})) {
            ps.setLong(1, post_id);
            ps.setString(2, name);
            ps.setString(3, text);
            ps.setDate(4, date);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new BlogRepositoryException(e);
        }
    }


    @Override
    public void addBlog(String title) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO blogs(title) VALUES (?) ", new String[] {"id"})) {
            ps.setString(1, title);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new BlogRepositoryException(e);
        }
    }


    private Post rsPost(ResultSet rs) throws SQLException {
        return new Post(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("body"),
                rs.getTimestamp("entryDate").toLocalDateTime(),
                rs.getLong("blog_id")
        );
    }

    private Blog rsBlog(ResultSet rs) throws SQLException {
        return new Blog(rs.getLong("id"), rs.getString("title"));
    }

    private Comment rsComment(ResultSet rs) throws SQLException {
        return new Comment(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("text"),
                rs.getTimestamp("entryDate").toLocalDateTime(),
                rs.getLong("post_id")
        );
    }


    private Author rsAuthor(ResultSet rs) throws SQLException {
        return new Author(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("username"),
                rs.getString("password")
        );
    }
}

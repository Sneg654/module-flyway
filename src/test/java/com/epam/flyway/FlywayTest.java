package com.epam.flyway;

import java.util.List;

import com.epam.domain.Company;
import com.epam.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
class FlywayTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void databaseHasBeenInitialized() {

        jdbcTemplate.execute(
                "insert into test_user (id, username, first_name, last_name) values(1, 'ivanspresov', 'Ivan', 'Spresov')"
        );

        final List<User> users = jdbcTemplate
                .query("SELECT id, username, first_name, last_name FROM test_user", (rs, rowNum) -> new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ));
        for (User user : users) {
            System.out.println(user);
        }

        assertThat(users.isEmpty(), is(false));
    }

    @Test
    void checkThatCompanyAdded() {
        List<Company> companies = getCompanies();

        assertThat(companies.size(), is(1));
    }

    private List<Company> getCompanies() {
        return jdbcTemplate
                .query("SELECT id, name FROM company", (rs, rowNum) -> new Company(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
    }

    @Test
    void checkThatCompanyUserAddedToCompany() {
        List<Company> companies = getCompanies();

        assertThat(companies.size(), is(1));

        List<User> users = jdbcTemplate
                .query("SELECT tu.* " +
                        "FROM company_user cu " +
                        "JOIN test_user tu ON tu.id = cu.user_id " +
                        "WHERE cu.company_id = " + companies.get(0).getId(),
                        (rs, rowNum) -> new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ));

        assertThat(users.size(), is(1));
        assertThat(users.get(0).getUsername(), is("olgaspresova"));
    }
}
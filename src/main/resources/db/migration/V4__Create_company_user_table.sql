-- Create company script

-- DDL
CREATE TABLE company_user(
    company_id INT NOT NULL,
    user_id INT NOT NULL,
    CONSTRAINT FK_company_id FOREIGN KEY (company_id) REFERENCES company(id),
    CONSTRAINT FK_user_id FOREIGN KEY (user_id) REFERENCES test_user(id)
);
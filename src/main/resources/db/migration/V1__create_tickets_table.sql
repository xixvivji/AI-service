CREATE TABLE tickets (
    id BIGINT NOT NULL AUTO_INCREMENT,
    customer_email VARCHAR(120) NOT NULL,
    subject VARCHAR(180) NOT NULL,
    content TEXT NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    ai_category VARCHAR(60) NOT NULL,
    ai_summary TEXT NOT NULL,
    ai_draft_reply TEXT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
);

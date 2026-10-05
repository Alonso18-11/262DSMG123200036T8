-- Crea la tabla email y la llena con datos de ejemplo.
-- received = fecha de recepción en tiempo Unix (segundos).

CREATE TABLE email (
    id INTEGER NOT NULL,
    subject TEXT NOT NULL,
    sender TEXT NOT NULL,
    folder TEXT NOT NULL,
    starred INTEGER NOT NULL,
    read INTEGER NOT NULL,
    received INTEGER NOT NULL,
    PRIMARY KEY(id)
);

INSERT INTO email VALUES (1, 'Fool me once, shame on you', 'hagrid@example.com', 'inbox', FALSE, TRUE, 1735846239);
INSERT INTO email VALUES (2, 'Fooling around with SQL', 'neville@example.com', 'inbox', FALSE, FALSE, 1735890540);
INSERT INTO email VALUES (3, 'April Fools'' Day ideas', 'hermione@example.com', 'inbox', FALSE, TRUE, 1735960689);
INSERT INTO email VALUES (4, 'The fool on the hill', 'ginny@example.com', 'trash', TRUE, FALSE, 1736064460);
INSERT INTO email VALUES (5, 'Nobody''s fool', 'harry@example.com', 'trash', FALSE, TRUE, 1736150577);
INSERT INTO email VALUES (6, 'Lorem ipsum party', 'prof@university.edu', 'inbox', FALSE, TRUE, 1736226907);
INSERT INTO email VALUES (7, 'Meeting at 10', 'hermione@example.com', 'trash', FALSE, FALSE, 1736318088);
INSERT INTO email VALUES (8, 'Your weekly report', 'team@company.com', 'inbox', FALSE, TRUE, 1736393570);
INSERT INTO email VALUES (9, 'Invoice #2341', 'hermione@example.com', 'trash', FALSE, FALSE, 1736532266);
INSERT INTO email VALUES (10, 'Lunch tomorrow?', 'ginny@example.com', 'trash', FALSE, TRUE, 1736612999);
INSERT INTO email VALUES (11, 'Project kickoff', 'luna@example.com', 'inbox', TRUE, TRUE, 1736671994);
INSERT INTO email VALUES (12, 'Happy birthday!', 'team@company.com', 'inbox', FALSE, TRUE, 1736771420);
INSERT INTO email VALUES (13, 'Re: Holiday plans', 'luna@example.com', 'sent', FALSE, TRUE, 1736879900);
INSERT INTO email VALUES (14, 'You won a prize!!!', 'spam@deals.com', 'spam', FALSE, FALSE, 1736919120);
INSERT INTO email VALUES (15, 'Cheap watches', 'spam@deals.com', 'spam', FALSE, TRUE, 1736995773);
INSERT INTO email VALUES (16, 'Free cruise', 'spam@deals.com', 'spam', FALSE, FALSE, 1737113123);
INSERT INTO email VALUES (17, 'Quarterly results', 'neville@example.com', 'inbox', FALSE, FALSE, 1737218195);
INSERT INTO email VALUES (18, 'Code review request', 'hermione@example.com', 'inbox', FALSE, TRUE, 1737253319);
INSERT INTO email VALUES (19, 'Database class notes', 'luna@example.com', 'inbox', FALSE, FALSE, 1737389611);
INSERT INTO email VALUES (20, 'Room vs SQLite', 'ginny@example.com', 'inbox', FALSE, TRUE, 1737478115);
INSERT INTO email VALUES (21, 'Weekend hike', 'hagrid@example.com', 'inbox', FALSE, TRUE, 1737532600);
INSERT INTO email VALUES (22, 'Movie night', 'hagrid@example.com', 'inbox', FALSE, TRUE, 1737655478);
INSERT INTO email VALUES (23, 'Fwd: funny cats', 'hagrid@example.com', 'inbox', FALSE, FALSE, 1737694747);
INSERT INTO email VALUES (24, 'Reminder: dentist', 'prof@university.edu', 'sent', FALSE, TRUE, 1737810224);
INSERT INTO email VALUES (25, 'Flight confirmation', 'ron@example.com', 'sent', TRUE, TRUE, 1737880003);
INSERT INTO email VALUES (26, 'Hotel booking', 'harry@example.com', 'inbox', FALSE, FALSE, 1737970438);
INSERT INTO email VALUES (27, 'Password reset', 'harry@example.com', 'inbox', TRUE, FALSE, 1738102329);
INSERT INTO email VALUES (28, 'Welcome to the team', 'neville@example.com', 'trash', FALSE, FALSE, 1738176366);
INSERT INTO email VALUES (29, 'Gym membership', 'friend@mail.com', 'trash', FALSE, TRUE, 1738268504);
INSERT INTO email VALUES (30, 'Grocery list', 'ginny@example.com', 'sent', FALSE, TRUE, 1738334086);
INSERT INTO email VALUES (31, 'Survey: tell us more', 'ron@example.com', 'inbox', TRUE, TRUE, 1738389273);
INSERT INTO email VALUES (32, 'Last chance discount', 'spam@deals.com', 'spam', TRUE, FALSE, 1738467819);
INSERT INTO email VALUES (33, 'Re: Re: question about Kotlin', 'team@company.com', 'inbox', TRUE, TRUE, 1738588459);
INSERT INTO email VALUES (34, 'Coffee chat', 'harry@example.com', 'trash', TRUE, TRUE, 1738676513);
INSERT INTO email VALUES (35, 'New comment on your post', 'friend@mail.com', 'inbox', FALSE, TRUE, 1738761331);
INSERT INTO email VALUES (36, 'Security alert', 'hermione@example.com', 'sent', TRUE, TRUE, 1738861078);
INSERT INTO email VALUES (37, 'Pizza Friday', 'noreply@bank.com', 'sent', FALSE, TRUE, 1738931309);
INSERT INTO email VALUES (38, 'Book club', 'noreply@bank.com', 'inbox', FALSE, TRUE, 1738975827);
INSERT INTO email VALUES (39, 'Fool''s gold for sale', 'prof@university.edu', 'inbox', FALSE, FALSE, 1739062744);
INSERT INTO email VALUES (40, 'Final exam schedule', 'luna@example.com', 'trash', FALSE, FALSE, 1739179824);

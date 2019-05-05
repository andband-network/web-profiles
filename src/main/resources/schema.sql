CREATE TABLE IF NOT EXISTS `profile` (
  `id` varchar(255) NOT NULL,
  `created_date` datetime NOT NULL,
  `account_id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `bio` varchar(255) DEFAULT NULL,
  `image_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `connection` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime NOT NULL,
  `profile_id` varchar(255) NOT NULL,
  `connected_profile_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `pending_connection` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime NOT NULL,
  `profile_id` varchar(255) NOT NULL,
  `connected_profile_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `message` (
  `id` varchar(255) NOT NULL,
  `created_date` datetime NOT NULL,
  `sender_profile_id` varchar(255) NOT NULL,
  `receiver_profile_id` varchar(255) NOT NULL,
  `subject` varchar(255) NOT NULL,
  `body` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


CREATE OR REPLACE VIEW `vw_message` AS
    SELECT
		m.id AS `id`,
        m.created_date AS `created_date`,
        m.sender_profile_id AS `sender_profile_id`,
        p.name AS `sender_profile_name`,
        m.receiver_profile_id AS `receiver_profile_id`,
        ms.receiver_profile_name AS `receiver_profile_name`,
        m.subject AS `subject`,
        m.body AS `body`
    FROM
        message m
            JOIN
        profile p ON m.sender_profile_id = p.id
            JOIN
        (SELECT
            m.id, p.name AS `receiver_profile_name`
        FROM
            message m
        JOIN profile p ON m.receiver_profile_id = p.id) ms ON ms.id = m.id
    ORDER BY created_date;

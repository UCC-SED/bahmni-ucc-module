CREATE TABLE `openmrs`.`notifications_rules` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `notification_sql` LONGTEXT NULL,
  `notification_reaction` VARCHAR(45) NULL,
  `status` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));

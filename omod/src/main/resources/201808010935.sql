CREATE TABLE `openmrs`.`nhif_authentication_header` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `header` VARCHAR(500) NULL,
  `issue_date` DATETIME NULL,
  `expire_date` DATETIME NULL,
  PRIMARY KEY (`id`));

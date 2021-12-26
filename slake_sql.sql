-- MySQL Script generated by MySQL Workbench
-- sam. 25 déc. 2021 15:52:38
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema 
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema Slake
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Slake` DEFAULT CHARACTER SET utf8 ;
USE `Slake` ;

-- -----------------------------------------------------
-- Table `Slake`.`Channel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Slake`.`Channel` (
                                                `idChannel` INT NOT NULL AUTO_INCREMENT,
                                                `Server_idServer` INT NOT NULL,
                                                `name` VARCHAR(45) NULL,
                                                PRIMARY KEY (`idChannel`, `Server_idServer`),
                                                INDEX `fk_Channel_Server1_idx` (`Server_idServer` ASC) VISIBLE,
                                                CONSTRAINT `fk_Channel_Server1`
                                                    FOREIGN KEY (`Server_idServer`)
                                                        REFERENCES `Slake`.`Server` (`idServer`)
                                                        ON DELETE CASCADE
                                                        ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Slake`.`Message`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Slake`.`Message` (
                                                `idMessage` INT NOT NULL AUTO_INCREMENT,
                                                `content` VARCHAR(255) NOT NULL,
                                                `Channel_idChannel` INT NOT NULL,
                                                `date` VARCHAR(45) NULL,
                                                `User_pseudo` VARCHAR(45) NOT NULL,
                                                PRIMARY KEY (`idMessage`, `User_pseudo`),
                                                INDEX `fk_Message_Channel1_idx` (`Channel_idChannel` ASC) VISIBLE,
                                                INDEX `fk_Message_User1_idx` (`User_pseudo` ASC) VISIBLE,
                                                CONSTRAINT `fk_Message_Channel1`
                                                    FOREIGN KEY (`Channel_idChannel`)
                                                        REFERENCES `Slake`.`Channel` (`idChannel`)
                                                        ON DELETE CASCADE
                                                        ON UPDATE NO ACTION,
                                                CONSTRAINT `fk_Message_User1`
                                                    FOREIGN KEY (`User_pseudo`)
                                                        REFERENCES `Slake`.`User` (`pseudo`)
                                                        ON DELETE CASCADE
                                                        ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Slake`.`Server`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Slake`.`Server` (
                                               `idServer` INT NOT NULL AUTO_INCREMENT,
                                               `name` VARCHAR(45) NOT NULL,
                                               `userCounter` INT NOT NULL,
                                               PRIMARY KEY (`idServer`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Slake`.`Server_has_User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Slake`.`Server_has_User` (
                                                        `Server_idServer` INT NOT NULL,
                                                        `User_pseudo` VARCHAR(45) NOT NULL,
                                                        PRIMARY KEY (`Server_idServer`, `User_pseudo`),
                                                        INDEX `fk_Server_has_User_User1_idx` (`User_pseudo` ASC) VISIBLE,
                                                        INDEX `fk_Server_has_User_Server1_idx` (`Server_idServer` ASC) VISIBLE,
                                                        CONSTRAINT `fk_Server_has_User_Server1`
                                                            FOREIGN KEY (`Server_idServer`)
                                                                REFERENCES `Slake`.`Server` (`idServer`)
                                                                ON DELETE CASCADE
                                                                ON UPDATE NO ACTION,
                                                        CONSTRAINT `fk_Server_has_User_User1`
                                                            FOREIGN KEY (`User_pseudo`)
                                                                REFERENCES `Slake`.`User` (`pseudo`)
                                                                ON DELETE CASCADE
                                                                ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Slake`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Slake`.`User` (
                                             `pseudo` VARCHAR(45) NOT NULL,
                                             UNIQUE INDEX `pseudo_UNIQUE` (`pseudo` ASC) VISIBLE,
                                             PRIMARY KEY (`pseudo`))
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
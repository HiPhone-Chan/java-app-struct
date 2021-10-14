package com.chf.app.web;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.chf.app.constants.SystemConstants;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(SystemConstants.BASE_PACKAGE);

        noClasses().that().resideInAnyPackage(SystemConstants.BASE_PACKAGE + ".service..").or()
                .resideInAnyPackage(SystemConstants.BASE_PACKAGE + ".repository..").should().dependOnClassesThat()
                .resideInAnyPackage(".." + SystemConstants.BASE_PACKAGE + ".web..")
                .because("Services and repositories should not depend on web layer").check(importedClasses);
    }
}

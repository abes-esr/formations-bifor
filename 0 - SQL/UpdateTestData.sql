/**
 * Mets à jour les informations sur les stagiaires pour cacher les emails
 */

DECLARE
    CURSOR cv_employee_cursor IS
        SELECT * FROM STAGIAIRE;

BEGIN
    FOR elem IN cv_employee_cursor LOOP
        UPDATE STAGIAIRE SET MAIL = CONCAT(CONCAT('comtet', (select dbms_random.random from dual)), '@abes.fr'), MAILCOORDINATEUR = 'comtet@abes.fr' WHERE IDSTAGIAIRE = elem.IDSTAGIAIRE;
    END LOOP;
END;

import extensions.File;
import extensions.CSVFile;

class CoursDada extends Program { 

    /**
     * Constantes globales liées à la sauvegarde des fichiers
     */
    final String NOM_FICHIER_SAUVEGARDE = "sauvegardeJoueurs.csv";
    final String CHEMIN_FICHIER_SAUVEGARDE = "../sauvegardes/sauvegardeJoueurs.csv";
    final String CHEMIN_DOSSIER_SAUVEGARDE = "../sauvegardes";
    final String CHEMIN_DOSSIER_QUESTIONS = "../questions/questions";
    final int IDX_NOM_SAUVEGARDE = 0;
    final int IDX_POSITION_SAUVEGARDE = 1;
    final int IDX_SCORE_SAUVEGARDE = 2;

    final int NOMBRE_CASE_PLATEAU = 12;

    final String RESET_COLOR = "\u001B[0m";
    final String ROUGE = "\u001B[31m";
    final String VERT = "\u001B[32m";
    final String JAUNE = "\u001B[33m";
    final String BLEU = "\u001B[34m";
    final String VIOLET = "\u001B[35m";
    final String CYAN = "\u001B[36m";
    final String BLANC = "\u001B[37m";

    boolean running = true;

    /**
     * Fonction d'algorithme principal
     */

    void _algorithm() {
        String[] facesDe = recupererFacesDe();
        println(facesDe[5]);
    }

    void algorithm() {
        /**
         * Récupération des données du jeu
         */
        fichierSauvegardeEstCree();
        String[][] contenuSauvegarde = recupererContenuCSV(CHEMIN_FICHIER_SAUVEGARDE);

        /**
         * Lancement du jeu et récupération des données correspondantes aux joueur
         */    
        passerLignes(50);
        clearScreen();
        String nom = lancerJeu();
        Joueur joueur = affecterJoueur(contenuSauvegarde, nom);
        contenuSauvegarde = recupererContenuCSV(CHEMIN_FICHIER_SAUVEGARDE);
        println();
        println("Bienvenue " + joueur.nom + " !");
        if (joueur.meilleurScore > 0) {
            println("Ton meilleur score est de : " + joueur.meilleurScore + " ! ");
            println("Arriveras-tu à le battre ? ");
        } else {
            println("Oh on dirait bien que c'est ta première partie ! ");
        }
        println();
        println("Clique sur la touche \"Entrée\" pour lancer la partie ! ");
        readString();
        clearScreen();

        final int[] THEMES_CASES = genererThemesCase();
        final String TITRE_JEU = VERT + supprimerCaractereIdx(length(lireFichier("../patterns/titre.txt")) - 1, lireFichier("../patterns/titre.txt")) + RESET_COLOR;
        final String[] FACES_DE = recupererFacesDe();

        String[][] casesPlateau = genererCasesPlateau(joueur.position, THEMES_CASES);
        String plateau = assemblerPlateau(casesPlateau);

        String affichageDe = "";
        dessinerJeu(TITRE_JEU, plateau, affichageDe);
        println("Appuyez sur la touche \"Entrée\" pour lancer le dé");
        readString();

        while (running) {
            int valeurDe = entierRandom(1, 7);
            affichageDe = ROUGE + FACES_DE[valeurDe - 1] + RESET_COLOR;
            deplacerJoueur(joueur, valeurDe);
            casesPlateau = genererCasesPlateau(joueur.position, THEMES_CASES);
            plateau = assemblerPlateau(casesPlateau);
            clearScreen();
            dessinerJeu(TITRE_JEU, plateau, affichageDe);

            print("REPONSE : ");
            int reponse = readInt();

            if (!(reponse == 1)) {
                joueur.position = 0;
                casesPlateau = genererCasesPlateau(joueur.position, THEMES_CASES);
                plateau = assemblerPlateau(casesPlateau);
                clearScreen();
                dessinerJeu(TITRE_JEU, plateau, affichageDe);
                println("Appuyez sur la touche \"Entrée\" pour lancer le dé");
            }

            readString();
            if (joueur.position == (NOMBRE_CASE_PLATEAU - 1)) {
                running = false;
            }
            clearScreen();
        }

        println("FINI");
    }

    /**
     * Fonction d'affichage du jeu 
     */

    String lancerJeu() {
        /* Lance l'affichage du jeu et renvoye le nom du joueur */
        println("*** Bienvenue dans CoursDada ! ***");
        print("Entre ton nom de joueur (Sans virgules ! ) : ");
        String nom = readString();
        while (!estPrenomValide(nom)) {
            println("Ton prénom est invalide :/ Oublie pas que les virgules ne sont pas acceptées ! ");
            print("Allez on recommence ! Entre un nom valide : ");
            nom = readString();
        }
        return nom;
    }
    
    void passerLignes(int nbLignesAPasser) {
        for (int cptLigne = 0; cptLigne < nbLignesAPasser; cptLigne++) {
            print('\n');
        }
    }

    String[] recupererFacesDe() {
        String chaineDes = lireFichier("../patterns/de_pattern.txt");
        String[] tableauFacesDe = new String[6];
        String chaine = "";
        int cpt = 0;
        for (int idxChaineDes = 0; idxChaineDes < (length(chaineDes) - 1); idxChaineDes++) {
            if (((charAt(chaineDes, idxChaineDes) == '\n') && (charAt(chaineDes, idxChaineDes + 1) == '\n')) || (idxChaineDes == (idxChaineDes - 3))) {
                tableauFacesDe[cpt] = chaine;
                cpt++;
                chaine = "";
            } else if (!(equals(chaine,"") && (charAt(chaineDes, idxChaineDes) == '\n'))) {
                chaine = chaine + charAt(chaineDes, idxChaineDes);
            }
        }
        tableauFacesDe[5] = chaine;
        return tableauFacesDe;
    }

    /**
     * Fonction liées au plateau
     */
    String[] creerPlateau() {
        String[] plateau = new String[30];

        return plateau;
    }

    void dessinerJeu(String titre, String plateau, String affichageDe) {
        println(titre);
        passerLignes(3);
        println(plateau);
        passerLignes(1);
        println(affichageDe);
        passerLignes(1);
    }

    /**
     * Fonctions liées à la sauvegarde de la partie du joueur.
     */

    void fichierSauvegardeEstCree () {
        boolean estCree = false;
        String[] allFilesFromDirectory = getAllFilesFromDirectory(CHEMIN_DOSSIER_SAUVEGARDE);
        int indice = 0;
        while (indice < length(allFilesFromDirectory) && !estCree) {
            if (equals(allFilesFromDirectory[indice], NOM_FICHIER_SAUVEGARDE)) {
                estCree = true;
            }
            indice = indice + 1;
        }
        if (!estCree) {
            creerCSVSauvegardeJoueurs();
        }
    }

    void creerCSVSauvegardeJoueurs() {
        String[][] joueursCSV = new String[1][3];
        joueursCSV[0][0] = "nomDuJoueur";
        joueursCSV[0][1] = "positionDernièrePartie";
        joueursCSV[0][2] = "meilleurScore";
        saveCSV(joueursCSV, CHEMIN_FICHIER_SAUVEGARDE);
        println("Fichier créé");
    }

    String[][] recupererContenuCSV(String cheminFichier) {
        CSVFile table = loadCSV(cheminFichier);
        int nombreLigneTable = rowCount(table);
        int nombreColonneTable = columnCount(table);
        String[][] contenuTable = new String[nombreLigneTable][nombreColonneTable];
        for (int idxLigne = 0; idxLigne < nombreLigneTable; idxLigne++) {
            for (int idxColonne = 0; idxColonne < nombreColonneTable; idxColonne++) {
                contenuTable[idxLigne][idxColonne] = getCell(table, idxLigne, idxColonne);
            }
        }
        return contenuTable;
    }

    void ajouterJoueurASauvegarde(Joueur joueur, String[][] contenuSauvegarde) {
        int nombreLigneTable = length(contenuSauvegarde);
        String[][] contenuNouvelleSauvegarde = new String[nombreLigneTable + 1][3];

        for (int idxLigne = 0; idxLigne < nombreLigneTable; idxLigne++) {
            contenuNouvelleSauvegarde[idxLigne] = contenuSauvegarde[idxLigne];
        }

        contenuNouvelleSauvegarde[nombreLigneTable][IDX_NOM_SAUVEGARDE] = joueur.nom;
        contenuNouvelleSauvegarde[nombreLigneTable][IDX_POSITION_SAUVEGARDE] = "" + joueur.position;
        contenuNouvelleSauvegarde[nombreLigneTable][IDX_SCORE_SAUVEGARDE] = "" + joueur.meilleurScore;

        saveCSV(contenuNouvelleSauvegarde, CHEMIN_FICHIER_SAUVEGARDE);
    }


    /**
     * Fonctions liées au Joueur
     */

    String toString(Joueur joueur) {
        return "Joueur : " + joueur.nom + " - Dernière position en jeu : " + joueur.position + " - Meilleur score du joueur : " + joueur.meilleurScore;
    }

    Joueur newJoueur(String nom, int dernierePosition, int meilleurScore) {
        Joueur joueur = new Joueur();
        joueur.nom = nom;
        joueur.position = dernierePosition;
        joueur.meilleurScore = meilleurScore;
        return joueur;
    }

    Joueur affecterJoueur(String[][] contenuSauvegarde, String nomJoueur) {
        Joueur joueur;
        int idxJoueurSauvegarde = idxStringDansTab(contenuSauvegarde, nomJoueur, IDX_NOM_SAUVEGARDE);
        if (idxJoueurSauvegarde > 0) {
            joueur = newJoueur( nomJoueur, 
                                stringToInt(contenuSauvegarde[idxJoueurSauvegarde][IDX_POSITION_SAUVEGARDE]), 
                                stringToInt(contenuSauvegarde[idxJoueurSauvegarde][IDX_SCORE_SAUVEGARDE]));
        } else {
            joueur = newJoueur(nomJoueur, 0, -1);
            ajouterJoueurASauvegarde(joueur, contenuSauvegarde);
        }
        return joueur;
    }

    void deplacerJoueur(Joueur joueur, int valeur) {
        if ((joueur.position + valeur) == (NOMBRE_CASE_PLATEAU - 1)) {
            running = false;
        } else if ((joueur.position + valeur) < NOMBRE_CASE_PLATEAU) {
            joueur.position = joueur.position + valeur;
        } else {
            joueur.position = 2 * (NOMBRE_CASE_PLATEAU - 1) - valeur - joueur.position;
        }
    }


    /**
     * Fonctions liées aux questions
     */

    String toString(Question question) {
        return "Question n° : " + question.num + " - Matière : " + question.matiere + " - Intitulé : " + question.intitule + " - Réponse : " + question.reponse;
    }

    Question newQuestion(int num, String matiere, String intitule, String reponse) {
        Question question = new Question();
        question.num = num;
        question.matiere = matiere;
        question.intitule = intitule;
        question.reponse = reponse;
        return question;
    }

    void testNewQuestion(){
        String matiere = "Francais";
        String[][] contenuQuestions = recupererContenuCSV(CHEMIN_DOSSIER_QUESTIONS + matiere + ".csv");
        println(toString(contenuQuestions));
        int indiceQuestion = 1;
        Question question = newQuestion(indiceQuestion, contenuQuestions[indiceQuestion][0], contenuQuestions[indiceQuestion][1],contenuQuestions[indiceQuestion][2]);
        println(toString(question));
    }

    Question obtenirQuestionAuHasardDansMatiere(String matiere) {
        String[][] contenuQuestions = recupererContenuCSV(CHEMIN_DOSSIER_QUESTIONS + matiere + ".csv");
        println(toString(contenuQuestions));
        int nombreQuestions = length(contenuQuestions)-1;
        int indiceQuestion= entierRandom(0, nombreQuestions+1); 
        Question question = newQuestion(indiceQuestion, contenuQuestions[indiceQuestion][0], contenuQuestions[indiceQuestion][1],contenuQuestions[indiceQuestion][2]);
        println(toString(question));
        return question;
    }

    /**
     * Fonctions de vérification de la saisie utilisateur
     */

    boolean estPrenomValide(String chaine) {
        return !(length(chaine) <= 0 || length(chaine) > 20 || charEstDansString(chaine, ',') || charEstDansString(chaine, '\n'));
    }

    /**
     * Fonctions de gestion du plateau de jeu
     */

    int[] genererThemesCase() {
        int[] themes = new int[NOMBRE_CASE_PLATEAU - 2];
        for (int idxCase = 0; idxCase < length(themes); idxCase++) {
            themes[idxCase] = entierRandom(0, 5);
        }
        return themes;
    }

    String[][] genererCasesPlateau(int positionJoueur, int[] themesCases) {
        /**
         * Indices des cases thèmes ouvertes sur la gauche dans le tableau de patterns
         *      7 --> 11
         * Indices des cases thèmes ouvertes sur la droite dans le tableau de patterns
         *      12 --> 16
         */
        final int IDX_CASE_DEPART = 0;
        final int IDX_CASE_FIN = 1;
        final int IDX_CASE_JOUEUR = 20;
        final int IDX_CASE_JOUEUR_GAUCHE = 21;
        final int IDX_CASE_JOUEUR_DROITE = 22;

        String[][] indicesPlateau = new String[NOMBRE_CASE_PLATEAU][7];
        String[][] patternsPlateau = recupererContenuCSV("../patterns/cases_pattern.csv");

        indicesPlateau[0] = patternsPlateau[IDX_CASE_DEPART];
        indicesPlateau[NOMBRE_CASE_PLATEAU - 1] = patternsPlateau[IDX_CASE_FIN];
        if (positionJoueur == (NOMBRE_CASE_PLATEAU - 1)) {
            indicesPlateau[positionJoueur] = patternsPlateau[IDX_CASE_JOUEUR_GAUCHE];
        } else if (positionJoueur == 0) {
            indicesPlateau[positionJoueur] = patternsPlateau[IDX_CASE_JOUEUR_DROITE];
        } else {
            indicesPlateau[positionJoueur] = patternsPlateau[IDX_CASE_JOUEUR];
        }
        for (int idxCase = 1; idxCase < positionJoueur; idxCase++) {
            indicesPlateau[idxCase] = patternsPlateau[themesCases[idxCase - 1] + 7];
        }
        for (int idxCase = positionJoueur + 1; idxCase < (NOMBRE_CASE_PLATEAU - 1); idxCase++) {
            indicesPlateau[idxCase] = patternsPlateau[themesCases[idxCase - 1] + 12];
        }

        return indicesPlateau;
    }

    String[][] deplacerJoueur(int anciennePosition, int positionJoueur, String[][] plateau) {
        return new String[0][0];
    }

    String assemblerPlateau(String[][] casesPlateau) {
        String plateau = "";
        for (int idxLigne = 0; idxLigne < length(casesPlateau[0]); idxLigne++) {
            for (int idxCase = 0; idxCase < length(casesPlateau); idxCase++) {
                if (idxCase == 0 || idxCase == (length(casesPlateau) - 1)) {
                    plateau = plateau + BLEU + casesPlateau[idxCase][idxLigne] + RESET_COLOR;
                } else if (idxCase == 1) {
                    String ligne = casesPlateau[idxCase][idxLigne];
                    plateau = plateau + BLEU + charAt(ligne, 0) + RESET_COLOR + substring(ligne, 1, length(ligne)); 
                } else {
                    plateau = plateau + casesPlateau[idxCase][idxLigne];
                }
            }
            plateau = plateau + '\n';
        }
        plateau = supprimerCaractereIdx(length(plateau) - 1, plateau);
        return plateau;
    }

    /**
     * Fonctions utiles
     */

    String toString(String[] tab) {
        String chaine = "";
        for (int idxTab = 0; idxTab < length(tab) - 1; idxTab++) {
            chaine = chaine + tab[idxTab] + " - ";
        }
        chaine = chaine + tab[length(tab) - 1];
        return chaine;
    }

    String toString(String[][] tab) {
        String chaine = "";
        for (int idxLigne = 0; idxLigne < length(tab); idxLigne++) {
            for (int idxColonne = 0; idxColonne < length(tab[idxLigne]); idxColonne++) {
                if (idxColonne == (length(tab[idxLigne]) - 1)) {
                    chaine = chaine + tab[idxLigne][idxColonne] + '\n';
                } else {
                    chaine = chaine + tab[idxLigne][idxColonne] + " - ";
                }
            }
        }
        return chaine;
    }

    String toString(int[] tab) {
        String chaine = "";
        for (int idxTab = 0; idxTab < (length(tab) - 1); idxTab++) {
            chaine = chaine + tab[idxTab] + " - ";
        }
        chaine = chaine + tab[length(tab) - 1];
        return chaine;
    }

    int idxStringDansTab(String[][] tab, String chaine, int idxColonneCiblee) {
        /**
         * Renvoie l'indice de la première occurence d'une chaine de caractere dans un 
         * tableau pour une colonne donnée. Si la chaine n'existe pas, -1 est renvoyé. 
         */
        int indice = -1;
        int idxLigne = 0;
        boolean trouve = false;
        chaine = toUpperCase(chaine);

        while (!trouve && idxLigne < length(tab)) {
            if (equals(toUpperCase(tab[idxLigne][idxColonneCiblee]), chaine)) {
                trouve = true;
                indice = idxLigne;
            } else {
                idxLigne = idxLigne + 1;
            }
        }

        return indice;
    }

    boolean charEstDansString(String chaine, char car) {
        boolean trouve = false;
        int idxChaine = 0;
        while (!trouve && (idxChaine < length(chaine))) {
            if (car == charAt(chaine, idxChaine)) {
                trouve = true;
            }
            idxChaine = idxChaine + 1;
        }
        return trouve;
    }

    void testCharEstDansString() {
        assertTrue(charEstDansString("bonjour", 'b'));
        assertFalse(charEstDansString("test", 'a'));
    }

    String lireFichier(String cheminFichier) {
        File fichierElements = newFile(cheminFichier);
        String chaine = "";
        while (ready(fichierElements)) {
            chaine = chaine + readLine(fichierElements) + '\n';
        }
        return chaine;
    }

    int entierRandom(int borneGauche, int borneDroite) {
        /**
         * Renvoie un entier compris dans l'intervalle borneGauche inclus à borneDroite exclus. 
         * Attention : borneDroite doit être supérieur ou égal à borneGauche sinon 0 est retourné.
         */

        if (borneDroite < borneGauche) {
            return 0;
        }

        double alea = random();
        return (int)(alea * (borneDroite - borneGauche) + borneGauche);
    }

    String supprimerCaractereIdx(int idxASupprimer, String chaine) {
        String chaineModifiee = "";
        for (int idxChaine = 0; idxChaine < length(chaine); idxChaine ++) {
            if (idxChaine != idxASupprimer) {
                chaineModifiee = chaineModifiee + charAt(chaine, idxChaine);
            }
        }
        return chaineModifiee;
    }

}
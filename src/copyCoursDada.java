
import extensions.File;
import extensions.CSVFile;

class copyCoursDada extends Program { 

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

    final int IDX_CASE_DEPART = 0;
    final int IDX_CASE_FIN = 1;
    final int IDX_CASE_JOUEUR = 20;
    final int IDX_CASE_JOUEUR_DROITE = 21;

    final String RESET_COLOR = "\u001B[0m";
    final String VERT = "\u001B[32m";

    /**
     * Fonction d'algorithme principal
     */

    void algorithm() {
        String[][] casesPlateau = genererCasesPlateauAleatoire(5);
        String plateau = assemblerPlateau(casesPlateau);
        println(plateau);
        String[][] plateauTab = txtCaseToString("cases_pattern.txt");
        String[][] deTab = txtCaseToString("de_pattern.txt");
        println(toString(plateauTab));
        println(plateauTab[0][0]+"  "+plateauTab[8][0]);
        println(plateauTab[0][1]+"  "+plateauTab[8][1]);
        println(plateauTab[0][2]+"  "+plateauTab[8][2]);
        println(plateauTab[0][3]+"  "+plateauTab[8][3]);
        println(plateauTab[0][4]+"  "+plateauTab[8][4]);
        println(plateauTab[0][5]+"  "+plateauTab[8][5]);
        println(plateauTab[0][6]+"  "+plateauTab[8][6]);

        println(toString(deTab));
        println(deTab[0][0]+"  "+deTab[2][0]);
        println(deTab[0][1]+"  "+deTab[2][1]);
        println(deTab[0][2]+"  "+deTab[2][2]);
        println(deTab[0][3]+"  "+deTab[2][3]);
        println(deTab[0][4]+"  "+deTab[2][4]);
        println(deTab[0][5]+"  "+deTab[2][5]);
        println(deTab[0][6]+"  "+deTab[2][6]);
    }

    void _algorithm() {
        /**
         * Constantes couleurs utilisés lors de l'affichage du jeu pour ce dernier soit coloré.
         */

        /**
         * Récupération des données du jeu
         */
        fichierSauvegardeEstCree();
        String[][] contenuSauvegarde = recupererContenuCSV(CHEMIN_FICHIER_SAUVEGARDE);

        /**
         * Lancement du jeu et récupération des données correspondantes aux joueur
         */    
        // Boucle permettant de s'assurer que le nettoyage amène l'écriture en bas de la page
        for (int cpt = 0; cpt < 100; cpt++) {
            println();
        }
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
        println("Clique sur une touche pour commencer le jeu ! ");
        readString();
        clearScreen();

        println(VERT + lireFichier("../patterns/titre.txt") + RESET_COLOR);
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

    /**
     * Fonction liées au plateau
     */
    String[] creerPlateau() {
        String[] plateau = new String[30];

        return plateau;
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

    void testObtenirQuestionAuHasardDansMatiere() {
        println(obtenirQuestionAuHasardDansMatiere("Francais"));
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

    String[][] genererCasesPlateauAleatoire(int positionJoueur) {

        /**
         * Indices des cases thèmes ouvertes sur la gauche dans le tableau de patterns
         *      7 --> 11
         * Indices des cases thèmes ouvertes sur la droite dans le tableau de patterns
         *      12 --> 16
         */

        final int NOMBRE_CASE_PLATEAU = 12;
        String[][] indicesPlateau = new String[NOMBRE_CASE_PLATEAU][7];
        String[][] patternsPlateau = recupererContenuCSV("../patterns/cases_pattern.csv");

        indicesPlateau[0] = patternsPlateau[IDX_CASE_DEPART];
        indicesPlateau[NOMBRE_CASE_PLATEAU - 1] = patternsPlateau[IDX_CASE_FIN];
        if (positionJoueur == (NOMBRE_CASE_PLATEAU - 1)) {
            indicesPlateau[positionJoueur] = patternsPlateau[IDX_CASE_JOUEUR_DROITE];
        } else {
            indicesPlateau[positionJoueur] = patternsPlateau[IDX_CASE_JOUEUR];
        }
        for (int idxCase = 1; idxCase < positionJoueur; idxCase++) {
            indicesPlateau[idxCase] = patternsPlateau[entierRandom(7,12)];
        }
        for (int idxCase = positionJoueur + 1; idxCase < (NOMBRE_CASE_PLATEAU - 1); idxCase++) {
            indicesPlateau[idxCase] = patternsPlateau[entierRandom(12,17)];
        }

        return indicesPlateau;
    }

    String assemblerPlateau(String[][] casesPlateau) {
        String plateau = "";
        for (int idxLigne = 0; idxLigne < length(casesPlateau[0]); idxLigne++) {
            for (int idxCase = 0; idxCase < length(casesPlateau); idxCase++) {
                plateau = plateau + casesPlateau[idxCase][idxLigne];
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

    int nbLignesFile(File file) {
        int nbLignesFile = 0;
        while (ready(file)) {
            readLine(file);
            nbLignesFile = nbLignesFile + 1;
        }
        return nbLignesFile;
    }

    String[][] txtCaseToString(String fichier) {
        File file = newFile("../patterns/"+fichier);
        int nbLignesFile = nbLignesFile(file);
        file = newFile("../patterns/"+fichier);
        String[][] fileAsString = new String[nbLignesFile/7][7];
        boolean finCase = false;
        int indiceLigne = 0;
        int indiceColonne = 0;
        while (ready(file)) {
            String ligne = readLine(file);
            println(ligne);
            println(equals(ligne,"§-----------§") || equals(ligne,"-----------§"));
            if (equals(ligne,"§-----------§") || equals(ligne,"-----------§") ) {
                finCase = !finCase;
            }
            fileAsString[indiceLigne][indiceColonne] = ligne;
            indiceColonne = indiceColonne + 1;
            if (!finCase) {
                indiceLigne = indiceLigne + 1;
                indiceColonne = 0;
            }
            println(indiceLigne + "    " + indiceColonne);
        }
        return fileAsString;
    }
}
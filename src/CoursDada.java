
import extensions.File;
import extensions.CSVFile;

class CoursDada extends Program { 

    final String NOM_FICHIER_SAUVEGARDE = "sauvegardeJoueurs.csv";
    final String CHEMIN_FICHIER_SAUVEGARDE = "../sauvegardes/sauvegardeJoueurs.csv";
    final String CHEMIN_DOSSIER_SAUVEGARDE = "../sauvegardes";
    final String CHEMIN_DOSSIER_QUESTIONS = "../questions/questions";
    final int IDX_NOM_SAUVEGARDE = 0;
    final int IDX_POSITION_SAUVEGARDE = 1;
    final int IDX_SCORE_SAUVEGARDE = 2;

    /**
     * Fonction d'algorithme principal
     */

    void algorithm() {
        fichierSauvegardeEstCree();
        String[][] contenuSauvegarde = recupererContenuCSV(CHEMIN_FICHIER_SAUVEGARDE);

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
    }

    /**
     * Fonction d'affichage du jeu 
     */

    String lancerJeu() {
        /* Lance l'affichage du jeu et renvoye le nom du joueur */
        println("*** Bienvenue dans CoursDada ! ***");
        print("Entre ton nom de joueur (Sans virgules ! ) : ");
        String nom = readString();
        while (charEstDansString(nom, ',') || charEstDansString(nom, '\n')) {
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
        int indiceQuestion = (int) (random()*nombreQuestions) + 1;
        Question question = newQuestion(indiceQuestion, contenuQuestions[indiceQuestion][0], contenuQuestions[indiceQuestion][1],contenuQuestions[indiceQuestion][2]);
        println(toString(question));
        return question;
    }

    void testObtenirQuestionAuHasardDansMatiere() {
        println(obtenirQuestionAuHasardDansMatiere("Francais"));
    }


    /**
     * Fonctions utiles
     */

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

}

import gestionClient.ConnexionClient;

import java.util.Scanner;

public class MainConnexionClient {
    public static void main(String[] args) {
        ConnexionClient connexionClient = new ConnexionClient();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bienvenue dans l'application.");
        System.out.println("1. Se connecter");
        System.out.println("2. S'inscrire");
        System.out.print("Choisissez une option : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer le saut de ligne

        switch (choix) {
            case 1:
                connexionClient.seConnecter();
                break;
            case 2:
                connexionClient.inscriptionClient();
                break;
            default:
                System.out.println("Option invalide. Veuillez réessayer.");
        }
    }
}

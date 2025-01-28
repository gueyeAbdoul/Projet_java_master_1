import gestionClient.Client;
import gestionClient.GestionClient;
import gestionCommande.Commande;
import gestionCommande.GestionCommande;
import gestionFacturation.Facture;
import gestionFacturation.GestionFacture;
import gestionProduit.GestionProduit;
import gestionProduit.Produit;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        GestionClient gestionClient = new GestionClient();
        GestionProduit gestionProduit = new GestionProduit();
        GestionCommande gestionCommande = new GestionCommande();
        GestionFacture gestionFacture = new GestionFacture();

        System.out.println("Information pour les clients ");
        for (Client c : gestionClient.listeClients()) {
            System.out.println(c);
        }

        Client client = new Client("Dupont", "Jean", "dupont@example.com", "123 rue Exemple", 0601020304, "Durant.dupont", "password123");
        gestionClient.ajouterClient(client);

        // Modifier un client
        client.setNomClient("Dupont Modifié");
        gestionClient.modifierClient(1, client);

        gestionClient.rechercherClient(3);


        gestionClient.supprimerClient(3);

        for (Client c : gestionClient.listeClients()) {
            System.out.println(c);
        }

        System.out.println("       ******************************************** ");

        System.out.println("Information pour les produits  ");

        Produit produit = new Produit("Laptop", "Un ordinateur portable performant", 1000, 50, "MACBOOK");
        gestionProduit.ajouterProduit(produit);

        for (Produit p : gestionProduit.listeProduits()) {
            System.out.println(p);
        }

        produit.setNomProduit("Laptop Gaming");
        gestionProduit.modifierProduit(1, produit);

        Produit produitRecherche = gestionProduit.rechercherProduit(1);
        if (produitRecherche != null) {
            System.out.println("Produit trouvé : " + produitRecherche);
        }

        //gestionProduit.supprimerProduit(1);

        System.out.println("          *********************************************** ");

        System.out.println("Information pour les commandes ");

        Commande commande1 = new Commande(1, LocalDateTime.now(), "en cours");
        gestionCommande.ajouterCommande(commande1);

        for (Commande c : gestionCommande.listeCommandes()) {
            System.out.println(c);
        }

        commande1.setEtatCommande("validée");
        gestionCommande.modifierCommande(1, commande1);

        Commande commandeRecherchee = gestionCommande.rechercherCommande(1);
        if (commandeRecherchee != null) {
            System.out.println("Commande trouvée : " + commandeRecherchee);
        }

        gestionCommande.supprimerCommande(6);

        for (Commande c : gestionCommande.listeCommandes()) {
            System.out.println(c);
        }

        System.out.println("Gestion de la facturation ");

        Facture facture1 = new Facture(3, LocalDateTime.now(), 150);
        gestionFacture.ajouterFacture(facture1);

        for (Facture f : gestionFacture.listeFactures()) {
            System.out.println(f);
        }

        facture1.setMontantFacture(200);
        gestionFacture.modifierFacture(1, facture1);

        Facture factureRecherchee = gestionFacture.rechercherFacture(1);
        if (factureRecherchee != null) {
            System.out.println("Facture trouvée : " + factureRecherchee);
        }

        gestionFacture.supprimerFacture(1);

        for (Facture f : gestionFacture.listeFactures()) {
            System.out.println(f);
        }

    }

}
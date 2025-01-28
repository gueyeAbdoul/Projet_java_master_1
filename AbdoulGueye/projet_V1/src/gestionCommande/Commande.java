package gestionCommande;

import java.time.LocalDateTime;

public class Commande {

    private int idCommande;
    private int idClient;
    private String etatCommande;
    private LocalDateTime dateCommande;

    public Commande(int idclient, LocalDateTime dateCommande, String etatCommande) {
        this.idClient = idclient;
        this.dateCommande = dateCommande;
        this.etatCommande = etatCommande;
    }

    public Commande(int id, int idClient, LocalDateTime dateCommande, String etatCommande) {
        this.idCommande = id;
        this.idClient = idClient;
        this.dateCommande = dateCommande;
        this.etatCommande = etatCommande;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getEtatCommande() {
        return etatCommande;
    }

    public void setEtatCommande(String etatCommande) {
        this.etatCommande = etatCommande;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String toString() {
        return "Commande #" + getIdCommande() +
                " du client #" + getIdClient() +
                " etat du commande : " + getEtatCommande() +
                " Date du commande : " + getDateCommande();
    }

}

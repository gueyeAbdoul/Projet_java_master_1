package gestionFacturation;

import java.time.LocalDateTime;

public class Facture {

    private int idFacture;
    private int idCommande;
    private float montantFacture;
    private LocalDateTime dateFacture;

    private static String DEVISE = "EURO";

    public Facture(int idCommande, LocalDateTime dateFacture, float montant) {
        this.idCommande = idCommande;
        this.dateFacture = dateFacture;
        this.montantFacture = montant;
    }

    public Facture(int idFacture, int idCommande, LocalDateTime dateFacture, float montant) {
        this.idFacture = idFacture;
        this.idCommande = idCommande;
        this.dateFacture = dateFacture;
        this.montantFacture = montant;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public float getMontantFacture() {
        return montantFacture;
    }

    public void setMontantFacture(float montantFacture) {
        this.montantFacture = montantFacture;
    }

    public LocalDateTime getDateFacture() {
        return dateFacture;
    }

    public String toString(){
        return " Facture #" + getIdFacture() +
                "de la commande #" + getIdCommande() +
                " de montant " + getMontantFacture() +
                " émis le " + getDateFacture();
    }

}

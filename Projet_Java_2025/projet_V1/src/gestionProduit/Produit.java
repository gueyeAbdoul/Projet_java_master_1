package gestionProduit;

public class Produit {

    private int idProduit;
    private String nomProduit;
    private String descriptionProduit;
    private float prixProduit;
    private int quantiteStock;
    private String categorie;

    private static String DEVISE = "EURO";

    public Produit(String nom, String description, float prix, int quantiteEnStock, String categorie) {
        this.nomProduit = nom;
        this.descriptionProduit = description;
        this.prixProduit = prix;
        this.quantiteStock = quantiteEnStock;
        this.categorie = categorie;
    }

    public Produit(int id, String nom, String description, float prix, int quantiteEnStock, String categorie) {
        this.idProduit = id;
        this.nomProduit = nom;
        this.descriptionProduit = description;
        this.prixProduit = prix;
        this.quantiteStock = quantiteEnStock;
        this.categorie = categorie;
    }

    public int getIdProduit() {
        return this.idProduit;
    }

    public String getNomProduit() {
        return this.nomProduit;
    }

    public String getDescriptionProduit() {
        return this.descriptionProduit;
    }

    public float getPrixProduit() {
        return this.prixProduit;
    }

    public int getQuantiteStock() {
        return this.quantiteStock;
    }

    public String getCategorie() {
        return this.categorie;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public void setDescriptionProduit(String descriptionProduit) {
        this.descriptionProduit = descriptionProduit;
    }

    public void setPrixProduit(float prixProduit) {
        this.prixProduit = prixProduit;
    }

    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String toString() {
        return "Nom produit : " + getNomProduit() + " #" + getIdProduit()
                + "\nDescription produit : " + getDescriptionProduit()
                + "\nPrix produit : " + getPrixProduit() + " " + DEVISE
                + "\nQuantite en stock : " + getQuantiteStock()
                + "\nCategorie : " + getCategorie()
        ;
    }
}

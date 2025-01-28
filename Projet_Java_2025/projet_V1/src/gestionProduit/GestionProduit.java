package gestionProduit;

import connexionJavaSql.ConnexionBasedeDonnee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestionProduit {

    // Ajouter un produit
    public void ajouterProduit(Produit produit) {
        String sql = "INSERT INTO produit (nomProduit, descriptionProduit, prixProduit, quantiteStock, categorie) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setString(1, produit.getNomProduit());
            statement.setString(2, produit.getDescriptionProduit());
            statement.setDouble(3, produit.getPrixProduit());
            statement.setInt(4, produit.getQuantiteStock());
            statement.setString(5, produit.getCategorie());
            statement.executeUpdate();
            System.out.println("Produit #" + produit.getIdProduit() + " ajouté avec succès : " + produit.getNomProduit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modifier un produit
    public void modifierProduit(int idProduit, Produit produit) {
        String sql = "UPDATE produit SET nomproduit = ?, descriptionproduit = ?, prixproduit = ?, quantiteStock = ?, categorie=? WHERE idproduit = ?";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setString(1, produit.getNomProduit());
            statement.setString(2, produit.getDescriptionProduit());
            statement.setDouble(3, produit.getPrixProduit());
            statement.setInt(4, produit.getQuantiteStock());
            statement.setString(5, produit.getCategorie());
            statement.setInt(6, idProduit);
            int execmodif = statement.executeUpdate();
            if (execmodif > 0) {
                System.out.println("Produit #" + produit.getIdProduit() + " modifié avec succès." + produit);
            } else {
                System.out.println("Aucun produit trouvé avec #" + idProduit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un produit
    public void supprimerProduit(int id) {
        String sql = "DELETE FROM produit WHERE idproduit = ?";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            int suppression = statement.executeUpdate();
            if (suppression > 0) {
                System.out.println("Produit #" + id + " supprimé avec succès.");
            } else {
                System.out.println("Aucun produit trouvé avec #" + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Rechercher un produit par ID
    public Produit rechercherProduit(int id) {
        String sql = "SELECT * FROM produit WHERE idProduit = ?";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Produit(
                            resultSet.getInt("idproduit"),
                            resultSet.getString("nomProduit"),
                            resultSet.getString("descriptionProduit"),
                            resultSet.getFloat("prixProduit"),
                            resultSet.getInt("quantiteStock"),
                            resultSet.getString("categorie")
                    );
                } else {
                    System.out.println("Aucun produit trouvé avec #" + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lister tous les produits
    public List<Produit> listeProduits() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit";
        try (Statement statement = ConnexionBasedeDonnee.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                produits.add(new Produit(
                        resultSet.getInt("idProduit"),
                        resultSet.getString("nomProduit"),
                        resultSet.getString("descriptionProduit"),
                        resultSet.getFloat("prixProduit"),
                        resultSet.getInt("quantiteStock"),
                        resultSet.getString("categorie")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }

    public static Object[][] obtenirProduits() {
        String query = "SELECT idproduit, nomproduit, descriptionproduit, prixproduit, quantitestock, categorie FROM produit";
        try (Connection conn = ConnexionBasedeDonnee.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            ArrayList<Object[]> produits = new ArrayList<>();
            while (rs.next()) {
                produits.add(new Object[]{
                        rs.getInt("idproduit"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDouble("prix"),
                        rs.getInt("quantiteStock"),
                        rs.getString("categorie")
                });
            }
            return produits.toArray(new Object[0][]);
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[0][];
        }
    }


}

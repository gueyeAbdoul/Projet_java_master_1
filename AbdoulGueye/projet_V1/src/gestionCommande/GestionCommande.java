package gestionCommande;

import connexionJavaSql.ConnexionBasedeDonnee;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestionCommande {

    // Ajouter une commande
    public void ajouterCommande(Commande commande) {
        String sql = "INSERT INTO commande (idclient, datecommande, etatcommande) VALUES (?, ?, ?)";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setInt(1, commande.getIdClient());
            statement.setTimestamp(2, Timestamp.valueOf(commande.getDateCommande()));
            statement.setString(3, commande.getEtatCommande());
            statement.executeUpdate();
            System.out.println("Commande ajoutée avec succès pour le client #" + commande.getIdClient());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modifier une commande
    public void modifierCommande(int idCommande, Commande commande) {
        String sql = "UPDATE commande SET idclient = ?, datecommande = ?, etatcommande = ? WHERE idcommande = ?";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setInt(1, commande.getIdClient());
            statement.setTimestamp(2, Timestamp.valueOf(commande.getDateCommande()));
            statement.setString(3, commande.getEtatCommande());
            statement.setInt(4, idCommande);
            int modif = statement.executeUpdate();
            if (modif > 0) {
                System.out.println("Commande #" + idCommande + " mise à jour avec succès.");
            } else {
                System.out.println("Aucune commande trouvée avec #" + idCommande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer une commande
    public void supprimerCommande(int id) {
        String sql = "DELETE FROM commande WHERE idcommande = ?";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            int suppression = statement.executeUpdate();
            if (suppression > 0) {
                System.out.println("Commande #" + id + " supprimée avec succès.");
            } else {
                System.out.println("Aucune commande trouvée avec #" + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Rechercher une commande par ID
    public Commande rechercherCommande(int id) {
        String sql = "SELECT * FROM commande WHERE idcommande = ?";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Commande(
                            resultSet.getInt("idcommande"),
                            resultSet.getInt("idClient"),
                            resultSet.getTimestamp("datecommande").toLocalDateTime(),
                            resultSet.getString("etatCommande")
                    );
                } else {
                    System.out.println("Aucune commande trouvée avec #" + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //lister toutes les commandes
    public List<Commande> listeCommandes() {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande";
        try (Statement statement = ConnexionBasedeDonnee.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                commandes.add(new Commande(
                        resultSet.getInt("idCommande"),
                        resultSet.getInt("idClient"),
                        resultSet.getTimestamp("dateCommande").toLocalDateTime(),
                        resultSet.getString("etatCommande")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

    public static Object[][] obtenirCommandesParClient(int clientId) {
        String query = "SELECT idcommande, datecommande, etatCommande, datecommande FROM commande WHERE idclient = ?";
        try (Connection conn = ConnexionBasedeDonnee.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                ArrayList<Object[]> commandes = new ArrayList<>();
                while (rs.next()) {
                    commandes.add(new Object[]{
                            rs.getInt("idcommande"),
                            rs.getDate("datecommande"),
                            rs.getString("etatCommande"),
                            rs.getTimestamp("datecommande").toLocalDateTime(),
                    });
                }
                return commandes.toArray(new Object[0][]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[0][];
        }
    }

}



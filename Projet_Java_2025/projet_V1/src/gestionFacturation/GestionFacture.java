package gestionFacturation;

import connexionJavaSql.ConnexionBasedeDonnee;

import javax.sound.midi.MidiChannel;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestionFacture {

    // Ajouter une facture
    public void ajouterFacture(Facture facture) {

        if (!verifierCommandeExiste(facture.getIdCommande())) {
            System.err.println("Erreur : La commande #" + facture.getIdCommande() + " n'existe pas. Facture non ajoutée.");
            return;
        }

        String sql = "INSERT INTO facture (idcommande, dateFacture, montantFacture) VALUES (?, ?, ?)";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setInt(1, facture.getIdCommande());
            statement.setTimestamp(2, Timestamp.valueOf(facture.getDateFacture()));
            statement.setDouble(3, facture.getMontantFacture());
            statement.executeUpdate();
            System.out.println("Facture ajoutée avec succès pour la commande #" + facture.getIdCommande());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modifier une facture
    public void modifierFacture(int idFacture, Facture facture) {
        String sql = "UPDATE facture SET idcommande = ?, dateFacture = ?, montantFacture = ? WHERE idfacture = ?";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setInt(1, facture.getIdCommande());
            statement.setTimestamp(2, Timestamp.valueOf(facture.getDateFacture()));
            statement.setDouble(3, facture.getMontantFacture());
            statement.setInt(4, idFacture);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Facture #" + idFacture + " modifiée avec succès.");
            } else {
                System.out.println("Aucune facture trouvée avec #" + idFacture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer une facture
    public void supprimerFacture(int idFacture) {
        String sql = "DELETE FROM facture WHERE idfacture = ?";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setInt(1, idFacture);
            int suppression = statement.executeUpdate();
            if (suppression > 0) {
                System.out.println("Facture #" + idFacture + " supprimée avec succès.");
            } else {
                System.out.println("Aucune facture trouvée avec #" + idFacture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Rechercher une facture par ID
    public Facture rechercherFacture(int idFacture) {
        String sql = "SELECT * FROM facture WHERE idFacture = ?";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setInt(1, idFacture);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Facture(
                            resultSet.getInt("idFacrture"),
                            resultSet.getInt("idCommande"),
                            resultSet.getTimestamp("dateFacture").toLocalDateTime(),
                            resultSet.getFloat("montantFacture")
                    );
                } else {
                    System.out.println("Aucune facture trouvée avec #: " + idFacture);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //lister toutes les factures
    public List<Facture> listeFactures() {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT * FROM facture";
        try (Statement statement = ConnexionBasedeDonnee.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                factures.add(new Facture(
                        resultSet.getInt("idFacture"),
                        resultSet.getInt("idCommande"),
                        resultSet.getTimestamp("dateFacture").toLocalDateTime(),
                        resultSet.getFloat("montantFacture")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return factures;
    }

    // Méthode pour vérifier si une commande existe
    private boolean verifierCommandeExiste(int idCommande) {
        String sql = "SELECT idCommande FROM commande WHERE idcommande = ?";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setInt(1, idCommande);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Object[][] obtenirFacturesParClient(int clientId) {
        String query = "SELECT idfacture, datefacture, montantfacture FROM facture WHERE idcommande IN " +
                "(SELECT idcommande FROM commande WHERE idclient = ?)";
        try (Connection conn = ConnexionBasedeDonnee.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                ArrayList<Object[]> factures = new ArrayList<>();
                while (rs.next()) {
                    factures.add(new Object[]{
                            rs.getInt("idfacture"),
                            rs.getDate("datefacture"),
                            rs.getDouble("montantfacture"),
                    });
                }
                return factures.toArray(new Object[0][]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[0][];
        }
    }


}

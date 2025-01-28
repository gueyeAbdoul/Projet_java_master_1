package gestionClient;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import connexionJavaSql.ConnexionBasedeDonnee;
import java.util.List;


public class GestionClient {

    private List<Client> clients;

    public GestionClient() {
        this.clients = new ArrayList<>();
    }

    /*
        cette fonction verifie si l'email est deja utilisé par un autre client
     */
    private boolean emailExiste(String email) {
        String sql = "SELECT 1 FROM client WHERE email = ?";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Ajouter un client
    public void ajouterClient(Client client) {

        if (emailExiste(client.getEmailClient())) {
            System.out.println("Un client avec cet email existe déjà : " + client.getEmailClient());
            return;
        }
        String sql = "INSERT INTO client (nomClient, prenomClient, email, adresse, telephone, login, motdepasse) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setString(1, client.getNomClient());
            statement.setString(2, client.getPrenomClient());
            statement.setString(3, client.getEmailClient());
            statement.setString(4, client.getAdresseClient());
            statement.setInt(5, client.getTelephoneClient());
            statement.setString(6, client.getLoginClient());
            statement.setString(7, client.getMotdepasseClient());
            statement.executeUpdate();
            System.out.println("Client #" + client.getIdClient() + " ajouté avec succès : " + client.getNomClient());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modifier un client
    public void modifierClient(int id, Client client) {

        String sql = "UPDATE client SET nomclient = ?, prenomclient = ?, email = ?, adresse = ?, telephone = ?, login = ?, motdepasse = ? WHERE idclient = ?";

        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setString(1, client.getNomClient());
            statement.setString(2, client.getPrenomClient());
            statement.setString(3, client.getEmailClient());
            statement.setString(4, client.getAdresseClient());
            statement.setInt(5, client.getTelephoneClient());
            statement.setString(6, client.getLoginClient());
            statement.setString(7, client.getMotdepasseClient());
            statement.setInt(8, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Client #" + id + "  modifié avec succès.");
            } else {
                System.out.println("Aucun client trouvé avec #" + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un client
    public void supprimerClient(int id) {

        String sql = "DELETE FROM client WHERE idclient = ?";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Client #" + id + " supprimé avec succès.");
            } else {
                System.out.println("Aucun client trouvé avec #" + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Rechercher un client par ID
    public Client rechercherClient(int id) {
        String sql = "SELECT * FROM client WHERE idclient = ?";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Client(
                            resultSet.getInt("idClient"),
                            resultSet.getString("nomClient"),
                            resultSet.getString("prenomClient"),
                            resultSet.getString("email"),
                            resultSet.getString("adresse"),
                            resultSet.getInt("telephone"),
                            resultSet.getString("login"),
                            resultSet.getString("mot_de_passe")
                    );
                } else {
                    System.out.println("Aucun client trouvé avec #" + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //liste de tous les clients
    public List<Client> listeClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";
        try (Statement statement = ConnexionBasedeDonnee.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                clients.add(new Client(
                        resultSet.getInt("idClient"),
                        resultSet.getString("nomClient"),
                        resultSet.getString("prenomClient"),
                        resultSet.getString("email"),
                        resultSet.getString("adresse"),
                        resultSet.getInt("telephone"),
                        resultSet.getString("login"),
                        resultSet.getString("motdepasse")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

}

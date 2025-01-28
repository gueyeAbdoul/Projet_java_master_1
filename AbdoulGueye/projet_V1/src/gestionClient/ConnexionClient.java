package gestionClient;

import connexionJavaSql.ConnexionBasedeDonnee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ConnexionClient {

    private final Scanner scanner = new Scanner(System.in);

    public void seConnecter() {
        System.out.println("Veuillez entrer votre login : ");
        String login = scanner.nextLine();

        System.out.println("Veuillez entrer votre mot de passe : ");
        String motDePasse = scanner.nextLine();

        String sql = "SELECT * FROM client WHERE login = ? AND motdepasse = ?";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setString(2, motDePasse);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Connexion réussie !");
                    System.out.println("Bienvenue, " + resultSet.getString("prenomClient") + " " + resultSet.getString("nomClient"));
                } else {
                    System.out.println("Échec de la connexion : Login ou mot de passe incorrect.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Inscription du client
    public void inscriptionClient() {
        System.out.println("Bienvenue sur la page d'inscription.");
        System.out.print("Nom : ");
        String nom = scanner.nextLine();

        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();

        System.out.print("Adresse : ");
        String adresse = scanner.nextLine();

        System.out.print("Email : ");
        String email = scanner.nextLine();

        System.out.print("Numéro de téléphone : ");
        String telephone = scanner.nextLine();

        System.out.print("Login : ");
        String login = scanner.nextLine();

        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();

        // Vérifier si l'email ou le login existe déjà
        if (emailOuLoginExistant(email, login)) {
            System.out.println("Erreur : L'email ou le login est déjà utilisé. Veuillez réessayer.");
            return;
        }

        // Ajouter le client dans la base de données
        String sql = "INSERT INTO client (nomclient, prenomclient, adresse, email, telephone, login, motdepasse) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setString(3, adresse);
            statement.setString(4, email);
            statement.setString(5, telephone);
            statement.setString(6, login);
            statement.setString(7, motDePasse);
            statement.executeUpdate();

            System.out.println("Inscription réussie ! Vous pouvez maintenant vous connecter.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Vérifier si un email ou un login existe déjà
    private boolean emailOuLoginExistant(String email, String login) {
        String sql = "SELECT * FROM client WHERE email = ? OR login = ?";
        try (PreparedStatement statement = ConnexionBasedeDonnee.getConnection().prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, login);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

}

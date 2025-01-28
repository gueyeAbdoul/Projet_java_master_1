package interfaceGraphique.gestionclient;

import connexionJavaSql.ConnexionBasedeDonnee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class InscriptionPanel extends JPanel {

    private JTextField nomCl, prenomCl, adresseCl, emailCl, telephoneCl, identifiant;
    private JPasswordField passwordField;
    private Consumer<Integer> onSuccessfulInscription; // Callback pour redirection après inscription

    public InscriptionPanel(Consumer<Integer> onSuccessfulInscription) {
        this.onSuccessfulInscription = onSuccessfulInscription;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Marges entre les composants
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1; // Prendre tou
        gbc.gridwidth = 1;

        // Champs pour l'inscription
        JLabel nomLabel = new JLabel("Nom du client");
        nomLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nomCl = new JTextField();

        JLabel prenomLabel = new JLabel("Prénom du client");
        prenomLabel.setHorizontalAlignment(SwingConstants.CENTER);
        prenomCl = new JTextField();

        JLabel adresseLabel = new JLabel("Adresse client");
        adresseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        adresseCl = new JTextField();

        JLabel emailLabel = new JLabel("Email client");
        emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emailCl = new JTextField();

        JLabel telephoneLabel = new JLabel("Téléphone client");
        telephoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
        telephoneCl = new JTextField();

        JLabel loginLabel = new JLabel("Login client");
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        identifiant = new JTextField();

        JLabel passwordLabel = new JLabel("Mot de passe client");
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField = new JPasswordField();

        JButton registerButton = new JButton("S'inscrire");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sInscrire();
            }
        });

        int row = 0;

        gbc.gridx = 0; // Colonne 0
        gbc.gridy = row++;
        add(nomLabel, gbc);
        gbc.gridy = row++;
        add(nomCl, gbc);

        gbc.gridy = row++;
        add(prenomLabel, gbc);
        gbc.gridy = row++;
        add(prenomCl, gbc);

        gbc.gridy = row++;
        add(adresseLabel, gbc);
        gbc.gridy = row++;
        add(adresseCl, gbc);

        gbc.gridy = row++;
        add(emailLabel, gbc);
        gbc.gridy = row++;
        add(emailCl, gbc);

        gbc.gridy = row++;
        add(telephoneLabel, gbc);
        gbc.gridy = row++;
        add(telephoneCl, gbc);

        gbc.gridy = row++;
        add(loginLabel, gbc);
        gbc.gridy = row++;
        add(identifiant, gbc);

        gbc.gridy = row++;
        add(passwordLabel, gbc);
        gbc.gridy = row++;
        add(passwordField, gbc);

        gbc.gridy = row++;
        gbc.gridwidth = 2; // Bouton occupe toute la largeur
        gbc.anchor = GridBagConstraints.CENTER; // Centrer le bouton
        add(registerButton, gbc);
    }

    private void sInscrire() {
        String nom = nomCl.getText();
        String prenom = prenomCl.getText();
        String adresse = adresseCl.getText();
        String email = emailCl.getText();
        String telephone = telephoneCl.getText();
        String login = identifiant.getText();
        String password = new String(passwordField.getPassword());

        if (nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty() || email.isEmpty() || telephone.isEmpty() || login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sqlInsert = "INSERT INTO client (nomClient, prenomClient, adresse, email, telephone, login, motdepasse) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlSelect = "SELECT idClient FROM client WHERE login = ?";

        try (Connection conn = ConnexionBasedeDonnee.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(sqlInsert);
             PreparedStatement selectStmt = conn.prepareStatement(sqlSelect)) {

            // Insertion des données
            insertStmt.setString(1, nom);
            insertStmt.setString(2, prenom);
            insertStmt.setString(3, adresse);
            insertStmt.setString(4, email);
            insertStmt.setString(5, telephone);
            insertStmt.setString(6, login);
            insertStmt.setString(7, password);
            insertStmt.executeUpdate();

            // Récupération de l'ID du client
            selectStmt.setString(1, login);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                int clientId = rs.getInt("idClient");
                JOptionPane.showMessageDialog(this, "Inscription réussie ! Vous êtes maintenant connecté.");
                if (onSuccessfulInscription != null) {
                    onSuccessfulInscription.accept(clientId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'inscription. Veuillez réessayer.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}

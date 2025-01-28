package interfaceGraphique.gestionclient;

import connexionJavaSql.ConnexionBasedeDonnee;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConnexionPanel extends JPanel {
    private JTextField nomClient;
    private JPasswordField motDePasse;
    private JButton loginButton;

    // Interface pour transmettre l'ID du client connecté
    private ClientConnexionCallback callback;

    public ConnexionPanel(ClientConnexionCallback callback) {
        this.callback = callback;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Connexion Client");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        JLabel usernameLabel = new JLabel("Nom d'utilisateur :");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(usernameLabel, gbc);

        nomClient = new JTextField(15);
        gbc.gridx = 1;
        add(nomClient, gbc);

        JLabel passwordLabel = new JLabel("Mot de passe :");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        motDePasse = new JPasswordField(15);
        gbc.gridx = 1;
        add(motDePasse, gbc);

        loginButton = new JButton("Se connecter");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginButton.addActionListener(e -> seConnecter());
        add(loginButton, gbc);
    }

    private void seConnecter() {
        String username = nomClient.getText();
        String password = new String(motDePasse.getPassword());

        try (Connection conn = ConnexionBasedeDonnee.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT idClient FROM client WHERE login = ? AND motDePasse = ?"
             )) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int clientId = rs.getInt("idClient"); // Récupérer l'ID du client
                callback.onClientConnected(clientId); // Appeler le callback avec l'ID
            } else {
                JOptionPane.showMessageDialog(this, "Nom d'utilisateur ou mot de passe incorrect", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Interface fonctionnelle pour transmettre l'ID du client
    public interface ClientConnexionCallback {
        void onClientConnected(int clientId);
    }
}
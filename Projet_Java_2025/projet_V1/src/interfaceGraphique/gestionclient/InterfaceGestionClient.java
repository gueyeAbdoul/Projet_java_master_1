package interfaceGraphique.gestionclient;

import connexionJavaSql.ConnexionBasedeDonnee;
import interfaceGraphique.BackgroundPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InterfaceGestionClient extends JPanel {

    private int clientId;
    private JTextField nomCl, prenomCl, adresseCl, emailCl, telephoneCl, loginCl;
    private JButton editButton, validateButton, cancelButton;

    public InterfaceGestionClient(int clientId) {
        this.clientId = clientId;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        BackgroundPanel mainPanel = new BackgroundPanel("/interfaceGraphique/images1.jpg");
        mainPanel.setLayout(new GridBagLayout());


        JLabel titleLabel = new JLabel("Informations clients");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Champs d'informations
        nomCl = createFieldWithLabel("Nom", gbc, 1);
        prenomCl = createFieldWithLabel("Prénom", gbc, 3);
        adresseCl = createFieldWithLabel("Adresse", gbc, 5);
        emailCl = createFieldWithLabel("Email", gbc, 7);
        telephoneCl = createFieldWithLabel("Téléphone", gbc, 9);
        loginCl = createFieldWithLabel("Login", gbc, 11);

        modifEditable(false);

        // Boutons
        editButton = new JButton("Modifier mes informations");
        editButton.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 13;
        gbc.gridwidth = 1;
        add(editButton, gbc);

        validateButton = new JButton("Valider la modification");
        cancelButton = new JButton("Annuler la modification");
        gbc.gridy = 13;
        gbc.gridx = 0;
        add(validateButton, gbc);
        gbc.gridx = 1;
        add(cancelButton, gbc);

        validateButton.setVisible(false);
        cancelButton.setVisible(false);

        chargerInformationsClient();

        // Listeners
        editButton.addActionListener(e -> activerEdition());
        validateButton.addActionListener(e -> validerModification());
        cancelButton.addActionListener(e -> rechargerPanel());
    }

    private JTextField createFieldWithLabel(String label, GridBagConstraints gbc, int row) {
        JLabel jLabel = new JLabel(label + ":");
        jLabel.setFont(new Font("Arial", Font.BOLD, 16));
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.CENTER;
        add(jLabel, gbc);

        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.gridy = row + 1;
        add(field, gbc);

        return field;
    }

    private void modifEditable(boolean editable) {
        nomCl.setEditable(editable);
        prenomCl.setEditable(editable);
        adresseCl.setEditable(editable);
        emailCl.setEditable(editable);
        telephoneCl.setEditable(editable);
        loginCl.setEditable(editable);
    }

    private void chargerInformationsClient() {
        String sql = "SELECT nomClient, prenomClient, adresse, email, telephone, login FROM client WHERE idClient = ?";
        try (Connection conn = ConnexionBasedeDonnee.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, clientId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                nomCl.setText(rs.getString("nomClient"));
                prenomCl.setText(rs.getString("prenomClient"));
                adresseCl.setText(rs.getString("adresse"));
                emailCl.setText(rs.getString("email"));
                telephoneCl.setText(rs.getString("telephone"));
                loginCl.setText(rs.getString("login"));
            } else {
                JOptionPane.showMessageDialog(this, "Aucune information trouvée pour ce client.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des informations client.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void activerEdition() {
        modifEditable(true);
        editButton.setVisible(false);
        validateButton.setVisible(true);
        cancelButton.setVisible(true);
    }

    private void validerModification() {
        String sql = "UPDATE client SET nomClient = ?, prenomClient = ?, adresse = ?, email = ?, telephone = ?, login = ? WHERE idClient = ?";
        try (Connection conn = ConnexionBasedeDonnee.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, nomCl.getText());
            statement.setString(2, prenomCl.getText());
            statement.setString(3, adresseCl.getText());
            statement.setString(4, emailCl.getText());
            statement.setString(5, telephoneCl.getText());
            statement.setString(6, loginCl.getText());
            statement.setInt(7, clientId);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Informations mises à jour avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            rechargerPanel();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour des informations client.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rechargerPanel() {
        chargerInformationsClient();
        modifEditable(false);
        validateButton.setVisible(false);
        cancelButton.setVisible(false);
        editButton.setVisible(true);
    }
}

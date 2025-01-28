package interfaceGraphique.gestionproduit;

import connexionJavaSql.ConnexionBasedeDonnee;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class InterfaceListeFacture extends JPanel {
    private final int clientId;

    public InterfaceListeFacture(int clientId) {
        this.clientId = clientId;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Disposition verticale

        JLabel titleLabel = new JLabel("Mes Factures");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(20)); // Espacement

        try (Connection conn = ConnexionBasedeDonnee.getConnection()) {
            String query = """
                SELECT idFacture, datefacture
                FROM Facture 
                WHERE idCommande IN (
                    SELECT idCommande 
                    FROM Commande 
                    WHERE idClient = ?
                )
            """;

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, clientId);
                try (ResultSet rs = stmt.executeQuery()) {
                    boolean hasFactures = false;

                    while (rs.next()) {
                        hasFactures = true;
                        int idFacture = rs.getInt("idFacture");
                        Date datefacture = rs.getDate("datefacture");

                        JButton factureButton = new JButton("Facture N°" + idFacture + " - Date " + datefacture);
                        factureButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
                        factureButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                        factureButton.addActionListener(e -> {

                            new InterfaceFacture(idFacture);
                        });
                        add(factureButton);
                        add(Box.createVerticalStrut(10)); // Espacement entre les boutons
                    }

                    if (!hasFactures) {
                        JLabel noFacturesLabel = new JLabel("Aucune facture trouvée.");
                        noFacturesLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
                        noFacturesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        add(noFacturesLabel);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Erreur lors du chargement des factures.");
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(errorLabel);
        }
    }
}

package interfaceGraphique.gestionproduit;

import connexionJavaSql.ConnexionBasedeDonnee;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class InterfaceListeCommande extends JPanel {
    private final int clientId;

    public InterfaceListeCommande(int clientId) {
        this.clientId = clientId;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Disposition verticale

        JLabel titleLabel = new JLabel("Mes Commandes");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(20)); // Espacement

        try (Connection conn = ConnexionBasedeDonnee.getConnection()) {
            String query = """
                SELECT idCommande, dateCommande 
                FROM Commande 
                WHERE idClient = ?
            """;

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, clientId);
                try (ResultSet rs = stmt.executeQuery()) {
                    boolean hasCommandes = false;

                    while (rs.next()) {
                        hasCommandes = true;
                        int idCommande = rs.getInt("idCommande");
                        String dateCommande = rs.getDate("dateCommande").toString();

                        JButton commandeButton = new JButton("Commande N°" + idCommande + " - Date: " + dateCommande);
                        commandeButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
                        commandeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                        commandeButton.addActionListener(e -> {
                            // Créer une nouvelle fenêtre pour afficher les détails de la commande
                            JFrame commandeFrame = new JFrame("Détails de la Commande N°" + idCommande);
                            commandeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            commandeFrame.setSize(800, 600); // Taille de la fenêtre
                            commandeFrame.add(afficherCommande(idCommande)); // Ajout du panneau d'affichage
                            commandeFrame.setVisible(true);
                        });
                        add(commandeButton);
                        add(Box.createVerticalStrut(10)); // Espacement entre les boutons
                    }

                    if (!hasCommandes) {
                        JLabel noCommandesLabel = new JLabel("Aucune commande trouvée.");
                        noCommandesLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
                        noCommandesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        add(noCommandesLabel);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Erreur lors du chargement des commandes.");
            errorLabel.setForeground(Color.RED);
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(errorLabel);
        }
    }

    private JPanel afficherCommande(int commandeId) {
        JPanel commandePanel = new JPanel();
        commandePanel.setLayout(new BoxLayout(commandePanel, BoxLayout.Y_AXIS));
        commandePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            String produitsQuery = """
            SELECT produit.imageProduit, produit.nomProduit, produit.categorie, 
                   produit.descriptionproduit, produit.prixproduit 
            FROM commande
            INNER JOIN panier ON commande.idPanier = panier.idPanier
            INNER JOIN produit_panier ON panier.idPanier = produit_panier.idPanier
            INNER JOIN produit ON produit_panier.idProduit = produit.idProduit
            WHERE commande.idCommande = ?
            """;

            try (PreparedStatement produitsStmt = ConnexionBasedeDonnee.getConnection().prepareStatement(produitsQuery)) {
                produitsStmt.setInt(1, commandeId);
                ResultSet rsProduits = produitsStmt.executeQuery();

                while (rsProduits.next()) {
                    String imageProduit = rsProduits.getString("imageProduit");
                    String nomProduit = rsProduits.getString("nomProduit");
                    String nomCategorie = rsProduits.getString("categorie");
                    String description = rsProduits.getString("descriptionproduit");
                    double prix = rsProduits.getDouble("prixproduit");

                    // Création du panneau pour chaque produit
                    JPanel produitPanel = new JPanel(new BorderLayout());
                    produitPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                    produitPanel.setPreferredSize(new Dimension(800, 120));

                    // Ajout de l'image du produit
                    JLabel produitImage = new JLabel();
                    ImageIcon icon = new ImageIcon(imageProduit);
                    Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    produitImage.setIcon(new ImageIcon(img));
                    produitImage.setHorizontalAlignment(SwingConstants.CENTER);
                    produitPanel.add(produitImage, BorderLayout.WEST);

                    // Ajout des détails du produit à droite de l'image
                    JPanel detailsPanel = new JPanel();
                    detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                    detailsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

                    detailsPanel.add(new JLabel("Nom: " + nomProduit));
                    detailsPanel.add(new JLabel("Catégorie: " + nomCategorie));
                    detailsPanel.add(new JLabel("Description: " + description));
                    detailsPanel.add(new JLabel("Prix: " + prix + " €"));

                    produitPanel.add(detailsPanel, BorderLayout.CENTER);
                    commandePanel.add(produitPanel);

                    commandePanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }

            String commandeInfoQuery = """
            SELECT etatCommande, montantCommande, dateCommande
            FROM commande
            WHERE idCommande = ?
            """;

            try (PreparedStatement infoStmt = ConnexionBasedeDonnee.getConnection().prepareStatement(commandeInfoQuery)) {
                infoStmt.setInt(1, commandeId);
                ResultSet rsInfo = infoStmt.executeQuery();

                if (rsInfo.next()) {
                    String etatCommande = rsInfo.getString("etatCommande");
                    double montantCommande = rsInfo.getDouble("montantCommande");
                    Date dateCommande = rsInfo.getDate("dateCommande");

                    JPanel infoPanel = new JPanel(new GridLayout(1, 3));
                    infoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                    infoPanel.setPreferredSize(new Dimension(800, 40));

                    infoPanel.add(new JLabel("État : " + etatCommande));
                    infoPanel.add(new JLabel("Montant : " + montantCommande + " €"));
                    infoPanel.add(new JLabel("Date : " + dateCommande));

                    commandePanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    commandePanel.add(infoPanel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement de la commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        return commandePanel;  // Retourne le panneau rempli avec les détails de la commande
    }
}
